package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateMenuProductRequest
{
    @Expose
    @SerializedName("menu_product_id")
    String menu_product_id;

    @Expose
    @SerializedName("menu_product_name")
    String menu_product_name;

    @Expose
    @SerializedName("menu_product_price")
    String menu_product_price;
    @Expose
    @SerializedName("menu_status")
    String menu_status;


    public String getMenu_category_id() {
        return menu_product_id;
    }

    public void setMenu_category_id(String menu_category_id) {
        this.menu_product_id = menu_category_id;
    }

    public String getMenu_product_name() {
        return menu_product_name;
    }

    public void setMenu_product_name(String menu_product_name) {
        this.menu_product_name = menu_product_name;
    }

    public String getMenu_product_price() {
        return menu_product_price;
    }

    public void setMenu_product_price(String menu_product_price) {
        this.menu_product_price = menu_product_price;
    }

    public String getMenu_status() {
        return menu_status;
    }

    public void setMenu_status(String menu_status) {
        this.menu_status = menu_status;
    }

    @Override
    public String toString() {
        return "UpdateMenuProductRequest{" +
                "menu_category_id='" + menu_product_id + '\'' +
                ", menu_product_name='" + menu_product_name + '\'' +
                ", menu_product_price='" + menu_product_price + '\'' +
                ", menu_status='" + menu_status + '\'' +
                '}';
    }
}
