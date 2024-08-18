package com.lyk.util;

import cn.easyes.core.conditions.select.LambdaEsQueryWrapper;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.lyk.dto.ConditionOperEnum;
import com.lyk.dto.FieldOperEnum;
import com.lyk.dto.QueryFieldCondition;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Consumer;

import static com.lyk.dto.ConditionOperEnum.AND;

@NoArgsConstructor
public class ElasticsearchUtil {

    /**
     * 查询条件是否是叶子节点
     * @param condition
     * @return
     */
    private static boolean isLeaf(QueryFieldCondition condition) {
        return condition.getConditions().size() > 0 ? false : true;
    }

    /**
     * 处理嵌套查询
     * @param conditions
     * @param esQueryWrapper
     */
    private static void consumeNestCondition(List<QueryFieldCondition> conditions, LambdaEsQueryWrapper<?> esQueryWrapper) {
        for (int i = 0; i < conditions.size();  i++) {
            QueryFieldCondition condition = conditions.get(i);
            boolean isLeaf = isLeaf(condition);
            ConditionOperEnum condOper = condition.getCondOper();
            if (isLeaf) {
                // 如果是叶子节点
                consumeLeaf(condition, esQueryWrapper);
            } else {
                switch (condOper){
                    case AND:
                        esQueryWrapper.and(consumer->consumeNestCondition(condition.getConditions(),consumer));
                        break;
                    case OR:
                        esQueryWrapper.or(consumer->consumeNestCondition(condition.getConditions(),consumer));
                        break;
                    default:
                }
            }
        }
    }

    // 叶子节点某个子项的条件
    private static void consumeLeaf(QueryFieldCondition condition, LambdaEsQueryWrapper<?> esQueryWrapper) {
        FieldOperEnum operatorEnum = condition.getOper();
        String column = condition.getName();
        List<Object> values = condition.getValues();
        switch (operatorEnum) {
            case EQ:
                consumeCondition(condition, val -> esQueryWrapper.eq(column, val));
                break;
            case GE:
                consumeCondition(condition, val -> esQueryWrapper.ge(column, val));
                break;
            case GT:
                consumeCondition(condition, val -> esQueryWrapper.gt(column, val));
                break;
            case LE:
                consumeCondition(condition, val -> esQueryWrapper.le(column, val));
                break;
            case LT:
                consumeCondition(condition, val -> esQueryWrapper.lt(column, val));
                break;
            case OR_LIKE:
                esQueryWrapper.and(lambdaEsQueryWrapper -> condition.getValues().forEach(val -> lambdaEsQueryWrapper.or().like(column, val)));
                break;
            case LIKE:
                consumeCondition(condition, val -> esQueryWrapper.like(column, val));
                break;
            case LIKE_LEFT:
                consumeCondition(condition, val -> esQueryWrapper.likeLeft(column, val));
                break;
            case LIKE_RIGHT:
                consumeCondition(condition, val -> esQueryWrapper.likeRight(column, val));
                break;
//            case NOT_LIKE:
//                consumeCondition(condition, val -> esQueryWrapper.notLike(column, val));
//                break;
            case MATCH:
                consumeCondition(condition, val -> esQueryWrapper.match(column, val));
                break;
            case BETWEEN:
                consumeBetweenCondition(esQueryWrapper, column, values);
                break;
//            case NOT_BETWEEN:
//                esQueryWrapper.notBetween(column, values.get(0), values.get(1));
//                break;
            case IN:
                esQueryWrapper.in(column, values);
                break;
//            case NOT_IN:
//                esQueryWrapper.notIn(column, values);
//                break;
            case CONTAIN_ALL:
                values.forEach(o -> esQueryWrapper.in(column, o));
                break;
            default:
        }
    }

    private static void consumeCondition(QueryFieldCondition condition, Consumer<Object> consumer) {
        condition.getValues().forEach(consumer);
    }
    private static void consumeBetweenCondition(LambdaEsQueryWrapper<?> esQueryWrapper, String column, List<Object> values) {
        esQueryWrapper.and(i -> {
            i.or(true);
            for (int i1 = 0; i1 < values.size(); i1++) {
                i.between(column, values.get(i1), values.get(++i1));
                if (i1 == values.size() - 1) {
                    break;
                }
                i.or();
            }
        });
    }

    public static void packConditionNew(LambdaEsQueryWrapper<?> esQueryWrapper, List<QueryFieldCondition> conditions) {
        if (CollectionUtil.isNotEmpty(conditions)) {
            consumeNestCondition(conditions,esQueryWrapper);
        }
    }

}
