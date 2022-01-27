package com.easyfoodvone.models;

import java.io.Serializable;

public class RevenueReportResponse implements Serializable
{
    boolean success;
    String message;
    RevenueReportDetail total_orders;

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

    public RevenueReportDetail getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(RevenueReportDetail total_orders) {
        this.total_orders = total_orders;
    }

    @Override
    public String toString() {
        return "RevenueReportResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", total_orders=" + total_orders +
                '}';
    }

    public class RevenueReportDetail implements Serializable{
        String  total_orders;
        String  total_discount;
        String  total_delivery_charge;
        String  total_product_sold;
        String  total_taxes_applied;
        String  total_discount_applied;
        String  total_revenue_collected;
        String  gross_profit;


        public String getTotal_orders() {
            return total_orders;
        }

        public void setTotal_orders(String total_orders) {
            this.total_orders = total_orders;
        }

        public String getTotal_discount() {
            return total_discount;
        }

        public void setTotal_discount(String total_discount) {
            this.total_discount = total_discount;
        }

        public String getTotal_delivery_charge() {
            return total_delivery_charge;
        }

        public void setTotal_delivery_charge(String total_delivery_charge) {
            this.total_delivery_charge = total_delivery_charge;
        }

        public String getTotal_product_sold() {
            return total_product_sold;
        }

        public void setTotal_product_sold(String total_product_sold) {
            this.total_product_sold = total_product_sold;
        }

        public String getTotal_taxes_applied() {
            return total_taxes_applied;
        }

        public void setTotal_taxes_applied(String total_taxes_applied) {
            this.total_taxes_applied = total_taxes_applied;
        }

        public String getTotal_discount_applied() {
            return total_discount_applied;
        }

        public void setTotal_discount_applied(String total_discount_applied) {
            this.total_discount_applied = total_discount_applied;
        }

        public String getTotal_revenue_collected() {
            return total_revenue_collected;
        }

        public void setTotal_revenue_collected(String total_revenue_collected) {
            this.total_revenue_collected = total_revenue_collected;
        }

        public String getGross_profit() {
            return gross_profit;
        }

        public void setGross_profit(String gross_profit) {
            this.gross_profit = gross_profit;
        }

        @Override
        public String toString() {
            return "RevenueReportDetail{" +
                    "total_orders='" + total_orders + '\'' +
                    ", total_discount='" + total_discount + '\'' +
                    ", total_delivery_charge='" + total_delivery_charge + '\'' +
                    ", total_product_sold='" + total_product_sold + '\'' +
                    ", total_taxes_applied='" + total_taxes_applied + '\'' +
                    ", total_discount_applied='" + total_discount_applied + '\'' +
                    ", total_revenue_collected='" + total_revenue_collected + '\'' +
                    ", gross_profit='" + gross_profit + '\'' +
                    '}';
        }
    }
}
