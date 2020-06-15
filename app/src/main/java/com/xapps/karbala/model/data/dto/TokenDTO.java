package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenDTO {
    @Expose
    @SerializedName(".expires")
    private String expires;
    @Expose
    @SerializedName(".issued")
    private String issued;
    @Expose
    @SerializedName("userName")
    private String userName;
    @Expose
    @SerializedName("expires_in")
    private float expires_in;
    @Expose
    @SerializedName("token_type")
    private String token_type;
    @Expose
    @SerializedName("access_token")
    private String access_token;

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public float getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(float expires_in) {
        this.expires_in = expires_in;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
