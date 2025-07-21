package com.netease.ntunisdk.external.protocol;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.netease.ntunisdk.external.protocol.data.Config;
import com.netease.ntunisdk.external.protocol.data.ProtocolFile;
import com.netease.ntunisdk.external.protocol.data.ProtocolInfo;
import com.netease.ntunisdk.external.protocol.data.ProtocolProp;
import com.netease.ntunisdk.external.protocol.data.ProtocolProvider;
import com.netease.ntunisdk.external.protocol.data.SDKRuntime;
import com.netease.ntunisdk.external.protocol.data.Store;
import com.netease.ntunisdk.external.protocol.data.User;
import com.netease.ntunisdk.external.protocol.plugins.PluginManager;
import com.netease.ntunisdk.external.protocol.plugins.Result;
import com.netease.ntunisdk.external.protocol.utils.AsyncTask;
import com.netease.ntunisdk.external.protocol.utils.CommonUtils;
import com.netease.ntunisdk.external.protocol.utils.FetchProtocolException;
import com.netease.ntunisdk.external.protocol.utils.FileUtil;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.ResUtils;
import com.netease.ntunisdk.external.protocol.utils.SysHelper;
import com.netease.ntunisdk.external.protocol.utils.TextCompat;
import com.netease.ntunisdk.external.protocol.view.AlerterEx;
import com.netease.ntunisdk.external.protocol.view.ContentDialog;
import com.netease.ntunisdk.external.protocol.view.OnBackPressedListener;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.unilogger.global.Const;
import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ProtocolManager {
    private static final int NO_NEED_SHOW = 102;
    private static final int RETRY = 100;
    private static final int SHOW = 101;
    private static final String TAG = "M";
    private static final int UNKNOWN = -1;
    private static volatile ProtocolManager instance;
    boolean hasSetTaskAffinity;
    private SoftReference<Activity> mActivityReference;
    private AlerterEx mAlerterEx;
    private Context mApplicationContext;
    ArrayList<ProtocolInfo.ConcreteSubProtocol> mConcreteSubProtocols;
    private ContentDialog mContentDialog;
    private User mCurrentUser;
    boolean mHasAcceptProtocolByLauncher;
    private Situation mLastSituation;
    private int mLastViewStyle;
    private ProtocolProp mProp;
    private String mUserId;
    private int screenHeightDp;
    private int screenWidthDp;
    int activityTaskId = -1;
    private boolean isUniSdkRunning = false;
    private boolean isProtocolDialogReShow = false;
    private boolean mHideWebViewLogo = false;
    private boolean hasInit = false;
    private boolean isShowing = false;
    private final ProtocolProvider mProvider = new ProtocolProvider();
    private UniSDKProxy mUniSDKProxy = new UniSDKProxy();
    private final CopyOnWriteArrayList<ProtocolCallback> mConcreteCallbacks = new CopyOnWriteArrayList<>();
    private final ExtendProtocolCallback mDefaultCallback = new ExtendProtocolCallback() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.1
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.external.protocol.ExtendProtocolCallback
        public void onFinish(int i, JSONObject jSONObject) {
            ProtocolManager.this.isShowing = false;
            StringBuilder sb = new StringBuilder();
            sb.append("onFinish:");
            sb.append(i);
            sb.append(", raw:");
            sb.append(jSONObject != null ? jSONObject.toString() : "null");
            L.d(ProtocolManager.TAG, sb.toString());
            if (i == 2) {
                Iterator it = ProtocolManager.this.mConcreteCallbacks.iterator();
                while (it.hasNext()) {
                    ProtocolCallback protocolCallback = (ProtocolCallback) it.next();
                    if (protocolCallback == null) {
                        it.remove();
                    } else if (protocolCallback instanceof ExtendProtocolCallback) {
                        ((ExtendProtocolCallback) protocolCallback).onFinish(i, jSONObject);
                    } else {
                        protocolCallback.onFinish(i);
                    }
                }
            } else {
                if (!ProtocolManager.this.mProvider.hasAcceptLaunchProtocol() && (i == 1 || i == 4 || i == 3)) {
                    ProtocolManager.this.mProvider.setAcceptLaunchProtocol();
                }
                Iterator it2 = ProtocolManager.this.mConcreteCallbacks.iterator();
                while (it2.hasNext()) {
                    ProtocolCallback protocolCallback2 = (ProtocolCallback) it2.next();
                    if (protocolCallback2 == null) {
                        it2.remove();
                    } else if (protocolCallback2 instanceof ExtendProtocolCallback) {
                        ((ExtendProtocolCallback) protocolCallback2).onFinish(i, jSONObject);
                    } else {
                        protocolCallback2.onFinish(i);
                    }
                }
            }
            SDKRuntime.getInstance().setProtocolShowing(false);
        }

        @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
        public void onFinish(int i) {
            ProtocolManager.this.isShowing = false;
            if (i == 2) {
                Iterator it = ProtocolManager.this.mConcreteCallbacks.iterator();
                while (it.hasNext()) {
                    ProtocolCallback protocolCallback = (ProtocolCallback) it.next();
                    if (protocolCallback == null) {
                        it.remove();
                    } else {
                        protocolCallback.onFinish(i);
                    }
                }
            } else {
                if (!ProtocolManager.this.mProvider.hasAcceptLaunchProtocol()) {
                    ProtocolManager.this.mProvider.setAcceptLaunchProtocol();
                }
                Iterator it2 = ProtocolManager.this.mConcreteCallbacks.iterator();
                while (it2.hasNext()) {
                    ProtocolCallback protocolCallback2 = (ProtocolCallback) it2.next();
                    if (protocolCallback2 == null) {
                        it2.remove();
                    } else {
                        protocolCallback2.onFinish(i);
                    }
                }
            }
            SDKRuntime.getInstance().setProtocolShowing(false);
        }

        @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
        public void onOpen() {
            Iterator it = ProtocolManager.this.mConcreteCallbacks.iterator();
            while (it.hasNext()) {
                ProtocolCallback protocolCallback = (ProtocolCallback) it.next();
                if (protocolCallback == null) {
                    it.remove();
                } else {
                    protocolCallback.onOpen();
                }
            }
        }
    };

    public interface OnProtocolCallback {
        void onFinish(Situation situation, ProtocolResult protocolResult);
    }

    public static abstract class ProtocolCallable<V> implements Callable<V> {
        ProtocolInfo mProtocolInfo;
        protected Situation mSituation;
    }

    public String getVersion() {
        return Const.VERSION;
    }

    private ProtocolManager() {
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$1 */
    class AnonymousClass1 implements ExtendProtocolCallback {
        AnonymousClass1() {
        }

        @Override // com.netease.ntunisdk.external.protocol.ExtendProtocolCallback
        public void onFinish(int i, JSONObject jSONObject) {
            ProtocolManager.this.isShowing = false;
            StringBuilder sb = new StringBuilder();
            sb.append("onFinish:");
            sb.append(i);
            sb.append(", raw:");
            sb.append(jSONObject != null ? jSONObject.toString() : "null");
            L.d(ProtocolManager.TAG, sb.toString());
            if (i == 2) {
                Iterator it = ProtocolManager.this.mConcreteCallbacks.iterator();
                while (it.hasNext()) {
                    ProtocolCallback protocolCallback = (ProtocolCallback) it.next();
                    if (protocolCallback == null) {
                        it.remove();
                    } else if (protocolCallback instanceof ExtendProtocolCallback) {
                        ((ExtendProtocolCallback) protocolCallback).onFinish(i, jSONObject);
                    } else {
                        protocolCallback.onFinish(i);
                    }
                }
            } else {
                if (!ProtocolManager.this.mProvider.hasAcceptLaunchProtocol() && (i == 1 || i == 4 || i == 3)) {
                    ProtocolManager.this.mProvider.setAcceptLaunchProtocol();
                }
                Iterator it2 = ProtocolManager.this.mConcreteCallbacks.iterator();
                while (it2.hasNext()) {
                    ProtocolCallback protocolCallback2 = (ProtocolCallback) it2.next();
                    if (protocolCallback2 == null) {
                        it2.remove();
                    } else if (protocolCallback2 instanceof ExtendProtocolCallback) {
                        ((ExtendProtocolCallback) protocolCallback2).onFinish(i, jSONObject);
                    } else {
                        protocolCallback2.onFinish(i);
                    }
                }
            }
            SDKRuntime.getInstance().setProtocolShowing(false);
        }

        @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
        public void onFinish(int i) {
            ProtocolManager.this.isShowing = false;
            if (i == 2) {
                Iterator it = ProtocolManager.this.mConcreteCallbacks.iterator();
                while (it.hasNext()) {
                    ProtocolCallback protocolCallback = (ProtocolCallback) it.next();
                    if (protocolCallback == null) {
                        it.remove();
                    } else {
                        protocolCallback.onFinish(i);
                    }
                }
            } else {
                if (!ProtocolManager.this.mProvider.hasAcceptLaunchProtocol()) {
                    ProtocolManager.this.mProvider.setAcceptLaunchProtocol();
                }
                Iterator it2 = ProtocolManager.this.mConcreteCallbacks.iterator();
                while (it2.hasNext()) {
                    ProtocolCallback protocolCallback2 = (ProtocolCallback) it2.next();
                    if (protocolCallback2 == null) {
                        it2.remove();
                    } else {
                        protocolCallback2.onFinish(i);
                    }
                }
            }
            SDKRuntime.getInstance().setProtocolShowing(false);
        }

        @Override // com.netease.ntunisdk.external.protocol.ProtocolCallback
        public void onOpen() {
            Iterator it = ProtocolManager.this.mConcreteCallbacks.iterator();
            while (it.hasNext()) {
                ProtocolCallback protocolCallback = (ProtocolCallback) it.next();
                if (protocolCallback == null) {
                    it.remove();
                } else {
                    protocolCallback.onOpen();
                }
            }
        }
    }

    public static ProtocolManager getInstance() {
        if (instance == null) {
            synchronized (ProtocolManager.class) {
                if (instance == null) {
                    instance = new ProtocolManager();
                }
            }
        }
        return instance;
    }

    public boolean isHideWebViewLogo() {
        return this.mHideWebViewLogo;
    }

    public void setHideWebViewLogo(boolean z) {
        this.mHideWebViewLogo = z;
    }

    public synchronized void init(Activity activity) {
        if (activity == null) {
            return;
        }
        if (!(activity instanceof ProtocolLauncher)) {
            this.activityTaskId = activity.getTaskId();
            this.hasSetTaskAffinity = CommonUtils.hasSetTaskAffinity(activity);
        }
        if (this.hasInit) {
            return;
        }
        Context applicationContext = activity.getApplicationContext();
        this.mApplicationContext = applicationContext;
        this.mProvider.init(applicationContext);
        this.hasInit = true;
    }

    public ProtocolProp getProp() {
        return this.mProp;
    }

    public void setProp(ProtocolProp protocolProp) {
        L.d(TAG, "setProp");
        this.mProp = protocolProp;
        this.mProvider.setProp(protocolProp);
    }

    public void setUniSDKProxy(UniSDKProxy uniSDKProxy) {
        if (uniSDKProxy != null) {
            this.mUniSDKProxy = uniSDKProxy;
        }
    }

    public ExtendProtocolCallback getCallback() {
        return this.mDefaultCallback;
    }

    public final void setCallback(ProtocolCallback protocolCallback) {
        if (protocolCallback == null || this.mConcreteCallbacks.contains(protocolCallback)) {
            return;
        }
        this.mConcreteCallbacks.add(protocolCallback);
    }

    public final void removeCallback(ProtocolCallback protocolCallback) {
        if (protocolCallback == null) {
            return;
        }
        this.mConcreteCallbacks.remove(protocolCallback);
    }

    public ProtocolInfo getCurrentBaseProtocol() {
        ProtocolProvider protocolProvider = this.mProvider;
        if (protocolProvider == null) {
            return null;
        }
        return protocolProvider.getBaseProtocol();
    }

    public int getProtocolType() {
        String url = this.mProp.getUrl();
        if (TextUtils.isEmpty(url)) {
            return 0;
        }
        if (url.endsWith("latest_v36.tw.json")) {
            return 1;
        }
        return url.endsWith("latest_v39.tw.json") ? 2 : 0;
    }

    public void handleOnConfigurationChanged(Activity activity, Configuration configuration) {
        L.d(TAG, "handleOnConfigurationChanged");
        if (configuration.screenWidthDp == this.screenWidthDp && configuration.screenHeightDp == this.screenHeightDp) {
            return;
        }
        this.screenWidthDp = configuration.screenWidthDp;
        this.screenHeightDp = configuration.screenHeightDp;
        if (this.mContentDialog == null || !SDKRuntime.getInstance().isProtocolShowing()) {
            return;
        }
        this.mContentDialog.onConfigurationChanged(activity);
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$2 */
    class AnonymousClass2 implements OnBackPressedListener {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ boolean val$isForceQuit;

        AnonymousClass2(boolean z, Activity activity) {
            z = z;
            activity = activity;
        }

        @Override // com.netease.ntunisdk.external.protocol.view.OnBackPressedListener
        public void onBackPressed() {
            if (z) {
                ProtocolManager.this.getCallback().onFinish(2);
                PluginManager.getInstance().exit(activity, new com.netease.ntunisdk.external.protocol.plugins.Callback() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.2.1
                    AnonymousClass1() {
                    }

                    @Override // com.netease.ntunisdk.external.protocol.plugins.Callback
                    public void onFinish(Result result) {
                        SysHelper.exitProcessInLaunch(activity);
                    }
                });
            }
        }

        /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$2$1 */
        class AnonymousClass1 implements com.netease.ntunisdk.external.protocol.plugins.Callback {
            AnonymousClass1() {
            }

            @Override // com.netease.ntunisdk.external.protocol.plugins.Callback
            public void onFinish(Result result) {
                SysHelper.exitProcessInLaunch(activity);
            }
        }
    }

    public void showErrorDialog(Activity activity, boolean z, Runnable runnable) throws Resources.NotFoundException {
        if (activity == null || activity.isFinishing()) {
            return;
        }
        this.mAlerterEx = new AlerterEx(activity, new OnBackPressedListener() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.2
            final /* synthetic */ Activity val$activity;
            final /* synthetic */ boolean val$isForceQuit;

            AnonymousClass2(boolean z2, Activity activity2) {
                z = z2;
                activity = activity2;
            }

            @Override // com.netease.ntunisdk.external.protocol.view.OnBackPressedListener
            public void onBackPressed() {
                if (z) {
                    ProtocolManager.this.getCallback().onFinish(2);
                    PluginManager.getInstance().exit(activity, new com.netease.ntunisdk.external.protocol.plugins.Callback() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.2.1
                        AnonymousClass1() {
                        }

                        @Override // com.netease.ntunisdk.external.protocol.plugins.Callback
                        public void onFinish(Result result) {
                            SysHelper.exitProcessInLaunch(activity);
                        }
                    });
                }
            }

            /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$2$1 */
            class AnonymousClass1 implements com.netease.ntunisdk.external.protocol.plugins.Callback {
                AnonymousClass1() {
                }

                @Override // com.netease.ntunisdk.external.protocol.plugins.Callback
                public void onFinish(Result result) {
                    SysHelper.exitProcessInLaunch(activity);
                }
            }
        });
        Resources resources = activity2.getResources();
        Locale resourcesConfigLocale = TextCompat.getResourcesConfigLocale(this.mProp.getLocale(), this.mProp.getUrl());
        if (resourcesConfigLocale != null) {
            Configuration configuration = resources.getConfiguration();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            configuration.locale = resourcesConfigLocale;
            resources.updateConfiguration(configuration, displayMetrics);
        }
        this.mAlerterEx.alert("  ", resources.getString(TextCompat.getResId(activity2, "unisdk_protocol_network_error", ResIdReader.RES_TYPE_STRING)), resources.getString(TextCompat.getResId(activity2, "unisdk_protocol_retry", ResIdReader.RES_TYPE_STRING)), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.3
            final /* synthetic */ Runnable val$runnable;

            AnonymousClass3(Runnable runnable2) {
                runnable = runnable2;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (runnable != null) {
                    new Thread(runnable).start();
                }
            }
        }, resources.getString(TextCompat.getResId(activity2, "unisdk_protocol_cancel", ResIdReader.RES_TYPE_STRING)), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.4
            final /* synthetic */ boolean val$isForceQuit;

            AnonymousClass4(boolean z2) {
                z = z2;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (z) {
                    ProtocolManager.this.getCallback().onFinish(2);
                }
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$3 */
    class AnonymousClass3 implements DialogInterface.OnClickListener {
        final /* synthetic */ Runnable val$runnable;

        AnonymousClass3(Runnable runnable2) {
            runnable = runnable2;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            if (runnable != null) {
                new Thread(runnable).start();
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$4 */
    class AnonymousClass4 implements DialogInterface.OnClickListener {
        final /* synthetic */ boolean val$isForceQuit;

        AnonymousClass4(boolean z2) {
            z = z2;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            if (z) {
                ProtocolManager.this.getCallback().onFinish(2);
            }
        }
    }

    public void loadProtocol(Situation situation) throws Exception {
        if (Situation.LAUNCHER == situation) {
            this.mProvider.loadLocalProtocolFromFiles();
        } else if ("1".equals(this.mProp.getOfflineGameFlag())) {
            this.mProvider.loadLocalProtocol();
        } else {
            this.mProvider.checkLatestVersionProtocol();
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$5 */
    class AnonymousClass5 implements Runnable {
        AnonymousClass5() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                L.d(ProtocolManager.TAG, "checkLatestProtocol start");
                ProtocolManager.this.mProvider.checkLatestVersionProtocol();
            } catch (Throwable unused) {
                L.d(ProtocolManager.TAG, "checkLatestProtocol failed");
            }
        }
    }

    public void checkLatestProtocol() {
        AsyncTask.concurrentExecute(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.5
            AnonymousClass5() {
            }

            @Override // java.lang.Runnable
            public void run() {
                try {
                    L.d(ProtocolManager.TAG, "checkLatestProtocol start");
                    ProtocolManager.this.mProvider.checkLatestVersionProtocol();
                } catch (Throwable unused) {
                    L.d(ProtocolManager.TAG, "checkLatestProtocol failed");
                }
            }
        });
    }

    @Deprecated
    public void showProtocolWhenLaunch() {
        Activity activity;
        SoftReference<Activity> softReference = this.mActivityReference;
        if (softReference == null || (activity = softReference.get()) == null || activity.isFinishing()) {
            return;
        }
        showProtocolWhenLaunch(activity, null);
    }

    @Deprecated
    public void showProtocol() {
        Activity activity;
        SoftReference<Activity> softReference = this.mActivityReference;
        if (softReference == null || (activity = softReference.get()) == null || activity.isFinishing()) {
            return;
        }
        showProtocol(activity);
    }

    public void showProtocolWhenLaunch(Activity activity, ProtocolInfo protocolInfo) {
        L.d(TAG, "showProtocolWhenLaunch");
        showProtocol(activity, Situation.LAUNCHER, Const.CONFIG_KEY.ALL, User.USER_NAME_LAUNCHER, protocolInfo, "");
    }

    public void showProtocolIfNeed(Activity activity, String str) {
        L.d(TAG, "showProtocol >> uid = " + str);
        showProtocol(activity, Situation.LOGIN, Const.CONFIG_KEY.ALL, str);
    }

    @Deprecated
    public void showProtocol(Activity activity) {
        L.d(TAG, "showProtocol(null uid)");
        String scene = this.mProp.getScene();
        if (TextUtils.isEmpty(scene)) {
            scene = Const.CONFIG_KEY.ALL;
        }
        showProtocol(activity, Situation.REVIEW, scene, this.mUserId);
    }

    public void showProtocol(Activity activity, String str) {
        L.d(TAG, "showProtocol");
        String scene = this.mProp.getScene();
        if (TextUtils.isEmpty(scene)) {
            scene = Const.CONFIG_KEY.ALL;
        }
        showProtocol(activity, Situation.REVIEW, scene, str);
    }

    public void showProtocol(Activity activity, String str, String str2) {
        L.d(TAG, "showProtocol");
        String scene = this.mProp.getScene();
        if (TextUtils.isEmpty(scene)) {
            scene = Const.CONFIG_KEY.ALL;
        }
        String str3 = scene;
        if (TextUtils.isEmpty(str)) {
            str = this.mUserId;
        }
        showProtocol(activity, Situation.REVIEW, str3, str, null, str2);
    }

    public synchronized void showProtocol(Activity activity, Situation situation, String str, String str2) {
        showProtocol(activity, situation, str, str2, null, null);
    }

    public synchronized void showProtocol(Activity activity, Situation situation, String str, String str2, ProtocolInfo protocolInfo, String str3) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                if (this.isShowing) {
                    return;
                }
                this.isShowing = true;
                boolean zIsFirstUser = User.isFirstUser(this.mCurrentUser, str2);
                if (this.mCurrentUser == null || ((!TextUtils.isEmpty(str2) && !this.mCurrentUser.getUid().equals(str2)) || (TextUtils.isEmpty(str2) && !this.mCurrentUser.isLauncher()))) {
                    this.mCurrentUser = new User(str2).setFirstUser(zIsFirstUser);
                }
                L.d(TAG, "showProtocol\uff0c Situation:" + situation.name() + ", scene:" + str + ", uid:" + str2 + ", isFirstLogin:" + zIsFirstUser + ", currentUser\uff1a" + this.mCurrentUser.getUid());
                new ProtocolExecutor().setSituation(situation).submitTask(new ProtocolCallable<Integer>() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.7
                    final /* synthetic */ Activity val$activity;
                    final /* synthetic */ String val$alias;
                    final /* synthetic */ ProtocolInfo val$info;
                    final /* synthetic */ boolean val$isFirstLogin;
                    final /* synthetic */ String val$scene;
                    final /* synthetic */ Situation val$situation;

                    AnonymousClass7(Situation situation2, String str4, Activity activity2, ProtocolInfo protocolInfo2, boolean zIsFirstUser2, String str32) {
                        situation = situation2;
                        str = str4;
                        activity = activity2;
                        protocolInfo = protocolInfo2;
                        z = zIsFirstUser2;
                        str = str32;
                    }

                    @Override // java.util.concurrent.Callable
                    public Integer call() throws Exception {
                        Config config;
                        L.d(ProtocolManager.TAG, "showProtocol\uff0c Situation:" + situation.name() + ", scene:" + str + ", user:" + ProtocolManager.this.mCurrentUser.getUid());
                        this.mSituation = situation;
                        if (ProtocolManager.this.mCurrentUser.isLogout() && Situation.REVIEW != this.mSituation) {
                            L.d(ProtocolManager.TAG, "showProtocol\uff0c Situation:" + situation.name() + "isLogout, no need show");
                            return 102;
                        }
                        SDKRuntime.getInstance().init(activity);
                        if (!SDKRuntime.getInstance().isPublishMainLand()) {
                            ProtocolManager.this.readConfig();
                            if (ProtocolManager.this.mProp != null && ((ProtocolManager.this.mProp.getProtocolConfig() == null || (ProtocolManager.this.mProp.getLanguage() != null && !ProtocolManager.this.mProp.getLanguage().equals(ProtocolManager.this.mProp.getProtocolConfig().language))) && (config = SDKRuntime.getInstance().getConfig()) != null)) {
                                ProtocolManager.this.mProp.setProtocolConfig(config.getProtocolConfig(null, ProtocolManager.this.mProp.getLanguage(), ProtocolManager.this.mProp.getUrl()));
                            }
                        }
                        ProtocolInfo protocolInfo2 = protocolInfo;
                        if (protocolInfo2 != null && protocolInfo2.isHtml) {
                            return 101;
                        }
                        try {
                            ProtocolManager.this.copyAssetsFiles(activity);
                        } catch (Exception unused) {
                        }
                        try {
                            ProtocolManager.this.loadProtocol(situation);
                            if (Situation.LOGIN == this.mSituation && !TextUtils.equals(ProtocolManager.this.mCurrentUser.getUid(), ProtocolManager.this.mUserId)) {
                                this.mSituation = Situation.SWITCH_ACCOUNT;
                            }
                            ProtocolManager.this.mCurrentUser.setAcceptProtocols(Store.getInstance().getAcceptedProtocolsByUid(z ? User.USER_NAME_LAUNCHER : ProtocolManager.this.mCurrentUser.getUid()));
                            ProtocolInfo baseProtocol = ProtocolManager.this.mProvider.getBaseProtocol();
                            if (baseProtocol != null && !TextUtils.isEmpty(str) && Situation.REVIEW == this.mSituation) {
                                Iterator<String> it = baseProtocol.mSubProtocolInfos.keySet().iterator();
                                while (true) {
                                    if (!it.hasNext()) {
                                        break;
                                    }
                                    String next = it.next();
                                    ProtocolInfo.SubProtocolInfo subProtocol = baseProtocol.getSubProtocol(next);
                                    if (str.equals(subProtocol.getAlias())) {
                                        ProtocolInfo protocol = SDKRuntime.getInstance().getProtocol(next);
                                        if (protocol == null && ((protocol = baseProtocol.findProtocolByUrl(subProtocol.mUrl)) == null || TextUtils.isEmpty(protocol.text))) {
                                            protocol = ProtocolManager.getInstance().getProvider().downloadAndCheckNewProtocol(baseProtocol.getProtocolFile(), protocol, subProtocol.mUrl);
                                        }
                                        if (protocol != null) {
                                            baseProtocol = protocol;
                                        }
                                    }
                                }
                            }
                            if (baseProtocol == null) {
                                return 102;
                            }
                            if (Situation.LOGIN == this.mSituation || Situation.SWITCH_ACCOUNT == this.mSituation) {
                                ProtocolManager protocolManager = ProtocolManager.this;
                                protocolManager.mUserId = protocolManager.mCurrentUser.getUid();
                                int iCheckNeedShowProtocolByUid = ProtocolManager.this.mProvider.checkNeedShowProtocolByUid(ProtocolManager.this.mCurrentUser, str);
                                L.d(ProtocolManager.TAG, "acceptStatus\uff1a" + iCheckNeedShowProtocolByUid);
                                if (iCheckNeedShowProtocolByUid == 1) {
                                    L.d(ProtocolManager.TAG, "no need showCompactView");
                                    baseProtocol.acceptStatus = 1;
                                    return 102;
                                }
                                int iCheckNeedShowProtocolByUid2 = z ? ProtocolManager.this.mProvider.checkNeedShowProtocolByUid(new User(User.USER_NAME_LAUNCHER), str) : 1;
                                if ((SDKRuntime.getInstance().isHasCustomProtocol() && iCheckNeedShowProtocolByUid == 0 && !baseProtocol.isHomeStyle()) || ((SDKRuntime.getInstance().isSilentMode() && iCheckNeedShowProtocolByUid == 0 && !baseProtocol.isHomeStyle()) || (z && !SDKRuntime.getInstance().isPublishMainLand() && iCheckNeedShowProtocolByUid2 == 1))) {
                                    L.d(ProtocolManager.TAG, "channel is netease\uff0c no need showCompactView ");
                                    ProtocolManager.this.mProvider.saveConfirmByUid(ProtocolManager.this.mUserId, false);
                                    baseProtocol.acceptStatus = 1;
                                    return 102;
                                }
                                baseProtocol.acceptStatus = iCheckNeedShowProtocolByUid;
                            }
                            ProtocolManager protocolManager2 = ProtocolManager.this;
                            protocolManager2.mConcreteSubProtocols = protocolManager2.mProvider.filterProtocolInfo(situation, str, baseProtocol, ProtocolManager.this.mCurrentUser);
                            if (baseProtocol.isHomeStyle()) {
                                if (ProtocolManager.this.mConcreteSubProtocols.isEmpty()) {
                                    return 102;
                                }
                                if (Situation.REVIEW == situation && ProtocolManager.this.mCurrentUser.isFirstUser() && !ProtocolManager.this.mCurrentUser.isLauncher()) {
                                    ProtocolManager.this.mProvider.saveConfirmByUid(ProtocolManager.this.mCurrentUser.getUid(), false);
                                }
                            }
                            this.mProtocolInfo = baseProtocol;
                            return 101;
                        } catch (FetchProtocolException e) {
                            e.printStackTrace();
                            ProtocolManager.this.postTrackerEvent(activity);
                            return 102;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            ProtocolManager.this.postTrackerEvent(activity);
                            return 102;
                        }
                    }
                }).setCallback(new AnonymousClass6(activity2, str4, str2, protocolInfo2)).execute(new Void[0]);
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$7 */
    class AnonymousClass7 extends ProtocolCallable<Integer> {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ String val$alias;
        final /* synthetic */ ProtocolInfo val$info;
        final /* synthetic */ boolean val$isFirstLogin;
        final /* synthetic */ String val$scene;
        final /* synthetic */ Situation val$situation;

        AnonymousClass7(Situation situation2, String str4, Activity activity2, ProtocolInfo protocolInfo2, boolean zIsFirstUser2, String str32) {
            situation = situation2;
            str = str4;
            activity = activity2;
            protocolInfo = protocolInfo2;
            z = zIsFirstUser2;
            str = str32;
        }

        @Override // java.util.concurrent.Callable
        public Integer call() throws Exception {
            Config config;
            L.d(ProtocolManager.TAG, "showProtocol\uff0c Situation:" + situation.name() + ", scene:" + str + ", user:" + ProtocolManager.this.mCurrentUser.getUid());
            this.mSituation = situation;
            if (ProtocolManager.this.mCurrentUser.isLogout() && Situation.REVIEW != this.mSituation) {
                L.d(ProtocolManager.TAG, "showProtocol\uff0c Situation:" + situation.name() + "isLogout, no need show");
                return 102;
            }
            SDKRuntime.getInstance().init(activity);
            if (!SDKRuntime.getInstance().isPublishMainLand()) {
                ProtocolManager.this.readConfig();
                if (ProtocolManager.this.mProp != null && ((ProtocolManager.this.mProp.getProtocolConfig() == null || (ProtocolManager.this.mProp.getLanguage() != null && !ProtocolManager.this.mProp.getLanguage().equals(ProtocolManager.this.mProp.getProtocolConfig().language))) && (config = SDKRuntime.getInstance().getConfig()) != null)) {
                    ProtocolManager.this.mProp.setProtocolConfig(config.getProtocolConfig(null, ProtocolManager.this.mProp.getLanguage(), ProtocolManager.this.mProp.getUrl()));
                }
            }
            ProtocolInfo protocolInfo2 = protocolInfo;
            if (protocolInfo2 != null && protocolInfo2.isHtml) {
                return 101;
            }
            try {
                ProtocolManager.this.copyAssetsFiles(activity);
            } catch (Exception unused) {
            }
            try {
                ProtocolManager.this.loadProtocol(situation);
                if (Situation.LOGIN == this.mSituation && !TextUtils.equals(ProtocolManager.this.mCurrentUser.getUid(), ProtocolManager.this.mUserId)) {
                    this.mSituation = Situation.SWITCH_ACCOUNT;
                }
                ProtocolManager.this.mCurrentUser.setAcceptProtocols(Store.getInstance().getAcceptedProtocolsByUid(z ? User.USER_NAME_LAUNCHER : ProtocolManager.this.mCurrentUser.getUid()));
                ProtocolInfo baseProtocol = ProtocolManager.this.mProvider.getBaseProtocol();
                if (baseProtocol != null && !TextUtils.isEmpty(str) && Situation.REVIEW == this.mSituation) {
                    Iterator<String> it = baseProtocol.mSubProtocolInfos.keySet().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        String next = it.next();
                        ProtocolInfo.SubProtocolInfo subProtocol = baseProtocol.getSubProtocol(next);
                        if (str.equals(subProtocol.getAlias())) {
                            ProtocolInfo protocol = SDKRuntime.getInstance().getProtocol(next);
                            if (protocol == null && ((protocol = baseProtocol.findProtocolByUrl(subProtocol.mUrl)) == null || TextUtils.isEmpty(protocol.text))) {
                                protocol = ProtocolManager.getInstance().getProvider().downloadAndCheckNewProtocol(baseProtocol.getProtocolFile(), protocol, subProtocol.mUrl);
                            }
                            if (protocol != null) {
                                baseProtocol = protocol;
                            }
                        }
                    }
                }
                if (baseProtocol == null) {
                    return 102;
                }
                if (Situation.LOGIN == this.mSituation || Situation.SWITCH_ACCOUNT == this.mSituation) {
                    ProtocolManager protocolManager = ProtocolManager.this;
                    protocolManager.mUserId = protocolManager.mCurrentUser.getUid();
                    int iCheckNeedShowProtocolByUid = ProtocolManager.this.mProvider.checkNeedShowProtocolByUid(ProtocolManager.this.mCurrentUser, str);
                    L.d(ProtocolManager.TAG, "acceptStatus\uff1a" + iCheckNeedShowProtocolByUid);
                    if (iCheckNeedShowProtocolByUid == 1) {
                        L.d(ProtocolManager.TAG, "no need showCompactView");
                        baseProtocol.acceptStatus = 1;
                        return 102;
                    }
                    int iCheckNeedShowProtocolByUid2 = z ? ProtocolManager.this.mProvider.checkNeedShowProtocolByUid(new User(User.USER_NAME_LAUNCHER), str) : 1;
                    if ((SDKRuntime.getInstance().isHasCustomProtocol() && iCheckNeedShowProtocolByUid == 0 && !baseProtocol.isHomeStyle()) || ((SDKRuntime.getInstance().isSilentMode() && iCheckNeedShowProtocolByUid == 0 && !baseProtocol.isHomeStyle()) || (z && !SDKRuntime.getInstance().isPublishMainLand() && iCheckNeedShowProtocolByUid2 == 1))) {
                        L.d(ProtocolManager.TAG, "channel is netease\uff0c no need showCompactView ");
                        ProtocolManager.this.mProvider.saveConfirmByUid(ProtocolManager.this.mUserId, false);
                        baseProtocol.acceptStatus = 1;
                        return 102;
                    }
                    baseProtocol.acceptStatus = iCheckNeedShowProtocolByUid;
                }
                ProtocolManager protocolManager2 = ProtocolManager.this;
                protocolManager2.mConcreteSubProtocols = protocolManager2.mProvider.filterProtocolInfo(situation, str, baseProtocol, ProtocolManager.this.mCurrentUser);
                if (baseProtocol.isHomeStyle()) {
                    if (ProtocolManager.this.mConcreteSubProtocols.isEmpty()) {
                        return 102;
                    }
                    if (Situation.REVIEW == situation && ProtocolManager.this.mCurrentUser.isFirstUser() && !ProtocolManager.this.mCurrentUser.isLauncher()) {
                        ProtocolManager.this.mProvider.saveConfirmByUid(ProtocolManager.this.mCurrentUser.getUid(), false);
                    }
                }
                this.mProtocolInfo = baseProtocol;
                return 101;
            } catch (FetchProtocolException e) {
                e.printStackTrace();
                ProtocolManager.this.postTrackerEvent(activity);
                return 102;
            } catch (Exception e2) {
                e2.printStackTrace();
                ProtocolManager.this.postTrackerEvent(activity);
                return 102;
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$6 */
    class AnonymousClass6 implements OnProtocolCallback {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ ProtocolInfo val$info;
        final /* synthetic */ String val$scene;
        final /* synthetic */ String val$uid;

        AnonymousClass6(Activity activity, String str, String str2, ProtocolInfo protocolInfo) {
            this.val$activity = activity;
            this.val$scene = str;
            this.val$uid = str2;
            this.val$info = protocolInfo;
        }

        @Override // com.netease.ntunisdk.external.protocol.ProtocolManager.OnProtocolCallback
        public void onFinish(Situation situation, ProtocolResult protocolResult) {
            Dialog dialogPreDialog;
            int i = protocolResult.result;
            if (i == 100) {
                this.val$activity.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.6.1
                    final /* synthetic */ Situation val$situation;

                    AnonymousClass1(Situation situation2) {
                        situation = situation2;
                    }

                    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$6$1$1 */
                    class RunnableC00761 implements Runnable {
                        RunnableC00761() {
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            ProtocolManager.this.showProtocol(AnonymousClass6.this.val$activity, situation, AnonymousClass6.this.val$scene, AnonymousClass6.this.val$uid);
                        }
                    }

                    @Override // java.lang.Runnable
                    public void run() throws Resources.NotFoundException {
                        ProtocolManager.this.isShowing = false;
                        ProtocolManager.this.showErrorDialog(AnonymousClass6.this.val$activity, Situation.REVIEW != situation, new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.6.1.1
                            RunnableC00761() {
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                ProtocolManager.this.showProtocol(AnonymousClass6.this.val$activity, situation, AnonymousClass6.this.val$scene, AnonymousClass6.this.val$uid);
                            }
                        });
                    }
                });
                return;
            }
            if (i == 101) {
                if (this.val$info != null) {
                    ProtocolManager protocolManager = ProtocolManager.this;
                    dialogPreDialog = protocolManager.preDialog(this.val$activity, situation2, this.val$scene, protocolManager.mCurrentUser, this.val$info, 1, true);
                } else {
                    ProtocolInfo baseProtocol = protocolResult.mProtocolInfo;
                    if (baseProtocol == null) {
                        baseProtocol = ProtocolManager.this.mProvider.getBaseProtocol();
                    }
                    ProtocolInfo protocolInfo = baseProtocol;
                    ProtocolManager protocolManager2 = ProtocolManager.this;
                    dialogPreDialog = protocolManager2.preDialog(this.val$activity, situation2, this.val$scene, protocolManager2.mCurrentUser, protocolInfo, (Situation.REVIEW == situation2 || (SDKRuntime.getInstance().isSilentMode() && !protocolInfo.isHomeStyle())) ? 1 : 2, false);
                }
                if (dialogPreDialog != null) {
                    Activity activity = this.val$activity;
                    if (activity == null || activity.isFinishing()) {
                        ProtocolManager protocolManager3 = ProtocolManager.this;
                        protocolManager3.notShowCallback(this.val$activity, situation2, protocolManager3.mCurrentUser);
                        return;
                    }
                    try {
                        dialogPreDialog.show();
                        SDKRuntime.getInstance().setProtocolShowing(true);
                        return;
                    } catch (Throwable th) {
                        th.printStackTrace();
                        ProtocolManager protocolManager4 = ProtocolManager.this;
                        protocolManager4.notShowCallback(this.val$activity, situation2, protocolManager4.mCurrentUser);
                        return;
                    }
                }
                ProtocolManager protocolManager5 = ProtocolManager.this;
                protocolManager5.notShowCallback(this.val$activity, situation2, protocolManager5.mCurrentUser);
                return;
            }
            ProtocolManager protocolManager6 = ProtocolManager.this;
            protocolManager6.notShowCallback(this.val$activity, situation2, protocolManager6.mCurrentUser);
        }

        /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$6$1 */
        class AnonymousClass1 implements Runnable {
            final /* synthetic */ Situation val$situation;

            AnonymousClass1(Situation situation2) {
                situation = situation2;
            }

            /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$6$1$1 */
            class RunnableC00761 implements Runnable {
                RunnableC00761() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    ProtocolManager.this.showProtocol(AnonymousClass6.this.val$activity, situation, AnonymousClass6.this.val$scene, AnonymousClass6.this.val$uid);
                }
            }

            @Override // java.lang.Runnable
            public void run() throws Resources.NotFoundException {
                ProtocolManager.this.isShowing = false;
                ProtocolManager.this.showErrorDialog(AnonymousClass6.this.val$activity, Situation.REVIEW != situation, new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.6.1.1
                    RunnableC00761() {
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        ProtocolManager.this.showProtocol(AnonymousClass6.this.val$activity, situation, AnonymousClass6.this.val$scene, AnonymousClass6.this.val$uid);
                    }
                });
            }
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$8 */
    class AnonymousClass8 implements Runnable {
        AnonymousClass8() {
        }

        @Override // java.lang.Runnable
        public void run() throws JSONException {
            Tracker.getInstance().onEvent(Tracker.EVENT_PROTOCOL_FETCH_FAILED_SKIP, "", "");
        }
    }

    public void postTrackerEvent(Activity activity) {
        try {
            activity.runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.8
                AnonymousClass8() {
                }

                @Override // java.lang.Runnable
                public void run() throws JSONException {
                    Tracker.getInstance().onEvent(Tracker.EVENT_PROTOCOL_FETCH_FAILED_SKIP, "", "");
                }
            });
        } catch (Throwable unused) {
        }
    }

    public boolean hasAcceptLaunchProtocol() {
        ProtocolProvider protocolProvider = this.mProvider;
        return protocolProvider != null && protocolProvider.hasAcceptLaunchProtocol();
    }

    public void readLocalData(Context context) {
        TreeSet<String> treeSetFilterAcceptProtocolByUid;
        copyAssetsFiles(context);
        if (!SDKRuntime.getInstance().isPublishMainLand()) {
            SDKRuntime.getInstance().init(context);
            readConfig();
        }
        String currentProtocolUrl = Store.getInstance().getCurrentProtocolUrl();
        ProtocolInfo localProtocol = new ProtocolFile(currentProtocolUrl).getLocalProtocol(currentProtocolUrl);
        String currentUid = Store.getInstance().getCurrentUid();
        if (TextUtils.isEmpty(currentUid)) {
            return;
        }
        if (SDKRuntime.getInstance().isPublishMainLand()) {
            treeSetFilterAcceptProtocolByUid = Store.getInstance().getAcceptedProtocolsByUid(currentUid);
        } else {
            treeSetFilterAcceptProtocolByUid = Store.getInstance().filterAcceptProtocolByUid(localProtocol, currentUid);
        }
        if (treeSetFilterAcceptProtocolByUid == null || treeSetFilterAcceptProtocolByUid.isEmpty()) {
            return;
        }
        User user = new User(currentUid);
        this.mCurrentUser = user;
        user.setAcceptProtocols(treeSetFilterAcceptProtocolByUid);
    }

    public boolean hasAcceptProtocol() {
        return this.mHasAcceptProtocolByLauncher;
    }

    public Dialog preDialog(Activity activity, Situation situation, String str, User user, ProtocolInfo protocolInfo, int i, boolean z) {
        String str2;
        String str3;
        if (protocolInfo == null) {
            return null;
        }
        this.mLastSituation = situation;
        this.mLastViewStyle = i;
        try {
            int i2 = AnonymousClass10.$SwitchMap$com$netease$ntunisdk$external$protocol$Situation[situation.ordinal()];
            if (i2 == 1 || i2 == 2) {
                if (protocolInfo.acceptStatus == 3) {
                    str2 = protocolInfo.updateText;
                    if (TextUtils.isEmpty(str2)) {
                        str2 = protocolInfo.text;
                    }
                } else {
                    str2 = protocolInfo.text;
                }
            } else if (i2 == 3) {
                str2 = protocolInfo.text;
            } else if (i2 == 4) {
                str2 = protocolInfo.reviewText;
            } else {
                str3 = null;
                if (!TextUtils.isEmpty(str3) && !protocolInfo.isHtml) {
                    L.d(TAG, "empty  ProtocolText");
                    return null;
                }
                ContentDialog contentDialog = new ContentDialog(activity);
                this.mContentDialog = contentDialog;
                contentDialog.init(activity, situation, str, user, i, str3, protocolInfo, z, this.mConcreteSubProtocols);
                return this.mContentDialog;
            }
            str3 = str2;
            if (!TextUtils.isEmpty(str3)) {
            }
            ContentDialog contentDialog2 = new ContentDialog(activity);
            this.mContentDialog = contentDialog2;
            contentDialog2.init(activity, situation, str, user, i, str3, protocolInfo, z, this.mConcreteSubProtocols);
            return this.mContentDialog;
        } catch (Exception e) {
            L.e(TAG, "preDialog Exception : " + e.getMessage());
            this.mContentDialog = null;
            return null;
        }
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$10 */
    static /* synthetic */ class AnonymousClass10 {
        static final /* synthetic */ int[] $SwitchMap$com$netease$ntunisdk$external$protocol$Situation;

        static {
            int[] iArr = new int[Situation.values().length];
            $SwitchMap$com$netease$ntunisdk$external$protocol$Situation = iArr;
            try {
                iArr[Situation.SWITCH_ACCOUNT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$netease$ntunisdk$external$protocol$Situation[Situation.LOGIN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$netease$ntunisdk$external$protocol$Situation[Situation.LAUNCHER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$netease$ntunisdk$external$protocol$Situation[Situation.REVIEW.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    final void notShowCallback(Activity activity, Situation situation, User user) {
        ProtocolInfo baseProtocol;
        if (activity == null || activity.isFinishing()) {
            return;
        }
        if (user == null) {
            this.mDefaultCallback.onFinish(3);
        } else if (!SDKRuntime.getInstance().isPublishMainLand() && (baseProtocol = this.mProvider.getBaseProtocol()) != null && baseProtocol.isHomeStyle()) {
            this.mDefaultCallback.onFinish(4, null);
        } else {
            this.mDefaultCallback.onFinish(3, null);
        }
    }

    final synchronized void copyAssetsFiles(Context context) {
        if (SDKRuntime.getInstance().getHasCopiedAsserts().booleanValue()) {
            return;
        }
        L.d(TAG, "start copyAssetsFiles");
        copyAssetsProtocol(context, Const.PROTOCOL_DEFAULT, false);
        copyAssetsProtocol(context, Const.PROTOCOL_CONFIG, true);
        SDKRuntime.getInstance().setHasCopiedAsserts(true);
    }

    final void readConfig() {
        this.mProvider.readConfig();
    }

    private void copyAssetsProtocol(Context context, String str, boolean z) throws Throwable {
        String[] list;
        if (context != null) {
            prepareProtocolInfo(context);
            File cacheDir = SDKRuntime.getInstance().getCacheDir();
            String strMd5 = TextCompat.md5(str);
            L.d(TAG, "protocolDir:" + cacheDir);
            if (!cacheDir.exists()) {
                if (cacheDir.mkdirs()) {
                    L.d(TAG, "load assert protocol");
                    FileUtil.copyAssetsFile(context, new File(strMd5 + ".zip").getPath(), SDKRuntime.getInstance().getCacheDirStr(), strMd5, z);
                    return;
                }
                return;
            }
            if (cacheDir.isDirectory() && (list = cacheDir.list()) != null) {
                for (String str2 : list) {
                    if (str2.contains(strMd5)) {
                        return;
                    }
                }
            }
            L.d(TAG, "load assert protocol");
            FileUtil.copyAssetsFile(context, new File(strMd5 + ".zip").getPath(), SDKRuntime.getInstance().getCacheDirStr(), strMd5, z);
        }
    }

    private void prepareProtocolInfo(Context context) {
        if (TextUtils.isEmpty(this.mProp.getIssuer())) {
            String string = ResUtils.getString(context, "protocol_issuer_name");
            if (!TextUtils.isEmpty(string)) {
                this.mProp.setIssuer(string);
            }
        }
        if (TextUtils.isEmpty(this.mProp.getUrl())) {
            this.mProp.setUrl(Const.PROTOCOL_DEFAULT);
        }
    }

    public void acceptProtocol() {
        this.mProvider.saveConfirmByUid(this.mUserId);
    }

    public void acceptProtocol(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mProvider.saveConfirmByUid(str);
    }

    public void acceptProtocol(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mProvider.saveConfirmByUid(str, z);
    }

    public synchronized void onDestroy(Context context) {
        L.d(TAG, "onDestroy");
        this.isShowing = false;
        try {
            ContentDialog contentDialog = this.mContentDialog;
            if (contentDialog != null && contentDialog.getContext() == context) {
                L.d(TAG, "onDestroy\uff0c dismiss dialog");
                this.mContentDialog.dismiss();
                this.mContentDialog = null;
            }
        } catch (Throwable unused) {
        }
        try {
            AlerterEx alerterEx = this.mAlerterEx;
            if (alerterEx != null) {
                alerterEx.onDismiss();
            }
        } catch (Throwable unused2) {
        }
        this.isUniSdkRunning = false;
        SDKRuntime.getInstance().setProtocolLauncherShowing(false);
        this.mAlerterEx = null;
    }

    public void onExit() {
        this.hasInit = false;
        this.mApplicationContext = null;
        SDKRuntime.getInstance().setProtocolLauncherShowing(false);
        instance = null;
    }

    /* renamed from: com.netease.ntunisdk.external.protocol.ProtocolManager$9 */
    class AnonymousClass9 implements Runnable {
        final /* synthetic */ Activity val$activity;
        final /* synthetic */ String val$uid;

        AnonymousClass9(Activity activity, String str) {
            activity = activity;
            str = str;
        }

        @Override // java.lang.Runnable
        public void run() {
            ProtocolManager.this.showProtocolIfNeed(activity, str);
        }
    }

    public void gameLoginSuccess(Activity activity, String str) {
        new Thread(new Runnable() { // from class: com.netease.ntunisdk.external.protocol.ProtocolManager.9
            final /* synthetic */ Activity val$activity;
            final /* synthetic */ String val$uid;

            AnonymousClass9(Activity activity2, String str2) {
                activity = activity2;
                str = str2;
            }

            @Override // java.lang.Runnable
            public void run() {
                ProtocolManager.this.showProtocolIfNeed(activity, str);
            }
        }).start();
    }

    public synchronized void setHasCustomProtocol(boolean z) {
        SDKRuntime.getInstance().setHasCustomProtocol(z);
    }

    public void setSilentMode(boolean z) {
        SDKRuntime.getInstance().setSilentMode(z);
    }

    public final void onSaveInstanceState(Bundle bundle) {
        if (bundle == null || !SDKRuntime.getInstance().isProtocolShowing()) {
            return;
        }
        bundle.putBoolean("isProtocolShowing", SDKRuntime.getInstance().isProtocolShowing());
        bundle.putInt("protocol_view_style", this.mLastViewStyle);
        bundle.putString("protocol_scene", this.mLastSituation.name());
        bundle.putString("uid", this.mUserId);
    }

    public boolean isProtocolShowing() {
        return SDKRuntime.getInstance().isProtocolShowing();
    }

    public void setProtocolShowing(boolean z) {
        SDKRuntime.getInstance().setProtocolShowing(z);
    }

    public final void onRestoreInstanceState(Activity activity, Bundle bundle) {
        if (bundle != null && !SDKRuntime.getInstance().isProtocolShowing()) {
            try {
                this.isProtocolDialogReShow = bundle.getBoolean("isProtocolShowing");
                this.mLastViewStyle = bundle.getInt("protocol_view_style");
                this.mLastSituation = Situation.valueOf(bundle.getString("protocol_scene"));
                this.mUserId = bundle.getString("uid");
            } catch (Throwable unused) {
                this.isProtocolDialogReShow = false;
            }
        }
        rebuildDialog(activity);
    }

    private void rebuildDialog(Activity activity) {
        if (SDKRuntime.getInstance().isProtocolShowing() || !this.isProtocolDialogReShow) {
            return;
        }
        ProtocolProvider protocolProvider = this.mProvider;
        if (protocolProvider != null) {
            protocolProvider.init(activity);
        }
        if (2 == this.mLastViewStyle && !TextUtils.isEmpty(this.mUserId)) {
            showProtocolIfNeed(activity, this.mUserId);
        } else {
            showProtocol(activity);
        }
    }

    public void syncClasses(String str, String str2, String str3) {
        syncClasses(str, str2, str3, false);
    }

    public void syncClassesDirectly(String str, String str2, String str3) {
        syncClasses(str, str2, str3, true);
    }

    public void syncClasses(String str, String str2, String str3, boolean z) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(Const.PROTOCOL_CLASS_DOWNLOAD) || TextUtils.isEmpty(Const.PROTOCOL_CLASS_UPLOAD)) {
            L.d(TAG, "no need upload classes");
            return;
        }
        L.d(TAG, "syncClasses start");
        SyncClassesTask syncClassesTask = new SyncClassesTask();
        syncClassesTask.setDirectly(z);
        syncClassesTask.init(this.mApplicationContext, str, str2, str3).start();
    }

    public final ProtocolProvider getProvider() {
        return this.mProvider;
    }

    @Deprecated
    public void setActivity(Activity activity) {
        this.mActivityReference = new SoftReference<>(activity);
        init(activity);
    }

    public int getCurrentProtocolId() {
        ProtocolProvider protocolProvider = this.mProvider;
        if (protocolProvider == null || protocolProvider.getBaseProtocol() == null) {
            return 0;
        }
        return this.mProvider.getBaseProtocol().id;
    }

    public int getCurrentProtocolVersion() {
        ProtocolProvider protocolProvider = this.mProvider;
        if (protocolProvider == null || protocolProvider.getBaseProtocol() == null) {
            return 0;
        }
        return this.mProvider.getBaseProtocol().version;
    }

    public int getLaunchProtocolId() {
        ProtocolProvider protocolProvider = this.mProvider;
        if (protocolProvider == null) {
            return 0;
        }
        return protocolProvider.getLaunchProtocolId();
    }

    public void setLaunchProtocolId(int i) {
        ProtocolProvider protocolProvider = this.mProvider;
        if (protocolProvider != null) {
            protocolProvider.setLaunchProtocolId(i);
        }
    }

    public int getLaunchProtocolVersion() {
        ProtocolProvider protocolProvider = this.mProvider;
        if (protocolProvider == null) {
            return 0;
        }
        return protocolProvider.getLaunchProtocolVersion();
    }

    public void setLaunchProtocolVersion(int i) {
        L.d(TAG, "setLaunchProtocolVersion [Manager]: " + i);
        ProtocolProvider protocolProvider = this.mProvider;
        if (protocolProvider != null) {
            protocolProvider.setLaunchProtocolVersion(i);
        }
    }

    public void updateCurrentUser(String str, TreeSet<String> treeSet) {
        User user = this.mCurrentUser;
        if (user == null || !user.getUid().equals(str)) {
            return;
        }
        this.mCurrentUser.setAcceptProtocols(treeSet);
    }

    public boolean isSupportShortcut() {
        return this.mUniSDKProxy.isSupportShortCut();
    }

    public boolean hasAppRunning() {
        return this.mUniSDKProxy.hasAppRunning();
    }

    public void setPublishArea(int i) {
        SDKRuntime.getInstance().setPublishArea(i);
    }

    public JSONObject queryAgreedProtocols(String str) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "queryAgreedProtocols");
            if (TextUtils.isEmpty(str)) {
                jSONObject.put("uid", "");
            } else {
                jSONObject.put("uid", str);
            }
            ProtocolInfo baseProtocol = this.mProvider.getBaseProtocol();
            if (baseProtocol == null) {
                jSONObject.put("protocols", "");
            } else {
                TreeSet<String> acceptedProtocolsByUid = Store.getInstance().getAcceptedProtocolsByUid(str);
                ArrayList arrayList = new ArrayList();
                Iterator<String> it = acceptedProtocolsByUid.iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    Iterator<String> it2 = baseProtocol.subProtocolUrls.iterator();
                    while (it2.hasNext()) {
                        ProtocolInfo.SubProtocolInfo subProtocol = baseProtocol.getSubProtocol(it2.next());
                        if (subProtocol != null) {
                            if (next.startsWith(subProtocol.getId() + "-") && !next.endsWith("-0")) {
                                arrayList.add(subProtocol.getAlias());
                            }
                        }
                    }
                }
                jSONObject.put("protocols", TextUtils.join(",", arrayList));
            }
        } catch (Exception unused) {
        }
        return jSONObject;
    }

    public void setRtlLayout(boolean z) {
        SDKRuntime.getInstance().setRTLLayout(z);
    }

    public boolean isUniSdkRunning() {
        return this.isUniSdkRunning;
    }

    public void setUniSdkRunning(boolean z) {
        this.isUniSdkRunning = z;
    }

    public JSONObject onProtocolListChangedCallback() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("methodId", "onProtocolListChangedCallback");
        } catch (Exception unused) {
        }
        return jSONObject;
    }

    public static class ProtocolResult {
        ProtocolInfo mProtocolInfo;
        Situation mSituation;
        int result;

        public ProtocolResult(Situation situation, int i, ProtocolInfo protocolInfo) {
            this.result = i;
            this.mSituation = situation;
            this.mProtocolInfo = protocolInfo;
        }
    }

    private static class ProtocolExecutor extends AsyncTask<Void, Void, ProtocolResult> {
        private OnProtocolCallback callback;
        private Situation mSituation;
        private ProtocolCallable<Integer> task;

        private ProtocolExecutor() {
        }

        /* synthetic */ ProtocolExecutor(AnonymousClass1 anonymousClass1) {
            this();
        }

        public ProtocolExecutor setSituation(Situation situation) {
            this.mSituation = situation;
            return this;
        }

        public ProtocolExecutor submitTask(ProtocolCallable<Integer> protocolCallable) {
            this.task = protocolCallable;
            return this;
        }

        public ProtocolExecutor setCallback(OnProtocolCallback onProtocolCallback) {
            this.callback = onProtocolCallback;
            return this;
        }

        @Override // com.netease.ntunisdk.external.protocol.utils.AsyncTask
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override // com.netease.ntunisdk.external.protocol.utils.AsyncTask
        public ProtocolResult doInBackground(Void... voidArr) {
            try {
                ProtocolCallable<Integer> protocolCallable = this.task;
                if (protocolCallable != null) {
                    int iIntValue = protocolCallable.call().intValue();
                    Situation situation = this.task.mSituation;
                    this.mSituation = situation;
                    return new ProtocolResult(situation, iIntValue, this.task.mProtocolInfo);
                }
                return new ProtocolResult(this.mSituation, -1, protocolCallable.mProtocolInfo);
            } catch (Exception unused) {
                return new ProtocolResult(this.mSituation, -1, this.task.mProtocolInfo);
            }
        }

        @Override // com.netease.ntunisdk.external.protocol.utils.AsyncTask
        public void onPostExecute(ProtocolResult protocolResult) {
            OnProtocolCallback onProtocolCallback = this.callback;
            if (onProtocolCallback != null) {
                onProtocolCallback.onFinish(this.mSituation, protocolResult);
            }
        }
    }

    public String getDisagreedAliasStr() {
        return Store.getInstance().getDisagreedAliasStr();
    }

    public boolean isDisagreedAliasChanged() {
        return Store.getInstance().isHasDisagreedAliasChanged();
    }

    public void consumeDisagreedAliasChanged() {
        Store.getInstance().setHasDisagreedAliasChanged(false);
    }

    public void setNoKillProcess(boolean z) {
        SDKRuntime.getInstance().setNotExitProcess(z);
    }

    public void setDelayShow(boolean z) {
        SDKRuntime.getInstance().setDelayShow(z);
    }
}