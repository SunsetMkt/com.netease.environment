package com.netease.ntunisdk.okhttp3.internal.http;

import com.netease.ntunisdk.okhttp3.MediaType;
import com.netease.ntunisdk.okhttp3.ResponseBody;
import com.netease.ntunisdk.okio.BufferedSource;
import javax.annotation.Nullable;

/* loaded from: classes5.dex */
public final class RealResponseBody extends ResponseBody {
    private final long contentLength;

    @Nullable
    private final String contentTypeString;
    private final BufferedSource source;

    public RealResponseBody(@Nullable String str, long j, BufferedSource bufferedSource) {
        this.contentTypeString = str;
        this.contentLength = j;
        this.source = bufferedSource;
    }

    @Override // com.netease.ntunisdk.okhttp3.ResponseBody
    public long contentLength() {
        return this.contentLength;
    }

    @Override // com.netease.ntunisdk.okhttp3.ResponseBody
    public MediaType contentType() {
        String str = this.contentTypeString;
        if (str != null) {
            return MediaType.parse(str);
        }
        return null;
    }

    @Override // com.netease.ntunisdk.okhttp3.ResponseBody
    public BufferedSource source() {
        return this.source;
    }
}