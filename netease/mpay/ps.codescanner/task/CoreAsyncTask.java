package com.netease.mpay.ps.codescanner.task;

import android.os.AsyncTask;
import android.os.Build;
import com.netease.mpay.ps.codescanner.widget.ThreadPool;

/* loaded from: classes5.dex */
public abstract class CoreAsyncTask<Progress, T> extends AsyncTask<Void, Progress, T> {
    public void doExecute() {
        if (Build.VERSION.SDK_INT >= 11) {
            super.executeOnExecutor(ThreadPool.getAsyncTaskThreadPoolInstance(), new Void[0]);
        } else {
            super.execute(new Void[0]);
        }
    }
}