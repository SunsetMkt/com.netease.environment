package com.netease.pharos.qos;

import android.text.TextUtils;
import android.util.Base64;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.modules.clientlog.constant.ClientLogConstant;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.pharos.Const;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.linkcheck.Result;
import com.netease.pharos.network.NetUtil;
import com.netease.pharos.network.NetworkDealer;
import com.netease.pharos.network.NetworkStatus;
import com.netease.pharos.report.ReportProxy;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class QosCore {
    private static final String TAG = "QosCore";
    private JSONObject mSource = null;
    private final JSONObject mResult = new JSONObject();
    private final JSONObject mQosResult = new JSONObject();
    private boolean mEnable = false;
    private boolean mCycle = false;
    private String mDest = null;
    private String mPhoneUrl = null;
    private String mPhone = null;
    private boolean mIsFitthreshold = false;
    private final boolean mIsCycle = false;
    private int mStauts = 0;
    private final NetworkDealer<Integer> qos_dealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.qos.QosCore.1
        @Override // com.netease.pharos.network.NetworkDealer
        public void processHeader(Map<String, List<String>> map, int i, Map<String, String> map2) {
        }

        AnonymousClass1() {
        }

        @Override // com.netease.pharos.network.NetworkDealer
        public /* bridge */ /* synthetic */ Integer processContent(InputStream inputStream, int i, Map map) throws Exception {
            return processContent(inputStream, i, (Map<String, String>) map);
        }

        @Override // com.netease.pharos.network.NetworkDealer
        public Integer processContent(InputStream inputStream, int i, Map<String, String> map) throws Exception {
            int i2;
            JSONObject jSONObjectOptJSONObject;
            LogUtil.stepLog("\u53d1\u8d77 QOS \u52a0\u901f---\u89e3\u6790\u5185\u5bb9");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            LogUtil.stepLog("\u53d1\u8d77 QOS \u52a0\u901f---\u89e3\u6790\u5185\u5bb9=" + ((Object) sb));
            if (TextUtils.isEmpty(sb.toString())) {
                i2 = 11;
            } else {
                try {
                    JSONObject jSONObject = new JSONObject(sb.toString());
                    int iOptInt = jSONObject.has("resend_flag") ? jSONObject.optInt("resend_flag") : -1;
                    String strOptString = null;
                    String strOptString2 = jSONObject.has("code") ? jSONObject.optString("code") : null;
                    if (jSONObject.has("data") && (jSONObjectOptJSONObject = jSONObject.optJSONObject("data")) != null && jSONObjectOptJSONObject.has("time")) {
                        strOptString = jSONObjectOptJSONObject.optString("time");
                    }
                    if (iOptInt == 1) {
                        QosCore.this.mResult.put("rap_qos_status", "11");
                    } else if (!TextUtils.isEmpty(strOptString2)) {
                        QosCore.this.mResult.put("rap_qos_status", strOptString2);
                    }
                    if (!TextUtils.isEmpty(strOptString)) {
                        QosCore.this.mResult.put("rap_qos_expire", strOptString);
                    }
                    QosCore.this.mStauts = 1;
                    LogUtil.i(QosCore.TAG, "\u53d1\u8d77 QOS \u52a0\u901f---\u6700\u7ec8\u8f93\u51fa\u7ed3\u679c  mResult=" + QosCore.this.mResult.toString());
                    i2 = 0;
                } catch (JSONException e) {
                    LogUtil.w(QosCore.TAG, "\u53d1\u8d77 QOS \u52a0\u901f---\u89e3\u6790\u5185\u5bb9  JSONException=" + e);
                }
            }
            QosCore.this.mStauts = 1;
            return Integer.valueOf(i2);
        }
    };
    private final NetworkDealer<Integer> dealer = new NetworkDealer<Integer>() { // from class: com.netease.pharos.qos.QosCore.2
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
        public Integer processContent(InputStream inputStream, int i, Map<String, String> map) throws Exception {
            int iQos;
            LogUtil.stepLog("\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u89e3\u6790\u5185\u5bb9");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            LogUtil.stepLog("\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u89e3\u6790\u5185\u5bb9=" + ((Object) sb));
            if (TextUtils.isEmpty(sb.toString())) {
                iQos = 11;
            } else {
                try {
                    JSONObject jSONObject = new JSONObject(sb.toString());
                    if (jSONObject.has("result")) {
                        QosCore.this.mPhone = jSONObject.optString("result");
                    }
                    iQos = 0;
                } catch (JSONException e) {
                    LogUtil.w(QosCore.TAG, "\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u89e3\u6790\u5185\u5bb9  JSONException=" + e);
                }
            }
            if (!TextUtils.isEmpty(QosCore.this.mPhone)) {
                LogUtil.i(QosCore.TAG, "\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u89e3\u6790\u5185\u5bb9  phone=" + QosCore.this.mPhone);
                iQos = QosCore.this.qos();
            } else {
                LogUtil.w(QosCore.TAG, "\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u89e3\u6790\u5185\u5bb9  phone \u9519\u8bef\uff0c\u4e0d\u53d1\u8d77Qos");
            }
            return Integer.valueOf(iQos);
        }
    };

    public void init(JSONObject jSONObject) throws JSONException {
        this.mSource = jSONObject;
        try {
            this.mResult.put("rap_qos_status", Const.QOS_DEFAULT);
            this.mResult.put("rap_qos_expire", "0");
        } catch (JSONException e) {
            LogUtil.i(TAG, "QosCore [init] JSONException=" + e);
        }
        String udid = DeviceInfo.getInstance().getUdid();
        String localIp = Util.getLocalIp();
        String ipaddr = DeviceInfo.getInstance().getIpaddr();
        LogUtil.i(TAG, "QosCore [qos] udid=" + udid + ", ip=" + localIp + ", ip_public=" + ipaddr + ", mPhone=" + this.mPhone + ", mDest=" + this.mDest);
        try {
            this.mQosResult.put(ResIdReader.RES_TYPE_ID, udid);
            this.mQosResult.put("ip", localIp);
            this.mQosResult.put("ip_public", ipaddr);
            this.mQosResult.put("phone", this.mPhone);
        } catch (JSONException e2) {
            LogUtil.w(TAG, "QosCore [init] \u521d\u59cb\u5316mQosResult JSONException =" + e2);
        }
        if (this.mSource != null) {
            LogUtil.i(TAG, "QosCore \u6d4b\u8bd5\u6570\u636e= " + this.mSource);
            LogUtil.i(TAG, "mQosResult \u6570\u636e= " + this.mQosResult.toString());
            LogUtil.i(TAG, "mResult \u6570\u636e= " + this.mResult.toString());
        }
    }

    public int parse() {
        JSONObject jSONObjectOptJSONObject;
        LogUtil.i(TAG, "QosCore [parse] mStauts=" + this.mStauts);
        JSONObject jSONObject = this.mSource;
        if (jSONObject == null) {
            return 14;
        }
        if (jSONObject.has("enable")) {
            this.mEnable = this.mSource.optBoolean("enable");
        }
        if (this.mSource.has("cycle")) {
            this.mCycle = this.mSource.optBoolean("cycle");
        }
        if (this.mSource.has("dest")) {
            this.mDest = this.mSource.optString("dest");
        }
        if (this.mSource.has("isp")) {
            JSONObject jSONObjectOptJSONObject2 = this.mSource.optJSONObject("isp");
            String networkIspName = DeviceInfo.getInstance().getNetworkIspName();
            LogUtil.i(TAG, "QosCore [parse] networkIspName=" + networkIspName);
            if (!TextUtils.isEmpty(networkIspName) && jSONObjectOptJSONObject2 != null && jSONObjectOptJSONObject2.has(networkIspName)) {
                JSONObject jSONObjectOptJSONObject3 = jSONObjectOptJSONObject2.optJSONObject(networkIspName);
                LogUtil.i(TAG, "QosCore [parse] json=" + jSONObjectOptJSONObject3);
                if (jSONObjectOptJSONObject3 != null) {
                    Iterator<String> itKeys = jSONObjectOptJSONObject3.keys();
                    while (itKeys.hasNext()) {
                        String next = itKeys.next();
                        String str = DeviceInfo.getInstance().getipProvince();
                        LogUtil.i(TAG, "QosCore [parse] key=" + next + ", ip_province=" + str);
                        if (!TextUtils.isEmpty(next) && !TextUtils.isEmpty(str) && next.equals(str)) {
                            String strOptString = jSONObjectOptJSONObject3.optString(next);
                            this.mPhoneUrl = strOptString;
                            if (!TextUtils.isEmpty(strOptString)) {
                                this.mPhoneUrl = new String(Base64.decode(this.mPhoneUrl.getBytes(), 0));
                            } else {
                                this.mPhoneUrl = "";
                            }
                        }
                    }
                    if (TextUtils.isEmpty(this.mPhoneUrl) && jSONObjectOptJSONObject3.has("_all")) {
                        String strOptString2 = jSONObjectOptJSONObject3.optString("_all");
                        this.mPhoneUrl = strOptString2;
                        if (!TextUtils.isEmpty(strOptString2)) {
                            this.mPhoneUrl = new String(Base64.decode(this.mPhoneUrl.getBytes(), 0));
                        } else {
                            this.mPhoneUrl = "";
                        }
                    }
                }
            }
        }
        if (this.mSource.has("threshold") && (jSONObjectOptJSONObject = this.mSource.optJSONObject("threshold")) != null) {
            Iterator<String> itKeys2 = jSONObjectOptJSONObject.keys();
            while (true) {
                if (!itKeys2.hasNext()) {
                    break;
                }
                String next2 = itKeys2.next();
                JSONArray jSONArrayOptJSONArray = jSONObjectOptJSONObject.optJSONArray(next2);
                if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() > 0 && jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() > 1 && isThreshHold(next2, jSONArrayOptJSONArray.optInt(0), jSONArrayOptJSONArray.optInt(1))) {
                    this.mIsFitthreshold = true;
                    break;
                }
            }
        }
        LogUtil.i(TAG, "mEnable=" + this.mEnable + ", mCycle=" + this.mCycle + ", mDest=" + this.mDest + ", mPhoneUrl=" + this.mPhoneUrl + ", mIsFitthreshold=" + this.mIsFitthreshold);
        return 11;
    }

    public int checkIsNeedToQos() throws JSONException {
        LogUtil.i(TAG, "QosCore [checkIsNeedToQos] mEnable=" + this.mEnable + ", isISP()=" + isISP() + ", mIsFitthreshold=" + this.mIsFitthreshold + ", checkExpire()=" + checkExpire() + ", TextUtils.isEmpty(mPhoneUrl)=" + TextUtils.isEmpty(this.mPhoneUrl) + ", mStauts=" + this.mStauts);
        int iStart = 11;
        if (this.mStauts == 2) {
            LogUtil.i(TAG, "QosCore already start");
            return 11;
        }
        if (this.mEnable) {
            LogUtil.i(TAG, "QosCore [checkIsNeedToQos] isISP()=" + isISP());
            if (isISP()) {
                if (this.mIsFitthreshold) {
                    if (checkExpire()) {
                        if (TextUtils.isEmpty(this.mPhoneUrl)) {
                            iStart = qos();
                        } else {
                            iStart = start(this.mPhoneUrl);
                        }
                    } else {
                        this.mResult.put("rap_qos_status", "11");
                    }
                } else {
                    this.mResult.put("rap_qos_status", Const.QOS_NOT_FIT_THRESHOLD);
                }
            } else {
                this.mResult.put("rap_qos_status", Const.QOS_IS_NOT_ISP);
            }
        } else {
            this.mResult.put("rap_qos_status", Const.QOS_DEFAULT);
        }
        LogUtil.i(TAG, "Qos\u56de\u8c03\u7ed3\u679c[\u4e0d\u518d\u56de\u8c03]=" + this.mQosResult.toString());
        Result.getInstance().setmRapQosExpire(this.mResult.optString("rap_qos_expire"));
        String linkCheckResultInfo = Result.getInstance().getLinkCheckResultInfo();
        if (!TextUtils.isEmpty(linkCheckResultInfo)) {
            LogUtil.i(TAG, "Qos \u4e0a\u4f20\u5185\u5bb9=" + linkCheckResultInfo);
            ReportProxy.getInstance().report(linkCheckResultInfo);
            Result.getInstance().clean();
        }
        return iStart;
    }

    public int qos() throws JSONException {
        LogUtil.i(TAG, "QosCore [qos] \u53d1\u8d77qos\u52a0\u901f");
        try {
            this.mQosResult.put("phone", this.mPhone);
        } catch (JSONException e) {
            LogUtil.w(TAG, "QosCore [qos] \u53d1\u8d77qos\u52a0\u901f JSONException =" + e);
        }
        LogUtil.i(TAG, "QosCore [qos] mQosResult=" + this.mQosResult);
        return qos_post(this.mQosResult.toString(), this.mDest);
    }

    /* renamed from: com.netease.pharos.qos.QosCore$1 */
    class AnonymousClass1 implements NetworkDealer<Integer> {
        @Override // com.netease.pharos.network.NetworkDealer
        public void processHeader(Map<String, List<String>> map, int i, Map<String, String> map2) {
        }

        AnonymousClass1() {
        }

        @Override // com.netease.pharos.network.NetworkDealer
        public /* bridge */ /* synthetic */ Integer processContent(InputStream inputStream, int i, Map map) throws Exception {
            return processContent(inputStream, i, (Map<String, String>) map);
        }

        @Override // com.netease.pharos.network.NetworkDealer
        public Integer processContent(InputStream inputStream, int i, Map<String, String> map) throws Exception {
            int i2;
            JSONObject jSONObjectOptJSONObject;
            LogUtil.stepLog("\u53d1\u8d77 QOS \u52a0\u901f---\u89e3\u6790\u5185\u5bb9");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            LogUtil.stepLog("\u53d1\u8d77 QOS \u52a0\u901f---\u89e3\u6790\u5185\u5bb9=" + ((Object) sb));
            if (TextUtils.isEmpty(sb.toString())) {
                i2 = 11;
            } else {
                try {
                    JSONObject jSONObject = new JSONObject(sb.toString());
                    int iOptInt = jSONObject.has("resend_flag") ? jSONObject.optInt("resend_flag") : -1;
                    String strOptString = null;
                    String strOptString2 = jSONObject.has("code") ? jSONObject.optString("code") : null;
                    if (jSONObject.has("data") && (jSONObjectOptJSONObject = jSONObject.optJSONObject("data")) != null && jSONObjectOptJSONObject.has("time")) {
                        strOptString = jSONObjectOptJSONObject.optString("time");
                    }
                    if (iOptInt == 1) {
                        QosCore.this.mResult.put("rap_qos_status", "11");
                    } else if (!TextUtils.isEmpty(strOptString2)) {
                        QosCore.this.mResult.put("rap_qos_status", strOptString2);
                    }
                    if (!TextUtils.isEmpty(strOptString)) {
                        QosCore.this.mResult.put("rap_qos_expire", strOptString);
                    }
                    QosCore.this.mStauts = 1;
                    LogUtil.i(QosCore.TAG, "\u53d1\u8d77 QOS \u52a0\u901f---\u6700\u7ec8\u8f93\u51fa\u7ed3\u679c  mResult=" + QosCore.this.mResult.toString());
                    i2 = 0;
                } catch (JSONException e) {
                    LogUtil.w(QosCore.TAG, "\u53d1\u8d77 QOS \u52a0\u901f---\u89e3\u6790\u5185\u5bb9  JSONException=" + e);
                }
            }
            QosCore.this.mStauts = 1;
            return Integer.valueOf(i2);
        }
    }

    public int qos_post(String str, String str2) {
        int iIntValue;
        LogUtil.stepLog("\u53d1\u8d77 QOS \u52a0\u901f");
        LogUtil.i(TAG, "\u53d1\u8d77 QOS \u52a0\u901f---\u53c2\u6570 info=" + str + ", url=" + str2);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            LogUtil.i(TAG, "\u53d1\u8d77 QOS \u52a0\u901f---\u53c2\u6570\u9519\u8bef");
            return 14;
        }
        String str3 = "https://" + str2;
        LogUtil.i(TAG, "\u53d1\u8d77 QOS \u52a0\u901f---\u5904\u7406\u540e\u7684url=" + str3);
        HashMap map = new HashMap();
        map.put("Content-Type", ClientLogConstant.NORMAL_TYPE_VALUE);
        HashMap map2 = new HashMap();
        map2.put("post_content", str);
        if (TextUtils.isEmpty(str3)) {
            iIntValue = 11;
        } else {
            try {
                iIntValue = ((Integer) NetUtil.doHttpReq(str3, map2, "POST", map, this.qos_dealer)).intValue();
            } catch (IOException e) {
                LogUtil.stepLog("\u53d1\u8d77 QOS \u52a0\u901f [qos_post] IOException=" + e);
            }
        }
        LogUtil.stepLog("\u53d1\u8d77 QOS \u52a0\u901f---\u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }

    private boolean isISP() {
        String networkType = NetworkStatus.getInstance().getNetworkType();
        String networkIspName = DeviceInfo.getInstance().getNetworkIspName();
        DeviceInfo.getInstance().getRegion();
        LogUtil.i(TAG, "QosCore [isISP] networkIspName=" + networkIspName + ", mobile=" + networkType);
        return !"unknow".equals(networkIspName) && ConstProp.NT_AUTH_NAME_MOBILE.equals(networkType);
    }

    /* JADX WARN: Code restructure failed: missing block: B:104:0x00b7, code lost:
    
        if (r11 < 800.0d) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x00f4, code lost:
    
        if (r11 < 800.0d) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x013c, code lost:
    
        if (r5 < 800.0d) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x0180, code lost:
    
        if (r13 < 800.0d) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x01c4, code lost:
    
        if (r13 < 800.0d) goto L95;
     */
    /* JADX WARN: Code restructure failed: missing block: B:94:0x0078, code lost:
    
        if (r11 < 800.0d) goto L95;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean isThreshHold(java.lang.String r17, int r18, int r19) {
        /*
            Method dump skipped, instructions count: 464
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.qos.QosCore.isThreshHold(java.lang.String, int, int):boolean");
    }

    private boolean checkExpire() {
        long j;
        if (!this.mResult.has("rap_qos_expire")) {
            return true;
        }
        try {
            j = Long.parseLong(this.mResult.optString("rap_qos_expire")) * 1000;
        } catch (Exception e) {
            LogUtil.i(TAG, "QosCore [checkExpire] Exception=" + e);
            j = 0L;
        }
        LogUtil.i(TAG, "QosCore [checkExpire] longTime=" + j + ", System.currentTimeMillis()=" + System.currentTimeMillis());
        return 0 == j || j < System.currentTimeMillis();
    }

    /* renamed from: com.netease.pharos.qos.QosCore$2 */
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
        public Integer processContent(InputStream inputStream, int i, Map<String, String> map) throws Exception {
            int iQos;
            LogUtil.stepLog("\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u89e3\u6790\u5185\u5bb9");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
            LogUtil.stepLog("\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u89e3\u6790\u5185\u5bb9=" + ((Object) sb));
            if (TextUtils.isEmpty(sb.toString())) {
                iQos = 11;
            } else {
                try {
                    JSONObject jSONObject = new JSONObject(sb.toString());
                    if (jSONObject.has("result")) {
                        QosCore.this.mPhone = jSONObject.optString("result");
                    }
                    iQos = 0;
                } catch (JSONException e) {
                    LogUtil.w(QosCore.TAG, "\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u89e3\u6790\u5185\u5bb9  JSONException=" + e);
                }
            }
            if (!TextUtils.isEmpty(QosCore.this.mPhone)) {
                LogUtil.i(QosCore.TAG, "\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u89e3\u6790\u5185\u5bb9  phone=" + QosCore.this.mPhone);
                iQos = QosCore.this.qos();
            } else {
                LogUtil.w(QosCore.TAG, "\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u89e3\u6790\u5185\u5bb9  phone \u9519\u8bef\uff0c\u4e0d\u53d1\u8d77Qos");
            }
            return Integer.valueOf(iQos);
        }
    }

    public int start(String str) {
        LogUtil.i(TAG, "\u83b7\u53d6\u624b\u673a\u53f7\u7801 url=" + str);
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "\u83b7\u53d6\u624b\u673a\u53f7\u7801 \u53c2\u6570\u9519\u8bef");
            return 11;
        }
        int iStart = start(str, null);
        LogUtil.i(TAG, "\u83b7\u53d6\u624b\u673a\u53f7\u7801  \u666e\u901a\u8bf7\u6c42\u7ed3\u679c=" + iStart);
        if (iStart != 0) {
            LogUtil.i(TAG, "\u4e0d\u652f\u6301HTTPDNS");
        }
        return iStart;
    }

    public int start(String str, String str2) {
        int iIntValue;
        LogUtil.stepLog("\u83b7\u53d6\u624b\u673a\u53f7\u7801");
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(str2)) {
            map.put("Host", str2);
        }
        if (TextUtils.isEmpty(str)) {
            iIntValue = 11;
        } else {
            try {
                iIntValue = ((Integer) NetUtil.doHttpReq(str, null, "GET", map, this.dealer)).intValue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.stepLog("\u83b7\u53d6\u624b\u673a\u53f7\u7801---\u7ed3\u679c=" + iIntValue);
        return iIntValue;
    }

    private JSONObject setTestData() throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (this.mSource == null) {
            try {
                jSONObject.put("enable", true);
                jSONObject.put("cycle", true);
                jSONObject.put("dest", "106.2.42.123:9995");
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put("guangdong", "aHR0cDovLzEyMC4xOTYuMTY2LjE1Ni9iZHByb3h5Lz9hcHBpZD1uZXRlYXNlCg==");
                JSONObject jSONObject3 = new JSONObject();
                jSONObject3.put("cmcc", jSONObject2);
                jSONObject.put("isp", jSONObject3);
                JSONObject jSONObject4 = new JSONObject();
                JSONArray jSONArray = new JSONArray();
                jSONArray.put(10);
                jSONArray.put(100);
                jSONObject4.put("nap_icmp", jSONArray);
                jSONObject4.put("rap_transfer", jSONArray);
                jSONObject4.put("rap_udp", jSONArray);
                jSONObject.put("threshold", jSONObject4);
            } catch (Exception unused) {
            }
        }
        return jSONObject;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x002b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.json.JSONObject getQosResult() throws org.json.JSONException {
        /*
            r4 = this;
            org.json.JSONObject r0 = new org.json.JSONObject
            r0.<init>()
            boolean r1 = r4.isISP()
            if (r1 == 0) goto L2b
            org.json.JSONObject r1 = r4.mResult
            java.lang.String r2 = "rap_qos_status"
            boolean r1 = r1.has(r2)
            if (r1 == 0) goto L2b
            org.json.JSONObject r1 = r4.mResult
            java.lang.String r1 = r1.optString(r2)
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto L2b
            int r1 = java.lang.Integer.parseInt(r1)
            r2 = -9
            if (r1 <= r2) goto L2b
            r1 = 1
            goto L2c
        L2b:
            r1 = 0
        L2c:
            org.json.JSONObject r2 = r4.mQosResult     // Catch: org.json.JSONException -> L3b
            java.lang.String r3 = "qos_effective"
            r2.put(r3, r1)     // Catch: org.json.JSONException -> L3b
            java.lang.String r1 = "qos"
            org.json.JSONObject r2 = r4.mQosResult     // Catch: org.json.JSONException -> L3b
            r0.put(r1, r2)     // Catch: org.json.JSONException -> L3b
            goto L4f
        L3b:
            r1 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            java.lang.String r3 = "QosCore [getResult] JSONException="
            r2.<init>(r3)
            r2.append(r1)
            java.lang.String r1 = r2.toString()
            java.lang.String r2 = "QosCore"
            com.netease.pharos.util.LogUtil.w(r2, r1)
        L4f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.qos.QosCore.getQosResult():org.json.JSONObject");
    }

    public boolean getQosEffective() {
        JSONObject jSONObject = this.mResult;
        if (jSONObject != null && jSONObject.has("qos_effective") && this.mResult.has("qos_effective")) {
            return this.mResult.optBoolean("qos_effective");
        }
        return false;
    }

    public String getDest() {
        return this.mDest;
    }

    public void clean() {
        this.mStauts = 0;
    }
}