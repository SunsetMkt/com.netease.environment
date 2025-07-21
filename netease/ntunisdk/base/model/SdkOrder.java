package com.netease.ntunisdk.base.model;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.OrderInfo;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.constant.a;
import com.tencent.open.SocialConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class SdkOrder extends SdkState {
    public double amount;
    public String channel;
    public String consumesn;
    public String cp_orderid;
    public String currency;
    public String desc;
    public String ext_info;
    public int goodscount;
    public String goodsid;
    public String goodsinfo;
    public double price;
    public double ratio;
    public String sn;
    public int yuanbao;

    public SdkOrder() {
    }

    public SdkOrder(String str) {
        super(str);
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public void fromJson(JSONObject jSONObject) {
        super.fromJson(jSONObject);
        this.sn = jSONObject.optString("sn");
        this.consumesn = jSONObject.optString("consumesn");
        this.goodsid = jSONObject.optString("goodsid");
        this.goodscount = jSONObject.optInt("goodscount", 1);
        this.goodsinfo = jSONObject.optString("goodsinfo");
        this.price = jSONObject.optDouble("price");
        this.amount = jSONObject.optDouble("amount");
        this.desc = jSONObject.optString(SocialConstants.PARAM_APP_DESC);
        if (jSONObject.has("channel")) {
            this.channel = jSONObject.optString("channel");
        }
        this.cp_orderid = jSONObject.optString("cp_orderid");
        this.currency = jSONObject.optString(ConstProp.CURRENCY);
        this.yuanbao = jSONObject.optInt("yuanbao");
        this.ratio = jSONObject.optDouble("ratio", 1.0d);
        this.ext_info = jSONObject.optString("ext_info");
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public Object wrapTo() throws JSONException {
        super.wrapTo();
        OrderInfo.regProduct(this.goodsid, this.goodsinfo, (float) this.price, (int) this.ratio);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.putOpt("cp_orderid", this.cp_orderid);
            jSONObject.putOpt("goodsinfo", this.goodsinfo);
            jSONObject.putOpt("price", String.valueOf(this.price));
            jSONObject.putOpt("yuanbao", Integer.valueOf(this.yuanbao));
            jSONObject.putOpt(ConstProp.CURRENCY, TextUtils.isEmpty(this.currency) ? "CNY" : this.currency);
        } catch (Exception e) {
            e.printStackTrace();
        }
        OrderInfo orderInfo = new OrderInfo(this.goodsid);
        if (!TextUtils.isEmpty(this.sn)) {
            orderInfo.setOrderId(this.sn);
        }
        if (!TextUtils.isEmpty(this.consumesn)) {
            orderInfo.setSdkOrderId(this.consumesn);
        }
        orderInfo.setCount(this.goodscount);
        orderInfo.setOrderDesc(this.desc);
        orderInfo.setOrderChannel(this.channel);
        orderInfo.setOrderCurrency(this.currency);
        orderInfo.setJfExtInfo(this.ext_info);
        orderInfo.setCpOrderId(this.cp_orderid);
        orderInfo.setExtendJson(jSONObject.toString());
        return orderInfo;
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public void wrapFrom(Object obj) {
        super.wrapFrom(obj);
        if (obj instanceof OrderInfo) {
            OrderInfo orderInfo = (OrderInfo) obj;
            this.sn = orderInfo.getOrderId();
            this.consumesn = orderInfo.getSdkOrderId();
            this.goodsid = orderInfo.getProductId();
            this.goodscount = orderInfo.getCount();
            this.goodsinfo = orderInfo.getProductName();
            this.price = orderInfo.getProductCurrentPrice();
            this.desc = orderInfo.getOrderDesc();
            this.channel = orderInfo.getCurOrderChannel();
            this.cp_orderid = orderInfo.getCpOrderId();
            this.currency = orderInfo.getOrderCurrency();
            this.ratio = orderInfo.getProductExchangeRatio();
            this.ext_info = orderInfo.getJfExtInfo();
            try {
                JSONObject jSONObject = new JSONObject(orderInfo.getExtendJson());
                this.yuanbao = jSONObject.optInt("yuanbao");
                this.amount = Double.parseDouble(jSONObject.optString("amount"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            int orderStatus = orderInfo.getOrderStatus();
            if (orderStatus == 1) {
                this.message = a.Checking.d;
                this.code = a.Checking.ordinal();
            } else if (orderStatus == 2) {
                this.message = a.Suc.d;
                this.code = a.Suc.ordinal();
            } else if (orderStatus == 11) {
                this.message = orderInfo.getOrderErrReason();
                if (TextUtils.isEmpty(this.message)) {
                    this.message = a.Cancel.d;
                }
                this.code = a.Cancel.ordinal();
            } else {
                this.message = orderInfo.getOrderErrReason();
                this.code = a.Fail.ordinal();
            }
            this.subcode = SdkMgr.getInst().getPropInt(ConstProp.UNISDK_FF_CHANNEL_RAW_CODE, 0);
        }
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        try {
            json.putOpt("sn", this.sn);
            json.putOpt("consumesn", this.consumesn);
            json.putOpt("goodsid", this.goodsid);
            json.putOpt("goodscount", Integer.valueOf(this.goodscount));
            json.putOpt("goodsinfo", this.goodsinfo);
            json.putOpt("price", Double.valueOf(this.price));
            json.putOpt("amount", Double.valueOf(this.amount));
            json.putOpt(SocialConstants.PARAM_APP_DESC, this.desc);
            json.putOpt("channel", this.channel);
            json.putOpt("cp_orderid", this.cp_orderid);
            json.putOpt(ConstProp.CURRENCY, this.currency);
            json.putOpt("yuanbao", Integer.valueOf(this.yuanbao));
            json.putOpt("ratio", Double.valueOf(this.ratio));
            json.putOpt("ext_info", this.ext_info);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public boolean check() {
        return (TextUtils.isEmpty(this.goodsid) || TextUtils.isEmpty(this.cp_orderid) || TextUtils.isEmpty(this.goodsinfo) || 0.0d >= this.price) ? false : true;
    }
}