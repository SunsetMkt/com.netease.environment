package com.netease.pharos.qos;

import com.netease.pharos.linkcheck.RegionConfigInfo;
import com.netease.pharos.util.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class QosProxy {
    private static final String TAG = "QosProxy";
    private static QosProxy sQosProxy;
    private boolean mIsInit = false;
    private boolean mIsStart = false;
    private QosCore mQosCore = null;

    private QosProxy() {
    }

    public static QosProxy getInstance() {
        if (sQosProxy == null) {
            synchronized (QosProxy.class) {
                if (sQosProxy == null) {
                    sQosProxy = new QosProxy();
                }
            }
        }
        return sQosProxy;
    }

    public void init() throws JSONException {
        if (!this.mIsInit) {
            LogUtil.i(TAG, "QosProxy [init] start");
            if (this.mQosCore == null) {
                this.mQosCore = new QosCore();
            }
            this.mQosCore.init(RegionConfigInfo.getInstance().getRapQos());
            this.mQosCore.parse();
            this.mIsInit = true;
            return;
        }
        LogUtil.i(TAG, "QosProxy [init] already init");
    }

    public void start_qosCore() {
        LogUtil.i(TAG, "QosProxy [start_qosCore] start");
        if (this.mQosCore != null) {
            LogUtil.i(TAG, "\u5f00\u59cbQos\u6a21\u5757");
            try {
                this.mQosCore.checkIsNeedToQos();
            } catch (JSONException e) {
                LogUtil.i(TAG, "QosProxy [start_qosCore] JSONException=" + e);
            }
        }
    }

    public JSONObject getQosResult() {
        QosCore qosCore = this.mQosCore;
        if (qosCore != null) {
            return qosCore.getQosResult();
        }
        return null;
    }

    public String getDest() {
        QosCore qosCore = this.mQosCore;
        if (qosCore != null) {
            return qosCore.getDest();
        }
        return null;
    }

    public boolean getQosEffective() {
        QosCore qosCore = this.mQosCore;
        if (qosCore != null) {
            return qosCore.getQosEffective();
        }
        return false;
    }

    public void clean() {
        LogUtil.i(TAG, "QosProxy [clean] start");
        QosCore qosCore = this.mQosCore;
        if (qosCore != null) {
            qosCore.clean();
        }
        this.mIsInit = false;
        this.mIsStart = false;
    }
}