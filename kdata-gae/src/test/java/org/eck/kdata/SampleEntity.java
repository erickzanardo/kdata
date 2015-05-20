package org.eck.kdata;

import java.util.List;

import org.eck.kdata.KEntity;
import org.eck.kdata.annotations.KField;
import org.eck.kdata.annotations.KId;

public class SampleEntity extends KEntity {

    @KId
    private Long id;

    @KField
    private String name;

    @KField
    private Integer age;

    @KField
    private Boolean cool;

    @KField
    private Double money;

    @KField
    private List<String> games;

    @KField
    private List<Boolean> booleanList;

    @KField
    private List<Integer> integerList;

    @KField
    private List<Long> longList;

    @KField
    private List<Double> doubleList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getCool() {
        return cool;
    }

    public void setCool(Boolean cool) {
        this.cool = cool;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public List<String> getGames() {
        return games;
    }

    public void setGames(List<String> games) {
        this.games = games;
    }

    public List<Boolean> getBooleanList() {
        return booleanList;
    }

    public void setBooleanList(List<Boolean> booleanList) {
        this.booleanList = booleanList;
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }

    public void setIntegerList(List<Integer> integerList) {
        this.integerList = integerList;
    }

    public List<Long> getLongList() {
        return longList;
    }

    public void setLongList(List<Long> longList) {
        this.longList = longList;
    }

    public List<Double> getDoubleList() {
        return doubleList;
    }

    public void setDoubleList(List<Double> doubleList) {
        this.doubleList = doubleList;
    }
}
