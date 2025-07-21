package com.netease.cloud.nos.android.core;

import android.content.Context;
import com.netease.cloud.nos.android.constants.Constants;
import com.netease.cloud.nos.android.http.HttpGetTask;
import com.netease.cloud.nos.android.http.HttpResult;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.cloud.nos.android.utils.Util;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class IOManager {
    private static final String LOGTAG = LogUtil.makeLogTag(IOManager.class);

    private static HttpResult executeQueryTask(String str, Context context, Map<String, String> map) throws InterruptedException {
        HttpResult[] httpResultArr = {null};
        CountDownLatch countDownLatchAcquireLock = Util.acquireLock();
        Util.getExecutorService().execute(new HttpGetTask(str, context, map, new a(httpResultArr, countDownLatchAcquireLock)));
        Util.setLock(countDownLatchAcquireLock);
        return httpResultArr[0];
    }

    public static HttpResult getLBSAddress(Context context, String str, boolean z) throws JSONException, InterruptedException {
        String str2 = WanAccelerator.getConf().getLbsHost() + ";" + WanAccelerator.getConf().getLbsIP();
        String data = Util.getData(context, str + Constants.LBS_KEY);
        if (z && data != null) {
            str2 = data + ";" + str2;
        }
        LogUtil.d(LOGTAG, "get lbs address with multiple urls: " + str2);
        String[] strArrSplit = str2.split(";");
        int length = strArrSplit.length;
        int i = 0;
        HttpResult httpResult = null;
        while (i < length) {
            String str3 = strArrSplit[i];
            String str4 = LOGTAG;
            LogUtil.d(str4, "get lbs address with url: " + str3);
            HttpResult httpResultExecuteQueryTask = executeQueryTask(Util.buildLBSUrl(str3, str), context, null);
            if (httpResultExecuteQueryTask.getStatusCode() == 200) {
                JSONObject msg = httpResultExecuteQueryTask.getMsg();
                LogUtil.d(str4, "LBS address result: " + msg.toString());
                httpResultExecuteQueryTask = Util.setLBSData(context, str, msg);
                if (httpResultExecuteQueryTask.getStatusCode() == 200) {
                    return httpResultExecuteQueryTask;
                }
            }
            LogUtil.w(str4, "failed to query LBS url " + str3 + " result: " + httpResultExecuteQueryTask.getStatusCode() + " msg: " + httpResultExecuteQueryTask.getMsg().toString());
            i++;
            httpResult = httpResultExecuteQueryTask;
        }
        return httpResult == null ? new HttpResult(400, new JSONObject(), null) : httpResult;
    }
}