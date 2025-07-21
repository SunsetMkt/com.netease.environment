package com.netease.pharos.config;

/* loaded from: classes5.dex */
public class CheckConfig {
    private String mExclude_game;
    private String mInclude_game;
    private int mInterval;
    private String mLinktest_protocal;
    private String mLinktest_region;
    private String mLinktest_size;
    private String mLocation;
    private String mNetwork;
    private String mPingtest_region;
    private long mTraceThreshold;

    public String getLocation() {
        return this.mLocation;
    }

    public void setLocation(String str) {
        this.mLocation = str;
    }

    public String getNetwork() {
        return this.mNetwork;
    }

    public void setNetwork(String str) {
        this.mNetwork = str;
    }

    public String getPingtest_region() {
        return this.mPingtest_region;
    }

    public void setPingtest_region(String str) {
        this.mPingtest_region = str;
    }

    public String getLinktest_region() {
        return this.mLinktest_region;
    }

    public void setLinktest_region(String str) {
        this.mLinktest_region = str;
    }

    public String getLinktest_protocal() {
        return this.mLinktest_protocal;
    }

    public void setLinktest_protocal(String str) {
        this.mLinktest_protocal = str;
    }

    public String getLinktest_size() {
        return this.mLinktest_size;
    }

    public void setLinktest_size(String str) {
        this.mLinktest_size = str;
    }

    public String getInclude_game() {
        return this.mInclude_game;
    }

    public void setInclude_game(String str) {
        this.mInclude_game = str;
    }

    public String getExclude_game() {
        return this.mExclude_game;
    }

    public void setExclude_game(String str) {
        this.mExclude_game = str;
    }

    public int getInterval() {
        return this.mInterval;
    }

    public void setInterval(int i) {
        this.mInterval = i;
    }

    public long getTraceThreshold() {
        return this.mTraceThreshold;
    }

    public void setTraceThreshold(long j) {
        this.mTraceThreshold = j;
    }
}