package com.netease.mpay.ps.codescanner.task;

import android.content.Context;
import com.netease.mpay.ps.codescanner.server.ApiCallException;
import com.netease.mpay.ps.codescanner.server.ServerApi;
import com.netease.mpay.ps.codescanner.server.api.UploadQrCodePayStatusReq;
import com.netease.mpay.ps.codescanner.utils.Logging;
import com.netease.mpay.ps.codescanner.widget.ThreadPool;

/* loaded from: classes5.dex */
public class UploadPayStatusTask {
    private Context mContext;
    private String mGameId;
    private String mOrderId;
    private String mSn;
    private int mStatus;
    private String mUid;

    public UploadPayStatusTask(Context context, String str, String str2, String str3, String str4, int i) {
        this.mContext = context;
        this.mGameId = str;
        this.mUid = str2;
        this.mOrderId = str3;
        this.mSn = str4;
        this.mStatus = i;
    }

    public void doExecute() {
        ThreadPool.getCustomThreadPoolInstance().execute(new Runnable() { // from class: com.netease.mpay.ps.codescanner.task.UploadPayStatusTask.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    new ServerApi(UploadPayStatusTask.this.mContext, UploadPayStatusTask.this.mGameId).fetch(new UploadQrCodePayStatusReq(UploadPayStatusTask.this.mUid, UploadPayStatusTask.this.mOrderId, UploadPayStatusTask.this.mSn, UploadPayStatusTask.this.mStatus));
                } catch (ApiCallException e) {
                    Logging.logStackTrace(e);
                }
            }
        });
    }
}