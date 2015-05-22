package org.eck.kdata;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

public class KGaeStorager extends KStorager {
    public static final String _TYPE = "_TYPE";

    @Override
    public void delete(KEntity entity, String idField) {
        if (entity != null) {
            DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
            KEntityEntry entry = KDataManager.getEntry(entity.getClass());
            String kind = entry.getKind();
            Method method = entry.getGetters().get(idField);
            try {
                Key key = KDataKeyFactory.getKey(kind, method.invoke(entity));
                datastoreService.delete(key);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public KEntity save(KEntity entity, String idField) {

        if (entity != null) {
            DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

            KEntityEntry entry = KDataManager.getEntry(entity.getClass());
            String kind = entry.getKind();

            Map<String, Object> map = entity.toMap();
            Entity gaeEntity;
            if (map.get(idField) != null) {
                gaeEntity = new Entity(KDataKeyFactory.getKey(kind, map.get(idField)));
            } else {
                gaeEntity = new Entity(kind);
            }

            Set<Entry<String, Object>> entrySet = map.entrySet();
            for (Entry<String, Object> fieldEntry : entrySet) {
                if (fieldEntry.getKey().equals(idField)) {
                    continue;
                }
                Object value = fieldEntry.getValue();
                if(value instanceof String) {
                    String s = (String) value;
                    if(s.length() > 1500) {
                        value = new Text(s);
                    }
                }
                gaeEntity.setProperty(fieldEntry.getKey(), value);
            }

            if (entry.isChild()) {
                gaeEntity.setProperty(_TYPE, entity.getClass().getName());
            }

            datastoreService.put(gaeEntity);
            Method setId = entry.getSetters().get(idField);
            try {
                if (gaeEntity.getKey().getName() != null)
                    setId.invoke(entity, gaeEntity.getKey().getName());
                else
                    setId.invoke(entity, gaeEntity.getKey().getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
