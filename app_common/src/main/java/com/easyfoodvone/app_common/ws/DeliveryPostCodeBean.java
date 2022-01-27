package com.easyfoodvone.app_common.ws;

import java.util.List;

public class DeliveryPostCodeBean {

    /**
     * success : true
     * data : [{"id":"24bd7876-9f1c-11ea-bff0-0ae3c2aa3024","restaurant_id":"d8d42dd8-5e2a-11ea-a49f-0657952ed75a","postcode":"NN16","delivery_min_value":"13.03","ship_cost":"12.03","free_delivery_over":"22.01","is_primary":0},{"id":"ad2a0274-5e2b-11ea-9e38-0657952ed75a","restaurant_id":"d8d42dd8-5e2a-11ea-a49f-0657952ed75a","postcode":"nn14","delivery_min_value":"1.00","ship_cost":"2.50","free_delivery_over":"123.00","is_primary":1}]
     */

    private boolean success;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 24bd7876-9f1c-11ea-bff0-0ae3c2aa3024
         * restaurant_id : d8d42dd8-5e2a-11ea-a49f-0657952ed75a
         * postcode : NN16
         * delivery_min_value : 13.03
         * ship_cost : 12.03
         * free_delivery_over : 22.01
         * is_primary : 0
         */

        private String id;
        private String restaurant_id;
        private String postcode;
        private String delivery_min_value;
        private String ship_cost;
        private String free_delivery_over;
        private int is_primary;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRestaurant_id() {
            return restaurant_id;
        }

        public void setRestaurant_id(String restaurant_id) {
            this.restaurant_id = restaurant_id;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getDelivery_min_value() {
            return delivery_min_value;
        }

        public void setDelivery_min_value(String delivery_min_value) {
            this.delivery_min_value = delivery_min_value;
        }

        public String getShip_cost() {
            return ship_cost;
        }

        public void setShip_cost(String ship_cost) {
            this.ship_cost = ship_cost;
        }

        public String getFree_delivery_over() {
            return free_delivery_over;
        }

        public void setFree_delivery_over(String free_delivery_over) {
            this.free_delivery_over = free_delivery_over;
        }

        public int getIs_primary() {
            return is_primary;
        }

        public void setIs_primary(int is_primary) {
            this.is_primary = is_primary;
        }
    }
}
