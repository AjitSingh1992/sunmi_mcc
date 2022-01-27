package com.easyfoodvone.new_order.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SizeModifier {
    @SerializedName("maxAllowedQuantity")
    @Expose
    private String maxAllowedQuantity;
    @SerializedName("minAllowedQuantity")
    @Expose
    private String minAllowedQuantity;
    @SerializedName("modifierId")
    @Expose
    private String modifierId;
    @SerializedName("modifierName")
    @Expose
    private String modifierName;
    @SerializedName("modifierType")
    @Expose
    private String modifierType;
    @SerializedName("sizeModifierProducts")
    @Expose
    private List<SizeModifierProduct> sizeModifierProducts = null;

    public String getMaxAllowedQuantity() {
        return maxAllowedQuantity;
    }

    public void setMaxAllowedQuantity(String maxAllowedQuantity) {
        this.maxAllowedQuantity = maxAllowedQuantity;
    }

    public String getMinAllowedQuantity() {
        return minAllowedQuantity;
    }

    public void setMinAllowedQuantity(String minAllowedQuantity) {
        this.minAllowedQuantity = minAllowedQuantity;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName;
    }

    public String getModifierType() {
        return modifierType;
    }

    public void setModifierType(String modifierType) {
        this.modifierType = modifierType;
    }

    public List<SizeModifierProduct> getSizeModifierProducts() {
        return sizeModifierProducts;
    }

    public void setSizeModifierProducts(List<SizeModifierProduct> sizeModifierProducts) {
        this.sizeModifierProducts = sizeModifierProducts;
    }


}
