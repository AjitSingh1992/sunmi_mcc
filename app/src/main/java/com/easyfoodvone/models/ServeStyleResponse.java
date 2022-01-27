package com.easyfoodvone.models;

import java.io.Serializable;

public class ServeStyleResponse implements Serializable
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

    public  class Data{
                int delivery;
                int collection;
                int dine_in;
                String serve_style;

        public int getDelivery() {
            return delivery;
        }

        public void setDelivery(int delivery) {
            this.delivery = delivery;
        }

        public int getCollection() {
            return collection;
        }

        public void setCollection(int collection) {
            this.collection = collection;
        }

        public int getDine_in() {
            return dine_in;
        }

        public void setDine_in(int dine_in) {
            this.dine_in = dine_in;
        }

        public String getServe_style() {
            return serve_style;
        }

        public void setServe_style(String serve_style) {
            this.serve_style = serve_style;
        }
    }

}
