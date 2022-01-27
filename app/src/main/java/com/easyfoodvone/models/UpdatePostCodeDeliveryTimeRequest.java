package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdatePostCodeDeliveryTimeRequest {

    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;

    @Expose
    @SerializedName("post_code")
    String post_code;

    @Expose
    @SerializedName("min_order_value")
    double min_order_value;

    @Expose
    @SerializedName("delivery_charges")
    double delivery_charges;

    @Expose
    @SerializedName("free_delivery_amount")
    double free_delivery_amount;

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

    public double getMin_order_value() {
        return min_order_value;
    }

    public void setMin_order_value(double min_order_value) {
        this.min_order_value = min_order_value;
    }

    public double getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(double delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    public double getFree_delivery_amount() {
        return free_delivery_amount;
    }

    public void setFree_delivery_amount(double free_delivery_amount) {
        this.free_delivery_amount = free_delivery_amount;
    }

    @Override
    public String toString() {
        return "UpdatePostCodeDeliveryTimeRequest{" +
                "restaurant_id='" + restaurant_id + '\'' +
                ", post_code='" + post_code + '\'' +
                ", min_order_value=" + min_order_value +
                ", delivery_charges=" + delivery_charges +
                ", free_delivery_amount=" + free_delivery_amount +
                '}';
    }
}
