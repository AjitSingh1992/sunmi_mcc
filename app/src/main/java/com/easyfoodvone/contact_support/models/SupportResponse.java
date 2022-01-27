package com.easyfoodvone.contact_support.models;

import java.io.Serializable;

public class SupportResponse implements Serializable
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
        return "SupportResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public class Data implements Serializable{
        String request_id;

        public String getRequest_id() {
            return request_id;
        }

        public void setRequest_id(String request_id) {
            this.request_id = request_id;
        }

        @Override
        public String toString() {
            return "OrdersDetailsTop{" +
                    "request_id='" + request_id + '\'' +
                    '}';
        }
    }
}
