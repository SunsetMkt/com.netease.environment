package com.netease.vrlib.common;

import android.os.Looper;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/* loaded from: classes5.dex */
public class MDGLHandler {
    private boolean died;
    private Queue<Runnable> mAddQueue = new LinkedBlockingQueue();
    private Queue<Runnable> mWorkQueue = new LinkedBlockingQueue();
    private final Object addLock = new Object();

    public void post(Runnable runnable) {
        if (this.died || runnable == null) {
            return;
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            synchronized (this.addLock) {
                this.mAddQueue.remove(runnable);
                this.mAddQueue.offer(runnable);
            }
            return;
        }
        runnable.run();
    }

    public void dealMessage() {
        synchronized (this.addLock) {
            this.mWorkQueue.addAll(this.mAddQueue);
            this.mAddQueue.clear();
        }
        while (this.mWorkQueue.size() > 0) {
            this.mWorkQueue.poll().run();
        }
    }

    public void destroy() {
        this.died = true;
    }
}