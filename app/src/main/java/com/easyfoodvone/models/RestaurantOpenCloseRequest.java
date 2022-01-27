package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RestaurantOpenCloseRequest implements Serializable
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
    @SerializedName("device_id")
    String device_id;
    @Expose
    @SerializedName("serve_style")
    String serve_style;

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

    public void setOpen_close(String  open_close) {
        this.open_close = open_close;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getServe_style() {
        return serve_style;
    }

    public void setServe_style(String serve_style) {
        this.serve_style = serve_style;
    }

    @Override
    public String toString() {
        return "RestaurantOpenCloseRequest{" +
                "user_id='" + user_id + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", open_close='" + open_close + '\'' +
                ", device_id='" + device_id + '\'' +
                '}';
    }
}
