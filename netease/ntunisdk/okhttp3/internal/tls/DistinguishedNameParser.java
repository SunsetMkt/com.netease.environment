package com.netease.ntunisdk.okhttp3.internal.tls;

import javax.security.auth.x500.X500Principal;

/* loaded from: classes5.dex */
final class DistinguishedNameParser {
    private int beg;
    private char[] chars;
    private int cur;
    private final String dn;
    private int end;
    private final int length;
    private int pos;

    DistinguishedNameParser(X500Principal x500Principal) {
        String name = x500Principal.getName("RFC2253");
        this.dn = name;
        this.length = name.length();
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x004d, code lost:
    
        r2 = r8.beg;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0057, code lost:
    
        return new java.lang.String(r1, r2, r8.end - r2);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String escapedAV() {
        /*
            r8 = this;
            int r0 = r8.pos
            r8.beg = r0
            r8.end = r0
        L6:
            int r0 = r8.pos
            int r1 = r8.length
            if (r0 < r1) goto L19
            java.lang.String r0 = new java.lang.String
            char[] r1 = r8.chars
            int r2 = r8.beg
            int r3 = r8.end
            int r3 = r3 - r2
            r0.<init>(r1, r2, r3)
            return r0
        L19:
            char[] r1 = r8.chars
            char r2 = r1[r0]
            r3 = 44
            r4 = 43
            r5 = 59
            r6 = 32
            if (r2 == r6) goto L58
            if (r2 == r5) goto L4d
            r5 = 92
            if (r2 == r5) goto L3a
            if (r2 == r4) goto L4d
            if (r2 == r3) goto L4d
            int r3 = r8.end
            int r4 = r3 + 1
            r8.end = r4
            r1[r3] = r2
            goto L48
        L3a:
            int r0 = r8.end
            int r2 = r0 + 1
            r8.end = r2
            char r2 = r8.getEscaped()
            r1[r0] = r2
            int r0 = r8.pos
        L48:
            int r0 = r0 + 1
            r8.pos = r0
            goto L6
        L4d:
            java.lang.String r0 = new java.lang.String
            int r2 = r8.beg
            int r3 = r8.end
            int r3 = r3 - r2
            r0.<init>(r1, r2, r3)
            return r0
        L58:
            int r2 = r8.end
            r8.cur = r2
            int r0 = r0 + 1
            r8.pos = r0
            int r0 = r2 + 1
            r8.end = r0
            r1[r2] = r6
        L66:
            int r0 = r8.pos
            int r1 = r8.length
            if (r0 >= r1) goto L7f
            char[] r2 = r8.chars
            char r7 = r2[r0]
            if (r7 != r6) goto L7f
            int r1 = r8.end
            int r7 = r1 + 1
            r8.end = r7
            r2[r1] = r6
            int r0 = r0 + 1
            r8.pos = r0
            goto L66
        L7f:
            if (r0 == r1) goto L8b
            char[] r1 = r8.chars
            char r0 = r1[r0]
            if (r0 == r3) goto L8b
            if (r0 == r4) goto L8b
            if (r0 != r5) goto L6
        L8b:
            java.lang.String r0 = new java.lang.String
            char[] r1 = r8.chars
            int r2 = r8.beg
            int r3 = r8.cur
            int r3 = r3 - r2
            r0.<init>(r1, r2, r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.okhttp3.internal.tls.DistinguishedNameParser.escapedAV():java.lang.String");
    }

    private int getByte(int i) {
        int i2;
        int i3;
        int i4 = i + 1;
        if (i4 >= this.length) {
            throw new IllegalStateException("Malformed DN: " + this.dn);
        }
        char[] cArr = this.chars;
        char c = cArr[i];
        if (c >= '0' && c <= '9') {
            i2 = c - '0';
        } else if (c >= 'a' && c <= 'f') {
            i2 = c - 'W';
        } else {
            if (c < 'A' || c > 'F') {
                throw new IllegalStateException("Malformed DN: " + this.dn);
            }
            i2 = c - '7';
        }
        char c2 = cArr[i4];
        if (c2 >= '0' && c2 <= '9') {
            i3 = c2 - '0';
        } else if (c2 >= 'a' && c2 <= 'f') {
            i3 = c2 - 'W';
        } else {
            if (c2 < 'A' || c2 > 'F') {
                throw new IllegalStateException("Malformed DN: " + this.dn);
            }
            i3 = c2 - '7';
        }
        return (i2 << 4) + i3;
    }

    private char getEscaped() {
        int i = this.pos + 1;
        this.pos = i;
        if (i == this.length) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        char c = this.chars[i];
        if (c == ' ' || c == '%' || c == '\\' || c == '_' || c == '\"' || c == '#') {
            return c;
        }
        switch (c) {
            case '*':
            case '+':
            case ',':
                return c;
            default:
                switch (c) {
                    case ';':
                    case '<':
                    case '=':
                    case '>':
                        return c;
                    default:
                        return getUTF8();
                }
        }
    }

    private char getUTF8() {
        int i;
        int i2;
        int i3 = getByte(this.pos);
        this.pos++;
        if (i3 < 128) {
            return (char) i3;
        }
        if (i3 < 192 || i3 > 247) {
            return '?';
        }
        if (i3 <= 223) {
            i = i3 & 31;
            i2 = 1;
        } else if (i3 <= 239) {
            i = i3 & 15;
            i2 = 2;
        } else {
            i = i3 & 7;
            i2 = 3;
        }
        for (int i4 = 0; i4 < i2; i4++) {
            int i5 = this.pos + 1;
            this.pos = i5;
            if (i5 == this.length || this.chars[i5] != '\\') {
                return '?';
            }
            int i6 = i5 + 1;
            this.pos = i6;
            int i7 = getByte(i6);
            this.pos++;
            if ((i7 & 192) != 128) {
                return '?';
            }
            i = (i << 6) + (i7 & 63);
        }
        return (char) i;
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x004c, code lost:
    
        r6.end = r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String hexAV() {
        /*
            r6 = this;
            int r0 = r6.pos
            int r1 = r0 + 4
            int r2 = r6.length
            java.lang.String r3 = "Unexpected end of DN: "
            if (r1 >= r2) goto L8d
            r6.beg = r0
        Lc:
            int r0 = r0 + 1
            r6.pos = r0
            int r0 = r6.pos
            int r1 = r6.length
            if (r0 == r1) goto L4c
            char[] r1 = r6.chars
            char r2 = r1[r0]
            r4 = 43
            if (r2 == r4) goto L4c
            r4 = 44
            if (r2 == r4) goto L4c
            r4 = 59
            if (r2 != r4) goto L27
            goto L4c
        L27:
            r4 = 32
            if (r2 != r4) goto L3e
            r6.end = r0
        L2d:
            int r0 = r0 + 1
            r6.pos = r0
            int r0 = r6.pos
            int r1 = r6.length
            if (r0 >= r1) goto L4e
            char[] r1 = r6.chars
            char r1 = r1[r0]
            if (r1 != r4) goto L4e
            goto L2d
        L3e:
            r4 = 65
            if (r2 < r4) goto Lc
            r4 = 70
            if (r2 > r4) goto Lc
            int r2 = r2 + 32
            char r2 = (char) r2
            r1[r0] = r2
            goto Lc
        L4c:
            r6.end = r0
        L4e:
            int r0 = r6.end
            int r1 = r6.beg
            int r0 = r0 - r1
            r2 = 5
            if (r0 < r2) goto L79
            r2 = r0 & 1
            if (r2 == 0) goto L79
            int r2 = r0 / 2
            byte[] r3 = new byte[r2]
            int r1 = r1 + 1
            r4 = 0
        L61:
            if (r4 >= r2) goto L6f
            int r5 = r6.getByte(r1)
            byte r5 = (byte) r5
            r3[r4] = r5
            int r1 = r1 + 2
            int r4 = r4 + 1
            goto L61
        L6f:
            java.lang.String r1 = new java.lang.String
            char[] r2 = r6.chars
            int r3 = r6.beg
            r1.<init>(r2, r3, r0)
            return r1
        L79:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>(r3)
            java.lang.String r2 = r6.dn
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L8d:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>(r3)
            java.lang.String r2 = r6.dn
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.netease.ntunisdk.okhttp3.internal.tls.DistinguishedNameParser.hexAV():java.lang.String");
    }

    private String nextAT() {
        int i;
        int i2;
        int i3;
        char c;
        int i4;
        int i5;
        char c2;
        char c3;
        while (true) {
            i = this.pos;
            i2 = this.length;
            if (i >= i2 || this.chars[i] != ' ') {
                break;
            }
            this.pos = i + 1;
        }
        if (i == i2) {
            return null;
        }
        this.beg = i;
        do {
            this.pos = i + 1;
            i = this.pos;
            i3 = this.length;
            if (i >= i3 || (c3 = this.chars[i]) == '=') {
                break;
            }
        } while (c3 != ' ');
        if (i >= i3) {
            throw new IllegalStateException("Unexpected end of DN: " + this.dn);
        }
        this.end = i;
        if (this.chars[i] == ' ') {
            while (true) {
                i4 = this.pos;
                i5 = this.length;
                if (i4 >= i5 || (c2 = this.chars[i4]) == '=' || c2 != ' ') {
                    break;
                }
                this.pos = i4 + 1;
            }
            if (this.chars[i4] != '=' || i4 == i5) {
                throw new IllegalStateException("Unexpected end of DN: " + this.dn);
            }
        }
        int i6 = this.pos;
        do {
            this.pos = i6 + 1;
            i6 = this.pos;
            if (i6 >= this.length) {
                break;
            }
        } while (this.chars[i6] == ' ');
        int i7 = this.end;
        int i8 = this.beg;
        if (i7 - i8 > 4) {
            char[] cArr = this.chars;
            if (cArr[i8 + 3] == '.' && (((c = cArr[i8]) == 'O' || c == 'o') && ((cArr[i8 + 1] == 'I' || cArr[i8 + 1] == 'i') && (cArr[i8 + 2] == 'D' || cArr[i8 + 2] == 'd')))) {
                this.beg = i8 + 4;
            }
        }
        char[] cArr2 = this.chars;
        int i9 = this.beg;
        return new String(cArr2, i9, i7 - i9);
    }

    private String quotedAV() {
        int i = this.pos + 1;
        this.pos = i;
        this.beg = i;
        while (true) {
            this.end = i;
            int i2 = this.pos;
            if (i2 == this.length) {
                throw new IllegalStateException("Unexpected end of DN: " + this.dn);
            }
            char[] cArr = this.chars;
            char c = cArr[i2];
            if (c == '\"') {
                do {
                    this.pos = i2 + 1;
                    i2 = this.pos;
                    if (i2 >= this.length) {
                        break;
                    }
                } while (this.chars[i2] == ' ');
                char[] cArr2 = this.chars;
                int i3 = this.beg;
                return new String(cArr2, i3, this.end - i3);
            }
            if (c == '\\') {
                cArr[this.end] = getEscaped();
            } else {
                cArr[this.end] = c;
            }
            this.pos++;
            i = this.end + 1;
        }
    }

    public String findMostSpecific(String str) {
        this.pos = 0;
        this.beg = 0;
        this.end = 0;
        this.cur = 0;
        this.chars = this.dn.toCharArray();
        String strNextAT = nextAT();
        if (strNextAT == null) {
            return null;
        }
        do {
            int i = this.pos;
            if (i == this.length) {
                return null;
            }
            char c = this.chars[i];
            String strEscapedAV = c != '\"' ? c != '#' ? (c == '+' || c == ',' || c == ';') ? "" : escapedAV() : hexAV() : quotedAV();
            if (str.equalsIgnoreCase(strNextAT)) {
                return strEscapedAV;
            }
            int i2 = this.pos;
            if (i2 >= this.length) {
                return null;
            }
            char c2 = this.chars[i2];
            if (c2 != ',' && c2 != ';' && c2 != '+') {
                throw new IllegalStateException("Malformed DN: " + this.dn);
            }
            this.pos = i2 + 1;
            strNextAT = nextAT();
        } while (strNextAT != null);
        throw new IllegalStateException("Malformed DN: " + this.dn);
    }
}