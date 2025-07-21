package com.netease.pharos.locationCheck;

import com.netease.pharos.config.CheckResult;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.util.LogUtil;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes5.dex */
public class RecheckResult {
    private static final String TAG = "RecheckResult";
    private static RecheckResult sRecheckResult;
    private final List<CheckResult> mCheckResultList = new ArrayList();

    public static RecheckResult getInstance() {
        if (sRecheckResult == null) {
            sRecheckResult = new RecheckResult();
        }
        return sRecheckResult;
    }

    public DeviceInfo chooseBest() {
        LogUtil.i(TAG, "mCheckResultList \u5927\u5c0f=" + this.mCheckResultList.size());
        List<CheckResult> list = this.mCheckResultList;
        if (list != null && list.size() > 0) {
            int i = -1;
            long j = -1;
            for (int i2 = 0; i2 < this.mCheckResultList.size(); i2++) {
                ArrayList<String> arrayList = new ArrayList<>();
                this.mCheckResultList.get(i2).getmPacketCount();
                int packetLossCount = this.mCheckResultList.get(i2).getPacketLossCount();
                long minTime = this.mCheckResultList.get(i2).getMinTime();
                arrayList.add(this.mCheckResultList.get(i2).getLoss() + "");
                arrayList.add(minTime + "");
                LogUtil.i(TAG, "lossCount=" + packetLossCount + ", bestRtt=" + j + ", rtt=" + minTime);
                if (packetLossCount <= 4 && minTime < j) {
                    i = i2;
                    j = minTime;
                }
                DeviceInfo.getInstance().getmUdpMap().put(this.mCheckResultList.get(i2).getmRegion(), arrayList);
            }
            LogUtil.i(TAG, "map\u4fe1\u606f=" + DeviceInfo.getInstance().getmUdpMap().toString());
            if (i >= 0 && i < this.mCheckResultList.size()) {
                DeviceInfo.getInstance().setmRegion(this.mCheckResultList.get(i).getmRegion());
                DeviceInfo.getInstance().setmProbeRegion(this.mCheckResultList.get(i).getmRegion());
                DeviceInfo.getInstance().setmMethod("udpping");
            }
        }
        return DeviceInfo.getInstance();
    }

    public List<CheckResult> getList() {
        return this.mCheckResultList;
    }

    public class RecheckResultUnit {
        public String mIp = null;
        public int mCount = -1;
        public int mSuccessCount = -1;
        public int mLoss = -1;
        public int mBsetRtt = -1;
        public int mWorstRtt = -1;

        public RecheckResultUnit() {
        }

        public void setmIp(String str) {
            if (this.mIp == null) {
                this.mIp = str;
            }
        }

        public void setmCount(int i) {
            if (-1 == this.mCount) {
                this.mCount = i;
            }
        }

        public void setmSuccessCount(int i) {
            this.mSuccessCount = i;
        }

        public void setmLoss(int i) {
            this.mLoss = i;
        }

        public void setmBsetRtt(int i) {
            if (this.mBsetRtt < i) {
                this.mBsetRtt = i;
            }
        }

        public void setmWorstRtt(int i) {
            if (i > this.mWorstRtt) {
                this.mWorstRtt = i;
            }
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer("\nmIp=");
            stringBuffer.append(this.mIp).append("\nmCount=");
            stringBuffer.append(this.mCount).append("\nmSuccessCount=");
            stringBuffer.append(this.mSuccessCount).append("\nmLoss=");
            stringBuffer.append(this.mLoss).append("\nmBsetRtt=");
            stringBuffer.append(this.mBsetRtt).append("\nmWorstRtt=");
            stringBuffer.append(this.mWorstRtt).append("\n");
            return stringBuffer.toString();
        }
    }
}