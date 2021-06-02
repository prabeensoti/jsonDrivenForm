package com.jsondriventemplate;

public enum  StatusCode {

    NOT_FOUND("404");

    private final String value;

    StatusCode(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

}
