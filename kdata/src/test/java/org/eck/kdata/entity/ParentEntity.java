package org.eck.kdata.entity;

import org.eck.kdata.KEntity;
import org.eck.kdata.annotations.KField;
import org.eck.kdata.annotations.KId;

public class ParentEntity extends KEntity {
    @KId
    private Long id;
    @KField
    private String parentField;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParentField() {
        return parentField;
    }

    public void setParentField(String parentField) {
        this.parentField = parentField;
    }
}
