package com.netease.ntunisdk.modules.clientlog;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.netease.ntunisdk.cutout.RespUtil;
import com.netease.ntunisdk.modules.base.BaseModules;
import com.netease.ntunisdk.modules.base.call.IModulesCall;
import com.netease.ntunisdk.modules.base.utils.LogModule;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogCode;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogThreadPool;
import com.netease.ntunisdk.modules.clientlog.core.ClientLogManager;
import com.netease.ntunisdk.modules.clientlog.core.ClientLogMessage;
import com.netease.ntunisdk.modules.clientlog.database.ClientLogTable;
import com.netease.ntunisdk.modules.clientlog.database.DatabaseManager;
import com.netease.ntunisdk.modules.clientlog.utils.ConfigUtil;
import com.tencent.open.SocialConstants;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ClientLogModule extends BaseModules {
    private final FHandler mHandler;
    private final HandlerThread mHandlerThread;

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getModuleName() {
        return ClientLogConstant.MODULE_NAME;
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getVersion() {
        return "1.1.0";
    }

    private static class FHandler extends Handler {
        public FHandler(Looper looper) {
            super(looper);
        }
    }

    public ClientLogModule(Context context, IModulesCall iModulesCall) throws JSONException, IOException {
        super(context, iModulesCall);
        ClientLogManager.getInstance().init(context);
        ConfigUtil.readLibraryConfig(context);
        ClientLogThreadPool.getInstance().init();
        HandlerThread handlerThread = new HandlerThread("ClientLogHandlerThread");
        this.mHandlerThread = handlerThread;
        handlerThread.start();
        this.mHandler = new FHandler(handlerThread.getLooper()) { // from class: com.netease.ntunisdk.modules.clientlog.ClientLogModule.1
            @Override // android.os.Handler
            public void handleMessage(Message message) throws JSONException {
                if (message.what == 0) {
                    ClientLogModule.this.startSubmitData((ClientLogMessage) message.obj);
                } else if (message.what == 1) {
                    ClientLogModule.this.stopSubmitData((ClientLogMessage) message.obj);
                }
            }
        };
        if (ClientLogConstant.SUBMIT_MODULE == ClientLogConstant.SubmitModel.MODEL_SINGLE) {
            flushData();
        }
    }

    public void flushData() throws JSONException {
        String deviceInfo = getDeviceInfo("getTransId");
        DatabaseManager databaseManager = ClientLogManager.getInstance().getDatabaseManager();
        if (databaseManager == null || !databaseManager.isOpen()) {
            return;
        }
        try {
            int iQueryAllSubmittingCount = databaseManager.queryAllSubmittingCount();
            for (int i = 0; i < iQueryAllSubmittingCount; i++) {
                ClientLogTable clientLogTableLimitQuerySubmittingRecord = databaseManager.limitQuerySubmittingRecord();
                if (clientLogTableLimitQuerySubmittingRecord != null) {
                    JSONObject jSONObject = new JSONObject(clientLogTableLimitQuerySubmittingRecord.getJsonString());
                    String transid = clientLogTableLimitQuerySubmittingRecord.getTransid();
                    if (!TextUtils.isEmpty(deviceInfo) && !transid.equals(deviceInfo)) {
                        jSONObject.put("status", 0);
                        databaseManager.update(jSONObject, ClientLogConstant.ID, new String[]{"" + clientLogTableLimitQuerySubmittingRecord.getID()});
                        LogModule.d(ClientLogConstant.TAG, "flushData: " + clientLogTableLimitQuerySubmittingRecord.getJsonString());
                    }
                }
            }
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "flushData: " + e);
        }
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String extendFunc(String str, String str2, String str3, Object... objArr) throws JSONException {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(str3);
            try {
                String strOptString = jSONObject.optString("methodId");
                LogModule.d(ClientLogConstant.TAG, "extendFunc originJson: " + jSONObject);
                if ("sendClientLog".equalsIgnoreCase(strOptString)) {
                    return sendClientLog(str2, str, jSONObject);
                }
                if ("closeClientLog".equals(strOptString)) {
                    return closeClientLog(str2, str, jSONObject);
                }
                extendCall(str2, str, ClientLogCode.NO_EXIST_METHOD, null, jSONObject);
                return synchronousCall(ClientLogCode.NO_EXIST_METHOD, null, jSONObject);
            } catch (Exception e) {
                e = e;
                LogModule.d(ClientLogConstant.TAG, "extendFunc Exception: " + e.getMessage());
                extendCall(str2, str, ClientLogCode.UNKNOWN_ERROR, null, jSONObject);
                return synchronousCall(ClientLogCode.UNKNOWN_ERROR, null, jSONObject);
            }
        } catch (Exception e2) {
            e = e2;
            jSONObject = null;
        }
    }

    private String sendClientLog(String str, String str2, JSONObject jSONObject) {
        try {
            if (!jSONObject.has(ClientLogConstant.LOG)) {
                return synchronousCall(ClientLogCode.PARAM_ERROR, null, jSONObject);
            }
            Message messageObtain = Message.obtain();
            JSONObject jSONObjectAddInfoForJson = addInfoForJson(str, jSONObject);
            if (jSONObjectAddInfoForJson == null) {
                LogModule.d(ClientLogConstant.TAG, "sendClientLog sendJson is null");
                return synchronousCall(ClientLogCode.UNKNOWN_ERROR, null, jSONObject);
            }
            ClientLogMessage clientLogMessage = new ClientLogMessage(str, str2, jSONObject, jSONObjectAddInfoForJson);
            messageObtain.what = 0;
            messageObtain.obj = clientLogMessage;
            this.mHandler.sendMessage(messageObtain);
            return synchronousCall(ClientLogCode.SUCCESS, null, jSONObject);
        } catch (Exception unused) {
            return synchronousCall(ClientLogCode.UNKNOWN_ERROR, null, jSONObject);
        }
    }

    private String closeClientLog(String str, String str2, JSONObject jSONObject) {
        try {
            Message messageObtain = Message.obtain();
            ClientLogMessage clientLogMessage = new ClientLogMessage(str, str2, jSONObject, null);
            messageObtain.what = 1;
            messageObtain.obj = clientLogMessage;
            this.mHandler.sendMessage(messageObtain);
            LogModule.d(ClientLogConstant.TAG, "closeClientLog Success");
            return synchronousCall(ClientLogCode.SUCCESS, null, jSONObject);
        } catch (Exception unused) {
            LogModule.d(ClientLogConstant.TAG, "closeClientLog Failed");
            return synchronousCall(ClientLogCode.UNKNOWN_ERROR, null, jSONObject);
        }
    }

    private JSONObject addInfoForJson(String str, JSONObject jSONObject) throws JSONException {
        try {
            JSONObject jSONObject2 = new JSONObject(jSONObject.toString());
            String str2 = new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.getDefault()).format(new Date());
            jSONObject2.put(ClientLogConstant.SDK, str);
            jSONObject2.put("status", 0);
            jSONObject2.put("type", ClientLogConstant.MODULE_TYPE);
            jSONObject2.put("platform", "ad");
            jSONObject2.put("timestamp", str2);
            jSONObject2.put("udid", getDeviceInfo("getUDID"));
            jSONObject2.put(ClientLogConstant.TRANSID, getDeviceInfo("getTransId"));
            return jSONObject2;
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "addInfoForJson Exception: " + e);
            return null;
        }
    }

    private String getDeviceInfo(String str) throws JSONException {
        try {
            registerModuleListener("deviceInfo");
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(SocialConstants.PARAM_SOURCE, "deviceInfo");
            jSONObject.put("methodId", str);
            return callSDKOthersModules("deviceInfo", jSONObject.toString());
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "getDeviceInfo Exception: " + e);
            return null;
        }
    }

    private void extendCall(final String str, final String str2, ClientLogCode clientLogCode, JSONObject jSONObject, JSONObject jSONObject2) throws JSONException {
        final JSONObject jSONObject3;
        try {
            if (jSONObject2 != null) {
                jSONObject3 = new JSONObject(jSONObject2.toString());
            } else {
                jSONObject3 = new JSONObject();
            }
            jSONObject3.put(RespUtil.UniSdkField.RESP_CODE, clientLogCode.getCode());
            jSONObject3.put(RespUtil.UniSdkField.RESP_MSG, clientLogCode.getMsg());
            if (clientLogCode.isSuccess() && jSONObject != null) {
                jSONObject3.put("result", jSONObject);
            }
            if (this.context instanceof Activity) {
                ((Activity) this.context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.modules.clientlog.ClientLogModule.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ClientLogModule.this.callback(str2, str, jSONObject3.toString());
                    }
                });
            } else {
                callback(str2, str, jSONObject3.toString());
            }
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "extendCall Exception: " + e);
        }
    }

    private String synchronousCall(ClientLogCode clientLogCode, JSONObject jSONObject, JSONObject jSONObject2) throws JSONException {
        JSONObject jSONObject3;
        try {
            if (jSONObject2 != null) {
                jSONObject3 = new JSONObject(jSONObject2.toString());
            } else {
                jSONObject3 = new JSONObject();
            }
            jSONObject3.put(RespUtil.UniSdkField.RESP_CODE, clientLogCode.getCode());
            jSONObject3.put(RespUtil.UniSdkField.RESP_MSG, clientLogCode.getMsg());
            if (clientLogCode.isSuccess() && jSONObject != null) {
                jSONObject3.put("result", jSONObject);
            }
            return jSONObject3.toString();
        } catch (Exception e) {
            JSONObject jSONObject4 = new JSONObject();
            try {
                if (jSONObject2 != null) {
                    jSONObject4 = new JSONObject(jSONObject2.toString());
                } else {
                    jSONObject4 = new JSONObject();
                }
                jSONObject4.put(RespUtil.UniSdkField.RESP_MSG, ClientLogCode.UNKNOWN_ERROR.getMsg());
                jSONObject4.put(RespUtil.UniSdkField.RESP_CODE, ClientLogCode.UNKNOWN_ERROR.getCode());
                return jSONObject4.toString();
            } catch (Exception unused) {
                LogModule.d(ClientLogConstant.TAG, "synchronousCall Exception Exception: " + e);
                return jSONObject4.toString();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSubmitData(ClientLogMessage clientLogMessage) throws JSONException {
        try {
            sendExtendCall(ClientLogManager.getInstance().startSubmit(clientLogMessage.getSubmitJson().toString()), clientLogMessage.getSource(), clientLogMessage.getCallType(), null, clientLogMessage.getOriginJson());
        } catch (Exception unused) {
            sendExtendCall(ClientLogCode.UNKNOWN_ERROR, clientLogMessage.getSource(), clientLogMessage.getCallType(), null, clientLogMessage.getOriginJson());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopSubmitData(ClientLogMessage clientLogMessage) throws JSONException {
        try {
            sendExtendCall(ClientLogManager.getInstance().stopSubmit(), clientLogMessage.getSource(), clientLogMessage.getCallType(), null, clientLogMessage.getOriginJson());
        } catch (Exception e) {
            LogModule.d(ClientLogConstant.TAG, "stop SubmitData Exception: " + e);
            sendExtendCall(ClientLogCode.UNKNOWN_ERROR, clientLogMessage.getSource(), clientLogMessage.getCallType(), null, clientLogMessage.getOriginJson());
        }
    }

    private void sendExtendCall(ClientLogCode clientLogCode, String str, String str2, JSONObject jSONObject, JSONObject jSONObject2) throws JSONException {
        int code = clientLogCode.getCode();
        if (code == 0) {
            extendCall(str, str2, ClientLogCode.SUCCESS, jSONObject, jSONObject2);
            return;
        }
        if (code == 1) {
            extendCall(str, str2, ClientLogCode.NO_EXIST_METHOD, jSONObject, jSONObject2);
            return;
        }
        if (code == 2) {
            extendCall(str, str2, ClientLogCode.PARAM_ERROR, jSONObject, jSONObject2);
        } else if (code == 7) {
            extendCall(str, str2, ClientLogCode.DATABASE_NOT_OPEN_ERROR, jSONObject, jSONObject2);
        } else {
            if (code != 1000) {
                return;
            }
            extendCall(str, str2, ClientLogCode.UNKNOWN_ERROR, jSONObject, jSONObject2);
        }
    }
}