package com.netease.download.config;

import com.netease.download.dns.DnsCore;
import com.netease.download.dns.DnsParams;
import com.netease.download.util.LogUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ConfigParams {
    private static final String TAG = "ConfigParams";
    public int mRemoveSlowCDNTopSpeed;
    public int mTotalWeight;
    public int[] mWeights;
    public Map<String, CdnUnit> mCdnMap = new HashMap();
    public boolean mRemovable = true;
    public int mRemoveSlowCDNPercent = 50;
    public int mRemoveSlowCDNSpeed = 250;
    public int mRemoveSlowCDNTime = 10;
    public int mSplitThreshold = 2048;
    public boolean mReport = true;
    public String mReportUrl = null;
    public String[] mReportIpArray = null;
    public boolean mIpDnsPicker = true;
    public String mPickerUrl = null;
    public String[] mLvsipArray = null;

    public ConfigParams() {
    }

    /* JADX WARN: Removed duplicated region for block: B:392:0x0381  */
    /* JADX WARN: Removed duplicated region for block: B:415:0x008d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean parse(java.lang.String r25, boolean r26) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 921
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.config.ConfigParams.parse(java.lang.String, boolean):boolean");
    }

    public ConfigParams(String str, boolean z) {
    }

    public String[] getLvsipArray() {
        return this.mLvsipArray;
    }

    public Map<String, CdnUnit> getmCdnMap() {
        return this.mCdnMap;
    }

    public String[] getReportIpArray() {
        return this.mReportIpArray;
    }

    public int getSplitThreshold() {
        return this.mSplitThreshold;
    }

    public String getReportUrl() {
        return this.mReportUrl;
    }

    public boolean isReport() {
        return this.mReport;
    }

    public boolean getIpDnsPicker() {
        return this.mIpDnsPicker;
    }

    public String getPickerURL() {
        return this.mPickerUrl;
    }

    public int getTotalWeight() {
        return this.mTotalWeight;
    }

    public int[] getWeights() {
        return this.mWeights;
    }

    public String toString() {
        return "ConfigParams{weights=" + Arrays.toString(this.mWeights) + ", removable=" + this.mRemovable + ", removeSlowCDNTopSpeed=" + this.mRemoveSlowCDNTopSpeed + ", removeSlowCDNPercent=" + this.mRemoveSlowCDNPercent + ", removeSlowCDNSpeed=" + this.mRemoveSlowCDNSpeed + ", removeSlowCDNTime=" + this.mRemoveSlowCDNTime + ", splitThreshold=" + this.mSplitThreshold + ", report=" + this.mReport + ", reportUrl='" + this.mReportUrl + "', reportIpArray='" + Arrays.toString(this.mReportIpArray) + "', ipDnsPicker=" + this.mIpDnsPicker + ", pickerURL='" + this.mPickerUrl + "', lvsipArray=" + Arrays.toString(this.mLvsipArray) + '}';
    }

    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public static JSONObject getDefaultConfing() throws JSONException {
        String str;
        String str2;
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        JSONObject jSONObject3 = new JSONObject();
        JSONArray jSONArray = new JSONArray();
        jSONArray.put("https://<$gameid>.gph.netease.com<100>");
        JSONArray jSONArray2 = new JSONArray();
        String str3 = "PickerURL";
        jSONArray2.put("https://<$gameid>.gdl.netease.com<100>");
        JSONArray jSONArray3 = new JSONArray();
        jSONArray3.put("42.186.82.32");
        jSONArray3.put("103.72.17.10");
        jSONArray3.put("103.72.16.24");
        try {
            jSONObject2.put("GPHList", jSONArray);
            jSONObject2.put("GDLList", jSONArray2);
            jSONObject2.put("SplitThreshold", 2048);
            jSONObject2.put("Removable", true);
            jSONObject2.put("RemoveSlowCDNPercent", "50");
            jSONObject2.put("RemoveSlowCDNSpeed", "250");
            jSONObject2.put("RemoveSlowCDNTime", "10");
            jSONObject2.put("Report", true);
            jSONObject2.put("ReportUrl", "https://sigma-orbit-impression.proxima.nie.netease.com/sdk");
            str2 = "IPDNSPicker";
            try {
                jSONObject2.put(str2, false);
                try {
                    jSONObject2.put(str3, "https://nstool.netease.com/internalquery/");
                    str3 = str3;
                    str = "ListLVSIP";
                } catch (JSONException e) {
                    e = e;
                    str3 = str3;
                    str = "ListLVSIP";
                    e.printStackTrace();
                    JSONArray jSONArray4 = new JSONArray();
                    jSONArray4.put("https://<$gameid>.gph.easebar.com<100>");
                    JSONArray jSONArray5 = new JSONArray();
                    jSONArray5.put("https://<$gameid>.gdl.easebar.com<100>");
                    JSONArray jSONArray6 = new JSONArray();
                    String str4 = str;
                    jSONArray6.put("13.229.40.98");
                    jSONArray6.put("52.221.3.167");
                    jSONArray6.put("52.76.137.125");
                    jSONObject3.put("GPHList", jSONArray4);
                    jSONObject3.put("GDLList", jSONArray5);
                    jSONObject3.put("SplitThreshold", 2048);
                    jSONObject3.put("Removable", true);
                    jSONObject3.put("RemoveSlowCDNPercent", "50");
                    jSONObject3.put("RemoveSlowCDNSpeed", "300");
                    jSONObject3.put("RemoveSlowCDNTime", "10");
                    jSONObject3.put("Report", true);
                    jSONObject3.put("ReportUrl", "https://udt-sigma.proxima.nie.easebar.com/query");
                    jSONObject3.put(str2, false);
                    jSONObject3.put(str3, "https://dl.nstool.easebar.com/internalquery/");
                    jSONObject3.put(str4, jSONArray6);
                    jSONObject.put("guonei", jSONObject2);
                    jSONObject.put("taiwan", jSONObject3);
                    LogUtil.i(TAG, "ConfigParams [setDefaultConfing] result=" + jSONObject);
                    return jSONObject;
                } catch (Exception e2) {
                    e = e2;
                    str3 = str3;
                    str = "ListLVSIP";
                    e.printStackTrace();
                    JSONArray jSONArray42 = new JSONArray();
                    jSONArray42.put("https://<$gameid>.gph.easebar.com<100>");
                    JSONArray jSONArray52 = new JSONArray();
                    jSONArray52.put("https://<$gameid>.gdl.easebar.com<100>");
                    JSONArray jSONArray62 = new JSONArray();
                    String str42 = str;
                    jSONArray62.put("13.229.40.98");
                    jSONArray62.put("52.221.3.167");
                    jSONArray62.put("52.76.137.125");
                    jSONObject3.put("GPHList", jSONArray42);
                    jSONObject3.put("GDLList", jSONArray52);
                    jSONObject3.put("SplitThreshold", 2048);
                    jSONObject3.put("Removable", true);
                    jSONObject3.put("RemoveSlowCDNPercent", "50");
                    jSONObject3.put("RemoveSlowCDNSpeed", "300");
                    jSONObject3.put("RemoveSlowCDNTime", "10");
                    jSONObject3.put("Report", true);
                    jSONObject3.put("ReportUrl", "https://udt-sigma.proxima.nie.easebar.com/query");
                    jSONObject3.put(str2, false);
                    jSONObject3.put(str3, "https://dl.nstool.easebar.com/internalquery/");
                    jSONObject3.put(str42, jSONArray62);
                    jSONObject.put("guonei", jSONObject2);
                    jSONObject.put("taiwan", jSONObject3);
                    LogUtil.i(TAG, "ConfigParams [setDefaultConfing] result=" + jSONObject);
                    return jSONObject;
                }
                try {
                    jSONObject2.put(str, jSONArray3);
                } catch (JSONException e3) {
                    e = e3;
                    e.printStackTrace();
                    JSONArray jSONArray422 = new JSONArray();
                    jSONArray422.put("https://<$gameid>.gph.easebar.com<100>");
                    JSONArray jSONArray522 = new JSONArray();
                    jSONArray522.put("https://<$gameid>.gdl.easebar.com<100>");
                    JSONArray jSONArray622 = new JSONArray();
                    String str422 = str;
                    jSONArray622.put("13.229.40.98");
                    jSONArray622.put("52.221.3.167");
                    jSONArray622.put("52.76.137.125");
                    jSONObject3.put("GPHList", jSONArray422);
                    jSONObject3.put("GDLList", jSONArray522);
                    jSONObject3.put("SplitThreshold", 2048);
                    jSONObject3.put("Removable", true);
                    jSONObject3.put("RemoveSlowCDNPercent", "50");
                    jSONObject3.put("RemoveSlowCDNSpeed", "300");
                    jSONObject3.put("RemoveSlowCDNTime", "10");
                    jSONObject3.put("Report", true);
                    jSONObject3.put("ReportUrl", "https://udt-sigma.proxima.nie.easebar.com/query");
                    jSONObject3.put(str2, false);
                    jSONObject3.put(str3, "https://dl.nstool.easebar.com/internalquery/");
                    jSONObject3.put(str422, jSONArray622);
                    jSONObject.put("guonei", jSONObject2);
                    jSONObject.put("taiwan", jSONObject3);
                    LogUtil.i(TAG, "ConfigParams [setDefaultConfing] result=" + jSONObject);
                    return jSONObject;
                } catch (Exception e4) {
                    e = e4;
                    e.printStackTrace();
                    JSONArray jSONArray4222 = new JSONArray();
                    jSONArray4222.put("https://<$gameid>.gph.easebar.com<100>");
                    JSONArray jSONArray5222 = new JSONArray();
                    jSONArray5222.put("https://<$gameid>.gdl.easebar.com<100>");
                    JSONArray jSONArray6222 = new JSONArray();
                    String str4222 = str;
                    jSONArray6222.put("13.229.40.98");
                    jSONArray6222.put("52.221.3.167");
                    jSONArray6222.put("52.76.137.125");
                    jSONObject3.put("GPHList", jSONArray4222);
                    jSONObject3.put("GDLList", jSONArray5222);
                    jSONObject3.put("SplitThreshold", 2048);
                    jSONObject3.put("Removable", true);
                    jSONObject3.put("RemoveSlowCDNPercent", "50");
                    jSONObject3.put("RemoveSlowCDNSpeed", "300");
                    jSONObject3.put("RemoveSlowCDNTime", "10");
                    jSONObject3.put("Report", true);
                    jSONObject3.put("ReportUrl", "https://udt-sigma.proxima.nie.easebar.com/query");
                    jSONObject3.put(str2, false);
                    jSONObject3.put(str3, "https://dl.nstool.easebar.com/internalquery/");
                    jSONObject3.put(str4222, jSONArray6222);
                    jSONObject.put("guonei", jSONObject2);
                    jSONObject.put("taiwan", jSONObject3);
                    LogUtil.i(TAG, "ConfigParams [setDefaultConfing] result=" + jSONObject);
                    return jSONObject;
                }
            } catch (JSONException e5) {
                e = e5;
            } catch (Exception e6) {
                e = e6;
            }
        } catch (JSONException e7) {
            e = e7;
            str = "ListLVSIP";
            str2 = "IPDNSPicker";
        } catch (Exception e8) {
            e = e8;
            str = "ListLVSIP";
            str2 = "IPDNSPicker";
        }
        JSONArray jSONArray42222 = new JSONArray();
        jSONArray42222.put("https://<$gameid>.gph.easebar.com<100>");
        JSONArray jSONArray52222 = new JSONArray();
        jSONArray52222.put("https://<$gameid>.gdl.easebar.com<100>");
        JSONArray jSONArray62222 = new JSONArray();
        String str42222 = str;
        jSONArray62222.put("13.229.40.98");
        jSONArray62222.put("52.221.3.167");
        jSONArray62222.put("52.76.137.125");
        try {
            jSONObject3.put("GPHList", jSONArray42222);
            jSONObject3.put("GDLList", jSONArray52222);
            jSONObject3.put("SplitThreshold", 2048);
            jSONObject3.put("Removable", true);
            jSONObject3.put("RemoveSlowCDNPercent", "50");
            jSONObject3.put("RemoveSlowCDNSpeed", "300");
            jSONObject3.put("RemoveSlowCDNTime", "10");
            jSONObject3.put("Report", true);
            jSONObject3.put("ReportUrl", "https://udt-sigma.proxima.nie.easebar.com/query");
            jSONObject3.put(str2, false);
            jSONObject3.put(str3, "https://dl.nstool.easebar.com/internalquery/");
            jSONObject3.put(str42222, jSONArray62222);
        } catch (JSONException e9) {
            e9.printStackTrace();
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        try {
            jSONObject.put("guonei", jSONObject2);
            jSONObject.put("taiwan", jSONObject3);
        } catch (JSONException e11) {
            e11.printStackTrace();
        } catch (Exception e12) {
            e12.printStackTrace();
        }
        LogUtil.i(TAG, "ConfigParams [setDefaultConfing] result=" + jSONObject);
        return jSONObject;
    }

    /* JADX WARN: Removed duplicated region for block: B:76:0x00c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void parseCdnInfo(org.json.JSONArray r22) throws org.json.JSONException, java.lang.NumberFormatException {
        /*
            Method dump skipped, instructions count: 264
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.download.config.ConfigParams.parseCdnInfo(org.json.JSONArray):void");
    }

    public class CdnUnit {
        String mCdnChannel;
        ArrayList<CdnUrlUnit> mCdnList;
        int mTotalWeight;

        public CdnUnit(ArrayList<CdnUrlUnit> arrayList, int i, String str) {
            new ArrayList();
            this.mCdnList = arrayList;
            this.mTotalWeight = i;
            this.mCdnChannel = str;
        }

        public ArrayList<CdnUrlUnit> getmCdnList() {
            return this.mCdnList;
        }

        public void setmCdnList(ArrayList<CdnUrlUnit> arrayList) {
            this.mCdnList = arrayList;
        }

        public int getmTotalWeight() {
            return this.mTotalWeight;
        }

        public void setmTotalWeight(int i) {
            this.mTotalWeight = i;
        }

        public String getmChannel() {
            return this.mCdnChannel;
        }

        public void setmChannel(String str) {
            this.mCdnChannel = str;
        }

        public String toString() {
            String str = "mCdnChannel=" + this.mCdnChannel + ", mTotalWeight=" + this.mTotalWeight;
            Iterator<CdnUrlUnit> it = this.mCdnList.iterator();
            while (it.hasNext()) {
                str = str + ", cdnUrlUnit=" + it.next().toString() + " ";
            }
            return str;
        }
    }

    public class CdnUrlUnit {
        ArrayList<String> mIpList;
        String mPort;
        String mUrl;
        String mUrlWithPort;
        int mWeight;

        public CdnUrlUnit(String str, String str2, String str3, int i) {
            this.mIpList = null;
            this.mUrlWithPort = str;
            this.mUrl = str2;
            this.mPort = str3;
            this.mWeight = i;
            DnsCore.getInstances().init(str2);
            ArrayList<DnsParams.Unit> arrayListStart = DnsCore.getInstances().start();
            if (arrayListStart == null || arrayListStart.size() <= 0) {
                return;
            }
            this.mIpList = arrayListStart.get(0).ipArrayList;
        }

        public String getmUrlWithPort() {
            return this.mUrlWithPort;
        }

        public void setmUrlWithPort(String str) {
            this.mUrlWithPort = str;
        }

        public String getmUrl() {
            return this.mUrl;
        }

        public void setmUrl(String str) {
            this.mUrl = str;
        }

        public String getmPort() {
            return this.mPort;
        }

        public void setmPort(String str) {
            this.mPort = str;
        }

        public int getmWeight() {
            return this.mWeight;
        }

        public void setmWeight(int i) {
            this.mWeight = i;
        }

        public ArrayList<String> getmIpList() {
            return this.mIpList;
        }

        public void setmIpList(ArrayList<String> arrayList) {
            this.mIpList = arrayList;
        }

        public String toString() {
            return "mUrlWithPort=" + this.mUrlWithPort + ", mUrl=" + this.mUrl + ", mPort=" + this.mPort + ", mWeight=" + this.mWeight + ", mIpList=" + this.mIpList;
        }
    }
}