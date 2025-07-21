package com.netease.androidcrashhandler.anr;

import android.os.Looper;
import com.netease.androidcrashhandler.anr.ANRError$$;
import com.netease.androidcrashhandler.util.LogUtils;
import java.text.MessageFormat;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: classes4.dex */
public class ANRError extends Error {
    private static final long serialVersionUID = 1;
    public final long duration;

    private ANRError(ANRError$$._Thread _thread, long j) {
        super("Application Not Responding for at least " + j + " ms.", _thread);
        this.duration = j;
    }

    @Override // java.lang.Throwable
    public Throwable fillInStackTrace() {
        setStackTrace(new StackTraceElement[0]);
        return this;
    }

    public static String allStackTrace() {
        LogUtils.i(LogUtils.TAG, "ANRError [allStackTrace] start");
        final Thread thread = Looper.getMainLooper().getThread();
        TreeMap treeMap = new TreeMap(new Comparator<Thread>() { // from class: com.netease.androidcrashhandler.anr.ANRError.1
            @Override // java.util.Comparator
            public int compare(Thread thread2, Thread thread3) {
                if (thread2 == thread3) {
                    return 0;
                }
                return (thread2.getName().equals(thread.getName()) || thread3.getName().equals(thread.getName())) ? -1 : 1;
            }
        });
        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        if (!treeMap.containsKey(thread)) {
            treeMap.put(thread, thread.getStackTrace());
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry entry2 : treeMap.entrySet()) {
            sb.append("Caused by: com.netease.androidcrashhandler.anr.ANRError$$$_Thread: ");
            sb.append(getThreadTitle((Thread) entry2.getKey()) + "\n");
            StackTraceElement[] stackTraceElementArr = (StackTraceElement[]) entry2.getValue();
            int length = stackTraceElementArr.length;
            for (int i = 0; i < length; i++) {
                StackTraceElement stackTraceElement = stackTraceElementArr[i];
                if (stackTraceElement.getLineNumber() < 0) {
                    sb.append(MessageFormat.format("\tat {0}.{1}(Native Method)\n", stackTraceElement.getClassName(), stackTraceElement.getMethodName()));
                } else {
                    sb.append(MessageFormat.format("\tat {0}.{1}({2}:{3})\n", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), stackTraceElement.getFileName(), String.valueOf(stackTraceElement.getLineNumber())));
                }
            }
        }
        LogUtils.i(LogUtils.TAG, "ANRError [allStackTrace] end");
        return sb.toString();
    }

    static ANRError New(long j, String str, boolean z) {
        final Thread thread = Looper.getMainLooper().getThread();
        TreeMap treeMap = new TreeMap(new Comparator<Thread>() { // from class: com.netease.androidcrashhandler.anr.ANRError.2
            @Override // java.util.Comparator
            public int compare(Thread thread2, Thread thread3) {
                if (thread2 == thread3) {
                    return 0;
                }
                Thread thread4 = thread;
                if (thread2 == thread4) {
                    return 1;
                }
                if (thread3 == thread4) {
                    return -1;
                }
                return thread3.getName().compareTo(thread2.getName());
            }
        });
        for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
            if (entry.getKey() == thread || (entry.getKey().getName().startsWith(str) && (z || entry.getValue().length > 0))) {
                treeMap.put(entry.getKey(), entry.getValue());
            }
        }
        if (!treeMap.containsKey(thread)) {
            treeMap.put(thread, thread.getStackTrace());
        }
        ANRError$$._Thread _thread = null;
        for (Map.Entry entry2 : treeMap.entrySet()) {
            _thread = new ANRError$$._Thread(_thread);
        }
        return new ANRError(_thread, j);
    }

    static ANRError NewMainOnly(long j) {
        Thread thread = Looper.getMainLooper().getThread();
        return new ANRError(new ANRError$$._Thread(null), j);
    }

    private static String getThreadTitle(Thread thread) {
        return thread.getName() + " (state = " + thread.getState() + ")";
    }
}