package com.easyfoodvone.app_common.ws;

import java.util.List;

public class NewDetailBean {
    private boolean success;
    private String message;
    private OrdersDetailsBean orders_details;
    private ErrorsBean errors;

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

    public OrdersDetailsBean getOrders_details() {
        return orders_details;
    }

    public void setOrders_details(OrdersDetailsBean orders_details) {
        this.orders_details = orders_details;
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public static class OrdersDetailsBean {
        private String average_delivery_time;
        private String average_preparation_time;
        private String order_id;
        private String restaurant_id;
        private String customer_id;
        private String cart_id;
        private String order_num;
        private String order_total;
        private String order_date_time;
        private Integer is_paid;
        private String payment_mode;
        private String payment_status;
        private Integer is_delivered;
        private String delivery_time;
        private String delivery_option;
        private String delivery_date_time;
        private String delivery_charge;
        private String discount_amount;
        private String delivery_pin;
        private Double sub_total;
        private String order_phone_number;
        private String voucher_id;
        private String offer_id;
        private String order_notes;
        private String order_reject_note;
        private String orderpdf;

        public String getUnitType() {
            return unitType;
        }

        public void setUnitType(String unitType) {
            this.unitType = unitType;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        private String unitType;
        private String unitId;

        private Cart cart;
        private DeliveryAddress delivery_address;

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

        public Integer getIs_paid() {
            return is_paid;
        }

        public void setIs_paid(Integer is_paid) {
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

        public Integer getIs_delivered() {
            return is_delivered;
        }

        public void setIs_delivered(Integer is_delivered) {
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
        public String getDelivery_pin() {
            return delivery_pin;
        }

        public void setDelivery_pin(String delivery_pin) {
            this.delivery_pin = delivery_pin;
        }

        public Double getSub_total() {
            return sub_total;
        }

        public void setSub_total(Double sub_total) {
            this.sub_total = sub_total;
        }

        public String getOrder_phone_number() {
            return order_phone_number;
        }

        public void setOrder_phone_number(String order_phone_number) {
            this.order_phone_number = order_phone_number;
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

        public String getOrderpdf() {
            return orderpdf;
        }

        public void setOrderpdf(String orderpdf) {
            this.orderpdf = orderpdf;
        }

        public Cart getCart() {
            return cart;
        }

        public void setCart(Cart cart) {
            this.cart = cart;
        }

        public DeliveryAddress getDelivery_address() {
            return delivery_address;
        }

        public void setDelivery_address(DeliveryAddress delivery_address) {
            this.delivery_address = delivery_address;
        }


        public static class Cart {
            private List<MenuBean> menu;

            public List<MenuBean> getMenu() {
                return menu;
            }

            public void setMenu(List<MenuBean> menu) {
                this.menu = menu;
            }

            public static class MenuBean {


                private String rowId;
                private String id;
                private String name;
                private int qty;
                private Double price;
                private String tax;
                private String productType;
                private OptionsBean options;
                private Double subtotal;

                public String getRowId() {
                    return rowId;
                }

                public void setRowId(String rowId) {
                    this.rowId = rowId;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getQty() {
                    return qty;
                }

                public void setQty(int qty) {
                    this.qty = qty;
                }

                public Double getPrice() {
                    return price;
                }

                public void setPrice(Double price) {
                    this.price = price;
                }

                public String getTax() {
                    return tax;
                }

                public void setTax(String tax) {
                    this.tax = tax;
                }

                public String getProductType() {
                    return productType;
                }

                public void setProductType(String productType) {
                    this.productType = productType;
                }

                public OptionsBean getOptions() {
                    return options;
                }

                public void setOptions(OptionsBean options) {
                    this.options = options;
                }

                public Double getSubtotal() {
                    return subtotal;
                }

                public void setSubtotal(Double subtotal) {
                    this.subtotal = subtotal;
                }

                public static class OptionsBean {
                    private List<ProductModifiersBean> productModifiers;
                    private List<MealProduct> mealProducts;
                    private Size size;

                    public Size getSize() {
                        return size;
                    }

                    public void setSizes(Size size) {
                        this.size = size;
                    }


                    public List<ProductModifiersBean> getProductModifiers() {
                        return productModifiers;
                    }

                    public void setProductModifiers(List<ProductModifiersBean> productModifiers) {
                        this.productModifiers = productModifiers;
                    }

                    public List<MealProduct> getMealProducts() {
                        return mealProducts;
                    }

                    public void setMealProducts(List<MealProduct> mealProducts) {
                        this.mealProducts = mealProducts;
                    }


                    public static class Size {
                        private String id;
                        private String productSizeName;
                        private Double productSizePrice;
                        private int quantity;
                        private List<Sizemodifier> sizemodifiers;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
                        }

                        public String getProductSizeName() {
                            return productSizeName;
                        }

                        public void setProductSizeName(String productSizeName) {
                            this.productSizeName = productSizeName;
                        }

                        public Double getProductSizePrice() {
                            return productSizePrice;
                        }

                        public void setProductSizePrice(Double productSizePrice) {
                            this.productSizePrice = productSizePrice;
                        }

                        public int getQuantity() {
                            return quantity;
                        }

                        public void setQuantity(int quantity) {
                            this.quantity = quantity;
                        }

                        public List<Sizemodifier> getSizemodifiers() {
                            return sizemodifiers;
                        }

                        public void setSizemodifiers(List<Sizemodifier> sizemodifiers) {
                            this.sizemodifiers = sizemodifiers;
                        }


                        public static class Sizemodifier {
                            private String modifierId;
                            private Integer minAllowedQuantity;
                            private Integer maxAllowedQuantity;
                            private String modifierType;
                            private String modifierName;

                            public String getModifierName() {
                                return modifierName;
                            }

                            public void setModifierName(String modifierName) {
                                this.modifierName = modifierName;
                            }


                            private List<SizeModifierProduct> sizeModifierProducts;

                            public String getModifierId() {
                                return modifierId;
                            }

                            public void setModifierId(String modifierId) {
                                this.modifierId = modifierId;
                            }

                            public Integer getMinAllowedQuantity() {
                                return minAllowedQuantity;
                            }

                            public void setMinAllowedQuantity(Integer minAllowedQuantity) {
                                this.minAllowedQuantity = minAllowedQuantity;
                            }

                            public Integer getMaxAllowedQuantity() {
                                return maxAllowedQuantity;
                            }

                            public void setMaxAllowedQuantity(Integer maxAllowedQuantity) {
                                this.maxAllowedQuantity = maxAllowedQuantity;
                            }

                            public String getModifierType() {
                                return modifierType;
                            }

                            public void setModifierType(String modifierType) {
                                this.modifierType = modifierType;
                            }

                            public List<SizeModifierProduct> getSizeModifierProducts() {
                                return sizeModifierProducts;
                            }

                            public void setSizeModifierProducts(List<SizeModifierProduct> sizeModifierProducts) {
                                this.sizeModifierProducts = sizeModifierProducts;
                            }


                            public class SizeModifierProduct {
                                private String modifierProductPrice;
                                private Double amount;
                                private String productId;
                                private String productName;
                                private Object unit;
                                private String quantity;
                                private String originalQuantity;
                                private Double originalAmount;
                                private Double originalAmount1;

                                public String getModifierProductPrice() {
                                    return modifierProductPrice;
                                }

                                public void setModifierProductPrice(String modifierProductPrice) {
                                    this.modifierProductPrice = modifierProductPrice;
                                }

                                public Double getAmount() {
                                    return amount;
                                }

                                public void setAmount(Double amount) {
                                    this.amount = amount;
                                }

                                public String getProductId() {
                                    return productId;
                                }

                                public void setProductId(String productId) {
                                    this.productId = productId;
                                }

                                public String getProductName() {
                                    return productName;
                                }

                                public void setProductName(String productName) {
                                    this.productName = productName;
                                }

                                public Object getUnit() {
                                    return unit;
                                }

                                public void setUnit(Object unit) {
                                    this.unit = unit;
                                }

                                public String getQuantity() {
                                    return quantity;
                                }

                                public void setQuantity(String quantity) {
                                    this.quantity = quantity;
                                }

                                public String getOriginalQuantity() {
                                    return originalQuantity;
                                }

                                public void setOriginalQuantity(String originalQuantity) {
                                    this.originalQuantity = originalQuantity;
                                }

                                public Double getOriginalAmount() {
                                    return originalAmount;
                                }

                                public void setOriginalAmount(Double originalAmount) {
                                    this.originalAmount = originalAmount;
                                }

                                public Double getOriginalAmount1() {
                                    return originalAmount1;
                                }

                                public void setOriginalAmount1(Double originalAmount1) {
                                    this.originalAmount1 = originalAmount1;
                                }


                            }


                        }


                    }

                    public static class ProductModifiersBean {
                        private int maxAllowedQuantity;
                        private int minAllowedQuantity;
                        private String modifierId;
                        private String modifierName;
                        private String modifierType;
                        private List<ModifierProductsBean> modifierProducts;

                        public int getMaxAllowedQuantity() {
                            return maxAllowedQuantity;
                        }

                        public void setMaxAllowedQuantity(int maxAllowedQuantity) {
                            this.maxAllowedQuantity = maxAllowedQuantity;
                        }

                        public int getMinAllowedQuantity() {
                            return minAllowedQuantity;
                        }

                        public void setMinAllowedQuantity(int minAllowedQuantity) {
                            this.minAllowedQuantity = minAllowedQuantity;
                        }

                        public String getModifierId() {
                            return modifierId;
                        }

                        public void setModifierId(String modifierId) {
                            this.modifierId = modifierId;
                        }

                        public String getModifierName() {
                            return modifierName;
                        }

                        public void setModifierName(String modifierName) {
                            this.modifierName = modifierName;
                        }

                        public String getModifierType() {
                            return modifierType;
                        }

                        public void setModifierType(String modifierType) {
                            this.modifierType = modifierType;
                        }

                        public List<ModifierProductsBean> getModifierProducts() {
                            return modifierProducts;
                        }

                        public void setModifierProducts(List<ModifierProductsBean> modifierProducts) {
                            this.modifierProducts = modifierProducts;
                        }

                        public static class ModifierProductsBean {

                            private int amount;
                            private String modifierProductPrice;
                            private int originalAmount1;
                            private String originalQuantity;
                            private String productId;
                            private String productName;
                            private String quantity;
                            private Object unit;

                            public int getAmount() {
                                return amount;
                            }

                            public void setAmount(int amount) {
                                this.amount = amount;
                            }

                            public String getModifierProductPrice() {
                                return modifierProductPrice;
                            }

                            public void setModifierProductPrice(String modifierProductPrice) {
                                this.modifierProductPrice = modifierProductPrice;
                            }

                            public int getOriginalAmount1() {
                                return originalAmount1;
                            }

                            public void setOriginalAmount1(int originalAmount1) {
                                this.originalAmount1 = originalAmount1;
                            }

                            public String getOriginalQuantity() {
                                return originalQuantity;
                            }

                            public void setOriginalQuantity(String originalQuantity) {
                                this.originalQuantity = originalQuantity;
                            }

                            public String getProductId() {
                                return productId;
                            }

                            public void setProductId(String productId) {
                                this.productId = productId;
                            }

                            public String getProductName() {
                                return productName;
                            }

                            public void setProductName(String productName) {
                                this.productName = productName;
                            }

                            public String getQuantity() {
                                return quantity;
                            }

                            public void setQuantity(String quantity) {
                                this.quantity = quantity;
                            }

                            public Object getUnit() {
                                return unit;
                            }

                            public void setUnit(Object unit) {
                                this.unit = unit;
                            }
                        }
                    }

                    public static class MealProduct {
                        private String id;
                        private String productId;
                        private String productName;
                        private String productNameAPP;
                        private String productSizeId;
                        private String productSizeName;
                        private Integer quantity;
                        private String amount;

                        private List<ProductModifiers> productModifiers;
                        private List<SizeModifier> sizeModifiers;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
                        }

                        public String getProductId() {
                            return productId;
                        }

                        public void setProductId(String productId) {
                            this.productId = productId;
                        }

                        public String getProductName() {
                            return productName;
                        }

                        public void setProductName(String productName) {
                            this.productName = productName;
                        }

                        public String getProductNameAPP() {
                            return productNameAPP;
                        }

                        public void setProductNameAPP(String productNameAPP) {
                            this.productNameAPP = productNameAPP;
                        }

                        public String getProductSizeId() {
                            return productSizeId;
                        }

                        public void setProductSizeId(String productSizeId) {
                            this.productSizeId = productSizeId;
                        }

                        public String getProductSizeName() {
                            return productSizeName;
                        }

                        public void setProductSizeName(String productSizeName) {
                            this.productSizeName = productSizeName;
                        }

                        public Integer getQuantity() {
                            return quantity;
                        }

                        public void setQuantity(Integer quantity) {
                            this.quantity = quantity;
                        }

                        public String getAmount() {
                            return amount;
                        }

                        public void setAmount(String amount) {
                            this.amount = amount;
                        }

                        public List<ProductModifiers> getProductModifiers() {
                            return productModifiers;
                        }

                        public void setProductModifiers(List<ProductModifiers> productModifiers) {
                            this.productModifiers = productModifiers;
                        }

                        public List<SizeModifier> getSizeModifiers() {
                            return sizeModifiers;
                        }

                        public void setSizeModifiers(List<SizeModifier> sizeModifiers) {
                            this.sizeModifiers = sizeModifiers;
                        }


                        public static class ProductModifiers {

                        }

                        public static class SizeModifier {

                            private String modifierId;
                            private Integer minAllowedQuantity;
                            private Integer maxAllowedQuantity;
                            private String modifierType;
                            private List<SizeModifierProducts> sizeModifierProducts;

                            public String getModifierId() {
                                return modifierId;
                            }

                            public void setModifierId(String modifierId) {
                                this.modifierId = modifierId;
                            }

                            public Integer getMinAllowedQuantity() {
                                return minAllowedQuantity;
                            }

                            public void setMinAllowedQuantity(Integer minAllowedQuantity) {
                                this.minAllowedQuantity = minAllowedQuantity;
                            }

                            public Integer getMaxAllowedQuantity() {
                                return maxAllowedQuantity;
                            }

                            public void setMaxAllowedQuantity(Integer maxAllowedQuantity) {
                                this.maxAllowedQuantity = maxAllowedQuantity;
                            }

                            public String getModifierType() {
                                return modifierType;
                            }

                            public void setModifierType(String modifierType) {
                                this.modifierType = modifierType;
                            }

                            public List<SizeModifierProducts> getSizeModifierProducts() {
                                return sizeModifierProducts;
                            }

                            public void setSizeModifierProducts(List<SizeModifierProducts> sizeModifierProducts) {
                                this.sizeModifierProducts = sizeModifierProducts;
                            }


                            public static class SizeModifierProducts {
                                private String modifierProductPrice;
                                private String productId;
                                private String productName;
                                private Integer quantity;
                                private Integer originalQuantity;
                                private String productPrice;
                                private String amount;
                                private String originalAmount;
                                private String originalAmount1;

                                public String getModifierProductPrice() {
                                    return modifierProductPrice;
                                }

                                public void setModifierProductPrice(String modifierProductPrice) {
                                    this.modifierProductPrice = modifierProductPrice;
                                }

                                public String getProductId() {
                                    return productId;
                                }

                                public void setProductId(String productId) {
                                    this.productId = productId;
                                }

                                public String getProductName() {
                                    return productName;
                                }

                                public void setProductName(String productName) {
                                    this.productName = productName;
                                }

                                public Integer getQuantity() {
                                    return quantity;
                                }

                                public void setQuantity(Integer quantity) {
                                    this.quantity = quantity;
                                }

                                public Integer getOriginalQuantity() {
                                    return originalQuantity;
                                }

                                public void setOriginalQuantity(Integer originalQuantity) {
                                    this.originalQuantity = originalQuantity;
                                }

                                public String getProductPrice() {
                                    return productPrice;
                                }

                                public void setProductPrice(String productPrice) {
                                    this.productPrice = productPrice;
                                }

                                public String getAmount() {
                                    return amount;
                                }

                                public void setAmount(String amount) {
                                    this.amount = amount;
                                }

                                public String getOriginalAmount() {
                                    return originalAmount;
                                }

                                public void setOriginalAmount(String originalAmount) {
                                    this.originalAmount = originalAmount;
                                }

                                public String getOriginalAmount1() {
                                    return originalAmount1;
                                }

                                public void setOriginalAmount1(String originalAmount1) {
                                    this.originalAmount1 = originalAmount1;
                                }


                            }
                        }

                    }
                }
            }


        }


        public static class DeliveryAddress {
            private String customer_name;
            private String phone_number;

            public String getCustomer_name() {
                return customer_name;
            }

            public void setCustomer_name(String customer_name) {
                this.customer_name = customer_name;
            }

            public String getPhone_number() {
                return phone_number;
            }

            public void setPhone_number(String phone_number) {
                this.phone_number = phone_number;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getCustomer_address_id() {
                return customer_address_id;
            }

            public void setCustomer_address_id(String customer_address_id) {
                this.customer_address_id = customer_address_id;
            }

            public String getCustomer_location() {
                return customer_location;
            }

            public void setCustomer_location(String customer_location) {
                this.customer_location = customer_location;
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

            public String getAddress_type() {
                return address_type;
            }

            public void setAddress_type(String address_type) {
                this.address_type = address_type;
            }

            private String email;
            private String customer_address_id;
            private String customer_location;
            private String address_1;
            private String address_2;
            private String city;
            private String post_code;
            private String country;
            private String address_type;

        }
    }

    public static class ErrorsBean {
    }
}
