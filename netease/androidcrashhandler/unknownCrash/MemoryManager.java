package com.netease.androidcrashhandler.unknownCrash;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.os.Process;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.util.LogUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/* loaded from: classes5.dex */
public class MemoryManager {
    private static MemoryInterface memoryInterface;
    private static MemoryManager MANAGER = new MemoryManager();
    private static MemoryStatus status = new MemoryStatus();

    public interface MemoryInterface {
        void onMemoryStateChanged(int i, int i2);
    }

    public static native int memoryAdviceInit(Context context);

    public native int registerMemoryWater();

    static {
        try {
            System.loadLibrary("unitrace_memory_advice");
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static MemoryManager getInstance() {
        return MANAGER;
    }

    public void registerMemoryState(MemoryInterface memoryInterface2) {
        memoryInterface = memoryInterface2;
    }

    public static void onMemoryStateChanged(int i, int i2, long j) {
        MemoryInterface memoryInterface2 = memoryInterface;
        if (memoryInterface2 != null) {
            memoryInterface2.onMemoryStateChanged(i, i2);
        }
    }

    public long getPssMemory() {
        try {
            status.totalPss = Debug.getPss() + Integer.parseInt(((ActivityManager) NTCrashHunterKit.sharedKit().getContext().getSystemService("activity")).getProcessMemoryInfo(new int[]{Process.myPid()})[0].getMemoryStat("summary.graphics"));
            LogUtils.d(LogUtils.TAG, "[MemoryStatus] getFullStatus:" + status.totalPss);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return status.totalPss;
    }

    public MemoryStatus getStatus() {
        try {
            ActivityManager activityManager = (ActivityManager) NTCrashHunterKit.sharedKit().getContext().getSystemService("activity");
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            getSystemTotalMemory(memoryInfo);
            getFreeMem(memoryInfo);
            getPssMemory();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return status;
    }

    private long getSystemTotalMemory(ActivityManager.MemoryInfo memoryInfo) {
        try {
            if (status.systemTotalPss <= 0) {
                if (memoryInfo.totalMem <= 0) {
                    status.systemTotalPss = getTotalMemory();
                } else {
                    status.systemTotalPss = memoryInfo.totalMem / 1024;
                }
                LogUtils.w(LogUtils.TAG, "getSystemTotalMemory======" + status.systemTotalPss + "=======" + memoryInfo.threshold);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return status.systemTotalPss;
    }

    private long getTotalMemory() throws IOException {
        LogUtils.i(LogUtils.TAG, "[MemoryManager] getTotalMemory start");
        long jLongValue = 0;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/proc/meminfo"), 8192);
            jLongValue = Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]).longValue() * 1024;
            bufferedReader.close();
            return jLongValue;
        } catch (IOException e) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getTotalMemory] IOException=" + e.toString());
            e.toString();
            return jLongValue;
        } catch (Exception e2) {
            LogUtils.w(LogUtils.TAG, "DiInfo [getTotalMemory] Exception=" + e2.toString());
            e2.toString();
            return jLongValue;
        }
    }

    private long getFreeMem(ActivityManager.MemoryInfo memoryInfo) {
        try {
            status.systemFreePss = memoryInfo.availMem > 0 ? memoryInfo.availMem / 1024 : -1L;
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return status.systemFreePss;
    }
}