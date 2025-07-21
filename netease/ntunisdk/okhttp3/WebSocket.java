package com.netease.ntunisdk.okhttp3;

import com.netease.ntunisdk.okio.ByteString;
import javax.annotation.Nullable;

/* loaded from: classes2.dex */
public interface WebSocket {

    public interface Factory {
        WebSocket newWebSocket(Request request, WebSocketListener webSocketListener);
    }

    void cancel();

    boolean close(int i, @Nullable String str);

    long queueSize();

    Request request();

    boolean send(ByteString byteString);

    boolean send(String str);
}