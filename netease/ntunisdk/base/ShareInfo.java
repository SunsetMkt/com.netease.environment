package com.netease.ntunisdk.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.netease.ntunisdk.base.utils.StrUtil;
import com.tencent.connect.common.Constants;
import com.tencent.open.SocialConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jose4j.jwx.HeaderParameterNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ShareInfo {
    public static final String SCOPE_CIRCLE = "SCOPE_CIRCLE";
    public static final String SCOPE_MULTIUSER = "SCOPE_MULTIUSER";
    public static final String SCOPE_TOUSER = "SCOPE_TOUSER";
    public static final String TYPE_ATTENTION = "TYPE_ATTENTION";
    public static final String TYPE_AUDIO = "TYPE_AUDIO";
    public static final String TYPE_CLEAR_TOKEN = "TYPE_CLEAR_TOKEN";
    public static final String TYPE_GAME_REQUEST = "TYPE_GAME_REQUEST";
    public static final String TYPE_GET_RTMP = "TYPE_GET_RTMP";
    public static final String TYPE_GET_TOKEN = "TYPE_GET_TOKEN";
    public static final String TYPE_GIF = "TYPE_GIF";
    public static final String TYPE_IMAGE = "TYPE_IMAGE";

    @Deprecated
    public static final String TYPE_IMAGE_ONLY = "TYPE_IMAGE_ONLY";
    public static final String TYPE_INVITE = "TYPE_INVITE";
    public static final String TYPE_LINK = "TYPE_LINK";
    public static final String TYPE_MINI_PROGRAM = "TYPE_MINI_PROGRAM";
    public static final String TYPE_MUSIC = "TYPE_MUSIC";
    public static final String TYPE_SUBSCRIBE = "TYPE_SUBSCRIBE";
    public static final String TYPE_TEXT_ONLY = "TYPE_TEXT_ONLY";
    public static final String TYPE_VIDEO = "TYPE_VIDEO";
    private int A;
    private String B;
    private String C;
    private String D;
    private String E;
    private String F;
    private String G;
    private String H;
    private String J;

    /* renamed from: a, reason: collision with root package name */
    private String f1794a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;
    private String i;
    private String j;
    private String r;
    private String u;
    private Bitmap v;
    private String w;
    private Bitmap x;
    private String y;
    private String z;
    private Map<String, String> k = new HashMap();
    private Map<String, String> l = new HashMap();
    private Map<String, String> m = new HashMap();
    private Map<String, String> n = new HashMap();
    private Map<String, String> o = new HashMap();
    private Map<String, String> p = new HashMap();
    private Map<String, String> q = new HashMap();
    private List<String> s = new ArrayList();
    private boolean t = false;
    private String I = null;

    public String getScope() {
        return this.f1794a;
    }

    public void setScope(String str) {
        this.f1794a = str;
    }

    public String getType() {
        return this.b;
    }

    public void setType(String str) {
        this.b = str;
    }

    public String getToUser() {
        return this.c;
    }

    public void setToUser(String str) {
        this.c = str;
    }

    public String getTitle() {
        return this.d;
    }

    public void setTitle(String str) {
        this.d = str;
    }

    public String getDesc() {
        return this.e;
    }

    public void setDesc(String str) {
        this.e = str;
    }

    public String getImage() {
        return this.f;
    }

    public void setImage(String str) {
        this.f = str;
    }

    public String getText() {
        return this.g;
    }

    public void setText(String str) {
        this.g = str;
    }

    public String getLink() {
        return this.h;
    }

    public void setLink(String str) {
        this.h = str;
    }

    public String getVideoUrl() {
        return this.y;
    }

    public void setVideoUrl(String str) {
        this.y = str;
    }

    public String getMusicUrl() {
        return this.z;
    }

    public void setMusicUrl(String str) {
        this.z = str;
    }

    public String getRoomId() {
        return this.B;
    }

    public void setRoomId(String str) {
        this.B = str;
    }

    public String getRoomToken() {
        return this.C;
    }

    public void setRoomToken(String str) {
        this.C = str;
    }

    public String getExtJson() {
        return this.D;
    }

    public void setExtJson(String str) {
        this.D = str;
    }

    public String getUserName() {
        return this.E;
    }

    public void setUserName(String str) {
        this.E = str;
    }

    public String getPath() {
        return this.F;
    }

    public void setPath(String str) {
        this.F = str;
    }

    public String getMiniProgramType() {
        return this.G;
    }

    public void setMiniProgramType(String str) {
        this.G = str;
    }

    public static ShareInfo jsonStr2Obj(String str) {
        ShareInfo shareInfo = new ShareInfo();
        if (TextUtils.isEmpty(str)) {
            return shareInfo;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            shareInfo.setScope(jSONObject.optString(Constants.PARAM_SCOPE));
            shareInfo.setType(jSONObject.optString("type"));
            shareInfo.setToUser(jSONObject.optString("toUser"));
            shareInfo.setTitle(jSONObject.optString("title"));
            shareInfo.setDesc(jSONObject.optString(SocialConstants.PARAM_APP_DESC));
            shareInfo.setImage(jSONObject.optString("image"));
            shareInfo.setShareChannel(jSONObject.optInt("shareChannel"));
            shareInfo.setImage(jSONObject.optString("image"));
            shareInfo.setText(jSONObject.optString("text"));
            shareInfo.setLink(jSONObject.optString("link"));
            shareInfo.setShareChannel(jSONObject.optInt("shareChannel"));
            ArrayList arrayList = new ArrayList();
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray("toUserList");
            if (jSONArrayOptJSONArray != null && jSONArrayOptJSONArray.length() > 0) {
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    arrayList.add(jSONArrayOptJSONArray.optString(i));
                }
            }
            shareInfo.setToUserList(arrayList);
            shareInfo.setVideoUrl(jSONObject.optString("videoUrl"));
            shareInfo.setShowShareDialog(jSONObject.optBoolean("showShareDialog"));
            String strOptString = jSONObject.optString("u3dshareThumb");
            if (!TextUtils.isEmpty(strOptString)) {
                shareInfo.setShareThumb(BitmapFactory.decodeFile(strOptString));
            }
            String strOptString2 = jSONObject.optString("u3dShareBitmap");
            if (!TextUtils.isEmpty(strOptString2)) {
                shareInfo.setShareBitmap(BitmapFactory.decodeFile(strOptString2));
            }
            shareInfo.setTemplateId(jSONObject.optString("templateId"));
            shareInfo.setMusicUrl(jSONObject.optString("musicUrl"));
            shareInfo.setRoomId(jSONObject.optString(com.netease.cc.ccplayerwrapper.Constants.KEY_ROOM_ID));
            shareInfo.setRoomToken(jSONObject.optString("roomToken"));
            shareInfo.setTag(jSONObject.optString(HeaderParameterNames.AUTHENTICATION_TAG));
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("extJson");
            if (jSONObjectOptJSONObject != null) {
                shareInfo.setExtJson(jSONObjectOptJSONObject.toString());
            }
            shareInfo.setMiniProgramType(jSONObject.optString("miniProgramType"));
            shareInfo.setUserName(jSONObject.optString("userName"));
            shareInfo.setPath(jSONObject.optString("path"));
            shareInfo.setMiniProgramID(jSONObject.optString("miniProgramID"));
            JSONObject jSONObjectOptJSONObject2 = jSONObject.optJSONObject("aLinkParams");
            if (jSONObjectOptJSONObject != null) {
                shareInfo.setALinkParams(StrUtil.jsonToStrMap(jSONObjectOptJSONObject2));
            }
            JSONObject jSONObjectOptJSONObject3 = jSONObject.optJSONObject("iLinkParams");
            if (jSONObjectOptJSONObject != null) {
                shareInfo.setILinkParams(StrUtil.jsonToStrMap(jSONObjectOptJSONObject3));
            }
            JSONObject jSONObjectOptJSONObject4 = jSONObject.optJSONObject("linkParams");
            if (jSONObjectOptJSONObject != null) {
                shareInfo.setLinkParams(StrUtil.jsonToStrMap(jSONObjectOptJSONObject4));
            }
        } catch (JSONException e) {
            UniSdkUtils.e("UniSDK ShareInfo", "jsonStr2Obj error");
            e.printStackTrace();
        } catch (Exception e2) {
            UniSdkUtils.e("UniSDK ShareInfo", "Exception:" + e2.getMessage());
            e2.printStackTrace();
        }
        return shareInfo;
    }

    public String getTemplateId() {
        return this.j;
    }

    public void setTemplateId(String str) {
        this.j = str;
    }

    public Map<String, String> getTextMsg() {
        return this.k;
    }

    public void setTextMsg(Map<String, String> map) {
        this.k = map;
    }

    public Map<String, String> getSubTextMsg() {
        return this.l;
    }

    public void setSubTextMsg(Map<String, String> map) {
        this.l = map;
    }

    public Map<String, String> getAltTextMsg() {
        return this.m;
    }

    public void setAltTextMsg(Map<String, String> map) {
        this.m = map;
    }

    public Map<String, String> getLinkTextMsg() {
        return this.n;
    }

    public void setLinkTextMsg(Map<String, String> map) {
        this.n = map;
    }

    public Map<String, String> getALinkParams() {
        return this.o;
    }

    public void setALinkParams(Map<String, String> map) {
        this.o = map;
    }

    public Map<String, String> getILinkParams() {
        return this.p;
    }

    public void setILinkParams(Map<String, String> map) {
        this.p = map;
    }

    public Map<String, String> getLinkParams() {
        return this.q;
    }

    public void setLinkParams(Map<String, String> map) {
        this.q = map;
    }

    public String getObjectValue() {
        return this.r;
    }

    public void setObjectValue(String str) {
        this.r = str;
    }

    public List<String> getToUserList() {
        return this.s;
    }

    public void setToUserList(List<String> list) {
        this.s = list;
    }

    public String getTimelineType() {
        return this.i;
    }

    public void setTimelineType(String str) {
        this.i = str;
    }

    public boolean isShowShareDialog() {
        return this.t;
    }

    public void setShowShareDialog(boolean z) {
        this.t = z;
    }

    public Bitmap getShareBitmap() {
        return this.x;
    }

    public void setShareBitmap(Bitmap bitmap) {
        this.x = bitmap;
    }

    public int getShareChannel() {
        return this.A;
    }

    public void setShareChannel(int i) {
        this.A = i;
    }

    public String getU3dshareThumb() {
        return this.u;
    }

    public void setU3dshareThumb(String str) {
        this.u = str;
    }

    public Bitmap getShareThumb() {
        return this.v;
    }

    public void setShareThumb(Bitmap bitmap) {
        this.v = bitmap;
    }

    public String getU3dShareBitmap() {
        return this.w;
    }

    public void setU3dShareBitmap(String str) {
        this.w = str;
    }

    public String getFailMsg() {
        return this.I;
    }

    public void setFailMsg(String str) {
        this.I = str;
    }

    public String getMiniProgramID() {
        return this.H;
    }

    public void setMiniProgramID(String str) {
        this.H = str;
    }

    public String getTag() {
        return this.J;
    }

    public void setTag(String str) {
        this.J = str;
    }

    public String toString() {
        return obj2JsonStr(this);
    }

    public static JSONObject obj2Json(ShareInfo shareInfo) throws JSONException {
        JSONObject jSONObject = new JSONObject();
        if (shareInfo != null) {
            try {
                jSONObject.put(Constants.PARAM_SCOPE, shareInfo.getScope());
                jSONObject.put("type", shareInfo.getType());
                jSONObject.put("toUser", shareInfo.getToUser());
                jSONObject.put("title", shareInfo.getTitle());
                jSONObject.put(SocialConstants.PARAM_APP_DESC, shareInfo.getDesc());
                jSONObject.put("image", shareInfo.getImage());
                jSONObject.put("text", shareInfo.getText());
                jSONObject.put("link", shareInfo.getLink());
                jSONObject.put("shareChannel", shareInfo.getShareChannel());
                jSONObject.put("u3dShareBitmap", shareInfo.getU3dShareBitmap());
                jSONObject.put("u3dshareThumb", shareInfo.getU3dshareThumb());
                jSONObject.put("showShareDialog", shareInfo.isShowShareDialog());
                jSONObject.put("videoUrl", shareInfo.getVideoUrl());
                jSONObject.put("templateId", shareInfo.getTemplateId());
                jSONObject.put("musicUrl", shareInfo.getMusicUrl());
                jSONObject.put(com.netease.cc.ccplayerwrapper.Constants.KEY_ROOM_ID, shareInfo.getRoomId());
                jSONObject.put("roomToken", shareInfo.getRoomToken());
                jSONObject.put(HeaderParameterNames.AUTHENTICATION_TAG, shareInfo.getTag());
                if (!TextUtils.isEmpty(shareInfo.getExtJson())) {
                    jSONObject.putOpt("extJson", new JSONObject(shareInfo.getExtJson()));
                }
                jSONObject.putOpt("aLinkParams", StrUtil.mapStrToJson(shareInfo.getALinkParams()));
                jSONObject.putOpt("iLinkParams", StrUtil.mapStrToJson(shareInfo.getILinkParams()));
                jSONObject.putOpt("linkParams", StrUtil.mapStrToJson(shareInfo.getLinkParams()));
                List<String> list = shareInfo.s;
                if (list != null && !list.isEmpty()) {
                    JSONArray jSONArray = new JSONArray();
                    Iterator<String> it = shareInfo.s.iterator();
                    while (it.hasNext()) {
                        jSONArray.put(it.next());
                    }
                    jSONObject.put("toUserList", jSONArray);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jSONObject;
    }

    public static String obj2JsonStr(ShareInfo shareInfo) {
        return obj2Json(shareInfo).toString();
    }
}