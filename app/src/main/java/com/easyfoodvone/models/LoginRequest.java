package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginRequest implements Serializable
{
    @Expose
    @SerializedName("email")
    String email;

    @Expose
    @SerializedName("password")
    String password;

    @Expose
    @SerializedName("device_id")
    String device_id;

    @Expose
    @SerializedName("firebase_id")
    String firebase_id;

    @Expose
    @SerializedName("device_type")
    String device_type;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", device_id='" + device_id + '\'' +
                ", firebase_id='" + firebase_id + '\'' +
                ", device_type='" + device_type + '\'' +
                '}';
    }
}
