package com.netease.ntunisdk.base.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.netease.ntunisdk.base.ShareInfo;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.constant.a;
import com.tencent.open.SocialConstants;
import java.io.File;
import org.jose4j.jwx.HeaderParameterNames;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class SdkShare extends SdkState {
    private static final String TAG = "SdkShare";
    private static final BitmapFactory.Options options;
    public String desc;
    public String image;
    public String link;
    public String miniprogramid;
    public int miniprogramtype;
    public String path;
    public int sharechannel;
    public String tag;
    public String text;
    public String thumbimagepath;
    public String title;
    public String type;
    public String username;
    public String video;

    static {
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options = options2;
        options2.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public SdkShare() {
    }

    public SdkShare(String str) {
        super(str);
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public void fromJson(JSONObject jSONObject) {
        super.fromJson(jSONObject);
        this.type = jSONObject.optString("type");
        this.text = jSONObject.optString("text");
        this.sharechannel = jSONObject.optInt("sharechannel");
        this.image = jSONObject.optString("image");
        this.video = jSONObject.optString("video");
        this.title = jSONObject.optString("title");
        this.link = jSONObject.optString("link");
        this.thumbimagepath = jSONObject.optString("thumbimagepath");
        this.desc = jSONObject.optString(SocialConstants.PARAM_APP_DESC);
        this.tag = jSONObject.optString(HeaderParameterNames.AUTHENTICATION_TAG);
        this.username = jSONObject.optString("username");
        this.path = jSONObject.optString("path");
        this.miniprogramtype = jSONObject.optInt("miniprogramtype");
        this.miniprogramid = jSONObject.optString("miniprogramid");
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public JSONObject toJson() throws JSONException {
        JSONObject json = super.toJson();
        try {
            json.putOpt("type", this.type);
            json.putOpt("text", this.text);
            int i = this.sharechannel;
            if (i > 0) {
                json.putOpt("sharechannel", Integer.valueOf(i));
            }
            json.putOpt("image", this.image);
            json.putOpt("video", this.video);
            json.putOpt("title", this.title);
            json.putOpt("link", this.link);
            json.putOpt("thumbimagepath", this.thumbimagepath);
            json.putOpt(SocialConstants.PARAM_APP_DESC, this.desc);
            json.putOpt(HeaderParameterNames.AUTHENTICATION_TAG, this.tag);
            json.putOpt("username", this.username);
            json.putOpt("path", this.path);
            json.putOpt("miniprogramtype", Integer.valueOf(this.miniprogramtype));
            json.putOpt("miniprogramid", this.miniprogramid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public Object wrapTo() {
        ShareInfo shareInfo = new ShareInfo();
        shareInfo.setShareChannel(this.sharechannel);
        shareInfo.setType(this.type);
        shareInfo.setTitle(this.title);
        shareInfo.setText(this.text);
        shareInfo.setDesc(this.desc);
        shareInfo.setTag(this.tag);
        if ("TYPE_IMAGE".equals(this.type)) {
            shareInfo.setImage(this.image);
            setShareThumb(shareInfo);
        } else if ("TYPE_LINK".equals(this.type)) {
            shareInfo.setLink(this.link);
            int i = this.sharechannel;
            if (105 == i || 106 == i) {
                if (!TextUtils.isEmpty(this.image)) {
                    shareInfo.setImage(this.image);
                } else if (!TextUtils.isEmpty(this.thumbimagepath)) {
                    shareInfo.setImage(this.thumbimagepath);
                }
            } else {
                setShareThumb(shareInfo);
            }
        } else if ("TYPE_VIDEO".equals(this.type)) {
            shareInfo.setVideoUrl(this.video);
            setShareThumb(shareInfo);
        }
        int i2 = this.sharechannel;
        if (i2 != 101) {
            if (i2 == 301) {
                if (ShareInfo.TYPE_SUBSCRIBE.equals(this.type)) {
                    shareInfo.setMiniProgramID(this.miniprogramid);
                } else {
                    shareInfo.setUserName(this.username);
                    shareInfo.setPath(this.path);
                    int i3 = this.miniprogramtype;
                    if (i3 < 0 || i3 > 2) {
                        UniSdkUtils.i(TAG, "miniprogramtype error, sdk will use 0 (for release) instead");
                        i3 = 0;
                    }
                    shareInfo.setMiniProgramType(String.valueOf(i3));
                }
            }
        } else if ("TYPE_MINI_PROGRAM".equals(this.type)) {
            shareInfo.setLink(this.link);
            shareInfo.setText(this.username);
            shareInfo.setDesc(this.path);
            setShareThumb(shareInfo);
        }
        return shareInfo;
    }

    @Override // com.netease.ntunisdk.base.model.SdkState, com.netease.ntunisdk.base.model.SdkModel
    public void wrapFrom(Object obj) {
        super.wrapFrom(obj);
        if (obj instanceof Boolean) {
            Boolean bool = (Boolean) obj;
            this.code = (bool.booleanValue() ? a.Suc : a.Fail).ordinal();
            this.message = (bool.booleanValue() ? a.Suc : a.Fail).d;
        }
    }

    private void setShareThumb(ShareInfo shareInfo) {
        String str = this.thumbimagepath;
        if (TextUtils.isEmpty(str)) {
            str = this.image;
        }
        try {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            File file = new File(str);
            if (!file.exists() || 0 >= file.length()) {
                return;
            }
            int i = 1;
            while (i * i < file.length() / 32768.0d) {
                i *= 2;
            }
            BitmapFactory.Options options2 = options;
            options2.inSampleSize = i;
            shareInfo.setShareThumb(BitmapFactory.decodeFile(str, options2));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}