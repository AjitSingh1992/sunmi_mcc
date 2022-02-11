package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliverySettingRequest {
    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;

    @Expose
    @SerializedName("delivery_travel_time")
    String delivery_travel_time;

    public String getDelivery_travel_time() {
        return delivery_travel_time;
    }

    public void setDelivery_travel_time(String delivery_travel_time) {
        this.delivery_travel_time = delivery_travel_time;
    }

    @Expose
    @SerializedName("type")
    String type;

    @Expose
    @SerializedName("prepration_time_quite")
    String prepration_time_quite;

    public String getPrepration_time_quite() {
        return prepration_time_quite;
    }

    public void setPrepration_time_quite(String prepration_time_quite) {
        this.prepration_time_quite = prepration_time_quite;
    }

    public String getPrepration_time_busy() {
        return prepration_time_busy;
    }

    public void setPrepration_time_busy(String prepration_time_busy) {
        this.prepration_time_busy = prepration_time_busy;
    }

    @Expose
    @SerializedName("prepration_time_normal")
    String prepration_time_normal;

   @Expose
    @SerializedName("prepration_time_busy")
    String prepration_time_busy;



    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrepration_time_normal() {
        return prepration_time_normal;
    }

    public void setPrepration_time_normal(String prepration_time_normal) {
        this.prepration_time_normal = prepration_time_normal;
    }



    @Override
    public String toString() {
        return "";
    }
}
