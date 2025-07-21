package com.netease.mpay.ps.codescanner.task;

import android.content.Context;
import com.netease.mc.mi.R;
import com.netease.mpay.ps.codescanner.module.DataStructure;
import com.netease.mpay.ps.codescanner.server.ApiCallException;
import com.netease.mpay.ps.codescanner.server.ServerApi;
import com.netease.mpay.ps.codescanner.server.api.ScanExternalReq;
import com.netease.mpay.ps.codescanner.widget.TextProgressDialog;

/* loaded from: classes5.dex */
public class ScanExternalTask extends CoreAsyncTask<Void, DataStructure.StInfo<Void>> {
    private Context mContext;
    private String mGameId;
    private ScanExternalListener mListener;
    private String mOrderId;
    private TextProgressDialog mProgressDialog;
    private String mUid;

    public ScanExternalTask(Context context, String str, String str2, String str3, ScanExternalListener scanExternalListener) {
        this.mContext = context;
        this.mGameId = str;
        this.mUid = str2;
        this.mOrderId = str3;
        this.mListener = scanExternalListener;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        Context context = this.mContext;
        this.mProgressDialog = TextProgressDialog.newInstance(context, R.layout.netease_mpay_ps_codescanner__login_progress_dialog, R.id.netease_mpay_ps_codescanner__login_text, context.getString(R.string.netease_mpay_ps_codescanner__in_progress), false);
        this.mProgressDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public DataStructure.StInfo<Void> doInBackground(Void... voidArr) {
        try {
            new ServerApi(this.mContext, this.mGameId).fetch(new ScanExternalReq(this.mUid, this.mOrderId));
            return new DataStructure.StInfo().success(null);
        } catch (ApiCallException e) {
            return new DataStructure.StInfo().fail(e.getErrorMsg());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(DataStructure.StInfo<Void> stInfo) {
        super.onPostExecute((ScanExternalTask) stInfo);
        this.mProgressDialog.dismiss();
        if (stInfo.success) {
            this.mListener.onSuccess();
        } else {
            this.mListener.onFailure(stInfo.errMsg);
        }
    }
}