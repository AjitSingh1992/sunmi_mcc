package com.easyfoodvone.new_order.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsRequest
{
    @Expose
    @SerializedName("order_number")
    String order_number;

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    @Override
    public String toString() {
        return "OrderDetailsRequest{" +
                "order_number='" + order_number + '\'' +
                '}';
    }
}
