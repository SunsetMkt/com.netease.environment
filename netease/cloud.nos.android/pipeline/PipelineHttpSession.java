package com.netease.cloud.nos.android.pipeline;

import a.a.c.ao;
import a.a.d.a.a.a;
import a.a.d.a.a.am;
import a.a.d.a.a.ar;
import a.a.d.a.a.z;
import com.JavaWebsocket.WebSocket;
import com.netease.cloud.nos.android.constants.Code;
import com.netease.cloud.nos.android.constants.Constants;
import com.netease.cloud.nos.android.core.Callback;
import com.netease.cloud.nos.android.core.UploadTask;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.core.WanNOSObject;
import com.netease.cloud.nos.android.exception.InvalidOffsetException;
import com.netease.cloud.nos.android.http.HttpResult;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.cloud.nos.android.utils.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class PipelineHttpSession {
    private static final int EACH_PART_SIZE = 131072;
    private static final String LOGTAG = LogUtil.makeLogTag(PipelineHttpSession.class);
    private static boolean isStop;
    private static long stopTime;
    private String MD5;
    private String bucketName;
    private Callback callback;
    private int chunkSize;
    private PipelineHttpClient client;
    private File file;
    private String fileName;
    private Object fileParam;
    private boolean isHttps;
    private WanNOSObject meta;
    private int timeout;
    private String token;
    private long totalLength;
    private volatile String uploadContext;
    private UploadTask uploadTask;
    private volatile long sendOffset = 0;
    private volatile long responseOffset = 0;
    private volatile long respNum = 0;
    private volatile boolean isComplete = false;
    private volatile int isSuccess = 0;
    private volatile boolean hasBreakQuery = false;
    private volatile long lastResponseTime = 0;
    private volatile boolean upCancelled = false;
    private volatile HttpResult rs = null;
    private Object completeCondition = new Object();

    public PipelineHttpSession(String str, String str2, String str3, Object obj, File file, String str4, boolean z, WanNOSObject wanNOSObject, String str5, Callback callback, int i, UploadTask uploadTask) {
        this.fileName = null;
        this.token = null;
        this.meta = null;
        this.callback = null;
        this.totalLength = 0L;
        this.file = null;
        this.MD5 = null;
        this.uploadContext = null;
        this.uploadTask = null;
        this.client = null;
        this.chunkSize = 131072;
        this.timeout = 30000;
        this.isHttps = false;
        this.bucketName = str2;
        this.fileName = str3;
        this.uploadContext = str4;
        this.callback = callback;
        this.fileParam = obj;
        this.totalLength = file.length();
        this.file = file;
        this.token = str;
        this.meta = wanNOSObject;
        this.isHttps = z;
        this.MD5 = str5;
        this.uploadTask = uploadTask;
        this.timeout = WanAccelerator.getConf().getSoTimeout();
        this.chunkSize = i;
        this.client = new PipelineHttpClient(z ? WebSocket.DEFAULT_WSS_PORT : 80, z, this);
    }

    private am buildBreakRequest(String str) {
        a aVar = new a(ar.b, z.b, str);
        aVar.f().a("Host", (Object) this.client.ip);
        aVar.f().a(Constants.HEADER_TOKEN, (Object) this.token);
        return aVar;
    }

    private a buildUploadRequest(InputStream inputStream, int i, String str) {
        a aVar = new a(ar.b, z.d, str);
        aVar.f().a("Host", (Object) this.client.ip).a("Content-Length", (Object) Integer.valueOf(i));
        aVar.f().a(Constants.HEADER_TOKEN, (Object) this.token);
        String str2 = this.MD5;
        if (str2 != null && !str2.equals("")) {
            aVar.f().a("Content-MD5", (Object) this.MD5);
        }
        WanNOSObject wanNOSObject = this.meta;
        if (wanNOSObject != null) {
            Util.pipeAddHeaders(aVar, wanNOSObject);
        }
        try {
            aVar.a().a(inputStream, i);
            return aVar;
        } catch (Exception e) {
            e.printStackTrace();
            setSessionSuccess(11, this.rs);
            LogUtil.e(LOGTAG, "failed to read file, readlength:" + i + ", totalLength:" + this.totalLength);
            return null;
        }
    }

    private void handlerComplete(HttpResult httpResult) {
        LogUtil.d(LOGTAG, "pipeline http post Complete");
        setSessionSuccess(0, httpResult);
    }

    private void handlerError(HttpResult httpResult, int i, String str) {
        LogUtil.e(LOGTAG, "handlerError cause: " + str);
        this.client.channelClose();
        setSessionSuccess(i, httpResult);
    }

    public static boolean isStop() {
        if (isStop && stopTime + WanAccelerator.getConf().getPipelineFailoverPeriod() <= System.currentTimeMillis()) {
            isStop = false;
        }
        return isStop;
    }

    private long oneUpload(String str, FileInputStream fileInputStream) throws InterruptedException, IOException {
        int i;
        int i2;
        String str2;
        HttpResult httpResult;
        String str3 = LOGTAG;
        LogUtil.d(str3, "pipeline one upload start");
        int i3 = 0;
        this.isComplete = false;
        this.isSuccess = 14;
        this.hasBreakQuery = false;
        this.responseOffset = 0L;
        this.respNum = 0L;
        this.rs = null;
        if (this.client.connect(str) == null) {
            LogUtil.d(str3, "failed to connect uploadServer:" + str);
            this.rs = new HttpResult(900, new JSONObject(), null);
            return 0L;
        }
        if (this.upCancelled) {
            return 0L;
        }
        LogUtil.d(str3, "uploadContext:" + this.uploadContext + ", uploadContextExist:" + uploadContextExist());
        int i4 = 1;
        if (uploadContextExist()) {
            breakQuery();
            if (!this.hasBreakQuery) {
                return 0L;
            }
        } else {
            this.hasBreakQuery = true;
        }
        if (this.upCancelled) {
            return 0L;
        }
        long j = this.responseOffset;
        if (!this.isComplete) {
            this.sendOffset = this.responseOffset;
            fileInputStream.getChannel().position(this.sendOffset);
        }
        this.lastResponseTime = System.currentTimeMillis();
        while (!this.isComplete && ((this.sendOffset < this.totalLength || (this.sendOffset == 0 && this.totalLength == 0)) && !this.upCancelled)) {
            int i5 = i3 + i4;
            ao aoVarSendPost = sendPost(fileInputStream, this.sendOffset, this.chunkSize);
            if (aoVarSendPost != null) {
                try {
                    aoVarSendPost.a(this.timeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    if (!this.upCancelled) {
                        e.printStackTrace();
                    }
                    LogUtil.w(LOGTAG, "pipeline upload is interrupted:" + e.getCause());
                }
                if (!this.upCancelled) {
                    String str4 = LOGTAG;
                    LogUtil.d(str4, "pipeline one block upload isDone:" + aoVarSendPost.isDone());
                    if (!aoVarSendPost.isDone()) {
                        i = i5;
                        if (System.currentTimeMillis() > this.lastResponseTime + this.timeout + 800) {
                            httpResult = new HttpResult(Code.HTTP_NO_RESPONSE, new JSONObject(), null);
                            str2 = "upload timeout for " + this.timeout + "ms, close channel";
                            i2 = 6;
                        }
                        handlerError(httpResult, i2, str2);
                        break;
                    }
                    i = i5;
                    if (this.totalLength == 0) {
                        break;
                    }
                    if (aoVarSendPost.d().a()) {
                        i3 = i;
                    } else {
                        StringBuilder sb = new StringBuilder("channel is not wirtable, sendCount:");
                        i3 = i;
                        sb.append(i3);
                        LogUtil.w(str4, sb.toString());
                        waitForWriteDone(aoVarSendPost, i3);
                    }
                    if (!aoVarSendPost.d().z()) {
                        httpResult = new HttpResult(Code.HTTP_EXCEPTION, new JSONObject(), null);
                        str2 = "Channel is not active";
                        i2 = 1;
                        handlerError(httpResult, i2, str2);
                        break;
                    }
                    if (1 == i3 && this.sendOffset < this.totalLength) {
                        waitForContext();
                    }
                    LogUtil.d(str4, "pipeline http post success, sendOffset: " + this.sendOffset + ", totalLength: " + this.totalLength + ", this is " + i3 + " block uploaded");
                    i4 = 1;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        waitForComplete();
        long j2 = this.responseOffset > j ? this.responseOffset - j : 0L;
        LogUtil.d(LOGTAG, "pipeline one upload isSuccess:" + this.isSuccess + " sendSize:" + j2);
        return j2;
    }

    public static void reStart() {
        if (isStop) {
            isStop = false;
            LogUtil.w(LOGTAG, "pipeline restart");
        }
    }

    public static void stop() {
        isStop = true;
        stopTime = System.currentTimeMillis();
        LogUtil.w(LOGTAG, "pipeline stopped for a while");
    }

    private boolean uploadContextExist() {
        return (this.uploadContext == null || this.uploadContext.equals("")) ? false : true;
    }

    private void waitForBreakResp() {
        try {
            if (!this.hasBreakQuery && !this.isComplete) {
                synchronized (this.completeCondition) {
                    this.lastResponseTime = System.currentTimeMillis();
                    while (!this.hasBreakQuery && !this.isComplete) {
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        long j = this.lastResponseTime;
                        int i = this.timeout;
                        if (jCurrentTimeMillis >= j + i) {
                            break;
                        } else {
                            this.completeCondition.wait(i);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.hasBreakQuery || this.isComplete) {
            return;
        }
        LogUtil.e(LOGTAG, "no breakQuery response");
        setSessionSuccess(3, new HttpResult(Code.HTTP_NO_RESPONSE, new JSONObject(), null));
    }

    private void waitForComplete() {
        try {
            if (!this.isComplete) {
                synchronized (this.completeCondition) {
                    this.lastResponseTime = System.currentTimeMillis();
                    while (!this.isComplete) {
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        long j = this.lastResponseTime;
                        int i = this.timeout;
                        if (jCurrentTimeMillis >= j + i) {
                            break;
                        } else {
                            this.completeCondition.wait(i);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.isComplete) {
            return;
        }
        handlerError(new HttpResult(Code.HTTP_NO_RESPONSE, new JSONObject(), null), 6, "upload timeout for " + this.timeout + "ms, close channel");
    }

    private void waitForContext() {
        try {
            synchronized (this.completeCondition) {
                this.lastResponseTime = System.currentTimeMillis();
                while (!uploadContextExist() && !this.isComplete) {
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    long j = this.lastResponseTime;
                    int i = this.timeout;
                    if (jCurrentTimeMillis >= j + i) {
                        break;
                    } else {
                        this.completeCondition.wait(i);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (uploadContextExist() || this.isComplete) {
            return;
        }
        LogUtil.e(LOGTAG, "no uploadContext received");
        setSessionSuccess(6, new HttpResult(Code.HTTP_NO_RESPONSE, new JSONObject(), null));
    }

    public void breakQuery() {
        String str;
        try {
            StringBuilder sb = new StringBuilder();
            if (this.isHttps) {
                str = "https://" + this.client.ip + ":443";
            } else {
                str = "";
            }
            sb.append(str);
            sb.append(Util.pipeBuildQueryUrl(this.bucketName, this.fileName, this.uploadContext));
            String string = sb.toString();
            String str2 = LOGTAG;
            LogUtil.d(str2, "break query upload server url: " + string);
            long jCurrentTimeMillis = System.currentTimeMillis();
            this.client.get(buildBreakRequest(string));
            waitForBreakResp();
            LogUtil.d(str2, "breakQuery duration: " + (System.currentTimeMillis() - jCurrentTimeMillis));
        } catch (Exception e) {
            LogUtil.e(LOGTAG, "build breakQueryUrl exception", e);
            this.rs = new HttpResult(Code.HTTP_EXCEPTION, new JSONObject(), e);
        }
    }

    public void cancel() {
        LogUtil.d(LOGTAG, "pipeline uploading is canceling");
        this.upCancelled = true;
        if (this.client != null) {
            handlerError(this.rs, 12, "pipeline upload is cancelled");
        }
    }

    public String getUploadContext() {
        return this.uploadContext;
    }

    public void handleBreakInfo(int i, JSONObject jSONObject) throws JSONException {
        if (i == 404) {
            this.uploadContext = null;
        } else {
            if (i != 200) {
                handlerError(new HttpResult(i, jSONObject, null), 4, "HTTP Response Code:" + i);
                return;
            }
            if (jSONObject == null || !jSONObject.has("offset")) {
                handlerError(new HttpResult(Code.INVALID_OFFSET, jSONObject, new InvalidOffsetException("offset is missing in breakQuery response")), 5, "no offset in breakQuery response");
                this.responseOffset = 0L;
                return;
            }
            this.responseOffset = jSONObject.getInt("offset");
        }
        long j = this.responseOffset;
        long j2 = this.totalLength;
        if ((j < j2 || j2 == 0) && this.responseOffset >= 0) {
            synchronized (this.completeCondition) {
                this.hasBreakQuery = true;
                this.completeCondition.notify();
            }
            return;
        }
        HttpResult httpResult = new HttpResult(Code.INVALID_OFFSET, new JSONObject(), new InvalidOffsetException("offset is invalid in server side, with offset: " + this.responseOffset + ", file length: " + this.totalLength));
        StringBuilder sb = new StringBuilder("HTTP Response Code:");
        sb.append(i);
        handlerError(httpResult, 5, sb.toString());
        this.responseOffset = 0L;
    }

    public void handleOffset(int i, HttpResult httpResult) {
        int i2;
        String str;
        this.lastResponseTime = System.currentTimeMillis();
        this.respNum++;
        long j = i;
        long j2 = this.totalLength;
        if (j == j2) {
            this.responseOffset = j;
            handlerComplete(httpResult);
        } else {
            if (j > j2 || i < 0) {
                i2 = 9;
                str = "offset error";
            } else if (j <= this.responseOffset) {
                LogUtil.w(LOGTAG, "pipeline backoff, offset: " + i + ", current responseOffset: " + this.responseOffset);
                i2 = 13;
                str = "pipeline offset backoff";
            } else {
                this.responseOffset = j;
            }
            handlerError(httpResult, i2, str);
        }
        this.uploadTask.getUploadProgress(j, this.totalLength);
        LogUtil.d(LOGTAG, "pipeline http response, offset: " + i + ", totalLength: " + this.totalLength + ", this is " + this.respNum + " block response");
    }

    public boolean hasBreakQuery() {
        return this.hasBreakQuery;
    }

    public boolean isUpCancelled() {
        return this.upCancelled;
    }

    public ao sendPost(FileInputStream fileInputStream, long j, int i) throws IOException {
        String str;
        if (this.isComplete) {
            LogUtil.d(LOGTAG, "iscomplete offset: " + j + ", totalLength: " + this.totalLength);
            return null;
        }
        long j2 = this.totalLength;
        if (j2 != 0 && j == j2) {
            handlerComplete(this.rs);
            LogUtil.d(LOGTAG, "sendPost complete offset: " + j + "= totalLength: " + this.totalLength);
            return null;
        }
        if (j > j2) {
            setSessionSuccess(10, this.rs);
            LogUtil.e(LOGTAG, "sendPost Error offset: " + j + ", totalLength: " + this.totalLength);
            return null;
        }
        int iMin = (int) Math.min(i, j2 - j);
        String str2 = LOGTAG;
        LogUtil.d(str2, "upload block size is: " + iMin + ", part_size:" + i);
        long j3 = ((long) iMin) + j;
        this.sendOffset = j3;
        boolean z = j3 == this.totalLength;
        StringBuilder sb = new StringBuilder();
        if (this.isHttps) {
            str = "https://" + this.client.ip + ":443";
        } else {
            str = "";
        }
        sb.append(str);
        sb.append(Util.pipeBuildPostDataUrl(this.bucketName, this.fileName, this.uploadContext, j, z));
        String string = sb.toString();
        LogUtil.d(str2, "post data url: " + string);
        ao aoVarPost = this.client.post(buildUploadRequest(fileInputStream, iMin, string));
        if (aoVarPost == null) {
            handlerError(new HttpResult(Code.HTTP_EXCEPTION, new JSONObject(), null), 2, "pipeline exception: ChannelFuture is null");
        }
        return aoVarPost;
    }

    public void setSessionSuccess(int i, HttpResult httpResult) {
        this.client.reset();
        if (this.isSuccess == 14) {
            this.isSuccess = i;
        }
        if (this.rs == null) {
            this.rs = httpResult;
        }
        synchronized (this.completeCondition) {
            this.isComplete = true;
            this.completeCondition.notify();
        }
    }

    public void setUploadContext(String str) {
        if (str.equals(this.uploadContext)) {
            return;
        }
        this.callback.onUploadContextCreate(this.fileParam, this.uploadContext, str);
        synchronized (this.completeCondition) {
            this.uploadContext = str;
            this.completeCondition.notify();
        }
        LogUtil.d(LOGTAG, "received new uploadContext: " + str);
    }

    public HttpResult upload(String str) throws InterruptedException, IOException {
        FileInputStream fileInputStream = new FileInputStream(this.file);
        LogUtil.d(LOGTAG, "start pipeline upload to uploadServer ip: " + str);
        long jCurrentTimeMillis = System.currentTimeMillis();
        long jOneUpload = 0L;
        long j = 0;
        while (!this.upCancelled) {
            jOneUpload += oneUpload(str, fileInputStream);
            if (this.upCancelled || (this.isSuccess != 13 && (this.isSuccess != 1 || (j != 0 && this.respNum == 0)))) {
                break;
            }
            LogUtil.w(LOGTAG, "retry to upload for reason:" + this.isSuccess + " count:" + j + ", current respNum:" + this.respNum);
            j++;
        }
        fileInputStream.close();
        long jCurrentTimeMillis2 = System.currentTimeMillis() - jCurrentTimeMillis;
        LogUtil.w(LOGTAG, "pipeline upload isSuccess:" + this.isSuccess + " duration:" + jCurrentTimeMillis2 + " totalSize:" + jOneUpload + " speed:" + ((float) ((jOneUpload / 1024.0d) / (jCurrentTimeMillis2 / 1000.0d))) + "KB/S");
        if (this.rs == null) {
            this.rs = new HttpResult(this.isSuccess == 0 ? 200 : Code.HTTP_EXCEPTION, new JSONObject(), null);
        }
        return this.rs;
    }

    public void waitForWriteDone(ao aoVar, int i) {
        try {
            if (!aoVar.d().a() && !this.isComplete) {
                synchronized (this.completeCondition) {
                    this.lastResponseTime = System.currentTimeMillis();
                    while (!aoVar.d().a() && !this.isComplete) {
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        long j = this.lastResponseTime;
                        int i2 = this.timeout;
                        if (jCurrentTimeMillis >= j + i2) {
                            break;
                        } else {
                            this.completeCondition.wait(i2);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (aoVar.d().a() || this.isComplete) {
            return;
        }
        LogUtil.e(LOGTAG, "wait for channel writable long time");
        handlerError(new HttpResult(Code.HTTP_EXCEPTION, new JSONObject(), null), 2, "pipeline exception: channel is not writable");
    }

    public void writeDone() {
        synchronized (this.completeCondition) {
            this.completeCondition.notify();
        }
    }
}