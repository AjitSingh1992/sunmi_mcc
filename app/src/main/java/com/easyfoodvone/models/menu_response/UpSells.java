package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.SerializedName;

public class UpSells {
    @SerializedName("amount")
    String amount;
    @SerializedName("categoryId")
    String categoryId;
    @SerializedName("description")
    String description;
    @SerializedName("groupId")
    String groupId;
    @SerializedName("id")
    String id;
    @SerializedName("item_id")
    String item_id;
    @SerializedName("originalAmount")
    String originalAmount;
    @SerializedName("originalAmount1")
    String originalAmount1;
    @SerializedName("originalQuantity")
    String originalQuantity;
    @SerializedName("productId")
    String productId;
    @SerializedName("productName")
    String productName;
    @SerializedName("quantity")
    String quantity;
    @SerializedName("productPrice")
    Double productPrice;
    @SerializedName("unit")
    String unit;

    public UpSells(String amount, String categoryId, String description, String groupId, String id, String item_id, String originalAmount, String originalAmount1, String originalQuantity, String productId, String productName, String quantity, Double productPrice,String unit) {
        this.amount = amount;
        this.categoryId = categoryId;
        this.description = description;
        this.groupId = groupId;
        this.id = id;
        this.item_id = item_id;
        this.originalAmount = originalAmount;
        this.originalAmount1 = originalAmount1;
        this.originalQuantity = originalQuantity;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(String originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getOriginalAmount1() {
        return originalAmount1;
    }

    public void setOriginalAmount1(String originalAmount1) {
        this.originalAmount1 = originalAmount1;
    }

    public String getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(String originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "UpSells{" +
                "amount='" + amount + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", description='" + description + '\'' +
                ", groupId='" + groupId + '\'' +
                ", id='" + id + '\'' +
                ", item_id='" + item_id + '\'' +
                ", originalAmount='" + originalAmount + '\'' +
                ", originalAmount1='" + originalAmount1 + '\'' +
                ", originalQuantity='" + originalQuantity + '\'' +
                ", productId='" + productId + '\'' +
                ", productName='" + productName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", productPrice=" + productPrice +
                ", unit='" + unit + '\'' +
                '}';
    }
}