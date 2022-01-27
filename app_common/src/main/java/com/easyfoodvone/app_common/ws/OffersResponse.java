package com.easyfoodvone.app_common.ws;

import java.util.List;

public class OffersResponse {
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

    @Override
    public String toString() {
        return "OffersResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        String total_running_offers;
        String total_expired_offers;
        String total_flat_discount_offers;
        String total_percentage_discount_offers;
        String total_combo_discount_offers;
        List<Offers> offers;


        public String getTotal_running_offers() {
            return total_running_offers;
        }

        public void setTotal_running_offers(String total_running_offers) {
            this.total_running_offers = total_running_offers;
        }

        public String getTotal_expired_offers() {
            return total_expired_offers;
        }

        public void setTotal_expired_offers(String total_expired_offers) {
            this.total_expired_offers = total_expired_offers;
        }

        public String getTotal_flat_discount_offers() {
            return total_flat_discount_offers;
        }

        public void setTotal_flat_discount_offers(String total_flat_discount_offers) {
            this.total_flat_discount_offers = total_flat_discount_offers;
        }

        public String getTotal_percentage_discount_offers() {
            return total_percentage_discount_offers;
        }

        public void setTotal_percentage_discount_offers(String total_percentage_discount_offers) {
            this.total_percentage_discount_offers = total_percentage_discount_offers;
        }

        public String getTotal_combo_discount_offers() {
            return total_combo_discount_offers;
        }

        public void setTotal_combo_discount_offers(String total_combo_discount_offers) {
            this.total_combo_discount_offers = total_combo_discount_offers;
        }

        public List<Offers> getOffers() {
            return offers;
        }

        public void setOffers(List<Offers> offers) {
            this.offers = offers;
        }

        @Override
        public String toString() {
            return "OrdersDetailsTop{" +
                    "total_running_offers='" + total_running_offers + '\'' +
                    ", total_expired_offers='" + total_expired_offers + '\'' +
                    ", total_flat_discount_offers='" + total_flat_discount_offers + '\'' +
                    ", total_percentage_discount_offers='" + total_percentage_discount_offers + '\'' +
                    ", total_combo_discount_offers='" + total_combo_discount_offers + '\'' +
                    ", offers=" + offers +
                    '}';
        }

        public class Offers {
            String id;
            String offer_title;
            String offer_type;
            String start_date;
            String end_date;
            String days_available;
            String total_offer_used;
            String status;
            String start_time;
            String end_time;
            String available_for;
            String discount_amount;
            String easyfood_share;

            String restaurant_share;
            String franchise_share;
            String max_discount_amount;
            String offer_description;
            String offer_details;
            String show_in_swipe;
            String spend_amount_to_avail_offer;
            String terms_conditions;

            String usage_per_customer;
            String usage_total_usage;
            String user_app;


            public String getMin_order_value() {
                return min_order_value;
            }

            public void setMin_order_value(String min_order_value) {
                this.min_order_value = min_order_value;
            }

            String min_order_value;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOffer_title() {
                return offer_title;
            }

            public void setOffer_title(String offer_title) {
                this.offer_title = offer_title;
            }

            public String getOffer_type() {
                return offer_type;
            }

            public void setOffer_type(String offer_type) {
                this.offer_type = offer_type;
            }

            public String getStart_date() {
                return start_date;
            }

            public void setStart_date(String start_date) {
                this.start_date = start_date;
            }

            public String getEnd_date() {
                return end_date;
            }

            public void setEnd_date(String end_date) {
                this.end_date = end_date;
            }

            public String getDays_available() {
                return days_available;
            }

            public void setDays_available(String days_available) {
                this.days_available = days_available;
            }

            public String getTotal_offer_used() {
                return total_offer_used;
            }

            public void setTotal_offer_used(String total_offer_used) {
                this.total_offer_used = total_offer_used;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getAvailable_for() {
                return available_for;
            }

            public void setAvailable_for(String available_for) {
                this.available_for = available_for;
            }

            public String getDiscount_amount() {
                return discount_amount;
            }

            public void setDiscount_amount(String discount_amount) {
                this.discount_amount = discount_amount;
            }

            public String getEasyfood_share() {
                return easyfood_share;
            }

            public void setEasyfood_share(String easyfood_share) {
                this.easyfood_share = easyfood_share;
            }

            public String getRestaurant_share() {
                return restaurant_share;
            }

            public void setRestaurant_share(String restaurant_share) {
                this.restaurant_share = restaurant_share;
            }

            public String getFranchise_share() {
                return franchise_share;
            }

            public void setFranchise_share(String franchise_share) {
                this.franchise_share = franchise_share;
            }

            public String getMax_discount_amount() {
                return max_discount_amount;
            }

            public void setMax_discount_amount(String max_discount_amount) {
                this.max_discount_amount = max_discount_amount;
            }

            public String getOffer_description() {
                return offer_description;
            }

            public void setOffer_description(String offer_description) {
                this.offer_description = offer_description;
            }

            public String getOffer_details() {
                return offer_details;
            }

            public void setOffer_details(String offer_details) {
                this.offer_details = offer_details;
            }

            public String getShow_in_swipe() {
                return show_in_swipe;
            }

            public void setShow_in_swipe(String show_in_swipe) {
                this.show_in_swipe = show_in_swipe;
            }

            public String getSpend_amount_to_avail_offer() {
                return spend_amount_to_avail_offer;
            }

            public void setSpend_amount_to_avail_offer(String spend_amount_to_avail_offer) {
                this.spend_amount_to_avail_offer = spend_amount_to_avail_offer;
            }

            public String getTerms_conditions() {
                return terms_conditions;
            }

            public void setTerms_conditions(String terms_conditions) {
                this.terms_conditions = terms_conditions;
            }

            public String getUsage_per_customer() {
                return usage_per_customer;
            }

            public void setUsage_per_customer(String usage_per_customer) {
                this.usage_per_customer = usage_per_customer;
            }

            public String getUsage_total_usage() {
                return usage_total_usage;
            }

            public void setUsage_total_usage(String usage_total_usage) {
                this.usage_total_usage = usage_total_usage;
            }

            public String getUser_app() {
                return user_app;
            }

            public void setUser_app(String user_app) {
                this.user_app = user_app;
            }

            @Override
            public String toString() {
                return "Offers{" +
                        "id='" + id + '\'' +
                        ", offer_title='" + offer_title + '\'' +
                        ", offer_type='" + offer_type + '\'' +
                        ", start_date='" + start_date + '\'' +
                        ", end_date='" + end_date + '\'' +
                        ", total_offer_used='" + total_offer_used + '\'' +
                        ", status='" + status + '\'' +

                        ", start_time='" + start_time + '\'' +
                        ", end_time='" + end_time + '\'' +
                        ", available_for='" + available_for + '\'' +
                        ", discount_amount='" + discount_amount + '\'' +
                        ", easyfood_share='" + easyfood_share + '\'' +
                        ", restaurant_share='" + restaurant_share + '\'' +
                        ", franchise_share='" + franchise_share + '\'' +
                        ", max_discount_amount='" + max_discount_amount + '\'' +
                        ", offer_description='" + offer_description + '\'' +
                        ", offer_details='" + offer_details + '\'' +

                        ", show_in_swipe='" + show_in_swipe + '\'' +
                        ", spend_amount_to_avail_offer='" + spend_amount_to_avail_offer + '\'' +
                        ", terms_conditions='" + terms_conditions + '\'' +
                        ", usage_per_customer='" + usage_per_customer + '\'' +
                        ", usage_total_usage='" + usage_total_usage + '\'' +
                        ", user_app='" + user_app + '\'' +

                        ", days_available='" + days_available + '\'' +
                        '}';
            }
        }
    }

}
