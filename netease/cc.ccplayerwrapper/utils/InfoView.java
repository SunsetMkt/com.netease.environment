package com.netease.cc.ccplayerwrapper.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes5.dex */
public class InfoView extends TextView {

    class a implements Runnable {

        /* renamed from: a, reason: collision with root package name */
        final /* synthetic */ StringBuilder f1542a;

        a(StringBuilder sb) {
            this.f1542a = sb;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                InfoView.this.setText(this.f1542a.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public InfoView(Context context) {
        super(context);
    }

    public void a(JSONObject jSONObject) throws JSONException {
        try {
            StringBuilder sb = new StringBuilder();
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                Object obj = jSONObject.get(next);
                if (obj instanceof String) {
                    sb.append(next);
                    sb.append(":");
                    sb.append(jSONObject.getString(next));
                } else if (obj instanceof Integer) {
                    sb.append(next);
                    sb.append(":");
                    sb.append(jSONObject.getInt(next));
                } else if (obj instanceof Double) {
                    sb.append(next);
                    sb.append(":");
                    sb.append(jSONObject.getDouble(next));
                } else if (obj instanceof Boolean) {
                    sb.append(next);
                    sb.append(":");
                    sb.append(jSONObject.getBoolean(next));
                }
                sb.append("\n");
            }
            post(new a(sb));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public InfoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}