package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class LoginDTO {

    @Expose
    @SerializedName("Token")
    private List<TokenDTO> token;
    @Expose
    @SerializedName("CreationDate")
    private String creationDate;
    @Expose
    @SerializedName("IdentityId")
    private String identityId;
    @Expose
    @SerializedName("Enabled")
    private boolean enabled;
    @Expose
    @SerializedName("Verified")
    private boolean verified;
    @Expose
    @SerializedName("ExpirationVCodeDate")
    private String expirationVCodeDate;
    @Expose
    @SerializedName("Vcode")
    private int vCode;
    @Expose
    @SerializedName("PhoneNumber")
    private String phoneNumber;
    @Expose
    @SerializedName("FullName")
    private String fullName;
    @Expose
    @SerializedName("Id")
    private long id;

    public List<TokenDTO> getToken() {
        return token;
    }

    public void setToken(List<TokenDTO> token) {
        this.token = token;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getExpirationVCodeDate() {
        return expirationVCodeDate;
    }

    public void setExpirationVCodeDate(String expirationVCodeDate) {
        this.expirationVCodeDate = expirationVCodeDate;
    }

    public int getvCode() {
        return vCode;
    }

    public void setvCode(int vCode) {
        this.vCode = vCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
