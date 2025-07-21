package com.netease.pharos.report;

/* loaded from: classes5.dex */
public class NetmonReport {
    private String mCliIp;
    private String mCliMtr;
    private long mKcptestTime;
    private String mLinktestId;
    private String mLinktestProtocol;
    private String mNetCarrier;
    private String mNetworkCondition;
    private long mPacketCount;
    public long mPacketLossCount = 0;
    private String mSvrIp;
    private String mSvrMtr;
    private long mTcptestTime;
    private String mTimeZone;
    private long mUdptestTime;
    private int mWifiSignal;

    public String getLinktestId() {
        return this.mLinktestId;
    }

    public void setLinktestId(String str) {
        this.mLinktestId = str;
    }

    public String getNetworkCondition() {
        return this.mNetworkCondition;
    }

    public void setNetworkCondition(String str) {
        this.mNetworkCondition = str;
    }

    public int getWifiSignal() {
        return this.mWifiSignal;
    }

    public void setWifiSignal(int i) {
        this.mWifiSignal = i;
    }

    public String getCliIp() {
        return this.mCliIp;
    }

    public void setCliIp(String str) {
        this.mCliIp = str;
    }

    public String getSvrIp() {
        return this.mSvrIp;
    }

    public void setSvrIp(String str) {
        this.mSvrIp = str;
    }

    public String getTimeZone() {
        return this.mTimeZone;
    }

    public void setTimeZone(String str) {
        this.mTimeZone = str;
    }

    public String getNetCarrier() {
        return this.mNetCarrier;
    }

    public void setNetCarrier(String str) {
        this.mNetCarrier = str;
    }

    public String getLinktestProtocol() {
        return this.mLinktestProtocol;
    }

    public void setLinktestProtocol(String str) {
        this.mLinktestProtocol = str;
    }

    public long getTcptestTime() {
        return this.mTcptestTime;
    }

    public void setTcptestTime(long j) {
        this.mTcptestTime = j;
    }

    public long getKcptestTime() {
        return this.mKcptestTime;
    }

    public void setKcptestTime(long j) {
        this.mKcptestTime = j;
    }

    public long getUdptestTime() {
        return this.mUdptestTime;
    }

    public void setUdptestTime(long j) {
        this.mUdptestTime = j;
    }

    public String getCliMtr() {
        return this.mCliMtr;
    }

    public void setCliMtr(String str) {
        this.mCliMtr = str;
    }

    public String getSvrMtr() {
        return this.mSvrMtr;
    }

    public void setSvrMtr(String str) {
        this.mSvrMtr = str;
    }

    public long getPacketLossCount() {
        return this.mPacketLossCount;
    }

    public synchronized void addPacketLossCount() {
        this.mPacketLossCount++;
    }

    public void setPacketLossCount(long j) {
        this.mPacketLossCount = j;
    }

    public long getPacketCount() {
        return this.mPacketCount;
    }

    public void setPacketCount(long j) {
        this.mPacketCount = j;
    }
}