package com.easyfoodvone.models.menu_response;

import com.easyfoodvone.models.OrderRequest;
import com.easyfoodvone.models.OrderRequestForItem;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemSwipeModel implements Serializable {


    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;

    @Expose
    @SerializedName("order")
    ArrayList<OrderRequestForItem> order;

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public ArrayList<OrderRequestForItem> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<OrderRequestForItem> order) {
        this.order = order;
    }
}
