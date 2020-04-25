package com.massage.welny.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomerPackage {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("hours_left")
    @Expose
    private Float hoursLeft;

    @SerializedName("package")
    @Expose
    private PackageResponse packageResponse;

    @SerializedName("submission_time")
    @Expose
    private String submissionTime;

    @SerializedName("expiration_time")
    @Expose
    private String expirationTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Float getHoursLeft() {
        return hoursLeft;
    }

    public void setHoursLeft(Float hoursLeft) {
        this.hoursLeft = hoursLeft;
    }

    public PackageResponse getPackageResponse() {
        return packageResponse;
    }

    public void setPackageResponse(PackageResponse packageResponse) {
        this.packageResponse = packageResponse;
    }

    public String getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(String submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(String expirationTime) {
        this.expirationTime = expirationTime;
    }
}
