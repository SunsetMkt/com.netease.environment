package com.netease.ntunisdk.okio;

import java.io.IOException;

/* loaded from: classes3.dex */
public abstract class ForwardingSink implements Sink {
    private final Sink delegate;

    public ForwardingSink(Sink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("delegate == null");
        }
        this.delegate = sink;
    }

    public final Sink delegate() {
        return this.delegate;
    }

    @Override // com.netease.ntunisdk.okio.Sink
    public void write(Buffer buffer, long j) throws IOException {
        this.delegate.write(buffer, j);
    }

    @Override // com.netease.ntunisdk.okio.Sink, java.io.Flushable
    public void flush() throws IOException {
        this.delegate.flush();
    }

    @Override // com.netease.ntunisdk.okio.Sink
    public Timeout timeout() {
        return this.delegate.timeout();
    }

    @Override // com.netease.ntunisdk.okio.Sink, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.delegate.close();
    }

    public String toString() {
        return String.valueOf(getClass().getSimpleName()) + "(" + this.delegate.toString() + ")";
    }
}