package com.easyfoodvone.new_order.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Options {
    @SerializedName("mealProducts")
    @Expose
    private List<MealProduct> mealProducts = null;
    @SerializedName("size")
    @Expose
    private Size size;
    @SerializedName("productModifiers")
    @Expose
    private List<ProductModifier> productModifiers = null;

    public List<MealProduct> getMealProducts() {
        return mealProducts;
    }

    public void setMealProducts(List<MealProduct> mealProducts) {
        this.mealProducts = mealProducts;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public List<ProductModifier> getProductModifiers() {
        return productModifiers;
    }

    public void setProductModifiers(List<ProductModifier> productModifiers) {
        this.productModifiers = productModifiers;
    }
}
