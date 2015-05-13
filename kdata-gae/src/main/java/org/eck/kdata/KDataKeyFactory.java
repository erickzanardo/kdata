package org.eck.kdata;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class KDataKeyFactory {
    public static Key getKey(String kind, Object id) {
        if (id instanceof Long) {
            return KeyFactory.createKey(kind, (Long) id);
        } else if (id instanceof String) {
            return KeyFactory.createKey(kind, (String) id);
        } else {
            throw new RuntimeException("Can't save this kind of id on GAE: " + id.getClass().getSimpleName());
        }
    }
}
