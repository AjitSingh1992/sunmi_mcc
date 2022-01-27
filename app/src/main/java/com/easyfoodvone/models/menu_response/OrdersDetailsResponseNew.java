package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrdersDetailsResponseNew {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("orders_details")
    @Expose
    private OrdersDetailsTop ordersDetails;

    public OrdersDetailsResponseNew(Boolean success, String message, OrdersDetailsTop ordersDetails) {
        this.success = success;
        this.message = message;
        this.ordersDetails = ordersDetails;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrdersDetailsTop getOrdersDetails() {
        return ordersDetails;
    }

    public void setOrdersDetails(OrdersDetailsTop ordersDetails) {
        this.ordersDetails = ordersDetails;
    }

    @Override
    public String toString() {
        return "OrdersDetailsResponseNew{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", ordersDetails=" + ordersDetails +
                '}';
    }
}