package com.qian.quickshop.data;

/**
 * Created by Qian on 4/6/16.
 */
public class ShoppingList {
    private String[] items;

    public ShoppingList(String str) {
        items = str.split(" ");
    }

    public String[] getShoppingList() {
        return items;
    }
}
