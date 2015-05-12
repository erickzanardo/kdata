package org.eck.kdata;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eck.kdata.annotations.KField;
import org.eck.kdata.annotations.KId;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public abstract class KEntity {
    public JsonObject toJson() {
        Map<String, Method> getters = getGetters();
        Set<Entry<String, Method>> entrySet = getters.entrySet();

        JsonObject obj = new JsonObject();
        for (Entry<String, Method> entry : entrySet) {
            try {
                obj.add(entry.getKey(), javaToJsonJson(entry.getValue().invoke(this)));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
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

    private Map<String, Method> getGetters() {
        // TODO this can be cached
        Map<String, Method> getters = new HashMap<String, Method>();
        Class<? extends KEntity> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(KId.class) || field.isAnnotationPresent(KField.class)) {
                String name = field.getName();

                StringBuilder getterName = new StringBuilder();
                getterName.append("get").append(name.substring(0, 1).toUpperCase()).append(name.substring(1, name.length()));

                try {
                    Method getter = clazz.getMethod(getterName.toString());
                    getters.put(name, getter);
                } catch (NoSuchMethodException | SecurityException e) {
                    throw new RuntimeException("We could not find a getter for the field: " + name, e);
                }
            }
        }

        return getters;
    }

    public void fromJson(JsonObject obj) {
        Set<Entry<String, JsonElement>> entrySet = obj.entrySet();
        Map<String, Method> setters = getSetters();
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

    private Map<String, Method> getSetters() {
        // TODO this can be cached
        Map<String, Method> setters = new HashMap<String, Method>();
        Class<? extends KEntity> clazz = this.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(KId.class) || field.isAnnotationPresent(KField.class)) {
                String name = field.getName();

                StringBuilder setterName = new StringBuilder();
                setterName.append("set").append(name.substring(0, 1).toUpperCase()).append(name.substring(1, name.length()));

                Method setter;
                try {
                    setter = clazz.getMethod(setterName.toString(), field.getType());
                } catch (NoSuchMethodException | SecurityException e) {
                    throw new RuntimeException("We could not find a setter for the field: " + name, e);
                }
                setters.put(name, setter);
            }
        }
        return setters;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Object jsonElementToJava(JsonElement elem, Class<?> type) {
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
