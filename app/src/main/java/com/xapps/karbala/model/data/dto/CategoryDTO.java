package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xapps.karbala.utils.LocalHelper;

import java.util.List;

public class CategoryDTO {
    @Expose
    @SerializedName("NameAR")
    public String nameAR;
    @Expose
    @SerializedName("Name")
    public String name;
    @Expose
    @SerializedName("Id")
    public long id;
    @Expose
    @SerializedName("Image")
    public String image;
    @Expose
    @SerializedName("ReportTypes")
    public List<SubCategoryDTO> subCategoryDTOList;

    public List<SubCategoryDTO> getSubCategoryDTOList() {
        return subCategoryDTOList;
    }

    public void setSubCategoryDTOList(List<SubCategoryDTO> subCategoryDTOList) {
        this.subCategoryDTOList = subCategoryDTOList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
