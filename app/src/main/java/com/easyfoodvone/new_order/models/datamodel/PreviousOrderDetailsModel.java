package com.easyfoodvone.new_order.models.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PreviousOrderDetailsModel {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("errors")
    @Expose
    private Errors errors;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
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

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }



    public class Data {
        @SerializedName("restaurant_name")
        @Expose
        private String restaurantName;
        @SerializedName("restaurant_logo")
        @Expose
        private String restaurantLogo;
        @SerializedName("restaurant_image")
        @Expose
        private String restaurantImage;
        @SerializedName("avg_rating")
        @Expose
        private String avgRating;
        @SerializedName("restaurant_cuisines")
        @Expose
        private String restaurantCuisines;
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("restaurant_id")
        @Expose
        private String restaurantId;
        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("cart_id")
        @Expose
        private String cartId;
        @SerializedName("order_num")
        @Expose
        private String orderNum;
        @SerializedName("order_total")
        @Expose
        private String orderTotal;
        @SerializedName("order_subtotal")
        @Expose
        private String orderSubtotal;
        @SerializedName("order_date_time")
        @Expose
        private String orderDateTime;
        @SerializedName("is_paid")
        @Expose
        private Integer isPaid;
        @SerializedName("payment_mode")
        @Expose
        private String paymentMode;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("is_delivered")
        @Expose
        private Integer isDelivered;
        @SerializedName("delivery_time")
        @Expose
        private String deliveryTime;
        @SerializedName("delivery_option")
        @Expose
        private String deliveryOption;
        @SerializedName("delivery_date_time")
        @Expose
        private String deliveryDateTime;
        @SerializedName("delivery_charge")
        @Expose
        private String deliveryCharge;
        @SerializedName("discount_amount")
        @Expose
        private String discountAmount;
        @SerializedName("voucher_id")
        @Expose
        private String voucherId;
        @SerializedName("offer_id")
        @Expose
        private String offerId;
        @SerializedName("order_notes")
        @Expose
        private String orderNotes;
        @SerializedName("order_reject_note")
        @Expose
        private String orderRejectNote;
        @SerializedName("order_status")
        @Expose
        private String orderStatus;
        @SerializedName("order_review_rating")
        @Expose
        private Integer orderReviewRating;
        @SerializedName("is_tomorrow")
        @Expose
        private Integer isTomorrow;
        @SerializedName("order_details")
        @Expose
        private OrderDetails orderDetails;
        @SerializedName("order_details_new")
        @Expose
        private List<Object> orderDetailsNew = null;
        @SerializedName("total")
        @Expose
        private Integer total;
        @SerializedName("customer_delivery_address")
        @Expose
        private CustomerDeliveryAddress customerDeliveryAddress;
        @SerializedName("customer_billing_address")
        @Expose
        private CustomerBillingAddress customerBillingAddress;

        public String getRestaurantName() {
            return restaurantName;
        }

        public void setRestaurantName(String restaurantName) {
            this.restaurantName = restaurantName;
        }

        public String getRestaurantLogo() {
            return restaurantLogo;
        }

        public void setRestaurantLogo(String restaurantLogo) {
            this.restaurantLogo = restaurantLogo;
        }

        public String getRestaurantImage() {
            return restaurantImage;
        }

        public void setRestaurantImage(String restaurantImage) {
            this.restaurantImage = restaurantImage;
        }

        public String getAvgRating() {
            return avgRating;
        }

        public void setAvgRating(String avgRating) {
            this.avgRating = avgRating;
        }

        public String getRestaurantCuisines() {
            return restaurantCuisines;
        }

        public void setRestaurantCuisines(String restaurantCuisines) {
            this.restaurantCuisines = restaurantCuisines;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(String restaurantId) {
            this.restaurantId = restaurantId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getOrderTotal() {
            return orderTotal;
        }

        public void setOrderTotal(String orderTotal) {
            this.orderTotal = orderTotal;
        }

        public String getOrderSubtotal() {
            return orderSubtotal;
        }

        public void setOrderSubtotal(String orderSubtotal) {
            this.orderSubtotal = orderSubtotal;
        }

        public String getOrderDateTime() {
            return orderDateTime;
        }

        public void setOrderDateTime(String orderDateTime) {
            this.orderDateTime = orderDateTime;
        }

        public Integer getIsPaid() {
            return isPaid;
        }

        public void setIsPaid(Integer isPaid) {
            this.isPaid = isPaid;
        }

        public String getPaymentMode() {
            return paymentMode;
        }

        public void setPaymentMode(String paymentMode) {
            this.paymentMode = paymentMode;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public Integer getIsDelivered() {
            return isDelivered;
        }

        public void setIsDelivered(Integer isDelivered) {
            this.isDelivered = isDelivered;
        }

        public String getDeliveryTime() {
            return deliveryTime;
        }

        public void setDeliveryTime(String deliveryTime) {
            this.deliveryTime = deliveryTime;
        }

        public String getDeliveryOption() {
            return deliveryOption;
        }

        public void setDeliveryOption(String deliveryOption) {
            this.deliveryOption = deliveryOption;
        }

        public String getDeliveryDateTime() {
            return deliveryDateTime;
        }

        public void setDeliveryDateTime(String deliveryDateTime) {
            this.deliveryDateTime = deliveryDateTime;
        }

        public String getDeliveryCharge() {
            return deliveryCharge;
        }

        public void setDeliveryCharge(String deliveryCharge) {
            this.deliveryCharge = deliveryCharge;
        }

        public String getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }

        public String getVoucherId() {
            return voucherId;
        }

        public void setVoucherId(String voucherId) {
            this.voucherId = voucherId;
        }

        public String getOfferId() {
            return offerId;
        }

        public void setOfferId(String offerId) {
            this.offerId = offerId;
        }

        public String getOrderNotes() {
            return orderNotes;
        }

        public void setOrderNotes(String orderNotes) {
            this.orderNotes = orderNotes;
        }

        public String getOrderRejectNote() {
            return orderRejectNote;
        }

        public void setOrderRejectNote(String orderRejectNote) {
            this.orderRejectNote = orderRejectNote;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public Integer getOrderReviewRating() {
            return orderReviewRating;
        }

        public void setOrderReviewRating(Integer orderReviewRating) {
            this.orderReviewRating = orderReviewRating;
        }

        public Integer getIsTomorrow() {
            return isTomorrow;
        }

        public void setIsTomorrow(Integer isTomorrow) {
            this.isTomorrow = isTomorrow;
        }

        public OrderDetails getOrderDetails() {
            return orderDetails;
        }

        public void setOrderDetails(OrderDetails orderDetails) {
            this.orderDetails = orderDetails;
        }

        public List<Object> getOrderDetailsNew() {
            return orderDetailsNew;
        }

        public void setOrderDetailsNew(List<Object> orderDetailsNew) {
            this.orderDetailsNew = orderDetailsNew;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public CustomerDeliveryAddress getCustomerDeliveryAddress() {
            return customerDeliveryAddress;
        }

        public void setCustomerDeliveryAddress(CustomerDeliveryAddress customerDeliveryAddress) {
            this.customerDeliveryAddress = customerDeliveryAddress;
        }

        public CustomerBillingAddress getCustomerBillingAddress() {
            return customerBillingAddress;
        }

        public void setCustomerBillingAddress(CustomerBillingAddress customerBillingAddress) {
            this.customerBillingAddress = customerBillingAddress;
        }

    }

    public class Errors {
    }

    public class CustomerBillingAddress {
    }

    public class CustomerDeliveryAddress {
        @SerializedName("customer_delivery_address_id")
        @Expose
        private String customerDeliveryAddressId;
        @SerializedName("customer_delivery_address_1")
        @Expose
        private String customerDeliveryAddress1;
        @SerializedName("customer_delivery_address_2")
        @Expose
        private String customerDeliveryAddress2;
        @SerializedName("customer_delivery_city")
        @Expose
        private String customerDeliveryCity;
        @SerializedName("customer_delivery_post_code")
        @Expose
        private String customerDeliveryPostCode;
        @SerializedName("customer_delivery_country")
        @Expose
        private String customerDeliveryCountry;
        @SerializedName("customer_delivery_address_type")
        @Expose
        private String customerDeliveryAddressType;

        public String getCustomerDeliveryAddressId() {
            return customerDeliveryAddressId;
        }

        public void setCustomerDeliveryAddressId(String customerDeliveryAddressId) {
            this.customerDeliveryAddressId = customerDeliveryAddressId;
        }

        public String getCustomerDeliveryAddress1() {
            return customerDeliveryAddress1;
        }

        public void setCustomerDeliveryAddress1(String customerDeliveryAddress1) {
            this.customerDeliveryAddress1 = customerDeliveryAddress1;
        }

        public String getCustomerDeliveryAddress2() {
            return customerDeliveryAddress2;
        }

        public void setCustomerDeliveryAddress2(String customerDeliveryAddress2) {
            this.customerDeliveryAddress2 = customerDeliveryAddress2;
        }

        public String getCustomerDeliveryCity() {
            return customerDeliveryCity;
        }

        public void setCustomerDeliveryCity(String customerDeliveryCity) {
            this.customerDeliveryCity = customerDeliveryCity;
        }

        public String getCustomerDeliveryPostCode() {
            return customerDeliveryPostCode;
        }

        public void setCustomerDeliveryPostCode(String customerDeliveryPostCode) {
            this.customerDeliveryPostCode = customerDeliveryPostCode;
        }

        public String getCustomerDeliveryCountry() {
            return customerDeliveryCountry;
        }

        public void setCustomerDeliveryCountry(String customerDeliveryCountry) {
            this.customerDeliveryCountry = customerDeliveryCountry;
        }

        public String getCustomerDeliveryAddressType() {
            return customerDeliveryAddressType;
        }

        public void setCustomerDeliveryAddressType(String customerDeliveryAddressType) {
            this.customerDeliveryAddressType = customerDeliveryAddressType;
        }
    }
}
