package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ErrorDTO {

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("message")
    @Expose
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
