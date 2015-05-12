package org.eck.kdata;

import org.eck.kdata.finder.KFinder;

public class KDataManager {
    private static KStorager storager;
    private static KFinder finder;

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
}
