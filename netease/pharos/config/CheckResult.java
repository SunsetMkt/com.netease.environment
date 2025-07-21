package com.netease.pharos.config;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public class CheckResult {
    private static final String TAG = "CheckResult";
    private String mAvgRtt;
    private String mExtra;
    private String mIp;
    private String mLoss;
    private int mPacketCount;
    private int mPacketLossCount;
    private int mPort;
    private int mProtocol;
    private String mRegion;
    private int mPacketBytesCount = 0;
    private final List<Long> mTimeList = new ArrayList();
    private ArrayList<String> mIpList = new ArrayList<>();

    public String getmRegion() {
        return this.mRegion;
    }

    public void setmRegion(String str) {
        this.mRegion = str;
    }

    public int getProtocol() {
        return this.mProtocol;
    }

    public int getmPort() {
        return this.mPort;
    }

    public void setmPort(int i) {
        this.mPort = i;
    }

    public void setProtocol(int i) {
        this.mProtocol = i;
    }

    public int getmPacketCount() {
        return this.mPacketCount;
    }

    public void setPacketCount(int i) {
        this.mPacketCount = i;
    }

    public String getIp() {
        return this.mIp;
    }

    public void setIp(String str) {
        this.mIp = str;
    }

    public String getmAvgRtt() {
        return this.mAvgRtt;
    }

    public void setmAvgRtt(String str) {
        this.mAvgRtt = str;
    }

    public String getmLoss() {
        return this.mLoss;
    }

    public void setmLoss(String str) {
        this.mLoss = str;
    }

    public ArrayList<String> getmIpList() {
        return this.mIpList;
    }

    public void setmIpList(ArrayList<String> arrayList) {
        this.mIpList = arrayList;
    }

    public String getmExtra() {
        return this.mExtra;
    }

    public void setmExtra(String str) {
        this.mExtra = str;
    }

    public int getPacketBytesCount() {
        return this.mPacketBytesCount;
    }

    public void setPacketBytesCount(int i) {
        this.mPacketBytesCount = i;
    }

    public List<Long> getTimeList() {
        return this.mTimeList;
    }

    public void addTime(long j) {
        this.mTimeList.add(Long.valueOf(j));
    }

    public int getPacketLossCount() {
        return this.mPacketLossCount;
    }

    public void setPacketLossCount(int i) {
        this.mPacketLossCount = i;
    }

    public long getMinTime() {
        List<Long> list = this.mTimeList;
        if (list == null || list.size() <= 0) {
            return -1L;
        }
        long jLongValue = this.mTimeList.get(0).longValue();
        for (int i = 1; i < this.mTimeList.size(); i++) {
            if (0 != this.mTimeList.get(i).longValue()) {
                jLongValue = Math.min(jLongValue, this.mTimeList.get(i).longValue());
            }
        }
        if (0 == jLongValue) {
            return -1L;
        }
        return jLongValue;
    }

    public long getMaxTime() {
        if (this.mTimeList.size() <= 0) {
            return -1L;
        }
        long jLongValue = this.mTimeList.get(0).longValue();
        for (int i = 1; i < this.mTimeList.size(); i++) {
            jLongValue = Math.max(jLongValue, this.mTimeList.get(i).longValue());
        }
        return jLongValue;
    }

    public long getAvgTime() {
        List<Long> list = this.mTimeList;
        if (list == null || list.size() <= 0) {
            return -1L;
        }
        int iLongValue = 0;
        for (int i = 0; i < this.mTimeList.size(); i++) {
            iLongValue = (int) (iLongValue + this.mTimeList.get(i).longValue());
        }
        return iLongValue / this.mTimeList.size();
    }

    public long getAvgSpeed() {
        long avgTime = getAvgTime();
        if (this.mPacketBytesCount == 0 || -1 == avgTime || 0 == getAvgTime()) {
            return -1L;
        }
        return ((this.mPacketBytesCount / getAvgTime()) * 1000) / 1024;
    }

    public double getLoss() {
        if (getmPacketCount() != 0) {
            return getPacketLossCount() / getmPacketCount();
        }
        return -1.0d;
    }

    public double getStddev() {
        return Math.sqrt(Math.abs(getVariance()));
    }

    public double getVariance() {
        List<Long> list = this.mTimeList;
        if (list == null || list.size() <= 0) {
            return -1.0d;
        }
        int size = this.mTimeList.size();
        long squareSum = getSquareSum();
        long avgTime = getAvgTime();
        long j = size;
        return (squareSum - ((j * avgTime) * avgTime)) / j;
    }

    public long getSquareSum() {
        List<Long> list = this.mTimeList;
        if (list == null || list.size() == 0) {
            return -1L;
        }
        int size = this.mTimeList.size();
        long jLongValue = 0;
        for (int i = 0; i < size; i++) {
            jLongValue += this.mTimeList.get(i).longValue() * this.mTimeList.get(i).longValue();
        }
        return jLongValue;
    }

    public String getPingInfo() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getProtocol()).append(" ping ").append(getIp()).append(": ").append(getPacketBytesCount()).append(" data bytes\n");
        for (int i = 0; i < getTimeList().size(); i++) {
            stringBuffer.append(getProtocol()).append(" ").append(getPacketBytesCount()).append(" bytes from ").append(getIp()).append(" seq=").append(i).append(" time=").append(getTimeList().get(i)).append("ms\n");
        }
        stringBuffer.append("--- ").append(getIp()).append(" ").append(getProtocol()).append(" ping statistics ---\n");
        stringBuffer.append(getmPacketCount()).append(" packets transmitted, ").append(getmPacketCount() - getPacketLossCount()).append(" packeds received, ").append(getPacketLossCount() / getmPacketCount()).append(" packed loss\nround-trip min/avg/max/stddev = ");
        stringBuffer.append(getMinTime()).append("/").append(getAvgTime()).append("/").append(getMaxTime()).append("/").append(getStddev()).append("\n");
        return stringBuffer.toString();
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("\nmRegion=");
        stringBuffer.append(this.mRegion).append("\nmProtocol=");
        stringBuffer.append(this.mProtocol).append("\nmIp=");
        stringBuffer.append(this.mIp).append("\nmPort=");
        stringBuffer.append(this.mPort).append("\nmPacketBytesCount=");
        stringBuffer.append(this.mPacketBytesCount).append("\nmPacketCount=");
        stringBuffer.append(this.mPacketCount).append("\nmPacketLossCount=");
        stringBuffer.append(this.mPacketLossCount).append("\nmCalculateLoss=");
        stringBuffer.append(getPacketLossCount() / getmPacketCount()).append("\nmBestRtt=");
        stringBuffer.append(getMinTime()).append("\ngetAvgTime=");
        stringBuffer.append(getAvgTime()).append("\nmAvgSpeed=");
        stringBuffer.append(getAvgSpeed()).append("\nmIpList=");
        stringBuffer.append(this.mIpList.toString()).append("\nmLoss=");
        stringBuffer.append(this.mLoss).append("\nmAvgRtt=");
        stringBuffer.append(this.mAvgRtt).append("\nmExtra=");
        stringBuffer.append(this.mExtra).append("\n");
        return stringBuffer.toString();
    }
}