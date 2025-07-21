package com.netease.ntunisdk.base;

import com.tencent.open.SocialConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SkuDetailsInfo {

    /* renamed from: a, reason: collision with root package name */
    String f1795a;
    String b;
    String c;
    String d;
    String e;
    String f;
    String g;

    public SkuDetailsInfo(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        this.f1795a = str;
        this.b = str2;
        this.c = str3;
        this.d = str4;
        this.e = str5;
        this.f = str6;
        this.g = str7;
    }

    public static JSONObject obj2Json(SkuDetailsInfo skuDetailsInfo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (skuDetailsInfo == null) {
            return jSONObject;
        }
        try {
            jSONObject.put("productId", skuDetailsInfo.getProductId());
            jSONObject.put("type", skuDetailsInfo.getType());
            jSONObject.put("price", skuDetailsInfo.getPrice());
            jSONObject.put("priceAmountMicros", skuDetailsInfo.getPriceAmountMicros());
            jSONObject.put("priceCurrencyCode", skuDetailsInfo.getPriceCurrencyCode());
            jSONObject.put("title", skuDetailsInfo.getTitle());
            jSONObject.put(SocialConstants.PARAM_COMMENT, skuDetailsInfo.getDescription());
        } catch (JSONException e) {
            UniSdkUtils.e("UniSDK SkuDetailsInfo", "obj2Json error");
            e.printStackTrace();
        }
        return jSONObject;
    }

    public String getProductId() {
        return this.f1795a;
    }

    public void setSku(String str) {
        this.f1795a = str;
    }

    public String getType() {
        return this.b;
    }

    public void setType(String str) {
        this.b = str;
    }

    public String getPrice() {
        return this.c;
    }

    public void setPrice(String str) {
        this.c = str;
    }

    public String getPriceAmountMicros() {
        return this.d;
    }

    public void setPriceAmountMicros(String str) {
        this.d = str;
    }

    public String getPriceCurrencyCode() {
        return this.e;
    }

    public void setPriceCurrencyCode(String str) {
        this.e = str;
    }

    public String getTitle() {
        return this.f;
    }

    public void setTitle(String str) {
        this.f = str;
    }

    public String getDescription() {
        return this.g;
    }

    public void setDescription(String str) {
        this.g = str;
    }

    public String toString() {
        return String.format("Skudetails: productId:%s, price:%s, priceAmountMicros:%s, priceCurrencyCode:%s, title:%s, description:%s", this.f1795a, this.c, this.d, this.e, this.f, this.g);
    }
}