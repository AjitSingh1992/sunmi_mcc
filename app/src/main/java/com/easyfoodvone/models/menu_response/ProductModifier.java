package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductModifier {
    @SerializedName("modifierName")
    @Expose
    private String modifierName;
    @SerializedName("modifierType")
    @Expose
    private String modifierType;
    @SerializedName("modifierId")
    @Expose
    private String modifierId;
    @SerializedName("minAllowedQuantity")
    @Expose
    private Integer minAllowedQuantity;
    @SerializedName("maxAllowedQuantity")
    @Expose
    private Integer maxAllowedQuantity;
    @SerializedName("modifierProducts")
    @Expose
    private List<Modifier> modifier = null;
    @Expose
    private String amount;

    public ProductModifier() {
    }

    public ProductModifier(String modifierName, String modifierType, String modifierId, Integer minAllowedQuantity, Integer maxAllowedQuantity, List<Modifier> modifier, String amount, Integer quantity) {
        this.modifierName = modifierName;
        this.modifierType = modifierType;
        this.modifierId = modifierId;
        this.minAllowedQuantity = minAllowedQuantity;
        this.maxAllowedQuantity = maxAllowedQuantity;
        this.modifier = modifier;
        this.amount = amount;
        this.quantity = quantity;
    }

    private Integer quantity;

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

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public Integer getMinAllowedQuantity() {
        return minAllowedQuantity;
    }

    public void setMinAllowedQuantity(Integer minAllowedQuantity) {
        this.minAllowedQuantity = minAllowedQuantity;
    }

    public Integer getMaxAllowedQuantity() {
        return maxAllowedQuantity;
    }

    public void setMaxAllowedQuantity(Integer maxAllowedQuantity) {
        this.maxAllowedQuantity = maxAllowedQuantity;
    }

    public List<Modifier> getModifier() {
        return modifier;
    }

    public void setModifier(List<Modifier> modifier) {
        this.modifier = modifier;
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

    @Override
    public String toString() {
        return "ProductModifier{" +
                "modifierName='" + modifierName + '\'' +
                ", modifierType='" + modifierType + '\'' +
                ", modifierId='" + modifierId + '\'' +
                ", minAllowedQuantity=" + minAllowedQuantity +
                ", maxAllowedQuantity=" + maxAllowedQuantity +
                ", modifier=" + modifier +
                ", amount='" + amount + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}