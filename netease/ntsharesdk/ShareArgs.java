package com.netease.ntsharesdk;

import java.util.HashMap;

/* loaded from: classes.dex */
public class ShareArgs {
    public static final String COMMENT = "comment";
    public static final String IMG_DATA = "img_data";
    public static final String IMG_PATH = "img_path";
    public static final String IMG_URL = "img_url";
    public static final String MINI_PROGRAM_TYPE = "MINI_PROGRAM_TYPE";
    public static final String MINI_RESPONSE = "MINI_RESPONSE";
    public static final String PATH = "PATH";
    public static final String TEXT = "text";
    public static final String THUMB_DATA = "thumb_data";
    public static final String TITLE = "title";
    public static final String TO_BLOG = "to_blog";
    public static final String TYPE = "type";
    public static final String TYPE_AUDIO = "TYPE_AUDIO";
    public static final String TYPE_GIF = "TYPE_GIF";
    public static final String TYPE_IMAGE = "TYPE_IMAGE";
    public static final String TYPE_LINK = "TYPE_LINK";
    public static final String TYPE_MINI_PROGRAM = "TYPE_MINI_PROGRAM";
    public static final String TYPE_MINI_PROGRAM_SUBSCRIBE = "TYPE_MINI_PROGRAM_SUBSCRIBE";
    public static final String TYPE_TEXT_ONLY = "TYPE_TEXT_ONLY";
    public static final String TYPE_TO_MINI_PROGRAM = "TYPE_TO_MINI_PROGRAM";
    public static final String TYPE_VIDEO = "TYPE_VIDEO";
    public static final String URL = "url";
    public static final String USER_NAME = "USER_NAME";
    public static final String VIDEO_URL = "video_url";
    private HashMap<String, Object> args;
    private String failMsg;

    public ShareArgs() {
        this.args = new HashMap<>();
        this.failMsg = null;
    }

    public ShareArgs(String str) {
        this.args = new HashMap<>();
        this.failMsg = null;
        this.failMsg = str;
    }

    public Object getValue(String str) {
        return getValue(str, null);
    }

    public Object getValue(String str, Object obj) {
        if (this.args.containsKey(str)) {
            return this.args.get(str);
        }
        return (obj == null && (str == "title" || str == "text")) ? "" : obj;
    }

    public void setValue(String str, Object obj) {
        if (obj == null) {
            this.args.remove(str);
        } else {
            this.args.put(str, obj);
        }
    }

    public Boolean hasImage() {
        return Boolean.valueOf((getValue(IMG_PATH) == null && getValue(IMG_URL) == null && getValue(IMG_DATA) == null) ? false : true);
    }

    public Boolean hasUrl() {
        return Boolean.valueOf(getValue("url") != null);
    }

    public String getFailMsg() {
        return this.failMsg;
    }

    public void setFailMsg(String str) {
        this.failMsg = str;
    }

    public String toString() {
        String str = "";
        for (String str2 : this.args.keySet()) {
            str = str + str2 + ":" + this.args.get(str2) + ",";
        }
        return str;
    }
}