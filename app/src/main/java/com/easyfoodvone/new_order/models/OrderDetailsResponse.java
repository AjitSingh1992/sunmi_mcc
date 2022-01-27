package com.easyfoodvone.new_order.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderDetailsResponse implements Serializable {
    boolean success;
    String message;
    OrderDetails orders_details;

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

    public OrderDetails getOrders_details() {
        return orders_details;
    }

    public void setOrders_details(OrderDetails orders_details) {
        this.orders_details = orders_details;
    }

    @Override
    public String toString() {
        return "OrderDetailsResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", orders_details=" + orders_details +
                '}';
    }

    public class OrderDetails implements Serializable {
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
        String delivery_charge;
        String discount_amount;
        String sub_total;
        String voucher_id;
        String offer_id;
        String order_notes;
        String customer_name;
        String contact_number;
        int average_delivery_time;
        int average_preparation_time;


        public int getAverage_preparation_time() {
            return average_preparation_time;
        }

        public void setAverage_preparation_time(int average_preparation_time) {
            this.average_preparation_time = average_preparation_time;
        }

        DeliveryDetails delivery_address;
        List<Cart> cart;

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

        public DeliveryDetails getDelivery_address() {
            return delivery_address;
        }

        public void setDelivery_address(DeliveryDetails delivery_address) {
            this.delivery_address = delivery_address;
        }

        public List<Cart> getCart() {
            return cart;
        }

        public void setCart(List<Cart> cart) {
            this.cart = cart;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getContact_number() {
            return contact_number;
        }

        public void setContact_number(String contact_number) {
            this.contact_number = contact_number;
        }

        public int getAverage_delivery_time() {
            return average_delivery_time;
        }

        public void setAverage_delivery_time(int average_delivery_time) {
            this.average_delivery_time = average_delivery_time;
        }

        @Override
        public String toString() {
            return "OrderDetails{" +
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
                    ", delivery_charge='" + delivery_charge + '\'' +
                    ", discount_amount='" + discount_amount + '\'' +
                    ", sub_total='" + sub_total + '\'' +
                    ", voucher_id='" + voucher_id + '\'' +
                    ", offer_id='" + offer_id + '\'' +
                    ", order_notes='" + order_notes + '\'' +
                    ", customer_name='" + customer_name + '\'' +
                    ", contact_number='" + contact_number + '\'' +
                    ", average_delivery_time='" + average_delivery_time + '\'' +
                    ", delivery_address=" + delivery_address +
                    ", cart=" + cart +
                    '}';
        }


        public class Cart implements Serializable {
            @SerializedName("menu_category_name")
            String categoryName;
            @SerializedName("menu_products")
            List<Items> items;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public List<Items> getItems() {
                return items;
            }

            public void setItems(List<Items> items) {
                this.items = items;
            }

            @Override
            public String toString() {
                return "Cart{" +
                        "categoryName='" + categoryName + '\'' +
                        ", items=" + items +
                        '}';
            }

            public class Items implements Serializable {
                String product_id;

                @SerializedName("product_name")
                String product_name;

                @SerializedName("product_quantity")
                String product_qty;

                @SerializedName("product_price")
                String product_price;

                @SerializedName("product_amount")
                String total_amount;

                String type;

                @SerializedName("modifiers")
                List<Items> modifiers;

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

                public String getProduct_qty() {
                    return product_qty;
                }

                public void setProduct_qty(String product_qty) {
                    this.product_qty = product_qty;
                }

                public String getProduct_price() {
                    return product_price;
                }

                public void setProduct_price(String product_price) {
                    this.product_price = product_price;
                }

                public String getTotal_amount() {
                    return total_amount;
                }

                public void setTotal_amount(String total_amount) {
                    this.total_amount = total_amount;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public List<Items> getModifiers() {
                    return modifiers;
                }

                public void setModifiers(List<Items> modifiers) {
                    this.modifiers = modifiers;
                }

                @Override
                public String toString() {
                    return "Items{" +
                            "product_id='" + product_id + '\'' +
                            ", product_name='" + product_name + '\'' +
                            ", product_qty='" + product_qty + '\'' +
                            ", product_price='" + product_price + '\'' +
                            ", total_amount='" + total_amount + '\'' +
                            ", type='" + type + '\'' +
                            ", modifiers=" + modifiers +
                            '}';
                }
            }


        }
    }


    public class DeliveryDetails implements Serializable {
        String customer_name;
        String customer_address_id;
        String phone_number;
        String customer_location;
        String email;

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
                    '}';
        }
    }


}
