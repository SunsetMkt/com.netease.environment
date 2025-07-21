package com.netease.download.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.netease.download.downloader.DownloadProxy;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.handler.Dispatcher;
import com.netease.download.listener.DownloadListenerProxy;
import com.netease.download.util.LogUtil;

/* loaded from: classes5.dex */
public class NetworkStatus {
    public static final int STATUS_MOBILE = 2;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_WIFI = 1;
    private static final String TAG = "NetworkStatus";
    private static boolean sIsInit;
    private static boolean sNeedRefresh;
    private static int sPreValidStatus;

    public static void initialize(Context context) {
        if (sIsInit) {
            return;
        }
        sPreValidStatus = isConnectedWifi(context) ? 1 : isConnectedMobile(context) ? 2 : 0;
        sIsInit = true;
    }

    static void change(Context context) {
        int i;
        try {
            boolean zIsConnected = isConnected(context);
            LogUtil.i(TAG, "\u7f51\u7edc\u662f\u5426\u8fde\u63a5=" + zIsConnected);
            if (!zIsConnected) {
                DownloadListenerProxy.getInstances();
                DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsgWithOtherSatus(12, 0L, 0L, "__DOWNLOAD_NETWORK_LOST__", "__DOWNLOAD_NETWORK_LOST__", "".getBytes(), "0", TaskHandleOp.getInstance().getTaskHandle().getDownloadId(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                TaskHandleOp.getInstance().getTaskHandle().setNetworkLost(1);
            }
            if (isConnectedWifi(context)) {
                LogUtil.i(TAG, "\u8fde\u63a5\u7684\u662fWIFI\u7f51\u7edc");
                i = 1;
            } else if (isConnectedMobile(context)) {
                LogUtil.i(TAG, "\u8fde\u63a5\u7684\u662f\u79fb\u52a8\u7f51\u7edc");
                i = 2;
            } else {
                i = 0;
            }
            LogUtil.i(TAG, "sPreValidStatus=" + sPreValidStatus + ", isNowConnected=" + zIsConnected);
            if (sPreValidStatus != 0 && !zIsConnected) {
                LogUtil.i(TAG, "\u6ca1\u6709\u7f51\u7edc\u8fde\u63a5,\u505c\u6b62\u6389\u6240\u6709\u4efb\u52a1");
                NetController.getInstances().setInterruptedCode(13);
                TaskHandleOp.getInstance().getTaskHandle().setStatus(0);
                DownloadProxy.stopAll();
            }
            if (sPreValidStatus == 0 && zIsConnected) {
                LogUtil.i(TAG, "\u6709\u7f51\u7edc\u8fde\u63a5\uff0c\u91cd\u65b0\u542f\u52a8\u6240\u6709\u4efb\u52a1");
                NetController.getInstances().setInterruptedCode(0);
            }
            int i2 = sPreValidStatus;
            if (i2 != 0 && i != i2) {
                LogUtil.i(TAG, "\u7f51\u7edc\u72b6\u6001\u53d1\u751f\u4e86\u6539\u53d8\uff0c\u539f\u6765\u662f" + sPreValidStatus + ", \u73b0\u5728\u662f" + i);
                TaskHandleOp.getInstance().getTaskHandle().setNetworkSwitch(1);
                Dispatcher.getInstance().notifyNetworkChanged();
                sNeedRefresh = true;
            }
            sPreValidStatus = i;
        } catch (Exception e) {
            LogUtil.w(TAG, "NetworkStatus [change] Exception=" + e.toString());
            e.printStackTrace();
        }
    }

    public static boolean needRefresh() {
        boolean z = sNeedRefresh;
        sNeedRefresh = false;
        return z;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager != null) {
                return connectivityManager.getActiveNetworkInfo();
            }
            return null;
        } catch (Exception e) {
            LogUtil.w(TAG, "NetworkStatus [getNetworkInfo]= " + e);
            return null;
        }
    }

    public static boolean isConnected(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected();
    }

    private static boolean isConnectedWifi(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == 1;
    }

    public static boolean isConnectedMobile(Context context) {
        NetworkInfo networkInfo = getNetworkInfo(context);
        return networkInfo != null && networkInfo.isConnected() && networkInfo.getType() == 0;
    }

    public static int getNetStatus() {
        return sPreValidStatus;
    }
}