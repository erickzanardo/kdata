package org.eck.kdata.finder;

import java.util.List;

import org.eck.kdata.KEntity;

public abstract class KFinder {
    public abstract <T extends KEntity> T get(Object id, Class<T> type);
    public abstract <T extends KEntity> List<T> find(Class<T> type, Filter ... filters);
}
