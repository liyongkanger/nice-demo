package com.lyk.dto;

public enum FieldTypeEnum {

    INTEGER("integer"),
    LONG("long"),
    FLOAT("float"),
    BOOLEAN("boolean"),
    DATE("date"),
    KEYWORD("keyword"),
    TEXT("text"),
    KEYWORD_TEXT("keyword&text")


    ;

    private String type;

    private FieldTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    }
