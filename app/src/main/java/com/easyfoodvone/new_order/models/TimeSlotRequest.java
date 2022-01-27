package com.easyfoodvone.new_order.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSlotRequest {
    @SerializedName("restaurant_id")
    @Expose
    private String restaurant_id;

    @SerializedName("date_time")
    @Expose
    private String date_time;

    public TimeSlotRequest(String restaurant_id, String date_time) {
        this.restaurant_id = restaurant_id;
        this.date_time = date_time;
    }
}
