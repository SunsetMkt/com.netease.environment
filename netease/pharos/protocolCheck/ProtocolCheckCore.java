package com.netease.pharos.protocolCheck;

import android.text.TextUtils;
import com.netease.pharos.linkcheck.CheckOverNotifyListener;
import com.netease.pharos.linkcheck.CycleTaskStopListener;
import com.netease.pharos.report.NetmonReport;
import com.netease.pharos.util.LogUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/* loaded from: classes4.dex */
public class ProtocolCheckCore implements Callable<Integer> {
    private static final String TAG = "NetmonProxy";
    public static Map<Integer, NetmonReport> mNetmonReportMap = new HashMap();
    private int mCount;
    private String mExtra;
    private String mIp;
    private int mPort;
    private String mRegion;
    private int mSize;
    private int mTime;
    private int mType;
    private ProtocolCheckListener mListener = null;
    private CycleTaskStopListener mCycleTaskStopListener = null;
    private CheckOverNotifyListener mCheckOverNotifyListener = null;
    private ProtocolCheck mLinkCheck = null;
    private int mInterval = -1;

    public void init(int i, String str, int i2, int i3, int i4, int i5) {
        this.mType = i;
        this.mIp = str;
        this.mPort = i2;
        this.mCount = i3;
        this.mTime = i4;
        this.mSize = i5;
    }

    public String getmExtra() {
        return this.mExtra;
    }

    public void setmExtra(String str) {
        this.mExtra = str;
    }

    public void setRegion(String str) {
        this.mRegion = str;
    }

    public String getRegion() {
        return this.mRegion;
    }

    public int getmInterval() {
        return this.mInterval;
    }

    public void setmInterval(int i) {
        this.mInterval = i;
    }

    public ProtocolCheckListener getmListener() {
        return this.mListener;
    }

    public void setmListener(ProtocolCheckListener protocolCheckListener) {
        this.mListener = protocolCheckListener;
    }

    public CycleTaskStopListener getmCycleTaskStopListener() {
        return this.mCycleTaskStopListener;
    }

    public void setmCycleTaskStopListener(CycleTaskStopListener cycleTaskStopListener) {
        this.mCycleTaskStopListener = cycleTaskStopListener;
    }

    public CheckOverNotifyListener getmCheckOverNotifyListener() {
        return this.mCheckOverNotifyListener;
    }

    public void setmCheckOverNotifyListener(CheckOverNotifyListener checkOverNotifyListener) {
        this.mCheckOverNotifyListener = checkOverNotifyListener;
    }

    public int check(int i, String str, int i2, int i3, int i4, int i5) {
        LogUtil.i(TAG, "NetmonCore check");
        LogUtil.i(TAG, "Link check1 \u53c2\u6570 type=" + i + ", ip=" + str + ", port=" + i2 + ", count=" + i3 + ", time=" + i4 + ", size=" + i5 + ", mInterval=" + this.mInterval + ", mExtra= " + this.mExtra);
        NetmonReport netmonReport = new NetmonReport();
        netmonReport.setPacketCount((long) i3);
        mNetmonReportMap.put(Integer.valueOf(i), netmonReport);
        this.mLinkCheck = new ProtocolCheck();
        if (!TextUtils.isEmpty(this.mRegion)) {
            this.mLinkCheck.setRegion(this.mRegion);
        }
        int i6 = this.mInterval;
        if (-1 != i6) {
            this.mLinkCheck.setInterval(i6);
        }
        ProtocolCheckListener protocolCheckListener = this.mListener;
        if (protocolCheckListener != null) {
            this.mLinkCheck.setmListener(protocolCheckListener);
        }
        CycleTaskStopListener cycleTaskStopListener = this.mCycleTaskStopListener;
        if (cycleTaskStopListener != null) {
            this.mLinkCheck.setmCycleTaskStopListener(cycleTaskStopListener);
        }
        CheckOverNotifyListener checkOverNotifyListener = this.mCheckOverNotifyListener;
        if (checkOverNotifyListener != null) {
            this.mLinkCheck.setmCheckOverNotifyListener(checkOverNotifyListener);
        }
        if (!TextUtils.isEmpty(this.mExtra)) {
            this.mLinkCheck.setmExtra(this.mExtra);
        }
        return this.mLinkCheck.check(i, str, i2, i3, i4, i5);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        return Integer.valueOf(check(this.mType, this.mIp, this.mPort, this.mCount, this.mTime, this.mSize));
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("\nmType=");
        stringBuffer.append(this.mType).append("\nmIp=");
        stringBuffer.append(this.mIp).append("\nmPort=");
        stringBuffer.append(this.mPort).append("\nmTime=");
        stringBuffer.append(this.mTime).append("\nmCount=");
        stringBuffer.append(this.mCount).append("\nmSize=");
        stringBuffer.append(this.mSize).append("\nmExtra=");
        stringBuffer.append(this.mExtra).append("\nmInterval=");
        stringBuffer.append(this.mInterval).append("\nmRegion=");
        stringBuffer.append(this.mRegion).append("\n");
        return stringBuffer.toString();
    }

    public void clean() {
        this.mLinkCheck.clean();
    }
}