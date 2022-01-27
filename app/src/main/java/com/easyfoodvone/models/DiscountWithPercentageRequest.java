package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiscountWithPercentageRequest {
    @Expose
    @SerializedName("user_id")
    String user_id;
    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;
    @Expose
    @SerializedName("id")
    String id;
    @Expose
    @SerializedName("offer_title")
    String offer_title;
    @Expose
    @SerializedName("offer_description")
    String offer_description;
    @Expose
    @SerializedName("discount_amount")
    String discount_amount;
    @Expose
    @SerializedName("max_discount_amount")
    String max_discount_amount;
    @Expose
    @SerializedName("show_in_swipe")
    String show_in_swipe;
    @Expose
    @SerializedName("spend_amount_to_avail_offer")
    String spend_amount_to_avail_offer;
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
    @SerializedName("easyfood_share")
    String easyfood_share;
    @SerializedName("franchise_share")
    String franchise_share;
    @SerializedName("restaurant_share")
    String restaurant_share;
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
    @SerializedName("usage_per_customer")
    String usage_per_customer;

    public String getUsage_per_customer() {
        return usage_per_customer;
    }

    public void setUsage_per_customer(String usage_per_customer) {
        this.usage_per_customer = usage_per_customer;
    }

    public String getUsage_total_usage() {
        return usage_total_usage;
    }

    public void setUsage_total_usage(String usage_total_usage) {
        this.usage_total_usage = usage_total_usage;
    }

    public String getUser_app() {
        return user_app;
    }

    public void setUser_app(String user_app) {
        this.user_app = user_app;
    }

    @SerializedName("usage_total_usage")
    String usage_total_usage;
    @SerializedName("user_app")
    String user_app;


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOffer_details() {
        return offer_details;
    }

    public void setOffer_details(String offer_details) {
        this.offer_details = offer_details;
    }

    public String getOffer_title() {
        return offer_title;
    }

    public void setOffer_title(String offer_title) {
        this.offer_title = offer_title;
    }

    public String getOffer_description() {
        return offer_description;
    }

    public void setOffer_description(String offer_description) {
        this.offer_description = offer_description;
    }

    public String getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(String discount_amount) {
        this.discount_amount = discount_amount;
    }

    public String getSpend_amount_to_avail_offer() {
        return spend_amount_to_avail_offer;
    }

    public void setSpend_amount_to_avail_offer(String spend_amount_to_avail_offer) {
        this.spend_amount_to_avail_offer = spend_amount_to_avail_offer;
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

    public String getTerms_conditions() {
        return terms_conditions;
    }

    public void setTerms_conditions(String terms_conditions) {
        this.terms_conditions = terms_conditions;
    }

    public String getMax_discount_amount() {
        return max_discount_amount;
    }

    public void setMax_discount_amount(String max_discount_amount) {
        this.max_discount_amount = max_discount_amount;
    }

    public String getEasyfood_share() {
        return easyfood_share;
    }

    public void setEasyfood_share(String easyfood_share) {
        this.easyfood_share = easyfood_share;
    }

    public String getFranchise_share() {
        return franchise_share;
    }

    public void setFranchise_share(String franchise_share) {
        this.franchise_share = franchise_share;
    }

    public String getRestaurant_share() {
        return restaurant_share;
    }

    public void setRestaurant_share(String restaurant_share) {
        this.restaurant_share = restaurant_share;
    }

    public String getShow_in_swipe() {
        return show_in_swipe;
    }

    public void setShow_in_swipe(String show_in_swipe) {
        this.show_in_swipe = show_in_swipe;
    }

    public String getOfferDetail() {
        return offer_details;
    }

    public void setOfferDetail(String offerDetail) {
        this.offer_details = offerDetail;
    }

    public static class OfferDetail {
        String offer_details;

        public String getOfferDetail() {
            return offer_details;
        }

        @Override
        public String toString() {
            return "OfferDetail{" +
                    "offer_details='" + offer_details + '\'' +
                    '}';
        }

        public void setOfferDetail(String offerDetail) {
            this.offer_details = offerDetail;


        }
    }

    @Override
    public String toString() {
        return "DiscountWithPercentageRequest{" +
                "user_id='" + user_id + '\'' +
                ", restaurant_id='" + restaurant_id + '\'' +
                ", id='" + id + '\'' +
                ", offer_title='" + offer_title + '\'' +
                ", offer_description='" + offer_description + '\'' +
                ", discount_amount='" + discount_amount + '\'' +
                ", show_in_swipe='" + show_in_swipe + '\'' +
                ", max_discount_amount='" + max_discount_amount + '\'' +
                ", spend_amount_to_avail_offer='" + spend_amount_to_avail_offer + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", usage_per_customer='" + usage_per_customer + '\'' +
                ", usage_total_usage='" + usage_total_usage + '\'' +
                ", user_app='" + user_app + '\'' +
                ", easyfood_share='" + easyfood_share + '\'' +
                ", restaurant_share='" + restaurant_share + '\'' +
                ", franchise_share='" + franchise_share + '\'' +
                ", days_available='" + days_available + '\'' +
                ", available_for='" + available_for + '\'' +
                ", offer_image='" + offer_image + '\'' +
                ", terms_conditions='" + terms_conditions + '\'' +
                ", offer_details=" + offer_details +
                '}';
    }
}
