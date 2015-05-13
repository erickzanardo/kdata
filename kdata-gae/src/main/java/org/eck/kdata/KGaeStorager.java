package org.eck.kdata;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

public class KGaeStorager extends KStorager {

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
                gaeEntity.setProperty(fieldEntry.getKey(), fieldEntry.getValue());
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
