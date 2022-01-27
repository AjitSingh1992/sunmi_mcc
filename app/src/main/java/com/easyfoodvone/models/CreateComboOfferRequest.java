package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateComboOfferRequest
{
    @Expose
    @SerializedName("user_id")
    String user_id;

    @Expose
    @SerializedName("start_date")
    String start_date;

    @Expose
    @SerializedName("combo_total")
    String combo_total;

    @Expose
    @SerializedName("discount_percentage")
    String discount_percentage;

    @Expose
    @SerializedName("main_product_id")
    String main_product_id;

    @Expose
    @SerializedName("spend_amount_to_avail_offer")
    String spend_amount_to_avail_offer;

    @Expose
    @SerializedName("discount_amount")
    String discount_amount;

    @Expose
    @SerializedName("offer_description")
    String offer_description;

    @Expose
    @SerializedName("offer_title")
    String offer_title;

    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;

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
    @SerializedName("terms_conditions")
    String terms_conditions;

    @Expose
    @SerializedName("offer_details")
    String offer_details;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getCombo_total() {
        return combo_total;
    }

    public void setCombo_total(String combo_total) {
        this.combo_total = combo_total;
    }

    public String getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(String discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getMain_product_id() {
        return main_product_id;
    }

    public void setMain_product_id(String main_product_id) {
        this.main_product_id = main_product_id;
    }

    public String getSpend_amount_to_avail_offer() {
        return spend_amount_to_avail_offer;
    }

    public void setSpend_amount_to_avail_offer(String spend_amount_to_avail_offer) {
        this.spend_amount_to_avail_offer = spend_amount_to_avail_offer;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getOffer_description() {
        return offer_description;
    }

    public void setOffer_description(String offer_description) {
        this.offer_description = offer_description;
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

    public String getTerms_conditions() {
        return terms_conditions;
    }

    public void setTerms_conditions(String terms_conditions) {
        this.terms_conditions = terms_conditions;
    }

    public String getOffer_details() {
        return offer_details;
    }

    public void setOffer_details(String offer_details) {
        this.offer_details = offer_details;
    }

    @Override
    public String toString() {
        return "CreateComboOfferRequest{" +
                "user_id='" + user_id + '\'' +
                ", start_date='" + start_date + '\'' +
                ", combo_total='" + combo_total + '\'' +
                ", discount_percentage='" + discount_percentage + '\'' +
                ", main_product_id='" + main_product_id + '\'' +
                ", spend_amount_to_avail_offer='" + spend_amount_to_avail_offer + '\'' +
                ", discount_amount='" + discount_amount + '\'' +
                ", offer_description='" + offer_description + '\'' +
                ", offer_title='" + offer_title + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", end_date='" + end_date + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", days_available='" + days_available + '\'' +
                ", available_for='" + available_for + '\'' +
                ", offer_image='" + offer_image + '\'' +
                ", terms_conditions='" + terms_conditions + '\'' +
                ", offer_details='" + offer_details + '\'' +
                '}';
    }
}
