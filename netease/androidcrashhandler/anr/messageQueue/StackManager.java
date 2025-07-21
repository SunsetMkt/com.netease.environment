package com.netease.androidcrashhandler.anr.messageQueue;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.netease.androidcrashhandler.AndroidCrashHandler;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes5.dex */
public class StackManager implements MessageCallback {
    private static final String TAG = "trace";
    private static volatile Handler mHandler;
    private static volatile HandlerThread mHandlerThread;
    private static StackManager sStackManager;
    private boolean needNativeStackTrace = false;
    private long mPointTime = 0;
    private final byte[] mLock = new byte[0];
    private LimitMap<Long, String> mStackTraceMap = new LimitMap<>(20);
    private LimitMap<Long, String> mNativeStackTraceMap = new LimitMap<>(20);
    private final byte[] mDataLock = new byte[0];

    private static class LimitMap<K, V> extends LinkedHashMap<K, V> {
        private int QUEUE_MAX_SIZE;

        public LimitMap(int i) {
            super(i + 1, 1.0f);
            this.QUEUE_MAX_SIZE = i;
        }

        @Override // java.util.LinkedHashMap
        protected boolean removeEldestEntry(Map.Entry<K, V> entry) {
            return size() > this.QUEUE_MAX_SIZE;
        }
    }

    private StackManager() {
        mHandlerThread = new HandlerThread("trace_stack");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    public static StackManager getInstance() {
        if (sStackManager == null) {
            sStackManager = new StackManager();
        }
        return sStackManager;
    }

    public String getJavaMainThreadStackTrack() {
        LogUtils.i("trace", "StackManager [getJavaMainThreadStackTrack] start");
        StackTraceElement[] stackTrace = Looper.getMainLooper().getThread().getStackTrace();
        if (stackTrace == null) {
            LogUtils.i("trace", "StackManager [printTrack] \u65e0\u6cd5\u6355\u83b7\u5806\u6808");
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (StackTraceElement stackTraceElement : stackTrace) {
            stringBuffer.append("#");
            stringBuffer.append(stackTraceElement.toString());
        }
        return stringBuffer.toString();
    }

    public void start() {
        LogUtils.i("trace", "StackManager [start] start");
        LooperMessageLoggingManager.getInstance().registerCallback(this);
        CUtil.runOnNewChildThread(new CUtil.ThreadTask() { // from class: com.netease.androidcrashhandler.anr.messageQueue.StackManager.1
            AnonymousClass1() {
            }

            @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
            public void run() throws InterruptedException {
                long j;
                long jNanoTime;
                while (true) {
                    try {
                        synchronized (StackManager.this.mLock) {
                            j = StackManager.this.mPointTime;
                            jNanoTime = System.nanoTime();
                        }
                        if (j > 0) {
                            long j2 = jNanoTime - j;
                            if (j2 / 1000000 > 800.0d) {
                                synchronized (StackManager.this.mDataLock) {
                                    if (!StackManager.this.mStackTraceMap.containsKey(Long.valueOf(j))) {
                                        LogUtils.i("trace", "cur:" + jNanoTime + "mPointTime:" + j + " dump stack trace");
                                        StackManager.this.mStackTraceMap.put(Long.valueOf(j), StackManager.this.getJavaMainThreadStackTrack());
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("size:");
                                        sb.append(StackManager.this.mStackTraceMap.size());
                                        LogUtils.i("trace", sb.toString());
                                    }
                                }
                            }
                            if (StackManager.this.isNeedNativeStackTrace() && j2 / 1000000 >= 1800.0d) {
                                synchronized (StackManager.this.mDataLock) {
                                    if (!StackManager.this.mNativeStackTraceMap.containsKey(Long.valueOf(j))) {
                                        LogUtils.i("trace", "cur:" + jNanoTime + "mPointTime:" + j + " dump  native stack trace");
                                        StackManager.this.mNativeStackTraceMap.put(Long.valueOf(j), AndroidCrashHandler.getInstance().getThreadFullUnwind());
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("size:");
                                        sb2.append(StackManager.this.mNativeStackTraceMap.size());
                                        LogUtils.i("trace", sb2.toString());
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.i("trace", "StackManager [start] Exception=" + e.toString());
                    }
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e2) {
                        throw new RuntimeException(e2);
                    }
                }
            }
        }, "StackManager");
    }

    /* renamed from: com.netease.androidcrashhandler.anr.messageQueue.StackManager$1 */
    class AnonymousClass1 implements CUtil.ThreadTask {
        AnonymousClass1() {
        }

        @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
        public void run() throws InterruptedException {
            long j;
            long jNanoTime;
            while (true) {
                try {
                    synchronized (StackManager.this.mLock) {
                        j = StackManager.this.mPointTime;
                        jNanoTime = System.nanoTime();
                    }
                    if (j > 0) {
                        long j2 = jNanoTime - j;
                        if (j2 / 1000000 > 800.0d) {
                            synchronized (StackManager.this.mDataLock) {
                                if (!StackManager.this.mStackTraceMap.containsKey(Long.valueOf(j))) {
                                    LogUtils.i("trace", "cur:" + jNanoTime + "mPointTime:" + j + " dump stack trace");
                                    StackManager.this.mStackTraceMap.put(Long.valueOf(j), StackManager.this.getJavaMainThreadStackTrack());
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("size:");
                                    sb.append(StackManager.this.mStackTraceMap.size());
                                    LogUtils.i("trace", sb.toString());
                                }
                            }
                        }
                        if (StackManager.this.isNeedNativeStackTrace() && j2 / 1000000 >= 1800.0d) {
                            synchronized (StackManager.this.mDataLock) {
                                if (!StackManager.this.mNativeStackTraceMap.containsKey(Long.valueOf(j))) {
                                    LogUtils.i("trace", "cur:" + jNanoTime + "mPointTime:" + j + " dump  native stack trace");
                                    StackManager.this.mNativeStackTraceMap.put(Long.valueOf(j), AndroidCrashHandler.getInstance().getThreadFullUnwind());
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("size:");
                                    sb2.append(StackManager.this.mNativeStackTraceMap.size());
                                    LogUtils.i("trace", sb2.toString());
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.i("trace", "StackManager [start] Exception=" + e.toString());
                }
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e2) {
                    throw new RuntimeException(e2);
                }
            }
        }
    }

    @Override // com.netease.androidcrashhandler.anr.messageQueue.MessageCallback
    public void messageStart(long j, long j2) {
        synchronized (this.mLock) {
            this.mPointTime = j;
        }
    }

    @Override // com.netease.androidcrashhandler.anr.messageQueue.MessageCallback
    public void messageEnd(long j, long j2) {
        synchronized (this.mLock) {
            this.mPointTime = j;
        }
    }

    public String getStackTrace(long j) {
        synchronized (this.mDataLock) {
            if (this.mStackTraceMap.containsKey(Long.valueOf(j))) {
                LogUtils.i("trace", "StackManager [msg] get stack trace=" + j);
                return this.mStackTraceMap.get(Long.valueOf(j));
            }
            LogUtils.i("trace", "StackManager [msg] fail match stack trace=" + j);
            return "";
        }
    }

    public String getNativeStackTrace(long j) {
        synchronized (this.mDataLock) {
            LogUtils.i("trace", "StackManager [msg] map size=" + this.mNativeStackTraceMap.size());
            if (this.mNativeStackTraceMap.containsKey(Long.valueOf(j))) {
                LogUtils.i("trace", "StackManager [msg] get native stack trace=" + j);
                return this.mNativeStackTraceMap.get(Long.valueOf(j));
            }
            LogUtils.i("trace", "StackManager [msg] fail match stack trace=" + j);
            return "";
        }
    }

    public void enableNativeStackTrace() {
        this.needNativeStackTrace = true;
        AndroidCrashHandler.getInstance().setTargetThreadUnwind(this.needNativeStackTrace);
    }

    public void disableNativeStackTrace() {
        this.needNativeStackTrace = false;
        AndroidCrashHandler.getInstance().setTargetThreadUnwind(this.needNativeStackTrace);
    }

    public boolean isNeedNativeStackTrace() {
        return this.needNativeStackTrace;
    }
}