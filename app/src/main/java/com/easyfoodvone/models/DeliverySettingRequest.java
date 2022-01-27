package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliverySettingRequest {
    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;

    @Expose
    @SerializedName("Average_delivery_time")
    String Average_delivery_time;

    @Expose
    @SerializedName("distance")
    String distance;

    @Expose
    @SerializedName("delivery_charges")
    String delivery_charges;

    @Expose
    @SerializedName("min_order_value")
    String min_order_value;

    @Expose
    @SerializedName("free_delivery")
    String free_delivery;

    @Expose
    @SerializedName("avg_preparation_time")
    String avg_preparation_time;

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getAverage_delivery_time() {
        return Average_delivery_time;
    }

    public void setAverage_delivery_time(String average_delivery_time) {
        Average_delivery_time = average_delivery_time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    public String getMin_order_value() {
        return min_order_value;
    }

    public void setMin_order_value(String min_order_value) {
        this.min_order_value = min_order_value;
    }

    public String getFree_delivery() {
        return free_delivery;
    }

    public void setFree_delivery(String free_delivery) {
        this.free_delivery = free_delivery;
    }

    public String getAvg_preparation_time() {
        return avg_preparation_time;
    }

    public void setAvg_preparation_time(String avg_preparation_time) {
        this.avg_preparation_time = avg_preparation_time;
    }

    @Override
    public String toString() {
        return "DeliverySettingRequest{" +
                "restaurant_id='" + restaurant_id + '\'' +
                ", Average_delivery_time='" + Average_delivery_time + '\'' +
                ", distance='" + distance + '\'' +
                ", delivery_charges='" + delivery_charges + '\'' +
                ", min_order_value='" + min_order_value + '\'' +
                ", free_delivery='" + free_delivery + '\'' +
                ", avg_preparation_time='" + avg_preparation_time + '\'' +
                '}';
    }
}
