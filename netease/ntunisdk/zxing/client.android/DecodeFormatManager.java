package com.netease.ntunisdk.zxing.client.android;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.netease.ntunisdk.zxing.client.android.Intents;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public final class DecodeFormatManager {
    private static final Map<String, Set<BarcodeFormat>> FORMATS_FOR_MODE;
    private static final Set<BarcodeFormat> ONE_D_FORMATS;
    static final Set<BarcodeFormat> QR_CODE_FORMATS = EnumSet.of(BarcodeFormat.QR_CODE);
    static final Set<BarcodeFormat> DATA_MATRIX_FORMATS = EnumSet.of(BarcodeFormat.DATA_MATRIX);
    static final Set<BarcodeFormat> AZTEC_FORMATS = EnumSet.of(BarcodeFormat.AZTEC);
    static final Set<BarcodeFormat> PDF417_FORMATS = EnumSet.of(BarcodeFormat.PDF_417);
    public static final Map<DecodeHintType, Object> TWO_DIMENSIONAL_HINTS = new EnumMap(DecodeHintType.class);
    static final Set<BarcodeFormat> PRODUCT_FORMATS = EnumSet.of(BarcodeFormat.UPC_A, BarcodeFormat.UPC_E, BarcodeFormat.EAN_13, BarcodeFormat.EAN_8, BarcodeFormat.RSS_14, BarcodeFormat.RSS_EXPANDED);
    static final Set<BarcodeFormat> INDUSTRIAL_FORMATS = EnumSet.of(BarcodeFormat.CODE_39, BarcodeFormat.CODE_93, BarcodeFormat.CODE_128, BarcodeFormat.ITF, BarcodeFormat.CODABAR);

    static {
        EnumSet enumSetCopyOf = EnumSet.copyOf((Collection) PRODUCT_FORMATS);
        ONE_D_FORMATS = enumSetCopyOf;
        enumSetCopyOf.addAll(INDUSTRIAL_FORMATS);
        HashMap map = new HashMap();
        FORMATS_FOR_MODE = map;
        map.put(Intents.Scan.ONE_D_MODE, ONE_D_FORMATS);
        FORMATS_FOR_MODE.put(Intents.Scan.PRODUCT_MODE, PRODUCT_FORMATS);
        FORMATS_FOR_MODE.put(Intents.Scan.QR_CODE_MODE, QR_CODE_FORMATS);
        FORMATS_FOR_MODE.put(Intents.Scan.DATA_MATRIX_MODE, DATA_MATRIX_FORMATS);
        FORMATS_FOR_MODE.put(Intents.Scan.AZTEC_MODE, AZTEC_FORMATS);
        FORMATS_FOR_MODE.put(Intents.Scan.PDF417_MODE, PDF417_FORMATS);
        addDecodeHintTypes(TWO_DIMENSIONAL_HINTS, getTwoDimensionalFormats());
    }

    private DecodeFormatManager() {
    }

    private static List<BarcodeFormat> getTwoDimensionalFormats() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(BarcodeFormat.AZTEC);
        arrayList.add(BarcodeFormat.DATA_MATRIX);
        arrayList.add(BarcodeFormat.MAXICODE);
        arrayList.add(BarcodeFormat.PDF_417);
        arrayList.add(BarcodeFormat.QR_CODE);
        return arrayList;
    }

    private static void addDecodeHintTypes(Map<DecodeHintType, Object> map, List<BarcodeFormat> list) {
        map.put(DecodeHintType.POSSIBLE_FORMATS, list);
        map.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        map.put(DecodeHintType.CHARACTER_SET, "UTF-8");
    }
}