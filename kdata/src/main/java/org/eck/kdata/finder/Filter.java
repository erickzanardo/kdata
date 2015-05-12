package org.eck.kdata.finder;

public class Filter {
    private String field;
    private Object value;

    public Filter(String field, Object value) {
        super();
        this.field = field;
        this.value = value;
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
}
