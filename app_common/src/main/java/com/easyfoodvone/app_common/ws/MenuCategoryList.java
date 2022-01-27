package com.easyfoodvone.app_common.ws;

import java.io.Serializable;
import java.util.List;

public class MenuCategoryList extends CommonResponse implements Serializable
{
    List<MenuCategories> menu_items;

    public List<MenuCategories> getMenu_items() {
        return menu_items;
    }

    public void setMenu_items(List<MenuCategories> menu_items) {
        this.menu_items = menu_items;
    }

    @Override
    public String toString() {
        return "MenuCategoryList{" +
                "menu_items=" + menu_items +
                '}';
    }

    public class MenuCategories implements Serializable
    {
        String menu_category_id;
        String menu_category_position;
        String menu_category_name;
        String menu_category_image;
        String number_of_menu_product;
        String menu_category_status;

        public String getMenu_category_status() {
            return menu_category_status;
        }

        public void setMenu_category_status(String menu_category_status) {
            this.menu_category_status = menu_category_status;
        }

        public String getMenu_category_id() {
            return menu_category_id;
        }

        public void setMenu_category_id(String menu_category_id) {
            this.menu_category_id = menu_category_id;
        }


        public String getMenu_category_name() {
            return menu_category_name;
        }

        public void setMenu_category_name(String menu_category_name) {
            this.menu_category_name = menu_category_name;
        }

        public String getMenu_category_image() {
            return menu_category_image;
        }

        public void setMenu_category_image(String menu_category_image) {
            this.menu_category_image = menu_category_image;
        }

        public String getMenu_category_position() {
            return menu_category_position;
        }

        public void setMenu_category_position(String menu_category_position) {
            this.menu_category_position = menu_category_position;
        }

        public String getNumber_of_menu_product() {
            return number_of_menu_product;
        }

        public void setNumber_of_menu_product(String number_of_menu_product) {
            this.number_of_menu_product = number_of_menu_product;
        }

        @Override
        public String toString() {
            return "MenuCategories{" +
                    "menu_category_id='" + menu_category_id + '\'' +
                    ", menu_category_position='" + menu_category_position + '\'' +
                    ", menu_category_name='" + menu_category_name + '\'' +
                    ", menu_category_image='" + menu_category_image + '\'' +
                    ", number_of_menu_product='" + number_of_menu_product + '\'' +
                    ", active='" + menu_category_status + '\'' +
                    '}';
        }
    }

}
