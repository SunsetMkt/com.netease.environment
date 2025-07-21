package com.netease.cloud.nos.android.monitor;

import android.os.Parcel;
import android.os.Parcelable;
import com.netease.cloud.nos.android.exception.InvalidParameterException;
import com.netease.cloud.nos.android.utils.LogUtil;

/* loaded from: classes5.dex */
public class MonitorConfig implements Parcelable {
    private int connectionTimeout;
    private String monitorHost;
    private long monitorInterval;
    private int soTimeout;
    private static final String LOGTAG = LogUtil.makeLogTag(MonitorConfig.class);
    public static final Parcelable.Creator<MonitorConfig> CREATOR = new b();

    public MonitorConfig() {
        this.monitorHost = "http://wanproxy.127.net";
        this.connectionTimeout = 10000;
        this.soTimeout = 30000;
        this.monitorInterval = 120000L;
    }

    public MonitorConfig(String str, int i, int i2, long j) {
        this.monitorHost = str;
        this.connectionTimeout = i;
        this.soTimeout = i2;
        this.monitorInterval = j;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public String getMonitorHost() {
        return this.monitorHost;
    }

    public long getMonitorInterval() {
        return this.monitorInterval;
    }

    public int getSoTimeout() {
        return this.soTimeout;
    }

    public void setConnectionTimeout(int i) throws InvalidParameterException {
        if (i > 0) {
            this.connectionTimeout = i;
        } else {
            throw new InvalidParameterException("Invalid ConnectionTimeout:" + i);
        }
    }

    public void setMonitorInterval(long j) {
        if (j >= 60000) {
            this.monitorInterval = j;
            return;
        }
        LogUtil.w(LOGTAG, "Invalid monitorInterval:" + j);
    }

    public void setMontiroHost(String str) {
        this.monitorHost = str;
    }

    public void setSoTimeout(int i) throws InvalidParameterException {
        if (i > 0) {
            this.soTimeout = i;
        } else {
            throw new InvalidParameterException("Invalid soTimeout:" + i);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.monitorHost);
        parcel.writeInt(this.connectionTimeout);
        parcel.writeInt(this.soTimeout);
        parcel.writeLong(this.monitorInterval);
    }
}