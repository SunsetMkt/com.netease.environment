package com.netease.ntunisdk.util.cutout;

import android.app.Activity;
import android.util.Log;
import com.xiaomi.gamecenter.sdk.web.webview.webkit.h;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class CutoutOppo implements CutoutInterface {
    private int bottom;
    private int left;
    private int right;
    private int top;

    private void getSizes(Activity activity) {
        if (this.left == 0 || this.right == 0 || this.bottom == 0) {
            try {
                Class<?> cls = Class.forName("android.os.SystemProperties");
                String[] strArrSplit = ((String) cls.getMethod(h.c, String.class).invoke(cls.newInstance(), "ro.oppo.screen.heteromorphism")).split(":");
                int length = strArrSplit.length;
                int i = 0;
                int i2 = 0;
                while (i < length) {
                    int i3 = i2;
                    for (String str : strArrSplit[i].split(",")) {
                        if (i3 == 0) {
                            this.left = Integer.parseInt(str.trim());
                        } else if (i3 == 1) {
                            this.top = Integer.parseInt(str.trim());
                        } else if (i3 == 2) {
                            this.right = Integer.parseInt(str.trim());
                        } else if (i3 == 3) {
                            this.bottom = Integer.parseInt(str.trim());
                        }
                        i3++;
                    }
                    i++;
                    i2 = i3;
                }
            } catch (ClassNotFoundException e) {
                Log.w("error", "get error() ", e);
            } catch (IllegalAccessException e2) {
                Log.w("error", "get error() ", e2);
            } catch (IllegalArgumentException e3) {
                Log.w("error", "get error() ", e3);
            } catch (InstantiationException e4) {
                Log.w("error", "get error() ", e4);
            } catch (NoSuchMethodException e5) {
                Log.w("error", "get error() ", e5);
            } catch (InvocationTargetException e6) {
                Log.w("error", "get error() ", e6);
            } catch (Throwable th) {
                Log.w("error", "get error() ", th);
            }
        }
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public boolean hasCutout(Activity activity) {
        return activity.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutWidthHeight(Activity activity) {
        getSizes(activity);
        return new int[]{this.right - this.left, this.bottom - this.top};
    }

    @Override // com.netease.ntunisdk.util.cutout.CutoutInterface
    public int[] getCutoutPosition(Activity activity) {
        return new int[]{this.left, this.top, this.right, this.bottom};
    }
}