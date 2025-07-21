package com.netease.ntunisdk.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.KeyEvent;
import java.util.List;

/* loaded from: classes2.dex */
public interface GamerInterface {
    int DRPF(String str);

    void exit();

    String getAppChannel();

    int getAuthType();

    String getAuthTypeName();

    int getCCPerformance();

    String getCCTypeByImsi();

    int getCCWindowState();

    String getChannel();

    String getChannelByImsi();

    String getChannelByImsiEx();

    String getFFChannelByPid(String str);

    String getPayChannel();

    String getPayChannelByPid(String str);

    String getPlatform();

    int getPropInt(String str, int i);

    String getPropStr(String str);

    String getPropStr(String str, String str2);

    String getSDKVersion(String str);

    String getUdid();

    String getUserInfo(String str);

    void handleOnActivityResult(int i, int i2, Intent intent);

    void handleOnBackPressed();

    void handleOnConfigurationChanged(Configuration configuration);

    void handleOnCreate(Bundle bundle);

    boolean handleOnKeyDown(int i, KeyEvent keyEvent);

    void handleOnLowMemory();

    void handleOnNewIntent(Intent intent);

    void handleOnPause();

    void handleOnRequestPermissionsResult(int i, String[] strArr, int[] iArr);

    void handleOnRestart();

    void handleOnResume();

    void handleOnSaveInstanceState(Bundle bundle);

    void handleOnStart();

    void handleOnStop();

    void handleOnUserLeaveHint();

    void handleOnWindowFocusChanged(boolean z);

    boolean hasFeature(String str);

    boolean hasLogin();

    boolean isBinded(String str);

    boolean isCCRecordMic();

    boolean isCCRecording();

    void ntAntiAddiction(String str);

    void ntApplyFriend(String str);

    void ntCCStartService();

    void ntCCStopService();

    void ntCallbackFail(String str);

    void ntCallbackSuccess(String str);

    void ntCancelLocalNotification(int i);

    boolean ntCheckArgs(ShareInfo shareInfo);

    void ntCheckOrder(OrderInfo orderInfo);

    void ntCloseFlash();

    void ntCloseWebView();

    void ntCollectEvent(String str);

    void ntConnectToChannel();

    void ntConsume(OrderInfo orderInfo);

    void ntCreateQRCode(String str, int i, int i2, String str2);

    void ntCreateQRCode(String str, int i, int i2, String str2, String str3);

    void ntDeleteInviters(List<String> list);

    void ntDisConnectFromChannel();

    void ntDisplayAchievement();

    void ntDisplayLeaderboard(String str);

    void ntDisplayQuests(int[] iArr);

    void ntDoSdkRealNameRegister();

    void ntExtendFunc(String str);

    void ntExtendFunc(String str, Object... objArr);

    void ntFlushCustomEvents();

    void ntGameLoginSuccess();

    void ntGetAnnouncementInfo();

    String ntGetChannelID();

    List<OrderInfo> ntGetCheckedOrders();

    void ntGetNotice(boolean z);

    void ntGetUsePushNotification();

    void ntGuestBind();

    boolean ntHasChannelConnected();

    boolean ntHasNotification();

    boolean ntHasPlatform(String str);

    void ntInit(OnFinishInitListener onFinishInitListener);

    void ntInviteFriendList(String str, String str2);

    void ntIsDarenUpdated();

    void ntLogin();

    void ntLogout();

    String ntModulesExtendFunc(String str, String str2);

    void ntMoreGame();

    void ntOpenEchoes();

    void ntOpenExitView();

    void ntOpenManager();

    void ntOpenNearby();

    void ntOpenPauseView();

    void ntOpenWebView(String str);

    void ntPrePay();

    void ntPresentQRCodeScanner();

    void ntPresentQRCodeScanner(String str, int i);

    void ntQueryAvailablesInvitees();

    void ntQueryFriendList();

    void ntQueryFriendListInGame();

    void ntQueryInventory();

    void ntQueryInviterList();

    void ntQueryMyAccount();

    void ntQueryRank(QueryRankInfo queryRankInfo);

    void ntQuerySkuDetails(String str, List<String> list);

    void ntRemoveCheckedOrders(String str);

    void ntScannerQRCode(String str);

    void ntSelectChannelOption(int i);

    void ntSendLocalNotification(String str);

    void ntSendProfile(String str, boolean z);

    void ntSendPushNotification(String str, List<String> list);

    void ntSetFloatBtnVisible(boolean z);

    void ntSetUsePushNotification(boolean z);

    void ntSetUserIdentifier(String str);

    void ntSetZone(String str);

    void ntShare(ShareInfo shareInfo);

    void ntShowBoard(String str, String str2, String str3);

    void ntShowCompactView(boolean z);

    void ntShowConversation();

    void ntShowDaren();

    void ntShowFAQs();

    void ntShowRewardView(List<String> list);

    void ntShowWeb(String str);

    void ntSwitchAccount();

    void ntTrackCustomEvent(String str, String str2);

    void ntUpLoadUserInfo();

    void ntUpdateAchievement(String str, int i);

    void ntUpdateApi(String str, String str2);

    void ntUpdateEvent(String str, int i);

    void ntUpdateRank(String str, double d);

    void ntVerifyMobile(int i);

    void ntVerifyOrder();

    void ntvGenericFunctionCall(String str);

    void setCodeScannerListener(OnCodeScannerListener onCodeScannerListener, int i);

    void setConnectListener(OnConnectListener onConnectListener, int i);

    void setContinueListener(OnContinueListener onContinueListener, int i);

    void setControllerListener(OnControllerListener onControllerListener, int i);

    void setDebugMode(boolean z);

    void setExitListener(OnExitListener onExitListener, int i);

    void setExtendFuncListener(OnExtendFuncListener onExtendFuncListener, int i);

    void setGlView(GLSurfaceView gLSurfaceView);

    void setLoginListener(OnLoginDoneListener onLoginDoneListener, int i);

    void setLogoutListener(OnLogoutDoneListener onLogoutDoneListener, int i);

    void setOnProtocolFinishListener(OnProtocolFinishListener onProtocolFinishListener, int i);

    void setOrderListener(OnOrderCheckListener onOrderCheckListener, int i);

    void setPropInt(String str, int i);

    void setPropStr(String str, String str2);

    void setPushListener(OnPushListener onPushListener, int i);

    void setQRCodeListener(OnQRCodeListener onQRCodeListener, int i);

    void setQueryFriendListener(QueryFriendListener queryFriendListener, int i);

    void setQueryRankListener(QueryRankListener queryRankListener, int i);

    void setQuerySkuDetailsListener(OnQuerySkuDetailsListener onQuerySkuDetailsListener, int i);

    void setQuestListener(OnQuestListener onQuestListener, int i);

    void setReceiveMsgListener(OnReceiveMsgListener onReceiveMsgListener, int i);

    void setShareListener(OnShareListener onShareListener, int i);

    void setShowViewListener(OnShowViewListener onShowViewListener, int i);

    void setStartupListener(OnStartupListener onStartupListener, int i);

    void setUserInfo(String str, String str2);

    void setVerifyListener(OnVerifyListener onVerifyListener, int i);

    void setWebViewListener(OnWebViewListener onWebViewListener, int i);
}