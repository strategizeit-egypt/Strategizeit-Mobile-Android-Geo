package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class ContactUsDTO {

    @Expose
    @SerializedName("CreationDate")
    private String creationDate;
    @Expose
    @SerializedName("CitizenId")
    private long citizenId;
    @Expose
    @SerializedName("Message")
    private String message;
    @Expose
    @SerializedName("Titlle")
    private String title;
    @Expose
    @SerializedName("Id")
    private long id;

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(long citizenId) {
        this.citizenId = citizenId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
