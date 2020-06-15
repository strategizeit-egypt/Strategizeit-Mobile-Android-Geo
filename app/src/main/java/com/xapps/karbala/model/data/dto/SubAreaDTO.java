package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubAreaDTO {
    @Expose
    @SerializedName("Id")
    private long id;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("NameAR")
    private String nameAr;
    @Expose
    @SerializedName("Description")
    private String description;
    @Expose
    @SerializedName("DescriptionAR")
    private String descriptionAr;
    @Expose
    @SerializedName("MunicipalityId")
    private long municipalityId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionAr() {
        return descriptionAr;
    }

    public void setDescriptionAr(String descriptionAr) {
        this.descriptionAr = descriptionAr;
    }

    public long getMunicipalityId() {
        return municipalityId;
    }

    public void setMunicipalityId(long municipalityId) {
        this.municipalityId = municipalityId;
    }
}
