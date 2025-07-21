package com.netease.pharos.linkcheck;

import android.os.Looper;
import android.text.TextUtils;
import com.netease.pharos.PharosListener;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.ServerProxy;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.locationCheck.NetAreaInfo;
import com.netease.pharos.qos.QosProxy;
import com.netease.pharos.util.LogUtil;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class Proxy {
    private static final String TAG = "LinkCheckProxy";
    public static Proxy sLinkCheckProxy;
    private boolean isCycle = false;
    private boolean isStarting = false;
    private volatile CopyOnWriteArrayList<String> mCycleList = new CopyOnWriteArrayList<>();
    private volatile CopyOnWriteArrayList<String> mOnceList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<String> mStopList = new CopyOnWriteArrayList<>();
    private JSONObject mPharosResultCache = null;
    private final CycleTaskStopListener mCycleTaskStopListener = new CycleTaskStopListener() { // from class: com.netease.pharos.linkcheck.Proxy.1
        @Override // com.netease.pharos.linkcheck.CycleTaskStopListener
        public void callBack(String str) {
            LogUtil.i(Proxy.TAG, "LinkCheckProxy [mCycleTaskStopListener][callBack] \u8be5\u4efb\u52a1\u5df2\u7ecf\u7ed3\u675f=" + str);
            Proxy.this.mStopList.add(str);
            if (Proxy.this.mCycleList == null || Proxy.this.mCycleList.size() <= 0 || Proxy.this.mStopList.size() <= 0 || !Proxy.this.mStopList.containsAll(Proxy.this.mCycleList)) {
                return;
            }
            LogUtil.i(Proxy.TAG, "LinkCheckProxy [mCycleTaskStopListener][callBack] \u7ed3\u675f\u4e00\u6b21\u5468\u671f");
            LogUtil.i(Proxy.TAG, "LinkCheckProxy [mCycleTaskStopListener][callBack] \u91cd\u65b0\u53d1\u8d77\u4e00\u6b21\u5468\u671f");
            Proxy.this.isCycle = false;
            Proxy.this.isStarting = false;
            Proxy.this.mStopList.clear();
            Proxy.this.start();
        }
    };
    private final ConfigInfoListener mConfigInfoListener = new ConfigInfoListener() { // from class: com.netease.pharos.linkcheck.Proxy.2
        @Override // com.netease.pharos.linkcheck.ConfigInfoListener
        public void callBack(boolean z, String str) {
            LogUtil.i(Proxy.TAG, "LinkCheckProxy [mConfigInfoListener][callBack]  mOnceList=" + Proxy.this.mOnceList.toString());
            if (!Proxy.this.mOnceList.contains(str)) {
                Proxy.this.mOnceList.add(str);
            }
            if (z) {
                LogUtil.i(Proxy.TAG, "LinkCheckProxy [mConfigInfoListener][callBack]  cycle=" + z + ", extra=" + str);
                Proxy.this.isCycle = z;
                if (Proxy.this.mCycleList.contains(str)) {
                    return;
                }
                Proxy.this.mCycleList.add(str);
            }
        }
    };

    private Proxy() {
    }

    public CopyOnWriteArrayList<String> getmCycleList() {
        return this.mCycleList;
    }

    public void setmCycleList(CopyOnWriteArrayList<String> copyOnWriteArrayList) {
        this.mCycleList = copyOnWriteArrayList;
    }

    public CopyOnWriteArrayList<String> getmOnceList() {
        return this.mOnceList;
    }

    public void setmOnceList(CopyOnWriteArrayList<String> copyOnWriteArrayList) {
        this.mOnceList = copyOnWriteArrayList;
    }

    public void cleanOnceList() {
        this.mOnceList.clear();
    }

    public JSONObject getmPharosResultCache() {
        return this.mPharosResultCache;
    }

    public void setmPharosResultCache(JSONObject jSONObject) {
        this.mPharosResultCache = jSONObject;
    }

    public static Proxy getInstance() {
        if (sLinkCheckProxy == null) {
            sLinkCheckProxy = new Proxy();
        }
        return sLinkCheckProxy;
    }

    public int downloadRegionConfig() {
        LogUtil.i(TAG, "LinkCheckProxy [downloadRegionConfig] \u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6");
        String str = DeviceInfo.getInstance().getmProbeRegion();
        if (TextUtils.isEmpty(str)) {
            return 11;
        }
        LogUtil.i(TAG, "LinkCheckProxy [downloadRegionConfig] probeRegion=" + str);
        String strReplaceAll = ServerProxy.getInstance().getRegionConfigUrl().replaceAll("%s", str);
        String str2 = NetAreaInfo.getInstances().getmProbeConfig();
        LogUtil.i(TAG, "LinkCheckProxy [downloadRegionConfig] probeConfig=" + str2);
        String strReplaceAll2 = strReplaceAll.replaceAll("%x", str2);
        LogUtil.i(TAG, "LinkCheckProxy [downloadRegionConfig] url=" + strReplaceAll2);
        LogUtil.i(TAG, "[Pharos] Probe Refresh url=" + strReplaceAll2);
        RegionConfigCore regionConfigCore = new RegionConfigCore();
        regionConfigCore.init(strReplaceAll2);
        return regionConfigCore.start();
    }

    public void start() {
        LogUtil.i(TAG, "LinkCheckProxy [start] isStarting=" + this.isStarting + ", isCycle=" + this.isCycle);
        if (this.isStarting) {
            LogUtil.i(TAG, "LinkCheckProxy [start] \u4efb\u52a1\u5df2\u7ecf\u8fdb\u884c\u4e2d");
            PharosListener pharosListener = PharosProxy.getInstance().getPharosListener();
            if (pharosListener != null) {
                JSONObject callBackInfo = getInstance().getCallBackInfo();
                if (callBackInfo != null) {
                    LogUtil.i(TAG, "LinkCheckProxy [start] call onResult");
                    pharosListener.onResult(callBackInfo);
                    LogUtil.i(TAG, "LinkCheckProxy [start] call onPharosPolicy");
                    pharosListener.onPharosPolicy(callBackInfo);
                } else {
                    LogUtil.i(TAG, "LinkCheckProxy [start] callBackInfo is null");
                }
                JSONObject qosResult = QosProxy.getInstance().getQosResult();
                if (qosResult != null) {
                    pharosListener.onResult(qosResult);
                    pharosListener.onPharosQos(qosResult);
                    return;
                } else {
                    LogUtil.i(TAG, "LinkCheckProxy [start] qosResult is null");
                    return;
                }
            }
            return;
        }
        if (this.isCycle) {
            LogUtil.i(TAG, "LinkCheckProxy [start] \u4efb\u52a1\u5b58\u5728\u5faa\u73af\u673a\u5236\uff0c\u4e0d\u80fd\u518d\u6b21\u542f\u52a8");
            PharosListener pharosListener2 = PharosProxy.getInstance().getPharosListener();
            if (pharosListener2 != null) {
                JSONObject qosResult2 = QosProxy.getInstance().getQosResult();
                if (qosResult2 != null) {
                    pharosListener2.onResult(qosResult2);
                    pharosListener2.onPharosQos(qosResult2);
                    return;
                } else {
                    LogUtil.i(TAG, "LinkCheckProxy [start] qosResult is null");
                    return;
                }
            }
            return;
        }
        this.mCycleList.clear();
        this.mOnceList.clear();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(new Runnable() { // from class: com.netease.pharos.linkcheck.Proxy.3
                @Override // java.lang.Runnable
                public void run() {
                    Proxy.this.isStarting = true;
                    LogUtil.i(Proxy.TAG, "LinkCheckProxy [start] \u53d1\u8d77\u4e00\u6b21\u63a2\u6d4b\u5468\u671f");
                    int iDownloadRegionConfig = Proxy.this.downloadRegionConfig();
                    LogUtil.i(Proxy.TAG, "LinkCheckProxy [start] \u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6\u7ed3\u679c=" + iDownloadRegionConfig);
                    if (iDownloadRegionConfig == 0) {
                        ScanProxy.getInstance().init(Proxy.this.mCycleTaskStopListener, Proxy.this.mConfigInfoListener);
                        ScanProxy.getInstance().start();
                    }
                    Proxy.this.isStarting = false;
                }
            }).start();
            return;
        }
        this.isStarting = true;
        LogUtil.i(TAG, "LinkCheckProxy [start] \u53d1\u8d77\u4e00\u6b21\u63a2\u6d4b\u5468\u671f");
        int iDownloadRegionConfig = downloadRegionConfig();
        LogUtil.i(TAG, "LinkCheckProxy [start] \u4e0b\u8f7d\u914d\u7f6e\u6587\u4ef6\u7ed3\u679c=" + iDownloadRegionConfig);
        if (iDownloadRegionConfig == 0) {
            ScanProxy.getInstance().init(this.mCycleTaskStopListener, this.mConfigInfoListener);
            ScanProxy.getInstance().start();
        }
        this.isStarting = false;
    }

    public JSONObject getPharosResultInfo() {
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        String linktestId = Result.getInstance().getLinktestId();
        String deviceInfo = DeviceInfo.getInstance().getDeviceInfo(true);
        String linkCheckResultInfo = Result.getInstance().getLinkCheckResultInfo();
        JSONObject jSONObject3 = null;
        try {
            jSONObject = !TextUtils.isEmpty(deviceInfo) ? new JSONObject(deviceInfo) : null;
            try {
                if (!TextUtils.isEmpty(linkCheckResultInfo)) {
                    jSONObject3 = new JSONObject(linkCheckResultInfo);
                }
            } catch (Exception e) {
                e = e;
                LogUtil.w(TAG, "LinkCheckProxy [getPharosResultInfo] getCallBackInfo Exception=" + e);
                jSONObject2.put("linktest_id", linktestId);
                jSONObject2.put("policy", jSONObject);
                jSONObject2.put("probe", jSONObject3);
                return jSONObject2;
            }
        } catch (Exception e2) {
            e = e2;
            jSONObject = null;
        }
        try {
            jSONObject2.put("linktest_id", linktestId);
            jSONObject2.put("policy", jSONObject);
            jSONObject2.put("probe", jSONObject3);
        } catch (JSONException e3) {
            e3.printStackTrace();
        }
        return jSONObject2;
    }

    public JSONObject getCallBackInfo() {
        JSONObject jSONObject = getInstance().getmPharosResultCache();
        if (jSONObject != null) {
            LogUtil.i(TAG, "LinkCheckProxy [getCallBackInfo] options=" + PharosProxy.getInstance().getmOption());
            if (PharosProxy.getInstance().getmOption() != 33) {
                jSONObject.remove("probe");
            }
        }
        return jSONObject;
    }

    public void clean() {
        this.isStarting = false;
        this.isCycle = false;
    }
}