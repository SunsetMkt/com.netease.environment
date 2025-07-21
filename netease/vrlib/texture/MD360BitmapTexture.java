package com.netease.vrlib.texture;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import com.netease.vrlib.MD360Program;
import com.netease.vrlib.MDVRLibrary;
import com.netease.vrlib.common.GLUtil;
import com.netease.vrlib.common.MDMainHandler;
import com.netease.vrlib.common.VRUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes5.dex */
public class MD360BitmapTexture extends MD360Texture {
    private static final String TAG = "MD360BitmapTexture";
    private AsyncCallback callback;
    private MDVRLibrary.IBitmapProvider mBitmapProvider;
    private Map<String, AsyncCallback> mCallbackList = new HashMap();
    private boolean mIsReady;

    public interface Callback {
        void texture(Bitmap bitmap);
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    public void release() {
    }

    public MD360BitmapTexture(MDVRLibrary.IBitmapProvider iBitmapProvider) {
        this.mBitmapProvider = iBitmapProvider;
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    protected int createTextureId() {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        int i = iArr[0];
        this.callback = new AsyncCallback();
        this.mCallbackList.put(Thread.currentThread().toString(), this.callback);
        MDMainHandler.sharedHandler().post(new Runnable() { // from class: com.netease.vrlib.texture.MD360BitmapTexture.1
            AnonymousClass1() {
            }

            @Override // java.lang.Runnable
            public void run() {
                MD360BitmapTexture.this.mBitmapProvider.onProvideBitmap(MD360BitmapTexture.this.callback);
            }
        });
        return i;
    }

    /* renamed from: com.netease.vrlib.texture.MD360BitmapTexture$1 */
    class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            MD360BitmapTexture.this.mBitmapProvider.onProvideBitmap(MD360BitmapTexture.this.callback);
        }
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    public boolean texture(MD360Program mD360Program) {
        AsyncCallback asyncCallback = this.mCallbackList.get(Thread.currentThread().toString());
        int currentTextureId = getCurrentTextureId();
        if (asyncCallback != null && asyncCallback.hasBitmap()) {
            textureInThread(currentTextureId, mD360Program, asyncCallback.getBitmap());
            asyncCallback.releaseBitmap();
            this.mIsReady = true;
        }
        if (isReady() && currentTextureId != 0) {
            GLES20.glActiveTexture(33984);
            GLES20.glBindTexture(3553, currentTextureId);
            GLES20.glUniform1i(mD360Program.getTextureUniformHandle(), 0);
        }
        return true;
    }

    /* renamed from: com.netease.vrlib.texture.MD360BitmapTexture$2 */
    class AnonymousClass2 implements Runnable {
        AnonymousClass2() {
        }

        @Override // java.lang.Runnable
        public void run() {
            MD360BitmapTexture.this.mBitmapProvider.onProvideBitmap(MD360BitmapTexture.this.callback);
        }
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    public void notifyChanged() {
        MDMainHandler.sharedHandler().post(new Runnable() { // from class: com.netease.vrlib.texture.MD360BitmapTexture.2
            AnonymousClass2() {
            }

            @Override // java.lang.Runnable
            public void run() {
                MD360BitmapTexture.this.mBitmapProvider.onProvideBitmap(MD360BitmapTexture.this.callback);
            }
        });
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    public boolean isReady() {
        return this.mIsReady;
    }

    @Override // com.netease.vrlib.texture.MD360Texture
    public void destroy() {
        Iterator<AsyncCallback> it = this.mCallbackList.values().iterator();
        while (it.hasNext()) {
            it.next().releaseBitmap();
        }
        this.mCallbackList.clear();
    }

    private void textureInThread(int i, MD360Program mD360Program, Bitmap bitmap) {
        VRUtil.notNull(bitmap, "bitmap can't be null!");
        if (isEmpty(i)) {
            return;
        }
        GLES20.glActiveTexture(33984);
        GLUtil.glCheck("MD360BitmapTexture glActiveTexture");
        GLES20.glBindTexture(3553, i);
        GLUtil.glCheck("MD360BitmapTexture glBindTexture");
        GLES20.glTexParameteri(3553, 10241, 9728);
        GLES20.glTexParameteri(3553, 10240, 9728);
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        GLUtil.glCheck("MD360BitmapTexture texImage2D");
        GLES20.glUniform1i(mD360Program.getTextureUniformHandle(), 0);
        GLUtil.glCheck("MD360BitmapTexture textureInThread");
    }

    private static class AsyncCallback implements Callback {
        private Bitmap bitmap;

        private AsyncCallback() {
        }

        /* synthetic */ AsyncCallback(AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // com.netease.vrlib.texture.MD360BitmapTexture.Callback
        public void texture(Bitmap bitmap) {
            this.bitmap = bitmap.copy(bitmap.getConfig(), true);
        }

        public Bitmap getBitmap() {
            return this.bitmap;
        }

        public boolean hasBitmap() {
            return this.bitmap != null;
        }

        public synchronized void releaseBitmap() {
            Bitmap bitmap = this.bitmap;
            if (bitmap != null && !bitmap.isRecycled()) {
                this.bitmap.recycle();
            }
            this.bitmap = null;
        }
    }
}