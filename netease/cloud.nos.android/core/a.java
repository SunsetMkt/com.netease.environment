package com.netease.cloud.nos.android.core;

import com.netease.cloud.nos.android.http.HttpResult;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.cloud.nos.android.utils.Util;
import java.util.concurrent.CountDownLatch;

/* loaded from: classes5.dex */
final class a implements RequestCallback {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ HttpResult[] f1544a;
    final /* synthetic */ CountDownLatch b;

    a(HttpResult[] httpResultArr, CountDownLatch countDownLatch) {
        this.f1544a = httpResultArr;
        this.b = countDownLatch;
    }

    @Override // com.netease.cloud.nos.android.core.RequestCallback
    public void onResult(HttpResult httpResult) {
        if (httpResult.getStatusCode() != 200) {
            LogUtil.w(IOManager.LOGTAG, "http query failed status code: " + httpResult.getStatusCode());
        } else {
            LogUtil.d(IOManager.LOGTAG, "http query success");
        }
        this.f1544a[0] = httpResult;
        Util.releaseLock(this.b);
    }
}