package org.eck.kdata;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eck.kdata.annotations.KField;
import org.eck.kdata.annotations.KId;
import org.eck.kdata.finder.KFinder;

public class KDataManager {
    private static KStorager storager;
    private static KFinder finder;
    @SuppressWarnings("rawtypes")
    private static Map<Class, KEntityEntry> entries = new HashMap<Class, KEntityEntry>();

    public static KStorager getStorager() {
        return storager;
    }

    public static void setStorager(KStorager storager) {
        KDataManager.storager = storager;
    }

    public static KFinder getFinder() {
        return finder;
    }

    public static void setFinder(KFinder finder) {
        KDataManager.finder = finder;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static KEntityEntry getEntry(Class type) {
        KEntityEntry kEntityEntry = entries.get(type);

        if (kEntityEntry == null) {
            kEntityEntry = new KEntityEntry();
            kEntityEntry.setKind(type.getSimpleName());

            List<Field> fields = new ArrayList<Field>();
            fields.addAll(Arrays.asList(type.getDeclaredFields()));

            Class superclass = type.getSuperclass();
            Class lastSuperClass = superclass;
            while (!superclass.equals(KEntity.class) && !superclass.equals(Object.class)) {
                fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
                lastSuperClass = superclass;
                superclass = superclass.getSuperclass();
            }

            if (lastSuperClass.equals(KEntity.class)) {
                kEntityEntry.setKind(type.getSimpleName());
            } else {
                kEntityEntry.setKind(lastSuperClass.getSimpleName());
                kEntityEntry.setChild(true);
            }

            Map<String, Method> getters = new HashMap<String, Method>();

            for (Field field : fields) {
                if (field.isAnnotationPresent(KId.class) || field.isAnnotationPresent(KField.class)) {
                    String name = field.getName();

                    StringBuilder getterName = new StringBuilder();
                    getterName.append("get").append(name.substring(0, 1).toUpperCase()).append(name.substring(1, name.length()));

                    try {
                        Method getter = type.getMethod(getterName.toString());
                        getters.put(name, getter);
                    } catch (NoSuchMethodException | SecurityException e) {
                        throw new RuntimeException("We could not find a getter for the field: " + name, e);
                    }
                }
            }

            Map<String, Method> setters = new HashMap<String, Method>();

            for (Field field : fields) {
                if (field.isAnnotationPresent(KId.class) || field.isAnnotationPresent(KField.class)) {
                    if (field.isAnnotationPresent(KId.class)) {
                        kEntityEntry.setIdField(field.getName());
                    }
                    String name = field.getName();

                    StringBuilder setterName = new StringBuilder();
                    setterName.append("set").append(name.substring(0, 1).toUpperCase()).append(name.substring(1, name.length()));

                    Method setter;
                    try {
                        setter = type.getMethod(setterName.toString(), field.getType());
                    } catch (NoSuchMethodException | SecurityException e) {
                        throw new RuntimeException("We could not find a setter for the field: " + name, e);
                    }
                    setters.put(name, setter);
                }
            }
            kEntityEntry.setGetters(getters);
            kEntityEntry.setSetters(setters);

            entries.put(type, kEntityEntry);
        }

        return kEntityEntry;
    }
}
