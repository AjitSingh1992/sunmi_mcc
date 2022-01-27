package com.easyfoodvone.models;

import com.google.gson.annotations.SerializedName;

public class RestaurantClosingTimeByDataModel {
    @SerializedName("success")
    Boolean success;
    @SerializedName("message")
    String message;

    @SerializedName("data")
    Data data;

    public RestaurantClosingTimeByDataModel(Boolean success, String message, Data data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

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

    @Override
    public String toString() {
        return "RestaurantClosingTimeByDataModel{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static class Data {
        @SerializedName("opening_start_time")
        String openingStartTime;
        @SerializedName("opening_end_time")
        String openingEndTime;

        public Data(String openingStartTime, String openingEndTime) {
            this.openingStartTime = openingStartTime;
            this.openingEndTime = openingEndTime;
        }

        public String getOpeningStartTime() {
            return openingStartTime;
        }

        public void setOpeningStartTime(String openingStartTime) {
            this.openingStartTime = openingStartTime;
        }

        public String getOpeningEndTime() {
            return openingEndTime;
        }

        public void setOpeningEndTime(String openingEndTime) {
            this.openingEndTime = openingEndTime;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "openingStartTime='" + openingStartTime + '\'' +
                    ", openingEndTime='" + openingEndTime + '\'' +
                    '}';
        }
    }
}
