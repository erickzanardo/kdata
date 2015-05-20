package org.eck.kdata.entity;

import java.util.Map;

import org.eck.kdata.KDataManager;
import org.eck.kdata.KEntityEntry;
import org.junit.Assert;
import org.junit.Test;

public class InheritanceTest {

    @Test
    public void testParentEntity() {
        ParentEntity pe = new ParentEntity();
        pe.setId(1l);
        pe.setParentField("bla");

        Map<String, Object> map = pe.toMap();
        Assert.assertEquals(new Long(1l), map.get("id"));
        Assert.assertEquals("bla", map.get("parentField"));

        KEntityEntry entry = KDataManager.getEntry(ParentEntity.class);
        Assert.assertEquals("ParentEntity", entry.getKind());
    }

    @Test
    public void testChildEntity() {
        ChildEntity ce = new ChildEntity();
        ce.setId(1l);
        ce.setParentField("bla");
        ce.setChildField("ble");

        Map<String, Object> map = ce.toMap();
        Assert.assertEquals(new Long(1l), map.get("id"));
        Assert.assertEquals("bla", map.get("parentField"));
        Assert.assertEquals("ble", map.get("childField"));

        KEntityEntry entry = KDataManager.getEntry(ChildEntity.class);
        Assert.assertEquals("ParentEntity", entry.getKind());
    }
}
