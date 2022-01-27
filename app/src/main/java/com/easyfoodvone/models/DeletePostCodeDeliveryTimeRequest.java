package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeletePostCodeDeliveryTimeRequest {
    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;

    @Expose
    @SerializedName("post_code")
    String post_code;

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    @Override
    public String toString() {
        return "DeletePostCodeDeliveryTimeRequest{" +
                "restaurant_id='" + restaurant_id + '\'' +
                ", post_code='" + post_code + '\'' +
                '}';
    }
}
