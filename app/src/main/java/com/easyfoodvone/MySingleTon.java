package com.easyfoodvone;


public class MySingleTon {

    private static MySingleTon _instance;

    private String fragmentName = "";
    private int Year = 0;
    private int month = 0;
    private int day = 0;

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public String getFragmentName() {
        return fragmentName;
    }

    public void setFragmentName(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    private MySingleTon() {

    }

    public static MySingleTon getInstance() {
        if (_instance == null) {
            _instance = new MySingleTon();
        }
        return _instance;
    }

}
