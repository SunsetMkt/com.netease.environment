package com.netease.download.listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.core.app.NotificationCompat;
import com.netease.download.downloader.DownloadParamsQueueManager;
import com.netease.download.downloader.DownloadProxy;
import com.netease.download.downloader.TaskHandleOp;
import com.netease.download.downloadpart.DownloadAllProxy;
import com.netease.download.handler.Dispatcher;
import com.netease.download.reporter.ReportCallBack;
import com.netease.download.reporter.ReportProxy;
import com.netease.download.taskManager.TaskExecutor;
import com.netease.download.util.LogUtil;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class DownloadListenerProxy {
    private static final String TAG = "DownloadListenerCore";
    private static volatile int mDownloadCancelFileCount;
    private static volatile int mDownloadFailFileCount;
    private static DownloadListenerHandler mDownloadListenerHandler;
    public static volatile long mHasDownloadSize;
    public static volatile int mHasFinishFileCount;
    private static DownloadListener mListener;
    public static volatile long mMergeTotalSize;
    public static volatile int mTotalFileCount;
    public static volatile long mTotalSize;
    private static DownloadListenerProxy sDownloadListenProxy;
    private static Object sObject = new Object();
    public static ArrayList<String> mFinishFilePathList = new ArrayList<>();
    private static volatile long mPreCallBackTime = -1;
    public static HashMap<String, Integer> mFileMap = new HashMap<>();
    private static String mFileName = null;
    private static String mFilePath = null;

    static /* synthetic */ int access$408() {
        int i = mDownloadFailFileCount;
        mDownloadFailFileCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$508() {
        int i = mDownloadCancelFileCount;
        mDownloadCancelFileCount = i + 1;
        return i;
    }

    private DownloadListenerProxy() {
    }

    public static DownloadListenerProxy getInstances() {
        if (sDownloadListenProxy == null) {
            sDownloadListenProxy = new DownloadListenerProxy();
            getDownloadListenerHandler();
        }
        return sDownloadListenProxy;
    }

    public void init(DownloadListener downloadListener) {
        LogUtil.i(TAG, "\u521d\u59cb\u5316\u56de\u8c03\u76d1\u542c\u5668");
        mListener = downloadListener;
        reset();
    }

    public DownloadListener getDownloadListener() {
        return mListener;
    }

    public static DownloadListenerHandler getDownloadListenerHandler() {
        if (mDownloadListenerHandler == null) {
            mDownloadListenerHandler = new DownloadListenerHandler(Looper.getMainLooper());
        }
        return mDownloadListenerHandler;
    }

    public synchronized long getHasDownloadSize() {
        return mHasDownloadSize;
    }

    public void clear() {
        mTotalSize = 0L;
        mHasDownloadSize = 0L;
        mTotalFileCount = 0;
        mHasFinishFileCount = 0;
    }

    public static int getmTotalFileCount() {
        return mTotalFileCount;
    }

    public static void setmTotalFileCount(int i) {
        mTotalFileCount = i;
    }

    public static long getmHasFinishFileCount() {
        return mHasFinishFileCount;
    }

    private void reset() {
        mFinishFilePathList = new ArrayList<>();
    }

    public static long getmTotalSize() {
        return mTotalSize;
    }

    public static void setmTotalSize(long j) {
        mTotalSize = j;
    }

    public static long getmMergeTotalSize() {
        return mMergeTotalSize;
    }

    public static void setmMergeTotalSize(long j) {
        mMergeTotalSize = j;
    }

    public static class DownloadListenerHandler extends Handler {
        private static final String TAG = "InnerDownloadHandler";
        JSONObject data;

        public String getErrorMessage(int i) {
            switch (i) {
                case 0:
                    return "\u4e0b\u8f7d\u6210\u529f";
                case 1:
                    return "\u8fde\u63a5\u9519\u8bef";
                case 2:
                    return "\u5927\u5c0f\u9a8c\u8bc1\u5931\u8d25";
                case 3:
                    return "MD5\u9a8c\u8bc1\u5931\u8d25";
                case 4:
                    return "\u5199\u5165\u6587\u4ef6\u5931\u8d25";
                case 5:
                    return "\u8bbe\u5907\u7a7a\u95f4\u4e0d\u8db3";
                case 6:
                case 7:
                case 8:
                case 10:
                case 11:
                default:
                    return "\u672a\u77e5\u9519\u8bef";
                case 9:
                    return "out of memory";
                case 12:
                    return "\u4e0b\u8f7d\u88ab\u53d6\u6d88";
                case 13:
                    return "\u8bfb\u53d6\u5185\u5bb9\u8d85\u65f6";
                case 14:
                    return "\u65e0\u6548\u7684\u4f20\u5165\u53c2\u6570";
                case 15:
                    return "\u65e0\u6548\u7684\u57df\u540d\uff0c\u65e0\u6cd5\u89e3\u6790";
                case 16:
                    return "\u914d\u7f6e\u6587\u4ef6\u4e0b\u8f7d\u9519\u8bef";
            }
        }

        /* synthetic */ DownloadListenerHandler(Looper looper, AnonymousClass1 anonymousClass1) {
            this(looper);
        }

        private DownloadListenerHandler(Looper looper) {
            super(looper);
            this.data = new JSONObject();
        }

        public synchronized void sendProgressMsg(long j, long j2, String str, String str2, String str3, String str4) {
            if (DownloadListenerProxy.mHasDownloadSize < DownloadListenerProxy.mMergeTotalSize) {
                if (DownloadListenerProxy.mHasDownloadSize + j2 > DownloadListenerProxy.mMergeTotalSize) {
                    DownloadListenerProxy.mHasDownloadSize = DownloadListenerProxy.mMergeTotalSize;
                } else {
                    DownloadListenerProxy.mHasDownloadSize += j2;
                }
            }
            long j3 = DownloadListenerProxy.mHasDownloadSize;
            if (0 != DownloadListenerProxy.mTotalSize && 0 != DownloadListenerProxy.mMergeTotalSize) {
                j3 = (long) (DownloadListenerProxy.mTotalSize * (DownloadListenerProxy.mHasDownloadSize / DownloadListenerProxy.mMergeTotalSize));
            }
            if (j3 > DownloadListenerProxy.mTotalSize) {
                j3 = DownloadListenerProxy.mTotalSize;
            }
            String unused = DownloadListenerProxy.mFileName = str;
            String unused2 = DownloadListenerProxy.mFilePath = str2;
            try {
                this.data.put("size", DownloadListenerProxy.mTotalSize);
                this.data.put("bytes", j3);
                this.data.put("filename", DownloadListenerProxy.mFileName);
                this.data.put("filepath", DownloadListenerProxy.mFilePath);
                this.data.put("downloadid", str3);
                this.data.put("orbitId", str4);
                this.data.put(NotificationCompat.CATEGORY_PROGRESS, 0 != DownloadListenerProxy.mTotalSize ? new DecimalFormat("0.000").format(j3 / DownloadListenerProxy.mTotalSize) : "0");
            } catch (Exception unused3) {
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (jCurrentTimeMillis - DownloadListenerProxy.mPreCallBackTime > TaskHandleOp.getInstance().getTaskHandle().getCallBackInterval() || j3 == DownloadListenerProxy.mTotalSize) {
                long unused4 = DownloadListenerProxy.mPreCallBackTime = jCurrentTimeMillis;
                sendMessage(obtainMessage(2, this.data));
            }
        }

        public void sendFinishMsgWithOtherSatus(int i, long j, long j2, String str, String str2, byte[] bArr, String str3, String str4, String str5) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("code", i);
                jSONObject.put("finished", true);
                jSONObject.put("size", j);
                if (j2 <= j) {
                    j = j2;
                }
                jSONObject.put("bytes", j);
                jSONObject.put("filename", str);
                jSONObject.put("filepath", str2);
                jSONObject.put("sessionid", str3);
                jSONObject.put("downloadid", str4);
                jSONObject.put("orbitid", str5);
                if (i == 0) {
                    jSONObject.put("filebytes", bArr);
                }
                jSONObject.put("error", getErrorMessage(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
            sendMessage(obtainMessage(4, jSONObject));
        }

        public void sendFinishMsg(int i, long j, long j2, String str, String str2, byte[] bArr, String str3, String str4) {
            sendFinishMsg(i, j, j2, str, str2, bArr, str3, str4, 0L);
        }

        /* JADX WARN: Removed duplicated region for block: B:164:0x020c  */
        /* JADX WARN: Removed duplicated region for block: B:166:0x0231  */
        /* JADX WARN: Removed duplicated region for block: B:169:0x0267  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void sendFinishMsg(int r20, long r21, long r23, java.lang.String r25, java.lang.String r26, byte[] r27, java.lang.String r28, java.lang.String r29, long r30) {
            /*
                Method dump skipped, instructions count: 856
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.download.listener.DownloadListenerProxy.DownloadListenerHandler.sendFinishMsg(int, long, long, java.lang.String, java.lang.String, byte[], java.lang.String, java.lang.String, long):void");
        }

        /* renamed from: com.netease.download.listener.DownloadListenerProxy$DownloadListenerHandler$1 */
        class AnonymousClass1 implements ReportCallBack {
            AnonymousClass1() {
            }

            @Override // com.netease.download.reporter.ReportCallBack
            public void reportFinish() {
                LogUtil.i(DownloadListenerHandler.TAG, "DownloadListenerCore [start] finish, report finish, callback to user");
                LogUtil.i(DownloadListenerHandler.TAG, "DownloadListenerCore [start] mHasFinishFileCount=" + DownloadListenerProxy.mHasFinishFileCount + ", mTotalFileCount=" + DownloadListenerProxy.mTotalFileCount);
                TaskHandleOp.getInstance().getTaskHandle().setTimeToEndTask(System.currentTimeMillis());
                if (DownloadListenerProxy.mHasFinishFileCount == DownloadListenerProxy.mTotalFileCount) {
                    TaskHandleOp.getInstance().addTaskFailCodeCountMap(12, DownloadListenerProxy.mDownloadCancelFileCount);
                    ReportProxy.getInstance().setmReportCallBack(null);
                    if (TaskHandleOp.getInstance().getTaskHandle().isAutoFree()) {
                        LogUtil.i(DownloadListenerHandler.TAG, "DownloadAllProxy [start] freeThreadPool");
                        DownloadAllProxy.getInstances().stop();
                        TaskExecutor.getInstance().closeFixedThreadPool();
                        Dispatcher.getInstance().close();
                    }
                    DownloadProxy.unregisterReceiver();
                    DownloadListenerProxy.getInstances();
                    DownloadListenerProxy.getDownloadListenerHandler().sendFinishMsg(0, 0L, 0L, "__DOWNLOAD_END__", "__DOWNLOAD_END__", "".getBytes(), TaskHandleOp.getInstance().getTaskHandle().getSessionid(), TaskHandleOp.getInstance().getTaskHandle().getNtesOrbitId());
                    long jCurrentTimeMillis = System.currentTimeMillis() - TaskHandleOp.getInstance().getTaskHandle().getTimeToStartTask();
                    LogUtil.i(DownloadListenerHandler.TAG, "[ORBIT] Finish Count=" + DownloadListenerProxy.mTotalFileCount + " Bytes=" + DownloadListenerProxy.mTotalSize + " Success=" + (DownloadListenerProxy.mTotalFileCount - DownloadListenerProxy.mDownloadFailFileCount) + " DownBytes=" + DownloadListenerProxy.mHasDownloadSize + " DownCost=" + jCurrentTimeMillis + " DownSpeed=" + (jCurrentTimeMillis > 0 ? DownloadListenerProxy.mHasDownloadSize / jCurrentTimeMillis : -1L));
                    LogUtil.i(DownloadListenerHandler.TAG, "[ORBIT] Log upload=true");
                    TaskHandleOp.getInstance().getTaskHandle().setTimeToEndTask(System.currentTimeMillis());
                    TaskHandleOp.getInstance().getTaskHandle().showTime();
                    TaskHandleOp.getInstance().reset();
                    DownloadParamsQueueManager.getInstance().dispatch();
                }
            }
        }

        private boolean isFile(String str) {
            return ("__DOWNLOAD_END__".equals(str) || "__DOWNLOAD_START__".equals(str) || "__DOWNLOAD_NETWORK_LOST__".equals(str) || "__DOWNLOAD_DNS_RESOLVED__".equals(str) || "__DOWNLOAD_CONFIG__".equals(str) || "__DOWNLOAD_PARAM_ERROR__".equals(str) || "__DOWNLOAD_STORAGE_ERROR__".equals(str) || "__DOWNLOAD_QUEUE_CLEAR__".equals(str)) ? false : true;
        }

        public synchronized void addHasDownloadMag(long j, String str, String str2, int i) {
            DownloadListenerProxy.mHasDownloadSize += j;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (DownloadListenerProxy.mListener == null || message == null) {
                return;
            }
            int i = message.what;
            if (i == 2) {
                DownloadListenerProxy.mListener.onProgress((JSONObject) message.obj);
            } else if (i == 4 || i == 5) {
                DownloadListenerProxy.mListener.onFinish((JSONObject) message.obj);
            } else {
                LogUtil.w(TAG, "not exist this type of msg!");
            }
        }
    }
}