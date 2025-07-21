package com.netease.environment.OIIO0OI;

import android.content.Context;
import com.netease.environment.EnvManager;
import com.netease.environment.OIIO0I.OIIO0OO;
import com.netease.environment.OIIO0II.OIIO;
import com.netease.environment.OIIO0II.OIIO0I0;
import com.netease.environment.OIIO0II.OIIO0II;
import com.netease.environment.OIIO0OO.OIIO0OI;
import com.netease.environment.regex.Pattern;
import com.xiaomi.onetrack.OneTrack;
import java.util.concurrent.Executors;
import org.json.JSONObject;

/* compiled from: DownloadUtils.java */
/* loaded from: classes5.dex */
public class OIIO00I {

    /* renamed from: OIIO00I, reason: collision with root package name */
    private static final String f1569OIIO00I = "OIIO00I";

    /* compiled from: DownloadUtils.java */
    /* renamed from: com.netease.environment.OIIO0OI.OIIO00I$OIIO00I, reason: collision with other inner class name */
    class RunnableC0063OIIO00I implements Runnable {

        /* renamed from: OIIO00I, reason: collision with root package name */
        final /* synthetic */ com.netease.environment.OIIO0I0.OIIO00I f1570OIIO00I;
        final /* synthetic */ String OIIO0O0;
        final /* synthetic */ String OIIO0OI;
        final /* synthetic */ String OIIO0OO;

        RunnableC0063OIIO00I(com.netease.environment.OIIO0I0.OIIO00I oiio00i, String str, String str2, String str3) {
            this.f1570OIIO00I = oiio00i;
            this.OIIO0O0 = str;
            this.OIIO0OO = str2;
            this.OIIO0OI = str3;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:129:0x0153 A[Catch: all -> 0x024b, TRY_ENTER, TryCatch #16 {all -> 0x024b, blocks: (B:129:0x0153, B:131:0x0159, B:132:0x015c, B:134:0x016c, B:162:0x01a8, B:164:0x01ae, B:165:0x01b1, B:167:0x01c1, B:195:0x01fc, B:197:0x0202, B:198:0x0205, B:200:0x0215), top: B:269:0x000b }] */
        /* JADX WARN: Removed duplicated region for block: B:134:0x016c A[Catch: all -> 0x024b, TRY_LEAVE, TryCatch #16 {all -> 0x024b, blocks: (B:129:0x0153, B:131:0x0159, B:132:0x015c, B:134:0x016c, B:162:0x01a8, B:164:0x01ae, B:165:0x01b1, B:167:0x01c1, B:195:0x01fc, B:197:0x0202, B:198:0x0205, B:200:0x0215), top: B:269:0x000b }] */
        /* JADX WARN: Removed duplicated region for block: B:162:0x01a8 A[Catch: all -> 0x024b, TRY_ENTER, TryCatch #16 {all -> 0x024b, blocks: (B:129:0x0153, B:131:0x0159, B:132:0x015c, B:134:0x016c, B:162:0x01a8, B:164:0x01ae, B:165:0x01b1, B:167:0x01c1, B:195:0x01fc, B:197:0x0202, B:198:0x0205, B:200:0x0215), top: B:269:0x000b }] */
        /* JADX WARN: Removed duplicated region for block: B:167:0x01c1 A[Catch: all -> 0x024b, TRY_LEAVE, TryCatch #16 {all -> 0x024b, blocks: (B:129:0x0153, B:131:0x0159, B:132:0x015c, B:134:0x016c, B:162:0x01a8, B:164:0x01ae, B:165:0x01b1, B:167:0x01c1, B:195:0x01fc, B:197:0x0202, B:198:0x0205, B:200:0x0215), top: B:269:0x000b }] */
        /* JADX WARN: Removed duplicated region for block: B:195:0x01fc A[Catch: all -> 0x024b, TRY_ENTER, TryCatch #16 {all -> 0x024b, blocks: (B:129:0x0153, B:131:0x0159, B:132:0x015c, B:134:0x016c, B:162:0x01a8, B:164:0x01ae, B:165:0x01b1, B:167:0x01c1, B:195:0x01fc, B:197:0x0202, B:198:0x0205, B:200:0x0215), top: B:269:0x000b }] */
        /* JADX WARN: Removed duplicated region for block: B:200:0x0215 A[Catch: all -> 0x024b, TRY_LEAVE, TryCatch #16 {all -> 0x024b, blocks: (B:129:0x0153, B:131:0x0159, B:132:0x015c, B:134:0x016c, B:162:0x01a8, B:164:0x01ae, B:165:0x01b1, B:167:0x01c1, B:195:0x01fc, B:197:0x0202, B:198:0x0205, B:200:0x0215), top: B:269:0x000b }] */
        /* JADX WARN: Removed duplicated region for block: B:247:0x027e  */
        /* JADX WARN: Removed duplicated region for block: B:249:0x0273 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:253:0x0172 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:257:0x01e8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:259:0x0268 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:263:0x017d A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:267:0x0193 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:270:0x0188 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:272:0x0226 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:274:0x021b A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:276:0x0231 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:278:0x023c A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:280:0x01d2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:282:0x0252 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:284:0x01c7 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:288:0x01dd A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:290:0x025d A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:309:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:310:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:311:? A[RETURN, SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:312:? A[SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r10v23 */
        /* JADX WARN: Type inference failed for: r10v3 */
        /* JADX WARN: Type inference failed for: r10v32 */
        /* JADX WARN: Type inference failed for: r10v33 */
        /* JADX WARN: Type inference failed for: r10v37 */
        /* JADX WARN: Type inference failed for: r10v38 */
        /* JADX WARN: Type inference failed for: r10v4, types: [java.io.FileOutputStream] */
        /* JADX WARN: Type inference failed for: r10v42 */
        /* JADX WARN: Type inference failed for: r10v43 */
        /* JADX WARN: Type inference failed for: r10v44 */
        /* JADX WARN: Type inference failed for: r10v45 */
        /* JADX WARN: Type inference failed for: r10v6, types: [java.io.FileOutputStream] */
        /* JADX WARN: Type inference failed for: r10v7, types: [java.io.FileOutputStream] */
        /* JADX WARN: Type inference failed for: r10v8, types: [java.io.FileOutputStream] */
        /* JADX WARN: Type inference failed for: r11v0 */
        /* JADX WARN: Type inference failed for: r11v1 */
        /* JADX WARN: Type inference failed for: r11v10 */
        /* JADX WARN: Type inference failed for: r11v11 */
        /* JADX WARN: Type inference failed for: r11v12 */
        /* JADX WARN: Type inference failed for: r11v13 */
        /* JADX WARN: Type inference failed for: r11v14 */
        /* JADX WARN: Type inference failed for: r11v15 */
        /* JADX WARN: Type inference failed for: r11v16 */
        /* JADX WARN: Type inference failed for: r11v17 */
        /* JADX WARN: Type inference failed for: r11v18 */
        /* JADX WARN: Type inference failed for: r11v19 */
        /* JADX WARN: Type inference failed for: r11v2 */
        /* JADX WARN: Type inference failed for: r11v20 */
        /* JADX WARN: Type inference failed for: r11v21 */
        /* JADX WARN: Type inference failed for: r11v22 */
        /* JADX WARN: Type inference failed for: r11v23, types: [java.io.BufferedOutputStream] */
        /* JADX WARN: Type inference failed for: r11v3 */
        /* JADX WARN: Type inference failed for: r11v4, types: [java.io.BufferedOutputStream] */
        /* JADX WARN: Type inference failed for: r11v5, types: [java.io.BufferedOutputStream] */
        /* JADX WARN: Type inference failed for: r11v6, types: [java.io.BufferedOutputStream] */
        /* JADX WARN: Type inference failed for: r11v7 */
        /* JADX WARN: Type inference failed for: r11v8 */
        /* JADX WARN: Type inference failed for: r11v9 */
        @Override // java.lang.Runnable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void run() throws java.lang.Throwable {
            /*
                Method dump skipped, instructions count: 642
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.netease.environment.OIIO0OI.OIIO00I.RunnableC0063OIIO00I.run():void");
        }
    }

    /* compiled from: DownloadUtils.java */
    class OIIO0O0 implements com.netease.environment.OIIO0I0.OIIO00I {

        /* renamed from: OIIO00I, reason: collision with root package name */
        long f1571OIIO00I = 0;
        final /* synthetic */ Context OIIO0O0;
        final /* synthetic */ String OIIO0OO;

        OIIO0O0(Context context, String str) {
            this.OIIO0O0 = context;
            this.OIIO0OO = str;
        }

        @Override // com.netease.environment.OIIO0I0.OIIO00I
        public void OIIO00I() {
            OIIO.OIIO0O0(OIIO00I.f1569OIIO00I, "download data file start");
            this.f1571OIIO00I = System.currentTimeMillis();
        }

        @Override // com.netease.environment.OIIO0I0.OIIO00I
        public void OIIO00I(int i) {
        }

        @Override // com.netease.environment.OIIO0I0.OIIO00I
        public void OIIO00I(boolean z) throws Throwable {
            OIIO.OIIO0O0(OIIO00I.f1569OIIO00I, "download data file result : " + z);
            if (z) {
                String strOIIO0OI = OIIO0I0.OIIO0OI(this.OIIO0O0);
                OIIO.OIIO0O0(OIIO00I.f1569OIIO00I, "tempFilePath: " + OIIO0OI.OIIOO0() + strOIIO0OI);
                String strOIIO0O0 = com.netease.environment.OIIO0II.OIIO0OI.OIIO0O0(OIIO0I0.OIIO0O0(strOIIO0OI).getBytes(), OIIO0OI.OIIOO0());
                String strOIIO0O02 = com.netease.environment.OIIO0II.OIIO0O0.OIIO0O0(com.netease.environment.OIIO0II.OIIO00I.OIIO00I(strOIIO0O0, OIIO0OI.OIIOO0()));
                String strOIIO0O03 = OIIO0I0.OIIO0O0(this.OIIO0O0);
                boolean zOIIO00I = OIIO0I0.OIIO00I(strOIIO0O03, strOIIO0O02);
                OIIO.OIIO0O0(OIIO00I.f1569OIIO00I, "regex file path : " + strOIIO0O03);
                if (OIIO0OI.OIIOOO()) {
                    if (!Pattern.initLocalMaps(this.OIIO0O0, strOIIO0O03, OIIO0OI.OIIOO0())) {
                        OIIO.OIIO00I(OIIO00I.f1569OIIO00I, "downloadRegularFile init regex maps error!!");
                    }
                } else if (!OIIO0II.OIIO00I(strOIIO0O0)) {
                    OIIO.OIIO0O0(OIIO00I.f1569OIIO00I, "decodeContent is not json");
                } else {
                    try {
                        JSONObject jSONObject = new JSONObject(strOIIO0O0);
                        JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject("settings");
                        com.netease.environment.OIIO0OO.OIIO0O0.OIIO0OO(this.OIIO0O0, jSONObjectOptJSONObject.optBoolean("enable", true));
                        com.netease.environment.OIIO0OO.OIIO0O0.OIIO0OI(this.OIIO0O0, jSONObjectOptJSONObject.optLong("updateInterval", 3600000L));
                        com.netease.environment.OIIO0OO.OIIO0O0.OIIO0O0(this.OIIO0O0, jSONObjectOptJSONObject.optLong("taskTimeout", 1000L));
                        OIIO0OO.OIIO00I(jSONObject.optJSONObject("regex"));
                    } catch (Exception e) {
                        OIIO.OIIO0O0(OIIO00I.f1569OIIO00I, "fail to save settings");
                        e.printStackTrace();
                    }
                }
                com.netease.environment.OIIO0OO.OIIO0O0.OIIO0OO(this.OIIO0O0, System.currentTimeMillis());
                if (zOIIO00I) {
                    com.netease.environment.OIIO0OO.OIIO0O0.OIIO0OI(this.OIIO0O0, OIIO0OI.OIIO0I(), this.OIIO0OO);
                    OIIO.OIIO0O0(OIIO00I.f1569OIIO00I, "new regularVersion : " + EnvManager.getRegularVersion());
                }
                OIIO.OIIO0O0(OIIO00I.f1569OIIO00I, "check data file file done");
            }
            com.netease.environment.OIIO0OO.OIIO0O0.OIIO0O0(this.OIIO0O0, false);
            long jCurrentTimeMillis = System.currentTimeMillis() - this.f1571OIIO00I;
            OIIO.OIIO0O0(OIIO00I.f1569OIIO00I, "download regular and parse cost time\uff1a" + jCurrentTimeMillis + "ms");
            com.netease.environment.OIIO0OO.OIIO00I.OIIO00I(OneTrack.Event.DOWNLOAD, jCurrentTimeMillis);
        }
    }

    private static void OIIO00I(String str, String str2, String str3, com.netease.environment.OIIO0I0.OIIO00I oiio00i) {
        Executors.newSingleThreadExecutor().execute(new RunnableC0063OIIO00I(oiio00i, str, str2, str3));
    }

    public static void OIIO00I(Context context, String str) {
        com.netease.environment.OIIO0OO.OIIO0O0.OIIO0O0(context, true);
        OIIO.OIIO0O0(f1569OIIO00I, "http get:" + str);
        OIIO00I(str, OIIO0I0.OIIO0OO(context), OIIO0I0.OIIO0O0(), new OIIO0O0(context, str));
    }
}