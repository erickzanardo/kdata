package org.eck.kdata.entity;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class JsonTest {

    @Test
    public void toJsonTest() {
        SampleEntity entity = new SampleEntity();
        entity.setId(1l);
        entity.setName("Erick");
        entity.setAge(24);
        entity.setCool(true);
        entity.setMoney(1.13);

        List<String> games = new ArrayList<String>();
        games.add("Assassins Creed");
        games.add("Battlefield");
        entity.setGames(games);

        List<Boolean> booleanList = new ArrayList<Boolean>();
        booleanList.add(true);
        booleanList.add(false);
        entity.setBooleanList(booleanList);

        List<Integer> integerList = new ArrayList<Integer>();
        integerList.add(1);
        integerList.add(2);
        entity.setIntegerList(integerList);

        List<Long> longList = new ArrayList<Long>();
        longList.add(1l);
        longList.add(2l);
        entity.setLongList(longList);

        List<Double> doubleList = new ArrayList<Double>();
        doubleList.add(1.3);
        doubleList.add(1.4);
        entity.setDoubleList(doubleList);

        List<Byte> rawData = new ArrayList<Byte>();
        rawData.add((byte) 0);
        rawData.add((byte) 1);
        entity.setRawData(rawData);

        JsonObject json = entity.toJson();
        Assert.assertEquals(1l, json.get("id").getAsLong());
        Assert.assertEquals("Erick", json.get("name").getAsString());
        Assert.assertEquals(24, json.get("age").getAsInt());
        Assert.assertEquals(true, json.get("cool").getAsBoolean());

        JsonArray gamesJson = json.get("games").getAsJsonArray();
        Assert.assertEquals(2, gamesJson.size());
        Assert.assertEquals("Assassins Creed", gamesJson.get(0).getAsJsonPrimitive().getAsString());
        Assert.assertEquals("Battlefield", gamesJson.get(1).getAsJsonPrimitive().getAsString());

        JsonArray booleanListJson = json.get("booleanList").getAsJsonArray();
        Assert.assertEquals(2, booleanList.size());
        Assert.assertEquals(true, booleanListJson.get(0).getAsJsonPrimitive().getAsBoolean());
        Assert.assertEquals(false, booleanListJson.get(1).getAsJsonPrimitive().getAsBoolean());

        JsonArray integerListJson = json.get("integerList").getAsJsonArray();
        Assert.assertEquals(2, integerListJson.size());
        Assert.assertEquals(1, integerListJson.get(0).getAsJsonPrimitive().getAsInt());
        Assert.assertEquals(2, integerListJson.get(1).getAsJsonPrimitive().getAsInt());

        JsonArray longListJson = json.get("longList").getAsJsonArray();
        Assert.assertEquals(2, longListJson.size());
        Assert.assertEquals(1, longListJson.get(0).getAsJsonPrimitive().getAsLong());
        Assert.assertEquals(2, longListJson.get(1).getAsJsonPrimitive().getAsLong());

        JsonArray doubleListJson = json.get("doubleList").getAsJsonArray();
        Assert.assertEquals(2, doubleListJson.size());
        Assert.assertEquals(1.3, doubleListJson.get(0).getAsJsonPrimitive().getAsDouble(), 0);
        Assert.assertEquals(1.4, doubleListJson.get(1).getAsJsonPrimitive().getAsDouble(), 0);

        JsonArray rawDataJson = json.get("rawData").getAsJsonArray();
        Assert.assertEquals(2, rawDataJson.size());
        Assert.assertEquals(0, rawDataJson.get(0).getAsJsonPrimitive().getAsByte());
        Assert.assertEquals(1, rawDataJson.get(1).getAsJsonPrimitive().getAsByte());
    }

    @Test
    public void fromJsonTest() {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", 1l);
        obj.addProperty("name", "Erick");
        obj.addProperty("age", 24);
        obj.addProperty("cool", true);

        JsonArray gamesJson = new JsonArray();
        gamesJson.add(new JsonPrimitive("Assassins Creed"));
        gamesJson.add(new JsonPrimitive("Battlefield"));
        obj.add("games", gamesJson);

        JsonArray booleanListJson = new JsonArray();
        booleanListJson.add(new JsonPrimitive(true));
        booleanListJson.add(new JsonPrimitive(false));
        obj.add("booleanList", booleanListJson);

        JsonArray integerListJson = new JsonArray();
        integerListJson.add(new JsonPrimitive(1));
        integerListJson.add(new JsonPrimitive(2));
        obj.add("integerList", integerListJson);

        JsonArray longListJson = new JsonArray();
        longListJson.add(new JsonPrimitive(1));
        longListJson.add(new JsonPrimitive(2l));
        obj.add("longList", longListJson);

        JsonArray doubleListJson = new JsonArray();
        doubleListJson.add(new JsonPrimitive(1.3));
        doubleListJson.add(new JsonPrimitive(1.4));
        obj.add("doubleList", doubleListJson);

        JsonArray rawDataJson = new JsonArray();
        rawDataJson.add(new JsonPrimitive(0));
        rawDataJson.add(new JsonPrimitive(1));
        obj.add("rawData", rawDataJson);

        SampleEntity se = new SampleEntity();
        se.fromJson(obj);

        Assert.assertEquals(new Long(1), se.getId());
        Assert.assertEquals("Erick", se.getName());
        Assert.assertEquals(new Integer(24), se.getAge());
        Assert.assertTrue(se.getCool());

        List<String> games = se.getGames();
        Assert.assertEquals(2, games.size());
        Assert.assertEquals("Assassins Creed", games.get(0));
        Assert.assertEquals("Battlefield", games.get(1));

        List<Boolean> booleanList = se.getBooleanList();
        Assert.assertEquals(2, booleanList.size());
        Assert.assertEquals(true, booleanList.get(0));
        Assert.assertEquals(false, booleanList.get(1));

        List<Integer> integerList = se.getIntegerList();
        Assert.assertEquals(2, integerList.size());
        Assert.assertEquals(new Integer(1), integerList.get(0));
        Assert.assertEquals(new Integer(2), integerList.get(1));

        List<Long> longList = se.getLongList();
        Assert.assertEquals(2, longList.size());
        Assert.assertEquals(new Integer(1), longList.get(0));
        Assert.assertEquals(new Long(2), longList.get(1));

        List<Double> doubleList = se.getDoubleList();
        Assert.assertEquals(2, doubleList.size());
        Assert.assertEquals(1.3, doubleList.get(0), 0);
        Assert.assertEquals(1.4, doubleList.get(1), 0);

        List<Byte> rawData = se.getRawData();
        Assert.assertEquals(2, rawData.size());
        Assert.assertEquals(new Integer(0), rawData.get(0));
        Assert.assertEquals(new Integer(1), rawData.get(1));
    }
}
