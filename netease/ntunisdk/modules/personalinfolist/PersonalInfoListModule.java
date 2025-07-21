package com.netease.ntunisdk.modules.personalinfolist;

import android.content.Context;
import com.netease.ntunisdk.modules.base.BaseModules;
import com.netease.ntunisdk.modules.base.call.IModulesCall;
import java.io.IOException;

/* loaded from: classes.dex */
public class PersonalInfoListModule extends BaseModules {
    private static final String TAG = "UNISDK PersonalInfoListModule";

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getModuleName() {
        return PILConstant.MODEL_NAME;
    }

    @Override // com.netease.ntunisdk.modules.base.BaseModules
    public String getVersion() {
        return "1.1.0";
    }

    public PersonalInfoListModule(Context context, IModulesCall iModulesCall) throws IOException {
        super(context, iModulesCall);
        readLibraryConfig();
        HookManager.init(context);
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x003b A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x003e  */
    @Override // com.netease.ntunisdk.modules.base.BaseModules
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String extendFunc(java.lang.String r2, java.lang.String r3, java.lang.String r4, java.lang.Object... r5) throws org.json.JSONException {
        /*
            r1 = this;
            r2 = 0
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch: org.json.JSONException -> L25
            r3.<init>(r4)     // Catch: org.json.JSONException -> L25
            java.lang.String r2 = "methodId"
            java.lang.String r2 = r3.optString(r2)     // Catch: org.json.JSONException -> L23
            java.lang.String r5 = "uploadPersonalInfoEventDirectly"
            boolean r2 = r5.equalsIgnoreCase(r2)     // Catch: org.json.JSONException -> L23
            if (r2 == 0) goto L1d
            com.netease.ntunisdk.modules.personalinfolist.ClientLogReporter.reportDirectly(r4)     // Catch: org.json.JSONException -> L23
            com.netease.ntunisdk.modules.personalinfolist.PILConstant$PILExtendCode r2 = com.netease.ntunisdk.modules.personalinfolist.PILConstant.PILExtendCode.SUCCESS     // Catch: org.json.JSONException -> L23
            com.netease.ntunisdk.modules.personalinfolist.PILConstant.PILExtendCode.formatResult(r3, r2)     // Catch: org.json.JSONException -> L23
            goto L39
        L1d:
            com.netease.ntunisdk.modules.personalinfolist.PILConstant$PILExtendCode r2 = com.netease.ntunisdk.modules.personalinfolist.PILConstant.PILExtendCode.NO_EXIST_METHOD     // Catch: org.json.JSONException -> L23
            com.netease.ntunisdk.modules.personalinfolist.PILConstant.PILExtendCode.formatResult(r3, r2)     // Catch: org.json.JSONException -> L23
            goto L39
        L23:
            r2 = move-exception
            goto L29
        L25:
            r3 = move-exception
            r0 = r3
            r3 = r2
            r2 = r0
        L29:
            r2.printStackTrace()
            if (r3 != 0) goto L34
            org.json.JSONObject r2 = new org.json.JSONObject
            r2.<init>()
            r3 = r2
        L34:
            com.netease.ntunisdk.modules.personalinfolist.PILConstant$PILExtendCode r2 = com.netease.ntunisdk.modules.personalinfolist.PILConstant.PILExtendCode.UNKNOWN_ERROR
            com.netease.ntunisdk.modules.personalinfolist.PILConstant.PILExtendCode.formatResult(r3, r2)
        L39:
            if (r3 != 0) goto L3e
            java.lang.String r2 = "unknow"
            goto L42
        L3e:
            java.lang.String r2 = r3.toString()
        L42:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.personalinfolist.PersonalInfoListModule.extendFunc(java.lang.String, java.lang.String, java.lang.String, java.lang.Object[]):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0029 A[Catch: all -> 0x0024, IOException -> 0x0054, TRY_LEAVE, TryCatch #3 {IOException -> 0x0054, blocks: (B:7:0x001d, B:11:0x0029, B:15:0x0034, B:20:0x0040), top: B:61:0x001d, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0034 A[Catch: all -> 0x0024, IOException -> 0x0054, TRY_ENTER, TRY_LEAVE, TryCatch #3 {IOException -> 0x0054, blocks: (B:7:0x001d, B:11:0x0029, B:15:0x0034, B:20:0x0040), top: B:61:0x001d, outer: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readLibraryConfig() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 263
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.personalinfolist.PersonalInfoListModule.readLibraryConfig():void");
    }
}