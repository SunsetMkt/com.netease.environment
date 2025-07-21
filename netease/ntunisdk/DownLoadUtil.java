package com.netease.ntunisdk;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.ShareInfo;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.base.utils.StrUtil;
import com.tencent.connect.common.Constants;
import com.tencent.open.SocialConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class DownLoadUtil {
    private static final String TAG = "UniSDK ngshare";

    private static void downURLFile(final Context context, final String str, final String str2, final ShareInfo shareInfo, final String str3) {
        new Thread(new Runnable() { // from class: com.netease.ntunisdk.DownLoadUtil.1
            @Override // java.lang.Runnable
            public void run() throws IOException {
                try {
                    UniSdkUtils.d(DownLoadUtil.TAG, "downURLFile tempUrl url\uff1a" + str);
                    if (TextUtils.isEmpty(str)) {
                        return;
                    }
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
                    httpURLConnection.setConnectTimeout(6000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    String str4 = context.getExternalFilesDir(null) + str2;
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(str4));
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int i = inputStream.read(bArr);
                        if (i <= 0) {
                            break;
                        } else {
                            fileOutputStream.write(bArr, 0, i);
                        }
                    }
                    fileOutputStream.close();
                    inputStream.close();
                    UniSdkUtils.d(DownLoadUtil.TAG, "save file path\uff1a" + str4);
                    if ("image".equals(str3)) {
                        shareInfo.setImage(str4);
                        DownLoadUtil.webShare(context, shareInfo);
                        return;
                    }
                    if ("shareThumb".equals(str3)) {
                        shareInfo.setU3dshareThumb(str4);
                        shareInfo.setShareThumb(BitmapFactory.decodeFile(shareInfo.getU3dshareThumb()));
                        DownLoadUtil.webShare(context, shareInfo);
                        return;
                    }
                    if ("shareBitmap".equals(str3)) {
                        shareInfo.setU3dShareBitmap(str4);
                        shareInfo.setShareThumb(BitmapFactory.decodeFile(shareInfo.getU3dShareBitmap()));
                        DownLoadUtil.webShare(context, shareInfo);
                        return;
                    }
                    if ("video".equals(str3)) {
                        shareInfo.setVideoUrl(str4);
                        DownLoadUtil.webShare(context, shareInfo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void webShare(Context context, ShareInfo shareInfo) {
        if (shareInfo.getImage() != null && shareInfo.getImage().startsWith("http")) {
            downURLFile(context, shareInfo.getImage(), "/tempShareFile.png", shareInfo, "image");
            return;
        }
        if (shareInfo.getU3dshareThumb() != null && shareInfo.getU3dshareThumb().startsWith("http")) {
            downURLFile(context, shareInfo.getU3dshareThumb(), "/tempShareThumbFile.png", shareInfo, "shareThumb");
            return;
        }
        if (shareInfo.getU3dShareBitmap() != null && shareInfo.getU3dShareBitmap().startsWith("http")) {
            downURLFile(context, shareInfo.getU3dShareBitmap(), "/tempShareBitmapFile.png", shareInfo, "shareBitmap");
            return;
        }
        if (shareInfo.getVideoUrl() != null && shareInfo.getVideoUrl().startsWith("http")) {
            if (301 == shareInfo.getShareChannel() || 101 == shareInfo.getShareChannel() || 102 == shareInfo.getShareChannel() || 118 == shareInfo.getShareChannel()) {
                SdkMgr.getInst().ntShare(shareInfo);
                return;
            } else {
                downURLFile(context, shareInfo.getVideoUrl(), "/tempVideoFile.mp4", shareInfo, "video");
                return;
            }
        }
        SdkMgr.getInst().ntShare(shareInfo);
    }

    public static boolean hasWebUrl(ShareInfo shareInfo) {
        if (shareInfo.getImage() != null && shareInfo.getImage().startsWith("http")) {
            return true;
        }
        if (shareInfo.getU3dshareThumb() != null && shareInfo.getU3dshareThumb().startsWith("http")) {
            return true;
        }
        if (shareInfo.getU3dShareBitmap() == null || !shareInfo.getU3dShareBitmap().startsWith("http")) {
            return (shareInfo.getVideoUrl() == null || !shareInfo.getVideoUrl().startsWith("http") || 301 == shareInfo.getShareChannel() || 101 == shareInfo.getShareChannel() || 102 == shareInfo.getShareChannel() || 118 == shareInfo.getShareChannel()) ? false : true;
        }
        return true;
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
            if (!TextUtils.isEmpty(strOptString) && !strOptString.startsWith("http")) {
                shareInfo.setShareThumb(BitmapFactory.decodeFile(strOptString));
            }
            shareInfo.setU3dshareThumb(strOptString);
            String strOptString2 = jSONObject.optString("u3dShareBitmap");
            if (!TextUtils.isEmpty(strOptString2) && !strOptString2.startsWith("http")) {
                shareInfo.setShareBitmap(BitmapFactory.decodeFile(strOptString2));
            }
            shareInfo.setU3dShareBitmap(strOptString2);
            shareInfo.setTemplateId(jSONObject.optString("templateId"));
            shareInfo.setMusicUrl(jSONObject.optString("musicUrl"));
            shareInfo.setRoomId(jSONObject.optString(com.netease.cc.ccplayerwrapper.Constants.KEY_ROOM_ID));
            shareInfo.setRoomToken(jSONObject.optString("roomToken"));
            shareInfo.setMiniProgramType(jSONObject.optString("miniProgramType"));
            shareInfo.setUserName(jSONObject.optString("userName"));
            shareInfo.setPath(jSONObject.optString("path"));
            shareInfo.setMiniProgramID(jSONObject.optString("miniProgramID"));
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("extJson");
            if (jSONObjectOptJSONObject != null) {
                shareInfo.setExtJson(jSONObjectOptJSONObject.toString());
            }
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
            UniSdkUtils.e(TAG, "jsonStr2Obj error");
            e.printStackTrace();
        } catch (Exception e2) {
            UniSdkUtils.e(TAG, "Exception:" + e2.getMessage());
            e2.printStackTrace();
        }
        return shareInfo;
    }
}