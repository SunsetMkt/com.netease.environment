package com.netease.androidcrashhandler.net;

import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.thirdparty.deviceInfoModule.DeviceInfoProxy;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.androidcrashhandler.util.StorageToFileProxy;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.okhttp3.MediaType;
import com.netease.ntunisdk.okhttp3.MultipartBody;
import com.netease.ntunisdk.okhttp3.Request;
import com.netease.ntunisdk.okhttp3.RequestBody;
import com.netease.ntunisdk.okhttp3.Response;
import com.xiaomi.gamecenter.sdk.utils.RSASignature;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class UploadZipRequest extends BaseRequest {
    private static final String BOUNDARY = "--------------------------THISISHUANGJIEFENG";
    private static final String PREFIX = "--";
    private JSONObject cfgFileContent;
    private String zipFileName;

    public UploadZipRequest(String str, JSONObject jSONObject) {
        this.zipFileName = str;
        this.cfgFileContent = jSONObject;
        LogUtils.i(LogUtils.TAG, "UploadZipRequest net [UploadZipRequest] cfgFileContent=" + jSONObject.toString());
    }

    private void setZipFileMd5ToParams() throws JSONException {
        if (TextUtils.isEmpty(this.zipFileName)) {
            LogUtils.i(LogUtils.TAG, "UploadZipRequest net [setZipFileMd5ToParams] mZipFileName error");
            return;
        }
        JSONObject jSONObject = this.cfgFileContent;
        if (jSONObject == null || jSONObject.length() <= 0) {
            LogUtils.i(LogUtils.TAG, "UploadZipRequest net [setZipFileMd5ToParams] mCfgFileContent error");
            return;
        }
        File file = new File(this.zipFileName);
        String fileMD5 = file.exists() ? CUtil.getFileMD5(file) : null;
        LogUtils.i(LogUtils.TAG, "UploadZipRequest net [setZipFileMd5ToParams] zipFileMd5 = " + fileMD5);
        if (TextUtils.isEmpty(fileMD5)) {
            return;
        }
        try {
            this.cfgFileContent.put("zip_md5", fileMD5);
        } catch (JSONException e) {
            LogUtils.i(LogUtils.TAG, "UploadZipRequest net [setZipFileMd5ToParams] JSONException = " + e.toString());
            e.printStackTrace();
        }
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public RequestBody createRequestBody() throws Exception {
        JSONObject jSONObject;
        LogUtils.i(LogUtils.TAG, "UploadZipRequest net [createRequestBody] zipFileName = " + this.zipFileName);
        if (TextUtils.isEmpty(this.zipFileName) || !new File(this.zipFileName).exists() || (jSONObject = this.cfgFileContent) == null || jSONObject.length() <= 0) {
            LogUtils.i(LogUtils.TAG, "UploadZipRequest net [createRequestBody] param error");
            return null;
        }
        setZipFileMd5ToParams();
        if (!this.cfgFileContent.has(ClientLogConstant.TRANSID)) {
            String strOptString = NTCrashHunterKit.sharedKit().getmCurrentParamsInfo().getmParamsJson().optString(ClientLogConstant.TRANSID);
            if (!DeviceInfoProxy.checkTransidValid(strOptString)) {
                DeviceInfoProxy.createTransid();
                strOptString = InitProxy.getInstance().getmTransid();
            }
            this.cfgFileContent.put(ClientLogConstant.TRANSID, strOptString);
        }
        MultipartBody.Builder builder = new MultipartBody.Builder("----------------------------THISISHUANGJIEFENG");
        builder.setType(MultipartBody.FORM);
        JSONObject jSONObject2 = this.cfgFileContent;
        if (jSONObject2 != null && jSONObject2.length() > 0) {
            Iterator<String> itKeys = this.cfgFileContent.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                builder.addFormDataPart(next, this.cfgFileContent.getString(next));
            }
        }
        builder.addFormDataPart("zipfile", this.zipFileName, RequestBody.create(MediaType.parse("application/octet-stream"), new File(this.zipFileName)));
        return builder.build();
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public Request createRequest(RequestBody requestBody) throws Exception {
        return new Request.Builder().url(CUtil.getSuitableUrl(InitProxy.getInstance().getUploadUrl())).post(requestBody).build();
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public void handleResponse(Response response) throws Exception {
        if (response == null) {
            LogUtils.i(LogUtils.TAG, "UploadZipRequest net [handleResponse] zipFileName = " + this.zipFileName + ", \u4e0a\u4f20\u5931\u8d25");
            LogUtils.i(LogUtils.TAG, "UploadZipRequest net [handleResponse] \u4fee\u6539zip\u5305\u4e0a\u4f20\u72b6\u6001");
            callback(-1, this.zipFileName);
            return;
        }
        int iCode = response.code();
        LogUtils.i(LogUtils.TAG, "UploadZipRequest net [handleResponse] zipFileName = " + this.zipFileName + ",  code=" + iCode);
        if (iCode == 200) {
            File file = new File(this.zipFileName);
            if (file.exists()) {
                LogUtils.i(LogUtils.TAG, "UploadZipRequest net [handleResponse] \u5df2\u4e0a\u4f20\u6210\u529f\uff0c\u672c\u5730\u5220\u9664zip\u5305=" + this.zipFileName);
                file.delete();
            }
            File file2 = new File(this.zipFileName + Const.FileNameTag.CFG_FILE);
            if (file2.exists()) {
                LogUtils.i(LogUtils.TAG, "UploadZipRequest net [handleResponse] delete cfg file");
                file2.delete();
            }
        }
        LogUtils.i(LogUtils.TAG, "UploadZipRequest net [handleResponse] \u4fee\u6539zip\u5305\u4e0a\u4f20\u72b6\u6001");
        callback(iCode, this.zipFileName);
        if (200 != iCode) {
            return;
        }
        InputStream inputStreamByteStream = response.body().byteStream();
        if (inputStreamByteStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamByteStream, RSASignature.c));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    sb.append(line);
                } else {
                    LogUtils.i(LogUtils.TAG, "UploadZipRequest net [onResponseHandle] \u8bf7\u6c42\u5185\u5bb9=" + sb.toString());
                    StorageToFileProxy.getInstances().flush();
                    return;
                }
            }
        } else {
            LogUtils.i(LogUtils.TAG, "UploadZipRequest net [handleResponse] response InputStream is null");
        }
    }
}