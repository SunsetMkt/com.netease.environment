package com.netease.cloud.nos.android.http;

import android.content.Context;
import com.netease.cloud.nos.android.core.RequestCallback;
import com.netease.cloud.nos.android.utils.LogUtil;
import java.util.Map;
import org.apache.http.client.methods.HttpGet;

/* loaded from: classes6.dex */
public class HttpGetTask implements Runnable {
    private static final String LOGTAG = LogUtil.makeLogTag(HttpGetTask.class);
    protected final RequestCallback callback;
    protected final Context ctx;
    protected volatile HttpGet getRequest;
    protected final Map<String, String> map;
    protected final String url;

    public HttpGetTask(String str, Context context, Map<String, String> map, RequestCallback requestCallback) {
        this.url = str;
        this.ctx = context;
        this.map = map;
        this.callback = requestCallback;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0084 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00bb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v8, types: [org.apache.http.client.HttpClient] */
    /* JADX WARN: Type inference failed for: r4v1 */
    /* JADX WARN: Type inference failed for: r4v4, types: [org.apache.http.HttpEntity] */
    /* JADX WARN: Type inference failed for: r4v5, types: [org.apache.http.client.methods.HttpGet, org.apache.http.client.methods.HttpUriRequest] */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() throws java.lang.Throwable {
        /*
            r9 = this;
            java.lang.String r0 = "Consume Content exception"
            java.lang.String r1 = "http get response is correct, response: "
            r2 = 0
            java.lang.String r3 = r9.url     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            org.apache.http.client.methods.HttpGet r3 = com.netease.cloud.nos.android.utils.Util.newGet(r3)     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            r9.getRequest = r3     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            java.util.Map<java.lang.String, java.lang.String> r3 = r9.map     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            if (r3 == 0) goto L1d
            org.apache.http.client.methods.HttpGet r3 = r9.getRequest     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            java.util.Map<java.lang.String, java.lang.String> r4 = r9.map     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            org.apache.http.client.methods.HttpRequestBase r3 = com.netease.cloud.nos.android.utils.Util.setHeader(r3, r4)     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            org.apache.http.client.methods.HttpGet r3 = (org.apache.http.client.methods.HttpGet) r3     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            r9.getRequest = r3     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
        L1d:
            android.content.Context r3 = r9.ctx     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            org.apache.http.client.HttpClient r3 = com.netease.cloud.nos.android.utils.Util.getLbsHttpClient(r3)     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            org.apache.http.client.methods.HttpGet r4 = r9.getRequest     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            org.apache.http.HttpResponse r3 = r3.execute(r4)     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            if (r3 == 0) goto L70
            org.apache.http.StatusLine r4 = r3.getStatusLine()     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            if (r4 == 0) goto L70
            org.apache.http.HttpEntity r4 = r3.getEntity()     // Catch: java.lang.Throwable -> L8c java.lang.Exception -> L8f
            if (r4 == 0) goto L71
            org.apache.http.StatusLine r3 = r3.getStatusLine()     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            int r3 = r3.getStatusCode()     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            java.lang.String r5 = org.apache.http.util.EntityUtils.toString(r4)     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            r6.<init>(r5)     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            r7 = 200(0xc8, float:2.8E-43)
            if (r3 != r7) goto L5e
            java.lang.String r7 = com.netease.cloud.nos.android.http.HttpGetTask.LOGTAG     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            r8.<init>(r1)     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            r8.append(r5)     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            java.lang.String r1 = r8.toString()     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            com.netease.cloud.nos.android.utils.LogUtil.d(r7, r1)     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            goto L65
        L5e:
            java.lang.String r1 = com.netease.cloud.nos.android.http.HttpGetTask.LOGTAG     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            java.lang.String r5 = "http get response is failed."
            com.netease.cloud.nos.android.utils.LogUtil.d(r1, r5)     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
        L65:
            com.netease.cloud.nos.android.core.RequestCallback r1 = r9.callback     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            com.netease.cloud.nos.android.http.HttpResult r5 = new com.netease.cloud.nos.android.http.HttpResult     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            r5.<init>(r3, r6, r2)     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            r1.onResult(r5)     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            goto L82
        L70:
            r4 = r2
        L71:
            com.netease.cloud.nos.android.core.RequestCallback r1 = r9.callback     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            com.netease.cloud.nos.android.http.HttpResult r3 = new com.netease.cloud.nos.android.http.HttpResult     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            org.json.JSONObject r5 = new org.json.JSONObject     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            r5.<init>()     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            r6 = 899(0x383, float:1.26E-42)
            r3.<init>(r6, r5, r2)     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
            r1.onResult(r3)     // Catch: java.lang.Exception -> L8a java.lang.Throwable -> Lb8
        L82:
            if (r4 == 0) goto Lb5
            r4.consumeContent()     // Catch: java.io.IOException -> L88
            goto Lb5
        L88:
            r1 = move-exception
            goto Lb0
        L8a:
            r1 = move-exception
            goto L91
        L8c:
            r1 = move-exception
            r4 = r2
            goto Lb9
        L8f:
            r1 = move-exception
            r4 = r2
        L91:
            java.lang.String r3 = com.netease.cloud.nos.android.http.HttpGetTask.LOGTAG     // Catch: java.lang.Throwable -> Lb8
            java.lang.String r5 = "http get task exception"
            com.netease.cloud.nos.android.utils.LogUtil.e(r3, r5, r1)     // Catch: java.lang.Throwable -> Lb8
            com.netease.cloud.nos.android.core.RequestCallback r3 = r9.callback     // Catch: java.lang.Throwable -> Lb8
            com.netease.cloud.nos.android.http.HttpResult r5 = new com.netease.cloud.nos.android.http.HttpResult     // Catch: java.lang.Throwable -> Lb8
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch: java.lang.Throwable -> Lb8
            r6.<init>()     // Catch: java.lang.Throwable -> Lb8
            r7 = 799(0x31f, float:1.12E-42)
            r5.<init>(r7, r6, r1)     // Catch: java.lang.Throwable -> Lb8
            r3.onResult(r5)     // Catch: java.lang.Throwable -> Lb8
            if (r4 == 0) goto Lb5
            r4.consumeContent()     // Catch: java.io.IOException -> Laf
            goto Lb5
        Laf:
            r1 = move-exception
        Lb0:
            java.lang.String r3 = com.netease.cloud.nos.android.http.HttpGetTask.LOGTAG
            com.netease.cloud.nos.android.utils.LogUtil.e(r3, r0, r1)
        Lb5:
            r9.getRequest = r2
            return
        Lb8:
            r1 = move-exception
        Lb9:
            if (r4 == 0) goto Lc5
            r4.consumeContent()     // Catch: java.io.IOException -> Lbf
            goto Lc5
        Lbf:
            r3 = move-exception
            java.lang.String r4 = com.netease.cloud.nos.android.http.HttpGetTask.LOGTAG
            com.netease.cloud.nos.android.utils.LogUtil.e(r4, r0, r3)
        Lc5:
            r9.getRequest = r2
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.cloud.nos.android.http.HttpGetTask.run():void");
    }
}