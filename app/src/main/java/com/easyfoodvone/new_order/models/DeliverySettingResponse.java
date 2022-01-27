package com.easyfoodvone.new_order.models;

import java.io.Serializable;

public class DeliverySettingResponse implements Serializable {


    boolean success;
    String message;

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

    @Override
    public String toString() {
        return "DeliverySettingResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }


}
