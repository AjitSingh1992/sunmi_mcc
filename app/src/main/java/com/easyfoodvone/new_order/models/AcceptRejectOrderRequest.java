package com.easyfoodvone.new_order.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcceptRejectOrderRequest
{
    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;
    @Expose
    @SerializedName("customer_id")
    String customer_id;
    @Expose
    @SerializedName("order_number")
    String order_number;
    @Expose
    @SerializedName("order_response")
    String order_response;
    @Expose
    @SerializedName("delivery_time")
    String delivey_time;
    @Expose
    @SerializedName("delivery_date_time")
    String delivey_date_time;
    @Expose
    @SerializedName("order_reject_note")
    String notes;

    @Expose
    @SerializedName("order_status")
    String orderStatus;




    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }



    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }




    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_response() {
        return order_response;
    }

    public void setOrder_response(String order_response) {
        this.order_response = order_response;
    }

    public String getDelivey_time() {
        return delivey_time;
    }

    public void setDelivey_time(String delivey_time) {
        this.delivey_time = delivey_time;
    }

    public String getDelivey_date_time() {
        return delivey_date_time;
    }

    public void setDelivey_date_time(String delivey_date_time) {
        this.delivey_date_time = delivey_date_time;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "AcceptRejectOrderRequest{" +
                "restaurant_id='" + restaurant_id + '\'' +
                ", customer_id='" + customer_id + '\'' +
                ", order_number='" + order_number + '\'' +
                ", order_response='" + order_response + '\'' +
                ", delivey_time='" + delivey_time + '\'' +
                ", delivey_date_time='" + delivey_date_time + '\'' +
                ", notes='" + notes + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}