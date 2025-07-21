package com.netease.cloud.nos.android.monitor;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes5.dex */
public class StatisticItem implements Parcelable {
    public static final Parcelable.Creator<StatisticItem> CREATOR = new e();
    private String bucketName;
    private int chunkRetryCount;
    private String clientIP;
    private long fileSize;
    private int lbsHttpCode;
    private String lbsIP;
    private int lbsSucc;
    private long lbsUseTime;
    private String netEnv;
    private String platform;
    private int queryRetryCount;
    private String sdkVersion;
    private int uploadRetryCount;
    private int uploadType;
    private int uploaderHttpCode;
    private String uploaderIP;
    private int uploaderSucc;
    private long uploaderUseTime;

    public StatisticItem() {
        this.platform = "android";
        this.sdkVersion = "2.0";
        this.lbsSucc = 0;
        this.uploaderSucc = 0;
        this.lbsHttpCode = 200;
        this.uploaderHttpCode = 200;
        this.chunkRetryCount = 0;
        this.queryRetryCount = 0;
        this.uploadRetryCount = 0;
        this.uploadType = 1000;
    }

    public StatisticItem(String str, String str2, String str3, String str4, String str5, long j, String str6, long j2, long j3, int i, int i2, int i3, int i4, int i5, int i6, int i7, String str7, int i8) {
        this.platform = str;
        this.clientIP = str2;
        this.sdkVersion = str3;
        this.lbsIP = str4;
        this.uploaderIP = str5;
        this.fileSize = j;
        this.netEnv = str6;
        this.lbsUseTime = j2;
        this.uploaderUseTime = j3;
        this.lbsSucc = i;
        this.uploaderSucc = i2;
        this.lbsHttpCode = i3;
        this.uploaderHttpCode = i4;
        this.chunkRetryCount = i5;
        this.queryRetryCount = i6;
        this.uploadRetryCount = i7;
        this.bucketName = str7;
        this.uploadType = i8;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getBucketName() {
        return this.bucketName;
    }

    public int getChunkRetryCount() {
        return this.chunkRetryCount;
    }

    public String getClientIP() {
        return this.clientIP;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public int getLbsHttpCode() {
        return this.lbsHttpCode;
    }

    public String getLbsIP() {
        return this.lbsIP;
    }

    public int getLbsSucc() {
        return this.lbsSucc;
    }

    public long getLbsUseTime() {
        return this.lbsUseTime;
    }

    public String getNetEnv() {
        return this.netEnv;
    }

    public String getPlatform() {
        return this.platform;
    }

    public int getQueryRetryCount() {
        return this.queryRetryCount;
    }

    public String getSdkVersion() {
        return this.sdkVersion;
    }

    public int getUploadRetryCount() {
        return this.uploadRetryCount;
    }

    public int getUploadType() {
        return this.uploadType;
    }

    public int getUploaderHttpCode() {
        return this.uploaderHttpCode;
    }

    public String getUploaderIP() {
        return this.uploaderIP;
    }

    public int getUploaderSucc() {
        return this.uploaderSucc;
    }

    public long getUploaderUseTime() {
        return this.uploaderUseTime;
    }

    public void setBucketName(String str) {
        this.bucketName = str;
    }

    public void setChunkRetryCount(int i) {
        this.chunkRetryCount = i;
    }

    public void setClientIP(String str) {
        this.clientIP = str;
    }

    public void setFileSize(long j) {
        this.fileSize = j;
    }

    public void setLbsHttpCode(int i) {
        this.lbsHttpCode = i;
    }

    public void setLbsIP(String str) {
        this.lbsIP = str;
    }

    public void setLbsSucc(int i) {
        this.lbsSucc = i;
    }

    public void setLbsUseTime(long j) {
        this.lbsUseTime = j;
    }

    public void setNetEnv(String str) {
        this.netEnv = str;
    }

    public void setQueryRetryCount(int i) {
        this.queryRetryCount = i;
    }

    public void setUploadRetryCount(int i) {
        this.uploadRetryCount = i;
    }

    public void setUploadType(int i) {
        this.uploadType = i;
    }

    public void setUploaderHttpCode(int i) {
        this.uploaderHttpCode = i;
    }

    public void setUploaderIP(String str) {
        this.uploaderIP = str;
    }

    public void setUploaderSucc(int i) {
        this.uploaderSucc = i;
    }

    public void setUploaderUseTime(long j) {
        this.uploaderUseTime = j;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.platform);
        parcel.writeString(this.clientIP);
        parcel.writeString(this.sdkVersion);
        parcel.writeString(this.lbsIP);
        parcel.writeString(this.uploaderIP);
        parcel.writeLong(this.fileSize);
        parcel.writeString(this.netEnv);
        parcel.writeLong(this.lbsUseTime);
        parcel.writeLong(this.uploaderUseTime);
        parcel.writeInt(this.lbsSucc);
        parcel.writeInt(this.uploaderSucc);
        parcel.writeInt(this.lbsHttpCode);
        parcel.writeInt(this.uploaderHttpCode);
        parcel.writeInt(this.chunkRetryCount);
        parcel.writeInt(this.queryRetryCount);
        parcel.writeInt(this.uploadRetryCount);
        parcel.writeString(this.bucketName);
        parcel.writeInt(this.uploadType);
    }
}