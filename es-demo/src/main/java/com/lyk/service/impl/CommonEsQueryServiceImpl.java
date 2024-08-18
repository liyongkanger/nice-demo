package com.lyk.service.impl;

import cn.easyes.common.constants.BaseEsConstants;
import cn.easyes.core.biz.EsPageInfo;
import cn.easyes.core.biz.OrderByParam;
import cn.easyes.core.conditions.select.LambdaEsQueryWrapper;
import cn.easyes.core.core.BaseEsMapperImpl;
import com.lyk.dto.EsCommonQueryInfo;
import com.lyk.dto.EsCommonQueryResult;
import com.lyk.service.CommonEsQueryService;
import com.lyk.util.ElasticsearchUtil;
import com.lyk.util.Sort;
import lombok.AllArgsConstructor;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CommonEsQueryServiceImpl implements CommonEsQueryService {

    private RestHighLevelClient highLevelClient;


    /**
     * 通用查询不指定类型
     * @param queryInfo
     * @return
     */
    @Override
    public EsCommonQueryResult<String> commonQuery(EsCommonQueryInfo queryInfo) {
        return commonQuery(queryInfo, String.class);
    }

    @Override
    public <T> EsCommonQueryResult<T> commonQuery(EsCommonQueryInfo queryInfo, Class<T> clazz) {
        BaseEsMapperImpl<T> baseEsMapper = new BaseEsMapperImpl<>();
        baseEsMapper.setClient(highLevelClient);
        baseEsMapper.setEntityClass(clazz);
        LambdaEsQueryWrapper<T> queryWrapper =  new LambdaEsQueryWrapper<>();
        // 设置查询结果实体类
        queryWrapper.setEntityClass(clazz);
        // 设置索引名称
        queryWrapper.index(queryInfo.getIndexName());
        // 查询显示列
        List<String> columns = queryInfo.getColumns();
        if (columns != null && !columns.isEmpty()) {
            queryWrapper.select(columns.toArray(new String[0]));
        }

        // 查询条件处理  queryFieldIds查询条件集合
        ElasticsearchUtil.packConditionNew(queryWrapper,queryInfo.getQueryField());

        // 排序处理
        Sort sort = queryInfo.getSort();
        if (sort != null && sort.getOrders() != null) {
            List<OrderByParam> orderByParamList = sort.getOrders().stream().map(order -> {
                OrderByParam orderByParam = new OrderByParam();
                orderByParam.setOrder(order.getProperty());
                orderByParam.setOrder(order.getDirection().toString());
                return orderByParam;
            }).collect(Collectors.toList());
            queryWrapper.orderBy(true,orderByParamList);
        }

        // 分页处理
        int pageNum = (int)queryInfo.getPageNum();
        int pageSize = (int)queryInfo.getPageSize();
        pageNum = pageNum > BaseEsConstants.ZERO ? pageNum : BaseEsConstants.PAGE_NUM;
        pageSize = pageSize > BaseEsConstants.ZERO ? pageSize : BaseEsConstants.PAGE_SIZE;
        // 公式设置分页
        queryWrapper.from((pageNum - 1) * pageSize);
        queryWrapper.size(pageSize);

        EsPageInfo<T> page = baseEsMapper.pageQuery(queryWrapper, pageNum, pageSize);


        return packQueryResult(page);
    }

    private <T> EsCommonQueryResult<T> packQueryResult(EsPageInfo<T> results) {
        EsCommonQueryResult<T> esCommonQueryResult = new EsCommonQueryResult<>();
        esCommonQueryResult.setDatas(results.getList());
        esCommonQueryResult.setTotalCount(results.getTotal());
        esCommonQueryResult.setPageNum(results.getPageNum());
        esCommonQueryResult.setPageSize(results.getPageSize());
        return esCommonQueryResult;
    }
}
