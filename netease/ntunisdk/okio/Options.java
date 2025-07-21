package com.netease.ntunisdk.okio;

import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

/* loaded from: classes3.dex */
public final class Options extends AbstractList<ByteString> implements RandomAccess {
    final ByteString[] byteStrings;
    final int[] trie;

    private Options(ByteString[] byteStringArr, int[] iArr) {
        this.byteStrings = byteStringArr;
        this.trie = iArr;
    }

    /* JADX WARN: Code restructure failed: missing block: B:106:0x009b, code lost:
    
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.netease.ntunisdk.okio.Options of(com.netease.ntunisdk.okio.ByteString... r12) {
        /*
            Method dump skipped, instructions count: 255
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.okio.Options.of(com.netease.ntunisdk.okio.ByteString[]):com.netease.ntunisdk.okio.Options");
    }

    private static void buildTrieRecursive(long j, Buffer buffer, int i, List<ByteString> list, int i2, int i3, List<Integer> list2) {
        int i4;
        ByteString byteString;
        int iIntValue;
        int i5;
        int i6;
        int i7;
        Buffer buffer2;
        if (i2 >= i3) {
            throw new AssertionError();
        }
        for (int i8 = i2; i8 < i3; i8++) {
            if (list.get(i8).size() < i) {
                throw new AssertionError();
            }
        }
        ByteString byteString2 = list.get(i2);
        ByteString byteString3 = list.get(i3 - 1);
        if (i == byteString2.size()) {
            iIntValue = list2.get(i2).intValue();
            int i9 = i2 + 1;
            byteString = list.get(i9);
            i4 = i9;
        } else {
            i4 = i2;
            byteString = byteString2;
            iIntValue = -1;
        }
        if (byteString.getByte(i) != byteString3.getByte(i)) {
            int i10 = 1;
            for (int i11 = i4 + 1; i11 < i3; i11++) {
                if (list.get(i11 - 1).getByte(i) != list.get(i11).getByte(i)) {
                    i10++;
                }
            }
            long jIntCount = j + intCount(buffer) + 2 + (i10 * 2);
            buffer.writeInt(i10);
            buffer.writeInt(iIntValue);
            for (int i12 = i4; i12 < i3; i12++) {
                byte b = list.get(i12).getByte(i);
                if (i12 == i4 || b != list.get(i12 - 1).getByte(i)) {
                    buffer.writeInt(b & 255);
                }
            }
            Buffer buffer3 = new Buffer();
            int i13 = i4;
            while (i13 < i3) {
                byte b2 = list.get(i13).getByte(i);
                int i14 = i13 + 1;
                int i15 = i14;
                while (true) {
                    if (i15 >= i3) {
                        i6 = i3;
                        break;
                    } else {
                        if (b2 != list.get(i15).getByte(i)) {
                            i6 = i15;
                            break;
                        }
                        i15++;
                    }
                }
                if (i14 == i6 && i + 1 == list.get(i13).size()) {
                    buffer.writeInt(list2.get(i13).intValue());
                    i7 = i6;
                    buffer2 = buffer3;
                } else {
                    buffer.writeInt((int) ((intCount(buffer3) + jIntCount) * (-1)));
                    i7 = i6;
                    buffer2 = buffer3;
                    buildTrieRecursive(jIntCount, buffer3, i + 1, list, i13, i6, list2);
                }
                i13 = i7;
                buffer3 = buffer2;
            }
            buffer.write(buffer3, buffer3.size());
            return;
        }
        int iMin = Math.min(byteString.size(), byteString3.size());
        int i16 = 0;
        for (int i17 = i; i17 < iMin && byteString.getByte(i17) == byteString3.getByte(i17); i17++) {
            i16++;
        }
        long jIntCount2 = 1 + j + intCount(buffer) + 2 + i16;
        buffer.writeInt(-i16);
        buffer.writeInt(iIntValue);
        int i18 = i;
        while (true) {
            i5 = i + i16;
            if (i18 >= i5) {
                break;
            }
            buffer.writeInt(byteString.getByte(i18) & 255);
            i18++;
        }
        if (i4 + 1 == i3) {
            if (i5 != list.get(i4).size()) {
                throw new AssertionError();
            }
            buffer.writeInt(list2.get(i4).intValue());
        } else {
            Buffer buffer4 = new Buffer();
            buffer.writeInt((int) ((intCount(buffer4) + jIntCount2) * (-1)));
            buildTrieRecursive(jIntCount2, buffer4, i5, list, i4, i3, list2);
            buffer.write(buffer4, buffer4.size());
        }
    }

    @Override // java.util.AbstractList, java.util.List
    public ByteString get(int i) {
        return this.byteStrings[i];
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.byteStrings.length;
    }

    private static int intCount(Buffer buffer) {
        return (int) (buffer.size() / 4);
    }
}