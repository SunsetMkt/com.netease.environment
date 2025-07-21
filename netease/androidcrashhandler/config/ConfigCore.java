package com.netease.androidcrashhandler.config;

import android.text.TextUtils;
import com.netease.androidcrashhandler.AndroidCrashHandler;
import com.netease.androidcrashhandler.NTCrashHunterKit;
import com.netease.androidcrashhandler.anr.messageQueue.LooperMessageLoggingManager;
import com.netease.androidcrashhandler.init.InitProxy;
import com.netease.androidcrashhandler.net.ConfigRequest;
import com.netease.androidcrashhandler.net.RequestCallback;
import com.netease.androidcrashhandler.thirdparty.unilogger.CUniLogger;
import com.netease.androidcrashhandler.unknownCrash.CheckNormalExitManager;
import com.netease.androidcrashhandler.util.CEmulatorDetector;
import com.netease.androidcrashhandler.util.CUtil;
import com.netease.androidcrashhandler.util.LogUtils;
import com.netease.ntunisdk.unilogger.global.Const;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ConfigCore {
    private static final String TAG = "ConfigCore";
    private static ConfigCore sConfigCore;
    private boolean mEnable = true;
    private boolean mWifiOnly = false;
    private int mExpire = 30;
    private int mQueueSize = 20;
    private int mFileLimit = 6144;
    private int mCarrierLimit = 6144;
    private int mDiFreshInterval = 86400;
    private boolean mMessageEnabled = true;
    private boolean mWacthDogEnabled = true;
    private boolean mLoggerEnabled = true;
    private boolean mLocalUnwindEnabled = false;
    private boolean mUnknownCrash = false;
    private boolean mAppCrashThrowable = true;
    private String CONFIG_FILE_NAME = "crashhunter_config.txt";
    private JSONArray mSystemSoArray = new JSONArray();

    private ConfigCore() {
    }

    public static ConfigCore getInstance() {
        if (sConfigCore == null) {
            sConfigCore = new ConfigCore();
        }
        return sConfigCore;
    }

    public void readLocalConfig() {
        int iOptInt;
        int iOptInt2;
        int iOptInt3;
        int iOptInt4;
        int iOptInt5;
        LogUtils.i(LogUtils.TAG, "ConfigCore [readLocalConfig] start");
        InitProxy.getInstance();
        String strFile2Str = CUtil.file2Str(InitProxy.sConfigFilePath, this.CONFIG_FILE_NAME);
        if (TextUtils.isEmpty(strFile2Str)) {
            LogUtils.i(LogUtils.TAG, "ConfigCore [readLocalConfig] configiInfo is null");
            return;
        }
        LogUtils.i(LogUtils.TAG, "ConfigCore [readLocalConfig] configiInfo=" + strFile2Str);
        try {
            JSONObject jSONObject = new JSONObject(strFile2Str);
            LogUtils.i(LogUtils.TAG, "ConfigCore [readLocalConfig] config=" + jSONObject.toString());
            if (jSONObject.has("enable")) {
                this.mEnable = jSONObject.optBoolean("enable");
            }
            if (jSONObject.has(Const.CONFIG_KEY.WIFI_ONLY)) {
                this.mWifiOnly = jSONObject.optBoolean(Const.CONFIG_KEY.WIFI_ONLY);
            }
            if (jSONObject.has(Const.CONFIG_KEY.EXPIRE) && (iOptInt5 = jSONObject.optInt(Const.CONFIG_KEY.EXPIRE)) > 0 && iOptInt5 <= 10000) {
                this.mExpire = iOptInt5;
            }
            if (jSONObject.has(Const.CONFIG_KEY.QUEUE_SIZE) && (iOptInt4 = jSONObject.optInt(Const.CONFIG_KEY.QUEUE_SIZE)) >= 5 && iOptInt4 <= 20) {
                this.mQueueSize = iOptInt4;
            }
            if (jSONObject.has(Const.CONFIG_KEY.FILE_LIMIT) && (iOptInt3 = jSONObject.optInt(Const.CONFIG_KEY.FILE_LIMIT)) >= 1024 && iOptInt3 <= 10240) {
                this.mFileLimit = iOptInt3;
            }
            if (jSONObject.has(Const.CONFIG_KEY.CARRIER_LIMIT) && (iOptInt2 = jSONObject.optInt(Const.CONFIG_KEY.CARRIER_LIMIT)) >= 1024 && iOptInt2 <= 30720) {
                this.mCarrierLimit = iOptInt2;
            }
            if (jSONObject.has("di_refresh_interval") && (iOptInt = jSONObject.optInt("di_refresh_interval")) >= 180 && iOptInt <= 3600) {
                this.mDiFreshInterval = iOptInt;
            }
            if (jSONObject.has("breakpad_enabled")) {
                InitProxy.getInstance().setmIsOpenBreakpad(jSONObject.optBoolean("breakpad_enabled"));
            }
            if (jSONObject.has("java_crash_enabled")) {
                InitProxy.getInstance().setmIsDetectJavaCrash(jSONObject.optBoolean("java_crash_enabled"));
            }
            if (jSONObject.has("watchdog_enabled")) {
                this.mWacthDogEnabled = jSONObject.optBoolean("watchdog_enabled");
            }
            if (jSONObject.has("message_enabled")) {
                this.mMessageEnabled = jSONObject.optBoolean("message_enabled");
            }
            if (jSONObject.has("sys_so")) {
                this.mSystemSoArray = jSONObject.optJSONArray("sys_so");
                LogUtils.i(LogUtils.TAG, "ConfigCore [readLocalConfig] mSystemSoArray=" + this.mSystemSoArray.toString());
            }
            if (jSONObject.has("app_crash_throwable")) {
                setAppCrashThrowable(jSONObject.optBoolean("app_crash_throwable"));
            }
            if (jSONObject.has("logger_enabled")) {
                this.mLoggerEnabled = jSONObject.optBoolean("logger_enabled");
            }
            if (jSONObject.has("uncaught_exception_enabled")) {
                this.mUnknownCrash = jSONObject.optBoolean("uncaught_exception_enabled");
            }
        } catch (Exception e) {
            LogUtils.i(LogUtils.TAG, "ConfigCore [readLocalConfig] Exception=" + e.toString());
            e.printStackTrace();
        }
    }

    public void fresh() {
        LogUtils.i(LogUtils.TAG, "ConfigCore [fresh] start");
        ConfigRequest configRequest = new ConfigRequest();
        configRequest.registerRequestCallback(new RequestCallback() { // from class: com.netease.androidcrashhandler.config.ConfigCore.1
            AnonymousClass1() {
            }

            /* JADX WARN: Removed duplicated region for block: B:63:0x0074 A[Catch: all -> 0x00c0, TRY_LEAVE, TryCatch #0 {all -> 0x00c0, blocks: (B:54:0x0047, B:56:0x0060, B:61:0x006e, B:63:0x0074, B:68:0x0082, B:70:0x0088, B:75:0x0096, B:77:0x009c), top: B:86:0x0047 }] */
            /* JADX WARN: Removed duplicated region for block: B:68:0x0082 A[Catch: all -> 0x00c0, PHI: r7
  0x0082: PHI (r7v5 boolean) = (r7v1 boolean), (r7v7 boolean) binds: [B:62:0x0072, B:65:0x007d] A[DONT_GENERATE, DONT_INLINE], TRY_ENTER, TryCatch #0 {all -> 0x00c0, blocks: (B:54:0x0047, B:56:0x0060, B:61:0x006e, B:63:0x0074, B:68:0x0082, B:70:0x0088, B:75:0x0096, B:77:0x009c), top: B:86:0x0047 }] */
            /* JADX WARN: Removed duplicated region for block: B:70:0x0088 A[Catch: all -> 0x00c0, TRY_LEAVE, TryCatch #0 {all -> 0x00c0, blocks: (B:54:0x0047, B:56:0x0060, B:61:0x006e, B:63:0x0074, B:68:0x0082, B:70:0x0088, B:75:0x0096, B:77:0x009c), top: B:86:0x0047 }] */
            /* JADX WARN: Removed duplicated region for block: B:75:0x0096 A[Catch: all -> 0x00c0, PHI: r5
  0x0096: PHI (r5v7 boolean) = (r5v3 boolean), (r5v9 boolean) binds: [B:69:0x0086, B:72:0x0091] A[DONT_GENERATE, DONT_INLINE], TRY_ENTER, TryCatch #0 {all -> 0x00c0, blocks: (B:54:0x0047, B:56:0x0060, B:61:0x006e, B:63:0x0074, B:68:0x0082, B:70:0x0088, B:75:0x0096, B:77:0x009c), top: B:86:0x0047 }] */
            /* JADX WARN: Removed duplicated region for block: B:77:0x009c A[Catch: all -> 0x00c0, TRY_LEAVE, TryCatch #0 {all -> 0x00c0, blocks: (B:54:0x0047, B:56:0x0060, B:61:0x006e, B:63:0x0074, B:68:0x0082, B:70:0x0088, B:75:0x0096, B:77:0x009c), top: B:86:0x0047 }] */
            @Override // com.netease.androidcrashhandler.net.RequestCallback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onResponse(int r12, java.lang.String r13) {
                /*
                    r11 = this;
                    java.lang.String r0 = "local_unwind"
                    java.lang.String r1 = "message_enabled"
                    java.lang.String r2 = "uncaught_exception_enabled"
                    java.lang.String r3 = "logger_enabled"
                    java.lang.String r4 = "ConfigCore [readLocalConfig] mLocalUnwindEnabled="
                    java.lang.StringBuilder r5 = new java.lang.StringBuilder
                    java.lang.String r6 = "ConfigCore [fresh] code="
                    r5.<init>(r6)
                    r5.append(r12)
                    java.lang.String r6 = ", info="
                    r5.append(r6)
                    r5.append(r13)
                    java.lang.String r5 = r5.toString()
                    java.lang.String r6 = "trace"
                    com.netease.androidcrashhandler.util.LogUtils.i(r6, r5)
                    com.netease.androidcrashhandler.config.ConfigCore r5 = com.netease.androidcrashhandler.config.ConfigCore.this
                    boolean r5 = r5.ismMessageEnabled()
                    com.netease.androidcrashhandler.config.ConfigCore r7 = com.netease.androidcrashhandler.config.ConfigCore.this
                    boolean r7 = r7.isOpenUnknownCrash()
                    com.netease.androidcrashhandler.config.ConfigCore r8 = com.netease.androidcrashhandler.config.ConfigCore.this
                    boolean r8 = com.netease.androidcrashhandler.config.ConfigCore.access$000(r8)
                    com.netease.androidcrashhandler.config.ConfigCore r9 = com.netease.androidcrashhandler.config.ConfigCore.this
                    boolean r9 = com.netease.androidcrashhandler.config.ConfigCore.access$100(r9)
                    boolean r10 = android.text.TextUtils.isEmpty(r13)
                    if (r10 != 0) goto Ld9
                    r10 = 200(0xc8, float:2.8E-43)
                    if (r10 != r12) goto Ld9
                    com.netease.androidcrashhandler.init.InitProxy.getInstance()     // Catch: java.lang.Throwable -> Lc0
                    java.lang.String r12 = com.netease.androidcrashhandler.init.InitProxy.sConfigFilePath     // Catch: java.lang.Throwable -> Lc0
                    com.netease.androidcrashhandler.config.ConfigCore r10 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> Lc0
                    java.lang.String r10 = com.netease.androidcrashhandler.config.ConfigCore.access$200(r10)     // Catch: java.lang.Throwable -> Lc0
                    com.netease.androidcrashhandler.util.CUtil.str2File(r13, r12, r10)     // Catch: java.lang.Throwable -> Lc0
                    org.json.JSONObject r12 = new org.json.JSONObject     // Catch: java.lang.Throwable -> Lc0
                    r12.<init>(r13)     // Catch: java.lang.Throwable -> Lc0
                    boolean r13 = r12.has(r3)     // Catch: java.lang.Throwable -> Lc0
                    if (r13 == 0) goto L6e
                    boolean r13 = r12.optBoolean(r3)     // Catch: java.lang.Throwable -> Lc0
                    com.netease.androidcrashhandler.config.ConfigCore r3 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> L6b
                    com.netease.androidcrashhandler.config.ConfigCore.access$002(r3, r13)     // Catch: java.lang.Throwable -> L6b
                    r8 = r13
                    goto L6e
                L6b:
                    r12 = move-exception
                    r8 = r13
                    goto Lc1
                L6e:
                    boolean r13 = r12.has(r2)     // Catch: java.lang.Throwable -> Lc0
                    if (r13 == 0) goto L82
                    boolean r13 = r12.optBoolean(r2)     // Catch: java.lang.Throwable -> Lc0
                    com.netease.androidcrashhandler.config.ConfigCore r2 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> L7f
                    com.netease.androidcrashhandler.config.ConfigCore.access$302(r2, r13)     // Catch: java.lang.Throwable -> L7f
                    r7 = r13
                    goto L82
                L7f:
                    r12 = move-exception
                    r7 = r13
                    goto Lc1
                L82:
                    boolean r13 = r12.has(r1)     // Catch: java.lang.Throwable -> Lc0
                    if (r13 == 0) goto L96
                    boolean r13 = r12.optBoolean(r1)     // Catch: java.lang.Throwable -> Lc0
                    com.netease.androidcrashhandler.config.ConfigCore r1 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> L93
                    com.netease.androidcrashhandler.config.ConfigCore.access$402(r1, r13)     // Catch: java.lang.Throwable -> L93
                    r5 = r13
                    goto L96
                L93:
                    r12 = move-exception
                    r5 = r13
                    goto Lc1
                L96:
                    boolean r13 = r12.has(r0)     // Catch: java.lang.Throwable -> Lc0
                    if (r13 == 0) goto Ld9
                    boolean r12 = r12.optBoolean(r0)     // Catch: java.lang.Throwable -> Lc0
                    com.netease.androidcrashhandler.config.ConfigCore r13 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> Lbc
                    com.netease.androidcrashhandler.config.ConfigCore.access$102(r13, r12)     // Catch: java.lang.Throwable -> Lbc
                    java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lbc
                    r13.<init>(r4)     // Catch: java.lang.Throwable -> Lbc
                    com.netease.androidcrashhandler.config.ConfigCore r0 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> Lbc
                    boolean r0 = com.netease.androidcrashhandler.config.ConfigCore.access$100(r0)     // Catch: java.lang.Throwable -> Lbc
                    r13.append(r0)     // Catch: java.lang.Throwable -> Lbc
                    java.lang.String r13 = r13.toString()     // Catch: java.lang.Throwable -> Lbc
                    com.netease.androidcrashhandler.util.LogUtils.i(r6, r13)     // Catch: java.lang.Throwable -> Lbc
                    r9 = r12
                    goto Ld9
                Lbc:
                    r13 = move-exception
                    r9 = r12
                    r12 = r13
                    goto Lc1
                Lc0:
                    r12 = move-exception
                Lc1:
                    r12.printStackTrace()
                    java.lang.StringBuilder r13 = new java.lang.StringBuilder
                    java.lang.String r0 = "ConfigCore [fresh] Exception = "
                    r13.<init>(r0)
                    java.lang.String r12 = r12.toString()
                    r13.append(r12)
                    java.lang.String r12 = r13.toString()
                    com.netease.androidcrashhandler.util.LogUtils.i(r6, r12)
                Ld9:
                    com.netease.androidcrashhandler.config.ConfigCore r12 = com.netease.androidcrashhandler.config.ConfigCore.this
                    com.netease.androidcrashhandler.config.ConfigCore.access$500(r12, r5, r7, r8, r9)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.config.ConfigCore.AnonymousClass1.onResponse(int, java.lang.String):void");
            }
        });
        CUtil.runOnNewChildThread(new CUtil.ThreadTask() { // from class: com.netease.androidcrashhandler.config.ConfigCore.2
            final /* synthetic */ ConfigRequest val$ConfigRequest;

            AnonymousClass2(ConfigRequest configRequest2) {
                configRequest = configRequest2;
            }

            @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
            public void run() {
                try {
                    configRequest.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, "crashhunter-config");
    }

    /* renamed from: com.netease.androidcrashhandler.config.ConfigCore$1 */
    class AnonymousClass1 implements RequestCallback {
        AnonymousClass1() {
        }

        @Override // com.netease.androidcrashhandler.net.RequestCallback
        public void onResponse(int v, String v2) {
            /*
                this = this;
                java.lang.String r0 = "local_unwind"
                java.lang.String r1 = "message_enabled"
                java.lang.String r2 = "uncaught_exception_enabled"
                java.lang.String r3 = "logger_enabled"
                java.lang.String r4 = "ConfigCore [readLocalConfig] mLocalUnwindEnabled="
                java.lang.StringBuilder r5 = new java.lang.StringBuilder
                java.lang.String r6 = "ConfigCore [fresh] code="
                r5.<init>(r6)
                r5.append(r12)
                java.lang.String r6 = ", info="
                r5.append(r6)
                r5.append(r13)
                java.lang.String r5 = r5.toString()
                java.lang.String r6 = "trace"
                com.netease.androidcrashhandler.util.LogUtils.i(r6, r5)
                com.netease.androidcrashhandler.config.ConfigCore r5 = com.netease.androidcrashhandler.config.ConfigCore.this
                boolean r5 = r5.ismMessageEnabled()
                com.netease.androidcrashhandler.config.ConfigCore r7 = com.netease.androidcrashhandler.config.ConfigCore.this
                boolean r7 = r7.isOpenUnknownCrash()
                com.netease.androidcrashhandler.config.ConfigCore r8 = com.netease.androidcrashhandler.config.ConfigCore.this
                boolean r8 = com.netease.androidcrashhandler.config.ConfigCore.access$000(r8)
                com.netease.androidcrashhandler.config.ConfigCore r9 = com.netease.androidcrashhandler.config.ConfigCore.this
                boolean r9 = com.netease.androidcrashhandler.config.ConfigCore.access$100(r9)
                boolean r10 = android.text.TextUtils.isEmpty(r13)
                if (r10 != 0) goto Ld9
                r10 = 200(0xc8, float:2.8E-43)
                if (r10 != r12) goto Ld9
                com.netease.androidcrashhandler.init.InitProxy.getInstance()     // Catch: java.lang.Throwable -> Lc0
                java.lang.String r12 = com.netease.androidcrashhandler.init.InitProxy.sConfigFilePath     // Catch: java.lang.Throwable -> Lc0
                com.netease.androidcrashhandler.config.ConfigCore r10 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> Lc0
                java.lang.String r10 = com.netease.androidcrashhandler.config.ConfigCore.access$200(r10)     // Catch: java.lang.Throwable -> Lc0
                com.netease.androidcrashhandler.util.CUtil.str2File(r13, r12, r10)     // Catch: java.lang.Throwable -> Lc0
                org.json.JSONObject r12 = new org.json.JSONObject     // Catch: java.lang.Throwable -> Lc0
                r12.<init>(r13)     // Catch: java.lang.Throwable -> Lc0
                boolean r13 = r12.has(r3)     // Catch: java.lang.Throwable -> Lc0
                if (r13 == 0) goto L6e
                boolean r13 = r12.optBoolean(r3)     // Catch: java.lang.Throwable -> Lc0
                com.netease.androidcrashhandler.config.ConfigCore r3 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> L6b
                com.netease.androidcrashhandler.config.ConfigCore.access$002(r3, r13)     // Catch: java.lang.Throwable -> L6b
                r8 = r13
                goto L6e
            L6b:
                r12 = move-exception
                r8 = r13
                goto Lc1
            L6e:
                boolean r13 = r12.has(r2)     // Catch: java.lang.Throwable -> Lc0
                if (r13 == 0) goto L82
                boolean r13 = r12.optBoolean(r2)     // Catch: java.lang.Throwable -> Lc0
                com.netease.androidcrashhandler.config.ConfigCore r2 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> L7f
                com.netease.androidcrashhandler.config.ConfigCore.access$302(r2, r13)     // Catch: java.lang.Throwable -> L7f
                r7 = r13
                goto L82
            L7f:
                r12 = move-exception
                r7 = r13
                goto Lc1
            L82:
                boolean r13 = r12.has(r1)     // Catch: java.lang.Throwable -> Lc0
                if (r13 == 0) goto L96
                boolean r13 = r12.optBoolean(r1)     // Catch: java.lang.Throwable -> Lc0
                com.netease.androidcrashhandler.config.ConfigCore r1 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> L93
                com.netease.androidcrashhandler.config.ConfigCore.access$402(r1, r13)     // Catch: java.lang.Throwable -> L93
                r5 = r13
                goto L96
            L93:
                r12 = move-exception
                r5 = r13
                goto Lc1
            L96:
                boolean r13 = r12.has(r0)     // Catch: java.lang.Throwable -> Lc0
                if (r13 == 0) goto Ld9
                boolean r12 = r12.optBoolean(r0)     // Catch: java.lang.Throwable -> Lc0
                com.netease.androidcrashhandler.config.ConfigCore r13 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> Lbc
                com.netease.androidcrashhandler.config.ConfigCore.access$102(r13, r12)     // Catch: java.lang.Throwable -> Lbc
                java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lbc
                r13.<init>(r4)     // Catch: java.lang.Throwable -> Lbc
                com.netease.androidcrashhandler.config.ConfigCore r0 = com.netease.androidcrashhandler.config.ConfigCore.this     // Catch: java.lang.Throwable -> Lbc
                boolean r0 = com.netease.androidcrashhandler.config.ConfigCore.access$100(r0)     // Catch: java.lang.Throwable -> Lbc
                r13.append(r0)     // Catch: java.lang.Throwable -> Lbc
                java.lang.String r13 = r13.toString()     // Catch: java.lang.Throwable -> Lbc
                com.netease.androidcrashhandler.util.LogUtils.i(r6, r13)     // Catch: java.lang.Throwable -> Lbc
                r9 = r12
                goto Ld9
            Lbc:
                r13 = move-exception
                r9 = r12
                r12 = r13
                goto Lc1
            Lc0:
                r12 = move-exception
            Lc1:
                r12.printStackTrace()
                java.lang.StringBuilder r13 = new java.lang.StringBuilder
                java.lang.String r0 = "ConfigCore [fresh] Exception = "
                r13.<init>(r0)
                java.lang.String r12 = r12.toString()
                r13.append(r12)
                java.lang.String r12 = r13.toString()
                com.netease.androidcrashhandler.util.LogUtils.i(r6, r12)
            Ld9:
                com.netease.androidcrashhandler.config.ConfigCore r12 = com.netease.androidcrashhandler.config.ConfigCore.this
                com.netease.androidcrashhandler.config.ConfigCore.access$500(r12, r5, r7, r8, r9)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.androidcrashhandler.config.ConfigCore.AnonymousClass1.onResponse(int, java.lang.String):void");
        }
    }

    /* renamed from: com.netease.androidcrashhandler.config.ConfigCore$2 */
    class AnonymousClass2 implements CUtil.ThreadTask {
        final /* synthetic */ ConfigRequest val$ConfigRequest;

        AnonymousClass2(ConfigRequest configRequest2) {
            configRequest = configRequest2;
        }

        @Override // com.netease.androidcrashhandler.util.CUtil.ThreadTask
        public void run() {
            try {
                configRequest.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean ismEnable() {
        return this.mEnable;
    }

    public boolean ismWifiOnly() {
        return this.mWifiOnly;
    }

    public int getmExpire() {
        return this.mExpire;
    }

    public int getmQueueSize() {
        return this.mQueueSize;
    }

    public int getmFileLimit() {
        return this.mFileLimit;
    }

    public int getmCarrierLimit() {
        return this.mCarrierLimit;
    }

    public int getmDiFreshTime() {
        return this.mDiFreshInterval;
    }

    public boolean ismMessageEnabled() {
        return this.mMessageEnabled;
    }

    public boolean isWacthDogEnabled() {
        return this.mWacthDogEnabled;
    }

    public JSONArray getmSystemSoArray() {
        return this.mSystemSoArray;
    }

    public boolean isAppCrashThrowable() {
        return this.mAppCrashThrowable;
    }

    public void setAppCrashThrowable(boolean z) {
        this.mAppCrashThrowable = z;
        AndroidCrashHandler.getInstance().setThrowable(this.mAppCrashThrowable);
    }

    public boolean isLoaclUnwindEnabled() {
        return this.mLocalUnwindEnabled;
    }

    public boolean isOpenUnknownCrash() {
        return this.mUnknownCrash;
    }

    public void switchPlugins(boolean z, boolean z2, boolean z3, boolean z4) {
        if (z) {
            LogUtils.i(LogUtils.TAG, "ANRWatchDogProxy [getInstance] \u542f\u52a8Message\u76d1\u63a7");
            LooperMessageLoggingManager.getInstance().start();
            AndroidCrashHandler.getInstance().openTouchEventHook();
        } else {
            LogUtils.i(LogUtils.TAG, "ANRWatchDogProxy [getInstance] \u65e0\u9700\u542f\u52a8Message\u76d1\u63a7");
        }
        if (z2) {
            CheckNormalExitManager.getInstance().init(NTCrashHunterKit.sharedKit().getContext());
            CheckNormalExitManager.getInstance().initMemoryAdvice(NTCrashHunterKit.sharedKit().getContext());
            CheckNormalExitManager.getInstance().refreshStartTime();
        }
        if (z3) {
            CUniLogger.createNewUniLoggerInstance(NTCrashHunterKit.sharedKit().getContext());
        }
        if (z4) {
            LogUtils.i(LogUtils.TAG, "CrashHunterProxy [start] setDumpModule");
            AndroidCrashHandler.getInstance().setDumpModule(NTCrashHunterKit.sharedKit().getContext().getApplicationInfo().nativeLibraryDir, 1, CEmulatorDetector.detectLocal(NTCrashHunterKit.sharedKit().getContext()));
        }
    }
}