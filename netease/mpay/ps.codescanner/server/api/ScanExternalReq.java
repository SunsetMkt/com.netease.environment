package com.netease.mpay.ps.codescanner.server.api;

import com.netease.mpay.ps.codescanner.net.BasicNameValuePair;
import com.netease.mpay.ps.codescanner.net.NameValuePair;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class ScanExternalReq extends Request {
    String orderId;
    ScanExternalResp resp;
    String uid;

    public ScanExternalReq(String str, String str2) {
        super(0, "/api/qrcode/scan_external");
        this.uid = str;
        this.orderId = str2;
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    public ScanExternalResp getResponse() {
        return this.resp;
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    ArrayList<NameValuePair> getDatas() {
        ArrayList<NameValuePair> arrayList = new ArrayList<>();
        arrayList.add(new BasicNameValuePair("uid", this.uid));
        arrayList.add(new BasicNameValuePair("order_id", this.orderId));
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    public ScanExternalResp parseContent(JSONObject jSONObject) throws JSONException {
        return new ScanExternalResp();
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    void saveResponse(Response response) {
        if (response instanceof ScanExternalResp) {
            this.resp = (ScanExternalResp) response;
        }
    }
}