package com.easyfoodvone.app_common.ws;

import java.io.Serializable;
import java.util.List;

public class OrderReportResponse implements Serializable {
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
        return "OrderReportResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public class Data implements Serializable {
        String total_orders;
        String total_items;
        String total_revenue;
        String total_discount;
        String wallet_balance;
        String total_orders_by_credit_card;
        String total_orders_by_credit_card_amount;
        String total_orders_by_cash;
        String total_orders_by_cash_per;
        String total_orders_accepted;
        String total_orders_accepted_per;
        String total_orders_declined;
        String total_orders_declined_per;
        String total_orders_accepted_amount;
        String total_orders_declined_amount;
        String total_orders_by_cash_amount;
        String total_taxes;
        String restaurant_wallet;
        List<OrdersList> order_list;


        public String getTotal_taxes() {
            return total_taxes;
        }

        public void setTotal_taxes(String total_taxes) {
            this.total_taxes = total_taxes;
        }

        public String getRestaurant_wallet() {
            return restaurant_wallet;
        }

        public void setRestaurant_wallet(String restaurant_wallet) {
            this.restaurant_wallet = restaurant_wallet;
        }
        public String getTotal_orders_accepted_amount() {
            return total_orders_accepted_amount;
        }

        public void setTotal_orders_accepted_amount(String total_orders_accepted_amount) {
            this.total_orders_accepted_amount = total_orders_accepted_amount;
        }

        public String getTotal_orders() {
            return total_orders;
        }

        public void setTotal_orders(String total_orders) {
            this.total_orders = total_orders;
        }

        public String getTotal_items() {
            return total_items;
        }

        public String getTotal_orders_declined_amount() {
            return total_orders_declined_amount;
        }

        public void setTotal_orders_declined_amount(String total_orders_declined_amount) {
            this.total_orders_declined_amount = total_orders_declined_amount;
        }

        public void setTotal_items(String total_items) {
            this.total_items = total_items;
        }

        public String getTotal_revenue() {
            return total_revenue;
        }

        public void setTotal_revenue(String total_revenue) {
            this.total_revenue = total_revenue;
        }

        public String getTotal_discount() {
            return total_discount;
        }

        public void setTotal_discount(String total_discount) {
            this.total_discount = total_discount;
        }

        public String getWallet_balance() {
            return wallet_balance;
        }

        public void setWallet_balance(String wallet_balance) {
            this.wallet_balance = wallet_balance;
        }

        public String getTotal_orders_by_credit_card() {
            return total_orders_by_credit_card;
        }

        public void setTotal_orders_by_credit_card(String total_orders_by_credit_card) {
            this.total_orders_by_credit_card = total_orders_by_credit_card;
        }

        public String getTotal_orders_by_cash() {
            return total_orders_by_cash;
        }

        public void setTotal_orders_by_cash(String total_orders_by_cash) {
            this.total_orders_by_cash = total_orders_by_cash;
        }

        public String getTotal_orders_by_cash_per() {
            return total_orders_by_cash_per;
        }

        public void setTotal_orders_by_cash_per(String total_orders_by_cash_per) {
            this.total_orders_by_cash_per = total_orders_by_cash_per;
        }

        public String getTotal_orders_accepted() {
            return total_orders_accepted;
        }

        public void setTotal_orders_accepted(String total_orders_accepted) {
            this.total_orders_accepted = total_orders_accepted;
        }

        public String getTotal_orders_accepted_per() {
            return total_orders_accepted_per;
        }

        public String getTotal_orders_by_cash_amount() {
            return total_orders_by_cash_amount;
        }

        public void setTotal_orders_by_cash_amount(String total_orders_by_cash_amount) {
            this.total_orders_by_cash_amount = total_orders_by_cash_amount;
        }

        public void setTotal_orders_accepted_per(String total_orders_accepted_per) {
            this.total_orders_accepted_per = total_orders_accepted_per;
        }

        public String getTotal_orders_declined() {
            return total_orders_declined;
        }

        public void setTotal_orders_declined(String total_orders_declined) {
            this.total_orders_declined = total_orders_declined;
        }

        public String getTotal_orders_declined_per() {
            return total_orders_declined_per;
        }

        public void setTotal_orders_declined_per(String total_orders_declined_per) {
            this.total_orders_declined_per = total_orders_declined_per;
        }

        public List<OrdersList> getOrder_list() {
            return order_list;
        }

        public void setOrder_list(List<OrdersList> order_list) {
            this.order_list = order_list;
        }

        public String getTotal_orders_by_credit_card_amount() {
            return total_orders_by_credit_card_amount;
        }

        public void setTotal_orders_by_credit_card_amount(String total_orders_by_credit_card_amount) {
            this.total_orders_by_credit_card_amount = total_orders_by_credit_card_amount;
        }

        @Override
        public String toString() {
            return "OrdersDetailsTop{" +
                    "total_orders='" + total_orders + '\'' +
                    ", total_items='" + total_items + '\'' +
                    ", total_revenue='" + total_revenue + '\'' +
                    ", total_discount='" + total_discount + '\'' +
                    ", wallet_balance='" + wallet_balance + '\'' +
                    ", total_orders_by_credit_card='" + total_orders_by_credit_card + '\'' +
                    ", total_orders_by_cash='" + total_orders_by_cash + '\'' +
                    ", total_orders_by_cash_per='" + total_orders_by_cash_per + '\'' +
                    ", total_orders_accepted='" + total_orders_accepted + '\'' +
                    ", total_orders_accepted_per='" + total_orders_accepted_per + '\'' +
                    ", total_orders_declined='" + total_orders_declined + '\'' +
                    ", total_orders_declined_per='" + total_orders_declined_per + '\'' +
                    ", order_list=" + order_list +
                    '}';
        }
    }

    public class OrdersList implements Serializable {
        String order_id;
        String customer_post_code;
        String order_date;
        String total_items;
        String order_subtotal;
        String order_total;
        String discount_amount;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getCustomer_post_code() {
            return customer_post_code;
        }

        public void setCustomer_post_code(String customer_post_code) {
            this.customer_post_code = customer_post_code;
        }

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        public String getTotal_items() {
            return total_items;
        }

        public void setTotal_items(String total_items) {
            this.total_items = total_items;
        }

        public String getOrder_subtotal() {
            return order_subtotal;
        }

        public void setOrder_subtotal(String order_subtotal) {
            this.order_subtotal = order_subtotal;
        }

        public String getOrder_total() {
            return order_total;
        }

        public void setOrder_total(String order_total) {
            this.order_total = order_total;
        }

        public String getDiscount_amount() {
            return discount_amount;
        }

        public void setDiscount_amount(String discount_amount) {
            this.discount_amount = discount_amount;
        }

        @Override
        public String toString() {
            return "OrdersList{" +
                    "order_id='" + order_id + '\'' +
                    ", customer_post_code='" + customer_post_code + '\'' +
                    ", order_date='" + order_date + '\'' +
                    ", total_items='" + total_items + '\'' +
                    ", order_subtotal='" + order_subtotal + '\'' +
                    ", order_total='" + order_total + '\'' +
                    ", discount_amount='" + discount_amount + '\'' +
                    '}';
        }
    }

}
