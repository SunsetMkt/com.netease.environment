package com.netease.ntunisdk.base;

import android.app.Activity;
import android.content.res.Resources;
import android.text.TextUtils;
import com.facebook.common.util.UriUtil;
import com.netease.androidcrashhandler.Const;
import com.netease.mpay.ps.codescanner.CodeScannerConst;
import com.netease.ntunisdk.base.function.c;
import com.netease.ntunisdk.base.function.f;
import com.netease.ntunisdk.base.utils.ApiRequestUtil;
import com.netease.ntunisdk.base.utils.HTTPCallback;
import com.netease.ntunisdk.base.utils.HTTPQueue;
import com.netease.ntunisdk.base.utils.ResUtils;
import com.netease.ntunisdk.base.utils.StrUtil;
import com.netease.ntunisdk.base.view.Alerter;
import com.netease.ntunisdk.base.view.NtSdkStringClickableSpan;
import com.netease.ntunisdk.base.view.NtSdkTagParser$OnSpanClickListener;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.unilogger.global.Const;
import com.tencent.connect.common.Constants;
import com.tencent.open.SocialConstants;
import com.tencent.open.SocialOperation;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class JfGas {

    /* renamed from: a, reason: collision with root package name */
    private SdkBase f1602a;

    public interface CreateOrderCallback {
        void callbackResult(String str);
    }

    public interface QueryProductCallback {
        void callbackResult();
    }

    public JfGas(SdkBase sdkBase) {
        this.f1602a = sdkBase;
    }

    public void createOrder(final OrderInfo orderInfo, final CreateOrderCallback createOrderCallback) {
        if (!OrderInfo.hasProduct(orderInfo)) {
            queryProduct(new QueryProductCallback() { // from class: com.netease.ntunisdk.base.JfGas.1
                /* JADX WARN: Removed duplicated region for block: B:35:0x0089 A[SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:38:? A[LOOP:0: B:10:0x002f->B:38:?, LOOP_END, SYNTHETIC] */
                @Override // com.netease.ntunisdk.base.JfGas.QueryProductCallback
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public final void callbackResult() throws java.lang.Throwable {
                    /*
                        r8 = this;
                        java.util.Hashtable r0 = com.netease.ntunisdk.base.OrderInfo.getProductList()
                        com.netease.ntunisdk.base.OrderInfo r1 = r2
                        boolean r1 = r1.isCartOrder()
                        java.lang.String r2 = "UniSDK JfGas"
                        if (r1 != 0) goto L9b
                        com.netease.ntunisdk.base.OrderInfo r1 = r2
                        java.lang.String r1 = r1.getProductId()
                        boolean r1 = com.netease.ntunisdk.base.OrderInfo.hasProduct(r1)
                        if (r1 != 0) goto L9b
                        if (r0 == 0) goto L9b
                        boolean r1 = r0.isEmpty()
                        if (r1 != 0) goto L9b
                        java.lang.String r1 = "check reg product id"
                        com.netease.ntunisdk.base.UniSdkUtils.d(r2, r1)
                        java.util.Set r1 = r0.keySet()
                        java.util.Iterator r1 = r1.iterator()
                    L2f:
                        boolean r3 = r1.hasNext()
                        if (r3 == 0) goto L9b
                        java.lang.Object r3 = r1.next()
                        java.lang.String r3 = (java.lang.String) r3
                        java.lang.Object r4 = r0.get(r3)
                        com.netease.ntunisdk.base.OrderInfo$ProductInfo r4 = (com.netease.ntunisdk.base.OrderInfo.ProductInfo) r4
                        java.util.Map<java.lang.String, java.lang.String> r4 = r4.sdkPids
                        if (r4 == 0) goto L86
                        boolean r5 = r4.isEmpty()
                        if (r5 != 0) goto L86
                        java.lang.StringBuilder r5 = new java.lang.StringBuilder
                        java.lang.String r6 = "sdkPids length:"
                        r5.<init>(r6)
                        int r6 = r4.size()
                        r5.append(r6)
                        java.lang.String r5 = r5.toString()
                        com.netease.ntunisdk.base.UniSdkUtils.d(r2, r5)
                        java.util.Set r5 = r4.keySet()
                        java.util.Iterator r5 = r5.iterator()
                    L68:
                        boolean r6 = r5.hasNext()
                        if (r6 == 0) goto L86
                        java.lang.Object r6 = r5.next()
                        java.lang.String r6 = (java.lang.String) r6
                        com.netease.ntunisdk.base.OrderInfo r7 = r2
                        java.lang.String r7 = r7.getProductId()
                        java.lang.Object r6 = r4.get(r6)
                        boolean r6 = r7.equals(r6)
                        if (r6 == 0) goto L68
                        r4 = 1
                        goto L87
                    L86:
                        r4 = 0
                    L87:
                        if (r4 == 0) goto L2f
                        java.lang.String r0 = "check reg product id:"
                        java.lang.String r1 = java.lang.String.valueOf(r3)
                        java.lang.String r0 = r0.concat(r1)
                        com.netease.ntunisdk.base.UniSdkUtils.d(r2, r0)
                        com.netease.ntunisdk.base.OrderInfo r0 = r2
                        r0.setProductId(r3)
                    L9b:
                        org.json.JSONObject r0 = new org.json.JSONObject
                        r0.<init>()
                        java.lang.String r1 = "step"
                        java.lang.String r3 = "createOrder_queryProduct"
                        r0.putOpt(r1, r3)     // Catch: org.json.JSONException -> Lb7
                        java.lang.String r1 = "channel"
                        com.netease.ntunisdk.base.JfGas r3 = com.netease.ntunisdk.base.JfGas.this     // Catch: org.json.JSONException -> Lb7
                        com.netease.ntunisdk.base.SdkBase r3 = com.netease.ntunisdk.base.JfGas.a(r3)     // Catch: org.json.JSONException -> Lb7
                        java.lang.String r3 = r3.getChannel()     // Catch: org.json.JSONException -> Lb7
                        r0.putOpt(r1, r3)     // Catch: org.json.JSONException -> Lb7
                        goto Lcd
                    Lb7:
                        r1 = move-exception
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder
                        java.lang.String r4 = "extraJson:"
                        r3.<init>(r4)
                        java.lang.String r1 = r1.getMessage()
                        r3.append(r1)
                        java.lang.String r1 = r3.toString()
                        com.netease.ntunisdk.base.UniSdkUtils.d(r2, r1)
                    Lcd:
                        com.netease.ntunisdk.base.JfGas r1 = com.netease.ntunisdk.base.JfGas.this
                        com.netease.ntunisdk.base.SdkBase r1 = com.netease.ntunisdk.base.JfGas.a(r1)
                        com.netease.ntunisdk.base.OrderInfo r2 = r2
                        java.lang.String r0 = r0.toString()
                        r1.saveClientLog(r2, r0)
                        com.netease.ntunisdk.base.OrderInfo r0 = r2
                        java.lang.String r0 = r0.getOrderChannel()
                        com.netease.ntunisdk.base.JfGas r1 = com.netease.ntunisdk.base.JfGas.this
                        com.netease.ntunisdk.base.OrderInfo r2 = r2
                        com.netease.ntunisdk.base.JfGas$CreateOrderCallback r3 = r3
                        com.netease.ntunisdk.base.JfGas.a(r1, r2, r3, r0)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.JfGas.AnonymousClass1.callbackResult():void");
                }
            });
        } else {
            a(orderInfo, createOrderCallback, orderInfo.getOrderChannel());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    public void a(final OrderInfo orderInfo, final CreateOrderCallback createOrderCallback, String str) throws Throwable {
        String str2;
        int iOptInt;
        int i;
        OrderInfo orderInfo2;
        String str3;
        String userName;
        String serverId;
        String uid;
        JSONObject jSONObject;
        Map<String, String> mapJsonToStrMap;
        JSONObject jSONObject2;
        UniSdkUtils.i("UniSDK JfGas", "JfGas createOrder");
        OrderInfo orderInfo3 = new OrderInfo(orderInfo);
        PayChannelManager payChannelManager = this.f1602a.getPayChannelManager();
        if (payChannelManager == null || !payChannelManager.allyPayEnabled()) {
            str2 = str;
        } else {
            str2 = "allysdk";
            orderInfo.setOrderChannel("allysdk");
        }
        if (this.f1602a.hasFeature("VIRTUAL_ORDER")) {
            str2 = "basechannel";
            orderInfo.setOrderChannel("basechannel");
        }
        String channel = str2;
        JSONObject jSONObject3 = new JSONObject();
        try {
            jSONObject3.putOpt(OneTrackParams.XMSdkParams.STEP, "createOrder_in");
        } catch (JSONException e) {
            UniSdkUtils.d("UniSDK JfGas", "extraJson:" + e.getMessage());
        }
        this.f1602a.saveClientLog(orderInfo, jSONObject3.toString());
        if (TextUtils.isEmpty(channel)) {
            UniSdkUtils.i("UniSDK JfGas", "paychannel empty");
            orderInfo.setOrderStatus(3);
            orderInfo.setOrderErrReason("paychannel empty");
            this.f1602a.checkOrderDone(orderInfo);
            return;
        }
        String propStr = this.f1602a.getPropStr(ConstProp.UNISDK_CREATEORDER_URL);
        if (TextUtils.isEmpty(propStr)) {
            String propStr2 = this.f1602a.getPropStr(ConstProp.UNISDK_JF_GAS3_URL);
            if (!TextUtils.isEmpty(propStr2)) {
                StringBuilder sb = new StringBuilder(propStr2);
                if (propStr2.endsWith("/")) {
                    sb.append("create_order");
                } else {
                    sb.append("/create_order");
                }
                propStr = sb.toString();
            }
        }
        if (TextUtils.isEmpty(orderInfo.getJfGas3Url()) && !TextUtils.isEmpty(propStr)) {
            orderInfo.setJfGas3Url(propStr.substring(0, propStr.lastIndexOf("/")));
        } else {
            UniSdkUtils.d("UniSDK JfGas", "url:order.getJfGas3Url()");
            String jfGas3Url = orderInfo.getJfGas3Url();
            if (!TextUtils.isEmpty(jfGas3Url)) {
                StringBuilder sb2 = new StringBuilder(jfGas3Url);
                if (jfGas3Url.endsWith("/")) {
                    sb2.append("create_order");
                } else {
                    sb2.append("/create_order");
                }
                propStr = sb2.toString();
            }
        }
        String str4 = propStr;
        if (TextUtils.isEmpty(str4)) {
            UniSdkUtils.e("UniSDK JfGas", "ConstProp.UNISDK_CREATEORDER_URL is empty");
            orderInfo.setOrderStatus(3);
            orderInfo.setOrderErrReason("create order fail");
            this.f1602a.checkOrderDone(orderInfo);
            return;
        }
        String propStr3 = this.f1602a.getPropStr(ConstProp.UID);
        if (TextUtils.isEmpty(propStr3) || "0".equals(propStr3)) {
            propStr3 = this.f1602a.getPropStr(ConstProp.GAS3_UID);
        }
        String str5 = propStr3;
        String propStr4 = this.f1602a.getPropStr(ConstProp.USERINFO_UID);
        String str6 = "0";
        String propStr5 = this.f1602a.getPropStr(ConstProp.USERINFO_HOSTID);
        int propInt = this.f1602a.getPropInt(ConstProp.USERINFO_AID, -1);
        String jfExtInfo = orderInfo.getJfExtInfo();
        if (TextUtils.isEmpty(jfExtInfo)) {
            iOptInt = propInt;
            jfExtInfo = this.f1602a.getPropStr(ConstProp.UNISDK_EXT_INFO);
        } else {
            iOptInt = propInt;
        }
        String str7 = "4";
        if (orderInfo.isWebPayment()) {
            String propStr6 = this.f1602a.getPropStr(ConstProp.UNISDK_SERVER_PRIVATEPARAM);
            if (!TextUtils.isEmpty(propStr6)) {
                jfExtInfo = propStr6;
            }
            str6 = "4";
            i = 2;
        } else {
            i = 1;
        }
        if (TextUtils.isEmpty(jfExtInfo)) {
            jfExtInfo = this.f1602a.getPropStr(ConstProp.UNISDK_SERVER_PRIVATEPARAM);
        }
        String jfExtInfo2 = jfExtInfo;
        String qrCodeParams = orderInfo.getQrCodeParams();
        if (!orderInfo.isWebPayment() || TextUtils.isEmpty(qrCodeParams)) {
            orderInfo2 = orderInfo3;
            str3 = ConstProp.UNISDK_CREATEORDER_URL;
            userName = propStr4;
            str7 = str6;
            serverId = propStr5;
            uid = str5;
        } else {
            orderInfo2 = orderInfo3;
            str3 = ConstProp.UNISDK_CREATEORDER_URL;
            UniSdkUtils.d("UniSDK JfGas", "qrCodeParams=".concat(String.valueOf(qrCodeParams)));
            try {
                jSONObject2 = new JSONObject(qrCodeParams);
            } catch (JSONException e2) {
                UniSdkUtils.d("UniSDK JfGas", "\u6570\u636eqrCodeParams json\u89e3\u6790\u9519\u8bef:" + e2.getMessage());
                jSONObject2 = null;
            }
            if (jSONObject2 != null) {
                uid = jSONObject2.optString(ConstProp.UID);
                String strOptString = jSONObject2.optString(ConstProp.USERINFO_UID);
                String strOptString2 = jSONObject2.optString(ConstProp.USERINFO_HOSTID);
                if (jSONObject2.optInt(ConstProp.USERINFO_AID, 0) != 0) {
                    iOptInt = jSONObject2.optInt(ConstProp.USERINFO_AID, 0);
                }
                propStr5 = strOptString2;
                propStr4 = strOptString;
            } else {
                uid = str5;
            }
            userName = propStr4;
            serverId = propStr5;
        }
        if (TextUtils.isEmpty(orderInfo.getUid())) {
            orderInfo.setUid(uid);
        } else {
            uid = orderInfo.getUid();
        }
        if (TextUtils.isEmpty(orderInfo.getAid())) {
            orderInfo.setAid(String.valueOf(iOptInt));
        } else {
            iOptInt = Integer.parseInt(orderInfo.getAid());
        }
        int i2 = iOptInt;
        if (TextUtils.isEmpty(orderInfo.getServerId())) {
            orderInfo.setServerId(serverId);
        } else {
            serverId = orderInfo.getServerId();
        }
        if (TextUtils.isEmpty(orderInfo.getUserName())) {
            orderInfo.setUserName(userName);
        } else {
            userName = orderInfo.getUserName();
        }
        if (TextUtils.isEmpty(orderInfo.getJfExtInfo())) {
            orderInfo.setJfExtInfo(jfExtInfo2);
        } else {
            jfExtInfo2 = orderInfo.getJfExtInfo();
        }
        try {
            int i3 = Integer.parseInt(serverId);
            JSONObject jSONObject4 = new JSONObject();
            if ("netease_simulator".equals(channel)) {
                UniSdkUtils.d("UniSDK JfGas", "change netease_simulator paychannel");
                channel = this.f1602a.getChannel();
                orderInfo.setOrderChannel(channel);
            }
            UniSdkUtils.d("UniSDK JfGas", "paychannel=".concat(String.valueOf(channel)));
            try {
                jSONObject4.put("gameid", this.f1602a.getPropStr("JF_GAMEID"));
                jSONObject4.put("hostid", i3);
                jSONObject4.put("login_channel", SdkBase.a());
                jSONObject4.put(CodeScannerConst.PAY_CHANNEL, channel);
                jSONObject4.put(Const.APP_CHANNEL, this.f1602a.getAppChannel());
                jSONObject4.put("platform", this.f1602a.getPlatform());
                jSONObject4.put("sdkuid", uid);
                SdkBase sdkBase = this.f1602a;
                jSONObject4.put("sdk_version", sdkBase.getSDKVersion(sdkBase.getChannel()));
                jSONObject4.put("aid", i2);
                jSONObject4.put(Const.CONFIG_KEY.ROLEID, userName);
                jSONObject4.put("goodsid", orderInfo.getProductId());
                jSONObject4.put("goodscount", orderInfo.getCount());
                jSONObject4.put("timestamp", (int) (System.currentTimeMillis() / 1000));
                String udid = this.f1602a.getUdid();
                if (TextUtils.isEmpty(udid)) {
                    String deviceUDID = UniSdkUtils.getDeviceUDID(this.f1602a.myCtx);
                    if (TextUtils.isEmpty(deviceUDID)) {
                        deviceUDID = "unknown";
                    }
                    String str8 = deviceUDID;
                    this.f1602a.setPropStr(ConstProp.UDID, str8);
                    JSONObject jSONObject5 = new JSONObject();
                    try {
                        jSONObject5.putOpt(OneTrackParams.XMSdkParams.STEP, "reset_udid");
                        jSONObject5.putOpt("udid", str8);
                    } catch (JSONException e3) {
                        UniSdkUtils.d("UniSDK JfGas", "extraJson:" + e3.getMessage());
                    }
                    this.f1602a.saveClientLog(orderInfo, jSONObject5.toString());
                    udid = str8;
                }
                jSONObject4.put("udid", udid);
                jSONObject4.put("deviceid", this.f1602a.getPropStr(ConstProp.DEVICE_ID));
                jSONObject4.put(SocialConstants.PARAM_SOURCE, i);
                jSONObject4.put("ext_info", jfExtInfo2);
                String appChannel = this.f1602a.getAppChannel();
                String platform = this.f1602a.getPlatform();
                if (orderInfo.isWebPayment()) {
                    appChannel = this.f1602a.getPropStr(ConstProp.SOURCE_APP_CHANNEL);
                    platform = this.f1602a.getPropStr(ConstProp.SOURCE_PLATFORM);
                }
                if (TextUtils.isEmpty(appChannel)) {
                    appChannel = "";
                }
                jSONObject4.put("source_app_channel", appChannel);
                if (TextUtils.isEmpty(platform)) {
                    platform = "";
                }
                jSONObject4.put("source_platform", platform);
                jSONObject4.putOpt("sdklog", f.a(this.f1602a.myCtx, orderInfo).toString());
                String propStr7 = this.f1602a.getPropStr(ConstProp.UNISDK_SERVER_EXTPARAM);
                if (!TextUtils.isEmpty(propStr7)) {
                    jSONObject = new JSONObject(propStr7);
                } else {
                    jSONObject = new JSONObject();
                }
                String unisdkJfExtCid = orderInfo.getUnisdkJfExtCid();
                if (TextUtils.isEmpty(unisdkJfExtCid)) {
                    unisdkJfExtCid = this.f1602a.getPropStr(ConstProp.UNISDK_JF_EXT_CID);
                }
                if (!TextUtils.isEmpty(unisdkJfExtCid)) {
                    jSONObject.put(OneTrackParams.CommonParams.CID, unisdkJfExtCid);
                }
                if (orderInfo.isCartOrder()) {
                    JSONArray jSONArrayProduceCartInfo = orderInfo.produceCartInfo();
                    if (jSONArrayProduceCartInfo != null && jSONArrayProduceCartInfo.length() > 0) {
                        jSONObject4.putOpt("goodscart", jSONArrayProduceCartInfo);
                        jSONObject4.putOpt("goodsid", ConstProp.GAS_GOODS_CART);
                        jSONObject4.put("goodscount", 1);
                    }
                } else {
                    try {
                        JSONObject jSONObject6 = new JSONObject(orderInfo.getExtendJson());
                        Iterator<String> itKeys = jSONObject6.keys();
                        while (itKeys.hasNext()) {
                            String next = itKeys.next();
                            if (!jSONObject4.has(next)) {
                                jSONObject4.putOpt(next, jSONObject6.get(next));
                            }
                        }
                    } catch (Exception unused) {
                    }
                }
                if ("wo_app".equals(channel)) {
                    UniSdkUtils.i("UniSDK JfGas", "extra pair for wo_app");
                    jSONObject.put("feename", orderInfo.getProductName());
                    String str9 = orderInfo.getSdkPids().get(channel);
                    if (!TextUtils.isEmpty(str9) && str9.contains(",")) {
                        jSONObject.put("serviceid", str9.split(",")[0]);
                    }
                    jSONObject.put("channelid", channel);
                    jSONObject.put("appversion", UniSdkUtils.getAppVersionName(this.f1602a.myCtx));
                    jSONObject.put("imei", UniSdkUtils.getMobileIMEI(this.f1602a.myCtx));
                } else if ("youxiqun_sdk".equals(channel)) {
                    jSONObject.put("sessionid", SdkMgr.getInst().getPropStr(ConstProp.SESSION));
                } else if ("myapp".equals(channel)) {
                    jSONObject.put("sessionid", SdkMgr.getInst().getPropStr(ConstProp.SESSION));
                    jSONObject.put("logintype", SdkMgr.getInst().getPropStr(ConstProp.LOGIN_TYPE));
                    jSONObject.put("paytoken", SdkMgr.getInst().getPropStr(ConstProp.PAY_TOKEN));
                    jSONObject.put(Constants.PARAM_PLATFORM_ID, SdkMgr.getInst().getPropStr(ConstProp.PF));
                    jSONObject.put("pfkey", SdkMgr.getInst().getPropStr(ConstProp.PFKEY));
                } else if (ConstProp.NT_AUTH_NAME_YIXIN.equals(channel)) {
                    jSONObject.put("sessionid", SdkMgr.getInst().getPropStr(ConstProp.SESSION));
                    jSONObject.put("channelversion", SdkMgr.getInst().getPropStr(ConstProp.APP_VERSION));
                    UniSdkUtils.i("UniSDK JfGas", "yixin channelversion=" + SdkMgr.getInst().getPropStr(ConstProp.APP_VERSION));
                } else if ("zqgame".equals(channel)) {
                    jSONObject.put("sessionid", SdkMgr.getInst().getPropStr(ConstProp.CPID));
                    jSONObject.put("rolename", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_NAME));
                } else if ("lewu".equals(channel)) {
                    jSONObject.put("role_name", SdkMgr.getInst().getPropStr(ConstProp.USERINFO_NAME));
                    jSONObject.put(Const.ParamKey.SERVER_NAME, SdkMgr.getInst().getPropStr(ConstProp.USERINFO_HOSTNAME));
                }
                String propStr8 = SdkMgr.getInst().getPropStr(ConstProp.AAS_HOSTID_REGION);
                String propStr9 = SdkMgr.getInst().getPropStr(ConstProp.AAS_LANGUAGE);
                if (!TextUtils.isEmpty(propStr8)) {
                    jSONObject4.put("aas_hostid_region", propStr8);
                }
                if (!TextUtils.isEmpty(propStr9)) {
                    jSONObject4.put("aas_language", propStr9);
                }
                String appPackageName = UniSdkUtils.getAppPackageName(this.f1602a.myCtx);
                String strA = c.a();
                Map<String, String> jfPaylMap = this.f1602a.getJfPaylMap(orderInfo);
                Iterator<String> it = this.f1602a.sdkInstMap.keySet().iterator();
                while (it.hasNext()) {
                    Map<String, String> jfPaylMap2 = this.f1602a.sdkInstMap.get(it.next()).getJfPaylMap(orderInfo);
                    if (jfPaylMap == null) {
                        jfPaylMap = jfPaylMap2;
                    } else if (jfPaylMap2 != null) {
                        jfPaylMap.putAll(jfPaylMap2);
                    }
                }
                String jfExtraJson = orderInfo.getJfExtraJson();
                if (!TextUtils.isEmpty(jfExtraJson) && (mapJsonToStrMap = StrUtil.jsonToStrMap(jfExtraJson)) != null) {
                    jfPaylMap.putAll(mapJsonToStrMap);
                }
                if (jfPaylMap != null && !jfPaylMap.isEmpty()) {
                    for (String str10 : jfPaylMap.keySet()) {
                        jSONObject.put(str10, jfPaylMap.get(str10));
                        String str11 = jfPaylMap.get(str10);
                        if ("client_cny".equals(str10)) {
                            jSONObject4.put("client_cny", str11);
                        } else if ("client_money".equals(str10)) {
                            jSONObject4.put("client_money", str11);
                        } else if ("region".equals(str10)) {
                            strA = str11;
                        } else if ("order_type".equals(str10)) {
                            str7 = str11;
                        }
                    }
                }
                String str12 = str7;
                jSONObject4.put("packagename", appPackageName);
                jSONObject4.put("region", strA);
                jSONObject4.put("order_type", str12);
                jSONObject4.put("operation_code", SdkMgr.getInst().getPropStr(ConstProp.YY_GAMEID, ""));
                jSONObject4.put("product_region", new JSONObject(SdkMgr.getInst().getPropStr(ConstProp.JF_AIM_INFO)).optString(com.netease.ntunisdk.external.protocol.Const.COUNTRY));
                orderInfo.setOrderType(Integer.valueOf(str12).intValue());
                jSONObject4.put(OneTrackParams.CommonParams.EXTRA, jSONObject);
                UniSdkUtils.d("UniSDK JfGas", String.format("/createorder url=%s, body=%s", str4, jSONObject4.toString()));
                HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
                queueItemNewQueueItem.method = "POST";
                queueItemNewQueueItem.url = str4;
                queueItemNewQueueItem.bSync = Boolean.TRUE;
                queueItemNewQueueItem.leftRetry = 0;
                queueItemNewQueueItem.setBody(jSONObject4.toString());
                queueItemNewQueueItem.transParam = str3;
                final OrderInfo orderInfo4 = orderInfo2;
                queueItemNewQueueItem.callback = new HTTPCallback() { // from class: com.netease.ntunisdk.base.JfGas.2
                    @Override // com.netease.ntunisdk.base.utils.HTTPCallback
                    public final boolean processResult(String str13, String str14) throws Throwable {
                        String str15;
                        JSONObject jSONObject7;
                        int iOptInt2;
                        int iOptInt3;
                        String strOptString3;
                        String strOptString4;
                        String strOptString5;
                        UniSdkUtils.d("UniSDK JfGas", "/createorder result=".concat(String.valueOf(str13)));
                        try {
                            JSONObject jSONObject8 = new JSONObject(str13);
                            if (jSONObject8.has("code") && jSONObject8.has("subcode")) {
                                JfGas.a(JfGas.this, "createOrder_res", str13, 200, orderInfo);
                            } else {
                                JfGas.a(JfGas.this, "createOrder_res", str13, 400, orderInfo);
                            }
                        } catch (Exception unused2) {
                            JfGas.a(JfGas.this, "createOrder_res", str13, 400, orderInfo);
                        }
                        if (TextUtils.isEmpty(str13)) {
                            UniSdkUtils.e("UniSDK JfGas", "/createorder no response");
                            orderInfo.setOrderStatus(3);
                            orderInfo.setOrderErrReason("create order fail");
                            JfGas.this.f1602a.checkOrderDone(orderInfo);
                            return false;
                        }
                        try {
                            jSONObject7 = new JSONObject(str13);
                            iOptInt2 = jSONObject7.optInt("code");
                            iOptInt3 = jSONObject7.optInt("subcode");
                            String strOptString6 = jSONObject7.optString("status");
                            strOptString3 = jSONObject7.optString("msg");
                            strOptString4 = jSONObject7.optString("popup", "None");
                            int iOptInt4 = jSONObject7.optInt("aas_ff_code", -1);
                            strOptString5 = jSONObject7.optString("aas_ff_rule");
                            JSONArray jSONArrayOptJSONArray = jSONObject7.optJSONArray("aas_version");
                            if (jSONArrayOptJSONArray != null) {
                                StringBuilder sb3 = new StringBuilder();
                                str15 = "create order fail";
                                for (int i4 = 0; i4 < jSONArrayOptJSONArray.length(); i4++) {
                                    try {
                                        if (i4 == jSONArrayOptJSONArray.length() - 1) {
                                            sb3.append(jSONArrayOptJSONArray.optString(i4));
                                        } else {
                                            sb3.append(jSONArrayOptJSONArray.optString(i4));
                                            sb3.append(",");
                                        }
                                    } catch (JSONException e4) {
                                        e = e4;
                                        UniSdkUtils.e("UniSDK JfGas", "/createorder fail:" + e.getMessage());
                                        orderInfo.setOrderStatus(3);
                                        UniSdkUtils.e("UniSDK JfGas", "/createorder fail");
                                        orderInfo.setOrderErrReason(str15);
                                        JfGas.this.f1602a.checkOrderDone(orderInfo);
                                        return false;
                                    }
                                }
                                JfGas.this.f1602a.setPropStr(ConstProp.AAS_VERSION, sb3.toString());
                            } else {
                                str15 = "create order fail";
                            }
                            orderInfo.setJfCode(iOptInt2);
                            orderInfo.setJfSubCode(iOptInt3);
                            orderInfo.setJfMessage(strOptString6);
                            orderInfo.setJfAasFfCode(iOptInt4);
                            orderInfo.setJfAasFfRule(strOptString5);
                        } catch (JSONException e5) {
                            e = e5;
                            str15 = "create order fail";
                        }
                        if (200 == iOptInt2) {
                            if (createOrderCallback != null) {
                                UniSdkUtils.d("UniSDK JfGas", "create order callbackResult");
                                createOrderCallback.callbackResult(str13);
                                return false;
                            }
                            JfGas.this.processWhenJfSuc(jSONObject7, orderInfo);
                            return false;
                        }
                        if (460 != iOptInt2 || 13 != iOptInt3 || !SdkMgr.getInst().hasFeature(ConstProp.MODE_HAS_LVU_DIALOG)) {
                            if (430 == iOptInt2 && iOptInt3 == 0) {
                                orderInfo.setOrderStatus(OrderInfo.S_SDK_CHANNEL_IN_BLACKLIST);
                                if ("None".equalsIgnoreCase(strOptString4)) {
                                    strOptString4 = "1";
                                }
                            } else {
                                orderInfo.setOrderStatus(3);
                            }
                            JfGas.createOrderTips(JfGas.this.f1602a, strOptString4, strOptString3, strOptString5);
                            UniSdkUtils.e("UniSDK JfGas", "/createorder fail");
                            orderInfo.setOrderErrReason(str15);
                            JfGas.this.f1602a.checkOrderDone(orderInfo);
                            return false;
                        }
                        JSONObject jSONObject9 = new JSONObject();
                        jSONObject9.put("methodId", "openLVU");
                        jSONObject9.put("channel", SdkMgr.getInst().getChannel());
                        jSONObject9.put("orderInfo", OrderInfo.obj2Json(orderInfo4));
                        SdkMgr.getInst().ntExtendFunc(jSONObject9.toString());
                        return false;
                    }
                };
                String propStr10 = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
                if (!TextUtils.isEmpty(propStr10)) {
                    HashMap map = new HashMap();
                    try {
                        ApiRequestUtil.addSecureHeader(map, propStr10, queueItemNewQueueItem.method, str4, jSONObject4.toString(), true);
                    } catch (Exception e4) {
                        UniSdkUtils.d("UniSDK JfGas", "hmacSHA256Signature exception:" + e4.getMessage());
                    }
                    queueItemNewQueueItem.setHeaders(map);
                } else {
                    UniSdkUtils.d("UniSDK JfGas", "JF_CLIENT_KEY empty");
                }
                HTTPQueue.getInstance(HTTPQueue.HTTPQUEUE_PAY).addItem(queueItemNewQueueItem);
                try {
                    jSONObject3.putOpt(OneTrackParams.XMSdkParams.STEP, "createOrder_exe");
                } catch (JSONException e5) {
                    UniSdkUtils.d("UniSDK JfGas", "extraJson:" + e5.getMessage());
                }
                this.f1602a.saveClientLog(orderInfo, jSONObject3.toString());
            } catch (JSONException e6) {
                e6.printStackTrace();
                UniSdkUtils.e("UniSDK JfGas", "JSONException:" + e6.getMessage());
                orderInfo.setOrderStatus(3);
                orderInfo.setOrderErrReason("create order fail");
                this.f1602a.checkOrderDone(orderInfo);
            }
        } catch (Exception unused2) {
            UniSdkUtils.e("UniSDK JfGas", "set ConstProp.USERINFO_HOSTID value must int");
            orderInfo.setOrderStatus(3);
            orderInfo.setOrderErrReason("set ConstProp.USERINFO_HOSTID value must int");
            this.f1602a.checkOrderDone(orderInfo);
        }
    }

    public void processWhenJfSuc(JSONObject jSONObject, OrderInfo orderInfo) throws JSONException {
        orderInfo.setOrderId(jSONObject.optString("sn"));
        Object objOpt = jSONObject.opt("data");
        if (objOpt != null && (objOpt instanceof JSONObject)) {
            JSONObject jSONObject2 = (JSONObject) objOpt;
            String strOptString = jSONObject2.optString("etc");
            String strOptString2 = jSONObject2.optString("sdkOrderId");
            String strOptString3 = jSONObject2.optString(SocialOperation.GAME_SIGNATURE);
            String strOptString4 = jSONObject2.optString("consumesn");
            if (this.f1602a.hasFeature("VIRTUAL_ORDER") && !TextUtils.isEmpty(strOptString4)) {
                orderInfo.setSdkOrderId(strOptString4);
            }
            if (!TextUtils.isEmpty(strOptString)) {
                orderInfo.setOrderEtc(strOptString);
            }
            if (!TextUtils.isEmpty(strOptString2)) {
                orderInfo.setSdkOrderId(strOptString2);
            }
            if (!TextUtils.isEmpty(strOptString3)) {
                orderInfo.setSignature(strOptString3);
            }
        }
        UniSdkUtils.i("UniSDK JfGas", "/createorder success");
        this.f1602a.a(orderInfo);
    }

    final void b(final OrderInfo orderInfo) {
        if (orderInfo == null) {
            return;
        }
        if (!OrderInfo.hasProduct(orderInfo)) {
            queryProduct(new QueryProductCallback() { // from class: com.netease.ntunisdk.base.JfGas.3
                /* JADX WARN: Removed duplicated region for block: B:35:0x0089 A[SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:38:? A[LOOP:0: B:10:0x002f->B:38:?, LOOP_END, SYNTHETIC] */
                @Override // com.netease.ntunisdk.base.JfGas.QueryProductCallback
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public final void callbackResult() throws java.lang.Throwable {
                    /*
                        r8 = this;
                        java.util.Hashtable r0 = com.netease.ntunisdk.base.OrderInfo.getProductList()
                        com.netease.ntunisdk.base.OrderInfo r1 = r2
                        boolean r1 = r1.isCartOrder()
                        java.lang.String r2 = "UniSDK JfGas"
                        if (r1 != 0) goto L9b
                        com.netease.ntunisdk.base.OrderInfo r1 = r2
                        java.lang.String r1 = r1.getProductId()
                        boolean r1 = com.netease.ntunisdk.base.OrderInfo.hasProduct(r1)
                        if (r1 != 0) goto L9b
                        if (r0 == 0) goto L9b
                        boolean r1 = r0.isEmpty()
                        if (r1 != 0) goto L9b
                        java.lang.String r1 = "check reg product id"
                        com.netease.ntunisdk.base.UniSdkUtils.d(r2, r1)
                        java.util.Set r1 = r0.keySet()
                        java.util.Iterator r1 = r1.iterator()
                    L2f:
                        boolean r3 = r1.hasNext()
                        if (r3 == 0) goto L9b
                        java.lang.Object r3 = r1.next()
                        java.lang.String r3 = (java.lang.String) r3
                        java.lang.Object r4 = r0.get(r3)
                        com.netease.ntunisdk.base.OrderInfo$ProductInfo r4 = (com.netease.ntunisdk.base.OrderInfo.ProductInfo) r4
                        java.util.Map<java.lang.String, java.lang.String> r4 = r4.sdkPids
                        if (r4 == 0) goto L86
                        boolean r5 = r4.isEmpty()
                        if (r5 != 0) goto L86
                        java.lang.StringBuilder r5 = new java.lang.StringBuilder
                        java.lang.String r6 = "sdkPids length:"
                        r5.<init>(r6)
                        int r6 = r4.size()
                        r5.append(r6)
                        java.lang.String r5 = r5.toString()
                        com.netease.ntunisdk.base.UniSdkUtils.d(r2, r5)
                        java.util.Set r5 = r4.keySet()
                        java.util.Iterator r5 = r5.iterator()
                    L68:
                        boolean r6 = r5.hasNext()
                        if (r6 == 0) goto L86
                        java.lang.Object r6 = r5.next()
                        java.lang.String r6 = (java.lang.String) r6
                        com.netease.ntunisdk.base.OrderInfo r7 = r2
                        java.lang.String r7 = r7.getProductId()
                        java.lang.Object r6 = r4.get(r6)
                        boolean r6 = r7.equals(r6)
                        if (r6 == 0) goto L68
                        r4 = 1
                        goto L87
                    L86:
                        r4 = 0
                    L87:
                        if (r4 == 0) goto L2f
                        java.lang.String r0 = "check reg product id:"
                        java.lang.String r1 = java.lang.String.valueOf(r3)
                        java.lang.String r0 = r0.concat(r1)
                        com.netease.ntunisdk.base.UniSdkUtils.d(r2, r0)
                        com.netease.ntunisdk.base.OrderInfo r0 = r2
                        r0.setProductId(r3)
                    L9b:
                        org.json.JSONObject r0 = new org.json.JSONObject
                        r0.<init>()
                        java.lang.String r1 = "step"
                        java.lang.String r3 = "queryOrder_queryProduct"
                        r0.putOpt(r1, r3)     // Catch: org.json.JSONException -> Lb7
                        java.lang.String r1 = "channel"
                        com.netease.ntunisdk.base.JfGas r3 = com.netease.ntunisdk.base.JfGas.this     // Catch: org.json.JSONException -> Lb7
                        com.netease.ntunisdk.base.SdkBase r3 = com.netease.ntunisdk.base.JfGas.a(r3)     // Catch: org.json.JSONException -> Lb7
                        java.lang.String r3 = r3.getChannel()     // Catch: org.json.JSONException -> Lb7
                        r0.putOpt(r1, r3)     // Catch: org.json.JSONException -> Lb7
                        goto Lcd
                    Lb7:
                        r1 = move-exception
                        java.lang.StringBuilder r3 = new java.lang.StringBuilder
                        java.lang.String r4 = "extraJson:"
                        r3.<init>(r4)
                        java.lang.String r1 = r1.getMessage()
                        r3.append(r1)
                        java.lang.String r1 = r3.toString()
                        com.netease.ntunisdk.base.UniSdkUtils.d(r2, r1)
                    Lcd:
                        com.netease.ntunisdk.base.JfGas r1 = com.netease.ntunisdk.base.JfGas.this
                        com.netease.ntunisdk.base.SdkBase r1 = com.netease.ntunisdk.base.JfGas.a(r1)
                        com.netease.ntunisdk.base.OrderInfo r2 = r2
                        java.lang.String r0 = r0.toString()
                        r1.saveClientLog(r2, r0)
                        com.netease.ntunisdk.base.OrderInfo r0 = r2
                        java.lang.String r0 = r0.getOrderChannel()
                        com.netease.ntunisdk.base.JfGas r1 = com.netease.ntunisdk.base.JfGas.this
                        com.netease.ntunisdk.base.OrderInfo r2 = r2
                        com.netease.ntunisdk.base.JfGas.a(r1, r2, r0)
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.JfGas.AnonymousClass3.callbackResult():void");
                }
            });
        } else {
            a(orderInfo, orderInfo.getOrderChannel());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't wrap try/catch for region: R(57:41|(1:43)|44|(1:48)|49|(1:51)(1:52)|53|(2:55|(1:58))(0)|59|(1:61)|62|(1:76)(48:66|230|67|(2:72|(1:74)(1:75))|78|(1:80)(1:81)|82|(1:84)(1:85)|86|(1:88)(1:89)|90|(1:92)(1:93)|94|(1:96)(1:97)|232|98|99|225|100|(1:102)(1:103)|104|105|(6:107|(1:109)|110|228|111|115)(1:116)|117|(1:119)|120|(1:122)|123|(1:125)|126|(1:128)|129|130|(2:132|(2:134|135)(21:136|137|217|138|139|221|140|141|223|142|143|152|(1:154)(1:155)|156|157|226|158|159|211|160|161))(1:168)|169|(4:172|(2:237|(3:236|175|241)(1:240))(3:235|176|239)|238|170)|234|(3:180|(2:183|181)|242)|184|185|(4:187|215|188|192)(1:193)|194|195|196|219|197|204|205)|77|78|(0)(0)|82|(0)(0)|86|(0)(0)|90|(0)(0)|94|(0)(0)|232|98|99|225|100|(0)(0)|104|105|(0)(0)|117|(0)|120|(0)|123|(0)|126|(0)|129|130|(0)(0)|169|(1:170)|234|(4:178|180|(1:181)|242)|184|185|(0)(0)|194|195|196|219|197|204|205) */
    /* JADX WARN: Code restructure failed: missing block: B:199:0x0656, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:203:0x065b, code lost:
    
        com.netease.ntunisdk.base.UniSdkUtils.d("UniSDK JfGas", r25 + r0.getMessage());
     */
    /* JADX WARN: Removed duplicated region for block: B:102:0x02df  */
    /* JADX WARN: Removed duplicated region for block: B:103:0x02e2 A[Catch: JSONException -> 0x067a, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0305 A[Catch: JSONException -> 0x067a, TRY_ENTER, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:116:0x034c  */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0360 A[Catch: JSONException -> 0x067a, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x037c A[Catch: JSONException -> 0x067a, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x038f A[Catch: JSONException -> 0x067a, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x039d A[Catch: JSONException -> 0x067a, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:132:0x03b2 A[Catch: JSONException -> 0x067a, TRY_ENTER, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:154:0x047c A[Catch: JSONException -> 0x067a, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0484  */
    /* JADX WARN: Removed duplicated region for block: B:168:0x051e  */
    /* JADX WARN: Removed duplicated region for block: B:172:0x0540 A[Catch: JSONException -> 0x067a, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:178:0x0560 A[Catch: JSONException -> 0x067a, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:183:0x0574 A[Catch: JSONException -> 0x067a, LOOP:1: B:181:0x056e->B:183:0x0574, LOOP_END, TryCatch #7 {JSONException -> 0x067a, blocks: (B:100:0x027a, B:104:0x02e6, B:107:0x0305, B:110:0x0315, B:115:0x033f, B:117:0x034e, B:119:0x0360, B:120:0x0372, B:122:0x037c, B:123:0x0385, B:125:0x038f, B:126:0x0397, B:128:0x039d, B:129:0x03a2, B:132:0x03b2, B:134:0x03b8, B:136:0x03be, B:138:0x0403, B:140:0x0408, B:142:0x040e, B:152:0x043a, B:154:0x047c, B:156:0x0486, B:158:0x0493, B:159:0x0497, B:160:0x04bb, B:161:0x04c8, B:169:0x0528, B:170:0x053a, B:172:0x0540, B:175:0x0558, B:178:0x0560, B:180:0x0566, B:181:0x056e, B:183:0x0574, B:184:0x0586, B:163:0x04f0, B:166:0x0507, B:151:0x0421, B:114:0x032c, B:103:0x02e2, B:111:0x0322), top: B:225:0x027a, inners: #0, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:187:0x0606  */
    /* JADX WARN: Removed duplicated region for block: B:193:0x0637  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00c4  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00ef  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00f5  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x0216  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0228  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x0230  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0244  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0248  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0256  */
    /* JADX WARN: Removed duplicated region for block: B:93:0x025a  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0269  */
    /* JADX WARN: Removed duplicated region for block: B:97:0x026d  */
    /* JADX WARN: Unreachable blocks removed: 2, instructions: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(com.netease.ntunisdk.base.OrderInfo r29, java.lang.String r30) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 1690
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.JfGas.a(com.netease.ntunisdk.base.OrderInfo, java.lang.String):void");
    }

    public void queryProduct(final QueryProductCallback queryProductCallback) {
        UniSdkUtils.i("UniSDK JfGas", "JfGas queryProduct");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "queryProduct_in");
        } catch (JSONException e) {
            UniSdkUtils.d("UniSDK JfGas", "extraJson:" + e.getMessage());
        }
        this.f1602a.saveClientLog(null, jSONObject.toString());
        String propStr = this.f1602a.getPropStr(ConstProp.UNISDK_JF_GAS3_URL);
        if (TextUtils.isEmpty(propStr)) {
            UniSdkUtils.e("UniSDK JfGas", "ConstProp.UNISDK_JF_GAS3_URL is empty");
            if (queryProductCallback != null) {
                queryProductCallback.callbackResult();
                return;
            }
            return;
        }
        StringBuilder sb = new StringBuilder(propStr);
        if (propStr.endsWith("/")) {
            sb.append("query_product?platform=" + this.f1602a.getPlatform());
        } else {
            sb.append("/query_product?platform=" + this.f1602a.getPlatform());
        }
        String string = sb.toString();
        String channel = this.f1602a.getChannel();
        String payChannel = this.f1602a.getPayChannel();
        if (!TextUtils.isEmpty(payChannel)) {
            channel = channel + "," + payChannel.replace(Marker.ANY_NON_NULL_MARKER, ",");
        }
        try {
            string = string + "&pay_channel=" + URLEncoder.encode(channel, "UTF-8");
        } catch (UnsupportedEncodingException e2) {
            UniSdkUtils.d("UniSDK JfGas", "UnsupportedEncodingException" + e2.getMessage());
        }
        String strA = a(string);
        UniSdkUtils.d("UniSDK JfGas", String.format("/queryProduct url=%s", strA));
        HTTPQueue.QueueItem queueItemNewQueueItem = HTTPQueue.NewQueueItem();
        queueItemNewQueueItem.method = "POST";
        queueItemNewQueueItem.url = strA;
        queueItemNewQueueItem.bSync = Boolean.TRUE;
        queueItemNewQueueItem.leftRetry = 0;
        queueItemNewQueueItem.keyRSA = SdkMgr.getInst().getPropStr(ConstProp.UNISDK_SERVER_KEY);
        queueItemNewQueueItem.transParam = "UNISD_JF_GAS3_QUERY_PRODUCT";
        queueItemNewQueueItem.callback = new HTTPCallback() { // from class: com.netease.ntunisdk.base.JfGas.5
            @Override // com.netease.ntunisdk.base.utils.HTTPCallback
            public final boolean processResult(String str, String str2) throws JSONException {
                boolean z;
                UniSdkUtils.d("UniSDK JfGas", "/queryProduct result=".concat(String.valueOf(str)));
                try {
                    JSONObject jSONObject2 = new JSONObject(str);
                    if (jSONObject2.has("code") && jSONObject2.has("subcode")) {
                        JfGas.a(JfGas.this, "queryProduct_res", null, 200, null);
                    } else {
                        JfGas.a(JfGas.this, "queryProduct_res", null, 400, null);
                    }
                } catch (Exception unused) {
                    JfGas.a(JfGas.this, "queryProduct_res", null, 400, null);
                }
                if (TextUtils.isEmpty(str)) {
                    UniSdkUtils.e("UniSDK JfGas", "/queryProduct no response");
                    QueryProductCallback queryProductCallback2 = queryProductCallback;
                    if (queryProductCallback2 != null) {
                        queryProductCallback2.callbackResult();
                    }
                    return false;
                }
                try {
                    JSONObject jSONObject3 = new JSONObject(str);
                    if (200 == jSONObject3.optInt("code")) {
                        JSONArray jSONArrayOptJSONArray = jSONObject3.optJSONArray("product_list_v2");
                        if (jSONArrayOptJSONArray == null) {
                            jSONArrayOptJSONArray = jSONObject3.optJSONArray("product_list");
                            z = false;
                        } else {
                            z = true;
                        }
                        if (jSONArrayOptJSONArray.length() > 0) {
                            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                                JSONObject jSONObject4 = jSONArrayOptJSONArray.getJSONObject(i);
                                if (z) {
                                    OrderInfo.isJFV2Product = true;
                                    OrderInfo.c(jSONObject4.toString());
                                } else {
                                    OrderInfo.isJFV2Product = false;
                                    OrderInfo.a(jSONObject4.toString());
                                }
                            }
                        }
                        UniSdkUtils.i("UniSDK JfGas", "/queryProduct success");
                        QueryProductCallback queryProductCallback3 = queryProductCallback;
                        if (queryProductCallback3 != null) {
                            queryProductCallback3.callbackResult();
                        }
                        return false;
                    }
                } catch (JSONException e3) {
                    UniSdkUtils.e("UniSDK JfGas", "/queryProduct fail:" + e3.getMessage());
                }
                UniSdkUtils.e("UniSDK JfGas", "/queryProduct fail");
                QueryProductCallback queryProductCallback4 = queryProductCallback;
                if (queryProductCallback4 != null) {
                    queryProductCallback4.callbackResult();
                }
                return false;
            }
        };
        String propStr2 = SdkMgr.getInst().getPropStr(ConstProp.JF_LOG_KEY);
        if (!TextUtils.isEmpty(propStr2)) {
            HashMap map = new HashMap();
            try {
                ApiRequestUtil.addSecureHeader(map, propStr2, queueItemNewQueueItem.method, strA, "", true);
            } catch (Exception e3) {
                UniSdkUtils.d("UniSDK JfGas", "hmacSHA256Signature exception:" + e3.getMessage());
            }
            queueItemNewQueueItem.setHeaders(map);
        } else {
            UniSdkUtils.d("UniSDK JfGas", "JF_CLIENT_KEY empty");
        }
        HTTPQueue.getInstance(HTTPQueue.HTTPQUEUE_PAY).addItem(queueItemNewQueueItem);
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, "queryProduct_exe");
        } catch (JSONException e4) {
            UniSdkUtils.d("UniSDK JfGas", "extraJson:" + e4.getMessage());
        }
        this.f1602a.saveClientLog(null, jSONObject.toString());
    }

    private static String a(String str) {
        try {
            String str2 = str + "&operation_code=" + URLEncoder.encode(SdkMgr.getInst().getPropStr(ConstProp.YY_GAMEID, ""), "UTF-8") + "&product_region=" + URLEncoder.encode(new JSONObject(SdkMgr.getInst().getPropStr(ConstProp.JF_AIM_INFO)).optString(com.netease.ntunisdk.external.protocol.Const.COUNTRY), "UTF-8");
            String propStr = SdkMgr.getInst().getPropStr(ConstProp.PRODUCT_LANGUAGE);
            if (TextUtils.isEmpty(propStr)) {
                return str2;
            }
            return str2 + "&product_language=" + URLEncoder.encode(propStr.toLowerCase(Locale.ROOT), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static void createOrderTips(final SdkBase sdkBase, String str, final String str2, final String str3) {
        if (sdkBase == null) {
            return;
        }
        int propInt = sdkBase.getPropInt(ConstProp.ENABLE_UNISDK_CREATEORDER_UI, 1);
        if ("1".equals(str) && 1 == propInt && !TextUtils.isEmpty(str2)) {
            if (sdkBase.myCtx != null) {
                ((Activity) sdkBase.myCtx).runOnUiThread(new Runnable() { // from class: com.netease.ntunisdk.base.JfGas.6
                    @Override // java.lang.Runnable
                    public final void run() throws Resources.NotFoundException {
                        if (sdkBase.showAASDialog(str2, str3)) {
                            return;
                        }
                        String string = sdkBase.myCtx.getResources().getString(ResUtils.getResId(sdkBase.myCtx, "unisdk_alert_dialog_tips", ResIdReader.RES_TYPE_STRING));
                        final String string2 = sdkBase.myCtx.getResources().getString(ResUtils.getResId(sdkBase.myCtx, "unisdk_alert_dialog_positive", ResIdReader.RES_TYPE_STRING));
                        new Alerter(sdkBase.myCtx).showDialog(string, str2, string2, null, new NtSdkStringClickableSpan(new NtSdkTagParser$OnSpanClickListener() { // from class: com.netease.ntunisdk.base.JfGas.6.1
                            @Override // com.netease.ntunisdk.base.view.NtSdkTagParser$OnSpanClickListener
                            protected final void a() throws Resources.NotFoundException {
                                UniSdkUtils.d("UniSDK JfGas", "OnSpanClickListener: onFFRulesClicked");
                                if (!TextUtils.isEmpty(str3)) {
                                    new Alerter(sdkBase.myCtx).showDialog(sdkBase.myCtx.getResources().getString(ResUtils.getResId(sdkBase.myCtx, "unisdk_alert_dialog_info", ResIdReader.RES_TYPE_STRING)), str3, string2, null, null);
                                } else {
                                    UniSdkUtils.d("UniSDK JfGas", "aasFfRule empty");
                                }
                            }
                        }));
                    }
                });
                return;
            }
            return;
        }
        UniSdkUtils.d("UniSDK JfGas", "not createOrderTips");
    }

    static /* synthetic */ void a(JfGas jfGas, String str, String str2, int i, OrderInfo orderInfo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt(OneTrackParams.XMSdkParams.STEP, str);
            jSONObject.putOpt(UriUtil.LOCAL_RESOURCE_SCHEME, str2);
            jSONObject.putOpt("res_code", Integer.valueOf(i));
            jfGas.f1602a.saveClientLog(orderInfo, jSONObject.toString());
        } catch (Exception e) {
            UniSdkUtils.d("UniSDK JfGas", "extraJson:" + e.getMessage());
        }
    }
}