package com.easyfoodvone.models.menu_response;

public class AllTypeMenuItemModel {
    MenuProduct menuProduct;
    SpecialOffer specialOffer;
    UpSells upSells;
    int menuType; // MENU_CATEGORY_ITEM_VIEW_TYPE = 1; SPECIAL_OFFER_ITEM_VIEW_TYPE = 2; UP_SELLS_ITEM_VIEW_TYPE = 3;

    public AllTypeMenuItemModel(MenuProduct menuProduct, SpecialOffer specialOffer, UpSells upSells, int menuType) {
        this.menuProduct = menuProduct;
        this.specialOffer = specialOffer;
        this.upSells = upSells;
        this.menuType = menuType;
    }

    public MenuProduct getMenuProduct() {
        return menuProduct;
    }

    public void setMenuProduct(MenuProduct menuProduct) {
        this.menuProduct = menuProduct;
    }

    public SpecialOffer getSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(SpecialOffer specialOffer) {
        this.specialOffer = specialOffer;
    }

    public UpSells getUpSells() {
        return upSells;
    }

    public void setUpSells(UpSells upSells) {
        this.upSells = upSells;
    }

    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    @Override
    public String toString() {
        return "AllTypeMenuItemModel{" +
                "menuProduct=" + menuProduct +
                ", specialOffer=" + specialOffer +
                ", upSells=" + upSells +
                ", menuType=" + menuType +
                '}';
    }
}
