package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuProductSize {
    @SerializedName("productSizeId")
    @Expose
    private String productSizeId;
    @SerializedName("productSizeName")
    @Expose
    private String productSizeName;
    @SerializedName("productSizePrice")
    @Expose
    private String productSizePrice;
    @SerializedName("sizeModifiers")
    @Expose
    private List<SizeModifier> sizeModifiers = null;
    @Expose
    private String amount;
    @Expose
    private Integer quantity;

    @Expose
    private String originalQuantity = null;
    @Expose
    private Double originalAmount1 = null;
    @Expose
    private Double originalAmount = null;

    @Expose
    public Boolean isSelected = false;

    public MenuProductSize() {
    }

    public MenuProductSize(String productSizeId, String productSizeName, String productSizePrice, String amount, Integer quantity, List<SizeModifier> sizeModifiers,String originalQuantity,Double originalAmount,Double originalAmount11) {
        this.productSizeId = productSizeId;
        this.productSizeName = productSizeName;
        this.productSizePrice = productSizePrice;
        this.amount = amount;
        this.quantity = quantity;
        this.sizeModifiers = sizeModifiers;
        this.originalQuantity = originalQuantity;
        this.originalAmount = originalAmount;
        this.originalAmount1 = originalAmount1;
    }

    public String getProductSizeId() {
        return productSizeId;
    }

    public void setProductSizeId(String productSizeId) {
        this.productSizeId = productSizeId;
    }

    public String getProductSizeName() {
        return productSizeName;
    }

    public void setProductSizeName(String productSizeName) {
        this.productSizeName = productSizeName;
    }

    public String getProductSizePrice() {
        return productSizePrice;
    }

    public void setProductSizePrice(String productSizePrice) {
        this.productSizePrice = productSizePrice;
    }

    public List<SizeModifier> getSizeModifiers() {
        return sizeModifiers;
    }

    public void setSizeModifiers(List<SizeModifier> sizeModifiers) {
        this.sizeModifiers = sizeModifiers;
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

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getOriginalQuantity() {
        return originalQuantity;
    }

    public void setOriginalQuantity(String originalQuantity) {
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
        return "MenuProductSize{" +
                "productSizeId='" + productSizeId + '\'' +
                ", productSizeName='" + productSizeName + '\'' +
                ", productSizePrice='" + productSizePrice + '\'' +
                ", sizeModifiers=" + sizeModifiers +
                ", amount='" + amount + '\'' +
                ", quantity=" + quantity +
                ", originalQuantity='" + originalQuantity + '\'' +
                ", originalAmount1=" + originalAmount1 +
                ", originalAmount=" + originalAmount +
                ", isSelected=" + isSelected +
                '}';
    }
}