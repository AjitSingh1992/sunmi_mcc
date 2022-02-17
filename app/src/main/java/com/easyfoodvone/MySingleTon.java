package com.easyfoodvone;


public class MySingleTon {

    private static MySingleTon _instance;

    private String fragmentName = "";

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
