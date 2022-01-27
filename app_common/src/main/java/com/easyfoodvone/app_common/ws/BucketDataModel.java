package com.easyfoodvone.app_common.ws;

public class BucketDataModel
{
    String between;
    String and;
    String detDiscount;

    public String getBetween() {
        return between;
    }

    public void setBetween(String between) {
        this.between = between;
    }

    public String getAnd() {
        return and;
    }

    public void setAnd(String and) {
        this.and = and;
    }

    public String getDetDiscount() {
        return detDiscount;
    }

    public void setDetDiscount(String detDiscount) {
        this.detDiscount = detDiscount;
    }

    public BucketDataModel(String between, String and, String detDiscount) {
        this.between = between;
        this.and = and;
        this.detDiscount = detDiscount;
    }
}
