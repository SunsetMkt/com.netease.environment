package com.netease.pharos.linkcheck;

import android.text.TextUtils;
import com.facebook.hermes.intl.Constants;
import com.netease.ntunisdk.external.protocol.Const;
import com.netease.pharos.ServerProxy;
import com.netease.pharos.util.LogUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes4.dex */
public class RegionConfigInfo {
    private static final String TAG = "RegionConfigInfo";
    private static RegionConfigInfo sRegionConfigInfo;
    private JSONObject mInfo = null;
    private JSONObject mResult = null;

    private RegionConfigInfo() {
    }

    public static RegionConfigInfo getInstance() {
        if (sRegionConfigInfo == null) {
            sRegionConfigInfo = new RegionConfigInfo();
        }
        return sRegionConfigInfo;
    }

    public JSONObject getmResult() {
        return this.mResult;
    }

    public void setmResult(JSONObject jSONObject) {
        this.mResult = jSONObject;
    }

    public void init(String str) {
        if (TextUtils.isEmpty(str)) {
            LogUtil.i(TAG, "RegionConfigInfo [init] \u53c2\u6570\u4e3a\u7a7a");
        }
        try {
            this.mInfo = new JSONObject(str);
        } catch (JSONException e) {
            LogUtil.w(TAG, "RegionConfigInfo [init] JSONException = " + e);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(23:289|(2:511|290)|519|294|517|295|(2:513|296)|306|515|307|(1:309)|310|311|(1:361)(6:315|(1:317)(1:318)|(1:330)(4:322|(4:325|(2:327|522)(1:523)|328|323)|521|329)|331|(3:333|(3:337|(4:340|(3:342|343|(2:345|526)(2:346|(2:348|527)(2:349|(2:355|529)(2:354|528))))(2:356|525)|357|338)|524)|358)(1:359)|360)|362|(1:410)(5:366|(1:368)(1:369)|(1:380)(3:373|(4:376|(2:378|531)(1:532)|379|374)|530)|381|(3:383|(3:387|(4:390|(3:392|393|(2:395|535)(2:396|(2:398|536)(2:399|(2:405|538)(2:404|537))))(2:406|534)|407|388)|533)|408)(1:409))|411|(5:415|(1:417)(1:418)|(1:429)(3:422|(4:425|(2:427|540)(1:541)|428|423)|539)|430|(3:432|(3:436|(4:439|(3:441|442|(2:444|544)(2:445|(2:447|545)(2:448|(2:454|547)(2:453|546))))(2:455|543)|456|437)|542)|457)(1:458))|459|(5:463|(1:465)(1:466)|(1:478)(4:470|(4:473|(2:475|549)(1:550)|476|471)|548|477)|479|(3:481|(3:485|(4:488|(1:561)(4:552|490|491|(3:554|493|565)(3:553|494|(3:556|496|564)(3:555|497|(2:503|562)(3:559|502|563))))|560|486)|551)|504))|505|509|510) */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x06e9, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x06ea, code lost:
    
        com.netease.pharos.util.LogUtil.e(com.netease.pharos.linkcheck.RegionConfigInfo.TAG, "RegionConfigInfo [parse] dictionaryCfg Exception = " + r0);
     */
    /* JADX WARN: Removed duplicated region for block: B:309:0x00a1 A[Catch: Exception -> 0x06e9, TryCatch #2 {Exception -> 0x06e9, blocks: (B:307:0x0092, B:309:0x00a1, B:310:0x00a9, B:313:0x00c9, B:315:0x00cf, B:317:0x00ef, B:320:0x0106, B:323:0x0111, B:325:0x0117, B:328:0x0123, B:331:0x012c, B:333:0x0142, B:335:0x014d, B:337:0x0155, B:338:0x015d, B:340:0x0163, B:342:0x016f, B:345:0x0179, B:346:0x01a2, B:348:0x01aa, B:349:0x01d0, B:351:0x01d6, B:354:0x01dd, B:355:0x0203, B:358:0x0233, B:362:0x0259, B:364:0x0261, B:366:0x0267, B:368:0x0278, B:371:0x0297, B:374:0x029f, B:376:0x02a5, B:379:0x02b1, B:381:0x02b5, B:383:0x02cb, B:385:0x02d6, B:387:0x02de, B:388:0x02e6, B:390:0x02ec, B:392:0x02f8, B:395:0x0300, B:396:0x0329, B:398:0x0331, B:399:0x0357, B:401:0x035d, B:404:0x0364, B:405:0x038a, B:408:0x03b6, B:411:0x03d8, B:413:0x03e2, B:415:0x03e8, B:417:0x03f9, B:420:0x0414, B:423:0x041c, B:425:0x0422, B:428:0x0430, B:430:0x0436, B:432:0x044c, B:434:0x0457, B:436:0x045f, B:437:0x0467, B:439:0x046d, B:441:0x0479, B:444:0x0481, B:445:0x04aa, B:447:0x04b2, B:448:0x04d8, B:450:0x04de, B:453:0x04e5, B:454:0x050b, B:457:0x0537, B:459:0x0556, B:461:0x0568, B:463:0x056e, B:465:0x057f, B:468:0x059a, B:471:0x05a3, B:473:0x05a9, B:476:0x05b5, B:479:0x05bc, B:481:0x05d2, B:483:0x05dd, B:485:0x05e5, B:486:0x05ed, B:488:0x05f3, B:490:0x05ff, B:493:0x0607, B:494:0x062d, B:496:0x0633, B:497:0x0659, B:499:0x065f, B:502:0x0666, B:503:0x068d, B:504:0x06b4, B:505:0x06ce), top: B:515:0x0092 }] */
    /* JADX WARN: Removed duplicated region for block: B:313:0x00c9 A[Catch: Exception -> 0x06e9, TRY_ENTER, TryCatch #2 {Exception -> 0x06e9, blocks: (B:307:0x0092, B:309:0x00a1, B:310:0x00a9, B:313:0x00c9, B:315:0x00cf, B:317:0x00ef, B:320:0x0106, B:323:0x0111, B:325:0x0117, B:328:0x0123, B:331:0x012c, B:333:0x0142, B:335:0x014d, B:337:0x0155, B:338:0x015d, B:340:0x0163, B:342:0x016f, B:345:0x0179, B:346:0x01a2, B:348:0x01aa, B:349:0x01d0, B:351:0x01d6, B:354:0x01dd, B:355:0x0203, B:358:0x0233, B:362:0x0259, B:364:0x0261, B:366:0x0267, B:368:0x0278, B:371:0x0297, B:374:0x029f, B:376:0x02a5, B:379:0x02b1, B:381:0x02b5, B:383:0x02cb, B:385:0x02d6, B:387:0x02de, B:388:0x02e6, B:390:0x02ec, B:392:0x02f8, B:395:0x0300, B:396:0x0329, B:398:0x0331, B:399:0x0357, B:401:0x035d, B:404:0x0364, B:405:0x038a, B:408:0x03b6, B:411:0x03d8, B:413:0x03e2, B:415:0x03e8, B:417:0x03f9, B:420:0x0414, B:423:0x041c, B:425:0x0422, B:428:0x0430, B:430:0x0436, B:432:0x044c, B:434:0x0457, B:436:0x045f, B:437:0x0467, B:439:0x046d, B:441:0x0479, B:444:0x0481, B:445:0x04aa, B:447:0x04b2, B:448:0x04d8, B:450:0x04de, B:453:0x04e5, B:454:0x050b, B:457:0x0537, B:459:0x0556, B:461:0x0568, B:463:0x056e, B:465:0x057f, B:468:0x059a, B:471:0x05a3, B:473:0x05a9, B:476:0x05b5, B:479:0x05bc, B:481:0x05d2, B:483:0x05dd, B:485:0x05e5, B:486:0x05ed, B:488:0x05f3, B:490:0x05ff, B:493:0x0607, B:494:0x062d, B:496:0x0633, B:497:0x0659, B:499:0x065f, B:502:0x0666, B:503:0x068d, B:504:0x06b4, B:505:0x06ce), top: B:515:0x0092 }] */
    /* JADX WARN: Removed duplicated region for block: B:361:0x0255  */
    /* JADX WARN: Removed duplicated region for block: B:410:0x03d6  */
    /* JADX WARN: Removed duplicated region for block: B:417:0x03f9 A[Catch: Exception -> 0x06e9, TryCatch #2 {Exception -> 0x06e9, blocks: (B:307:0x0092, B:309:0x00a1, B:310:0x00a9, B:313:0x00c9, B:315:0x00cf, B:317:0x00ef, B:320:0x0106, B:323:0x0111, B:325:0x0117, B:328:0x0123, B:331:0x012c, B:333:0x0142, B:335:0x014d, B:337:0x0155, B:338:0x015d, B:340:0x0163, B:342:0x016f, B:345:0x0179, B:346:0x01a2, B:348:0x01aa, B:349:0x01d0, B:351:0x01d6, B:354:0x01dd, B:355:0x0203, B:358:0x0233, B:362:0x0259, B:364:0x0261, B:366:0x0267, B:368:0x0278, B:371:0x0297, B:374:0x029f, B:376:0x02a5, B:379:0x02b1, B:381:0x02b5, B:383:0x02cb, B:385:0x02d6, B:387:0x02de, B:388:0x02e6, B:390:0x02ec, B:392:0x02f8, B:395:0x0300, B:396:0x0329, B:398:0x0331, B:399:0x0357, B:401:0x035d, B:404:0x0364, B:405:0x038a, B:408:0x03b6, B:411:0x03d8, B:413:0x03e2, B:415:0x03e8, B:417:0x03f9, B:420:0x0414, B:423:0x041c, B:425:0x0422, B:428:0x0430, B:430:0x0436, B:432:0x044c, B:434:0x0457, B:436:0x045f, B:437:0x0467, B:439:0x046d, B:441:0x0479, B:444:0x0481, B:445:0x04aa, B:447:0x04b2, B:448:0x04d8, B:450:0x04de, B:453:0x04e5, B:454:0x050b, B:457:0x0537, B:459:0x0556, B:461:0x0568, B:463:0x056e, B:465:0x057f, B:468:0x059a, B:471:0x05a3, B:473:0x05a9, B:476:0x05b5, B:479:0x05bc, B:481:0x05d2, B:483:0x05dd, B:485:0x05e5, B:486:0x05ed, B:488:0x05f3, B:490:0x05ff, B:493:0x0607, B:494:0x062d, B:496:0x0633, B:497:0x0659, B:499:0x065f, B:502:0x0666, B:503:0x068d, B:504:0x06b4, B:505:0x06ce), top: B:515:0x0092 }] */
    /* JADX WARN: Removed duplicated region for block: B:418:0x0410  */
    /* JADX WARN: Removed duplicated region for block: B:429:0x0435  */
    /* JADX WARN: Removed duplicated region for block: B:432:0x044c A[Catch: Exception -> 0x06e9, TryCatch #2 {Exception -> 0x06e9, blocks: (B:307:0x0092, B:309:0x00a1, B:310:0x00a9, B:313:0x00c9, B:315:0x00cf, B:317:0x00ef, B:320:0x0106, B:323:0x0111, B:325:0x0117, B:328:0x0123, B:331:0x012c, B:333:0x0142, B:335:0x014d, B:337:0x0155, B:338:0x015d, B:340:0x0163, B:342:0x016f, B:345:0x0179, B:346:0x01a2, B:348:0x01aa, B:349:0x01d0, B:351:0x01d6, B:354:0x01dd, B:355:0x0203, B:358:0x0233, B:362:0x0259, B:364:0x0261, B:366:0x0267, B:368:0x0278, B:371:0x0297, B:374:0x029f, B:376:0x02a5, B:379:0x02b1, B:381:0x02b5, B:383:0x02cb, B:385:0x02d6, B:387:0x02de, B:388:0x02e6, B:390:0x02ec, B:392:0x02f8, B:395:0x0300, B:396:0x0329, B:398:0x0331, B:399:0x0357, B:401:0x035d, B:404:0x0364, B:405:0x038a, B:408:0x03b6, B:411:0x03d8, B:413:0x03e2, B:415:0x03e8, B:417:0x03f9, B:420:0x0414, B:423:0x041c, B:425:0x0422, B:428:0x0430, B:430:0x0436, B:432:0x044c, B:434:0x0457, B:436:0x045f, B:437:0x0467, B:439:0x046d, B:441:0x0479, B:444:0x0481, B:445:0x04aa, B:447:0x04b2, B:448:0x04d8, B:450:0x04de, B:453:0x04e5, B:454:0x050b, B:457:0x0537, B:459:0x0556, B:461:0x0568, B:463:0x056e, B:465:0x057f, B:468:0x059a, B:471:0x05a3, B:473:0x05a9, B:476:0x05b5, B:479:0x05bc, B:481:0x05d2, B:483:0x05dd, B:485:0x05e5, B:486:0x05ed, B:488:0x05f3, B:490:0x05ff, B:493:0x0607, B:494:0x062d, B:496:0x0633, B:497:0x0659, B:499:0x065f, B:502:0x0666, B:503:0x068d, B:504:0x06b4, B:505:0x06ce), top: B:515:0x0092 }] */
    /* JADX WARN: Removed duplicated region for block: B:458:0x0554  */
    /* JADX WARN: Removed duplicated region for block: B:465:0x057f A[Catch: Exception -> 0x06e9, TryCatch #2 {Exception -> 0x06e9, blocks: (B:307:0x0092, B:309:0x00a1, B:310:0x00a9, B:313:0x00c9, B:315:0x00cf, B:317:0x00ef, B:320:0x0106, B:323:0x0111, B:325:0x0117, B:328:0x0123, B:331:0x012c, B:333:0x0142, B:335:0x014d, B:337:0x0155, B:338:0x015d, B:340:0x0163, B:342:0x016f, B:345:0x0179, B:346:0x01a2, B:348:0x01aa, B:349:0x01d0, B:351:0x01d6, B:354:0x01dd, B:355:0x0203, B:358:0x0233, B:362:0x0259, B:364:0x0261, B:366:0x0267, B:368:0x0278, B:371:0x0297, B:374:0x029f, B:376:0x02a5, B:379:0x02b1, B:381:0x02b5, B:383:0x02cb, B:385:0x02d6, B:387:0x02de, B:388:0x02e6, B:390:0x02ec, B:392:0x02f8, B:395:0x0300, B:396:0x0329, B:398:0x0331, B:399:0x0357, B:401:0x035d, B:404:0x0364, B:405:0x038a, B:408:0x03b6, B:411:0x03d8, B:413:0x03e2, B:415:0x03e8, B:417:0x03f9, B:420:0x0414, B:423:0x041c, B:425:0x0422, B:428:0x0430, B:430:0x0436, B:432:0x044c, B:434:0x0457, B:436:0x045f, B:437:0x0467, B:439:0x046d, B:441:0x0479, B:444:0x0481, B:445:0x04aa, B:447:0x04b2, B:448:0x04d8, B:450:0x04de, B:453:0x04e5, B:454:0x050b, B:457:0x0537, B:459:0x0556, B:461:0x0568, B:463:0x056e, B:465:0x057f, B:468:0x059a, B:471:0x05a3, B:473:0x05a9, B:476:0x05b5, B:479:0x05bc, B:481:0x05d2, B:483:0x05dd, B:485:0x05e5, B:486:0x05ed, B:488:0x05f3, B:490:0x05ff, B:493:0x0607, B:494:0x062d, B:496:0x0633, B:497:0x0659, B:499:0x065f, B:502:0x0666, B:503:0x068d, B:504:0x06b4, B:505:0x06ce), top: B:515:0x0092 }] */
    /* JADX WARN: Removed duplicated region for block: B:466:0x0596  */
    /* JADX WARN: Removed duplicated region for block: B:478:0x05bb  */
    /* JADX WARN: Removed duplicated region for block: B:481:0x05d2 A[Catch: Exception -> 0x06e9, TryCatch #2 {Exception -> 0x06e9, blocks: (B:307:0x0092, B:309:0x00a1, B:310:0x00a9, B:313:0x00c9, B:315:0x00cf, B:317:0x00ef, B:320:0x0106, B:323:0x0111, B:325:0x0117, B:328:0x0123, B:331:0x012c, B:333:0x0142, B:335:0x014d, B:337:0x0155, B:338:0x015d, B:340:0x0163, B:342:0x016f, B:345:0x0179, B:346:0x01a2, B:348:0x01aa, B:349:0x01d0, B:351:0x01d6, B:354:0x01dd, B:355:0x0203, B:358:0x0233, B:362:0x0259, B:364:0x0261, B:366:0x0267, B:368:0x0278, B:371:0x0297, B:374:0x029f, B:376:0x02a5, B:379:0x02b1, B:381:0x02b5, B:383:0x02cb, B:385:0x02d6, B:387:0x02de, B:388:0x02e6, B:390:0x02ec, B:392:0x02f8, B:395:0x0300, B:396:0x0329, B:398:0x0331, B:399:0x0357, B:401:0x035d, B:404:0x0364, B:405:0x038a, B:408:0x03b6, B:411:0x03d8, B:413:0x03e2, B:415:0x03e8, B:417:0x03f9, B:420:0x0414, B:423:0x041c, B:425:0x0422, B:428:0x0430, B:430:0x0436, B:432:0x044c, B:434:0x0457, B:436:0x045f, B:437:0x0467, B:439:0x046d, B:441:0x0479, B:444:0x0481, B:445:0x04aa, B:447:0x04b2, B:448:0x04d8, B:450:0x04de, B:453:0x04e5, B:454:0x050b, B:457:0x0537, B:459:0x0556, B:461:0x0568, B:463:0x056e, B:465:0x057f, B:468:0x059a, B:471:0x05a3, B:473:0x05a9, B:476:0x05b5, B:479:0x05bc, B:481:0x05d2, B:483:0x05dd, B:485:0x05e5, B:486:0x05ed, B:488:0x05f3, B:490:0x05ff, B:493:0x0607, B:494:0x062d, B:496:0x0633, B:497:0x0659, B:499:0x065f, B:502:0x0666, B:503:0x068d, B:504:0x06b4, B:505:0x06ce), top: B:515:0x0092 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void parse() throws org.json.JSONException {
        /*
            Method dump skipped, instructions count: 1795
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.pharos.linkcheck.RegionConfigInfo.parse():void");
    }

    public boolean getEnable() throws JSONException {
        if (!this.mResult.has("measure")) {
            return false;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("enable")) {
                return false;
            }
            return jSONObject.getBoolean("enable");
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getInterval() throws JSONException {
        if (!this.mResult.has("measure")) {
            return 0;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("interval")) {
                return 0;
            }
            try {
                return jSONObject.getInt("interval");
            } catch (JSONException e) {
                e.printStackTrace();
                return 0;
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            return 0;
        }
    }

    public JSONObject getNapIcmp() throws JSONException {
        if (!this.mResult.has("measure")) {
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("nap_icmp")) {
                return null;
            }
            try {
                return jSONObject.getJSONObject("nap_icmp");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public JSONObject getRapIcmp() throws JSONException {
        if (!this.mResult.has("measure")) {
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("rap_icmp")) {
                return null;
            }
            try {
                return jSONObject.getJSONObject("rap_icmp");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public JSONObject getRapUdp() throws JSONException {
        if (!this.mResult.has("measure")) {
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("rap_udp")) {
                return null;
            }
            try {
                return jSONObject.getJSONObject("rap_udp");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public JSONObject getRapTransfer() throws JSONException {
        if (!this.mResult.has("measure")) {
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("rap_transfer")) {
                return null;
            }
            try {
                return jSONObject.getJSONObject("rap_transfer");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public JSONObject getRapMtr() throws JSONException {
        if (!this.mResult.has("measure")) {
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("rap_mtr")) {
                return null;
            }
            try {
                return jSONObject.getJSONObject("rap_mtr");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public JSONObject getSapUdp() throws JSONException {
        if (!this.mResult.has("measure")) {
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("sap_udp")) {
                return null;
            }
            try {
                return jSONObject.getJSONObject("sap_udp");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public JSONObject getSapTransfer() throws JSONException {
        if (!this.mResult.has("measure")) {
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("sap_transfer")) {
                return null;
            }
            try {
                return jSONObject.getJSONObject("sap_transfer");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public JSONObject getResolve() throws JSONException {
        if (!this.mResult.has("measure")) {
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("resolve")) {
                return null;
            }
            try {
                return jSONObject.getJSONObject("resolve");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public JSONObject getRapQos() throws JSONException {
        LogUtil.i(TAG, "RegionConfigInfo [getRapQos] mResult=" + this.mResult);
        if (!this.mResult.has("measure")) {
            return null;
        }
        try {
            JSONObject jSONObject = this.mResult.getJSONObject("measure");
            if (jSONObject == null || !jSONObject.has("rap_qos")) {
                return null;
            }
            try {
                return jSONObject.getJSONObject("rap_qos");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public void setTestResult() throws JSONException {
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        JSONObject jSONObject3 = new JSONObject();
        JSONObject jSONObject4 = new JSONObject();
        try {
            jSONObject4.put("enable", true);
            jSONObject4.put("cycle", true);
            jSONObject4.put("count", 10);
            JSONObject jSONObject5 = new JSONObject();
            jSONObject5.put("dest", "106.2.42.128");
            jSONObject5.put("enable", true);
            jSONObject5.put("cycle", false);
            jSONObject5.put("count", 20);
            JSONObject jSONObject6 = new JSONObject();
            jSONObject6.put("dest", "106.2.42.128:8001");
            jSONObject6.put("enable", true);
            jSONObject6.put("cycle", false);
            jSONObject6.put("count", 10);
            jSONObject6.put("package", 2);
            jSONObject6.put("gate", 800);
            JSONObject jSONObject7 = new JSONObject();
            jSONObject7.put("dest", "106.2.42.128:8001");
            jSONObject7.put("enable", true);
            jSONObject7.put("cycle", false);
            jSONObject7.put("count", 10);
            jSONObject7.put("protocol", "kcp");
            jSONObject7.put("package", 2);
            JSONObject jSONObject8 = new JSONObject();
            try {
                jSONObject8.put("enable", true);
                jSONObject8.put("cycle", false);
                jSONObject8.put("count", 10);
                JSONObject jSONObject9 = new JSONObject();
                jSONObject9.put("dest", "106.2.42.128:8001");
                jSONObject9.put("enable", true);
                jSONObject9.put("cycle", false);
                jSONObject9.put("count", 10);
                jSONObject9.put("gate", 800);
                JSONObject jSONObject10 = new JSONObject();
                jSONObject10.put("dest", "106.2.42.128:8001");
                jSONObject10.put("enable", true);
                jSONObject10.put("cycle", false);
                jSONObject10.put("count", 10);
                jSONObject10.put("protocol", "tcp");
                jSONObject10.put("package", 2);
                JSONObject jSONObject11 = new JSONObject();
                jSONObject11.put("dest", ServerProxy.getInstance().getHost());
                jSONObject11.put("enable", true);
                jSONObject11.put("cycle", false);
                jSONObject3.put("nap_icmp", jSONObject4);
                jSONObject3.put("rap_icmp", jSONObject5);
                jSONObject3.put("rap_udp", jSONObject6);
                jSONObject3.put("rap_transfer", jSONObject7);
                jSONObject3.put("rap_mtr", jSONObject8);
                jSONObject3.put("sap_udp", jSONObject9);
                jSONObject3.put("sap_transfer", jSONObject10);
                jSONObject3.put("resolve", jSONObject11);
                jSONObject3.put("interval", 20);
                jSONObject3.put("enable", true);
                jSONObject3.put("test", "test");
                jSONObject = jSONObject2;
                try {
                    jSONObject.put("measure", jSONObject3);
                } catch (Exception e) {
                    e = e;
                    LogUtil.w(TAG, "Exception=" + e);
                    this.mResult = jSONObject;
                }
            } catch (Exception e2) {
                e = e2;
                jSONObject = jSONObject2;
            }
        } catch (Exception e3) {
            e = e3;
            jSONObject = jSONObject2;
        }
        this.mResult = jSONObject;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v15, types: [java.lang.Object, org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r0v16, types: [java.lang.Object, org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r0v17, types: [java.lang.Object, org.json.JSONObject] */
    /* JADX WARN: Type inference failed for: r5v1 */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v4, types: [org.json.JSONObject] */
    public JSONObject getTestConfig() throws JSONException {
        ?? r5;
        JSONObject jSONObject;
        JSONObject jSONObject2 = new JSONObject();
        JSONObject jSONObject3 = new JSONObject();
        JSONObject jSONObject4 = new JSONObject();
        JSONObject jSONObject5 = new JSONObject();
        try {
            jSONObject5.put("enable", true);
            jSONObject5.put("cycle", false);
            jSONObject5.put("count", 10);
            JSONObject jSONObject6 = new JSONObject();
            jSONObject6.put("dest", "106.2.42.128");
            jSONObject6.put("enable", true);
            jSONObject6.put("cycle", true);
            jSONObject6.put("count", 20);
            JSONObject jSONObject7 = new JSONObject();
            jSONObject7.put("dest", "106.2.42.128:8001");
            jSONObject7.put("enable", true);
            jSONObject7.put("cycle", false);
            jSONObject7.put("count", 10);
            jSONObject7.put("package", 2);
            jSONObject7.put("gate", 800);
            JSONObject jSONObject8 = new JSONObject();
            jSONObject8.put("dest", "106.2.42.128:8002");
            jSONObject8.put("enable", true);
            jSONObject8.put("cycle", false);
            jSONObject8.put("count", 10);
            jSONObject8.put("protocol", "tcp");
            jSONObject8.put("package", 2);
            JSONObject jSONObject9 = new JSONObject();
            jSONObject9.put("enable", true);
            jSONObject9.put("cycle", false);
            jSONObject9.put("count", 10);
            JSONObject jSONObject10 = new JSONObject();
            jSONObject10.put("dest", "52.52.108.248:8001");
            jSONObject10.put("enable", true);
            jSONObject10.put("cycle", false);
            jSONObject10.put("count", 10);
            jSONObject10.put("gate", 800);
            JSONObject jSONObject11 = new JSONObject();
            jSONObject11.put("dest", "52.52.108.248:8002");
            jSONObject11.put("enable", true);
            jSONObject11.put("cycle", false);
            jSONObject11.put("count", 10);
            jSONObject11.put("protocol", "tcp");
            jSONObject11.put("package", 2);
            JSONObject jSONObject12 = new JSONObject();
            jSONObject12.put("dest", ServerProxy.getInstance().getHost());
            jSONObject12.put("enable", true);
            jSONObject12.put("cycle", false);
            jSONObject3.put("nap_icmp", jSONObject5);
            jSONObject3.put("rap_icmp", jSONObject6);
            jSONObject3.put("rap_udp", jSONObject7);
            jSONObject3.put("rap_transfer", jSONObject8);
            jSONObject3.put("rap_mtr", jSONObject9);
            jSONObject3.put("sap_udp", jSONObject10);
            jSONObject3.put("sap_transfer", jSONObject11);
            jSONObject3.put("resolve", jSONObject12);
            jSONObject3.put("interval", 20);
            jSONObject3.put("enable", true);
            jSONObject3.put("test", "test");
            jSONObject4.put("measure", jSONObject3);
            r5 = jSONObject2;
            try {
                r5.put(Constants.COLLATION_DEFAULT, jSONObject4);
                JSONArray jSONArray = new JSONArray();
                JSONObject jSONObject13 = new JSONObject();
                jSONArray.put("asia");
                jSONObject13.put("items", jSONArray);
                JSONObject jSONObject14 = new JSONObject();
                JSONObject jSONObject15 = new JSONObject();
                jSONObject14.put("enable", false);
                jSONObject14.put("cycle", false);
                jSONObject14.put("count", 20);
                jSONObject15.put("nap_icmp", jSONObject14);
                jSONObject15.put("interval", 100);
                jSONObject15.put("enable", false);
                jSONObject15.put("test", "test1");
                jSONObject13.put("measure", jSONObject15);
                r5.put("continent", jSONObject13);
                ?? jSONObject16 = new JSONObject();
                JSONArray jSONArray2 = new JSONArray();
                jSONArray2.put("china");
                jSONObject16.put("items", jSONArray2);
                JSONObject jSONObject17 = new JSONObject();
                JSONObject jSONObject18 = new JSONObject();
                jSONObject18.put("enable", true);
                jSONObject18.put("cycle", true);
                jSONObject18.put("count", 30);
                jSONObject17.put("nap_icmp", jSONObject18);
                jSONObject16.put("measure", jSONObject17);
                r5.put(Const.COUNTRY, jSONObject16);
                ?? jSONObject19 = new JSONObject();
                JSONArray jSONArray3 = new JSONArray();
                jSONArray3.put("guangdong");
                jSONObject19.put("items", jSONArray3);
                JSONObject jSONObject20 = new JSONObject();
                JSONObject jSONObject21 = new JSONObject();
                jSONObject21.put("enable", false);
                jSONObject21.put("cycle", false);
                jSONObject21.put("count", 40);
                jSONObject20.put("nap_icmp", jSONObject21);
                jSONObject19.put("measure", jSONObject20);
                r5.put("province", jSONObject19);
                ?? jSONObject22 = new JSONObject();
                JSONArray jSONArray4 = new JSONArray();
                jSONArray4.put("111");
                jSONObject22.put("items", jSONArray4);
                JSONObject jSONObject23 = new JSONObject();
                JSONObject jSONObject24 = new JSONObject();
                jSONObject24.put("enable", true);
                jSONObject24.put("cycle", true);
                jSONObject24.put("count", 50);
                jSONObject23.put("nap_icmp", jSONObject24);
                jSONObject22.put("measure", jSONObject23);
                r5.put("project", jSONObject22);
                jSONObject = r5;
            } catch (Exception e) {
                e = e;
                LogUtil.w(TAG, "Exception=" + e);
                jSONObject = r5;
                return jSONObject;
            }
        } catch (Exception e2) {
            e = e2;
            r5 = jSONObject2;
        }
        return jSONObject;
    }
}