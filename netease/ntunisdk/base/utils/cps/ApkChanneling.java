package com.netease.ntunisdk.base.utils.cps;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes5.dex */
public class ApkChanneling {
    private static final String LEGACY_PATTERN = "NETEASE";
    private static final String TAG = "ApkChanneling";
    private static final String V2_MAGIC_PREFIX = "APK Sig Block";

    /* JADX WARN: Removed duplicated region for block: B:18:0x006b  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x007d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getChannel(java.io.File r10) throws java.lang.Throwable {
        /*
            java.lang.String r0 = readZipComment(r10)
            java.io.RandomAccessFile r1 = new java.io.RandomAccessFile
            java.lang.String r2 = "r"
            r1.<init>(r10, r2)
            r2 = 22
            if (r0 == 0) goto L14
            int r3 = r0.length()
            int r2 = r2 + r3
        L14:
            com.netease.ntunisdk.base.utils.cps.ECDR r3 = new com.netease.ntunisdk.base.utils.cps.ECDR
            r3.<init>(r1, r2)
            long r4 = r3.offset()
            r6 = 16
            long r4 = r4 - r6
            r2 = 16
            byte[] r2 = new byte[r2]
            r1.seek(r4)
            r1.readFully(r2)
            java.lang.String r4 = new java.lang.String
            java.lang.String r5 = "UTF-8"
            r4.<init>(r2, r5)
            java.lang.String r2 = "APK Sig Block"
            boolean r2 = r4.startsWith(r2)
            r4 = 0
            java.lang.String r5 = "ApkChanneling"
            if (r2 == 0) goto L68
            long r6 = r3.offset()
            r8 = 24
            long r6 = r6 - r8
            r1.seek(r6)
            long r6 = com.netease.ntunisdk.base.utils.cps.a.a(r1)
            long r8 = r3.offset()
            long r8 = r8 - r6
            r6 = 8
            long r8 = r8 - r6
            com.netease.ntunisdk.base.utils.cps.SignatureBlock r3 = new com.netease.ntunisdk.base.utils.cps.SignatureBlock     // Catch: java.lang.Exception -> L5e
            r3.<init>(r1, r8)     // Catch: java.lang.Exception -> L5e
            boolean r2 = r3.checkV2()     // Catch: java.lang.Exception -> L5c
            goto L69
        L5c:
            r6 = move-exception
            goto L60
        L5e:
            r6 = move-exception
            r3 = r4
        L60:
            java.lang.String r6 = java.lang.String.valueOf(r6)
            com.netease.ntunisdk.base.UniSdkUtils.e(r5, r6)
            goto L69
        L68:
            r3 = r4
        L69:
            if (r2 == 0) goto L7d
            if (r3 == 0) goto L77
            java.lang.String r10 = "v2"
            com.netease.ntunisdk.base.UniSdkUtils.i(r5, r10)
            java.lang.String r4 = r3.getChannel()
            goto L86
        L77:
            java.lang.String r10 = "v2 but corrupted signature"
            com.netease.ntunisdk.base.UniSdkUtils.i(r5, r10)
            goto L86
        L7d:
            java.lang.String r2 = "v1"
            com.netease.ntunisdk.base.UniSdkUtils.i(r5, r2)
            java.lang.String r4 = getChannelForV1Legacy(r10, r0)
        L86:
            r1.close()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.cps.ApkChanneling.getChannel(java.io.File):java.lang.String");
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x0041  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0063 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0064 A[Catch: Exception -> 0x00ad, all -> 0x00af, TRY_LEAVE, TryCatch #2 {Exception -> 0x00ad, blocks: (B:17:0x0042, B:19:0x0048, B:21:0x004e, B:22:0x005d, B:25:0x0064), top: B:73:0x0042 }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:44:0x00a7 -> B:75:0x00d7). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    static java.lang.String getChannelForV1Legacy(java.io.File r8, java.lang.String r9) throws java.lang.Throwable {
        /*
            java.lang.String r0 = "NETEASE"
            r1 = 0
            boolean r2 = android.text.TextUtils.isEmpty(r9)     // Catch: java.lang.Throwable -> Laf java.lang.Exception -> Lc6
            java.lang.String r3 = "UTF-8"
            if (r2 != 0) goto L41
            boolean r2 = r9.contains(r0)     // Catch: java.lang.Throwable -> Laf java.lang.Exception -> Lc6
            if (r2 == 0) goto L41
            int r2 = r9.indexOf(r0)     // Catch: java.lang.Throwable -> Laf java.lang.Exception -> Lc6
            int r4 = r2 + (-2)
            int r0 = r9.indexOf(r0)     // Catch: java.lang.Throwable -> Laf java.lang.Exception -> Lc6
            int r0 = r0 + 7
            if (r4 <= 0) goto L41
            java.lang.String r2 = r9.substring(r4, r2)     // Catch: java.lang.Throwable -> Laf java.lang.Exception -> Lc6
            byte[] r2 = r2.getBytes(r3)     // Catch: java.lang.Throwable -> Laf java.lang.Exception -> Lc6
            r5 = 0
            r5 = r2[r5]     // Catch: java.lang.Throwable -> Laf java.lang.Exception -> Lc6
            r6 = 1
            r2 = r2[r6]     // Catch: java.lang.Throwable -> Laf java.lang.Exception -> Lc6
            short r2 = com.netease.ntunisdk.base.utils.cps.a.a(r5, r2)     // Catch: java.lang.Throwable -> Laf java.lang.Exception -> Lc6
            if (r2 < 0) goto L41
            int r0 = r0 + 2
            int r0 = r0 + (-128)
            int r5 = r0 + r2
            if (r5 >= r4) goto L41
            int r4 = r4 - r2
            java.lang.String r0 = r9.substring(r0, r4)     // Catch: java.lang.Throwable -> Laf java.lang.Exception -> Lc6
            goto L42
        L41:
            r0 = r1
        L42:
            boolean r2 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Exception -> Lad java.lang.Throwable -> Laf
            if (r2 == 0) goto L5d
            boolean r2 = android.text.TextUtils.isEmpty(r9)     // Catch: java.lang.Exception -> Lad java.lang.Throwable -> Laf
            if (r2 != 0) goto L5d
            java.lang.String r2 = "ApkChanneling"
            java.lang.String r4 = "invalid zip comment: "
            java.lang.String r9 = java.lang.String.valueOf(r9)     // Catch: java.lang.Exception -> Lad java.lang.Throwable -> Laf
            java.lang.String r9 = r4.concat(r9)     // Catch: java.lang.Exception -> Lad java.lang.Throwable -> Laf
            com.netease.ntunisdk.base.UniSdkUtils.i(r2, r9)     // Catch: java.lang.Exception -> Lad java.lang.Throwable -> Laf
        L5d:
            boolean r9 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Exception -> Lad java.lang.Throwable -> Laf
            if (r9 != 0) goto L64
            return r0
        L64:
            java.util.zip.ZipFile r9 = new java.util.zip.ZipFile     // Catch: java.lang.Exception -> Lad java.lang.Throwable -> Laf
            r9.<init>(r8)     // Catch: java.lang.Exception -> Lad java.lang.Throwable -> Laf
            java.lang.String r8 = "META-INF/appchannel"
            java.util.zip.ZipEntry r8 = r9.getEntry(r8)     // Catch: java.lang.Throwable -> Lab java.lang.Exception -> Lc8
            if (r8 == 0) goto L98
            long r4 = r8.getSize()     // Catch: java.lang.Throwable -> Lab java.lang.Exception -> Lc8
            r6 = 0
            int r2 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
            if (r2 >= 0) goto L98
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> Lab java.lang.Exception -> Lc8
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> Lab java.lang.Exception -> Lc8
            java.io.InputStream r8 = r9.getInputStream(r8)     // Catch: java.lang.Throwable -> Lab java.lang.Exception -> Lc8
            r4.<init>(r8, r3)     // Catch: java.lang.Throwable -> Lab java.lang.Exception -> Lc8
            r2.<init>(r4)     // Catch: java.lang.Throwable -> Lab java.lang.Exception -> Lc8
            java.lang.String r8 = r2.readLine()     // Catch: java.lang.Throwable -> L93 java.lang.Exception -> L96
            java.lang.String r0 = r8.trim()     // Catch: java.lang.Throwable -> L93 java.lang.Exception -> L96
            r1 = r2
            goto L98
        L93:
            r8 = move-exception
            r1 = r2
            goto Lb1
        L96:
            r1 = r2
            goto Lc8
        L98:
            if (r1 == 0) goto La2
            r1.close()     // Catch: java.io.IOException -> L9e
            goto La2
        L9e:
            r8 = move-exception
            r8.printStackTrace()
        La2:
            r9.close()     // Catch: java.io.IOException -> La6
            goto Ld7
        La6:
            r8 = move-exception
            r8.printStackTrace()
            goto Ld7
        Lab:
            r8 = move-exception
            goto Lb1
        Lad:
            r9 = r1
            goto Lc8
        Laf:
            r8 = move-exception
            r9 = r1
        Lb1:
            if (r1 == 0) goto Lbb
            r1.close()     // Catch: java.io.IOException -> Lb7
            goto Lbb
        Lb7:
            r0 = move-exception
            r0.printStackTrace()
        Lbb:
            if (r9 == 0) goto Lc5
            r9.close()     // Catch: java.io.IOException -> Lc1
            goto Lc5
        Lc1:
            r9 = move-exception
            r9.printStackTrace()
        Lc5:
            throw r8
        Lc6:
            r9 = r1
            r0 = r9
        Lc8:
            if (r1 == 0) goto Ld2
            r1.close()     // Catch: java.io.IOException -> Lce
            goto Ld2
        Lce:
            r8 = move-exception
            r8.printStackTrace()
        Ld2:
            if (r9 == 0) goto Ld7
            r9.close()     // Catch: java.io.IOException -> La6
        Ld7:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.base.utils.cps.ApkChanneling.getChannelForV1Legacy(java.io.File, java.lang.String):java.lang.String");
    }

    private static long getECDRHead(File file) throws IOException {
        boolean z;
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME);
        int i = 4;
        byte[] bArr = {80, 75, 5, 6};
        long length = file.length();
        int iMin = (int) Math.min(1024L, length);
        byte[] bArr2 = new byte[iMin];
        long j = iMin;
        long j2 = length - j;
        long j3 = -1;
        while (true) {
            randomAccessFile.seek(j2);
            randomAccessFile.readFully(bArr2);
            int i2 = 0;
            boolean z2 = false;
            while (i2 != iMin - 4) {
                int i3 = 0;
                while (true) {
                    if (i3 == i) {
                        z = true;
                        break;
                    }
                    if (bArr2[i2 + i3] != bArr[i3]) {
                        z = false;
                        break;
                    }
                    i3++;
                    i = 4;
                }
                if (z) {
                    j3 = i2 + j2;
                    z2 = true;
                }
                i2++;
                i = 4;
            }
            if (z2 || 0 == j2) {
                break;
            }
            j2 = (j2 - j) + 3;
            if (j2 < 0) {
                j2 = 0;
            }
            if (j2 < 0) {
                break;
            }
            i = 4;
        }
        randomAccessFile.close();
        return j3;
    }

    private static String readZipComment(File file) throws IOException {
        String str;
        long eCDRHead = getECDRHead(file) + 20;
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME);
        randomAccessFile.seek(eCDRHead);
        byte[] bArr = new byte[2];
        randomAccessFile.readFully(bArr);
        int iA = a.a(bArr[0], bArr[1]);
        if (iA > 0) {
            byte[] bArr2 = new byte[iA];
            randomAccessFile.seek(eCDRHead + 2);
            randomAccessFile.readFully(bArr2);
            str = new String(bArr2, "UTF-8");
        } else {
            str = null;
        }
        randomAccessFile.close();
        return str;
    }
}