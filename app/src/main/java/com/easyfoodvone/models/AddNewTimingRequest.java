package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddNewTimingRequest
{
    @Expose
    @SerializedName("user_id")
    String user_id;
    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;
    @Expose
    @SerializedName("open_close")
    String open_close;
    @Expose
    @SerializedName("open_close_time")
    String open_close_time;
    @Expose
    @SerializedName("delivery_time")
    String delivery_time;
    @Expose
    @SerializedName("collection_time")
    String collection_time;
    @Expose
    @SerializedName("day")
    String day;
    @Expose
    @SerializedName("id")
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getOpen_close() {
        return open_close;
    }

    public void setOpen_close(String open_close) {
        this.open_close = open_close;
    }

    public String getOpen_close_time() {
        return open_close_time;
    }

    public void setOpen_close_time(String open_close_time) {
        this.open_close_time = open_close_time;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getCollection_time() {
        return collection_time;
    }

    public void setCollection_time(String collection_time) {
        this.collection_time = collection_time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "AddNewTimingRequest{" +
                "user_id='" + user_id + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", open_close='" + open_close + '\'' +
                ", open_close_time='" + open_close_time + '\'' +
                ", delivery_time='" + delivery_time + '\'' +
                ", collection_time='" + collection_time + '\'' +
                ", day='" + day + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
