package com.easyfoodvone.new_order.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeSlotResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("error")
    @Expose
    private Errors errors;

    public class Data {

        @SerializedName("today")
        @Expose
        private List<String> today = null;
        @SerializedName("tomorrow")
        @Expose
        private List<String> tomorrow = null;

        public List<String> getToday() {
            return today;
        }

        public void setToday(List<String> today) {
            this.today = today;
        }

        public List<String> getTomorrow() {
            return tomorrow;
        }

        public void setTomorrow(List<String> tomorrow) {
            this.tomorrow = tomorrow;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "today=" + today +
                    ", tomorrow=" + tomorrow +
                    '}';
        }
    }

    public class Errors {

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

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "TimeSlotResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errors=" + errors +
                '}';
    }
}
