package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonRequest {
    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;
    @Expose
    @SerializedName("menu_category_id")
    String menu_category_id;
    @Expose
    @SerializedName("menu_category_current_position")
    String menu_category_current_position;
    @Expose
    @SerializedName("menu_category_new_position")
    String menu_category_new_position;
    @Expose
    @SerializedName("menu_status")
    String menu_status;
    @Expose
    @SerializedName("menu_category_status")
    String menu_category_status;

    @Expose
    @SerializedName("menu_product_id")
    String menu_product_id;

    @Expose
    @SerializedName("user_id")
    String user_id;
    @Expose
    @SerializedName("id")
    String id;
    @Expose
    @SerializedName("message")
    String message;
    @Expose
    @SerializedName("menu_product_current_position")
    String menu_product_current_position;
    @Expose
    @SerializedName("menu_product_new_position")
    String menu_product_new_position;
    @Expose
    @SerializedName("status")
    String status;
    @Expose
    @SerializedName("offer_id")
    String offer_id;
    @Expose
    @SerializedName("offer_type")
    String offer_type;
    @Expose
    @SerializedName("filter")
    String filter;

    public String getFcm_id() {
        return fcm_id;
    }

    public void setFcm_id(String fcm_id) {
        this.fcm_id = fcm_id;
    }

    @Expose
    @SerializedName("fcm_id")
    String fcm_id;

    @Expose
    @SerializedName("is_permanent")
    String is_permanent;



    public String getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(String offer_type) {
        this.offer_type = offer_type;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getOffer_id() {
        return offer_id;
    }

    public void setOffer_id(String offer_id) {
        this.offer_id = offer_id;
    }

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMenu_product_id() {
        return menu_product_id;
    }

    public void setMenu_product_id(String menu_product_id) {
        this.menu_product_id = menu_product_id;
    }


    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getMenu_category_id() {
        return menu_category_id;
    }

    public void setMenu_category_id(String menu_category_id) {
        this.menu_category_id = menu_category_id;
    }

    public String getMenu_category_current_position() {
        return menu_category_current_position;
    }

    public void setMenu_category_current_position(String menu_category_current_position) {
        this.menu_category_current_position = menu_category_current_position;
    }

    public String getMenu_category_new_position() {
        return menu_category_new_position;
    }

    public void setMenu_category_new_position(String menu_category_new_position) {
        this.menu_category_new_position = menu_category_new_position;
    }

    public String getMenu_status() {
        return menu_status;
    }

    public void setMenu_status(String menu_status) {
        this.menu_status = menu_status;
    }

    public String getMenu_category_status() {
        return menu_category_status;
    }

    public void setMenu_category_status(String menu_category_status) {
        this.menu_category_status = menu_category_status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMenu_product_current_position() {
        return menu_product_current_position;
    }

    public void setMenu_product_current_position(String menu_product_current_position) {
        this.menu_product_current_position = menu_product_current_position;
    }

    public String getMenu_product_new_position() {
        return menu_product_new_position;
    }

    public void setMenu_product_new_position(String menu_product_new_position) {
        this.menu_product_new_position = menu_product_new_position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIs_permanent() {
        return is_permanent;
    }

    public void setIs_permanent(String is_permanent) {
        this.is_permanent = is_permanent;
    }

    @Override
    public String toString() {
        return "CommonRequest{" +
                "restaurant_id='" + restaurant_id + '\'' +
                ", menu_category_id='" + menu_category_id + '\'' +
                ", menu_category_current_position='" + menu_category_current_position + '\'' +
                ", menu_category_new_position='" + menu_category_new_position + '\'' +
                ", menu_status='" + menu_status + '\'' +
                ", menu_category_status='" + menu_category_status + '\'' +
                ", menu_product_id='" + menu_product_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", menu_product_current_position='" + menu_product_current_position + '\'' +
                ", menu_product_new_position='" + menu_product_new_position + '\'' +
                ", status='" + status + '\'' +
                ", is_permanent='" + is_permanent + '\'' +
                '}';
    }
}
