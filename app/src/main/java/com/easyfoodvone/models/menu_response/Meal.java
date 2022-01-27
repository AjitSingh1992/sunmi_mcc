package com.easyfoodvone.models.menu_response;

import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Meal {
    @Expose
    int menuId;
    @Expose
    int menuSubCatId;

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    String id = null;
    @SerializedName("mealId")
    @Expose
    private String mealId;
    @SerializedName("mealName")
    @Expose
    private String mealName;
    @SerializedName("mealPrice")
    @Expose
    private String mealPrice;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("vegType")
    @Expose
    private String vegType;
    @SerializedName("mealImage")
    @Expose
    private String mealImage;
    @SerializedName("isMeal")
    @Expose
    private int isMeal;
    @SerializedName("mealCategories")
    @Expose
    private List<MealCategory> mealCategories = null;
    @Expose
    private String amount;
    @Expose
    private Integer quantity;
    @Expose
    private Integer originalQuantity;
    @Expose
    private Double originalAmount1;
    public Meal() {
    }

    public Meal(int menuId, int menuSubCatId, @NonNull String id, String mealId, String mealName, String mealPrice, String description, String vegType, String mealImage, int isMeal, List<MealCategory> mealCategories) {
        this.menuId = menuId;
        this.menuSubCatId = menuSubCatId;
        this.id = id;
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealPrice = mealPrice;
        this.description = description;
        this.vegType = vegType;
        this.mealImage = mealImage;
        this.isMeal = isMeal;
        this.mealCategories = mealCategories;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getMenuSubCatId() {
        return menuSubCatId;
    }

    public void setMenuSubCatId(int menuSubCatId) {
        this.menuSubCatId = menuSubCatId;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(String mealPrice) {
        this.mealPrice = mealPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVegType() {
        return vegType;
    }

    public void setVegType(String vegType) {
        this.vegType = vegType;
    }

    public String getMealImage() {
        return mealImage;
    }

    public void setMealImage(String mealImage) {
        this.mealImage = mealImage;
    }

    public int getIsMeal() {
        return isMeal;
    }

    public void setIsMeal(int isMeal) {
        this.isMeal = isMeal;
    }

    public List<MealCategory> getMealCategories() {
        return mealCategories;
    }

    public void setMealCategories(List<MealCategory> mealCategories) {
        this.mealCategories = mealCategories;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(Integer originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public Double getOriginalAmount1() {
        return originalAmount1;
    }

    public void setOriginalAmount1(Double originalAmount1) {
        this.originalAmount1 = originalAmount1;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "menuId=" + menuId +
                ", menuSubCatId=" + menuSubCatId +
                ", id='" + id + '\'' +
                ", mealId='" + mealId + '\'' +
                ", mealName='" + mealName + '\'' +
                ", mealPrice='" + mealPrice + '\'' +
                ", description='" + description + '\'' +
                ", vegType='" + vegType + '\'' +
                ", mealImage='" + mealImage + '\'' +
                ", isMeal=" + isMeal +
                ", mealCategories=" + mealCategories +
                ", amount='" + amount + '\'' +
                ", quantity=" + quantity +
                ", originalQuantity=" + originalQuantity +
                ", originalAmount1=" + originalAmount1 +
                '}';
    }
}