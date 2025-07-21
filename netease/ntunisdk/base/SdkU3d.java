package com.netease.ntunisdk.base;

import android.content.Context;
import android.text.TextUtils;
import com.netease.download.listener.DownloadListener;
import com.netease.ntunisdk.base.callback.U3dDlCallback;
import com.netease.ntunisdk.base.model.SdkDlBytes;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SdkU3d implements OnCodeScannerListener, OnContinueListener, OnControllerListener, OnExitListener, OnExtendFuncListener, OnFinishInitListener, OnLeaveSdkListener, OnLoginDoneListener, OnLogoutDoneListener, OnOrderCheckListener, OnProtocolFinishListener, OnPushListener, OnQRCodeListener, OnQuerySkuDetailsListener, OnQuestListener, OnReceiveMsgListener, OnShareListener, OnShowViewListener, OnVerifyListener, OnWebViewListener, QueryFriendListener, QueryRankListener {
    public static final String CALLBACKTYPE_DL_OnFinish = "DL_OnFinish";
    public static final String CALLBACKTYPE_DL_OnProgress = "DL_OnProgress";
    public static final String CALLBACKTYPE_OnApplyFriend = "OnApplyFriend";
    public static final String CALLBACKTYPE_OnCancelLocalPushFinished = "OnCancelLocalPushFinished";
    public static final String CALLBACKTYPE_OnClosed = "OnClosed";
    public static final String CALLBACKTYPE_OnCodeScannerFinish = "OnCodeScannerFinish";
    public static final String CALLBACKTYPE_OnContinue = "OnContinue";
    public static final String CALLBACKTYPE_OnCreateQRCodeDone = "OnCreateQRCodeDone";
    public static final String CALLBACKTYPE_OnEnterGame = "OnEnterGame";
    public static final String CALLBACKTYPE_OnExitView = "OnExitView";
    public static final String CALLBACKTYPE_OnExitViewFailed = "OnExitViewFailed";
    public static final String CALLBACKTYPE_OnExtendFuncCall = "OnExtendFuncCall";
    public static final String CALLBACKTYPE_OnFailed = "OnFailed";
    public static final String CALLBACKTYPE_OnFailure = "OnFailure";
    public static final String CALLBACKTYPE_OnFinishInit = "OnFinishInit";
    public static final String CALLBACKTYPE_OnGetUserPushFinished = "OnGetUserPushFinished";
    public static final String CALLBACKTYPE_OnIsDarenUpdated = "OnIsDarenUpdated";
    public static final String CALLBACKTYPE_OnLeaveSdk = "OnLeaveSdk";
    public static final String CALLBACKTYPE_OnLoginDone = "OnLoginDone";
    public static final String CALLBACKTYPE_OnLogoutDone = "onLogoutDone";
    public static final String CALLBACKTYPE_OnOpened = "OnOpened";
    public static final String CALLBACKTYPE_OnOrderCheck = "OnOrderCheck";
    public static final String CALLBACKTYPE_OnProtocolFinish = "OnProtocolFinish";
    public static final String CALLBACKTYPE_OnQueryAvailablesInvitees = "OnQueryAvailablesInvitees";
    public static final String CALLBACKTYPE_OnQueryFriendList = "OnQueryFriendList";
    public static final String CALLBACKTYPE_OnQueryFriendListInGame = "OnQueryFriendListInGame";
    public static final String CALLBACKTYPE_OnQueryMyAccount = "OnQueryMyAccount";
    public static final String CALLBACKTYPE_OnQueryRank = "OnQueryRank";
    public static final String CALLBACKTYPE_OnQuerySkuDetails = "OnQuerySkuDetails";
    public static final String CALLBACKTYPE_OnQuestCompleted = "OnQuestCompleted";
    public static final String CALLBACKTYPE_OnReceivedNotification = "OnReceivedNotification";
    public static final String CALLBACKTYPE_OnRewarded = "OnRewarded";
    public static final String CALLBACKTYPE_OnSendLocalNotificationFinished = "OnSendLocalNotificationFinished";
    public static final String CALLBACKTYPE_OnSendPushNotificationFinished = "OnSendPushNotificationFinished";
    public static final String CALLBACKTYPE_OnSetUserPushFinished = "OnSetUserPushFinished";
    public static final String CALLBACKTYPE_OnShare = "OnShare";
    public static final String CALLBACKTYPE_OnSuccess = "OnSuccess";
    public static final String CALLBACKTYPE_OnUpdateRank = "OnUpdateRank";
    public static final String CALLBACKTYPE_OnWebViewNativeCall = "OnWebViewNativeCall";

    /* renamed from: a, reason: collision with root package name */
    private static Class<?> f1793a = null;
    private static String b = "Main Camera";
    private static U3dDlCallback c;
    public static GamerInterface inst;
    public static SdkU3d instU3d;

    @Override // com.netease.ntunisdk.base.QueryFriendListener
    public void onInviteFriendListFinished(List<String> list) {
    }

    @Override // com.netease.ntunisdk.base.QueryFriendListener
    public void onInviterListFinished(List<AccountInfo> list) {
    }

    @Override // com.netease.ntunisdk.base.OnControllerListener
    public void onKeyDown(int i, PadEvent padEvent) {
    }

    @Override // com.netease.ntunisdk.base.OnControllerListener
    public void onKeyPressure(int i, float f, PadEvent padEvent) {
    }

    @Override // com.netease.ntunisdk.base.OnControllerListener
    public void onKeyUp(int i, PadEvent padEvent) {
    }

    @Override // com.netease.ntunisdk.base.OnControllerListener
    public void onLeftStick(float f, float f2, PadEvent padEvent) {
    }

    @Override // com.netease.ntunisdk.base.OnControllerListener
    public void onRightStick(float f, float f2, PadEvent padEvent) {
    }

    @Override // com.netease.ntunisdk.base.OnControllerListener
    public void onStateEvent(PadEvent padEvent) {
    }

    @Override // com.netease.ntunisdk.base.QueryRankListener
    public void onUpdateAchievement(boolean z) {
    }

    @Override // com.netease.ntunisdk.base.OnOrderCheckListener
    public void orderConsumeDone(OrderInfo orderInfo) {
    }

    private static Class<?> a() throws ClassNotFoundException {
        if (f1793a == null) {
            f1793a = Class.forName("com.unity3d.player.UnityPlayer");
        }
        return f1793a;
    }

    public static SdkU3d getInstU3d() {
        if (instU3d == null) {
            instU3d = new SdkU3d();
        }
        return instU3d;
    }

    public static GamerInterface getInst() throws IllegalAccessException, NoSuchFieldException, IOException, SecurityException, ClassNotFoundException, IllegalArgumentException {
        if (inst == null) {
            SdkMgr.init((Context) a().getField("currentActivity").get(null));
            GamerInterface inst2 = SdkMgr.getInst();
            inst = inst2;
            inst2.setPropInt(ConstProp.GAME_ENGINE, 1);
            inst.setLoginListener(getInstU3d(), 1);
            inst.setLogoutListener(getInstU3d(), 1);
            inst.setOrderListener(getInstU3d(), 1);
            inst.setContinueListener(getInstU3d(), 1);
            inst.setExitListener(getInstU3d(), 1);
            inst.setQueryFriendListener(getInstU3d(), 1);
            inst.setQueryRankListener(getInstU3d(), 1);
            inst.setShareListener(getInstU3d(), 1);
            inst.setQuerySkuDetailsListener(getInstU3d(), 1);
            inst.setControllerListener(getInstU3d(), 1);
            inst.setPushListener(getInstU3d(), 1);
            inst.setQuestListener(getInstU3d(), 1);
            inst.setShowViewListener(getInstU3d(), 1);
            inst.setReceiveMsgListener(getInstU3d(), 1);
            inst.setExtendFuncListener(getInstU3d(), 1);
            inst.setWebViewListener(getInstU3d(), 1);
            inst.setVerifyListener(getInstU3d(), 1);
            inst.setCodeScannerListener(getInstU3d(), 1);
            inst.setOnProtocolFinishListener(getInstU3d(), 1);
            inst.setQRCodeListener(getInstU3d(), 1);
        }
        return inst;
    }

    public static void init() {
        try {
            UniSdkUtils.i("UniSDK SdkU3d", "try SdkU3d init");
            getInst().ntInit(getInstU3d());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (NoSuchFieldException e4) {
            e4.printStackTrace();
        }
    }

    public static void destroyInst() {
        inst = null;
    }

    public static void ntCheckOrder(String str, String str2, String str3) {
        try {
            OrderInfo orderInfo = new OrderInfo(str);
            orderInfo.setOrderId(str2);
            orderInfo.setOrderEtc(str3);
            SdkMgr.getInst().ntCheckOrder(orderInfo);
        } catch (Exception e) {
            UniSdkUtils.e("UniSDK SdkU3d", "order create error");
            e.printStackTrace();
        }
    }

    public static void ntCheckOrder(String str) {
        UniSdkUtils.i("UniSDK SdkU3d", "try SdkU3d ntCheckOrder: ".concat(String.valueOf(str)));
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.i("UniSDK SdkU3d", "jsonStr is null");
        } else {
            SdkMgr.getInst().ntCheckOrder(OrderInfo.jsonStr2Obj(str));
        }
    }

    public static void ntQueryRank(String str) {
        UniSdkUtils.i("UniSDK SdkU3d", "try SdkU3d ntQueryRank: ".concat(String.valueOf(str)));
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.i("UniSDK SdkU3d", "jsonStr is null");
        } else {
            SdkMgr.getInst().ntQueryRank(QueryRankInfo.jsonStr2Obj(str));
        }
    }

    public static void ntShare(String str) {
        UniSdkUtils.i("UniSDK SdkU3d", "try SdkU3d ntShare: ".concat(String.valueOf(str)));
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.i("UniSDK SdkU3d", "jsonStr is null");
        } else {
            SdkMgr.getInst().ntShare(ShareInfo.jsonStr2Obj(str));
        }
    }

    public static void ntConsume(String str) {
        UniSdkUtils.i("UniSDK SdkU3d", "try SdkU3d ntConsume: ".concat(String.valueOf(str)));
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.i("UniSDK SdkU3d", "jsonStr is null");
        } else {
            SdkMgr.getInst().ntConsume(OrderInfo.jsonStr2Obj(str));
        }
    }

    public static void ntQuerySkuDetails(String str, String str2) {
        UniSdkUtils.i("UniSDK SdkU3d", "try SdkU3d ntQuerySkuDetails: itemType=" + str + ", jsonStr" + str2);
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.i("UniSDK SdkU3d", "itemType is null");
            return;
        }
        if (TextUtils.isEmpty(str2)) {
            UniSdkUtils.i("UniSDK SdkU3d", "jsonStr is null");
            return;
        }
        try {
            JSONArray jSONArray = new JSONArray(str2);
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(jSONArray.get(i).toString());
            }
            SdkMgr.getInst().ntQuerySkuDetails(str, arrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void setCallbackModule(String str) {
        b = str;
        UniSdkUtils.d("NtUniSdk", "SdkU3d setCallbackModule:".concat(String.valueOf(str)));
    }

    public static void unity3dSendMessage(String str) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        try {
            a().getMethod("UnitySendMessage", String.class, String.class, String.class).invoke(a(), b, "OnSdkMsgCallback", str);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (NoSuchMethodException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
    }

    public static void callback(String str, int i, Object obj) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("callbackType", str);
            jSONObject.put("code", i);
            jSONObject.put("data", obj);
            unity3dSendMessage(jSONObject.toString());
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    @Override // com.netease.ntunisdk.base.OnFinishInitListener
    public void finishInit(int i) {
        UniSdkUtils.i("UniSDK SdkU3d", "finishInit,code=".concat(String.valueOf(i)));
        callback(CALLBACKTYPE_OnFinishInit, i, "");
    }

    @Override // com.netease.ntunisdk.base.OnLoginDoneListener
    public void loginDone(int i) {
        UniSdkUtils.i("UniSDK SdkU3d", "loginDone,code=".concat(String.valueOf(i)));
        callback(CALLBACKTYPE_OnLoginDone, i, "");
    }

    @Override // com.netease.ntunisdk.base.OnLogoutDoneListener
    public void logoutDone(int i) {
        UniSdkUtils.i("UniSDK SdkU3d", "logoutDone,code=".concat(String.valueOf(i)));
        callback(CALLBACKTYPE_OnLogoutDone, i, "");
    }

    @Override // com.netease.ntunisdk.base.OnLeaveSdkListener
    public void leaveSdk(int i) {
        callback(CALLBACKTYPE_OnLeaveSdk, i, "");
    }

    @Override // com.netease.ntunisdk.base.OnOrderCheckListener
    public void orderCheckDone(OrderInfo orderInfo) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "begin check order done...");
        int orderStatus = orderInfo.getOrderStatus();
        UniSdkUtils.i("UniSDK SdkU3d", "checkOrderDone,code=".concat(String.valueOf(orderStatus)));
        JSONObject jSONObjectObj2Json = OrderInfo.obj2Json(orderInfo);
        UniSdkUtils.i("UniSDK SdkU3d", jSONObjectObj2Json.toString());
        callback(CALLBACKTYPE_OnOrderCheck, orderStatus, jSONObjectObj2Json);
    }

    @Override // com.netease.ntunisdk.base.OnExitListener
    public void exitApp() {
        UniSdkUtils.i("UniSDK SdkU3d", "exitApp");
        callback(CALLBACKTYPE_OnExitView, 1, "");
    }

    @Override // com.netease.ntunisdk.base.OnExitListener
    public void onOpenExitViewFailed() {
        UniSdkUtils.i("UniSDK SdkU3d", "onOpenExitViewFailed");
        callback(CALLBACKTYPE_OnExitViewFailed, 1, "");
    }

    @Override // com.netease.ntunisdk.base.OnContinueListener
    public void continueGame() {
        UniSdkUtils.i("UniSDK SdkU3d", "continueGame");
        callback(CALLBACKTYPE_OnContinue, 1, "");
    }

    @Override // com.netease.ntunisdk.base.QueryFriendListener
    public void onQueryFriendListFinished(List<AccountInfo> list) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "on query friends finished");
        JSONArray jSONArray = new JSONArray();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jSONObjectObj2Json = AccountInfo.obj2Json(list.get(i));
                if (jSONObjectObj2Json != null) {
                    try {
                        jSONArray.put(i, jSONObjectObj2Json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        UniSdkUtils.i("UniSDK SdkU3d", jSONArray.toString());
        callback(CALLBACKTYPE_OnQueryFriendList, 1, jSONArray.toString());
    }

    @Override // com.netease.ntunisdk.base.QueryFriendListener
    public void onQueryAvailablesInviteesFinished(List<AccountInfo> list) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "on query availables invitees finished");
        JSONArray jSONArray = new JSONArray();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jSONObjectObj2Json = AccountInfo.obj2Json(list.get(i));
                if (jSONObjectObj2Json != null) {
                    try {
                        jSONArray.put(i, jSONObjectObj2Json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        UniSdkUtils.i("UniSDK SdkU3d", jSONArray.toString());
        callback(CALLBACKTYPE_OnQueryAvailablesInvitees, 1, jSONArray.toString());
    }

    @Override // com.netease.ntunisdk.base.QueryFriendListener
    public void onQueryMyAccountFinished(AccountInfo accountInfo) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "on query myAccount finished");
        JSONObject jSONObjectObj2Json = AccountInfo.obj2Json(accountInfo);
        UniSdkUtils.i("UniSDK SdkU3d", jSONObjectObj2Json.toString());
        callback(CALLBACKTYPE_OnQueryMyAccount, 1, jSONObjectObj2Json.toString());
    }

    @Override // com.netease.ntunisdk.base.QueryFriendListener
    public void onApplyFriendFinished(boolean z) {
        UniSdkUtils.i("UniSDK SdkU3d", "on apply friend finished");
        callback(CALLBACKTYPE_OnApplyFriend, 1, Boolean.valueOf(z));
    }

    @Override // com.netease.ntunisdk.base.QueryFriendListener
    public void onIsDarenUpdated(boolean z) {
        UniSdkUtils.i("UniSDK SdkU3d", "on isDarenUpdated");
        callback(CALLBACKTYPE_OnIsDarenUpdated, 1, Boolean.valueOf(z));
    }

    @Override // com.netease.ntunisdk.base.QueryRankListener
    public void onQueryRankFinished(List<AccountInfo> list) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "on query rank finished");
        JSONArray jSONArray = new JSONArray();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jSONObjectObj2Json = AccountInfo.obj2Json(list.get(i));
                if (jSONObjectObj2Json != null) {
                    try {
                        jSONArray.put(i, jSONObjectObj2Json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        UniSdkUtils.i("UniSDK SdkU3d", jSONArray.toString());
        callback(CALLBACKTYPE_OnQueryRank, 1, jSONArray.toString());
    }

    @Override // com.netease.ntunisdk.base.QueryRankListener
    public void onUpdateRankFinished(boolean z) {
        UniSdkUtils.i("UniSDK SdkU3d", "on update rank finished");
        callback(CALLBACKTYPE_OnUpdateRank, 1, Boolean.valueOf(z));
    }

    @Override // com.netease.ntunisdk.base.QueryFriendListener
    public void onQueryFriendListInGameFinished(List<AccountInfo> list) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "on query friends inGame finished");
        JSONArray jSONArray = new JSONArray();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jSONObjectObj2Json = AccountInfo.obj2Json(list.get(i));
                if (jSONObjectObj2Json != null) {
                    try {
                        jSONArray.put(i, jSONObjectObj2Json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        UniSdkUtils.i("UniSDK SdkU3d", jSONArray.toString());
        callback(CALLBACKTYPE_OnQueryFriendListInGame, 1, jSONArray.toString());
    }

    @Override // com.netease.ntunisdk.base.OnShareListener
    public void onShareFinished(boolean z) {
        UniSdkUtils.i("UniSDK SdkU3d", "on share finished");
        callback(CALLBACKTYPE_OnShare, 1, Boolean.valueOf(z));
    }

    @Override // com.netease.ntunisdk.base.OnReceiveMsgListener
    public void onReceivedNotification() {
        UniSdkUtils.i("UniSDK SdkU3d", "onReceivedNotification");
        callback(CALLBACKTYPE_OnReceivedNotification, 1, "");
    }

    @Override // com.netease.ntunisdk.base.OnReceiveMsgListener
    public void onEnterGame(String str, String str2) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "onEnterGame");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("op", str);
            jSONObject.put("jsonParams", new JSONObject(str2));
        } catch (JSONException e) {
            UniSdkUtils.e("UniSDK SdkU3d", "JSONException");
            e.printStackTrace();
        }
        UniSdkUtils.d("UniSDK SdkU3d", "data:" + jSONObject.toString());
        callback(CALLBACKTYPE_OnEnterGame, 1, jSONObject.toString());
    }

    @Override // com.netease.ntunisdk.base.OnQuerySkuDetailsListener
    public void querySkuDetailsFinished(List<SkuDetailsInfo> list) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "querySkuDetailsFinished");
        JSONArray jSONArray = new JSONArray();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                JSONObject jSONObjectObj2Json = SkuDetailsInfo.obj2Json(list.get(i));
                if (jSONObjectObj2Json != null) {
                    try {
                        jSONArray.put(i, jSONObjectObj2Json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        callback(CALLBACKTYPE_OnQuerySkuDetails, 1, jSONArray.toString());
    }

    @Override // com.netease.ntunisdk.base.OnShowViewListener
    public void onRewarded() {
        UniSdkUtils.i("UniSDK SdkU3d", "onRewarded");
        callback(CALLBACKTYPE_OnRewarded, 1, "");
    }

    @Override // com.netease.ntunisdk.base.OnShowViewListener
    public void onOpened() {
        UniSdkUtils.i("UniSDK SdkU3d", "onOpened");
        callback(CALLBACKTYPE_OnOpened, 1, "");
    }

    @Override // com.netease.ntunisdk.base.OnShowViewListener
    public void onFailed() {
        UniSdkUtils.i("UniSDK SdkU3d", "onFailed");
        callback(CALLBACKTYPE_OnFailed, 1, "");
    }

    @Override // com.netease.ntunisdk.base.OnShowViewListener
    public void onClosed() {
        UniSdkUtils.i("UniSDK SdkU3d", "onClosed");
        callback(CALLBACKTYPE_OnClosed, 1, "");
    }

    @Override // com.netease.ntunisdk.base.OnQuestListener
    public void onQuestCompleted(String str) {
        UniSdkUtils.i("UniSDK SdkU3d", "onQuestCompleted");
        callback(CALLBACKTYPE_OnQuestCompleted, 1, str);
    }

    @Override // com.netease.ntunisdk.base.OnPushListener
    public void onSendPushNotificationFinished(boolean z) {
        UniSdkUtils.i("UniSDK SdkU3d", "onSendPushNotificationFinished");
        callback(CALLBACKTYPE_OnSendPushNotificationFinished, 1, Boolean.valueOf(z));
    }

    @Override // com.netease.ntunisdk.base.OnPushListener
    public void onSendLocalNotificationFinished(int i) {
        UniSdkUtils.i("UniSDK SdkU3d", "onSendLocalNotificationFinished");
        callback(CALLBACKTYPE_OnSendLocalNotificationFinished, 1, Integer.valueOf(i));
    }

    @Override // com.netease.ntunisdk.base.OnPushListener
    public void onGetUserPushFinished(boolean z) {
        UniSdkUtils.i("UniSDK SdkU3d", "onGetUserPushFinished");
        callback(CALLBACKTYPE_OnGetUserPushFinished, 1, Boolean.valueOf(z));
    }

    @Override // com.netease.ntunisdk.base.OnPushListener
    public void onSetUserPushFinished(boolean z) {
        UniSdkUtils.i("UniSDK SdkU3d", "onSetUserPushFinished");
        callback(CALLBACKTYPE_OnSetUserPushFinished, 1, Boolean.valueOf(z));
    }

    @Override // com.netease.ntunisdk.base.OnPushListener
    public void onCancelLocalPushFinished(boolean z) {
        UniSdkUtils.i("UniSDK SdkU3d", "onCancelLocalPushFinished");
        callback(CALLBACKTYPE_OnCancelLocalPushFinished, 1, Boolean.valueOf(z));
    }

    @Override // com.netease.ntunisdk.base.OnExtendFuncListener
    public void onExtendFuncCall(String str) {
        UniSdkUtils.i("UniSDK SdkU3d", "onExtendFuncCall");
        callback(CALLBACKTYPE_OnExtendFuncCall, 1, str);
    }

    @Override // com.netease.ntunisdk.base.OnWebViewListener
    public void OnWebViewNativeCall(String str, String str2) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", CALLBACKTYPE_OnWebViewNativeCall);
        try {
            JSONObject jSONObject = new JSONObject(str2);
            jSONObject.put("action", str);
            callback(CALLBACKTYPE_OnWebViewNativeCall, 1, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.netease.ntunisdk.base.OnVerifyListener
    public void onSuccess(int i, String str) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "onSuccess");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("code", i);
            jSONObject.put("message", str);
            callback(CALLBACKTYPE_OnSuccess, 1, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.netease.ntunisdk.base.OnVerifyListener
    public void onFailure(int i, String str) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "onFailure");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("code", i);
            jSONObject.put("message", str);
            callback(CALLBACKTYPE_OnFailure, 1, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.netease.ntunisdk.base.OnCodeScannerListener
    public void codeScannerFinish(int i, String str) throws JSONException {
        UniSdkUtils.i("UniSDK SdkU3d", "codeScannerFinish");
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("code", i);
            jSONObject.put(OneTrackParams.CommonParams.EXTRA, str);
            callback(CALLBACKTYPE_OnCodeScannerFinish, 1, jSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override // com.netease.ntunisdk.base.OnProtocolFinishListener
    public void onProtocolFinish(int i) {
        UniSdkUtils.i("UniSDK SdkU3d", "onProtocolFinish");
        callback(CALLBACKTYPE_OnProtocolFinish, 1, Integer.valueOf(i));
    }

    @Override // com.netease.ntunisdk.base.OnQRCodeListener
    public void createQRCodeDone(String str) {
        UniSdkUtils.i("UniSDK SdkU3d", "createQRCodeDone");
        callback(CALLBACKTYPE_OnCreateQRCodeDone, 1, str);
    }

    public static void initDL() throws NoSuchMethodException, SecurityException, InvocationTargetException {
        try {
            SdkMgr.getDLInst().setContext((Context) a().getField("currentActivity").get(null));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (NoSuchFieldException e4) {
            e4.printStackTrace();
        }
        SdkMgr.getDLInst().setDownloadCallback(new DownloadListener() { // from class: com.netease.ntunisdk.base.SdkU3d.1
            @Override // com.netease.download.listener.DownloadListener
            public final void onProgress(JSONObject jSONObject) {
                UniSdkUtils.i("UniSDK SdkU3d", "onProgress");
                if (SdkU3d.c != null) {
                    SdkU3d.c.onProgress(jSONObject.toString());
                } else {
                    SdkU3d.callback(SdkU3d.CALLBACKTYPE_DL_OnProgress, 1, jSONObject.toString());
                }
            }

            @Override // com.netease.download.listener.DownloadListener
            public final void onFinish(JSONObject jSONObject) {
                UniSdkUtils.i("UniSDK SdkU3d", "onFinish");
                if (SdkU3d.c != null) {
                    Object objOpt = jSONObject.opt("filebytes");
                    SdkU3d.c.onFinish(jSONObject.toString(), new SdkDlBytes(!(objOpt instanceof byte[]) ? null : (byte[]) objOpt));
                } else {
                    SdkU3d.callback(SdkU3d.CALLBACKTYPE_DL_OnFinish, 1, jSONObject.toString());
                }
            }
        });
    }

    public static void extendFuncDL(String str) throws JSONException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        SdkMgr.getDLInst().extendFunc(str);
    }

    public static void setU3dDlCallback(U3dDlCallback u3dDlCallback) {
        c = u3dDlCallback;
    }
}