package com.netease.pharos.qos;

import android.text.TextUtils;
import com.netease.pharos.Const;
import com.netease.pharos.PharosListener;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.config.CheckResult;
import com.netease.pharos.locationCheck.NetAreaInfo;
import com.netease.pharos.protocolCheck.ProtocolCheckListener;
import com.netease.pharos.protocolCheck.ProtocolCheckProxy;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class CheckHighSpeedListCore {
    private static final String TAG = "HighSpeedListCore";
    private JSONObject mData;
    private int mHighSpeedIpCount;
    private final ProtocolCheckListener mHighSpeedUDPListener;
    private final ArrayList<CheckResult> mHighSpeedUdpResult;
    private int mIndex;
    private String mIp;
    private String mPort;
    private JSONArray mPorts;

    static /* synthetic */ int access$108(CheckHighSpeedListCore checkHighSpeedListCore) {
        int i = checkHighSpeedListCore.mIndex;
        checkHighSpeedListCore.mIndex = i + 1;
        return i;
    }

    public CheckHighSpeedListCore(String str, JSONArray jSONArray) {
        this.mData = new JSONObject();
        this.mIp = null;
        this.mPort = null;
        this.mPorts = null;
        this.mHighSpeedIpCount = 0;
        this.mIndex = 0;
        this.mHighSpeedUdpResult = new ArrayList<>();
        this.mHighSpeedUDPListener = new ProtocolCheckListener() { // from class: com.netease.pharos.qos.CheckHighSpeedListCore.1
            AnonymousClass1() {
            }

            @Override // com.netease.pharos.protocolCheck.ProtocolCheckListener
            public void callBack(CheckResult checkResult) throws JSONException {
                LogUtil.i(CheckHighSpeedListCore.TAG, "CheckHighSpeedList \u52a0\u901f\u5217\u8868UDP \u56de\u8c03\u7ed3\u679c=" + checkResult.toString());
                if (checkResult.getLoss() < 1.0d && checkResult.getLoss() >= 0.0d && checkResult.getAvgTime() < 800 && checkResult.getAvgTime() > 0) {
                    CheckHighSpeedListCore.this.mHighSpeedUdpResult.add(checkResult);
                }
                CheckHighSpeedListCore.access$108(CheckHighSpeedListCore.this);
                LogUtil.i(CheckHighSpeedListCore.TAG, "CheckHighSpeedList UDP mHighSpeedIpCount=" + CheckHighSpeedListCore.this.mHighSpeedIpCount + ", mIndex=" + CheckHighSpeedListCore.this.mIndex);
                if (CheckHighSpeedListCore.this.mHighSpeedIpCount == CheckHighSpeedListCore.this.mIndex) {
                    int iSort = CheckHighSpeedListCore.this.sort();
                    CheckHighSpeedResult.getInstance().setHighSpeedUdpResult(CheckHighSpeedListCore.this.mHighSpeedUdpResult);
                    JSONObject result = CheckHighSpeedResult.getInstance().getResult(iSort);
                    LogUtil.i(CheckHighSpeedListCore.TAG, "\u67e5\u8be2\u9ad8\u901f\u5217\u8868 \u6700\u7ec8\u7ed3\u679c=" + result.toString());
                    PharosListener pharosListener = PharosProxy.getInstance().getPharosListener();
                    if (pharosListener != null) {
                        if (result != null) {
                            pharosListener.onResult(result);
                            pharosListener.onPharosServer(result);
                        } else {
                            LogUtil.i(CheckHighSpeedListCore.TAG, "qosResult is null");
                        }
                    }
                }
            }
        };
        this.mIp = str;
        this.mPorts = jSONArray;
        CheckHighSpeedResult.getInstance().init(this.mIp, this.mPorts);
    }

    public CheckHighSpeedListCore(String str, String str2) {
        this.mData = new JSONObject();
        this.mIp = null;
        this.mPort = null;
        this.mPorts = null;
        this.mHighSpeedIpCount = 0;
        this.mIndex = 0;
        this.mHighSpeedUdpResult = new ArrayList<>();
        this.mHighSpeedUDPListener = new ProtocolCheckListener() { // from class: com.netease.pharos.qos.CheckHighSpeedListCore.1
            AnonymousClass1() {
            }

            @Override // com.netease.pharos.protocolCheck.ProtocolCheckListener
            public void callBack(CheckResult checkResult) throws JSONException {
                LogUtil.i(CheckHighSpeedListCore.TAG, "CheckHighSpeedList \u52a0\u901f\u5217\u8868UDP \u56de\u8c03\u7ed3\u679c=" + checkResult.toString());
                if (checkResult.getLoss() < 1.0d && checkResult.getLoss() >= 0.0d && checkResult.getAvgTime() < 800 && checkResult.getAvgTime() > 0) {
                    CheckHighSpeedListCore.this.mHighSpeedUdpResult.add(checkResult);
                }
                CheckHighSpeedListCore.access$108(CheckHighSpeedListCore.this);
                LogUtil.i(CheckHighSpeedListCore.TAG, "CheckHighSpeedList UDP mHighSpeedIpCount=" + CheckHighSpeedListCore.this.mHighSpeedIpCount + ", mIndex=" + CheckHighSpeedListCore.this.mIndex);
                if (CheckHighSpeedListCore.this.mHighSpeedIpCount == CheckHighSpeedListCore.this.mIndex) {
                    int iSort = CheckHighSpeedListCore.this.sort();
                    CheckHighSpeedResult.getInstance().setHighSpeedUdpResult(CheckHighSpeedListCore.this.mHighSpeedUdpResult);
                    JSONObject result = CheckHighSpeedResult.getInstance().getResult(iSort);
                    LogUtil.i(CheckHighSpeedListCore.TAG, "\u67e5\u8be2\u9ad8\u901f\u5217\u8868 \u6700\u7ec8\u7ed3\u679c=" + result.toString());
                    PharosListener pharosListener = PharosProxy.getInstance().getPharosListener();
                    if (pharosListener != null) {
                        if (result != null) {
                            pharosListener.onResult(result);
                            pharosListener.onPharosServer(result);
                        } else {
                            LogUtil.i(CheckHighSpeedListCore.TAG, "qosResult is null");
                        }
                    }
                }
            }
        };
        this.mIp = str;
        this.mPort = str2;
        CheckHighSpeedResult.getInstance().init(this.mIp, this.mPort);
    }

    public void setData(JSONObject jSONObject) {
        this.mData = jSONObject;
    }

    /* renamed from: com.netease.pharos.qos.CheckHighSpeedListCore$1 */
    class AnonymousClass1 implements ProtocolCheckListener {
        AnonymousClass1() {
        }

        @Override // com.netease.pharos.protocolCheck.ProtocolCheckListener
        public void callBack(CheckResult checkResult) throws JSONException {
            LogUtil.i(CheckHighSpeedListCore.TAG, "CheckHighSpeedList \u52a0\u901f\u5217\u8868UDP \u56de\u8c03\u7ed3\u679c=" + checkResult.toString());
            if (checkResult.getLoss() < 1.0d && checkResult.getLoss() >= 0.0d && checkResult.getAvgTime() < 800 && checkResult.getAvgTime() > 0) {
                CheckHighSpeedListCore.this.mHighSpeedUdpResult.add(checkResult);
            }
            CheckHighSpeedListCore.access$108(CheckHighSpeedListCore.this);
            LogUtil.i(CheckHighSpeedListCore.TAG, "CheckHighSpeedList UDP mHighSpeedIpCount=" + CheckHighSpeedListCore.this.mHighSpeedIpCount + ", mIndex=" + CheckHighSpeedListCore.this.mIndex);
            if (CheckHighSpeedListCore.this.mHighSpeedIpCount == CheckHighSpeedListCore.this.mIndex) {
                int iSort = CheckHighSpeedListCore.this.sort();
                CheckHighSpeedResult.getInstance().setHighSpeedUdpResult(CheckHighSpeedListCore.this.mHighSpeedUdpResult);
                JSONObject result = CheckHighSpeedResult.getInstance().getResult(iSort);
                LogUtil.i(CheckHighSpeedListCore.TAG, "\u67e5\u8be2\u9ad8\u901f\u5217\u8868 \u6700\u7ec8\u7ed3\u679c=" + result.toString());
                PharosListener pharosListener = PharosProxy.getInstance().getPharosListener();
                if (pharosListener != null) {
                    if (result != null) {
                        pharosListener.onResult(result);
                        pharosListener.onPharosServer(result);
                    } else {
                        LogUtil.i(CheckHighSpeedListCore.TAG, "qosResult is null");
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:134:0x0136  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int start() throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 352
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.qos.CheckHighSpeedListCore.start():int");
    }

    private boolean startHighSpeedProbe(ProtocolCheckProxy protocolCheckProxy, boolean z, JSONObject jSONObject, String str) throws JSONException {
        JSONArray jSONArrayOptJSONArray;
        boolean z2;
        if (TextUtils.isEmpty(str) || !jSONObject.has(str) || (jSONArrayOptJSONArray = jSONObject.optJSONArray(str)) == null) {
            return z;
        }
        int i = 0;
        boolean z3 = z;
        int i2 = 0;
        while (i2 < jSONArrayOptJSONArray.length()) {
            try {
                JSONArray jSONArray = jSONArrayOptJSONArray.getJSONArray(i2);
                LogUtil.i(TAG, "CheckHighSpeedList [start] info=" + jSONArrayOptJSONArray + ", " + i2 + "=" + jSONArray.toString());
                String strOptString = jSONArray.optString(i);
                z2 = true;
                int iString2Int = Util.string2Int(jSONArray.optString(1));
                LogUtil.i(TAG, "CheckHighSpeedList [start] mPort=" + str + ", port=" + iString2Int);
                if (!TextUtils.isEmpty(strOptString) && -1 != iString2Int) {
                    try {
                        this.mHighSpeedIpCount++;
                        LogUtil.i(TAG, "CheckHighSpeedList [start]  \u662f\u5426\u8981\u8fdb\u884cudp\u63a2\u6d4b=" + PharosProxy.getInstance().ismHarborudp());
                        LogUtil.i(TAG, "CheckHighSpeedList [start]  \u63d0\u4ea4udp\u63a2\u6d4b \u53c2\u6570 ip=" + strOptString + ", port=9999, \u6e38\u620f\u670d\u52a1\u5668port=" + str + ", port=" + iString2Int);
                        if (!PharosProxy.getInstance().ismHarborudp().isEmpty() && PharosProxy.getInstance().ismHarborudp().equals("true")) {
                            LogUtil.i(TAG, "CheckHighSpeedList [start]  \u8fdb\u884cudp\u63a2\u6d4b ");
                            protocolCheckProxy.addProtocolCheckCore(2, strOptString, Const.UDP_PORT, NetAreaInfo.getInstances().getUdpCount(), 800, NetAreaInfo.getInstances().getPackageNum() * 32, null, this.mHighSpeedUDPListener, 0, null, null, str + "," + iString2Int);
                        } else {
                            LogUtil.i(TAG, "CheckHighSpeedList [start]  \u4e0d\u9700\u8981\u8fdb\u884cudp\u63a2\u6d4b ");
                            CheckResult checkResult = new CheckResult();
                            checkResult.setIp(strOptString);
                            checkResult.setmPort(Const.UDP_PORT);
                            checkResult.setmExtra(str + "," + iString2Int);
                            this.mHighSpeedUdpResult.add(checkResult);
                            LogUtil.i(TAG, "CheckHighSpeedList [start]  \u4e0d\u9700\u8981\u8fdb\u884cudp\u63a2\u6d4b mHighSpeedUdpResult=" + this.mHighSpeedUdpResult.toString());
                        }
                        z3 = true;
                    } catch (Exception e) {
                        e = e;
                        LogUtil.i(TAG, "CheckHighSpeedList [start] Exception=" + e);
                        e.printStackTrace();
                        return z2;
                    }
                }
                i2++;
                i = 0;
            } catch (Exception e2) {
                e = e2;
                z2 = z3;
            }
        }
        return z3;
    }

    private void reset() {
        this.mHighSpeedIpCount = 0;
        this.mIndex = 0;
        CheckHighSpeedResult.getInstance().cleanData();
    }

    public int sort() {
        ArrayList<CheckResult> arrayList = this.mHighSpeedUdpResult;
        if (arrayList == null || arrayList.size() <= 0) {
            LogUtil.i(TAG, "CheckHighSpeedList [chooseBest] \u53c2\u6570\u9519\u8bef1");
            return 14;
        }
        try {
            Collections.sort(this.mHighSpeedUdpResult, new Comparator<CheckResult>() { // from class: com.netease.pharos.qos.CheckHighSpeedListCore.2
                AnonymousClass2() {
                }

                @Override // java.util.Comparator
                public int compare(CheckResult checkResult, CheckResult checkResult2) {
                    if (checkResult.getLoss() > checkResult2.getLoss()) {
                        return 1;
                    }
                    return checkResult.getLoss() < checkResult2.getLoss() ? -1 : 0;
                }
            });
            Collections.sort(this.mHighSpeedUdpResult, new Comparator<CheckResult>() { // from class: com.netease.pharos.qos.CheckHighSpeedListCore.3
                AnonymousClass3() {
                }

                @Override // java.util.Comparator
                public int compare(CheckResult checkResult, CheckResult checkResult2) {
                    if (checkResult.getAvgTime() > checkResult2.getAvgTime()) {
                        return 1;
                    }
                    return checkResult.getAvgTime() < checkResult2.getAvgTime() ? -1 : 0;
                }
            });
            return 0;
        } catch (Exception e) {
            LogUtil.w(TAG, "CheckHighSpeedList [chooseBest] Exception=" + e);
            return 11;
        }
    }

    /* renamed from: com.netease.pharos.qos.CheckHighSpeedListCore$2 */
    class AnonymousClass2 implements Comparator<CheckResult> {
        AnonymousClass2() {
        }

        @Override // java.util.Comparator
        public int compare(CheckResult checkResult, CheckResult checkResult2) {
            if (checkResult.getLoss() > checkResult2.getLoss()) {
                return 1;
            }
            return checkResult.getLoss() < checkResult2.getLoss() ? -1 : 0;
        }
    }

    /* renamed from: com.netease.pharos.qos.CheckHighSpeedListCore$3 */
    class AnonymousClass3 implements Comparator<CheckResult> {
        AnonymousClass3() {
        }

        @Override // java.util.Comparator
        public int compare(CheckResult checkResult, CheckResult checkResult2) {
            if (checkResult.getAvgTime() > checkResult2.getAvgTime()) {
                return 1;
            }
            return checkResult.getAvgTime() < checkResult2.getAvgTime() ? -1 : 0;
        }
    }
}