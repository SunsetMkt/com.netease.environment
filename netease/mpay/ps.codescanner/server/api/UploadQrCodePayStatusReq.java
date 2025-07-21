package com.netease.mpay.ps.codescanner.server.api;

import com.netease.mpay.ps.codescanner.net.BasicNameValuePair;
import com.netease.mpay.ps.codescanner.net.NameValuePair;
import java.util.ArrayList;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class UploadQrCodePayStatusReq extends Request {
    String orderId;
    UploadQrCodePayStatusResp resp;
    String sn;
    int status;
    String uid;

    enum PayStatus {
        SUCCESS,
        FAILURE,
        UNKNOWN,
        LOGOUT,
        CANCEL;

        static PayStatus get(int i) {
            try {
                return values()[i];
            } catch (ArrayIndexOutOfBoundsException unused) {
                return UNKNOWN;
            }
        }
    }

    public UploadQrCodePayStatusReq(String str, String str2, String str3, int i) {
        super(1, "/api/qrcode/external_pay_callback");
        this.uid = str;
        this.orderId = str2;
        this.sn = str3;
        this.status = i;
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    public UploadQrCodePayStatusResp getResponse() {
        return this.resp;
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    ArrayList<NameValuePair> getDatas() {
        ArrayList<NameValuePair> arrayList = new ArrayList<>();
        arrayList.add(new BasicNameValuePair("uid", this.uid));
        arrayList.add(new BasicNameValuePair("order_id", this.orderId));
        arrayList.add(new BasicNameValuePair("sn", this.sn));
        arrayList.add(new BasicNameValuePair("status", String.valueOf(getStatus(this.status))));
        return arrayList;
    }

    /* renamed from: com.netease.mpay.ps.codescanner.server.api.UploadQrCodePayStatusReq$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$netease$mpay$ps$codescanner$server$api$UploadQrCodePayStatusReq$PayStatus = new int[PayStatus.values().length];

        static {
            try {
                $SwitchMap$com$netease$mpay$ps$codescanner$server$api$UploadQrCodePayStatusReq$PayStatus[PayStatus.SUCCESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$netease$mpay$ps$codescanner$server$api$UploadQrCodePayStatusReq$PayStatus[PayStatus.CANCEL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$netease$mpay$ps$codescanner$server$api$UploadQrCodePayStatusReq$PayStatus[PayStatus.FAILURE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    int getStatus(int i) {
        int i2 = AnonymousClass1.$SwitchMap$com$netease$mpay$ps$codescanner$server$api$UploadQrCodePayStatusReq$PayStatus[PayStatus.get(i).ordinal()];
        int i3 = 1;
        if (i2 != 1) {
            i3 = 2;
            if (i2 != 2 && i2 != 3) {
                return 3;
            }
        }
        return i3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    public UploadQrCodePayStatusResp parseContent(JSONObject jSONObject) {
        return new UploadQrCodePayStatusResp();
    }

    @Override // com.netease.mpay.ps.codescanner.server.api.Request
    void saveResponse(Response response) {
        if (response instanceof UploadQrCodePayStatusResp) {
            this.resp = (UploadQrCodePayStatusResp) response;
        }
    }
}