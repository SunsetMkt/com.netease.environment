package com.netease.mpay.ps.codescanner.server.api;

/* loaded from: classes5.dex */
public class GetQRCodeInfoResp extends Response {
    public QRCodeAction action;
    public String uuid;
    public String webGameId;
    public String webGameName;
    public int webTokenPersist;

    public GetQRCodeInfoResp(String str, QRCodeAction qRCodeAction, String str2, String str3, int i) {
        this.uuid = str;
        this.action = qRCodeAction;
        this.webGameId = str2;
        this.webGameName = str3;
        this.webTokenPersist = i;
    }

    public enum QRCodeAction {
        QRCODE_UNKNOWN(-1),
        QRCODE_LOGIN(1),
        QRCODE_PAY(2);

        private int value;

        QRCodeAction(int i) {
            this.value = i;
        }

        public static QRCodeAction convert(int i) {
            if (i == 1) {
                return QRCODE_LOGIN;
            }
            if (i == 2) {
                return QRCODE_PAY;
            }
            return QRCODE_UNKNOWN;
        }

        public int valueOf() {
            return this.value;
        }
    }
}