package com.netease.ntunisdk.base.view;

import android.graphics.Color;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* compiled from: NtSdkTagParser.java */
/* loaded from: classes5.dex */
public class c {

    /* renamed from: a, reason: collision with root package name */
    private static final Pattern f1854a = Pattern.compile("<ntsdk (.*?)>(.*?)</ntsdk>");
    private static final Pattern b = Pattern.compile("(\\w*)=\"(\\S*)\"");

    public static List<b> parseNtSdkTags(String str) {
        if (TextUtils.isEmpty(str)) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        Matcher matcher = f1854a.matcher(str);
        int iEnd = 0;
        while (matcher.find()) {
            String strGroup = matcher.group(1);
            String strGroup2 = matcher.group(2);
            if (iEnd < matcher.start()) {
                arrayList.add(new b(str.substring(iEnd, matcher.start())));
            }
            b bVar = new b(strGroup2, strGroup);
            a(bVar);
            arrayList.add(bVar);
            iEnd = matcher.end();
        }
        if (iEnd < str.length()) {
            arrayList.add(new b(str.substring(iEnd)));
        }
        return arrayList;
    }

    private static void a(b bVar) {
        Matcher matcher = b.matcher(bVar.b);
        while (matcher.find()) {
            String strGroup = matcher.group(1);
            String strGroup2 = matcher.group(2);
            if (!TextUtils.isEmpty(strGroup) && !TextUtils.isEmpty(strGroup2)) {
                if ("href".equals(strGroup)) {
                    bVar.h = strGroup2;
                } else if ("href_2".equals(strGroup)) {
                    bVar.i = strGroup2;
                } else if ("scene".equals(strGroup)) {
                    bVar.k = strGroup2;
                } else if ("open_type".equals(strGroup)) {
                    bVar.j = strGroup2;
                } else if ("color".equals(strGroup)) {
                    bVar.d = strGroup2.replaceAll("^0[xX]", "#");
                } else if ("action".equals(strGroup)) {
                    bVar.e = strGroup2;
                } else if ("bold".equals(strGroup)) {
                    bVar.f = Boolean.parseBoolean(strGroup2);
                } else if ("underline".equals(strGroup)) {
                    bVar.g = Boolean.parseBoolean(strGroup2);
                }
            }
        }
    }

    public static void setNtSdkStrings2TextView(List<b> list, TextView textView) {
        setNtSdkStrings2TextView(list, new NtSdkTagParser$RichTextView(textView, null));
    }

    public static void setNtSdkStrings2TextView(List<b> list, NtSdkTagParser$RichTextView ntSdkTagParser$RichTextView) {
        final TextView textView;
        if (list == null || list.size() <= 0 || (textView = ntSdkTagParser$RichTextView.f1851a) == null) {
            return;
        }
        final NtSdkTagParser$OnSpanClickListener ntSdkTagParser$OnSpanClickListener = ntSdkTagParser$RichTextView.b;
        NtSdkStringClickableSpan ntSdkStringClickableSpan = ntSdkTagParser$RichTextView.c;
        textView.setText("");
        textView.setMovementMethod(new a((byte) 0));
        for (final b bVar : list) {
            final SpannableString spannableString = new SpannableString(bVar.f1853a);
            if (!bVar.c) {
                int length = bVar.f1853a.length();
                if (!TextUtils.isEmpty(bVar.d)) {
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor(bVar.d)), 0, length, 33);
                }
                if (!TextUtils.isEmpty(bVar.e)) {
                    if ("realname".equals(bVar.e) || "native_realname".equals(bVar.e)) {
                        spannableString.setSpan(new ClickableSpan() { // from class: com.netease.ntunisdk.base.view.NtSdkTagParser$1
                            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                            public final void updateDrawState(TextPaint textPaint) {
                            }

                            @Override // android.text.style.ClickableSpan
                            public final void onClick(View view) {
                                if (ntSdkTagParser$OnSpanClickListener != null) {
                                    NtSdkTagParser$OnSpanClickListener.b();
                                }
                            }
                        }, 0, length, 33);
                    } else if ("ff_rules".equals(bVar.e)) {
                        spannableString.setSpan(new ClickableSpan() { // from class: com.netease.ntunisdk.base.view.NtSdkTagParser$2
                            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                            public final void updateDrawState(TextPaint textPaint) {
                            }

                            @Override // android.text.style.ClickableSpan
                            public final void onClick(View view) {
                                NtSdkTagParser$OnSpanClickListener ntSdkTagParser$OnSpanClickListener2 = ntSdkTagParser$OnSpanClickListener;
                                if (ntSdkTagParser$OnSpanClickListener2 != null) {
                                    ntSdkTagParser$OnSpanClickListener2.a();
                                }
                            }
                        }, 0, length, 33);
                    } else if ("urs_realname".equals(bVar.e) || "webview_realname".equals(bVar.e)) {
                        spannableString.setSpan(new ClickableSpan() { // from class: com.netease.ntunisdk.base.view.NtSdkTagParser$3
                            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                            public final void updateDrawState(TextPaint textPaint) {
                            }

                            @Override // android.text.style.ClickableSpan
                            public final void onClick(View view) {
                                NtSdkTagParser$OnSpanClickListener ntSdkTagParser$OnSpanClickListener2 = ntSdkTagParser$OnSpanClickListener;
                                if (ntSdkTagParser$OnSpanClickListener2 != null) {
                                    ntSdkTagParser$OnSpanClickListener2.c();
                                }
                            }
                        }, 0, length, 33);
                    } else if ("outlink".equals(bVar.e)) {
                        spannableString.setSpan(new ClickableSpan() { // from class: com.netease.ntunisdk.base.view.NtSdkTagParser$4
                            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                            public final void updateDrawState(TextPaint textPaint) {
                            }

                            @Override // android.text.style.ClickableSpan
                            public final void onClick(View view) {
                                NtSdkTagParser$OnSpanClickListener ntSdkTagParser$OnSpanClickListener2 = ntSdkTagParser$OnSpanClickListener;
                                if (ntSdkTagParser$OnSpanClickListener2 != null) {
                                    ntSdkTagParser$OnSpanClickListener2.onOutLinkClicked(textView.getContext(), bVar.h, spannableString.toString(), bVar.i);
                                }
                            }
                        }, 0, length, 33);
                    } else if ("openlink".equals(bVar.e)) {
                        spannableString.setSpan(new ClickableSpan() { // from class: com.netease.ntunisdk.base.view.NtSdkTagParser$5
                            @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
                            public final void updateDrawState(TextPaint textPaint) {
                            }

                            @Override // android.text.style.ClickableSpan
                            public final void onClick(View view) {
                                NtSdkTagParser$OnSpanClickListener ntSdkTagParser$OnSpanClickListener2 = ntSdkTagParser$OnSpanClickListener;
                                if (ntSdkTagParser$OnSpanClickListener2 != null) {
                                    ntSdkTagParser$OnSpanClickListener2.onOpenLinkClicked(bVar.h, NtSdkTagParser$OpenLinkType.convert(bVar.j), spannableString.toString());
                                }
                            }
                        }, 0, length, 33);
                    } else if (ntSdkStringClickableSpan != null) {
                        ntSdkStringClickableSpan.setAction(bVar.e);
                        spannableString.setSpan(ntSdkStringClickableSpan, 0, length, 33);
                    }
                }
                if (bVar.f) {
                    spannableString.setSpan(new StyleSpan(1), 0, length, 33);
                }
                if (bVar.g) {
                    spannableString.setSpan(new UnderlineSpan(), 0, length, 33);
                }
            }
            textView.append(spannableString);
        }
    }

    /* compiled from: NtSdkTagParser.java */
    private static class a extends LinkMovementMethod {
        private a() {
        }

        /* synthetic */ a(byte b) {
            this();
        }

        @Override // android.text.method.LinkMovementMethod, android.text.method.ScrollingMovementMethod, android.text.method.BaseMovementMethod, android.text.method.MovementMethod
        public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
            boolean zOnTouchEvent = super.onTouchEvent(textView, spannable, motionEvent);
            Selection.removeSelection(spannable);
            return zOnTouchEvent;
        }
    }
}