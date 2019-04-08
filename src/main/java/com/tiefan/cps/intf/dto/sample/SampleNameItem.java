package com.tiefan.cps.intf.dto.sample;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

/**
 * Sample Dto
 * Created by chengyao on 2017/7/20.
 */
public class SampleNameItem {

    private Integer id;

    private String name;

    public SampleNameItem() {

    }

    public SampleNameItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public boolean isEmpty() {
        return this.id == null;
    }

    public static SampleNameItem empty() {
        return new SampleNameItem();
    }

    public static SampleNameItem random() {
        return new SampleNameItem(RandomUtils.nextInt(0, 100), RandomStringUtils.randomAlphanumeric(10));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SampleNameItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
