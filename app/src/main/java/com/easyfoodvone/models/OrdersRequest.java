package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrdersRequest implements Serializable {
    public final static transient String STATUS_NEW = "new";
    public final static transient String STATUS_ACCEPTED = "accepted";
    public final static transient String STATUS_REJECTED = "rejected";

    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;
    @Expose
    @SerializedName("status")
    String status;
    @Expose
    @SerializedName("offset")
    String offset;
    @Expose
    @SerializedName("limit")
    String limit;

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "OrdersRequest{" +
                "restaurant_id='" + restaurant_id + '\'' +
                ", status='" + status + '\'' +
                ", offset='" + offset + '\'' +
                ", limit='" + limit + '\'' +
                '}';
    }
}
