package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuCategory {

    @SerializedName("menuCategoryId")
    @Expose
    private String menuCategoryId;
    @SerializedName("menuCategoryName")
    @Expose
    private String menuCategoryName;
    @SerializedName("menuSubCategory")
    @Expose
    private List<MenuCategory> menuSubCategory = null;
    @SerializedName("menuProducts")
    @Expose
    private List<MenuProduct> menuProducts = null;
    @SerializedName("meal")
    @Expose
    List<Meal> meal;


    public MenuCategory(String menuCategoryId, String menuCategoryName, List<MenuCategory> menuSubCategory, List<MenuProduct> menuProducts, List<Meal> meal) {
        this.menuCategoryId = menuCategoryId;
        this.menuCategoryName = menuCategoryName;
        this.menuSubCategory = menuSubCategory;
        this.menuProducts = menuProducts;
        this.meal = meal;
    }

    public String getMenuCategoryId() {
        return menuCategoryId;
    }

    public void setMenuCategoryId(String menuCategoryId) {
        this.menuCategoryId = menuCategoryId;
    }

    public String getMenuCategoryName() {
        return menuCategoryName;
    }

    public void setMenuCategoryName(String menuCategoryName) {
        this.menuCategoryName = menuCategoryName;
    }

    public List<MenuCategory> getMenuSubCategory() {
        return menuSubCategory;
    }

    public void setMenuSubCategory(List<MenuCategory> menuSubCategory) {
        this.menuSubCategory = menuSubCategory;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<Meal> getMeal() {
        return meal;
    }

    public void setMeal(List<Meal> meal) {
        this.meal = meal;
    }

    @Override
    public String toString() {
        return "MenuCategory{" +
                "menuCategoryId='" + menuCategoryId + '\'' +
                ", menuCategoryName='" + menuCategoryName + '\'' +
                ", menuSubCategory=" + menuSubCategory +
                ", menuProducts=" + menuProducts +
                ", meal=" + meal +
                '}';
    }
}