package com.netease.download.downloader;

import android.content.Context;
import android.content.IntentFilter;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.hermes.intl.Constants;
import com.netease.download.Const;
import com.netease.download.check.CheckTime;
import com.netease.download.config.ConfigProxy;
import com.netease.download.dns.CdnIpController;
import com.netease.download.downloadpart.DownloadAllProxy;
import com.netease.download.handler.Dispatcher;
import com.netease.download.httpdns.HttpdnsProxy;
import com.netease.download.list.PatchListProxy;
import com.netease.download.listener.DownloadListener;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.lvsip.Lvsip;
import com.netease.download.network.ConnectionChangeReceiver;
import com.netease.download.network.NetController;
import com.netease.download.network.NetworkStatus;
import com.netease.download.ohter.DownloadOhterProxy;
import com.netease.download.reporter.ReportProxy;
import com.netease.download.reporter.ReportUtil;
import com.netease.download.storage.StorageUtil;
import com.netease.download.taskManager.TaskExecutor;
import com.netease.download.util.HashUtil;
import com.netease.download.util.LogUtil;
import com.netease.download.util.Util;
import com.netease.download.woffset.WoffsetProxy;
import com.xiaomi.onetrack.c.c;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class DownloadProxy {
    private static final String TAG = "DownloadProxy";
    public static Context mContext;
    private static ConnectionChangeReceiver mReceiver;
    private static DownloadProxy sDownloadProxy;
    public static boolean sOnceStop;
    private DownloadListener mListener = null;
    private List<DownloadParams> mParamsList = null;

    private DownloadProxy() {
    }

    public static DownloadProxy getInstance() {
        if (sDownloadProxy == null) {
            sDownloadProxy = new DownloadProxy();
        }
        return sDownloadProxy;
    }

    public static void registerReceiver(Context context) {
        LogUtil.i(TAG, "\u6ce8\u518c\u7f51\u7edc\u5e7f\u64ad\u76d1\u542c\u5668");
        if (mReceiver == null) {
            try {
                ConnectionChangeReceiver connectionChangeReceiver = new ConnectionChangeReceiver();
                mReceiver = connectionChangeReceiver;
                context.registerReceiver(connectionChangeReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            } catch (Exception e) {
                LogUtil.w(TAG, "\u6ce8\u518c\u7f51\u7edc\u5e7f\u64ad\u76d1\u542c\u5668 Exception=" + e);
            }
        }
    }

    public static void unregisterReceiver() {
        ConnectionChangeReceiver connectionChangeReceiver;
        LogUtil.i(TAG, "\u6ce8\u9500\u7f51\u7edc\u5e7f\u64ad\u76d1\u542c\u5668");
        Context context = mContext;
        if (context == null || (connectionChangeReceiver = mReceiver) == null) {
            return;
        }
        try {
            context.unregisterReceiver(connectionChangeReceiver);
            mReceiver = null;
        } catch (Exception e) {
            LogUtil.w(TAG, "\u6ce8\u9500\u7f51\u7edc\u5e7f\u64ad\u76d1\u542c\u5668 Exception=" + e);
        }
    }

    public void init() {
        NetController.getInstances().restore();
        NetworkStatus.initialize(mContext);
        registerReceiver(mContext);
        DownloadInitInfo.getInstances().setContext(mContext);
    }

    public void downloadFunc(Context context, JSONObject jSONObject, DownloadListener downloadListener) throws JSONException {
        JSONArray jSONArrayOptJSONArray;
        Log.i("Downloader", "DownloadProxy [downloadFunc] downloadFunc");
        if (context == null) {
            Log.w("Downloader", "DownloadProxy [downloadFunc] pContext is null");
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(14, 0L, 0L, "__DOWNLOAD_PARAM_ERROR__", "__DOWNLOAD_PARAM_ERROR__", "".getBytes(), "__DOWNLOAD_PARAM_ERROR__", "");
            TaskHandleOp.getInstance().getTaskHandle().setStatus(14);
            return;
        }
        if (downloadListener == null) {
            Log.w("Downloader", "DownloadProxy [downloadFunc] pListener is null");
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(14, 0L, 0L, "__DOWNLOAD_PARAM_ERROR__", "__DOWNLOAD_PARAM_ERROR__", "".getBytes(), "__DOWNLOAD_PARAM_ERROR__", "");
            TaskHandleOp.getInstance().getTaskHandle().setStatus(14);
            return;
        }
        if (jSONObject == null) {
            Log.w("Downloader", "DownloadProxy [downloadFunc] paramsJson is null");
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(14, 0L, 0L, "__DOWNLOAD_PARAM_ERROR__", "__DOWNLOAD_PARAM_ERROR__", "".getBytes(), "__DOWNLOAD_PARAM_ERROR__", "");
            TaskHandleOp.getInstance().getTaskHandle().setStatus(14);
            return;
        }
        if (mContext == null) {
            mContext = context;
        }
        Util.getUniqueData(mContext);
        if (this.mListener == null) {
            this.mListener = downloadListener;
        }
        TaskHandle.sWriteToLogFile = LogUtil.containLogFile();
        if (jSONObject.has("methodId")) {
            try {
                String string = jSONObject.getString("methodId");
                LogUtil.i(TAG, "DownloadProxy [downloadFunc] methodId =" + string);
                if ("downloadqueueclear".equals(string)) {
                    LogUtil.i(TAG, "DownloadProxy [downloadFunc] TaskManager clear");
                    DownloadParamsQueueManager.getInstance().pause();
                    List<JSONObject> paramsList = DownloadParamsQueueManager.getInstance().getParamsList();
                    if (paramsList != null && paramsList.size() > 0) {
                        for (JSONObject jSONObject2 : paramsList) {
                            if (jSONObject2.has("downloadid")) {
                                String strOptString = jSONObject2.optString("downloadid");
                                DownloadListenerProxy.getInstances();
                                DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsgWithOtherSatus(0, 0L, 0L, "__DOWNLOAD_QUEUE_CLEAR__", "__DOWNLOAD_QUEUE_CLEAR__", "".getBytes(), strOptString, strOptString, "");
                            }
                        }
                    }
                    DownloadParamsQueueManager.getInstance().clear();
                    return;
                }
                if ("downloadcancel".equals(string)) {
                    LogUtil.i(TAG, "DownloadProxy [downloadFunc] downloadcancel");
                    if (TaskHandleOp.getInstance().getTaskHandle() != null) {
                        TaskHandleOp.getInstance().getTaskHandle().setStatus(2);
                    }
                    stopAll();
                    return;
                }
                if ("downloadversion".equals(string)) {
                    DownloadListenerProxy.getInstances();
                    DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsgWithOtherSatus(0, 0L, 0L, Const.VERSION, "", "".getBytes(), "0", "", "");
                    return;
                }
                if ("cleancache".equals(string)) {
                    LogUtil.i(TAG, "DownloadProxy [downloadFunc] cleancache");
                    if (!jSONObject.has("downloadid") || (jSONArrayOptJSONArray = jSONObject.optJSONArray("downloadid")) == null || jSONArrayOptJSONArray.length() <= 0) {
                        return;
                    }
                    for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                        String strOptString2 = jSONArrayOptJSONArray.optString(i);
                        if (!TextUtils.isEmpty(strOptString2)) {
                            clearDownloadId(mContext, strOptString2);
                        }
                    }
                    return;
                }
                DownloadListenerProxy.getInstances().init(this.mListener);
                LogUtil.i(TAG, "DownloadProxy [downloadFunc] put");
                DownloadParamsQueueManager.getInstance().put(jSONObject);
            } catch (Exception e) {
                LogUtil.w(TAG, "DownloadProxy [downloadFunc] Exception =" + e.toString());
                e.printStackTrace();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0047 A[Catch: Exception -> 0x004d, TRY_LEAVE, TryCatch #0 {Exception -> 0x004d, blocks: (B:25:0x000d, B:27:0x0013, B:29:0x002c, B:31:0x0034, B:32:0x0041, B:33:0x0047), top: B:39:0x000d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void dispachMethod(org.json.JSONObject r5) throws org.json.JSONException {
        /*
            r4 = this;
            java.lang.String r0 = "methodId"
            java.lang.String r1 = "DownloadProxy [downloadFunc] methodId22 ="
            java.lang.String r2 = "DownloadProxy [dispachMethod] start"
            java.lang.String r3 = "DownloadProxy"
            com.netease.download.util.LogUtil.i(r3, r2)
            if (r5 == 0) goto L47
            boolean r2 = r5.has(r0)     // Catch: java.lang.Exception -> L4d
            if (r2 == 0) goto L47
            java.lang.String r0 = r5.getString(r0)     // Catch: java.lang.Exception -> L4d
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L4d
            r2.<init>(r1)     // Catch: java.lang.Exception -> L4d
            r2.append(r0)     // Catch: java.lang.Exception -> L4d
            java.lang.String r1 = r2.toString()     // Catch: java.lang.Exception -> L4d
            com.netease.download.util.LogUtil.i(r3, r1)     // Catch: java.lang.Exception -> L4d
            boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Exception -> L4d
            if (r1 != 0) goto L41
            java.lang.String r1 = "download"
            boolean r0 = r1.equals(r0)     // Catch: java.lang.Exception -> L4d
            if (r0 == 0) goto L62
            java.lang.String r0 = "DownloadProxy [downloadFunc] download"
            com.netease.download.util.LogUtil.i(r3, r0)     // Catch: java.lang.Exception -> L4d
            android.content.Context r0 = com.netease.download.downloader.DownloadProxy.mContext     // Catch: java.lang.Exception -> L4d
            com.netease.download.listener.DownloadListener r1 = r4.mListener     // Catch: java.lang.Exception -> L4d
            r4.asyncDownloadArray(r0, r5, r1)     // Catch: java.lang.Exception -> L4d
            goto L62
        L41:
            java.lang.String r5 = "DownloadProxy [downloadFunc] methodId is error"
            com.netease.download.util.LogUtil.w(r3, r5)     // Catch: java.lang.Exception -> L4d
            goto L62
        L47:
            java.lang.String r5 = "DownloadProxy [downloadFunc] params has not methodId"
            com.netease.download.util.LogUtil.w(r3, r5)     // Catch: java.lang.Exception -> L4d
            goto L62
        L4d:
            r5 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "DownloadProxy [downloadFunc] Exception ="
            r0.<init>(r1)
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            com.netease.download.util.LogUtil.w(r3, r0)
            r5.printStackTrace()
        L62:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.downloader.DownloadProxy.dispachMethod(org.json.JSONObject):void");
    }

    public void asyncDownloadArray(Context context, JSONObject jSONObject, DownloadListener downloadListener) {
        LogUtil.i(TAG, "DownloadParams [createParamsArray] start");
        if (jSONObject == null) {
            LogUtil.w(TAG, "DownloadProxy [asyncDownloadArray] paramsJson is null");
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_START__", "__DOWNLOAD_START__", "".getBytes(), "", "");
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsgWithOtherSatus(14, 0L, 0L, "__DOWNLOAD_PARAM_ERROR__", "__DOWNLOAD_PARAM_ERROR__", "".getBytes(), "0", "__DOWNLOAD_PARAM_ERROR__", "");
            TaskHandleOp.getInstance().getTaskHandle().setStatus(14);
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_END__", "__DOWNLOAD_END__", "".getBytes(), "", "");
            return;
        }
        String strOptString = jSONObject.has("downloadid") ? jSONObject.optString("downloadid") : "";
        DownloadListenerProxy.getInstances();
        DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_START__", "__DOWNLOAD_START__", "".getBytes(), strOptString, "");
        if (context == null) {
            LogUtil.w(TAG, "DownloadProxy [asyncDownloadArray] pContext is null");
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsgWithOtherSatus(14, 0L, 0L, "__DOWNLOAD_PARAM_ERROR__", "__DOWNLOAD_PARAM_ERROR__", "".getBytes(), "0", "__DOWNLOAD_PARAM_ERROR__", "");
            TaskHandleOp.getInstance().getTaskHandle().setStatus(14);
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_END__", "__DOWNLOAD_END__", "".getBytes(), strOptString, "");
            return;
        }
        if (downloadListener == null) {
            LogUtil.w(TAG, "DownloadProxy [asyncDownloadArray] pListener is null");
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsgWithOtherSatus(14, 0L, 0L, "__DOWNLOAD_PARAM_ERROR__", "__DOWNLOAD_PARAM_ERROR__", "".getBytes(), "0", "__DOWNLOAD_PARAM_ERROR__", "");
            TaskHandleOp.getInstance().getTaskHandle().setStatus(14);
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_END__", "__DOWNLOAD_END__", "".getBytes(), strOptString, "");
            return;
        }
        if (!NetworkStatus.isConnected(context)) {
            DownloadListenerProxy.getInstances();
            String str = strOptString;
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(12, 0L, 0L, "__DOWNLOAD_NETWORK_LOST__", "__DOWNLOAD_NETWORK_LOST__", "".getBytes(), str, "");
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_END__", "__DOWNLOAD_END__", "".getBytes(), str, "");
            LogUtil.w(TAG, "DownloadProxy [asyncDownloadArray] no network connected");
            return;
        }
        new Thread(new Runnable() { // from class: com.netease.download.downloader.DownloadProxy.1
            final /* synthetic */ Context val$pContext;
            final /* synthetic */ DownloadListener val$pListener;
            final /* synthetic */ JSONObject val$paramsJson;

            AnonymousClass1(JSONObject jSONObject2, Context context2, DownloadListener downloadListener2) {
                jSONObject = jSONObject2;
                context = context2;
                downloadListener = downloadListener2;
            }

            @Override // java.lang.Runnable
            public void run() {
                TaskHandleOp.getInstance().reset();
                TaskHandleOp.getInstance().start();
                TaskHandleOp.getInstance().getTaskHandle().setStatus(-1);
                TaskHandleOp.getInstance().getTaskHandle().setTimeToStartTask(System.currentTimeMillis());
                DownloadProxy.this.reset();
                DownloadProxy.this.createBaseInfo();
                TaskHandleOp.getInstance().getTaskHandle().setTimeToStartParseParams(System.currentTimeMillis());
                DownloadProxy downloadProxy = DownloadProxy.this;
                downloadProxy.mParamsList = downloadProxy.parseParam(jSONObject);
                TaskHandleOp.getInstance().getTaskHandle().setTimeToFinishParseParams(System.currentTimeMillis());
                LogUtil.i(DownloadProxy.TAG, "DownloadParams [createParamsArray] \u4e0b\u8f7d\u524d\u671f\uff0c\u53d1\u9001\u65e5\u5fd7\uff08\u4e0a\u4e00\u6b21\u9057\u7559\u6587\u4ef6\uff09");
                ReportProxy.getInstance().report(context, 1);
                DownloadProxy.initReportInfo(context);
                ReportProxy.getInstance().init(context);
                if (DownloadProxy.mContext == null) {
                    DownloadProxy.mContext = context;
                }
                if (DownloadProxy.this.mListener == null) {
                    DownloadProxy.this.mListener = downloadListener;
                }
                DownloadProxy.this.init();
                if (DownloadProxy.this.mParamsList == null || DownloadProxy.this.mParamsList.size() <= 0) {
                    LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] mParamsList params error");
                    ReportProxy.getInstance().setOpen(false);
                    String strOptString2 = jSONObject.has("downloadid") ? jSONObject.optString("downloadid") : "";
                    int status = TaskHandleOp.getInstance().getTaskHandle().getStatus();
                    if (9 == status) {
                        DownloadListenerProxy.getInstances();
                        DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(9, 0L, 0L, "", "", "".getBytes(), strOptString2, TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                    } else if (5 == status) {
                        DownloadListenerProxy.getInstances();
                        DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(5, 0L, 0L, "__DOWNLOAD_STORAGE_ERROR__", "__DOWNLOAD_STORAGE_ERROR__", "".getBytes(), "", TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId(), TaskHandleOp.getInstance().getTaskHandle().getFreeSpace());
                    } else {
                        TaskHandleOp.getInstance().getTaskHandle().setStatus(14);
                        DownloadListenerProxy.getInstances();
                        DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(14, 0L, 0L, "__DOWNLOAD_PARAM_ERROR__", "__DOWNLOAD_PARAM_ERROR__", "".getBytes(), strOptString2, TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                    }
                    DownloadListenerProxy.getInstances();
                    DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_END__", "__DOWNLOAD_END__", "".getBytes(), strOptString2, TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                    return;
                }
                LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] TaskHandleOp.getInstance().getTaskHandle().getmType()=" + TaskHandleOp.getInstance().getTaskHandle().getType());
                LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] TaskHandleOp.getInstance().getTaskHandle().getNotUseCdn()=" + TaskHandleOp.getInstance().getTaskHandle().getNotUseCdn());
                if (Constants.CASEFIRST_FALSE.equals(TaskHandleOp.getInstance().getTaskHandle().getNotUseCdn())) {
                    LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] 111");
                } else {
                    LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] 222");
                }
                if (Const.TYPE_TARGET_NORMAL.equals(TaskHandleOp.getInstance().getTaskHandle().getType())) {
                    LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] \u5217\u8868\u6587\u4ef6\u4e0b\u8f7d");
                    DownloadParams downloadParams = (DownloadParams) DownloadProxy.this.mParamsList.get(0);
                    if (downloadParams != null) {
                        PatchListProxy.getInstances().init(DownloadProxy.mContext, downloadParams);
                        PatchListProxy.getInstances().start();
                        return;
                    }
                    return;
                }
                if (!Const.TYPE_TARGET_PATCH.equals(TaskHandleOp.getInstance().getTaskHandle().getType()) || !Constants.CASEFIRST_FALSE.equals(TaskHandleOp.getInstance().getTaskHandle().getNotUseCdn())) {
                    DownloadOhterProxy.getInstances().init(DownloadProxy.this.mParamsList);
                    DownloadOhterProxy.getInstances().start();
                    return;
                }
                LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] patch\u6587\u4ef6\u4e0b\u8f7d");
                DownloadPreHandler.getInstatnces().init(DownloadProxy.mContext, DownloadInitInfo.getInstances().getProjectId());
                int iStart = DownloadPreHandler.getInstatnces().start();
                LogUtil.i(DownloadProxy.TAG, "\u9884\u5904\u7406\u7ed3\u679c=" + iStart);
                if (iStart == 0) {
                    LogUtil.i(DownloadProxy.TAG, "\u5f00\u542f\u4e00\u4e2apatch\u7cfb\u5217\u4e0b\u8f7d");
                    Dispatcher.getInstance().startSyn(DownloadProxy.mContext, DownloadProxy.this.mParamsList);
                } else {
                    LogUtil.i(DownloadProxy.TAG, "\u9884\u5904\u7406\u4e0d\u6210\u529f\uff0c\u76f4\u63a5\u4e0a\u4f20\u65e5\u5fd7\u3002");
                    ReportProxy.getInstance().close(1L);
                }
            }
        }).start();
    }

    /* renamed from: com.netease.download.downloader.DownloadProxy$1 */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ Context val$pContext;
        final /* synthetic */ DownloadListener val$pListener;
        final /* synthetic */ JSONObject val$paramsJson;

        AnonymousClass1(JSONObject jSONObject2, Context context2, DownloadListener downloadListener2) {
            jSONObject = jSONObject2;
            context = context2;
            downloadListener = downloadListener2;
        }

        @Override // java.lang.Runnable
        public void run() {
            TaskHandleOp.getInstance().reset();
            TaskHandleOp.getInstance().start();
            TaskHandleOp.getInstance().getTaskHandle().setStatus(-1);
            TaskHandleOp.getInstance().getTaskHandle().setTimeToStartTask(System.currentTimeMillis());
            DownloadProxy.this.reset();
            DownloadProxy.this.createBaseInfo();
            TaskHandleOp.getInstance().getTaskHandle().setTimeToStartParseParams(System.currentTimeMillis());
            DownloadProxy downloadProxy = DownloadProxy.this;
            downloadProxy.mParamsList = downloadProxy.parseParam(jSONObject);
            TaskHandleOp.getInstance().getTaskHandle().setTimeToFinishParseParams(System.currentTimeMillis());
            LogUtil.i(DownloadProxy.TAG, "DownloadParams [createParamsArray] \u4e0b\u8f7d\u524d\u671f\uff0c\u53d1\u9001\u65e5\u5fd7\uff08\u4e0a\u4e00\u6b21\u9057\u7559\u6587\u4ef6\uff09");
            ReportProxy.getInstance().report(context, 1);
            DownloadProxy.initReportInfo(context);
            ReportProxy.getInstance().init(context);
            if (DownloadProxy.mContext == null) {
                DownloadProxy.mContext = context;
            }
            if (DownloadProxy.this.mListener == null) {
                DownloadProxy.this.mListener = downloadListener;
            }
            DownloadProxy.this.init();
            if (DownloadProxy.this.mParamsList == null || DownloadProxy.this.mParamsList.size() <= 0) {
                LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] mParamsList params error");
                ReportProxy.getInstance().setOpen(false);
                String strOptString2 = jSONObject.has("downloadid") ? jSONObject.optString("downloadid") : "";
                int status = TaskHandleOp.getInstance().getTaskHandle().getStatus();
                if (9 == status) {
                    DownloadListenerProxy.getInstances();
                    DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(9, 0L, 0L, "", "", "".getBytes(), strOptString2, TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                } else if (5 == status) {
                    DownloadListenerProxy.getInstances();
                    DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(5, 0L, 0L, "__DOWNLOAD_STORAGE_ERROR__", "__DOWNLOAD_STORAGE_ERROR__", "".getBytes(), "", TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId(), TaskHandleOp.getInstance().getTaskHandle().getFreeSpace());
                } else {
                    TaskHandleOp.getInstance().getTaskHandle().setStatus(14);
                    DownloadListenerProxy.getInstances();
                    DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(14, 0L, 0L, "__DOWNLOAD_PARAM_ERROR__", "__DOWNLOAD_PARAM_ERROR__", "".getBytes(), strOptString2, TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                }
                DownloadListenerProxy.getInstances();
                DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_END__", "__DOWNLOAD_END__", "".getBytes(), strOptString2, TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                return;
            }
            LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] TaskHandleOp.getInstance().getTaskHandle().getmType()=" + TaskHandleOp.getInstance().getTaskHandle().getType());
            LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] TaskHandleOp.getInstance().getTaskHandle().getNotUseCdn()=" + TaskHandleOp.getInstance().getTaskHandle().getNotUseCdn());
            if (Constants.CASEFIRST_FALSE.equals(TaskHandleOp.getInstance().getTaskHandle().getNotUseCdn())) {
                LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] 111");
            } else {
                LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] 222");
            }
            if (Const.TYPE_TARGET_NORMAL.equals(TaskHandleOp.getInstance().getTaskHandle().getType())) {
                LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] \u5217\u8868\u6587\u4ef6\u4e0b\u8f7d");
                DownloadParams downloadParams = (DownloadParams) DownloadProxy.this.mParamsList.get(0);
                if (downloadParams != null) {
                    PatchListProxy.getInstances().init(DownloadProxy.mContext, downloadParams);
                    PatchListProxy.getInstances().start();
                    return;
                }
                return;
            }
            if (!Const.TYPE_TARGET_PATCH.equals(TaskHandleOp.getInstance().getTaskHandle().getType()) || !Constants.CASEFIRST_FALSE.equals(TaskHandleOp.getInstance().getTaskHandle().getNotUseCdn())) {
                DownloadOhterProxy.getInstances().init(DownloadProxy.this.mParamsList);
                DownloadOhterProxy.getInstances().start();
                return;
            }
            LogUtil.i(DownloadProxy.TAG, "DownloadProxy [asyncDownloadArray] patch\u6587\u4ef6\u4e0b\u8f7d");
            DownloadPreHandler.getInstatnces().init(DownloadProxy.mContext, DownloadInitInfo.getInstances().getProjectId());
            int iStart = DownloadPreHandler.getInstatnces().start();
            LogUtil.i(DownloadProxy.TAG, "\u9884\u5904\u7406\u7ed3\u679c=" + iStart);
            if (iStart == 0) {
                LogUtil.i(DownloadProxy.TAG, "\u5f00\u542f\u4e00\u4e2apatch\u7cfb\u5217\u4e0b\u8f7d");
                Dispatcher.getInstance().startSyn(DownloadProxy.mContext, DownloadProxy.this.mParamsList);
            } else {
                LogUtil.i(DownloadProxy.TAG, "\u9884\u5904\u7406\u4e0d\u6210\u529f\uff0c\u76f4\u63a5\u4e0a\u4f20\u65e5\u5fd7\u3002");
                ReportProxy.getInstance().close(1L);
            }
        }
    }

    public void createBaseInfo() {
        LogUtil.i(TAG, "DownloadProxy [createBaseInfo] start");
        TaskHandleOp.getInstance().getTaskHandle().setSessionid(Util.createSessionId());
        String strCalculateStrHash = HashUtil.calculateStrHash(DownloadInitInfo.getInstances().getmTransid() + "-" + TaskHandleOp.getInstance().getTaskHandle().getSessionid());
        TaskHandleOp.getInstance().getTaskHandle().setNtesOrbitId(strCalculateStrHash);
        StringBuilder sb = new StringBuilder("DownloadProxy [createBaseInfo] X-Ntes-Orbit-ID=");
        sb.append(strCalculateStrHash);
        LogUtil.i(TAG, sb.toString());
        LogUtil.i(TAG, "DownloadProxy [createBaseInfo] end");
    }

    public static void clearDownloadId(Context context, String str) {
        LogUtil.i(TAG, "clearDownloadId downloadId=" + str);
        if (context == null) {
            LogUtil.i(TAG, "context is null");
        } else if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "clearDownloadId param error");
        } else {
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsgWithOtherSatus(0, 0L, 0L, "__DOWNLOAD_CLEAN_CACHE__", "__DOWNLOAD_CLEAN_CACHE__", "".getBytes(), "", TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
        }
    }

    public static synchronized void stopAll() {
        LogUtil.d(TAG, "DownloadProxy [stopAll] start");
        sOnceStop = true;
        NetController.getInstances().setInterruptedCode(12);
    }

    public static synchronized void freeAllThreadPool() {
        LogUtil.d(TAG, "DownloadProxy [freeAllThreadPool] start");
        DownloadAllProxy.getInstances().stop();
        TaskExecutor.getInstance().closeFixedThreadPool();
        Dispatcher.getInstance().close();
    }

    public static String getDownloadId() {
        LogUtil.d(TAG, "getDownloadId");
        return Util.getRandomId();
    }

    public static String getCurrentSessionId() {
        return TaskHandleOp.getInstance().getTaskHandle().getSessionid();
    }

    public static void initReportInfo(Context context) {
        LogUtil.i(TAG, "DownloadProxy [initReportInfo] start");
        ReportUtil.getInstances().init(mContext);
        DownloadInitInfo.getInstances().setmUnisdkVersion(ReportUtil.getInstances().getUnisdkVer());
        DownloadInitInfo.getInstances().setmUnisdkChannelVersion(ReportUtil.getInstances().getChanelVer());
        LogUtil.i(TAG, "DownloadProxy [init] \u4e0b\u8f7d\u524d\u671f\uff0c\u53d1\u9001\u65e5\u5fd7");
        ReportProxy.getInstance().reportInfo(mContext, 1, 0);
    }

    public void reset() {
        DownloadListenerProxy.getInstances().clear();
        ReportProxy.getInstance().clean();
        HttpdnsProxy.getInstances().clean();
        CdnIpController.getInstances().clean();
        CheckTime.clean();
        Lvsip.getInstance().clean();
        WoffsetProxy.getInstances().reset();
    }

    public int hashCode() {
        return super.hashCode();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v17 */
    /* JADX WARN: Type inference failed for: r13v18, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r13v19 */
    /* JADX WARN: Type inference failed for: r13v21 */
    /* JADX WARN: Type inference failed for: r13v22, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r13v23, types: [java.util.ArrayList] */
    public ArrayList<DownloadParams> parseParam(JSONObject jSONObject) throws NumberFormatException {
        ArrayList<DownloadParams> arrayList;
        String str;
        String str2;
        String str3;
        int i;
        int iOptInt;
        boolean zOptBoolean;
        boolean z;
        String str4;
        boolean zOptBoolean2;
        String str5;
        long j;
        long j2;
        long j3;
        long j4;
        long j5;
        String str6;
        String str7;
        String strOptString;
        String str8;
        String str9;
        String str10;
        boolean z2;
        String str11;
        String str12;
        long j6;
        String str13;
        long j7;
        String str14;
        long j8;
        long j9;
        String str15;
        String str16;
        String str17;
        long j10;
        long j11;
        String str18;
        String str19;
        String str20;
        ?? r13;
        String str21;
        long j12;
        long j13;
        DownloadParams downloadParams;
        long j14;
        String str22;
        LogUtil.w(TAG, "DownloadProxy [parseParam]");
        LogUtil.w(TAG, "DownloadProxy [parseParam] \u89e3\u6790\u53c2\u6570");
        ArrayList<DownloadParams> arrayList2 = new ArrayList<>();
        if (jSONObject == null) {
            LogUtil.w(TAG, "DownloadProxy [parseParam] paramsJson is null");
            return arrayList2;
        }
        try {
            String strOptString2 = jSONObject.optString("type");
            String strOptString3 = jSONObject.optString("projectid");
            DownloadInitInfo.getInstances().setProjectId(strOptString3);
            String str23 = "true";
            boolean zEquals = "true".equals(jSONObject.optString("wifionly"));
            LogUtil.i(TAG, "DownloadProxy [parseParam] wifiOnly=" + zEquals);
            TaskHandleOp.getInstance().getTaskHandle().setWifiOnly(zEquals);
            boolean zEquals2 = "true".equals(jSONObject.optString("logopen"));
            TaskHandleOp.getInstance().getTaskHandle().setLogOpen(zEquals2);
            LogUtil.setIsShowLog(zEquals2);
            String strOptString4 = jSONObject.optString("oversea");
            TaskHandleOp.getInstance().getTaskHandle().setOverSea(strOptString4);
            if (!"-1".equals(strOptString4) && !"0".equals(strOptString4) && ((!"1".equals(strOptString4)) & (!"2".equals(strOptString4)))) {
                LogUtil.w(TAG, "DownloadProxy [parseParam] oversea error");
                return arrayList2;
            }
            String strOptString5 = jSONObject.optString("needrefresh");
            ConfigProxy.getInstances().setNeedRefresh(strOptString5);
            if (jSONObject.has("notusecdn")) {
                String strOptString6 = jSONObject.optString("notusecdn");
                arrayList = arrayList2;
                TaskHandleOp.getInstance().getTaskHandle().setNotUseCdn(strOptString6);
                str = strOptString6;
            } else {
                arrayList = arrayList2;
                str = Constants.CASEFIRST_FALSE;
            }
            try {
                str2 = str;
                str3 = strOptString4;
                i = Integer.parseInt(jSONObject.optString("threadnum"));
            } catch (Exception e) {
                str2 = str;
                str3 = strOptString4;
                LogUtil.w(TAG, "DownloadProxy [parseParam] get threadnum Exception=" + e);
                i = 3;
            }
            TaskHandleOp.getInstance().getTaskHandle().setThreadnum(i);
            try {
                iOptInt = jSONObject.optInt(c.a.g);
            } catch (Exception e2) {
                LogUtil.w(TAG, "DownloadProxy [parseParam] get priority Exception=" + e2);
                e2.printStackTrace();
                iOptInt = -1;
            }
            int i2 = iOptInt;
            TaskHandleOp.getInstance().getTaskHandle().setPriority(i2);
            try {
                zOptBoolean = jSONObject.optBoolean("priority_task");
            } catch (Exception e3) {
                LogUtil.w(TAG, "DownloadProxy [parseParam] get priority Exception=" + e3);
                e3.printStackTrace();
                zOptBoolean = false;
            }
            TaskHandleOp.getInstance().getTaskHandle().setPriorityTask(zOptBoolean);
            String strOptString7 = jSONObject.optString("testlog");
            if (!TextUtils.isEmpty(strOptString7)) {
                if ("1".equals(strOptString7)) {
                    TaskHandleOp.getInstance().getTaskHandle().setLogTest(1);
                } else {
                    TaskHandleOp.getInstance().getTaskHandle().setLogTest(0);
                }
            }
            String strOptString8 = jSONObject.optString("isrenew");
            if (TextUtils.isEmpty(strOptString8)) {
                z = zOptBoolean;
            } else if ("true".equals(strOptString8)) {
                z = zOptBoolean;
                TaskHandleOp.getInstance().getTaskHandle().setIsRenew(true);
            } else {
                z = zOptBoolean;
                TaskHandleOp.getInstance().getTaskHandle().setIsRenew(false);
            }
            String strOptString9 = jSONObject.optString("chf");
            LogUtil.i(TAG, "DownloadProxy [parseParam] chf=" + strOptString9);
            if (!TextUtils.isEmpty(strOptString9)) {
                if (Const.KEY_MD5.equals(strOptString9)) {
                    TaskHandleOp.getInstance().getTaskHandle().setEncryptionAlgorithm("MD5");
                } else if ("sha1".contains(strOptString9)) {
                    TaskHandleOp.getInstance().getTaskHandle().setEncryptionAlgorithm("SHA1");
                }
            }
            String strOptString10 = jSONObject.optString("checkfs");
            String str24 = Const.KEY_MD5;
            LogUtil.i(TAG, "DownloadProxy [parseParam] checkfs=" + strOptString10);
            if (!TextUtils.isEmpty(strOptString10) && Constants.CASEFIRST_FALSE.equals(strOptString10)) {
                TaskHandleOp.getInstance().getTaskHandle().setCheckfs(false);
            }
            String strOptString11 = jSONObject.optString("buffercount");
            String str25 = "DownloadProxy [parseParam] Exception=";
            if (TextUtils.isEmpty(strOptString11)) {
                str4 = strOptString11;
            } else {
                try {
                    int i3 = Integer.parseInt(strOptString11);
                    str4 = strOptString11;
                    if (i3 <= 0 || i3 > 10) {
                        i3 = 3;
                    }
                    TaskHandleOp.getInstance().getTaskHandle().setBufferCount(i3);
                    LogUtil.i(TAG, "DownloadProxy [parseParam] buffercount=" + i3);
                } catch (Exception e4) {
                    LogUtil.w(TAG, "DownloadProxy [parseParam] Exception=" + e4.toString());
                    e4.printStackTrace();
                    return new ArrayList<>();
                }
            }
            String strOptString12 = jSONObject.optString("netlimit");
            if (!TextUtils.isEmpty(strOptString12)) {
                try {
                    float f = Float.parseFloat(strOptString12);
                    if (f < 1024.0f || f > 1.048576E8f) {
                        f = 0.0f;
                    }
                    TaskHandleOp.getInstance().getTaskHandle().setNetAllSpeedLimit(f);
                    LogUtil.i(TAG, "DownloadProxy [parseParam] netlimitFloat=" + f);
                } catch (Exception e5) {
                    LogUtil.w(TAG, "DownloadProxy [parseParam] Exception=" + e5.toString());
                    e5.printStackTrace();
                    return new ArrayList<>();
                }
            }
            try {
                zOptBoolean2 = jSONObject.optBoolean("rammode");
            } catch (Exception e6) {
                LogUtil.w(TAG, "DownloadProxy [parseParam] get rammode Exception=" + e6);
                e6.printStackTrace();
                zOptBoolean2 = false;
            }
            TaskHandleOp.getInstance().getTaskHandle().setRammode(zOptBoolean2);
            String strOptString13 = jSONObject.optString("ramlimit");
            if (TextUtils.isEmpty(strOptString13) || !TextUtils.isDigitsOnly(strOptString13)) {
                str5 = TAG;
                j = 33554432;
            } else {
                try {
                    long j15 = Long.parseLong(strOptString13);
                    if (j15 < 0 || j15 > 1073741824) {
                        str22 = TAG;
                        j = 33554432;
                    } else {
                        str22 = TAG;
                        j = j15;
                    }
                    try {
                        TaskHandleOp.getInstance().getTaskHandle().setRamlimit(j);
                        str5 = str22;
                        try {
                            LogUtil.i(str5, "DownloadProxy [parseParam] netlimitLong=" + j);
                        } catch (Exception e7) {
                            e = e7;
                            LogUtil.w(str5, "DownloadProxy [parseParam] Exception=" + e.toString());
                            e.printStackTrace();
                            return new ArrayList<>();
                        }
                    } catch (Exception e8) {
                        e = e8;
                        str5 = str22;
                    }
                } catch (Exception e9) {
                    e = e9;
                    str5 = TAG;
                }
            }
            String str26 = Constants.CASEFIRST_FALSE;
            LogUtil.i(str5, "DownloadProxy [parseParam] rammode=" + zOptBoolean2 + ", ramlimit=" + strOptString13 + ", ramlimitLong=" + j);
            String strOptString14 = jSONObject.optString("mergemax");
            if (TextUtils.isEmpty(strOptString14) || !Util.isNumeric(strOptString14)) {
                j2 = j;
                j3 = PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED;
            } else {
                j2 = j;
                try {
                    j3 = Long.parseLong(strOptString14);
                    LogUtil.i(str5, "DownloadProxy [parseParam] mergeMaxInt=" + j3);
                } catch (Exception e10) {
                    LogUtil.w(str5, "DownloadProxy [parseParam] Exception=" + e10.toString());
                    e10.printStackTrace();
                    return new ArrayList<>();
                }
            }
            TaskHandleOp.getInstance().getTaskHandle().setMergeMax(j3);
            String strOptString15 = jSONObject.optString("mergespace");
            if (TextUtils.isEmpty(strOptString15) || !Util.isNumeric(strOptString15)) {
                j4 = j3;
                j5 = 0;
            } else {
                j4 = j3;
                try {
                    j5 = Long.parseLong(strOptString15);
                    LogUtil.i(str5, "DownloadProxy [parseParam] mergeSpaceInt=" + j5);
                } catch (Exception e11) {
                    LogUtil.w(str5, "DownloadProxy [parseParam] Exception=" + e11.toString());
                    e11.printStackTrace();
                    return new ArrayList<>();
                }
            }
            TaskHandleOp.getInstance().getTaskHandle().setMergeSpace(j5);
            String strOptString16 = jSONObject.optString("autofree");
            if (TextUtils.isEmpty(strOptString16)) {
                str6 = strOptString16;
            } else if ("true".equals(strOptString16)) {
                str6 = strOptString16;
                TaskHandleOp.getInstance().getTaskHandle().setAutoFree(true);
            } else {
                str6 = strOptString16;
                TaskHandleOp.getInstance().getTaskHandle().setAutoFree(false);
            }
            boolean z3 = zOptBoolean2;
            String str27 = "DownloadProxy [parseParam] mMergeMode=";
            if (jSONObject.has("mergemode")) {
                strOptString = jSONObject.optString("mergemode");
                str7 = ", ramlimitLong=";
                LogUtil.i(str5, "DownloadProxy [parseParam] mMergeMode=" + strOptString);
                TaskHandleOp.getInstance().getTaskHandle().setDownloadMode("mergemode");
            } else {
                str7 = ", ramlimitLong=";
                strOptString = str26;
            }
            TaskHandleOp.getInstance().getTaskHandle().setMergeMode("true".equals(strOptString));
            String strOptString17 = jSONObject.has("configurl") ? jSONObject.optString("configurl") : null;
            TaskHandleOp.getInstance().getTaskHandle().setConfigurl(strOptString17);
            String strOptString18 = jSONObject.has("downloadid") ? jSONObject.optString("downloadid") : null;
            String str28 = strOptString17;
            TaskHandleOp.getInstance().getTaskHandle().setDownloadId(strOptString18);
            String strOptString19 = jSONObject.optString("callback_interval");
            if (TextUtils.isEmpty(strOptString19) || !Util.isNumeric(strOptString19)) {
                str8 = strOptString19;
                str9 = strOptString14;
                str10 = strOptString18;
            } else {
                try {
                    long j16 = Long.parseLong(strOptString19);
                    if (j16 < 0) {
                        str9 = strOptString14;
                        str10 = strOptString18;
                        j14 = 0;
                    } else {
                        str9 = strOptString14;
                        str10 = strOptString18;
                        j14 = j16;
                    }
                    str8 = strOptString19;
                    TaskHandleOp.getInstance().getTaskHandle().setCallBackInterval(j14);
                    LogUtil.i(str5, "DownloadProxy [parseParam] callBackIntervalLong=" + TaskHandleOp.getInstance().getTaskHandle().getCallBackInterval());
                } catch (Exception e12) {
                    LogUtil.w(str5, "DownloadProxy [parseParam] Exception=" + e12.toString());
                    e12.printStackTrace();
                    return new ArrayList<>();
                }
            }
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("downfile");
            if (jSONArrayOptJSONArray == null || jSONArrayOptJSONArray.length() <= 0) {
                ArrayList<DownloadParams> arrayList3 = arrayList;
                LogUtil.i(str5, "DownloadProxy [parseParam] fileArray is error");
                return arrayList3;
            }
            int length = jSONArrayOptJSONArray.length();
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.setmTotalFileCount(length);
            TaskHandleOp.getInstance().getTaskHandle().setTaskFileCount(length);
            LogUtil.i(str5, "DownloadProxy [parseParam] \u9700\u8981\u4e0b\u8f7d\u7684\u6587\u4ef6\u603b\u4e2a\u6570 =" + length);
            if (jSONArrayOptJSONArray.length() < 100) {
                LogUtil.i(str5, "DownloadProxy [parseParam] \u53c2\u6570\u6587\u4ef6\u6570\u91cf\u5c0f\u4e8e100\u4e2a\uff0c\u663e\u793a\u53c2\u6570\u4fe1\u606f");
                LogUtil.i(str5, "DownloadProxy [parseParam] \u53c2\u6570\u6587\u4ef6\u6570\u91cf\u5c0f\u4e8e100\u4e2a\uff0c\u663e\u793a\u53c2\u6570\u4fe1\u606f paramsJson=" + jSONObject.toString());
                z2 = zEquals;
            } else {
                LogUtil.i(str5, "DownloadProxy [parseParam] \u53c2\u6570\u6587\u4ef6\u6570\u91cf\u5927\u4e8e100\u4e2a\uff0c\u53ea\u663e\u793a\u7b2c\u4e00\u4e2a\u53c2\u6570");
                JSONObject jSONObjectOptJSONObject = jSONArrayOptJSONArray.optJSONObject(0);
                StringBuilder sb = new StringBuilder("DownloadProxy [parseParam] \u53c2\u6570\u6587\u4ef6\u6570\u91cf\u5927\u4e8e100\u4e2a\uff0c\u53ea\u663e\u793a\u7b2c\u4e00\u4e2a\u53c2\u6570  projectId=");
                sb.append(strOptString3);
                sb.append(", wifiOnly=");
                z2 = zEquals;
                sb.append(z2);
                sb.append(", logOpen=");
                sb.append(zEquals2);
                sb.append(", oversea=");
                sb.append(str3);
                sb.append(", threadnum=");
                sb.append(i);
                sb.append(", testLog=");
                sb.append(strOptString7);
                sb.append(", isRenew=");
                sb.append(strOptString8);
                sb.append(", downfile[0]=");
                sb.append(jSONObjectOptJSONObject);
                LogUtil.i(str5, sb.toString());
            }
            boolean z4 = z2;
            long j17 = j5;
            int i4 = i;
            String type = strOptString2;
            long j18 = 0;
            DownloadParams downloadParams2 = null;
            DownloadParams downloadParams3 = null;
            long j19 = -100;
            long j20 = -100;
            int i5 = 0;
            long j21 = 0;
            while (i5 < jSONArrayOptJSONArray.length()) {
                DownloadParams downloadParams4 = new DownloadParams();
                String str29 = strOptString15;
                JSONObject jSONObjectOptJSONObject2 = jSONArrayOptJSONArray.optJSONObject(i5);
                if (jSONObjectOptJSONObject2 == null) {
                    return new ArrayList<>();
                }
                JSONArray jSONArray = jSONArrayOptJSONArray;
                String strOptString20 = jSONObjectOptJSONObject2.optString("targeturl");
                downloadParams4.setTargetUrl(strOptString20);
                int i6 = i5;
                if (!strOptString20.startsWith("http://") && !strOptString20.startsWith("https://")) {
                    return new ArrayList<>();
                }
                String str30 = str23;
                LogUtil.i(str5, "DownloadProxy [parseParam] type=" + type);
                if (TextUtils.isEmpty(type)) {
                    type = Util.parseType(strOptString20);
                }
                TaskHandleOp.getInstance().getTaskHandle().setType(type);
                LogUtil.i(str5, "DownloadProxy [parseParam] \u6700\u7ec8type=" + TaskHandleOp.getInstance().getTaskHandle().getType());
                if (TextUtils.isEmpty(null)) {
                    TaskHandleOp.getInstance().getTaskHandle().setChannel1(Util.parseChannel(strOptString20));
                }
                downloadParams4.setmChannel(Util.getCdnChannel(downloadParams4.getTargetUrl()));
                if (jSONObjectOptJSONObject2.has("filepath")) {
                    String strOptString21 = jSONObjectOptJSONObject2.optString("filepath");
                    if (TextUtils.isEmpty(strOptString21)) {
                        return new ArrayList<>();
                    }
                    if (jSONObjectOptJSONObject2.has("filename")) {
                        downloadParams4.setUserFileName(jSONObjectOptJSONObject2.optString("filename"));
                    }
                    downloadParams4.setFilePath(strOptString21);
                    downloadParams4.setmUrlResName(Util.getSuffixFromUrl(strOptString20));
                    downloadParams4.setOriginUrl(Util.getPrefixFromUrl(strOptString20));
                    downloadParams4.setUrlPrefix(Util.getPrefixFromUrl(strOptString20));
                    downloadParams4.setmWriteToExistFile(false);
                    String str31 = str24;
                    downloadParams4.setMd5(jSONObjectOptJSONObject2.optString(str31));
                    if (jSONObjectOptJSONObject2.has("size")) {
                        LogUtil.i(str5, "DownloadProxy [parseParam] \u53c2\u6570\u4e2d\u5305\u542bsize\u5b57\u6bb5\uff0c\u89e3\u6790size");
                        try {
                            String strOptString22 = jSONObjectOptJSONObject2.optString("size");
                            if (!TextUtils.isEmpty(strOptString22) && Util.isNumeric(strOptString22)) {
                                j6 = Long.parseLong(strOptString22);
                                if (j6 < 0) {
                                    return new ArrayList<>();
                                }
                            }
                            return new ArrayList<>();
                        } catch (Exception e13) {
                            LogUtil.i(str5, str25 + e13);
                            e13.printStackTrace();
                            return new ArrayList<>();
                        }
                    }
                    j6 = 0;
                    if (jSONObjectOptJSONObject2.has("first") && jSONObjectOptJSONObject2.has("last")) {
                        LogUtil.i(str5, "DownloadProxy [parseParam] \u53c2\u6570\u4e2d\u5305\u542bfirst\u3001last\u5b57\u6bb5\uff0c\u89e3\u6790first\u3001last");
                        try {
                            String strOptString23 = jSONObjectOptJSONObject2.optString("first");
                            str14 = str31;
                            String strOptString24 = jSONObjectOptJSONObject2.optString("last");
                            if (!TextUtils.isEmpty(strOptString23) && !TextUtils.isEmpty(strOptString24) && Util.isNumeric(strOptString23) && Util.isNumeric(strOptString24)) {
                                j7 = j21;
                                long j22 = Long.parseLong(strOptString23);
                                str13 = strOptString21;
                                long j23 = Long.parseLong(strOptString24);
                                if (j22 >= 0 && j23 > 0 && j23 >= j22) {
                                    downloadParams4.setStart(j22);
                                    downloadParams4.setmPriSegmentStart(j22);
                                    downloadParams4.setLast(j23);
                                    j6 = j23 - j22;
                                }
                                LogUtil.i(str5, "DownloadProxy [parseParam] \u53c2\u6570\u9519\u8bef\uff0cfirst last \u6570\u503c\u4e0d\u6b63\u786e");
                                return new ArrayList<>();
                            }
                            LogUtil.i(str5, "DownloadProxy [parseParam] \u53c2\u6570\u9519\u8bef\uff0cfirst last \u4e3a\u7a7a\uff0c\u6216\u8005 \u4e0d\u662f\u6570\u5b57\u5f62\u5f0f\uff0c\u76f4\u63a5\u8fd4\u56de");
                            return new ArrayList<>();
                        } catch (Exception e14) {
                            LogUtil.i(str5, str25 + e14);
                            e14.printStackTrace();
                            return new ArrayList<>();
                        }
                    }
                    str13 = strOptString21;
                    j7 = j21;
                    str14 = str31;
                    long j24 = j6;
                    downloadParams4.setSize(j24);
                    downloadParams4.setRealFileSize(j24);
                    if (-100 == j20 || j24 < j20) {
                        j20 = j24;
                    }
                    if (-100 == j19 || j24 > j19) {
                        j19 = j24;
                    }
                    if (jSONObjectOptJSONObject2.has("woffset") && type.equals(Const.TYPE_TARGET_PATCH)) {
                        str15 = str26;
                        if (str15.equals(strOptString)) {
                            try {
                                String strOptString25 = jSONObjectOptJSONObject2.optString("woffset");
                                if (!TextUtils.isEmpty(strOptString25) && Util.isNumeric(strOptString25)) {
                                    j8 = j19;
                                    long j25 = Long.parseLong(strOptString25);
                                    if (j25 < 0) {
                                        return new ArrayList<>();
                                    }
                                    if (j25 >= 0) {
                                        downloadParams4.setmWoffset(j25);
                                        StringBuilder sb2 = new StringBuilder();
                                        String str32 = str13;
                                        sb2.append(str32);
                                        j9 = j20;
                                        sb2.append("_");
                                        sb2.append(j25);
                                        downloadParams4.setFilePath(sb2.toString());
                                        downloadParams4.setmOffsetTempFileName(str32);
                                        downloadParams4.setmWriteToExistFile(true);
                                    } else {
                                        j9 = j20;
                                    }
                                    TaskHandleOp.getInstance().getTaskHandle().setDownloadMode("woffset");
                                }
                                return new ArrayList<>();
                            } catch (Exception e15) {
                                LogUtil.i(str5, "DownloadProxy [parseParam] woffset Exception=" + e15);
                                return new ArrayList<>();
                            }
                        }
                        j8 = j19;
                        j9 = j20;
                    } else {
                        j8 = j19;
                        j9 = j20;
                        str15 = str26;
                    }
                    downloadParams4.setFileId(downloadParams4.hashCode() + "");
                    StringBuilder sb3 = new StringBuilder(str27);
                    sb3.append(strOptString);
                    LogUtil.i(str5, sb3.toString());
                    long j26 = j7 + j24;
                    if (str30.equals(strOptString) && Const.TYPE_TARGET_PATCH.equals(type)) {
                        String str33 = str2;
                        if (str15.equals(str33)) {
                            if (arrayList.size() <= 0) {
                                downloadParams4.addElement(downloadParams4.getFilePath(), downloadParams4.getStart(), downloadParams4.getLast(), downloadParams4.getMd5());
                                r13 = arrayList;
                                r13.add(downloadParams4);
                                LogUtil.i(str5, "DownloadProxy [parseParam] preParams11=" + downloadParams4.toString());
                                str16 = str27;
                                str17 = type;
                                j10 = j24;
                                j11 = j26;
                                downloadParams = downloadParams4;
                                str21 = str33;
                                str18 = str15;
                                str19 = strOptString;
                                str20 = str25;
                                j12 = j4;
                                j13 = j17;
                            } else {
                                r13 = arrayList;
                                String targetUrl = downloadParams2.getTargetUrl();
                                long size = downloadParams2.getSize();
                                str16 = str27;
                                str17 = type;
                                long last = downloadParams2.getLast();
                                j10 = j24;
                                long start = downloadParams4.getStart();
                                j11 = j26;
                                long j27 = start - last;
                                str21 = str33;
                                long last2 = downloadParams4.getLast() - downloadParams2.getStart();
                                str18 = str15;
                                str20 = str25;
                                LogUtil.i(str5, "DownloadProxy [parseParam] curStart=" + start + ", preLast=" + last + ", curParams.getLast()=" + downloadParams4.getLast() + ", preParams.getStart()=" + downloadParams2.getStart());
                                StringBuilder sb4 = new StringBuilder("DownloadProxy [parseParam] interval=");
                                sb4.append(j27);
                                sb4.append(", mergeSpaceInt=");
                                j13 = j17;
                                sb4.append(j13);
                                sb4.append(", mergeMaxInt=");
                                str19 = strOptString;
                                j12 = j4;
                                sb4.append(j12);
                                sb4.append(", t_size=");
                                sb4.append(last2);
                                LogUtil.i(str5, sb4.toString());
                                if (!TextUtils.isEmpty(strOptString20) && !TextUtils.isEmpty(targetUrl) && strOptString20.equals(targetUrl) && last2 >= 0 && j27 >= 0 && j27 <= j13 && last2 <= j12) {
                                    j18 -= size;
                                    downloadParams = downloadParams2;
                                    downloadParams.setSize(last2);
                                    downloadParams.setLast(downloadParams4.getLast());
                                    downloadParams.setRealFileSize(downloadParams.getRealFileSize() + downloadParams4.getRealFileSize());
                                    downloadParams.addElement(downloadParams4.getFilePath(), downloadParams4.getStart(), downloadParams4.getLast(), downloadParams4.getMd5());
                                    r13.remove(r13.size() - 1);
                                    r13.add(downloadParams);
                                    j10 = last2;
                                } else {
                                    downloadParams4.addElement(downloadParams4.getFilePath(), downloadParams4.getStart(), downloadParams4.getLast(), downloadParams4.getMd5());
                                    r13.add(downloadParams4);
                                    downloadParams = downloadParams4;
                                }
                            }
                            j18 += j10;
                            downloadParams2 = downloadParams;
                            j17 = j13;
                            arrayList = r13;
                            j4 = j12;
                            str27 = str16;
                            str24 = str14;
                            jSONArrayOptJSONArray = jSONArray;
                            type = str17;
                            j20 = j9;
                            j21 = j11;
                            str2 = str21;
                            str26 = str18;
                            str25 = str20;
                            strOptString = str19;
                            i5 = i6 + 1;
                            str23 = str30;
                            downloadParams3 = downloadParams4;
                            strOptString15 = str29;
                            j19 = j8;
                        } else {
                            str16 = str27;
                            str17 = type;
                            j10 = j24;
                            j11 = j26;
                            str21 = str33;
                            str18 = str15;
                            str19 = strOptString;
                            str20 = str25;
                            r13 = arrayList;
                        }
                    } else {
                        str16 = str27;
                        str17 = type;
                        j10 = j24;
                        j11 = j26;
                        str18 = str15;
                        str19 = strOptString;
                        str20 = str25;
                        r13 = arrayList;
                        str21 = str2;
                    }
                    j12 = j4;
                    j13 = j17;
                    downloadParams = downloadParams2;
                    r13.add(downloadParams4);
                    j18 += j10;
                    downloadParams2 = downloadParams;
                    j17 = j13;
                    arrayList = r13;
                    j4 = j12;
                    str27 = str16;
                    str24 = str14;
                    jSONArrayOptJSONArray = jSONArray;
                    type = str17;
                    j20 = j9;
                    j21 = j11;
                    str2 = str21;
                    str26 = str18;
                    str25 = str20;
                    strOptString = str19;
                    i5 = i6 + 1;
                    str23 = str30;
                    downloadParams3 = downloadParams4;
                    strOptString15 = str29;
                    j19 = j8;
                } else {
                    return new ArrayList<>();
                }
            }
            JSONArray jSONArray2 = jSONArrayOptJSONArray;
            String str34 = strOptString15;
            long j28 = j21;
            String str35 = str23;
            String str36 = strOptString;
            ArrayList<DownloadParams> arrayList4 = arrayList;
            String str37 = str2;
            long j29 = j4;
            long j30 = j17;
            StringBuilder sb5 = new StringBuilder("DownloadProxy [parseParam] curAllSize=");
            long j31 = j18;
            sb5.append(j31);
            sb5.append(", preAllSize=");
            long j32 = j20;
            sb5.append(j28);
            LogUtil.i(str5, sb5.toString());
            if (j30 >= 0 && j29 > 0 && 0 != j28 && str35.equals(str36)) {
                StringBuilder sb6 = new StringBuilder("[ORBIT] Merge mergespace=");
                str12 = str34;
                sb6.append(str12);
                sb6.append(" mergemax=");
                str11 = str9;
                sb6.append(str11);
                sb6.append(" mergerate=");
                float f2 = (j31 - j28) / j28;
                sb6.append(f2);
                LogUtil.i(str5, sb6.toString());
                TaskHandleOp.getInstance().getTaskHandle().setMergeRate(f2);
            } else {
                str11 = str9;
                str12 = str34;
                TaskHandleOp.getInstance().getTaskHandle().mMergeRate = 0.0f;
            }
            StringBuilder sb7 = new StringBuilder("DownloadProxy [parseParam] maxBytes * threadnum=");
            long j33 = i4 * j19;
            sb7.append(j33);
            sb7.append(str7);
            long j34 = j2;
            sb7.append(j34);
            LogUtil.i(str5, sb7.toString());
            if (z3 && 0 != j19 && j33 > j34) {
                LogUtil.i(str5, "DownloadProxy [parseParam] \u4e0b\u8f7d\u8fc7\u7a0b\u4e2d\uff0c\u53ef\u80fd\u51fa\u73b0\u5185\u5b58\u6ea2\u51fa");
                TaskHandleOp.getInstance().getTaskHandle().setStatus(9);
                return new ArrayList<>();
            }
            LogUtil.i(str5, "[ORBIT] Params projectid=" + strOptString3 + ", wifiOnly=" + z4 + ", logOpen=" + zEquals2 + ", oversea=" + str3 + ", needrefresh=" + strOptString5 + ", notusecdn=" + str37 + ", priority=" + i2 + ", priority_task=" + z + ", threadnum=" + i4 + ", testlog=" + strOptString7 + ", isRenew=" + strOptString8 + ", buffercount=" + str4 + ", mergemax=" + str11 + ", mergespace=" + str12 + ", autofree=" + str6 + ", mergemode=" + str36 + ", configurl=" + str28 + ", downloadid=" + str10 + ", callback_interval=" + str8);
            StringBuilder sb8 = new StringBuilder("DownloadProxy [parseParam] \u6240\u6709\u53c2\u6570\u7ed3\u679c=");
            sb8.append(arrayList4.toString());
            LogUtil.i(str5, sb8.toString());
            TaskHandleOp.getInstance().getTaskHandle().setTaskAllSizes(j28);
            TaskHandleOp.getInstance().getTaskHandle().setTaskMergeAllSizes(j31);
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.setmTotalSize(j28);
            DownloadListenerProxy.getInstances();
            DownloadListenerProxy.setmMergeTotalSize(j31);
            if (TaskHandleOp.getInstance().getTaskHandle().isCheckfs()) {
                DownloadParams downloadParams5 = downloadParams3;
                boolean zIsFreeSpaceEnough = isFreeSpaceEnough(downloadParams5, j31);
                LogUtil.i(str5, "DownloadProxy [parseParam] \u6240\u6709isFreeSpaceEnough\u7ed3\u679c=" + zIsFreeSpaceEnough);
                if (!zIsFreeSpaceEnough) {
                    LogUtil.i(str5, "DownloadProxy [parseParam] \u89e3\u6790\u53c2\u6570\u65f6\uff0c\u53d1\u73b0\u7a7a\u95f4\u4e0d\u8db3\uff0c\u7ec8\u6b62\u4e0b\u8f7d");
                    TaskHandleOp.getInstance().getTaskHandle().setStatus(5);
                    TaskHandleOp.getInstance().getTaskHandle().setFreeSpace(StorageUtil.getFreeSpaceSize(downloadParams5.getFilePath()));
                    return new ArrayList<>();
                }
            }
            LogUtil.i(str5, "DownloadProxy [parseParam] \u6240\u6709\u6587\u4ef6\u603b\u5927\u5c0f=" + j28);
            LogUtil.i(str5, "[ORBIT] Downfile Count=" + jSONArray2.length() + " Bytes=" + j28 + " MinBytes=" + j32 + " MaxBytes=" + j19 + " AvgBytes=" + (j31 / jSONArray2.length()));
            StringBuilder sb9 = new StringBuilder("DownloadProxy [parseParam] \u6240\u6709\u6587\u4ef6\u603b\u5927\u5c0f result=");
            sb9.append(arrayList4.toString());
            LogUtil.i(str5, sb9.toString());
            long downloadedSize = Util.getDownloadedSize(arrayList4);
            StringBuilder sb10 = new StringBuilder("DownloadProxy [parseParam] \u5df2\u7ecf\u4e0b\u8f7d\u597d\u7684\u603b\u5927\u5c0f\u4e3a=");
            sb10.append(downloadedSize);
            LogUtil.i(str5, sb10.toString());
            return arrayList4;
        } catch (NumberFormatException e16) {
            LogUtil.w(TAG, "DownloadProxy [parseParam] NumberFormatException = " + e16);
            return new ArrayList<>();
        }
    }

    private boolean isFreeSpaceEnough(DownloadParams downloadParams, long j) {
        LogUtil.i(TAG, "DownloadProxy [isFreeSpaceEnough] start");
        if (downloadParams == null || j < 0) {
            LogUtil.i(TAG, "DownloadProxy [isFreeSpaceEnough] param error");
            return false;
        }
        boolean zIsCheckFreeSpace = TaskHandleOp.getInstance().getTaskHandle().isCheckFreeSpace();
        LogUtil.i(TAG, "DownloadProxy [isFreeSpaceEnough] checkFreeSpace=" + zIsCheckFreeSpace + ", param.getFilePath()=" + downloadParams.getFilePath() + ", taskFileAllSize=" + j);
        if (zIsCheckFreeSpace) {
            int iCanStore = StorageUtil.canStore(downloadParams.getFilePath(), j, 1.1d);
            LogUtil.i(TAG, "DownloadProxy [isFreeSpaceEnough] canStore=" + iCanStore);
            if (1 != iCanStore) {
                return false;
            }
        }
        return true;
    }
}