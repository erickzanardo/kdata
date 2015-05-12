package org.eck.kdata;

public abstract class KStorager {
    public abstract KEntity save(KEntity entity, String idField);
    public abstract void delete(KEntity entity, String idField);
}
