package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpendXgetXdiscountRequest {
    @Expose
    @SerializedName("user_id")
    String user_id;

    @Expose
    @SerializedName("offer_title")
    String offer_title;

    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;

    @Expose
    @SerializedName("terms_conditions")
    String terms_conditions;

    @Expose
    @SerializedName("start_date")
    String start_date;

    @Expose
    @SerializedName("end_date")
    String end_date;

    @Expose
    @SerializedName("start_time")
    String start_time;

    @Expose
    @SerializedName("end_time")
    String end_time;

    @Expose
    @SerializedName("days_available")
    String days_available;

    @Expose
    @SerializedName("available_for")
    String available_for;

    @Expose
    @SerializedName("offer_image")
    String offer_image;

    @Expose
    @SerializedName("offer_description")
    String offer_details;
    @Expose
    @SerializedName("spendx")
    String spendx;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getTerms_conditions() {
        return terms_conditions;
    }

    public void setTerms_conditions(String terms_conditions) {
        this.terms_conditions = terms_conditions;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDays_available() {
        return days_available;
    }

    public void setDays_available(String days_available) {
        this.days_available = days_available;
    }

    public String getAvailable_for() {
        return available_for;
    }

    public void setAvailable_for(String available_for) {
        this.available_for = available_for;
    }

    public String getOffer_image() {
        return offer_image;
    }

    public void setOffer_image(String offer_image) {
        this.offer_image = offer_image;
    }

    public String getOffer_details() {
        return offer_details;
    }

    public void setOffer_details(String offer_details) {
        this.offer_details = offer_details;
    }


    public String getSpendx() {
        return spendx;
    }

    public void setSpendx(String spendx) {
        this.spendx = spendx;
    }

    @Override
    public String toString() {
        return "SpendXgetXdiscountRequest{" +
                "user_id='" + user_id + '\'' +
                ", offer_title='" + offer_title + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", terms_conditions='" + terms_conditions + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", days_available='" + days_available + '\'' +
                ", available_for='" + available_for + '\'' +
                ", offer_image='" + offer_image + '\'' +
                ", offer_details='" + offer_details + '\'' +
                ", spendx='" + spendx + '\'' +
                '}';
    }
}
