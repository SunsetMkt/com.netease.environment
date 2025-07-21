package com.netease.ngrtc;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes4.dex */
public class ProtoClient {
    private static final String TAG = "NGRTC_ProtoClient";
    private static final Map<Integer, RTCError> intToRTCErrorMap = new HashMap();

    public enum RTCError {
        ERR_NONE,
        ERR_PARAM_LACK,
        ERR_PARAM_INVALID,
        ERR_PANIC,
        ERR_GET_HTTP_CLIENT,
        ERR_GET_HTTP_REQUEST,
        ERR_HTTP_GET,
        ERR_HTTP_READ_RESPONSE,
        ERR_HTTP_PARSE_RESPONSE,
        ERR_HTTP_RETURN_ERR,
        ERR_SERVER_BUSY,
        ERR_SERVER_STOPPED,
        ERR_SERVER_NO_HANDLER,
        ERR_SERVER_PROXY,
        ERR_SERVER_DB,
        ERR_JSON,
        ERR_CRYPT,
        ERR_ROOM_OBJ_NOTEXIST,
        ERR_INVALID_SESSION,
        ERR_SESSION_CONNECT,
        ERR_CREATE_ROOM,
        ERR_NOT_AUTHORIZED,
        ERR_REPEATED_REQUEST,
        ERR_ROOM_NOTEXIST,
        ERR_RPC_PROXY,
        ERR_GATEWAY_CLOSED,
        ERR_INVALID_REQUEST_TYPE,
        ERR_INVALID_SESSIONID,
        ERR_EXPIRED_SESSIONID,
        ERR_SESSION_NONEXIST,
        ERR_UID_MISMATCH,
        ERR_SESSION_CLOSED,
        ERR_HANGUP,
        ERR_RETRY_LATER,
        ERR_ROOM_CLOSED,
        ERR_REDIS,
        ERR_NGRTC_OVERLOAD,
        ERR_JANUS_OVERLOAD,
        ERR_SESSION_REPLACED,
        ERR_NGRTC_CLOSED,
        ERR_NETWORK,
        ERR_KICK_OFF,
        ERR_PERMISSION_DENY,
        ERR_UNSUPPORT_SYSTEM,
        ERR_UNKNOWN;

        /* renamed from: values, reason: to resolve conflict with enum method */
        public static RTCError[] valuesCustom() {
            RTCError[] rTCErrorArrValuesCustom = values();
            int length = rTCErrorArrValuesCustom.length;
            RTCError[] rTCErrorArr = new RTCError[length];
            System.arraycopy(rTCErrorArrValuesCustom, 0, rTCErrorArr, 0, length);
            return rTCErrorArr;
        }
    }

    static {
        for (RTCError rTCError : RTCError.valuesCustom()) {
            intToRTCErrorMap.put(Integer.valueOf(rTCError.ordinal()), rTCError);
        }
    }

    public static RTCError getRTCError(int i) {
        RTCError rTCError = intToRTCErrorMap.get(Integer.valueOf(i));
        return rTCError == null ? RTCError.ERR_UNKNOWN : rTCError;
    }
}