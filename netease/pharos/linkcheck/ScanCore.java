package com.netease.pharos.linkcheck;

import android.text.TextUtils;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.protocolCheck.ProtocolCheckListener;
import com.netease.pharos.protocolCheck.ProtocolCheckProxy;
import com.netease.pharos.util.LogUtil;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class ScanCore implements Callable<Integer> {
    private static final String TAG = "ScanCore";
    private String mStyle = null;
    private ProtocolCheckListener mListener = null;
    private CycleTaskStopListener mCycleTaskStopListener = null;
    private ConfigInfoListener mConfigInfoListener = null;
    private CheckOverNotifyListener mCheckOverNotifyListener = null;
    private ProtocolCheckProxy mNetmonProxy = null;

    public void setProtocolProxyProxy(ProtocolCheckProxy protocolCheckProxy) {
        this.mNetmonProxy = protocolCheckProxy;
    }

    public void init(String str, ProtocolCheckListener protocolCheckListener, CycleTaskStopListener cycleTaskStopListener, ConfigInfoListener configInfoListener, CheckOverNotifyListener checkOverNotifyListener) {
        this.mStyle = str;
        this.mListener = protocolCheckListener;
        this.mCycleTaskStopListener = cycleTaskStopListener;
        this.mConfigInfoListener = configInfoListener;
        this.mCheckOverNotifyListener = checkOverNotifyListener;
    }

    private String getFinalDest(JSONArray jSONArray, String str) throws JSONException {
        String str2 = null;
        if (jSONArray != null && jSONArray.length() > 0) {
            int length = jSONArray.length();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < length; i++) {
                try {
                    String string = jSONArray.getString(i);
                    if (!TextUtils.isEmpty(string)) {
                        if (string.contains(".")) {
                            arrayList2.add(string);
                        } else {
                            arrayList.add(string);
                        }
                    }
                } catch (Exception unused) {
                }
            }
            if (DeviceInfo.getInstance().isSupportIpV6() && !arrayList.isEmpty()) {
                str2 = (String) arrayList.get(new Random().nextInt(arrayList.size()));
            }
            if (str2 == null) {
                int iNextInt = new Random().nextInt(arrayList2.size());
                LogUtil.i(TAG, "ScanCore [getFinalDest] num=" + iNextInt);
                str2 = (String) arrayList2.get(iNextInt);
            }
        }
        if (!TextUtils.isEmpty(str2) || TextUtils.isEmpty(str)) {
            str = str2;
        }
        LogUtil.i(TAG, "ScanCore [getFinalDest] result=" + str);
        return str;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x0069  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x005f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int startOnceNapIcmp() throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 283
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.linkcheck.ScanCore.startOnceNapIcmp():int");
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x005e A[Catch: Exception -> 0x007d, TryCatch #0 {Exception -> 0x007d, blocks: (B:22:0x0058, B:24:0x005e, B:25:0x0062, B:27:0x0068), top: B:62:0x0058 }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0068 A[Catch: Exception -> 0x007d, TRY_LEAVE, TryCatch #0 {Exception -> 0x007d, blocks: (B:22:0x0058, B:24:0x005e, B:25:0x0062, B:27:0x0068), top: B:62:0x0058 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0075 A[Catch: Exception -> 0x007b, TRY_LEAVE, TryCatch #1 {Exception -> 0x007b, blocks: (B:30:0x006f, B:32:0x0075), top: B:64:0x006f }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x009d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x009e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int startOnceRapIcmp() throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 332
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.linkcheck.ScanCore.startOnceRapIcmp():int");
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0065 A[Catch: Exception -> 0x00ea, TryCatch #1 {Exception -> 0x00ea, blocks: (B:22:0x005f, B:24:0x0065, B:26:0x006b, B:28:0x0071, B:29:0x0075, B:31:0x007b, B:33:0x0081, B:35:0x0087, B:37:0x008d, B:39:0x0093, B:40:0x0097, B:42:0x009d, B:45:0x00a6), top: B:93:0x005f }] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x006a  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0071 A[Catch: Exception -> 0x00ea, TryCatch #1 {Exception -> 0x00ea, blocks: (B:22:0x005f, B:24:0x0065, B:26:0x006b, B:28:0x0071, B:29:0x0075, B:31:0x007b, B:33:0x0081, B:35:0x0087, B:37:0x008d, B:39:0x0093, B:40:0x0097, B:42:0x009d, B:45:0x00a6), top: B:93:0x005f }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x007b A[Catch: Exception -> 0x00ea, TryCatch #1 {Exception -> 0x00ea, blocks: (B:22:0x005f, B:24:0x0065, B:26:0x006b, B:28:0x0071, B:29:0x0075, B:31:0x007b, B:33:0x0081, B:35:0x0087, B:37:0x008d, B:39:0x0093, B:40:0x0097, B:42:0x009d, B:45:0x00a6), top: B:93:0x005f }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0087 A[Catch: Exception -> 0x00ea, TryCatch #1 {Exception -> 0x00ea, blocks: (B:22:0x005f, B:24:0x0065, B:26:0x006b, B:28:0x0071, B:29:0x0075, B:31:0x007b, B:33:0x0081, B:35:0x0087, B:37:0x008d, B:39:0x0093, B:40:0x0097, B:42:0x009d, B:45:0x00a6), top: B:93:0x005f }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0093 A[Catch: Exception -> 0x00ea, TryCatch #1 {Exception -> 0x00ea, blocks: (B:22:0x005f, B:24:0x0065, B:26:0x006b, B:28:0x0071, B:29:0x0075, B:31:0x007b, B:33:0x0081, B:35:0x0087, B:37:0x008d, B:39:0x0093, B:40:0x0097, B:42:0x009d, B:45:0x00a6), top: B:93:0x005f }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00b1  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x00bc A[Catch: Exception -> 0x00e7, TryCatch #2 {Exception -> 0x00e7, blocks: (B:49:0x00b2, B:51:0x00bc, B:53:0x00c4), top: B:95:0x00b2 }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x013a A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x01a5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int startOnceRapTransfer() throws org.json.JSONException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 434
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.linkcheck.ScanCore.startOnceRapTransfer():int");
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0063 A[Catch: Exception -> 0x00b3, TryCatch #3 {Exception -> 0x00b3, blocks: (B:22:0x005d, B:24:0x0063, B:25:0x0067, B:27:0x006d, B:29:0x0073, B:31:0x0079, B:33:0x007f, B:35:0x0085, B:36:0x0089, B:38:0x0093, B:40:0x009b, B:44:0x00ac), top: B:76:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006d A[Catch: Exception -> 0x00b3, TryCatch #3 {Exception -> 0x00b3, blocks: (B:22:0x005d, B:24:0x0063, B:25:0x0067, B:27:0x006d, B:29:0x0073, B:31:0x0079, B:33:0x007f, B:35:0x0085, B:36:0x0089, B:38:0x0093, B:40:0x009b, B:44:0x00ac), top: B:76:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0079 A[Catch: Exception -> 0x00b3, TryCatch #3 {Exception -> 0x00b3, blocks: (B:22:0x005d, B:24:0x0063, B:25:0x0067, B:27:0x006d, B:29:0x0073, B:31:0x0079, B:33:0x007f, B:35:0x0085, B:36:0x0089, B:38:0x0093, B:40:0x009b, B:44:0x00ac), top: B:76:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0085 A[Catch: Exception -> 0x00b3, TryCatch #3 {Exception -> 0x00b3, blocks: (B:22:0x005d, B:24:0x0063, B:25:0x0067, B:27:0x006d, B:29:0x0073, B:31:0x0079, B:33:0x007f, B:35:0x0085, B:36:0x0089, B:38:0x0093, B:40:0x009b, B:44:0x00ac), top: B:76:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0093 A[Catch: Exception -> 0x00b3, TryCatch #3 {Exception -> 0x00b3, blocks: (B:22:0x005d, B:24:0x0063, B:25:0x0067, B:27:0x006d, B:29:0x0073, B:31:0x0079, B:33:0x007f, B:35:0x0085, B:36:0x0089, B:38:0x0093, B:40:0x009b, B:44:0x00ac), top: B:76:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x017b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int startOnceRapUdp() throws org.json.JSONException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 409
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.linkcheck.ScanCore.startOnceRapUdp():int");
    }

    public void startOnceRapMtr() throws JSONException {
        boolean z;
        boolean z2;
        LogUtil.i(TAG, "RapMtr \u63a2\u6d4b");
        boolean enable = RegionConfigInfo.getInstance().getEnable();
        int interval = RegionConfigInfo.getInstance().getInterval();
        JSONObject rapMtr = RegionConfigInfo.getInstance().getRapMtr();
        boolean z3 = false;
        if (rapMtr != null) {
            try {
                z = rapMtr.has("enable") ? rapMtr.getBoolean("enable") : false;
                try {
                    z2 = rapMtr.has("cycle") ? rapMtr.getBoolean("cycle") : false;
                    z3 = z;
                } catch (JSONException e) {
                    e = e;
                    e.printStackTrace();
                    this.mCheckOverNotifyListener.callBack("rap_mtr");
                    if (enable) {
                    }
                    this.mCheckOverNotifyListener.callBack("rap_mtr");
                    LogUtil.i(TAG, "enable == 0, \u4e0d\u6267\u884c");
                }
            } catch (JSONException e2) {
                e = e2;
                z = false;
            }
        } else {
            z2 = false;
        }
        z = z3;
        z3 = z2;
        this.mCheckOverNotifyListener.callBack("rap_mtr");
        if (enable || !z) {
            this.mCheckOverNotifyListener.callBack("rap_mtr");
            LogUtil.i(TAG, "enable == 0, \u4e0d\u6267\u884c");
        } else if (interval >= 1 && interval <= 60 && z3) {
            LogUtil.i(TAG, "\u5468\u671f\u5904\u7406");
        } else {
            LogUtil.i(TAG, "\u4e00\u6b21\u6027\u5904\u7406");
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0064 A[Catch: Exception -> 0x00d0, TryCatch #0 {Exception -> 0x00d0, blocks: (B:22:0x005e, B:24:0x0064, B:25:0x0068, B:27:0x006e, B:29:0x0074, B:31:0x007a, B:33:0x0080, B:35:0x0086, B:36:0x008a, B:38:0x0090, B:41:0x0099), top: B:81:0x005e }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006e A[Catch: Exception -> 0x00d0, TryCatch #0 {Exception -> 0x00d0, blocks: (B:22:0x005e, B:24:0x0064, B:25:0x0068, B:27:0x006e, B:29:0x0074, B:31:0x007a, B:33:0x0080, B:35:0x0086, B:36:0x008a, B:38:0x0090, B:41:0x0099), top: B:81:0x005e }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x007a A[Catch: Exception -> 0x00d0, TryCatch #0 {Exception -> 0x00d0, blocks: (B:22:0x005e, B:24:0x0064, B:25:0x0068, B:27:0x006e, B:29:0x0074, B:31:0x007a, B:33:0x0080, B:35:0x0086, B:36:0x008a, B:38:0x0090, B:41:0x0099), top: B:81:0x005e }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0086 A[Catch: Exception -> 0x00d0, TryCatch #0 {Exception -> 0x00d0, blocks: (B:22:0x005e, B:24:0x0064, B:25:0x0068, B:27:0x006e, B:29:0x0074, B:31:0x007a, B:33:0x0080, B:35:0x0086, B:36:0x008a, B:38:0x0090, B:41:0x0099), top: B:81:0x005e }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00a3  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00ae A[Catch: Exception -> 0x00ce, TryCatch #1 {Exception -> 0x00ce, blocks: (B:45:0x00a4, B:47:0x00ae, B:49:0x00b6, B:53:0x00c7), top: B:83:0x00a4 }] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x011f A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0184  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int startOnceSapTransfer() throws org.json.JSONException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 418
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.linkcheck.ScanCore.startOnceSapTransfer():int");
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0063 A[Catch: Exception -> 0x00b3, TryCatch #3 {Exception -> 0x00b3, blocks: (B:22:0x005d, B:24:0x0063, B:25:0x0067, B:27:0x006d, B:29:0x0073, B:31:0x0079, B:33:0x007f, B:35:0x0085, B:36:0x0089, B:38:0x0093, B:40:0x009b, B:44:0x00ac), top: B:76:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006d A[Catch: Exception -> 0x00b3, TryCatch #3 {Exception -> 0x00b3, blocks: (B:22:0x005d, B:24:0x0063, B:25:0x0067, B:27:0x006d, B:29:0x0073, B:31:0x0079, B:33:0x007f, B:35:0x0085, B:36:0x0089, B:38:0x0093, B:40:0x009b, B:44:0x00ac), top: B:76:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0079 A[Catch: Exception -> 0x00b3, TryCatch #3 {Exception -> 0x00b3, blocks: (B:22:0x005d, B:24:0x0063, B:25:0x0067, B:27:0x006d, B:29:0x0073, B:31:0x0079, B:33:0x007f, B:35:0x0085, B:36:0x0089, B:38:0x0093, B:40:0x009b, B:44:0x00ac), top: B:76:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0085 A[Catch: Exception -> 0x00b3, TryCatch #3 {Exception -> 0x00b3, blocks: (B:22:0x005d, B:24:0x0063, B:25:0x0067, B:27:0x006d, B:29:0x0073, B:31:0x0079, B:33:0x007f, B:35:0x0085, B:36:0x0089, B:38:0x0093, B:40:0x009b, B:44:0x00ac), top: B:76:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0093 A[Catch: Exception -> 0x00b3, TryCatch #3 {Exception -> 0x00b3, blocks: (B:22:0x005d, B:24:0x0063, B:25:0x0067, B:27:0x006d, B:29:0x0073, B:31:0x0079, B:33:0x007f, B:35:0x0085, B:36:0x0089, B:38:0x0093, B:40:0x009b, B:44:0x00ac), top: B:76:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x015b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int startOnceSapUdp() throws org.json.JSONException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 377
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.linkcheck.ScanCore.startOnceSapUdp():int");
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0058 A[Catch: Exception -> 0x005d, TRY_LEAVE, TryCatch #1 {Exception -> 0x005d, blocks: (B:22:0x0052, B:24:0x0058), top: B:50:0x0052 }] */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e4  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int startOnceResolve() throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 258
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.linkcheck.ScanCore.startOnceResolve():int");
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.concurrent.Callable
    public Integer call() throws Exception {
        int iStartOnceResolve;
        LogUtil.i(TAG, "mStyle=" + this.mStyle);
        if (this.mStyle.equals("nap_icmp")) {
            iStartOnceResolve = startOnceNapIcmp();
        } else if (this.mStyle.equals("rap_icmp")) {
            iStartOnceResolve = startOnceRapIcmp();
        } else if (this.mStyle.equals("rap_udp")) {
            iStartOnceResolve = startOnceRapUdp();
        } else if (this.mStyle.equals("rap_transfer")) {
            iStartOnceResolve = startOnceRapTransfer();
        } else {
            if (this.mStyle.equals("rap_mtr")) {
                startOnceRapMtr();
            } else if (this.mStyle.equals("sap_udp")) {
                startOnceSapUdp();
            } else if (this.mStyle.equals("sap_transfer")) {
                iStartOnceResolve = startOnceSapTransfer();
            } else if (this.mStyle.equals("resolve")) {
                iStartOnceResolve = startOnceResolve();
            }
            iStartOnceResolve = 0;
        }
        return Integer.valueOf(iStartOnceResolve);
    }
}