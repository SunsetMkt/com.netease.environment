package com.netease.pharos;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.hermes.intl.Constants;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.deviceCheck.DevicesInfoProxy;
import com.netease.pharos.linkcheck.Proxy;
import com.netease.pharos.linkcheck.Result;
import com.netease.pharos.locationCheck.LocationCheckProxy;
import com.netease.pharos.netlag.NetworkCheckProxy;
import com.netease.pharos.network.ConnectionChangeReceiver;
import com.netease.pharos.network.NetworkStatus;
import com.netease.pharos.protocolCheck.ProtocolCheckProxy;
import com.netease.pharos.qos.HighSpeedListCoreProxy;
import com.netease.pharos.qos.Qos4GProxy;
import com.netease.pharos.report.ReportProxy;
import com.netease.pharos.util.LogFileProxy;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import com.xiaomi.onetrack.api.b;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PharosProxy {
    private static final String TAG = "PharosProxy";
    public static final Executor THREAD_POOL_EXECUTOR = Executors.newSingleThreadExecutor();
    private static PharosProxy sPharosProxy;
    private Runnable mActive;
    private OnNetLagCallback mOnNetLagCallback;
    private Thread mTaskExecutor;
    private Context mContext = null;
    private String mProjectId = null;
    private String mUdid = null;
    private String mNetId = null;
    private boolean mEB = false;
    private PharosListener mPharosListener = null;
    private String mIp = null;
    private String mPort = null;
    private JSONArray mPorts = null;
    private String mHighSpeedUrl = null;
    private int mOption = 1;
    private int mDecision = 0;
    private String mHarborudp = Constants.CASEFIRST_FALSE;
    private String mIpv6Verify = Constants.CASEFIRST_FALSE;
    public String mCallbackPolicy = Constants.CASEFIRST_FALSE;
    private String mQosIp = null;
    private JSONArray mQosIps = null;
    private boolean mIsDebug = true;
    private boolean mNetworkAware = false;
    private int mArea = -1;
    private boolean mHasSet = false;
    private boolean mHasSetTestHost = false;
    private int mTestLog = 0;
    private String mLagID = "";
    private String mQosExecIp = null;
    private int mQosExecDuration = 0;
    private ConnectionChangeReceiver mReceiver = null;
    private boolean mIsInit = false;
    private boolean mIsRegistered = false;
    private long mRunTime = 0;
    private ArrayBlockingQueue<Runnable> mTasks = new ArrayBlockingQueue<>(1024);

    @Deprecated
    public String getmLinktestId() {
        String linktestId = Result.getInstance().getLinktestId();
        return linktestId == null ? "" : linktestId;
    }

    public int getTestLog() {
        return this.mTestLog;
    }

    public OnNetLagCallback getOnNetLagCallback() {
        return this.mOnNetLagCallback;
    }

    public void setOnNetLagCallback(OnNetLagCallback onNetLagCallback) {
        this.mOnNetLagCallback = onNetLagCallback;
    }

    public void registerReceiver(Context context) {
        LogUtil.i(TAG, "\u6ce8\u518c\u7f51\u7edc\u5e7f\u64ad\u76d1\u542c\u5668 start");
        if (!this.mIsRegistered) {
            LogUtil.i(TAG, "\u6ce8\u518c\u7f51\u7edc\u5e7f\u64ad\u76d1\u542c\u5668");
            if (context == null) {
                LogUtil.i(TAG, "\u6ce8\u518c\u7f51\u7edc\u5e7f\u64ad\u76d1\u542c\u5668\u5931\u8d25");
                return;
            }
            ConnectionChangeReceiver connectionChangeReceiver = new ConnectionChangeReceiver();
            this.mReceiver = connectionChangeReceiver;
            context.registerReceiver(connectionChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.mIsRegistered = true;
            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
                    if (connectivityManager != null) {
                        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() { // from class: com.netease.pharos.PharosProxy.1
                            private LinkProperties currentLinkProperties;
                            private Network currentNetwork;

                            AnonymousClass1() {
                            }

                            @Override // android.net.ConnectivityManager.NetworkCallback
                            public void onAvailable(Network network) {
                                super.onAvailable(network);
                                LogUtil.i(PharosProxy.TAG, "onAvailable netId=" + network);
                            }

                            @Override // android.net.ConnectivityManager.NetworkCallback
                            public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                                super.onLinkPropertiesChanged(network, linkProperties);
                                List<LinkAddress> linkAddresses = linkProperties.getLinkAddresses();
                                LogUtil.i(PharosProxy.TAG, "onLinkPropertiesChanged netId=" + network + ", getLinkAddresses=" + linkAddresses);
                                Network network2 = this.currentNetwork;
                                if (network2 == null || !network2.equals(network)) {
                                    LogUtil.i(PharosProxy.TAG, "onLinkPropertiesChanged currentNetwork =" + this.currentNetwork);
                                } else {
                                    LogUtil.i(PharosProxy.TAG, "onLinkPropertiesChanged \u7f51\u7edc\u8fde\u63a5\u672a\u53d1\u751f\u53d8\u5316");
                                    List<LinkAddress> linkAddresses2 = this.currentLinkProperties.getLinkAddresses();
                                    if (linkAddresses.size() != linkAddresses2.size() || !linkAddresses.containsAll(linkAddresses2)) {
                                        LogUtil.i(PharosProxy.TAG, "onLinkPropertiesChanged \u7f51\u7edc\u8fde\u63a5\u672a\u53d1\u751f\u53d8\u5316\uff0cIP\u5730\u5740\u53d1\u751f\u53d8\u5316");
                                        PharosProxy.getInstance().start();
                                    }
                                }
                                this.currentNetwork = network;
                                this.currentLinkProperties = linkProperties;
                            }
                        });
                        return;
                    }
                    return;
                } catch (Exception e) {
                    LogUtil.w(TAG, "PharosProxy [registerReceiver] = " + e);
                    return;
                }
            }
            return;
        }
        LogUtil.i(TAG, "\u5df2\u7ecf\u6ce8\u518c\u8fc7\u7f51\u7edc\u5e7f\u64ad\u76d1\u542c\u5668\uff0c\u65e0\u9700\u91cd\u590d\u6ce8\u518c");
    }

    /* renamed from: com.netease.pharos.PharosProxy$1 */
    class AnonymousClass1 extends ConnectivityManager.NetworkCallback {
        private LinkProperties currentLinkProperties;
        private Network currentNetwork;

        AnonymousClass1() {
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onAvailable(Network network) {
            super.onAvailable(network);
            LogUtil.i(PharosProxy.TAG, "onAvailable netId=" + network);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
            super.onLinkPropertiesChanged(network, linkProperties);
            List<LinkAddress> linkAddresses = linkProperties.getLinkAddresses();
            LogUtil.i(PharosProxy.TAG, "onLinkPropertiesChanged netId=" + network + ", getLinkAddresses=" + linkAddresses);
            Network network2 = this.currentNetwork;
            if (network2 == null || !network2.equals(network)) {
                LogUtil.i(PharosProxy.TAG, "onLinkPropertiesChanged currentNetwork =" + this.currentNetwork);
            } else {
                LogUtil.i(PharosProxy.TAG, "onLinkPropertiesChanged \u7f51\u7edc\u8fde\u63a5\u672a\u53d1\u751f\u53d8\u5316");
                List<LinkAddress> linkAddresses2 = this.currentLinkProperties.getLinkAddresses();
                if (linkAddresses.size() != linkAddresses2.size() || !linkAddresses.containsAll(linkAddresses2)) {
                    LogUtil.i(PharosProxy.TAG, "onLinkPropertiesChanged \u7f51\u7edc\u8fde\u63a5\u672a\u53d1\u751f\u53d8\u5316\uff0cIP\u5730\u5740\u53d1\u751f\u53d8\u5316");
                    PharosProxy.getInstance().start();
                }
            }
            this.currentNetwork = network;
            this.currentLinkProperties = linkProperties;
        }
    }

    public void unregisterReceiver() {
        ConnectionChangeReceiver connectionChangeReceiver;
        LogUtil.i(TAG, "\u6ce8\u9500\u7f51\u7edc\u5e7f\u64ad\u76d1\u542c\u5668");
        Context context = this.mContext;
        if (context == null || (connectionChangeReceiver = this.mReceiver) == null || !this.mIsRegistered) {
            return;
        }
        context.unregisterReceiver(connectionChangeReceiver);
        this.mIsRegistered = false;
        this.mReceiver = null;
    }

    public void onDestory() {
        clean();
        unregisterReceiver();
    }

    private PharosProxy() {
    }

    public Context getContext() {
        return this.mContext;
    }

    public boolean isDebug() {
        return this.mIsDebug;
    }

    public void setDebug(boolean z) {
        this.mIsDebug = z;
    }

    public void setTestHost(boolean z) {
        this.mHasSetTestHost = z;
    }

    public boolean isHasSetTestHost() {
        return this.mHasSetTestHost;
    }

    public boolean isHasSet() {
        return this.mHasSet;
    }

    @Deprecated
    public void setmHasSet(boolean z) {
        setHasSet(z);
    }

    public void setHasSet(boolean z) {
        this.mHasSet = z;
    }

    public String getmIp() {
        return this.mIp;
    }

    public void setmIp(String str) {
        this.mIp = str;
    }

    public String getmPort() {
        return this.mPort;
    }

    public void setmPort(String str) {
        this.mPort = str;
    }

    public JSONArray getmPorts() {
        return this.mPorts;
    }

    public void setmPorts(JSONArray jSONArray) {
        this.mPorts = jSONArray;
    }

    public String getmHighSpeedUrl() {
        return this.mHighSpeedUrl;
    }

    public void setmHighSpeedUrl(String str) {
        this.mHighSpeedUrl = str;
    }

    public int getmDecision() {
        return this.mDecision;
    }

    public void setmDecision(int i) {
        this.mDecision = i;
    }

    public String ismHarborudp() {
        return this.mHarborudp;
    }

    public void setmHarborudp(String str) {
        this.mHarborudp = str;
    }

    public boolean isIpv6Verify() {
        try {
            return Boolean.parseBoolean(this.mIpv6Verify);
        } catch (Exception unused) {
            return false;
        }
    }

    public void setIpv6Verify(String str) {
        this.mIpv6Verify = str;
    }

    public boolean isCallbackPolicy() {
        try {
            return Boolean.parseBoolean(this.mCallbackPolicy);
        } catch (Exception unused) {
            return false;
        }
    }

    public void setCallbackPolicy(String str) {
        this.mCallbackPolicy = str;
    }

    public String getProjectId() {
        return this.mProjectId;
    }

    public String getUdid() {
        return this.mUdid;
    }

    public String getmNetId() {
        return this.mNetId;
    }

    public void setmOption(int i) {
        this.mOption = i;
    }

    public int getmOption() {
        return this.mOption;
    }

    public String getmQosIp() {
        return this.mQosIp;
    }

    public void setmQosIp(String str) {
        this.mQosIp = str;
    }

    public PharosListener getPharosListener() {
        return this.mPharosListener;
    }

    public boolean ismEB() {
        return this.mEB;
    }

    public void setmEB(boolean z) {
        this.mEB = z;
    }

    public void setNetworkAware(boolean z) {
        NetworkStatus.getInstance().clean();
        this.mNetworkAware = z;
    }

    public boolean getNetworkAware() {
        return this.mNetworkAware;
    }

    public boolean isOversea() {
        int i = this.mArea;
        if (i == 0) {
            return false;
        }
        if (i == 1) {
            return true;
        }
        return ismEB();
    }

    @Deprecated
    public void setmPharosListener(PharosListener pharosListener) {
        setPharosListener(pharosListener);
    }

    public void setPharosListener(PharosListener pharosListener) {
        this.mPharosListener = pharosListener;
    }

    public static PharosProxy getInstance() {
        if (sPharosProxy == null) {
            synchronized (PharosProxy.class) {
                if (sPharosProxy == null) {
                    sPharosProxy = new PharosProxy();
                }
            }
        }
        return sPharosProxy;
    }

    public void init(Context context, String str) {
        Log.i(TAG, "PharosProxy [init] start mIsInit=" + this.mIsInit + ", mHasSet=" + this.mHasSet + ", mIsDebug=" + this.mIsDebug + ", mProjectId=" + this.mProjectId + ", projectId =" + str);
        boolean z = false;
        if (!TextUtils.isEmpty(this.mProjectId) && !TextUtils.equals(str, this.mProjectId)) {
            this.mIsInit = false;
        }
        if (!this.mIsInit) {
            ModulesManager.getInst().init(context);
            this.mContext = context;
            this.mProjectId = str;
            this.mUdid = Util.getUnisdkDeviceId();
            if (!this.mHasSet) {
                this.mIsDebug = Util.isApkDebugable(context);
            }
            StringBuilder sb = new StringBuilder("Pharos useTestMode = ");
            sb.append(this.mIsDebug && this.mHasSetTestHost);
            Log.i(TAG, sb.toString());
            ServerProxy serverProxy = ServerProxy.getInstance();
            if (this.mIsDebug && this.mHasSetTestHost) {
                z = true;
            }
            serverProxy.setTestMode(z);
            Log.i(TAG, "Pharos isDebug = " + this.mIsDebug);
            LogUtil.setIsShowLog(this.mIsDebug);
            this.mNetId = this.mUdid + "-" + System.currentTimeMillis();
            LogFileProxy.getInstance().init(context, 10240);
            this.mIsInit = true;
        }
        Log.i(TAG, "PharosProxy [init] projectId=" + str + ", mIsInit=" + this.mIsInit + ", mHasSet=" + this.mHasSet + ", mIsDebug=" + this.mIsDebug);
    }

    @Deprecated
    public void start() {
        executeAll();
    }

    /* renamed from: com.netease.pharos.PharosProxy$2 */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PharosProxy.this.startAllTask();
        }
    }

    public void executeAll() {
        execute(new Runnable() { // from class: com.netease.pharos.PharosProxy.2
            AnonymousClass2() {
            }

            @Override // java.lang.Runnable
            public void run() {
                PharosProxy.this.startAllTask();
            }
        });
    }

    public void execute(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            new Thread(runnable).start();
        } else {
            runnable.run();
        }
    }

    public void startAllTask() {
        startPolicyTask();
        startHarborTask();
    }

    private void startPolicyTask() {
        int iStart;
        int iStart2;
        JSONObject callBackInfo;
        if (DevicesInfoProxy.getInstances().isHasRun()) {
            iStart = 0;
        } else {
            LogUtil.i(TAG, "\u7f51\u7edc\u76d1\u63a7----\u8bbe\u5907\u63a2\u6d4b");
            DevicesInfoProxy.getInstances().init(this.mContext);
            iStart = DevicesInfoProxy.getInstances().start();
        }
        LogUtil.i(TAG, "\u7f51\u7edc\u76d1\u63a7----\u8bbe\u5907\u63a2\u6d4b\uff0c\u7ed3\u679c=" + DeviceInfo.getInstance().getDeviceInfo(false) + ", result:" + iStart);
        if (LocationCheckProxy.getInstances().isHasRun()) {
            iStart2 = 0;
        } else {
            LogUtil.i(TAG, "\u7f51\u7edc\u76d1\u63a7----\u533a\u57df\u51b3\u7b56");
            iStart2 = LocationCheckProxy.getInstances().start();
        }
        LogUtil.i(TAG, "\u7f51\u7edc\u76d1\u63a7----\u533a\u57df\u51b3\u7b56\uff0c\u7ed3\u679c=" + DeviceInfo.getInstance().getDeviceInfo(false));
        LogUtil.i(TAG, "\u7f51\u7edc\u76d1\u63a7----\u533a\u57df\u51b3\u7b56\uff0c\u8fd4\u56de\u7801=" + iStart2);
        if (iStart == 0 && iStart2 == 0) {
            Proxy.getInstance().setmPharosResultCache(Proxy.getInstance().getPharosResultInfo());
            PharosListener pharosListener = getInstance().isCallbackPolicy() ? getInstance().getPharosListener() : null;
            if (pharosListener != null && (callBackInfo = Proxy.getInstance().getCallBackInfo()) != null) {
                pharosListener.onResult(callBackInfo);
                pharosListener.onPharosPolicy(callBackInfo);
            }
            LogUtil.i(TAG, "\u7f51\u7edc\u76d1\u63a7----\u94fe\u8def\u63a2\u6d4b");
            Proxy.getInstance().start();
            LogUtil.i(TAG, "\u7f51\u7edc\u76d1\u63a7----\u94fe\u8def\u63a2\u6d4b\uff0c\u7ed3\u675f");
        }
    }

    private void startHarborTask() {
        LogUtil.i(TAG, "\u83b7\u53d6\u9ad8\u901f\u5217\u8868");
        HighSpeedListCoreProxy.getInstance().init();
        HighSpeedListCoreProxy.getInstance().start();
    }

    public synchronized void pharosFunc(JSONObject jSONObject) {
        LogUtil.w(TAG, "PharosProxy [pharosFunc] paramJson =" + jSONObject);
        String strOptString = jSONObject.optString("methodId");
        if ("pharosnetlag".equals(strOptString)) {
            execute(new Runnable() { // from class: com.netease.pharos.PharosProxy.3
                final /* synthetic */ JSONObject val$jsonObject;

                AnonymousClass3(JSONObject jSONObject2) {
                    jSONObject = jSONObject2;
                }

                @Override // java.lang.Runnable
                public void run() {
                    PharosProxy.this.pharosNetLag(jSONObject);
                }
            });
        } else if ("pharosnetlagcancel".equals(strOptString)) {
            pharosCancelNetLag();
        } else {
            this.mTasks.offer(new Runnable() { // from class: com.netease.pharos.PharosProxy.4
                final /* synthetic */ JSONObject val$jsonObject;

                AnonymousClass4(JSONObject jSONObject2) {
                    jSONObject = jSONObject2;
                }

                @Override // java.lang.Runnable
                public void run() {
                    try {
                        Runnable runnableWrapTask = PharosProxy.this.wrapTask(jSONObject);
                        if (runnableWrapTask != null) {
                            runnableWrapTask.run();
                        }
                    } finally {
                        PharosProxy.this.scheduleNext();
                    }
                }
            });
            if (this.mActive == null) {
                scheduleNext();
            }
        }
    }

    /* renamed from: com.netease.pharos.PharosProxy$3 */
    class AnonymousClass3 implements Runnable {
        final /* synthetic */ JSONObject val$jsonObject;

        AnonymousClass3(JSONObject jSONObject2) {
            jSONObject = jSONObject2;
        }

        @Override // java.lang.Runnable
        public void run() {
            PharosProxy.this.pharosNetLag(jSONObject);
        }
    }

    /* renamed from: com.netease.pharos.PharosProxy$4 */
    class AnonymousClass4 implements Runnable {
        final /* synthetic */ JSONObject val$jsonObject;

        AnonymousClass4(JSONObject jSONObject2) {
            jSONObject = jSONObject2;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                Runnable runnableWrapTask = PharosProxy.this.wrapTask(jSONObject);
                if (runnableWrapTask != null) {
                    runnableWrapTask.run();
                }
            } finally {
                PharosProxy.this.scheduleNext();
            }
        }
    }

    protected synchronized void scheduleNext() {
        Runnable runnablePoll = this.mTasks.poll();
        this.mActive = runnablePoll;
        if (runnablePoll != null) {
            if ((System.currentTimeMillis() - this.mRunTime) / 1000 > ((long) Const.CACHE_EXPIRE.get())) {
                clean();
                this.mRunTime = System.currentTimeMillis();
            }
            THREAD_POOL_EXECUTOR.execute(this.mActive);
        }
    }

    public Runnable wrapTask(JSONObject jSONObject) throws JSONException {
        if (jSONObject != null) {
            LogUtil.w(TAG, "PharosProxy [pharosFunc] paramJson =" + jSONObject);
            String strOptString = jSONObject.optString("methodId");
            if (!TextUtils.isEmpty(strOptString)) {
                parseConfig(jSONObject);
                if ("pharosprobe".equals(strOptString) || "pharos_probe".equals(strOptString)) {
                    return new Runnable() { // from class: com.netease.pharos.PharosProxy.5
                        AnonymousClass5() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PharosProxy.this.executeAll();
                        }
                    };
                }
                if ("pharospolicy".equals(strOptString) || "pharos_policy".equals(strOptString)) {
                    return new Runnable() { // from class: com.netease.pharos.PharosProxy.6
                        AnonymousClass6() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PharosProxy.this.pharospolicy();
                        }
                    };
                }
                if ("pharosharbor".equals(strOptString) || "pharos_harbor".equals(strOptString)) {
                    return new Runnable() { // from class: com.netease.pharos.PharosProxy.7
                        AnonymousClass7() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PharosProxy.this.pharosharbor();
                        }
                    };
                }
                if ("pharosqosprobe".equals(strOptString) || "pharos_qos_probe".equals(strOptString)) {
                    return new Runnable() { // from class: com.netease.pharos.PharosProxy.8
                        AnonymousClass8() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PharosProxy.this.pharosqosprobe();
                        }
                    };
                }
                if ("pharosqosstatus".equals(strOptString) || "pharos_qos_status".equals(strOptString)) {
                    return new Runnable() { // from class: com.netease.pharos.PharosProxy.9
                        final /* synthetic */ JSONObject val$jsonObject;

                        AnonymousClass9(JSONObject jSONObject2) {
                            jSONObject = jSONObject2;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PharosProxy.this.mQosIps = jSONObject.optJSONArray("qos_ips");
                            PharosProxy pharosProxy = PharosProxy.this;
                            pharosProxy.pharosqosstatus(pharosProxy.mQosIps);
                        }
                    };
                }
                if ("pharosqosexec".equals(strOptString) || "pharos_qos_exec".equals(strOptString)) {
                    return new Runnable() { // from class: com.netease.pharos.PharosProxy.10
                        final /* synthetic */ JSONObject val$jsonObject;

                        AnonymousClass10(JSONObject jSONObject2) {
                            jSONObject = jSONObject2;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PharosProxy.this.mQosIps = jSONObject.optJSONArray("qos_ips");
                            long jOptLong = jSONObject.optLong("duration", 0L);
                            PharosProxy pharosProxy = PharosProxy.this;
                            pharosProxy.pharosqosexec(pharosProxy.mQosIps, jOptLong);
                        }
                    };
                }
                if ("pharosqoscancel".equals(strOptString) || "pharos_qos_cancel".equals(strOptString)) {
                    return new Runnable() { // from class: com.netease.pharos.PharosProxy.11
                        final /* synthetic */ JSONObject val$jsonObject;

                        AnonymousClass11(JSONObject jSONObject2) {
                            jSONObject = jSONObject2;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            PharosProxy.this.mQosIps = jSONObject.optJSONArray("qos_ips");
                            PharosProxy pharosProxy = PharosProxy.this;
                            pharosProxy.pharosqoscancel(pharosProxy.mQosIps);
                        }
                    };
                }
                if ("pharos_login_info_upload".equals(strOptString)) {
                    return new Runnable() { // from class: com.netease.pharos.PharosProxy.12
                        final /* synthetic */ JSONObject val$jsonObject;

                        AnonymousClass12(JSONObject jSONObject2) {
                            jSONObject = jSONObject2;
                        }

                        @Override // java.lang.Runnable
                        public void run() throws JSONException {
                            PharosProxy.this.pharosLoginInfoUpload(jSONObject);
                        }
                    };
                }
                return null;
            }
        }
        return null;
    }

    /* renamed from: com.netease.pharos.PharosProxy$5 */
    class AnonymousClass5 implements Runnable {
        AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PharosProxy.this.executeAll();
        }
    }

    /* renamed from: com.netease.pharos.PharosProxy$6 */
    class AnonymousClass6 implements Runnable {
        AnonymousClass6() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PharosProxy.this.pharospolicy();
        }
    }

    /* renamed from: com.netease.pharos.PharosProxy$7 */
    class AnonymousClass7 implements Runnable {
        AnonymousClass7() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PharosProxy.this.pharosharbor();
        }
    }

    /* renamed from: com.netease.pharos.PharosProxy$8 */
    class AnonymousClass8 implements Runnable {
        AnonymousClass8() {
        }

        @Override // java.lang.Runnable
        public void run() {
            PharosProxy.this.pharosqosprobe();
        }
    }

    /* renamed from: com.netease.pharos.PharosProxy$9 */
    class AnonymousClass9 implements Runnable {
        final /* synthetic */ JSONObject val$jsonObject;

        AnonymousClass9(JSONObject jSONObject2) {
            jSONObject = jSONObject2;
        }

        @Override // java.lang.Runnable
        public void run() {
            PharosProxy.this.mQosIps = jSONObject.optJSONArray("qos_ips");
            PharosProxy pharosProxy = PharosProxy.this;
            pharosProxy.pharosqosstatus(pharosProxy.mQosIps);
        }
    }

    /* renamed from: com.netease.pharos.PharosProxy$10 */
    class AnonymousClass10 implements Runnable {
        final /* synthetic */ JSONObject val$jsonObject;

        AnonymousClass10(JSONObject jSONObject2) {
            jSONObject = jSONObject2;
        }

        @Override // java.lang.Runnable
        public void run() {
            PharosProxy.this.mQosIps = jSONObject.optJSONArray("qos_ips");
            long jOptLong = jSONObject.optLong("duration", 0L);
            PharosProxy pharosProxy = PharosProxy.this;
            pharosProxy.pharosqosexec(pharosProxy.mQosIps, jOptLong);
        }
    }

    /* renamed from: com.netease.pharos.PharosProxy$11 */
    class AnonymousClass11 implements Runnable {
        final /* synthetic */ JSONObject val$jsonObject;

        AnonymousClass11(JSONObject jSONObject2) {
            jSONObject = jSONObject2;
        }

        @Override // java.lang.Runnable
        public void run() {
            PharosProxy.this.mQosIps = jSONObject.optJSONArray("qos_ips");
            PharosProxy pharosProxy = PharosProxy.this;
            pharosProxy.pharosqoscancel(pharosProxy.mQosIps);
        }
    }

    /* renamed from: com.netease.pharos.PharosProxy$12 */
    class AnonymousClass12 implements Runnable {
        final /* synthetic */ JSONObject val$jsonObject;

        AnonymousClass12(JSONObject jSONObject2) {
            jSONObject = jSONObject2;
        }

        @Override // java.lang.Runnable
        public void run() throws JSONException {
            PharosProxy.this.pharosLoginInfoUpload(jSONObject);
        }
    }

    private void parseConfig(JSONObject jSONObject) throws JSONException {
        int iOptInt;
        int iOptInt2;
        String strOptString;
        String str;
        JSONArray jSONArrayOptJSONArray;
        String strOptString2;
        boolean z = true;
        if (jSONObject.has("options")) {
            iOptInt = jSONObject.optInt("options");
            setmOption(iOptInt);
        } else {
            iOptInt = 1;
        }
        if (jSONObject.has("decision")) {
            iOptInt2 = jSONObject.optInt("decision");
            setmDecision(iOptInt2);
        } else {
            iOptInt2 = 0;
        }
        String strOptString3 = null;
        if (jSONObject.has("ip")) {
            strOptString = jSONObject.optString("ip");
            setmIp(strOptString);
        } else {
            strOptString = null;
        }
        if (jSONObject.has(b.F)) {
            try {
                str = "" + jSONObject.getLong(b.F);
            } catch (JSONException e) {
                e.printStackTrace();
                str = null;
            }
            setmPort(str);
        } else {
            str = null;
        }
        if (jSONObject.has("ports")) {
            jSONArrayOptJSONArray = jSONObject.optJSONArray("ports");
            if (jSONArrayOptJSONArray != null) {
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    try {
                        jSONArrayOptJSONArray.getLong(0);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        z = false;
                    }
                }
                setmPorts(z ? jSONArrayOptJSONArray : null);
            }
        } else {
            jSONArrayOptJSONArray = null;
        }
        if (jSONObject.has("url")) {
            strOptString2 = jSONObject.optString("url");
            setmHighSpeedUrl(strOptString2);
        } else {
            strOptString2 = null;
        }
        if (jSONObject.has("qos_ip")) {
            strOptString3 = jSONObject.optString("qos_ip");
            setmQosIp(strOptString3);
        }
        this.mTestLog = jSONObject.optInt("testlog", 0);
        if (jSONObject.has("harborudp")) {
            String strOptString4 = jSONObject.optString("harborudp");
            this.mHarborudp = strOptString4;
            setmHarborudp(strOptString4);
        }
        this.mLagID = jSONObject.optString("lag_id", "");
        if (jSONObject.has("logopen")) {
            String strOptString5 = jSONObject.optString("logopen");
            LogUtil.i(TAG, "PharosProxy [pharosFunc] param logOpen =" + strOptString5);
            if (!TextUtils.isEmpty(strOptString5)) {
                LogUtil.setIsShowLog("true".equals(strOptString5));
            }
        }
        this.mIpv6Verify = jSONObject.optString("ipv6_verify", Constants.CASEFIRST_FALSE);
        if (jSONObject.has("callback_policy")) {
            setCallbackPolicy(jSONObject.optString("callback_policy", Constants.CASEFIRST_FALSE));
        }
        this.mArea = jSONObject.optInt("area", -1);
        try {
            Const.CACHE_EXPIRE.set(jSONObject.optInt("cache_expire", 1800));
            LogUtil.i(TAG, "cache_expire:" + Const.CACHE_EXPIRE.get());
        } catch (Exception unused) {
            Const.CACHE_EXPIRE.set(1800);
        }
        startRegisterReceiver(jSONObject);
        LogUtil.i(TAG, "PharosProxy [pharosFunc] ip =" + strOptString + ", port=" + str + ", ports=" + jSONArrayOptJSONArray + ", url=" + strOptString2 + ", qosIp=" + strOptString3 + ", options=" + iOptInt + ", decision=" + iOptInt2 + ", area=" + this.mArea);
    }

    /* renamed from: com.netease.pharos.PharosProxy$13 */
    class AnonymousClass13 implements Runnable {
        final /* synthetic */ JSONObject val$paramJson;

        AnonymousClass13(JSONObject jSONObject) {
            jSONObject = jSONObject;
        }

        @Override // java.lang.Runnable
        public void run() {
            boolean z;
            try {
                z = Boolean.parseBoolean(jSONObject.optString("network_aware", Constants.CASEFIRST_FALSE));
            } catch (Exception unused) {
                z = false;
            }
            PharosProxy.this.setNetworkAware(z);
            if (z) {
                NetworkStatus.getInstance().initialize();
                PharosProxy pharosProxy = PharosProxy.this;
                pharosProxy.registerReceiver(pharosProxy.mContext);
                return;
            }
            PharosProxy.this.unregisterReceiver();
        }
    }

    private void startRegisterReceiver(JSONObject jSONObject) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.netease.pharos.PharosProxy.13
            final /* synthetic */ JSONObject val$paramJson;

            AnonymousClass13(JSONObject jSONObject2) {
                jSONObject = jSONObject2;
            }

            @Override // java.lang.Runnable
            public void run() {
                boolean z;
                try {
                    z = Boolean.parseBoolean(jSONObject.optString("network_aware", Constants.CASEFIRST_FALSE));
                } catch (Exception unused) {
                    z = false;
                }
                PharosProxy.this.setNetworkAware(z);
                if (z) {
                    NetworkStatus.getInstance().initialize();
                    PharosProxy pharosProxy = PharosProxy.this;
                    pharosProxy.registerReceiver(pharosProxy.mContext);
                    return;
                }
                PharosProxy.this.unregisterReceiver();
            }
        }, 4000L);
    }

    public void pharospolicy() {
        if (this.mPharosListener == null || !getInstance().isCallbackPolicy()) {
            LogUtil.i(TAG, "PharosProxy [pharosharbor] mPharosListener is null ");
            return;
        }
        JSONObject pharosResultInfo = Proxy.getInstance().getPharosResultInfo();
        LogUtil.i(TAG, "PharosProxy [pharosharbor] call onPharosPolicy ");
        this.mPharosListener.onPharosPolicy(pharosResultInfo);
    }

    public void pharosharbor() {
        HighSpeedListCoreProxy.getInstance().init();
        HighSpeedListCoreProxy.getInstance().start();
    }

    public void pharosqosprobe() {
        Proxy.getInstance().start();
    }

    public void pharosqosexec(JSONArray jSONArray, long j) {
        LogUtil.i(TAG, "PharosProxy [pharosqosexec]");
        if (jSONArray == null || jSONArray.length() < 0 || j <= 0) {
            LogUtil.i(TAG, "PharosProxy [pharosqosexec] \u53c2\u6570\u9519\u8bef");
            return;
        }
        LogUtil.i(TAG, "PharosProxy [pharosqosexec] ips=" + jSONArray + ", duratio=" + j);
        Qos4GProxy.getInstance().pharosqosexec(jSONArray, j * 1000);
    }

    public void pharosqoscancel(JSONArray jSONArray) {
        if (jSONArray == null || jSONArray.length() <= 0) {
            LogUtil.i(TAG, "PharosProxy [pharosqoscancel] \u53c2\u6570\u9519\u8bef");
        } else {
            Qos4GProxy.getInstance().cancel(jSONArray);
        }
    }

    public void pharosqosstatus(JSONArray jSONArray) {
        LogUtil.i(TAG, "PharosProxy [pharosqosstatus]");
        if (jSONArray == null || jSONArray.length() < 0) {
            LogUtil.i(TAG, "PharosProxy [pharosqosstatus] \u53c2\u6570\u9519\u8bef");
            return;
        }
        LogUtil.i(TAG, "PharosProxy [pharosqosstatus] ips=" + jSONArray);
        if (this.mPharosListener == null) {
            LogUtil.i(TAG, "PharosProxy [pharosqosstatus] mPharosListener is null");
            return;
        }
        JSONObject result = Qos4GProxy.getInstance().getResult(jSONArray);
        this.mPharosListener.onResult(result);
        this.mPharosListener.onPharosQos(result);
    }

    public void pharosqosstatus() {
        if (this.mPharosListener == null) {
            LogUtil.i(TAG, "PharosProxy [pharosqosstatus] mPharosListener is null");
            return;
        }
        JSONObject result = Qos4GProxy.getInstance().getResult();
        this.mPharosListener.onResult(result);
        this.mPharosListener.onPharosQos(result);
    }

    public void pharosLoginInfoUpload(JSONObject jSONObject) throws JSONException {
        LogUtil.i(TAG, "PharosProxy [pharosLoginInfoUpload]");
        if (jSONObject == null || jSONObject.length() <= 0) {
            LogUtil.i(TAG, "PharosProxy [pharosLoginInfoUpload] params error");
        }
        try {
            String deviceInfo = DeviceInfo.getInstance().getDeviceInfo(true);
            if (TextUtils.isEmpty(deviceInfo)) {
                LogUtil.i(TAG, "PharosProxy [pharosLoginInfoUpload] deviceInfo1 error");
            }
            JSONObject jSONObject2 = new JSONObject(deviceInfo);
            LogUtil.i(TAG, "PharosProxy [pharosLoginInfoUpload] deviceInfoJson=" + jSONObject2);
            String strOptString = jSONObject2.has("project") ? jSONObject2.optString("project") : "";
            String strOptString2 = jSONObject2.has("os_name") ? jSONObject2.optString("os_name") : "";
            String strOptString3 = jSONObject2.has("udid") ? jSONObject2.optString("udid") : "";
            String strOptString4 = jSONObject2.has("netid") ? jSONObject2.optString("netid") : "";
            String strOptString5 = jSONObject2.has("method") ? jSONObject2.optString("method") : "";
            String strOptString6 = jSONObject2.has("version") ? jSONObject2.optString("version") : "";
            jSONObject.remove("methodId");
            jSONObject.put("project", strOptString);
            jSONObject.put("os_name", strOptString2);
            jSONObject.put("udid", strOptString3);
            jSONObject.put("netid", strOptString4);
            jSONObject.put("method", strOptString5);
            jSONObject.put("version", strOptString6);
            jSONObject.put("type", "login");
            ReportProxy.getInstance().report(jSONObject.toString());
        } catch (Exception e) {
            LogUtil.i(TAG, "PharosProxy [pharosLoginInfoUpload] Exception=" + e);
        }
    }

    public synchronized void clean() {
        DevicesInfoProxy.getInstances().clean();
        HighSpeedListCoreProxy.getInstance().clean();
        LocationCheckProxy.getInstances().clean();
        ProtocolCheckProxy.getInstance().clean();
        Proxy.getInstance().clean();
        this.mRunTime = 0L;
    }

    public void pharosNetLag(JSONObject jSONObject) {
        NetworkCheckProxy.getInstance().start(jSONObject, this.mOnNetLagCallback);
    }

    public void pharosCancelNetLag() {
        NetworkCheckProxy.getInstance().cancel(this.mOnNetLagCallback);
    }
}