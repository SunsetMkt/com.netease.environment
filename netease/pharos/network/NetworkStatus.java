package com.netease.pharos.network;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.UniWebView;
import com.netease.pharos.PharosListener;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import java.util.Timer;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class NetworkStatus {
    private static final String TAG = "NetworkStatus";
    private static NetworkStatus sNetworkStatus;
    private NetworkType mCurrentNetwork;
    private boolean sIsInit = false;
    private boolean hasNetworkChanged = false;
    private long lastChangeTime = 0;
    private NetworkType mPreNetwork = NetworkType.INIT;
    private Timer mTimer = new Timer();

    private NetworkStatus() {
    }

    public static NetworkStatus getInstance() {
        if (sNetworkStatus == null) {
            synchronized (NetworkStatus.class) {
                if (sNetworkStatus == null) {
                    sNetworkStatus = new NetworkStatus();
                }
            }
        }
        return sNetworkStatus;
    }

    public String getNetwork() {
        return this.mCurrentNetwork.value();
    }

    public synchronized void setNetwork(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mCurrentNetwork = NetworkType.getNetwork(str);
        }
    }

    public String getNetworkType() {
        this.mCurrentNetwork = NetworkType.getNetwork(Util.getSystemParams(UniWebView.ACTION_GETNETWORKTYPE));
        LogUtil.i(TAG, "\u65e5\u5fd7\u4e0a\u4f20\u6a21\u5757---Network Type : " + this.mCurrentNetwork.value());
        return this.mCurrentNetwork.value();
    }

    public void initialize() {
        if (this.sIsInit) {
            return;
        }
        this.sIsInit = true;
    }

    public synchronized void change() {
        NetworkType networkType;
        long jCurrentTimeMillis = System.currentTimeMillis();
        LogUtil.i(TAG, "NetworkStatus [change]");
        boolean z = true;
        Util.isNeedReadIp.set(true);
        if (isConnectedWifi()) {
            LogUtil.i(TAG, "\u8fde\u63a5\u7684\u662fWIFI\u7f51\u7edc");
            networkType = NetworkType.WIFI;
        } else if (isConnectedMobile()) {
            LogUtil.i(TAG, "\u8fde\u63a5\u7684\u662f\u79fb\u52a8\u7f51\u7edc");
            networkType = NetworkType.MOBILE;
        } else {
            LogUtil.i(TAG, "\u65e0\u7f51\u7edc");
            networkType = NetworkType.NONE;
        }
        if (networkType == this.mCurrentNetwork && NetworkType.INIT == this.mPreNetwork) {
            this.mPreNetwork = networkType;
            return;
        }
        if (NetworkType.INIT == this.mPreNetwork) {
            this.mPreNetwork = networkType;
        }
        this.mCurrentNetwork = networkType;
        if (jCurrentTimeMillis - this.lastChangeTime < 1000 && this.mPreNetwork == networkType) {
            this.lastChangeTime = jCurrentTimeMillis;
            LogUtil.i(TAG, "network_switch#has changed:" + this.hasNetworkChanged + ",Pre Network:" + this.mPreNetwork.value() + ",current Network:" + this.mCurrentNetwork.value());
            return;
        }
        if (this.mPreNetwork == networkType) {
            z = false;
        }
        this.hasNetworkChanged = z;
        LogUtil.i(TAG, "network_switch#has changed:" + this.hasNetworkChanged + ",Pre Network:" + this.mPreNetwork.value() + ",current Network:" + this.mCurrentNetwork.value());
        if (this.mCurrentNetwork != this.mPreNetwork) {
            PharosProxy.getInstance().clean();
        }
        PharosListener pharosListener = PharosProxy.getInstance().getPharosListener();
        if (pharosListener != null) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("methodId", "pharosOnNetworkChange");
                jSONObject.put("project", DeviceInfo.getInstance().getProject());
                jSONObject.put("udid", DeviceInfo.getInstance().getUdid());
                jSONObject.put("network", this.mCurrentNetwork.value());
                pharosListener.onResult(jSONObject);
                pharosListener.onNetworkChanged(jSONObject);
            } catch (Exception unused) {
            }
        }
        this.mPreNetwork = this.mCurrentNetwork;
        this.lastChangeTime = jCurrentTimeMillis;
    }

    private boolean isConnected() {
        return "unknown".equalsIgnoreCase(Util.getSystemParams(UniWebView.ACTION_GETNETWORKTYPE));
    }

    public boolean isNetworkChanged() {
        return this.hasNetworkChanged;
    }

    private boolean isConnectedWifi() {
        return "wifi".equalsIgnoreCase(Util.getSystemParams(UniWebView.ACTION_GETNETWORKTYPE));
    }

    private boolean isConnectedMobile() {
        return ConstProp.NT_AUTH_NAME_MOBILE.equalsIgnoreCase(Util.getSystemParams(UniWebView.ACTION_GETNETWORKTYPE));
    }

    private int getNetStatus() {
        return this.mPreNetwork.flag();
    }

    public void clean() {
        LogUtil.i(TAG, "network_switch:" + this.hasNetworkChanged + ",pre network:" + this.mPreNetwork.value() + "[clean]");
        this.hasNetworkChanged = false;
    }
}