package com.netease.ntunisdk.unilogger.network;

import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.okhttp3.Credentials;
import com.netease.ntunisdk.okhttp3.MediaType;
import com.netease.ntunisdk.okhttp3.MultipartBody;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.RequestBody;
import com.netease.ntunisdk.okhttp3.Response;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.ntunisdk.unilogger.global.GlobalPrarm;
import com.netease.ntunisdk.unilogger.utils.LogUtils;
import com.netease.ntunisdk.unilogger.utils.Utils;
import java.io.File;

/* loaded from: classes5.dex */
public class UploadRequest extends BaseRequest {
    private String uploadFilePath = null;
    private final String authKey = "unisdk";
    private final String authValue = "uni@123";

    public void setUploadFilePath(String str) {
        this.uploadFilePath = str;
    }

    @Override // com.netease.ntunisdk.unilogger.network.BaseRequest
    public RequestBody createRequestBody() throws Exception {
        LogUtils.i_inner("UploadRequest net [createRequestBody] uploadFilePath=" + this.uploadFilePath);
        return new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(ClientLogConstant.LOG, this.uploadFilePath, RequestBody.create(MediaType.parse("application/ontet-stream"), new File(this.uploadFilePath))).build();
    }

    @Override // com.netease.ntunisdk.unilogger.network.BaseRequest
    public Request createRequest(RequestBody requestBody) throws Exception {
        String strBasic = Credentials.basic("unisdk", "uni@123");
        Request.Builder builder = new Request.Builder();
        StringBuilder sb = Utils.isOversea() ? new StringBuilder(Const.Network.UPLOAD_URL_OVERSEA) : new StringBuilder(Const.Network.UPLOAD_URL_MAINLAND);
        sb.append(GlobalPrarm.realGameId);
        return builder.url(sb.toString()).header("Authorization", strBasic).post(requestBody).build();
    }

    @Override // com.netease.ntunisdk.unilogger.network.BaseRequest
    public void handleResponse(Response response) {
        if (response == null) {
            callback(-1, this.uploadFilePath);
            return;
        }
        int iCode = response.code();
        LogUtils.i_inner("UploadRequest net [handleResponse] response=" + response.toString());
        try {
            LogUtils.i_inner("UploadRequest net [handleResponse] body=" + response.body().string().toString());
        } catch (Exception unused) {
        }
        if (200 == iCode || 201 == iCode) {
            callback(1, this.uploadFilePath);
        } else {
            callback(-1, this.uploadFilePath);
        }
    }
}