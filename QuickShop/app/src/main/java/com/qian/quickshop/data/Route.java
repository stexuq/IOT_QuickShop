package com.qian.quickshop.data;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Qian on 4/6/16.
 */
public class Route {
    private HashMap<String, Integer> map;
    private HashMap<String, Integer> onSaleMap;

    public Route() {
        // initialize the shop map and the on sale items
        map = new HashMap<>();
        onSaleMap = new HashMap<>();
    }

    public void initShopMap(HashMap<String, Integer> m) {
        Set<String> keySet = m.keySet();

        for (String key : keySet) {
            Integer v = m.get(key);
            map.put(key, v);
        }
    }

    public void initOnSaleMap(HashMap<String, Integer> m) {
        Set<String> keySet = m.keySet();

        for (String key : keySet) {
            Integer v = m.get(key);
            onSaleMap.put(key, v);
        }
    }


    /**
     *
     * @param shoppinglist the shopping list array entered by user
     * @return an Array of <item><aisle number> Pair
     */
    public Pair[] getRoute(String[] shoppinglist) {
        Pair[] res = new Pair[shoppinglist.length];
        // if can be find, set bundle aisle number greater than 0
        int len = shoppinglist.length;
        for (int i = 0; i < len; i++) {
            String s = shoppinglist[i];
            if (map.containsKey(s)) {
                Pair p = new Pair(s, map.get(s));
                res[i] = p;
            } else {
                Pair p = new Pair(s, -1);
                res[i] = p;
            }
        }

        Arrays.sort(res);

        return res;
    }


    /**
     *
     * @param set The HashSet of aisle number that user will visit
     * @return the on sale item <item><aisle number> pair
     */
    public Pair[] getOnSaleRoute(Set<Integer> set) {

        ArrayList<Pair> al = new ArrayList<>();

        for (String key : onSaleMap.keySet()) {
            for (Integer i : set) {
                if (onSaleMap.get(key).equals(i)) {
                    Pair p = new Pair(key, i);
                    al.add(p);
                }
            }
        }
        int len = al.size();
        Pair[] r = new Pair[len];
        for (int i = 0; i < len; i++) {
            r[i] = al.get(i);
        }

        Arrays.sort(r);

        return r;

    }

}
