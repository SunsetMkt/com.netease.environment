package com.netease.ntunisdk.unilogger.zip;

import com.netease.ntunisdk.unilogger.utils.LogUtils;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/* loaded from: classes5.dex */
public class ZipCore {
    private static ZipCore zipCore;
    private BlockingQueue<ZipUnit> zipQueue = new ArrayBlockingQueue(20);
    private boolean isStart = false;

    private ZipCore() {
    }

    public static ZipCore getInstance() {
        if (zipCore == null) {
            zipCore = new ZipCore();
        }
        return zipCore;
    }

    public boolean addZipUnitToQueue(ZipUnit zipUnit) {
        LogUtils.v_inner("ZipCore [addZipUnitToQueue] start");
        try {
            BlockingQueue<ZipUnit> blockingQueue = this.zipQueue;
            if (blockingQueue != null) {
                return blockingQueue.add(zipUnit);
            }
            return false;
        } catch (Exception e) {
            LogUtils.w_inner("ZipCore [addZipUnitToQueue] Exception = " + e.toString());
            e.printStackTrace();
            return false;
        }
    }

    public synchronized void start() {
        LogUtils.v_inner("ZipCore [start] start, isStart=" + this.isStart);
        if (!this.isStart) {
            this.isStart = true;
            new Thread(new Runnable() { // from class: com.netease.ntunisdk.unilogger.zip.ZipCore.1
                @Override // java.lang.Runnable
                public void run() {
                    while (true) {
                        try {
                            ZipUnit zipUnit = (ZipUnit) ZipCore.this.zipQueue.take();
                            if (zipUnit == null) {
                                return;
                            } else {
                                zipUnit.zip();
                            }
                        } catch (Exception e) {
                            LogUtils.w_inner("ZipCore [start] Exception = " + e.toString());
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            }, "ZipCore").start();
        }
    }
}