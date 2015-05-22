package org.eck.kdata;

import org.eck.kdata.finder.KGaeFinder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class BigTextTest {
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
    public void test() {
        String text = generateBigText();
        SampleEntity se = new SampleEntity();
        se.setName(text);
        se.save();
        
        se = KDataManager.getFinder().get(se.getId(), SampleEntity.class);
        Assert.assertEquals(text, se.getName());
    }

    public String generateBigText() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3000; i++)
            sb.append("A");

        return sb.toString();
    }
}
