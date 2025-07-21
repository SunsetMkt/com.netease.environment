package com.netease.ntunisdk.okhttp3;

import com.netease.ntunisdk.okhttp3.internal.Util;
import com.netease.ntunisdk.okio.Buffer;
import com.netease.ntunisdk.okio.BufferedSource;
import com.netease.ntunisdk.okio.ByteString;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import org.xbill.DNS.TTL;

/* loaded from: classes2.dex */
public abstract class ResponseBody implements Closeable {

    @Nullable
    private Reader reader;

    /* renamed from: com.netease.ntunisdk.okhttp3.ResponseBody$1 */
    class AnonymousClass1 extends ResponseBody {
        final /* synthetic */ BufferedSource val$content;
        final /* synthetic */ long val$contentLength;

        AnonymousClass1(long j, BufferedSource bufferedSource) {
            j = j;
            bufferedSource = bufferedSource;
        }

        @Override // com.netease.ntunisdk.okhttp3.ResponseBody
        public long contentLength() {
            return j;
        }

        @Override // com.netease.ntunisdk.okhttp3.ResponseBody
        @Nullable
        public MediaType contentType() {
            return mediaType;
        }

        @Override // com.netease.ntunisdk.okhttp3.ResponseBody
        public BufferedSource source() {
            return bufferedSource;
        }
    }

    static final class BomAwareReader extends Reader {
        private final Charset charset;
        private boolean closed;

        @Nullable
        private Reader delegate;
        private final BufferedSource source;

        BomAwareReader(BufferedSource bufferedSource, Charset charset) {
            this.source = bufferedSource;
            this.charset = charset;
        }

        @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.closed = true;
            Reader reader = this.delegate;
            if (reader != null) {
                reader.close();
            } else {
                this.source.close();
            }
        }

        @Override // java.io.Reader
        public int read(char[] cArr, int i, int i2) throws IOException {
            if (this.closed) {
                throw new IOException("Stream closed");
            }
            Reader reader = this.delegate;
            if (reader == null) {
                InputStreamReader inputStreamReader = new InputStreamReader(this.source.inputStream(), Util.bomAwareCharset(this.source, this.charset));
                this.delegate = inputStreamReader;
                reader = inputStreamReader;
            }
            return reader.read(cArr, i, i2);
        }
    }

    private Charset charset() {
        MediaType mediaTypeContentType = contentType();
        return mediaTypeContentType != null ? mediaTypeContentType.charset(Util.UTF_8) : Util.UTF_8;
    }

    public static ResponseBody create(@Nullable MediaType mediaType, long j, BufferedSource bufferedSource) {
        if (bufferedSource != null) {
            return new ResponseBody() { // from class: com.netease.ntunisdk.okhttp3.ResponseBody.1
                final /* synthetic */ BufferedSource val$content;
                final /* synthetic */ long val$contentLength;

                AnonymousClass1(long j2, BufferedSource bufferedSource2) {
                    j = j2;
                    bufferedSource = bufferedSource2;
                }

                @Override // com.netease.ntunisdk.okhttp3.ResponseBody
                public long contentLength() {
                    return j;
                }

                @Override // com.netease.ntunisdk.okhttp3.ResponseBody
                @Nullable
                public MediaType contentType() {
                    return mediaType;
                }

                @Override // com.netease.ntunisdk.okhttp3.ResponseBody
                public BufferedSource source() {
                    return bufferedSource;
                }
            };
        }
        throw new NullPointerException("source == null");
    }

    public static ResponseBody create(@Nullable MediaType mediaType, ByteString byteString) {
        return create(mediaType, byteString.size(), new Buffer().write(byteString));
    }

    public static ResponseBody create(@Nullable MediaType mediaType, String str) {
        Charset charset = Util.UTF_8;
        if (mediaType != null && (charset = mediaType.charset()) == null) {
            charset = Util.UTF_8;
            mediaType = MediaType.parse(mediaType + "; charset=utf-8");
        }
        Buffer bufferWriteString = new Buffer().writeString(str, charset);
        return create(mediaType, bufferWriteString.size(), bufferWriteString);
    }

    public static ResponseBody create(@Nullable MediaType mediaType, byte[] bArr) {
        return create(mediaType, bArr.length, new Buffer().write(bArr));
    }

    public final InputStream byteStream() {
        return source().inputStream();
    }

    public final byte[] bytes() throws IOException {
        long jContentLength = contentLength();
        if (jContentLength > TTL.MAX_VALUE) {
            throw new IOException("Cannot buffer entire body for content length: " + jContentLength);
        }
        BufferedSource bufferedSourceSource = source();
        try {
            byte[] byteArray = bufferedSourceSource.readByteArray();
            Util.closeQuietly(bufferedSourceSource);
            if (jContentLength == -1 || jContentLength == byteArray.length) {
                return byteArray;
            }
            throw new IOException("Content-Length (" + jContentLength + ") and stream length (" + byteArray.length + ") disagree");
        } catch (Throwable th) {
            Util.closeQuietly(bufferedSourceSource);
            throw th;
        }
    }

    public final Reader charStream() {
        Reader reader = this.reader;
        if (reader != null) {
            return reader;
        }
        BomAwareReader bomAwareReader = new BomAwareReader(source(), charset());
        this.reader = bomAwareReader;
        return bomAwareReader;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Util.closeQuietly(source());
    }

    public abstract long contentLength();

    @Nullable
    public abstract MediaType contentType();

    public abstract BufferedSource source();

    public final String string() throws IOException {
        BufferedSource bufferedSourceSource = source();
        try {
            return bufferedSourceSource.readString(Util.bomAwareCharset(bufferedSourceSource, charset()));
        } finally {
            Util.closeQuietly(bufferedSourceSource);
        }
    }
}