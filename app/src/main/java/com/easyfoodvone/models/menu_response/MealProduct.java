package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealProduct {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("mealId")
    @Expose
    private String mealId;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("productSizeId")
    @Expose
    private String productSizeId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productSizeName")
    @Expose
    private String productSizeName;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("menuProductSize")
    @Expose
    private List<MenuProductSize> menuProductSize;
    @SerializedName("productModifiers")
    @Expose
    private List<ProductModifier> productModifiers ;

    @Expose
    public Boolean isSelected = false;

    public MealProduct() {
    }

    public MealProduct(String id, String mealId, String productId, String productSizeId, String productName, String productSizeName, int quantity) {
        this.id = id;
        this.mealId = mealId;
        this.productId = productId;
        this.productSizeId = productSizeId;
        this.productName = productName;
        this.productSizeName = productSizeName;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(String productSizeId) {
        this.productSizeId = productSizeId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSizeName() {
        return productSizeName;
    }

    public void setProductSizeName(String productSizeName) {
        this.productSizeName = productSizeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<MenuProductSize> getMenuProductSize() {
        return menuProductSize;
    }

    public void setMenuProductSize(List<MenuProductSize> menuProductSize) {
        this.menuProductSize = menuProductSize;
    }

    public List<ProductModifier> getProductModifiers() {
        return productModifiers;
    }

    public void setProductModifiers(List<ProductModifier> productModifiers) {
        this.productModifiers = productModifiers;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    @Override
    public String toString() {
        return "MealProduct{" +
                "id='" + id + '\'' +
                ", mealId='" + mealId + '\'' +
                ", productId='" + productId + '\'' +
                ", productSizeId='" + productSizeId + '\'' +
                ", productName='" + productName + '\'' +
                ", productSizeName='" + productSizeName + '\'' +
                ", quantity=" + quantity +
                ", menuProductSize=" + menuProductSize +
                ", productModifiers=" + productModifiers +
                ", isSelected=" + isSelected +
                '}';
    }
}
