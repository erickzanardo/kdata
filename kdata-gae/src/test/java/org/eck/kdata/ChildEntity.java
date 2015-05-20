package org.eck.kdata;

import org.eck.kdata.annotations.KField;

public class ChildEntity extends ParentEntity {
    @KField
    private String childField;

    public String getChildField() {
        return childField;
    }

    public void setChildField(String childField) {
        this.childField = childField;
    }

}
