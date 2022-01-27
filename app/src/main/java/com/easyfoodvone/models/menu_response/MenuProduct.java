package com.easyfoodvone.models.menu_response;

import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class MenuProduct {
    @Expose
    String id;
    @Expose
    String menuId;
    @Expose
    String menuSubCatId;

    @PrimaryKey
    @NonNull
    @SerializedName("menuProductId")
    @Expose
    String menuProductId = null;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("vegType")
    @Expose
    private String vegType;
    @SerializedName("menuProductPrice")
    @Expose
    private String menuProductPrice;
    @SerializedName("userappProductImage")
    @Expose
    private String userappProductImage;
    @SerializedName("ecomProductImage")
    @Expose
    private String ecomProductImage;
    @SerializedName("productOverallRating")
    @Expose
    private String productOverallRating;
    @SerializedName("menuProductSize")
    @Expose
    private List<MenuProductSize> menuProductSize = null;
    @SerializedName("productModifiers")
    @Expose
    private List<ProductModifier> productModifiers = null;
    @SerializedName("mealProducts")
    @Expose
    private List<MealProduct> mealProducts = null;
    @Expose
    private String amount;
    @Expose
    private Integer quantity;
    @Expose
    private Integer originalQuantity;
    @Expose
    private Double originalAmount;
    @Expose
    private Double originalAmount1;

    public MenuProduct() {
    }

    public MenuProduct(String menuProductId, String productName, String description, String vegType, String menuProductPrice, String userappProductImage, String ecomProductImage, String productOverallRating, List<MenuProductSize> menuProductSize, List<ProductModifier> productModifiers, List<MealProduct> mealProducts,String amount, Integer quantity, Integer originalQuantity, Double originalAmount, Double originalAmount1) {
        this.menuProductId = menuProductId;
        this.productName = productName;
        this.description = description;
        this.vegType = vegType;
        this.menuProductPrice = menuProductPrice;
        this.userappProductImage = userappProductImage;
        this.ecomProductImage = ecomProductImage;
        this.productOverallRating = productOverallRating;
        this.menuProductSize = menuProductSize;
        this.productModifiers = productModifiers;
        this.mealProducts = mealProducts;
        this.amount = amount;
        this.quantity = quantity;
        this.originalQuantity = originalQuantity;
        this.originalAmount = originalAmount;
        this.originalAmount1 = originalAmount1;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getMenuSubCatId() {
        return menuSubCatId;
    }

    public void setMenuSubCatId(String menuSubCatId) {
        this.menuSubCatId = menuSubCatId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMenuProductId() {
        return menuProductId;
    }

    public void setMenuProductId(String menuProductId) {
        this.menuProductId = menuProductId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVegType() {
        return vegType;
    }

    public void setVegType(String vegType) {
        this.vegType = vegType;
    }

    public String getMenuProductPrice() {
        return menuProductPrice;
    }

    public void setMenuProductPrice(String menuProductPrice) {
        this.menuProductPrice = menuProductPrice;
    }

    public String getUserappProductImage() {
        return userappProductImage;
    }

    public void setUserappProductImage(String userappProductImage) {
        this.userappProductImage = userappProductImage;
    }

    public String getEcomProductImage() {
        return ecomProductImage;
    }

    public void setEcomProductImage(String ecomProductImage) {
        this.ecomProductImage = ecomProductImage;
    }

    public String getProductOverallRating() {
        return productOverallRating;
    }

    public void setProductOverallRating(String productOverallRating) {
        this.productOverallRating = productOverallRating;
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

    public List<MealProduct> getMealProducts() {
        return mealProducts;
    }

    public void setMealProducts(List<MealProduct> mealProducts) {
        this.mealProducts = mealProducts;
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

    public Double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(Double originalAmount) {
        this.originalAmount = originalAmount;
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "id=" + id +
                ", menuId=" + menuId +
                ", menuSubCatId=" + menuSubCatId +
                ", menuProductId='" + menuProductId + '\'' +
                ", productName='" + productName + '\'' +
                ", description='" + description + '\'' +
                ", vegType='" + vegType + '\'' +
                ", menuProductPrice='" + menuProductPrice + '\'' +
                ", userappProductImage='" + userappProductImage + '\'' +
                ", ecomProductImage='" + ecomProductImage + '\'' +
                ", productOverallRating='" + productOverallRating + '\'' +
                ", menuProductSize=" + menuProductSize +
                ", productModifiers=" + productModifiers +
                ", mealProducts=" + mealProducts +
                ", amount='" + amount + '\'' +
                ", quantity=" + quantity +
                ", originalQuantity=" + originalQuantity +
                ", originalAmount=" + originalAmount +
                ", originalAmount1=" + originalAmount1 +
                '}';
    }
}