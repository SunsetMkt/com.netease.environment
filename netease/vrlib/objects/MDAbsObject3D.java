package com.netease.vrlib.objects;

import android.content.Context;
import android.opengl.GLES20;
import android.util.SparseArray;
import com.netease.vrlib.MD360Program;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/* loaded from: classes5.dex */
public abstract class MDAbsObject3D {
    private static final int sPositionDataSize = 3;
    private static final int sTextureCoordinateDataSize = 2;
    private ShortBuffer mIndicesBuffer;
    private int mNumIndices;
    private SparseArray<FloatBuffer> mTexCoordinateBuffers = new SparseArray<>(2);
    private SparseArray<FloatBuffer> mVerticesBuffers = new SparseArray<>(2);

    protected abstract void executeLoad(Context context);

    public void uploadVerticesBufferIfNeed(MD360Program mD360Program, int i) {
        FloatBuffer verticesBuffer = getVerticesBuffer(i);
        if (verticesBuffer == null) {
            return;
        }
        verticesBuffer.position(0);
        int positionHandle = mD360Program.getPositionHandle();
        GLES20.glVertexAttribPointer(positionHandle, 3, 5126, false, 0, (Buffer) verticesBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);
    }

    public void uploadTexCoordinateBufferIfNeed(MD360Program mD360Program, int i) {
        FloatBuffer texCoordinateBuffer = getTexCoordinateBuffer(i);
        if (texCoordinateBuffer == null) {
            return;
        }
        texCoordinateBuffer.position(0);
        int textureCoordinateHandle = mD360Program.getTextureCoordinateHandle();
        GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, 5126, false, 0, (Buffer) texCoordinateBuffer);
        GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
    }

    public int getNumIndices() {
        return this.mNumIndices;
    }

    public void setNumIndices(int i) {
        this.mNumIndices = i;
    }

    public FloatBuffer getVerticesBuffer(int i) {
        return this.mVerticesBuffers.get(i);
    }

    public void setVerticesBuffer(int i, FloatBuffer floatBuffer) {
        this.mVerticesBuffers.put(i, floatBuffer);
    }

    public FloatBuffer getTexCoordinateBuffer(int i) {
        return this.mTexCoordinateBuffers.get(i);
    }

    public void setTexCoordinateBuffer(int i, FloatBuffer floatBuffer) {
        this.mTexCoordinateBuffers.put(i, floatBuffer);
    }

    public ShortBuffer getIndicesBuffer() {
        return this.mIndicesBuffer;
    }

    public void setIndicesBuffer(ShortBuffer shortBuffer) {
        this.mIndicesBuffer = shortBuffer;
    }

    public void draw() {
        if (getIndicesBuffer() != null) {
            getIndicesBuffer().position(0);
            GLES20.glDrawElements(4, getNumIndices(), 5123, getIndicesBuffer());
        } else {
            GLES20.glDrawArrays(4, 0, getNumIndices());
        }
    }
}