package org.eck.kdata.manager.memory;

import java.util.List;

import org.eck.kdata.KDataManager;
import org.eck.kdata.KMemoryDB;
import org.eck.kdata.KMemoryStorager;
import org.eck.kdata.finder.Filter;
import org.eck.kdata.finder.KMemoryFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InMemoryTest {

    @Before
    public void setup() {
        KMemoryDB.db().clear();
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

    @Test
    public void testFind() {
        MemoryEntity e = new MemoryEntity();
        e.setName("Erick");
        e.setAge(24);
        e.save();

        e = new MemoryEntity();
        e.setName("John");
        e.setAge(26);
        e.save();

        List<MemoryEntity> result = KDataManager.getFinder().find(MemoryEntity.class, new Filter("age", 24));
        Assert.assertEquals(1, result.size());
        e = result.get(0);

        Assert.assertEquals("Erick", e.getName());
        Assert.assertEquals(new Integer(24), e.getAge());
    }
}
