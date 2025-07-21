package com.netease.cc.ccplayerwrapper;

/* loaded from: classes4.dex */
public class Constants {
    public static final int CMD_PAUSE_VIDEO_DISPLAY = 204;
    public static final int CMD_PLAYER_SETTING = 201;
    public static final int CMD_SET_PLAYBACK_SPEED = 210;
    public static final int CMD_SET_RADICAL_LEVEL = 207;
    public static final int CMD_SET_SCALE_MODE = 208;
    public static final int CMD_SET_SCREEN_ON_WHILE_PLAYING = 209;
    public static final int CMD_SHOW_INFO_VIEW = 206;
    public static final int CMD_SWITCH_CDN = 203;
    public static final int CMD_SWITCH_VBR = 202;
    public static final int CMD_UPDATE_USER_INFO = 205;
    public static final String KEY_CCID = "ccid";
    public static final String KEY_CDN_LIST = "cdnList";
    public static final String KEY_CDN_SEL = "cdnSel";
    public static final String KEY_CHANNEL_ID = "subId";
    public static final String KEY_CMD = "cmd";
    public static final String KEY_EID = "eid";
    public static final String KEY_GAME_TYPE = "gametype";
    public static final String KEY_OPERATION = "operation";
    public static final String KEY_ROOM_ID = "roomId";
    public static final String KEY_SETTING_HTTPFLV = "useHttpFlv";
    public static final String KEY_SPEED = "speed";
    public static final String KEY_UID = "uid";
    public static final String KEY_URS = "urs";
    public static final String KEY_VALUE = "value";
    public static final String KEY_VBR = "vbr";
    public static final String KEY_VBRNAME_LIST = "vbrname_list";
    public static final String KEY_VBRNAME_SEL = "vbrname_sel";
    public static final String KEY_VBR_LIST = "vbrList";
    public static final String KEY_VBR_SEL = "vbrSel";
    public static final String KEY_VBR_SEL_CHOOSE = "vbrSelChoose";
    public static final String KEY_VBR_SEL_VALUE = "vbrSelValue";
    public static final int MEDIA_ERROR_CCPLAYER = -40000;
    public static final int MEDIA_ERROR_HTTP_FLV_AUDIO_CODECS_ERROR = -1005;
    public static final int MEDIA_ERROR_HTTP_FLV_CONN_BADFORMAT = -1003;
    public static final int MEDIA_ERROR_HTTP_FLV_CONN_CLOSE = -1002;
    public static final int MEDIA_ERROR_HTTP_FLV_CONN_FAILED = -1000;
    public static final int MEDIA_ERROR_HTTP_FLV_CONN_METATIMEOUT = -1004;
    public static final int MEDIA_ERROR_HTTP_FLV_CONN_TIMEOUT = -1001;
    public static final int MEDIA_ERROR_HTTP_FLV_HEADER_PARSE_ERROR = -1009;
    public static final int MEDIA_ERROR_HTTP_FLV_INVALID_STATUE = -1011;
    public static final int MEDIA_ERROR_HTTP_FLV_NO_HEADER = -1010;
    public static final int MEDIA_ERROR_HTTP_FLV_OPEN_ERROR = -1007;
    public static final int MEDIA_ERROR_HTTP_FLV_SOCKRT_ERROR = -1008;
    public static final int MEDIA_ERROR_HTTP_FLV_VIDEO_CODECS_ERROR = -1006;
    public static final int MEDIA_ERROR_INVALID_PARAM = -41002;
    public static final int MEDIA_ERROR_LOGIC = -41001;
    public static final int MEDIA_ERROR_NO_LIVE = -40410;
    public static final int MEDIA_ERROR_STATE = -41000;
    public static final int MEDIA_GET_VBR_INFO = 20001;
    public static final int MEDIA_STATE_CHANGE = 20002;
    public static final short MSG_ENABLE_LOG = 112;
    public static final short MSG_ENABLE_MEDIA_CODEC = 115;
    public static final short MSG_INIT = 100;
    public static final short MSG_MOBILE_URL_RESULT = 122;
    public static final short MSG_MUTE_AUDIO = 106;
    public static final short MSG_PAUSE = 107;
    public static final short MSG_PLAY = 118;
    public static final short MSG_RADICAL_BUFFER = 114;
    public static final short MSG_RELEASE_PLAYER = 102;
    public static final short MSG_RESUME = 108;
    public static final short MSG_SEEK_TO = 113;
    public static final short MSG_SEND_CMD = 117;
    public static final short MSG_SET_DEV_MODE = 111;
    public static final short MSG_SET_SCALE_MODE = 109;
    public static final short MSG_SET_SCREEN_ON = 116;
    public static final short MSG_SET_VOLUME = 105;
    public static final short MSG_START_PLAY = 104;
    public static final short MSG_STOP = 119;
    public static final short MSG_STOP_PLAY = 110;
    public static final short MSG_UPDATE_INFO = 121;
    public static final int OPERATION_ADD = 1;
    public static final int OPERATION_DEL = 2;
    public static final String QUERY_STAT_BASE_URL_DEBUG = "http://192.168.229.163:18899/query?content=";
    public static final String QUERY_STAT_BASE_URL_RELEASE = "https://vquery.cc.163.com/query?content=";
    public static final int RadicalLevel_0 = 0;
    public static final int RadicalLevel_1 = 1;
    public static final int RadicalLevel_2 = 2;
    public static final int RadicalLevel_3 = 3;
    public static final short SCALE_ASPECT_FILL = 3;
    public static final short SCALE_ASPECT_FIT = 2;
    public static final short SCALE_TO_FILL = 1;

    public enum PLAY_STATE {
        INIT("INIT"),
        PREPARING("PREPARING"),
        PLAYING("PLAYING"),
        PAUSE("PAUSE"),
        STOP("STOP"),
        RELEASE("RELEASE");

        private String name;

        PLAY_STATE(String str) {
            this.name = str;
        }

        @Override // java.lang.Enum
        public String toString() {
            return this.name;
        }
    }
}