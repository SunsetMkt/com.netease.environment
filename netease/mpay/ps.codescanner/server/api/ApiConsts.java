package com.netease.mpay.ps.codescanner.server.api;

import android.text.TextUtils;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;

/* loaded from: classes5.dex */
public class ApiConsts {

    static final class ApiArgs {
        static final String APP_CHANNEL = "app_channel";
        static final String ENC_UUID = "enc_uuid";
        static final String EXTRA_DATA = "extra_data";
        static final String EXTRA_UNISDK_DATA = "extra_unisdk_data";
        static final String GAME_ID = "game_id";
        static final String GAME_VERSION = "gv";
        static final String GAME_VERSION_NAME = "gvn";
        static final String IS_REMEMBER = "is_remember";
        static final String LOGIN_CHANNEL = "login_channel";
        static final String ORDER_ID = "order_id";
        static final String PAY_CHANNEL = "pay_channel";
        static final String REDIRECT_URL = "redirect_url";
        static final String SCENE = "scene";
        static final String SDK_VERSION = "sdk_version";
        static final String SDK_VERSION_CODE = "cv";
        static final String SN = "sn";
        static final String STATUS = "status";
        static final String TOKEN = "token";
        static final String UDID = "udid";
        static final String UID = "uid";
        static final String USER_ID = "user_id";
        static final String UUID = "uuid";

        ApiArgs() {
        }
    }

    static final class ApiResults {
        static final String ACTION = "action";
        static final String ERROR_CODE = "code";
        static final String ERROR_REASON = "reason";
        static final String GAME = "game";
        static final String ID = "id";
        static final String NAME = "name";
        static final String ORDER = "order";
        static final String QRCODE_INFO = "qrcode_info";
        static final String UUID = "uuid";
        static final String WEB_TOKEN_PERSIST_3PARTY = "web_token_persist_3party";

        ApiResults() {
        }
    }

    public static final class WebTokenPersistMode {
        public static final int NOT_SHOW = 0;
        public static final int SHOW_AND_AGREE = 2;
        public static final int SHOW_AND_DISAGREE = 1;

        public static boolean shouldShow(int i) {
            if (preCheck()) {
                return i == 1 || i == 2;
            }
            return false;
        }

        public static boolean shouldAgree(int i) {
            return preCheck() && i == 2;
        }

        private static boolean preCheck() {
            return !TextUtils.isEmpty(SdkMgr.getInst().getPropStr(ConstProp.UNISDK_JF_ACCESS_TOKEN));
        }
    }
}