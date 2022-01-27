package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SpecialOffer {
    @SerializedName("offerId")
    @Expose
    private String offerId;
    @SerializedName("offerTitle")
    @Expose
    private String offerTitle;
    @SerializedName("offerAvailable")
    @Expose
    private String offerAvailable;
    @SerializedName("spendAmountToAvaliableOffer")
    @Expose
    private String spendAmountToAvaliableOffer;
    @SerializedName("offerDiscountPercentage")
    @Expose
    private String offerDiscountPercentage;
    @SerializedName("offerDeliveryOption")
    @Expose
    private String offerDeliveryOption;
    @SerializedName("offerDetails")
    @Expose
    private String offerDetails;
    @SerializedName("originalPrice")
    @Expose
    private String originalPrice;
    @SerializedName("totalDiscount")
    @Expose
    private String totalDiscount;
    @SerializedName("offerPrice")
    @Expose
    private String offerPrice;
    @Expose
    private int quantity;
    @Expose
    private Double amount;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String offerTitle) {
        this.offerTitle = offerTitle;
    }

    public String getOfferAvailable() {
        return offerAvailable;
    }

    public void setOfferAvailable(String offerAvailable) {
        this.offerAvailable = offerAvailable;
    }

    public String getSpendAmountToAvaliableOffer() {
        return spendAmountToAvaliableOffer;
    }

    public void setSpendAmountToAvaliableOffer(String spendAmountToAvaliableOffer) {
        this.spendAmountToAvaliableOffer = spendAmountToAvaliableOffer;
    }

    public String getOfferDiscountPercentage() {
        return offerDiscountPercentage;
    }

    public void setOfferDiscountPercentage(String offerDiscountPercentage) {
        this.offerDiscountPercentage = offerDiscountPercentage;
    }

    public String getOfferDeliveryOption() {
        return offerDeliveryOption;
    }

    public void setOfferDeliveryOption(String offerDeliveryOption) {
        this.offerDeliveryOption = offerDeliveryOption;
    }

    public String getOfferDetails() {
        return offerDetails;
    }

    public void setOfferDetails(String offerDetails) {
        this.offerDetails = offerDetails;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "SpecialOffer{" +
                "offerId='" + offerId + '\'' +
                ", offerTitle='" + offerTitle + '\'' +
                ", offerAvailable='" + offerAvailable + '\'' +
                ", spendAmountToAvaliableOffer='" + spendAmountToAvaliableOffer + '\'' +
                ", offerDiscountPercentage='" + offerDiscountPercentage + '\'' +
                ", offerDeliveryOption='" + offerDeliveryOption + '\'' +
                ", offerDetails='" + offerDetails + '\'' +
                ", originalPrice='" + originalPrice + '\'' +
                ", totalDiscount='" + totalDiscount + '\'' +
                ", offerPrice='" + offerPrice + '\'' +
                ", quantity=" + quantity +
                ", amount=" + amount +
                '}';
    }
}