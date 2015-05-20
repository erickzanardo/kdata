package org.eck.kdata;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class KEntityEntry {
    private String kind;
    private String idField;
    private Map<String, Method> setters = new HashMap<String, Method>();
    private Map<String, Method> getters = new HashMap<String, Method>();
    private boolean child;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public Map<String, Method> getSetters() {
        return setters;
    }

    public void setSetters(Map<String, Method> setters) {
        this.setters = setters;
    }

    public Map<String, Method> getGetters() {
        return getters;
    }

    public void setGetters(Map<String, Method> getters) {
        this.getters = getters;
    }

    public boolean isChild() {
        return child;
    }

    public void setChild(boolean child) {
        this.child = child;
    }
}
