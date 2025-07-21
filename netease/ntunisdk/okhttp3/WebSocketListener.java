package com.netease.ntunisdk.okhttp3;

import com.netease.ntunisdk.okio.ByteString;
import javax.annotation.Nullable;

/* loaded from: classes2.dex */
public abstract class WebSocketListener {
    public void onClosed(WebSocket webSocket, int i, String str) {
    }

    public void onClosing(WebSocket webSocket, int i, String str) {
    }

    public void onFailure(WebSocket webSocket, Throwable th, @Nullable Response response) {
    }

    public void onMessage(WebSocket webSocket, ByteString byteString) {
    }

    public void onMessage(WebSocket webSocket, String str) {
    }

    public void onOpen(WebSocket webSocket, Response response) {
    }
}