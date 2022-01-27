package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateMenuCategoryRequest
{
    @Expose
    @SerializedName("menu_category_id")
    String menu_category_id;

     @Expose
    @SerializedName("menu_category_name")
    String menu_category_name;

     @Expose
    @SerializedName("menu_category_status")
    String menu_category_status;


    public String getMenu_category_id() {
        return menu_category_id;
    }

    public void setMenu_category_id(String menu_category_id) {
        this.menu_category_id = menu_category_id;
    }

    public String getMenu_category_name() {
        return menu_category_name;
    }

    public void setMenu_category_name(String menu_category_name) {
        this.menu_category_name = menu_category_name;
    }

    public String getMenu_category_status() {
        return menu_category_status;
    }

    public void setMenu_category_status(String menu_category_status) {
        this.menu_category_status = menu_category_status;
    }

    @Override
    public String toString() {
        return "UpdateMenuCategoryRequest{" +
                "menu_category_id='" + menu_category_id + '\'' +
                ", menu_category_name='" + menu_category_name + '\'' +
                ", menu_category_status='" + menu_category_status + '\'' +
                '}';
    }
}
