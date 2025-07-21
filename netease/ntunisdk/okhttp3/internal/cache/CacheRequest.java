package com.netease.ntunisdk.okhttp3.internal.cache;

import com.netease.ntunisdk.okio.Sink;
import java.io.IOException;

/* loaded from: classes5.dex */
public interface CacheRequest {
    void abort();

    Sink body() throws IOException;
}