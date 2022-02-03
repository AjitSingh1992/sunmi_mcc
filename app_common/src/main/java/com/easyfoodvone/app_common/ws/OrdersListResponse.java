package com.easyfoodvone.app_common.ws;

import java.io.Serializable;
import java.util.List;

public class OrdersListResponse implements Serializable {
    boolean success;
    String message;
    Data data;

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

    @Override
    public String toString() {
        return "OrdersListResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public class Data implements Serializable {
        List<Orders> orders;
        int total_new_order;
        int total_rejected_order;
        int total_accepted_order;
        int total_refunded_order;
        String android_version;
        boolean is_open;

        public int getTotal_refunded_order() {
            return total_refunded_order;
        }

        public void setTotal_refunded_order(int total_refunded_order) {
            this.total_refunded_order = total_refunded_order;
        }

        String delivery_partner;

        public String getAndroid_version() {
            return android_version;
        }

        public void setAndroid_version(String android_version) {
            this.android_version = android_version;
        }

        public String getIos_version() {
            return ios_version;
        }

        public void setIos_version(String ios_version) {
            this.ios_version = ios_version;
        }

        public String getForce_update() {
            return force_update;
        }

        public void setForce_update(String force_update) {
            this.force_update = force_update;
        }

        String ios_version;
        String force_update;

        public List<Orders> getOrders() {
            return orders;
        }

        public void setOrders(List<Orders> orders) {
            this.orders = orders;
        }

        public int getTotal_new_order() {
            return total_new_order;
        }

        public void setTotal_new_order(int total_new_order) {
            this.total_new_order = total_new_order;
        }

        public int getTotal_rejected_order() {
            return total_rejected_order;
        }

        public void setTotal_rejected_order(int total_rejected_order) {
            this.total_rejected_order = total_rejected_order;
        }

        public int getTotal_accepted_order() {
            return total_accepted_order;
        }

        public void setTotal_accepted_order(int total_accepted_order) {
            this.total_accepted_order = total_accepted_order;
        }

        public boolean isIs_open() {
            return is_open;
        }

        public void setIs_open(boolean is_open) {
            this.is_open = is_open;
        }

        public String getDelivery_partner() {
            return delivery_partner;
        }

        public void setDelivery_partner(String delivery_partner) {
            this.delivery_partner = delivery_partner;
        }

        @Override
        public String toString() {
            return "OrdersDetailsTop{" +
                    "orders=" + orders +
                    ", total_new_order=" + total_new_order +
                    ", ios_version=" + ios_version +
                    ", android_version=" + android_version +
                    ", force_update=" + force_update +
                    ", total_rejected_order=" + total_rejected_order +
                    ", total_accepted_order=" + total_accepted_order +
                    ", is_open=" + is_open +
                    ", delivery_partner=" + delivery_partner +
                    '}';
        }
    }

    public class Orders implements Serializable {
        String order_id;
        String restaurant_id;
        String customer_id;
        String cart_id;
        String order_num;
        String order_total;
        String order_date_time;
        int is_paid;
        String payment_mode;
        String payment_status;
        int is_delivered;
        String delivery_time;
        String delivery_option;
        String delivery_date_time;
        String delivery_date_time_label;
        String delivery_charge;
        String discount_amount;
        String voucher_id;
        String offer_id;
        String order_status;
        String order_notes;
        String customer_name;
        String customer_contact;
        String customer_location;
        String is_preorder;
        boolean newOrder = false;

        String previous_order;
        int avg_delivery_time;


        public Boolean getNewOrder() {
            return newOrder;
        }

        public void setNewOrder(Boolean newOrder) {
            this.newOrder = newOrder;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getRestaurant_id() {
            return restaurant_id;
        }

        public void setRestaurant_id(String restaurant_id) {
            this.restaurant_id = restaurant_id;
        }

        public String getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(String customer_id) {
            this.customer_id = customer_id;
        }

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getOrder_total() {
            return order_total;
        }

        public void setOrder_total(String order_total) {
            this.order_total = order_total;
        }

        public String getOrder_date_time() {
            return order_date_time;
        }

        public void setOrder_date_time(String order_date_time) {
            this.order_date_time = order_date_time;
        }

        public int getIs_paid() {
            return is_paid;
        }

        public void setIs_paid(int is_paid) {
            this.is_paid = is_paid;
        }

        public String getPayment_mode() {
            return payment_mode;
        }

        public void setPayment_mode(String payment_mode) {
            this.payment_mode = payment_mode;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public int getIs_delivered() {
            return is_delivered;
        }

        public void setIs_delivered(int is_delivered) {
            this.is_delivered = is_delivered;
        }

        public String getDelivery_time() {
            return delivery_time;
        }

        public void setDelivery_time(String delivery_time) {
            this.delivery_time = delivery_time;
        }

        public String getDelivery_option() {
            return delivery_option;
        }

        public void setDelivery_option(String delivery_option) {
            this.delivery_option = delivery_option;
        }

        public String getDelivery_date_time() {
           // return "2019-09-23 10:15:20";
            return delivery_date_time;

        }

        public void setDelivery_date_time(String delivery_date_time) {
            this.delivery_date_time = delivery_date_time;
        }

        public String getDelivery_date_time_label() {
            return delivery_date_time_label;
        }

        public void setDelivery_date_time_label(String delivery_date_time_label) {
            this.delivery_date_time_label = delivery_date_time_label;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getDiscount_amount() {
            return discount_amount;
        }

        public void setDiscount_amount(String discount_amount) {
            this.discount_amount = discount_amount;
        }

        public String getVoucher_id() {
            return voucher_id;
        }

        public void setVoucher_id(String voucher_id) {
            this.voucher_id = voucher_id;
        }

        public String getOffer_id() {
            return offer_id;
        }

        public void setOffer_id(String offer_id) {
            this.offer_id = offer_id;
        }

        public String getOrder_notes() {
            return order_notes;
        }

        public void setOrder_notes(String order_notes) {
            this.order_notes = order_notes;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getCustomer_contact() {
            return customer_contact;
        }

        public void setCustomer_contact(String customer_contact) {
            this.customer_contact = customer_contact;
        }

        public String getCustomer_location() {
            return customer_location;
        }

        public void setCustomer_location(String customer_location) {
            this.customer_location = customer_location;
        }

        public String getOrder_status() {
            return order_status;
        }

        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }


        public String getPrev_order() {
            return previous_order;
        }

        public void setPrev_order(String prev_order) {
            this.previous_order = prev_order;
        }

        public int getAverage_delivery_time() {
            return avg_delivery_time;
        }

        public void setAverage_delivery_time(int average_delivery_time) {
            this.avg_delivery_time = average_delivery_time;
        }

        public String getIs_preorder() {
            return is_preorder;
        }

        public void setIs_preorder(String is_preorder) {
            this.is_preorder = is_preorder;
        }

        public String getPrevious_order() {
            return previous_order;
        }

        public void setPrevious_order(String previous_order) {
            this.previous_order = previous_order;
        }

        public int getAvg_delivery_time() {
            return avg_delivery_time;
        }

        public void setAvg_delivery_time(int avg_delivery_time) {
            this.avg_delivery_time = avg_delivery_time;
        }

        @Override
        public String toString() {
            return "Orders{" +
                    "order_id='" + order_id + '\'' +
                    ", restaurant_id='" + restaurant_id + '\'' +
                    ", customer_id='" + customer_id + '\'' +
                    ", cart_id='" + cart_id + '\'' +
                    ", order_num='" + order_num + '\'' +
                    ", order_total='" + order_total + '\'' +
                    ", order_date_time='" + order_date_time + '\'' +
                    ", is_paid=" + is_paid +
                    ", payment_mode='" + payment_mode + '\'' +
                    ", payment_status='" + payment_status + '\'' +
                    ", is_delivered=" + is_delivered +
                    ", delivery_time='" + delivery_time + '\'' +
                    ", delivery_option='" + delivery_option + '\'' +
                    ", delivery_date_time='" + delivery_date_time + '\'' +
                    ", delivery_date_time_label='" + delivery_date_time_label + '\'' +
                    ", delivery_charge='" + delivery_charge + '\'' +
                    ", discount_amount='" + discount_amount + '\'' +
                    ", voucher_id='" + voucher_id + '\'' +
                    ", offer_id='" + offer_id + '\'' +
                    ", order_status='" + order_status + '\'' +
                    ", order_notes='" + order_notes + '\'' +
                    ", customer_name='" + customer_name + '\'' +
                    ", customer_contact='" + customer_contact + '\'' +
                    ", customer_location='" + customer_location + '\'' +
                    ", prev_order='" + previous_order + '\'' +
                    ", average_delivery_time='" + avg_delivery_time + '\'' +
                    '}';
        }
    }


}
