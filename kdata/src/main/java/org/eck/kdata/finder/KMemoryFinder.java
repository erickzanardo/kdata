package org.eck.kdata.finder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eck.kdata.KDataManager;
import org.eck.kdata.KEntity;
import org.eck.kdata.KMemoryDB;

public class KMemoryFinder extends KFinder {

    @SuppressWarnings("unchecked")
    @Override
    public <T extends KEntity> T get(Object id, Class<T> type) {
        String kind = KDataManager.getEntry(type).getKind();
        Map<Long, KEntity> kinds = KMemoryDB.db().get(kind);
        if (kinds != null) {
            return (T) kinds.get(id);
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T extends KEntity> List<T> find(Class<T> type, Filter... filters) {
        List<T> result = new ArrayList<T>();

        String kind = KDataManager.getEntry(type).getKind();

        Map<Long, KEntity> kinds = KMemoryDB.db().get(kind);
        if (kinds != null) {
            Set<Entry<Long, KEntity>> entrySet = kinds.entrySet();
            for (Entry<Long, KEntity> entry : entrySet) {
                T thisE = (T) entry.getValue();
                Map<String, Object> map = thisE.toMap();
                boolean match = true;
                for (Filter filter : filters) {
                    if (filter.getOperator().equals(Filter.O.EQ)) {
                        if (!map.get(filter.getField()).equals(filter.getValue())) {
                            match = false;
                        }
                    } else {
                        Comparable c = ((Comparable) map.get(filter.getField()));
                        if (filter.getOperator().equals(Filter.O.GT)) {
                            if (c.compareTo(filter.getValue()) < 1) {
                                match = false;
                            }
                        } else if (filter.getOperator().equals(Filter.O.GET)) {
                            if (c.compareTo(filter.getValue()) < 0) {
                                match = false;
                            }
                        } else if (filter.getOperator().equals(Filter.O.LT)) {
                            if (c.compareTo(filter.getValue()) >= 0) {
                                match = false;
                            }
                        } else if (filter.getOperator().equals(Filter.O.LET)) {
                            if (c.compareTo(filter.getValue()) > 0) {
                                match = false;
                            }
                        }
                    }
                }
                if (match) {
                    result.add(thisE);
                }
            }
        }

        return result;
    }
}
