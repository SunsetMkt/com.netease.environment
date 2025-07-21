package com.netease.cloud.nos.android.core;

import java.util.Map;

/* loaded from: classes5.dex */
public class WanNOSObject {
    private String contentMD5;
    private String contentType;
    private String nosBucketName;
    private String nosObjectName;
    private String uploadToken;
    private Map<String, String> userMetadata;

    public WanNOSObject() {
    }

    public WanNOSObject(String str, String str2, String str3, String str4, Map<String, String> map) {
        this.uploadToken = str;
        this.nosBucketName = str2;
        this.nosObjectName = str3;
        this.contentMD5 = str4;
        this.userMetadata = map;
    }

    public String getContentMD5() {
        return this.contentMD5;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getNosBucketName() {
        return this.nosBucketName;
    }

    public String getNosObjectName() {
        return this.nosObjectName;
    }

    public String getUploadToken() {
        return this.uploadToken;
    }

    public Map<String, String> getUserMetadata() {
        return this.userMetadata;
    }

    public void setContentMD5(String str) {
        this.contentMD5 = str;
    }

    public void setContentType(String str) {
        this.contentType = str;
    }

    public void setNosBucketName(String str) {
        this.nosBucketName = str;
    }

    public void setNosObjectName(String str) {
        this.nosObjectName = str;
    }

    public void setUploadToken(String str) {
        this.uploadToken = str;
    }

    public void setUserMetadata(Map<String, String> map) {
        this.userMetadata = map;
    }
}