package com.qian.quickshop.data;

import java.util.HashMap;

/**
 * use to store the on sale items
 */
public class BaseOnSale {

    /**
     * HashMap is use to store the key-value pair of the on sale items
     * key: item name
     * value: asile number
     */
    private  HashMap<String, Integer> map;

    public BaseOnSale() {
        map = new HashMap<>();

    }

    public  HashMap<String, Integer> getBaseOnSaleMap(String shopNum) {
        switch (shopNum) {
            case "0": {
                map.clear();
                map.put("mushroom", 1);
                map.put("broccoli", 2);
                map.put("bacon", 3);
                map.put("cherry", 4);
                map.put("towel", 5);
                map.put("pencils",6);
                map.put("lock",7);
                map.put("cup",8);
                map.put("soy-sauce",9);
                map.put("coke",11);
                map.put("socks",12);
                map.put("ice-cream",13);
                map.put("wine", 14);
            }
            case "1": {
                map.clear();
                map.put("mushroom", 1);
                map.put("broccoli", 2);
                map.put("bacon", 3);
                map.put("cherry", 4);
                map.put("towel", 5);
                map.put("pencils",6);
                map.put("lock",7);
                map.put("cup",8);
                map.put("soy-sauce",9);
                map.put("coke",11);
                map.put("socks",12);
                map.put("ice-cream",14);
            }
            default: {}

        }

        return map;
    }
}