package org.eck.kdata;

import org.eck.kdata.KEntity;
import org.eck.kdata.annotations.KField;
import org.eck.kdata.annotations.KId;

public class TestEntity extends KEntity {
    @KId
    private Long id;
    @KField
    private String name;
    @KField
    private Integer age;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
