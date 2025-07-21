package com.netease.ntunisdk.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import androidx.core.app.NotificationCompat;
import com.mojang.minecraftpe.SdkCallback;
import com.netease.mpay.httpdns.HttpDns;
import com.netease.mpay.ps.codescanner.CodeScannerConst;
import com.netease.ntunisdk.SdkPersonalInfoList;
import com.netease.ntunisdk.base.JfGas;
import com.netease.ntunisdk.base.RealNameUpdate;
import com.netease.ntunisdk.base.SDKSwitcher;
import com.netease.ntunisdk.base.StartupDialog;
import com.netease.ntunisdk.base.function.ExtendFunc;
import com.netease.ntunisdk.base.function.d;
import com.netease.ntunisdk.base.function.e;
import com.netease.ntunisdk.base.function.g;
import com.netease.ntunisdk.base.function.h;
import com.netease.ntunisdk.base.function.i;
import com.netease.ntunisdk.base.function.k;
import com.netease.ntunisdk.base.utils.ApiRequestUtil;
import com.netease.ntunisdk.base.utils.CachedThreadPoolUtil;
import com.netease.ntunisdk.base.utils.Crypto;
import com.netease.ntunisdk.base.utils.DeviceDataCenter;
import com.netease.ntunisdk.base.utils.HTTPCallback;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import com.netease.ntunisdk.base.utils.HashUtil;
import com.netease.ntunisdk.base.utils.LifeCycleChecker;
import com.netease.ntunisdk.base.utils.LoadingDialog;
import com.netease.ntunisdk.base.utils.NetConnectivity;
import com.netease.ntunisdk.base.utils.NetUtil;
import com.netease.ntunisdk.base.utils.NtUniSDKConfigUtil;
import com.netease.ntunisdk.base.utils.StrUtil;
import com.netease.ntunisdk.base.utils.WgetDoneCallback;
import com.netease.ntunisdk.base.utils.clientlog.ChannelVersionsLog;
import com.netease.ntunisdk.base.utils.clientlog.ClientLog;
import com.netease.ntunisdk.base.utils.clientlog.ClientLogUtils;
import com.netease.ntunisdk.base.utils.clientlog.MCountProxy;
import com.netease.ntunisdk.base.utils.drpf.DRPF;
import com.netease.ntunisdk.cutout.RespUtil;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.modules.api.ModulesCallback;
import com.netease.ntunisdk.modules.api.ModulesManager;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.unilogger.global.Const;
import com.netease.rnccplayer.VideoViewManager;
import com.tencent.connect.common.Constants;
import com.tencent.open.SocialConstants;
import com.tencent.open.SocialOperation;
import com.wali.gamecenter.report.ReportOrigin;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import com.xiaomi.onetrack.OneTrack;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;
import tv.danmaku.ijk.media.player.DecoderConfig;

/* loaded from: classes2.dex */
public abstract class SdkBase implements SurfaceHolder.Callback, GamerInterface {
    public static final int DRPF_ERR_INVALID_INPUT_JSON = 5;
    public static final int DRPF_ERR_JSON = 4;
    public static final int DRPF_ERR_NO_PROJECT = 1;
    public static final int DRPF_ERR_NO_SOURCE = 2;
    public static final int DRPF_ERR_NO_TYPE = 3;
    public static final int DRPF_SUCCESS = 0;
    public static final String HTTP_QUEUE_LOG = "LOG";
    public static final String HTTP_QUEUE_UNISDK = "UniSDK";
    protected static String IMEI = null;
    private static int L = 0;
    protected static final String ORDERS_CREATED_PREFIX = "OrdersCreated_";
    protected static final String ORDERS_ENCRYPTED_PREFIX = "OrdersEncrypted_";
    protected static final long ORDER_TTL = 604800;
    private static String P = "\u4e3a\u652f\u6301\u8ba1\u8d39\u7cfb\u7edf\u7684\u652f\u4ed8\u5bf9\u8d26, UniSDK\u4f1a\u4e0a\u4f20\u9996\u6b21\u6253\u5f00\u6e38\u620f\u65e5\u5fd7\u548c\u5145\u503c\u6210\u529f\u65e5\u5fd7\u5230\u8ba1\u8d39 \n\u6e38\u620f\u9700\u8981\u5728UniPack\u9879\u76ee\u53c2\u6570\u4e2d\u914d\u7f6e\u8ba1\u8d39\u76f8\u5173\u53c2\u6570\u6216\u8005\u5728\u6bcd\u5305\u91cc\u9762SdkMgr.init()\u4e4b\u540e\uff0cSdkMgr.getInst().ntInit()\u4e4b\u524d\u8c03\u7528\u4e0b\u9762\u7684\u63a5\u53e3\uff1a \nSdkMgr.getInst().setPropStr(ConstProp.JF_GAMEID, \"\u4ece\u8ba1\u8d39Jelly\u83b7\u53d6\u7684gameid\"); \nSdkMgr.getInst().setPropStr(ConstProp.JF_LOG_KEY, \"\u4ece\u8ba1\u8d39Jelly\u83b7\u53d6\u7684log_key\"); \n";
    private static boolean Q = false;
    protected static final int SDK_MODE_GAMESERVER = 0;
    protected static final int SDK_MODE_NO_GAMESERVER = 1;
    protected static final String UNISDK_FIRST_OPEN = "UniSDK_FirstOpen";
    public static boolean hasChangeLocation;
    public static Object hasFeatureLock = new Object();
    private String A;
    private long C;
    private long D;
    private long E;
    private long F;
    private long G;
    private long H;
    private PayChannelManager K;
    protected Context myCtx = null;

    /* renamed from: a */
    private OnStartupListener f1633a = null;
    private OnLoginDoneListener loginListener = null;
    private OnLoginDoneListener b = null;
    private OnOrderCheckListener orderListener = null;
    private OnOrderCheckListener c = null;
    private OnLogoutDoneListener logoutListener = null;
    private OnLeaveSdkListener d = null;
    private OnContinueListener e = null;
    private OnReceiveMsgListener f = null;
    private OnExitListener g = null;
    private QueryFriendListener h = null;
    private QueryRankListener i = null;
    private OnQuestListener j = null;
    private OnShareListener k = null;
    private OnPushListener l = null;
    private OnQuerySkuDetailsListener querySkuDetailsListener = null;
    private OnControllerListener m = null;
    private OnShowViewListener n = null;
    private OnConnectListener o = null;
    private OnVerifyListener p = null;
    private OnCodeScannerListener q = null;
    private OnQRCodeListener r = null;
    private OnExtendFuncListener s = null;
    protected long uiThreadId = 0;
    private GLSurfaceView t = null;
    private BlockingQueue<Runnable> u = new LinkedBlockingQueue();
    private boolean v = false;
    private Map<String, String> w = new HashMap();
    protected Map<String, SdkBase> sdkInstMap = new ConcurrentHashMap();
    protected Map<String, SdkBase> loginSdkInstMap = new ConcurrentHashMap();
    private List<String> x = new CopyOnWriteArrayList();
    private Map<String, String> y = new HashMap();
    private Map<String, String> z = new HashMap();
    private OnProtocolFinishListener B = null;
    private boolean I = false;
    private List<String> J = new ArrayList();
    private ChannelVersionsLog T = null;
    private HTTPCallback M = new HTTPCallback() { // from class: com.netease.ntunisdk.base.SdkBase.84
        AnonymousClass84() {
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String str, String str2) {
            UniSdkUtils.i("UniSDK Base", String.format("processResult result=%s, transParam=%s", str, str2));
            if (TextUtils.isEmpty(str)) {
                return true;
            }
            if ("DRPF".equals(str2) || "DETECT".equals(str2) || "SDC".equals(str2) || ConstProp.JF_PAY_LOG_URL.equals(str2)) {
                return false;
            }
            ConstProp.JF_CLIENT_LOG_URL.equals(str2);
            return false;
        }
    };
    private Hashtable<String, String> N = new Hashtable<>();
    protected boolean hasInit = false;
    private AtomicInteger R = new AtomicInteger(-1);
    private AtomicInteger S = new AtomicInteger(3);
    private Hashtable<String, String> O = new Hashtable<>();

    public void antiAddiction(String str) {
    }

    public void applyFriend(String str) {
    }

    public void callbackFail(String str) {
    }

    public void callbackSuccess(String str) {
    }

    protected void cancelLocalNotification(int i) {
    }

    public void ccStartService() {
    }

    public void ccStopService() {
    }

    protected void channelSpecialParams(String str) {
    }

    public boolean checkArgs(ShareInfo shareInfo) {
        return true;
    }

    public abstract void checkOrder(OrderInfo orderInfo);

    public void checkRealName(int i) {
    }

    protected void collectEvent(String str) {
    }

    protected void connectToChannel() {
    }

    protected void consume(OrderInfo orderInfo) {
    }

    public void createQRCode(String str, int i, int i2, String str2) {
    }

    public void createQRCode(String str, int i, int i2, String str2, String str3) {
    }

    protected void deleteInviters(List<String> list) {
    }

    public void disConnectFromChannel() {
    }

    public void displayAchievement() {
    }

    public void displayLeaderboard(String str) {
    }

    public void displayQuests(int[] iArr) {
    }

    public void doSdkRealNameRegister() {
    }

    protected void doSepcialConfigVal(JSONObject jSONObject) {
    }

    public void extendFunc(String str) {
    }

    public void extendFunc(String str, Object... objArr) {
    }

    public void finishLoginDoneBeforeCb(int i) {
    }

    protected void flushCustomEvents() {
    }

    protected void gameLoginSuccess() {
    }

    public void getAnnouncementInfo() {
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public int getAuthType() {
        return 1;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getAuthTypeName() {
        return "native";
    }

    public abstract String getChannel();

    protected String getChannelID() {
        return null;
    }

    public String getChannelPropStr(String str) {
        return null;
    }

    public Map<String, String> getJfQueryOrderlMap(OrderInfo orderInfo) {
        return null;
    }

    public abstract String getLoginSession();

    public abstract String getLoginUid();

    protected void getNotice(boolean z) {
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getPlatform() {
        return "ad";
    }

    public String getSDKVersion() {
        return "";
    }

    protected String getUniSDKVersion() {
        return "";
    }

    protected void getUsePushNotification() {
    }

    public void guestBind() {
    }

    protected boolean hasChannelConnected() {
        return false;
    }

    public boolean hasNotification() {
        return false;
    }

    public boolean hasPlatform(String str) {
        return false;
    }

    public abstract void init(OnFinishInitListener onFinishInitListener);

    protected void inviteFriendList(String str, String str2) {
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public boolean isBinded(String str) {
        return true;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public boolean isCCRecordMic() {
        return false;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public boolean isCCRecording() {
        return false;
    }

    public void isDarenUpdated() {
    }

    public abstract void login();

    public abstract void logout();

    public void moreGame() {
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntvGenericFunctionCall(String str) {
    }

    public boolean openExitView() {
        return false;
    }

    public void openGmView() {
    }

    public abstract void openManager();

    public void openNearby() {
    }

    public void openPauseView() {
    }

    public void prePay() {
    }

    public void presentQRCodeScanner() {
    }

    public void presentQRCodeScanner(String str, int i) {
    }

    public void queryAvailablesInvitees() {
    }

    public void queryFriendList() {
    }

    public void queryFriendListInGame() {
    }

    protected void queryInventory() {
    }

    protected void queryInventory4PromoCodes() {
    }

    protected void queryInviterList() {
    }

    public void queryMyAccount() {
    }

    public void queryRank(QueryRankInfo queryRankInfo) {
    }

    public void querySkuDetails(String str, List<String> list) {
    }

    public void removeCheckedOrders(String str) {
    }

    public void scannerQRCode(String str) {
    }

    public void sdkOnActivityResult(int i, int i2, Intent intent) {
    }

    public void sdkOnBackPressed() {
    }

    public void sdkOnConfigurationChanged(Configuration configuration) {
    }

    public void sdkOnCreate(Bundle bundle) {
    }

    public boolean sdkOnKeyDown(int i, KeyEvent keyEvent) {
        return false;
    }

    public void sdkOnLowMemory() {
    }

    public void sdkOnNewIntent(Intent intent) {
    }

    public void sdkOnPause() {
    }

    public void sdkOnRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
    }

    public void sdkOnRestart() {
    }

    public void sdkOnRestoreInstanceState(Bundle bundle) {
    }

    public void sdkOnResume() {
    }

    public void sdkOnSaveInstanceState(Bundle bundle) {
    }

    public void sdkOnStart() {
    }

    public void sdkOnStop() {
    }

    public void sdkOnUserLeaveHint() {
    }

    public void sdkOnWindowFocusChanged(boolean z) {
    }

    protected void selectChannelOption(int i) {
    }

    protected void sendLocalNotification(String str) {
    }

    protected void sendProfile(String str, boolean z) {
    }

    protected void sendPushNotification(String str, List<String> list) {
    }

    public void setDebugMode(boolean z) {
    }

    public void setFloatBtnVisible(boolean z) {
    }

    protected void setUsePushNotification(boolean z) {
    }

    public void setUserIdentifier(String str) {
    }

    protected void setZone(String str) {
    }

    public void share(ShareInfo shareInfo) {
    }

    protected boolean showAASDialog(String str, String str2) {
        return false;
    }

    protected void showBoard(String str, String str2, String str3) {
    }

    public void showCompactView(boolean z) {
    }

    public void showConversation() {
    }

    public void showDaren() {
    }

    public void showFAQs() {
    }

    protected void showRewardView(List<String> list) {
    }

    protected void showWeb(String str) {
    }

    protected String specialShareChannel(ShareInfo shareInfo) {
        return "";
    }

    public void switchAccount() {
    }

    protected void trackCustomEvent(String str, String str2) {
    }

    public abstract void upLoadUserInfo();

    public void updateAchievement(String str, int i) {
    }

    public void updateApi(String str, String str2) {
    }

    public void updateEvent(String str, int i) {
    }

    public void updateRank(String str, double d) {
    }

    public void verifyMobile(int i) {
    }

    protected void runOnGLThread(Runnable runnable) {
        if (this.v) {
            this.t.queueEvent(runnable);
        } else {
            this.u.add(runnable);
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (this.v) {
            Iterator it = this.u.iterator();
            while (it.hasNext()) {
                this.t.queueEvent((Runnable) it.next());
            }
            this.u.clear();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.v = true;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.v = false;
    }

    public SdkBase(Context context) {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:285:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:287:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:328:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:356:0x026f A[Catch: IOException -> 0x0272, TRY_ENTER, TRY_LEAVE, TryCatch #1 {IOException -> 0x0272, blocks: (B:356:0x026f, B:341:0x0237), top: B:431:0x01d6 }] */
    /* JADX WARN: Removed duplicated region for block: B:359:0x0278  */
    /* JADX WARN: Removed duplicated region for block: B:390:0x05f8  */
    /* JADX WARN: Removed duplicated region for block: B:393:0x060b  */
    /* JADX WARN: Removed duplicated region for block: B:395:0x0615  */
    /* JADX WARN: Removed duplicated region for block: B:400:0x0627  */
    /* JADX WARN: Removed duplicated region for block: B:441:0x028c A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:443:0x05b6 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:450:0x01e5 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:455:0x00d7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r13v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v1 */
    /* JADX WARN: Type inference failed for: r13v10, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r13v11 */
    /* JADX WARN: Type inference failed for: r13v12 */
    /* JADX WARN: Type inference failed for: r13v14, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r13v15 */
    /* JADX WARN: Type inference failed for: r13v16 */
    /* JADX WARN: Type inference failed for: r13v17 */
    /* JADX WARN: Type inference failed for: r13v18 */
    /* JADX WARN: Type inference failed for: r13v19 */
    /* JADX WARN: Type inference failed for: r13v2 */
    /* JADX WARN: Type inference failed for: r13v20 */
    /* JADX WARN: Type inference failed for: r13v21 */
    /* JADX WARN: Type inference failed for: r13v22 */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r13v4 */
    /* JADX WARN: Type inference failed for: r13v5 */
    /* JADX WARN: Type inference failed for: r13v6, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r13v7 */
    /* JADX WARN: Type inference failed for: r13v8 */
    /* JADX WARN: Type inference failed for: r21v0 */
    /* JADX WARN: Type inference failed for: r21v1 */
    /* JADX WARN: Type inference failed for: r21v10 */
    /* JADX WARN: Type inference failed for: r21v11, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r21v12 */
    /* JADX WARN: Type inference failed for: r21v13 */
    /* JADX WARN: Type inference failed for: r21v15 */
    /* JADX WARN: Type inference failed for: r21v16 */
    /* JADX WARN: Type inference failed for: r21v19 */
    /* JADX WARN: Type inference failed for: r21v2, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r21v20 */
    /* JADX WARN: Type inference failed for: r21v21 */
    /* JADX WARN: Type inference failed for: r21v28 */
    /* JADX WARN: Type inference failed for: r21v29 */
    /* JADX WARN: Type inference failed for: r21v3 */
    /* JADX WARN: Type inference failed for: r21v34 */
    /* JADX WARN: Type inference failed for: r21v4 */
    /* JADX WARN: Type inference failed for: r21v5, types: [java.io.InputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setCtx(android.content.Context r25) {
        /*
            Method dump skipped, instructions count: 1629
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.setCtx(android.content.Context):void");
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$1 */
    final class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.uiThreadId = Thread.currentThread().getId();
        }
    }

    private String b() throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.myCtx.getAssets().open("com.netease.apk_distro/config.json")));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line != null) {
                    sb.append(line);
                } else {
                    return new JSONObject(sb.toString()).getString("distro_id");
                }
            }
        } catch (IOException | JSONException unused) {
            return null;
        }
    }

    public boolean c() {
        UniSdkUtils.d("UniSDK Base", "hasFeature(ConstProp.REQUEST_CMCC_PAYTYPE)=" + hasFeature(ConstProp.REQUEST_CMCC_PAYTYPE));
        UniSdkUtils.d("UniSDK Base", "getCCTypeByImsi()=" + getCCTypeByImsi());
        UniSdkUtils.d("UniSDK Base", "contain_mm=" + this.sdkInstMap.containsKey("mm_10086"));
        UniSdkUtils.d("UniSDK Base", "contain_g=" + this.sdkInstMap.containsKey("g_10086"));
        return (SdkMgr.getInst().hasFeature(ConstProp.REQUEST_CMCC_PAYTYPE) && getCCTypeByImsi().equals(ConstProp.CCTYPE_CMCC) && Boolean.valueOf(this.sdkInstMap.containsKey("mm_10086") || getChannel().equals("mm_10086")).booleanValue() && Boolean.valueOf(this.sdkInstMap.containsKey("g_10086") || getChannel().equals("g_10086")).booleanValue()) || getChannel().equals("9game");
    }

    final void a(Map<String, SdkBase> map) {
        if (map != null) {
            this.sdkInstMap.putAll(map);
        }
    }

    public Map<String, SdkBase> getSdkInstMap() {
        return this.sdkInstMap;
    }

    public Map<String, SdkBase> getLoginSdkInstMap() {
        return this.loginSdkInstMap;
    }

    final void b(Map<String, SdkBase> map) {
        if (map != null) {
            this.loginSdkInstMap.putAll(map);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getUdid() {
        return getPropStr(ConstProp.UDID, "unknown");
    }

    public String getInnerGameId() {
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.YY_GAMEID);
        return TextUtils.isEmpty(propStr) ? SdkMgr.getInst().getPropStr("JF_GAMEID") : propStr;
    }

    protected void doConfigVal(JSONObject jSONObject, String str) throws JSONException {
        doConfigVal(jSONObject, str, true);
    }

    protected void doConfigVal(JSONObject jSONObject, String str, boolean z) throws JSONException {
        String strValidate;
        if (jSONObject.has(str)) {
            try {
                strValidate = jSONObject.getString(str);
            } catch (JSONException unused) {
                UniSdkUtils.d("UniSDK Base", "no tag:".concat(String.valueOf(str)));
                strValidate = null;
            }
            if (strValidate == null || getPropStr(str) != null) {
                return;
            }
            UniSdkUtils.d("UniSDK Base", "doConfigVal: " + str + "--->" + strValidate);
            if (z) {
                strValidate = StrUtil.validate(strValidate);
            }
            if (ConstProp.APP_CHANNEL.equalsIgnoreCase(str)) {
                this.N.put(ConstProp.APP_CHANNEL, strValidate);
            } else {
                setPropStr(str, strValidate);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$80 */
    final class AnonymousClass80 implements NtUniSDKConfigUtil.SdkBaseHandle {
        AnonymousClass80() {
        }

        @Override // com.netease.ntunisdk.base.utils.NtUniSDKConfigUtil.SdkBaseHandle
        public final void setPropStr(String str, String str2) throws JSONException {
            SdkBase.this.setPropStr(str, str2);
        }

        @Override // com.netease.ntunisdk.base.utils.NtUniSDKConfigUtil.SdkBaseHandle
        public final void doConfigVal(JSONObject jSONObject, String str, boolean z) throws JSONException {
            SdkBase.this.doConfigVal(jSONObject, str, z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$91 */
    final class AnonymousClass91 implements Runnable {
        AnonymousClass91() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.N.put("APP_PACKAGE_NAME", UniSdkUtils.getAppPackageName(SdkBase.this.myCtx));
            Hashtable hashtable = SdkBase.this.N;
            StringBuilder sb = new StringBuilder();
            sb.append(UniSdkUtils.getAppVersionCode(SdkBase.this.myCtx));
            hashtable.put("APP_VERSION_CODE", sb.toString());
            SdkBase.this.N.put("APP_VERSION_NAME", UniSdkUtils.getAppVersionName(SdkBase.this.myCtx));
            SdkBase.this.N.put("IS_EMULATOR", UniSdkUtils.isEmulator(SdkBase.this.myCtx) ? "1" : "0");
            SdkBase.this.N.put("IS_MUMU", UniSdkUtils.isMuMu() ? "1" : "0");
            SdkBase.this.N.put("IS_DSEMULATOR", UniSdkUtils.isDsEmulator() ? "1" : "0");
            SdkBase.this.N.put("IS_DEVICE_ROOTED", UniSdkUtils.isDeviceRooted() ? "1" : "0");
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setLoginListener(OnLoginDoneListener onLoginDoneListener, int i) throws JSONException {
        this.loginListener = onLoginDoneListener;
        setPropInt(ConstProp.LOGIN_CALLER_THREAD, i);
    }

    public OnLoginDoneListener getLoginListener() {
        return this.loginListener;
    }

    public void setWebLoginByCodeScannerListener(OnLoginDoneListener onLoginDoneListener, int i) throws JSONException {
        Iterator<String> it = this.loginSdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.loginSdkInstMap.get(it.next()).setWebLoginByCodeScannerListener(onLoginDoneListener, i);
        }
        this.b = onLoginDoneListener;
        setPropInt(ConstProp.WEB_LOGIN_CALLER_THREAD, i);
    }

    public void setWebOrderByCodeScannerListener(OnOrderCheckListener onOrderCheckListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setWebOrderByCodeScannerListener(onOrderCheckListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setWebOrderByCodeScannerListener(onOrderCheckListener, i);
        }
        this.c = onOrderCheckListener;
        setPropInt(ConstProp.ORDER_CALLER_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setStartupListener(OnStartupListener onStartupListener, int i) throws JSONException {
        Iterator<String> it = this.loginSdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.loginSdkInstMap.get(it.next()).setStartupListener(onStartupListener, i);
        }
        this.f1633a = onStartupListener;
        setPropInt(ConstProp.LOGIN_CALLER_THREAD, i);
    }

    public void startupDone() {
        if (this.f1633a == null) {
            UniSdkUtils.e("UniSDK Base", "startupListener not set");
        } else if (getPropInt(ConstProp.CONTINUE_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.102
                AnonymousClass102() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.f1633a.startupDone();
                }
            });
        } else {
            this.f1633a.startupDone();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$102 */
    final class AnonymousClass102 implements Runnable {
        AnonymousClass102() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.f1633a.startupDone();
        }
    }

    public void onClickSplashDone() {
        if (this.f1633a == null) {
            UniSdkUtils.e("UniSDK Base", "startupListener not set");
        } else if (getPropInt(ConstProp.CONTINUE_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.113
                AnonymousClass113() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.f1633a.onClickSplash();
                }
            });
        } else {
            this.f1633a.onClickSplash();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$113 */
    final class AnonymousClass113 implements Runnable {
        AnonymousClass113() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.f1633a.onClickSplash();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setLogoutListener(OnLogoutDoneListener onLogoutDoneListener, int i) throws JSONException {
        Iterator<String> it = this.loginSdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.loginSdkInstMap.get(it.next()).setLogoutListener(onLogoutDoneListener, i);
        }
        this.logoutListener = onLogoutDoneListener;
        setPropInt(ConstProp.LOGOUT_CALLER_THREAD, i);
    }

    public OnLogoutDoneListener getLogoutListener() {
        return this.logoutListener;
    }

    public void setLeaveSdkListener(OnLeaveSdkListener onLeaveSdkListener, int i) throws JSONException {
        Iterator<String> it = this.loginSdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.loginSdkInstMap.get(it.next()).setLeaveSdkListener(onLeaveSdkListener, i);
        }
        this.d = onLeaveSdkListener;
        setPropInt(ConstProp.LEAVE_SDK_CALLER_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setOrderListener(OnOrderCheckListener onOrderCheckListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setOrderListener(onOrderCheckListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setOrderListener(onOrderCheckListener, i);
        }
        this.orderListener = onOrderCheckListener;
        setPropInt(ConstProp.ORDER_CALLER_THREAD, i);
    }

    public OnOrderCheckListener getOrderListener() {
        return this.orderListener;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setContinueListener(OnContinueListener onContinueListener, int i) throws JSONException {
        Iterator<String> it = this.loginSdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.loginSdkInstMap.get(it.next()).setContinueListener(onContinueListener, i);
        }
        this.e = onContinueListener;
        setPropInt(ConstProp.CONTINUE_CALLER_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setReceiveMsgListener(OnReceiveMsgListener onReceiveMsgListener, int i) throws JSONException {
        Iterator<String> it = this.loginSdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.loginSdkInstMap.get(it.next()).setReceiveMsgListener(onReceiveMsgListener, i);
        }
        this.f = onReceiveMsgListener;
        setPropInt(ConstProp.CONTINUE_CALLER_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setExitListener(OnExitListener onExitListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setExitListener(onExitListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setExitListener(onExitListener, i);
        }
        this.g = onExitListener;
        setPropInt(ConstProp.EXIT_CALLER_THREAD, i);
    }

    public void webLoginByCodeScanner() {
        webLoginByCodeScannerDone(0);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntLogin() throws Throwable {
        UniSdkUtils.d("UniSDK Base", "ntLogin");
        if (TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.ORIGIN_APP_CHANNEL))) {
            SdkMgr.getInst().setPropStr(ConstProp.ORIGIN_APP_CHANNEL, SdkMgr.getInst().getAppChannel());
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "login_start_base");
        } catch (JSONException e) {
            UniSdkUtils.d("UniSDK Base", "extraJson:" + e.getMessage());
        }
        saveClientLog(null, jSONObject.toString());
        Q = false;
        ClientLog.getInst().startUniTransaction(this.myCtx);
        setPropInt(ConstProp.SDK_LOGINING, 1);
        setPropStr(ConstProp.NT_ERROR_CODE, "");
        setPropStr(ConstProp.NT_ERROR_SUB_CODE, "");
        setPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, "");
        if (SdkMgr.getInst().hasFeature(ConstProp.ENABLE_CHANGE_LOCATION) && this.myCtx != null && getSharedPref().getBoolean(UNISDK_FIRST_OPEN, true)) {
            saveLogToJFOnOpen();
            getSharedPref().edit().putBoolean(UNISDK_FIRST_OPEN, false).commit();
        }
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).e();
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).e();
        }
        e();
        if (useNewSdkProcedure()) {
            dispatchDrpf(g.a(), "LoginUI");
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.124

            /* renamed from: a */
            final /* synthetic */ long f1659a;

            AnonymousClass124(long j) {
                j = j;
            }

            @Override // java.lang.Runnable
            public final void run() {
                UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                SdkBase.this.E = System.currentTimeMillis();
                if (SdkBase.this.hasGuestLogined()) {
                    SdkBase.this.sdkInstMap.get("ngguest").login();
                } else {
                    SdkBase.this.login();
                }
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$124 */
    final class AnonymousClass124 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ long f1659a;

        AnonymousClass124(long j) {
            j = j;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
            SdkBase.this.E = System.currentTimeMillis();
            if (SdkBase.this.hasGuestLogined()) {
                SdkBase.this.sdkInstMap.get("ngguest").login();
            } else {
                SdkBase.this.login();
            }
        }
    }

    private String d() throws Throwable {
        String propStr = SdkMgr.getInst().getPropStr("JF_GAMEID");
        String str = "";
        if (TextUtils.isEmpty(propStr)) {
            StrUtil.showAlertDialog((Activity) this.myCtx, "", "\u8bf7\u5728assets\\xxx_data\u4e2d\u914d\u7f6eJF_GAMEID");
        }
        String propStr2 = getPropStr(ConstProp.SESSION);
        String sDKVersion = getSDKVersion(getChannel());
        StringBuilder sb = new StringBuilder();
        try {
            Object[] objArr = new Object[8];
            objArr[0] = propStr;
            objArr[1] = getChannel();
            objArr[2] = getAppChannel();
            objArr[3] = getPlatform();
            objArr[4] = getPropStr(ConstProp.UID) + "@" + getPlatform() + "." + getChannel() + ".win.163.com";
            objArr[5] = getUdid();
            String strEncode = "null";
            objArr[6] = propStr2 == null ? "null" : URLEncoder.encode(propStr2, "UTF-8");
            if (sDKVersion != null) {
                strEncode = URLEncoder.encode(sDKVersion, "UTF-8");
            }
            objArr[7] = strEncode;
            sb.append(String.format("gameid=%s&login_channel=%s&app_channel=%s&platform=%s&username=%s&udid=%s&sessionid=%s&sdk_version=%s", objArr));
            String propStr3 = getPropStr(ConstProp.LOCAL_IP, "127.0.0.1");
            sb.append("&ip=");
            sb.append(URLEncoder.encode(propStr3, "UTF-8"));
            if (!TextUtils.isEmpty(getPropStr(ConstProp.JF_AIM_INFO_2))) {
                sb.append("&aim_info=");
                sb.append(URLEncoder.encode(getPropStr(ConstProp.JF_AIM_INFO_2), "UTF-8"));
            }
            for (String str2 : this.z.keySet()) {
                sb.append(com.alipay.sdk.m.s.a.l);
                sb.append(str2);
                sb.append("=");
                sb.append(URLEncoder.encode(this.z.get(str2), "UTF-8"));
            }
            if (hasGuestLogined()) {
                sb.append("&is_unisdk_guest=1");
            }
            sb.append("&client_login_sn=");
            sb.append(SdkMgr.getInst().getPropStr(ConstProp.CLIENT_LOGIN_SN));
            String propStr4 = getPropStr(ConstProp.PRI_SAC);
            String propStr5 = getPropStr(ConstProp.PRI_SP);
            sb.append("&source_app_channel=");
            if (TextUtils.isEmpty(propStr4)) {
                propStr4 = "";
            }
            sb.append(propStr4);
            sb.append("&source_platform=");
            if (!TextUtils.isEmpty(propStr5)) {
                str = propStr5;
            }
            sb.append(str);
            if (SdkMgr.getInst().hasFeature(ConstProp.ENABLE_CHANGE_LOCATION)) {
                String propStr6 = SdkMgr.getInst().getPropStr(ConstProp.PLAYER_REGION);
                String propStr7 = SdkMgr.getInst().getPropStr(ConstProp.PLAYER_COUNTRYCODE);
                String propStr8 = SdkMgr.getInst().getPropStr(ConstProp.PLAYER_PROVINCECODE);
                if (!TextUtils.isEmpty(propStr6)) {
                    sb.append("&law_region_code=");
                    sb.append(propStr6);
                }
                if (!TextUtils.isEmpty(propStr7)) {
                    sb.append("&area_code=");
                    sb.append(propStr7);
                }
                if (!TextUtils.isEmpty(propStr8)) {
                    sb.append("&province_code=");
                    sb.append(propStr8);
                }
            }
        } catch (UnsupportedEncodingException e) {
            UniSdkUtils.d("UniSDK Base", "UnsupportedEncodingException" + e.getMessage());
        }
        UniSdkUtils.d("UniSDK Base", "SAUTH_STR:" + sb.toString());
        return sb.toString();
    }

    private String getJFSauthJson() throws Throwable {
        String propStr = getPropStr("JF_GAMEID");
        String str = "";
        if (TextUtils.isEmpty(propStr)) {
            StrUtil.showAlertDialog((Activity) this.myCtx, "", "\u8bf7\u5728assets\\xxx_data\u4e2d\u914d\u7f6eJF_GAMEID");
        }
        String propStr2 = getPropStr(ConstProp.SESSION);
        String sDKVersion = getSDKVersion(getChannel());
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("gameid", propStr);
            jSONObject.put("login_channel", getChannel());
            jSONObject.put(Const.APP_CHANNEL, getAppChannel());
            jSONObject.put("platform", getPlatform());
            jSONObject.put("sdkuid", getPropStr(ConstProp.UID));
            jSONObject.put("udid", getUdid());
            jSONObject.put("sessionid", propStr2);
            jSONObject.put("sdk_version", sDKVersion);
            jSONObject.putOpt("is_unisdk_guest", Integer.valueOf(hasGuestLogined() ? 1 : 0));
            jSONObject.put("ip", getPropStr(ConstProp.LOCAL_IP, "127.0.0.1"));
            jSONObject.put("aim_info", getPropStr(ConstProp.JF_AIM_INFO_2));
            String propStr3 = getPropStr(ConstProp.PRI_SAC);
            String propStr4 = getPropStr(ConstProp.PRI_SP);
            if (TextUtils.isEmpty(propStr3)) {
                propStr3 = "";
            }
            jSONObject.put("source_app_channel", propStr3);
            if (!TextUtils.isEmpty(propStr4)) {
                str = propStr4;
            }
            jSONObject.put("source_platform", str);
            for (String str2 : this.z.keySet()) {
                jSONObject.put(str2, this.z.get(str2));
            }
            jSONObject.put("client_login_sn", SdkMgr.getInst().getPropStr(ConstProp.CLIENT_LOGIN_SN));
            if (SdkMgr.getInst().hasFeature(ConstProp.ENABLE_CHANGE_LOCATION)) {
                String propStr5 = SdkMgr.getInst().getPropStr(ConstProp.PLAYER_REGION);
                String propStr6 = SdkMgr.getInst().getPropStr(ConstProp.PLAYER_COUNTRYCODE);
                String propStr7 = SdkMgr.getInst().getPropStr(ConstProp.PLAYER_PROVINCECODE);
                if (!TextUtils.isEmpty(propStr5)) {
                    jSONObject.put("law_region_code", propStr5);
                }
                if (!TextUtils.isEmpty(propStr6)) {
                    jSONObject.put("area_code", propStr6);
                }
                if (!TextUtils.isEmpty(propStr7)) {
                    jSONObject.put("province_code", propStr7);
                }
            }
        } catch (JSONException e) {
            UniSdkUtils.e("UniSDK Base", "sauthJson JSONException:" + e.getMessage());
            e.printStackTrace();
        }
        UniSdkUtils.d("UniSDK Base", "SAUTH_JSON:" + jSONObject.toString());
        return jSONObject.toString();
    }

    /* JADX WARN: Removed duplicated region for block: B:119:0x0126  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x014d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void loginDone(int r13) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 352
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.loginDone(int):void");
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$135 */
    final class AnonymousClass135 implements LifeCycleChecker.OnTimeoutListener {
        AnonymousClass135() {
        }

        @Override // com.netease.ntunisdk.base.utils.LifeCycleChecker.OnTimeoutListener
        public final void onTimeout() {
            LoadingDialog.getInstance((Activity) SdkBase.this.myCtx).dismiss();
        }
    }

    protected void genClientLoginSn() {
        SdkMgr.getInst().setPropStr(ConstProp.CLIENT_LOGIN_SN, HashUtil.calculateStrHash("MD5", UniSdkUtils.getDeviceUDID(this.myCtx) + "_" + System.currentTimeMillis() + "_" + String.format(Locale.US, "%09d", Integer.valueOf(new Random().nextInt(1000000000))) + "_" + getPropStr(ConstProp.UID) + "_" + getPropStr(ConstProp.SESSION)));
    }

    public void setLoginSauthInfo() throws JSONException {
        setPropStr(ConstProp.WEB_UID, getPropStr(ConstProp.UID));
        setPropStr(ConstProp.WEB_SESSION, getPropStr(ConstProp.SESSION));
        setPropStr(ConstProp.LOGIN_CHANNEL, getChannel());
        setPropStr(ConstProp.SAUTH_STR, d());
        setPropStr(ConstProp.SAUTH_JSON, getJFSauthJson());
    }

    public void a(int i) throws JSONException {
        this.F = System.currentTimeMillis();
        if (i != 0) {
            if (i == 13) {
                c(getDetectData(38, i, ""));
            } else {
                c(getDetectData(8, i, ""));
            }
        }
        if (10 == i && TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG))) {
            SdkMgr.getInst().setPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, "sdk login error");
        }
        genClientLoginSn();
        SdkMgr.getInst().setPropStr(ConstProp.WEB_LOGIN_STATUS, "0");
        setLoginSauthInfo();
        boolean z = false;
        boolean z2 = i == 0 || i == 13 || i == 130;
        boolean zUseNewSdkProcedure = useNewSdkProcedure();
        boolean zHasFeature = hasFeature(ConstProp.UNI_SAUTH_FALLBACK);
        boolean z3 = 1 == getPropInt(ConstProp.ENABLE_CLIENT_SAUTH, 0);
        boolean zHasFeature2 = hasFeature(ConstProp.OVERSEA_PROJECT);
        boolean z4 = hasFeature(ConstProp.ENABLE_UNI_SAUTH) || SdkMgr.getInst().getPropInt(ConstProp.ENABLE_OVERSEA_CHILD_PROTECT, 0) == 1;
        if (zUseNewSdkProcedure || ((!zHasFeature && !zHasFeature2) || (!zHasFeature && z4))) {
            z = true;
        }
        UniSdkUtils.i("UniSDK Base", "isNoah:" + zUseNewSdkProcedure + ", uniSauthFallback:" + zHasFeature + ", overseaProject:" + zHasFeature2);
        UniSdkUtils.i("UniSDK Base", "should UniSauth: ".concat(String.valueOf(z)));
        if (!z2 || (!z && !z3)) {
            b(i);
        } else if (z) {
            h.a(this.myCtx, this, i);
        } else {
            c(i);
        }
    }

    public void b(int i) throws JSONException {
        if (i == 0 || 13 == i || 130 == i) {
            SdkMgr.getInst().setPropStr(ConstProp.UNISDK_LOGIN_ERR_MSG, "");
            SdkMgr.getInst().setPropStr(ConstProp.NT_ERROR_CODE, "");
            SdkMgr.getInst().setPropStr(ConstProp.NT_ERROR_SUB_CODE, "");
            Q = true;
        } else {
            Q = false;
        }
        LoadingDialog.getInstance((Activity) this.myCtx).dismiss();
        LifeCycleChecker.getInst().setOnTimeoutListener(null);
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            if (sdkBase != null) {
                sdkBase.finishLoginDoneBeforeCb(i);
            }
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
            if (sdkBase2 != null) {
                sdkBase2.finishLoginDoneBeforeCb(i);
            }
        }
        finishLoginDoneBeforeCb(i);
        if (this.loginListener != null) {
            if (getPropInt(ConstProp.LOGIN_CALLER_THREAD, 1) == 2) {
                runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.146

                    /* renamed from: a */
                    final /* synthetic */ int f1678a;

                    AnonymousClass146(int i2) {
                        i = i2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        UniSdkUtils.i("UniSDK Base", "runOnGLThread, loginDone: code=" + i + ", current thread=" + Thread.currentThread().getId());
                        SdkBase.this.loginListener.loginDone(i);
                    }
                });
            } else {
                UniSdkUtils.i("UniSDK Base", "runOnUIThread, loginDone: code=" + i2 + ", current thread=" + Thread.currentThread().getId());
                this.loginListener.loginDone(i2);
            }
        } else {
            UniSdkUtils.e("UniSDK Base", "OnLoginDoneListener not set");
        }
        JSONObject jSONObject = new JSONObject();
        try {
            if (!TextUtils.isEmpty(getPropStr(ConstProp.SAUTH_JSON))) {
                jSONObject.putOpt(ConstProp.SAUTH_JSON, new JSONObject(getPropStr(ConstProp.SAUTH_JSON)));
            }
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, ClientLog.Step.LOGIN_DONE_BASE);
            if (SdkMgr.getInst() != null && !TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.NT_ERROR_CODE)) && TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.NT_ERROR_SUB_CODE))) {
                jSONObject.putOpt("unisdk_code", SdkMgr.getInst().getPropStr(ConstProp.NT_ERROR_CODE));
            } else {
                jSONObject.putOpt("unisdk_code", String.valueOf(i2));
            }
        } catch (JSONException e) {
            UniSdkUtils.d("UniSDK Base", "extraJson:" + e.getMessage());
        }
        saveClientLog(null, jSONObject.toString());
        if (this.T == null) {
            this.T = new ChannelVersionsLog(this.myCtx);
        }
        this.T.send();
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$146 */
    final class AnonymousClass146 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1678a;

        AnonymousClass146(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "runOnGLThread, loginDone: code=" + i + ", current thread=" + Thread.currentThread().getId());
            SdkBase.this.loginListener.loginDone(i);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:93:0x00f6  */
    /* JADX WARN: Removed duplicated region for block: B:95:0x00ff  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void c(int r13) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 370
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.c(int):void");
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$157 */
    final class AnonymousClass157 implements HTTPCallback {

        /* renamed from: a */
        final /* synthetic */ int f1686a;

        AnonymousClass157(int i) {
            i = i;
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String str, String str2) throws JSONException {
            UniSdkUtils.d("UniSDK Base", "clientSauth processResult");
            if (!TextUtils.isEmpty(str)) {
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    if (200 == jSONObject.optInt("code")) {
                        SdkMgr.getInst().setPropStr(ConstProp.UNISDK_LOGIN_JSON, jSONObject.optString("unisdk_login_json"));
                        ((Activity) SdkBase.this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.157.1
                            AnonymousClass1() {
                            }

                            @Override // java.lang.Runnable
                            public final void run() throws JSONException {
                                SdkBase.this.b(i);
                                SdkMgr.getInst().ntGameLoginSuccess();
                            }
                        });
                    } else {
                        UniSdkUtils.e("UniSDK Base", "clientSauth processResult code != 200, result:".concat(String.valueOf(str)));
                        SdkBase.this.b(10);
                    }
                    return false;
                } catch (JSONException e) {
                    UniSdkUtils.d("UniSDK Base", "clientSauth processResult exception:" + e.getMessage());
                }
            }
            SdkBase.this.b(10);
            return false;
        }

        /* renamed from: com.netease.ntunisdk.base.SdkBase$157$1 */
        final class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public final void run() throws JSONException {
                SdkBase.this.b(i);
                SdkMgr.getInst().ntGameLoginSuccess();
            }
        }
    }

    public void sdkCheckRealNameDone(int i) {
        i.a(this, this.z, i);
    }

    public void jfCheckRealNameDone(int i) {
        Context context = this.myCtx;
        if (context != null) {
            ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.2
                final /* synthetic */ int b;

                AnonymousClass2(int i2) {
                    i = i2;
                }

                @Override // java.lang.Runnable
                public final void run() throws JSONException {
                    if (d.a(SdkBase.this, i)) {
                        return;
                    }
                    int i2 = i;
                    if (i2 != 17) {
                        SdkBase.this.b(i2);
                    } else {
                        LoadingDialog.getInstance((Activity) SdkBase.this.myCtx).dismiss();
                        SdkBase.this.logoutDone(i);
                    }
                    if (SdkBase.this.getPropInt(ConstProp.ENABLE_CLIENT_SAUTH, 0) != 0) {
                        SdkMgr.getInst().ntGameLoginSuccess();
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$2 */
    final class AnonymousClass2 implements Runnable {
        final /* synthetic */ int b;

        AnonymousClass2(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() throws JSONException {
            if (d.a(SdkBase.this, i)) {
                return;
            }
            int i2 = i;
            if (i2 != 17) {
                SdkBase.this.b(i2);
            } else {
                LoadingDialog.getInstance((Activity) SdkBase.this.myCtx).dismiss();
                SdkBase.this.logoutDone(i);
            }
            if (SdkBase.this.getPropInt(ConstProp.ENABLE_CLIENT_SAUTH, 0) != 0) {
                SdkMgr.getInst().ntGameLoginSuccess();
            }
        }
    }

    public void finishLoginDone(int i) {
        Context context = this.myCtx;
        if (context != null) {
            ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.13

                /* renamed from: a */
                final /* synthetic */ int f1664a;

                AnonymousClass13(int i2) {
                    i = i2;
                }

                @Override // java.lang.Runnable
                public final void run() throws JSONException {
                    SdkBase.this.b(i);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$13 */
    final class AnonymousClass13 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1664a;

        AnonymousClass13(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() throws JSONException {
            SdkBase.this.b(i);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$24 */
    final class AnonymousClass24 implements HTTPCallback {

        /* renamed from: a */
        final /* synthetic */ int f1720a;

        AnonymousClass24(int i) {
            i = i;
        }

        /* renamed from: com.netease.ntunisdk.base.SdkBase$24$1 */
        final class AnonymousClass1 implements Runnable {

            /* renamed from: a */
            final /* synthetic */ String f1721a;

            AnonymousClass1(String str) {
                str = str;
            }

            @Override // java.lang.Runnable
            public final void run() throws JSONException {
                UniSdkUtils.d("UniSDK Base", "handleChannelSdkUid result:" + str);
                if (!TextUtils.isEmpty(str)) {
                    try {
                        JSONObject jSONObject = new JSONObject(str);
                        if (200 != jSONObject.optInt("code")) {
                            SdkBase.this.a(10);
                            return;
                        }
                        String strOptString = jSONObject.optString("sdkuid");
                        if (!TextUtils.isEmpty(strOptString)) {
                            SdkMgr.getInst().setPropStr(ConstProp.UID, strOptString);
                        }
                        String strOptString2 = jSONObject.optString("gas_token");
                        if (!TextUtils.isEmpty(strOptString2)) {
                            SdkMgr.getInst().setPropStr(ConstProp.UNISDK_JF_GAS_TOKEN, strOptString2);
                        }
                        String strOptString3 = jSONObject.optString("unisdk_login_json");
                        if (!TextUtils.isEmpty(strOptString3)) {
                            String str = new String(Base64.decode(strOptString3, 0), "UTF-8");
                            SdkBase.this.channelSpecialParams(str);
                            JSONObject jSONObject2 = new JSONObject(str);
                            String strOptString4 = jSONObject2.optString(Constants.PARAM_ACCESS_TOKEN);
                            String strOptString5 = jSONObject2.optString(Constants.PARAM_EXPIRES_IN);
                            String strOptString6 = jSONObject2.optString("refresh_token");
                            UniSdkUtils.d("UniSDK Base", "access_token = " + strOptString4 + ", expires_in = " + strOptString5 + ", refresh_token=" + strOptString6);
                            if (!TextUtils.isEmpty(strOptString4)) {
                                SdkMgr.getInst().setPropStr(ConstProp.SESSION, strOptString4);
                            }
                            if (!TextUtils.isEmpty(strOptString5)) {
                                SdkMgr.getInst().setPropStr(ConstProp.TIMESTAMP, strOptString5);
                            }
                            if (!TextUtils.isEmpty(strOptString6)) {
                                SdkMgr.getInst().setPropStr(ConstProp.REFRESH_TOKEN, strOptString6);
                            }
                        }
                        SdkBase.this.a(i);
                        return;
                    } catch (Exception e) {
                        UniSdkUtils.d("UniSDK Base", "channelSpecialParams exception:" + e.getMessage());
                    }
                }
                SdkBase.this.a(10);
            }
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String str, String str2) {
            if (SdkBase.this.myCtx == null) {
                return false;
            }
            ((Activity) SdkBase.this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.24.1

                /* renamed from: a */
                final /* synthetic */ String f1721a;

                AnonymousClass1(String str3) {
                    str = str3;
                }

                @Override // java.lang.Runnable
                public final void run() throws JSONException {
                    UniSdkUtils.d("UniSDK Base", "handleChannelSdkUid result:" + str);
                    if (!TextUtils.isEmpty(str)) {
                        try {
                            JSONObject jSONObject = new JSONObject(str);
                            if (200 != jSONObject.optInt("code")) {
                                SdkBase.this.a(10);
                                return;
                            }
                            String strOptString = jSONObject.optString("sdkuid");
                            if (!TextUtils.isEmpty(strOptString)) {
                                SdkMgr.getInst().setPropStr(ConstProp.UID, strOptString);
                            }
                            String strOptString2 = jSONObject.optString("gas_token");
                            if (!TextUtils.isEmpty(strOptString2)) {
                                SdkMgr.getInst().setPropStr(ConstProp.UNISDK_JF_GAS_TOKEN, strOptString2);
                            }
                            String strOptString3 = jSONObject.optString("unisdk_login_json");
                            if (!TextUtils.isEmpty(strOptString3)) {
                                String str3 = new String(Base64.decode(strOptString3, 0), "UTF-8");
                                SdkBase.this.channelSpecialParams(str3);
                                JSONObject jSONObject2 = new JSONObject(str3);
                                String strOptString4 = jSONObject2.optString(Constants.PARAM_ACCESS_TOKEN);
                                String strOptString5 = jSONObject2.optString(Constants.PARAM_EXPIRES_IN);
                                String strOptString6 = jSONObject2.optString("refresh_token");
                                UniSdkUtils.d("UniSDK Base", "access_token = " + strOptString4 + ", expires_in = " + strOptString5 + ", refresh_token=" + strOptString6);
                                if (!TextUtils.isEmpty(strOptString4)) {
                                    SdkMgr.getInst().setPropStr(ConstProp.SESSION, strOptString4);
                                }
                                if (!TextUtils.isEmpty(strOptString5)) {
                                    SdkMgr.getInst().setPropStr(ConstProp.TIMESTAMP, strOptString5);
                                }
                                if (!TextUtils.isEmpty(strOptString6)) {
                                    SdkMgr.getInst().setPropStr(ConstProp.REFRESH_TOKEN, strOptString6);
                                }
                            }
                            SdkBase.this.a(i);
                            return;
                        } catch (Exception e) {
                            UniSdkUtils.d("UniSDK Base", "channelSpecialParams exception:" + e.getMessage());
                        }
                    }
                    SdkBase.this.a(10);
                }
            });
            return false;
        }
    }

    protected void webLoginByCodeScannerDone(int i) {
        this.E = System.currentTimeMillis();
        if (this.b == null) {
            UniSdkUtils.e("UniSDK Base", "OnWebLoginDoneListener not set");
            return;
        }
        if (i == 0) {
            SdkMgr.getInst().setPropStr(ConstProp.WEB_LOGIN_STATUS, "1");
        }
        if (getPropInt(ConstProp.WEB_LOGIN_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.35

                /* renamed from: a */
                final /* synthetic */ int f1731a;

                AnonymousClass35(int i2) {
                    i = i2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "runOnGLThread, web loginDone: code=" + i + ", current thread=" + Thread.currentThread().getId());
                    SdkBase.this.b.loginDone(i);
                }
            });
            return;
        }
        UniSdkUtils.i("UniSDK Base", "runOnUIThread, web loginDone: code=" + i2 + ", current thread=" + Thread.currentThread().getId());
        this.b.loginDone(i2);
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$35 */
    final class AnonymousClass35 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1731a;

        AnonymousClass35(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "runOnGLThread, web loginDone: code=" + i + ", current thread=" + Thread.currentThread().getId());
            SdkBase.this.b.loginDone(i);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$46 */
    final class AnonymousClass46 implements Runnable {
        AnonymousClass46() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).queryInventory4PromoCodes();
            }
            SdkBase.this.queryInventory4PromoCodes();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntQueryInventory() {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.46
            AnonymousClass46() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).queryInventory4PromoCodes();
                }
                SdkBase.this.queryInventory4PromoCodes();
            }
        });
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCheckOrder(OrderInfo orderInfo) throws JSONException {
        ClientLog.getInst().startUniTransaction(this.myCtx);
        if (!orderInfo.isWebPayment()) {
            if (TextUtils.isEmpty(orderInfo.getUserName()) && !TextUtils.isEmpty(getPropStr(ConstProp.USERINFO_UID))) {
                orderInfo.setUserName(getPropStr(ConstProp.USERINFO_UID));
            }
            if (TextUtils.isEmpty(orderInfo.getServerId()) && !TextUtils.isEmpty(getPropStr(ConstProp.USERINFO_HOSTID))) {
                orderInfo.setServerId(getPropStr(ConstProp.USERINFO_HOSTID));
            }
            if (TextUtils.isEmpty(orderInfo.getUid()) && !TextUtils.isEmpty(getPropStr(ConstProp.UID))) {
                orderInfo.setUid(getPropStr(ConstProp.UID));
            }
            if (TextUtils.isEmpty(orderInfo.getAid()) && !TextUtils.isEmpty(getPropStr(ConstProp.USERINFO_AID))) {
                orderInfo.setAid(getPropStr(ConstProp.USERINFO_AID));
            }
            if (TextUtils.isEmpty(orderInfo.getJfExtInfo()) && !TextUtils.isEmpty(getPropStr(ConstProp.UNISDK_EXT_INFO))) {
                orderInfo.setJfExtInfo(getPropStr(ConstProp.UNISDK_EXT_INFO));
            }
        }
        PayChannelManager payChannelManager = this.K;
        if (payChannelManager != null && payChannelManager.allyPayEnabled()) {
            orderInfo.setOrderChannel("allysdk");
        }
        if (hasFeature("VIRTUAL_ORDER")) {
            orderInfo.setOrderChannel("basechannel");
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "ntCheckOrder_call");
        } catch (JSONException e) {
            UniSdkUtils.d("UniSDK Base", "extraJson:" + e.getMessage());
        }
        saveClientLog(orderInfo, jSONObject.toString());
        a(orderInfo, true);
    }

    public void a(OrderInfo orderInfo, boolean z) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.57

            /* renamed from: a */
            final /* synthetic */ OrderInfo f1749a;
            final /* synthetic */ long c;
            final /* synthetic */ boolean d;

            AnonymousClass57(long j, OrderInfo orderInfo2, boolean z2) {
                j = j;
                orderInfo = orderInfo2;
                z = z2;
            }

            @Override // java.lang.Runnable
            public final void run() throws Throwable {
                UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + SdkBase.this.uiThreadId);
                if (SdkBase.this.K != null && SdkBase.this.K.payDisabled()) {
                    UniSdkUtils.e("UniSDK Base", "get_ff_channel error");
                    orderInfo.setOrderStatus(168);
                    SdkBase.this.checkOrderDone(orderInfo);
                } else {
                    if (k.a(SdkBase.this.myCtx, orderInfo, z)) {
                        return;
                    }
                    if (!SdkBase.this.hasFeature(ConstProp.INNER_MODE_AI_DETECT)) {
                        SdkBase.this.continueOrderSetting(orderInfo, z);
                    } else {
                        com.netease.ntunisdk.base.function.a.a(orderInfo, z);
                    }
                }
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$57 */
    final class AnonymousClass57 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ OrderInfo f1749a;
        final /* synthetic */ long c;
        final /* synthetic */ boolean d;

        AnonymousClass57(long j, OrderInfo orderInfo2, boolean z2) {
            j = j;
            orderInfo = orderInfo2;
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() throws Throwable {
            UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + SdkBase.this.uiThreadId);
            if (SdkBase.this.K != null && SdkBase.this.K.payDisabled()) {
                UniSdkUtils.e("UniSDK Base", "get_ff_channel error");
                orderInfo.setOrderStatus(168);
                SdkBase.this.checkOrderDone(orderInfo);
            } else {
                if (k.a(SdkBase.this.myCtx, orderInfo, z)) {
                    return;
                }
                if (!SdkBase.this.hasFeature(ConstProp.INNER_MODE_AI_DETECT)) {
                    SdkBase.this.continueOrderSetting(orderInfo, z);
                } else {
                    com.netease.ntunisdk.base.function.a.a(orderInfo, z);
                }
            }
        }
    }

    public void continueOrderSetting(OrderInfo orderInfo, boolean z) throws Throwable {
        if (SdkMgr.getInst().hasFeature(ConstProp.SHOW_ORDER_LOADING)) {
            LoadingDialog.getInstance((Activity) this.myCtx).show(SdkMgr.getInst().getPropStr(ConstProp.CHECK_ORDER_INTERVAL, "0"));
        }
        if (useNewSdkProcedure()) {
            e.a((SdkBase) SdkMgr.getInst(), orderInfo);
            return;
        }
        if (!OrderInfo.hasProduct(orderInfo)) {
            if (z && (SdkMgr.getInst().hasFeature(ConstProp.UNISDK_JF_GAS3) || SdkMgr.getInst().hasFeature(ConstProp.UNISDK_JF_GAS3_WEB))) {
                new JfGas(this).queryProduct(new JfGas.QueryProductCallback() { // from class: com.netease.ntunisdk.base.SdkBase.68

                    /* renamed from: a */
                    final /* synthetic */ OrderInfo f1760a;

                    AnonymousClass68(OrderInfo orderInfo2) {
                        orderInfo = orderInfo2;
                    }

                    @Override // com.netease.ntunisdk.base.JfGas.QueryProductCallback
                    public final void callbackResult() {
                        SdkBase.this.a(orderInfo, false);
                    }
                });
                return;
            }
            if (!orderInfo2.isCartOrder()) {
                UniSdkUtils.e("UniSDK Base", "\u9053\u5177\u7f16\u53f7 " + orderInfo2.getProductId() + " \u4e0d\u5b58\u5728\uff0c\u4f7f\u7528gas3\u7684\u6e38\u620f\uff0c\u8bf7\u68c0\u67e5jelly\u4e0a\u9762\u662f\u5426\u5df2\u7ecf\u914d\u7f6e\u597d\u5546\u54c1\u4ee5\u53caUNISDK_JF_GAS3_URL\u662f\u5426\u8bbe\u7f6e\u6b63\u786e\uff1b\u4f7f\u7528gas2\u7684\u6e38\u620f\uff0c\u8bf7\u5148\u901a\u8fc7regProduct\u6ce8\u518c\u5546\u54c1");
                orderInfo2.setOrderStatus(7);
                orderInfo2.setOrderErrReason("\u9053\u5177\u7f16\u53f7 " + orderInfo2.getProductId() + " \u4e0d\u5b58\u5728\uff0c\u4f7f\u7528gas3\u7684\u6e38\u620f\uff0c\u8bf7\u68c0\u67e5jelly\u4e0a\u9762\u662f\u5426\u5df2\u7ecf\u914d\u7f6e\u597d\u5546\u54c1\u4ee5\u53caUNISDK_JF_GAS3_URL\u662f\u5426\u8bbe\u7f6e\u6b63\u786e\uff1b\u4f7f\u7528gas2\u7684\u6e38\u620f\uff0c\u8bf7\u5148\u901a\u8fc7regProduct\u6ce8\u518c\u5546\u54c1");
                checkOrderDone(orderInfo2);
                return;
            }
        }
        if (!SdkMgr.getInst().hasFeature(ConstProp.UNISDK_JF_GAS3) && (!orderInfo2.isWebPayment() || !SdkMgr.getInst().hasFeature(ConstProp.UNISDK_JF_GAS3_WEB))) {
            if (orderInfo2.isWebPayment() || hasFeature(ConstProp.REQUEST_UNISDK_SERVER)) {
                c(orderInfo2);
                return;
            } else {
                a(orderInfo2);
                return;
            }
        }
        new JfGas((SdkBase) SdkMgr.getInst()).createOrder(orderInfo2, null);
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$68 */
    final class AnonymousClass68 implements JfGas.QueryProductCallback {

        /* renamed from: a */
        final /* synthetic */ OrderInfo f1760a;

        AnonymousClass68(OrderInfo orderInfo2) {
            orderInfo = orderInfo2;
        }

        @Override // com.netease.ntunisdk.base.JfGas.QueryProductCallback
        public final void callbackResult() {
            SdkBase.this.a(orderInfo, false);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$77 */
    final class AnonymousClass77 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ OrderInfo f1768a;

        AnonymousClass77(OrderInfo orderInfo) {
            orderInfo = orderInfo;
        }

        /* JADX WARN: Removed duplicated region for block: B:156:0x02b8  */
        /* JADX WARN: Removed duplicated region for block: B:159:0x02d1  */
        /* JADX WARN: Removed duplicated region for block: B:160:0x02d9  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final void run() throws java.lang.Throwable {
            /*
                Method dump skipped, instructions count: 859
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.AnonymousClass77.run():void");
        }
    }

    final void a(OrderInfo orderInfo) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.77

            /* renamed from: a */
            final /* synthetic */ OrderInfo f1768a;

            AnonymousClass77(OrderInfo orderInfo2) {
                orderInfo = orderInfo2;
            }

            @Override // java.lang.Runnable
            public final void run() throws Throwable {
                /*
                    Method dump skipped, instructions count: 859
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.AnonymousClass77.run():void");
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:211:0x0125  */
    /* JADX WARN: Removed duplicated region for block: B:212:0x0129  */
    /* JADX WARN: Removed duplicated region for block: B:215:0x0137  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x013b  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0149  */
    /* JADX WARN: Removed duplicated region for block: B:220:0x014d  */
    /* JADX WARN: Removed duplicated region for block: B:223:0x015b  */
    /* JADX WARN: Removed duplicated region for block: B:224:0x015f  */
    /* JADX WARN: Removed duplicated region for block: B:227:0x016d  */
    /* JADX WARN: Removed duplicated region for block: B:228:0x0171  */
    /* JADX WARN: Removed duplicated region for block: B:231:0x0188  */
    /* JADX WARN: Removed duplicated region for block: B:236:0x019b  */
    /* JADX WARN: Removed duplicated region for block: B:239:0x0258  */
    /* JADX WARN: Removed duplicated region for block: B:240:0x025b  */
    /* JADX WARN: Removed duplicated region for block: B:243:0x0286  */
    /* JADX WARN: Removed duplicated region for block: B:246:0x02af  */
    /* JADX WARN: Removed duplicated region for block: B:249:0x02c3  */
    /* JADX WARN: Removed duplicated region for block: B:252:0x02cf  */
    /* JADX WARN: Removed duplicated region for block: B:255:0x02dd  */
    /* JADX WARN: Removed duplicated region for block: B:261:0x032c  */
    /* JADX WARN: Removed duplicated region for block: B:277:0x0410  */
    /* JADX WARN: Removed duplicated region for block: B:290:0x0444  */
    /* JADX WARN: Removed duplicated region for block: B:295:0x0458  */
    /* JADX WARN: Removed duplicated region for block: B:300:0x0485  */
    /* JADX WARN: Removed duplicated region for block: B:303:0x0494  */
    /* JADX WARN: Removed duplicated region for block: B:308:0x04ab  */
    /* JADX WARN: Removed duplicated region for block: B:319:0x0434 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void c(com.netease.ntunisdk.base.OrderInfo r25) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 1264
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.c(com.netease.ntunisdk.base.OrderInfo):void");
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$78 */
    final class AnonymousClass78 implements HTTPCallback {
        final /* synthetic */ OrderInfo b;

        AnonymousClass78(OrderInfo orderInfo) {
            orderInfo = orderInfo;
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String str, String str2) throws Throwable {
            JSONObject jSONObject;
            int iOptInt;
            String strOptString;
            String strOptString2;
            String str3;
            String str4;
            String str5;
            String strOptString3;
            JSONObject jSONObjectOptJSONObject;
            UniSdkUtils.d("UniSDK Base", "/createorder result=".concat(String.valueOf(str)));
            if (TextUtils.isEmpty(str)) {
                UniSdkUtils.e("UniSDK Base", "/createorder no response");
                orderInfo.setOrderStatus(3);
                orderInfo.setOrderErrReason("create order fail");
                SdkBase.this.checkOrderDone(orderInfo);
                return false;
            }
            try {
                jSONObject = new JSONObject(str);
                iOptInt = jSONObject.optInt("code");
                int iOptInt2 = jSONObject.optInt("subcode");
                String strOptString4 = jSONObject.optString(NotificationCompat.CATEGORY_ERROR);
                String strOptString5 = jSONObject.optString("msg");
                strOptString = jSONObject.optString("popup", "None");
                int iOptInt3 = jSONObject.optInt("aas_ff_code", -1);
                strOptString2 = jSONObject.optString("aas_ff_rule");
                JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("aas_version");
                if (jSONArrayOptJSONArray != null) {
                    StringBuilder sb = new StringBuilder();
                    str3 = strOptString4;
                    str4 = strOptString5;
                    for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                        if (i == jSONArrayOptJSONArray.length() - 1) {
                            sb.append(jSONArrayOptJSONArray.optString(i));
                        } else {
                            sb.append(jSONArrayOptJSONArray.optString(i));
                            sb.append(",");
                        }
                    }
                    SdkBase.this.setPropStr(ConstProp.AAS_VERSION, sb.toString());
                } else {
                    str3 = strOptString4;
                    str4 = strOptString5;
                }
                if (420 == iOptInt) {
                    strOptString3 = jSONObject.optString("message");
                    str5 = !TextUtils.isEmpty(strOptString3) ? strOptString3 : str3;
                    if ("None".equalsIgnoreCase(strOptString)) {
                        strOptString = "1";
                    }
                } else {
                    str5 = str3;
                    strOptString3 = str4;
                }
                orderInfo.setJfCode(iOptInt);
                orderInfo.setJfSubCode(iOptInt2);
                orderInfo.setJfMessage(str5);
                orderInfo.setJfAasFfCode(iOptInt3);
                orderInfo.setJfAasFfRule(strOptString2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (200 == iOptInt) {
                String strOptString6 = jSONObject.optString("sn");
                if (SdkBase.this.hasFeature("VIRTUAL_ORDER") && jSONObject.has("data") && (jSONObjectOptJSONObject = jSONObject.optJSONObject("data")) != null && jSONObjectOptJSONObject.has("consumesn")) {
                    orderInfo.setSdkOrderId(jSONObjectOptJSONObject.optString("consumesn"));
                }
                String strOptString7 = jSONObject.optString("etc");
                String strOptString8 = jSONObject.optString("sdkOrderId");
                String strOptString9 = jSONObject.optString(SocialOperation.GAME_SIGNATURE);
                orderInfo.setOrderId(strOptString6);
                if (!TextUtils.isEmpty(strOptString7)) {
                    orderInfo.setOrderEtc(strOptString7);
                }
                if (!TextUtils.isEmpty(strOptString8)) {
                    orderInfo.setSdkOrderId(strOptString8);
                }
                if (!TextUtils.isEmpty(strOptString9)) {
                    orderInfo.setSignature(strOptString9);
                }
                UniSdkUtils.i("UniSDK Base", "/createorder success");
                SdkBase.this.a(orderInfo);
                return false;
            }
            JfGas.createOrderTips((SdkBase) SdkMgr.getInst(), strOptString, strOptString3, strOptString2);
            UniSdkUtils.e("UniSDK Base", "/createorder fail");
            orderInfo.setOrderStatus(3);
            orderInfo.setOrderErrReason("create order fail");
            SdkBase.this.checkOrderDone(orderInfo);
            return false;
        }
    }

    static String a() {
        return SdkMgr.getInst().getChannel();
    }

    public String choosePayChannel(Map<String, String> map) {
        String channel = getChannel();
        UniSdkUtils.i("UniSDK Base", "defaultChannel: ".concat(String.valueOf(channel)));
        if (map == null || map.size() == 0) {
            UniSdkUtils.i("UniSDK Base", "sdkPids is null, use  defaultPayChannel: ".concat(String.valueOf(channel)));
            return channel;
        }
        if (this.sdkInstMap.isEmpty()) {
            UniSdkUtils.i("UniSDK Base", "sdkInstMap is empty, use  defaultPayChannel: ".concat(String.valueOf(channel)));
            return channel;
        }
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            if (isTelecomChannel(it.next())) {
                i++;
            }
        }
        if (isTelecomChannel(getChannel())) {
            i++;
        }
        UniSdkUtils.i("UniSDK Base", "telecomInstCnt: ".concat(String.valueOf(i)));
        for (String str : map.keySet()) {
            SdkBase sdkBase = this.sdkInstMap.get(str);
            if (sdkBase != null && (!isTelecomChannel(str) || i == 1 || isValidTelecomChannel(str) || sdkBase.getPropInt(ConstProp.HAS_3N, 0) == 1)) {
                UniSdkUtils.i("UniSDK Base", "choose payChannel: ".concat(String.valueOf(str)));
                return str;
            }
        }
        UniSdkUtils.i("UniSDK Base", "sdkInstMap dont match, use  defaultPayChannel: ".concat(String.valueOf(channel)));
        return channel;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getChannelByImsi() {
        String cCTypeByImsi = getCCTypeByImsi();
        return cCTypeByImsi.equals(ConstProp.CCTYPE_CMCC) ? "mm_10086" : cCTypeByImsi.equals(ConstProp.CCTYPE_CUCC) ? "wo_app" : cCTypeByImsi.equals(ConstProp.CCTYPE_CTCC) ? "play_telecom" : ConstProp.CHANNEL_UNKNOW;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getChannelByImsiEx() {
        String cCTypeByImsi = getCCTypeByImsi();
        if (!cCTypeByImsi.equals(ConstProp.CCTYPE_CMCC)) {
            return cCTypeByImsi.equals(ConstProp.CCTYPE_CUCC) ? "wo_app" : cCTypeByImsi.equals(ConstProp.CCTYPE_CTCC) ? "play_telecom" : ConstProp.CHANNEL_UNKNOW;
        }
        if (c()) {
            return this.A;
        }
        return (this.sdkInstMap.containsKey("mm_10086") || getChannel().equals("mm_10086")) ? "mm_10086" : (this.sdkInstMap.containsKey("g_10086") || getChannel().equals("g_10086")) ? "g_10086" : "mm_10086";
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getCCTypeByImsi() {
        String mobileIMSI = UniSdkUtils.getMobileIMSI(this.myCtx);
        return mobileIMSI == null ? ConstProp.CCTYPE_UNKNOW : (mobileIMSI.startsWith("46000") || mobileIMSI.startsWith("46002") || mobileIMSI.startsWith("46007")) ? ConstProp.CCTYPE_CMCC : (mobileIMSI.startsWith("46001") || mobileIMSI.startsWith("460006")) ? ConstProp.CCTYPE_CUCC : (mobileIMSI.startsWith("46003") || mobileIMSI.startsWith("460005") || mobileIMSI.startsWith("460011")) ? ConstProp.CCTYPE_CTCC : ConstProp.CCTYPE_UNKNOW;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntSetFloatBtnVisible(boolean z) {
        Activity activity = (Activity) this.myCtx;
        if (activity == null) {
            UniSdkUtils.e("UniSDK Base", "ntSetFloatBtnVisible myCtx is null");
        } else {
            activity.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.79
                final /* synthetic */ boolean b;

                AnonymousClass79(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                    while (it.hasNext()) {
                        SdkBase.this.sdkInstMap.get(it.next()).setFloatBtnVisible(z);
                    }
                    SdkBase.this.setFloatBtnVisible(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$79 */
    final class AnonymousClass79 implements Runnable {
        final /* synthetic */ boolean b;

        AnonymousClass79(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).setFloatBtnVisible(z);
            }
            SdkBase.this.setFloatBtnVisible(z);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntVerifyOrder() {
        UniSdkUtils.i("UniSDK Base", "ntVerifyOrder");
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_SERVER_KEY);
        if (TextUtils.isEmpty(propStr)) {
            UniSdkUtils.e("UniSDK Base", "ConstProp.UNISDK_SERVER_KEY is empty");
            return;
        }
        String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.USERINFO_UID);
        if (TextUtils.isEmpty(propStr2)) {
            UniSdkUtils.e("UniSDK Base", "ConstProp.USERINFO_UID is empty");
            return;
        }
        String string = getSharedPrefUniSDKServer().getString(ORDERS_CREATED_PREFIX.concat(String.valueOf(propStr2)), "");
        if (TextUtils.isEmpty(string)) {
            UniSdkUtils.w("UniSDK Base", "ntVerifyOrder, ORDERS_CREATED is empty for roleid:".concat(String.valueOf(propStr2)));
            return;
        }
        new TreeMap();
        try {
            String str = new String(Base64.decode(string, 0), "UTF-8");
            try {
                Map<String, Object> mapJsonToMapSet = StrUtil.jsonToMapSet(str);
                UniSdkUtils.d("UniSDK Base", "ntVerifyOrder, orders_created=".concat(String.valueOf(mapJsonToMapSet)));
                TreeMap treeMap = new TreeMap();
                Iterator<Map.Entry<String, Object>> it = mapJsonToMapSet.entrySet().iterator();
                while (it.hasNext()) {
                    String key = it.next().getKey();
                    String string2 = getSharedPrefUniSDKServer().getString(key, "");
                    if (!TextUtils.isEmpty(string2)) {
                        treeMap.put(key, string2);
                    }
                }
                if (treeMap.size() == 0) {
                    UniSdkUtils.w("UniSDK Base", "ntVerifyOrder, no order to consume for roleid:".concat(String.valueOf(propStr2)));
                    return;
                }
                String string3 = StrUtil.mapToJson(treeMap).toString();
                String string4 = getSharedPrefUniSDKServer().getString(ORDERS_ENCRYPTED_PREFIX.concat(String.valueOf(propStr2)), "");
                if (TextUtils.isEmpty(string4)) {
                    UniSdkUtils.e("UniSDK Base", "ntVerifyOrder, ORDERS_ENCRYPTED is empty for roleid:".concat(String.valueOf(propStr2)));
                    return;
                }
                String propStr3 = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_CONSUMEORDER_URL);
                if (TextUtils.isEmpty(propStr3)) {
                    UniSdkUtils.e("UniSDK Base", "ConstProp.UNISDK_CONSUMEORDER_URL is empty");
                    return;
                }
                TreeMap treeMap2 = new TreeMap();
                int i = L + 1;
                L = i;
                if (i >= 1000) {
                    L = 0;
                }
                String str2 = System.currentTimeMillis() + "_" + String.format("%03d", Integer.valueOf(L));
                treeMap2.put(Const.CONFIG_KEY.ROLEID, propStr2);
                treeMap2.put("orderinfo", string3);
                treeMap2.put("encrypted", string4);
                treeMap2.put("privateparam", str2);
                HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
                queueItemNewQueueItem.method = "POST";
                queueItemNewQueueItem.url = propStr3;
                queueItemNewQueueItem.bSync = Boolean.TRUE;
                queueItemNewQueueItem.leftRetry = 0;
                queueItemNewQueueItem.setBody(treeMap2);
                queueItemNewQueueItem.keyRSA = propStr;
                queueItemNewQueueItem.transParam = str2;
                queueItemNewQueueItem.callback = new a();
                HTTPQueue.getInstance(HTTPQueue.HTTPQUEUE_PAY).addItem(queueItemNewQueueItem);
            } catch (Exception e) {
                e = e;
                string = str;
                UniSdkUtils.e("UniSDK Base", "ntVerifyOrder, jsonToMapSet error:" + e + ", strJson=" + string);
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e = e2;
        }
    }

    private class a implements HTTPCallback {
        public a() {
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String str, String str2) {
            UniSdkUtils.d("UniSDK Base", "VerifyOrderCallback result=" + str + ", transParam=" + str2);
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            try {
                new TreeMap();
                Map<String, Object> mapJsonToMapSet = StrUtil.jsonToMapSet(str);
                int iIntValue = ((Integer) mapJsonToMapSet.get("code")).intValue();
                String str3 = "UTF-8";
                if (200 != iIntValue) {
                    if (430 != iIntValue) {
                        return false;
                    }
                    String str4 = (String) mapJsonToMapSet.get(Const.CONFIG_KEY.ROLEID);
                    String string = SdkBase.this.getSharedPrefUniSDKServer().getString(SdkBase.ORDERS_CREATED_PREFIX.concat(String.valueOf(str4)), "");
                    Map treeMap = new TreeMap();
                    try {
                        treeMap = StrUtil.jsonToMapSet(new String(Base64.decode(string, 0), "UTF-8"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences.Editor editorEdit = SdkBase.this.getSharedPrefUniSDKServer().edit();
                    Iterator it = treeMap.entrySet().iterator();
                    while (it.hasNext()) {
                        editorEdit.remove((String) ((Map.Entry) it.next()).getKey());
                    }
                    editorEdit.putString(SdkBase.ORDERS_CREATED_PREFIX.concat(String.valueOf(str4)), "");
                    editorEdit.putString(SdkBase.ORDERS_ENCRYPTED_PREFIX.concat(String.valueOf(str4)), "");
                    editorEdit.commit();
                    return false;
                }
                String str5 = (String) mapJsonToMapSet.get(Const.CONFIG_KEY.ROLEID);
                String propStr = SdkMgr.getInst().getPropStr(ConstProp.USERINFO_UID);
                if (!str5.equals(propStr)) {
                    UniSdkUtils.e("UniSDK Base", String.format("roleid mismatch %s<>%s", str5, propStr));
                    return false;
                }
                String str6 = (String) mapJsonToMapSet.get("privateparam");
                if (!str2.equals(str6)) {
                    UniSdkUtils.e("UniSDK Base", String.format("VerifyOrderCallback verify privateparam failed: %s<>%s", str2, str6));
                    return false;
                }
                String str7 = (String) mapJsonToMapSet.get("success");
                String str8 = (String) mapJsonToMapSet.get("expired");
                String str9 = (String) mapJsonToMapSet.get(com.facebook.hermes.intl.Constants.COLLATION_INVALID);
                String str10 = (String) mapJsonToMapSet.get("encryptedold");
                String str11 = (String) mapJsonToMapSet.get("encryptednew");
                String str12 = (String) mapJsonToMapSet.get("sign");
                String str13 = "code=" + iIntValue + "&roleid=" + str5 + "&success=" + str7 + "&expired=" + str8 + "&invalid=" + str9 + "&encryptedold=" + str10 + "&encryptednew=" + str11 + "&privateparam=" + str6;
                String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_SERVER_KEY);
                if (!Crypto.rsaVerify(str13.getBytes("UTF-8"), str12, propStr2)) {
                    UniSdkUtils.e("UniSDK Base", "VerifyOrderCallback rsaVerify failed");
                    return false;
                }
                String str14 = new String(Base64.decode(str7, 0), "UTF-8");
                new TreeMap();
                try {
                    Map<String, Object> mapJsonToMapSet2 = StrUtil.jsonToMapSet(str14);
                    String str15 = new String(Base64.decode(str8, 0), "UTF-8");
                    new TreeMap();
                    try {
                        Map<String, Object> mapJsonToMapSet3 = StrUtil.jsonToMapSet(str15);
                        String str16 = new String(Base64.decode(str9, 0), "UTF-8");
                        new TreeMap();
                        try {
                            Map<String, Object> mapJsonToMapSet4 = StrUtil.jsonToMapSet(str16);
                            TreeSet<String> treeSet = new TreeSet();
                            Iterator<Map.Entry<String, Object>> it2 = mapJsonToMapSet2.entrySet().iterator();
                            while (it2.hasNext()) {
                                Map.Entry<String, Object> next = it2.next();
                                String key = next.getKey();
                                TreeMap treeMap2 = (TreeMap) next.getValue();
                                treeSet.add(key);
                                String str17 = (String) treeMap2.get("bid");
                                Iterator<Map.Entry<String, Object>> it3 = it2;
                                String str18 = (String) treeMap2.get("sn");
                                String str19 = str3;
                                String str20 = propStr2;
                                String str21 = str11;
                                UniSdkUtils.d("UniSDK Base", String.format("VerifyOrderCallback, order to ship, id:%s, info:%s", key, treeMap2));
                                OrderInfo orderInfo = new OrderInfo(str17);
                                orderInfo.setOrderId(str18);
                                if (SdkBase.this.getPropInt(ConstProp.ORDER_CALLER_THREAD, 1) != 2) {
                                    SdkBase.this.orderListener.orderConsumeDone(orderInfo);
                                } else {
                                    SdkBase.this.runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.a.1

                                        /* renamed from: a */
                                        final /* synthetic */ OrderInfo f1790a;

                                        AnonymousClass1(OrderInfo orderInfo2) {
                                            orderInfo = orderInfo2;
                                        }

                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            SdkBase.this.orderListener.orderConsumeDone(orderInfo);
                                        }
                                    });
                                }
                                it2 = it3;
                                str3 = str19;
                                propStr2 = str20;
                                str11 = str21;
                            }
                            String str22 = propStr2;
                            String str23 = str11;
                            String str24 = str3;
                            for (Map.Entry<String, Object> entry : mapJsonToMapSet3.entrySet()) {
                                String key2 = entry.getKey();
                                UniSdkUtils.d("UniSDK Base", String.format("VerifyOrderCallback, order expired, id:%s, info:%s", key2, (TreeMap) entry.getValue()));
                                treeSet.add(key2);
                            }
                            for (Map.Entry<String, Object> entry2 : mapJsonToMapSet4.entrySet()) {
                                String key3 = entry2.getKey();
                                UniSdkUtils.d("UniSDK Base", String.format("VerifyOrderCallback, order invalid, id:%s, info:%s", key3, (TreeMap) entry2.getValue()));
                                treeSet.add(key3);
                            }
                            String string2 = SdkBase.this.getSharedPrefUniSDKServer().getString(SdkBase.ORDERS_ENCRYPTED_PREFIX.concat(String.valueOf(str5)), "");
                            if (string2.equals(str10)) {
                                UniSdkUtils.i("UniSDK Base", "no order created when calling /consumeorder");
                                SdkBase.this.getSharedPrefUniSDKServer().edit().putString(SdkBase.ORDERS_ENCRYPTED_PREFIX.concat(String.valueOf(str5)), str23).commit();
                            } else {
                                UniSdkUtils.w("UniSDK Base", "order created when calling /consumeorder");
                                TreeMap treeMap3 = new TreeMap();
                                if (!TextUtils.isEmpty(string2)) {
                                    treeMap3.put("encrypted", string2);
                                }
                                Iterator it4 = treeSet.iterator();
                                while (it4.hasNext()) {
                                    treeMap3.put((String) it4.next(), "");
                                }
                                try {
                                    SdkBase.this.getSharedPrefUniSDKServer().edit().putString(SdkBase.ORDERS_ENCRYPTED_PREFIX.concat(String.valueOf(str5)), Crypto.rsaEncrypt(StrUtil.mapToJson(treeMap3).toString(), str22)).commit();
                                } catch (Exception e2) {
                                    UniSdkUtils.e("UniSDK Base", "VerifyOrderCallback rsaEncrypt error:".concat(String.valueOf(e2)));
                                    e2.printStackTrace();
                                    return false;
                                }
                            }
                            String string3 = SdkBase.this.getSharedPrefUniSDKServer().getString(SdkBase.ORDERS_CREATED_PREFIX.concat(String.valueOf(str5)), "");
                            Map treeMap4 = new TreeMap();
                            try {
                                treeMap4 = StrUtil.jsonToMapSet(new String(Base64.decode(string3, 0), str24));
                            } catch (JSONException e3) {
                                e3.printStackTrace();
                            }
                            SharedPreferences.Editor editorEdit2 = SdkBase.this.getSharedPrefUniSDKServer().edit();
                            for (String str25 : treeSet) {
                                treeMap4.remove(str25);
                                editorEdit2.remove(str25);
                            }
                            editorEdit2.commit();
                            SdkBase.this.getSharedPrefUniSDKServer().edit().putString(SdkBase.ORDERS_CREATED_PREFIX.concat(String.valueOf(str5)), Base64.encodeToString(StrUtil.mapToJson(treeMap4).toString().getBytes(), 0)).commit();
                            return false;
                        } catch (JSONException e4) {
                            UniSdkUtils.e("UniSDK Base", "VerifyOrderCallback, jsonToMapSet error:" + e4 + ", strInvalid=" + str16);
                            e4.printStackTrace();
                            return false;
                        }
                    } catch (JSONException e5) {
                        UniSdkUtils.e("UniSDK Base", "VerifyOrderCallback, jsonToMapSet error:" + e5 + ", strExpired=" + str15);
                        e5.printStackTrace();
                        return false;
                    }
                } catch (JSONException e6) {
                    UniSdkUtils.e("UniSDK Base", "VerifyOrderCallback, jsonToMapSet error:" + e6 + ", strSuccess=" + str14);
                    e6.printStackTrace();
                    return false;
                }
            } catch (Exception e7) {
                UniSdkUtils.e("UniSDK Base", "/consumeorder processResult error:".concat(String.valueOf(e7)));
                e7.printStackTrace();
                return false;
            }
            UniSdkUtils.e("UniSDK Base", "/consumeorder processResult error:".concat(String.valueOf(e7)));
            e7.printStackTrace();
            return false;
        }

        /* renamed from: com.netease.ntunisdk.base.SdkBase$a$1 */
        final class AnonymousClass1 implements Runnable {

            /* renamed from: a */
            final /* synthetic */ OrderInfo f1790a;

            AnonymousClass1(OrderInfo orderInfo2) {
                orderInfo = orderInfo2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.orderListener.orderConsumeDone(orderInfo);
            }
        }
    }

    public void checkOrderDone(OrderInfo orderInfo) throws Throwable {
        if (SdkMgr.getInst().hasFeature(ConstProp.SHOW_ORDER_LOADING)) {
            LoadingDialog.getInstance((Activity) this.myCtx).dismissAndRemoveMessage();
        }
        this.H = System.currentTimeMillis();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "checkOrderDone_call");
        } catch (JSONException e) {
            UniSdkUtils.d("UniSDK Base", "extraJson:" + e.getMessage());
        }
        saveClientLog(orderInfo, jSONObject.toString());
        int orderStatus = orderInfo.getOrderStatus();
        boolean z = true;
        if (2 == orderStatus || 1 == orderStatus || 10 == orderStatus) {
            c(getDetectData(39, orderStatus, orderInfo.getOrderErrReason()));
            saveLogToJFOnPay(orderInfo);
        } else {
            c(getDetectData(9, orderStatus, orderInfo.getOrderErrReason()));
        }
        if (!SdkMgr.getInst().hasFeature(ConstProp.UNISDK_JF_GAS3) && (!orderInfo.isWebPayment() || !SdkMgr.getInst().hasFeature(ConstProp.UNISDK_JF_GAS3_WEB))) {
            z = false;
        }
        if (z) {
            new JfGas((SdkBase) SdkMgr.getInst()).b(orderInfo);
        } else if (orderInfo.isWebPayment() || SdkMgr.getInst().hasFeature(ConstProp.REQUEST_UNISDK_SERVER)) {
            b(orderInfo);
        }
        innerOrderCallback(orderInfo);
    }

    public void innerOrderCallback(OrderInfo orderInfo) {
        if (this.c != null) {
            if (getPropInt(ConstProp.ORDER_CALLER_THREAD, 1) == 2) {
                runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.81

                    /* renamed from: a */
                    final /* synthetic */ OrderInfo f1773a;

                    AnonymousClass81(OrderInfo orderInfo2) {
                        orderInfo = orderInfo2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        SdkBase.this.c.orderCheckDone(orderInfo);
                    }
                });
            } else {
                this.c.orderCheckDone(orderInfo2);
            }
        }
        if (this.orderListener == null) {
            UniSdkUtils.e("UniSDK Base", "OnOrderCheckListener not set");
        } else if (getPropInt(ConstProp.ORDER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.82
                final /* synthetic */ OrderInfo b;

                AnonymousClass82(OrderInfo orderInfo2) {
                    orderInfo = orderInfo2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.orderListener.orderCheckDone(orderInfo);
                }
            });
        } else {
            this.orderListener.orderCheckDone(orderInfo2);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$81 */
    final class AnonymousClass81 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ OrderInfo f1773a;

        AnonymousClass81(OrderInfo orderInfo2) {
            orderInfo = orderInfo2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.c.orderCheckDone(orderInfo);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$82 */
    final class AnonymousClass82 implements Runnable {
        final /* synthetic */ OrderInfo b;

        AnonymousClass82(OrderInfo orderInfo2) {
            orderInfo = orderInfo2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.orderListener.orderCheckDone(orderInfo);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:142:0x00c8  */
    /* JADX WARN: Removed duplicated region for block: B:143:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x013b  */
    /* JADX WARN: Removed duplicated region for block: B:154:0x0169  */
    /* JADX WARN: Removed duplicated region for block: B:167:0x01a7 A[LOOP:1: B:165:0x01a1->B:167:0x01a7, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:170:0x01c7  */
    /* JADX WARN: Removed duplicated region for block: B:186:0x0271  */
    /* JADX WARN: Removed duplicated region for block: B:198:0x02fc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void b(com.netease.ntunisdk.base.OrderInfo r24) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 846
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.b(com.netease.ntunisdk.base.OrderInfo):void");
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$83 */
    final class AnonymousClass83 implements HTTPCallback {
        final /* synthetic */ OrderInfo b;

        AnonymousClass83(OrderInfo orderInfo) {
            orderInfo = orderInfo;
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String str, String str2) {
            int iOptInt;
            UniSdkUtils.i("UniSDK Base", String.format("processResult result=%s, transParam=%s", str, str2));
            if (TextUtils.isEmpty(str)) {
                return true;
            }
            try {
                iOptInt = new JSONObject(str).optInt("code");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (200 == iOptInt) {
                UniSdkUtils.i("UniSDK Base", "/queryorder success");
                if (1 == SdkMgr.getInst().getPropInt(ConstProp.UNISDK_SERVER_MODE, 0)) {
                    SdkMgr.getInst().ntVerifyOrder();
                }
                SdkBase.this.ntConsume(orderInfo);
                return false;
            }
            UniSdkUtils.e("UniSDK Base", "/queryorder failed\uff0ccode=".concat(String.valueOf(iOptInt)));
            return true;
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$84 */
    final class AnonymousClass84 implements HTTPCallback {
        AnonymousClass84() {
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String str, String str2) {
            UniSdkUtils.i("UniSDK Base", String.format("processResult result=%s, transParam=%s", str, str2));
            if (TextUtils.isEmpty(str)) {
                return true;
            }
            if ("DRPF".equals(str2) || "DETECT".equals(str2) || "SDC".equals(str2) || ConstProp.JF_PAY_LOG_URL.equals(str2)) {
                return false;
            }
            ConstProp.JF_CLIENT_LOG_URL.equals(str2);
            return false;
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$85 */
    final class AnonymousClass85 implements Runnable {
        AnonymousClass85() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (SdkBase.this.hasGuestLogined()) {
                SdkBase.this.sdkInstMap.get("ngguest").logout();
            } else {
                SdkBase.this.logout();
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntLogout() {
        SdkPersonalInfoList.hookLogout();
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.85
            AnonymousClass85() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                if (SdkBase.this.hasGuestLogined()) {
                    SdkBase.this.sdkInstMap.get("ngguest").logout();
                } else {
                    SdkBase.this.logout();
                }
            }
        });
    }

    public void logoutDone(int i) throws JSONException {
        setPropStr(ConstProp.UNISDK_JF_GAS_TOKEN, "");
        Q = false;
        if (this.logoutListener != null) {
            if (getPropInt(ConstProp.LOGOUT_CALLER_THREAD, 1) == 2) {
                runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.86
                    final /* synthetic */ int b;

                    AnonymousClass86(int i2) {
                        i = i2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        SdkBase.this.logoutListener.logoutDone(i);
                    }
                });
            } else {
                this.logoutListener.logoutDone(i2);
            }
        } else {
            UniSdkUtils.e("UniSDK Base", "OnLogoutDoneListener not set");
        }
        if (useNewSdkProcedure()) {
            g.e();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$86 */
    final class AnonymousClass86 implements Runnable {
        final /* synthetic */ int b;

        AnonymousClass86(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.logoutListener.logoutDone(i);
        }
    }

    public void onReceivedNotificationDone() {
        UniSdkUtils.d("UniSDK Base", "onReceivedNotificationDone");
        if (this.f == null) {
            UniSdkUtils.d("UniSDK Base", "receiveMsgListener null");
        } else if (getPropInt(ConstProp.CONTINUE_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.87
                AnonymousClass87() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.f.onReceivedNotification();
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.88
                AnonymousClass88() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.f.onReceivedNotification();
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$87 */
    final class AnonymousClass87 implements Runnable {
        AnonymousClass87() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.f.onReceivedNotification();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$88 */
    final class AnonymousClass88 implements Runnable {
        AnonymousClass88() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.f.onReceivedNotification();
        }
    }

    public void onEnterGameDone(String str, String str2) {
        UniSdkUtils.d("UniSDK Base", "onEnterGameDone");
        if (this.f == null) {
            UniSdkUtils.e("UniSDK Base", "receiveMsgListener null");
        } else if (getPropInt(ConstProp.CONTINUE_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.89

                /* renamed from: a */
                final /* synthetic */ String f1779a;
                final /* synthetic */ String c;

                AnonymousClass89(String str3, String str22) {
                    str = str3;
                    str = str22;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.f.onEnterGame(str, str);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.90
                final /* synthetic */ String b;
                final /* synthetic */ String c;

                AnonymousClass90(String str3, String str22) {
                    str = str3;
                    str = str22;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.f.onEnterGame(str, str);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$89 */
    final class AnonymousClass89 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1779a;
        final /* synthetic */ String c;

        AnonymousClass89(String str3, String str22) {
            str = str3;
            str = str22;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.f.onEnterGame(str, str);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$90 */
    final class AnonymousClass90 implements Runnable {
        final /* synthetic */ String b;
        final /* synthetic */ String c;

        AnonymousClass90(String str3, String str22) {
            str = str3;
            str = str22;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.f.onEnterGame(str, str);
        }
    }

    public void continueDone() {
        if (this.e == null) {
            UniSdkUtils.e("UniSDK Base", "continueListener not set");
        } else if (getPropInt(ConstProp.CONTINUE_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.92
                AnonymousClass92() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.e.continueGame();
                }
            });
        } else {
            this.e.continueGame();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$92 */
    final class AnonymousClass92 implements Runnable {
        AnonymousClass92() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.e.continueGame();
        }
    }

    public void exitDone() {
        if (this.g == null) {
            UniSdkUtils.e("UniSDK Base", "exitViewListener not set");
        } else if (getPropInt(ConstProp.EXIT_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.93
                AnonymousClass93() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.g.exitApp();
                }
            });
        } else {
            this.g.exitApp();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$93 */
    final class AnonymousClass93 implements Runnable {
        AnonymousClass93() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.g.exitApp();
        }
    }

    public void openExitViewFailed() {
        if (this.g == null) {
            UniSdkUtils.e("UniSDK Base", "exitViewListener not set");
        } else if (getPropInt(ConstProp.EXIT_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.94
                AnonymousClass94() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.g.onOpenExitViewFailed();
                }
            });
        } else {
            this.g.onOpenExitViewFailed();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$94 */
    final class AnonymousClass94 implements Runnable {
        AnonymousClass94() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.g.onOpenExitViewFailed();
        }
    }

    public void leaveSdk(int i) {
        if (this.d == null) {
            UniSdkUtils.e("UniSDK Base", "OnLeaveSdkListener not set");
            return;
        }
        if (getPropInt(ConstProp.LEAVE_SDK_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.95
                final /* synthetic */ int b;

                AnonymousClass95(int i2) {
                    i = i2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "leave sdk : current thread:" + Thread.currentThread().getId());
                    SdkBase.this.d.leaveSdk(i);
                }
            });
            return;
        }
        UniSdkUtils.i("UniSDK Base", "leave sdk : current thread:" + Thread.currentThread().getId());
        this.d.leaveSdk(i2);
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$95 */
    final class AnonymousClass95 implements Runnable {
        final /* synthetic */ int b;

        AnonymousClass95(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "leave sdk : current thread:" + Thread.currentThread().getId());
            SdkBase.this.d.leaveSdk(i);
        }
    }

    public void exit() {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).exit();
        }
        CachedThreadPoolUtil.getInstance().close();
        HTTPQueue.getInstance("LOG").close();
        HTTPQueue.getInstance("UniSDK").close();
        HTTPQueue.getInstance(HTTPQueue.HTTPQUEUE_PAY).close();
        resetFields();
    }

    protected void resetFields() {
        this.hasInit = false;
        this.v = false;
        this.uiThreadId = 0L;
        this.myCtx = null;
        this.loginListener = null;
        this.orderListener = null;
        this.logoutListener = null;
        this.d = null;
        this.e = null;
        this.g = null;
        this.m = null;
        this.h = null;
        this.querySkuDetailsListener = null;
        this.k = null;
        this.l = null;
        this.i = null;
        this.j = null;
        this.p = null;
        this.s = null;
        this.t = null;
        BlockingQueue<Runnable> blockingQueue = this.u;
        if (blockingQueue != null) {
            blockingQueue.clear();
        }
        Hashtable<String, String> hashtable = this.N;
        if (hashtable != null) {
            hashtable.clear();
        }
        Hashtable<String, String> hashtable2 = this.O;
        if (hashtable2 != null) {
            hashtable2.clear();
        }
        Map<String, String> map = this.w;
        if (map != null) {
            map.clear();
        }
        Map<String, SdkBase> map2 = this.sdkInstMap;
        if (map2 != null) {
            Iterator<String> it = map2.keySet().iterator();
            while (it.hasNext()) {
                this.sdkInstMap.get(it.next()).resetFields();
            }
            this.sdkInstMap.clear();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntOpenManager() {
        ClientLog.getInst().startUniTransaction(this.myCtx);
        if (!d("ntOpenManager") && hasFeature(ConstProp.MODE_HAS_MANAGER)) {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.96
                AnonymousClass96() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    if (SdkBase.this.getPropInt(ConstProp.MODE_HAS_MANAGER, 0) != 0) {
                        SdkBase.this.openManager();
                        return;
                    }
                    Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                    while (it.hasNext()) {
                        SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                        if (sdkBase.getPropInt(ConstProp.MODE_HAS_MANAGER, 0) != 0) {
                            sdkBase.openManager();
                            return;
                        }
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$96 */
    final class AnonymousClass96 implements Runnable {
        AnonymousClass96() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (SdkBase.this.getPropInt(ConstProp.MODE_HAS_MANAGER, 0) != 0) {
                SdkBase.this.openManager();
                return;
            }
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                if (sdkBase.getPropInt(ConstProp.MODE_HAS_MANAGER, 0) != 0) {
                    sdkBase.openManager();
                    return;
                }
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntSwitchAccount() {
        if (!d("ntSwitchAccount") && hasFeature(ConstProp.MODE_HAS_SWITCH_ACCOUNT)) {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.97
                AnonymousClass97() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    if (SdkBase.this.hasFeature(ConstProp.MODE_HAS_SWITCH_ACCOUNT)) {
                        SdkBase.this.switchAccount();
                        return;
                    }
                    Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                    while (it.hasNext()) {
                        SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                        if (sdkBase.hasFeature(ConstProp.MODE_HAS_SWITCH_ACCOUNT)) {
                            sdkBase.switchAccount();
                            return;
                        }
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$97 */
    final class AnonymousClass97 implements Runnable {
        AnonymousClass97() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (SdkBase.this.hasFeature(ConstProp.MODE_HAS_SWITCH_ACCOUNT)) {
                SdkBase.this.switchAccount();
                return;
            }
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                if (sdkBase.hasFeature(ConstProp.MODE_HAS_SWITCH_ACCOUNT)) {
                    sdkBase.switchAccount();
                    return;
                }
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntOpenNearby() {
        if (hasFeature(ConstProp.MODE_HAS_NEARBY)) {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.98
                AnonymousClass98() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    if (SdkBase.this.hasFeature(ConstProp.MODE_HAS_NEARBY)) {
                        SdkBase.this.openNearby();
                        return;
                    }
                    Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                    while (it.hasNext()) {
                        SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                        if (sdkBase.hasFeature(ConstProp.MODE_HAS_NEARBY)) {
                            sdkBase.openNearby();
                            return;
                        }
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$98 */
    final class AnonymousClass98 implements Runnable {
        AnonymousClass98() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (SdkBase.this.hasFeature(ConstProp.MODE_HAS_NEARBY)) {
                SdkBase.this.openNearby();
                return;
            }
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                if (sdkBase.hasFeature(ConstProp.MODE_HAS_NEARBY)) {
                    sdkBase.openNearby();
                    return;
                }
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntOpenPauseView() {
        if (hasFeature(ConstProp.MODE_HAS_PAUSE_VIEW)) {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.99
                AnonymousClass99() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    if (SdkBase.this.getPropInt(ConstProp.MODE_HAS_PAUSE_VIEW, 0) != 0) {
                        SdkBase.this.openPauseView();
                        return;
                    }
                    Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                    while (it.hasNext()) {
                        SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                        if (sdkBase.getPropInt(ConstProp.MODE_HAS_PAUSE_VIEW, 0) != 0) {
                            sdkBase.openPauseView();
                            return;
                        }
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$99 */
    final class AnonymousClass99 implements Runnable {
        AnonymousClass99() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (SdkBase.this.getPropInt(ConstProp.MODE_HAS_PAUSE_VIEW, 0) != 0) {
                SdkBase.this.openPauseView();
                return;
            }
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                if (sdkBase.getPropInt(ConstProp.MODE_HAS_PAUSE_VIEW, 0) != 0) {
                    sdkBase.openPauseView();
                    return;
                }
            }
        }
    }

    public String getDeviceId() throws Throwable {
        String propStr = getPropStr(ConstProp.DEVICE_ID);
        return propStr == null ? ConstProp.INVALID_DEVICE_ID : propStr;
    }

    private static String b(String str) {
        if ("UID".equals(str)) {
            return ConstProp.UID;
        }
        if ("FULL_UID".equals(str)) {
            return ConstProp.FULL_UID;
        }
        if ("USERINFO_REGION_ID".equals(str)) {
            return ConstProp.USERINFO_REGION_ID;
        }
        if ("USERINFO_REGION_NAME".equals(str)) {
            return ConstProp.USERINFO_REGION_NAME;
        }
        if ("CURRENCY".equals(str)) {
            return ConstProp.CURRENCY;
        }
        if ("RATE".equals(str)) {
            return ConstProp.RATE;
        }
        if ("APP_DATA".equals(str)) {
            return ConstProp.APP_DATA;
        }
        if ("JF_OVERSEA_FF_LOG_URL".equalsIgnoreCase(str)) {
            return ConstProp.JF_OVERSEA_PAY_LOG_URL;
        }
        if ("JF_FF_LOG_URL".equalsIgnoreCase(str)) {
            return ConstProp.JF_PAY_LOG_URL;
        }
        if ("HAS_FF_CB".equalsIgnoreCase(str)) {
            return ConstProp.HAS_PAY_CB;
        }
        if ("FF_CB_URL".equalsIgnoreCase(str)) {
            return ConstProp.PAY_CB_URL;
        }
        if (ConstProp.JF_CLIENT_KEY.equalsIgnoreCase(str)) {
            return ConstProp.JF_LOG_KEY;
        }
        if ("X_LBS_TOKEN".equals(str)) {
            return ConstProp.X_LBS_TOKEN;
        }
        if ("MODE_HAS_CC_RECORD".equals(str)) {
            return ConstProp.MODE_HAS_CC_RECORD;
        }
        if (str == null || !str.startsWith("MODE_")) {
            return (str == null || !str.startsWith("NT_")) ? str : str.substring(3);
        }
        return str.replace("MODE_", "FEATURE_");
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getPropStr(String str) throws Throwable {
        String[] strArr;
        String[] strArr2;
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            if (sdkBase != null) {
                String channelPropStr = sdkBase.getChannelPropStr(str);
                if (!TextUtils.isEmpty(channelPropStr)) {
                    return channelPropStr;
                }
            }
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
            if (sdkBase2 != null) {
                String channelPropStr2 = sdkBase2.getChannelPropStr(str);
                if (!TextUtils.isEmpty(channelPropStr2)) {
                    return channelPropStr2;
                }
            }
        }
        String channelPropStr3 = getChannelPropStr(str);
        if (!TextUtils.isEmpty(channelPropStr3)) {
            return channelPropStr3;
        }
        String strB = b(str);
        if (!strB.startsWith("SDC_LOG")) {
            String str2 = this.N.containsKey(ConstProp.ENABLE_CLIENT_CHECK_REALNAME) && "1".equals(this.N.get(ConstProp.ENABLE_CLIENT_CHECK_REALNAME)) ? "sdk_token" : "gas_token";
            if (ConstProp.SAUTH_STR.equals(strB) && this.N.containsKey(strB)) {
                String str3 = this.N.get(strB) + "&step=" + UniSdkUtils.a(this.myCtx, 0, 0) + "&step2=" + UniSdkUtils.a(this.myCtx, 1, 0);
                if (!this.N.containsKey(ConstProp.UNISDK_JF_GAS_TOKEN)) {
                    return str3;
                }
                String strEncode = this.N.get(ConstProp.UNISDK_JF_GAS_TOKEN);
                if (TextUtils.isEmpty(strEncode)) {
                    return str3;
                }
                try {
                    strEncode = URLEncoder.encode(strEncode, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    UniSdkUtils.d("UniSDK Base", "UnsupportedEncodingException" + e.getMessage());
                }
                return str3 + com.alipay.sdk.m.s.a.l + str2 + "=" + strEncode;
            }
            if (ConstProp.SAUTH_JSON.equals(strB) && this.N.containsKey(strB)) {
                String str4 = this.N.get(ConstProp.UNISDK_JF_GAS_TOKEN);
                if (TextUtils.isEmpty(str4)) {
                    strArr = new String[]{OneTrackParams.XMSdkParams.STEP, "step2"};
                    strArr2 = new String[]{UniSdkUtils.a(this.myCtx, 0, 0), UniSdkUtils.a(this.myCtx, 1, 0)};
                } else {
                    strArr = new String[]{OneTrackParams.XMSdkParams.STEP, "step2", str2};
                    strArr2 = new String[]{UniSdkUtils.a(this.myCtx, 0, 0), UniSdkUtils.a(this.myCtx, 1, 0), str4};
                }
                return StrUtil.getAppendedJsonStr(this.N.get(strB), strArr, strArr2);
            }
            if ("DCTOOL_DEVICEINFO".equals(strB)) {
                JSONObject jSONObject = new JSONObject();
                String strNtGetNetworktype = UniSdkUtils.ntGetNetworktype(this.myCtx);
                String[] ramMemory = UniSdkUtils.getRamMemory(this.myCtx);
                String strNtGetCpuName = UniSdkUtils.ntGetCpuName();
                String curCpuFreq = UniSdkUtils.getCurCpuFreq();
                String deviceUDID = UniSdkUtils.getDeviceUDID(this.myCtx);
                try {
                    jSONObject.put("network_type", strNtGetNetworktype);
                    jSONObject.put("mem_total", ramMemory[0]);
                    jSONObject.put("mem_idle", ramMemory[1]);
                    jSONObject.put(DecoderConfig.KEY_CPU_MODEL, strNtGetCpuName);
                    jSONObject.put("cpu_clockspeed", curCpuFreq);
                    jSONObject.put("device_id", deviceUDID);
                    jSONObject.put("mobile_type", UniSdkUtils.getMobildBrand() + " " + UniSdkUtils.getMobileModel());
                    jSONObject.put("os", "Android");
                    jSONObject.put("os_version", UniSdkUtils.getMobileVersion());
                    jSONObject.put(ClientLogConstant.TRANSID, UniSdkUtils.getTransid(this.myCtx));
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
                UniSdkUtils.i("UniSDK Base", "SdkBase [getPropStr] json=" + jSONObject.toString());
                return jSONObject.toString();
            }
            if (ConstProp.UDID.equals(strB)) {
                return UniSdkUtils.getDeviceUDID(this.myCtx);
            }
            if (ConstProp.UNISDK_DEVICE_ID.equals(strB)) {
                return UniSdkUtils.getUnisdkDeviceId(this.myCtx);
            }
            if (ConstProp.ORI_DEVICE_ID.equals(strB)) {
                return UniSdkUtils.b(this.myCtx, SdkMgr.getInst());
            }
            if (ConstProp.ORI_ADVERTISING_ID.equals(strB)) {
                return UniSdkUtils.c(this.myCtx, SdkMgr.getInst());
            }
            if ("OAID".equals(strB)) {
                return UniSdkUtils.getOAID(this.myCtx);
            }
            if (this.N.containsKey(strB)) {
                if (ConstProp.UNISDK_JF_ACCESS_TOKEN.equals(strB)) {
                    ApiRequestUtil.refreshToken();
                }
                return this.N.get(strB);
            }
            if (ConstProp.TRANS_ID.equals(strB)) {
                return UniSdkUtils.getTransid(this.myCtx);
            }
            if (!ConstProp.WIFI_INFO_LIST.equals(strB)) {
                return null;
            }
            try {
                JSONArray jSONArrayOptJSONArray = new JSONObject(UniSdkUtils.getWifiListJson()).optJSONArray("wifi");
                if (jSONArrayOptJSONArray != null) {
                    return jSONArrayOptJSONArray.toString();
                }
                return null;
            } catch (Exception unused) {
                return null;
            }
        }
        if (ConstProp.SDC_LOG_DEVICE_WIDTH.equals(strB)) {
            return Integer.toString(UniSdkUtils.getDisplayPixels(this.myCtx)[0]);
        }
        if (ConstProp.SDC_LOG_DEVICE_HEIGHT.equals(strB)) {
            return Integer.toString(UniSdkUtils.getDisplayPixels(this.myCtx)[1]);
        }
        if (ConstProp.SDC_LOG_OS_NAME.equals(strB)) {
            return "android";
        }
        if (ConstProp.SDC_LOG_OS_VER.equals(strB)) {
            return UniSdkUtils.getMobileVersion();
        }
        if (ConstProp.SDC_LOG_MAC_ADDR.equals(strB)) {
            return UniSdkUtils.getMacAddress(this.myCtx);
        }
        if (ConstProp.SDC_LOG_DEVICE_MODEL.equals(strB)) {
            return UniSdkUtils.getMobileModel2();
        }
        if (ConstProp.SDC_LOG_UDID.equals(strB)) {
            return getUdid();
        }
        if (ConstProp.SDC_LOG_APP_CHANNEL.equals(strB)) {
            return getAppChannel();
        }
        if (ConstProp.SDC_LOG_APP_NETWORK.equals(strB)) {
            return NetConnectivity.getNetworkType(this.myCtx);
        }
        if (ConstProp.SDC_LOG_APP_ISP.equals(strB)) {
            return UniSdkUtils.getMobileIMSI(this.myCtx);
        }
        if (ConstProp.SDC_LOG_APP_VER.equals(strB)) {
            return UniSdkUtils.getAppVersionName(this.myCtx);
        }
        if (ConstProp.SDC_LOG_MOBILE_MODEL.equals(strB)) {
            return UniSdkUtils.getMobileModel();
        }
        if (ConstProp.SDC_LOG_MOBILE_SDKVERSION.equals(strB)) {
            StringBuilder sb = new StringBuilder();
            sb.append(UniSdkUtils.getMobileSDKVersion());
            return sb.toString();
        }
        if (ConstProp.SDC_LOG_MOBILE_MANUFACTURER.equals(strB)) {
            return UniSdkUtils.getMobileManufacturer();
        }
        if (ConstProp.SDC_LOG_MOBILE_BRAND.equals(strB)) {
            return UniSdkUtils.getMobildBrand();
        }
        if (ConstProp.SDC_LOG_APP_PACKAGENAME.equals(strB)) {
            return UniSdkUtils.getAppPackageName(this.myCtx);
        }
        if (ConstProp.SDC_LOG_APP_VERSIONCODE.equals(strB)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(UniSdkUtils.getAppVersionCode(this.myCtx));
            return sb2.toString();
        }
        if (ConstProp.SDC_LOG_SYSTEMLANGUAGE.equals(strB)) {
            return UniSdkUtils.getSystemLanguage();
        }
        if (ConstProp.SDC_LOG_SIM_COUNTRY.equals(strB)) {
            return UniSdkUtils.getSimCountryIso();
        }
        if (ConstProp.SDC_LOG_CPU_MHZ.equals(strB)) {
            return UniSdkUtils.getCpuMhz();
        }
        if (ConstProp.SDC_LOG_CPU_CORE.equals(strB)) {
            return UniSdkUtils.getCpuCore();
        }
        if (ConstProp.SDC_LOG_CPU_NAME.equals(strB)) {
            return UniSdkUtils.getCpuName();
        }
        return ConstProp.SDC_LOG_CPU_CURFREQ.equals(strB) ? UniSdkUtils.getCurCpuFreq() : "";
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getPropStr(String str, String str2) throws Throwable {
        String propStr = getPropStr(str);
        return TextUtils.isEmpty(propStr) ? str2 : propStr;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setPropStr(String str, String str2) throws JSONException {
        String strB = b(str);
        UniSdkUtils.d("UniSDK Base", "key:" + strB + ",val:" + str2);
        if (str2 == null) {
            this.N.remove(strB);
        } else {
            if (ConstProp.FULL_UID.equals(strB)) {
                setPropStr(ConstProp.UID, str2.substring(0, str2.lastIndexOf("@")));
            } else if ("ENABLE_FAKE_ABOUT_ID".equals(strB)) {
                ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", String.format("{\"methodId\":\"setFake\",\"isFake\":%s}", Boolean.valueOf("1".equals(str2))));
            }
            this.N.put(strB, str2);
            if (!"".equals(str2) && ConstProp.UID.equals(strB)) {
                this.N.put(ConstProp.GAS3_UID, str2);
            }
            if (ConstProp.UNISDK_LOGIN_JSON.equals(strB)) {
                try {
                    if (!TextUtils.isEmpty(str2)) {
                        String str3 = new String(Base64.decode(str2, 0), "UTF-8");
                        if (!TextUtils.isEmpty(str3)) {
                            String strOptString = new JSONObject(str3).optString("aas_version");
                            if (!TextUtils.isEmpty(strOptString)) {
                                SdkMgr.getInst().setPropStr(ConstProp.JF_LOGIN_AAS_VERSION, strOptString);
                            }
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e2) {
                    e2.printStackTrace();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
            if (ConstProp.UNISDK_JF_ACCESS_TOKEN.equals(strB)) {
                ApiRequestUtil.setAccessToken(str2);
            }
            if (ConstProp.GP_MINOR_STATUS.equals(strB) && ModulesManager.getInst() != null) {
                ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"setMinorStatus\",\"status\":" + str2 + com.alipay.sdk.m.u.i.d);
            }
            if (ConstProp.ENABLE_HTTP_DNS.equals(strB)) {
                if ("1".equals(str2)) {
                    HttpDns.getInstance().setEnable(true);
                } else {
                    HttpDns.getInstance().setEnable(false);
                }
            }
        }
        if (ConstProp.USERINFO_AID.equals(strB) && "g18".equals(getPropStr("JF_GAMEID"))) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "setPropStr_".concat(String.valueOf(strB)));
                if (TextUtils.isEmpty(str2)) {
                    jSONObject.putOpt(strB, com.netease.ntunisdk.external.protocol.Const.EMPTY);
                } else {
                    jSONObject.putOpt(strB, str2);
                }
            } catch (JSONException e4) {
                UniSdkUtils.d("UniSDK Base", "extraJson:" + e4.getMessage());
            }
            saveClientLog(null, jSONObject.toString());
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public int getPropInt(String str, int i) {
        String propStr = getPropStr(str);
        if (propStr == null) {
            return i;
        }
        try {
            return Integer.parseInt(propStr);
        } catch (Exception unused) {
            return i;
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setPropInt(String str, int i) throws JSONException {
        setPropStr(str, Integer.toString(i));
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public boolean hasFeature(String str) {
        synchronized (hasFeatureLock) {
            Iterator<String> it = this.sdkInstMap.keySet().iterator();
            do {
                boolean z = true;
                if (!it.hasNext()) {
                    if (getPropInt(str, 0) == 0) {
                        z = false;
                    }
                    return z;
                }
            } while (this.sdkInstMap.get(it.next()).getPropInt(str, 0) == 0);
            return true;
        }
    }

    public void setFeature(String str, boolean z) throws JSONException {
        setPropInt(str, z ? 1 : 0);
    }

    public void resetCommonProp() throws JSONException {
        setPropInt(ConstProp.LOGIN_STAT, 0);
        setPropStr(ConstProp.UID, null);
        setPropStr(ConstProp.SESSION, null);
        setFeature(ConstProp.REQUIRE_AI_DETECT, false);
        setPropStr(ConstProp.AI_GLDT_TOKEN, null);
        setPropStr(ConstProp.AI_GLDT_TIMESTAMP, null);
        setPropStr(ConstProp.AI_GLDT_ALL, null);
        setPropStr(ConstProp.PROTOCOL_IN_LOGIN_SRC, null);
        setPropStr(ConstProp.UNISDK_JF_ACCESS_TOKEN, null);
        setPropStr(ConstProp.UNISDK_JF_REFRESH_TOKEN, null);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "resetCommonProp");
        } catch (JSONException e) {
            UniSdkUtils.d("UniSDK Base", "extraJson:" + e.getMessage());
        }
        saveClientLog(null, jSONObject.toString());
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public boolean hasLogin() {
        return (SdkMgr.getInst() == null || SdkMgr.getInst().getPropInt(ConstProp.ENABLE_NEW_HASLOGIN, 0) != 1) ? getPropInt(ConstProp.LOGIN_STAT, 0) == 1 || hasGuestLogined() : Q && (getPropInt(ConstProp.LOGIN_STAT, 0) == 1 || hasGuestLogined());
    }

    public void setLoginStat(int i) throws JSONException {
        setPropInt(ConstProp.LOGIN_STAT, i);
    }

    protected GLSurfaceView getGlView() {
        return this.t;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setGlView(GLSurfaceView gLSurfaceView) {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setGlView(gLSurfaceView);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setGlView(gLSurfaceView);
        }
        this.t = gLSurfaceView;
        gLSurfaceView.getHolder().addCallback(this);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getAppChannel() {
        UniSdkUtils.i("UniSDK Base", "APP_CHANNEL:" + getPropStr(ConstProp.APP_CHANNEL));
        return getPropStr(ConstProp.APP_CHANNEL);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getPayChannel() {
        if (hasFeature("VIRTUAL_ORDER")) {
            return "basechannel";
        }
        PayChannelManager payChannelManager = this.K;
        if (payChannelManager != null && payChannelManager.allyPayEnabled()) {
            return "allysdk";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            if (1 != sdkBase.getPropInt(ConstProp.INNER_MODE_NO_PAY, 0)) {
                if (sb.length() > 0) {
                    sb.append(Marker.ANY_NON_NULL_MARKER);
                }
                sb.append(sdkBase.getChannel());
            }
        }
        return sb.toString();
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getFFChannelByPid(String str) {
        return getPayChannelByPid(str);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getPayChannelByPid(String str) {
        if (hasFeature("VIRTUAL_ORDER")) {
            return "basechannel";
        }
        PayChannelManager payChannelManager = this.K;
        if (payChannelManager != null && payChannelManager.allyPayEnabled()) {
            return "allysdk";
        }
        if (!OrderInfo.hasProduct(str)) {
            return "";
        }
        String orderChannel = new OrderInfo(str).getOrderChannel();
        return c() ? (orderChannel.equals("g_10086") || orderChannel.equals("mm_10086")) ? this.A : orderChannel : orderChannel;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntOpenExitView() {
        if (hasFeature(ConstProp.MODE_EXIT_VIEW)) {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.100
                AnonymousClass100() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    if (SdkBase.this.getPropInt(ConstProp.MODE_EXIT_VIEW, 0) != 0) {
                        SdkBase.this.openExitView();
                        return;
                    }
                    String channelByImsi = SdkBase.this.getChannelByImsi();
                    SdkBase sdkBase = SdkBase.this.sdkInstMap.get(channelByImsi);
                    if (sdkBase == null && "mm_10086".equals(channelByImsi)) {
                        sdkBase = SdkBase.this.sdkInstMap.get("g_10086");
                    }
                    if (sdkBase != null && sdkBase.getPropInt(ConstProp.MODE_EXIT_VIEW, 0) != 0) {
                        sdkBase.openExitView();
                        return;
                    }
                    Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                    while (it.hasNext()) {
                        SdkBase sdkBase2 = SdkBase.this.sdkInstMap.get(it.next());
                        if (sdkBase2.getPropInt(ConstProp.MODE_EXIT_VIEW, 0) != 0) {
                            sdkBase2.openExitView();
                            return;
                        }
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$100 */
    final class AnonymousClass100 implements Runnable {
        AnonymousClass100() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (SdkBase.this.getPropInt(ConstProp.MODE_EXIT_VIEW, 0) != 0) {
                SdkBase.this.openExitView();
                return;
            }
            String channelByImsi = SdkBase.this.getChannelByImsi();
            SdkBase sdkBase = SdkBase.this.sdkInstMap.get(channelByImsi);
            if (sdkBase == null && "mm_10086".equals(channelByImsi)) {
                sdkBase = SdkBase.this.sdkInstMap.get("g_10086");
            }
            if (sdkBase != null && sdkBase.getPropInt(ConstProp.MODE_EXIT_VIEW, 0) != 0) {
                sdkBase.openExitView();
                return;
            }
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase2 = SdkBase.this.sdkInstMap.get(it.next());
                if (sdkBase2.getPropInt(ConstProp.MODE_EXIT_VIEW, 0) != 0) {
                    sdkBase2.openExitView();
                    return;
                }
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnStart() {
        try {
            if (this.S.get() == 0 || this.S.get() == 2) {
                this.S.set(2);
            } else {
                if (this.S.get() == 1) {
                    this.S.set(3);
                }
                Iterator<String> it = this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase sdkBase = this.sdkInstMap.get(it.next());
                    if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                        sdkBase.sdkOnStart();
                    }
                }
                Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
                while (it2.hasNext()) {
                    SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
                    if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                        sdkBase2.sdkOnStart();
                    }
                }
            }
            if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
                sdkOnStart();
            }
            ModulesManager.getInst().onStart();
        } catch (ConcurrentModificationException unused) {
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnUserLeaveHint() {
        try {
            Iterator<String> it = this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = this.sdkInstMap.get(it.next());
                if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                    sdkBase.sdkOnUserLeaveHint();
                }
            }
            Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
            while (it2.hasNext()) {
                SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
                if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                    sdkBase2.sdkOnUserLeaveHint();
                }
            }
            if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
                sdkOnUserLeaveHint();
            }
            ModulesManager.getInst().onUserLeaveHint();
        } catch (ConcurrentModificationException unused) {
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnPause() {
        try {
            if (this.S.get() == 3) {
                Iterator<String> it = this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase sdkBase = this.sdkInstMap.get(it.next());
                    if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                        sdkBase.sdkOnPause();
                    }
                }
                Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
                while (it2.hasNext()) {
                    SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
                    if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                        sdkBase2.sdkOnPause();
                    }
                }
            }
            if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
                sdkOnPause();
            }
            ModulesManager.getInst().onPause();
            LifeCycleChecker.getInst().onGamePause();
        } catch (ConcurrentModificationException unused) {
        }
        WebViewProxy.getInstance().onPause();
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnBackPressed() {
        try {
            Iterator<String> it = this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = this.sdkInstMap.get(it.next());
                if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                    sdkBase.sdkOnBackPressed();
                }
            }
            Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
            while (it2.hasNext()) {
                SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
                if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                    sdkBase2.sdkOnBackPressed();
                }
            }
            if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
                sdkOnBackPressed();
            }
            ModulesManager.getInst().onBackPressed();
        } catch (ConcurrentModificationException unused) {
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        try {
            Iterator<String> it = this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = this.sdkInstMap.get(it.next());
                if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                    sdkBase.sdkOnRequestPermissionsResult(i, strArr, iArr);
                }
            }
            Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
            while (it2.hasNext()) {
                SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
                if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                    sdkBase2.sdkOnRequestPermissionsResult(i, strArr, iArr);
                }
            }
            if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
                sdkOnRequestPermissionsResult(i, strArr, iArr);
            }
            ModulesManager.getInst().onRequestPermissionsResult(i, strArr, iArr);
        } catch (ConcurrentModificationException unused) {
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnStop() {
        try {
            if (this.S.get() == 3) {
                Iterator<String> it = this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase sdkBase = this.sdkInstMap.get(it.next());
                    if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                        sdkBase.sdkOnStop();
                    }
                }
                Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
                while (it2.hasNext()) {
                    SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
                    if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                        sdkBase2.sdkOnStop();
                    }
                }
            }
            if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
                sdkOnStop();
            }
            ModulesManager.getInst().onStop();
        } catch (ConcurrentModificationException unused) {
        }
        if (useNewSdkProcedure()) {
            g.c();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnResume() {
        try {
            if (this.S.get() == 0 || this.S.get() == 2) {
                this.S.set(2);
            } else {
                if (this.S.get() == 1) {
                    this.S.set(3);
                }
                Iterator<String> it = this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase sdkBase = this.sdkInstMap.get(it.next());
                    if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                        sdkBase.sdkOnResume();
                        sdkBase.e();
                    }
                }
                Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
                while (it2.hasNext()) {
                    SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
                    if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                        sdkBase2.sdkOnResume();
                        sdkBase2.e();
                    }
                }
            }
            if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
                sdkOnResume();
                e();
            }
            ModulesManager.getInst().onResume();
            LifeCycleChecker.getInst().onGameResume();
        } catch (ConcurrentModificationException unused) {
        }
        WebViewProxy.getInstance().onResume();
        if (useNewSdkProcedure()) {
            g.d();
        }
    }

    private void e() {
        if (this.I) {
            return;
        }
        this.I = true;
        synchronized (this.J) {
            Iterator<String> it = this.J.iterator();
            while (it.hasNext()) {
                extendFuncCall(it.next());
            }
            this.J.clear();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnRestart() {
        try {
            Iterator<String> it = this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = this.sdkInstMap.get(it.next());
                if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                    sdkBase.sdkOnRestart();
                }
            }
            Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
            while (it2.hasNext()) {
                SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
                if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                    sdkBase2.sdkOnRestart();
                }
            }
            if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
                sdkOnRestart();
            }
            ModulesManager.getInst().onRestart();
        } catch (ConcurrentModificationException unused) {
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnActivityResult(int i, int i2, Intent intent) {
        try {
            Iterator<String> it = this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = this.sdkInstMap.get(it.next());
                if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                    sdkBase.sdkOnActivityResult(i, i2, intent);
                }
            }
            Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
            while (it2.hasNext()) {
                SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
                if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                    sdkBase2.sdkOnActivityResult(i, i2, intent);
                }
            }
            if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
                sdkOnActivityResult(i, i2, intent);
            }
            ModulesManager.getInst().onActivityResult(i, i2, intent);
        } catch (ConcurrentModificationException unused) {
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public boolean handleOnKeyDown(int i, KeyEvent keyEvent) {
        if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
            return sdkOnKeyDown(i, keyEvent);
        }
        return false;
    }

    protected boolean hasInitAlready() {
        return this.hasInit;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntInit(OnFinishInitListener onFinishInitListener) throws JSONException {
        MCountProxy.getInst().init(this.myCtx);
        ClientLog.getInst().startUniTransaction(this.myCtx);
        this.C = System.currentTimeMillis();
        UniSdkUtils.a();
        if (hasInitAlready()) {
            UniSdkUtils.i("UniSDK Base", "ntInit already");
            if (SdkMgr.getInst().hasFeature(ConstProp.REINIT_ALL_SDK)) {
                UniSdkUtils.i("UniSDK Base", "reInit all sdks");
                a(0, onFinishInitListener);
                return;
            }
            return;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "init_start_base");
        } catch (JSONException e) {
            UniSdkUtils.d("UniSDK Base", "extraJson:" + e.getMessage());
        }
        saveClientLog(null, jSONObject.toString());
        try {
            HttpDns.getInstance().init(this.myCtx, SdkMgr.getInst().getPropStr("JF_GAMEID"), "1".equalsIgnoreCase(SdkMgr.getInst().getPropStr("EB")) ? 1 : 0, UniSdkUtils.isDebug);
            HttpDns.getInstance().registerUrl(SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_GAS3_URL));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.91
            AnonymousClass91() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.N.put("APP_PACKAGE_NAME", UniSdkUtils.getAppPackageName(SdkBase.this.myCtx));
                Hashtable hashtable = SdkBase.this.N;
                StringBuilder sb = new StringBuilder();
                sb.append(UniSdkUtils.getAppVersionCode(SdkBase.this.myCtx));
                hashtable.put("APP_VERSION_CODE", sb.toString());
                SdkBase.this.N.put("APP_VERSION_NAME", UniSdkUtils.getAppVersionName(SdkBase.this.myCtx));
                SdkBase.this.N.put("IS_EMULATOR", UniSdkUtils.isEmulator(SdkBase.this.myCtx) ? "1" : "0");
                SdkBase.this.N.put("IS_MUMU", UniSdkUtils.isMuMu() ? "1" : "0");
                SdkBase.this.N.put("IS_DSEMULATOR", UniSdkUtils.isDsEmulator() ? "1" : "0");
                SdkBase.this.N.put("IS_DEVICE_ROOTED", UniSdkUtils.isDeviceRooted() ? "1" : "0");
            }
        });
        ModulesManager.getInst().addModuleCallback(ConstProp.UNISDKBASE, new ModulesCallback() { // from class: com.netease.ntunisdk.base.SdkBase.101
            AnonymousClass101() {
            }

            @Override // com.netease.ntunisdk.modules.api.ModulesCallback
            public final void extendFuncCallback(String str, String str2, String str3) {
                UniSdkUtils.d("UniSDK Base", "ModulesManager extendFuncCallback source:" + str + " module:" + str2 + " json:" + str3);
                SdkBase.b(SdkBase.this, str3);
                ((Activity) SdkBase.this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.101.1

                    /* renamed from: a */
                    final /* synthetic */ String f1637a;

                    AnonymousClass1(String str32) {
                        str = str32;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        SdkBase.this.extendFuncCall(str);
                    }
                });
            }

            /* renamed from: com.netease.ntunisdk.base.SdkBase$101$1 */
            final class AnonymousClass1 implements Runnable {

                /* renamed from: a */
                final /* synthetic */ String f1637a;

                AnonymousClass1(String str32) {
                    str = str32;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.extendFuncCall(str);
                }
            }
        });
        if (SdkMgr.getInst() != null && SdkMgr.getInst().hasFeature(ConstProp.ENABLE_SPLASH_IN_ADVANCE) && getPropInt(ConstProp.SPLASH, 0) == 1) {
            this.R.set(-2);
            StartupDialog.popStartup(this.myCtx, new StartupDialog.StartupFinishListener() { // from class: com.netease.ntunisdk.base.SdkBase.103

                /* renamed from: a */
                final /* synthetic */ OnFinishInitListener f1639a;

                AnonymousClass103(OnFinishInitListener onFinishInitListener2) {
                    onFinishInitListener = onFinishInitListener2;
                }

                @Override // com.netease.ntunisdk.base.StartupDialog.StartupFinishListener
                public final void finishListener() throws Throwable {
                    if (SdkBase.this.R.get() < 0) {
                        SdkBase.this.R.set(-1);
                    } else {
                        SdkBase sdkBase = SdkBase.this;
                        SdkBase.a(sdkBase, onFinishInitListener, sdkBase.R.get());
                    }
                }
            }, getPropInt(ConstProp.SPLASH_COLOR, -1));
            setPropInt(ConstProp.SPLASH, 0);
        }
        ntInit();
        c(getDetectData(60, 0, ""));
        UniSdkUtils.b();
        String appChannel = getAppChannel();
        String platform = getPlatform();
        UniSdkUtils.d("UniSDK Base", "sac = " + appChannel + ", sp = " + platform);
        setPropStr(ConstProp.SOURCE_APP_CHANNEL, appChannel);
        setPropStr(ConstProp.PRI_SAC, appChannel);
        setPropStr(ConstProp.SOURCE_PLATFORM, platform);
        setPropStr(ConstProp.PRI_SP, platform);
        a((JSONObject) null);
        SDKSwitcher.getInstance().start(new SDKSwitcher.ParseDoneCallback() { // from class: com.netease.ntunisdk.base.SdkBase.104
            AnonymousClass104() {
            }

            @Override // com.netease.ntunisdk.base.SDKSwitcher.ParseDoneCallback
            public final void onResult(HashMap<String, Boolean> map) throws JSONException {
                boolean zBooleanValue;
                UniSdkUtils.i("UniSDK Base", "SDKSwitcher result = " + SDKSwitcher.getInstance().getSDKSwitcherMap().toString());
                JSONObject jSONObject2 = new JSONObject();
                try {
                    jSONObject2.put("methodId", "ntSdkSwitch");
                    jSONObject2.put("result", "0");
                    SdkBase.this.extendFuncCall(jSONObject2.toString());
                } catch (Exception e3) {
                    UniSdkUtils.i("UniSDK Base", "enableFreeFlow Exception:".concat(String.valueOf(e3)));
                }
                HashMap<String, Boolean> sDKSwitcherMap = SDKSwitcher.getInstance().getSDKSwitcherMap();
                if (sDKSwitcherMap != null) {
                    UniSdkUtils.i("UniSDK Base", "switcherMap =" + sDKSwitcherMap.toString());
                    boolean zBooleanValue2 = sDKSwitcherMap.containsKey("ngdevice") ? sDKSwitcherMap.get("ngdevice").booleanValue() : false;
                    zBooleanValue = sDKSwitcherMap.containsKey("ngdevice_init_post") ? sDKSwitcherMap.get("ngdevice_init_post").booleanValue() : false;
                    z = zBooleanValue2;
                } else {
                    zBooleanValue = false;
                }
                UniSdkUtils.i("UniSDK Base", "ngDeviceIsOpen=" + z + ", ngDeviceIsPostInfoInInit=" + zBooleanValue);
                if (zBooleanValue) {
                    DeviceDataCenter.getInstance().setmPostDataInInit(zBooleanValue);
                }
                if (z) {
                    DeviceDataCenter.getInstance().initDeviceInfos(SdkBase.this.myCtx);
                }
                SDKPharos.getInstance().setContext(SdkBase.this.myCtx);
                JSONObject jSONObject3 = new JSONObject();
                try {
                    jSONObject3.put("methodId", "pharosprobe");
                    String propStr = SdkMgr.getInst().getPropStr("JF_GAMEID");
                    if (!TextUtils.isEmpty(propStr)) {
                        jSONObject3.put("project", propStr);
                        jSONObject3.put("options", 1);
                        jSONObject3.put("harborudp", com.facebook.hermes.intl.Constants.CASEFIRST_FALSE);
                        UniSdkUtils.i("UniSDK Base", "SDKPharos params = " + jSONObject3.toString());
                        SDKPharos.getInstance().setPharosListener(null);
                        SDKPharos.getInstance().extendFunc(jSONObject3.toString());
                        return;
                    }
                    UniSdkUtils.i("UniSDK Base", "SDKPharos gameid is null");
                } catch (JSONException e4) {
                    UniSdkUtils.e("UniSDK Base", "SdkBase start SDKPharos JSONException = ".concat(String.valueOf(e4)));
                    e4.printStackTrace();
                }
            }
        }, this.myCtx);
        HTTPQueue.getInstance("LOG").init(this.myCtx, this.M);
        HTTPQueue.getInstance("UniSDK").init(this.myCtx, this.M);
        HTTPQueue.getInstance(HTTPQueue.HTTPQUEUE_PAY).init(this.myCtx, null);
        UniSdkUtils.d("UniSDK Base", "needCheckChannelRemote()=" + c());
        if (c()) {
            this.A = getPropStr(ConstProp.DEFAULT_CMCC_PAYTYPE, "mm_10086");
            queryCmccPaytype();
        }
        if (SdkMgr.getInst() != null && SdkMgr.getInst().hasFeature(ConstProp.ENABLE_MAINSDKINT_IN_ADVANCE)) {
            this.S.set(0);
            CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.110
                AnonymousClass110() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.d(0);
                }
            });
            if (1 == getPropInt(ConstProp.DEBUG_MODE, 0)) {
                setDebugMode(true);
            } else {
                setDebugMode(false);
            }
            int propInt = SdkMgr.getInst().getPropInt(ConstProp.SPLASH_SECOND, 0);
            UniSdkUtils.d("UniSDK Base", "ConstProp.SPLASH_SECOND:".concat(String.valueOf(propInt)));
            if (1 == propInt) {
                init(new OnFinishInitListener() { // from class: com.netease.ntunisdk.base.SdkBase.111

                    /* renamed from: a */
                    final /* synthetic */ OnFinishInitListener f1647a;

                    AnonymousClass111(OnFinishInitListener onFinishInitListener2) {
                        onFinishInitListener = onFinishInitListener2;
                    }

                    @Override // com.netease.ntunisdk.base.OnFinishInitListener
                    public final void finishInit(int i) throws Throwable {
                        SdkBase.this.D = System.currentTimeMillis();
                        UniSdkUtils.d("UniSDK Base", SdkBase.this.getChannel() + " ntInit code: " + i);
                        if (i != 0 && i != 2) {
                            SdkBase.c(SdkBase.this.getDetectData(7, i, ""));
                        } else {
                            UniSdkUtils.d("UniSDK Base", "StartupDialog.popStartupSecond");
                            StartupDialog.popStartupSecond(SdkBase.this.myCtx, new StartupDialog.OnClickSplashFinishListener() { // from class: com.netease.ntunisdk.base.SdkBase.111.1
                                AnonymousClass1() {
                                }

                                @Override // com.netease.ntunisdk.base.StartupDialog.OnClickSplashFinishListener
                                public final void onClickSplash() {
                                    SdkBase.this.onClickSplashDone();
                                }
                            });
                            SdkBase.c(SdkBase.this.getDetectData(37, i, ""));
                        }
                        SdkBase.this.hasInit = true;
                        if (SdkBase.this.R.get() != -1) {
                            if (SdkBase.this.R.get() == -2) {
                                SdkBase.this.R.set(i);
                                return;
                            }
                            return;
                        }
                        SdkBase.a(SdkBase.this, onFinishInitListener, i);
                    }

                    /* renamed from: com.netease.ntunisdk.base.SdkBase$111$1 */
                    final class AnonymousClass1 implements StartupDialog.OnClickSplashFinishListener {
                        AnonymousClass1() {
                        }

                        @Override // com.netease.ntunisdk.base.StartupDialog.OnClickSplashFinishListener
                        public final void onClickSplash() {
                            SdkBase.this.onClickSplashDone();
                        }
                    }
                });
            } else {
                init(new OnFinishInitListener() { // from class: com.netease.ntunisdk.base.SdkBase.112

                    /* renamed from: a */
                    final /* synthetic */ OnFinishInitListener f1649a;

                    AnonymousClass112(OnFinishInitListener onFinishInitListener2) {
                        onFinishInitListener = onFinishInitListener2;
                    }

                    @Override // com.netease.ntunisdk.base.OnFinishInitListener
                    public final void finishInit(int i) throws Throwable {
                        SdkBase.this.D = System.currentTimeMillis();
                        UniSdkUtils.d("UniSDK Base", SdkBase.this.getChannel() + " ntInit code: " + i);
                        if (i == 0 || i == 2) {
                            SdkBase.c(SdkBase.this.getDetectData(37, i, ""));
                        } else {
                            SdkBase.c(SdkBase.this.getDetectData(7, i, ""));
                        }
                        SdkBase.this.hasInit = true;
                        if (SdkBase.this.R.get() != -1) {
                            if (SdkBase.this.R.get() == -2) {
                                SdkBase.this.R.set(i);
                                return;
                            }
                            return;
                        }
                        SdkBase.a(SdkBase.this, onFinishInitListener, i);
                    }
                });
            }
            this.x.add(getChannel());
            g();
            if (!SdkMgr.getInst().hasFeature(ConstProp.ENABLE_CHANGE_LOCATION) && this.myCtx != null && getSharedPref().getBoolean(UNISDK_FIRST_OPEN, true)) {
                saveLogToJFOnOpen();
                getSharedPref().edit().putBoolean(UNISDK_FIRST_OPEN, false).commit();
            }
        } else {
            a(0, onFinishInitListener2);
        }
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.105
            AnonymousClass105() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.h();
            }
        });
        if (SdkMgr.getInst() == null || SdkMgr.getInst().hasFeature(ConstProp.ENABLE_CHANGE_LOCATION)) {
            return;
        }
        ApiRequestUtil.generateTimestampDiff();
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$101 */
    final class AnonymousClass101 implements ModulesCallback {
        AnonymousClass101() {
        }

        @Override // com.netease.ntunisdk.modules.api.ModulesCallback
        public final void extendFuncCallback(String str, String str2, String str32) {
            UniSdkUtils.d("UniSDK Base", "ModulesManager extendFuncCallback source:" + str + " module:" + str2 + " json:" + str32);
            SdkBase.b(SdkBase.this, str32);
            ((Activity) SdkBase.this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.101.1

                /* renamed from: a */
                final /* synthetic */ String f1637a;

                AnonymousClass1(String str322) {
                    str = str322;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.extendFuncCall(str);
                }
            });
        }

        /* renamed from: com.netease.ntunisdk.base.SdkBase$101$1 */
        final class AnonymousClass1 implements Runnable {

            /* renamed from: a */
            final /* synthetic */ String f1637a;

            AnonymousClass1(String str322) {
                str = str322;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.extendFuncCall(str);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$103 */
    final class AnonymousClass103 implements StartupDialog.StartupFinishListener {

        /* renamed from: a */
        final /* synthetic */ OnFinishInitListener f1639a;

        AnonymousClass103(OnFinishInitListener onFinishInitListener2) {
            onFinishInitListener = onFinishInitListener2;
        }

        @Override // com.netease.ntunisdk.base.StartupDialog.StartupFinishListener
        public final void finishListener() throws Throwable {
            if (SdkBase.this.R.get() < 0) {
                SdkBase.this.R.set(-1);
            } else {
                SdkBase sdkBase = SdkBase.this;
                SdkBase.a(sdkBase, onFinishInitListener, sdkBase.R.get());
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$104 */
    final class AnonymousClass104 implements SDKSwitcher.ParseDoneCallback {
        AnonymousClass104() {
        }

        @Override // com.netease.ntunisdk.base.SDKSwitcher.ParseDoneCallback
        public final void onResult(HashMap<String, Boolean> map) throws JSONException {
            boolean zBooleanValue;
            UniSdkUtils.i("UniSDK Base", "SDKSwitcher result = " + SDKSwitcher.getInstance().getSDKSwitcherMap().toString());
            JSONObject jSONObject2 = new JSONObject();
            try {
                jSONObject2.put("methodId", "ntSdkSwitch");
                jSONObject2.put("result", "0");
                SdkBase.this.extendFuncCall(jSONObject2.toString());
            } catch (Exception e3) {
                UniSdkUtils.i("UniSDK Base", "enableFreeFlow Exception:".concat(String.valueOf(e3)));
            }
            HashMap<String, Boolean> sDKSwitcherMap = SDKSwitcher.getInstance().getSDKSwitcherMap();
            if (sDKSwitcherMap != null) {
                UniSdkUtils.i("UniSDK Base", "switcherMap =" + sDKSwitcherMap.toString());
                boolean zBooleanValue2 = sDKSwitcherMap.containsKey("ngdevice") ? sDKSwitcherMap.get("ngdevice").booleanValue() : false;
                zBooleanValue = sDKSwitcherMap.containsKey("ngdevice_init_post") ? sDKSwitcherMap.get("ngdevice_init_post").booleanValue() : false;
                z = zBooleanValue2;
            } else {
                zBooleanValue = false;
            }
            UniSdkUtils.i("UniSDK Base", "ngDeviceIsOpen=" + z + ", ngDeviceIsPostInfoInInit=" + zBooleanValue);
            if (zBooleanValue) {
                DeviceDataCenter.getInstance().setmPostDataInInit(zBooleanValue);
            }
            if (z) {
                DeviceDataCenter.getInstance().initDeviceInfos(SdkBase.this.myCtx);
            }
            SDKPharos.getInstance().setContext(SdkBase.this.myCtx);
            JSONObject jSONObject3 = new JSONObject();
            try {
                jSONObject3.put("methodId", "pharosprobe");
                String propStr = SdkMgr.getInst().getPropStr("JF_GAMEID");
                if (!TextUtils.isEmpty(propStr)) {
                    jSONObject3.put("project", propStr);
                    jSONObject3.put("options", 1);
                    jSONObject3.put("harborudp", com.facebook.hermes.intl.Constants.CASEFIRST_FALSE);
                    UniSdkUtils.i("UniSDK Base", "SDKPharos params = " + jSONObject3.toString());
                    SDKPharos.getInstance().setPharosListener(null);
                    SDKPharos.getInstance().extendFunc(jSONObject3.toString());
                    return;
                }
                UniSdkUtils.i("UniSDK Base", "SDKPharos gameid is null");
            } catch (JSONException e4) {
                UniSdkUtils.e("UniSDK Base", "SdkBase start SDKPharos JSONException = ".concat(String.valueOf(e4)));
                e4.printStackTrace();
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$105 */
    final class AnonymousClass105 implements Runnable {
        AnonymousClass105() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.h();
        }
    }

    private void ntInit() throws JSONException {
        String strA = UniSdkUtils.a(this.myCtx, this);
        if (TextUtils.isEmpty(strA)) {
            strA = "unknown";
        }
        String firstDeviceId = UniSdkUtils.getFirstDeviceId(this.myCtx);
        String str = TextUtils.isEmpty(firstDeviceId) ? "unknown" : firstDeviceId;
        setPropStr(ConstProp.DEVICE_ID, strA);
        setPropStr(ConstProp.UDID, strA);
        setPropStr(ConstProp.UNISDK_FIRST_DEVICE_ID, str);
        for (SdkBase sdkBase : getLoginSdkInstMap().values()) {
            sdkBase.setPropStr(ConstProp.DEVICE_ID, strA);
            sdkBase.setPropStr(ConstProp.UDID, strA);
            sdkBase.setPropStr(ConstProp.UNISDK_FIRST_DEVICE_ID, str);
        }
        for (SdkBase sdkBase2 : getSdkInstMap().values()) {
            sdkBase2.setPropStr(ConstProp.DEVICE_ID, strA);
            sdkBase2.setPropStr(ConstProp.UDID, strA);
            sdkBase2.setPropStr(ConstProp.UNISDK_FIRST_DEVICE_ID, str);
        }
        if (SdkMgr.getInst() == null || !SdkMgr.getInst().hasFeature("EB")) {
            return;
        }
        ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getAppSetID\"}");
    }

    private void a(JSONObject jSONObject) throws JSONException {
        String str = "";
        JSONObject jSONObject2 = new JSONObject();
        try {
            TimeZone timeZone = TimeZone.getDefault();
            jSONObject2.put(OneTrack.Param.TZ, timeZone.getDisplayName(false, 0).replace("GMT", "").replace("UTC", "").replace(":", ""));
            jSONObject2.put("tzid", timeZone.getID());
            jSONObject2.put(com.netease.ntunisdk.external.protocol.Const.COUNTRY, UniSdkUtils.getSimCountryIso());
            jSONObject2.put("celluar_ip", "");
            String simOperator = UniSdkUtils.getSimOperator(this.myCtx);
            if (simOperator != null) {
                str = simOperator;
            }
            jSONObject2.put("operator", str);
            jSONObject2.put("is_vpn_enabled", UniSdkUtils.isVpnRunning(this.myCtx));
        } catch (Exception e) {
            UniSdkUtils.d("UniSDK Base", "get timeZone exception:" + e.getMessage());
        }
        setPropStr(ConstProp.JF_AIM_INFO, jSONObject2.toString());
        try {
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put(com.netease.ntunisdk.external.protocol.Const.COUNTRY, jSONObject2.optString(com.netease.ntunisdk.external.protocol.Const.COUNTRY));
            jSONObject3.put(OneTrack.Param.TZ, jSONObject2.optString(OneTrack.Param.TZ));
            jSONObject3.put("tzid", jSONObject2.optString("tzid"));
            jSONObject3.put("celluar_ip", jSONObject2.optString("celluar_ip"));
            jSONObject3.put("operator", jSONObject2.optString("operator"));
            jSONObject3.put("is_vpn_enabled", jSONObject2.optString("is_vpn_enabled"));
            setPropStr(ConstProp.JF_AIM_INFO_2, jSONObject3.toString());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.SDK_WHOAMI_REQ_URL);
        if (TextUtils.isEmpty(propStr)) {
            propStr = SdkMgr.getInst().getPropInt("EB", 0) == 1 ? "https://whoami.nie.easebar.com/v6" : "https://whoami.nie.netease.com/v6";
        }
        if (TextUtils.isEmpty(propStr)) {
            UniSdkUtils.i("UniSDK Base", "null or empty url, request aim info will not go on");
            return;
        }
        HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
        queueItemNewQueueItem.method = "GET";
        queueItemNewQueueItem.url = propStr;
        queueItemNewQueueItem.callback = new HTTPCallback() { // from class: com.netease.ntunisdk.base.SdkBase.106

            /* renamed from: a */
            final /* synthetic */ JSONObject f1642a;
            final /* synthetic */ JSONObject c;

            AnonymousClass106(JSONObject jSONObject4, JSONObject jSONObject22) {
                jSONObject = jSONObject4;
                jSONObject = jSONObject22;
            }

            /* JADX WARN: Removed duplicated region for block: B:122:0x0198  */
            /* JADX WARN: Removed duplicated region for block: B:124:0x019c  */
            /* JADX WARN: Removed duplicated region for block: B:139:0x0053 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            @Override // com.netease.ntunisdk.base.utils.HTTPCallback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public final boolean processResult(java.lang.String r18, java.lang.String r19) throws java.lang.Throwable {
                /*
                    Method dump skipped, instructions count: 478
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.AnonymousClass106.processResult(java.lang.String, java.lang.String):boolean");
            }
        };
        HashMap map = new HashMap();
        map.put("X-AUTH-PRODUCT", "g0");
        map.put("X-AUTH-TOKEN", "token.efa8zUW6sxjR");
        map.put("X-IPDB-LOCALE", "en");
        if (SdkMgr.getInst() != null) {
            map.put("X-PROJECT-CODE", SdkMgr.getInst().getPropStr("JF_GAMEID"));
        }
        queueItemNewQueueItem.setHeaders(map);
        HTTPQueue.getInstance("UniSDK").addItem(queueItemNewQueueItem);
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$106 */
    final class AnonymousClass106 implements HTTPCallback {

        /* renamed from: a */
        final /* synthetic */ JSONObject f1642a;
        final /* synthetic */ JSONObject c;

        AnonymousClass106(JSONObject jSONObject4, JSONObject jSONObject22) {
            jSONObject = jSONObject4;
            jSONObject = jSONObject22;
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String v, String v2) throws Throwable {
            /*
                Method dump skipped, instructions count: 478
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.AnonymousClass106.processResult(java.lang.String, java.lang.String):boolean");
        }
    }

    public void a(int i, OnFinishInitListener onFinishInitListener) {
        SdkBase sdkBase;
        if (i >= this.sdkInstMap.size() + this.loginSdkInstMap.size()) {
            if (1 == getPropInt(ConstProp.DEBUG_MODE, 0)) {
                setDebugMode(true);
            } else {
                setDebugMode(false);
            }
            int propInt = SdkMgr.getInst().getPropInt(ConstProp.SPLASH_SECOND, 0);
            UniSdkUtils.d("UniSDK Base", "ConstProp.SPLASH_SECOND:".concat(String.valueOf(propInt)));
            if (1 == propInt) {
                init(new OnFinishInitListener() { // from class: com.netease.ntunisdk.base.SdkBase.107
                    final /* synthetic */ OnFinishInitListener c;

                    AnonymousClass107(OnFinishInitListener onFinishInitListener2) {
                        onFinishInitListener = onFinishInitListener2;
                    }

                    @Override // com.netease.ntunisdk.base.OnFinishInitListener
                    public final void finishInit(int i2) throws Throwable {
                        SdkBase.this.D = System.currentTimeMillis();
                        UniSdkUtils.d("UniSDK Base", SdkBase.this.getChannel() + " ntInit code: " + i2);
                        if (i2 != 0 && i2 != 2) {
                            SdkBase.c(SdkBase.this.getDetectData(7, i2, ""));
                        } else {
                            UniSdkUtils.d("UniSDK Base", "StartupDialog.popStartupSecond");
                            StartupDialog.popStartupSecond(SdkBase.this.myCtx, new StartupDialog.OnClickSplashFinishListener() { // from class: com.netease.ntunisdk.base.SdkBase.107.1
                                AnonymousClass1() {
                                }

                                @Override // com.netease.ntunisdk.base.StartupDialog.OnClickSplashFinishListener
                                public final void onClickSplash() {
                                    SdkBase.this.onClickSplashDone();
                                }
                            });
                            SdkBase.c(SdkBase.this.getDetectData(37, i2, ""));
                        }
                        SdkBase.this.hasInit = true;
                        if (SdkBase.this.R.get() != -1) {
                            if (SdkBase.this.R.get() == -2) {
                                SdkBase.this.R.set(i2);
                                return;
                            }
                            return;
                        }
                        SdkBase.a(SdkBase.this, onFinishInitListener, i2);
                    }

                    /* renamed from: com.netease.ntunisdk.base.SdkBase$107$1 */
                    final class AnonymousClass1 implements StartupDialog.OnClickSplashFinishListener {
                        AnonymousClass1() {
                        }

                        @Override // com.netease.ntunisdk.base.StartupDialog.OnClickSplashFinishListener
                        public final void onClickSplash() {
                            SdkBase.this.onClickSplashDone();
                        }
                    }
                });
            } else {
                init(new OnFinishInitListener() { // from class: com.netease.ntunisdk.base.SdkBase.108
                    final /* synthetic */ OnFinishInitListener c;

                    AnonymousClass108(OnFinishInitListener onFinishInitListener2) {
                        onFinishInitListener = onFinishInitListener2;
                    }

                    @Override // com.netease.ntunisdk.base.OnFinishInitListener
                    public final void finishInit(int i2) throws Throwable {
                        SdkBase.this.D = System.currentTimeMillis();
                        UniSdkUtils.d("UniSDK Base", SdkBase.this.getChannel() + " ntInit code: " + i2);
                        if (i2 == 0 || i2 == 2) {
                            SdkBase.c(SdkBase.this.getDetectData(37, i2, ""));
                        } else {
                            SdkBase.c(SdkBase.this.getDetectData(7, i2, ""));
                        }
                        SdkBase.this.hasInit = true;
                        if (SdkBase.this.R.get() != -1) {
                            if (SdkBase.this.R.get() == -2) {
                                SdkBase.this.R.set(i2);
                                return;
                            }
                            return;
                        }
                        SdkBase.a(SdkBase.this, onFinishInitListener, i2);
                    }
                });
            }
            this.x.add(getChannel());
            g();
            if (SdkMgr.getInst().hasFeature(ConstProp.ENABLE_CHANGE_LOCATION) || this.myCtx == null || !getSharedPref().getBoolean(UNISDK_FIRST_OPEN, true)) {
                return;
            }
            saveLogToJFOnOpen();
            getSharedPref().edit().putBoolean(UNISDK_FIRST_OPEN, false).commit();
            return;
        }
        if (i < this.loginSdkInstMap.size()) {
            Map<String, SdkBase> map = this.loginSdkInstMap;
            sdkBase = map.get(map.keySet().toArray()[i]);
        } else {
            Map<String, SdkBase> map2 = this.sdkInstMap;
            sdkBase = map2.get(map2.keySet().toArray()[i - this.loginSdkInstMap.size()]);
        }
        if (1 == sdkBase.getPropInt(ConstProp.DEBUG_MODE, 0)) {
            sdkBase.setDebugMode(true);
        } else {
            sdkBase.setDebugMode(false);
        }
        UniSdkUtils.d("UniSDK Base", sdkBase.getChannel() + " ntInit");
        if (sdkBase.getPropInt(ConstProp.INNER_MODE_SECOND_CHANNEL_NO_INIT, 0) == 0) {
            sdkBase.init(new OnFinishInitListener() { // from class: com.netease.ntunisdk.base.SdkBase.109
                final /* synthetic */ int b;
                final /* synthetic */ OnFinishInitListener c;

                AnonymousClass109(int i2, OnFinishInitListener onFinishInitListener2) {
                    i = i2;
                    onFinishInitListener = onFinishInitListener2;
                }

                @Override // com.netease.ntunisdk.base.OnFinishInitListener
                public final void finishInit(int i2) {
                    UniSdkUtils.d("UniSDK Base", " ntInit code: ".concat(String.valueOf(i2)));
                    SdkBase.this.a(i + 1, onFinishInitListener);
                }
            });
        } else {
            UniSdkUtils.d("UniSDK Base", "ship init:" + sdkBase.getChannel());
            a(i2 + 1, onFinishInitListener2);
        }
        this.x.add(sdkBase.getChannel());
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$107 */
    final class AnonymousClass107 implements OnFinishInitListener {
        final /* synthetic */ OnFinishInitListener c;

        AnonymousClass107(OnFinishInitListener onFinishInitListener2) {
            onFinishInitListener = onFinishInitListener2;
        }

        @Override // com.netease.ntunisdk.base.OnFinishInitListener
        public final void finishInit(int i2) throws Throwable {
            SdkBase.this.D = System.currentTimeMillis();
            UniSdkUtils.d("UniSDK Base", SdkBase.this.getChannel() + " ntInit code: " + i2);
            if (i2 != 0 && i2 != 2) {
                SdkBase.c(SdkBase.this.getDetectData(7, i2, ""));
            } else {
                UniSdkUtils.d("UniSDK Base", "StartupDialog.popStartupSecond");
                StartupDialog.popStartupSecond(SdkBase.this.myCtx, new StartupDialog.OnClickSplashFinishListener() { // from class: com.netease.ntunisdk.base.SdkBase.107.1
                    AnonymousClass1() {
                    }

                    @Override // com.netease.ntunisdk.base.StartupDialog.OnClickSplashFinishListener
                    public final void onClickSplash() {
                        SdkBase.this.onClickSplashDone();
                    }
                });
                SdkBase.c(SdkBase.this.getDetectData(37, i2, ""));
            }
            SdkBase.this.hasInit = true;
            if (SdkBase.this.R.get() != -1) {
                if (SdkBase.this.R.get() == -2) {
                    SdkBase.this.R.set(i2);
                    return;
                }
                return;
            }
            SdkBase.a(SdkBase.this, onFinishInitListener, i2);
        }

        /* renamed from: com.netease.ntunisdk.base.SdkBase$107$1 */
        final class AnonymousClass1 implements StartupDialog.OnClickSplashFinishListener {
            AnonymousClass1() {
            }

            @Override // com.netease.ntunisdk.base.StartupDialog.OnClickSplashFinishListener
            public final void onClickSplash() {
                SdkBase.this.onClickSplashDone();
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$108 */
    final class AnonymousClass108 implements OnFinishInitListener {
        final /* synthetic */ OnFinishInitListener c;

        AnonymousClass108(OnFinishInitListener onFinishInitListener2) {
            onFinishInitListener = onFinishInitListener2;
        }

        @Override // com.netease.ntunisdk.base.OnFinishInitListener
        public final void finishInit(int i2) throws Throwable {
            SdkBase.this.D = System.currentTimeMillis();
            UniSdkUtils.d("UniSDK Base", SdkBase.this.getChannel() + " ntInit code: " + i2);
            if (i2 == 0 || i2 == 2) {
                SdkBase.c(SdkBase.this.getDetectData(37, i2, ""));
            } else {
                SdkBase.c(SdkBase.this.getDetectData(7, i2, ""));
            }
            SdkBase.this.hasInit = true;
            if (SdkBase.this.R.get() != -1) {
                if (SdkBase.this.R.get() == -2) {
                    SdkBase.this.R.set(i2);
                    return;
                }
                return;
            }
            SdkBase.a(SdkBase.this, onFinishInitListener, i2);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$109 */
    final class AnonymousClass109 implements OnFinishInitListener {
        final /* synthetic */ int b;
        final /* synthetic */ OnFinishInitListener c;

        AnonymousClass109(int i2, OnFinishInitListener onFinishInitListener2) {
            i = i2;
            onFinishInitListener = onFinishInitListener2;
        }

        @Override // com.netease.ntunisdk.base.OnFinishInitListener
        public final void finishInit(int i2) {
            UniSdkUtils.d("UniSDK Base", " ntInit code: ".concat(String.valueOf(i2)));
            SdkBase.this.a(i + 1, onFinishInitListener);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$110 */
    final class AnonymousClass110 implements Runnable {
        AnonymousClass110() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.d(0);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$111 */
    final class AnonymousClass111 implements OnFinishInitListener {

        /* renamed from: a */
        final /* synthetic */ OnFinishInitListener f1647a;

        AnonymousClass111(OnFinishInitListener onFinishInitListener2) {
            onFinishInitListener = onFinishInitListener2;
        }

        @Override // com.netease.ntunisdk.base.OnFinishInitListener
        public final void finishInit(int i) throws Throwable {
            SdkBase.this.D = System.currentTimeMillis();
            UniSdkUtils.d("UniSDK Base", SdkBase.this.getChannel() + " ntInit code: " + i);
            if (i != 0 && i != 2) {
                SdkBase.c(SdkBase.this.getDetectData(7, i, ""));
            } else {
                UniSdkUtils.d("UniSDK Base", "StartupDialog.popStartupSecond");
                StartupDialog.popStartupSecond(SdkBase.this.myCtx, new StartupDialog.OnClickSplashFinishListener() { // from class: com.netease.ntunisdk.base.SdkBase.111.1
                    AnonymousClass1() {
                    }

                    @Override // com.netease.ntunisdk.base.StartupDialog.OnClickSplashFinishListener
                    public final void onClickSplash() {
                        SdkBase.this.onClickSplashDone();
                    }
                });
                SdkBase.c(SdkBase.this.getDetectData(37, i, ""));
            }
            SdkBase.this.hasInit = true;
            if (SdkBase.this.R.get() != -1) {
                if (SdkBase.this.R.get() == -2) {
                    SdkBase.this.R.set(i);
                    return;
                }
                return;
            }
            SdkBase.a(SdkBase.this, onFinishInitListener, i);
        }

        /* renamed from: com.netease.ntunisdk.base.SdkBase$111$1 */
        final class AnonymousClass1 implements StartupDialog.OnClickSplashFinishListener {
            AnonymousClass1() {
            }

            @Override // com.netease.ntunisdk.base.StartupDialog.OnClickSplashFinishListener
            public final void onClickSplash() {
                SdkBase.this.onClickSplashDone();
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$112 */
    final class AnonymousClass112 implements OnFinishInitListener {

        /* renamed from: a */
        final /* synthetic */ OnFinishInitListener f1649a;

        AnonymousClass112(OnFinishInitListener onFinishInitListener2) {
            onFinishInitListener = onFinishInitListener2;
        }

        @Override // com.netease.ntunisdk.base.OnFinishInitListener
        public final void finishInit(int i) throws Throwable {
            SdkBase.this.D = System.currentTimeMillis();
            UniSdkUtils.d("UniSDK Base", SdkBase.this.getChannel() + " ntInit code: " + i);
            if (i == 0 || i == 2) {
                SdkBase.c(SdkBase.this.getDetectData(37, i, ""));
            } else {
                SdkBase.c(SdkBase.this.getDetectData(7, i, ""));
            }
            SdkBase.this.hasInit = true;
            if (SdkBase.this.R.get() != -1) {
                if (SdkBase.this.R.get() == -2) {
                    SdkBase.this.R.set(i);
                    return;
                }
                return;
            }
            SdkBase.a(SdkBase.this, onFinishInitListener, i);
        }
    }

    public void d(int i) {
        SdkBase sdkBase;
        if (i >= this.sdkInstMap.size() + this.loginSdkInstMap.size()) {
            if (this.S.get() == 0) {
                this.S.set(1);
                return;
            }
            if (this.S.get() == 2) {
                this.S.set(3);
                Iterator<String> it = this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase sdkBase2 = this.sdkInstMap.get(it.next());
                    if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                        sdkBase2.sdkOnResume();
                        sdkBase2.e();
                        sdkBase2.sdkOnStart();
                    }
                }
                Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
                while (it2.hasNext()) {
                    SdkBase sdkBase3 = this.loginSdkInstMap.get(it2.next());
                    if (sdkBase3.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase3.getChannel())) {
                        sdkBase3.sdkOnResume();
                        sdkBase3.e();
                        sdkBase3.sdkOnStart();
                    }
                }
                return;
            }
            return;
        }
        if (i < this.loginSdkInstMap.size()) {
            Map<String, SdkBase> map = this.loginSdkInstMap;
            sdkBase = map.get(map.keySet().toArray()[i]);
        } else {
            Map<String, SdkBase> map2 = this.sdkInstMap;
            sdkBase = map2.get(map2.keySet().toArray()[i - this.loginSdkInstMap.size()]);
        }
        if (1 == sdkBase.getPropInt(ConstProp.DEBUG_MODE, 0)) {
            sdkBase.setDebugMode(true);
        } else {
            sdkBase.setDebugMode(false);
        }
        UniSdkUtils.d("UniSDK Base", sdkBase.getChannel() + " ntInit");
        if (sdkBase.getPropInt(ConstProp.INNER_MODE_SECOND_CHANNEL_NO_INIT, 0) == 0) {
            sdkBase.init(new OnFinishInitListener() { // from class: com.netease.ntunisdk.base.SdkBase.114

                /* renamed from: a */
                final /* synthetic */ int f1650a;

                AnonymousClass114(int i2) {
                    i = i2;
                }

                @Override // com.netease.ntunisdk.base.OnFinishInitListener
                public final void finishInit(int i2) {
                    UniSdkUtils.d("UniSDK Base", " ntInit code: ".concat(String.valueOf(i2)));
                    SdkBase.this.d(i + 1);
                }
            });
        } else {
            UniSdkUtils.d("UniSDK Base", "ship init:" + sdkBase.getChannel());
            d(i2 + 1);
        }
        this.x.add(sdkBase.getChannel());
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$114 */
    final class AnonymousClass114 implements OnFinishInitListener {

        /* renamed from: a */
        final /* synthetic */ int f1650a;

        AnonymousClass114(int i2) {
            i = i2;
        }

        @Override // com.netease.ntunisdk.base.OnFinishInitListener
        public final void finishInit(int i2) {
            UniSdkUtils.d("UniSDK Base", " ntInit code: ".concat(String.valueOf(i2)));
            SdkBase.this.d(i + 1);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x0061 A[Catch: JSONException -> 0x0175, TryCatch #0 {JSONException -> 0x0175, blocks: (B:54:0x0066, B:56:0x00a8, B:57:0x00b2, B:59:0x00da, B:60:0x00e3, B:48:0x002b, B:51:0x004d, B:50:0x0049, B:49:0x0030, B:52:0x004f, B:53:0x0061), top: B:66:0x0019 }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00a8 A[Catch: JSONException -> 0x0175, TryCatch #0 {JSONException -> 0x0175, blocks: (B:54:0x0066, B:56:0x00a8, B:57:0x00b2, B:59:0x00da, B:60:0x00e3, B:48:0x002b, B:51:0x004d, B:50:0x0049, B:49:0x0030, B:52:0x004f, B:53:0x0061), top: B:66:0x0019 }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00da A[Catch: JSONException -> 0x0175, TryCatch #0 {JSONException -> 0x0175, blocks: (B:54:0x0066, B:56:0x00a8, B:57:0x00b2, B:59:0x00da, B:60:0x00e3, B:48:0x002b, B:51:0x004d, B:50:0x0049, B:49:0x0030, B:52:0x004f, B:53:0x0061), top: B:66:0x0019 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String getDetectData(int r9, int r10, java.lang.String r11) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 398
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.getDetectData(int, int, java.lang.String):java.lang.String");
    }

    public static void c(String str) {
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.w("UniSDK Base", "detect jsonStr is empty");
            return;
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.SDK_DETECT_URL);
        if (TextUtils.isEmpty(propStr)) {
            propStr = SdkMgr.getInst().getPropInt("EB", -1) == 1 ? "https://data-detect.nie.easebar.com/client/mobile_upload" : "https://data-detect.nie.netease.com/client/mobile_upload";
        }
        if (TextUtils.isEmpty(propStr)) {
            UniSdkUtils.i("UniSDK Base", "null or empty url, detect will not go on");
            return;
        }
        HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
        queueItemNewQueueItem.method = "POST";
        queueItemNewQueueItem.url = propStr;
        queueItemNewQueueItem.bSync = Boolean.TRUE;
        queueItemNewQueueItem.setBody(str);
        queueItemNewQueueItem.transParam = "DETECT";
        HTTPQueue.getInstance("LOG").checkResend();
        HTTPQueue.getInstance("LOG").addItem(queueItemNewQueueItem);
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$115 */
    final class AnonymousClass115 implements HTTPCallback {

        /* renamed from: a */
        final /* synthetic */ SharedPreferences f1651a;

        AnonymousClass115(SharedPreferences sharedPreferences) {
            sharedPreferences = sharedPreferences;
        }

        @Override // com.netease.ntunisdk.base.utils.HTTPCallback
        public final boolean processResult(String str, String str2) {
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            try {
                ((Activity) SdkBase.this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.115.1
                    AnonymousClass1() {
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            sharedPreferences.edit().putLong("SDC_DEVINFO_UPLOAD_TIME", System.currentTimeMillis()).apply();
                        } catch (Exception unused) {
                        }
                    }
                });
            } catch (Exception unused) {
            }
            return false;
        }

        /* renamed from: com.netease.ntunisdk.base.SdkBase$115$1 */
        final class AnonymousClass1 implements Runnable {
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                try {
                    sharedPreferences.edit().putLong("SDC_DEVINFO_UPLOAD_TIME", System.currentTimeMillis()).apply();
                } catch (Exception unused) {
                }
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setUserInfo(String str, String str2) throws JSONException {
        setPropStr(str, str2);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getUserInfo(String str) {
        return getPropStr(str);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntUpLoadUserInfo() throws Throwable {
        UniSdkUtils.d("UniSDK Base", "ntUpLoadUserInfo");
        if (this.myCtx == null) {
            return;
        }
        if (useNewSdkProcedure()) {
            String propStr = getPropStr(ConstProp.USERINFO_STAGE, "");
            if (ConstProp.USERINFO_STAGE_ENTER_SERVER.equalsIgnoreCase(propStr)) {
                ntGameLoginSuccess();
                dispatchDrpf(g.b(this.myCtx), "LoginRole");
                g.a(this.myCtx, this);
            } else if (ConstProp.USERINFO_STAGE_CREATE_ROLE.equalsIgnoreCase(propStr)) {
                dispatchDrpf(g.a(this.myCtx), "CreateRole");
            }
        }
        if (d("ntUpLoadUserInfo")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.116
            AnonymousClass116() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).upLoadUserInfo();
                }
                SdkBase.this.upLoadUserInfo();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$116 */
    final class AnonymousClass116 implements Runnable {
        AnonymousClass116() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).upLoadUserInfo();
            }
            SdkBase.this.upLoadUserInfo();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntAntiAddiction(String str) {
        if (d("ntAntiAddiction")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.117

            /* renamed from: a */
            final /* synthetic */ String f1654a;

            AnonymousClass117(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.antiAddiction(str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$117 */
    final class AnonymousClass117 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1654a;

        AnonymousClass117(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.antiAddiction(str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntDoSdkRealNameRegister() {
        if (d("ntAntiAddiction")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.118
            AnonymousClass118() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.doSdkRealNameRegister();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$118 */
    final class AnonymousClass118 implements Runnable {
        AnonymousClass118() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.doSdkRealNameRegister();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$119 */
    final class AnonymousClass119 implements Runnable {
        AnonymousClass119() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (SdkBase.this.hasGuestLogined()) {
                SdkBase.this.sdkInstMap.get("ngguest").guestBind();
            } else {
                SdkBase.this.guestBind();
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntGuestBind() {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.119
            AnonymousClass119() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                if (SdkBase.this.hasGuestLogined()) {
                    SdkBase.this.sdkInstMap.get("ngguest").guestBind();
                } else {
                    SdkBase.this.guestBind();
                }
            }
        });
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setQueryFriendListener(QueryFriendListener queryFriendListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setQueryFriendListener(queryFriendListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setQueryFriendListener(queryFriendListener, i);
        }
        this.h = queryFriendListener;
        setPropInt(ConstProp.FRIEND_CALLER_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntApplyFriend(String str) {
        if (d("ntApplyFriend")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.120

            /* renamed from: a */
            final /* synthetic */ String f1656a;

            AnonymousClass120(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.applyFriend(str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$120 */
    final class AnonymousClass120 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1656a;

        AnonymousClass120(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.applyFriend(str);
        }
    }

    public void applyFriendFinished(boolean z) {
        if (this.h == null) {
            UniSdkUtils.e("UniSDK Base", "QueryFriendListener not set");
        } else if (getPropInt(ConstProp.FRIEND_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.121

                /* renamed from: a */
                final /* synthetic */ boolean f1657a;

                AnonymousClass121(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQueryFriendListFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onApplyFriendFinished(z);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.122

                /* renamed from: a */
                final /* synthetic */ boolean f1658a;

                AnonymousClass122(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQueryFriendListFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onApplyFriendFinished(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$121 */
    final class AnonymousClass121 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1657a;

        AnonymousClass121(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQueryFriendListFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onApplyFriendFinished(z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$122 */
    final class AnonymousClass122 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1658a;

        AnonymousClass122(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQueryFriendListFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onApplyFriendFinished(z);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntQueryFriendList() {
        if (d("ntQueryFriendList")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.123
            AnonymousClass123() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.queryFriendList();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$123 */
    final class AnonymousClass123 implements Runnable {
        AnonymousClass123() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.queryFriendList();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntQueryFriendListInGame() {
        if (d("ntQueryFriendListInGame")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.125
            AnonymousClass125() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.queryFriendListInGame();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$125 */
    final class AnonymousClass125 implements Runnable {
        AnonymousClass125() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.queryFriendListInGame();
        }
    }

    public void queryFriendListInGameFinished(List<AccountInfo> list) {
        if (this.h == null) {
            UniSdkUtils.e("UniSDK Base", "QueryFriendListener not set");
        } else if (getPropInt(ConstProp.FRIEND_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.126

                /* renamed from: a */
                final /* synthetic */ List f1660a;

                AnonymousClass126(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQueryFriendListInGameFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onQueryFriendListInGameFinished(list);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.127

                /* renamed from: a */
                final /* synthetic */ List f1661a;

                AnonymousClass127(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQueryFriendListInGameFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onQueryFriendListInGameFinished(list);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$126 */
    final class AnonymousClass126 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ List f1660a;

        AnonymousClass126(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQueryFriendListInGameFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onQueryFriendListInGameFinished(list);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$127 */
    final class AnonymousClass127 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ List f1661a;

        AnonymousClass127(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQueryFriendListInGameFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onQueryFriendListInGameFinished(list);
        }
    }

    public void queryFriendListFinished(List<AccountInfo> list) {
        if (this.h == null) {
            UniSdkUtils.e("UniSDK Base", "QueryFriendListener not set");
        } else if (getPropInt(ConstProp.FRIEND_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.128

                /* renamed from: a */
                final /* synthetic */ List f1662a;

                AnonymousClass128(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQueryFriendListFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onQueryFriendListFinished(list);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.129

                /* renamed from: a */
                final /* synthetic */ List f1663a;

                AnonymousClass129(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQueryFriendListFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onQueryFriendListFinished(list);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$128 */
    final class AnonymousClass128 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ List f1662a;

        AnonymousClass128(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQueryFriendListFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onQueryFriendListFinished(list);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$129 */
    final class AnonymousClass129 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ List f1663a;

        AnonymousClass129(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQueryFriendListFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onQueryFriendListFinished(list);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntQueryAvailablesInvitees() {
        if (d("ntQueryAvailablesInvitees")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.130
            AnonymousClass130() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.queryAvailablesInvitees();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$130 */
    final class AnonymousClass130 implements Runnable {
        AnonymousClass130() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.queryAvailablesInvitees();
        }
    }

    public void queryAvailablesInviteesFinished(List<AccountInfo> list) {
        if (this.h == null) {
            UniSdkUtils.e("UniSDK Base", "QueryFriendListener not set");
        } else if (getPropInt(ConstProp.FRIEND_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.131

                /* renamed from: a */
                final /* synthetic */ List f1665a;

                AnonymousClass131(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQueryAvailablesInviteesFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onQueryAvailablesInviteesFinished(list);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.132

                /* renamed from: a */
                final /* synthetic */ List f1666a;

                AnonymousClass132(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQueryAvailablesInviteesFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onQueryAvailablesInviteesFinished(list);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$131 */
    final class AnonymousClass131 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ List f1665a;

        AnonymousClass131(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQueryAvailablesInviteesFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onQueryAvailablesInviteesFinished(list);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$132 */
    final class AnonymousClass132 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ List f1666a;

        AnonymousClass132(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQueryAvailablesInviteesFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onQueryAvailablesInviteesFinished(list);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntQueryMyAccount() {
        if (d("ntQueryMyAccount")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.133
            AnonymousClass133() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.queryMyAccount();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$133 */
    final class AnonymousClass133 implements Runnable {
        AnonymousClass133() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.queryMyAccount();
        }
    }

    public void queryMyAccountFinished(AccountInfo accountInfo) {
        if (this.h == null) {
            UniSdkUtils.e("UniSDK Base", "QueryFriendListener not set");
        } else if (getPropInt(ConstProp.FRIEND_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.134
                final /* synthetic */ AccountInfo b;

                AnonymousClass134(AccountInfo accountInfo2) {
                    accountInfo = accountInfo2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQueryMyAccountFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onQueryMyAccountFinished(accountInfo);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.136
                final /* synthetic */ AccountInfo b;

                AnonymousClass136(AccountInfo accountInfo2) {
                    accountInfo = accountInfo2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQueryMyAccountFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onQueryMyAccountFinished(accountInfo);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$134 */
    final class AnonymousClass134 implements Runnable {
        final /* synthetic */ AccountInfo b;

        AnonymousClass134(AccountInfo accountInfo2) {
            accountInfo = accountInfo2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQueryMyAccountFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onQueryMyAccountFinished(accountInfo);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$136 */
    final class AnonymousClass136 implements Runnable {
        final /* synthetic */ AccountInfo b;

        AnonymousClass136(AccountInfo accountInfo2) {
            accountInfo = accountInfo2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQueryMyAccountFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onQueryMyAccountFinished(accountInfo);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setQueryRankListener(QueryRankListener queryRankListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setQueryRankListener(queryRankListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setQueryRankListener(queryRankListener, i);
        }
        this.i = queryRankListener;
        setPropInt(ConstProp.RANK_CALLER_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntQueryRank(QueryRankInfo queryRankInfo) {
        if (d("ntQueryRank")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.137

            /* renamed from: a */
            final /* synthetic */ QueryRankInfo f1669a;

            AnonymousClass137(QueryRankInfo queryRankInfo2) {
                queryRankInfo = queryRankInfo2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).queryRank(queryRankInfo);
                }
                SdkBase.this.queryRank(queryRankInfo);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$137 */
    final class AnonymousClass137 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ QueryRankInfo f1669a;

        AnonymousClass137(QueryRankInfo queryRankInfo2) {
            queryRankInfo = queryRankInfo2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).queryRank(queryRankInfo);
            }
            SdkBase.this.queryRank(queryRankInfo);
        }
    }

    public void queryRankFinished(List<AccountInfo> list) {
        if (this.i == null) {
            UniSdkUtils.e("UniSDK Base", "QueryRankListener not set");
        } else if (getPropInt(ConstProp.RANK_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.138
                final /* synthetic */ List b;

                AnonymousClass138(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "queryRankFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.i.onQueryRankFinished(list);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.139
                final /* synthetic */ List b;

                AnonymousClass139(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "queryRankFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.i.onQueryRankFinished(list);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$138 */
    final class AnonymousClass138 implements Runnable {
        final /* synthetic */ List b;

        AnonymousClass138(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "queryRankFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.i.onQueryRankFinished(list);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$139 */
    final class AnonymousClass139 implements Runnable {
        final /* synthetic */ List b;

        AnonymousClass139(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "queryRankFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.i.onQueryRankFinished(list);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntUpdateRank(String str, double d) {
        if (d("ntUpdateRank")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.140

            /* renamed from: a */
            final /* synthetic */ String f1672a;
            final /* synthetic */ double c;

            AnonymousClass140(String str2, double d2) {
                str = str2;
                d = d2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).updateRank(str, d);
                }
                SdkBase.this.updateRank(str, d);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$140 */
    final class AnonymousClass140 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1672a;
        final /* synthetic */ double c;

        AnonymousClass140(String str2, double d2) {
            str = str2;
            d = d2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).updateRank(str, d);
            }
            SdkBase.this.updateRank(str, d);
        }
    }

    public void updateRankFinished(boolean z) {
        if (this.i == null) {
            UniSdkUtils.e("UniSDK Base", "QueryRankListener not set");
        } else if (getPropInt(ConstProp.RANK_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.141

                /* renamed from: a */
                final /* synthetic */ boolean f1673a;

                AnonymousClass141(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "updateRankFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.i.onUpdateRankFinished(z);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.142

                /* renamed from: a */
                final /* synthetic */ boolean f1674a;

                AnonymousClass142(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "updateRankFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.i.onUpdateRankFinished(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$141 */
    final class AnonymousClass141 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1673a;

        AnonymousClass141(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "updateRankFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.i.onUpdateRankFinished(z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$142 */
    final class AnonymousClass142 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1674a;

        AnonymousClass142(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "updateRankFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.i.onUpdateRankFinished(z);
        }
    }

    public void updateAchievementFinished(boolean z) {
        if (this.i == null) {
            UniSdkUtils.e("UniSDK Base", "QueryRankListener not set");
        } else if (getPropInt(ConstProp.RANK_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.143

                /* renamed from: a */
                final /* synthetic */ boolean f1675a;

                AnonymousClass143(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "updateAchievementFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.i.onUpdateAchievement(z);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.144

                /* renamed from: a */
                final /* synthetic */ boolean f1676a;

                AnonymousClass144(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "updateAchievementFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.i.onUpdateAchievement(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$143 */
    final class AnonymousClass143 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1675a;

        AnonymousClass143(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "updateAchievementFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.i.onUpdateAchievement(z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$144 */
    final class AnonymousClass144 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1676a;

        AnonymousClass144(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "updateAchievementFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.i.onUpdateAchievement(z);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setQuestListener(OnQuestListener onQuestListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setQuestListener(onQuestListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setQuestListener(onQuestListener, i);
        }
        this.j = onQuestListener;
        setPropInt(ConstProp.QUEST_CALLER_THREAD, i);
    }

    public void onQuestCompleted(String str) {
        if (this.j == null) {
            UniSdkUtils.e("UniSDK Base", "OnQuestListener not set");
        } else if (getPropInt(ConstProp.QUEST_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.145

                /* renamed from: a */
                final /* synthetic */ String f1677a;

                AnonymousClass145(String str2) {
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQuestCompleted, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.j.onQuestCompleted(str);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.147

                /* renamed from: a */
                final /* synthetic */ String f1679a;

                AnonymousClass147(String str2) {
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onQuestCompleted, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.j.onQuestCompleted(str);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$145 */
    final class AnonymousClass145 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1677a;

        AnonymousClass145(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQuestCompleted, current thread=" + Thread.currentThread().getId());
            SdkBase.this.j.onQuestCompleted(str);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$147 */
    final class AnonymousClass147 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1679a;

        AnonymousClass147(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onQuestCompleted, current thread=" + Thread.currentThread().getId());
            SdkBase.this.j.onQuestCompleted(str);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$148 */
    final class AnonymousClass148 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ ShareInfo f1680a;

        AnonymousClass148(ShareInfo shareInfo) {
            shareInfo = shareInfo;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            String strSpecialShareChannel = "";
            while (it.hasNext()) {
                strSpecialShareChannel = SdkBase.this.sdkInstMap.get(it.next()).specialShareChannel(shareInfo);
                if (!TextUtils.isEmpty(strSpecialShareChannel)) {
                    break;
                }
            }
            if (TextUtils.isEmpty(strSpecialShareChannel)) {
                Iterator<SdkBase> it2 = SdkBase.this.loginSdkInstMap.values().iterator();
                while (it2.hasNext()) {
                    strSpecialShareChannel = it2.next().specialShareChannel(shareInfo);
                    if (!TextUtils.isEmpty(strSpecialShareChannel)) {
                        break;
                    }
                }
            }
            if (!TextUtils.isEmpty(strSpecialShareChannel)) {
                UniSdkUtils.i("UniSDK Base", "shareChannel no null");
                if (SdkBase.this.sdkInstMap.containsKey(strSpecialShareChannel)) {
                    UniSdkUtils.i("UniSDK Base", "sdkInstMap.get(shareChannel).share(shareInfo)");
                    SdkBase.this.sdkInstMap.get(strSpecialShareChannel).share(shareInfo);
                    return;
                } else {
                    if (SdkBase.this.loginSdkInstMap.containsKey(strSpecialShareChannel)) {
                        UniSdkUtils.i("UniSDK Base", "loginSdkInstMap.get(shareChannel).share(shareInfo)");
                        SdkBase.this.loginSdkInstMap.get(strSpecialShareChannel).share(shareInfo);
                        return;
                    }
                    return;
                }
            }
            UniSdkUtils.i("UniSDK Base", "shareChannel null");
            int shareChannel = shareInfo.getShareChannel();
            UniSdkUtils.d("UniSDK Base", "ntShare platform:".concat(String.valueOf(shareChannel)));
            if (!SdkBase.this.sdkInstMap.isEmpty() && SdkBase.this.sdkInstMap.containsKey("ngshare") && (100 == shareChannel || 117 == shareChannel || 105 == shareChannel || 106 == shareChannel || 101 == shareChannel || 102 == shareChannel || 118 == shareChannel || 103 == shareChannel || 104 == shareChannel)) {
                UniSdkUtils.d("UniSDK Base", "call ngshare");
                SdkBase.this.sdkInstMap.get("ngshare").share(shareInfo);
            } else if (!SdkBase.this.sdkInstMap.isEmpty() && SdkBase.this.sdkInstMap.containsKey("ngshare_compat")) {
                UniSdkUtils.d("UniSDK Base", "call ngshare_compat");
                SdkBase.this.sdkInstMap.get("ngshare_compat").share(shareInfo);
            } else {
                SdkBase.this.share(shareInfo);
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntShare(ShareInfo shareInfo) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.148

            /* renamed from: a */
            final /* synthetic */ ShareInfo f1680a;

            AnonymousClass148(ShareInfo shareInfo2) {
                shareInfo = shareInfo2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                String strSpecialShareChannel = "";
                while (it.hasNext()) {
                    strSpecialShareChannel = SdkBase.this.sdkInstMap.get(it.next()).specialShareChannel(shareInfo);
                    if (!TextUtils.isEmpty(strSpecialShareChannel)) {
                        break;
                    }
                }
                if (TextUtils.isEmpty(strSpecialShareChannel)) {
                    Iterator<SdkBase> it2 = SdkBase.this.loginSdkInstMap.values().iterator();
                    while (it2.hasNext()) {
                        strSpecialShareChannel = it2.next().specialShareChannel(shareInfo);
                        if (!TextUtils.isEmpty(strSpecialShareChannel)) {
                            break;
                        }
                    }
                }
                if (!TextUtils.isEmpty(strSpecialShareChannel)) {
                    UniSdkUtils.i("UniSDK Base", "shareChannel no null");
                    if (SdkBase.this.sdkInstMap.containsKey(strSpecialShareChannel)) {
                        UniSdkUtils.i("UniSDK Base", "sdkInstMap.get(shareChannel).share(shareInfo)");
                        SdkBase.this.sdkInstMap.get(strSpecialShareChannel).share(shareInfo);
                        return;
                    } else {
                        if (SdkBase.this.loginSdkInstMap.containsKey(strSpecialShareChannel)) {
                            UniSdkUtils.i("UniSDK Base", "loginSdkInstMap.get(shareChannel).share(shareInfo)");
                            SdkBase.this.loginSdkInstMap.get(strSpecialShareChannel).share(shareInfo);
                            return;
                        }
                        return;
                    }
                }
                UniSdkUtils.i("UniSDK Base", "shareChannel null");
                int shareChannel = shareInfo.getShareChannel();
                UniSdkUtils.d("UniSDK Base", "ntShare platform:".concat(String.valueOf(shareChannel)));
                if (!SdkBase.this.sdkInstMap.isEmpty() && SdkBase.this.sdkInstMap.containsKey("ngshare") && (100 == shareChannel || 117 == shareChannel || 105 == shareChannel || 106 == shareChannel || 101 == shareChannel || 102 == shareChannel || 118 == shareChannel || 103 == shareChannel || 104 == shareChannel)) {
                    UniSdkUtils.d("UniSDK Base", "call ngshare");
                    SdkBase.this.sdkInstMap.get("ngshare").share(shareInfo);
                } else if (!SdkBase.this.sdkInstMap.isEmpty() && SdkBase.this.sdkInstMap.containsKey("ngshare_compat")) {
                    UniSdkUtils.d("UniSDK Base", "call ngshare_compat");
                    SdkBase.this.sdkInstMap.get("ngshare_compat").share(shareInfo);
                } else {
                    SdkBase.this.share(shareInfo);
                }
            }
        });
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public boolean ntCheckArgs(ShareInfo shareInfo) {
        UniSdkUtils.d("UniSDK Base", "call ntCheckArgs");
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            if (!this.sdkInstMap.get(it.next()).checkArgs(shareInfo)) {
                return false;
            }
        }
        return checkArgs(shareInfo);
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$149 */
    final class AnonymousClass149 implements Runnable {
        AnonymousClass149() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.openGmView();
        }
    }

    public void ntOpenGmView() {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.149
            AnonymousClass149() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.openGmView();
            }
        });
    }

    public boolean isValidOrderId(String str) {
        return str.length() <= 64;
    }

    public void setBackSauthLoginJson(String str) throws JSONException {
        Object obj;
        int i;
        UniSdkUtils.d("UniSDK Base", "loginJsonB64 = ".concat(String.valueOf(str)));
        try {
            try {
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                String str2 = new String(Base64.decode(str, 0), "UTF-8");
                if (TextUtils.isEmpty(str2)) {
                    return;
                }
                JSONObject jSONObject = new JSONObject(str2);
                String strOptString = jSONObject.optString("aid");
                String strOptString2 = jSONObject.optString("sdkuid");
                String strOptString3 = jSONObject.optString("gas_token", jSONObject.optString("sdk_token"));
                JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("realname_msg");
                String strOptString4 = jSONObject.optString("username");
                if (TextUtils.isEmpty(strOptString4)) {
                    obj = "sdk_token";
                } else {
                    obj = "sdk_token";
                    SdkMgr.getInst().setPropStr(ConstProp.FULL_UID, strOptString4);
                }
                UniSdkUtils.d("UniSDK Base", "aid = " + strOptString + ", uid = " + strOptString2);
                if (!TextUtils.isEmpty(strOptString)) {
                    SdkMgr.getInst().setPropStr(ConstProp.USERINFO_AID, strOptString);
                }
                if (!TextUtils.isEmpty(strOptString2)) {
                    SdkMgr.getInst().setPropStr(ConstProp.UID, strOptString2);
                }
                if (!TextUtils.isEmpty(strOptString3)) {
                    SdkMgr.getInst().setPropStr(ConstProp.UNISDK_JF_GAS_TOKEN, strOptString3);
                }
                String strOptString5 = jSONObject.optString(Constants.PARAM_ACCESS_TOKEN);
                String strOptString6 = jSONObject.optString(Constants.PARAM_EXPIRES_IN);
                String strOptString7 = jSONObject.optString("refresh_token");
                if (!TextUtils.isEmpty(strOptString5) && !TextUtils.equals(strOptString5, SdkMgr.getInst().getPropStr(ConstProp.SESSION))) {
                    SdkMgr.getInst().setPropStr(ConstProp.SESSION, strOptString5);
                }
                if (!TextUtils.isEmpty(strOptString6) && !TextUtils.equals(strOptString6, SdkMgr.getInst().getPropStr(ConstProp.TIMESTAMP))) {
                    SdkMgr.getInst().setPropStr(ConstProp.TIMESTAMP, strOptString6);
                }
                if (!TextUtils.isEmpty(strOptString7) && !TextUtils.equals(strOptString7, SdkMgr.getInst().getPropStr(ConstProp.REFRESH_TOKEN))) {
                    SdkMgr.getInst().setPropStr(ConstProp.REFRESH_TOKEN, strOptString7);
                }
                String strOptString8 = jSONObject.optString("aas_version");
                if (!TextUtils.isEmpty(strOptString8)) {
                    SdkMgr.getInst().setPropStr(ConstProp.JF_LOGIN_AAS_VERSION, strOptString8);
                }
                if (jSONObjectOptJSONObject != null) {
                    UniSdkUtils.d("UniSDK Base", "realnameMsg = " + jSONObjectOptJSONObject.toString());
                    String strOptString9 = jSONObjectOptJSONObject.optString("realname_status");
                    String strOptString10 = jSONObjectOptJSONObject.optString("is_adult");
                    if ("1".equals(strOptString9)) {
                        if ("0".equals(strOptString10)) {
                            i = 1;
                        } else {
                            i = "1".equals(strOptString10) ? 2 : 3;
                        }
                    } else {
                        i = "0".equals(strOptString9) ? 0 : -99;
                    }
                    if (i >= 0) {
                        SdkMgr.getInst().setPropInt(ConstProp.REAL_NAME_VERIFIED, i);
                        JSONObject jSONObject2 = new JSONObject();
                        try {
                            jSONObject2.put("methodId", "getRealnameStatus");
                            jSONObject2.put("status", i);
                            extendFuncCall(jSONObject2.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                com.netease.ntunisdk.base.function.a.a(this, jSONObject);
                String strOptString11 = jSONObject.optString("region");
                if (!TextUtils.isEmpty(strOptString11)) {
                    SdkMgr.getInst().setPropStr(ConstProp.FIRST_LOGIN_REGION, strOptString11);
                }
                JSONObject jSONObjectOptJSONObject2 = jSONObject.optJSONObject("oauth2");
                if (jSONObjectOptJSONObject2 != null) {
                    String strOptString12 = jSONObjectOptJSONObject2.optString(Constants.PARAM_ACCESS_TOKEN);
                    String strOptString13 = jSONObjectOptJSONObject2.optString("refresh_token");
                    UniSdkUtils.d("UniSDK Base", "oauthAccessToken = ".concat(String.valueOf(strOptString12)));
                    UniSdkUtils.d("UniSDK Base", "oauthRefreshToken = ".concat(String.valueOf(strOptString13)));
                    if (TextUtils.isEmpty(strOptString12)) {
                        strOptString3 = "";
                        strOptString13 = "";
                    } else if (!strOptString12.equals("gas_token") && !strOptString12.equals(obj)) {
                        strOptString3 = strOptString12;
                    }
                    UniSdkUtils.d("UniSDK Base", "access_token = ".concat(String.valueOf(strOptString3)));
                    UniSdkUtils.d("UniSDK Base", "refresh_token = ".concat(String.valueOf(strOptString13)));
                    if (!TextUtils.isEmpty(strOptString3) && !TextUtils.isEmpty(strOptString13)) {
                        SdkMgr.getInst().setPropStr(ConstProp.UNISDK_JF_ACCESS_TOKEN, strOptString3);
                        SdkMgr.getInst().setPropStr(ConstProp.UNISDK_JF_REFRESH_TOKEN, strOptString13);
                        ApiRequestUtil.getExpiration();
                    }
                }
                if (jSONObject.optBoolean("is_foreign_user")) {
                    SdkMgr.getInst().setPropStr(ConstProp.IS_FOREIGN_USER, "1");
                } else {
                    SdkMgr.getInst().setPropStr(ConstProp.IS_FOREIGN_USER, "0");
                }
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } catch (JSONException e4) {
            e4.printStackTrace();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntGameLoginSuccess() {
        Context context = this.myCtx;
        if (context == null) {
            UniSdkUtils.d("UniSDK Base", "call ntGameLoginSuccess, myCtx null");
        } else {
            ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.150
                AnonymousClass150() {
                }

                @Override // java.lang.Runnable
                public final void run() throws Throwable {
                    SdkBase.v(SdkBase.this);
                    SdkBase.u(SdkBase.this);
                    if ((SdkBase.this.hasFeature(ConstProp.UNI_SAUTH_FALLBACK) || SdkBase.this.hasFeature(ConstProp.OVERSEA_PROJECT)) ? false : true) {
                        SdkBase.this.f();
                        return;
                    }
                    SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
                    boolean zHasFeature = sdkBase.hasFeature(ConstProp.SHOW_PROTOCOL_IN_LOGIN);
                    UniSdkUtils.i("LoginProtocol", "gameLoginSuccess protocol: ".concat(String.valueOf(zHasFeature)));
                    if (zHasFeature) {
                        try {
                            JSONObject jSONObject = new JSONObject();
                            jSONObject.putOpt("methodId", "showProtocolInLogin");
                            jSONObject.putOpt(VideoViewManager.PROP_SRC, "gameLoginSuccess");
                            jSONObject.putOpt("uid", sdkBase.getPropStr(ConstProp.UID, ""));
                            sdkBase.setPropStr(ConstProp.PROTOCOL_IN_LOGIN_SRC, "gameLoginSuccess");
                            SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
                            return;
                        } catch (Exception unused) {
                            return;
                        }
                    }
                    com.netease.ntunisdk.base.function.a.a();
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$150 */
    final class AnonymousClass150 implements Runnable {
        AnonymousClass150() {
        }

        @Override // java.lang.Runnable
        public final void run() throws Throwable {
            SdkBase.v(SdkBase.this);
            SdkBase.u(SdkBase.this);
            if ((SdkBase.this.hasFeature(ConstProp.UNI_SAUTH_FALLBACK) || SdkBase.this.hasFeature(ConstProp.OVERSEA_PROJECT)) ? false : true) {
                SdkBase.this.f();
                return;
            }
            SdkBase sdkBase = (SdkBase) SdkMgr.getInst();
            boolean zHasFeature = sdkBase.hasFeature(ConstProp.SHOW_PROTOCOL_IN_LOGIN);
            UniSdkUtils.i("LoginProtocol", "gameLoginSuccess protocol: ".concat(String.valueOf(zHasFeature)));
            if (zHasFeature) {
                try {
                    JSONObject jSONObject = new JSONObject();
                    jSONObject.putOpt("methodId", "showProtocolInLogin");
                    jSONObject.putOpt(VideoViewManager.PROP_SRC, "gameLoginSuccess");
                    jSONObject.putOpt("uid", sdkBase.getPropStr(ConstProp.UID, ""));
                    sdkBase.setPropStr(ConstProp.PROTOCOL_IN_LOGIN_SRC, "gameLoginSuccess");
                    SdkMgr.getInst().ntExtendFunc(jSONObject.toString());
                    return;
                } catch (Exception unused) {
                    return;
                }
            }
            com.netease.ntunisdk.base.function.a.a();
        }
    }

    public void f() throws JSONException {
        c(getDetectData(38, 0, ""));
        try {
            Context context = this.myCtx;
            if (context != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences("SDC", 0);
                long j = sharedPreferences.getLong("SDC_DEVINFO_UPLOAD_TIME", -1L);
                long jCurrentTimeMillis = System.currentTimeMillis() - j;
                if (j == -1 || jCurrentTimeMillis <= 0 || jCurrentTimeMillis >= 86400000) {
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put("project", "unisdk");
                        jSONObject.put(SocialConstants.PARAM_SOURCE, "unisdk");
                        jSONObject.put("type", "device_info");
                        jSONObject.put("device_model", UniSdkUtils.getMobileModel());
                        jSONObject.put("udid", SdkMgr.getInst().getUdid());
                        jSONObject.put("imei", UniSdkUtils.getMobileIMEI(this.myCtx));
                        jSONObject.put("idfa", UniSdkUtils.getMobileIMSI(this.myCtx));
                        jSONObject.put("game_project", SdkMgr.getInst().getPropStr("JF_GAMEID"));
                        jSONObject.put("os_name", "android");
                        jSONObject.put("ntesid", SdkMgr.getInst().getPropStr("ntes_id"));
                        jSONObject.put(com.netease.ntunisdk.external.protocol.Const.APP_CHANNEL, SdkMgr.getInst().getAppChannel());
                        jSONObject.put(Constants.PARAM_SDK_VER, getSDKVersion(getChannel()));
                        jSONObject.put("common_sdk_ver", SdkMgr.getBaseVersion());
                        jSONObject.putOpt("unisdk_deviceid", SdkMgr.getInst().getPropStr(ConstProp.UNISDK_DEVICE_ID));
                        String oaid = UniSdkUtils.getOAID(this.myCtx);
                        if (!TextUtils.isEmpty(oaid)) {
                            jSONObject.putOpt(OneTrack.Param.OAID, oaid);
                        }
                        String propStr = SdkMgr.getInst().getPropStr(ConstProp.MSA_OAID);
                        if (!TextUtils.isEmpty(propStr)) {
                            jSONObject.putOpt("msa_oaid", propStr);
                        }
                        String propStr2 = SdkMgr.getInst().getPropStr("VAID");
                        if (!TextUtils.isEmpty(propStr2)) {
                            jSONObject.putOpt("vaid", propStr2);
                        }
                        String propStr3 = SdkMgr.getInst().getPropStr("AAID");
                        if (!TextUtils.isEmpty(propStr3)) {
                            jSONObject.putOpt("aaid", propStr3);
                        }
                    } catch (JSONException e) {
                        UniSdkUtils.w("UniSDK Base", "device_info parse error");
                        e.printStackTrace();
                    }
                    String propStr4 = SdkMgr.getInst().getPropStr(ConstProp.SDC_DEVICE_INFO_URL);
                    if (TextUtils.isEmpty(propStr4)) {
                        propStr4 = SdkMgr.getInst().getPropInt("EB", -1) == 1 ? "https://unisdk.proxima.nie.easebar.com" : "https://unisdk.proxima.nie.netease.com";
                    }
                    if (TextUtils.isEmpty(propStr4)) {
                        UniSdkUtils.i("UniSDK Base", "null or empty url, postDevInfo2SDC will not go on");
                    } else {
                        HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
                        queueItemNewQueueItem.method = "POST";
                        queueItemNewQueueItem.url = propStr4;
                        queueItemNewQueueItem.bSync = Boolean.TRUE;
                        queueItemNewQueueItem.setBody(jSONObject.toString());
                        queueItemNewQueueItem.transParam = "SDC";
                        queueItemNewQueueItem.callback = new HTTPCallback() { // from class: com.netease.ntunisdk.base.SdkBase.115

                            /* renamed from: a */
                            final /* synthetic */ SharedPreferences f1651a;

                            AnonymousClass115(SharedPreferences sharedPreferences2) {
                                sharedPreferences = sharedPreferences2;
                            }

                            @Override // com.netease.ntunisdk.base.utils.HTTPCallback
                            public final boolean processResult(String str, String str2) {
                                if (TextUtils.isEmpty(str)) {
                                    return false;
                                }
                                try {
                                    ((Activity) SdkBase.this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.115.1
                                        AnonymousClass1() {
                                        }

                                        @Override // java.lang.Runnable
                                        public final void run() {
                                            try {
                                                sharedPreferences.edit().putLong("SDC_DEVINFO_UPLOAD_TIME", System.currentTimeMillis()).apply();
                                            } catch (Exception unused) {
                                            }
                                        }
                                    });
                                } catch (Exception unused) {
                                }
                                return false;
                            }

                            /* renamed from: com.netease.ntunisdk.base.SdkBase$115$1 */
                            final class AnonymousClass1 implements Runnable {
                                AnonymousClass1() {
                                }

                                @Override // java.lang.Runnable
                                public final void run() {
                                    try {
                                        sharedPreferences.edit().putLong("SDC_DEVINFO_UPLOAD_TIME", System.currentTimeMillis()).apply();
                                    } catch (Exception unused) {
                                    }
                                }
                            }
                        };
                        HTTPQueue.getInstance("LOG").checkResend();
                        HTTPQueue.getInstance("LOG").addItem(queueItemNewQueueItem);
                    }
                }
            }
        } catch (Exception unused) {
        }
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            sdkBase.gameLoginSuccess();
            sdkBase.anonymousLogin();
        }
        gameLoginSuccess();
        anonymousLogin();
    }

    public PayChannelManager getPayChannelManager() {
        return this.K;
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$151 */
    final class AnonymousClass151 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ OrderInfo f1682a;

        AnonymousClass151(OrderInfo orderInfo) {
            orderInfo = orderInfo;
        }

        @Override // java.lang.Runnable
        public final void run() {
            String orderChannel = orderInfo.getOrderChannel();
            if (orderChannel.equals(SdkBase.this.getChannel())) {
                SdkBase.this.consume(orderInfo);
                return;
            }
            SdkBase sdkBase = SdkBase.this.sdkInstMap.get(orderChannel);
            if (sdkBase != null) {
                sdkBase.consume(orderInfo);
                return;
            }
            UniSdkUtils.w("UniSDK Base", "orderChannel SdkBase is null, use login channel: " + SdkBase.this.getChannel());
            SdkBase.this.consume(orderInfo);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntConsume(OrderInfo orderInfo) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.151

            /* renamed from: a */
            final /* synthetic */ OrderInfo f1682a;

            AnonymousClass151(OrderInfo orderInfo2) {
                orderInfo = orderInfo2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                String orderChannel = orderInfo.getOrderChannel();
                if (orderChannel.equals(SdkBase.this.getChannel())) {
                    SdkBase.this.consume(orderInfo);
                    return;
                }
                SdkBase sdkBase = SdkBase.this.sdkInstMap.get(orderChannel);
                if (sdkBase != null) {
                    sdkBase.consume(orderInfo);
                    return;
                }
                UniSdkUtils.w("UniSDK Base", "orderChannel SdkBase is null, use login channel: " + SdkBase.this.getChannel());
                SdkBase.this.consume(orderInfo);
            }
        });
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntShowDaren() {
        if (d("ntShowDaren")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.152
            AnonymousClass152() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.showDaren();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$152 */
    final class AnonymousClass152 implements Runnable {
        AnonymousClass152() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.showDaren();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntIsDarenUpdated() {
        Context context;
        if (d("ntIsDarenUpdated") || (context = this.myCtx) == null) {
            return;
        }
        ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.153
            AnonymousClass153() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.isDarenUpdated();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$153 */
    final class AnonymousClass153 implements Runnable {
        AnonymousClass153() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.isDarenUpdated();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public boolean ntHasNotification() {
        return hasNotification();
    }

    public void isDarenUpdatedFinished(boolean z) {
        if (this.h == null) {
            UniSdkUtils.e("UniSDK Base", "QueryFriendListener not set");
        } else if (getPropInt(ConstProp.FRIEND_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.154

                /* renamed from: a */
                final /* synthetic */ boolean f1683a;

                AnonymousClass154(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "isDarenUpdateFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onIsDarenUpdated(z);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.155

                /* renamed from: a */
                final /* synthetic */ boolean f1684a;

                AnonymousClass155(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "isDarenUpdateFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onIsDarenUpdated(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$154 */
    final class AnonymousClass154 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1683a;

        AnonymousClass154(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "isDarenUpdateFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onIsDarenUpdated(z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$155 */
    final class AnonymousClass155 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1684a;

        AnonymousClass155(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "isDarenUpdateFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onIsDarenUpdated(z);
        }
    }

    public void shareFinished(boolean z) {
        if (this.k == null) {
            UniSdkUtils.e("UniSDK Base", "shareListener not set");
        } else if (getPropInt(ConstProp.SHARE_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.156

                /* renamed from: a */
                final /* synthetic */ boolean f1685a;

                AnonymousClass156(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "shareFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.k.onShareFinished(z);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.158

                /* renamed from: a */
                final /* synthetic */ boolean f1688a;

                AnonymousClass158(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "shareFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.k.onShareFinished(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$156 */
    final class AnonymousClass156 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1685a;

        AnonymousClass156(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "shareFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.k.onShareFinished(z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$158 */
    final class AnonymousClass158 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1688a;

        AnonymousClass158(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "shareFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.k.onShareFinished(z);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setShareListener(OnShareListener onShareListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setShareListener(onShareListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setShareListener(onShareListener, i);
        }
        this.k = onShareListener;
        setPropInt(ConstProp.SHARE_CALLER_THREAD, i);
    }

    public OnShareListener getShareListener() {
        return this.k;
    }

    public void sendPushNotificationFinished(boolean z) {
        if (this.l == null) {
            UniSdkUtils.e("UniSDK Base", "pushListener not set");
        } else if (getPropInt(ConstProp.PUSH_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.159

                /* renamed from: a */
                final /* synthetic */ boolean f1689a;

                AnonymousClass159(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onSendPushNotificationFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.l.onSendPushNotificationFinished(z);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.160

                /* renamed from: a */
                final /* synthetic */ boolean f1691a;

                AnonymousClass160(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onSendPushNotificationFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.l.onSendPushNotificationFinished(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$159 */
    final class AnonymousClass159 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1689a;

        AnonymousClass159(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onSendPushNotificationFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.l.onSendPushNotificationFinished(z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$160 */
    final class AnonymousClass160 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1691a;

        AnonymousClass160(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onSendPushNotificationFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.l.onSendPushNotificationFinished(z);
        }
    }

    public void sendLocalNotificationFinished(int i) {
        if (this.l == null) {
            UniSdkUtils.e("UniSDK Base", "pushListener not set");
        } else if (getPropInt(ConstProp.PUSH_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.161

                /* renamed from: a */
                final /* synthetic */ int f1692a;

                AnonymousClass161(int i2) {
                    i = i2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onSendLocalNotificationFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.l.onSendLocalNotificationFinished(i);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.162

                /* renamed from: a */
                final /* synthetic */ int f1693a;

                AnonymousClass162(int i2) {
                    i = i2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onSendLocalNotificationFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.l.onSendLocalNotificationFinished(i);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$161 */
    final class AnonymousClass161 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1692a;

        AnonymousClass161(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onSendLocalNotificationFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.l.onSendLocalNotificationFinished(i);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$162 */
    final class AnonymousClass162 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1693a;

        AnonymousClass162(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onSendLocalNotificationFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.l.onSendLocalNotificationFinished(i);
        }
    }

    public void getUserPushFinished(boolean z) {
        if (this.l == null) {
            UniSdkUtils.e("UniSDK Base", "pushListener not set");
        } else if (getPropInt(ConstProp.PUSH_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.163

                /* renamed from: a */
                final /* synthetic */ boolean f1694a;

                AnonymousClass163(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onGetUserPushFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.l.onGetUserPushFinished(z);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.164
                final /* synthetic */ boolean b;

                AnonymousClass164(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onGetUserPushFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.l.onGetUserPushFinished(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$163 */
    final class AnonymousClass163 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1694a;

        AnonymousClass163(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onGetUserPushFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.l.onGetUserPushFinished(z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$164 */
    final class AnonymousClass164 implements Runnable {
        final /* synthetic */ boolean b;

        AnonymousClass164(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onGetUserPushFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.l.onGetUserPushFinished(z);
        }
    }

    public void cancelLocalPushFinished(boolean z) {
        if (this.l == null) {
            UniSdkUtils.e("UniSDK Base", "pushListener not set");
        } else if (getPropInt(ConstProp.PUSH_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.165
                final /* synthetic */ boolean b;

                AnonymousClass165(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onCancelLocalPushFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.l.onCancelLocalPushFinished(z);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.166

                /* renamed from: a */
                final /* synthetic */ boolean f1697a;

                AnonymousClass166(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onCancelLocalPushFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.l.onCancelLocalPushFinished(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$165 */
    final class AnonymousClass165 implements Runnable {
        final /* synthetic */ boolean b;

        AnonymousClass165(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onCancelLocalPushFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.l.onCancelLocalPushFinished(z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$166 */
    final class AnonymousClass166 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1697a;

        AnonymousClass166(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onCancelLocalPushFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.l.onCancelLocalPushFinished(z);
        }
    }

    public void connectToChannelFinished(int i) {
        if (this.o == null) {
            UniSdkUtils.e("UniSDK Base", "connectListener not set");
        } else if (getPropInt(ConstProp.PUSH_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.167

                /* renamed from: a */
                final /* synthetic */ int f1698a;

                AnonymousClass167(int i2) {
                    i = i2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.o.onConnectToChannelFinished(i);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.3

                /* renamed from: a */
                final /* synthetic */ int f1727a;

                AnonymousClass3(int i2) {
                    i = i2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.o.onConnectToChannelFinished(i);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$167 */
    final class AnonymousClass167 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1698a;

        AnonymousClass167(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.o.onConnectToChannelFinished(i);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$3 */
    final class AnonymousClass3 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1727a;

        AnonymousClass3(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.o.onConnectToChannelFinished(i);
        }
    }

    public void disConnectToChannelFinished(int i) {
        if (this.o == null) {
            UniSdkUtils.e("UniSDK Base", "connectListener not set");
        } else if (getPropInt(ConstProp.PUSH_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.4

                /* renamed from: a */
                final /* synthetic */ int f1736a;

                AnonymousClass4(int i2) {
                    i = i2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onDisConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.o.onDisConnectToChannelFinished(i);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.5

                /* renamed from: a */
                final /* synthetic */ int f1744a;

                AnonymousClass5(int i2) {
                    i = i2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onDisConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.o.onDisConnectToChannelFinished(i);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$4 */
    final class AnonymousClass4 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1736a;

        AnonymousClass4(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onDisConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.o.onDisConnectToChannelFinished(i);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$5 */
    final class AnonymousClass5 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1744a;

        AnonymousClass5(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onDisConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.o.onDisConnectToChannelFinished(i);
        }
    }

    public void selectChannelOptionFinished(boolean z) {
        if (this.o == null) {
            UniSdkUtils.e("UniSDK Base", "connectListener not set");
        } else if (getPropInt(ConstProp.PUSH_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.6

                /* renamed from: a */
                final /* synthetic */ boolean f1752a;

                AnonymousClass6(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onDisConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.o.onSelectChannelOptionFinished(z);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.7

                /* renamed from: a */
                final /* synthetic */ boolean f1762a;

                AnonymousClass7(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onDisConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.o.onSelectChannelOptionFinished(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$6 */
    final class AnonymousClass6 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1752a;

        AnonymousClass6(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onDisConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.o.onSelectChannelOptionFinished(z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$7 */
    final class AnonymousClass7 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1762a;

        AnonymousClass7(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onDisConnectToChannelFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.o.onSelectChannelOptionFinished(z);
        }
    }

    public void showViewFinished(String str) {
        if (this.n == null) {
            UniSdkUtils.e("UniSDK Base", "showViewListener not set");
        } else if (getPropInt(ConstProp.SHOW_VIEW_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.8

                /* renamed from: a */
                final /* synthetic */ String f1771a;

                AnonymousClass8(String str2) {
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "showViewFinished, current thread=" + Thread.currentThread().getId());
                    if ("onRewarded()".equals(str)) {
                        SdkBase.this.n.onRewarded();
                    }
                    if ("onOpened()".equals(str)) {
                        SdkBase.this.n.onOpened();
                    }
                    if ("onFailed()".equals(str)) {
                        SdkBase.this.n.onFailed();
                    }
                    if ("onClosed()".equals(str)) {
                        SdkBase.this.n.onClosed();
                    }
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.9

                /* renamed from: a */
                final /* synthetic */ String f1780a;

                AnonymousClass9(String str2) {
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "showViewListener, current thread=" + Thread.currentThread().getId());
                    if ("onRewarded()".equals(str)) {
                        SdkBase.this.n.onRewarded();
                    }
                    if ("onOpened()".equals(str)) {
                        SdkBase.this.n.onOpened();
                    }
                    if ("onFailed()".equals(str)) {
                        SdkBase.this.n.onFailed();
                    }
                    if ("onClosed()".equals(str)) {
                        SdkBase.this.n.onClosed();
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$8 */
    final class AnonymousClass8 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1771a;

        AnonymousClass8(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "showViewFinished, current thread=" + Thread.currentThread().getId());
            if ("onRewarded()".equals(str)) {
                SdkBase.this.n.onRewarded();
            }
            if ("onOpened()".equals(str)) {
                SdkBase.this.n.onOpened();
            }
            if ("onFailed()".equals(str)) {
                SdkBase.this.n.onFailed();
            }
            if ("onClosed()".equals(str)) {
                SdkBase.this.n.onClosed();
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$9 */
    final class AnonymousClass9 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1780a;

        AnonymousClass9(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "showViewListener, current thread=" + Thread.currentThread().getId());
            if ("onRewarded()".equals(str)) {
                SdkBase.this.n.onRewarded();
            }
            if ("onOpened()".equals(str)) {
                SdkBase.this.n.onOpened();
            }
            if ("onFailed()".equals(str)) {
                SdkBase.this.n.onFailed();
            }
            if ("onClosed()".equals(str)) {
                SdkBase.this.n.onClosed();
            }
        }
    }

    public void setUserPushFinished(boolean z) {
        if (this.l == null) {
            UniSdkUtils.e("UniSDK Base", "pushListener not set");
        } else if (getPropInt(ConstProp.SHOW_VIEW_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.10

                /* renamed from: a */
                final /* synthetic */ boolean f1635a;

                AnonymousClass10(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onSetUserPushFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.l.onSetUserPushFinished(z);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.11

                /* renamed from: a */
                final /* synthetic */ boolean f1645a;

                AnonymousClass11(boolean z2) {
                    z = z2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onSetUserPushFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.l.onSetUserPushFinished(z);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$10 */
    final class AnonymousClass10 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1635a;

        AnonymousClass10(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onSetUserPushFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.l.onSetUserPushFinished(z);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$11 */
    final class AnonymousClass11 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1645a;

        AnonymousClass11(boolean z2) {
            z = z2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onSetUserPushFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.l.onSetUserPushFinished(z);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setPushListener(OnPushListener onPushListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            sdkBase.setPushListener(onPushListener, i);
            sdkBase.setPropInt(ConstProp.PUSH_CALLER_THREAD, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setPushListener(onPushListener, i);
        }
        this.l = onPushListener;
        setPropInt(ConstProp.PUSH_CALLER_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setConnectListener(OnConnectListener onConnectListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            sdkBase.setConnectListener(onConnectListener, i);
            sdkBase.setPropInt(ConstProp.PUSH_CALLER_THREAD, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setConnectListener(onConnectListener, i);
        }
        this.o = onConnectListener;
        setPropInt(ConstProp.PUSH_CALLER_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setShowViewListener(OnShowViewListener onShowViewListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            sdkBase.setShowViewListener(onShowViewListener, i);
            sdkBase.setPropInt(ConstProp.SHOW_VIEW_THREAD, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setShowViewListener(onShowViewListener, i);
        }
        this.n = onShowViewListener;
        setPropInt(ConstProp.SHOW_VIEW_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnWindowFocusChanged(boolean z) {
        UniSdkUtils.d("UniSDK Base", "handleOnWindowFocusChanged...".concat(String.valueOf(z)));
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                sdkBase.sdkOnWindowFocusChanged(z);
            }
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
            if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                sdkBase2.sdkOnWindowFocusChanged(z);
            }
        }
        if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
            sdkOnWindowFocusChanged(z);
        }
        ModulesManager.getInst().onWindowFocusChanged(z);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnConfigurationChanged(Configuration configuration) {
        UniSdkUtils.d("UniSDK Base", "handleOnConfigurationChanged...");
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                sdkBase.sdkOnConfigurationChanged(configuration);
            }
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
            if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                sdkBase2.sdkOnConfigurationChanged(configuration);
            }
        }
        if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
            sdkOnConfigurationChanged(configuration);
        }
        WebViewProxy.getInstance().onConfigChange(configuration);
        ModulesManager.getInst().onConfigurationChanged(configuration);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnNewIntent(Intent intent) {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                sdkBase.sdkOnNewIntent(intent);
            }
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
            if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                sdkBase2.sdkOnNewIntent(intent);
            }
        }
        if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
            sdkOnNewIntent(intent);
        }
        ModulesManager.getInst().onNewIntent(intent);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntMoreGame() {
        if (hasFeature(ConstProp.MODE_HAS_MOREGAME_BTN)) {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.12
                AnonymousClass12() {
                }

                @Override // java.lang.Runnable
                public final void run() {
                    Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                    while (it.hasNext()) {
                        SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                        if (sdkBase.getPropInt(ConstProp.MODE_HAS_MOREGAME_BTN, 0) != 0) {
                            sdkBase.moreGame();
                        }
                    }
                    if (SdkBase.this.getPropInt(ConstProp.MODE_HAS_MOREGAME_BTN, 0) != 0) {
                        SdkBase.this.moreGame();
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$12 */
    final class AnonymousClass12 implements Runnable {
        AnonymousClass12() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                if (sdkBase.getPropInt(ConstProp.MODE_HAS_MOREGAME_BTN, 0) != 0) {
                    sdkBase.moreGame();
                }
            }
            if (SdkBase.this.getPropInt(ConstProp.MODE_HAS_MOREGAME_BTN, 0) != 0) {
                SdkBase.this.moreGame();
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String getSDKVersion(String str) {
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (str.equals(getChannel())) {
            sb.append(getSDKVersion());
        }
        SdkBase sdkBase = getLoginSdkInstMap().get(str);
        if (sdkBase != null) {
            if (sb.length() > 0) {
                sb.append(Marker.ANY_NON_NULL_MARKER);
            }
            sb.append(sdkBase.getSDKVersion());
        }
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase2 = this.sdkInstMap.get(it.next());
            if (str.equals(sdkBase2.getChannel())) {
                if (sb.length() > 0) {
                    sb.append(Marker.ANY_NON_NULL_MARKER);
                }
                sb.append(sdkBase2.getSDKVersion());
            }
        }
        return sb.toString();
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntPrePay() {
        if (d("ntPrePay")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.14
            AnonymousClass14() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.prePay();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$14 */
    final class AnonymousClass14 implements Runnable {
        AnonymousClass14() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.prePay();
        }
    }

    public void anonymousLogin() {
        this.E = System.currentTimeMillis();
    }

    public boolean isLoginInst() {
        return SdkMgr.getInst() == this;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnSaveInstanceState(Bundle bundle) {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                sdkBase.sdkOnSaveInstanceState(bundle);
            }
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
            if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                sdkBase2.sdkOnSaveInstanceState(bundle);
            }
        }
        if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
            sdkOnSaveInstanceState(bundle);
        }
        ModulesManager.getInst().onSaveInstanceState(bundle);
    }

    public void handleOnRestoreInstanceState(Bundle bundle) {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                sdkBase.sdkOnRestoreInstanceState(bundle);
            }
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
            if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                sdkBase2.sdkOnRestoreInstanceState(bundle);
            }
        }
        if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
            sdkOnRestoreInstanceState(bundle);
        }
        ModulesManager.getInst().onRestoreInstanceState(bundle);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnCreate(Bundle bundle) {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                sdkBase.sdkOnCreate(bundle);
            }
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
            if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                sdkBase2.sdkOnCreate(bundle);
            }
        }
        if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
            sdkOnCreate(bundle);
        }
        ModulesManager.getInst().onCreate(bundle);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void handleOnLowMemory() {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            SdkBase sdkBase = this.sdkInstMap.get(it.next());
            if (sdkBase.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase.getChannel())) {
                sdkBase.sdkOnLowMemory();
            }
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            SdkBase sdkBase2 = this.loginSdkInstMap.get(it2.next());
            if (sdkBase2.getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(sdkBase2.getChannel())) {
                sdkBase2.sdkOnLowMemory();
            }
        }
        if (getPropInt(ConstProp.CALL_LIFECYCLE_AFTER_INIT, 0) == 0 || this.x.contains(getChannel())) {
            sdkOnLowMemory();
        }
        ModulesManager.getInst().onLowMemory();
    }

    public boolean isTelecomChannel(String str) {
        return "play_telecom".equals(str) || "play".equals(str) || "mm_10086".equals(str) || "g_10086".equals(str) || "wo_app".equals(str);
    }

    public boolean isValidTelecomChannel(String str) {
        if (!isTelecomChannel(str)) {
            return false;
        }
        String channelByImsi = getChannelByImsi();
        if (str.equals(channelByImsi)) {
            return true;
        }
        return "g_10086".equals(str) && "mm_10086".equals(channelByImsi);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setQuerySkuDetailsListener(OnQuerySkuDetailsListener onQuerySkuDetailsListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setQuerySkuDetailsListener(onQuerySkuDetailsListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setQuerySkuDetailsListener(onQuerySkuDetailsListener, i);
        }
        this.querySkuDetailsListener = onQuerySkuDetailsListener;
        setPropInt(ConstProp.QUERYSKUDETAILS_CALLER_THREAD, i);
    }

    protected void querySkuDetailsDone(List<SkuDetailsInfo> list) {
        if (this.querySkuDetailsListener == null) {
            UniSdkUtils.e("UniSDK Base", "OnQuerySkuDetailsListener not set");
        } else if (getPropInt(ConstProp.ORDER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.15

                /* renamed from: a */
                final /* synthetic */ List f1681a;

                AnonymousClass15(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.querySkuDetailsListener.querySkuDetailsFinished(list);
                }
            });
        } else {
            this.querySkuDetailsListener.querySkuDetailsFinished(list2);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$15 */
    final class AnonymousClass15 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ List f1681a;

        AnonymousClass15(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.querySkuDetailsListener.querySkuDetailsFinished(list);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntQuerySkuDetails(String str, List<String> list) {
        if (d("ntQuerySkuDetails")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.16
            final /* synthetic */ String b;
            final /* synthetic */ List c;

            AnonymousClass16(String str2, List list2) {
                str = str2;
                list = list2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).querySkuDetails(str, list);
                }
                SdkBase.this.querySkuDetails(str, list);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$16 */
    final class AnonymousClass16 implements Runnable {
        final /* synthetic */ String b;
        final /* synthetic */ List c;

        AnonymousClass16(String str2, List list2) {
            str = str2;
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).querySkuDetails(str, list);
            }
            SdkBase.this.querySkuDetails(str, list);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setControllerListener(OnControllerListener onControllerListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setControllerListener(onControllerListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setControllerListener(onControllerListener, i);
        }
        this.m = onControllerListener;
        setPropInt(ConstProp.CONTROLLER_CALLER_THREAD, i);
    }

    public void onKeyDown(int i, PadEvent padEvent) {
        if (this.m == null) {
            UniSdkUtils.e("UniSDK Base", "OnControllerListener not set");
        } else if (getPropInt(ConstProp.CONTROLLER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.17
                final /* synthetic */ PadEvent b;
                final /* synthetic */ int c;

                AnonymousClass17(int i2, PadEvent padEvent2) {
                    i = i2;
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onKeyDown, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onKeyDown(i, padEvent);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.18

                /* renamed from: a */
                final /* synthetic */ PadEvent f1711a;
                final /* synthetic */ int c;

                AnonymousClass18(int i2, PadEvent padEvent2) {
                    i = i2;
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onKeyDown, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onKeyDown(i, padEvent);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$17 */
    final class AnonymousClass17 implements Runnable {
        final /* synthetic */ PadEvent b;
        final /* synthetic */ int c;

        AnonymousClass17(int i2, PadEvent padEvent2) {
            i = i2;
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onKeyDown, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onKeyDown(i, padEvent);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$18 */
    final class AnonymousClass18 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ PadEvent f1711a;
        final /* synthetic */ int c;

        AnonymousClass18(int i2, PadEvent padEvent2) {
            i = i2;
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onKeyDown, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onKeyDown(i, padEvent);
        }
    }

    public void onKeyUp(int i, PadEvent padEvent) {
        if (this.m == null) {
            UniSdkUtils.e("UniSDK Base", "OnControllerListener not set");
        } else if (getPropInt(ConstProp.CONTROLLER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.19

                /* renamed from: a */
                final /* synthetic */ PadEvent f1714a;
                final /* synthetic */ int c;

                AnonymousClass19(int i2, PadEvent padEvent2) {
                    i = i2;
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onKeyUp, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onKeyUp(i, padEvent);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.20

                /* renamed from: a */
                final /* synthetic */ PadEvent f1716a;
                final /* synthetic */ int b;

                AnonymousClass20(int i2, PadEvent padEvent2) {
                    i = i2;
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onKeyUp, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onKeyUp(i, padEvent);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$19 */
    final class AnonymousClass19 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ PadEvent f1714a;
        final /* synthetic */ int c;

        AnonymousClass19(int i2, PadEvent padEvent2) {
            i = i2;
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onKeyUp, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onKeyUp(i, padEvent);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$20 */
    final class AnonymousClass20 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ PadEvent f1716a;
        final /* synthetic */ int b;

        AnonymousClass20(int i2, PadEvent padEvent2) {
            i = i2;
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onKeyUp, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onKeyUp(i, padEvent);
        }
    }

    public void onKeyPressure(int i, float f, PadEvent padEvent) {
        if (this.m == null) {
            UniSdkUtils.e("UniSDK Base", "OnControllerListener not set");
        } else if (getPropInt(ConstProp.CONTROLLER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.21

                /* renamed from: a */
                final /* synthetic */ PadEvent f1717a;
                final /* synthetic */ int b;
                final /* synthetic */ float c;

                AnonymousClass21(int i2, float f2, PadEvent padEvent2) {
                    i = i2;
                    f = f2;
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onKeyPressure, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onKeyPressure(i, f, padEvent);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.22

                /* renamed from: a */
                final /* synthetic */ PadEvent f1718a;
                final /* synthetic */ int c;
                final /* synthetic */ float d;

                AnonymousClass22(int i2, float f2, PadEvent padEvent2) {
                    i = i2;
                    f = f2;
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onKeyPressure, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onKeyPressure(i, f, padEvent);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$21 */
    final class AnonymousClass21 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ PadEvent f1717a;
        final /* synthetic */ int b;
        final /* synthetic */ float c;

        AnonymousClass21(int i2, float f2, PadEvent padEvent2) {
            i = i2;
            f = f2;
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onKeyPressure, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onKeyPressure(i, f, padEvent);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$22 */
    final class AnonymousClass22 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ PadEvent f1718a;
        final /* synthetic */ int c;
        final /* synthetic */ float d;

        AnonymousClass22(int i2, float f2, PadEvent padEvent2) {
            i = i2;
            f = f2;
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onKeyPressure, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onKeyPressure(i, f, padEvent);
        }
    }

    public void onLeftStick(float f, float f2, PadEvent padEvent) {
        if (this.m == null) {
            UniSdkUtils.e("UniSDK Base", "OnControllerListener not set");
        } else if (getPropInt(ConstProp.CONTROLLER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.23

                /* renamed from: a */
                final /* synthetic */ float f1719a;
                final /* synthetic */ float b;
                final /* synthetic */ PadEvent d;

                AnonymousClass23(float f3, float f22, PadEvent padEvent2) {
                    f = f3;
                    f = f22;
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onLeftStick, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onLeftStick(f, f, padEvent);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.25

                /* renamed from: a */
                final /* synthetic */ float f1722a;
                final /* synthetic */ float c;
                final /* synthetic */ PadEvent d;

                AnonymousClass25(float f3, float f22, PadEvent padEvent2) {
                    f = f3;
                    f = f22;
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onLeftStick, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onLeftStick(f, f, padEvent);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$23 */
    final class AnonymousClass23 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ float f1719a;
        final /* synthetic */ float b;
        final /* synthetic */ PadEvent d;

        AnonymousClass23(float f3, float f22, PadEvent padEvent2) {
            f = f3;
            f = f22;
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onLeftStick, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onLeftStick(f, f, padEvent);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$25 */
    final class AnonymousClass25 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ float f1722a;
        final /* synthetic */ float c;
        final /* synthetic */ PadEvent d;

        AnonymousClass25(float f3, float f22, PadEvent padEvent2) {
            f = f3;
            f = f22;
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onLeftStick, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onLeftStick(f, f, padEvent);
        }
    }

    public void onRightStick(float f, float f2, PadEvent padEvent) {
        if (this.m == null) {
            UniSdkUtils.e("UniSDK Base", "OnControllerListener not set");
        } else if (getPropInt(ConstProp.CONTROLLER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.26

                /* renamed from: a */
                final /* synthetic */ float f1723a;
                final /* synthetic */ float c;
                final /* synthetic */ PadEvent d;

                AnonymousClass26(float f3, float f22, PadEvent padEvent2) {
                    f = f3;
                    f = f22;
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onRightStick, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onRightStick(f, f, padEvent);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.27
                final /* synthetic */ PadEvent b;
                final /* synthetic */ float c;
                final /* synthetic */ float d;

                AnonymousClass27(float f3, float f22, PadEvent padEvent2) {
                    f = f3;
                    f = f22;
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onRightStick, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onRightStick(f, f, padEvent);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$26 */
    final class AnonymousClass26 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ float f1723a;
        final /* synthetic */ float c;
        final /* synthetic */ PadEvent d;

        AnonymousClass26(float f3, float f22, PadEvent padEvent2) {
            f = f3;
            f = f22;
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onRightStick, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onRightStick(f, f, padEvent);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$27 */
    final class AnonymousClass27 implements Runnable {
        final /* synthetic */ PadEvent b;
        final /* synthetic */ float c;
        final /* synthetic */ float d;

        AnonymousClass27(float f3, float f22, PadEvent padEvent2) {
            f = f3;
            f = f22;
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onRightStick, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onRightStick(f, f, padEvent);
        }
    }

    public void onStateEvent(PadEvent padEvent) {
        if (this.m == null) {
            UniSdkUtils.e("UniSDK Base", "OnControllerListener not set");
        } else if (getPropInt(ConstProp.CONTROLLER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.28

                /* renamed from: a */
                final /* synthetic */ PadEvent f1725a;

                AnonymousClass28(PadEvent padEvent2) {
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onStateEvent, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onStateEvent(padEvent);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.29

                /* renamed from: a */
                final /* synthetic */ PadEvent f1726a;

                AnonymousClass29(PadEvent padEvent2) {
                    padEvent = padEvent2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onStateEvent, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.m.onStateEvent(padEvent);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$28 */
    final class AnonymousClass28 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ PadEvent f1725a;

        AnonymousClass28(PadEvent padEvent2) {
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onStateEvent, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onStateEvent(padEvent);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$29 */
    final class AnonymousClass29 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ PadEvent f1726a;

        AnonymousClass29(PadEvent padEvent2) {
            padEvent = padEvent2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onStateEvent, current thread=" + Thread.currentThread().getId());
            SdkBase.this.m.onStateEvent(padEvent);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$30 */
    final class AnonymousClass30 implements Runnable {
        AnonymousClass30() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).ccStartService();
            }
            SdkBase.this.ccStartService();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCCStartService() {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.30
            AnonymousClass30() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).ccStartService();
                }
                SdkBase.this.ccStartService();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$31 */
    final class AnonymousClass31 implements Runnable {
        AnonymousClass31() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).ccStopService();
            }
            SdkBase.this.ccStopService();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCCStopService() {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.31
            AnonymousClass31() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).ccStopService();
                }
                SdkBase.this.ccStopService();
            }
        });
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public int getCCPerformance() {
        return getPropInt(ConstProp.CC_PERFORMANCE, -1);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public int getCCWindowState() {
        return getPropInt(ConstProp.CC_WINDOW_STATE, 0);
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$32 */
    final class AnonymousClass32 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1728a;

        AnonymousClass32(String str) {
            str = str;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).showWeb(str);
            }
            SdkBase.this.showWeb(str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntShowWeb(String str) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.32

            /* renamed from: a */
            final /* synthetic */ String f1728a;

            AnonymousClass32(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).showWeb(str);
                }
                SdkBase.this.showWeb(str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$33 */
    final class AnonymousClass33 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1729a;

        AnonymousClass33(String str) {
            str = str;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.collectEvent(str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCollectEvent(String str) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.33

            /* renamed from: a */
            final /* synthetic */ String f1729a;

            AnonymousClass33(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.collectEvent(str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$34 */
    final class AnonymousClass34 implements Runnable {
        final /* synthetic */ String b;
        final /* synthetic */ String c;
        final /* synthetic */ String d;

        AnonymousClass34(String str, String str2, String str3) {
            str = str;
            str = str2;
            str = str3;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.showBoard(str, str, str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntShowBoard(String str, String str2, String str3) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.34
            final /* synthetic */ String b;
            final /* synthetic */ String c;
            final /* synthetic */ String d;

            AnonymousClass34(String str4, String str22, String str32) {
                str = str4;
                str = str22;
                str = str32;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.showBoard(str, str, str);
            }
        });
    }

    /* JADX WARN: Removed duplicated region for block: B:67:0x0034 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0035  */
    @Override // com.netease.ntunisdk.base.GamerInterface
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int DRPF(java.lang.String r7) throws org.json.JSONException {
        /*
            r6 = this;
            java.lang.String r0 = "DRPF"
            java.lang.String r1 = "UniSDK Base"
            com.netease.ntunisdk.base.UniSdkUtils.i(r1, r0)
            java.lang.String r0 = "strJson="
            java.lang.String r2 = java.lang.String.valueOf(r7)
            java.lang.String r0 = r0.concat(r2)
            com.netease.ntunisdk.base.UniSdkUtils.i(r1, r0)
            r0 = 0
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch: java.lang.Exception -> L1b
            r1.<init>(r7)     // Catch: java.lang.Exception -> L1b
            goto L20
        L1b:
            r7 = move-exception
            r7.printStackTrace()
            r1 = r0
        L20:
            if (r1 != 0) goto L24
            r7 = 5
            return r7
        L24:
            r7 = 1
            java.lang.String r2 = "project"
            java.lang.String r2 = r1.getString(r2)     // Catch: java.lang.Exception -> L30
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch: java.lang.Exception -> L31
            goto L32
        L30:
            r2 = r0
        L31:
            r3 = r7
        L32:
            if (r3 == 0) goto L35
            return r7
        L35:
            r7 = 2
            java.lang.String r4 = "source"
            java.lang.String r4 = r1.getString(r4)     // Catch: java.lang.Exception -> L43
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch: java.lang.Exception -> L44
            if (r5 == 0) goto L45
            goto L44
        L43:
            r4 = r0
        L44:
            r3 = r7
        L45:
            if (r3 == 0) goto L48
            return r7
        L48:
            r7 = 3
            java.lang.String r5 = "type"
            java.lang.String r0 = r1.getString(r5)     // Catch: java.lang.Exception -> L55
            boolean r5 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Exception -> L55
            if (r5 == 0) goto L56
        L55:
            r3 = r7
        L56:
            if (r3 == 0) goto L59
            return r7
        L59:
            android.content.Context r7 = r6.myCtx
            com.netease.ntunisdk.base.utils.drpf.DRPF.dispatchDrpf(r7, r1, r2, r4, r0)
            r7 = 0
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.SdkBase.DRPF(java.lang.String):int");
    }

    public void dispatchDrpf(JSONObject jSONObject, String str) throws Throwable {
        String propStr = getPropStr(ConstProp.XM_GAMEID);
        try {
            jSONObject.putOpt("project", propStr);
            jSONObject.putOpt(SocialConstants.PARAM_SOURCE, "sdk_p1");
            jSONObject.putOpt("type", str);
        } catch (JSONException unused) {
        }
        DRPF.dispatchDrpf(this.myCtx, jSONObject, propStr, "sdk_p1", str);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntShowRewardView(List<String> list) {
        if (d("ntShowRewardView")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.36

            /* renamed from: a */
            final /* synthetic */ List f1732a;

            AnonymousClass36(List list2) {
                list = list2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.showRewardView(list);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$36 */
    final class AnonymousClass36 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ List f1732a;

        AnonymousClass36(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.showRewardView(list);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntInviteFriendList(String str, String str2) {
        if (d("ntInviteFriendList")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.37

            /* renamed from: a */
            final /* synthetic */ String f1733a;
            final /* synthetic */ String c;

            AnonymousClass37(String str3, String str22) {
                str = str3;
                str = str22;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.inviteFriendList(str, str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$37 */
    final class AnonymousClass37 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1733a;
        final /* synthetic */ String c;

        AnonymousClass37(String str3, String str22) {
            str = str3;
            str = str22;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.inviteFriendList(str, str);
        }
    }

    public void inviteFriendListFinished(List<String> list) {
        if (this.h == null) {
            UniSdkUtils.e("UniSDK Base", "QueryFriendListener not set");
        } else if (getPropInt(ConstProp.FRIEND_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.38
                final /* synthetic */ List b;

                AnonymousClass38(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onInviteFriendListFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onInviteFriendListFinished(list);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.39
                final /* synthetic */ List b;

                AnonymousClass39(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onInviteFriendListFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onInviteFriendListFinished(list);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$38 */
    final class AnonymousClass38 implements Runnable {
        final /* synthetic */ List b;

        AnonymousClass38(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onInviteFriendListFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onInviteFriendListFinished(list);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$39 */
    final class AnonymousClass39 implements Runnable {
        final /* synthetic */ List b;

        AnonymousClass39(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onInviteFriendListFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onInviteFriendListFinished(list);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntQueryInviterList() {
        if (d("ntQueryInviterList")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.40
            AnonymousClass40() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.queryInviterList();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$40 */
    final class AnonymousClass40 implements Runnable {
        AnonymousClass40() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.queryInviterList();
        }
    }

    public void inviterListFinished(List<AccountInfo> list) {
        if (this.h == null) {
            UniSdkUtils.e("UniSDK Base", "QueryFriendListener not set");
        } else if (getPropInt(ConstProp.FRIEND_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.41

                /* renamed from: a */
                final /* synthetic */ List f1737a;

                AnonymousClass41(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onInviterListFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onInviterListFinished(list);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.42
                final /* synthetic */ List b;

                AnonymousClass42(List list2) {
                    list = list2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "onInviterListFinished, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.h.onInviterListFinished(list);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$41 */
    final class AnonymousClass41 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ List f1737a;

        AnonymousClass41(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onInviterListFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onInviterListFinished(list);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$42 */
    final class AnonymousClass42 implements Runnable {
        final /* synthetic */ List b;

        AnonymousClass42(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "onInviterListFinished, current thread=" + Thread.currentThread().getId());
            SdkBase.this.h.onInviterListFinished(list);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntDeleteInviters(List<String> list) {
        if (d("ntDeleteInviters")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.43

            /* renamed from: a */
            final /* synthetic */ List f1739a;

            AnonymousClass43(List list2) {
                list = list2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.deleteInviters(list);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$43 */
    final class AnonymousClass43 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ List f1739a;

        AnonymousClass43(List list2) {
            list = list2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.deleteInviters(list);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$44 */
    final class AnonymousClass44 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1740a;
        final /* synthetic */ List b;

        AnonymousClass44(String str, List list) {
            str = str;
            list = list;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.sendPushNotification(str, list);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntSendPushNotification(String str, List<String> list) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.44

            /* renamed from: a */
            final /* synthetic */ String f1740a;
            final /* synthetic */ List b;

            AnonymousClass44(String str2, List list2) {
                str = str2;
                list = list2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.sendPushNotification(str, list);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$45 */
    final class AnonymousClass45 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1741a;

        AnonymousClass45(String str) {
            str = str;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.sendLocalNotification(str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntSendLocalNotification(String str) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.45

            /* renamed from: a */
            final /* synthetic */ String f1741a;

            AnonymousClass45(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.sendLocalNotification(str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$47 */
    final class AnonymousClass47 implements Runnable {
        final /* synthetic */ int b;

        AnonymousClass47(int i) {
            i = i;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.cancelLocalNotification(i);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCancelLocalNotification(int i) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.47
            final /* synthetic */ int b;

            AnonymousClass47(int i2) {
                i = i2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.cancelLocalNotification(i);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$48 */
    final class AnonymousClass48 implements Runnable {
        AnonymousClass48() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.getUsePushNotification();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntGetUsePushNotification() {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.48
            AnonymousClass48() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.getUsePushNotification();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$49 */
    final class AnonymousClass49 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1743a;

        AnonymousClass49(boolean z) {
            z = z;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.setUsePushNotification(z);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntSetUsePushNotification(boolean z) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.49

            /* renamed from: a */
            final /* synthetic */ boolean f1743a;

            AnonymousClass49(boolean z2) {
                z = z2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SdkBase.this.setUsePushNotification(z);
            }
        });
    }

    public void consumeOrderDone(OrderInfo orderInfo) {
        if (this.orderListener == null) {
            UniSdkUtils.e("UniSDK Base", "OnOrderCheckListener not set");
        } else if (getPropInt(ConstProp.ORDER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.50

                /* renamed from: a */
                final /* synthetic */ OrderInfo f1745a;

                AnonymousClass50(OrderInfo orderInfo2) {
                    orderInfo = orderInfo2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.orderListener.orderConsumeDone(orderInfo);
                }
            });
        } else {
            this.orderListener.orderConsumeDone(orderInfo2);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$50 */
    final class AnonymousClass50 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ OrderInfo f1745a;

        AnonymousClass50(OrderInfo orderInfo2) {
            orderInfo = orderInfo2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.orderListener.orderConsumeDone(orderInfo);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntConnectToChannel() {
        connectToChannel();
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntDisConnectFromChannel() {
        disConnectFromChannel();
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public boolean ntHasChannelConnected() {
        return hasChannelConnected();
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String ntGetChannelID() {
        return getChannelID();
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntSelectChannelOption(int i) {
        selectChannelOption(i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntSetZone(String str) {
        setZone(str);
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$51 */
    final class AnonymousClass51 implements Runnable {
        AnonymousClass51() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).showConversation();
            }
            SdkBase.this.showConversation();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntShowConversation() {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.51
            AnonymousClass51() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).showConversation();
                }
                SdkBase.this.showConversation();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$52 */
    final class AnonymousClass52 implements Runnable {
        AnonymousClass52() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).showFAQs();
            }
            SdkBase.this.showFAQs();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntShowFAQs() {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.52
            AnonymousClass52() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).showFAQs();
                }
                SdkBase.this.showFAQs();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$53 */
    final class AnonymousClass53 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1746a;

        AnonymousClass53(String str) {
            str = str;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).setUserIdentifier(str);
            }
            SdkBase.this.setUserIdentifier(str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntSetUserIdentifier(String str) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.53

            /* renamed from: a */
            final /* synthetic */ String f1746a;

            AnonymousClass53(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).setUserIdentifier(str);
                }
                SdkBase.this.setUserIdentifier(str);
            }
        });
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntDisplayLeaderboard(String str) {
        if (d("ntDisplayLeaderboard")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.54

            /* renamed from: a */
            final /* synthetic */ String f1747a;

            AnonymousClass54(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).displayLeaderboard(str);
                }
                SdkBase.this.displayLeaderboard(str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$54 */
    final class AnonymousClass54 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1747a;

        AnonymousClass54(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).displayLeaderboard(str);
            }
            SdkBase.this.displayLeaderboard(str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntDisplayAchievement() {
        if (d("ntDisplayAchievement")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.55
            AnonymousClass55() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).displayAchievement();
                }
                SdkBase.this.displayAchievement();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$55 */
    final class AnonymousClass55 implements Runnable {
        AnonymousClass55() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).displayAchievement();
            }
            SdkBase.this.displayAchievement();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntUpdateAchievement(String str, int i) {
        if (d("ntUpdateAchievement")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.56
            final /* synthetic */ String b;
            final /* synthetic */ int c;

            AnonymousClass56(String str2, int i2) {
                str = str2;
                i = i2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).updateAchievement(str, i);
                }
                SdkBase.this.updateAchievement(str, i);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$56 */
    final class AnonymousClass56 implements Runnable {
        final /* synthetic */ String b;
        final /* synthetic */ int c;

        AnonymousClass56(String str2, int i2) {
            str = str2;
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).updateAchievement(str, i);
            }
            SdkBase.this.updateAchievement(str, i);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntUpdateEvent(String str, int i) {
        if (d("ntUpdateEvent")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.58

            /* renamed from: a */
            final /* synthetic */ String f1750a;
            final /* synthetic */ int b;

            AnonymousClass58(String str2, int i2) {
                str = str2;
                i = i2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).updateEvent(str, i);
                }
                SdkBase.this.updateEvent(str, i);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$58 */
    final class AnonymousClass58 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1750a;
        final /* synthetic */ int b;

        AnonymousClass58(String str2, int i2) {
            str = str2;
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).updateEvent(str, i);
            }
            SdkBase.this.updateEvent(str, i);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntDisplayQuests(int[] iArr) {
        if (d("ntDisplayQuests")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.59
            final /* synthetic */ int[] b;

            AnonymousClass59(int[] iArr2) {
                iArr = iArr2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).displayQuests(iArr);
                }
                SdkBase.this.displayQuests(iArr);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$59 */
    final class AnonymousClass59 implements Runnable {
        final /* synthetic */ int[] b;

        AnonymousClass59(int[] iArr2) {
            iArr = iArr2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).displayQuests(iArr);
            }
            SdkBase.this.displayQuests(iArr);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntPresentQRCodeScanner() {
        if (d("ntPresentQRCodeScanner")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.60
            AnonymousClass60() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).presentQRCodeScanner();
                }
                SdkBase.this.presentQRCodeScanner();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$60 */
    final class AnonymousClass60 implements Runnable {
        AnonymousClass60() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).presentQRCodeScanner();
            }
            SdkBase.this.presentQRCodeScanner();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntPresentQRCodeScanner(String str, int i) {
        if (d("ntPresentQRCodeScanner")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.61

            /* renamed from: a */
            final /* synthetic */ int f1753a;
            final /* synthetic */ String c;

            AnonymousClass61(String str2, int i2) {
                str = str2;
                i = i2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                    sdkBase.presentQRCodeScanner();
                    sdkBase.presentQRCodeScanner(str, i);
                }
                SdkBase.this.presentQRCodeScanner();
                SdkBase.this.presentQRCodeScanner(str, i);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$61 */
    final class AnonymousClass61 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1753a;
        final /* synthetic */ String c;

        AnonymousClass61(String str2, int i2) {
            str = str2;
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase sdkBase = SdkBase.this.sdkInstMap.get(it.next());
                sdkBase.presentQRCodeScanner();
                sdkBase.presentQRCodeScanner(str, i);
            }
            SdkBase.this.presentQRCodeScanner();
            SdkBase.this.presentQRCodeScanner(str, i);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setCodeScannerListener(OnCodeScannerListener onCodeScannerListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setCodeScannerListener(onCodeScannerListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setCodeScannerListener(onCodeScannerListener, i);
        }
        this.q = onCodeScannerListener;
        setPropInt(ConstProp.CODESCANNER_CALLER_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setQRCodeListener(OnQRCodeListener onQRCodeListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setQRCodeListener(onQRCodeListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setQRCodeListener(onQRCodeListener, i);
        }
        this.r = onQRCodeListener;
        setPropInt(ConstProp.QRCODE_CALLER_THREAD, i);
    }

    protected void codeScannerDone(int i, String str) {
        if (this.q == null) {
            UniSdkUtils.e("UniSDK Base", "OnCodeScannerListener not set");
        } else if (getPropInt(ConstProp.CODESCANNER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.62

                /* renamed from: a */
                final /* synthetic */ int f1754a;
                final /* synthetic */ String c;

                AnonymousClass62(int i2, String str2) {
                    i = i2;
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "codeScannerDone, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.q.codeScannerFinish(i, str);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.63

                /* renamed from: a */
                final /* synthetic */ int f1755a;
                final /* synthetic */ String c;

                AnonymousClass63(int i2, String str2) {
                    i = i2;
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "codeScannerDone, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.q.codeScannerFinish(i, str);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$62 */
    final class AnonymousClass62 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1754a;
        final /* synthetic */ String c;

        AnonymousClass62(int i2, String str2) {
            i = i2;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "codeScannerDone, current thread=" + Thread.currentThread().getId());
            SdkBase.this.q.codeScannerFinish(i, str);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$63 */
    final class AnonymousClass63 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1755a;
        final /* synthetic */ String c;

        AnonymousClass63(int i2, String str2) {
            i = i2;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "codeScannerDone, current thread=" + Thread.currentThread().getId());
            SdkBase.this.q.codeScannerFinish(i, str);
        }
    }

    public void createQRCodeDone(String str) {
        if (this.r == null) {
            UniSdkUtils.e("UniSDK Base", "OnQRCodeListener not set");
        } else if (getPropInt(ConstProp.CODESCANNER_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.64

                /* renamed from: a */
                final /* synthetic */ String f1756a;

                AnonymousClass64(String str2) {
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "createQRCodeDone, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.r.createQRCodeDone(str);
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.65

                /* renamed from: a */
                final /* synthetic */ String f1757a;

                AnonymousClass65(String str2) {
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "createQRCodeDone, current thread=" + Thread.currentThread().getId());
                    SdkBase.this.r.createQRCodeDone(str);
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$64 */
    final class AnonymousClass64 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1756a;

        AnonymousClass64(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "createQRCodeDone, current thread=" + Thread.currentThread().getId());
            SdkBase.this.r.createQRCodeDone(str);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$65 */
    final class AnonymousClass65 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1757a;

        AnonymousClass65(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "createQRCodeDone, current thread=" + Thread.currentThread().getId());
            SdkBase.this.r.createQRCodeDone(str);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$66 */
    final class AnonymousClass66 implements Runnable {
        final /* synthetic */ boolean b;

        AnonymousClass66(boolean z) {
            z = z;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).getNotice(z);
            }
            SdkBase.this.getNotice(z);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntGetNotice(boolean z) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.66
            final /* synthetic */ boolean b;

            AnonymousClass66(boolean z2) {
                z = z2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).getNotice(z);
                }
                SdkBase.this.getNotice(z);
            }
        });
    }

    public void getNoticeMsgDone(String str) {
        if (this.f1633a == null) {
            UniSdkUtils.e("UniSDK Base", "startupListener not set");
        } else if (getPropInt(ConstProp.CONTINUE_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.67
                final /* synthetic */ String b;

                AnonymousClass67(String str2) {
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SdkBase.this.f1633a.getNoticeMsgDone(str);
                }
            });
        } else {
            this.f1633a.getNoticeMsgDone(str2);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$67 */
    final class AnonymousClass67 implements Runnable {
        final /* synthetic */ String b;

        AnonymousClass67(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            SdkBase.this.f1633a.getNoticeMsgDone(str);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$69 */
    final class AnonymousClass69 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1761a;
        final /* synthetic */ String b;

        AnonymousClass69(String str, String str2) {
            str = str;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            if (!SdkBase.this.sdkInstMap.isEmpty() && SdkBase.this.sdkInstMap.containsKey("ngadvert")) {
                UniSdkUtils.d("UniSDK Base", "call ngadvert");
                SdkBase.this.sdkInstMap.get("ngadvert").trackCustomEvent(str, str);
                return;
            }
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).trackCustomEvent(str, str);
            }
            SdkBase.this.trackCustomEvent(str, str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntTrackCustomEvent(String str, String str2) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.69

            /* renamed from: a */
            final /* synthetic */ String f1761a;
            final /* synthetic */ String b;

            AnonymousClass69(String str3, String str22) {
                str = str3;
                str = str22;
            }

            @Override // java.lang.Runnable
            public final void run() {
                if (!SdkBase.this.sdkInstMap.isEmpty() && SdkBase.this.sdkInstMap.containsKey("ngadvert")) {
                    UniSdkUtils.d("UniSDK Base", "call ngadvert");
                    SdkBase.this.sdkInstMap.get("ngadvert").trackCustomEvent(str, str);
                    return;
                }
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).trackCustomEvent(str, str);
                }
                SdkBase.this.trackCustomEvent(str, str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$70 */
    final class AnonymousClass70 implements Runnable {
        AnonymousClass70() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).flushCustomEvents();
            }
            SdkBase.this.flushCustomEvents();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntFlushCustomEvents() {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.70
            AnonymousClass70() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).flushCustomEvents();
                }
                SdkBase.this.flushCustomEvents();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$71 */
    final class AnonymousClass71 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1763a;
        final /* synthetic */ boolean c;

        AnonymousClass71(String str, boolean z) {
            str = str;
            z = z;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).sendProfile(str, z);
            }
            SdkBase.this.sendProfile(str, z);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntSendProfile(String str, boolean z) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.71

            /* renamed from: a */
            final /* synthetic */ String f1763a;
            final /* synthetic */ boolean c;

            AnonymousClass71(String str2, boolean z2) {
                str = str2;
                z = z2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).sendProfile(str, z);
                }
                SdkBase.this.sendProfile(str, z);
            }
        });
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntGetAnnouncementInfo() {
        Context context = this.myCtx;
        if (context == null) {
            return;
        }
        ((Activity) context).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.72
            AnonymousClass72() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).getAnnouncementInfo();
                }
                SdkBase.this.getAnnouncementInfo();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$72 */
    final class AnonymousClass72 implements Runnable {
        AnonymousClass72() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).getAnnouncementInfo();
            }
            SdkBase.this.getAnnouncementInfo();
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$73 */
    final class AnonymousClass73 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1764a;
        final /* synthetic */ String b;

        AnonymousClass73(String str, String str2) {
            str = str;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).updateApi(str, str);
            }
            SdkBase.this.updateApi(str, str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntUpdateApi(String str, String str2) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.73

            /* renamed from: a */
            final /* synthetic */ String f1764a;
            final /* synthetic */ String b;

            AnonymousClass73(String str3, String str22) {
                str = str3;
                str = str22;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).updateApi(str, str);
                }
                SdkBase.this.updateApi(str, str);
            }
        });
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public boolean ntHasPlatform(String str) {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            if (this.sdkInstMap.get(it.next()).hasPlatform(str)) {
                return true;
            }
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            if (this.loginSdkInstMap.get(it2.next()).hasPlatform(str)) {
                return true;
            }
        }
        return hasPlatform(str);
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$74 */
    final class AnonymousClass74 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1765a;

        AnonymousClass74(String str) {
            str = str;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).callbackSuccess(str);
            }
            SdkBase.this.callbackSuccess(str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCallbackSuccess(String str) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.74

            /* renamed from: a */
            final /* synthetic */ String f1765a;

            AnonymousClass74(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).callbackSuccess(str);
                }
                SdkBase.this.callbackSuccess(str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$75 */
    final class AnonymousClass75 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1766a;

        AnonymousClass75(String str) {
            str = str;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).callbackFail(str);
            }
            SdkBase.this.callbackFail(str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCallbackFail(String str) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.75

            /* renamed from: a */
            final /* synthetic */ String f1766a;

            AnonymousClass75(String str2) {
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).callbackFail(str);
                }
                SdkBase.this.callbackFail(str);
            }
        });
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntOpenWebView(String str) {
        UniSdkUtils.d("UniSDK Base", "ntOpenWebView:".concat(String.valueOf(str)));
        WebViewProxy.getInstance().openWebView(this.myCtx, str);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCloseWebView() {
        UniSdkUtils.d("UniSDK Base", "ntCloseWebView");
        WebViewProxy.getInstance().closeWebView();
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setWebViewListener(OnWebViewListener onWebViewListener, int i) throws JSONException {
        WebViewProxy.getInstance().setWebViewListener(onWebViewListener);
        setPropInt(ConstProp.WEBVIEW_CALLER_THREAD, i);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntVerifyMobile(int i) {
        if (d("ntVerifyMobile")) {
            return;
        }
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.76

            /* renamed from: a */
            final /* synthetic */ int f1767a;

            AnonymousClass76(int i2) {
                i = i2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).verifyMobile(i);
                }
                Iterator<String> it2 = SdkBase.this.loginSdkInstMap.keySet().iterator();
                while (it2.hasNext()) {
                    SdkBase.this.loginSdkInstMap.get(it2.next()).verifyMobile(i);
                }
                SdkBase.this.verifyMobile(i);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$76 */
    final class AnonymousClass76 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1767a;

        AnonymousClass76(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).verifyMobile(i);
            }
            Iterator<String> it2 = SdkBase.this.loginSdkInstMap.keySet().iterator();
            while (it2.hasNext()) {
                SdkBase.this.loginSdkInstMap.get(it2.next()).verifyMobile(i);
            }
            SdkBase.this.verifyMobile(i);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setVerifyListener(OnVerifyListener onVerifyListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setVerifyListener(onVerifyListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setVerifyListener(onVerifyListener, i);
        }
        this.p = onVerifyListener;
        setPropInt(ConstProp.VERIFY_CALLER_THREAD, i);
    }

    public void verifyDone(boolean z, int i, String str) {
        if (this.p == null) {
            UniSdkUtils.e("UniSDK Base", "OnVerifyListener not set");
        } else if (getPropInt(ConstProp.VERIFY_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.168

                /* renamed from: a */
                final /* synthetic */ boolean f1699a;
                final /* synthetic */ int b;
                final /* synthetic */ String d;

                AnonymousClass168(boolean z2, int i2, String str2) {
                    z = z2;
                    i = i2;
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "verifyDone, current thread=" + Thread.currentThread().getId());
                    if (z) {
                        SdkBase.this.p.onSuccess(i, str);
                    } else {
                        SdkBase.this.p.onFailure(i, str);
                    }
                }
            });
        } else {
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.169

                /* renamed from: a */
                final /* synthetic */ boolean f1700a;
                final /* synthetic */ int c;
                final /* synthetic */ String d;

                AnonymousClass169(boolean z2, int i2, String str2) {
                    z = z2;
                    i = i2;
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.i("UniSDK Base", "verifyDone, current thread=" + Thread.currentThread().getId());
                    if (z) {
                        SdkBase.this.p.onSuccess(i, str);
                    } else {
                        SdkBase.this.p.onFailure(i, str);
                    }
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$168 */
    final class AnonymousClass168 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1699a;
        final /* synthetic */ int b;
        final /* synthetic */ String d;

        AnonymousClass168(boolean z2, int i2, String str2) {
            z = z2;
            i = i2;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "verifyDone, current thread=" + Thread.currentThread().getId());
            if (z) {
                SdkBase.this.p.onSuccess(i, str);
            } else {
                SdkBase.this.p.onFailure(i, str);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$169 */
    final class AnonymousClass169 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ boolean f1700a;
        final /* synthetic */ int c;
        final /* synthetic */ String d;

        AnonymousClass169(boolean z2, int i2, String str2) {
            z = z2;
            i = i2;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "verifyDone, current thread=" + Thread.currentThread().getId());
            if (z) {
                SdkBase.this.p.onSuccess(i, str);
            } else {
                SdkBase.this.p.onFailure(i, str);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$170 */
    final class AnonymousClass170 implements Runnable {
        AnonymousClass170() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            StartupDialog.ntCloseFlash();
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCloseFlash() {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.170
            AnonymousClass170() {
            }

            @Override // java.lang.Runnable
            public final void run() {
                StartupDialog.ntCloseFlash();
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$171 */
    final class AnonymousClass171 implements Runnable {
        AnonymousClass171() {
        }

        @Override // java.lang.Runnable
        public final void run() throws Throwable {
            String propStr = SdkBase.this.getPropStr("JF_GAMEID");
            String propStr2 = SdkBase.this.getPropStr(ConstProp.GAME_VERSION);
            String propStr3 = SdkBase.this.getPropStr(ConstProp.DERIVE_CHANNEL);
            String propStr4 = SdkBase.this.getPropStr(ConstProp.CMCC_PAYTYPE_URL);
            SharedPreferences sharedPreferences = SdkBase.this.myCtx.getSharedPreferences("UNISDK", 0);
            if (TextUtils.isEmpty(propStr4)) {
                UniSdkUtils.e("UniSDK Base", "CMCC_PAYTYPE_URL is null");
                SdkBase sdkBase = SdkBase.this;
                sdkBase.A = sharedPreferences.getString("CMCC_PAYTYPE", sdkBase.A);
                UniSdkUtils.d("UniSDK Base", "cmcc_paytype:" + SdkBase.this.A);
                return;
            }
            String str = String.format(propStr4 + "?gameTag=%s&gameVersion=%s&channelTag=%s", propStr, propStr2, propStr3);
            UniSdkUtils.d("UniSDK Base", str);
            NetUtil.wget(str, new WgetDoneCallback() { // from class: com.netease.ntunisdk.base.SdkBase.171.1

                /* renamed from: a */
                final /* synthetic */ SharedPreferences f1702a;

                AnonymousClass1(SharedPreferences sharedPreferences2) {
                    sharedPreferences = sharedPreferences2;
                }

                @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
                public final void ProcessResult(String str2) {
                    try {
                        if (!TextUtils.isEmpty(str2)) {
                            JSONObject jSONObject = new JSONObject(str2);
                            if ("S_OK".equals(jSONObject.getString("code"))) {
                                SdkBase.this.A = jSONObject.getString("data");
                                SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                                editorEdit.putString("CMCC_PAYTYPE", SdkBase.this.A);
                                editorEdit.commit();
                            } else {
                                SdkBase.this.A = sharedPreferences.getString("CMCC_PAYTYPE", SdkBase.this.A);
                            }
                        } else {
                            SdkBase.this.A = sharedPreferences.getString("CMCC_PAYTYPE", SdkBase.this.A);
                        }
                        UniSdkUtils.d("UniSDK Base", "cmcc_paytype:" + SdkBase.this.A);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        UniSdkUtils.e("UniSDK Base", "queryCmccPaytype parse json error");
                    }
                }
            });
        }

        /* renamed from: com.netease.ntunisdk.base.SdkBase$171$1 */
        final class AnonymousClass1 implements WgetDoneCallback {

            /* renamed from: a */
            final /* synthetic */ SharedPreferences f1702a;

            AnonymousClass1(SharedPreferences sharedPreferences2) {
                sharedPreferences = sharedPreferences2;
            }

            @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
            public final void ProcessResult(String str2) {
                try {
                    if (!TextUtils.isEmpty(str2)) {
                        JSONObject jSONObject = new JSONObject(str2);
                        if ("S_OK".equals(jSONObject.getString("code"))) {
                            SdkBase.this.A = jSONObject.getString("data");
                            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                            editorEdit.putString("CMCC_PAYTYPE", SdkBase.this.A);
                            editorEdit.commit();
                        } else {
                            SdkBase.this.A = sharedPreferences.getString("CMCC_PAYTYPE", SdkBase.this.A);
                        }
                    } else {
                        SdkBase.this.A = sharedPreferences.getString("CMCC_PAYTYPE", SdkBase.this.A);
                    }
                    UniSdkUtils.d("UniSDK Base", "cmcc_paytype:" + SdkBase.this.A);
                } catch (JSONException e) {
                    e.printStackTrace();
                    UniSdkUtils.e("UniSDK Base", "queryCmccPaytype parse json error");
                }
            }
        }
    }

    public void queryCmccPaytype() {
        CachedThreadPoolUtil.getInstance().exec(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.171
            AnonymousClass171() {
            }

            @Override // java.lang.Runnable
            public final void run() throws Throwable {
                String propStr = SdkBase.this.getPropStr("JF_GAMEID");
                String propStr2 = SdkBase.this.getPropStr(ConstProp.GAME_VERSION);
                String propStr3 = SdkBase.this.getPropStr(ConstProp.DERIVE_CHANNEL);
                String propStr4 = SdkBase.this.getPropStr(ConstProp.CMCC_PAYTYPE_URL);
                SharedPreferences sharedPreferences2 = SdkBase.this.myCtx.getSharedPreferences("UNISDK", 0);
                if (TextUtils.isEmpty(propStr4)) {
                    UniSdkUtils.e("UniSDK Base", "CMCC_PAYTYPE_URL is null");
                    SdkBase sdkBase = SdkBase.this;
                    sdkBase.A = sharedPreferences2.getString("CMCC_PAYTYPE", sdkBase.A);
                    UniSdkUtils.d("UniSDK Base", "cmcc_paytype:" + SdkBase.this.A);
                    return;
                }
                String str = String.format(propStr4 + "?gameTag=%s&gameVersion=%s&channelTag=%s", propStr, propStr2, propStr3);
                UniSdkUtils.d("UniSDK Base", str);
                NetUtil.wget(str, new WgetDoneCallback() { // from class: com.netease.ntunisdk.base.SdkBase.171.1

                    /* renamed from: a */
                    final /* synthetic */ SharedPreferences f1702a;

                    AnonymousClass1(SharedPreferences sharedPreferences22) {
                        sharedPreferences = sharedPreferences22;
                    }

                    @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
                    public final void ProcessResult(String str2) {
                        try {
                            if (!TextUtils.isEmpty(str2)) {
                                JSONObject jSONObject = new JSONObject(str2);
                                if ("S_OK".equals(jSONObject.getString("code"))) {
                                    SdkBase.this.A = jSONObject.getString("data");
                                    SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                                    editorEdit.putString("CMCC_PAYTYPE", SdkBase.this.A);
                                    editorEdit.commit();
                                } else {
                                    SdkBase.this.A = sharedPreferences.getString("CMCC_PAYTYPE", SdkBase.this.A);
                                }
                            } else {
                                SdkBase.this.A = sharedPreferences.getString("CMCC_PAYTYPE", SdkBase.this.A);
                            }
                            UniSdkUtils.d("UniSDK Base", "cmcc_paytype:" + SdkBase.this.A);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            UniSdkUtils.e("UniSDK Base", "queryCmccPaytype parse json error");
                        }
                    }
                });
            }

            /* renamed from: com.netease.ntunisdk.base.SdkBase$171$1 */
            final class AnonymousClass1 implements WgetDoneCallback {

                /* renamed from: a */
                final /* synthetic */ SharedPreferences f1702a;

                AnonymousClass1(SharedPreferences sharedPreferences22) {
                    sharedPreferences = sharedPreferences22;
                }

                @Override // com.netease.ntunisdk.base.utils.WgetDoneCallback
                public final void ProcessResult(String str2) {
                    try {
                        if (!TextUtils.isEmpty(str2)) {
                            JSONObject jSONObject = new JSONObject(str2);
                            if ("S_OK".equals(jSONObject.getString("code"))) {
                                SdkBase.this.A = jSONObject.getString("data");
                                SharedPreferences.Editor editorEdit = sharedPreferences.edit();
                                editorEdit.putString("CMCC_PAYTYPE", SdkBase.this.A);
                                editorEdit.commit();
                            } else {
                                SdkBase.this.A = sharedPreferences.getString("CMCC_PAYTYPE", SdkBase.this.A);
                            }
                        } else {
                            SdkBase.this.A = sharedPreferences.getString("CMCC_PAYTYPE", SdkBase.this.A);
                        }
                        UniSdkUtils.d("UniSDK Base", "cmcc_paytype:" + SdkBase.this.A);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        UniSdkUtils.e("UniSDK Base", "queryCmccPaytype parse json error");
                    }
                }
            }
        });
    }

    public void saveLogToJFOnPay(OrderInfo orderInfo) {
        GamerInterface inst = SdkMgr.getInst();
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("gameid", inst.getPropStr("JF_GAMEID"));
        String orderChannel = orderInfo.getOrderChannel();
        treeMap.put(CodeScannerConst.PAY_CHANNEL, orderChannel);
        String appChannel = inst.getAppChannel();
        if (TextUtils.isEmpty(appChannel)) {
            StrUtil.showAlertDialog((Activity) this.myCtx, "", "\u8bf7\u8bbe\u7f6eAPP_CHANNEL");
            return;
        }
        treeMap.put(com.netease.ntunisdk.external.protocol.Const.APP_CHANNEL, appChannel);
        treeMap.put("udid", inst.getUdid());
        treeMap.put("platform", inst.getPlatform());
        if (1 == getPropInt(ConstProp.HAS_PAY_CB, 1)) {
            treeMap.put("isonline", "true");
        } else {
            treeMap.put("isonline", com.facebook.hermes.intl.Constants.CASEFIRST_FALSE);
        }
        treeMap.put("goodsid", orderInfo.getProductId());
        treeMap.put("goodsname", orderInfo.getProductName());
        treeMap.put("sn", orderInfo.getOrderId());
        treeMap.put("consumesn", orderInfo.getSdkOrderId());
        StringBuilder sb = new StringBuilder();
        sb.append(orderInfo.getProductCurrentPrice() * orderInfo.getCount());
        treeMap.put("ordermoney", sb.toString());
        treeMap.put(ConstProp.CURRENCY, orderInfo.getOrderCurrency());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(orderInfo.getProductCurrentPrice() * orderInfo.getCount());
        treeMap.put("paymoney", sb2.toString());
        treeMap.put("paycurrency", orderInfo.getOrderCurrency());
        treeMap.put("logtime", new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.US).format(new Date()));
        treeMap.put("sdkversion", getSDKVersion(orderChannel));
        treeMap.put("extendjson", orderInfo.getExtendJson());
        String propStr = ClientLogUtils.isEBOversea() ? "https://applog.matrix.easebar.com/client/sdk/pay_log" : SdkCallback.JF_PAY_LOG_URL;
        if (!TextUtils.isEmpty(inst.getPropStr(ConstProp.JF_PAY_LOG_URL))) {
            propStr = inst.getPropStr(ConstProp.JF_PAY_LOG_URL);
        }
        if (!TextUtils.isEmpty(inst.getPropStr(ConstProp.JF_OVERSEA_PAY_LOG_URL))) {
            propStr = inst.getPropStr(ConstProp.JF_OVERSEA_PAY_LOG_URL);
        }
        if (!TextUtils.isEmpty(propStr)) {
            saveLogToJF(treeMap, propStr);
        } else {
            UniSdkUtils.i("UniSDK Base", "null or empty jfPayLogUrl");
        }
    }

    public void saveLogToJFOnOpen() {
        GamerInterface inst = SdkMgr.getInst();
        Map<String, Object> treeMap = new TreeMap<>();
        treeMap.put("gameid", inst.getPropStr("JF_GAMEID"));
        String payChannel = inst.getPayChannel();
        if (TextUtils.isEmpty(payChannel)) {
            payChannel = inst.getChannel();
        }
        treeMap.put(CodeScannerConst.PAY_CHANNEL, payChannel);
        String appChannel = inst.getAppChannel();
        if (TextUtils.isEmpty(appChannel)) {
            StrUtil.showAlertDialog((Activity) this.myCtx, "", "\u8bf7\u8bbe\u7f6eAPP_CHANNEL");
            return;
        }
        treeMap.put(com.netease.ntunisdk.external.protocol.Const.APP_CHANNEL, appChannel);
        treeMap.put("udid", inst.getUdid());
        treeMap.put("platform", inst.getPlatform());
        treeMap.put("ordermoney", JSONObject.NULL);
        treeMap.put(ConstProp.CURRENCY, JSONObject.NULL);
        treeMap.put("paymoney", JSONObject.NULL);
        treeMap.put("paycurrency", JSONObject.NULL);
        treeMap.put("logtime", new SimpleDateFormat(ClientLogConstant.DATA_FORMAT, Locale.US).format(new Date()));
        treeMap.put("sdkversion", getSDKVersion(payChannel));
        String propStr = ClientLogUtils.isEBOversea() ? "https://applog.matrix.easebar.com/client/sdk/open_log" : SdkCallback.JF_OPEN_LOG_URL;
        if (!TextUtils.isEmpty(inst.getPropStr(ConstProp.JF_OPEN_LOG_URL))) {
            propStr = inst.getPropStr(ConstProp.JF_OPEN_LOG_URL);
        }
        if (!TextUtils.isEmpty(inst.getPropStr(ConstProp.JF_OVERSEA_OPEN_LOG_URL))) {
            propStr = inst.getPropStr(ConstProp.JF_OVERSEA_OPEN_LOG_URL);
        }
        if (!TextUtils.isEmpty(propStr)) {
            saveLogToJF(treeMap, propStr);
        } else {
            UniSdkUtils.i("UniSDK Base", "null or empty jfOpenLogUrl");
        }
    }

    private void g() {
        if (this.myCtx == null) {
            return;
        }
        if (TextUtils.isEmpty(SdkMgr.getInst().getPropStr("JF_GAMEID"))) {
            StrUtil.showAlertDialog((Activity) this.myCtx, "", "no JF_GAMEID \n" + P);
            return;
        }
        if (TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY))) {
            StrUtil.showAlertDialog((Activity) this.myCtx, "", "no JF_LOG_KEY \n" + P);
        }
    }

    public void saveLogToJF(Map<String, Object> map, String str) {
        UniSdkUtils.i("UniSDK Base", "saveLogToJF, params=".concat(String.valueOf(map)));
        if (!map.containsKey("gameid")) {
            UniSdkUtils.e("UniSDK Base", "no JF_GAMEID");
            return;
        }
        String str2 = (String) map.get("gameid");
        if (TextUtils.isEmpty(str2)) {
            UniSdkUtils.e("UniSDK Base", "no JF_GAMEID");
            return;
        }
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.e("UniSDK Base", "no JF_OPEN_LOG_URL or JF_PAY_LOG_URL");
            return;
        }
        String propStr = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
        if (TextUtils.isEmpty(propStr)) {
            UniSdkUtils.e("UniSDK Base", "no JF_LOG_KEY");
            return;
        }
        String string = StrUtil.mapToJson(map).toString();
        UniSdkUtils.d("UniSDK Base", "saveLogToJF, strJson=".concat(String.valueOf(string)));
        try {
            String strEncodeToString = Base64.encodeToString(Crypto.aesEncrypt(string.getBytes("UTF-8"), Base64.encodeToString(propStr.getBytes("UTF-8"), 0)), 0);
            TreeMap treeMap = new TreeMap();
            treeMap.put("gameid", str2);
            treeMap.put("data", strEncodeToString);
            HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
            queueItemNewQueueItem.method = "POST";
            queueItemNewQueueItem.url = str;
            queueItemNewQueueItem.bSync = Boolean.TRUE;
            queueItemNewQueueItem.leftRetry = 50;
            queueItemNewQueueItem.setBody(treeMap);
            queueItemNewQueueItem.transParam = ConstProp.JF_PAY_LOG_URL;
            HTTPQueue.getInstance("LOG").checkResend();
            HTTPQueue.getInstance("LOG").addItem(queueItemNewQueueItem);
        } catch (Exception e) {
            e.printStackTrace();
            UniSdkUtils.e("UniSDK Base", "saveLogToJF, AES encrypt error:".concat(String.valueOf(e)));
        }
    }

    public void saveClientLog(OrderInfo orderInfo, String str) throws JSONException {
        saveClientLog(orderInfo, str, null);
    }

    public void saveClientLog(OrderInfo orderInfo, String str, HTTPCallback hTTPCallback) throws JSONException {
        if (ClientLogUtils.isClientLogEnabled()) {
            String clientLogUrl = ClientLogUtils.getClientLogUrl();
            if (TextUtils.isEmpty(clientLogUrl)) {
                UniSdkUtils.i("UniSDK Base", "null or empty clientLogUrl");
                return;
            }
            JSONObject jSONObject = new JSONObject();
            if (!TextUtils.isEmpty(str)) {
                try {
                    jSONObject = new JSONObject(str);
                } catch (JSONException e) {
                    UniSdkUtils.e("UniSDK Base", "new JSONObject exception:" + e.getMessage());
                }
            }
            try {
                ClientLogUtils.addBasicInfo2Json(this.myCtx, jSONObject);
                ClientLogUtils.addOrderInfo2Json(orderInfo, jSONObject);
                jSONObject.put("uni_version", ((SdkBase) SdkMgr.getInst()).getUniSDKVersion());
                jSONObject.put("channel_2", getChannel());
                jSONObject.put("version_2", getSDKVersion());
                jSONObject.put("uni_version_2", getUniSDKVersion());
                UniSdkUtils.traverseJSONObject2removeIP(jSONObject);
                UniSdkUtils.d("UniSDK Base", String.format("/saveClientLog url=%s, bodyPairs=%s", clientLogUrl, jSONObject.toString()));
                if (MCountProxy.getInst().saveClientLog(this.myCtx, jSONObject.toString(), false)) {
                    if (hTTPCallback != null) {
                        hTTPCallback.processResult("{\"code\":200,\"subcode\":1,\"status\":\"ok\"}", "MCountProxy");
                        return;
                    }
                    return;
                }
                HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
                queueItemNewQueueItem.method = "POST";
                queueItemNewQueueItem.url = clientLogUrl;
                queueItemNewQueueItem.bSync = Boolean.TRUE;
                queueItemNewQueueItem.leftRetry = 0;
                queueItemNewQueueItem.setBody(jSONObject.toString());
                queueItemNewQueueItem.transParam = ConstProp.JF_CLIENT_LOG_URL;
                if (hTTPCallback != null) {
                    queueItemNewQueueItem.callback = hTTPCallback;
                }
                String propStr = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
                if (!TextUtils.isEmpty(propStr)) {
                    HashMap map = new HashMap();
                    try {
                        map.put("Gas3-Clientlog-Signature", Crypto.hmacSHA256Signature(propStr, jSONObject.toString()));
                    } catch (Exception e2) {
                        UniSdkUtils.d("UniSDK Base", "hmacSHA256Signature exception:" + e2.getMessage());
                    }
                    queueItemNewQueueItem.setHeaders(map);
                } else {
                    UniSdkUtils.d("UniSDK Base", "JF_CLIENT_KEY empty");
                }
                if (SdkMgr.getInst().hasFeature(ConstProp.ENABLE_CHANGE_LOCATION) && !hasChangeLocation) {
                    HTTPQueue.getInstance("LOG").addItemDelay(queueItemNewQueueItem, "clientlog");
                } else {
                    HTTPQueue.getInstance("LOG").addItem(queueItemNewQueueItem);
                }
            } catch (Exception e3) {
                e3.printStackTrace();
                UniSdkUtils.e("UniSDK Base", "saveClientLog fail, JSONException:" + e3.getMessage());
            }
        }
    }

    protected SharedPreferences getSharedPref() {
        return this.myCtx.getSharedPreferences("UniSDK", 0);
    }

    protected SharedPreferences getSharedPrefUniSDKServer() {
        return this.myCtx.getSharedPreferences("UniSDKServer", 0);
    }

    public void ntShowCompactView(boolean z) {
        SdkBase sdkBase = this.sdkInstMap.get("protocol");
        if (sdkBase == null) {
            UniSdkUtils.e("UniSDK Base", "no protocol channel");
        } else {
            sdkBase.showCompactView(z);
        }
    }

    protected void setJFSauth(String str, String str2, boolean z) {
        this.z.put(str, str2);
    }

    public Map<String, String> getJfSauthChannelMap() {
        return this.z;
    }

    protected void setJFPayMap(String str, String str2, boolean z) {
        this.y.put(str, str2);
    }

    public Map<String, String> getJfPaylMap(OrderInfo orderInfo) {
        return this.y;
    }

    protected void checkRealName(RealNameUpdate.RealNameUpdateListener realNameUpdateListener) {
        RealNameUpdate.b(this, realNameUpdateListener);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntOpenEchoes() {
        SDKEchoes.getInstance().ntOpenEchoes();
    }

    public boolean a(String str, JSONObject jSONObject) throws JSONException {
        boolean zExtendFuncForInner = ExtendFunc.extendFuncForInner(this, this.myCtx, str, jSONObject);
        if (zExtendFuncForInner) {
            return zExtendFuncForInner;
        }
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.w("UniSDK Base", "methodId invalid.");
            return zExtendFuncForInner;
        }
        if (!this.sdkInstMap.containsKey("pharos") && str.startsWith("pharos")) {
            UniSdkUtils.d("UniSDK Base", "ntExtendFunc \u8c03\u7528\u6bcd\u5305\u706f\u5854\u63a5\u53e3\u7c7b\u7684extendFunc");
            SDKPharos.getInstance().extendFunc(jSONObject.toString());
        } else if ("fromAiDetect".equalsIgnoreCase(str)) {
            UniSdkUtils.i("UniSDK Base", String.valueOf(jSONObject));
            if (!"aiDetect4GameLoginSuc".equalsIgnoreCase(jSONObject.optString(VideoViewManager.PROP_SRC))) {
                return zExtendFuncForInner;
            }
            f();
        } else if ("getAllChannelVersions".equalsIgnoreCase(str)) {
            try {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(com.facebook.hermes.intl.Constants.SENSITIVITY_BASE, SdkMgr.getBaseVersion());
                jSONObject2.put(SdkMgr.getInst().getChannel(), SdkMgr.getInst().getSDKVersion(SdkMgr.getInst().getChannel()));
                Iterator<String> it = this.loginSdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase sdkBase = this.loginSdkInstMap.get(it.next());
                    jSONObject2.put(sdkBase.getChannel(), sdkBase.getSDKVersion());
                }
                Iterator<String> it2 = this.sdkInstMap.keySet().iterator();
                while (it2.hasNext()) {
                    SdkBase sdkBase2 = this.sdkInstMap.get(it2.next());
                    jSONObject2.put(sdkBase2.getChannel(), sdkBase2.getSDKVersion());
                }
                jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 0);
                jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "succ");
                jSONObject.put("result", jSONObject2);
                extendFuncCall(jSONObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if ("getAppOccupiedStorage".equalsIgnoreCase(str)) {
            if (SdkMgr.getInst() == null) {
                ModulesManager.getInst().init(this.myCtx);
            }
            try {
                ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", "{\"methodId\":\"getAppOccupiedStorage\"}");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if ("BaseSupportDeviceInfo".equalsIgnoreCase(str)) {
            if (SdkMgr.getInst() == null) {
                ModulesManager.getInst().init(this.myCtx);
            }
            try {
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("methodId", "BaseSupportDeviceInfo");
                jSONObject3.put(ReportOrigin.ORIGIN_CATEGORY, jSONObject.optString(ReportOrigin.ORIGIN_CATEGORY));
                extendFuncCall(ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, "deviceInfo", jSONObject3.toString()));
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        } else {
            if (!"getAimInfo".equalsIgnoreCase(str)) {
                return zExtendFuncForInner;
            }
            a(jSONObject);
        }
        return true;
    }

    public boolean a(String str, JSONObject jSONObject, Object... objArr) {
        return ExtendFunc.extendFuncForInner(this, this.myCtx, str, jSONObject, objArr);
    }

    public void registerExtendFuncMethod(String str) {
        ExtendFunc.register(str, this);
    }

    public void a(String str, String str2) {
        HashSet<SdkBase> inst = ExtendFunc.getInst(str);
        if (inst != null && !inst.isEmpty()) {
            Iterator<SdkBase> it = inst.iterator();
            while (it.hasNext()) {
                SdkBase next = it.next();
                if (next != null) {
                    next.extendFunc(str2);
                }
            }
        }
        for (String str3 : this.loginSdkInstMap.keySet()) {
            UniSdkUtils.d("UniSDK Base", "ntExtendFunc key1=".concat(String.valueOf(str3)));
            SdkBase sdkBase = this.loginSdkInstMap.get(str3);
            if (inst == null || !inst.contains(sdkBase)) {
                if (sdkBase != null) {
                    sdkBase.extendFunc(str2);
                }
            }
        }
        for (String str4 : this.sdkInstMap.keySet()) {
            UniSdkUtils.d("UniSDK Base", "ntExtendFunc key2=".concat(String.valueOf(str4)));
            SdkBase sdkBase2 = this.sdkInstMap.get(str4);
            if (inst == null || !inst.contains(sdkBase2)) {
                if (sdkBase2 != null) {
                    sdkBase2.extendFunc(str2);
                }
            }
        }
        if (inst == null || !inst.contains(this)) {
            extendFunc(str2);
        }
    }

    public void a(String str, String str2, Object... objArr) {
        HashSet<SdkBase> inst = ExtendFunc.getInst(str);
        if (inst != null && !inst.isEmpty()) {
            Iterator<SdkBase> it = inst.iterator();
            while (it.hasNext()) {
                SdkBase next = it.next();
                if (next != null) {
                    next.extendFunc(str2, objArr);
                }
            }
        }
        for (String str3 : this.loginSdkInstMap.keySet()) {
            UniSdkUtils.d("UniSDK Base", "ntExtendFunc key1=".concat(String.valueOf(str3)));
            SdkBase sdkBase = this.loginSdkInstMap.get(str3);
            if (inst == null || !inst.contains(sdkBase)) {
                if (sdkBase != null) {
                    sdkBase.extendFunc(str2, objArr);
                }
            }
        }
        for (String str4 : this.sdkInstMap.keySet()) {
            UniSdkUtils.d("UniSDK Base", "ntExtendFunc key2=".concat(String.valueOf(str4)));
            SdkBase sdkBase2 = this.sdkInstMap.get(str4);
            if (inst == null || !inst.contains(sdkBase2)) {
                if (sdkBase2 != null) {
                    sdkBase2.extendFunc(str2, objArr);
                }
            }
        }
        if (inst == null || !inst.contains(this)) {
            extendFunc(str2, objArr);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntExtendFunc(String str) {
        if (this.myCtx == null) {
            UniSdkUtils.d("UniSDK Base", "call ntExtendFunc(json), myCtx null");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            String strOptString2 = jSONObject.optString("moduleName");
            if (!TextUtils.isEmpty(strOptString2) && ModulesManager.getInst().hasModule(strOptString2)) {
                ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.172

                    /* renamed from: a */
                    final /* synthetic */ long f1703a;
                    final /* synthetic */ String b;
                    final /* synthetic */ JSONObject d;
                    final /* synthetic */ String e;

                    AnonymousClass172(long j, String strOptString22, String str2, JSONObject jSONObject2) {
                        j = j;
                        str = strOptString22;
                        str = str2;
                        jSONObject = jSONObject2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                            UniSdkUtils.d("UniSDK Base", "call ModulesManager extendFunc");
                            SdkBase.a(SdkBase.this, jSONObject, ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, str, str));
                        } catch (Exception e) {
                            UniSdkUtils.e("UniSDK Base", "ModulesManager act.runOnUiThread Exception:" + e.getMessage());
                        }
                    }
                });
                return;
            }
            boolean zOptBoolean = jSONObject2.optBoolean("notMainThread", false);
            UniSdkUtils.d("UniSDK Base", "ntExtendFunc methodId:".concat(String.valueOf(strOptString)));
            long id = Thread.currentThread().getId();
            if (zOptBoolean) {
                UniSdkUtils.d("UniSDK Base", "cur thread:".concat(String.valueOf(id)));
                if (a(strOptString, jSONObject2)) {
                    return;
                }
                a(strOptString, str2);
                return;
            }
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.173

                /* renamed from: a */
                final /* synthetic */ long f1704a;
                final /* synthetic */ String c;
                final /* synthetic */ String d;
                final /* synthetic */ JSONObject f;

                AnonymousClass173(long id2, String strOptString3, JSONObject jSONObject2, String str2) {
                    j = id2;
                    str = strOptString3;
                    jSONObject = jSONObject2;
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    try {
                        UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                        if (SdkBase.this.a(str, jSONObject)) {
                            return;
                        }
                        SdkBase.this.a(str, str);
                    } catch (Exception e) {
                        UniSdkUtils.e("UniSDK Base", "sdkbase act.runOnUiThread Exception:" + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            UniSdkUtils.e("UniSDK Base", "ntExtendFunc Exception:" + e.getMessage());
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$172 */
    final class AnonymousClass172 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ long f1703a;
        final /* synthetic */ String b;
        final /* synthetic */ JSONObject d;
        final /* synthetic */ String e;

        AnonymousClass172(long j, String strOptString22, String str2, JSONObject jSONObject2) {
            j = j;
            str = strOptString22;
            str = str2;
            jSONObject = jSONObject2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                UniSdkUtils.d("UniSDK Base", "call ModulesManager extendFunc");
                SdkBase.a(SdkBase.this, jSONObject, ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, str, str));
            } catch (Exception e) {
                UniSdkUtils.e("UniSDK Base", "ModulesManager act.runOnUiThread Exception:" + e.getMessage());
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$173 */
    final class AnonymousClass173 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ long f1704a;
        final /* synthetic */ String c;
        final /* synthetic */ String d;
        final /* synthetic */ JSONObject f;

        AnonymousClass173(long id2, String strOptString3, JSONObject jSONObject2, String str2) {
            j = id2;
            str = strOptString3;
            jSONObject = jSONObject2;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                if (SdkBase.this.a(str, jSONObject)) {
                    return;
                }
                SdkBase.this.a(str, str);
            } catch (Exception e) {
                UniSdkUtils.e("UniSDK Base", "sdkbase act.runOnUiThread Exception:" + e.getMessage());
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntExtendFunc(String str, Object... objArr) {
        if (this.myCtx == null) {
            UniSdkUtils.d("UniSDK Base", "call ntExtendFunc(json, objects), myCtx null");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("methodId");
            String strOptString2 = jSONObject.optString("moduleName");
            if (!TextUtils.isEmpty(strOptString2) && ModulesManager.getInst().hasModule(strOptString2)) {
                ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.174

                    /* renamed from: a */
                    final /* synthetic */ long f1705a;
                    final /* synthetic */ String c;
                    final /* synthetic */ JSONObject d;
                    final /* synthetic */ Object[] e;
                    final /* synthetic */ String f;

                    AnonymousClass174(long j, String strOptString22, String str2, Object[] objArr2, JSONObject jSONObject2) {
                        j = j;
                        str = strOptString22;
                        str = str2;
                        objArr = objArr2;
                        jSONObject = jSONObject2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        try {
                            UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                            UniSdkUtils.d("UniSDK Base", "call ModulesManager extendFunc(json,objects)");
                            SdkBase.a(SdkBase.this, jSONObject, ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, str, str, objArr));
                        } catch (Exception e) {
                            UniSdkUtils.e("UniSDK Base", "ModulesManager act.runOnUiThread Exception:" + e.getMessage());
                        }
                    }
                });
                return;
            }
            boolean zOptBoolean = jSONObject2.optBoolean("notMainThread", false);
            UniSdkUtils.d("UniSDK Base", "ntExtendFunc(json,objects) methodId:".concat(String.valueOf(strOptString)));
            long id = Thread.currentThread().getId();
            if (zOptBoolean) {
                UniSdkUtils.d("UniSDK Base", "cur thread:".concat(String.valueOf(id)));
                if (a(strOptString, jSONObject2, objArr2)) {
                    return;
                }
                a(strOptString, str2, objArr2);
                return;
            }
            ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.175

                /* renamed from: a */
                final /* synthetic */ String f1706a;
                final /* synthetic */ long c;
                final /* synthetic */ String d;
                final /* synthetic */ JSONObject e;
                final /* synthetic */ Object[] f;

                AnonymousClass175(long id2, String strOptString3, JSONObject jSONObject2, Object[] objArr2, String str2) {
                    j = id2;
                    str = strOptString3;
                    jSONObject = jSONObject2;
                    objArr = objArr2;
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    try {
                        UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                        if (SdkBase.this.a(str, jSONObject, objArr)) {
                            return;
                        }
                        SdkBase.this.a(str, str, objArr);
                    } catch (Exception e) {
                        UniSdkUtils.e("UniSDK Base", "sdkbase act.runOnUiThread Exception:" + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            UniSdkUtils.d("UniSDK Base", "ntExtendFunc(json,objects) Exception:" + e.getMessage());
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$174 */
    final class AnonymousClass174 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ long f1705a;
        final /* synthetic */ String c;
        final /* synthetic */ JSONObject d;
        final /* synthetic */ Object[] e;
        final /* synthetic */ String f;

        AnonymousClass174(long j, String strOptString22, String str2, Object[] objArr2, JSONObject jSONObject2) {
            j = j;
            str = strOptString22;
            str = str2;
            objArr = objArr2;
            jSONObject = jSONObject2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                UniSdkUtils.d("UniSDK Base", "call ModulesManager extendFunc(json,objects)");
                SdkBase.a(SdkBase.this, jSONObject, ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, str, str, objArr));
            } catch (Exception e) {
                UniSdkUtils.e("UniSDK Base", "ModulesManager act.runOnUiThread Exception:" + e.getMessage());
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$175 */
    final class AnonymousClass175 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1706a;
        final /* synthetic */ long c;
        final /* synthetic */ String d;
        final /* synthetic */ JSONObject e;
        final /* synthetic */ Object[] f;

        AnonymousClass175(long id2, String strOptString3, JSONObject jSONObject2, Object[] objArr2, String str2) {
            j = id2;
            str = strOptString3;
            jSONObject = jSONObject2;
            objArr = objArr2;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            try {
                UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                if (SdkBase.this.a(str, jSONObject, objArr)) {
                    return;
                }
                SdkBase.this.a(str, str, objArr);
            } catch (Exception e) {
                UniSdkUtils.e("UniSDK Base", "sdkbase act.runOnUiThread Exception:" + e.getMessage());
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setExtendFuncListener(OnExtendFuncListener onExtendFuncListener, int i) throws JSONException {
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            this.sdkInstMap.get(it.next()).setExtendFuncListener(onExtendFuncListener, i);
        }
        Iterator<String> it2 = this.loginSdkInstMap.keySet().iterator();
        while (it2.hasNext()) {
            this.loginSdkInstMap.get(it2.next()).setExtendFuncListener(onExtendFuncListener, i);
        }
        this.s = onExtendFuncListener;
        setPropInt(ConstProp.EXTEND_FUNC_CALLER_THREAD, i);
    }

    public void extendFuncCall(String str) {
        if (!this.I) {
            synchronized (this.J) {
                this.J.add(str);
            }
            return;
        }
        if (this.s == null) {
            UniSdkUtils.e2("UniSDK Base", "extendFuncListener is null");
            return;
        }
        if (getPropInt(ConstProp.EXTEND_FUNC_CALLER_THREAD, 1) == 2) {
            runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.176

                /* renamed from: a */
                final /* synthetic */ String f1707a;

                AnonymousClass176(String str2) {
                    str = str2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    UniSdkUtils.d2("UniSDK Base", "runOnGLThread, onExtendFuncCall: json=" + str + ", current thread=" + Thread.currentThread().getId());
                    if (SdkBase.this.s != null) {
                        SdkBase.this.s.onExtendFuncCall(str);
                    }
                }
            });
            return;
        }
        UniSdkUtils.d2("UniSDK Base", "runOnUIThread/runOnCallerThread, onExtendFuncCall: json=" + str2 + ", current thread=" + Thread.currentThread().getId());
        OnExtendFuncListener onExtendFuncListener = this.s;
        if (onExtendFuncListener != null) {
            onExtendFuncListener.onExtendFuncCall(str2);
        }
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$176 */
    final class AnonymousClass176 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ String f1707a;

        AnonymousClass176(String str2) {
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.d2("UniSDK Base", "runOnGLThread, onExtendFuncCall: json=" + str + ", current thread=" + Thread.currentThread().getId());
            if (SdkBase.this.s != null) {
                SdkBase.this.s.onExtendFuncCall(str);
            }
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntRemoveCheckedOrders(String str) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.177

            /* renamed from: a */
            final /* synthetic */ long f1708a;
            final /* synthetic */ String b;

            AnonymousClass177(long j, String str2) {
                j = j;
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).removeCheckedOrders(str);
                }
                SdkBase.this.removeCheckedOrders(str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$177 */
    final class AnonymousClass177 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ long f1708a;
        final /* synthetic */ String b;

        AnonymousClass177(long j, String str2) {
            j = j;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).removeCheckedOrders(str);
            }
            SdkBase.this.removeCheckedOrders(str);
        }
    }

    public List<OrderInfo> getCheckedOrders() {
        return new ArrayList();
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public List<OrderInfo> ntGetCheckedOrders() {
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = this.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            List<OrderInfo> checkedOrders = this.sdkInstMap.get(it.next()).getCheckedOrders();
            if (checkedOrders != null && !checkedOrders.isEmpty()) {
                arrayList.addAll(checkedOrders);
            }
        }
        List<OrderInfo> checkedOrders2 = getCheckedOrders();
        if (checkedOrders2 != null && !checkedOrders2.isEmpty()) {
            arrayList.addAll(checkedOrders2);
        }
        UniSdkUtils.d("UniSDK Base", "ntGetCheckedOrders size:" + arrayList.size());
        return arrayList;
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCreateQRCode(String str, int i, int i2, String str2) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.178

            /* renamed from: a */
            final /* synthetic */ long f1709a;
            final /* synthetic */ String b;
            final /* synthetic */ int c;
            final /* synthetic */ int d;
            final /* synthetic */ String e;

            AnonymousClass178(long j, String str3, int i3, int i22, String str22) {
                j = j;
                str = str3;
                i = i3;
                i = i22;
                str = str22;
            }

            @Override // java.lang.Runnable
            public final void run() {
                UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).createQRCode(str, i, i, str);
                }
                SdkBase.this.createQRCode(str, i, i, str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$178 */
    final class AnonymousClass178 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ long f1709a;
        final /* synthetic */ String b;
        final /* synthetic */ int c;
        final /* synthetic */ int d;
        final /* synthetic */ String e;

        AnonymousClass178(long j, String str3, int i3, int i22, String str22) {
            j = j;
            str = str3;
            i = i3;
            i = i22;
            str = str22;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).createQRCode(str, i, i, str);
            }
            SdkBase.this.createQRCode(str, i, i, str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntCreateQRCode(String str, int i, int i2, String str2, String str3) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.179

            /* renamed from: a */
            final /* synthetic */ long f1710a;
            final /* synthetic */ String b;
            final /* synthetic */ int d;
            final /* synthetic */ int e;
            final /* synthetic */ String f;
            final /* synthetic */ String g;

            AnonymousClass179(long j, String str4, int i3, int i22, String str22, String str32) {
                j = j;
                str = str4;
                i = i3;
                i = i22;
                str = str22;
                str = str32;
            }

            @Override // java.lang.Runnable
            public final void run() {
                UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).createQRCode(str, i, i, str, str);
                }
                SdkBase.this.createQRCode(str, i, i, str, str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$179 */
    final class AnonymousClass179 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ long f1710a;
        final /* synthetic */ String b;
        final /* synthetic */ int d;
        final /* synthetic */ int e;
        final /* synthetic */ String f;
        final /* synthetic */ String g;

        AnonymousClass179(long j, String str4, int i3, int i22, String str22, String str32) {
            j = j;
            str = str4;
            i = i3;
            i = i22;
            str = str22;
            str = str32;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).createQRCode(str, i, i, str, str);
            }
            SdkBase.this.createQRCode(str, i, i, str, str);
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void ntScannerQRCode(String str) {
        ((Activity) this.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.180

            /* renamed from: a */
            final /* synthetic */ long f1712a;
            final /* synthetic */ String c;

            AnonymousClass180(long j, String str2) {
                j = j;
                str = str2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
                Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    SdkBase.this.sdkInstMap.get(it.next()).scannerQRCode(str);
                }
                SdkBase.this.scannerQRCode(str);
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$180 */
    final class AnonymousClass180 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ long f1712a;
        final /* synthetic */ String c;

        AnonymousClass180(long j, String str2) {
            j = j;
            str = str2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.d("UniSDK Base", "cur thread:" + j + ",ui thread:" + Thread.currentThread().getId());
            Iterator<String> it = SdkBase.this.sdkInstMap.keySet().iterator();
            while (it.hasNext()) {
                SdkBase.this.sdkInstMap.get(it.next()).scannerQRCode(str);
            }
            SdkBase.this.scannerQRCode(str);
        }
    }

    public synchronized void h() {
        String strA = UniSdkUtils.a(this.myCtx, 0, 0);
        String strA2 = UniSdkUtils.a(this.myCtx, 0, 1);
        UniSdkUtils.a(this.myCtx, 1, 0);
        if (strA.equalsIgnoreCase(strA2) && !"0".equalsIgnoreCase(strA)) {
            UniSdkUtils.e("UniSDK Base", "a terrible step error occur!!!");
        }
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public void setOnProtocolFinishListener(OnProtocolFinishListener onProtocolFinishListener, int i) throws JSONException {
        this.B = onProtocolFinishListener;
        setPropInt(ConstProp.PROTOCOL_FINISH_CALLER_THREAD, i);
    }

    public void protocolFinish(int i) {
        if (this.B != null) {
            if (getPropInt(ConstProp.PROTOCOL_FINISH_CALLER_THREAD, 1) == 2) {
                runOnGLThread(new Runnable() { // from class: com.netease.ntunisdk.base.SdkBase.181

                    /* renamed from: a */
                    final /* synthetic */ int f1713a;

                    AnonymousClass181(int i2) {
                        i = i2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        UniSdkUtils.i("UniSDK Base", "runOnGLThread, protocolFinish: code=" + i + ", current thread=" + Thread.currentThread().getId());
                        SdkBase.this.B.onProtocolFinish(i);
                    }
                });
                return;
            }
            UniSdkUtils.i("UniSDK Base", "runOnUIThread, protocolFinish: code=" + i2 + ", current thread=" + Thread.currentThread().getId());
            this.B.onProtocolFinish(i2);
            return;
        }
        UniSdkUtils.e("UniSDK Base", "OnProtocolFinishListener not set");
    }

    /* renamed from: com.netease.ntunisdk.base.SdkBase$181 */
    final class AnonymousClass181 implements Runnable {

        /* renamed from: a */
        final /* synthetic */ int f1713a;

        AnonymousClass181(int i2) {
            i = i2;
        }

        @Override // java.lang.Runnable
        public final void run() {
            UniSdkUtils.i("UniSDK Base", "runOnGLThread, protocolFinish: code=" + i + ", current thread=" + Thread.currentThread().getId());
            SdkBase.this.B.onProtocolFinish(i);
        }
    }

    private boolean d(String str) {
        if (!hasGuestLogined()) {
            return false;
        }
        guestNotAllowCallback(str);
        return true;
    }

    protected boolean hasGuestLogined() {
        return SdkMgr.getInst().hasFeature(ConstProp.UNISDK_GUEST_LOGIN_STATE);
    }

    protected void guestNotAllowCallback(String str) {
        extendFuncCall("{\"methodId\": \"guestNotAllow\", \"from\":\"" + str + "\"}");
    }

    protected boolean useNewSdkProcedure() {
        return 1 == getPropInt(ConstProp.ENABLE_CLIENT_CHECK_REALNAME, 0);
    }

    @Override // com.netease.ntunisdk.base.GamerInterface
    public String ntModulesExtendFunc(String str, String str2) {
        try {
        } catch (Exception e) {
            UniSdkUtils.d("UniSDK Base", "ntModulesExtendFunc Exception:" + e.getMessage());
        }
        if (this.myCtx == null) {
            UniSdkUtils.d("UniSDK Base", "call ntModulesExtendFunc(moduleName,json), myCtx null");
            return "";
        }
        if (SdkMgr.getInst() == null) {
            ModulesManager.getInst().init(this.myCtx);
        }
        if (!TextUtils.isEmpty(str) && ModulesManager.getInst().hasModule(str)) {
            UniSdkUtils.d("UniSDK Base", "cur thread:" + Thread.currentThread().getId() + ",ui thread:" + Thread.currentThread().getId());
            UniSdkUtils.d("UniSDK Base", "call ModulesManager extendFunc");
            String strExtendFunc = ModulesManager.getInst().extendFunc(ConstProp.UNISDKBASE, str, str2);
            UniSdkUtils.d("UniSDK Base", "modulesManager extendFunc sync callback\uff1a".concat(String.valueOf(strExtendFunc)));
            return strExtendFunc;
        }
        UniSdkUtils.d("UniSDK Base", "ntModulesExtendFunc error: " + str + " is not found");
        return "";
    }

    static /* synthetic */ void b(SdkBase sdkBase, String str) {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if ("getAppSetID".equalsIgnoreCase(jSONObject.optString("methodId")) && jSONObject.has(ResIdReader.RES_TYPE_ID) && jSONObject.has(Constants.PARAM_SCOPE)) {
                String strOptString = jSONObject.optString(ResIdReader.RES_TYPE_ID, "unknown");
                String strOptString2 = jSONObject.optString(Constants.PARAM_SCOPE, "unknown");
                sdkBase.setPropStr(ConstProp.UNISDK_APPSET_ID, strOptString);
                sdkBase.setPropStr(ConstProp.UNISDK_APPSET_ID_SCOPE, strOptString2);
                for (SdkBase sdkBase2 : sdkBase.getLoginSdkInstMap().values()) {
                    sdkBase2.setPropStr(ConstProp.UNISDK_APPSET_ID, strOptString);
                    sdkBase2.setPropStr(ConstProp.UNISDK_APPSET_ID_SCOPE, strOptString2);
                }
                for (SdkBase sdkBase3 : sdkBase.getSdkInstMap().values()) {
                    sdkBase3.setPropStr(ConstProp.UNISDK_APPSET_ID, strOptString);
                    sdkBase3.setPropStr(ConstProp.UNISDK_APPSET_ID_SCOPE, strOptString2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static /* synthetic */ void a(SdkBase sdkBase, OnFinishInitListener onFinishInitListener, int i) throws Throwable {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "onInitDone_base");
            jSONObject.putOpt("unisdk_code", Integer.toString(i));
        } catch (JSONException e) {
            UniSdkUtils.d("UniSDK Base", "extraJson:" + e.getMessage());
        }
        sdkBase.saveClientLog(null, jSONObject.toString());
        onFinishInitListener.finishInit(i);
        try {
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.putOpt("methodId", "sdkInitFinish");
            jSONObject2.putOpt("code", Integer.valueOf(i));
            SdkMgr.getInst().ntExtendFunc(jSONObject2.toString());
        } catch (Exception unused) {
        }
        String propStr = sdkBase.getPropStr(ConstProp.DEEP_LINK_FROM_LAUNCH);
        UniSdkUtils.i("UniSDK Base", "deeplinkCallbackFromLaunch: ".concat(String.valueOf(propStr)));
        if (!TextUtils.isEmpty(propStr)) {
            sdkBase.extendFuncCall(propStr);
        }
        sdkBase.R.set(-1);
        SdkMgr.getInst().setPropStr(ConstProp.ORIGIN_CHANNEL, SdkMgr.getInst().getChannel());
    }

    static /* synthetic */ void v(SdkBase sdkBase) throws Throwable {
        String propStr = sdkBase.getPropStr(ConstProp.UNISDK_LOGIN_JSON);
        sdkBase.setBackSauthLoginJson(propStr);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(ConstProp.UNISDK_LOGIN_JSON, propStr);
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "ntGameLoginSuccess_sauthCallback");
        } catch (JSONException e) {
            UniSdkUtils.d("UniSDK Base", "extraJson:" + e.getMessage());
        }
        sdkBase.saveClientLog(null, jSONObject.toString());
    }

    static /* synthetic */ void u(SdkBase sdkBase) {
        if (SdkMgr.getInst().hasFeature(ConstProp.UNISDK_JF_GAS3) || SdkMgr.getInst().hasFeature(ConstProp.UNISDK_JF_GAS3_WEB)) {
            new JfGas(sdkBase).queryProduct(null);
        }
        if (sdkBase.sdkInstMap.get("allysdk") != null && sdkBase.K == null) {
            PayChannelManager payChannelManager = new PayChannelManager(sdkBase);
            sdkBase.K = payChannelManager;
            payChannelManager.initAsync();
        }
        Iterator<String> it = sdkBase.sdkInstMap.keySet().iterator();
        while (it.hasNext()) {
            sdkBase.sdkInstMap.get(it.next()).queryInventory();
        }
        sdkBase.queryInventory();
    }

    static /* synthetic */ void a(SdkBase sdkBase, JSONObject jSONObject, String str) throws JSONException {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        UniSdkUtils.d("UniSDK Base", "modulesManager extendFunc sync callback\uff1a".concat(String.valueOf(str)));
        try {
            try {
                new JSONObject(str);
                sdkBase.extendFuncCall(str);
            } catch (Exception e) {
                UniSdkUtils.d("UniSDK Base", "ModulesManager extendFuncCall exception:" + e.toString());
            }
        } catch (Exception unused) {
            jSONObject.put(RespUtil.UniSdkField.RESP_CODE, 0);
            jSONObject.put(RespUtil.UniSdkField.RESP_MSG, "succ");
            jSONObject.put("result", str);
            sdkBase.extendFuncCall(jSONObject.toString());
        }
    }
}