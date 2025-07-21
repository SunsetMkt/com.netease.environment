package com.netease.pharos.qos;

import android.text.TextUtils;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.pharos.Const;
import com.netease.pharos.network.NetUtil;
import com.netease.pharos.network.NetworkDealer;
import com.netease.pharos.util.LogUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class Qos {
    private static final String TAG = "Qos";
    private static boolean mIsCycleQosOpen = true;
    private final ArrayList<String> mFirstQosIpList = new ArrayList<>();
    private JSONArray mIps = new JSONArray();
    private long mDuration = 0;
    private final long mValidity = 0;
    private final boolean hasQos = false;
    private String mId = null;
    private final NetworkDealer<Integer> qos_dealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.qos.Qos.2
        @Override // com.netease.pharos.network.NetworkDealer
        public void processHeader(Map<String, List<String>> map, int i, Map<String, String> map2) {
        }

        AnonymousClass2() {
        }

        @Override // com.netease.pharos.network.NetworkDealer
        public /* bridge */ /* synthetic */ Integer processContent(InputStream inputStream, int i, Map map) throws Exception {
            return processContent(inputStream, i, (Map<String, String>) map);
        }

        /* JADX WARN: Removed duplicated region for block: B:127:0x00d3  */
        @Override // com.netease.pharos.network.NetworkDealer
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.Integer processContent(java.io.InputStream r17, int r18, java.util.Map<java.lang.String, java.lang.String> r19) throws java.lang.Exception {
            /*
                Method dump skipped, instructions count: 616
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.qos.Qos.AnonymousClass2.processContent(java.io.InputStream, int, java.util.Map):java.lang.Integer");
        }
    };

    /* renamed from: com.netease.pharos.qos.Qos$1 */
    class AnonymousClass1 implements Runnable {
        final /* synthetic */ int val$result;

        AnonymousClass1(int i) {
            i = i;
        }

        /* JADX WARN: Removed duplicated region for block: B:33:0x006d  */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() throws org.json.JSONException, java.lang.InterruptedException, java.lang.NumberFormatException {
            /*
                r8 = this;
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                java.lang.String r1 = "Qos [cycleQos2] result="
                r0.<init>(r1)
                int r1 = r2
                r0.append(r1)
                java.lang.String r1 = ", mIps="
                r0.append(r1)
                com.netease.pharos.qos.Qos r1 = com.netease.pharos.qos.Qos.this
                org.json.JSONArray r1 = com.netease.pharos.qos.Qos.access$000(r1)
                java.lang.String r1 = r1.toString()
                r0.append(r1)
                java.lang.String r0 = r0.toString()
                java.lang.String r1 = "Qos"
                com.netease.pharos.util.LogUtil.i(r1, r0)
                com.netease.pharos.qos.QosStatus r0 = com.netease.pharos.qos.QosStatus.getInstance()
                long r2 = r0.getMinExpire()
                int r0 = r2
                java.lang.String r4 = "qos"
                java.lang.String r5 = "Qos [cycleQos2] \u7761\u7720\u65f6\u95f4\u7ed3\u675f\uff0c\u81ea\u52a8\u8fdb\u5165\u5468\u671f"
                if (r0 != 0) goto L6d
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                java.lang.String r6 = "Qos [cycleQos2] \u53d1\u8d77\u52a0\u901f\u540e\uff0csleepTime="
                r0.<init>(r6)
                r0.append(r2)
                java.lang.String r0 = r0.toString()
                com.netease.pharos.util.LogUtil.i(r1, r0)
                r6 = 0
                int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                if (r0 <= 0) goto L6d
                java.lang.Thread.sleep(r2)     // Catch: java.lang.InterruptedException -> L5b
                com.netease.pharos.util.LogUtil.i(r1, r5)     // Catch: java.lang.InterruptedException -> L5b
                com.netease.pharos.qos.Qos r0 = com.netease.pharos.qos.Qos.this     // Catch: java.lang.InterruptedException -> L5b
                r0.qos(r4)     // Catch: java.lang.InterruptedException -> L5b
                r0 = 0
                goto L6e
            L5b:
                r0 = move-exception
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = "Qos [cycleQos2] InterruptedException1="
                r2.<init>(r3)
                r2.append(r0)
                java.lang.String r0 = r2.toString()
                com.netease.pharos.util.LogUtil.w(r1, r0)
            L6d:
                r0 = 1
            L6e:
                if (r0 == 0) goto L9b
                java.lang.String r0 = "Qos [cycleQos2] \u53d1\u751f\u5f02\u5e38"
                com.netease.pharos.util.LogUtil.i(r1, r0)     // Catch: java.lang.InterruptedException -> L89
                java.lang.String r0 = "Qos [cycleQos2] \u4f11\u772030\u5206\u949f"
                com.netease.pharos.util.LogUtil.i(r1, r0)     // Catch: java.lang.InterruptedException -> L89
                r2 = 1800000(0x1b7740, double:8.89318E-318)
                java.lang.Thread.sleep(r2)     // Catch: java.lang.InterruptedException -> L89
                com.netease.pharos.util.LogUtil.i(r1, r5)     // Catch: java.lang.InterruptedException -> L89
                com.netease.pharos.qos.Qos r0 = com.netease.pharos.qos.Qos.this     // Catch: java.lang.InterruptedException -> L89
                r0.qos(r4)     // Catch: java.lang.InterruptedException -> L89
                goto L9b
            L89:
                r0 = move-exception
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                java.lang.String r3 = "Qos [cycleQos2] InterruptedException2="
                r2.<init>(r3)
                r2.append(r0)
                java.lang.String r0 = r2.toString()
                com.netease.pharos.util.LogUtil.w(r1, r0)
            L9b:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.qos.Qos.AnonymousClass1.run():void");
        }
    }

    public void cycleQos2(int i) {
        new Thread(new Runnable() { // from class: com.netease.pharos.qos.Qos.1
            final /* synthetic */ int val$result;

            AnonymousClass1(int i2) {
                i = i2;
            }

            @Override // java.lang.Runnable
            public void run() throws JSONException, InterruptedException, NumberFormatException {
                /*
                    this = this;
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    java.lang.String r1 = "Qos [cycleQos2] result="
                    r0.<init>(r1)
                    int r1 = r2
                    r0.append(r1)
                    java.lang.String r1 = ", mIps="
                    r0.append(r1)
                    com.netease.pharos.qos.Qos r1 = com.netease.pharos.qos.Qos.this
                    org.json.JSONArray r1 = com.netease.pharos.qos.Qos.access$000(r1)
                    java.lang.String r1 = r1.toString()
                    r0.append(r1)
                    java.lang.String r0 = r0.toString()
                    java.lang.String r1 = "Qos"
                    com.netease.pharos.util.LogUtil.i(r1, r0)
                    com.netease.pharos.qos.QosStatus r0 = com.netease.pharos.qos.QosStatus.getInstance()
                    long r2 = r0.getMinExpire()
                    int r0 = r2
                    java.lang.String r4 = "qos"
                    java.lang.String r5 = "Qos [cycleQos2] \u7761\u7720\u65f6\u95f4\u7ed3\u675f\uff0c\u81ea\u52a8\u8fdb\u5165\u5468\u671f"
                    if (r0 != 0) goto L6d
                    java.lang.StringBuilder r0 = new java.lang.StringBuilder
                    java.lang.String r6 = "Qos [cycleQos2] \u53d1\u8d77\u52a0\u901f\u540e\uff0csleepTime="
                    r0.<init>(r6)
                    r0.append(r2)
                    java.lang.String r0 = r0.toString()
                    com.netease.pharos.util.LogUtil.i(r1, r0)
                    r6 = 0
                    int r0 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
                    if (r0 <= 0) goto L6d
                    java.lang.Thread.sleep(r2)     // Catch: java.lang.InterruptedException -> L5b
                    com.netease.pharos.util.LogUtil.i(r1, r5)     // Catch: java.lang.InterruptedException -> L5b
                    com.netease.pharos.qos.Qos r0 = com.netease.pharos.qos.Qos.this     // Catch: java.lang.InterruptedException -> L5b
                    r0.qos(r4)     // Catch: java.lang.InterruptedException -> L5b
                    r0 = 0
                    goto L6e
                L5b:
                    r0 = move-exception
                    java.lang.StringBuilder r2 = new java.lang.StringBuilder
                    java.lang.String r3 = "Qos [cycleQos2] InterruptedException1="
                    r2.<init>(r3)
                    r2.append(r0)
                    java.lang.String r0 = r2.toString()
                    com.netease.pharos.util.LogUtil.w(r1, r0)
                L6d:
                    r0 = 1
                L6e:
                    if (r0 == 0) goto L9b
                    java.lang.String r0 = "Qos [cycleQos2] \u53d1\u751f\u5f02\u5e38"
                    com.netease.pharos.util.LogUtil.i(r1, r0)     // Catch: java.lang.InterruptedException -> L89
                    java.lang.String r0 = "Qos [cycleQos2] \u4f11\u772030\u5206\u949f"
                    com.netease.pharos.util.LogUtil.i(r1, r0)     // Catch: java.lang.InterruptedException -> L89
                    r2 = 1800000(0x1b7740, double:8.89318E-318)
                    java.lang.Thread.sleep(r2)     // Catch: java.lang.InterruptedException -> L89
                    com.netease.pharos.util.LogUtil.i(r1, r5)     // Catch: java.lang.InterruptedException -> L89
                    com.netease.pharos.qos.Qos r0 = com.netease.pharos.qos.Qos.this     // Catch: java.lang.InterruptedException -> L89
                    r0.qos(r4)     // Catch: java.lang.InterruptedException -> L89
                    goto L9b
                L89:
                    r0 = move-exception
                    java.lang.StringBuilder r2 = new java.lang.StringBuilder
                    java.lang.String r3 = "Qos [cycleQos2] InterruptedException2="
                    r2.<init>(r3)
                    r2.append(r0)
                    java.lang.String r0 = r2.toString()
                    com.netease.pharos.util.LogUtil.w(r1, r0)
                L9b:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.qos.Qos.AnonymousClass1.run():void");
            }
        }).start();
    }

    public int pharosqosexec(JSONArray jSONArray, long j) throws JSONException {
        LogUtil.i(TAG, "Qos [pharosqosexec] start");
        if (jSONArray == null || jSONArray.length() < 0 || j < 0) {
            LogUtil.w(TAG, "Qos [pharosqosexec] param error1");
            return 14;
        }
        LogUtil.i(TAG, "Qos [pharosqosexec] ips=" + jSONArray + ", duration=" + j);
        StringBuilder sb = new StringBuilder("Qos [pharosqosexec] QosStatus result11=");
        sb.append(QosStatus.getInstance().getResult());
        LogUtil.i(TAG, sb.toString());
        QosStatus.getInstance().cleanTimeOutIps();
        LogUtil.i(TAG, "Qos [pharosqosexec] QosStatus result22=" + QosStatus.getInstance().getResult());
        JSONArray jSONArray2 = new JSONArray();
        ArrayList<String> allIp = QosStatus.getInstance().getAllIp();
        this.mIps = new JSONArray();
        if (allIp != null && allIp.size() > 0) {
            Iterator<String> it = allIp.iterator();
            while (it.hasNext()) {
                this.mIps.put(it.next());
            }
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            if (!allIp.contains(jSONArray.optString(i))) {
                this.mIps.put(jSONArray.optString(i));
                jSONArray2.put(jSONArray.optString(i));
            }
        }
        LogUtil.i(TAG, "Qos [pharosqosexec] QosStatus mIps=" + this.mIps.toString());
        this.mDuration = j;
        LogUtil.i(TAG, "Qos [pharosqosexec] QosStatus result=" + QosStatus.getInstance().getResult());
        if (!QosStatus.getInstance().has(jSONArray)) {
            if (isFirstQos(this.mIps)) {
                LogUtil.i(TAG, "Qos [pharosqosexec] \u9996\u6b21\u8fdb\u5165\u52a0\u901f");
                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                    String strOptString = jSONArray2.optString(i2);
                    addToFirstQos(jSONArray2);
                    QosStatus.getInstance().setIp(strOptString);
                }
                LogUtil.i(TAG, "Qos [pharosqosexec] qos\u524d\uff0c\u7ed3\u679c=" + QosStatus.getInstance().getResult().toString());
                LogUtil.i(TAG, "Qos [pharosqosexec] getResult=" + Qos4GProxy.getInstance().getResult().toString());
                JSONObject result = Qos4GProxy.getInstance().getResult();
                if (result != null && result.has("qos_effective")) {
                    if (result.optBoolean("qos_effective")) {
                        qos("qos");
                        return 11;
                    }
                    LogUtil.w(TAG, "Qos [pharosqosexec] qos_effective = false");
                    return 11;
                }
                LogUtil.w(TAG, "Qos [pharosqosexec] \u63a2\u6d4b\u73af\u8282\u4e2dqos\u90e8\u5206\u51fa\u95ee\u9898\uff0c\u65e0\u6cd5\u5f97\u5230qos\u7ed3\u679c");
                return 11;
            }
            LogUtil.w(TAG, "Qos [pharosqosexec] \u9996\u6b21\u52a0\u901f\u8fdb\u884c\u4e2d");
            return 11;
        }
        LogUtil.w(TAG, "Qos [pharosqosexec] \u6240\u6709ip\u5df2\u5904\u4e8e\u52a0\u901f\u4e2d");
        return 11;
    }

    public int pharosqoscancel(JSONArray jSONArray) throws JSONException {
        LogUtil.i(TAG, "Qos [pharosqoscancel] start");
        if (jSONArray == null || jSONArray.length() < 0) {
            LogUtil.w(TAG, "Qos [pharosqoscancel] param error1");
            return 14;
        }
        LogUtil.i(TAG, "Qos [pharosqoscancel] ips=" + jSONArray);
        QosStatus.getInstance().cleanTimeOutIps();
        ArrayList<String> allIp = QosStatus.getInstance().getAllIp();
        this.mIps = new JSONArray();
        for (int i = 0; i < jSONArray.length(); i++) {
            int i2 = 0;
            while (true) {
                if (i2 >= allIp.size()) {
                    break;
                }
                if (allIp.get(i2).equals(jSONArray.optString(i))) {
                    QosStatus.getInstance().setValidity(jSONArray.optString(i), 0L);
                    break;
                }
                i2++;
            }
        }
        return 11;
    }

    private boolean isFirstQos(JSONArray jSONArray) {
        for (int i = 0; i < jSONArray.length(); i++) {
            String strOptString = jSONArray.optString(i);
            LogUtil.i(TAG, "isFirstQos mFirstQosIpList=" + this.mFirstQosIpList.listIterator() + ", ip=" + strOptString);
            if (!this.mFirstQosIpList.contains(strOptString)) {
                LogUtil.i(TAG, "isFirstQos mFirstQosIpList=" + this.mFirstQosIpList.listIterator() + ", ip1111=" + strOptString);
                return true;
            }
        }
        return false;
    }

    private void addToFirstQos(JSONArray jSONArray) {
        for (int i = 0; i < jSONArray.length(); i++) {
            String strOptString = jSONArray.optString(i);
            if (!this.mFirstQosIpList.contains(strOptString)) {
                this.mFirstQosIpList.add(strOptString);
            }
        }
    }

    public boolean ismIsCycleQosOpen() {
        return mIsCycleQosOpen;
    }

    public void setmIsCycleQosOpen(boolean z) {
        mIsCycleQosOpen = z;
    }

    /* JADX WARN: Removed duplicated region for block: B:88:0x00c0 A[Catch: JSONException -> 0x00d1, TryCatch #0 {JSONException -> 0x00d1, blocks: (B:86:0x00b1, B:88:0x00c0, B:90:0x00c9, B:89:0x00c4), top: B:98:0x00b1 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x00c4 A[Catch: JSONException -> 0x00d1, TryCatch #0 {JSONException -> 0x00d1, blocks: (B:86:0x00b1, B:88:0x00c0, B:90:0x00c9, B:89:0x00c4), top: B:98:0x00b1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int qos(java.lang.String r15) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 259
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.qos.Qos.qos(java.lang.String):int");
    }

    public int qos_post(String str, String str2, String str3) throws JSONException {
        int iIntValue;
        LogUtil.stepLog("Qos [qos_post] start");
        LogUtil.i(TAG, "Qos [qos_post]---\u53c2\u6570 info=" + str + ", url=" + str2);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogUtil.i(TAG, "Qos [qos_post]---\u53c2\u6570\u9519\u8bef");
            return 14;
        }
        String str4 = "https://" + str2;
        LogUtil.i(TAG, "Qos [qos_post]---\u5904\u7406\u540e\u7684url=" + str4);
        HashMap map = new HashMap();
        map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
        HashMap map2 = new HashMap();
        map2.put("post_content", str);
        try {
            JSONObject jSONObject = new JSONObject(str);
            jSONObject.put(ResIdReader.RES_TYPE_STYLE, str3);
            map2.put("extra_data", jSONObject);
            LogUtil.stepLog("Qos [qos_post] pParams=" + map2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(str4)) {
            iIntValue = 11;
        } else {
            try {
                String str5 = "cancel_qos".equals(str3) ? Const.METHOD_DELETE : "POST";
                LogUtil.stepLog("Qos [qos_post] style=".concat(str5));
                iIntValue = ((Integer) NetUtil.doHttpReq(str4, map2, str5, map, this.qos_dealer)).intValue();
            } catch (IOException e2) {
                LogUtil.stepLog("Qos [qos_post] IOException=" + e2);
            }
        }
        LogUtil.i(TAG, "Qos [qos_post] \u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }

    /* renamed from: com.netease.pharos.qos.Qos$2 */
    class AnonymousClass2 implements NetworkDealer<Integer> {
        @Override // com.netease.pharos.network.NetworkDealer
        public void processHeader(Map<String, List<String>> map, int i, Map<String, String> map2) {
        }

        AnonymousClass2() {
        }

        @Override // com.netease.pharos.network.NetworkDealer
        public /* bridge */ /* synthetic */ Integer processContent(InputStream inputStream, int i, Map map) throws Exception {
            return processContent(inputStream, i, (Map<String, String>) map);
        }

        @Override // com.netease.pharos.network.NetworkDealer
        public Integer processContent(InputStream v, int v2, Map<String, String> v3) throws Exception {
            /*
                Method dump skipped, instructions count: 616
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.qos.Qos.AnonymousClass2.processContent(java.io.InputStream, int, java.util.Map):java.lang.Integer");
        }
    }

    public int clean(JSONArray jSONArray) throws JSONException {
        LogUtil.i(TAG, "Qos [clean] \u53d6\u6d88\u52a0\u901f ipArray=" + jSONArray.toString());
        int iPharosqoscancel = pharosqoscancel(jSONArray);
        if (iPharosqoscancel == 0) {
            mIsCycleQosOpen = false;
            for (int i = 0; i < jSONArray.length(); i++) {
                LogUtil.i(TAG, "Qos [clean] \u53d6\u6d88\u52a0\u901f \u6e05\u7406\u6570\u636e ip=" + jSONArray.optString(i));
                QosStatus.getInstance().cleanIp(jSONArray.optString(i));
            }
        }
        return iPharosqoscancel;
    }
}