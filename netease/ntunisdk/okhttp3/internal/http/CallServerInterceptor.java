package com.netease.ntunisdk.okhttp3.internal.http;

import com.CCMsgSdk.ControlCmdType;
import com.netease.ntunisdk.okhttp3.Interceptor;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.Response;
import com.netease.ntunisdk.okhttp3.ResponseBody;
import com.netease.ntunisdk.okhttp3.internal.Util;
import com.netease.ntunisdk.okhttp3.internal.connection.RealConnection;
import com.netease.ntunisdk.okhttp3.internal.connection.StreamAllocation;
import com.netease.ntunisdk.okio.Buffer;
import com.netease.ntunisdk.okio.BufferedSink;
import com.netease.ntunisdk.okio.ForwardingSink;
import com.netease.ntunisdk.okio.Okio;
import com.netease.ntunisdk.okio.Sink;
import java.io.IOException;
import java.net.ProtocolException;

/* loaded from: classes5.dex */
public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    static final class CountingSink extends ForwardingSink {
        long successfulCount;

        CountingSink(Sink sink) {
            super(sink);
        }

        @Override // com.netease.ntunisdk.okio.ForwardingSink, com.netease.ntunisdk.okio.Sink
        public void write(Buffer buffer, long j) throws IOException {
            super.write(buffer, j);
            this.successfulCount += j;
        }
    }

    public CallServerInterceptor(boolean z) {
        this.forWebSocket = z;
    }

    @Override // com.netease.ntunisdk.okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Response.Builder builderNewBuilder;
        ResponseBody responseBodyOpenResponseBody;
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        HttpCodec httpCodecHttpStream = realInterceptorChain.httpStream();
        StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
        RealConnection realConnection = (RealConnection) realInterceptorChain.connection();
        Request request = realInterceptorChain.request();
        long jCurrentTimeMillis = System.currentTimeMillis();
        realInterceptorChain.eventListener().requestHeadersStart(realInterceptorChain.call());
        httpCodecHttpStream.writeRequestHeaders(request);
        realInterceptorChain.eventListener().requestHeadersEnd(realInterceptorChain.call(), request);
        Response.Builder responseHeaders = null;
        if (HttpMethod.permitsRequestBody(request.method()) && request.body() != null) {
            if ("100-continue".equalsIgnoreCase(request.header("Expect"))) {
                httpCodecHttpStream.flushRequest();
                realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
                responseHeaders = httpCodecHttpStream.readResponseHeaders(true);
            }
            if (responseHeaders == null) {
                realInterceptorChain.eventListener().requestBodyStart(realInterceptorChain.call());
                CountingSink countingSink = new CountingSink(httpCodecHttpStream.createRequestBody(request, request.body().contentLength()));
                BufferedSink bufferedSinkBuffer = Okio.buffer(countingSink);
                request.body().writeTo(bufferedSinkBuffer);
                bufferedSinkBuffer.close();
                realInterceptorChain.eventListener().requestBodyEnd(realInterceptorChain.call(), countingSink.successfulCount);
            } else if (!realConnection.isMultiplexed()) {
                streamAllocation.noNewStreams();
            }
        }
        httpCodecHttpStream.finishRequest();
        if (responseHeaders == null) {
            realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
            responseHeaders = httpCodecHttpStream.readResponseHeaders(false);
        }
        Response responseBuild = responseHeaders.request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(jCurrentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int iCode = responseBuild.code();
        if (iCode == 100) {
            responseBuild = httpCodecHttpStream.readResponseHeaders(false).request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(jCurrentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
            iCode = responseBuild.code();
        }
        realInterceptorChain.eventListener().responseHeadersEnd(realInterceptorChain.call(), responseBuild);
        if (this.forWebSocket && iCode == 101) {
            builderNewBuilder = responseBuild.newBuilder();
            responseBodyOpenResponseBody = Util.EMPTY_RESPONSE;
        } else {
            builderNewBuilder = responseBuild.newBuilder();
            responseBodyOpenResponseBody = httpCodecHttpStream.openResponseBody(responseBuild);
        }
        Response responseBuild2 = builderNewBuilder.body(responseBodyOpenResponseBody).build();
        if (ControlCmdType.CLOSE.equalsIgnoreCase(responseBuild2.request().header("Connection")) || ControlCmdType.CLOSE.equalsIgnoreCase(responseBuild2.header("Connection"))) {
            streamAllocation.noNewStreams();
        }
        if ((iCode != 204 && iCode != 205) || responseBuild2.body().contentLength() <= 0) {
            return responseBuild2;
        }
        throw new ProtocolException("HTTP " + iCode + " had non-zero Content-Length: " + responseBuild2.body().contentLength());
    }
}