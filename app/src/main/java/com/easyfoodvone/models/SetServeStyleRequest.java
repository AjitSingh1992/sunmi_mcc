package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SetServeStyleRequest {
    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;
    @Expose
    @SerializedName("collection")
    String collection;
    @Expose
    @SerializedName("delivery")
    String delivery;
    @Expose
    @SerializedName("dine_in")
    String dine_in;


    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDine_in() {
        return dine_in;
    }

    public void setDine_in(String dine_in) {
        this.dine_in = dine_in;
    }
}
