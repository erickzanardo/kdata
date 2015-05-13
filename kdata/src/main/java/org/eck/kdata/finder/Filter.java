package org.eck.kdata.finder;

public class Filter {
    private String field;
    private Object value;
    private O operator;

    public Filter(String field, Object value) {
        super();
        this.field = field;
        this.value = value;
        this.operator = O.EQ;
    }

    public Filter(String field, Object value, O operator) {
        super();
        this.field = field;
        this.value = value;
        this.operator = operator;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public O getOperator() {
        return operator;
    }

    public void setOperator(O operator) {
        this.operator = operator;
    }

    public enum O {
        EQ, GT, LT, GET, LET
    }
}
