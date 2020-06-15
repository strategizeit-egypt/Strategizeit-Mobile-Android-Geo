package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xapps.karbala.utils.LocalHelper;

import java.util.List;

public class ComplaintDTO {

    @Expose
    @SerializedName("ReportFiles")
    private List<ComplaintFilesDTO> complaintFileDTOS;
    @Expose
    @SerializedName("ReportLogs")
    private List<ComplaintLogDTO> complaintLogDTOList;
    @Expose
    @SerializedName("ReportTypeNameAR")
    private String reportTypeNameAR;
    @Expose
    @SerializedName("ReportTypeName")
    private String reportTypeName;
    @Expose
    @SerializedName("ReportStatusNameAR")
    private String reportStatusNameAR;
    @Expose
    @SerializedName("ReportStatusName")
    private String reportStatusName;
    @Expose
    @SerializedName("CitizenId")
    private long citizenId;
    @Expose
    @SerializedName("CreationDate")
    private String creationDate;
    @Expose
    @SerializedName("ReportStatusId")
    private long reportStatusId;
    @Expose
    @SerializedName("ReportTypeId")
    private long reportTypeId;
    @Expose
    @SerializedName("Details")
    private String details;
    @Expose
    @SerializedName("Title")
    private String title;
    @Expose
    @SerializedName("Id")
    private long id;
    @Expose
    @SerializedName("Latitude")
    private double latitude;
    @Expose
    @SerializedName("Longitude")
    private double longitude;
    @Expose
    @SerializedName("Color")
    private String color;
    @Expose
    @SerializedName("Duration")
    private double duration;
    @Expose
    @SerializedName("VideoImage")
    private String videoImage;

    public List<ComplaintLogDTO> getComplaintLogDTOList() {
        return complaintLogDTOList;
    }

    public void setComplaintLogDTOList(List<ComplaintLogDTO> complaintLogDTOList) {
        this.complaintLogDTOList = complaintLogDTOList;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<ComplaintFilesDTO> getComplaintFileDTOS() {
        return complaintFileDTOS;
    }

    public void setComplaintFileDTOS(List<ComplaintFilesDTO> complaintFileDTOS) {
        this.complaintFileDTOS = complaintFileDTOS;
    }

    public String getReportTypeNameAR() {
        return reportTypeNameAR;
    }

    public void setReportTypeNameAR(String reportTypeNameAR) {
        this.reportTypeNameAR = reportTypeNameAR;
    }

    public String getReportTypeName() {
        return LocalHelper.isArabic() ? reportTypeNameAR : reportTypeName;
    }

    public void setReportTypeName(String reportTypeName) {
        this.reportTypeName = reportTypeName;
    }

    public String getReportStatusNameAR() {
        return reportStatusNameAR;
    }

    public void setReportStatusNameAR(String reportStatusNameAR) {
        this.reportStatusNameAR = reportStatusNameAR;
    }

    public String getReportStatusName() {
        return LocalHelper.isArabic() ? reportStatusNameAR : reportStatusName;
    }

    public void setReportStatusName(String reportStatusName) {
        this.reportStatusName = reportStatusName;
    }

    public long getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(long citizenId) {
        this.citizenId = citizenId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public long getReportStatusId() {
        return reportStatusId;
    }

    public void setReportStatusId(long reportStatusId) {
        this.reportStatusId = reportStatusId;
    }

    public long getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(long reportTypeId) {
        this.reportTypeId = reportTypeId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }
}
