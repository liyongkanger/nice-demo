package com.lyk.test.aqs.countdownlatch.work;

import com.lyk.test.aqs.countdownlatch.work.dto.GenColumnDto;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GenSql {

    public static final String VAR = "var";
    public static final String INT = "int";

    public static void main(String[] args) {

        System.out.println("---------------生成添加字段sql开始-----------------");
        Map<String, List<GenColumnDto>> tableNames = new HashMap<>();
        // 设置参数
        handSetParams(tableNames);
        // 生成
        List<String> dataBaseTypes = new ArrayList<>(Arrays.asList("oracle", "mysql", "postgrep"));
        // 生成三种数据库sql
        dataBaseTypes.forEach(dataBaseType -> {
            genColumnTotal(tableNames, dataBaseType);
        });
    }

    /**
     *  设置变量
     *  eg:
     *  tableNames.put("tinf_pro_picproduct_ext", new ArrayList<GenColumnDto>() {{
     *  //dealMode  新增: 1   修改: 2   var/int
     * //            add(new GenColumnDto("column_length",
     * //                    "column_type",
     * //                    "column_comment",
     * //                    "column_key","deal_mode"));
     *         }});
     * @param tableNames
     */
    private static void handSetParams(Map<String, List<GenColumnDto>> tableNames) {
        // key: table_name   value: column_name

        tableNames.put("tinf_pro_picproduct_ext", new ArrayList<GenColumnDto>() {{
            add(new GenColumnDto("32",
                    "var",
                    "客群专享",
                    "cust_group_exclusive", "2"));

        }});
    }

    private static void genColumnTotal(Map<String, List<GenColumnDto>> tableNames, String dataBaseType) {
        for (Map.Entry<String, List<GenColumnDto>> entry : tableNames.entrySet()) {
            String tableName = entry.getKey();
            List<GenColumnDto> columnDtos = entry.getValue();
            // 遍历每个字段
            StringBuilder sb = new StringBuilder();
            for (GenColumnDto columnDto : columnDtos) {
                // 表名
                sb.append("\n");

                if (columnDto.getDealMode().equals("1")) {
                    // 新增
                    addColumnStr(dataBaseType, tableName, sb, columnDto);
                } else if (columnDto.getDealMode().equals("2")) {
                    // 修改
                    modifyColumnStr(dataBaseType, tableName, sb, columnDto);

                }
                sb.append("\n");
            }
            log.info("---------------{}-----------------", dataBaseType);
            System.out.println("\n");
            System.out.println(sb);
            System.out.println("\n");

        }
    }

    /**
     * 修改sql
     * @param dataBaseType
     * @param tableName
     * @param sb
     * @param columnDto
     */
    private static void modifyColumnStr(String dataBaseType, String tableName, StringBuilder sb, GenColumnDto columnDto) {
        subCommonStr(tableName, sb, columnDto);
        // 拼接剩下得
        subRemain(dataBaseType, sb, columnDto);
    }

    /**
     * 新增
     * @param dataBaseType
     * @param tableName
     * @param sb
     * @param columnDto
     */
    private static void addColumnStr(String dataBaseType, String tableName, StringBuilder sb, GenColumnDto columnDto) {
        sb.append("call proc_addcolumn('alter table ").append(tableName).append(" add ");
        // 字段名
        sb.append(columnDto.getColumnKey()).append(" ");
        // 长度
        dealVarcharType(dataBaseType, columnDto, sb);
        dealDecimalType(dataBaseType, columnDto, sb);
        // 字段解释
        dealColumnComment(dataBaseType, tableName, columnDto, sb);
    }

    private static void subRemain(String dataBaseType, StringBuilder sb, GenColumnDto columnDto) {
        if (dataBaseType.equals("oracle")) {
            sb.append(" modify ").append(columnDto.getColumnKey()).append(" ");
            if (columnDto.getColumnType().equals(VAR)) {
                sb.append("varchar2(").append(columnDto.getColumnLength()).append(")');");
            } else if (columnDto.getColumnType().equals(INT)) {
                sb.append("'decimal(").append(columnDto.getColumnLength()).append(")');");
            }
        } else if (dataBaseType.equals("mysql")) {
            sb.append(" modify ").append(columnDto.getColumnKey()).append(" ");
            if (columnDto.getColumnType().equals(VAR)) {
                sb.append("varchar(").append(columnDto.getColumnLength()).append(")');");
            } else if (columnDto.getColumnType().equals(INT)) {
                sb.append("decimal(").append(columnDto.getColumnLength()).append(")');");
            }
        } else if (dataBaseType.equals("postgrep")) {
            sb.append(" alter column ").append(columnDto.getColumnKey())
                    .append(" type ");
            if (columnDto.getColumnType().equals(VAR)) {
                sb.append("varchar(").append(columnDto.getColumnLength()).append(")');");
            } else if (columnDto.getColumnType().equals(INT)) {
                sb.append("decimal(").append(columnDto.getColumnLength()).append(")');");
            }
        }
    }

    private static void subCommonStr(String tableName, StringBuilder sb, GenColumnDto columnDto) {
        sb.append("call proc_modColumn('").append(tableName).append("','").append(columnDto.getColumnKey()).append("',");
        sb.append("'alter table ").append(tableName);
    }

    private static void dealColumnComment(String dataBaseType, String tableName, GenColumnDto columnDto, StringBuilder sb) {
        if (columnDto.getColumnComment() != null && !dataBaseType.equals("mysql")) {
            sb.append("\n");
            sb.append("comment on column ").append(tableName).append(".").append(columnDto.getColumnKey()).append(" ");
            sb.append(" is '").append(columnDto.getColumnComment()).append("';");
        }
    }

    private static void dealDecimalType(String dataBaseType, GenColumnDto columnDto, StringBuilder sb) {
        if (columnDto.getColumnType().equals(INT) && dataBaseType.equals("oracle")) {
            sb.append(" decimal(").append(columnDto.getColumnLength()).append(")");
            sb.append(" default 0 not null);");
        } else if (columnDto.getColumnType().equals(INT) && dataBaseType.equals("mysql")) {
            sb.append(" decimal(").append(columnDto.getColumnLength()).append(")");
            sb.append(" not null default 0 comment ''").append(columnDto.getColumnComment()).append("''');");
        } else if (columnDto.getColumnType().equals(INT) && dataBaseType.equals("postgrep")) {
            sb.append("decimal(").append(columnDto.getColumnLength()).append(")");
            sb.append(" not null default 0 ");
        }
    }

    private static void dealVarcharType(String dataBaseType, GenColumnDto columnDto, StringBuilder sb) {
        if (columnDto.getColumnType().equals(VAR) && dataBaseType.equals("oracle")) {
            sb.append(" varchar2(").append(columnDto.getColumnLength()).append(")");
            sb.append(" default '' '' not null');");
        } else if (columnDto.getColumnType().equals(VAR) && dataBaseType.equals("mysql")) {
            sb.append(" varchar(").append(columnDto.getColumnLength()).append(")");
        } else if (columnDto.getColumnType().equals(VAR) && dataBaseType.equals("postgrep")) {
            sb.append(" varchar(").append(columnDto.getColumnLength()).append(")");
        }
    }

}
