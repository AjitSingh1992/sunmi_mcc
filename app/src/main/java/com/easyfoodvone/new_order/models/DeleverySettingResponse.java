package com.easyfoodvone.new_order.models;

import java.io.Serializable;
import java.util.List;

public class DeleverySettingResponse implements Serializable
{
    boolean success;
    String message;
    int distance;
    int Average_delivery_time;
    String free_delivery;
    String delivery_charges;
    String avg_preparation_time;
    String min_order_value;
    boolean set_one_amount;
    List<Data> data;

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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getAverage_delivery_time() {
        return Average_delivery_time;
    }

    public void setAverage_delivery_time(int average_delivery_time) {
        Average_delivery_time = average_delivery_time;
    }

    public boolean isSet_one_amount() {
        return set_one_amount;
    }

    public void setSet_one_amount(boolean set_one_amount) {
        this.set_one_amount = set_one_amount;
    }

    public String getFree_delivery() {
        return free_delivery;
    }

    public void setFree_delivery(String free_delivery) {
        this.free_delivery = free_delivery;
    }

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    public String getAvg_preparation_time() {
        return avg_preparation_time;
    }

    public void setAvg_preparation_time(String avg_preparation_time) {
        this.avg_preparation_time = avg_preparation_time;
    }

    public String getMin_order_value() {
        return min_order_value;
    }

    public void setMin_order_value(String min_order_value) {
        this.min_order_value = min_order_value;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable{
        String id;
        String restaurant_id;
        String post_code;
        String min_order_value;
        String delivery_charge;
        String free_delivery;
        int status;
        String created_by;
        String updated_by;
        String created_at;
        String updated_at;
        String deleted_at;

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

        public String getPost_code() {
            return post_code;
        }

        public void setPost_code(String post_code) {
            this.post_code = post_code;
        }

        public String getMin_order_value() {
            return min_order_value;
        }

        public void setMin_order_value(String min_order_value) {
            this.min_order_value = min_order_value;
        }

        public String getDelivery_charge() {
            return delivery_charge;
        }

        public void setDelivery_charge(String delivery_charge) {
            this.delivery_charge = delivery_charge;
        }

        public String getFree_delivery() {
            return free_delivery;
        }

        public void setFree_delivery(String free_delivery) {
            this.free_delivery = free_delivery;
        }


        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getUpdated_by() {
            return updated_by;
        }

        public void setUpdated_by(String updated_by) {
            this.updated_by = updated_by;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }

        @Override
        public String toString() {
            return "OrdersDetailsTop{" +
                    "id='" + id + '\'' +
                    ", restaurant_id='" + restaurant_id + '\'' +
                    ", post_code='" + post_code + '\'' +
                    ", min_order_value='" + min_order_value + '\'' +
                    ", delivery_charge='" + delivery_charge + '\'' +
                    ", free_delivery='" + free_delivery + '\'' +
                    ", status=" + status +
                    ", created_by='" + created_by + '\'' +
                    ", updated_by='" + updated_by + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    ", deleted_at='" + deleted_at + '\'' +
                    '}';
        }
    }
}
