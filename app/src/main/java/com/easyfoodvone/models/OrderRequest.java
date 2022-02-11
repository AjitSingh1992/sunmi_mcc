package com.easyfoodvone.models;

public class OrderRequest {

    String position;
    String Category_id;

    public OrderRequest(String position, String category_id) {
        this.position = position;
        this.Category_id = category_id;
    }
}
