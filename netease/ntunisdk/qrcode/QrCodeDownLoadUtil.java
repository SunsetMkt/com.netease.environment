package com.netease.ntunisdk.qrcode;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.ShareInfo;
import com.netease.ntunisdk.base.UniSdkUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* loaded from: classes.dex */
public class QrCodeDownLoadUtil {
    private static final String TAG = "UniSDK qrcode";

    private static void downURLFile(final Context context, final String str, final String str2, final ShareInfo shareInfo, final String str3) {
        new Thread(new Runnable() { // from class: com.netease.ntunisdk.qrcode.QrCodeDownLoadUtil.1
            @Override // java.lang.Runnable
            public void run() throws IOException {
                try {
                    UniSdkUtils.d(QrCodeDownLoadUtil.TAG, "downURLFile tempUrl url\uff1a" + str);
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
                    UniSdkUtils.d(QrCodeDownLoadUtil.TAG, "save file path\uff1a" + str4);
                    if ("image".equals(str3)) {
                        shareInfo.setImage(str4);
                        QrCodeDownLoadUtil.webShare(context, shareInfo);
                        return;
                    }
                    if ("shareThumb".equals(str3)) {
                        shareInfo.setU3dshareThumb(str4);
                        shareInfo.setShareThumb(BitmapFactory.decodeFile(shareInfo.getU3dshareThumb()));
                        QrCodeDownLoadUtil.webShare(context, shareInfo);
                        return;
                    }
                    if ("shareBitmap".equals(str3)) {
                        shareInfo.setU3dShareBitmap(str4);
                        shareInfo.setShareThumb(BitmapFactory.decodeFile(shareInfo.getU3dShareBitmap()));
                        QrCodeDownLoadUtil.webShare(context, shareInfo);
                        return;
                    }
                    if ("video".equals(str3)) {
                        shareInfo.setVideoUrl(str4);
                        QrCodeDownLoadUtil.webShare(context, shareInfo);
                    }
                } catch (Exception e) {
                    UniSdkUtils.e(QrCodeDownLoadUtil.TAG, "downURLFile: " + e.getMessage());
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
}