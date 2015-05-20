package org.eck.kdata.manager.memory;

import java.util.Comparator;
import java.util.List;

import org.eck.kdata.KDataManager;
import org.eck.kdata.KMemoryDB;
import org.eck.kdata.KMemoryStorager;
import org.eck.kdata.entity.ChildEntity;
import org.eck.kdata.entity.ParentEntity;
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
        ChildEntity e = new ChildEntity();
        e.setParentField("Bla");
        e.setChildField("Ble");
        e.save();

        Assert.assertTrue(e.getId() != null);
    }

    @Test
    public void testGet() {
        ChildEntity e = new ChildEntity();
        e.setParentField("Bla");
        e.setChildField("Ble");
        e.save();

        ChildEntity result = KDataManager.getFinder().get(e.getId(), ChildEntity.class);
        Assert.assertEquals(e.getId(), result.getId());
        Assert.assertEquals(e.getParentField(), result.getParentField());
        Assert.assertEquals(e.getChildField(), result.getChildField());
    }

    @Test
    public void testDelete() {
        ChildEntity e = new ChildEntity();
        e.setParentField("Bla");
        e.setChildField("Ble");
        e.save();

        e.delete();
        ChildEntity result = KDataManager.getFinder().get(e.getId(), ChildEntity.class);
        Assert.assertNull(result);
    }

    @Test
    public void testFind() {
        ChildEntity e = new ChildEntity();
        e.setParentField("Bla1");
        e.setChildField("Ble");
        e.save();

        ParentEntity pe = new ParentEntity();
        pe.setParentField("Bla");
        pe.save();

        // EQ
        List<ParentEntity> result = KDataManager.getFinder().find(ParentEntity.class);
        sort(result);
        Assert.assertEquals(2, result.size());

        pe = result.get(0);
        Assert.assertEquals("Bla", pe.getParentField());
        Assert.assertTrue(pe instanceof ParentEntity);

        pe = result.get(1);
        Assert.assertEquals("Bla1", pe.getParentField());
        Assert.assertTrue(pe instanceof ChildEntity);

        result = KDataManager.getFinder().find(ParentEntity.class, new Filter("childField", "Ble"));
        Assert.assertEquals(1, result.size());

        ParentEntity parentEntity = result.get(0);
        Assert.assertTrue(parentEntity instanceof ChildEntity);
        Assert.assertEquals("Ble", ((ChildEntity) parentEntity).getChildField());
    }

    private void sort(List<ParentEntity> result) {
        result.sort(new Comparator<ParentEntity>() {
            @Override
            public int compare(ParentEntity o1, ParentEntity o2) {
                return o1.getParentField().compareTo(o2.getParentField());
            }
        });
    }
}
