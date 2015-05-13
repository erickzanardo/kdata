package org.eck.kdata;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public abstract class KEntity {

    public void save() {
        KDataManager.getStorager().save(this, getIdFieldName());
    }

    public void delete() {
        KDataManager.getStorager().delete(this, getIdFieldName());
    }

    private String getIdFieldName() {
        return KDataManager.getEntry(getClass()).getIdField();
    }

    public JsonObject toJson() {
        Map<String, Object> map = toMap();
        Set<Entry<String, Object>> entrySet = map.entrySet();

        JsonObject obj = new JsonObject();
        for (Entry<String, Object> entry : entrySet) {
            obj.add(entry.getKey(), javaToJsonJson(entry.getValue()));
        }

        return obj;
    }

    @SuppressWarnings("rawtypes")
    private JsonElement javaToJsonJson(Object value) {
        if (value instanceof List) {
            List list = (List) value;
            JsonArray jsonArray = new JsonArray();
            for (Object object : list) {
                jsonArray.add(javaToJsonJson(object));
            }
            return jsonArray;
        } else if (value instanceof Boolean) {
            return new JsonPrimitive((Boolean) value);
        } else if (value instanceof Number) {
            return new JsonPrimitive((Number) value);
        } else if (value instanceof String) {
            return new JsonPrimitive((String) value);
        }
        return null;
    }

    public Map<String, Object> toMap() {
        KEntityEntry entityEntry = KDataManager.getEntry(this.getClass());
        Map<String, Method> getters = entityEntry.getGetters();
        Set<Entry<String, Method>> entrySet = getters.entrySet();

        Map<String, Object> map = new HashMap<String, Object>();
        for (Entry<String, Method> entry : entrySet) {
            try {
                map.put(entry.getKey(), entry.getValue().invoke(this));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    public void fromJson(JsonObject obj) {
        KEntityEntry entityEntry = KDataManager.getEntry(this.getClass());
        Set<Entry<String, JsonElement>> entrySet = obj.entrySet();
        Map<String, Method> setters = entityEntry.getSetters();
        for (Entry<String, JsonElement> entry : entrySet) {
            Method setter = setters.get(entry.getKey());
            try {
                Class<?> type = setter.getParameterTypes()[0];
                setter.invoke(this, jsonElementToJava(entry.getValue(), type));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object jsonElementToJava(JsonElement elem, Class<?> type) {
        if (elem == null || elem.isJsonNull()) return null;
        if (type.equals(Number.class)) {
            return elem.getAsNumber();
        } else if (type.equals(Long.class)) {
            return elem.getAsLong();
        } else if (type.equals(Integer.class)) {
            return elem.getAsInt();
        } else if (type.equals(String.class)) {
            return elem.getAsString();
        } else if (type.equals(Boolean.class)) {
            return elem.getAsBoolean();
        } else if (type.equals(List.class)) {
            JsonArray jsonArray = elem.getAsJsonArray();
            JsonPrimitive first = jsonArray.get(0).getAsJsonPrimitive();
            Class<?> iType = null;
            if (first.isNumber()) {
                iType = Number.class;
            } else if (first.isBoolean()) {
                iType = Boolean.class;
            } else if (first.isString()) {
                iType = String.class;
            }
            List value = new ArrayList();
            for (JsonElement jsonElement : jsonArray) {
                value.add(jsonElementToJava(jsonElement, iType));
            }
            return value;
        }
        return null;
    }

}
