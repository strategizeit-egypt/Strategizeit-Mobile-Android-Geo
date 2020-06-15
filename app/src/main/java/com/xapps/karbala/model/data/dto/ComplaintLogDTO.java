package com.xapps.karbala.model.data.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class ComplaintLogDTO {
    @Expose
    @SerializedName("CreationDate")
    private String creationdate;
    @Expose
    @SerializedName("ReportStatusId")
    private int reportstatusid;
    @Expose
    @SerializedName("Description")
    private String description;
    @Expose
    @SerializedName("CommentForManager")
    private String commentformanager;
    @Expose
    @SerializedName("CommentForUser")
    private String commentforuser;
    @Expose
    @SerializedName("Email")
    private String email;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("ReportId")
    private long reportid;
    @Expose
    @SerializedName("Id")
    private long id;

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public int getReportstatusid() {
        return reportstatusid;
    }

    public void setReportstatusid(int reportstatusid) {
        this.reportstatusid = reportstatusid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommentformanager() {
        return commentformanager;
    }

    public void setCommentformanager(String commentformanager) {
        this.commentformanager = commentformanager;
    }

    public String getCommentforuser() {
        return commentforuser;
    }

    public void setCommentforuser(String commentforuser) {
        this.commentforuser = commentforuser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getReportid() {
        return reportid;
    }

    public void setReportid(long reportid) {
        this.reportid = reportid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
