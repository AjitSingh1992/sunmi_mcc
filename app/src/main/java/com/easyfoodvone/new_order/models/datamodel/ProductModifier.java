package com.easyfoodvone.new_order.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModifier {
    @SerializedName("maxAllowedQuantity")
    @Expose
    private Integer maxAllowedQuantity;
    @SerializedName("minAllowedQuantity")
    @Expose
    private Integer minAllowedQuantity;
    @SerializedName("modifierProducts")
    @Expose
    private List<ModifierProduct> modifierProducts = null;
    @SerializedName("modifierId")
    @Expose
    private String modifierId;
    @SerializedName("modifierName")
    @Expose
    private String modifierName;
    @SerializedName("modifierType")
    @Expose
    private String modifierType;

    public Integer getMaxAllowedQuantity() {
        return maxAllowedQuantity;
    }

    public void setMaxAllowedQuantity(Integer maxAllowedQuantity) {
        this.maxAllowedQuantity = maxAllowedQuantity;
    }

    public Integer getMinAllowedQuantity() {
        return minAllowedQuantity;
    }

    public void setMinAllowedQuantity(Integer minAllowedQuantity) {
        this.minAllowedQuantity = minAllowedQuantity;
    }

    public List<ModifierProduct> getModifierProducts() {
        return modifierProducts;
    }

    public void setModifierProducts(List<ModifierProduct> modifierProducts) {
        this.modifierProducts = modifierProducts;
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
}
