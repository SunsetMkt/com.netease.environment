package com.netease.ntunisdk.base;

import android.text.TextUtils;
import com.netease.androidcrashhandler.unknownCrash.CheckNormalExitModel;
import com.tencent.open.SocialOperation;
import com.xiaomi.gamecenter.sdk.statistics.OneTrackParams;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class OrderInfo implements Serializable {
    public static final int NT_OS_SDK_INVALID_CURRENCY = 9;
    public static final int ORDERID_MAX_LEN = 64;
    public static final int S_GS_CHECKING = 4;
    public static final int S_GS_CHECK_ERR = 6;
    public static final int S_GS_CHECK_OK = 5;
    public static final int S_PREPARING = 0;
    public static final int S_SDK_CHANNEL_IN_BLACKLIST = 430;
    public static final int S_SDK_CHECKING = 1;
    public static final int S_SDK_CHECK_CANCEL = 11;
    public static final int S_SDK_CHECK_ERR = 3;
    public static final int S_SDK_CHECK_OK = 2;
    public static final int S_SDK_CHECK_RESTORE_OK = 10;
    public static final int S_SDK_NO_CHANNEL = 168;
    public static final int S_WRONG_ORDER_ID = 8;
    public static final int S_WRONG_PRODUCT_ID = 7;

    /* renamed from: a, reason: collision with root package name */
    private static Hashtable<String, ProductInfo> f1609a = new Hashtable<>();
    public static boolean isJFV2Product;
    private String A;
    private String B;
    private int C;
    private String D;
    private String E;
    private String F;
    private long G;
    private int H;
    private String I;
    private int J;
    private String K;
    private String b;
    private String c;
    private String d;
    private float e;
    private int f;
    private String g;
    private int h;
    private String i;
    private String j;
    private String k;
    private String l;
    private String m;
    private String n;
    private boolean o;
    private String p;
    private String q;
    private String r;
    private String s;
    private String t;
    private String u;
    private String v;
    private String w;
    private String x;
    private int y;
    private int z;

    public static class ProductInfo {
        public String defaultInfoJson;
        public int exchangeRatio;
        public String jellyExtra;
        public String payChannel;
        public String productId;
        public String productName;
        public float productPrice;
        public Map<String, String> sdkPids = new HashMap();
        public Map<String, Integer> channelGoodsTypes = new HashMap();
        public Map<String, Integer> jellRatios = new HashMap();
        public Map<String, String> currencyMap = new HashMap();
        public Map<String, Integer> goldMap = new HashMap();
        public Map<String, String> goodsinfoMap = new HashMap();
        public Map<String, Float> ratioMap = new HashMap();
        public Map<String, Float> priceMap = new HashMap();
        public Map<String, Integer> pay_yuanbaoMap = new HashMap();
        public Map<String, Integer> free_yuanbaoMap = new HashMap();
        public Map<String, String> extraMap = new HashMap();

        public ProductInfo copy() {
            ProductInfo productInfo = new ProductInfo();
            productInfo.productId = this.productId;
            productInfo.productName = this.productName;
            productInfo.productPrice = this.productPrice;
            productInfo.exchangeRatio = this.exchangeRatio;
            productInfo.payChannel = this.payChannel;
            productInfo.jellyExtra = this.jellyExtra;
            productInfo.defaultInfoJson = this.defaultInfoJson;
            productInfo.sdkPids.putAll(this.sdkPids);
            productInfo.channelGoodsTypes.putAll(this.channelGoodsTypes);
            productInfo.jellRatios.putAll(this.jellRatios);
            productInfo.currencyMap.putAll(this.currencyMap);
            productInfo.goldMap.putAll(this.goldMap);
            productInfo.goodsinfoMap.putAll(this.goodsinfoMap);
            productInfo.ratioMap.putAll(this.ratioMap);
            productInfo.priceMap.putAll(this.priceMap);
            productInfo.pay_yuanbaoMap.putAll(this.pay_yuanbaoMap);
            productInfo.free_yuanbaoMap.putAll(this.free_yuanbaoMap);
            productInfo.extraMap.putAll(this.extraMap);
            return productInfo;
        }

        public String toString() {
            return "ProductInfo{productId='" + this.productId + "', productName='" + this.productName + "', productPrice=" + this.productPrice + ", exchangeRatio=" + this.exchangeRatio + ", sdkPids=" + this.sdkPids + ", channelGoodsTypes=" + this.channelGoodsTypes + ", jellRatios=" + this.jellRatios + ", jellyExtra='" + this.jellyExtra + "', payChannel='" + this.payChannel + "', currencyMap=" + this.currencyMap + ", goldMap=" + this.goldMap + ", goodsinfoMap=" + this.goodsinfoMap + ", ratioMap=" + this.ratioMap + ", priceMap=" + this.priceMap + ", pay_yuanbaoMap=" + this.pay_yuanbaoMap + ", free_yuanbaoMap=" + this.free_yuanbaoMap + ", extraMap=" + this.extraMap + ", defaultInfoJson='" + this.defaultInfoJson + "'}";
        }
    }

    public static Hashtable<String, ProductInfo> getProductList() {
        return f1609a;
    }

    public static void regProduct(String str, String str2, float f, int i) {
        regProduct(str, str2, f, i, null);
    }

    public static void regProduct(String str) {
        JSONObject jSONObjectOptJSONObject;
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.e("UniSDK OrderInfo", "prodJson is null");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString(CheckNormalExitModel.JSON_PID);
            if (TextUtils.isEmpty(strOptString)) {
                strOptString = jSONObject.optString("pId");
            }
            if (TextUtils.isEmpty(strOptString)) {
                strOptString = jSONObject.optString("bid");
            }
            String strOptString2 = jSONObject.optString("productName");
            if (TextUtils.isEmpty(strOptString2)) {
                strOptString2 = jSONObject.optString("name");
            }
            String strOptString3 = jSONObject.optString("productPrice");
            float fOptDouble = TextUtils.isEmpty(strOptString3) ? (float) jSONObject.optDouble("price") : 0.0f;
            if (fOptDouble <= 0.0f) {
                fOptDouble = Float.valueOf(strOptString3).floatValue();
            }
            String channel = SdkMgr.getInst().getChannel();
            int iOptInt = jSONObject.optInt("eRatio");
            if (iOptInt <= 0 && (jSONObjectOptJSONObject = jSONObject.optJSONObject("ratios")) != null && jSONObjectOptJSONObject.length() > 0) {
                Map mapJsonToMap = jsonToMap(jSONObjectOptJSONObject);
                if (mapJsonToMap.containsKey(channel)) {
                    iOptInt = ((Integer) mapJsonToMap.get(channel)).intValue();
                }
            }
            if (iOptInt <= 0) {
                iOptInt = 1;
            }
            Map map = new HashMap();
            JSONObject jSONObjectOptJSONObject2 = jSONObject.optJSONObject("sdkPids");
            if (jSONObjectOptJSONObject2 == null) {
                jSONObjectOptJSONObject2 = jSONObject.optJSONObject("pids");
            }
            if (jSONObjectOptJSONObject2 != null && jSONObjectOptJSONObject2.length() > 0) {
                map = jsonToMap(jSONObjectOptJSONObject2);
            }
            regProduct(strOptString, strOptString2, fOptDouble, iOptInt, map);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:52:0x01f9 A[Catch: Exception -> 0x024a, TryCatch #0 {Exception -> 0x024a, blocks: (B:6:0x0010, B:9:0x005d, B:10:0x0065, B:13:0x007b, B:15:0x0091, B:17:0x0110, B:18:0x0115, B:20:0x0138, B:23:0x015a, B:27:0x0176, B:29:0x017c, B:33:0x019c, B:35:0x01a3, B:41:0x01be, B:43:0x01c4, B:50:0x01df, B:52:0x01f9, B:54:0x0207, B:56:0x0235, B:57:0x023a, B:53:0x0202, B:46:0x01cf, B:48:0x01d5, B:49:0x01da, B:37:0x01b0, B:31:0x018d, B:25:0x0167), top: B:62:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0202 A[Catch: Exception -> 0x024a, TryCatch #0 {Exception -> 0x024a, blocks: (B:6:0x0010, B:9:0x005d, B:10:0x0065, B:13:0x007b, B:15:0x0091, B:17:0x0110, B:18:0x0115, B:20:0x0138, B:23:0x015a, B:27:0x0176, B:29:0x017c, B:33:0x019c, B:35:0x01a3, B:41:0x01be, B:43:0x01c4, B:50:0x01df, B:52:0x01f9, B:54:0x0207, B:56:0x0235, B:57:0x023a, B:53:0x0202, B:46:0x01cf, B:48:0x01d5, B:49:0x01da, B:37:0x01b0, B:31:0x018d, B:25:0x0167), top: B:62:0x0010 }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0235 A[Catch: Exception -> 0x024a, TryCatch #0 {Exception -> 0x024a, blocks: (B:6:0x0010, B:9:0x005d, B:10:0x0065, B:13:0x007b, B:15:0x0091, B:17:0x0110, B:18:0x0115, B:20:0x0138, B:23:0x015a, B:27:0x0176, B:29:0x017c, B:33:0x019c, B:35:0x01a3, B:41:0x01be, B:43:0x01c4, B:50:0x01df, B:52:0x01f9, B:54:0x0207, B:56:0x0235, B:57:0x023a, B:53:0x0202, B:46:0x01cf, B:48:0x01d5, B:49:0x01da, B:37:0x01b0, B:31:0x018d, B:25:0x0167), top: B:62:0x0010 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static void c(java.lang.String r24) {
        /*
            Method dump skipped, instructions count: 591
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.OrderInfo.c(java.lang.String):void");
    }

    static void a(String str) {
        if (TextUtils.isEmpty(str)) {
            UniSdkUtils.e("UniSDK OrderInfo", "prodJson is null");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String strOptString = jSONObject.optString("goodsid");
            String strOptString2 = jSONObject.optString("goodsinfo");
            float fOptDouble = (float) jSONObject.optDouble("price");
            String channel = SdkMgr.getInst().getChannel();
            Map map = new HashMap();
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("ratios");
            int iIntValue = 0;
            if (jSONObjectOptJSONObject != null && jSONObjectOptJSONObject.length() > 0) {
                map = jsonToMap(jSONObjectOptJSONObject);
                if (map.containsKey(channel)) {
                    iIntValue = ((Integer) map.get(channel)).intValue();
                }
            }
            Map map2 = map;
            int i = iIntValue <= 0 ? 1 : iIntValue;
            Map map3 = new HashMap();
            JSONObject jSONObjectOptJSONObject2 = jSONObject.optJSONObject("channel_goodsids");
            if (jSONObjectOptJSONObject2 != null && jSONObjectOptJSONObject2.length() > 0) {
                map3 = jsonToMap(jSONObjectOptJSONObject2);
            }
            Map map4 = map3;
            Map map5 = new HashMap();
            JSONObject jSONObjectOptJSONObject3 = jSONObject.optJSONObject("channel_goodstypes");
            if (jSONObjectOptJSONObject3 != null && jSONObjectOptJSONObject3.length() > 0) {
                map5 = jsonToMap(jSONObjectOptJSONObject3);
            }
            Map map6 = map5;
            String string = "";
            JSONObject jSONObjectOptJSONObject4 = jSONObject.optJSONObject(OneTrackParams.CommonParams.EXTRA);
            if (jSONObjectOptJSONObject4 != null && jSONObjectOptJSONObject4.length() > 0) {
                string = jSONObjectOptJSONObject4.toString();
            }
            regProduct(strOptString, strOptString2, fOptDouble, i, map4, map6, map2, string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Map jsonToMap(JSONObject jSONObject) throws JSONException {
        return jSONObject != JSONObject.NULL ? toMap(jSONObject) : new HashMap();
    }

    public static Map toMap(JSONObject jSONObject) throws JSONException {
        HashMap map = new HashMap();
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            Object map2 = jSONObject.get(next);
            if (map2 instanceof JSONArray) {
                map2 = toList((JSONArray) map2);
            } else if (map2 instanceof JSONObject) {
                map2 = toMap((JSONObject) map2);
            }
            map.put(next, map2);
        }
        return map;
    }

    public static List toList(JSONArray jSONArray) throws JSONException {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            Object map = jSONArray.get(i);
            if (map instanceof JSONArray) {
                map = toList((JSONArray) map);
            } else if (map instanceof JSONObject) {
                map = toMap((JSONObject) map);
            }
            arrayList.add(map);
        }
        return arrayList;
    }

    public static void regProduct(String str, String str2, float f, int i, Map<String, String> map) {
        ProductInfo productInfo;
        StringBuilder sb = new StringBuilder("regProduct: ");
        sb.append(str);
        sb.append(" ");
        sb.append(str2);
        sb.append(" ");
        sb.append(f);
        sb.append(" ");
        sb.append(i);
        sb.append(" ");
        sb.append(map == null ? "" : map.toString());
        UniSdkUtils.i("UniSDK OrderInfo", sb.toString());
        if (f1609a.containsKey(str)) {
            productInfo = f1609a.get(str);
        } else {
            productInfo = new ProductInfo();
        }
        productInfo.productId = str;
        productInfo.productName = str2;
        productInfo.productPrice = f;
        productInfo.exchangeRatio = i;
        if (map != null) {
            productInfo.sdkPids.putAll(map);
        }
        productInfo.payChannel = ((SdkBase) SdkMgr.getInst()).choosePayChannel(map);
        UniSdkUtils.i("UniSDK OrderInfo", "choose payChannel:" + productInfo.payChannel + " for pId:" + productInfo.productId);
        f1609a.put(str, productInfo);
    }

    public static void regProduct(String str, String str2, float f, int i, Map<String, String> map, Map<String, Integer> map2, Map<String, Integer> map3, String str3) {
        ProductInfo productInfo;
        regProduct(str, str2, f, i, map);
        if (f1609a.containsKey(str)) {
            productInfo = f1609a.get(str);
        } else {
            productInfo = new ProductInfo();
        }
        if (map2 != null) {
            productInfo.channelGoodsTypes.putAll(map2);
        }
        if (map3 != null) {
            productInfo.jellRatios.putAll(map3);
        }
        productInfo.jellyExtra = str3;
    }

    public static boolean hasProduct(String str) {
        return f1609a.containsKey(str);
    }

    public static boolean hasProduct(OrderInfo orderInfo) {
        if (orderInfo == null) {
            return false;
        }
        String productId = orderInfo.getProductId();
        if (TextUtils.isEmpty(productId)) {
            return false;
        }
        if (ConstProp.GAS_GOODS_CART.equals(productId)) {
            return orderInfo.produceCartInfo() != null;
        }
        return hasProduct(productId);
    }

    public OrderInfo(String str) {
        this.e = -1.0f;
        this.f = 1;
        this.g = "";
        this.h = 0;
        this.i = "";
        this.j = "";
        this.k = "";
        this.l = "";
        this.m = "";
        this.n = "";
        this.o = false;
        this.p = "";
        this.q = "";
        this.r = "";
        this.s = "";
        this.t = "";
        this.u = "";
        this.v = "";
        this.w = "";
        this.x = "";
        this.A = "";
        this.B = "";
        this.C = -1;
        this.D = "";
        this.I = "";
        this.E = "";
        setProductId(str);
        setBid(str);
        this.G = System.currentTimeMillis();
    }

    public OrderInfo(OrderInfo orderInfo) {
        this.e = -1.0f;
        this.f = 1;
        this.g = "";
        this.h = 0;
        this.i = "";
        this.j = "";
        this.k = "";
        this.l = "";
        this.m = "";
        this.n = "";
        this.o = false;
        this.p = "";
        this.q = "";
        this.r = "";
        this.s = "";
        this.t = "";
        this.u = "";
        this.v = "";
        this.w = "";
        this.x = "";
        this.A = "";
        this.B = "";
        this.C = -1;
        this.D = "";
        this.I = "";
        this.E = "";
        this.b = orderInfo.b;
        this.c = orderInfo.c;
        this.F = orderInfo.F;
        this.d = orderInfo.d;
        this.e = orderInfo.e;
        this.f = orderInfo.f;
        this.g = orderInfo.g;
        this.h = orderInfo.h;
        this.i = orderInfo.i;
        this.j = orderInfo.j;
        this.k = orderInfo.k;
        this.l = orderInfo.l;
        this.m = orderInfo.m;
        this.n = orderInfo.n;
        this.o = orderInfo.o;
        this.p = orderInfo.p;
        this.G = System.currentTimeMillis();
        this.q = orderInfo.q;
        this.r = orderInfo.r;
        this.s = orderInfo.s;
        this.t = orderInfo.t;
        this.u = orderInfo.u;
        this.v = orderInfo.v;
        this.w = orderInfo.w;
        this.x = orderInfo.x;
        this.y = orderInfo.y;
        this.z = orderInfo.z;
        this.A = orderInfo.A;
        this.B = orderInfo.B;
        this.C = orderInfo.C;
        this.D = orderInfo.D;
        this.I = orderInfo.I;
        this.E = orderInfo.E;
        this.J = orderInfo.J;
        this.H = orderInfo.H;
        this.K = orderInfo.K;
    }

    public String getOrderId() {
        return TextUtils.isEmpty(this.b) ? "" : this.b;
    }

    public void setOrderId(String str) {
        this.b = str;
    }

    public String getProductId() {
        return this.d;
    }

    public void setProductId(String str) {
        this.d = str;
    }

    public float getProductCurrentPrice() {
        ProductInfo productInfoA;
        if (this.e < 0.0f && (productInfoA = a()) != null) {
            return productInfoA.productPrice;
        }
        return this.e;
    }

    public void setProductCurrentPrice(float f) throws Exception {
        if (f < 0.0f || f > Float.MAX_VALUE) {
            throw new Exception("product price error:".concat(String.valueOf(f)));
        }
        this.e = f;
    }

    public int getCount() {
        if (this.f <= 0) {
            this.f = 1;
        }
        return this.f;
    }

    public void setCount(int i) {
        this.f = i;
    }

    public String getOrderDesc() {
        return this.g;
    }

    public void setOrderDesc(String str) {
        this.g = str;
    }

    private ProductInfo a() {
        JSONObject jSONObject;
        float fOptDouble;
        String str;
        String str2;
        ProductInfo productInfoCopy = f1609a.get(this.d);
        if (isJFV2Product && productInfoCopy != null) {
            productInfoCopy = productInfoCopy.copy();
            String orderChannel = getOrderChannel();
            try {
                jSONObject = new JSONObject(productInfoCopy.defaultInfoJson);
            } catch (Exception unused) {
                jSONObject = null;
            }
            if (productInfoCopy.goodsinfoMap.containsKey(orderChannel)) {
                str = productInfoCopy.goodsinfoMap.get(orderChannel);
                fOptDouble = productInfoCopy.priceMap.get(orderChannel).floatValue();
                str2 = productInfoCopy.extraMap.get(orderChannel);
            } else if (jSONObject != null) {
                String strOptString = jSONObject.optString("goodsinfo");
                fOptDouble = (float) jSONObject.optDouble("price");
                JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(OneTrackParams.CommonParams.EXTRA);
                if (jSONObjectOptJSONObject == null) {
                    jSONObjectOptJSONObject = new JSONObject();
                }
                String string = jSONObjectOptJSONObject.toString();
                str = strOptString;
                str2 = string;
            }
            productInfoCopy.productName = str;
            productInfoCopy.productPrice = fOptDouble;
            productInfoCopy.jellyExtra = str2;
        }
        return productInfoCopy;
    }

    public String getProductName() {
        ProductInfo productInfoA = a();
        return productInfoA != null ? productInfoA.productName : "";
    }

    public float getProductPrice() {
        ProductInfo productInfoA = a();
        if (productInfoA != null) {
            return productInfoA.productPrice;
        }
        return 0.0f;
    }

    public Map<String, String> getSdkPids() {
        ProductInfo productInfoA = a();
        if (productInfoA != null) {
            return productInfoA.sdkPids;
        }
        return null;
    }

    public Map<String, Integer> getChannelGoodsTypes() {
        ProductInfo productInfoA = a();
        if (productInfoA != null) {
            return productInfoA.channelGoodsTypes;
        }
        return null;
    }

    public Map<String, Integer> getJellRatios() {
        ProductInfo productInfoA = a();
        if (productInfoA != null) {
            return productInfoA.jellRatios;
        }
        return null;
    }

    public String getJellyExtra() {
        ProductInfo productInfoA = a();
        return productInfoA != null ? productInfoA.jellyExtra : "";
    }

    public int getProductExchangeRatio() {
        ProductInfo productInfoA = a();
        if (productInfoA != null) {
            return productInfoA.exchangeRatio;
        }
        return 0;
    }

    public String getPayChannel() {
        ProductInfo productInfo = f1609a.get(this.d);
        if (productInfo != null) {
            return productInfo.payChannel;
        }
        return ((SdkBase) SdkMgr.getInst()).choosePayChannel(getSdkPids());
    }

    public String getFFChannel() {
        return getPayChannel();
    }

    public String getSdkOrderId() {
        String str = TextUtils.isEmpty(this.c) ? this.b : this.c;
        return str == null ? "" : str;
    }

    public void setSdkOrderId(String str) {
        this.c = str;
    }

    public String getCpOrderId() {
        return this.F;
    }

    public void setCpOrderId(String str) {
        this.F = str;
    }

    public int getOrderStatus() {
        return this.h;
    }

    public void setOrderStatus(int i) {
        this.h = i;
    }

    public String getOrderErrReason() {
        return this.i;
    }

    public void setOrderErrReason(String str) {
        this.i = str;
    }

    public String getOrderEtc() {
        return this.j;
    }

    public void setOrderEtc(String str) {
        this.j = str;
    }

    public String getSignature() {
        return this.k;
    }

    public void setSignature(String str) {
        this.k = str;
    }

    public String getOrderCurrency() {
        return this.m;
    }

    public void setOrderCurrency(String str) {
        this.m = str;
    }

    public String getOrderChannel() {
        UniSdkUtils.d("UniSDK OrderInfo", "getOrderChannel...");
        if (TextUtils.isEmpty(this.l)) {
            UniSdkUtils.d("UniSDK OrderInfo", "getPayChannel()");
            return getPayChannel();
        }
        return this.l;
    }

    public String getCurOrderChannel() {
        UniSdkUtils.d("UniSDK OrderInfo", "getCurOrderChannel...");
        return this.l;
    }

    public void setOrderChannel(String str) {
        this.l = str;
    }

    public String getExternalChannelId() {
        return this.n;
    }

    public void setExternalChannelId(String str) {
        this.n = str;
    }

    public boolean isWebPayment() {
        return this.o;
    }

    public boolean isCartOrder() {
        String productId = getProductId();
        return !TextUtils.isEmpty(productId) && productId.equals(ConstProp.GAS_GOODS_CART);
    }

    public void setIsWebPayment(boolean z) {
        this.o = z;
    }

    public void setIsQRCodeOrder(boolean z) {
        setIsWebPayment(z);
    }

    public boolean isQRCodeOrder() {
        return isWebPayment();
    }

    public String getQrCodeParams() {
        return this.p;
    }

    public void setQrCodeParams(String str) {
        this.p = str;
    }

    public String getUserData() {
        return this.q;
    }

    public void setUserData(String str) {
        this.q = str;
    }

    public String getJfExtInfo() {
        return this.s;
    }

    public void setJfExtInfo(String str) {
        this.s = str;
    }

    public String getJfGas3Url() {
        return this.t;
    }

    public void setJfGas3Url(String str) {
        this.t = str;
    }

    public String getUserName() {
        return this.u;
    }

    public void setUserName(String str) {
        this.u = str;
    }

    public String getServerId() {
        return this.v;
    }

    public void setServerId(String str) {
        this.v = str;
    }

    public String getUid() {
        return this.w;
    }

    public void setUid(String str) {
        this.w = str;
    }

    public String getAid() {
        return this.x;
    }

    public void setAid(String str) {
        this.x = str;
    }

    public int getJfCode() {
        return this.y;
    }

    public void setJfCode(int i) {
        this.y = i;
    }

    public int getJfSubCode() {
        return this.z;
    }

    public void setJfSubCode(int i) {
        this.z = i;
    }

    public String getJfMessage() {
        return this.A;
    }

    public void setJfMessage(String str) {
        this.A = str;
    }

    public String getArrPriceLocaleId() {
        return this.r;
    }

    public void setArrPriceLocaleId(String str) {
        this.r = str;
    }

    public String getUnisdkJfExtCid() {
        return this.B;
    }

    public void setUnisdkJfExtCid(String str) {
        this.B = str;
    }

    public int getJfAasFfCode() {
        return this.C;
    }

    public void setJfAasFfCode(int i) {
        this.C = i;
    }

    public String getJfAasFfRule() {
        return this.D;
    }

    public void setJfAasFfRule(String str) {
        this.D = str;
    }

    public String getJfExtraJson() {
        return this.I;
    }

    public void setJfExtraJson(String str) {
        this.I = str;
    }

    public String getExtendJson() {
        return this.E;
    }

    public void setExtendJson(String str) {
        this.E = str;
    }

    public long getTimeStamp() {
        return this.G;
    }

    public int getOrderType() {
        return this.H;
    }

    public void setOrderType(int i) {
        this.H = i;
    }

    public String getBid() {
        return this.K;
    }

    public void setBid(String str) {
        this.K = str;
    }

    public static OrderInfo jsonStr2Obj(String str) {
        String strOptString;
        String strOptString2;
        String strOptString3;
        String strOptString4;
        double dOptDouble;
        int iOptInt;
        String strOptString5;
        int iOptInt2;
        String strOptString6;
        String strOptString7;
        String strOptString8;
        String strOptString9;
        String strOptString10;
        boolean zOptBoolean;
        String strOptString11;
        String strOptString12;
        String strOptString13;
        String strOptString14;
        String strOptString15;
        String strOptString16;
        String strOptString17;
        String strOptString18;
        int iOptInt3;
        int iOptInt4;
        String strOptString19;
        String strOptString20;
        String strOptString21;
        int iOptInt5;
        String strOptString22;
        JSONObject jSONObjectOptJSONObject;
        JSONObject jSONObjectOptJSONObject2;
        int iOptInt6;
        OrderInfo orderInfo = new OrderInfo("");
        if (TextUtils.isEmpty(str)) {
            return orderInfo;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            strOptString = jSONObject.optString("orderId");
            strOptString2 = jSONObject.optString("sdkOrderId");
            strOptString3 = jSONObject.optString("cpOrderId");
            String strOptString23 = jSONObject.optString(CheckNormalExitModel.JSON_PID);
            strOptString4 = jSONObject.optString("bid");
            dOptDouble = jSONObject.optDouble("productCurrentPrice");
            iOptInt = jSONObject.optInt("productCount");
            strOptString5 = jSONObject.optString("orderDesc");
            iOptInt2 = jSONObject.optInt("orderStatus");
            strOptString6 = jSONObject.optString("orderErrReason");
            strOptString7 = jSONObject.optString("orderEtc");
            strOptString8 = jSONObject.optString(SocialOperation.GAME_SIGNATURE);
            strOptString9 = jSONObject.optString("orderChannel");
            try {
                strOptString10 = jSONObject.optString("orderCurrency");
                zOptBoolean = jSONObject.optBoolean("webPayment");
                strOptString11 = jSONObject.optString("qrCodeParams");
                strOptString12 = jSONObject.optString("userData");
                strOptString13 = jSONObject.optString("jfExtInfo");
                strOptString14 = jSONObject.optString("jfGas3Url");
                strOptString15 = jSONObject.optString("uid");
                strOptString16 = jSONObject.optString("aid");
                strOptString17 = jSONObject.optString("serverId");
                strOptString18 = jSONObject.optString("userName");
                iOptInt3 = jSONObject.optInt("jfCode");
                iOptInt4 = jSONObject.optInt("jfSubCode");
                strOptString19 = jSONObject.optString("jfMessage");
                strOptString20 = jSONObject.optString("arrPriceLocaleId");
                strOptString21 = jSONObject.optString("unisdkJfExtCid");
                iOptInt5 = jSONObject.optInt("jfAasFfCode");
                strOptString22 = jSONObject.optString("jfAasFfRule");
                jSONObjectOptJSONObject = jSONObject.optJSONObject("extendJson");
                jSONObjectOptJSONObject2 = jSONObject.optJSONObject("jfExtraJson");
                iOptInt6 = jSONObject.optInt("orderType");
                orderInfo = new OrderInfo(strOptString23);
            } catch (Exception e) {
                e = e;
                orderInfo = orderInfo;
            }
        } catch (Exception e2) {
            e = e2;
        }
        try {
            orderInfo.setBid(strOptString4);
            orderInfo.setOrderId(strOptString);
            orderInfo.setSdkOrderId(strOptString2);
            orderInfo.setCpOrderId(strOptString3);
            if (dOptDouble > 0.0d) {
                orderInfo.setProductCurrentPrice((float) dOptDouble);
            }
            if (iOptInt > 0) {
                orderInfo.setCount(iOptInt);
            }
            orderInfo.setOrderDesc(strOptString5);
            orderInfo.setOrderStatus(iOptInt2);
            orderInfo.setOrderErrReason(strOptString6);
            orderInfo.setOrderEtc(strOptString7);
            orderInfo.setSignature(strOptString8);
            orderInfo.setOrderChannel(strOptString9);
            orderInfo.setOrderCurrency(strOptString10);
            orderInfo.setIsWebPayment(zOptBoolean);
            orderInfo.setQrCodeParams(strOptString11);
            orderInfo.setUserData(strOptString12);
            orderInfo.setJfExtInfo(strOptString13);
            orderInfo.setJfGas3Url(strOptString14);
            orderInfo.setUid(strOptString15);
            orderInfo.setAid(strOptString16);
            orderInfo.setServerId(strOptString17);
            orderInfo.setUserName(strOptString18);
            orderInfo.setJfCode(iOptInt3);
            orderInfo.setJfSubCode(iOptInt4);
            orderInfo.setJfMessage(strOptString19);
            orderInfo.setArrPriceLocaleId(strOptString20);
            orderInfo.setUnisdkJfExtCid(strOptString21);
            orderInfo.setJfAasFfCode(iOptInt5);
            orderInfo.setJfAasFfRule(strOptString22);
            orderInfo.setOrderType(iOptInt6);
            if (jSONObjectOptJSONObject != null) {
                orderInfo.setExtendJson(jSONObjectOptJSONObject.toString());
            }
            if (jSONObjectOptJSONObject2 != null) {
                orderInfo.setJfExtraJson(jSONObjectOptJSONObject2.toString());
            }
        } catch (Exception e3) {
            e = e3;
            UniSdkUtils.e("UniSDK OrderInfo", "jsonStr2Obj error");
            e.printStackTrace();
            return orderInfo;
        }
        return orderInfo;
    }

    public static JSONObject obj2Json(OrderInfo orderInfo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (orderInfo == null) {
            return jSONObject;
        }
        try {
            jSONObject.put("orderId", orderInfo.getOrderId());
            jSONObject.put("sdkOrderId", orderInfo.getSdkOrderId());
            jSONObject.put("cpOrderId", orderInfo.getCpOrderId());
            jSONObject.put(CheckNormalExitModel.JSON_PID, orderInfo.getProductId());
            jSONObject.put("bid", orderInfo.getBid());
            jSONObject.put("productCurrentPrice", orderInfo.getProductCurrentPrice());
            jSONObject.put("productCount", orderInfo.getCount());
            jSONObject.put("orderDesc", orderInfo.getOrderDesc());
            jSONObject.put("orderStatus", orderInfo.getOrderStatus());
            jSONObject.put("orderErrReason", orderInfo.getOrderErrReason());
            jSONObject.put("orderEtc", orderInfo.getOrderEtc());
            jSONObject.put(SocialOperation.GAME_SIGNATURE, orderInfo.getSignature());
            jSONObject.put("orderChannel", orderInfo.getOrderChannel());
            jSONObject.put("orderCurrency", orderInfo.getOrderCurrency());
            jSONObject.put("productName", orderInfo.getProductName());
            jSONObject.put("webPayment", orderInfo.isWebPayment());
            jSONObject.put("qrCodeParams", orderInfo.getQrCodeParams());
            jSONObject.put("userData", orderInfo.getUserData());
            jSONObject.put("jfExtInfo", orderInfo.getJfExtInfo());
            jSONObject.put("jfGas3Url", orderInfo.getJfGas3Url());
            jSONObject.put("uid", orderInfo.getUid());
            jSONObject.put("aid", orderInfo.getAid());
            jSONObject.put("serverId", orderInfo.getServerId());
            jSONObject.put("userName", orderInfo.getUserName());
            jSONObject.put("jfCode", orderInfo.getJfCode());
            jSONObject.put("jfSubCode", orderInfo.getJfSubCode());
            jSONObject.put("jfMessage", orderInfo.getJfMessage());
            jSONObject.put("arrPriceLocaleId", orderInfo.getArrPriceLocaleId());
            jSONObject.put("unisdkJfExtCid", orderInfo.getUnisdkJfExtCid());
            jSONObject.put("jfAasFfCode", orderInfo.getJfAasFfCode());
            jSONObject.put("jfAasFfRule", orderInfo.getJfAasFfRule());
            jSONObject.put("timeStamp", orderInfo.getTimeStamp());
            jSONObject.put("orderType", orderInfo.getOrderType());
            if (!TextUtils.isEmpty(orderInfo.getExtendJson())) {
                jSONObject.putOpt("extendJson", new JSONObject(orderInfo.getExtendJson()));
            }
            if (!TextUtils.isEmpty(orderInfo.getJfExtraJson())) {
                jSONObject.putOpt("jfExtraJson", new JSONObject(orderInfo.getJfExtraJson()));
            }
        } catch (JSONException e) {
            UniSdkUtils.i("UniSDK OrderInfo", "obj2Json error");
            e.printStackTrace();
        }
        return jSONObject;
    }

    public JSONArray produceCartInfo() {
        JSONArray jSONArrayB = b(getExtendJson());
        if (jSONArrayB == null) {
            return jSONArrayB;
        }
        try {
            if (jSONArrayB.length() <= 0) {
                return jSONArrayB;
            }
            float f = 0.0f;
            for (int i = 0; i < jSONArrayB.length(); i++) {
                JSONObject jSONObjectOptJSONObject = jSONArrayB.optJSONObject(i);
                String strOptString = jSONObjectOptJSONObject.optString("goodsid");
                int iOptInt = jSONObjectOptJSONObject.optInt("goodscount");
                if (!TextUtils.isEmpty(strOptString) && hasProduct(strOptString) && iOptInt > 0) {
                    ProductInfo productInfo = f1609a.get(strOptString);
                    if (productInfo == null) {
                        throw new IllegalStateException("production for [" + strOptString + "] not found");
                    }
                    UniSdkUtils.i("UniSDK OrderInfo", productInfo.productId + ":" + productInfo.productName + ":" + productInfo.productPrice);
                    f += productInfo.productPrice * ((float) iOptInt);
                }
            }
            UniSdkUtils.i("UniSDK OrderInfo", "priceTotal=".concat(String.valueOf(f)));
            if (0.0f >= f) {
                return jSONArrayB;
            }
            setProductCurrentPrice(f);
            setCount(1);
            return jSONArrayB;
        } catch (Exception e) {
            UniSdkUtils.d("UniSDK OrderInfo", "goodscart traverse:" + e.getMessage());
            return null;
        }
    }

    private static JSONArray b(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return new JSONObject(str).optJSONArray("goodscart");
            } catch (Exception e) {
                UniSdkUtils.d("UniSDK OrderInfo", "extJson(goodscart):" + e.getMessage());
            }
        }
        return null;
    }

    public String toString() {
        return String.format("orderId:%s&orderEtc:%s&userData:%s&jfExtInfo:%s&jfCode:%s&jfSubCode:%s&jfMessage:%s&arrPriceLocaleId:%s&unisdkJfExtCid:%s&userName:%s&serverId:%s&uid:%s&aid:%s&jfAasFfCode:%s&extendJson:%s", this.b, this.j, this.q, this.s, Integer.valueOf(this.y), Integer.valueOf(this.z), this.A, this.r, this.B, this.u, this.v, this.w, this.x, Integer.valueOf(this.C), this.E);
    }
}