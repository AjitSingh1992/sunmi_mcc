package com.easyfoodvone.models.menu_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrdersDetailsTop {

    @SerializedName("average_delivery_time")
    String average_delivery_time;
    @SerializedName("average_preparation_time")
    String average_preparation_time;
    @SerializedName("order_id")
    String order_id;
    @SerializedName("restaurant_id")
    String restaurant_id;
    @SerializedName("customer_id")
    String customer_id;
    @SerializedName("cart_id")
    String cart_id;
    @SerializedName("order_num")
    String order_num;
    @SerializedName("order_total")
    String order_total;
    @SerializedName("order_date_time")
    String order_date_time;
    @SerializedName("is_paid")
    int is_paid;
    @SerializedName("payment_mode")
    String payment_mode;
    @SerializedName("payment_status")
    String payment_status;
    @SerializedName("is_delivered")
    int is_delivered;
    @SerializedName("delivery_time")
    String delivery_time;
    @SerializedName("delivery_option")
    String delivery_option;
    @SerializedName("delivery_date_time")
    String delivery_date_time;
    @SerializedName("delivery_charge")
    String delivery_charge;
    @SerializedName("discount_amount")
    String discount_amount;
    @SerializedName("sub_total")
    String sub_total;
    @SerializedName("voucher_id")
    String voucher_id;
    @SerializedName("offer_id")
    String offer_id;
    @SerializedName("order_notes")
    String order_notes;
    @SerializedName("order_reject_note")
    String order_reject_note;

    @SerializedName("address_1")
    String address_1;
    @SerializedName("address_2")
    String address_2;
    @SerializedName("city")
    String city;
    @SerializedName("post_code")
    String post_code;
    @SerializedName("country")
    String country;



    @SerializedName("cart")
    Cart cart;

    @SerializedName("delivery_address")
    DeliveryDetails deliveryDetails;

    public OrdersDetailsTop(String average_delivery_time, String average_preparation_time, String order_id, String restaurant_id, String customer_id, String cart_id, String order_num, String order_total, String order_date_time, int is_paid, String payment_mode, String payment_status, int is_delivered, String delivery_time, String delivery_option, String delivery_date_time, String delivery_charge, String discount_amount, String sub_total, String voucher_id, String offer_id, String order_notes, String order_reject_note, Cart cart, DeliveryDetails deliveryDetails,
                            String address_1,String address_2,String city,String post_code,String country) {
        this.average_delivery_time = average_delivery_time;
        this.average_preparation_time = average_preparation_time;
        this.order_id = order_id;
        this.restaurant_id = restaurant_id;
        this.customer_id = customer_id;
        this.cart_id = cart_id;
        this.order_num = order_num;
        this.order_total = order_total;
        this.order_date_time = order_date_time;
        this.is_paid = is_paid;
        this.payment_mode = payment_mode;
        this.payment_status = payment_status;
        this.is_delivered = is_delivered;
        this.delivery_time = delivery_time;
        this.delivery_option = delivery_option;
        this.delivery_date_time = delivery_date_time;
        this.delivery_charge = delivery_charge;
        this.discount_amount = discount_amount;
        this.sub_total = sub_total;
        this.voucher_id = voucher_id;
        this.offer_id = offer_id;
        this.order_notes = order_notes;
        this.order_reject_note = order_reject_note;
        this.cart = cart;
        this.deliveryDetails = deliveryDetails;

        this.address_1 = address_1;
        this.address_2 = address_2;
        this.city = city;
        this.post_code = post_code;
        this.country = country;
    }

    public String getAverage_delivery_time() {
        return average_delivery_time;
    }

    public void setAverage_delivery_time(String average_delivery_time) {
        this.average_delivery_time = average_delivery_time;
    }

    public String getAverage_preparation_time() {
        return average_preparation_time;
    }

    public void setAverage_preparation_time(String average_preparation_time) {
        this.average_preparation_time = average_preparation_time;
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
        return delivery_date_time;
    }

    public void setDelivery_date_time(String delivery_date_time) {
        this.delivery_date_time = delivery_date_time;
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

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
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

    public String getOrder_reject_note() {
        return order_reject_note;
    }

    public void setOrder_reject_note(String order_reject_note) {
        this.order_reject_note = order_reject_note;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
    }

    @Override
    public String toString() {
        return "OrdersDetailsTop{" +
                "average_delivery_time='" + average_delivery_time + '\'' +
                ", average_preparation_time='" + average_preparation_time + '\'' +
                ", order_id='" + order_id + '\'' +
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
                ", delivery_charge='" + delivery_charge + '\'' +
                ", discount_amount='" + discount_amount + '\'' +
                ", sub_total='" + sub_total + '\'' +
                ", voucher_id='" + voucher_id + '\'' +
                ", offer_id='" + offer_id + '\'' +
                ", order_notes='" + order_notes + '\'' +
                ", order_reject_note='" + order_reject_note + '\'' +
                ", cart=" + cart +
                '}';
    }

    public static class Cart {
        @SerializedName("deliveryCharge")
        String deliveryCharge;
        @SerializedName("maxLength")
        String maxLength;
        @SerializedName("orderType")
        String orderType;
        @SerializedName("org_delivery_charge")
        String org_delivery_charge;
        @SerializedName("restaurantId")
        String restaurantId;
        @SerializedName("restaurantName")
        String restaurantName;
        @SerializedName("restaurantSlug")
        String restaurantSlug;
        @SerializedName("totalCartPrice")
        String totalCartPrice;
        @SerializedName("voucherCode")
        String voucherCode;
        @SerializedName("voucherDiscount")
        String voucherDiscount;

        @SerializedName("menu")
        @Expose
        private Menu menu;

        public Cart(String deliveryCharge, String maxLength, String orderType, String org_delivery_charge, String restaurantId, String restaurantName, String restaurantSlug, String totalCartPrice, String voucherCode, String voucherDiscount, Menu menu) {
            this.deliveryCharge = deliveryCharge;
            this.maxLength = maxLength;
            this.orderType = orderType;
            this.org_delivery_charge = org_delivery_charge;
            this.restaurantId = restaurantId;
            this.restaurantName = restaurantName;
            this.restaurantSlug = restaurantSlug;
            this.totalCartPrice = totalCartPrice;
            this.voucherCode = voucherCode;
            this.voucherDiscount = voucherDiscount;
            this.menu = menu;
        }

        public String getDeliveryCharge() {
            return deliveryCharge;
        }

        public void setDeliveryCharge(String deliveryCharge) {
            this.deliveryCharge = deliveryCharge;
        }

        public String getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(String maxLength) {
            this.maxLength = maxLength;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrg_delivery_charge() {
            return org_delivery_charge;
        }

        public void setOrg_delivery_charge(String org_delivery_charge) {
            this.org_delivery_charge = org_delivery_charge;
        }

        public String getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(String restaurantId) {
            this.restaurantId = restaurantId;
        }

        public String getRestaurantName() {
            return restaurantName;
        }

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
        }

        public String getRestaurantSlug() {
            return restaurantSlug;
        }

        public void setRestaurantSlug(String restaurantSlug) {
            this.restaurantSlug = restaurantSlug;
        }

        public String getTotalCartPrice() {
            return totalCartPrice;
        }

        public void setTotalCartPrice(String totalCartPrice) {
            this.totalCartPrice = totalCartPrice;
        }

        public String getVoucherCode() {
            return voucherCode;
        }

        public void setVoucherCode(String voucherCode) {
            this.voucherCode = voucherCode;
        }

        public String getVoucherDiscount() {
            return voucherDiscount;
        }

        public void setVoucherDiscount(String voucherDiscount) {
            this.voucherDiscount = voucherDiscount;
        }

        public Menu getMenu() {
            return menu;
        }

        public void setMenu(Menu menu) {
            this.menu = menu;
        }

        @Override
        public String toString() {
            return "Cart{" +
                    "deliveryCharge='" + deliveryCharge + '\'' +
                    ", maxLength='" + maxLength + '\'' +
                    ", orderType='" + orderType + '\'' +
                    ", org_delivery_charge='" + org_delivery_charge + '\'' +
                    ", restaurantId='" + restaurantId + '\'' +
                    ", restaurantName='" + restaurantName + '\'' +
                    ", restaurantSlug='" + restaurantSlug + '\'' +
                    ", totalCartPrice='" + totalCartPrice + '\'' +
                    ", voucherCode='" + voucherCode + '\'' +
                    ", voucherDiscount='" + voucherDiscount + '\'' +
                    ", menu=" + menu +
                    '}';
        }
    }

    public class DeliveryDetails implements Serializable {

        String customer_name;
        String customer_address_id;
        String phone_number;
        String customer_location;
        String email;
        String address_1;
        String address_2;
        String city;
        String post_code;
        String country;
        String address_type;

        public String getAddress_type() {
            return address_type;
        }

        public void setAddress_type(String address_type) {
            this.address_type = address_type;
        }

        public String getAddress_1() {
            return address_1;
        }

        public void setAddress_1(String address_1) {
            this.address_1 = address_1;
        }

        public String getAddress_2() {
            return address_2;
        }

        public void setAddress_2(String address_2) {
            this.address_2 = address_2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPost_code() {
            return post_code;
        }

        public void setPost_code(String post_code) {
            this.post_code = post_code;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }


        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getCustomer_address_id() {
            return customer_address_id;
        }

        public void setCustomer_address_id(String customer_address_id) {
            this.customer_address_id = customer_address_id;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getCustomer_location() {
            return customer_location;
        }

        public void setCustomer_location(String customer_location) {
            this.customer_location = customer_location;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "DeliveryDetails{" +
                    "customer_name='" + customer_name + '\'' +
                    ", customer_address_id='" + customer_address_id + '\'' +
                    ", phone_number='" + phone_number + '\'' +
                    ", customer_location='" + customer_location + '\'' +
                    ", address_1='" + address_1 + '\'' +
                    ", address_2='" + address_2 + '\'' +
                    ", city='" + city + '\'' +
                    ", post_code='" + post_code + '\'' +
                    ", country='" + country + '\'' +
                    ", address_type='" + address_type + '\'' +
                    '}';
        }

    }

}