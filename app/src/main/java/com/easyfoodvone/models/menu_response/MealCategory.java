package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealCategory {

    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("customizable")
    @Expose
    private int customizable;

    @SerializedName("products")
    @Expose
    private List<MealProduct> mealProducts = null;

    public MealCategory() {
    }


    public MealCategory(String categoryName, int quantity, int customizable, List<MealProduct> mealProducts) {
        this.categoryName = categoryName;
        this.quantity = quantity;
        this.customizable = customizable;
        this.mealProducts = mealProducts;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCustomizable() {
        return customizable;
    }

    public void setCustomizable(int customizable) {
        this.customizable = customizable;
    }

    public List<MealProduct> getMealProducts() {
        return mealProducts;
    }

    public void setMealProducts(List<MealProduct> mealProducts) {
        this.mealProducts = mealProducts;
    }

    @Override
    public String toString() {
        return "MealCategory{" +
                "categoryName='" + categoryName + '\'' +
                ", quantity=" + quantity +
                ", customizable=" + customizable +
                ", mealProducts=" + mealProducts +
                '}';
    }
}
