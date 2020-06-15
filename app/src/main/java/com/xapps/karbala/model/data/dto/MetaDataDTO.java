package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaDataDTO {

    @Expose
    @SerializedName("FileTypes")
    private List<FileTypesDTO> fileTypesDTO;
    @Expose
    @SerializedName("Settings")
    private List<SettingsDTO> settingsDTO;
    @Expose
    @SerializedName("ReportTypes")
    private List<SubCategoryDTO> subCategoryDTOList;
    @Expose
    @SerializedName("ReportStatuses")
    private List<ReportStatusesDTO> reportStatusesDTO;
    @Expose
    @SerializedName("ReportTypeCategories")
    private List<CategoryDTO> categoryDTOList;
    @Expose
    @SerializedName("Municipalities")
    private List<AreaDTO> areaDTOList;
    @Expose
    @SerializedName("Governorates")
    private List<GovernorateDTO> governorateDTOList;
    @Expose
    @SerializedName("Countries")
    private List<CountryDTO> countryDTOList;

    public List<CountryDTO> getCountryDTOList() {
        return countryDTOList;
    }

    public void setCountryDTOList(List<CountryDTO> countryDTOList) {
        this.countryDTOList = countryDTOList;
    }

    public List<CategoryDTO> getCategoryDTOList() {
        return categoryDTOList;
    }

    public void setCategoryDTOList(List<CategoryDTO> categoryDTOList) {
        this.categoryDTOList = categoryDTOList;
    }

    public List<AreaDTO> getAreaDTOList() {
        return areaDTOList;
    }

    public void setAreaDTOList(List<AreaDTO> areaDTOList) {
        this.areaDTOList = areaDTOList;
    }

    public List<FileTypesDTO> getFileTypesDTO() {
        return fileTypesDTO;
    }

    public void setFileTypesDTO(List<FileTypesDTO> fileTypesDTO) {
        this.fileTypesDTO = fileTypesDTO;
    }

    public List<SettingsDTO> getSettingsDTO() {
        return settingsDTO;
    }

    public void setSettingsDTO(List<SettingsDTO> settingsDTO) {
        this.settingsDTO = settingsDTO;
    }

    public List<ReportStatusesDTO> getReportStatusesDTO() {
        return reportStatusesDTO;
    }

    public void setReportStatusesDTO(List<ReportStatusesDTO> reportStatusesDTO) {
        this.reportStatusesDTO = reportStatusesDTO;
    }

    public List<SubCategoryDTO> getSubCategoryDTOList() {
        return subCategoryDTOList;
    }

    public void setSubCategoryDTOList(List<SubCategoryDTO> subCategoryDTOList) {
        this.subCategoryDTOList = subCategoryDTOList;
    }

    public List<GovernorateDTO> getGovernorateDTOList() {
        return governorateDTOList;
    }

    public void setGovernorateDTOList(List<GovernorateDTO> governorateDTOList) {
        this.governorateDTOList = governorateDTOList;
    }
}
