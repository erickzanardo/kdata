package org.eck.kdata.manager.memory;

import java.util.Comparator;
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
        e.setName("Erick 2");
        e.setAge(24);
        e.save();

        e = new MemoryEntity();
        e.setName("John");
        e.setAge(26);
        e.save();

        List<MemoryEntity> result = KDataManager.getFinder().find(MemoryEntity.class, new Filter("age", 24));
        result.sort(new Comparator<MemoryEntity>() {
            @Override
            public int compare(MemoryEntity o1, MemoryEntity o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        Assert.assertEquals(2, result.size());
        e = result.get(0);

        Assert.assertEquals("Erick", e.getName());
        Assert.assertEquals(new Integer(24), e.getAge());

        e = result.get(1);

        Assert.assertEquals("Erick 2", e.getName());
        Assert.assertEquals(new Integer(24), e.getAge());

    }
}
