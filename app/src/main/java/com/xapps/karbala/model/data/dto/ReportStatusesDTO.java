package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xapps.karbala.utils.LocalHelper;

public class ReportStatusesDTO {
    @Expose
    @SerializedName("NameAR")
    public String nameAR;
    @Expose
    @SerializedName("Name")
    public String name;
    @Expose
    @SerializedName("Id")
    public long id;

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
