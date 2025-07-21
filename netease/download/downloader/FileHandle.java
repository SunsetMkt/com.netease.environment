package com.netease.download.downloader;

import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes3.dex */
public class FileHandle {
    private ConcurrentHashMap<String, Integer> mCdnerrorMap;
    private DownloadParams mDownloadParams;
    private String mErrorcdn;
    private ConcurrentHashMap<String, Integer> mPartResultMap;
    private double mPatchDlspeed;

    public FileHandle(DownloadParams downloadParams) {
        this.mDownloadParams = downloadParams;
    }

    public ConcurrentHashMap<String, Integer> getPartResultMap() {
        if (this.mPartResultMap == null) {
            this.mPartResultMap = new ConcurrentHashMap<>();
        }
        return this.mPartResultMap;
    }

    public void setPartResultMap(ConcurrentHashMap<String, Integer> concurrentHashMap) {
        this.mPartResultMap = concurrentHashMap;
    }

    public double getPatchDlspeed() {
        return this.mPatchDlspeed;
    }

    public void setPatchDlspeed(double d) {
        this.mPatchDlspeed = d;
    }

    public DownloadParams getDownloadParams() {
        return this.mDownloadParams;
    }

    public void setDownloadParams(DownloadParams downloadParams) {
        this.mDownloadParams = downloadParams;
    }

    public String getErrorcdn() {
        return this.mErrorcdn;
    }

    public void setErrorcdn(String str) {
        this.mErrorcdn = str;
    }

    public ConcurrentHashMap<String, Integer> getCdnerrorMap() {
        if (this.mCdnerrorMap == null) {
            this.mCdnerrorMap = new ConcurrentHashMap<>();
        }
        return this.mCdnerrorMap;
    }

    public void setCdnerrorMap(ConcurrentHashMap<String, Integer> concurrentHashMap) {
        this.mCdnerrorMap = concurrentHashMap;
    }
}