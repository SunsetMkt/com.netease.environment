package com.netease.ntunisdk.external.protocol.data;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.external.protocol.utils.Base64;
import com.netease.ntunisdk.external.protocol.utils.L;
import com.netease.ntunisdk.external.protocol.utils.ResUtils;
import com.netease.ntunisdk.external.protocol.utils.SysHelper;
import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SDKRuntime {
    private static final String TAG = "Protocol Memory";
    private static SDKRuntime sInstance;
    private File cacheDir;
    private String chromePackage;
    private Config config;
    private boolean hasRead;
    private File rootDir;
    private boolean hasInit = false;
    private Boolean hasCopiedAsserts = false;
    private int publishArea = -1;
    private boolean isSupportOpenBrowser = true;
    private String mAppChannel = "";
    private String mJFGameId = "";
    private final String mPlatform = "a";
    private String mBase64Data = "";
    private boolean showContentByTextView = false;
    private boolean mHideLogo = false;
    private boolean isRTLLayout = false;
    private boolean hasCustomProtocol = false;
    private boolean isSilentMode = false;
    private boolean isSouthAmericaAndJapan = false;
    private boolean isProtocolLauncherShowing = false;
    private boolean isProtocolShowing = false;
    private boolean isProtocolLauncher = false;
    private String gameLauncherActivity = "";
    private final ConcurrentHashMap<String, ProtocolInfo> mProtocolPools = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> sMD5Cache = new ConcurrentHashMap<>();
    private int zoomSize = -1;
    private boolean hiddenClose = false;
    private boolean notExitProcess = false;
    private boolean delayShow = false;

    public String getPlatform() {
        return "a";
    }

    private SDKRuntime() {
    }

    public static SDKRuntime getInstance() {
        if (sInstance == null) {
            synchronized (SDKRuntime.class) {
                if (sInstance == null) {
                    sInstance = new SDKRuntime();
                }
            }
        }
        return sInstance;
    }

    public String getGameLauncherActivity() {
        return this.gameLauncherActivity;
    }

    public void setGameLauncherActivity(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.gameLauncherActivity = str;
    }

    public boolean isProtocolLauncherShowing() {
        return this.isProtocolLauncherShowing;
    }

    public void setProtocolLauncherShowing(boolean z) {
        this.isProtocolLauncherShowing = z;
    }

    public boolean isHasCustomProtocol() {
        return this.hasCustomProtocol;
    }

    public void setHasCustomProtocol(boolean z) {
        this.hasCustomProtocol = z;
    }

    public boolean isSilentMode() {
        return this.isSilentMode;
    }

    public void setSilentMode(boolean z) {
        this.isSilentMode = z;
    }

    public boolean isSouthAmericaAndJapan() {
        return this.isSouthAmericaAndJapan;
    }

    public void setSouthAmericaAndJapan(boolean z) {
        this.isSouthAmericaAndJapan = z;
    }

    public Config getConfig() {
        return this.config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public int getPublishArea() {
        return this.publishArea;
    }

    public void setPublishArea(int i) {
        this.publishArea = i;
    }

    public boolean isRTLLayout() {
        return this.isRTLLayout;
    }

    public void setRTLLayout(boolean z) {
        this.isRTLLayout = z;
    }

    public synchronized void addProtocolIntoMemory(ProtocolInfo protocolInfo) {
        if (protocolInfo != null) {
            L.d(TAG, "add[Protocol] : " + protocolInfo.url);
            this.mProtocolPools.put(protocolInfo.url, protocolInfo);
        }
    }

    public synchronized ProtocolInfo getProtocol(String str) {
        return this.mProtocolPools.get(str);
    }

    public void clearProtocolInMemory() {
        this.mProtocolPools.clear();
    }

    public synchronized boolean hasInMemory(String str) {
        return this.mProtocolPools.containsKey(str);
    }

    public synchronized String getMD5Str(String str) {
        return this.sMD5Cache.get(str);
    }

    public synchronized void addMD5Cache(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            return;
        }
        this.sMD5Cache.put(str, str2);
    }

    public synchronized void init(Context context) {
        if (!this.hasInit) {
            String string = ResUtils.getString(context, "protocol_hide_all_logo", "0");
            String string2 = ResUtils.getString(context, "protocol_disable_webview", "0");
            String string3 = ResUtils.getString(context, "protocol_disable_browser", "0");
            String string4 = ResUtils.getString(context, "protocol_not_exit", "0");
            String string5 = ResUtils.getString(context, "protocol_delay_show", "0");
            if (Build.VERSION.SDK_INT > 22) {
                setShowContentByTextView("1".equals(string2));
            } else {
                setShowContentByTextView(true);
            }
            setHideLogo("1".equals(string));
            setSupportOpenBrowser("0".equals(string3));
            setNotExitProcess("1".equals(string4));
            setDelayShow("1".equals(string5));
            File file = new File(context.getFilesDir(), "protocol");
            this.rootDir = file;
            if (!file.exists()) {
                this.hasInit = this.rootDir.mkdir();
            } else {
                this.hasInit = true;
            }
            this.cacheDir = new File(this.rootDir, "cache");
            computeWebViewZoom(context);
            if (!this.cacheDir.exists()) {
                this.hasInit = this.cacheDir.mkdir();
            } else {
                this.hasInit = true;
            }
        }
    }

    public void computeWebViewZoom(Context context) {
        if (this.zoomSize == -1) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int i = displayMetrics.widthPixels;
            int i2 = displayMetrics.heightPixels;
            int i3 = (int) ((i > i2 ? i2 : i) / displayMetrics.density);
            this.zoomSize = i3 / 640;
            L.d(TAG, "screenDIP:" + i3 + ",zoom:" + this.zoomSize);
            if (this.zoomSize < 1) {
                this.zoomSize = 1;
            }
            if (this.zoomSize > 5) {
                this.zoomSize = 5;
            }
            L.d(TAG, "screenDIP:" + i3 + ",zoom:" + this.zoomSize);
        }
    }

    public int getZoomSize() {
        return this.zoomSize;
    }

    public String getRootDirStr() {
        try {
            return this.hasInit ? this.rootDir.getCanonicalPath() : "";
        } catch (Throwable unused) {
            return "";
        }
    }

    public String getCacheDirStr() {
        try {
            return this.hasInit ? this.cacheDir.getCanonicalPath() : "";
        } catch (Throwable unused) {
            return "";
        }
    }

    public File getRootDir() {
        return this.rootDir;
    }

    public File getCacheDir() {
        return this.cacheDir;
    }

    public Boolean getHasCopiedAsserts() {
        return this.hasCopiedAsserts;
    }

    public void setHasCopiedAsserts(Boolean bool) {
        this.hasCopiedAsserts = bool;
    }

    public boolean isPublishMainLand() {
        int i = this.publishArea;
        return i == 0 || i == -1;
    }

    public boolean isPublishMiddleEast() {
        int i = this.publishArea;
        return i == 3 || i == -1;
    }

    public String getAppChannel() {
        return this.mAppChannel;
    }

    public void setAppChannel(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mAppChannel = str;
    }

    public String getJFGameId() {
        return this.mJFGameId;
    }

    public void setJFGameId(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mJFGameId = str;
    }

    public boolean isShowContentByTextView() {
        return this.showContentByTextView;
    }

    public void setShowContentByTextView(boolean z) {
        this.showContentByTextView = z;
    }

    public boolean isHideLogo() {
        return this.mHideLogo;
    }

    public void setHideLogo(boolean z) {
        this.mHideLogo = z;
    }

    public String getDataStr() {
        if (TextUtils.isEmpty(this.mBase64Data)) {
            return getDataStrForceRefresh();
        }
        return this.mBase64Data;
    }

    public synchronized String getDataStrForceRefresh() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("gameid", this.mJFGameId);
            jSONObject.put(Const.APP_CHANNEL, this.mAppChannel);
            jSONObject.put("platform", "a");
            this.mBase64Data = Base64.encodeToString(jSONObject.toString().getBytes("UTF-8"), 10);
            L.d("data=" + this.mBase64Data);
        } catch (Throwable unused) {
            this.mBase64Data = "";
        }
        return this.mBase64Data;
    }

    public boolean isSupportOpenBrowser() {
        return this.isSupportOpenBrowser;
    }

    public void setSupportOpenBrowser(boolean z) {
        this.isSupportOpenBrowser = z;
    }

    public String getChromePackage(Activity activity) {
        if (TextUtils.isEmpty(this.chromePackage) && !this.hasRead) {
            this.chromePackage = SysHelper.getChromePackage(activity);
            this.hasRead = true;
        }
        return this.chromePackage;
    }

    public boolean isProtocolLauncher() {
        return this.isProtocolLauncher;
    }

    public void setProtocolLauncher(boolean z) {
        this.isProtocolLauncher = z;
    }

    public boolean isProtocolShowing() {
        return this.isProtocolShowing;
    }

    public void setProtocolShowing(boolean z) {
        this.isProtocolShowing = z;
    }

    public boolean isHiddenClose() {
        return this.hiddenClose;
    }

    public void setHiddenClose(boolean z) {
        this.hiddenClose = z;
    }

    public boolean isNotExitProcess() {
        return this.notExitProcess;
    }

    public void setNotExitProcess(boolean z) {
        this.notExitProcess = z;
    }

    public boolean isDelayShow() {
        return this.delayShow;
    }

    public void setDelayShow(boolean z) {
        this.delayShow = z;
    }
}