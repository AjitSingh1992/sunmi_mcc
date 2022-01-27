package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Menu {

    @SerializedName("specialOffers")
    @Expose
    private List<SpecialOffer> specialOffers;
    @SerializedName("menuCategory")
    @Expose
    private List<MenuCategory> menuCategory;

    @SerializedName("upsellProducts")
    @Expose
    private List<UpSells> upSellProducts;

    public Menu(List<SpecialOffer> specialOffers, List<MenuCategory> menuCategory, List<UpSells> upSellProducts) {
        this.specialOffers = specialOffers;
        this.menuCategory = menuCategory;
        this.upSellProducts = upSellProducts;
    }

    public List<SpecialOffer> getSpecialOffers() {
        return specialOffers;
    }

    public void setSpecialOffers(List<SpecialOffer> specialOffers) {
        this.specialOffers = specialOffers;
    }

    public List<MenuCategory> getMenuCategory() {
        return menuCategory;
    }

    public void setMenuCategory(List<MenuCategory> menuCategory) {
        this.menuCategory = menuCategory;
    }

    public List<UpSells> getUpSellProducts() {
        return upSellProducts;
    }

    public void setUpSellProducts(List<UpSells> upSellProducts) {
        this.upSellProducts = upSellProducts;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "specialOffers=" + specialOffers +
                ", menuCategory=" + menuCategory +
                ", upSellProducts=" + upSellProducts +
                '}';
    }
}