package com.netease.cloud.nos.android.http;

import android.content.Context;
import com.netease.cloud.nos.android.constants.Code;
import com.netease.cloud.nos.android.constants.Constants;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.cloud.nos.android.utils.Util;
import java.io.IOException;
import java.util.concurrent.Callable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/* loaded from: classes6.dex */
public class HttpPostTask implements Callable<HttpResult> {
    private static final String LOGTAG = LogUtil.makeLogTag(HttpPostTask.class);
    protected final byte[] chunkData;
    protected final Context ctx;
    protected volatile HttpPost postRequest;
    protected final String token;
    protected final String url;

    public HttpPostTask(String str, String str2, Context context, byte[] bArr) {
        this.url = str;
        this.token = str2;
        this.ctx = context;
        this.chunkData = bArr;
    }

    private HttpEntity buildHttpEntity(byte[] bArr) throws IOException {
        return new ByteArrayEntity(bArr);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public HttpResult call() throws Exception {
        HttpResult httpResult;
        String str;
        String str2 = LOGTAG;
        LogUtil.d(str2, "http post task is executing");
        try {
            this.postRequest = Util.newPost(this.url);
            this.postRequest.addHeader(Constants.HEADER_TOKEN, this.token);
            this.postRequest.setEntity(buildHttpEntity(this.chunkData));
            HttpResponse httpResponseExecute = Util.getHttpClient(this.ctx).execute(this.postRequest);
            if (httpResponseExecute == null || httpResponseExecute.getStatusLine() == null || httpResponseExecute.getEntity() == null) {
                httpResult = new HttpResult(Code.HTTP_NO_RESPONSE, null, null);
            } else {
                int statusCode = httpResponseExecute.getStatusLine().getStatusCode();
                String string = EntityUtils.toString(httpResponseExecute.getEntity());
                if (statusCode == 200) {
                    str = "http post response is correct, response: " + string;
                } else {
                    new HttpResult(statusCode, null, null);
                    LogUtil.d(str2, "http post response is failed, status code: " + statusCode);
                    if (httpResponseExecute.getEntity() != null) {
                        str = "http post response is failed, result: " + string;
                    }
                    httpResult = new HttpResult(statusCode, new JSONObject(string), null);
                }
                LogUtil.d(str2, str);
                httpResult = new HttpResult(statusCode, new JSONObject(string), null);
            }
            return httpResult;
        } catch (Exception e) {
            LogUtil.e(LOGTAG, "http post exception", e);
            return new HttpResult(Code.HTTP_EXCEPTION, null, e);
        } finally {
            this.postRequest = null;
        }
    }
}