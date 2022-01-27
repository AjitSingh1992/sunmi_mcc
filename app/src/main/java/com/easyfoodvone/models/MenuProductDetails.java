package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MenuProductDetails implements Serializable {
    boolean success;
    String message;


    @Expose
    @SerializedName("restaurant_id")
    String restaurant_id;


    @Expose
    @SerializedName("menu_product_id")
    String menu_product_id;

    @Expose
    @SerializedName("data")
    Data data;

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getMenu_product_id() {
        return menu_product_id;
    }

    public void setMenu_product_id(String menu_product_id) {
        this.menu_product_id = menu_product_id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data
    {
        @Expose
        @SerializedName("id")
        String id;

        @Expose
        @SerializedName("product_name")
        String product_name;

        @Expose
        @SerializedName("product_price")
        String product_price;

        @Expose
        @SerializedName("menu_product_size")
        List<MenuProductSizes> menu_product_size;

        @Expose
        @SerializedName("product_modifiers")
        List<ProductModifiers> product_modifiers;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public List<MenuProductSizes> getMenu_product_size() {
            return menu_product_size;
        }

        public void setMenu_product_size(List<MenuProductSizes> menu_product_size) {
            this.menu_product_size = menu_product_size;
        }

        public List<ProductModifiers> getProduct_modifiers() {
            return product_modifiers;
        }

        public void setProduct_modifiers(List<ProductModifiers> product_modifiers) {
            this.product_modifiers = product_modifiers;
        }
    }

    public class MenuProductSizes {
        @Expose
        @SerializedName("size_id")
        String size_id;

        @Expose
        @SerializedName("size_name")
        String size_name;

        @Expose
        @SerializedName("size_price")
        String size_price;

        @Expose
        @SerializedName("size_modifiers")
        List<SizeModifiers> size_modifiers;

        public String getSize_id() {
            return size_id;
        }

        public void setSize_id(String size_id) {
            this.size_id = size_id;
        }

        public String getSize_name() {
            return size_name;
        }

        public void setSize_name(String size_name) {
            this.size_name = size_name;
        }

        public String getSize_price() {
            return size_price;
        }

        public void setSize_price(String size_price) {
            this.size_price = size_price;
        }

        public List<SizeModifiers> getSize_modifiers() {
            return size_modifiers;
        }

        public void setSize_modifiers(List<SizeModifiers> size_modifiers) {
            this.size_modifiers = size_modifiers;
        }
    }


    public class SizeModifiers {
        @Expose
        @SerializedName("modifier_name")
        String modifier_name;

        @Expose
        @SerializedName("modifier_type")
        String modifier_type;

        @Expose
        @SerializedName("modifier_id")
        String modifier_id;

        @Expose
        @SerializedName("min_allowed_quantity")
        int min_allowed_quantity;

        @Expose
        @SerializedName("max_allowed_quantity")
        int max_allowed_quantity;

        @Expose
        @SerializedName("size_modifier_products")
        List<SizeModifierProducts> size_modifier_products;

        public String getModifier_name() {
            return modifier_name;
        }

        public void setModifier_name(String modifier_name) {
            this.modifier_name = modifier_name;
        }

        public String getModifier_type() {
            return modifier_type;
        }

        public void setModifier_type(String modifier_type) {
            this.modifier_type = modifier_type;
        }

        public String getModifier_id() {
            return modifier_id;
        }

        public void setModifier_id(String modifier_id) {
            this.modifier_id = modifier_id;
        }

        public int getMin_allowed_quantity() {
            return min_allowed_quantity;
        }

        public void setMin_allowed_quantity(int min_allowed_quantity) {
            this.min_allowed_quantity = min_allowed_quantity;
        }

        public int getMax_allowed_quantity() {
            return max_allowed_quantity;
        }

        public void setMax_allowed_quantity(int max_allowed_quantity) {
            this.max_allowed_quantity = max_allowed_quantity;
        }

        public List<SizeModifierProducts> getSize_modifier_products() {
            return size_modifier_products;
        }

        public void setSize_modifier_products(List<SizeModifierProducts> size_modifier_products) {
            this.size_modifier_products = size_modifier_products;
        }
    }


    public class SizeModifierProducts
    {
        @Expose
        @SerializedName("id")
        String id;

        @Expose
        @SerializedName("modifier_group_id")
        String modifier_group_id;

        @Expose
        @SerializedName("product_id")
        String product_id;

        @Expose
        @SerializedName("quantity")
        String quantity;

        @Expose
        @SerializedName("unit")
        String unit;

        @Expose
        @SerializedName("wastage_percentage")
        String wastage_percentage;

        @Expose
        @SerializedName("buy_price")
        String buy_price;

        @Expose
        @SerializedName("sell_price")
        String sell_price;

        @Expose
        @SerializedName("status")
        String status;

        @Expose
        @SerializedName("modifier_product_price")
        String modifier_product_price;

        @Expose
        @SerializedName("product_name")
        String product_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getModifier_group_id() {
            return modifier_group_id;
        }

        public void setModifier_group_id(String modifier_group_id) {
            this.modifier_group_id = modifier_group_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getWastage_percentage() {
            return wastage_percentage;
        }

        public void setWastage_percentage(String wastage_percentage) {
            this.wastage_percentage = wastage_percentage;
        }

        public String getBuy_price() {
            return buy_price;
        }

        public void setBuy_price(String buy_price) {
            this.buy_price = buy_price;
        }

        public String getSell_price() {
            return sell_price;
        }

        public void setSell_price(String sell_price) {
            this.sell_price = sell_price;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getModifier_product_price() {
            return modifier_product_price;
        }

        public void setModifier_product_price(String modifier_product_price) {
            this.modifier_product_price = modifier_product_price;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }
    }


    public class ProductModifiers {

        @Expose
        @SerializedName("modifier_name")
        String modifier_name;

        @Expose
        @SerializedName("modifier_type")
        String modifier_type;

        @Expose
        @SerializedName("modifier_id")
        String modifier_id;

        @Expose
        @SerializedName("min_allowed_quantity")
        String min_allowed_quantity;

        @Expose
        @SerializedName("max_allowed_quantity")
        String max_allowed_quantity;

        @Expose
        @SerializedName("modifier_products")
        List<ModifierProducts> modifier_products;

        public String getModifier_name() {
            return modifier_name;
        }

        public void setModifier_name(String modifier_name) {
            this.modifier_name = modifier_name;
        }

        public String getModifier_type() {
            return modifier_type;
        }

        public void setModifier_type(String modifier_type) {
            this.modifier_type = modifier_type;
        }

        public String getModifier_id() {
            return modifier_id;
        }

        public void setModifier_id(String modifier_id) {
            this.modifier_id = modifier_id;
        }

        public String getMin_allowed_quantity() {
            return min_allowed_quantity;
        }

        public void setMin_allowed_quantity(String min_allowed_quantity) {
            this.min_allowed_quantity = min_allowed_quantity;
        }

        public String getMax_allowed_quantity() {
            return max_allowed_quantity;
        }

        public void setMax_allowed_quantity(String max_allowed_quantity) {
            this.max_allowed_quantity = max_allowed_quantity;
        }

        public List<ModifierProducts> getModifier_products() {
            return modifier_products;
        }

        public void setModifier_products(List<ModifierProducts> modifier_products) {
            this.modifier_products = modifier_products;
        }
    }


    public class ModifierProducts
    {

        @Expose
        @SerializedName("id")
        String id;

        @Expose
        @SerializedName("modifier_group_id")
        String modifier_group_id;

        @Expose
        @SerializedName("product_id")
        String product_id;

        @Expose
        @SerializedName("quantity")
        int quantity;

        @Expose
        @SerializedName("unit")
        String unit;

        @Expose
        @SerializedName("wastage_percentage")
        String wastage_percentage;

        @Expose
        @SerializedName("buy_price")
        String buy_price;

        @Expose
        @SerializedName("sell_price")
        String sell_price;

        @Expose
        @SerializedName("status")
        int status;

        @Expose
        @SerializedName("modifier_product_price")
        String modifier_product_price;

        @Expose
        @SerializedName("product_name")
        String product_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getModifier_group_id() {
            return modifier_group_id;
        }

        public void setModifier_group_id(String modifier_group_id) {
            this.modifier_group_id = modifier_group_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getWastage_percentage() {
            return wastage_percentage;
        }

        public void setWastage_percentage(String wastage_percentage) {
            this.wastage_percentage = wastage_percentage;
        }

        public String getBuy_price() {
            return buy_price;
        }

        public void setBuy_price(String buy_price) {
            this.buy_price = buy_price;
        }

        public String getSell_price() {
            return sell_price;
        }

        public void setSell_price(String sell_price) {
            this.sell_price = sell_price;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getModifier_product_price() {
            return modifier_product_price;
        }

        public void setModifier_product_price(String modifier_product_price) {
            this.modifier_product_price = modifier_product_price;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }
    }
}
