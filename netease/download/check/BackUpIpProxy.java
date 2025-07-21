package com.netease.download.check;

import android.text.TextUtils;
import com.netease.download.util.LogUtil;

/* loaded from: classes6.dex */
public class BackUpIpProxy {
    public static final int DEFAULT = 0;
    public static final int INIT = 1;
    private static final String TAG = "BackUpIpManager";
    public static final int USE = 2;
    public static final int USE_FAIL = -1;
    private static BackUpIpProxy sBackUpIpProxy;
    private String mHistoryTopSpeedIp = null;
    private String mHistoryTopSpeedHost = null;
    private long mHistoryTopSpeed = 0;
    private int mBackUpIpStatus = 0;

    private BackUpIpProxy() {
    }

    public static BackUpIpProxy getInstances() {
        if (sBackUpIpProxy == null) {
            sBackUpIpProxy = new BackUpIpProxy();
        }
        return sBackUpIpProxy;
    }

    public String getHistoryTopSpeedIp() {
        return this.mHistoryTopSpeedIp;
    }

    public String getHistoryTopSpeedHost() {
        return this.mHistoryTopSpeedHost;
    }

    public long getHistoryTopSpeed() {
        return this.mHistoryTopSpeed;
    }

    public void setBackUpInfo(String str, String str2, long j) {
        LogUtil.i(TAG, "BackUpIpProxy [setBackUpInfo] start");
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        LogUtil.i(TAG, "BackUpIpProxy [setBackUpInfo] \u8bbe\u7f6e\u5907\u7528ip\u4fe1\u606f mHistoryTopSpeedIp=" + this.mHistoryTopSpeedIp + ", mHistoryTopSpeedHost=" + this.mHistoryTopSpeedHost + ", mHistoryTopSpeed=" + this.mHistoryTopSpeed);
        this.mHistoryTopSpeedIp = str;
        this.mHistoryTopSpeedHost = str2;
        this.mHistoryTopSpeed = j;
    }

    public void setBackUpIpStatus(int i) {
        int i2 = this.mBackUpIpStatus;
        if (-1 == i2) {
            return;
        }
        if (-1 == i) {
            this.mBackUpIpStatus = i;
        } else if (i2 < i) {
            this.mBackUpIpStatus = i;
        }
    }

    public boolean neverUseBackUpIp() {
        LogUtil.i(TAG, "BackUpIpProxy [neverUseBackUpIp] start");
        if (-1 != this.mBackUpIpStatus) {
            return false;
        }
        LogUtil.i(TAG, "BackUpIpProxy [neverUseBackUpIp] \u5df2\u7ecf\u4f7f\u7528\u8fc7BackUpIp\uff0c\u5e76\u4e14\u5931\u8d25\u8fc7\uff0c\u5176\u4ed6\u65b9\u65e0\u9700\u518d\u4f7f\u7528BackUpIp\u4e86");
        return true;
    }

    public boolean hasInitBackUpIp() {
        int i = this.mBackUpIpStatus;
        return i >= 1 || i == -1;
    }
}