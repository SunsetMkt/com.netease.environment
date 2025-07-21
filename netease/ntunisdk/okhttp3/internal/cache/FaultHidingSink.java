package com.netease.ntunisdk.okhttp3.internal.cache;

import com.netease.ntunisdk.okio.Buffer;
import com.netease.ntunisdk.okio.ForwardingSink;
import com.netease.ntunisdk.okio.Sink;
import java.io.IOException;

/* loaded from: classes5.dex */
class FaultHidingSink extends ForwardingSink {
    private boolean hasErrors;

    FaultHidingSink(Sink sink) {
        super(sink);
    }

    @Override // com.netease.ntunisdk.okio.ForwardingSink, com.netease.ntunisdk.okio.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.hasErrors) {
            return;
        }
        try {
            super.close();
        } catch (IOException e) {
            this.hasErrors = true;
            onException(e);
        }
    }

    @Override // com.netease.ntunisdk.okio.ForwardingSink, com.netease.ntunisdk.okio.Sink, java.io.Flushable
    public void flush() throws IOException {
        if (this.hasErrors) {
            return;
        }
        try {
            super.flush();
        } catch (IOException e) {
            this.hasErrors = true;
            onException(e);
        }
    }

    protected void onException(IOException iOException) {
    }

    @Override // com.netease.ntunisdk.okio.ForwardingSink, com.netease.ntunisdk.okio.Sink
    public void write(Buffer buffer, long j) throws IOException {
        if (this.hasErrors) {
            buffer.skip(j);
            return;
        }
        try {
            super.write(buffer, j);
        } catch (IOException e) {
            this.hasErrors = true;
            onException(e);
        }
    }
}