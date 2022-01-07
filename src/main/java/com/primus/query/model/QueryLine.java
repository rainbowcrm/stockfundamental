package com.primus.query.model;

public class QueryLine {

    public  enum  PropertySet {
        INDS,SCT,CRPRC,EPS,PE,BV,PB,ROE,DIV,CPSZ,RVN
    };


    PropertySet property;
    String operator;
    String value;
    String condition;

    int openingBraces;
    int closingBraces;

    public PropertySet getProperty() {
        return property;
    }

    public void setProperty(PropertySet property) {
        this.property = property;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    public int getOpeningBraces() {
        return openingBraces;
    }

    public void setOpeningBraces(int openingBraces) {
        this.openingBraces = openingBraces;
    }

    public int getClosingBraces() {
        return closingBraces;
    }

    public void setClosingBraces(int closingBraces) {
        this.closingBraces = closingBraces;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
