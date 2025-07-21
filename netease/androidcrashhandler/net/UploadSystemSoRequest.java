package com.netease.androidcrashhandler.net;

import android.text.TextUtils;
import com.netease.androidcrashhandler.Const;
import com.netease.androidcrashhandler.so.SystemSoHandler;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.HashUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.androidcrashhandler.zip.ZipUtil;
import com.netease.ntunisdk.okhttp3.Headers;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class UploadSystemSoRequest extends BaseRequest {
    private static final String BOUNDARY = "--------------------------THISISHUANGJIEFENG";
    private static final String PREFIX = "--";
    private String crashhunterSystemSoDir;
    private List<SystemSoHandler.Unit> unUploadSystemSoList;
    private SystemSoHandler.UpLoadUnit upLoadUnit;
    private ArrayList<File> uploadFileList = new ArrayList<>();

    public UploadSystemSoRequest(SystemSoHandler.UpLoadUnit upLoadUnit, List<SystemSoHandler.Unit> list, String str) {
        this.upLoadUnit = upLoadUnit;
        this.unUploadSystemSoList = list;
        this.crashhunterSystemSoDir = str;
    }

    private String getParentDirPath(String str) {
        LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [getParentDirPath] start, uuid=" + str);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [getParentDirPath] unUploadSystemSoList=" + this.unUploadSystemSoList.toString() + ", uuid=" + str);
        for (int i = 0; i < this.unUploadSystemSoList.size(); i++) {
            SystemSoHandler.Unit unit = this.unUploadSystemSoList.get(i);
            LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [getParentDirPath] unit=" + unit.toString() + ", uuid=" + str);
            if (unit != null && str.equalsIgnoreCase(unit.mSoUuid)) {
                LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [getParentDirPath] unit.mParentDir=" + unit.mParentDir);
                return unit.mParentDir;
            }
        }
        return null;
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public RequestBody createRequestBody() throws Exception {
        LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [createRequestBody] start");
        if (this.upLoadUnit == null) {
            return null;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder("----------------------------THISISHUANGJIEFENG");
        builder.setType(MultipartBody.FORM);
        JSONArray jSONArray = this.upLoadUnit.mUploadSoArray;
        if (jSONArray != null && jSONArray.length() > 0) {
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArray.optJSONObject(i);
                if (jSONObjectOptJSONObject != null && jSONObjectOptJSONObject.has("name") && jSONObjectOptJSONObject.has("uuid")) {
                    String strOptString = jSONObjectOptJSONObject.optString("name");
                    String strOptString2 = jSONObjectOptJSONObject.optString("uuid");
                    String parentDirPath = getParentDirPath(strOptString2);
                    LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [createRequestBody] soName=" + strOptString + ", parentDir=" + parentDirPath);
                    StringBuilder sb = new StringBuilder();
                    sb.append(parentDirPath);
                    sb.append(strOptString);
                    File fileGZip = ZipUtil.gZip(sb.toString(), this.crashhunterSystemSoDir + "/" + strOptString + "_" + strOptString2 + ".gz");
                    if (fileGZip != null && fileGZip.exists()) {
                        String str = "android " + strOptString + " " + strOptString2;
                        RequestBody requestBodyCreate = RequestBody.create(MediaType.parse("application/octet-stream"), fileGZip);
                        Headers.Builder builder2 = new Headers.Builder();
                        builder2.add("Content-Disposition", "form-data; name=\"" + str + "\"; filename=\"" + strOptString + ".gz\"");
                        builder2.add("x-sysso-hash", HashUtil.calculateFileHash("MD5", fileGZip.getAbsolutePath()));
                        builder.addPart(MultipartBody.Part.create(builder2.build(), requestBodyCreate));
                        this.uploadFileList.add(fileGZip);
                        LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [createRequestBody] fileKey=" + str + ", value=" + strOptString + ".gz, x-sysso-hash=" + HashUtil.calculateFileHash("MD5", fileGZip.getAbsolutePath()));
                    }
                }
            }
        }
        return builder.build();
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public Request createRequest(RequestBody requestBody) throws Exception {
        LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [createRequestBody] token=" + this.upLoadUnit.mToken);
        return new Request.Builder().addHeader("X-SYSSO-TOKEN", this.upLoadUnit.mToken).url(CUtil.getSuitableUrl(Const.URL.DEFAULT_SYSTEM_SO_UPLOAD_URL)).post(requestBody).build();
    }

    private void deleteAllFile() {
        ArrayList<File> arrayList = this.uploadFileList;
        if (arrayList == null || arrayList.size() <= 0) {
            return;
        }
        Iterator<File> it = this.uploadFileList.iterator();
        while (it.hasNext()) {
            File next = it.next();
            next.delete();
            LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [createRequestBody] \u5220\u9664gz\u6587\u4ef6\uff0c\u6587\u4ef6\u540d=" + next.getAbsolutePath());
        }
    }

    @Override // com.netease.androidcrashhandler.net.BaseRequest
    public void handleResponse(Response response) throws Exception {
        String string;
        LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [handleResponse]");
        deleteAllFile();
        if (response != null) {
            int iCode = response.code();
            LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [handleResponse] code=" + iCode);
            InputStream inputStreamByteStream = response.body().byteStream();
            if (inputStreamByteStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamByteStream, RSASignature.c));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    } else {
                        sb.append(line);
                    }
                }
                string = sb.toString();
                LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [handleResponse] \u8bf7\u6c42\u7ed3\u679c=" + string);
            } else {
                LogUtils.i(LogUtils.TAG, "UploadSystemSoRequest net [handleResponse] param error");
                string = "";
            }
            callback(iCode, string);
            return;
        }
        callback(-2, "EXCEPTION_ERROR");
    }
}