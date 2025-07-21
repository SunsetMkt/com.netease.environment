package com.netease.pharos.linkcheck;

import android.text.TextUtils;
import com.netease.pharos.Const;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.deviceCheck.DeviceInfo;
import com.netease.pharos.network.NetworkStatus;
import com.netease.pharos.qos.QosProxy;
import com.netease.pharos.util.Util;
import java.util.ArrayList;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class Result {
    private static final String TAG = "LinkCheckResult";
    private static Result sLinkCheckResult;
    private String mLinktestId = null;
    private String mIpaddr = null;
    private int mTestlog = 1;
    private String mType = null;
    private String mNapIcmpDest = "";
    private int mNapIcmpLost = -1;
    private double mNapIcmpRtt = -1.0d;
    private double mNapIcmpStddev = -1.0d;
    private String mRapIcmpDest = "";
    private int mRapIcmpLost = -1;
    private double mRapIcmpRtt = -1.0d;
    private double mRapIcmpStddev = -1.0d;
    private String mRapTransferDest = "";
    private double mRapTransferFail = -1.0d;
    private long mRapTransferRtt = -1;
    private long mRapTransferSpeed = 0;
    private double mRapTransferStddev = -1.0d;
    private String mRapUdpDest = "";
    private double mRapUdpLost = -1.0d;
    private long mRapUdpRtt = -1;
    private double mRapUdpStddev = -1.0d;
    private String mSapTransferDest = "";
    private double mSapTransferFail = -1.0d;
    private long mSapTransferRtt = -1;
    private long mSapTransferSpeed = 0;
    private double mSapTransferStddev = -1.0d;
    private String mSapUdpDest = "";
    private double mSapUdpLost = -1.0d;
    private long mSapUdpRtt = -1;
    private double mSapUdpStddev = -1.0d;
    private ArrayList<String> mIpList = new ArrayList<>();
    private String mRapMtr = null;
    private String mRapQosExpire = "0";

    private Result() {
    }

    public static Result getInstance() {
        if (sLinkCheckResult == null) {
            sLinkCheckResult = new Result();
        }
        return sLinkCheckResult;
    }

    public String getmRapQosExpire() {
        return this.mRapQosExpire;
    }

    public void setmRapQosExpire(String str) {
        this.mRapQosExpire = str;
    }

    public String getLinktestId() {
        if (TextUtils.isEmpty(this.mLinktestId)) {
            this.mLinktestId = PharosProxy.getInstance().getUdid() + "-" + System.currentTimeMillis();
        }
        return this.mLinktestId;
    }

    public void setmLinktestId(String str) {
        this.mLinktestId = str;
    }

    public String getmIpaddr() {
        return this.mIpaddr;
    }

    public void setmIpaddr(String str) {
        this.mIpaddr = str;
    }

    public int getTestlog() {
        int testLog = PharosProxy.getInstance().getTestLog();
        this.mTestlog = testLog;
        return testLog;
    }

    public String getmType() {
        return this.mType;
    }

    public void setmType(String str) {
        this.mType = str;
    }

    public int getmNapIcmpLost() {
        int i = this.mNapIcmpLost;
        if (i == 0) {
            this.mNapIcmpLost = 0;
        } else if (-1 == i || 1.0d == this.mSapUdpLost) {
            this.mNapIcmpLost = 100;
        } else if (i < 1) {
            this.mNapIcmpLost = i * 100;
        }
        return this.mNapIcmpLost;
    }

    public void setmNapIcmpLost(int i) {
        this.mNapIcmpLost = i;
    }

    public double getmNapIcmpRtt() {
        if (-1.0d == this.mNapIcmpRtt) {
            this.mNapIcmpRtt = 1000.0d;
        }
        return this.mNapIcmpRtt;
    }

    public void setmNapIcmpRtt(double d) {
        this.mNapIcmpRtt = d;
    }

    public int getmRapIcmpLost() {
        int i = this.mRapIcmpLost;
        if (-1 == i || 1.0d == this.mSapUdpLost) {
            this.mRapIcmpLost = 100;
        } else if (i < 1) {
            this.mRapIcmpLost = i * 100;
        }
        return this.mRapIcmpLost;
    }

    public void setmRapIcmpLost(int i) {
        this.mRapIcmpLost = i;
    }

    public double getmRapIcmpRtt() {
        if (-1.0d == this.mRapIcmpRtt) {
            this.mRapIcmpRtt = 1000.0d;
        }
        return this.mRapIcmpRtt;
    }

    public void setmRapIcmpRtt(double d) {
        this.mRapIcmpRtt = d;
    }

    public double getmRapTransferFail() {
        double d = this.mRapTransferFail;
        if (-1.0d == d || 1.0d == d) {
            this.mRapTransferFail = 100.0d;
        }
        double d2 = this.mRapTransferFail;
        if (d2 < 1.0d) {
            this.mRapTransferFail = d2 * 100.0d;
        }
        return this.mRapTransferFail;
    }

    public void setmRapTransferFail(double d) {
        this.mRapTransferFail = d;
    }

    public long getmRapTransferRtt() {
        if (-1 == this.mRapTransferRtt) {
            this.mRapTransferRtt = 1000L;
        }
        return this.mRapTransferRtt;
    }

    public void setmRapTransferRtt(long j) {
        this.mRapTransferRtt = j;
    }

    public long getmRapTransferSpeed() {
        if (-1 == this.mRapTransferSpeed) {
            this.mRapTransferSpeed = 0L;
        }
        return this.mRapTransferSpeed;
    }

    public void setmRapTransferSpeed(long j) {
        this.mRapTransferSpeed = j;
    }

    public double getmRapUdpLost() {
        double d = this.mRapUdpLost;
        if (-1.0d == d || 1.0d == d) {
            this.mRapUdpLost = 100.0d;
        } else if (d < 1.0d) {
            this.mRapUdpLost = d * 100.0d;
        }
        return this.mRapUdpLost;
    }

    public void setmRapUdpLost(double d) {
        this.mRapUdpLost = d;
    }

    public long getmRapUdpRtt() {
        if (-1 == this.mRapUdpRtt) {
            this.mRapUdpRtt = 1000L;
        }
        return this.mRapUdpRtt;
    }

    public void setmRapUdpRtt(long j) {
        this.mRapUdpRtt = j;
    }

    public double getmSapTransferFail() {
        double d = this.mSapTransferFail;
        if (-1.0d == d || 1.0d == d) {
            this.mSapTransferFail = 100.0d;
        }
        double d2 = this.mSapTransferFail;
        if (d2 < 1.0d) {
            this.mSapTransferFail = d2 * 100.0d;
        }
        return this.mSapTransferFail;
    }

    public void setmSapTransferFail(double d) {
        this.mSapTransferFail = d;
    }

    public long getmSapTransferRtt() {
        if (-1 == this.mSapTransferRtt) {
            this.mSapTransferRtt = 1000L;
        }
        return this.mSapTransferRtt;
    }

    public void setmSapTransferRtt(long j) {
        this.mSapTransferRtt = j;
    }

    public long getmSapTransferSpeed() {
        if (-1 == this.mSapTransferSpeed) {
            this.mSapTransferSpeed = 0L;
        }
        return this.mSapTransferSpeed;
    }

    public void setmSapTransferSpeed(long j) {
        this.mSapTransferSpeed = j;
    }

    public double getmSapUdpLost() {
        double d = this.mSapUdpLost;
        if (-1.0d == d || 1.0d == d) {
            this.mSapUdpLost = 100.0d;
        } else if (d < 1.0d) {
            this.mSapUdpLost = d * 100.0d;
        }
        return this.mSapUdpLost;
    }

    public void setmSapUdpLost(double d) {
        this.mSapUdpLost = d;
    }

    public long getmSapUdpRtt() {
        if (-1 == this.mSapUdpRtt) {
            this.mSapUdpRtt = 1000L;
        }
        return this.mSapUdpRtt;
    }

    public void setmSapUdpRtt(long j) {
        this.mSapUdpRtt = j;
    }

    public ArrayList<String> getmResolveHost() {
        return this.mIpList;
    }

    public void setmResolveHost(ArrayList<String> arrayList) {
        this.mIpList = arrayList;
    }

    public String getmRapMtr() {
        return this.mRapMtr;
    }

    public void setmRapMtr(String str) {
        this.mRapMtr = str;
    }

    public String getmNapIcmpDest() {
        return this.mNapIcmpDest;
    }

    public void setmNapIcmpDest(String str) {
        this.mNapIcmpDest = str;
    }

    public String getmRapIcmpDest() {
        return this.mRapIcmpDest;
    }

    public void setmRapIcmpDest(String str) {
        this.mRapIcmpDest = str;
    }

    public String getmRapTransferDest() {
        return this.mRapTransferDest;
    }

    public void setmRapTransferDest(String str) {
        this.mRapTransferDest = str;
    }

    public String getmRapUdpDest() {
        return this.mRapUdpDest;
    }

    public void setmRapUdpDest(String str) {
        this.mRapUdpDest = str;
    }

    public String getmSapTransferDest() {
        return this.mSapTransferDest;
    }

    public void setmSapTransferDest(String str) {
        this.mSapTransferDest = str;
    }

    public String getmSapUdpDest() {
        return this.mSapUdpDest;
    }

    public void setmSapUdpDest(String str) {
        this.mSapUdpDest = str;
    }

    public double getmNapIcmpStddev() {
        return this.mNapIcmpStddev;
    }

    public void setmNapIcmpStddev(double d) {
        this.mNapIcmpStddev = d;
    }

    public double getmRapIcmpStddev() {
        return this.mRapIcmpStddev;
    }

    public void setmRapIcmpStddev(double d) {
        this.mRapIcmpStddev = d;
    }

    public double getmRapTransferStddev() {
        return this.mRapTransferStddev;
    }

    public void setmRapTransferStddev(double d) {
        this.mRapTransferStddev = d;
    }

    public double getmRapUdpStddev() {
        return this.mRapUdpStddev;
    }

    public void setmRapUdpStddev(double d) {
        this.mRapUdpStddev = d;
    }

    public double getmSapTransferStddev() {
        return this.mSapTransferStddev;
    }

    public void setmSapTransferStddev(double d) {
        this.mSapTransferStddev = d;
    }

    public double getmSapUdpStddev() {
        return this.mSapUdpStddev;
    }

    public void setmSapUdpStddev(double d) {
        this.mSapUdpStddev = d;
    }

    public void clean() {
        this.mLinktestId = null;
        this.mIpaddr = null;
        this.mType = null;
        this.mNapIcmpDest = "";
        this.mNapIcmpLost = -1;
        this.mNapIcmpRtt = -1.0d;
        this.mNapIcmpStddev = -1.0d;
        this.mRapIcmpDest = "";
        this.mRapIcmpLost = -1;
        this.mRapIcmpRtt = -1.0d;
        this.mRapIcmpStddev = -1.0d;
        this.mRapTransferDest = "";
        this.mRapTransferFail = -1.0d;
        this.mRapTransferRtt = -1L;
        this.mRapTransferSpeed = -1L;
        this.mRapTransferStddev = -1.0d;
        this.mRapUdpDest = "";
        this.mRapUdpLost = -1.0d;
        this.mRapUdpRtt = -1L;
        this.mRapUdpStddev = -1.0d;
        this.mSapTransferDest = "";
        this.mSapTransferFail = -1.0d;
        this.mSapTransferRtt = -1L;
        this.mSapTransferSpeed = -1L;
        this.mSapTransferStddev = -1.0d;
        this.mSapUdpDest = "";
        this.mSapUdpLost = -1.0d;
        this.mSapUdpRtt = -1L;
        this.mSapUdpStddev = -1.0d;
        this.mIpList = new ArrayList<>();
        this.mRapMtr = null;
        this.mRapQosExpire = "0";
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("\nmProject=");
        stringBuffer.append(PharosProxy.getInstance().getProjectId()).append("\nmUdid=");
        stringBuffer.append(PharosProxy.getInstance().getUdid()).append("\nmNetid=");
        stringBuffer.append(PharosProxy.getInstance().getmNetId()).append("\nmLinktestId=");
        stringBuffer.append(getLinktestId()).append("\nmIpaddr=");
        stringBuffer.append(this.mIpaddr).append("\nmTestlog=");
        stringBuffer.append(getTestlog()).append("\nmType=");
        stringBuffer.append(this.mType).append("\nmNapIcmpDest=");
        stringBuffer.append(this.mNapIcmpDest).append("\nmNapIcmpLost=");
        stringBuffer.append(this.mNapIcmpLost).append("\nmNapIcmpRtt=");
        stringBuffer.append(this.mNapIcmpRtt).append("\nmNapIcmpStddev=");
        stringBuffer.append(this.mNapIcmpStddev).append("\nmRapIcmpDest=");
        stringBuffer.append(this.mRapIcmpDest).append("\nmRapIcmpLost=");
        stringBuffer.append(this.mRapIcmpLost).append("\nmRapIcmpRtt=");
        stringBuffer.append(this.mRapIcmpRtt).append("\nmRapIcmpStddev=");
        stringBuffer.append(this.mRapIcmpStddev).append("\nmRapTransferDest=");
        stringBuffer.append(this.mRapTransferDest).append("\nmRapTransferFail=");
        stringBuffer.append(this.mRapTransferFail).append("\nmRapTransferRtt=");
        stringBuffer.append(this.mRapTransferRtt).append("\nmRapTransferSpeed=");
        stringBuffer.append(this.mRapTransferSpeed).append("\nmRapTransferStddev=");
        stringBuffer.append(this.mRapTransferStddev).append("\nmRapUdpDest=");
        stringBuffer.append(this.mRapUdpDest).append("\nmRapUdpLost=");
        stringBuffer.append(this.mRapUdpLost).append("\nmRapUdpRtt=");
        stringBuffer.append(this.mRapUdpRtt).append("\nmRapUdpStddev=");
        stringBuffer.append(this.mRapUdpStddev).append("\nmSapTransferDest=");
        stringBuffer.append(this.mSapTransferDest).append("\nmSapTransferFail=");
        stringBuffer.append(this.mSapTransferFail).append("\nmSapTransferRtt=");
        stringBuffer.append(this.mSapTransferRtt).append("\nmSapTransferSpeed=");
        stringBuffer.append(this.mSapTransferSpeed).append("\nmSapTransferStddev=");
        stringBuffer.append(this.mSapTransferStddev).append("\nmSapUdpDest=");
        stringBuffer.append(this.mSapUdpDest).append("\nmSapUdpLost=");
        stringBuffer.append(this.mSapUdpLost).append("\nmSapUdpRtt=");
        stringBuffer.append(this.mSapUdpRtt).append("\nmSapUdpStddev=");
        stringBuffer.append(this.mSapUdpStddev).append("\nmIpList=");
        stringBuffer.append(this.mIpList.toString()).append("\nmRapMtr=");
        stringBuffer.append(this.mRapMtr).append("\nmRapQosExpire=");
        stringBuffer.append(this.mRapQosExpire).append("\n");
        return stringBuffer.toString();
    }

    public String getLinkCheckResultInfo() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("project", PharosProxy.getInstance().getProjectId());
            jSONObject.put("version", Const.VERSION);
            jSONObject.put("udid", PharosProxy.getInstance().getUdid());
            jSONObject.put("netid", PharosProxy.getInstance().getmNetId());
            jSONObject.put("linktest_id", getLinktestId());
            jSONObject.put("network", NetworkStatus.getInstance().getNetwork());
            jSONObject.put("network_switch", NetworkStatus.getInstance().isNetworkChanged() ? "1" : "0");
            jSONObject.put("network_isp_name", DeviceInfo.getInstance().getNetworkIspName());
            jSONObject.put("qos_effective", QosProxy.getInstance().getQosEffective());
            String ipaddr = DeviceInfo.getInstance().getIpaddr();
            String str = "";
            if (TextUtils.isEmpty(ipaddr)) {
                ipaddr = "";
            }
            String region = DeviceInfo.getInstance().getRegion();
            if (TextUtils.isEmpty(region)) {
                region = "";
            }
            String str2 = DeviceInfo.getInstance().getmProbeRegion();
            if (!TextUtils.isEmpty(str2)) {
                str = str2;
            }
            jSONObject.put("ipaddr", ipaddr);
            jSONObject.put("region", region);
            jSONObject.put("probe_region", str);
            jSONObject.put("testlog", getTestlog());
            jSONObject.put("cell_id", Util.getCellId());
            jSONObject.put("ip_local", Util.getLocalIp());
            jSONObject.put("network_ipv6", DeviceInfo.getInstance().isSupportIpV6());
            jSONObject.put("ip_local_v6", DeviceInfo.getInstance().getIpLocalAddrV6());
            jSONObject.put("ipaddr_v6", DeviceInfo.getInstance().getIpaddrV6());
            jSONObject.put("type", "probe");
            jSONObject.put("os_name", "android");
            if (!TextUtils.isEmpty(this.mNapIcmpDest)) {
                jSONObject.put("nap_icmp_dest", this.mNapIcmpDest);
                jSONObject.put("nap_icmp_lost", getmNapIcmpLost());
                jSONObject.put("nap_icmp_rtt", getmNapIcmpRtt());
                jSONObject.put("nap_icmp_stddev", getmNapIcmpStddev());
            }
            if (!TextUtils.isEmpty(this.mRapIcmpDest)) {
                jSONObject.put("rap_icmp_dest", this.mRapIcmpDest);
                jSONObject.put("rap_icmp_lost", getmRapIcmpLost());
                jSONObject.put("rap_icmp_rtt", getmRapIcmpRtt());
                jSONObject.put("rap_icmp_stddev", getmRapIcmpStddev());
            }
            if (!TextUtils.isEmpty(this.mRapTransferDest)) {
                jSONObject.put("rap_transfer_dest", this.mRapTransferDest);
                jSONObject.put("rap_transfer_fail", getmRapTransferFail());
                jSONObject.put("rap_transfer_rtt", getmRapTransferRtt());
                jSONObject.put("rap_transfer_speed", getmRapTransferSpeed());
                jSONObject.put("rap_transfer_stddev", getmRapTransferStddev());
            }
            if (!TextUtils.isEmpty(this.mRapUdpDest)) {
                jSONObject.put("rap_udp_dest", this.mRapUdpDest);
                jSONObject.put("rap_udp_lost", getmRapUdpLost());
                jSONObject.put("rap_udp_rtt", getmRapUdpRtt());
                jSONObject.put("rap_udp_stddev", getmRapUdpStddev());
            }
            if (!TextUtils.isEmpty(this.mSapTransferDest)) {
                jSONObject.put("sap_transfer_dest", this.mSapTransferDest);
                jSONObject.put("sap_transfer_fail", getmSapTransferFail());
                jSONObject.put("sap_transfer_rtt", getmSapTransferRtt());
                jSONObject.put("sap_transfer_speed", getmSapTransferSpeed());
                jSONObject.put("sap_transfer_stddev", getmSapTransferStddev());
            }
            if (!TextUtils.isEmpty(this.mSapUdpDest)) {
                jSONObject.put("sap_udp_dest", this.mSapUdpDest);
                jSONObject.put("sap_udp_lost", getmSapUdpLost());
                jSONObject.put("sap_udp_rtt", getmSapUdpRtt());
                jSONObject.put("sap_udp_stddev", getmSapUdpStddev());
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < this.mIpList.size(); i++) {
                stringBuffer.append(this.mIpList.get(i));
                if (i != this.mIpList.size() - 1) {
                    stringBuffer.append(",");
                }
            }
            if (!TextUtils.isEmpty(stringBuffer.toString())) {
                jSONObject.put("resolve_host", stringBuffer.toString());
            }
            jSONObject.put("rap_qos_expire", this.mRapQosExpire);
        } catch (Exception unused) {
        }
        return jSONObject.toString();
    }
}