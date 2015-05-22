package org.eck.kdata.finder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eck.kdata.KDataKeyFactory;
import org.eck.kdata.KDataManager;
import org.eck.kdata.KEntity;
import org.eck.kdata.KEntityEntry;
import org.eck.kdata.KGaeStorager;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Text;

public class KGaeFinder extends KFinder {

    @Override
    public <T extends KEntity> List<T> find(Class<T> type, Filter... filters) {
        KEntityEntry entry = KDataManager.getEntry(type);

        List<T> result = new ArrayList<T>();
        Query q = new Query(entry.getKind());
        if (filters.length > 0) {
            List<Query.Filter> gaeFilters = new ArrayList<Query.Filter>();

            for (Filter f : filters) {
                Query.FilterOperator o = null;

                if (f.getOperator().equals(Filter.O.EQ))
                    o = Query.FilterOperator.EQUAL;
                else if (f.getOperator().equals(Filter.O.GET))
                    o = Query.FilterOperator.GREATER_THAN_OR_EQUAL;
                else if (f.getOperator().equals(Filter.O.GT))
                    o = Query.FilterOperator.GREATER_THAN;
                else if (f.getOperator().equals(Filter.O.LET))
                    o = Query.FilterOperator.LESS_THAN_OR_EQUAL;
                else if (f.getOperator().equals(Filter.O.LT))
                    o = Query.FilterOperator.LESS_THAN;

                FilterPredicate filterPredicate = new Query.FilterPredicate(f.getField(), o, f.getValue());
                gaeFilters.add(filterPredicate);
            }

            if (gaeFilters.size() > 1) {
                q.setFilter(CompositeFilterOperator.and(gaeFilters));
            } else {
                q.setFilter(gaeFilters.get(0));
            }
        }

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = datastoreService.prepare(q);
        Iterable<Entity> asIterable = preparedQuery.asIterable();
        for (Entity entity : asIterable) {
            result.add(gaeEntityToKentity(type, entity));
        }

        return result;
    }

    @Override
    public <T extends KEntity> T get(Object id, Class<T> type) {
        KEntityEntry entry = KDataManager.getEntry(type);

        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key key = KDataKeyFactory.getKey(entry.getKind(), id);
        try {
            Entity entity = datastoreService.get(key);
            return gaeEntityToKentity(type, entity);
        } catch (EntityNotFoundException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends KEntity> T gaeEntityToKentity(Class<T> type, Entity entity) {
        String _type = (String) entity.getProperty(KGaeStorager._TYPE);
        if (_type != null) {
            try {
                type = (Class<T>) Class.forName(_type);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        KEntityEntry entry = KDataManager.getEntry(type);
        String idField = entry.getIdField();
        Map<String, Method> setters = entry.getSetters();

        Object id = null;
        if (entity.getKey().getName() != null) {
            id = entity.getKey().getName();
        } else {
            id = entity.getKey().getId();
        }

        try {
            T kEntity = type.newInstance();
            Method setId = setters.get(idField);
            setId.invoke(kEntity, id);

            Set<Entry<String, Method>> entrySet = setters.entrySet();
            for (Entry<String, Method> setterEntry : entrySet) {
                if (setterEntry.getKey().equals(idField))
                    continue;

                Method setter = setterEntry.getValue();

                // Gae treats ever number as long,
                // so we need to check that our setter is an Integer
                Class<?> paramType = setter.getParameterTypes()[0];
                Object val = entity.getProperty(setterEntry.getKey());
                if (paramType.equals(Integer.class)) {
                    Long long1 = (Long) val;
                    if(long1 != null)
                        val = long1.intValue();
                } else if (val instanceof Text) {
                    val = ((Text) val).getValue();
                }

                setter.invoke(kEntity, val);
            }
            return kEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
