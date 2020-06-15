package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xapps.karbala.utils.LocalHelper;

public class TownshipDTO {

    @Expose
    @SerializedName("DescriptionAR")
    private String descriptionAR;
    @Expose
    @SerializedName("Description")
    private String description;
    @Expose
    @SerializedName("NameAR")
    private String nameAR;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("Id")
    private long id;

    public String getDescriptionAR() {
        return descriptionAR;
    }

    public void setDescriptionAR(String descriptionAR) {
        this.descriptionAR = descriptionAR;
    }

    public String getDescription() {
        return LocalHelper.isArabic() ? descriptionAR : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNameAR() {
        return nameAR;
    }

    public void setNameAR(String nameAR) {
        this.nameAR = nameAR;
    }

    public String getName() {
        return LocalHelper.isArabic() ? nameAR : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
