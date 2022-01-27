/*Created by Omnisttechhub Solution*/
package com.easyfoodvone.charity.webservice.responsebean;


public class CommonResponseBean {


    private boolean success;
    private String message;
    private ErrorsBean errors;

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

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public static class ErrorsBean {
    }
}
