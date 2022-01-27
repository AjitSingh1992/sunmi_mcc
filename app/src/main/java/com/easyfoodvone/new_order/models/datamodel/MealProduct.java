package com.easyfoodvone.new_order.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MealProduct {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productSizeId")
    @Expose
    private String productSizeId;
    @SerializedName("productSizeName")
    @Expose
    private String productSizeName;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("productModifiers")
    @Expose
    private List<ProductModifier> productModifiers = null;
    @SerializedName("sizeModifiers")
    @Expose
    private List<SizeModifier> sizeModifiers = null;

    public MealProduct(String id, String productId, String productName, String productSizeId, String productSizeName, Integer quantity, List<ProductModifier> productModifiers, List<SizeModifier> sizeModifiers) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productSizeId = productSizeId;
        this.productSizeName = productSizeName;
        this.quantity = quantity;
        this.productModifiers = productModifiers;
        this.sizeModifiers = sizeModifiers;
    }

    public List<SizeModifier> getSizeModifiers() {
        return sizeModifiers;
    }

    public void setSizeModifiers(List<SizeModifier> sizeModifiers) {
        this.sizeModifiers = sizeModifiers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<ProductModifier> getProductModifiers() {
        return productModifiers;
    }

    public void setProductModifiers(List<ProductModifier> productModifiers) {
        this.productModifiers = productModifiers;
    }
}
