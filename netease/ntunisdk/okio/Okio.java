package com.netease.ntunisdk.okio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

/* loaded from: classes3.dex */
public final class Okio {
    static final Logger logger = Logger.getLogger(Okio.class.getName());

    private Okio() {
    }

    public static BufferedSource buffer(Source source) {
        return new RealBufferedSource(source);
    }

    public static BufferedSink buffer(Sink sink) {
        return new RealBufferedSink(sink);
    }

    public static Sink sink(OutputStream outputStream) {
        return sink(outputStream, new Timeout());
    }

    private static Sink sink(final OutputStream outputStream, final Timeout timeout) {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        if (timeout == null) {
            throw new IllegalArgumentException("timeout == null");
        }
        return new Sink() { // from class: com.netease.ntunisdk.okio.Okio.1
            @Override // com.netease.ntunisdk.okio.Sink
            public void write(Buffer buffer, long j) throws IOException {
                Util.checkOffsetAndCount(buffer.size, 0L, j);
                while (j > 0) {
                    timeout.throwIfReached();
                    Segment segment = buffer.head;
                    int iMin = (int) Math.min(j, segment.limit - segment.pos);
                    outputStream.write(segment.data, segment.pos, iMin);
                    segment.pos += iMin;
                    long j2 = iMin;
                    j -= j2;
                    buffer.size -= j2;
                    if (segment.pos == segment.limit) {
                        buffer.head = segment.pop();
                        SegmentPool.recycle(segment);
                    }
                }
            }

            @Override // com.netease.ntunisdk.okio.Sink, java.io.Flushable
            public void flush() throws IOException {
                outputStream.flush();
            }

            @Override // com.netease.ntunisdk.okio.Sink, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                outputStream.close();
            }

            @Override // com.netease.ntunisdk.okio.Sink
            public Timeout timeout() {
                return timeout;
            }

            public String toString() {
                return "sink(" + outputStream + ")";
            }
        };
    }

    public static Sink sink(Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        if (socket.getOutputStream() == null) {
            throw new IOException("socket's output stream == null");
        }
        AsyncTimeout asyncTimeoutTimeout = timeout(socket);
        return asyncTimeoutTimeout.sink(sink(socket.getOutputStream(), asyncTimeoutTimeout));
    }

    public static Source source(InputStream inputStream) {
        return source(inputStream, new Timeout());
    }

    private static Source source(final InputStream inputStream, final Timeout timeout) {
        if (inputStream == null) {
            throw new IllegalArgumentException("in == null");
        }
        if (timeout == null) {
            throw new IllegalArgumentException("timeout == null");
        }
        return new Source() { // from class: com.netease.ntunisdk.okio.Okio.2
            @Override // com.netease.ntunisdk.okio.Source
            public long read(Buffer buffer, long j) throws IOException {
                if (j < 0) {
                    throw new IllegalArgumentException("byteCount < 0: " + j);
                }
                if (j == 0) {
                    return 0L;
                }
                try {
                    timeout.throwIfReached();
                    Segment segmentWritableSegment = buffer.writableSegment(1);
                    int i = inputStream.read(segmentWritableSegment.data, segmentWritableSegment.limit, (int) Math.min(j, 8192 - segmentWritableSegment.limit));
                    if (i == -1) {
                        return -1L;
                    }
                    segmentWritableSegment.limit += i;
                    long j2 = i;
                    buffer.size += j2;
                    return j2;
                } catch (AssertionError e) {
                    if (Okio.isAndroidGetsocknameError(e)) {
                        throw new IOException(e);
                    }
                    throw e;
                }
            }

            @Override // com.netease.ntunisdk.okio.Source, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
                inputStream.close();
            }

            @Override // com.netease.ntunisdk.okio.Source
            public Timeout timeout() {
                return timeout;
            }

            public String toString() {
                return "source(" + inputStream + ")";
            }
        };
    }

    public static Source source(File file) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return source(new FileInputStream(file));
    }

    public static Source source(Path path, OpenOption... openOptionArr) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }
        return source(Files.newInputStream(path, openOptionArr));
    }

    public static Sink sink(File file) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return sink(new FileOutputStream(file));
    }

    public static Sink appendingSink(File file) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        return sink(new FileOutputStream(file, true));
    }

    public static Sink sink(Path path, OpenOption... openOptionArr) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }
        return sink(Files.newOutputStream(path, openOptionArr));
    }

    public static Sink blackhole() {
        return new Sink() { // from class: com.netease.ntunisdk.okio.Okio.3
            @Override // com.netease.ntunisdk.okio.Sink, java.io.Closeable, java.lang.AutoCloseable
            public void close() throws IOException {
            }

            @Override // com.netease.ntunisdk.okio.Sink, java.io.Flushable
            public void flush() throws IOException {
            }

            @Override // com.netease.ntunisdk.okio.Sink
            public void write(Buffer buffer, long j) throws IOException {
                buffer.skip(j);
            }

            @Override // com.netease.ntunisdk.okio.Sink
            public Timeout timeout() {
                return Timeout.NONE;
            }
        };
    }

    public static Source source(Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("socket == null");
        }
        if (socket.getInputStream() == null) {
            throw new IOException("socket's input stream == null");
        }
        AsyncTimeout asyncTimeoutTimeout = timeout(socket);
        return asyncTimeoutTimeout.source(source(socket.getInputStream(), asyncTimeoutTimeout));
    }

    private static AsyncTimeout timeout(final Socket socket) {
        return new AsyncTimeout() { // from class: com.netease.ntunisdk.okio.Okio.4
            @Override // com.netease.ntunisdk.okio.AsyncTimeout
            protected IOException newTimeoutException(@Nullable IOException iOException) {
                SocketTimeoutException socketTimeoutException = new SocketTimeoutException("timeout");
                if (iOException != null) {
                    socketTimeoutException.initCause(iOException);
                }
                return socketTimeoutException;
            }

            @Override // com.netease.ntunisdk.okio.AsyncTimeout
            protected void timedOut() throws IOException {
                try {
                    socket.close();
                } catch (AssertionError e) {
                    if (Okio.isAndroidGetsocknameError(e)) {
                        Okio.logger.log(Level.WARNING, "Failed to close timed out socket " + socket, (Throwable) e);
                        return;
                    }
                    throw e;
                } catch (Exception e2) {
                    Okio.logger.log(Level.WARNING, "Failed to close timed out socket " + socket, (Throwable) e2);
                }
            }
        };
    }

    static boolean isAndroidGetsocknameError(AssertionError assertionError) {
        return (assertionError.getCause() == null || assertionError.getMessage() == null || !assertionError.getMessage().contains("getsockname failed")) ? false : true;
    }
}