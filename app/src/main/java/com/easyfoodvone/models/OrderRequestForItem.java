package com.easyfoodvone.models;

public class OrderRequestForItem {

    String position;
    String menu_id;

    public OrderRequestForItem(String position, String menu_id) {
        this.position = position;
        this.menu_id = menu_id;
    }
}
