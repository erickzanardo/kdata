package org.eck.kdata.manager.memory;

import org.eck.kdata.KDataManager;
import org.eck.kdata.KMemoryDB;
import org.eck.kdata.KMemoryStorager;
import org.eck.kdata.finder.KMemoryFinder;
import org.eck.kdata.manager.AbstractTest;
import org.junit.Before;

public class InMemoryTest extends AbstractTest {
    @Before
    public void setup() {
        KMemoryDB.db().clear();
        KDataManager.setFinder(new KMemoryFinder());
        KDataManager.setStorager(new KMemoryStorager());
    }
}
