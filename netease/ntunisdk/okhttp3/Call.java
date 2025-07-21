package com.netease.ntunisdk.okhttp3;

import com.netease.ntunisdk.okio.Timeout;
import java.io.IOException;

/* loaded from: classes2.dex */
public interface Call extends Cloneable {

    public interface Factory {
        Call newCall(Request request);
    }

    void cancel();

    Call clone();

    void enqueue(Callback callback);

    Response execute() throws IOException;

    boolean isCanceled();

    boolean isExecuted();

    Request request();

    Timeout timeout();
}