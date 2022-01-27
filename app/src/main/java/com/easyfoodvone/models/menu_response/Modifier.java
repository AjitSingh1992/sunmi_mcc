package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Modifier {
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("modifierProductPrice")
    @Expose
    private String modifierProductPrice;
    @SerializedName("productName")
    @Expose
    private String productName;
    @Expose
    private String quantity = "0";
    @Expose
    private String originalQuantity = "0";
    @Expose
    private Double amount = null;
    @Expose
    private Double originalAmount1 = null;

    public Modifier() {
    }

    public Modifier(String productId, String unit, String modifierProductPrice, String productName, String quantity, String originalQuantity, Double amount, Double originalAmount1) {
        this.productId = productId;
        this.unit = unit;
        this.modifierProductPrice = modifierProductPrice;
        this.productName = productName;
        this.quantity = quantity;
        this.originalQuantity = originalQuantity;
        this.amount = amount;
        this.originalAmount1 = originalAmount1;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getModifierProductPrice() {
        return modifierProductPrice;
    }

    public void setModifierProductPrice(String modifierProductPrice) {
        this.modifierProductPrice = modifierProductPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

   /* public String getQuantity() {
        return quantity;
    }*/

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(String originalQuantity) {
        this.originalQuantity = originalQuantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getOriginalAmount1() {
        return originalAmount1;
    }

    public void setOriginalAmount1(Double originalAmount1) {
        this.originalAmount1 = originalAmount1;
    }

    @Override
    public String toString() {
        return "Modifier{" +
                "productId='" + productId + '\'' +
                ", unit='" + unit + '\'' +
                ", modifierProductPrice='" + modifierProductPrice + '\'' +
                ", productName='" + productName + '\'' +
                ", quantity='" + quantity + '\'' +
                ", originalQuantity='" + originalQuantity + '\'' +
                ", amount=" + amount +
                ", originalAmount1=" + originalAmount1 +
                '}';
    }
}