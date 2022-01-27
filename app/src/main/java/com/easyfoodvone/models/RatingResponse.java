package com.easyfoodvone.models;

import java.io.Serializable;
import java.util.List;

public class RatingResponse implements Serializable
{
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
        return "RatingResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public class Data implements Serializable
    {
        String total_no_ratings;
        String total_ratings;
        List<UserRatingsList> user_review_ratings;

        public String getTotal_no_ratings() {
            return total_no_ratings;
        }

        public void setTotal_no_ratings(String total_no_ratings) {
            this.total_no_ratings = total_no_ratings;
        }

        public String getTotal_ratings() {
            return total_ratings;
        }

        public void setTotal_ratings(String total_ratings) {
            this.total_ratings = total_ratings;
        }

        public List<UserRatingsList> getUser_review_ratings() {
            return user_review_ratings;
        }

        public void setUser_review_ratings(List<UserRatingsList> user_review_ratings) {
            this.user_review_ratings = user_review_ratings;
        }

        @Override
        public String toString() {
            return "OrdersDetailsTop{" +
                    "total_no_ratings='" + total_no_ratings + '\'' +
                    ", total_ratings='" + total_ratings + '\'' +
                    ", user_review_ratings=" + user_review_ratings +
                    '}';
        }

        public class UserRatingsList implements Serializable {
            String overall_rating;
            String food_quality_rating;
            String delivery_rating;
            String order_again_rating;
            String recommendation_rating;
            String customer_post_code;
            String customer_name;
            String rating_date;
            String rating_time;
            String customer_id;


            public String getOverall_rating() {
                return overall_rating;
            }

            public void setOverall_rating(String overall_rating) {
                this.overall_rating = overall_rating;
            }

            public String getFood_quality_rating() {
                return food_quality_rating;
            }

            public void setFood_quality_rating(String food_quality_rating) {
                this.food_quality_rating = food_quality_rating;
            }

            public String getDelivery_rating() {
                return delivery_rating;
            }

            public void setDelivery_rating(String delivery_rating) {
                this.delivery_rating = delivery_rating;
            }

            public String getOrder_again_rating() {
                return order_again_rating;
            }

            public void setOrder_again_rating(String order_again_rating) {
                this.order_again_rating = order_again_rating;
            }

            public String getRecommendation_rating() {
                return recommendation_rating;
            }

            public void setRecommendation_rating(String recommendation_rating) {
                this.recommendation_rating = recommendation_rating;
            }

            public String getCustomer_post_code() {
                return customer_post_code;
            }

            public void setCustomer_post_code(String customer_post_code) {
                this.customer_post_code = customer_post_code;
            }

            public String getCustomer_name() {
                return customer_name;
            }

            public void setCustomer_name(String customer_name) {
                this.customer_name = customer_name;
            }

            public String getRating_date() {
                return rating_date;
            }

            public void setRating_date(String rating_date) {
                this.rating_date = rating_date;
            }

            public String getRating_time() {
                return rating_time;
            }

            public void setRating_time(String rating_time) {
                this.rating_time = rating_time;
            }

            public String getCustomer_id() {
                return customer_id;
            }

            public void setCustomer_id(String customer_id) {
                this.customer_id = customer_id;
            }

            @Override
            public String toString() {
                return "UserRatingsList{" +
                        "overall_rating='" + overall_rating + '\'' +
                        ", food_quality_rating='" + food_quality_rating + '\'' +
                        ", delivery_rating='" + delivery_rating + '\'' +
                        ", order_again_rating='" + order_again_rating + '\'' +
                        ", recommendation_rating='" + recommendation_rating + '\'' +
                        ", customer_post_code='" + customer_post_code + '\'' +
                        ", customer_name='" + customer_name + '\'' +
                        ", rating_date='" + rating_date + '\'' +
                        ", rating_time='" + rating_time + '\'' +
                        ", customer_id='" + customer_id + '\'' +
                        '}';
            }
        }


    }


}
