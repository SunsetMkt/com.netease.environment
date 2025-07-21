package com.netease.vrlib.common;

import android.util.Log;

/* loaded from: classes5.dex */
public class Fps {
    private static final String TAG = "fps";
    private int mFrameCount;
    private long mLastTimestamp;

    public void step() {
        if (this.mFrameCount % 120 == 0) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (this.mLastTimestamp != 0) {
                Log.w(TAG, "fps:" + ((this.mFrameCount * 1000.0f) / (jCurrentTimeMillis - r2)));
            }
            this.mFrameCount = 0;
            this.mLastTimestamp = jCurrentTimeMillis;
        }
        this.mFrameCount++;
    }
}