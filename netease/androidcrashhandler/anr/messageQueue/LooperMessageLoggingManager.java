package com.netease.androidcrashhandler.anr.messageQueue;

import android.os.Looper;
import android.text.TextUtils;
import android.util.Printer;
import com.netease.androidcrashhandler.util.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONArray;

/* loaded from: classes5.dex */
public class LooperMessageLoggingManager {
    private static final String END = "<<<<< Finished";
    private static final String END_TYPE = "END";
    private static final String IDLE_MERGE_TYPE = "IDLE_MERGE";
    private static final String IDLE_TYPE = "IDLE";
    private static final String INPUT_TYPE = "INPUT";
    public static final long LAG_TIME = 2000;
    private static final String LAG_TYPE = "LAG";
    public static final long MERGE_TIME = 1000;
    private static final String MERGE_TYPE = "MERGE";
    private static final int QUEUE_MAX = 20;
    private static final String START = ">>>>> Dispatching";
    private static final String TAG = "trace";
    public static final int TIME_MILLIS_TO_NANO = 1000000;
    public static final int TIME_SECOND_TO_NANO = 1000000000;
    private static LooperMessageLoggingManager sLooperMessageLoggingManager;
    private long index;
    private static ArrayList<HandleMessage> mList = new ArrayList<>();
    private static BlockingQueue<HandleMessage> mHandleQueue = new LinkedBlockingQueue(20);
    private static BlockingQueue<HandleMessage> mInputHandleQueue = new LinkedBlockingQueue(10);
    private HandleMessage mHandleMessage = new HandleMessage();
    private HandleMessage mPreMessage = new HandleMessage();
    private HandleMessage mInputLagMessage = new HandleMessage();
    private final byte[] mLock = new byte[1];
    private List<MessageCallback> mCallbackList = new ArrayList();

    private LooperMessageLoggingManager() {
    }

    public static LooperMessageLoggingManager getInstance() {
        if (sLooperMessageLoggingManager == null) {
            sLooperMessageLoggingManager = new LooperMessageLoggingManager();
        }
        return sLooperMessageLoggingManager;
    }

    private void add2Queue(HandleMessage handleMessage) {
        BlockingQueue<HandleMessage> blockingQueue;
        if (handleMessage == null || (blockingQueue = mHandleQueue) == null) {
            return;
        }
        try {
            if (blockingQueue.size() >= 20) {
                mHandleQueue.remove();
            }
            mHandleQueue.offer(handleMessage);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("trace", "LooperMessageLoggingManager [printAnrMeessage] Exception = " + e.toString());
        }
    }

    private void addInput2Queue(HandleMessage handleMessage) {
        BlockingQueue<HandleMessage> blockingQueue;
        if (handleMessage == null || (blockingQueue = mInputHandleQueue) == null) {
            return;
        }
        try {
            if (blockingQueue.size() >= 10) {
                mInputHandleQueue.remove();
            }
            mInputHandleQueue.offer(handleMessage);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.i("trace", "LooperMessageLoggingManager [printAnrMeessage] Exception = " + e.toString());
        }
    }

    public void registerCallback(MessageCallback messageCallback) {
        synchronized (this.mCallbackList) {
            if (!this.mCallbackList.contains(messageCallback)) {
                this.mCallbackList.add(messageCallback);
            }
        }
    }

    public void unregisterCallback(MessageCallback messageCallback) {
        synchronized (this.mCallbackList) {
            if (this.mCallbackList.contains(messageCallback)) {
                this.mCallbackList.remove(messageCallback);
            }
        }
    }

    public void sendInputEventLagMessage(String str) {
        LogUtils.i("trace", "LooperMessageLoggingManager [sendInputEventLagMessage] start");
        synchronized (this.mLock) {
            this.mInputLagMessage.setStartTime(System.nanoTime() - 2000000000);
            this.mInputLagMessage.setStartInfo(">>>>> Dispatching Input Lag Start");
            this.mInputLagMessage.addMessageCount();
            this.mInputLagMessage.setType(INPUT_TYPE);
            this.mInputLagMessage.setNativeStaicTrace(str);
            this.mInputLagMessage.setActionTime(System.currentTimeMillis());
            this.mInputLagMessage.setStackTrace(StackManager.getInstance().getJavaMainThreadStackTrack());
            LogUtils.i("trace", "LooperMessageLoggingManager [sendInputEventLagMessage] end");
        }
    }

    public void sendInputEventLagFinish() {
        LogUtils.i("trace", "LooperMessageLoggingManager [sendInputEventLagFinish] startTime:" + this.mInputLagMessage.getStartTime());
        synchronized (this.mLock) {
            if (this.mInputLagMessage.hasStartTime()) {
                HandleMessage handleMessage = new HandleMessage();
                try {
                    handleMessage = this.mInputLagMessage.m514clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                    handleMessage.setStartTime(this.mInputLagMessage.getStartTime());
                    handleMessage.setStartInfo(this.mInputLagMessage.getStartInfo());
                    handleMessage.setMessageCount(this.mInputLagMessage.getMessageCount());
                    handleMessage.setType(this.mInputLagMessage.getType());
                    handleMessage.setNativeStaicTrace(this.mInputLagMessage.getNativeStaicTrace());
                    handleMessage.setActionTime(this.mInputLagMessage.getActionTime());
                    handleMessage.setStackTrace(this.mInputLagMessage.getStackTrace());
                }
                handleMessage.setEndTime(System.nanoTime());
                handleMessage.setEndInfo("<<<<< Finished Input Lag Finish");
                handleMessage.calculateDuration();
                LogUtils.i("trace", "LooperMessageLoggingManager [handleMessageInfo] \u6536\u96c6\u5230InputQueue  [A]MSG=" + handleMessage.toString());
                addInput2Queue(handleMessage);
                this.mInputLagMessage.clean();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x010d A[Catch: all -> 0x0178, TryCatch #1 {, blocks: (B:4:0x000c, B:6:0x0014, B:7:0x0020, B:11:0x006a, B:10:0x0028, B:12:0x0096, B:14:0x00a2, B:16:0x00ac, B:17:0x00f7, B:23:0x0156, B:24:0x0165, B:25:0x016f, B:28:0x0172, B:29:0x0177, B:20:0x0107, B:21:0x010c, B:22:0x010d), top: B:35:0x000c, inners: #0, #2, #3 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void getAnrMessage(org.json.JSONObject r7) {
        /*
            Method dump skipped, instructions count: 379
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.getAnrMessage(org.json.JSONObject):void");
    }

    public void start() {
        LogUtils.i("trace", "LooperMessageLoggingManager [start] start");
        StackManager.getInstance().start();
        Looper.getMainLooper().setMessageLogging(new Printer() { // from class: com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.1
            /* JADX WARN: Removed duplicated region for block: B:39:0x00b6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            @Override // android.util.Printer
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void println(java.lang.String r9) {
                /*
                    r8 = this;
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r0 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this
                    byte[] r0 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$000(r0)
                    monitor-enter(r0)
                    java.lang.String r1 = ">>>>> Dispatching"
                    boolean r1 = r9.startsWith(r1)     // Catch: java.lang.Throwable -> Le0
                    if (r1 == 0) goto L62
                    long r1 = java.lang.System.nanoTime()     // Catch: java.lang.Throwable -> Le0
                    long r3 = android.os.SystemClock.currentThreadTimeMillis()     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r6 = new com.netease.androidcrashhandler.anr.messageQueue.HandleMessage     // Catch: java.lang.Throwable -> Le0
                    r6.<init>()     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$102(r5, r6)     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$100(r5)     // Catch: java.lang.Throwable -> Le0
                    r5.setStartTime(r1)     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$100(r5)     // Catch: java.lang.Throwable -> Le0
                    r5.setStartCpuTime(r3)     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$100(r5)     // Catch: java.lang.Throwable -> Le0
                    r5.setStartInfo(r9)     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    java.util.List r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$200(r5)     // Catch: java.lang.Throwable -> Le0
                    monitor-enter(r5)     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r6 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> L5f
                    java.util.List r6 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$200(r6)     // Catch: java.lang.Throwable -> L5f
                    java.util.Iterator r6 = r6.iterator()     // Catch: java.lang.Throwable -> L5f
                L4d:
                    boolean r7 = r6.hasNext()     // Catch: java.lang.Throwable -> L5f
                    if (r7 == 0) goto L5d
                    java.lang.Object r7 = r6.next()     // Catch: java.lang.Throwable -> L5f
                    com.netease.androidcrashhandler.anr.messageQueue.MessageCallback r7 = (com.netease.androidcrashhandler.anr.messageQueue.MessageCallback) r7     // Catch: java.lang.Throwable -> L5f
                    r7.messageStart(r1, r3)     // Catch: java.lang.Throwable -> L5f
                    goto L4d
                L5d:
                    monitor-exit(r5)     // Catch: java.lang.Throwable -> L5f
                    goto L62
                L5f:
                    r9 = move-exception
                    monitor-exit(r5)     // Catch: java.lang.Throwable -> L5f
                    throw r9     // Catch: java.lang.Throwable -> Le0
                L62:
                    java.lang.String r1 = "<<<<< Finished"
                    boolean r1 = r9.startsWith(r1)     // Catch: java.lang.Throwable -> Le0
                    if (r1 == 0) goto Lde
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r1 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r1 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$100(r1)     // Catch: java.lang.Throwable -> Le0
                    long r1 = r1.getStartTime()     // Catch: java.lang.Throwable -> Le0
                    r3 = 0
                    int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                    if (r1 <= 0) goto Lde
                    long r1 = java.lang.System.nanoTime()     // Catch: java.lang.Throwable -> Le0
                    long r3 = android.os.SystemClock.currentThreadTimeMillis()     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$100(r5)     // Catch: java.lang.Throwable -> Le0
                    r5.setEndTime(r1)     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$100(r5)     // Catch: java.lang.Throwable -> Le0
                    r5.setEndCpuTime(r3)     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$100(r5)     // Catch: java.lang.Throwable -> Le0
                    r5.setEndInfo(r9)     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r9 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r9 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$100(r9)     // Catch: java.lang.Throwable -> Le0
                    r9.addMessageCount()     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r9 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r9 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$100(r9)     // Catch: java.lang.Throwable -> Le0
                    r9.calculateDuration()     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r9 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    java.util.List r9 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$200(r9)     // Catch: java.lang.Throwable -> Le0
                    monitor-enter(r9)     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Ldb
                    java.util.List r5 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$200(r5)     // Catch: java.lang.Throwable -> Ldb
                    java.util.Iterator r5 = r5.iterator()     // Catch: java.lang.Throwable -> Ldb
                Lc0:
                    boolean r6 = r5.hasNext()     // Catch: java.lang.Throwable -> Ldb
                    if (r6 == 0) goto Ld0
                    java.lang.Object r6 = r5.next()     // Catch: java.lang.Throwable -> Ldb
                    com.netease.androidcrashhandler.anr.messageQueue.MessageCallback r6 = (com.netease.androidcrashhandler.anr.messageQueue.MessageCallback) r6     // Catch: java.lang.Throwable -> Ldb
                    r6.messageEnd(r1, r3)     // Catch: java.lang.Throwable -> Ldb
                    goto Lc0
                Ld0:
                    monitor-exit(r9)     // Catch: java.lang.Throwable -> Ldb
                    com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager r9 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.this     // Catch: java.lang.Throwable -> Le0
                    com.netease.androidcrashhandler.anr.messageQueue.HandleMessage r1 = com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.access$100(r9)     // Catch: java.lang.Throwable -> Le0
                    r9.handleMessageInfoNew(r1)     // Catch: java.lang.Throwable -> Le0
                    goto Lde
                Ldb:
                    r1 = move-exception
                    monitor-exit(r9)     // Catch: java.lang.Throwable -> Ldb
                    throw r1     // Catch: java.lang.Throwable -> Le0
                Lde:
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> Le0
                    return
                Le0:
                    r9 = move-exception
                    monitor-exit(r0)     // Catch: java.lang.Throwable -> Le0
                    throw r9
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager.AnonymousClass1.println(java.lang.String):void");
            }
        });
    }

    public void handleMessageInfoNew(HandleMessage handleMessage) {
        boolean z;
        HandleMessage handleMessage2 = new HandleMessage();
        boolean z2 = false;
        if (mList.size() > 0) {
            handleMessage2.setStartTime(mList.get(0).getStartTime());
            ArrayList<HandleMessage> arrayList = mList;
            handleMessage2.setEndTime(arrayList.get(arrayList.size() - 1).getEndTime());
            handleMessage2.setStartCpuTime(mList.get(0).getStartCpuTime());
            ArrayList<HandleMessage> arrayList2 = mList;
            handleMessage2.setEndCpuTime(arrayList2.get(arrayList2.size() - 1).getEndCpuTime());
            handleMessage2.calculateDuration();
            StringBuilder sb = new StringBuilder(">>>>> Dispatching MergeMsg:");
            ArrayList<HandleMessage> arrayList3 = mList;
            sb.append(arrayList3.get(arrayList3.size() - 1).getStartInfo());
            handleMessage2.setStartInfo(sb.toString());
            StringBuilder sb2 = new StringBuilder("<<<<< Finished MergeMsg:");
            ArrayList<HandleMessage> arrayList4 = mList;
            sb2.append(arrayList4.get(arrayList4.size() - 1).getEndInfo());
            handleMessage2.setEndInfo(sb2.toString());
            handleMessage2.setMessageCount(mList.size());
            handleMessage2.setType(MERGE_TYPE);
        }
        HandleMessage handleMessage3 = new HandleMessage();
        if (0 != this.mPreMessage.getEndTime()) {
            handleMessage3.setStartTime(this.mPreMessage.getEndTime());
            handleMessage3.setStartCpuTime(this.mPreMessage.getEndCpuTime());
            handleMessage3.setStartInfo(">>>>> Dispatching nativePollOnce");
            handleMessage3.setEndTime(handleMessage.getStartTime());
            handleMessage3.setEndCpuTime(handleMessage.getStartCpuTime());
            handleMessage3.calculateDuration();
            handleMessage3.setEndInfo("<<<<< Finished nativePollOnce");
            handleMessage3.addMessageCount();
            handleMessage3.setType(IDLE_TYPE);
        }
        if (handleMessage2.getDuration() > 1.0E9d) {
            handleMessage2.setActionTime(System.currentTimeMillis());
            z = true;
        } else {
            z = false;
        }
        if (handleMessage3.getDuration() > 1.0E9d) {
            if (!z && mList.size() > 0) {
                handleMessage2.setActionTime(System.currentTimeMillis());
                z = true;
            }
            if (handleMessage3.getDuration() > 2.0E9d) {
                handleMessage3.setNativeStaicTrace(StackManager.getInstance().getNativeStackTrace(handleMessage3.getStartTime()));
            }
            handleMessage3.setStackTrace(StackManager.getInstance().getStackTrace(handleMessage3.getStartTime()));
            handleMessage3.setActionTime(System.currentTimeMillis());
            z2 = true;
        } else if (!z && handleMessage2.getDuration() + handleMessage3.getDuration() > 1.0E9d) {
            double duration = handleMessage2.getDuration() / (handleMessage3.getDuration() + handleMessage2.getDuration());
            handleMessage2.setActionTime(System.currentTimeMillis());
            if (duration > 0.5d) {
                handleMessage2.setEndTime(handleMessage3.getEndTime());
                handleMessage2.setEndCpuTime(handleMessage3.getEndCpuTime());
                handleMessage2.calculateDuration();
                handleMessage2.setType(IDLE_MERGE_TYPE);
                handleMessage2.setMessageCount(mList.size() + handleMessage3.getMessageCount());
            } else {
                z2 = true;
            }
            z = true;
        } else if (z) {
            handleMessage2.setEndTime(handleMessage3.getEndTime());
            handleMessage2.setEndCpuTime(handleMessage3.getEndCpuTime());
            handleMessage2.calculateDuration();
            handleMessage2.setActionTime(System.currentTimeMillis());
            handleMessage2.setType(MERGE_TYPE);
            handleMessage2.addMessageCount();
        }
        if (z) {
            add2Queue(handleMessage2);
            mList.clear();
        }
        if (z2) {
            add2Queue(handleMessage3);
        }
        if (!z && !z2 && handleMessage3.getDuration() > 0.0d) {
            if (mList.size() == 0) {
                handleMessage2.setStartCpuTime(handleMessage3.getStartCpuTime());
                handleMessage2.setStartTime(handleMessage3.getStartTime());
                handleMessage2.setStartInfo(">>>>> Dispatching MergeMsg");
                handleMessage2.setEndInfo("<<<<< Finished MergeMsg");
                handleMessage2.setMessageCount(mList.size());
                handleMessage2.setType(MERGE_TYPE);
            }
            mList.add(handleMessage3);
            ArrayList<HandleMessage> arrayList5 = mList;
            handleMessage2.setEndTime(arrayList5.get(arrayList5.size() - 1).getEndTime());
            ArrayList<HandleMessage> arrayList6 = mList;
            handleMessage2.setEndCpuTime(arrayList6.get(arrayList6.size() - 1).getEndCpuTime());
            handleMessage2.calculateDuration();
            handleMessage2.setMessageCount(mList.size());
        }
        if (handleMessage.getDuration() > 1.0E9d) {
            if (handleMessage.getDuration() > 2.0E9d) {
                handleMessage.setNativeStaicTrace(StackManager.getInstance().getNativeStackTrace(handleMessage.getStartTime()));
            }
            if (TextUtils.isEmpty(handleMessage.getStackTrace())) {
                handleMessage.setStackTrace(StackManager.getInstance().getStackTrace(handleMessage.getStartTime()));
            }
            handleMessage.setActionTime(System.currentTimeMillis());
            if (TextUtils.isEmpty(handleMessage.getType())) {
                handleMessage.setType(LAG_TYPE);
            }
            if (!z && !z2) {
                handleMessage2.setActionTime(System.currentTimeMillis());
                add2Queue(handleMessage2);
                mList.clear();
            }
            add2Queue(handleMessage);
        } else if ("END".equals(handleMessage.getType())) {
            if (!z && !z2) {
                handleMessage2.setActionTime(System.currentTimeMillis());
                add2Queue(handleMessage2);
                mList.clear();
            }
            add2Queue(handleMessage);
        } else {
            mList.add(handleMessage);
        }
        this.mPreMessage = handleMessage;
    }

    private JSONArray dumpLooperMsg() {
        JSONArray jSONArray = new JSONArray();
        BlockingQueue<HandleMessage> blockingQueue = mHandleQueue;
        if (blockingQueue != null && blockingQueue.size() > 0) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.mLock) {
                while (!mHandleQueue.isEmpty()) {
                    arrayList.add(mHandleQueue.remove());
                }
            }
            for (int i = 0; i < arrayList.size(); i++) {
                jSONArray.put(((HandleMessage) arrayList.get(i)).parse2Json());
            }
        }
        return jSONArray;
    }

    private JSONArray dumpInputMsg() {
        JSONArray jSONArray = new JSONArray();
        BlockingQueue<HandleMessage> blockingQueue = mInputHandleQueue;
        if (blockingQueue != null && blockingQueue.size() > 0) {
            ArrayList arrayList = new ArrayList();
            synchronized (this.mLock) {
                while (!mInputHandleQueue.isEmpty()) {
                    arrayList.add(mInputHandleQueue.remove());
                }
            }
            for (int i = 0; i < arrayList.size(); i++) {
                jSONArray.put(((HandleMessage) arrayList.get(i)).parse2Json());
            }
        }
        return jSONArray;
    }
}