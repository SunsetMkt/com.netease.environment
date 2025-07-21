package com.netease.cloud.nos.android.monitor;

import android.content.Context;
import com.facebook.common.callercontext.ContextChain;
import com.netease.cloud.nos.android.core.WanAccelerator;
import com.netease.cloud.nos.android.utils.LogUtil;
import com.netease.cloud.nos.android.utils.Util;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.zip.GZIPOutputStream;
import org.jose4j.jwk.OctetSequenceJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class Monitor {
    private static final int maxListNum = 500;
    private static final String LOGTAG = LogUtil.makeLogTag(Monitor.class);
    private static List<StatisticItem> LIST = null;
    private static boolean prompt = false;

    public static void add(Context context, StatisticItem statisticItem) {
        if (!WanAccelerator.getConf().isMonitorThreadEnabled()) {
            MonitorManager.sendStatItem(context, statisticItem);
            return;
        }
        String str = LOGTAG;
        LogUtil.d(str, "monitor add item for thread");
        if (set(statisticItem)) {
            LogUtil.d(str, "send monitor data immediately");
            new Timer().schedule(new MonitorTask(context), 0L);
        }
    }

    public static synchronized void clean() {
        List<StatisticItem> list = LIST;
        if (list != null) {
            list.clear();
        }
    }

    public static synchronized List<StatisticItem> get() {
        List<StatisticItem> list;
        list = LIST;
        LIST = null;
        prompt = false;
        return list;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v15 */
    /* JADX WARN: Type inference failed for: r1v16 */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v19 */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.util.zip.GZIPOutputStream] */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r1v5, types: [java.util.zip.GZIPOutputStream] */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.util.zip.GZIPOutputStream] */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /* JADX WARN: Type inference failed for: r1v9 */
    public static ByteArrayOutputStream getPostData(List<StatisticItem> list) throws Throwable {
        ?? r1 = 0;
        r1 = 0;
        r1 = 0;
        if (list == null || list.size() == 0) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            try {
                try {
                    GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                    try {
                        JSONArray jSONArray = new JSONArray();
                        Iterator<StatisticItem> it = list.iterator();
                        while (it.hasNext()) {
                            jSONArray.put(toJSON(it.next()));
                        }
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("items", jSONArray);
                        LogUtil.e(LOGTAG, "monitor result: " + jSONObject.toString());
                        String str = "UTF-8";
                        gZIPOutputStream.write(jSONObject.toString().getBytes("UTF-8"));
                        gZIPOutputStream.flush();
                        gZIPOutputStream.finish();
                        gZIPOutputStream.close();
                        r1 = str;
                    } catch (IOException e) {
                        e = e;
                        r1 = gZIPOutputStream;
                        LogUtil.e(LOGTAG, "get post data io exception", e);
                        if (r1 != 0) {
                            r1.close();
                            r1 = r1;
                        }
                        return byteArrayOutputStream;
                    } catch (JSONException e2) {
                        e = e2;
                        r1 = gZIPOutputStream;
                        LogUtil.e(LOGTAG, "get post data json exception", e);
                        if (r1 != 0) {
                            r1.close();
                            r1 = r1;
                        }
                        return byteArrayOutputStream;
                    } catch (Throwable th) {
                        th = th;
                        r1 = gZIPOutputStream;
                        if (r1 != 0) {
                            try {
                                r1.close();
                            } catch (IOException e3) {
                                LogUtil.e(LOGTAG, "gos close exception", e3);
                            }
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    e = e4;
                } catch (JSONException e5) {
                    e = e5;
                }
            } catch (IOException e6) {
                r1 = LOGTAG;
                LogUtil.e(r1, "gos close exception", e6);
            }
            return byteArrayOutputStream;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static synchronized boolean set(StatisticItem statisticItem) {
        if (LIST == null) {
            LIST = new ArrayList();
        }
        LIST.add(statisticItem);
        if (LIST.size() < 500 || prompt) {
            return false;
        }
        LogUtil.d(LOGTAG, "monitor item num " + LIST.size() + " >= 500");
        prompt = true;
        return true;
    }

    private static JSONObject toJSON(StatisticItem statisticItem) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("a", statisticItem.getPlatform());
            if (statisticItem.getClientIP() != null && !statisticItem.getClientIP().equals("")) {
                jSONObject.put("b", Util.ipToLong(statisticItem.getClientIP()));
            }
            jSONObject.put("c", statisticItem.getSdkVersion());
            if (statisticItem.getLbsIP() != null && !statisticItem.getLbsIP().equals("")) {
                jSONObject.put("d", Util.ipToLong(Util.getIPString(statisticItem.getLbsIP())));
            }
            jSONObject.put(RsaJsonWebKey.EXPONENT_MEMBER_NAME, Util.ipToLong(Util.getIPString(statisticItem.getUploaderIP())));
            jSONObject.put("f", statisticItem.getFileSize());
            jSONObject.put("g", statisticItem.getNetEnv());
            if (statisticItem.getLbsUseTime() != 0) {
                jSONObject.put("h", statisticItem.getLbsUseTime());
            }
            jSONObject.put(ContextChain.TAG_INFRA, statisticItem.getUploaderUseTime());
            if (statisticItem.getLbsSucc() != 0) {
                jSONObject.put("j", statisticItem.getLbsSucc());
            }
            if (statisticItem.getUploaderSucc() != 0) {
                jSONObject.put(OctetSequenceJsonWebKey.KEY_VALUE_MEMBER_NAME, statisticItem.getUploaderSucc());
            }
            if (statisticItem.getLbsHttpCode() != 200) {
                jSONObject.put(com.xiaomi.onetrack.b.e.f2682a, statisticItem.getLbsHttpCode());
            }
            if (statisticItem.getUploaderHttpCode() != 200) {
                jSONObject.put("m", statisticItem.getUploaderHttpCode());
            }
            if (statisticItem.getUploadRetryCount() != 0) {
                jSONObject.put(RsaJsonWebKey.MODULUS_MEMBER_NAME, statisticItem.getUploadRetryCount());
            }
            if (statisticItem.getChunkRetryCount() != 0) {
                jSONObject.put("o", statisticItem.getChunkRetryCount());
            }
            if (statisticItem.getQueryRetryCount() != 0) {
                jSONObject.put("p", statisticItem.getQueryRetryCount());
            }
            if (statisticItem.getBucketName() != null && !statisticItem.getBucketName().equals("")) {
                jSONObject.put(RsaJsonWebKey.SECOND_PRIME_FACTOR_MEMBER_NAME, statisticItem.getBucketName());
            }
            if (statisticItem.getUploadType() != 1000) {
                jSONObject.put(RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME, statisticItem.getUploadType());
            }
        } catch (JSONException e) {
            LogUtil.e(LOGTAG, "parse statistic item json exception", e);
        }
        return jSONObject;
    }
}