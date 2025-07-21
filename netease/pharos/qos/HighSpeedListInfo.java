package com.netease.pharos.qos;

import android.text.TextUtils;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class HighSpeedListInfo {
    private static final String TAG = "HighSpeedInfo";
    private static HighSpeedListInfo sHighSpeedListInfo;
    private String currentIp;
    private ConcurrentHashMap<String, CopyOnWriteArrayList<String>> mOriInfo = new ConcurrentHashMap<>();
    private byte[] resultData;

    public int start() {
        return 11;
    }

    private HighSpeedListInfo() {
    }

    public static HighSpeedListInfo getInstance() {
        if (sHighSpeedListInfo == null) {
            synchronized (HighSpeedListInfo.class) {
                if (sHighSpeedListInfo == null) {
                    sHighSpeedListInfo = new HighSpeedListInfo();
                }
            }
        }
        return sHighSpeedListInfo;
    }

    public synchronized void add(String str) {
        if (this.mOriInfo != null && str != null) {
            String[] strArrSplit = str.split(" ");
            if (strArrSplit.length > 1) {
                String[] strArrSplit2 = strArrSplit[0].split(":");
                if (strArrSplit2.length < 2) {
                    return;
                }
                String str2 = strArrSplit2[0];
                CopyOnWriteArrayList<String> copyOnWriteArrayList = this.mOriInfo.get(str2);
                if (copyOnWriteArrayList == null) {
                    copyOnWriteArrayList = new CopyOnWriteArrayList<>();
                }
                copyOnWriteArrayList.add(str);
                this.mOriInfo.put(str2, copyOnWriteArrayList);
            }
        }
    }

    public void addList(List<String> list) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            add(it.next());
        }
        LogUtil.i(TAG, "HighSpeedListInfo [addList] cost=" + (System.currentTimeMillis() - jCurrentTimeMillis));
    }

    public synchronized void clean() {
        LogUtil.i(TAG, "HighSpeedListInfo [clean]");
        this.mOriInfo = new ConcurrentHashMap<>();
        this.currentIp = null;
        this.resultData = null;
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x012b  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x01d8  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x01d3 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x01e0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public synchronized org.json.JSONObject parse(java.lang.String r27) {
        /*
            Method dump skipped, instructions count: 740
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.qos.HighSpeedListInfo.parse(java.lang.String):org.json.JSONObject");
    }

    public JSONObject genResult(JSONObject jSONObject, String str, String str2, String str3, int i, String str4, String str5, String str6, String str7) throws JSONException, NumberFormatException {
        int i2;
        int i3;
        String str8 = str2;
        String str9 = str6;
        LogUtil.i(TAG, "HighSpeedListInfo [parse1] \u6e38\u620f\u670d\u52a1\u5668ip=" + str + ", \u6e38\u620f\u670d\u52a1\u5668\u7aef\u53e3=" + str8 + ", \u6e38\u620f\u670d\u52a1\u5668\u6b65\u957f=" + str3 + ", \u6b65\u6570=" + i + ", \u8fb9\u7f18\u8282\u70b9\u670d\u52a1\u5668\u7aef\u53e3=" + str4 + ", \u8fb9\u7f18\u8282\u70b9\u670d\u52a1\u5668\u6b65\u957f=" + str5 + ", \u8fb9\u7f18\u8282\u70b9\u670d\u52a1\u5668Ip=" + str9 + ", \u6240\u5c5e\u7fa4\u7ec4 =" + str7);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || -1 == i || TextUtils.isEmpty(str4) || TextUtils.isEmpty(str5) || TextUtils.isEmpty(str6) || TextUtils.isEmpty(str7)) {
            LogUtil.i(TAG, "HighSpeedListInfo [parse1] \u53c2\u6570\u9519\u8bef1");
            return jSONObject;
        }
        JSONObject jSONObject2 = jSONObject == null ? new JSONObject() : jSONObject;
        JSONObject jSONObject3 = new JSONObject();
        JSONObject jSONObject4 = new JSONObject();
        try {
            i2 = Integer.parseInt(str5);
        } catch (Exception unused) {
            i2 = -10000;
        }
        if (-10000 == i2) {
            LogUtil.i(TAG, "HighSpeedListInfo [parse1] \u53c2\u6570\u9519\u8bef2");
            return jSONObject2;
        }
        try {
            i3 = Integer.parseInt(str3);
        } catch (Exception unused2) {
            i3 = -10000;
        }
        if (-10000 == i3) {
            LogUtil.i(TAG, "HighSpeedListInfo [parse1] \u53c2\u6570\u9519\u8bef3");
            return jSONObject2;
        }
        LogUtil.i(TAG, "HighSpeedListInfo [parse1] infos=" + jSONObject2 + ", ori_ip=" + str);
        int i4 = 0;
        if (!TextUtils.isEmpty(str) && jSONObject2.has(str)) {
            try {
                jSONObject4 = jSONObject2.getJSONObject(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (jSONObject4 != null) {
                if (!TextUtils.isEmpty(str7) && jSONObject4.has(str7)) {
                    try {
                        jSONObject3 = jSONObject4.getJSONObject(str7);
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                    if (jSONObject3 != null) {
                        LogUtil.i(TAG, "HighSpeedListInfo [parse1] secondData=" + jSONObject3 + ", edge_port=" + str4 + ", edge_ip=" + str9 + ", ori_port=" + str8);
                        while (i4 < i) {
                            JSONArray jSONArray = new JSONArray();
                            JSONArray jSONArray2 = new JSONArray();
                            if (!TextUtils.isEmpty(str8) && jSONObject3.has(str8)) {
                                try {
                                    jSONArray = jSONObject3.getJSONArray(str8);
                                } catch (JSONException e3) {
                                    e3.printStackTrace();
                                }
                            }
                            try {
                                int i5 = Integer.parseInt(str4) + (i4 * i2);
                                jSONArray2.put(str9);
                                jSONArray2.put(i5 + "");
                                if (!jSONArray.toString().contains(jSONArray2.toString())) {
                                    jSONArray.put(jSONArray2);
                                }
                                int i6 = Integer.parseInt(str8);
                                jSONObject3.put(i6 + "", jSONArray);
                                str8 = (i6 + i3) + "";
                            } catch (Exception e4) {
                                e4.printStackTrace();
                                LogUtil.i(TAG, "HighSpeedListInfo [parse1] Exception=" + e4);
                            }
                            i4++;
                            str9 = str6;
                        }
                        if (jSONObject3 != null) {
                            try {
                                jSONObject4.put(str7, jSONObject3);
                            } catch (JSONException unused3) {
                            }
                        }
                        if (jSONObject4 != null) {
                            try {
                                jSONObject2.put(str, jSONObject4);
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                    }
                } else {
                    while (i4 < i) {
                        JSONArray jSONArray3 = new JSONArray();
                        try {
                            JSONArray jSONArray4 = new JSONArray();
                            int i7 = Integer.parseInt(str4) + (i4 * i2);
                            try {
                                jSONArray4.put(str6);
                                jSONArray4.put(i7 + "");
                                jSONArray3.put(jSONArray4);
                                jSONObject3.put((Integer.parseInt(str2) + (i4 * i3)) + "", jSONArray3);
                            } catch (Exception e6) {
                                e = e6;
                                LogUtil.w(TAG, "HighSpeedListInfo [parse1] Exception1 =" + e);
                                i4++;
                            }
                        } catch (Exception e7) {
                            e = e7;
                        }
                        i4++;
                    }
                    try {
                        jSONObject4.put(str7, jSONObject3);
                    } catch (Exception e8) {
                        LogUtil.w(TAG, "HighSpeedListInfo [parse1] Exception2 =" + e8);
                    }
                    if (jSONObject4 != null) {
                        try {
                            jSONObject2.put(str, jSONObject4);
                        } catch (Exception e9) {
                            LogUtil.w(TAG, "HighSpeedListInfo [parse1] Exception3 =" + e9);
                        }
                    }
                }
            }
        } else {
            LogUtil.i(TAG, "HighSpeedListInfo [parse1] \u4e0d\u5305\u542b\u8be5\u6e38\u620f\u670d\u52a1\u5668ip\u4fe1\u606f ");
            while (i4 < i) {
                JSONArray jSONArray5 = new JSONArray();
                try {
                    int i8 = Integer.parseInt(str4) + (i4 * i2);
                    JSONArray jSONArray6 = new JSONArray();
                    jSONArray6.put(str9);
                    jSONArray6.put(i8 + "");
                    jSONArray5.put(jSONArray6);
                    jSONObject3.put((Integer.parseInt(str2) + (i4 * i3)) + "", jSONArray5);
                } catch (Exception e10) {
                    LogUtil.w(TAG, "HighSpeedListInfo [parse1] Exception4 =" + e10);
                }
                i4++;
            }
            try {
                jSONObject4.put(str7, jSONObject3);
            } catch (Exception e11) {
                LogUtil.w(TAG, "HighSpeedListInfo [parse1] Exception5 =" + e11);
            }
            try {
                jSONObject2.put(str, jSONObject4);
            } catch (Exception e12) {
                LogUtil.w(TAG, "HighSpeedListInfo [parse1] Exception6 =" + e12);
            }
        }
        return jSONObject2;
    }

    private JSONObject create(String str, String str2, String str3, String str4, String str5, int i) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        int iString2Int = Util.string2Int(str4);
        int iString2Int2 = Util.string2Int(str2);
        int iString2Int3 = Util.string2Int(str);
        int iString2Int4 = Util.string2Int(str5);
        LogUtil.i(TAG, "HighSpeedListInfo [create] param error lightenIp=" + str3 + ", sourcePortStepLength=" + str2 + ", tLightenStepLength=" + iString2Int4 + ", step=" + i + ", tLightenPort=" + iString2Int + ", tSourcePort=" + iString2Int3);
        if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4) && i > 0 && -1 != iString2Int && -1 != iString2Int3) {
            for (int i2 = 0; i2 < i; i2++) {
                JSONArray jSONArray = new JSONArray();
                int i3 = (i2 * iString2Int2) + iString2Int3;
                jSONArray.put(str3);
                jSONArray.put((i2 * iString2Int4) + iString2Int);
                try {
                    jSONObject.put(i3 + "", jSONArray);
                } catch (JSONException e) {
                    LogUtil.e(TAG, "HighSpeedListInfo [create] Exception2=" + e);
                }
            }
        }
        return jSONObject;
    }
}