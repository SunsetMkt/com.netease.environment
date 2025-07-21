package com.netease.cloud.nos.android.pipeline;

import a.a.c.am;
import a.a.c.aw;
import a.a.d.a.a.aq;
import a.a.d.a.a.p;
import a.a.e.b;
import com.netease.cloud.nos.android.constants.Code;
import com.netease.cloud.nos.android.exception.InvalidOffsetException;
import com.netease.cloud.nos.android.http.HttpResult;
import com.netease.cloud.nos.android.utils.LogUtil;
import java.nio.charset.Charset;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class PipelineHttpClientHandler extends am {
    private static final String LOGTAG = LogUtil.makeLogTag(PipelineHttpClientHandler.class);

    private void handlerError(aw awVar, HttpResult httpResult, int i, String str) {
        LogUtil.e(LOGTAG, "handlerError cause: " + str);
        if (awVar.b().y()) {
            awVar.b().h();
        }
        notifySessionResult(awVar, httpResult, i);
    }

    private void notifySessionResult(aw awVar, HttpResult httpResult, int i) {
        PipelineHttpSession pipelineHttpSession = (PipelineHttpSession) awVar.b().a((b) PipelineHttpClient.SESSION_KEY).get();
        if (pipelineHttpSession == null) {
            return;
        }
        pipelineHttpSession.setSessionSuccess(i, httpResult);
    }

    @Override // a.a.c.ay, a.a.c.ax
    public void channelInactive(aw awVar) throws Exception {
        handlerError(awVar, new HttpResult(Code.HTTP_EXCEPTION, new JSONObject(), null), 1, "pipeline channelInactive");
    }

    @Override // a.a.c.ay, a.a.c.ax
    public void channelRead(aw awVar, Object obj) throws Exception {
        JSONObject jSONObject;
        String str = LOGTAG;
        LogUtil.d(str, "Do channelRead");
        p pVar = (p) obj;
        PipelineHttpSession pipelineHttpSession = (PipelineHttpSession) awVar.b().a((b) PipelineHttpClient.SESSION_KEY).get();
        if (pipelineHttpSession == null) {
            LogUtil.w(str, "pipeline no httpSession");
            return;
        }
        if (pVar.a() != null) {
            jSONObject = new JSONObject(pVar.a().a(Charset.defaultCharset()));
            LogUtil.d(str, "received nosInfo: " + jSONObject);
        } else {
            jSONObject = new JSONObject();
            LogUtil.w(str, "no content in response");
        }
        int iA = pVar.i().a();
        HttpResult httpResult = new HttpResult(iA, jSONObject, null);
        if (!pipelineHttpSession.hasBreakQuery()) {
            pipelineHttpSession.handleBreakInfo(iA, jSONObject);
            return;
        }
        if (iA != aq.d.a()) {
            handlerError(awVar, httpResult, 7, "HTTP Response Code:" + iA);
        } else {
            if (!jSONObject.has("context") || !jSONObject.has("offset")) {
                handlerError(awVar, new HttpResult(701, new JSONObject(), new InvalidOffsetException("context or offset is missing in response")), 8, "no context or offset in response");
                return;
            }
            try {
                String string = jSONObject.getString("context");
                int i = Integer.parseInt(jSONObject.getString("offset"));
                pipelineHttpSession.setUploadContext(string);
                pipelineHttpSession.handleOffset(i, httpResult);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("post response has not context or offset");
            }
        }
    }

    @Override // a.a.c.ay, a.a.c.ax
    public void channelWritabilityChanged(aw awVar) throws Exception {
        String str = LOGTAG;
        LogUtil.d(str, "channelWritabilityChanged isWritable: " + awVar.b().a());
        PipelineHttpSession pipelineHttpSession = (PipelineHttpSession) awVar.b().a((b) PipelineHttpClient.SESSION_KEY).get();
        if (pipelineHttpSession == null) {
            return;
        }
        LogUtil.d(str, "get PipelineHttpSession from the channel");
        if (awVar.b().a()) {
            pipelineHttpSession.writeDone();
        }
    }

    @Override // a.a.c.ay, a.a.c.av, a.a.c.at
    public void exceptionCaught(aw awVar, Throwable th) throws Exception {
        handlerError(awVar, new HttpResult(Code.HTTP_EXCEPTION, new JSONObject(), (Exception) th), 2, "pipeline exception Caught:" + th.toString());
    }

    public String getLogPrefix() {
        return "PipelineHttpClientHandler";
    }
}