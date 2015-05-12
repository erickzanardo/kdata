package org.eck.kdata.finder;

import java.util.List;
import java.util.Map;

import org.eck.kdata.KEntity;
import org.eck.kdata.KMemoryDB;

public class KMemoryFinder extends KFinder {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends KEntity> T get(Object id, Class<T> type) {
        try {
            KEntity e = type.newInstance();
            String kind = e.kind();

            Map<Long, KEntity> kinds = KMemoryDB.db().get(kind);
            if(kinds != null) {
                return (T) kinds.get(id);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public <T extends KEntity> List<T> find(Class<T> type, Filter... filters) {
        // TODO Auto-generated method stub
        return null;
    }

}
