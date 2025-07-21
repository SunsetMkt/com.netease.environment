package com.netease.ntunisdk.zxing.client.android;

import android.R;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import com.facebook.imagepipeline.common.RotationOptions;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.netease.ntunisdk.SdkQRCode;
import com.netease.ntunisdk.UniAlertDialog;
import com.netease.ntunisdk.base.ConstProp;
import com.netease.ntunisdk.base.SdkMgr;
import com.netease.ntunisdk.base.UniSdkUtils;
import com.netease.ntunisdk.imageutil.ImageUtil;
import com.netease.ntunisdk.modules.ngwebviewgeneral.ui.ResIdReader;
import com.netease.ntunisdk.modules.personalinfolist.HookManager;
import com.netease.ntunisdk.zxing.client.android.camera.CameraManager;
import com.netease.ntunisdk.zxing.utils.Util;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/* loaded from: classes.dex */
public class CaptureActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {
    private static final float BEEP_VOLUME = 0.1f;
    public static final String INTENT_EXTRA_KEY_QR_ALBUM = "qr_album_result";
    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";
    public static final String INTENT_EXTRA_NO_PERMISSION = "no_permission";
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 31126;
    private static final int REQUEST_CODE_START_PICK_IMAGE = 104;
    public static final int RESULT_CODE_QR_ALBUM = 162;
    public static final int RESULT_CODE_QR_SCAN = 161;
    private static final String TAG = "UniQR act";
    private static final long VIBRATE_DURATION = 200;
    private static boolean isTurnOffSensor;
    private ImageView back;
    private CameraManager cameraManager;
    private String characterSet;
    private Vector<BarcodeFormat> decodeFormats;
    private Map<DecodeHintType, ?> decodeHints;
    private boolean flashOpened;
    private CaptureActivityHandler handler;
    private boolean hasSurface;
    private int headHeight;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private ImageView openAlbumIv;
    private ImageView openFlashIv;
    private OrientationDetector orientationDetector;
    private boolean playBeep;
    private View rootView;
    private TextView scanTips;
    private SurfaceView surfaceView;
    private boolean vibrate;
    private ViewfinderView viewfinderView;
    private int scrOrientation = 0;
    private boolean doChangeCameraOrientation = true;
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.1
        AnonymousClass1() {
        }

        @Override // android.media.MediaPlayer.OnCompletionListener
        public void onCompletion(MediaPlayer mediaPlayer) throws IllegalStateException {
            mediaPlayer.seekTo(0);
        }
    };
    private boolean isPortrait = false;

    /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$1 */
    class AnonymousClass1 implements MediaPlayer.OnCompletionListener {
        AnonymousClass1() {
        }

        @Override // android.media.MediaPlayer.OnCompletionListener
        public void onCompletion(MediaPlayer mediaPlayer) throws IllegalStateException {
            mediaPlayer.seekTo(0);
        }
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        Window window = getWindow();
        if (window != null) {
            window.requestFeature(1);
            window.addFlags(1024);
        }
        super.onCreate(bundle);
        UniSdkUtils.i(TAG, "onCreate...");
        if (getWindow() != null) {
            Util.hideSystemUI(getWindow());
        }
        setOrientation();
        if (isPortrait()) {
            setContentView(Util.getResId(this, "ntunisdk_scanner_port", ResIdReader.RES_TYPE_LAYOUT));
        } else {
            setContentView(Util.getResId(this, "ntunisdk_scanner_land", ResIdReader.RES_TYPE_LAYOUT));
        }
        this.rootView = findViewById(R.id.content);
        this.openFlashIv = (ImageView) findViewById(Util.getResId(this, "open_flash_btn", ResIdReader.RES_TYPE_ID));
        this.openAlbumIv = (ImageView) findViewById(Util.getResId(this, "open_album_btn", ResIdReader.RES_TYPE_ID));
        if (SdkMgr.getInst().getPropInt(SdkQRCode.HIDE_QRCODE_FLASH_BTN, 0) == 1) {
            this.openFlashIv.setVisibility(8);
        } else {
            this.openFlashIv.setOnClickListener(this);
        }
        boolean z = getIntent().getIntExtra(SdkQRCode.REMOVE_JUMP_ALBUM, 0) == 1;
        if (SdkMgr.getInst().getPropInt(SdkQRCode.HIDE_QRCODE_ALBUM_BTN, 0) == 1 || z) {
            this.openAlbumIv.setVisibility(8);
        } else {
            this.openAlbumIv.setOnClickListener(this);
        }
        TextView textView = (TextView) findViewById(Util.getResId(this, "ntunisdk_scan_tips", ResIdReader.RES_TYPE_ID));
        this.scanTips = textView;
        textView.setVisibility(0);
        String propStr = SdkMgr.getInst() != null ? SdkMgr.getInst().getPropStr(SdkQRCode.QRCODE_SCAN_TIPS) : "";
        if (TextUtils.isEmpty(propStr)) {
            propStr = Util.getLocalString(this, "ntunisdk_scan_tips");
        }
        this.scanTips.setText(propStr);
        ImageView imageView = (ImageView) findViewById(Util.getResId(this, "ntunisdk_scanner_toolbar_back", ResIdReader.RES_TYPE_ID));
        this.back = imageView;
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.2
            AnonymousClass2() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CaptureActivity.this.finish();
            }
        });
        this.hasSurface = false;
        this.inactivityTimer = new InactivityTimer(this);
        FrameLayout frameLayout = (FrameLayout) findViewById(Util.getResId(this, "ntunisdk_head_rlt", ResIdReader.RES_TYPE_ID));
        Util.adaptNotch(this, isPortrait(), new View[]{frameLayout});
        frameLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.3
            final /* synthetic */ FrameLayout val$headRlt;

            AnonymousClass3(FrameLayout frameLayout2) {
                frameLayout = frameLayout2;
            }

            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                CaptureActivity.this.headHeight = frameLayout.getHeight();
                UniSdkUtils.i(CaptureActivity.TAG, "headHeight = " + CaptureActivity.this.headHeight);
                frameLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
        ((TextView) findViewById(Util.getResId(this, "ntunisdk_scanner_toolbar_title", ResIdReader.RES_TYPE_ID))).setText(Util.getLocalString(this, "ntunisdk_scan_head_tv"));
        boolean booleanExtra = getIntent().getBooleanExtra(SdkQRCode.TURN_OFF_SENSOR, false);
        isTurnOffSensor = booleanExtra;
        if (!booleanExtra) {
            this.orientationDetector = new OrientationDetector();
        }
        this.doChangeCameraOrientation = 5 == this.scrOrientation;
        checkShowLatestImage();
    }

    /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$2 */
    class AnonymousClass2 implements View.OnClickListener {
        AnonymousClass2() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            CaptureActivity.this.finish();
        }
    }

    /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$3 */
    class AnonymousClass3 implements ViewTreeObserver.OnPreDrawListener {
        final /* synthetic */ FrameLayout val$headRlt;

        AnonymousClass3(FrameLayout frameLayout2) {
            frameLayout = frameLayout2;
        }

        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            CaptureActivity.this.headHeight = frameLayout.getHeight();
            UniSdkUtils.i(CaptureActivity.TAG, "headHeight = " + CaptureActivity.this.headHeight);
            frameLayout.getViewTreeObserver().removeOnPreDrawListener(this);
            return true;
        }
    }

    private void setOrientation() {
        this.isPortrait = false;
        int intExtra = getIntent().getIntExtra(ConstProp.SCR_ORIENTATION, 0);
        this.scrOrientation = intExtra;
        if (intExtra == 1) {
            this.isPortrait = true;
            setRequestedOrientation(1);
        } else if (intExtra == 2) {
            setRequestedOrientation(0);
        } else if (intExtra == 5) {
            setRequestedOrientation(6);
        } else {
            this.isPortrait = getResources().getConfiguration().orientation == 1;
        }
    }

    @Override // android.app.Activity
    protected void onResume() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        super.onResume();
        UniSdkUtils.i(TAG, "onResume...");
        Util.hideSystemUI(getWindow());
        this.flashOpened = false;
        this.openFlashIv.setImageResource(Util.getResId(this, "ntunisdk_open_flash", ResIdReader.RES_TYPE_DRAWABLE));
        if (this.cameraManager == null) {
            this.cameraManager = new CameraManager(this);
            ViewfinderView viewfinderView = (ViewfinderView) findViewById(Util.getResId(this, "ntunisdk_viewfinder_content", ResIdReader.RES_TYPE_ID));
            this.viewfinderView = viewfinderView;
            viewfinderView.setCameraManager(this.cameraManager);
        }
        SurfaceView surfaceView = (SurfaceView) findViewById(Util.getResId(this, "ntunisdk_scanner_view", ResIdReader.RES_TYPE_ID));
        this.surfaceView = surfaceView;
        SurfaceHolder holder = surfaceView.getHolder();
        if (this.hasSurface) {
            initCamera(holder);
        } else {
            holder.addCallback(this);
            holder.setType(3);
        }
        this.decodeFormats = null;
        this.characterSet = null;
        this.playBeep = true;
        if (((AudioManager) getSystemService("audio")).getRingerMode() != 2) {
            this.playBeep = false;
        }
        initBeepSound();
        this.vibrate = true;
        Intent intent = getIntent();
        if (intent != null) {
            this.decodeHints = DecodeHintManager.parseDecodeHints(intent);
        }
        OrientationDetector orientationDetector = this.orientationDetector;
        if (orientationDetector != null) {
            orientationDetector.enable();
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        UniSdkUtils.i(TAG, "onWindowFocusChanged... " + z);
        CameraManager cameraManager = this.cameraManager;
        if (cameraManager != null) {
            cameraManager.autoFocus();
        }
        Util.hideSystemUI(getWindow());
    }

    private void checkShowLatestImage() {
        if (!(SdkMgr.getInst().getPropInt(SdkQRCode.ENABLE_POPUP_QR_PIC, 1) == 1)) {
            UniSdkUtils.d(TAG, "enableImagePopup FALSE");
            return;
        }
        String recentImagePath = ImageUtil.getRecentImagePath(this, 5);
        if (TextUtils.isEmpty(recentImagePath)) {
            return;
        }
        Util.scanningImage(recentImagePath, new Util.ScanImageCallback() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.4
            final /* synthetic */ String val$path;

            AnonymousClass4(String recentImagePath2) {
                str = recentImagePath2;
            }

            @Override // com.netease.ntunisdk.zxing.utils.Util.ScanImageCallback
            public void onSuccess(String str) {
                UniSdkUtils.d(CaptureActivity.TAG, "scanningImage SUCCESS");
                CaptureActivity.this.showImagePopupWindow(str, str);
            }

            @Override // com.netease.ntunisdk.zxing.utils.Util.ScanImageCallback
            public void onFailed() {
                UniSdkUtils.d(CaptureActivity.TAG, "scanningImage FAILED");
            }
        });
    }

    /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$4 */
    class AnonymousClass4 implements Util.ScanImageCallback {
        final /* synthetic */ String val$path;

        AnonymousClass4(String recentImagePath2) {
            str = recentImagePath2;
        }

        @Override // com.netease.ntunisdk.zxing.utils.Util.ScanImageCallback
        public void onSuccess(String str) {
            UniSdkUtils.d(CaptureActivity.TAG, "scanningImage SUCCESS");
            CaptureActivity.this.showImagePopupWindow(str, str);
        }

        @Override // com.netease.ntunisdk.zxing.utils.Util.ScanImageCallback
        public void onFailed() {
            UniSdkUtils.d(CaptureActivity.TAG, "scanningImage FAILED");
        }
    }

    public void showImagePopupWindow(String str, String str2) {
        if (ImageUtil.isFinishing(this)) {
            UniSdkUtils.i(TAG, this + " is finishing");
            return;
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.5
            final /* synthetic */ String val$path;
            final /* synthetic */ String val$result;

            AnonymousClass5(String str3, String str22) {
                str = str3;
                str = str22;
            }

            /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$5$1 */
            class AnonymousClass1 implements View.OnClickListener {
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    CaptureActivity.this.retQrScanResult(str);
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                CaptureActivity captureActivity = CaptureActivity.this;
                ImageUtil.showImagePopupWindow(captureActivity, str, captureActivity.openAlbumIv, new View.OnClickListener() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.5.1
                    AnonymousClass1() {
                    }

                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        CaptureActivity.this.retQrScanResult(str);
                    }
                });
            }
        }, 0L);
    }

    /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$5 */
    class AnonymousClass5 implements Runnable {
        final /* synthetic */ String val$path;
        final /* synthetic */ String val$result;

        AnonymousClass5(String str3, String str22) {
            str = str3;
            str = str22;
        }

        /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$5$1 */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                CaptureActivity.this.retQrScanResult(str);
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            CaptureActivity captureActivity = CaptureActivity.this;
            ImageUtil.showImagePopupWindow(captureActivity, str, captureActivity.openAlbumIv, new View.OnClickListener() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.5.1
                AnonymousClass1() {
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    CaptureActivity.this.retQrScanResult(str);
                }
            });
        }
    }

    public void retQrScanResult(String str) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, str);
        intent.putExtras(bundle);
        setResult(RESULT_CODE_QR_SCAN, intent);
        finish();
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        UniSdkUtils.i(TAG, "onPause...");
        CaptureActivityHandler captureActivityHandler = this.handler;
        if (captureActivityHandler != null) {
            captureActivityHandler.quitSynchronously();
            this.handler = null;
        }
        this.cameraManager.closeDriver();
        OrientationDetector orientationDetector = this.orientationDetector;
        if (orientationDetector != null) {
            orientationDetector.disable();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            int width = this.surfaceView.getWidth();
            int height = this.surfaceView.getHeight() + this.headHeight;
            Point screenSize = Util.getScreenSize(this);
            UniSdkUtils.d(TAG, "initCamera displayView: " + width + ", " + height + ", screenSize: " + screenSize);
            double d = (double) (width * height);
            double d2 = screenSize.x;
            Double.isNaN(d2);
            double d3 = d2 * 0.85d;
            double d4 = screenSize.y;
            Double.isNaN(d4);
            if (d < d3 * d4) {
                UniSdkUtils.d(TAG, "initCamera openDriver with fixed size");
                this.cameraManager.openDriver(surfaceHolder, new Point(width, height));
            } else {
                this.cameraManager.openDriver(surfaceHolder, null);
            }
            if (this.handler == null) {
                this.handler = new CaptureActivityHandler(this, this.decodeFormats, this.decodeHints, this.characterSet, this.cameraManager);
            }
        } catch (Exception e) {
            UniSdkUtils.e(TAG, "initCamera exception:" + e.getMessage());
            retQrScanNoPermission();
        }
    }

    private void retQrScanNoPermission() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_EXTRA_KEY_QR_SCAN, "");
        bundle.putInt(INTENT_EXTRA_NO_PERMISSION, 1);
        intent.putExtras(bundle);
        setResult(RESULT_CODE_QR_SCAN, intent);
        finish();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        UniSdkUtils.i(TAG, "surfaceCreated...");
        if (!this.hasSurface) {
            UniSdkUtils.i(TAG, "surfaceCreated, hasSurface is false...");
            this.hasSurface = true;
            initCamera(surfaceHolder);
        }
        fixPreView(surfaceHolder);
        calculateBtnsLayout();
    }

    private void fixPreView(SurfaceHolder surfaceHolder) {
        try {
            if (this.surfaceView != null) {
                ViewGroup.LayoutParams layoutParams = this.surfaceView.getLayoutParams();
                Point cameraResolution = this.cameraManager.getCameraResolution();
                Point screenResolution = this.cameraManager.getScreenResolution();
                UniSdkUtils.d(TAG, "cameraPoint: " + cameraResolution + ", screenPoint: " + screenResolution);
                if (screenResolution == null || cameraResolution == null || cameraResolution.equals(screenResolution.x, screenResolution.y) || cameraResolution.equals(screenResolution.y, screenResolution.x)) {
                    return;
                }
                if (isPortrait()) {
                    layoutParams.width = screenResolution.x;
                    layoutParams.height = Math.min((int) (cameraResolution.x * (screenResolution.x / cameraResolution.y)), screenResolution.y);
                } else {
                    layoutParams.width = Math.min((int) (cameraResolution.x * (screenResolution.y / cameraResolution.y)), screenResolution.x);
                    layoutParams.height = screenResolution.y;
                }
                UniSdkUtils.d(TAG, "holder size, width: " + layoutParams.width + ", height: " + layoutParams.height);
                surfaceHolder.setFixedSize(layoutParams.width, layoutParams.height);
            }
        } catch (Exception e) {
            UniSdkUtils.i(TAG, "fixPreView : " + e.getMessage());
        }
    }

    private void calculateBtnsLayout() {
        Rect framingRect = this.cameraManager.getFramingRect();
        if (framingRect == null) {
            return;
        }
        UniSdkUtils.i(TAG, "FramingRect : " + framingRect);
        Point screenResolution = this.cameraManager.getScreenResolution();
        if (screenResolution == null) {
            return;
        }
        UniSdkUtils.i(TAG, "ScreenResolution : " + screenResolution);
        LinearLayout linearLayout = (LinearLayout) findViewById(Util.getResId(this, "btns_layout", ResIdReader.RES_TYPE_ID));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
        int i = layoutParams.width;
        int i2 = layoutParams.height;
        UniSdkUtils.i(TAG, "btnsLayoutWidth : " + i);
        UniSdkUtils.i(TAG, "btnsLayoutHeight : " + i2);
        if (isPortrait()) {
            int i3 = ((screenResolution.y - (framingRect.bottom - framingRect.top)) - framingRect.top) - i2;
            if (i3 > 0) {
                layoutParams.bottomMargin = i3 > 400 ? i3 + IMediaPlayer.MEDIA_ERROR_START_PLAY_UNKNOW : i3 / 2;
            } else {
                layoutParams.bottomMargin = 0;
            }
            UniSdkUtils.i(TAG, "params.bottomMargin : " + layoutParams.bottomMargin);
        } else {
            int i4 = ((screenResolution.x - (framingRect.right - framingRect.left)) - framingRect.left) - i;
            if (Build.VERSION.SDK_INT >= 17) {
                if (i4 > 0) {
                    layoutParams.setMarginEnd(i4 > 160 ? i4 - 80 : i4 / 2);
                } else {
                    layoutParams.setMarginEnd(0);
                }
            }
            UniSdkUtils.i(TAG, "params.rightMargin : " + layoutParams.rightMargin);
        }
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setVisibility(0);
        if (this.viewfinderView == null || this.scanTips == null) {
            return;
        }
        int i5 = (framingRect.bottom - framingRect.top) / 2;
        UniSdkUtils.i(TAG, "scanTips height : " + i5);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.scanTips.getLayoutParams();
        UniSdkUtils.i(TAG, "params2 : " + layoutParams2.leftMargin + " " + layoutParams2.topMargin + " " + layoutParams2.rightMargin + " " + layoutParams2.bottomMargin);
        if (layoutParams2.topMargin < i5) {
            layoutParams2.setMargins(layoutParams2.leftMargin, layoutParams2.topMargin + i5, layoutParams2.rightMargin, layoutParams2.bottomMargin);
            this.scanTips.setLayoutParams(layoutParams2);
        }
        this.scanTips.setVisibility(0);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        UniSdkUtils.i(TAG, "surfaceChanged... " + i2 + ", " + i3);
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        UniSdkUtils.i(TAG, "surfaceDestroyed...");
        this.hasSurface = false;
    }

    Handler getHandler() {
        return this.handler;
    }

    CameraManager getCameraManager() {
        return this.cameraManager;
    }

    void handleDecode(Result result) throws IllegalStateException {
        this.inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String text = result.getText();
        UniSdkUtils.i(TAG, "Decode resultString : " + text);
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, Util.getLocalString(this, "ntunisdk_scan_fail"), 0).show();
            text = "";
        }
        retQrScanResult(text);
    }

    void drawViewfinder() {
        this.viewfinderView.drawViewfinder();
    }

    public int getHeadHeight() {
        return this.headHeight;
    }

    private void initBeepSound() throws IllegalStateException, Resources.NotFoundException, IOException, IllegalArgumentException {
        if (this.playBeep && this.mediaPlayer == null) {
            setVolumeControlStream(3);
            MediaPlayer mediaPlayer = new MediaPlayer();
            this.mediaPlayer = mediaPlayer;
            mediaPlayer.setAudioStreamType(3);
            this.mediaPlayer.setOnCompletionListener(this.beepListener);
            try {
                AssetFileDescriptor assetFileDescriptorOpenRawResourceFd = getResources().openRawResourceFd(Util.getResId(this, "ntunisdk_beep", "raw"));
                this.mediaPlayer.setDataSource(assetFileDescriptorOpenRawResourceFd.getFileDescriptor(), assetFileDescriptorOpenRawResourceFd.getStartOffset(), assetFileDescriptorOpenRawResourceFd.getLength());
                assetFileDescriptorOpenRawResourceFd.close();
                this.mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                this.mediaPlayer.prepare();
            } catch (Exception unused) {
                this.mediaPlayer = null;
            }
        }
    }

    private void playBeepSoundAndVibrate() throws IllegalStateException {
        MediaPlayer mediaPlayer;
        if (this.playBeep && (mediaPlayer = this.mediaPlayer) != null) {
            mediaPlayer.start();
        }
        if (this.vibrate) {
            ((Vibrator) getSystemService("vibrator")).vibrate(VIBRATE_DURATION);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.openFlashIv) {
            if (this.flashOpened) {
                this.cameraManager.setTorch(false);
                this.flashOpened = false;
                this.openFlashIv.setImageResource(Util.getResId(this, "ntunisdk_open_flash", ResIdReader.RES_TYPE_DRAWABLE));
                return;
            } else {
                this.cameraManager.setTorch(true);
                this.flashOpened = true;
                this.openFlashIv.setImageResource(Util.getResId(this, "ntunisdk_open_flash_selected", ResIdReader.RES_TYPE_DRAWABLE));
                return;
            }
        }
        if (view == this.openAlbumIv) {
            requestAlbumThenExecute(this);
        }
    }

    private static void requestAlbumThenExecute(Activity activity) {
        if (Util.isMediaPermissionGranted(activity)) {
            startPickLocalImage(activity);
            return;
        }
        if (SdkMgr.getInst().getPropInt(SdkQRCode.ENABLE_UNISDK_PERMISSION_TIPS, 0) != 0) {
            String propStr = SdkMgr.getInst().getPropStr(SdkQRCode.UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_TIPS);
            UniSdkUtils.d(TAG, "requestPermission,UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_TIPS:" + propStr);
            if (TextUtils.isEmpty(propStr)) {
                propStr = Util.getLocalString(activity, "ntunisdk_scan_request_external_storage");
            }
            new UniAlertDialog(activity).alert(propStr, Util.getLocalString(activity, "ntunisdk_scan_camera_permission_sure"), Util.getLocalString(activity, "ntunisdk_scan_camera_permission_cancel"), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.6
                final /* synthetic */ Activity val$activity;

                AnonymousClass6(Activity activity2) {
                    activity = activity2;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    Util.requestMediaPermission(activity, CaptureActivity.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            }, null, false);
            return;
        }
        Util.requestMediaPermission(activity2, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$6 */
    class AnonymousClass6 implements DialogInterface.OnClickListener {
        final /* synthetic */ Activity val$activity;

        AnonymousClass6(Activity activity2) {
            activity = activity2;
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            Util.requestMediaPermission(activity, CaptureActivity.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    private static void startPickLocalImage(Activity activity) {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
        } else {
            intent = new Intent("android.intent.action.GET_CONTENT", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        }
        HookManager.startActivityForResult(activity, intent, 104);
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (iArr.length > 0 && iArr[0] == 0) {
                startPickLocalImage(this);
                return;
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Util.getMediaPermission(this))) {
                String propStr = SdkMgr.getInst().getPropStr(SdkQRCode.UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_REFUSE_TIPS);
                UniSdkUtils.d(TAG, "requestPermission,UNISDK_EXTERNAL_STORAGE_PERMISSION_REFUSE_TIPS:" + propStr);
                if (TextUtils.isEmpty(propStr)) {
                    propStr = Util.getLocalString(this, "ntunisdk_scan_refuse_external_storage");
                }
                new UniAlertDialog(this).alert(propStr, Util.getLocalString(this, "ntunisdk_scan_permission_retry"), Util.getLocalString(this, "ntunisdk_scan_camera_permission_cancel"), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.7
                    AnonymousClass7() {
                    }

                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i2) {
                        Util.requestMediaPermission(CaptureActivity.this, CaptureActivity.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }, null, false);
                return;
            }
            String propStr2 = SdkMgr.getInst().getPropStr(SdkQRCode.UNISDK_QRCODE_EXTERNAL_STORAGE_PERMISSION_REFUSE_TIPS);
            UniSdkUtils.d(TAG, "requestPermission,UNISDK_EXTERNAL_STORAGE_PERMISSION_REFUSE_TIPS:" + propStr2);
            if (TextUtils.isEmpty(propStr2)) {
                propStr2 = Util.getLocalString(this, "ntunisdk_scan_refuse_external_storage");
            }
            new UniAlertDialog(this).alert(propStr2, Util.getLocalString(this, "ntunisdk_scan_permission_setting"), Util.getLocalString(this, "ntunisdk_scan_camera_permission_cancel"), new DialogInterface.OnClickListener() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.8
                AnonymousClass8() {
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", CaptureActivity.this.getPackageName(), null));
                    try {
                        CaptureActivity.this.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null, false);
        }
    }

    /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$7 */
    class AnonymousClass7 implements DialogInterface.OnClickListener {
        AnonymousClass7() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i2) {
            Util.requestMediaPermission(CaptureActivity.this, CaptureActivity.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$8 */
    class AnonymousClass8 implements DialogInterface.OnClickListener {
        AnonymousClass8() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i2) {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", CaptureActivity.this.getPackageName(), null));
            try {
                CaptureActivity.this.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        UniSdkUtils.i(TAG, "onActivityResult");
        if (104 == i) {
            if (i2 != -1) {
                UniSdkUtils.d(TAG, "pickImage FAILED");
                return;
            }
            String path = null;
            if (intent != null && intent.getData() != null) {
                path = Util.getPath(this, intent.getData());
            }
            if (TextUtils.isEmpty(path)) {
                return;
            }
            Util.scanningImage(path, new Util.ScanImageCallback() { // from class: com.netease.ntunisdk.zxing.client.android.CaptureActivity.9
                AnonymousClass9() {
                }

                @Override // com.netease.ntunisdk.zxing.utils.Util.ScanImageCallback
                public void onSuccess(String str) {
                    UniSdkUtils.d(CaptureActivity.TAG, "scanningImage SUCCESS");
                    CaptureActivity.this.retQrScanResult(str);
                }

                @Override // com.netease.ntunisdk.zxing.utils.Util.ScanImageCallback
                public void onFailed() {
                    UniSdkUtils.d(CaptureActivity.TAG, "scanningImage FAILED");
                    CaptureActivity captureActivity = CaptureActivity.this;
                    Util.toastMessage(captureActivity, Util.getLocalString(captureActivity, "ntunisdk_scan_not_valid_qr"));
                }
            });
        }
    }

    /* renamed from: com.netease.ntunisdk.zxing.client.android.CaptureActivity$9 */
    class AnonymousClass9 implements Util.ScanImageCallback {
        AnonymousClass9() {
        }

        @Override // com.netease.ntunisdk.zxing.utils.Util.ScanImageCallback
        public void onSuccess(String str) {
            UniSdkUtils.d(CaptureActivity.TAG, "scanningImage SUCCESS");
            CaptureActivity.this.retQrScanResult(str);
        }

        @Override // com.netease.ntunisdk.zxing.utils.Util.ScanImageCallback
        public void onFailed() {
            UniSdkUtils.d(CaptureActivity.TAG, "scanningImage FAILED");
            CaptureActivity captureActivity = CaptureActivity.this;
            Util.toastMessage(captureActivity, Util.getLocalString(captureActivity, "ntunisdk_scan_not_valid_qr"));
        }
    }

    public boolean isPortrait() {
        return this.isPortrait;
    }

    private class OrientationDetector extends OrientationEventListener {
        private int mDisplayRotation;

        private int getCameraDisplayOrientation(int i) {
            if (i == -1) {
                return -1;
            }
            if (i > 350 || i < 10) {
                return 0;
            }
            if (i > 70 && i < 110) {
                return 90;
            }
            if (i > 170 && i < 190) {
                return 180;
            }
            if (i <= 250 || i >= 290) {
                return -1;
            }
            return RotationOptions.ROTATE_270;
        }

        public OrientationDetector() {
            super(CaptureActivity.this);
            this.mDisplayRotation = -1;
        }

        @Override // android.view.OrientationEventListener
        public void onOrientationChanged(int i) {
            int cameraDisplayOrientation = getCameraDisplayOrientation(i);
            if (cameraDisplayOrientation == -1) {
                return;
            }
            int displayRotation = getDisplayRotation();
            boolean z = displayRotation != this.mDisplayRotation;
            this.mDisplayRotation = displayRotation;
            if (CaptureActivity.this.cameraManager != null && CaptureActivity.this.doChangeCameraOrientation && z) {
                CaptureActivity.this.cameraManager.setCameraDisplayOrientation(cameraDisplayOrientation);
                if (Build.VERSION.SDK_INT < 17 || !"1".equals(SdkMgr.getInst().getPropStr(ConstProp.ENABLE_RTL)) || CaptureActivity.this.rootView == null) {
                    return;
                }
                UniSdkUtils.i(CaptureActivity.TAG, "set RTL");
                Util.setViewTextDirection(CaptureActivity.this.rootView, 4);
                CaptureActivity.this.back.setImageResource(Util.getResId(CaptureActivity.this, "ntunisdk_back_reverse", ResIdReader.RES_TYPE_DRAWABLE));
            }
        }

        private int getDisplayRotationDegrees() {
            int i = this.mDisplayRotation;
            if (i == 1) {
                return 90;
            }
            if (i == 2) {
                return 180;
            }
            if (i != 3) {
                return 0;
            }
            return RotationOptions.ROTATE_270;
        }

        private int getDisplayRotation() {
            return CaptureActivity.this.getWindowManager().getDefaultDisplay().getRotation();
        }
    }
}