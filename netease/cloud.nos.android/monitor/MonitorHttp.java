package com.netease.cloud.nos.android.monitor;

import android.content.Context;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.cloud.nos.android.utils.Util;
import com.netease.ntunisdk.external.protocol.Const;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

/* loaded from: classes5.dex */
public class MonitorHttp {
    private static final String LOGTAG = LogUtil.makeLogTag(MonitorHttp.class);

    public static void post(Context context, String str) throws Throwable {
        HttpPost httpPostNewPost = Util.newPost(Util.getMonitorUrl(str));
        httpPostNewPost.addHeader("Content-Encoding", Const.ACCEPT_ENCODING_GZIP);
        List<StatisticItem> list = Monitor.get();
        ByteArrayOutputStream postData = Monitor.getPostData(list);
        if (postData == null) {
            LogUtil.d(LOGTAG, "post data is null");
            return;
        }
        httpPostNewPost.setEntity(new ByteArrayEntity(postData.toByteArray()));
        try {
            try {
                try {
                    try {
                        HttpResponse httpResponseExecute = Util.getHttpClient(context).execute(httpPostNewPost);
                        if (httpResponseExecute != null && httpResponseExecute.getStatusLine() != null && httpResponseExecute.getEntity() != null) {
                            int statusCode = httpResponseExecute.getStatusLine().getStatusCode();
                            String string = EntityUtils.toString(httpResponseExecute.getEntity());
                            if (statusCode == 200) {
                                LogUtil.d(LOGTAG, "http post response is correct, response: " + string);
                            } else {
                                String str2 = LOGTAG;
                                LogUtil.d(str2, "http post response is failed, status code: " + statusCode);
                                if (httpResponseExecute.getEntity() != null) {
                                    LogUtil.d(str2, "http post response is failed, result: " + string);
                                }
                            }
                        }
                        if (list != null) {
                            list.clear();
                        }
                        if (postData != null) {
                            postData.close();
                        }
                    } catch (ClientProtocolException e) {
                        LogUtil.e(LOGTAG, "post monitor data failed with client protocol exception", e);
                        if (list != null) {
                            list.clear();
                        }
                        if (postData != null) {
                            postData.close();
                        }
                    }
                } catch (IOException e2) {
                    LogUtil.e(LOGTAG, "post monitor data failed with io exception", e2);
                    if (list != null) {
                        list.clear();
                    }
                    if (postData != null) {
                        postData.close();
                    }
                }
            } catch (IOException e3) {
                LogUtil.e(LOGTAG, "bos close exception", e3);
            }
        } catch (Throwable th) {
            if (list != null) {
                list.clear();
            }
            if (postData != null) {
                try {
                    postData.close();
                } catch (IOException e4) {
                    LogUtil.e(LOGTAG, "bos close exception", e4);
                }
            }
            throw th;
        }
    }
}