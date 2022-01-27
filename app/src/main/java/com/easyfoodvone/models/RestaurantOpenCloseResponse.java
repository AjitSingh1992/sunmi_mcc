package com.easyfoodvone.models;

import java.io.Serializable;

public class RestaurantOpenCloseResponse implements Serializable
{
    boolean success;
    String message;
    boolean response;

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

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "RestaurantOpenCloseResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }
}
