package com.netease.ntunisdk.modules.ngwebviewgeneral.util;

import com.netease.ntunisdk.okhttp3.Call;
import com.netease.ntunisdk.okhttp3.Callback;
import com.netease.ntunisdk.okhttp3.OkHttpClient;
import com.netease.ntunisdk.okhttp3.Request;
import java.io.IOException;

/* loaded from: classes.dex */
public class DownloadUtil {

    public interface OnDownloadListener {
        void onDownloadFailed(String str);

        void onDownloadSuccess(String str);

        void onDownloading(int i);
    }

    public static void download(String str, final String str2, final OnDownloadListener onDownloadListener) {
        new OkHttpClient().newCall(new Request.Builder().url(str).build()).enqueue(new Callback() { // from class: com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil.1
            @Override // com.netease.ntunisdk.okhttp3.Callback
            public void onFailure(Call call, IOException iOException) {
                onDownloadListener.onDownloadFailed(iOException.getMessage());
            }

            /* JADX WARN: Removed duplicated region for block: B:60:0x008f A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:66:0x0099 A[EXC_TOP_SPLITTER, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:77:? A[SYNTHETIC] */
            /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:40:0x0087 -> B:57:0x008a). Please report as a decompilation issue!!! */
            @Override // com.netease.ntunisdk.okhttp3.Callback
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public void onResponse(com.netease.ntunisdk.okhttp3.Call r10, com.netease.ntunisdk.okhttp3.Response r11) throws java.lang.Throwable {
                /*
                    r9 = this;
                    r10 = 2048(0x800, float:2.87E-42)
                    byte[] r10 = new byte[r10]
                    r0 = 0
                    com.netease.ntunisdk.okhttp3.ResponseBody r1 = r11.body()     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L6b
                    java.io.InputStream r1 = r1.byteStream()     // Catch: java.lang.Throwable -> L67 java.lang.Exception -> L6b
                    com.netease.ntunisdk.okhttp3.ResponseBody r11 = r11.body()     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L63
                    long r2 = r11.contentLength()     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L63
                    java.io.File r11 = new java.io.File     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L63
                    java.lang.String r4 = r2     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L63
                    r11.<init>(r4)     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L63
                    java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L63
                    r4.<init>(r11)     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L63
                    r5 = 0
                L23:
                    int r0 = r1.read(r10)     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    r7 = -1
                    if (r0 == r7) goto L42
                    r7 = 0
                    r4.write(r10, r7, r0)     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    long r7 = (long) r0     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    long r5 = r5 + r7
                    float r0 = (float) r5     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    r7 = 1065353216(0x3f800000, float:1.0)
                    float r0 = r0 * r7
                    float r7 = (float) r2     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    float r0 = r0 / r7
                    r7 = 1120403456(0x42c80000, float:100.0)
                    float r0 = r0 * r7
                    int r0 = (int) r0     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil$OnDownloadListener r7 = r1     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    r7.onDownloading(r0)     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    goto L23
                L42:
                    r4.flush()     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil$OnDownloadListener r10 = r1     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    java.lang.String r11 = r11.getAbsolutePath()     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    r10.onDownloadSuccess(r11)     // Catch: java.lang.Throwable -> L5c java.lang.Exception -> L5e
                    if (r1 == 0) goto L58
                    r1.close()     // Catch: java.io.IOException -> L54
                    goto L58
                L54:
                    r10 = move-exception
                    r10.printStackTrace()
                L58:
                    r4.close()     // Catch: java.io.IOException -> L86
                    goto L8a
                L5c:
                    r10 = move-exception
                    goto L8d
                L5e:
                    r10 = move-exception
                    goto L65
                L60:
                    r10 = move-exception
                    r4 = r0
                    goto L8d
                L63:
                    r10 = move-exception
                    r4 = r0
                L65:
                    r0 = r1
                    goto L6d
                L67:
                    r10 = move-exception
                    r1 = r0
                    r4 = r1
                    goto L8d
                L6b:
                    r10 = move-exception
                    r4 = r0
                L6d:
                    com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil$OnDownloadListener r11 = r1     // Catch: java.lang.Throwable -> L8b
                    java.lang.String r10 = r10.getMessage()     // Catch: java.lang.Throwable -> L8b
                    r11.onDownloadFailed(r10)     // Catch: java.lang.Throwable -> L8b
                    if (r0 == 0) goto L80
                    r0.close()     // Catch: java.io.IOException -> L7c
                    goto L80
                L7c:
                    r10 = move-exception
                    r10.printStackTrace()
                L80:
                    if (r4 == 0) goto L8a
                    r4.close()     // Catch: java.io.IOException -> L86
                    goto L8a
                L86:
                    r10 = move-exception
                    r10.printStackTrace()
                L8a:
                    return
                L8b:
                    r10 = move-exception
                    r1 = r0
                L8d:
                    if (r1 == 0) goto L97
                    r1.close()     // Catch: java.io.IOException -> L93
                    goto L97
                L93:
                    r11 = move-exception
                    r11.printStackTrace()
                L97:
                    if (r4 == 0) goto La1
                    r4.close()     // Catch: java.io.IOException -> L9d
                    goto La1
                L9d:
                    r11 = move-exception
                    r11.printStackTrace()
                La1:
                    goto La3
                La2:
                    throw r10
                La3:
                    goto La2
                */
                throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.modules.ngwebviewgeneral.util.DownloadUtil.AnonymousClass1.onResponse(com.netease.ntunisdk.okhttp3.Call, com.netease.ntunisdk.okhttp3.Response):void");
            }
        });
    }
}