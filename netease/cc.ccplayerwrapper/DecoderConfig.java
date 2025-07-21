package com.netease.cc.ccplayerwrapper;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.netease.cc.ccplayerwrapper.utils.LogUtil;
import com.netease.ntunisdk.external.protocol.Const;
import java.lang.reflect.Method;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.DeviceConfig;
import tv.danmaku.ijk.media.player.HttpCallback;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.tools.SwitcherConfig;

/* loaded from: classes4.dex */
public class DecoderConfig {

    /* renamed from: a, reason: collision with root package name */
    public static IjkMediaPlayer.DecoderInfo f1519a = null;
    public static IjkMediaPlayer.DecoderInfo b = null;
    public static String c = "http://vapi.dev.cc.163.com/api/req_android_decoder?";
    public static String d = "http://cgi.v.cc.163.com/api/req_android_decoder?";
    public static String e = "http://vapi.dev.cc.163.com/api/report_android_device_detail?";
    public static String f = "http://cgi.v.cc.163.com/api/report_android_device_detail?";

    static class a implements HttpCallback {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ StringBuilder f1520a;
        final /* synthetic */ Context b;

        a(StringBuilder sb, Context context) {
            this.f1520a = sb;
            this.b = context;
        }

        @Override // tv.danmaku.ijk.media.player.HttpCallback
        public void callback(int i, String str) {
            StringBuilder sb = new StringBuilder("h4 ");
            sb.append(this.f1520a.toString());
            sb.append(" result:");
            sb.append(i);
            sb.append(" ");
            sb.append(str != null ? str : Const.EMPTY);
            LogUtil.LOGF("DecoderConfig", sb.toString());
            if (i == 200) {
                try {
                    SwitcherConfig.setSwitcherInt(this.b, "enable_hd4", new JSONObject(str).getInt("enablehd"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class b implements HttpCallback {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ StringBuilder f1521a;
        final /* synthetic */ Context b;

        b(StringBuilder sb, Context context) {
            this.f1521a = sb;
            this.b = context;
        }

        @Override // tv.danmaku.ijk.media.player.HttpCallback
        public void callback(int i, String str) {
            StringBuilder sb = new StringBuilder("h5 ");
            sb.append(this.f1521a.toString());
            sb.append(" result:");
            sb.append(i);
            sb.append(" ");
            sb.append(str != null ? str : Const.EMPTY);
            LogUtil.LOGF("DecoderConfig", sb.toString());
            if (i == 200) {
                try {
                    SwitcherConfig.setSwitcherInt(this.b, "enable_hd5", new JSONObject(str).getInt("enablehd"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class c implements HttpCallback {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ StringBuilder f1522a;
        final /* synthetic */ Context b;

        c(StringBuilder sb, Context context) {
            this.f1522a = sb;
            this.b = context;
        }

        @Override // tv.danmaku.ijk.media.player.HttpCallback
        public void callback(int i, String str) {
            StringBuilder sb = new StringBuilder("report ");
            sb.append(this.f1522a.toString());
            sb.append(" result:");
            sb.append(i);
            sb.append(" ");
            if (str == null) {
                str = Const.EMPTY;
            }
            sb.append(str);
            LogUtil.LOGF("DecoderConfig", sb.toString());
            if (i == 200) {
                SwitcherConfig.setSwitcherInt(this.b, tv.danmaku.ijk.media.player.DecoderConfig.KEY_REPORT_HD, 1);
            }
        }
    }

    public static boolean H265MediaCodecEnable(Context context) {
        return b != null && SwitcherConfig.getSwitcherValueInt(context, "enable_hd5") == 1;
    }

    public static void Init(Context context, boolean z, boolean z2) throws NoSuchMethodException, SecurityException {
        DeviceConfig.isTV = z2;
        a(context, z, z2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:43:0x00c5, code lost:
    
        r4 = true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void a(android.content.Context r16, boolean r17, boolean r18) throws java.lang.NoSuchMethodException, java.lang.SecurityException {
        /*
            Method dump skipped, instructions count: 365
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.cc.ccplayerwrapper.DecoderConfig.a(android.content.Context, boolean, boolean):void");
    }

    public static String getCPUModel(Context context) throws NoSuchMethodException, SecurityException {
        try {
            if (!TextUtils.isEmpty(SwitcherConfig.getSwitcherValueString(context, tv.danmaku.ijk.media.player.DecoderConfig.KEY_CPU_MODEL))) {
                return "null";
            }
            Method declaredMethod = Build.class.getDeclaredMethod("getString", String.class);
            declaredMethod.setAccessible(true);
            String str = (String) declaredMethod.invoke(null, "ro.arch");
            if ("unknown".equals(str)) {
                str = (String) declaredMethod.invoke(null, "ro.board.platform");
            }
            SwitcherConfig.setSwicherString(context, tv.danmaku.ijk.media.player.DecoderConfig.KEY_CPU_MODEL, str);
            return str;
        } catch (Exception e2) {
            e2.printStackTrace();
            return "exception";
        }
    }

    public static String getGpu() {
        return "nouse";
    }

    public static boolean isMediaCodecEnable(Context context) {
        return (f1519a == null || SwitcherConfig.getSwitcherValueInt(context, "enable_hd4", -1) == 0) ? false : true;
    }
}