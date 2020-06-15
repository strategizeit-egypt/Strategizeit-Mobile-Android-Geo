package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplaintFilesDTO {
    @Expose
    @SerializedName("FilePath")
    private String filePath;
    @Expose
    @SerializedName("FileTypeId")
    private int fileTypeId;
    @Expose
    @SerializedName("ReportId")
    private int reportId;
    @Expose
    @SerializedName("Id")
    private long id;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(int fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
