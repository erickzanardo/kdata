package org.eck.kdata;

import java.util.Comparator;
import java.util.List;

import org.eck.kdata.finder.Filter;
import org.eck.kdata.finder.KGaeFinder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class KGaeTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setup() {
        KDataManager.setFinder(new KGaeFinder());
        KDataManager.setStorager(new KGaeStorager());

        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void testInsert() {
        TestEntity e = new TestEntity();
        e.setName("Erick");
        e.save();

        Assert.assertTrue(e.getId() != null);
    }

    @Test
    public void testGet() {
        TestEntity e = new TestEntity();
        e.setName("Erick");
        e.save();

        TestEntity result = KDataManager.getFinder().get(e.getId(), TestEntity.class);
        Assert.assertEquals(e.getId(), result.getId());
        Assert.assertEquals(e.getName(), result.getName());
    }

    @Test
    public void testDelete() {
        TestEntity e = new TestEntity();
        e.setName("Erick");
        e.save();

        e.delete();
        TestEntity result = KDataManager.getFinder().get(e.getId(), TestEntity.class);
        Assert.assertNull(result);
    }

    @Test
    public void testFind() {
        TestEntity e = new TestEntity();
        e.setName("Erick");
        e.setAge(24);
        e.save();

        e = new TestEntity();
        e.setName("Erick 2");
        e.setAge(24);
        e.save();

        e = new TestEntity();
        e.setName("John");
        e.setAge(26);
        e.save();

        e = new TestEntity();
        e.setName("James");
        e.setAge(27);
        e.save();

        // EQ
        List<TestEntity> result = KDataManager.getFinder().find(TestEntity.class, new Filter("age", 24));
        sort(result);
        Assert.assertEquals(2, result.size());
        e = result.get(0);

        Assert.assertEquals("Erick", e.getName());
        Assert.assertEquals(new Integer(24), e.getAge());

        e = result.get(1);

        Assert.assertEquals("Erick 2", e.getName());
        Assert.assertEquals(new Integer(24), e.getAge());

        // GT
        result = KDataManager.getFinder().find(TestEntity.class, new Filter("age", 24, Filter.O.GT));
        sort(result);
        Assert.assertEquals(2, result.size());

        e = result.get(0);
        Assert.assertEquals("James", e.getName());
        Assert.assertEquals(new Integer(27), e.getAge());

        e = result.get(1);
        Assert.assertEquals("John", e.getName());
        Assert.assertEquals(new Integer(26), e.getAge());

        // GET
        result = KDataManager.getFinder().find(TestEntity.class, new Filter("age", 24, Filter.O.GET));
        sort(result);
        Assert.assertEquals(4, result.size());

        // LT
        result = KDataManager.getFinder().find(TestEntity.class, new Filter("age", 27, Filter.O.LT));
        sort(result);
        Assert.assertEquals(3, result.size());

        // LET
        result = KDataManager.getFinder().find(TestEntity.class, new Filter("age", 27, Filter.O.LET));
        sort(result);
        Assert.assertEquals(4, result.size());
    }

    private void sort(List<TestEntity> result) {
        result.sort(new Comparator<TestEntity>() {
            @Override
            public int compare(TestEntity o1, TestEntity o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
    }
}
