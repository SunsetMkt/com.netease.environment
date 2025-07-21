package com.netease.mpay.ps.codescanner.task;

import android.content.Context;
import android.text.TextUtils;
import com.netease.mc.mi.R;
import com.netease.mpay.ps.codescanner.module.AppExtra;
import com.netease.mpay.ps.codescanner.module.DataStructure;
import com.netease.mpay.ps.codescanner.server.ApiCallException;
import com.netease.mpay.ps.codescanner.server.ServerApi;
import com.netease.mpay.ps.codescanner.server.api.ConfirmLoginReq;
import com.netease.mpay.ps.codescanner.server.api.ConfirmLoginResp;
import com.netease.mpay.ps.codescanner.utils.DataUtils;
import com.netease.mpay.ps.codescanner.utils.Logging;
import com.netease.mpay.ps.codescanner.widget.TextProgressDialog;

/* loaded from: classes5.dex */
public class LoginWebTask extends CoreAsyncTask<Void, DataStructure.StInfo<ConfirmLoginResp>> {
    private String mChannel;
    private Context mContext;
    private AppExtra mExtra;
    private String mGameId;
    private boolean mIsRemember;
    private LoginWebListener mListener;
    private TextProgressDialog mProgressDialog;
    private String mScene;
    private String mToken;
    private String mUUID;
    private String mUid;

    public LoginWebTask(Context context, String str, String str2, String str3, String str4, String str5, String str6, AppExtra appExtra, boolean z, LoginWebListener loginWebListener) {
        this.mContext = context;
        this.mGameId = str;
        this.mUUID = str2;
        this.mScene = str3;
        this.mUid = str4;
        this.mToken = str5;
        this.mChannel = str6;
        this.mExtra = appExtra;
        this.mIsRemember = z;
        this.mListener = loginWebListener;
    }

    @Override // android.os.AsyncTask
    protected void onPreExecute() {
        Context context = this.mContext;
        this.mProgressDialog = TextProgressDialog.newInstance(context, R.layout.netease_mpay_ps_codescanner__login_progress_dialog, R.id.netease_mpay_ps_codescanner__login_text, context.getString(R.string.netease_mpay_ps_codescanner__in_login_progress), false);
        this.mProgressDialog.show();
    }

    @Override // android.os.AsyncTask
    public DataStructure.StInfo<ConfirmLoginResp> doInBackground(Void... voidArr) {
        ServerApi serverApi = new ServerApi(this.mContext, this.mGameId);
        try {
            ConfirmLoginReq confirmLoginReqAddEncryptKey = new ConfirmLoginReq(this.mUUID, this.mScene, this.mUid, this.mToken, this.mChannel, this.mExtra, this.mIsRemember).addEncryptKey(getEncryptKey());
            serverApi.fetch(confirmLoginReqAddEncryptKey);
            return new DataStructure.StInfo().success(confirmLoginReqAddEncryptKey.getResponse());
        } catch (ApiCallException e) {
            return new DataStructure.StInfo().fail(e.getErrorMsg());
        }
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(DataStructure.StInfo<ConfirmLoginResp> stInfo) {
        super.onPostExecute((LoginWebTask) stInfo);
        this.mProgressDialog.dismiss();
        if (stInfo.success) {
            this.mListener.onLoginSuccess(stInfo.data);
        } else {
            this.mListener.onLoginFailure(stInfo.errMsg);
        }
    }

    private byte[] getEncryptKey() {
        byte[] bArrMd5 = null;
        try {
            bArrMd5 = DataUtils.md5(String.format("%s_%s_%s", this.mGameId, this.mChannel, !TextUtils.isEmpty(this.mExtra.channel) ? this.mExtra.channel : "").getBytes());
        } catch (Exception e) {
            Logging.logStackTrace(e);
        }
        return bArrMd5 == null ? new byte[0] : bArrMd5;
    }
}