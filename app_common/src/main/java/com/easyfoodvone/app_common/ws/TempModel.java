package com.easyfoodvone.app_common.ws;

public class TempModel
{
    String product_id;
    String product_name;
    String product_price;
    String price;
    String quantity;
    boolean is_checked;


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isIs_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }

    public TempModel(String product_id, String product_name, String product_price, String price, String quantity, boolean is_checked) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.price = price;
        this.quantity = quantity;
        this.is_checked = is_checked;
    }
}
