package com.netease.pharos.netlag;

import com.netease.pharos.netlag.NetworkCheckConfig;
import com.netease.pharos.util.LogUtil;
import com.xiaomi.gamecenter.sdk.robust.Constants;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import com.xiaomi.onetrack.OneTrack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class UDPCostComputer {

    public interface CallBack {
        void onFinish(String str, JSONObject jSONObject);
    }

    private static double std(long[] jArr) {
        int length = jArr.length;
        double d = 0.0d;
        double d2 = 0.0d;
        for (long j : jArr) {
            d2 += j;
        }
        double d3 = length;
        double d4 = d2 / d3;
        for (long j2 : jArr) {
            d += (j2 - d4) * (j2 - d4);
        }
        return Math.sqrt(d / d3);
    }

    public JSONObject compute(ArrayList<NetworkCheckConfig.IpInfo> arrayList, HashMap<String, ArrayList<Long>> map, int i) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        Iterator<NetworkCheckConfig.IpInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            NetworkCheckConfig.IpInfo next = it.next();
            ArrayList<Long> arrayList2 = map.get(next.ip);
            int iMax = Math.max(0, (i * 2) - arrayList2.size());
            int i2 = i - iMax;
            long[] jArr = new long[i];
            for (int i3 = 0; i3 < i2; i3++) {
                jArr[i3] = arrayList2.get(i + i3).longValue() - arrayList2.get(i3).longValue();
            }
            while (i2 < i) {
                jArr[i2] = -1;
                i2++;
            }
            try {
                JSONObject result = getResult(i, jArr, iMax);
                LogUtil.i("NetLag ", "key:" + next.name + Constants.C + next.ip + "],result:" + result.toString());
                jSONObject.put(next.name, result);
            } catch (Exception unused) {
            }
        }
        return jSONObject;
    }

    private JSONObject getResult(int i, long[] jArr, int i2) throws JSONException {
        long j;
        double dStd;
        long j2;
        long j3;
        long j4;
        try {
            JSONObject jSONObject = new JSONObject();
            JSONObject jSONObject2 = new JSONObject();
            JSONObject jSONObject3 = new JSONObject();
            int i3 = i2;
            long[] jArrFilterArrays = filterArrays(jArr, i3);
            if (jArrFilterArrays == null || jArrFilterArrays.length <= 0) {
                j = 1000;
                dStd = 0.0d;
                i3 = i;
                j2 = 1000;
                j3 = 1000;
                j4 = 1000;
            } else {
                Arrays.sort(jArrFilterArrays);
                dStd = std(jArrFilterArrays);
                j = jArrFilterArrays[jArrFilterArrays.length - 1];
                j4 = jArrFilterArrays[0];
                long j5 = jArrFilterArrays[jArrFilterArrays.length / 2];
                int length = jArrFilterArrays.length;
                long j6 = 0;
                int i4 = 0;
                int i5 = 0;
                while (i4 < length) {
                    long j7 = jArrFilterArrays[i4];
                    long[] jArr2 = jArrFilterArrays;
                    if (Math.abs(j7 - j5) < dStd) {
                        j6 += j7;
                        i5++;
                    }
                    i4++;
                    jArrFilterArrays = jArr2;
                }
                j2 = j5;
                j3 = j6 / (i5 == 0 ? 1 : i5);
            }
            jSONObject.put("rtt", jSONObject2);
            jSONObject2.put("max", j);
            jSONObject2.put("min", j4);
            jSONObject2.put(OneTrackParams.XMSdkParams.MID, j2);
            jSONObject2.put("std", dStd);
            jSONObject2.put("total", j3);
            jSONObject3.put("send", i);
            jSONObject3.put("recv", i - i3);
            jSONObject3.put("loss", i3);
            jSONObject.put(OneTrack.Param.PKG, jSONObject3);
            return jSONObject;
        } catch (Exception unused) {
            return null;
        }
    }

    private long[] filterArrays(long[] jArr, int i) {
        if (i == 0 || i == jArr.length) {
            return jArr;
        }
        int length = jArr.length - i;
        long[] jArr2 = new long[length];
        int i2 = 0;
        for (long j : jArr) {
            if (j != -1) {
                if (i2 >= length) {
                    break;
                }
                jArr2[i2] = j;
                i2++;
            }
        }
        return jArr2;
    }
}