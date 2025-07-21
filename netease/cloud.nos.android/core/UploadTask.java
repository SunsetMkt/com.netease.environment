package com.netease.cloud.nos.android.core;

import android.content.Context;
import android.os.AsyncTask;
import com.netease.cloud.nos.android.constants.Code;
import com.netease.cloud.nos.android.constants.Constants;
import com.netease.cloud.nos.android.exception.InvalidOffsetException;
import com.netease.cloud.nos.android.http.HttpResult;
import com.netease.cloud.nos.android.monitor.Monitor;
import com.netease.cloud.nos.android.monitor.StatisticItem;
import com.netease.cloud.nos.android.pipeline.PipelineHttpSession;
import com.netease.cloud.nos.android.utils.FileDigest;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.cloud.nos.android.utils.NetworkType;
import com.netease.cloud.nos.android.utils.Util;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class UploadTask extends AsyncTask<Object, Object, CallRet> {
    private static final String LOGTAG = LogUtil.makeLogTag(UploadTask.class);
    private String MD5;
    private String bucketName;
    private Callback callback;
    private Context context;
    private File file;
    private String fileName;
    private Object fileParam;
    protected volatile HttpGet get;
    private boolean isHttps;
    private WanNOSObject meta;
    private long offset;
    protected volatile HttpPost post;
    private String token;
    private String uploadContext;
    private volatile boolean upCancelled = false;
    protected volatile PipelineHttpSession uploader = null;
    private StatisticItem item = new StatisticItem();

    public UploadTask(Context context, String str, String str2, String str3, File file, Object obj, String str4, Callback callback, boolean z, WanNOSObject wanNOSObject) {
        this.MD5 = null;
        this.context = context;
        this.token = str;
        this.bucketName = str2;
        this.fileName = str3;
        this.file = file;
        this.fileParam = obj;
        this.uploadContext = str4;
        this.callback = callback;
        this.isHttps = z;
        this.meta = wanNOSObject;
        String contentMD5 = wanNOSObject.getContentMD5();
        this.MD5 = contentMD5;
        if (contentMD5 != null || file.length() > WanAccelerator.getConf().getMd5FileMaxSize()) {
            return;
        }
        this.MD5 = FileDigest.getFileMD5(file);
    }

    private void abort() {
        if (this.get != null) {
            try {
                this.get.abort();
            } catch (Exception e) {
                LogUtil.d(LOGTAG, "get method abort exception", e);
            }
        }
        if (this.post != null) {
            try {
                this.post.abort();
            } catch (Exception e2) {
                LogUtil.d(LOGTAG, "post method abort exception", e2);
            }
        }
    }

    private HttpEntity buildHttpEntity(byte[] bArr) throws IOException {
        return new ByteArrayEntity(bArr);
    }

    private CallRet createCancelCallRet() {
        return new CallRet(this.fileParam, this.uploadContext, Code.UPLOADING_CANCEL, "", "", "uploading is cancelled", null);
    }

    private HttpResult doUpload(int i) throws Throwable {
        String str;
        int i2;
        boolean z;
        String str2;
        String str3;
        boolean z2 = WanAccelerator.getConf().getHttpClient() == null && WanAccelerator.getConf().isPipelineEnabled() && !PipelineHttpSession.isStop();
        String str4 = LOGTAG;
        LogUtil.d(str4, "file parameters: ContentMD5=" + this.meta.getContentMD5() + ", realMD5=" + this.MD5 + ", ContentType=" + this.meta.getContentType() + ", chunkSize=" + i);
        if (z2 && this.file.length() > i) {
            str = "offset is invalid in server side, with offset:";
            this.uploader = new PipelineHttpSession(this.token, this.bucketName, this.fileName, this.fileParam, this.file, this.uploadContext, this.isHttps, this.meta, this.MD5, this.callback, i, this);
            HttpResult httpResultPipeUpload = pipeUpload(this.context);
            this.uploadContext = this.uploader.getUploadContext();
            this.item.setUploadType(1);
            if (this.upCancelled) {
                str3 = "pipeline upload is cancelled, Don't fall back";
                str2 = str4;
            } else {
                str2 = str4;
                int statusCode = httpResultPipeUpload.getStatusCode();
                if (statusCode != 200 && statusCode != 403 && statusCode != 520) {
                    i2 = Code.INVALID_OFFSET;
                    if (statusCode != 699 && statusCode != 500 && statusCode != 400) {
                        LogUtil.d(str2, "pipeline upload result: " + statusCode + ", fall back to non pipeline");
                        z = true;
                    }
                }
                str3 = "pipeline upload result: " + statusCode + ", Don't fall back";
            }
            LogUtil.d(str2, str3);
            return httpResultPipeUpload;
        }
        str = "offset is invalid in server side, with offset:";
        i2 = Code.INVALID_OFFSET;
        z = false;
        try {
            String str5 = this.uploadContext;
            if (str5 != null && !str5.equals("")) {
                HttpResult breakOffset = getBreakOffset(this.context, this.bucketName, this.fileName, this.uploadContext, this.token, this.isHttps);
                if (breakOffset.getStatusCode() == 404) {
                    this.uploadContext = null;
                } else {
                    if (breakOffset.getStatusCode() != 200) {
                        return breakOffset;
                    }
                    this.offset = breakOffset.getMsg().getInt("offset");
                }
            }
            if (this.offset < this.file.length() || this.file.length() == 0) {
                long j = this.offset;
                if (j >= 0) {
                    HttpResult httpResultPutFile = putFile(this.context, this.file, j, i, this.bucketName, this.fileName, this.token, this.uploadContext, this.isHttps);
                    if (z && httpResultPutFile.getStatusCode() == 200) {
                        PipelineHttpSession.stop();
                    }
                    this.item.setUploadType(z ? 2 : 0);
                    return httpResultPutFile;
                }
            }
            return new HttpResult(i2, new JSONObject(), new InvalidOffsetException(str + this.offset + ", file length: " + this.file.length()));
        } catch (Exception e) {
            LogUtil.e(LOGTAG, "offset result exception", e);
            return new HttpResult(Code.HTTP_EXCEPTION, new JSONObject(), e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00b9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r7v8, types: [org.apache.http.client.HttpClient] */
    /* JADX WARN: Type inference failed for: r8v0, types: [android.content.Context] */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v2 */
    /* JADX WARN: Type inference failed for: r8v3, types: [org.apache.http.HttpEntity] */
    /* JADX WARN: Type inference failed for: r8v5, types: [org.apache.http.HttpEntity] */
    /* JADX WARN: Type inference failed for: r8v7, types: [org.apache.http.client.methods.HttpGet, org.apache.http.client.methods.HttpUriRequest] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.netease.cloud.nos.android.http.HttpResult executeQueryTask(java.lang.String r7, android.content.Context r8, java.util.Map<java.lang.String, java.lang.String> r9) throws java.lang.Throwable {
        /*
            r6 = this;
            java.lang.String r0 = "Consume Content exception"
            java.lang.String r1 = "http get response is correct, response: "
            r2 = 0
            org.apache.http.client.methods.HttpGet r7 = com.netease.cloud.nos.android.utils.Util.newGet(r7)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            r6.get = r7     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            if (r9 == 0) goto L17
            org.apache.http.client.methods.HttpGet r7 = r6.get     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            org.apache.http.client.methods.HttpRequestBase r7 = com.netease.cloud.nos.android.utils.Util.setHeader(r7, r9)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            org.apache.http.client.methods.HttpGet r7 = (org.apache.http.client.methods.HttpGet) r7     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            r6.get = r7     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
        L17:
            org.apache.http.client.HttpClient r7 = com.netease.cloud.nos.android.utils.Util.getHttpClient(r8)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            org.apache.http.client.methods.HttpGet r8 = r6.get     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            org.apache.http.HttpResponse r7 = r7.execute(r8)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            if (r7 == 0) goto L71
            org.apache.http.StatusLine r8 = r7.getStatusLine()     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            if (r8 == 0) goto L71
            org.apache.http.HttpEntity r8 = r7.getEntity()     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            if (r8 == 0) goto L72
            org.apache.http.StatusLine r7 = r7.getStatusLine()     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            int r7 = r7.getStatusCode()     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            java.lang.String r9 = org.apache.http.util.EntityUtils.toString(r8)     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            r3.<init>(r9)     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            r4 = 200(0xc8, float:2.8E-43)
            if (r7 != r4) goto L56
            java.lang.String r4 = com.netease.cloud.nos.android.core.UploadTask.LOGTAG     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            r5.<init>(r1)     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            r5.append(r9)     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            java.lang.String r9 = r5.toString()     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            com.netease.cloud.nos.android.utils.LogUtil.d(r4, r9)     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            goto L5d
        L56:
            java.lang.String r9 = com.netease.cloud.nos.android.core.UploadTask.LOGTAG     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            java.lang.String r1 = "http get response is failed."
            com.netease.cloud.nos.android.utils.LogUtil.d(r9, r1)     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
        L5d:
            com.netease.cloud.nos.android.http.HttpResult r9 = new com.netease.cloud.nos.android.http.HttpResult     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            r9.<init>(r7, r3, r2)     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            if (r8 == 0) goto L6e
            r8.consumeContent()     // Catch: java.io.IOException -> L68
            goto L6e
        L68:
            r7 = move-exception
            java.lang.String r8 = com.netease.cloud.nos.android.core.UploadTask.LOGTAG
            com.netease.cloud.nos.android.utils.LogUtil.e(r8, r0, r7)
        L6e:
            r6.get = r2
            return r9
        L71:
            r8 = r2
        L72:
            com.netease.cloud.nos.android.http.HttpResult r7 = new com.netease.cloud.nos.android.http.HttpResult     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            org.json.JSONObject r9 = new org.json.JSONObject     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            r9.<init>()     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            r1 = 899(0x383, float:1.26E-42)
            r7.<init>(r1, r9, r2)     // Catch: java.lang.Exception -> L8d java.lang.Throwable -> Lb6
            if (r8 == 0) goto L8a
            r8.consumeContent()     // Catch: java.io.IOException -> L84
            goto L8a
        L84:
            r8 = move-exception
            java.lang.String r9 = com.netease.cloud.nos.android.core.UploadTask.LOGTAG
            com.netease.cloud.nos.android.utils.LogUtil.e(r9, r0, r8)
        L8a:
            r6.get = r2
            return r7
        L8d:
            r7 = move-exception
            goto L94
        L8f:
            r7 = move-exception
            r8 = r2
            goto Lb7
        L92:
            r7 = move-exception
            r8 = r2
        L94:
            java.lang.String r9 = com.netease.cloud.nos.android.core.UploadTask.LOGTAG     // Catch: java.lang.Throwable -> Lb6
            java.lang.String r1 = "http get task exception"
            com.netease.cloud.nos.android.utils.LogUtil.e(r9, r1, r7)     // Catch: java.lang.Throwable -> Lb6
            com.netease.cloud.nos.android.http.HttpResult r9 = new com.netease.cloud.nos.android.http.HttpResult     // Catch: java.lang.Throwable -> Lb6
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch: java.lang.Throwable -> Lb6
            r1.<init>()     // Catch: java.lang.Throwable -> Lb6
            r3 = 799(0x31f, float:1.12E-42)
            r9.<init>(r3, r1, r7)     // Catch: java.lang.Throwable -> Lb6
            if (r8 == 0) goto Lb3
            r8.consumeContent()     // Catch: java.io.IOException -> Lad
            goto Lb3
        Lad:
            r7 = move-exception
            java.lang.String r8 = com.netease.cloud.nos.android.core.UploadTask.LOGTAG
            com.netease.cloud.nos.android.utils.LogUtil.e(r8, r0, r7)
        Lb3:
            r6.get = r2
            return r9
        Lb6:
            r7 = move-exception
        Lb7:
            if (r8 == 0) goto Lc3
            r8.consumeContent()     // Catch: java.io.IOException -> Lbd
            goto Lc3
        Lbd:
            r8 = move-exception
            java.lang.String r9 = com.netease.cloud.nos.android.core.UploadTask.LOGTAG
            com.netease.cloud.nos.android.utils.LogUtil.e(r9, r0, r8)
        Lc3:
            r6.get = r2
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.cloud.nos.android.core.UploadTask.executeQueryTask(java.lang.String, android.content.Context, java.util.Map):com.netease.cloud.nos.android.http.HttpResult");
    }

    private void failureOperation(CallRet callRet) {
        this.item.setUploaderSucc(1);
        Monitor.add(this.context, this.item);
        this.callback.onFailure(callRet);
    }

    private HttpResult getBreakOffset(Context context, String str, String str2, String str3, String str4, boolean z) throws Throwable {
        HttpResult httpResultRetryQuery;
        String[] uploadServer = Util.getUploadServer(context, str, z);
        LogUtil.d(LOGTAG, "upload servers: " + Arrays.toString(uploadServer));
        HashMap map = new HashMap();
        map.put(Constants.HEADER_TOKEN, str4);
        try {
            httpResultRetryQuery = null;
            for (String str5 : uploadServer) {
                try {
                    String strBuildQueryUrl = Util.buildQueryUrl(str5, str, str2, str3);
                    LogUtil.d(LOGTAG, "break query upload server url: " + strBuildQueryUrl);
                    httpResultRetryQuery = retryQuery(strBuildQueryUrl, context, map);
                    if (this.upCancelled || httpResultRetryQuery.getStatusCode() == 200 || httpResultRetryQuery.getStatusCode() == 404) {
                        return httpResultRetryQuery;
                    }
                } catch (Exception e) {
                    e = e;
                    LogUtil.e(LOGTAG, "get break offset exception", e);
                    return httpResultRetryQuery == null ? new HttpResult(Code.HTTP_EXCEPTION, new JSONObject(), null) : httpResultRetryQuery;
                }
            }
            return httpResultRetryQuery;
        } catch (Exception e2) {
            e = e2;
            httpResultRetryQuery = null;
        }
    }

    private String getErrorString(int i) {
        return "statusCode " + i + ", " + Code.getDes(i);
    }

    private HttpResult pipeUpload(Context context) {
        int statusCode;
        this.item.setFileSize(this.file.length());
        try {
            String[] uploadServer = Util.getUploadServer(context, this.bucketName, this.isHttps);
            HttpResult httpResultRetryPipeUpload = null;
            int i = 0;
            for (String str : uploadServer) {
                String iPString = Util.getIPString(str);
                this.item.setUploaderIP(str);
                httpResultRetryPipeUpload = retryPipeUpload(iPString);
                if (!this.upCancelled && (statusCode = httpResultRetryPipeUpload.getStatusCode()) != 200 && statusCode != 403 && statusCode != 520 && statusCode != 699 && statusCode != 400) {
                    i++;
                    this.item.setUploadRetryCount(i);
                    if (i >= uploadServer.length) {
                        LogUtil.w(LOGTAG, "pipeline upload failed with all tries");
                    }
                    LogUtil.w(LOGTAG, "http post failed: " + i);
                }
                return httpResultRetryPipeUpload;
            }
            return httpResultRetryPipeUpload;
        } catch (Exception e) {
            LogUtil.e(LOGTAG, "pipeline upload file exception", e);
            return new HttpResult(Code.HTTP_EXCEPTION, new JSONObject(), e);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00d9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00a7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r9v0, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v2 */
    /* JADX WARN: Type inference failed for: r9v3, types: [org.apache.http.HttpEntity] */
    /* JADX WARN: Type inference failed for: r9v5, types: [org.apache.http.HttpEntity] */
    /* JADX WARN: Type inference failed for: r9v9, types: [java.lang.String] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.netease.cloud.nos.android.http.HttpResult post(java.lang.String r8, byte[] r9) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 230
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.cloud.nos.android.core.UploadTask.post(java.lang.String, byte[]):com.netease.cloud.nos.android.http.HttpResult");
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x01f8, code lost:
    
        r6 = r20 ? 1 : 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x01fa, code lost:
    
        r6 = r6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.netease.cloud.nos.android.http.HttpResult putFile(android.content.Context r28, java.io.File r29, long r30, int r32, java.lang.String r33, java.lang.String r34, java.lang.String r35, java.lang.String r36, boolean r37) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 546
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.cloud.nos.android.core.UploadTask.putFile(android.content.Context, java.io.File, long, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean):com.netease.cloud.nos.android.http.HttpResult");
    }

    private HttpResult queryLBS(String str) throws JSONException, InterruptedException {
        String data = Util.getData(this.context, this.bucketName + Constants.NET_TYPE);
        if (data == null || !data.equals(str)) {
            LogUtil.d(LOGTAG, "network connection change for bucket " + this.bucketName);
            Util.setBooleanData(this.context, this.bucketName + Constants.LBS_STATUS, false);
            Util.setData(this.context, this.bucketName + Constants.NET_TYPE, str);
        }
        if (Util.getBooleanData(this.context, this.bucketName + Constants.LBS_STATUS)) {
            if (Util.getData(this.context, this.bucketName + Constants.UPLOAD_SERVER_KEY) != null) {
                if (Util.getLongData(this.context, this.bucketName + Constants.LBS_TIME) + WanAccelerator.getConf().getRefreshInterval() > System.currentTimeMillis() && WanAccelerator.isOpened) {
                    return null;
                }
            }
        }
        WanAccelerator.isOpened = true;
        LogUtil.d(LOGTAG, "get lbs address");
        long jCurrentTimeMillis = System.currentTimeMillis();
        HttpResult lBSAddress = IOManager.getLBSAddress(this.context, this.bucketName, true);
        this.item.setLbsUseTime(System.currentTimeMillis() - jCurrentTimeMillis);
        if (lBSAddress.getStatusCode() == 200) {
            try {
                this.item.setLbsIP(lBSAddress.getMsg().getString("lbs"));
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(LOGTAG, "Failed to parse LBS result: " + e.getMessage());
            }
        } else {
            this.item.setLbsSucc(1);
            this.item.setLbsHttpCode(Util.getHttpCode(lBSAddress));
        }
        return lBSAddress;
    }

    private HttpResult retryPipeUpload(String str) {
        String str2;
        int statusCode;
        int chunkRetryCount = WanAccelerator.getConf().getChunkRetryCount();
        LogUtil.d(LOGTAG, "user set the retry times is : " + chunkRetryCount);
        int i = 0;
        HttpResult httpResultUpload = null;
        while (true) {
            int i2 = i + 1;
            if (i >= chunkRetryCount) {
                return httpResultUpload;
            }
            try {
                if (!this.upCancelled) {
                    str2 = LOGTAG;
                    LogUtil.d(str2, "pipeline put file to server : " + str + ", retryTime: " + i2);
                    httpResultUpload = this.uploader.upload(str);
                    if (!this.upCancelled) {
                        statusCode = httpResultUpload.getStatusCode();
                        if (statusCode == 200 || statusCode == 403 || statusCode == 520 || statusCode == 500 || statusCode == 699 || statusCode == 400) {
                            break;
                        }
                        LogUtil.d(str2, "pipeline retry server " + str + " with result: " + getErrorString(statusCode));
                        StatisticItem statisticItem = this.item;
                        statisticItem.setChunkRetryCount(statisticItem.getChunkRetryCount() + 1);
                        i = i2;
                    } else {
                        return httpResultUpload;
                    }
                } else {
                    return httpResultUpload;
                }
            } catch (Exception e) {
                LogUtil.e(LOGTAG, "put file exception", e);
                return new HttpResult(Code.HTTP_EXCEPTION, new JSONObject(), e);
            }
        }
        LogUtil.d(str2, "pipeline upload result: " + getErrorString(statusCode));
        return httpResultUpload;
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x007d, code lost:
    
        return r3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.netease.cloud.nos.android.http.HttpResult retryPutFile(java.lang.String r8, java.lang.String r9, android.content.Context r10, byte[] r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 260
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.cloud.nos.android.core.UploadTask.retryPutFile(java.lang.String, java.lang.String, android.content.Context, byte[]):com.netease.cloud.nos.android.http.HttpResult");
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x006b, code lost:
    
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.netease.cloud.nos.android.http.HttpResult retryQuery(java.lang.String r7, android.content.Context r8, java.util.Map<java.lang.String, java.lang.String> r9) throws java.lang.Throwable {
        /*
            r6 = this;
            com.netease.cloud.nos.android.core.AcceleratorConf r0 = com.netease.cloud.nos.android.core.WanAccelerator.getConf()
            int r0 = r0.getQueryRetryCount()
            r1 = 0
            r2 = 0
        La:
            int r3 = r1 + 1
            if (r1 >= r0) goto L6b
            boolean r1 = r6.upCancelled
            if (r1 != 0) goto L6b
            java.lang.String r1 = com.netease.cloud.nos.android.core.UploadTask.LOGTAG
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r4 = "query offset with url: "
            r2.<init>(r4)
            r2.append(r7)
            java.lang.String r4 = ", retry times: "
            r2.append(r4)
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            com.netease.cloud.nos.android.utils.LogUtil.d(r1, r2)
            com.netease.cloud.nos.android.http.HttpResult r2 = r6.executeQueryTask(r7, r8, r9)
            int r4 = r2.getStatusCode()
            r5 = 200(0xc8, float:2.8E-43)
            if (r4 != r5) goto L53
            org.json.JSONObject r7 = r2.getMsg()
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "get break offset result:"
            r8.<init>(r9)
            java.lang.String r7 = r7.toString()
            r8.append(r7)
            java.lang.String r7 = r8.toString()
        L4f:
            com.netease.cloud.nos.android.utils.LogUtil.d(r1, r7)
            return r2
        L53:
            com.netease.cloud.nos.android.monitor.StatisticItem r4 = r6.item
            int r5 = r4.getQueryRetryCount()
            int r5 = r5 + 1
            r4.setQueryRetryCount(r5)
            int r4 = r2.getStatusCode()
            r5 = 404(0x194, float:5.66E-43)
            if (r4 != r5) goto L69
            java.lang.String r7 = "upload file is expired in server side."
            goto L4f
        L69:
            r1 = r3
            goto La
        L6b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.cloud.nos.android.core.UploadTask.retryQuery(java.lang.String, android.content.Context, java.util.Map):com.netease.cloud.nos.android.http.HttpResult");
    }

    private void successOperation(CallRet callRet) {
        this.item.setUploaderSucc(0);
        Monitor.add(this.context, this.item);
        this.callback.onSuccess(callRet);
    }

    public void cancel() {
        LogUtil.d(LOGTAG, "uploading is canceling");
        if (this.uploader != null) {
            this.uploader.cancel();
        }
        this.upCancelled = true;
        abort();
        cancel(true);
        abort();
        cancel(true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // android.os.AsyncTask
    public CallRet doInBackground(Object... objArr) throws Throwable {
        try {
            NetworkType netWorkType = NetworkType.getNetWorkType(this.context);
            this.item.setNetEnv(netWorkType.getNetworkType());
            this.item.setClientIP(Util.getIPAddress());
            this.item.setBucketName(this.bucketName);
            HttpResult httpResultQueryLBS = queryLBS(netWorkType.getNetworkType());
            if (httpResultQueryLBS != null && httpResultQueryLBS.getStatusCode() != 200) {
                if (Util.getData(this.context, this.bucketName + Constants.UPLOAD_SERVER_KEY) == null) {
                    return new CallRet(this.fileParam, this.uploadContext, httpResultQueryLBS.getStatusCode(), Util.getResultString(httpResultQueryLBS, "requestID"), Util.getResultString(httpResultQueryLBS, "callbackRetMsg"), httpResultQueryLBS.getMsg().toString(), null);
                }
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            HttpResult httpResultDoUpload = doUpload(netWorkType.getChunkSize());
            if (httpResultDoUpload == null) {
                httpResultDoUpload = new HttpResult(500, new JSONObject(), null);
            }
            long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
            String str = LOGTAG;
            LogUtil.w(str, "upload result:" + httpResultDoUpload.getStatusCode() + ", speed:" + ((float) (((this.file.length() - this.offset) / 1024.0d) / (jCurrentTimeMillis2 / 1000.0d))) + "KB/S");
            this.item.setUploaderUseTime(jCurrentTimeMillis2);
            this.item.setUploaderHttpCode(Util.getHttpCode(httpResultDoUpload));
            if (httpResultDoUpload.getStatusCode() != 200 && !this.upCancelled) {
                Util.setBooleanData(this.context, this.bucketName + Constants.LBS_STATUS, false);
            }
            return new CallRet(this.fileParam, this.uploadContext, httpResultDoUpload.getStatusCode(), Util.getResultString(httpResultDoUpload, "requestID"), Util.getResultString(httpResultDoUpload, "callbackRetMsg"), httpResultDoUpload.getMsg().toString(), null);
        } catch (Exception e) {
            LogUtil.e(LOGTAG, "upload exception", e);
            return new CallRet(this.fileParam, this.uploadContext, Code.HTTP_EXCEPTION, "", "", null, e);
        }
    }

    public void getUploadProgress(long j, long j2) {
        LogUtil.d(LOGTAG, "uploading Progress offset:" + j + ", file length:" + j2);
        publishProgress(Long.valueOf(j), Long.valueOf(j2));
    }

    public boolean isUpCancelled() {
        return this.upCancelled;
    }

    @Override // android.os.AsyncTask
    protected void onCancelled() {
        LogUtil.d(LOGTAG, "on cancelled");
        this.item.setUploaderSucc(2);
        this.item.setUploaderHttpCode(Code.UPLOADING_CANCEL);
        Monitor.add(this.context, this.item);
        this.callback.onCanceled(createCancelCallRet());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(CallRet callRet) {
        LogUtil.d(LOGTAG, "on post executed");
        if (callRet == null) {
            failureOperation(new CallRet(this.fileParam, this.uploadContext, 999, "", "", "result is null", null));
        } else if (callRet.getException() == null && callRet.getHttpCode() == 200) {
            successOperation(callRet);
        } else {
            failureOperation(callRet);
        }
    }

    @Override // android.os.AsyncTask
    protected void onProgressUpdate(Object... objArr) {
        LogUtil.d(LOGTAG, "on process update");
        this.callback.onProcess(this.fileParam, ((Long) objArr[0]).longValue(), ((Long) objArr[1]).longValue());
    }
}