package com.easyfoodvone.app_common.ws;

import java.io.Serializable;
import java.util.List;

public class MenuCategoryItemsResponse extends CommonResponse implements Serializable
{
    List<Items> items;

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "MenuCategoryItemsResponse{" +
                "items=" + items +
                '}';
    }

    public class Items implements Serializable{
        String menu_product_id;
        String menu_product_position;
        String menu_product_name;
        String menu_product_price;
        String active;

        public String getMenu_product_id() {
            return menu_product_id;
        }

        public void setMenu_product_id(String menu_product_id) {
            this.menu_product_id = menu_product_id;
        }

        public String getMenu_product_position() {
            return menu_product_position;
        }

        public void setMenu_product_position(String menu_product_position) {
            this.menu_product_position = menu_product_position;
        }

        public String getMenu_product_name() {
            return menu_product_name;
        }

        public void setMenu_product_name(String menu_product_name) {
            this.menu_product_name = menu_product_name;
        }

        public String getMenu_product_price() {
            return menu_product_price;
        }

        public void setMenu_product_price(String menu_product_price) {
            this.menu_product_price = menu_product_price;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        @Override
        public String toString() {
            return "Items{" +
                    "menu_product_id='" + menu_product_id + '\'' +
                    ", menu_product_position='" + menu_product_position + '\'' +
                    ", menu_product_name='" + menu_product_name + '\'' +
                    ", menu_product_price='" + menu_product_price + '\'' +
                    ", active='" + active + '\'' +
                    '}';
        }
    }
}
