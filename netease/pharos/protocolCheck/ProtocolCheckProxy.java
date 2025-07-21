package com.netease.pharos.protocolCheck;

import com.netease.pharos.linkcheck.CheckOverNotifyListener;
import com.netease.pharos.linkcheck.CycleTaskStopListener;
import com.netease.pharos.threadManager.ThreadPoolManager;
import com.netease.pharos.util.LogUtil;

/* loaded from: classes4.dex */
public class ProtocolCheckProxy {
    private static final String TAG = "NetmonProxy";
    private static ProtocolCheckProxy sNetmonProxy;
    private boolean mCycleOpen = true;

    private ProtocolCheckProxy() {
    }

    public static ProtocolCheckProxy getInstance() {
        if (sNetmonProxy == null) {
            synchronized (ProtocolCheckProxy.class) {
                if (sNetmonProxy == null) {
                    sNetmonProxy = new ProtocolCheckProxy();
                }
            }
        }
        return sNetmonProxy;
    }

    public void addProtocolCheckCore(int i, String str, int i2, int i3, int i4, int i5, ProtocolCheckListener protocolCheckListener, int i6, CycleTaskStopListener cycleTaskStopListener, CheckOverNotifyListener checkOverNotifyListener, String str2) {
        try {
            LogUtil.i(TAG, "NetmonProxy [addNetmonCore1] \u53c2\u6570 type=" + i + ", ip=" + str + ", port=" + i2 + ", count=" + i3 + ", time=" + i4 + ", size=" + i5 + ", interval=" + i6 + ", extra=" + str2);
            ProtocolCheckCore protocolCheckCore = new ProtocolCheckCore();
            protocolCheckCore.init(i, str, i2, i3, i4, i5);
            protocolCheckCore.setmListener(protocolCheckListener);
            protocolCheckCore.setmCycleTaskStopListener(cycleTaskStopListener);
            protocolCheckCore.setmCheckOverNotifyListener(checkOverNotifyListener);
            protocolCheckCore.setmInterval(i6);
            protocolCheckCore.setmExtra(str2);
            ThreadPoolManager.getInstance().getFixedThreadPool().submit(protocolCheckCore);
        } catch (Exception e) {
            LogUtil.i(TAG, "NetmonProxy [addNetmonCore1] Exception=" + e);
            e.printStackTrace();
        }
    }

    public void addProtocolCheckCore(int i, String str, int i2, int i3, int i4, int i5, String str2, ProtocolCheckListener protocolCheckListener, int i6, CycleTaskStopListener cycleTaskStopListener, CheckOverNotifyListener checkOverNotifyListener, String str3) {
        try {
            LogUtil.i(TAG, "NetmonProxy [addNetmonCore2] \u53c2\u6570 type=" + i + ", ip=" + str + ", port=" + i2 + ", count=" + i3 + ", time=" + i4 + ", size=" + i5 + ", region=" + str2 + ", interval=" + i6 + ", extra=" + str3);
            ProtocolCheckCore protocolCheckCore = new ProtocolCheckCore();
            protocolCheckCore.init(i, str, i2, i3, i4, i5);
            protocolCheckCore.setRegion(str2);
            protocolCheckCore.setmListener(protocolCheckListener);
            protocolCheckCore.setmCycleTaskStopListener(cycleTaskStopListener);
            protocolCheckCore.setmCheckOverNotifyListener(checkOverNotifyListener);
            protocolCheckCore.setmInterval(i6);
            protocolCheckCore.setmExtra(str3);
            ThreadPoolManager.getInstance().getSingleThreadExecutor().submit(protocolCheckCore);
        } catch (Exception e) {
            LogUtil.i(TAG, "NetmonProxy [addNetmonCore2] Exception=" + e);
            e.printStackTrace();
        }
    }

    public boolean getCycleOpen() {
        return this.mCycleOpen;
    }

    public synchronized void clean() {
        this.mCycleOpen = false;
        sNetmonProxy = new ProtocolCheckProxy();
    }
}