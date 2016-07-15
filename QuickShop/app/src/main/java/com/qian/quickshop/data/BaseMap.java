package com.qian.quickshop.data;

import java.util.HashMap;

/**
 * Created by Qian.
 */

/**
 * Store Map information of different stores.
 */
public class BaseMap {
    /**
     * HashMap is used to hold the map information.
     * key: item name
     * value: asile number
     */
    private HashMap<String, Integer> map;

    public BaseMap() {
        map = new HashMap<>();
    }


    /**
     * Load the base based on shop number
     */
    public HashMap<String, Integer> getBaseMap(String shopNum) {
        switch (shopNum) {
            case "0": {
                map.clear();
                map.put("sandwich", 5);
                map.put("cookies", 1);
                map.put("donut", 1);
                map.put("egg", 5);
                map.put("cheese", 6);
                map.put("cream", 6);
                map.put("milk", 5);
                map.put("yogurt", 7);
                map.put("juice", 8);
                map.put("apple", 2);
                map.put("orange", 2);
                map.put("banana", 2);
                map.put("cake", 1);
                map.put("bread", 1);
                map.put("potato", 3);
                map.put("tomato", 3);
                map.put("pepper", 3);
                map.put("beef", 4);
                map.put("chicken",4);
                map.put("turkey", 4);
                map.put("towel", 9);
                map.put("coke", 12);
                map.put("chips", 11);
            }
            case "1": {
                map.clear();
                map.put("egg", 2);
                map.put("cheese", 3);
                map.put("cream", 3);
                map.put("milk", 2);
                map.put("yogurt", 12);
                map.put("juice", 12);
                map.put("apple", 5);
                map.put("orange", 5);
                map.put("banana", 5);
                map.put("cake", 4);
                map.put("bread", 4);
                map.put("sandwich", 5);
                map.put("potato", 6);
                map.put("tomato", 6);
                map.put("pepper", 6);
                map.put("beef", 8);
                map.put("chicken",8);
                map.put("turkey", 8);
                map.put("towel", 7);
                map.put("coke", 22);
                map.put("chips", 13);
            }
            default: {}

        }

        return map;
    }
}
