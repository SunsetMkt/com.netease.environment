package com.netease.download.check;

import com.netease.download.config.ConfigParams;
import com.netease.download.config.ConfigProxy;
import com.netease.download.util.LogUtil;

/* loaded from: classes6.dex */
public class CheckTime {
    private static final String TAG = "CheckTime";
    private static long mTopSpeed;
    private long mTimeMarked;
    private long mTimeStarted;
    private long mTotalDownloadBytes = 0;
    private long mCurrentSpeed = 0;
    private int mCheckMinutes = 0;

    private CheckTime() {
    }

    public static CheckTime newInstance() {
        CheckTime checkTime = new CheckTime();
        checkTime.mTimeStarted = System.currentTimeMillis();
        return checkTime;
    }

    public long getAverageSpeed() {
        return this.mCurrentSpeed;
    }

    public CheckTime calculate() {
        long j = this.mTimeMarked;
        long j2 = this.mTimeStarted;
        if (j - j2 > 1000) {
            this.mCurrentSpeed = ((this.mTotalDownloadBytes / 1024) * 1000) / (j - j2);
        }
        return this;
    }

    public void mark(long j) {
        this.mTimeMarked = System.currentTimeMillis();
        this.mTotalDownloadBytes += j;
    }

    public boolean check(String str, ConfigParams configParams, String str2, String str3, boolean z) {
        int i;
        if (!configParams.mRemovable || (i = (int) (((this.mTimeMarked - this.mTimeStarted) / 1000) / ConfigProxy.getInstances().getmConfigParams().mRemoveSlowCDNTime)) == this.mCheckMinutes) {
            return false;
        }
        this.mCheckMinutes = i;
        long averageSpeed = getAverageSpeed();
        if (averageSpeed > mTopSpeed) {
            mTopSpeed = averageSpeed;
            if (averageSpeed > BackUpIpProxy.getInstances().getHistoryTopSpeed()) {
                BackUpIpProxy.getInstances().setBackUpInfo(str2, str3, averageSpeed);
                BackUpIpProxy.getInstances().setBackUpIpStatus(1);
                if (!z) {
                    LogUtil.i(TAG, "[ORBIT] Backup Ip=" + BackUpIpProxy.getInstances().getHistoryTopSpeedIp() + " Domain=" + BackUpIpProxy.getInstances().getHistoryTopSpeedHost() + " Speed=" + BackUpIpProxy.getInstances().getHistoryTopSpeed());
                }
            }
        }
        if (true != (((averageSpeed > ((mTopSpeed * ((long) configParams.mRemoveSlowCDNPercent)) / 100) ? 1 : (averageSpeed == ((mTopSpeed * ((long) configParams.mRemoveSlowCDNPercent)) / 100) ? 0 : -1)) < 0) && ((averageSpeed > ((long) configParams.mRemoveSlowCDNSpeed) ? 1 : (averageSpeed == ((long) configParams.mRemoveSlowCDNSpeed) ? 0 : -1)) < 0))) {
            return false;
        }
        LogUtil.i(TAG, "[ORBIT] Removed Ip=" + str2 + " Domain=" + str3 + " Speed=" + averageSpeed + " MaxSpeed=" + mTopSpeed);
        mTopSpeed = 0L;
        return true;
    }

    public static void clean() {
        mTopSpeed = 0L;
    }

    public String toString() {
        return "CheckTime {mTimeStarted=" + this.mTimeStarted + ", mTimeMarked=" + this.mTimeMarked + ", mTotalDownloadBytes=" + this.mTotalDownloadBytes + ", mAverageSpeed=" + this.mCurrentSpeed + '}';
    }
}