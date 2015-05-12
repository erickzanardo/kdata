package org.eck.kdata;

import java.util.HashMap;
import java.util.Map;

public class KMemoryDB {
    private static Map<String, Map<Long, KEntity>> db = new HashMap<String, Map<Long, KEntity>>();

    public static Map<String, Map<Long, KEntity>> db() {
        return db;
    }
}
