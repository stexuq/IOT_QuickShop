package com.qian.quickshop.data;

/**
 * Created by Qian.
 */


/**
 * Store the information on a <item name><aisle number> Pair
 */
public class Pair implements Comparable<Pair> {
    private Integer aisleNum;
    private String name;

    public Pair(String s, Integer n) {
        aisleNum = n;
        name = s;
    }

    public Pair() {
        aisleNum = -1;
        name = "";
    }

    public Integer getAisleNum() {
        return aisleNum;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAisleNum(Integer asileNum) {

        this.aisleNum = asileNum;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Pair another) {
        int i = this.aisleNum - another.aisleNum;
        if (i != 0) return i;

        return this.name.compareTo(another.name);
    }
}

