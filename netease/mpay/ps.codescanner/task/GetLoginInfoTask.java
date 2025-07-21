package com.netease.mpay.ps.codescanner.task;

import android.content.Context;
import com.netease.mc.mi.R;
import com.netease.mpay.ps.codescanner.module.DataStructure;
import com.netease.mpay.ps.codescanner.server.ApiCallException;
import com.netease.mpay.ps.codescanner.server.ServerApi;
import com.netease.mpay.ps.codescanner.server.api.GetQRCodeInfoReq;
import com.netease.mpay.ps.codescanner.server.api.GetQRCodeInfoResp;
import com.netease.mpay.ps.codescanner.widget.TextProgressDialog;

/* loaded from: classes5.dex */
public class GetLoginInfoTask extends CoreAsyncTask<Void, DataStructure.StInfo<GetQRCodeInfoResp>> {
    private String mAppChannel;
    private Context mContext;
    private String mGameId;
    private GetLoginInfoListener mListener;
    private String mLoginChannel;
    private TextProgressDialog mProgressDialog;
    private String mSdkJsonData;
    private String mUUID;

    public GetLoginInfoTask(Context context, String str, String str2, String str3, String str4, String str5, GetLoginInfoListener getLoginInfoListener) {
        this.mContext = context;
        this.mGameId = str;
        this.mUUID = str2;
        this.mLoginChannel = str3;
        this.mAppChannel = str4;
        this.mSdkJsonData = str5;
        this.mListener = getLoginInfoListener;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        Context context = this.mContext;
        this.mProgressDialog = TextProgressDialog.newInstance(context, R.layout.netease_mpay_ps_codescanner__login_progress_dialog, R.id.netease_mpay_ps_codescanner__login_text, context.getString(R.string.netease_mpay_ps_codescanner__in_progress), false);
        this.mProgressDialog.show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public DataStructure.StInfo<GetQRCodeInfoResp> doInBackground(Void... voidArr) {
        ServerApi serverApi = new ServerApi(this.mContext, this.mGameId);
        try {
            GetQRCodeInfoReq getQRCodeInfoReq = new GetQRCodeInfoReq(this.mUUID, this.mLoginChannel, this.mAppChannel, this.mSdkJsonData);
            serverApi.fetch(getQRCodeInfoReq);
            return new DataStructure.StInfo().success(getQRCodeInfoReq.getResponse());
        } catch (ApiCallException e) {
            return new DataStructure.StInfo().fail(e.getErrorMsg());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    public void onPostExecute(DataStructure.StInfo<GetQRCodeInfoResp> stInfo) {
        super.onPostExecute((GetLoginInfoTask) stInfo);
        this.mProgressDialog.dismiss();
        if (stInfo.success) {
            this.mListener.onSuccess(stInfo.data);
        } else {
            this.mListener.onFailure(stInfo.errMsg);
        }
    }
}