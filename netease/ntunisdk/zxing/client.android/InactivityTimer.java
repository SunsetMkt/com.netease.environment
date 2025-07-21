package com.netease.ntunisdk.zxing.client.android;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

/* loaded from: classes.dex */
final class InactivityTimer {
    private static final long INACTIVITY_DELAY_MS = 300000;
    private static final String TAG = "UniQR timer";
    private final Activity activity;
    private AsyncTask<Object, Object, Object> inactivityTask;

    InactivityTimer(Activity activity) {
        this.activity = activity;
        onActivity();
    }

    synchronized void onActivity() {
        cancel();
        this.inactivityTask = new InactivityAsyncTask();
        if (Build.VERSION.SDK_INT >= 11) {
            this.inactivityTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
        } else {
            this.inactivityTask.execute(new Object[0]);
        }
    }

    private synchronized void cancel() {
        AsyncTask<Object, Object, Object> asyncTask = this.inactivityTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
            this.inactivityTask = null;
        }
    }

    void shutdown() {
        cancel();
    }

    private final class InactivityAsyncTask extends AsyncTask<Object, Object, Object> {
        private InactivityAsyncTask() {
        }

        @Override // android.os.AsyncTask
        protected Object doInBackground(Object... objArr) throws InterruptedException {
            try {
                Thread.sleep(300000L);
                Log.i(InactivityTimer.TAG, "Finishing activity due to inactivity");
                InactivityTimer.this.activity.finish();
                return null;
            } catch (InterruptedException unused) {
                return null;
            }
        }
    }
}