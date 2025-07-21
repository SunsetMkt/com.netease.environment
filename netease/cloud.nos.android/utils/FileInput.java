package com.netease.cloud.nos.android.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.jose4j.jwk.RsaJsonWebKey;

/* loaded from: classes5.dex */
public class FileInput {
    private static final String LOGTAG = LogUtil.makeLogTag(FileInput.class);
    private final File file;
    private final String filename;
    private final RandomAccessFile randomAccessFile;

    public FileInput(File file) throws FileNotFoundException {
        this(file, null);
    }

    public FileInput(File file, String str) throws FileNotFoundException {
        this.file = file;
        this.randomAccessFile = new RandomAccessFile(file, RsaJsonWebKey.PRIME_FACTOR_OTHER_MEMBER_NAME);
        this.filename = (str == null || str.trim().length() <= 0) ? file.getName() : str;
    }

    public void delete() {
        File file = this.file;
        if (file != null) {
            file.delete();
        }
    }

    public void doClose() throws IOException {
        RandomAccessFile randomAccessFile = this.randomAccessFile;
        if (randomAccessFile != null) {
            try {
                randomAccessFile.close();
            } catch (IOException e) {
                LogUtil.e(LOGTAG, "close file exception", e);
            }
        }
    }

    public String getFilename() {
        return this.filename;
    }

    public long length() {
        return this.file.length();
    }

    public byte[] read(long j, int i) throws IOException {
        if (j == 0 && i == 0 && length() == 0) {
            return new byte[0];
        }
        if (j >= length()) {
            return null;
        }
        byte[] bArr = new byte[i];
        this.randomAccessFile.seek(j);
        this.randomAccessFile.read(bArr);
        return bArr;
    }
}