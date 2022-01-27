package com.easyfoodvone.new_order.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Size {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("productSizeName")
    @Expose
    private String productSizeName;
    @SerializedName("productSizePrice")
    @Expose
    private String productSizePrice;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("sizemodifiers")
    @Expose
    private List<SizeModifier> sizeModifiers = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    public List<SizeModifier> getSizeModifiers() {
        return sizeModifiers;
    }

    public void setSizeModifiers(List<SizeModifier> sizemodifiers) {
        this.sizeModifiers = sizemodifiers;
    }
}
