package com.easyfoodvone.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class LoginResponse implements Serializable
{
    boolean success;
    String message;
    String login_attempt;

    public String getLogin_attempt() {
        return login_attempt;
    }

    public void setLogin_attempt(String login_attempt) {
        this.login_attempt = login_attempt;
    }

    Data data;
    private Map<String, List<String>> errors;


    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

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
        return "LoginResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public class Data implements Serializable
    {
        String user_id;
        String email;
        String phone_number;
        String restaurant_id;
        String restaurant_name;
        String restaurant_image;
        boolean is_open;
        boolean home_delivery;
        String post_code;
        int email_verified;
        int phone_number_verified;
        String token;
        String contact_person;
        String about;
        String address;
        String logo;
        String restaurant_email;
        String serve_style;
        String pincode;
        String website_url;
        String landline_number;


        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        List<RestaurantImages>restaurant_images;


        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getRestaurant_id() {
            return restaurant_id;
        }

        public void setRestaurant_id(String restaurant_id) {
            this.restaurant_id = restaurant_id;
        }

        public String getRestaurant_name() {
            return restaurant_name;
        }

        public void setRestaurant_name(String restaurant_name) {
            this.restaurant_name = restaurant_name;
        }

        public String getRestaurant_image() {
            return restaurant_image;
        }

        public void setRestaurant_image(String restaurant_image) {
            this.restaurant_image = restaurant_image;
        }

        public boolean isOpen() {
            return is_open;
        }

        public void setOpen(boolean open) {
            is_open = open;
        }

        public boolean isHome_delivery() {
            return home_delivery;
        }

        public void setHome_delivery(boolean home_delivery) {
            this.home_delivery = home_delivery;
        }

        public String getPost_code() {
            return post_code;
        }

        public void setPost_code(String post_code) {
            this.post_code = post_code;
        }

        public int getEmail_verified() {
            return email_verified;
        }

        public void setEmail_verified(int email_verified) {
            this.email_verified = email_verified;
        }

        public int getPhone_number_verified() {
            return phone_number_verified;
        }

        public void setPhone_number_verified(int phone_number_verified) {
            this.phone_number_verified = phone_number_verified;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public boolean isIs_open() {
            return is_open;
        }

        public void setIs_open(boolean is_open) {
            this.is_open = is_open;
        }

        public String getContact_person() {
            return contact_person;
        }

        public void setContact_person(String contact_person) {
            this.contact_person = contact_person;
        }

        public String getAbout() {
            return about;
        }

        public void setAbout(String about) {
            this.about = about;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getRestaurant_email() {
            return restaurant_email;
        }

        public void setRestaurant_email(String restaurant_email) {
            this.restaurant_email = restaurant_email;
        }

        public String getServe_style() {
            return serve_style;
        }

        public void setServe_style(String serve_style) {
            this.serve_style = serve_style;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public List<RestaurantImages> getRestaurant_images() {
            return restaurant_images;
        }

        public void setRestaurant_images(List<RestaurantImages> restaurant_images) {
            this.restaurant_images = restaurant_images;
        }

        public String getWebsite_url() {
            return website_url;
        }

        public void setWebsite_url(String website_url) {
            this.website_url = website_url;
        }

        public String getLandline_number() {
            return landline_number;
        }

        public void setLandline_number(String landline_number) {
            this.landline_number = landline_number;
        }

        @Override
        public String toString() {
            return "OrdersDetailsTop{" +
                    "user_id='" + user_id + '\'' +
                    ", email='" + email + '\'' +
                    ", phone_number='" + phone_number + '\'' +
                    ", restaurant_id='" + restaurant_id + '\'' +
                    ", restaurant_name='" + restaurant_name + '\'' +
                    ", restaurant_image='" + restaurant_image + '\'' +
                    ", is_open=" + is_open +
                    ", home_delivery=" + home_delivery +
                    ", post_code='" + post_code + '\'' +
                    ", email_verified=" + email_verified +
                    ", phone_number_verified=" + phone_number_verified +
                    ", token='" + token + '\'' +
                    ", contact_person='" + contact_person + '\'' +
                    ", about='" + about + '\'' +
                    ", address='" + address + '\'' +
                    ", restaurant_email='" + restaurant_email + '\'' +
                    ", serve_style='" + serve_style + '\'' +
                    ", pincode='" + pincode + '\'' +
                    ", website_url='" + website_url + '\'' +
                    ", landline_number='" + landline_number + '\'' +
                    ", restaurant_images=" + restaurant_images +
                    '}';
        }
    }
    public class Errors {

        @SerializedName("email")
        @Expose
        private List<String> email;

        public List<String> getEmail() {
            return email;
        }

        public void setEmail(List<String> email) {
            this.email = email;
        }

    }

    public class RestaurantImages implements Serializable
    {
        String image_url;

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }
}
