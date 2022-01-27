package com.easyfoodvone.contact_support.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SupportRequest implements Serializable {
    @Expose
    @SerializedName("email")
    String email;
    @Expose
    @SerializedName("message")
    String message;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SupportRequest{" +
                "email='" + email + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
