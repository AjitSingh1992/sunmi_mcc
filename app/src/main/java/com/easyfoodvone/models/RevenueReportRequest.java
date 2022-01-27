package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevenueReportRequest {
    @Expose
    @SerializedName("user_id")
    String user_id;


    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;


    @Expose
    @SerializedName("from_date")
    String from_date;


    @Expose
    @SerializedName("end_date")
    String end_date;
    @Expose
    @SerializedName("date")
    String date;


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

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RevenueReportRequest{" +
                "user_id='" + user_id + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", from_date='" + from_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
