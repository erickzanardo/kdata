package org.eck.kdata.finder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eck.kdata.KDataKeyFactory;
import org.eck.kdata.KDataManager;
import org.eck.kdata.KEntity;
import org.eck.kdata.KEntityEntry;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;

public class KGaeFinder extends KFinder {

    @Override
    public <T extends KEntity> List<T> find(Class<T> type, Filter... filters) {
        return null;
    }

    @Override
    public <T extends KEntity> T get(Object id, Class<T> type) {
        KEntityEntry entry = KDataManager.getEntry(type);

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key key = KDataKeyFactory.getKey(entry.getKind(), id);
        try {
            Entity entity = datastoreService.get(key);

            String idField = entry.getIdField();
            Map<String, Method> setters = entry.getSetters();
            Set<Entry<String, Method>> entrySet = setters.entrySet();
            
            try {
                T kEntity = type.newInstance();
                Method setId = setters.get(idField);
                setId.invoke(kEntity, id);
                for (Entry<String, Method> setterEntry : entrySet) {
                    if (setterEntry.getKey().equals(idField))
                        continue;
                    
                    Method setter = setterEntry.getValue();
                    setter.invoke(kEntity, entity.getProperty(setterEntry.getKey()));
                }
                return kEntity;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

}
