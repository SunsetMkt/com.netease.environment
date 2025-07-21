package com.netease.ntunisdk.zxing.client.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.netease.ntunisdk.SdkQRCode;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.langutil.LanguageUtil;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.zxing.client.android.camera.CameraManager;

/* loaded from: classes.dex */
public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 2;
    private static final int CORNER_RECT_HEIGHT = 40;
    private static final int CORNER_RECT_WIDTH = 8;
    private static final int SCANNER_LINE_HEIGHT = 10;
    private static final int SCANNER_LINE_MOVE_DISTANCE = 5;
    private static int frameBottom;
    public static int scannerEnd;
    public static int scannerStart;
    private CameraManager cameraManager;
    private final int cornerColor;
    private boolean down;
    private final int frameColor;
    private String labelText;
    private final int labelTextColor;
    private final float labelTextSize;
    private final int laserColor;
    private final int maskColor;
    private final Paint paint;

    public ViewfinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.down = true;
        this.laserColor = getResColor(context, "ntunisdk_laser_color");
        this.cornerColor = getResColor(context, "ntunisdk_corner_color");
        this.frameColor = getResColor(context, "ntunisdk_viewfinder_frame");
        this.maskColor = getResColor(context, "ntunisdk_viewfinder_mask");
        this.labelTextColor = getResColor(context, "ntunisdk_head_text");
        if (SdkMgr.getInst() != null) {
            this.labelText = SdkMgr.getInst().getPropStr(SdkQRCode.QRCODE_SCAN_TIPS);
        }
        if (TextUtils.isEmpty(this.labelText)) {
            this.labelText = getResString(context, "ntunisdk_scan_tips");
        }
        this.labelTextSize = getResources().getDimension(getResources().getIdentifier("ntunisdk_camera_text_size", ResIdReader.RES_TYPE_DIMEN, context.getPackageName()));
        Paint paint = new Paint();
        this.paint = paint;
        paint.setAntiAlias(true);
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        Rect framingRect;
        int i;
        CameraManager cameraManager = this.cameraManager;
        if (cameraManager == null || (framingRect = cameraManager.getFramingRect()) == null) {
            return;
        }
        if (scannerStart == 0 || (i = scannerEnd) == 0 || i != framingRect.bottom) {
            scannerStart = framingRect.top;
            scannerEnd = framingRect.bottom;
        }
        drawExterior(canvas, framingRect, canvas.getWidth(), canvas.getHeight());
        drawFrame(canvas, framingRect);
        drawCorner(canvas, framingRect);
        drawLaserScanner(canvas, framingRect);
        postInvalidateDelayed(2L, framingRect.left, framingRect.top, framingRect.right, framingRect.bottom);
    }

    private void drawTextInfo(Canvas canvas, Rect rect) {
        StaticLayout staticLayout;
        if (TextUtils.isEmpty(this.labelText)) {
            return;
        }
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(this.labelTextColor);
        textPaint.setTextSize(this.labelTextSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        if (Build.VERSION.SDK_INT >= 23) {
            String str = this.labelText;
            staticLayout = StaticLayout.Builder.obtain(str, 0, str.length(), textPaint, canvas.getWidth()).setEllipsize(TextUtils.TruncateAt.END).setMaxLines(2).build();
        } else {
            String str2 = this.labelText;
            staticLayout = new StaticLayout(str2, 0, str2.length(), textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true, TextUtils.TruncateAt.END, canvas.getWidth());
        }
        canvas.save();
        canvas.translate(rect.left + (rect.width() / 2.0f), rect.bottom + 10);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    private void drawCorner(Canvas canvas, Rect rect) {
        this.paint.setColor(this.cornerColor);
        canvas.drawRect(rect.left, rect.top, rect.left + 8, rect.top + 40, this.paint);
        canvas.drawRect(rect.left, rect.top, rect.left + 40, rect.top + 8, this.paint);
        canvas.drawRect(rect.right - 8, rect.top, rect.right, rect.top + 40, this.paint);
        canvas.drawRect(rect.right - 40, rect.top, rect.right, rect.top + 8, this.paint);
        canvas.drawRect(rect.left, rect.bottom - 8, rect.left + 40, rect.bottom, this.paint);
        canvas.drawRect(rect.left, rect.bottom - 40, rect.left + 8, rect.bottom, this.paint);
        canvas.drawRect(rect.right - 8, rect.bottom - 40, rect.right, rect.bottom, this.paint);
        canvas.drawRect(rect.right - 40, rect.bottom - 8, rect.right, rect.bottom, this.paint);
    }

    private void drawLaserScanner(Canvas canvas, Rect rect) {
        this.paint.setColor(this.laserColor);
        float fWidth = rect.left + (rect.width() / 2);
        float f = scannerStart + 5;
        int i = this.laserColor;
        this.paint.setShader(new RadialGradient(fWidth, f, 360.0f, i, shadeColor(i), Shader.TileMode.MIRROR));
        if (this.down) {
            if (scannerStart <= rect.bottom) {
                canvas.drawOval(new RectF(rect.left + 20, scannerStart, rect.right - 20, scannerStart + 10), this.paint);
                scannerStart += 5;
            } else {
                this.down = false;
            }
        } else if (scannerStart >= rect.top) {
            canvas.drawOval(new RectF(rect.left + 20, scannerStart, rect.right - 20, scannerStart + 10), this.paint);
            scannerStart -= 5;
        } else {
            this.down = true;
        }
        this.paint.setShader(null);
    }

    public int shadeColor(int i) {
        return Integer.valueOf("20" + Integer.toHexString(i).substring(2), 16).intValue();
    }

    private void drawFrame(Canvas canvas, Rect rect) {
        this.paint.setColor(this.frameColor);
        canvas.drawRect(rect.left, rect.top, rect.right + 1, rect.top + 2, this.paint);
        canvas.drawRect(rect.left, rect.top + 2, rect.left + 2, rect.bottom - 1, this.paint);
        canvas.drawRect(rect.right - 1, rect.top, rect.right + 1, rect.bottom - 1, this.paint);
        canvas.drawRect(rect.left, rect.bottom - 1, rect.right + 1, rect.bottom + 1, this.paint);
    }

    private void drawExterior(Canvas canvas, Rect rect, int i, int i2) {
        this.paint.setColor(this.maskColor);
        float f = i;
        canvas.drawRect(0.0f, 0.0f, f, rect.top, this.paint);
        canvas.drawRect(0.0f, rect.top, rect.left, rect.bottom + 1, this.paint);
        canvas.drawRect(rect.right + 1, rect.top, f, rect.bottom + 1, this.paint);
        canvas.drawRect(0.0f, rect.bottom + 1, f, i2, this.paint);
    }

    public void drawViewfinder() {
        invalidate();
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return ZoomHandler.handleTouchEvent(motionEvent);
    }

    private static int getResColor(Context context, String str) {
        return getResColor(context, str, "color");
    }

    private static int getResColor(Context context, String str, String str2) {
        return context.getResources().getColor(context.getResources().getIdentifier(str, str2, context.getPackageName()));
    }

    private static String getResString(Context context, String str) {
        return LanguageUtil.getString(context, context.getResources().getIdentifier(str, ResIdReader.RES_TYPE_STRING, context.getPackageName()));
    }
}