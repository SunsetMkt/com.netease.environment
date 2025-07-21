package com.netease.cc.ccplayerwrapper.b;

import com.netease.cc.ccplayerwrapper.Constants;
import com.netease.cc.ccplayerwrapper.UserInfo;
import com.netease.cc.ccplayerwrapper.VideoConfig;
import com.netease.cc.ccplayerwrapper.utils.LogUtil;
import com.xiaomi.onetrack.OneTrack;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.HttpCallback;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.JitterBufferSetting;
import tv.danmaku.ijk.media.player.PlayerConfig;
import tv.danmaku.ijk.media.player.tools.PlayerHelper;

/* compiled from: PlayUrlHelper.java */
/* loaded from: classes5.dex */
public class b {
    private com.netease.cc.ccplayerwrapper.b.a b;
    private com.netease.cc.ccplayerwrapper.a.b d;
    private int c = 3;

    /* renamed from: a, reason: collision with root package name */
    private PlayerConfig f1535a = new PlayerConfig();

    /* compiled from: PlayUrlHelper.java */
    class a implements HttpCallback {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ String f1536a;

        a(String str) {
            this.f1536a = str;
        }

        @Override // tv.danmaku.ijk.media.player.HttpCallback
        public void callback(int i, String str) {
            StringBuilder sb = new StringBuilder("getVideoUrlResponse code:");
            sb.append(str);
            sb.append(" videoUrl:");
            sb.append(b.this.f1535a != null ? b.this.f1535a.videoUrl : "null");
            sb.append(" cdn_sel:");
            sb.append(b.this.d.j);
            sb.append(" vbr_sel:");
            sb.append(b.this.d.g);
            sb.append(" reqNum:");
            sb.append(b.this.c);
            LogUtil.LOGD("PlayUrlHelper", sb.toString());
            if (b.this.b != null) {
                b.this.b.a(i, str, this.f1536a);
            }
        }
    }

    public b(com.netease.cc.ccplayerwrapper.b.a aVar, com.netease.cc.ccplayerwrapper.a.b bVar) {
        this.d = bVar;
        this.b = aVar;
    }

    public final JSONObject e() throws JSONException {
        JSONObject jSONObject;
        JSONException e;
        try {
        } catch (JSONException e2) {
            jSONObject = null;
            e = e2;
        }
        if (this.d == null) {
            return null;
        }
        jSONObject = new JSONObject();
        try {
            JSONArray jSONArray = new JSONArray();
            Iterator<String> it = this.d.k.iterator();
            while (it.hasNext()) {
                jSONArray.put(it.next());
            }
            jSONObject.put(Constants.KEY_VBR_LIST, jSONArray);
            jSONObject.put(Constants.KEY_VBR_SEL, this.d.g);
            jSONObject.put(Constants.KEY_VBR_SEL_VALUE, this.d.h);
            jSONObject.put(Constants.KEY_VBR_SEL_CHOOSE, this.d.i);
            jSONObject.put(Constants.KEY_VBRNAME_LIST, jSONArray);
            jSONObject.put(Constants.KEY_VBRNAME_SEL, this.d.g);
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            return jSONObject;
        }
        return jSONObject;
    }

    private void b(String str) {
        PlayerHelper.httpGet(str, new a(str));
    }

    public void a(com.netease.cc.ccplayerwrapper.a.b bVar) {
        a(bVar.b.getCdn(), bVar.b.getVbr(), true);
    }

    public void c(String str) {
        boolean z;
        if (str == null || str.isEmpty() || this.d.m == null) {
            com.netease.cc.ccplayerwrapper.a.b bVar = this.d;
            bVar.m.remove(bVar.j);
            if (this.d.m.isEmpty()) {
                LogUtil.LOGE("PlayUrlHelper", " switch cdn for no candidate, cdnSel:" + this.d.j);
                return;
            } else {
                z = false;
                str = this.d.m.get(0);
                this.d.m.remove(str);
            }
        } else {
            z = true;
        }
        a(str, this.d.g, z);
    }

    public PlayerConfig d() {
        return this.f1535a;
    }

    private void b() {
        com.netease.cc.ccplayerwrapper.a.b bVar = this.d;
        if (bVar != null) {
            bVar.m = null;
        }
    }

    public void a(com.netease.cc.ccplayerwrapper.a.b bVar, String str) {
        if (bVar != null) {
            bVar.b.setVbr(str);
            a(bVar.b.getCdn(), str, true);
        }
    }

    private void a(String str, String str2, boolean z) {
        this.c = 3;
        if (z) {
            b();
        }
        this.d.f = a(str, str2);
        b(this.d.f);
    }

    public void a(int i, String str) {
        int i2;
        PlayerConfig playerConfig;
        if (i != 200) {
            if (i != 410 && (i2 = this.c) > 0) {
                this.c = i2 - 1;
                b(this.d.f);
                return;
            } else {
                this.b.a(Constants.MEDIA_ERROR_CCPLAYER - i, "");
                return;
            }
        }
        String strA = a(str);
        if (!strA.isEmpty() && (playerConfig = this.f1535a) != null) {
            this.d.j = playerConfig.cdn;
            this.b.a(i, strA);
            this.b.a(e());
            return;
        }
        this.b.a(Constants.MEDIA_ERROR_LOGIC, "");
    }

    public final JSONObject c() throws JSONException {
        JSONObject jSONObject;
        JSONException e;
        try {
        } catch (JSONException e2) {
            jSONObject = null;
            e = e2;
        }
        if (this.d == null) {
            return null;
        }
        jSONObject = new JSONObject();
        try {
            JSONArray jSONArray = new JSONArray();
            Iterator<String> it = this.d.l.iterator();
            while (it.hasNext()) {
                jSONArray.put(it.next());
            }
            jSONObject.put(Constants.KEY_CDN_LIST, jSONArray);
            jSONObject.put(Constants.KEY_CDN_SEL, this.d.j);
        } catch (JSONException e3) {
            e = e3;
            e.printStackTrace();
            return jSONObject;
        }
        return jSONObject;
    }

    public boolean a() {
        com.netease.cc.ccplayerwrapper.a.b bVar = this.d;
        if (bVar.m != null && !bVar.j.isEmpty()) {
            com.netease.cc.ccplayerwrapper.a.b bVar2 = this.d;
            bVar2.m.remove(bVar2.j);
            if (!this.d.m.isEmpty()) {
                return true;
            }
            LogUtil.LOGE("PlayUrlHelper", " canSwitchCdn cdn for no candidate, cdnSel:" + this.d.j);
            return false;
        }
        LogUtil.LOGE("PlayUrlHelper", " canSwitchCdn cdn fail user:" + this.d + " playconfig:" + this.f1535a + " cdnCandidate:" + this.d.m + " cdnSel:" + this.d.j);
        return false;
    }

    private String a(String str, String str2) {
        VideoConfig videoConfig;
        com.netease.cc.ccplayerwrapper.a.b bVar = this.d;
        if (bVar == null || (videoConfig = bVar.b) == null || videoConfig.getUserInfo() == null) {
            return "";
        }
        VideoConfig videoConfig2 = this.d.b;
        UserInfo userInfo = videoConfig2.getUserInfo();
        StringBuilder sb = new StringBuilder();
        sb.append(this.d.b.getMobileurl());
        sb.append("?vbrmode=1&src=");
        sb.append(videoConfig2.getSrc());
        if (videoConfig2.getCoPlatform() != null && !videoConfig2.getCoPlatform().isEmpty()) {
            sb.append("&coplatform=");
            sb.append(this.d.b.getCoPlatform());
        }
        String str3 = userInfo.urs;
        if (str3 != null && !str3.isEmpty()) {
            sb.append("&urs=");
            sb.append(userInfo.urs);
        }
        if (userInfo.ccid > 0) {
            sb.append("&udp_identy=udp&uid=");
            sb.append(userInfo.ccid);
        }
        if (str != null && !str.isEmpty()) {
            sb.append("&cdn=");
            sb.append(str);
        }
        if (str2 != null && !str2.isEmpty()) {
            sb.append("&vbrname=");
            sb.append(str2);
        }
        if (this.d.b.getVbrValue() > 0) {
            sb.append("&vbr=");
            sb.append(this.d.b.getVbrValue());
        }
        if (this.d.b.getVbrChooseByUser() == 1) {
            sb.append("&vbr_choose_by_user=1");
        }
        sb.append("&client_type=android&version=");
        sb.append(IjkMediaPlayer.getVersion());
        return sb.toString();
    }

    private boolean a(JSONObject jSONObject, JitterBufferSetting jitterBufferSetting) {
        if (jSONObject != null && jitterBufferSetting != null) {
            try {
                jitterBufferSetting.fwdnew = jSONObject.optBoolean("fwdnew");
                jitterBufferSetting.canfwd = jSONObject.optBoolean("canfwd");
                jitterBufferSetting.firstjitter = jSONObject.optInt("firstjitter");
                jitterBufferSetting.minjitter = jSONObject.optInt("minjitter");
                jitterBufferSetting.maxjitter = jSONObject.optInt("maxjitter");
                jitterBufferSetting.buffertime = jSONObject.optInt("buffertime");
                jitterBufferSetting.fwdexttime = Float.parseFloat(jSONObject.optString("fwdexttime"));
                return true;
            } catch (Exception unused) {
                LogUtil.LOGE("PlayUrlHelper", "parse the mediaplayer control-parameters error.");
            }
        }
        return false;
    }

    private String a(String str) {
        boolean z;
        String str2 = "";
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.length() <= 0) {
                return "";
            }
            JSONArray jSONArrayOptJSONArray = jSONObject.optJSONArray(Constants.KEY_VBRNAME_LIST);
            if (jSONArrayOptJSONArray != null) {
                this.d.k.clear();
                for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                    this.d.k.add(jSONArrayOptJSONArray.optString(i, ""));
                }
            }
            JSONArray jSONArrayOptJSONArray2 = jSONObject.optJSONArray("cdn_list");
            if (jSONArrayOptJSONArray2 == null || this.d.m != null) {
                z = false;
            } else {
                this.d.m = new ArrayList();
                z = true;
            }
            this.d.l.clear();
            for (int i2 = 0; i2 < jSONArrayOptJSONArray2.length(); i2++) {
                String strOptString = jSONArrayOptJSONArray2.optString(i2, "");
                this.d.l.add(strOptString);
                if (z) {
                    this.d.m.add(strOptString);
                }
            }
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("mplayersetting");
            if (jSONObjectOptJSONObject != null) {
                a(jSONObjectOptJSONObject.optJSONObject(com.facebook.hermes.intl.Constants.COLLATION_DEFAULT), this.f1535a.wifiSetting);
                if (!a(jSONObjectOptJSONObject.optJSONObject("cell"), this.f1535a.cellSetting)) {
                    PlayerConfig playerConfig = this.f1535a;
                    playerConfig.cellSetting = playerConfig.wifiSetting;
                }
                if (jSONObjectOptJSONObject.optInt(Constants.KEY_CCID, 0) != 0) {
                    this.f1535a.anchorCCid = jSONObjectOptJSONObject.optInt(Constants.KEY_CCID, 0);
                }
            }
            this.d.g = jSONObject.optString(Constants.KEY_VBRNAME_SEL, "");
            this.d.h = jSONObject.optInt("vbr_sel", 0);
            this.d.i = jSONObject.optInt("vbr_choose_by_user", 0);
            String strOptString2 = jSONObject.optString("videourl", "");
            StringBuilder sb = new StringBuilder();
            sb.append(strOptString2);
            if (!strOptString2.isEmpty()) {
                sb.append("&src=");
                sb.append(this.d.b.getSrc());
                sb.append("&ccid=");
                sb.append(this.d.b.getAnchorCCid());
            }
            String string = sb.toString();
            try {
                int iIndexOf = string.indexOf("?");
                if (iIndexOf < 0) {
                    iIndexOf = string.length();
                }
                this.f1535a.videoUrl = string.substring(0, iIndexOf);
                this.f1535a.cdn = jSONObject.optString("cdn_sel", "");
                this.d.j = this.f1535a.cdn;
                if (jSONObject.optInt("transformer_id", -1) != -1) {
                    this.f1535a.templateType = jSONObject.optInt("transformer_id", -1);
                }
                if (jSONObject.optInt(Constants.KEY_GAME_TYPE, -1) != -1) {
                    this.f1535a.gametype = jSONObject.optInt(Constants.KEY_GAME_TYPE, -1);
                }
                if (jSONObject.optInt(Constants.KEY_ROOM_ID, -1) != -1) {
                    this.f1535a.roomId = jSONObject.optInt(Constants.KEY_ROOM_ID, -1);
                }
                if (jSONObject.optInt(Constants.KEY_CHANNEL_ID, -1) != -1) {
                    this.f1535a.subId = jSONObject.optInt(Constants.KEY_CHANNEL_ID, -1);
                }
                if (jSONObject.optInt(OneTrack.Param.ANCHOR_UID, -1) != -1) {
                    this.f1535a.anchorUid = jSONObject.optInt(OneTrack.Param.ANCHOR_UID, -2);
                }
                return string;
            } catch (JSONException e) {
                str2 = string;
                e = e;
                e.printStackTrace();
                return str2;
            }
        } catch (JSONException e2) {
            e = e2;
        }
    }
}