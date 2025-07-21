package com.netease.pharos.locationCheck;

import android.content.Context;
import android.text.TextUtils;
import com.netease.pharos.Const;
import com.netease.pharos.PharosProxy;
import com.netease.pharos.util.LogUtil;
import com.netease.pharos.util.Util;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class NetAreaInfo {
    private static final String TAG = "NetAreaInfo";
    private static NetAreaInfo sNetAreaInfo;
    private String mConfigConfigRegion;
    private String mLocation = null;
    private Map<String, ArrayList<NetAreaInfoUnit>> mIpHashMap = new HashMap();
    private Map<String, ArrayList<NetAreaInfoUnit>> mTimezonehashMap = new HashMap();
    private Map<String, String> mUdphashMap = new HashMap();
    private int mPackageNum = 1;
    private int mUdpCount = 4;
    private boolean mIsProbe = false;
    private boolean mIsHarbor = false;
    private double mIspNum = -1.0d;
    private double mIpv6Num = -1.0d;
    private boolean mLogUpload = true;
    private String mProbeConfig = "explore";
    private String mConfigConfigIpv6RegionTag = "";

    private NetAreaInfo() {
    }

    public static NetAreaInfo getInstances() {
        if (sNetAreaInfo == null) {
            synchronized (NetAreaInfo.class) {
                if (sNetAreaInfo == null) {
                    sNetAreaInfo = new NetAreaInfo();
                }
            }
        }
        return sNetAreaInfo;
    }

    public static String getDefaultNetDecisionConfigContent() throws Throwable {
        if (PharosProxy.getInstance().getContext() == null) {
            LogUtil.i(TAG, "Utils [getCfgFileContent] param is error");
            return "";
        }
        String assetFileContent = Util.getAssetFileContent(PharosProxy.getInstance().getContext(), Const.DEFAULT_NET_DECISION_CONFIG);
        LogUtil.i(TAG, "Utils [getCfgFileContent] defaultNetDecisionCfg =" + assetFileContent);
        return assetFileContent;
    }

    public static String getLastNetDecisionConfigContent() throws IOException {
        Context context = PharosProxy.getInstance().getContext();
        if (context == null) {
            LogUtil.i(TAG, "Utils [getCfgFileContent] param is error");
            return "";
        }
        String strFile2Info = Util.file2Info(context.getFileStreamPath(Const.LAST_NET_DECISION_CONFIG));
        LogUtil.i(TAG, "Utils [getCfgFileContent] lastNetDecisionCfg =" + strFile2Info);
        return strFile2Info;
    }

    /* JADX WARN: Removed duplicated region for block: B:384:0x0410 A[Catch: JSONException -> 0x055a, TryCatch #0 {JSONException -> 0x055a, blocks: (B:227:0x004d, B:229:0x0058, B:231:0x005f, B:233:0x0070, B:238:0x007a, B:240:0x0083, B:243:0x008b, B:245:0x0091, B:246:0x0095, B:248:0x009b, B:250:0x00b0, B:252:0x00b6, B:253:0x00ba, B:255:0x00c0, B:256:0x00e1, B:257:0x00f1, B:259:0x00f9, B:262:0x0101, B:264:0x0107, B:265:0x010b, B:267:0x0111, B:269:0x0122, B:271:0x0128, B:272:0x012c, B:274:0x0132, B:275:0x0153, B:276:0x0161, B:278:0x0167, B:281:0x016f, B:283:0x0175, B:284:0x0179, B:286:0x017f, B:287:0x018f, B:289:0x0195, B:292:0x019d, B:294:0x01a3, B:296:0x01a9, B:298:0x01af, B:302:0x01da, B:307:0x01e3, B:308:0x01e5, B:310:0x01eb, B:313:0x01f3, B:315:0x01f9, B:317:0x01ff, B:318:0x0205, B:320:0x020b, B:321:0x0211, B:323:0x021e, B:326:0x0226, B:328:0x022c, B:330:0x0248, B:331:0x024e, B:333:0x0256, B:334:0x025c, B:336:0x0269, B:341:0x0273, B:343:0x0279, B:345:0x02c2, B:347:0x02c8, B:348:0x02ef, B:350:0x02f6, B:352:0x02fc, B:354:0x0302, B:355:0x0329, B:357:0x0330, B:359:0x0336, B:361:0x0358, B:363:0x035e, B:365:0x0364, B:367:0x038a, B:377:0x03c7, B:379:0x03e6, B:382:0x0403, B:384:0x0410, B:387:0x0418, B:389:0x041e, B:391:0x0452, B:393:0x045a, B:395:0x0462, B:397:0x046a, B:399:0x0470, B:401:0x0476, B:402:0x047c, B:404:0x0482, B:406:0x0488, B:407:0x048e, B:408:0x04a4, B:410:0x04b1, B:411:0x04b5, B:413:0x04cb, B:415:0x04d3, B:417:0x04db, B:419:0x04e1, B:421:0x0517, B:422:0x051e, B:424:0x0528, B:426:0x052e, B:427:0x0534, B:429:0x0550, B:428:0x054b, B:369:0x0393, B:371:0x039a, B:373:0x03a0, B:375:0x03be, B:380:0x03fb), top: B:435:0x004d }] */
    /* JADX WARN: Removed duplicated region for block: B:385:0x0415  */
    /* JADX WARN: Removed duplicated region for block: B:387:0x0418 A[Catch: JSONException -> 0x055a, TryCatch #0 {JSONException -> 0x055a, blocks: (B:227:0x004d, B:229:0x0058, B:231:0x005f, B:233:0x0070, B:238:0x007a, B:240:0x0083, B:243:0x008b, B:245:0x0091, B:246:0x0095, B:248:0x009b, B:250:0x00b0, B:252:0x00b6, B:253:0x00ba, B:255:0x00c0, B:256:0x00e1, B:257:0x00f1, B:259:0x00f9, B:262:0x0101, B:264:0x0107, B:265:0x010b, B:267:0x0111, B:269:0x0122, B:271:0x0128, B:272:0x012c, B:274:0x0132, B:275:0x0153, B:276:0x0161, B:278:0x0167, B:281:0x016f, B:283:0x0175, B:284:0x0179, B:286:0x017f, B:287:0x018f, B:289:0x0195, B:292:0x019d, B:294:0x01a3, B:296:0x01a9, B:298:0x01af, B:302:0x01da, B:307:0x01e3, B:308:0x01e5, B:310:0x01eb, B:313:0x01f3, B:315:0x01f9, B:317:0x01ff, B:318:0x0205, B:320:0x020b, B:321:0x0211, B:323:0x021e, B:326:0x0226, B:328:0x022c, B:330:0x0248, B:331:0x024e, B:333:0x0256, B:334:0x025c, B:336:0x0269, B:341:0x0273, B:343:0x0279, B:345:0x02c2, B:347:0x02c8, B:348:0x02ef, B:350:0x02f6, B:352:0x02fc, B:354:0x0302, B:355:0x0329, B:357:0x0330, B:359:0x0336, B:361:0x0358, B:363:0x035e, B:365:0x0364, B:367:0x038a, B:377:0x03c7, B:379:0x03e6, B:382:0x0403, B:384:0x0410, B:387:0x0418, B:389:0x041e, B:391:0x0452, B:393:0x045a, B:395:0x0462, B:397:0x046a, B:399:0x0470, B:401:0x0476, B:402:0x047c, B:404:0x0482, B:406:0x0488, B:407:0x048e, B:408:0x04a4, B:410:0x04b1, B:411:0x04b5, B:413:0x04cb, B:415:0x04d3, B:417:0x04db, B:419:0x04e1, B:421:0x0517, B:422:0x051e, B:424:0x0528, B:426:0x052e, B:427:0x0534, B:429:0x0550, B:428:0x054b, B:369:0x0393, B:371:0x039a, B:373:0x03a0, B:375:0x03be, B:380:0x03fb), top: B:435:0x004d }] */
    /* JADX WARN: Removed duplicated region for block: B:391:0x0452 A[Catch: JSONException -> 0x055a, TryCatch #0 {JSONException -> 0x055a, blocks: (B:227:0x004d, B:229:0x0058, B:231:0x005f, B:233:0x0070, B:238:0x007a, B:240:0x0083, B:243:0x008b, B:245:0x0091, B:246:0x0095, B:248:0x009b, B:250:0x00b0, B:252:0x00b6, B:253:0x00ba, B:255:0x00c0, B:256:0x00e1, B:257:0x00f1, B:259:0x00f9, B:262:0x0101, B:264:0x0107, B:265:0x010b, B:267:0x0111, B:269:0x0122, B:271:0x0128, B:272:0x012c, B:274:0x0132, B:275:0x0153, B:276:0x0161, B:278:0x0167, B:281:0x016f, B:283:0x0175, B:284:0x0179, B:286:0x017f, B:287:0x018f, B:289:0x0195, B:292:0x019d, B:294:0x01a3, B:296:0x01a9, B:298:0x01af, B:302:0x01da, B:307:0x01e3, B:308:0x01e5, B:310:0x01eb, B:313:0x01f3, B:315:0x01f9, B:317:0x01ff, B:318:0x0205, B:320:0x020b, B:321:0x0211, B:323:0x021e, B:326:0x0226, B:328:0x022c, B:330:0x0248, B:331:0x024e, B:333:0x0256, B:334:0x025c, B:336:0x0269, B:341:0x0273, B:343:0x0279, B:345:0x02c2, B:347:0x02c8, B:348:0x02ef, B:350:0x02f6, B:352:0x02fc, B:354:0x0302, B:355:0x0329, B:357:0x0330, B:359:0x0336, B:361:0x0358, B:363:0x035e, B:365:0x0364, B:367:0x038a, B:377:0x03c7, B:379:0x03e6, B:382:0x0403, B:384:0x0410, B:387:0x0418, B:389:0x041e, B:391:0x0452, B:393:0x045a, B:395:0x0462, B:397:0x046a, B:399:0x0470, B:401:0x0476, B:402:0x047c, B:404:0x0482, B:406:0x0488, B:407:0x048e, B:408:0x04a4, B:410:0x04b1, B:411:0x04b5, B:413:0x04cb, B:415:0x04d3, B:417:0x04db, B:419:0x04e1, B:421:0x0517, B:422:0x051e, B:424:0x0528, B:426:0x052e, B:427:0x0534, B:429:0x0550, B:428:0x054b, B:369:0x0393, B:371:0x039a, B:373:0x03a0, B:375:0x03be, B:380:0x03fb), top: B:435:0x004d }] */
    /* JADX WARN: Removed duplicated region for block: B:392:0x0459  */
    /* JADX WARN: Removed duplicated region for block: B:395:0x0462 A[Catch: JSONException -> 0x055a, TryCatch #0 {JSONException -> 0x055a, blocks: (B:227:0x004d, B:229:0x0058, B:231:0x005f, B:233:0x0070, B:238:0x007a, B:240:0x0083, B:243:0x008b, B:245:0x0091, B:246:0x0095, B:248:0x009b, B:250:0x00b0, B:252:0x00b6, B:253:0x00ba, B:255:0x00c0, B:256:0x00e1, B:257:0x00f1, B:259:0x00f9, B:262:0x0101, B:264:0x0107, B:265:0x010b, B:267:0x0111, B:269:0x0122, B:271:0x0128, B:272:0x012c, B:274:0x0132, B:275:0x0153, B:276:0x0161, B:278:0x0167, B:281:0x016f, B:283:0x0175, B:284:0x0179, B:286:0x017f, B:287:0x018f, B:289:0x0195, B:292:0x019d, B:294:0x01a3, B:296:0x01a9, B:298:0x01af, B:302:0x01da, B:307:0x01e3, B:308:0x01e5, B:310:0x01eb, B:313:0x01f3, B:315:0x01f9, B:317:0x01ff, B:318:0x0205, B:320:0x020b, B:321:0x0211, B:323:0x021e, B:326:0x0226, B:328:0x022c, B:330:0x0248, B:331:0x024e, B:333:0x0256, B:334:0x025c, B:336:0x0269, B:341:0x0273, B:343:0x0279, B:345:0x02c2, B:347:0x02c8, B:348:0x02ef, B:350:0x02f6, B:352:0x02fc, B:354:0x0302, B:355:0x0329, B:357:0x0330, B:359:0x0336, B:361:0x0358, B:363:0x035e, B:365:0x0364, B:367:0x038a, B:377:0x03c7, B:379:0x03e6, B:382:0x0403, B:384:0x0410, B:387:0x0418, B:389:0x041e, B:391:0x0452, B:393:0x045a, B:395:0x0462, B:397:0x046a, B:399:0x0470, B:401:0x0476, B:402:0x047c, B:404:0x0482, B:406:0x0488, B:407:0x048e, B:408:0x04a4, B:410:0x04b1, B:411:0x04b5, B:413:0x04cb, B:415:0x04d3, B:417:0x04db, B:419:0x04e1, B:421:0x0517, B:422:0x051e, B:424:0x0528, B:426:0x052e, B:427:0x0534, B:429:0x0550, B:428:0x054b, B:369:0x0393, B:371:0x039a, B:373:0x03a0, B:375:0x03be, B:380:0x03fb), top: B:435:0x004d }] */
    /* JADX WARN: Removed duplicated region for block: B:396:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:410:0x04b1 A[Catch: JSONException -> 0x055a, TryCatch #0 {JSONException -> 0x055a, blocks: (B:227:0x004d, B:229:0x0058, B:231:0x005f, B:233:0x0070, B:238:0x007a, B:240:0x0083, B:243:0x008b, B:245:0x0091, B:246:0x0095, B:248:0x009b, B:250:0x00b0, B:252:0x00b6, B:253:0x00ba, B:255:0x00c0, B:256:0x00e1, B:257:0x00f1, B:259:0x00f9, B:262:0x0101, B:264:0x0107, B:265:0x010b, B:267:0x0111, B:269:0x0122, B:271:0x0128, B:272:0x012c, B:274:0x0132, B:275:0x0153, B:276:0x0161, B:278:0x0167, B:281:0x016f, B:283:0x0175, B:284:0x0179, B:286:0x017f, B:287:0x018f, B:289:0x0195, B:292:0x019d, B:294:0x01a3, B:296:0x01a9, B:298:0x01af, B:302:0x01da, B:307:0x01e3, B:308:0x01e5, B:310:0x01eb, B:313:0x01f3, B:315:0x01f9, B:317:0x01ff, B:318:0x0205, B:320:0x020b, B:321:0x0211, B:323:0x021e, B:326:0x0226, B:328:0x022c, B:330:0x0248, B:331:0x024e, B:333:0x0256, B:334:0x025c, B:336:0x0269, B:341:0x0273, B:343:0x0279, B:345:0x02c2, B:347:0x02c8, B:348:0x02ef, B:350:0x02f6, B:352:0x02fc, B:354:0x0302, B:355:0x0329, B:357:0x0330, B:359:0x0336, B:361:0x0358, B:363:0x035e, B:365:0x0364, B:367:0x038a, B:377:0x03c7, B:379:0x03e6, B:382:0x0403, B:384:0x0410, B:387:0x0418, B:389:0x041e, B:391:0x0452, B:393:0x045a, B:395:0x0462, B:397:0x046a, B:399:0x0470, B:401:0x0476, B:402:0x047c, B:404:0x0482, B:406:0x0488, B:407:0x048e, B:408:0x04a4, B:410:0x04b1, B:411:0x04b5, B:413:0x04cb, B:415:0x04d3, B:417:0x04db, B:419:0x04e1, B:421:0x0517, B:422:0x051e, B:424:0x0528, B:426:0x052e, B:427:0x0534, B:429:0x0550, B:428:0x054b, B:369:0x0393, B:371:0x039a, B:373:0x03a0, B:375:0x03be, B:380:0x03fb), top: B:435:0x004d }] */
    /* JADX WARN: Removed duplicated region for block: B:413:0x04cb A[Catch: JSONException -> 0x055a, TryCatch #0 {JSONException -> 0x055a, blocks: (B:227:0x004d, B:229:0x0058, B:231:0x005f, B:233:0x0070, B:238:0x007a, B:240:0x0083, B:243:0x008b, B:245:0x0091, B:246:0x0095, B:248:0x009b, B:250:0x00b0, B:252:0x00b6, B:253:0x00ba, B:255:0x00c0, B:256:0x00e1, B:257:0x00f1, B:259:0x00f9, B:262:0x0101, B:264:0x0107, B:265:0x010b, B:267:0x0111, B:269:0x0122, B:271:0x0128, B:272:0x012c, B:274:0x0132, B:275:0x0153, B:276:0x0161, B:278:0x0167, B:281:0x016f, B:283:0x0175, B:284:0x0179, B:286:0x017f, B:287:0x018f, B:289:0x0195, B:292:0x019d, B:294:0x01a3, B:296:0x01a9, B:298:0x01af, B:302:0x01da, B:307:0x01e3, B:308:0x01e5, B:310:0x01eb, B:313:0x01f3, B:315:0x01f9, B:317:0x01ff, B:318:0x0205, B:320:0x020b, B:321:0x0211, B:323:0x021e, B:326:0x0226, B:328:0x022c, B:330:0x0248, B:331:0x024e, B:333:0x0256, B:334:0x025c, B:336:0x0269, B:341:0x0273, B:343:0x0279, B:345:0x02c2, B:347:0x02c8, B:348:0x02ef, B:350:0x02f6, B:352:0x02fc, B:354:0x0302, B:355:0x0329, B:357:0x0330, B:359:0x0336, B:361:0x0358, B:363:0x035e, B:365:0x0364, B:367:0x038a, B:377:0x03c7, B:379:0x03e6, B:382:0x0403, B:384:0x0410, B:387:0x0418, B:389:0x041e, B:391:0x0452, B:393:0x045a, B:395:0x0462, B:397:0x046a, B:399:0x0470, B:401:0x0476, B:402:0x047c, B:404:0x0482, B:406:0x0488, B:407:0x048e, B:408:0x04a4, B:410:0x04b1, B:411:0x04b5, B:413:0x04cb, B:415:0x04d3, B:417:0x04db, B:419:0x04e1, B:421:0x0517, B:422:0x051e, B:424:0x0528, B:426:0x052e, B:427:0x0534, B:429:0x0550, B:428:0x054b, B:369:0x0393, B:371:0x039a, B:373:0x03a0, B:375:0x03be, B:380:0x03fb), top: B:435:0x004d }] */
    /* JADX WARN: Removed duplicated region for block: B:428:0x054b A[Catch: JSONException -> 0x055a, TryCatch #0 {JSONException -> 0x055a, blocks: (B:227:0x004d, B:229:0x0058, B:231:0x005f, B:233:0x0070, B:238:0x007a, B:240:0x0083, B:243:0x008b, B:245:0x0091, B:246:0x0095, B:248:0x009b, B:250:0x00b0, B:252:0x00b6, B:253:0x00ba, B:255:0x00c0, B:256:0x00e1, B:257:0x00f1, B:259:0x00f9, B:262:0x0101, B:264:0x0107, B:265:0x010b, B:267:0x0111, B:269:0x0122, B:271:0x0128, B:272:0x012c, B:274:0x0132, B:275:0x0153, B:276:0x0161, B:278:0x0167, B:281:0x016f, B:283:0x0175, B:284:0x0179, B:286:0x017f, B:287:0x018f, B:289:0x0195, B:292:0x019d, B:294:0x01a3, B:296:0x01a9, B:298:0x01af, B:302:0x01da, B:307:0x01e3, B:308:0x01e5, B:310:0x01eb, B:313:0x01f3, B:315:0x01f9, B:317:0x01ff, B:318:0x0205, B:320:0x020b, B:321:0x0211, B:323:0x021e, B:326:0x0226, B:328:0x022c, B:330:0x0248, B:331:0x024e, B:333:0x0256, B:334:0x025c, B:336:0x0269, B:341:0x0273, B:343:0x0279, B:345:0x02c2, B:347:0x02c8, B:348:0x02ef, B:350:0x02f6, B:352:0x02fc, B:354:0x0302, B:355:0x0329, B:357:0x0330, B:359:0x0336, B:361:0x0358, B:363:0x035e, B:365:0x0364, B:367:0x038a, B:377:0x03c7, B:379:0x03e6, B:382:0x0403, B:384:0x0410, B:387:0x0418, B:389:0x041e, B:391:0x0452, B:393:0x045a, B:395:0x0462, B:397:0x046a, B:399:0x0470, B:401:0x0476, B:402:0x047c, B:404:0x0482, B:406:0x0488, B:407:0x048e, B:408:0x04a4, B:410:0x04b1, B:411:0x04b5, B:413:0x04cb, B:415:0x04d3, B:417:0x04db, B:419:0x04e1, B:421:0x0517, B:422:0x051e, B:424:0x0528, B:426:0x052e, B:427:0x0534, B:429:0x0550, B:428:0x054b, B:369:0x0393, B:371:0x039a, B:373:0x03a0, B:375:0x03be, B:380:0x03fb), top: B:435:0x004d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void init(java.lang.String r29) throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 1409
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.locationCheck.NetAreaInfo.init(java.lang.String):void");
    }

    public String useIspNmaeGetProbeRegion(JSONObject jSONObject, String str, String str2) {
        LogUtil.i(TAG, "NetAreaInfo [useIspNmaeGetProbeRegion] isphashJson=" + jSONObject + ", ispName=" + str);
        String strOptString = null;
        if (jSONObject != null && !TextUtils.isEmpty(str) && jSONObject.has(str)) {
            JSONObject jSONObjectOptJSONObject = jSONObject.optJSONObject(str);
            LogUtil.i(TAG, "NetAreaInfo [useIspNmaeGetProbeRegion] network_isp_name_json=" + jSONObjectOptJSONObject);
            if (jSONObjectOptJSONObject != null && jSONObjectOptJSONObject.length() > 0) {
                LogUtil.i(TAG, "NetAreaInfo [useIspNmaeGetProbeRegion] ip_province=" + str2);
                if (!TextUtils.isEmpty(str2) && jSONObjectOptJSONObject.has(str2)) {
                    strOptString = jSONObjectOptJSONObject.optString(str2);
                    LogUtil.i(TAG, "NetAreaInfo [useIspNmaeGetProbeRegion] network_isp_name ip_province tProbeRegion=" + strOptString);
                    if (!TextUtils.isEmpty(strOptString)) {
                        getInstances().setmConfigRegion(strOptString);
                    }
                } else if (jSONObjectOptJSONObject.has("_all")) {
                    strOptString = jSONObjectOptJSONObject.optString("_all");
                    LogUtil.i(TAG, "NetAreaInfo [useIspNmaeGetProbeRegion] network_isp_name_json _all tProbeRegion=" + strOptString);
                    if (!TextUtils.isEmpty(strOptString)) {
                        getInstances().setmConfigRegion(strOptString);
                    }
                } else {
                    LogUtil.i(TAG, "NetAreaInfo [useIspNmaeGetProbeRegion] network_isp_name_json \u65e0\u6cd5\u5339\u914d");
                }
            }
        }
        return strOptString;
    }

    public String getmLocation() {
        return this.mLocation;
    }

    public void setmLocation(String str) {
        this.mLocation = str;
    }

    public Map<String, ArrayList<NetAreaInfoUnit>> getmIpHashMap() {
        return this.mIpHashMap;
    }

    public void setmIpHashMap(Map<String, ArrayList<NetAreaInfoUnit>> map) {
        if (map == null) {
            return;
        }
        this.mIpHashMap = map;
    }

    public Map<String, ArrayList<NetAreaInfoUnit>> getmTimezonehashMap() {
        return this.mTimezonehashMap;
    }

    public void setmTimezonehashMap(Map<String, ArrayList<NetAreaInfoUnit>> map) {
        if (map == null) {
            return;
        }
        this.mTimezonehashMap = map;
    }

    public Map<String, String> getMudphashMap() {
        return this.mUdphashMap;
    }

    public void setMudphashMap(Map<String, String> map) {
        if (map == null) {
            return;
        }
        this.mUdphashMap = map;
    }

    public String timezonehashMapGetValue(String str, String str2) {
        String str3 = null;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            LogUtil.i(TAG, "mTimezonehashMap=" + this.mTimezonehashMap.toString());
            if (this.mTimezonehashMap.containsKey(str)) {
                Iterator<NetAreaInfoUnit> it = this.mTimezonehashMap.get(str).iterator();
                while (it.hasNext()) {
                    NetAreaInfoUnit next = it.next();
                    String str4 = next.mKey;
                    String str5 = next.mValue;
                    if (str2.equals(str4)) {
                        str3 = str5;
                    }
                }
            }
        }
        return str3;
    }

    public int getPackageNum() {
        return this.mPackageNum;
    }

    public int getUdpCount() {
        return this.mUdpCount;
    }

    public boolean ismProbe() {
        return this.mIsProbe;
    }

    public void setmIsProbe(boolean z) {
        this.mIsProbe = z;
    }

    public double getmIspNum() {
        return this.mIspNum;
    }

    public void setmIspNum(long j) {
        this.mIspNum = j;
    }

    public double getmIpv6Num() {
        return this.mIpv6Num;
    }

    public void setmIpv6Num(long j) {
        this.mIpv6Num = j;
    }

    public boolean ismHarbor() {
        return this.mIsHarbor;
    }

    public void setmIsHarbor(boolean z) {
        this.mIsHarbor = z;
    }

    public String getmConfigRegion() {
        return this.mConfigConfigRegion;
    }

    public void setmConfigRegion(String str) {
        this.mConfigConfigRegion = str;
    }

    public String getmConfigConfigIpv6RegionTag() {
        return this.mConfigConfigIpv6RegionTag;
    }

    public void setmConfigConfigIpv6RegionTag(String str) {
        this.mConfigConfigIpv6RegionTag = str;
    }

    public boolean ismLogUpload() {
        return this.mLogUpload;
    }

    public void setmLogUpload(boolean z) {
        this.mLogUpload = z;
    }

    public String getmProbeConfig() {
        return this.mProbeConfig;
    }

    public void setmProbeConfig(String str) {
        this.mProbeConfig = str;
    }

    public String ipHashMapGetValue(String str, String str2) {
        String str3 = null;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            LogUtil.i(TAG, "mIpHashMap=" + this.mIpHashMap.toString());
            if (this.mIpHashMap.containsKey(str)) {
                Iterator<NetAreaInfoUnit> it = this.mIpHashMap.get(str).iterator();
                while (it.hasNext()) {
                    NetAreaInfoUnit next = it.next();
                    String str4 = next.mKey;
                    String str5 = next.mValue;
                    if (str2.equals(str4)) {
                        str3 = str5;
                    }
                }
            }
        }
        return str3;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("mLocation=");
        stringBuffer.append(this.mLocation).append("\n\nmIpHashMap=");
        stringBuffer.append(this.mIpHashMap.toString()).append("\n\nmTimezonehashMap=");
        stringBuffer.append(this.mTimezonehashMap.toString()).append("\n\nmudphashMap=");
        stringBuffer.append(this.mUdphashMap.toString()).append("\n\n");
        return stringBuffer.toString();
    }

    public synchronized void clean() {
        sNetAreaInfo = new NetAreaInfo();
    }

    public class NetAreaInfoUnit {
        public String mKey;
        public String mValue;

        public NetAreaInfoUnit(String str, String str2) {
            this.mKey = str;
            this.mValue = str2;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer("mKey=");
            stringBuffer.append(this.mKey).append(", mValue=").append(this.mValue).append("\n");
            return stringBuffer.toString();
        }
    }
}