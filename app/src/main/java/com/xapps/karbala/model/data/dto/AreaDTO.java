package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xapps.karbala.utils.LocalHelper;

import java.util.List;

public class AreaDTO {
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
    @SerializedName("Townships")
    List<SubAreaDTO> subAreaDTOList;

    public AreaDTO(long id, String name, String nameAr) {
        this.id = id;
        this.name = name;
        this.nameAr = nameAr;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return LocalHelper.isArabic() ? nameAr : name;
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

    public List<SubAreaDTO> getSubAreaDTOList() {
        return subAreaDTOList;
    }

    public void setSubAreaDTOList(List<SubAreaDTO> subAreaDTOList) {
        this.subAreaDTOList = subAreaDTOList;
    }

    @Override
    public String toString() {
        return getName();
    }
}
