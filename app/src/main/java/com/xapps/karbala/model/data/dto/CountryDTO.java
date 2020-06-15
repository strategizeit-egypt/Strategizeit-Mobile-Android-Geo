package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xapps.karbala.utils.LocalHelper;

public class CountryDTO {
    @Expose
    @SerializedName("NameAR")
    private String NameAR;
    @Expose
    @SerializedName("Name")
    private String Name;
    @Expose
    @SerializedName("Id")
    private long Id;

    public CountryDTO(String nameAR, String name, long id) {
        NameAR = nameAR;
        Name = name;
        Id = id;
    }

    public String getNameAR() {
        return NameAR;
    }

    public void setNameAR(String nameAR) {
        NameAR = nameAR;
    }

    public String getName() {
        return LocalHelper.isArabic() ? NameAR : Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    @Override
    public String toString() {
        return getName();
    }
}
