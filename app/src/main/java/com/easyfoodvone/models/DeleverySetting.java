package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleverySetting {

    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String tostring(){
        return "GetDeleverySetting{"+
                "restaurant_id=" + restaurant_id + '\'' +
                '}';
    }


}
