package org.eck.kdata.manager.memory;

import org.eck.kdata.KEntity;
import org.eck.kdata.annotations.KField;
import org.eck.kdata.annotations.KId;

public class MemoryEntity extends KEntity {
    @KId
    private Long id;
    @KField
    private String name;

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
}
