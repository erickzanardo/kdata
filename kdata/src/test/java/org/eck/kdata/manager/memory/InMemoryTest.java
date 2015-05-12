package org.eck.kdata.manager.memory;

import org.eck.kdata.KDataManager;
import org.eck.kdata.KMemoryStorager;
import org.eck.kdata.finder.KMemoryFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InMemoryTest {

    @Before
    public void setup() {
        KDataManager.setFinder(new KMemoryFinder());
        KDataManager.setStorager(new KMemoryStorager());
    }

    @Test
    public void testInsert() {
        MemoryEntity e = new MemoryEntity();
        e.setName("Erick");
        e.save();

        Assert.assertTrue(e.getId() != null);
    }

    @Test
    public void testGet() {
        MemoryEntity e = new MemoryEntity();
        e.setName("Erick");
        e.save();

        MemoryEntity result = KDataManager.getFinder().get(e.getId(), MemoryEntity.class);
        Assert.assertEquals(e.getId(), result.getId());
        Assert.assertEquals(e.getName(), result.getName());
    }

    @Test
    public void testDelete() {
        MemoryEntity e = new MemoryEntity();
        e.setName("Erick");
        e.save();

        e.delete();
        MemoryEntity result = KDataManager.getFinder().get(e.getId(), MemoryEntity.class);
        Assert.assertNull(result);
    }
}
