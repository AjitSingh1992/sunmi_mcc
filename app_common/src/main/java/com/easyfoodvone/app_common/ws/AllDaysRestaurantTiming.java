package com.easyfoodvone.app_common.ws;

import java.util.List;

public class AllDaysRestaurantTiming {
    boolean success;
    String message;
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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AllDaysRestaurantTiming{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public class Data {
        String day;
        List<TimingData> data;


        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public List<TimingData> getData() {
            return data;
        }

        public void setData(List<TimingData> data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "OrdersDetailsTop{" +
                    "day='" + day + '\'' +
                    ", data=" + data +
                    '}';
        }

        public class TimingData {
            String id;
            String restaurant_id;
            String day;
            String opening_start_time;
            String opening_end_time;
            String collection_start_time;
            String collection_end_time;
            String delivery_start_time;
            String delivery_end_time;
            String status;
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

            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            public String getOpening_start_time() {
                return opening_start_time;
            }

            public void setOpening_start_time(String opening_start_time) {
                this.opening_start_time = opening_start_time;
            }

            public String getOpening_end_time() {
                return opening_end_time;
            }

            public void setOpening_end_time(String opening_end_time) {
                this.opening_end_time = opening_end_time;
            }

            public String getCollection_start_time() {
                return collection_start_time;
            }

            public void setCollection_start_time(String collection_start_time) {
                this.collection_start_time = collection_start_time;
            }

            public String getCollection_end_time() {
                return collection_end_time;
            }

            public void setCollection_end_time(String collection_end_time) {
                this.collection_end_time = collection_end_time;
            }

            public String getDelivery_start_time() {
                return delivery_start_time;
            }

            public void setDelivery_start_time(String delivery_start_time) {
                this.delivery_start_time = delivery_start_time;
            }

            public String getDelivery_end_time() {
                return delivery_end_time;
            }

            public void setDelivery_end_time(String delivery_end_time) {
                this.delivery_end_time = delivery_end_time;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
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
                return "TimingData{" +
                        "id='" + id + '\'' +
                        ", restaurant_id='" + restaurant_id + '\'' +
                        ", day='" + day + '\'' +
                        ", opening_start_time='" + opening_start_time + '\'' +
                        ", opening_end_time='" + opening_end_time + '\'' +
                        ", collection_start_time='" + collection_start_time + '\'' +
                        ", collection_end_time='" + collection_end_time + '\'' +
                        ", delivery_start_time='" + delivery_start_time + '\'' +
                        ", delivery_end_time='" + delivery_end_time + '\'' +
                        ", status='" + status + '\'' +
                        ", created_by='" + created_by + '\'' +
                        ", updated_by='" + updated_by + '\'' +
                        ", created_at='" + created_at + '\'' +
                        ", updated_at='" + updated_at + '\'' +
                        ", deleted_at='" + deleted_at + '\'' +
                        '}';
            }
        }


    }


}
