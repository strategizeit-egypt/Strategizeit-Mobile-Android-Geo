package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xapps.karbala.utils.LocalHelper;

public class GovernorateDTO {

    @Expose
    @SerializedName("NameAR")
    private String namear;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("Id")
    private long id;

    public GovernorateDTO(String namear, String name, long id) {
        this.namear = namear;
        this.name = name;
        this.id = id;
    }

    public String getNamear() {
        return namear;
    }

    public void setNamear(String namear) {
        this.namear = namear;
    }

    public String getName() {
        return LocalHelper.isArabic() ? namear : name;
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

    @Override
    public String toString() {
        return getName();
    }
}
