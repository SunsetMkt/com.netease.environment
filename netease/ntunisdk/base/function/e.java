package com.netease.ntunisdk.base.function;

import android.text.TextUtils;
import com.netease.ntunisdk.base.JfGas;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkBase;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.util.HashMap;
import org.json.JSONObject;

/* compiled from: IapOrder.java */
/* loaded from: classes4.dex */
public final class e {
    public static void a(final SdkBase sdkBase, final OrderInfo orderInfo) {
        final JfGas jfGas = new JfGas(sdkBase);
        jfGas.createOrder(orderInfo, new JfGas.CreateOrderCallback() { // from class: com.netease.ntunisdk.base.function.e.1
            @Override // com.netease.ntunisdk.base.JfGas.CreateOrderCallback
            public final void callbackResult(String str) {
                String str2;
                UniSdkUtils.i("IapOrder", "callbackResult: ".concat(String.valueOf(str)));
                try {
                    JSONObject jSONObject = new JSONObject(str);
                    String strOptString = jSONObject.optString("sn");
                    String strOptString2 = jSONObject.optString("cp_orderid");
                    String strOptString3 = jSONObject.optString("goodsid");
                    String strOptString4 = jSONObject.optString("channel_goodsid");
                    String strOptString5 = jSONObject.optString("goodsinfo");
                    String strOptString6 = jSONObject.optString("amount");
                    double d = Float.parseFloat(strOptString6);
                    String strOptString7 = jSONObject.optString("channel_goodsinfo");
                    String strOptString8 = jSONObject.optString("consumesn");
                    if (TextUtils.isEmpty(strOptString2) || !strOptString2.equals(orderInfo.getCpOrderId())) {
                        str2 = "cp_orderid invalid";
                    } else if (TextUtils.isEmpty(strOptString3) || !strOptString3.equals(orderInfo.getProductId())) {
                        str2 = "goodsid invalid";
                    } else if (TextUtils.isEmpty(strOptString5) && TextUtils.isEmpty(strOptString7)) {
                        str2 = "goodsinfo or channelGoodsinfo invalid";
                    } else {
                        if (d != orderInfo.getProductPrice() * orderInfo.getCount()) {
                            UniSdkUtils.w("IapOrder", "amount or price maybe invalid");
                        }
                        str2 = null;
                    }
                    if (!TextUtils.isEmpty(strOptString6)) {
                        try {
                            String extendJson = orderInfo.getExtendJson();
                            String str3 = "{}";
                            if (TextUtils.isEmpty(extendJson)) {
                                extendJson = "{}";
                            }
                            String strTrim = extendJson.trim();
                            if (strTrim.startsWith("{") && strTrim.endsWith(com.alipay.sdk.m.u.i.d)) {
                                str3 = strTrim;
                            }
                            JSONObject jSONObject2 = new JSONObject(str3);
                            jSONObject2.putOpt("amount", strOptString6);
                            orderInfo.setExtendJson(jSONObject2.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (!TextUtils.isEmpty(strOptString)) {
                        orderInfo.setOrderId(strOptString);
                    }
                    if (!TextUtils.isEmpty(strOptString8)) {
                        orderInfo.setSdkOrderId(strOptString8);
                    }
                    if (!TextUtils.isEmpty(strOptString4)) {
                        if (TextUtils.isEmpty(strOptString7)) {
                            strOptString7 = orderInfo.getProductName();
                        }
                        HashMap map = new HashMap();
                        map.put(orderInfo.getOrderChannel(), strOptString4);
                        OrderInfo.regProduct(strOptString3, strOptString7, orderInfo.getProductPrice(), orderInfo.getProductExchangeRatio(), map);
                    }
                    if (!TextUtils.isEmpty(str2)) {
                        orderInfo.setOrderStatus(3);
                        orderInfo.setOrderErrReason(str2);
                        sdkBase.checkOrderDone(orderInfo);
                        return;
                    }
                    jfGas.processWhenJfSuc(jSONObject, orderInfo);
                } catch (Throwable th) {
                    UniSdkUtils.w("IapOrder", th.getMessage());
                }
            }
        });
    }
}