package com.netease.mpay.ps.codescanner.widget2;

import android.view.View;

/* loaded from: classes5.dex */
public class AgreeCheckBox implements View.OnClickListener {
    View agreeChecked;
    View agreeUnChecked;
    OnAgreeChangeListener listener;

    public interface OnAgreeChangeListener {
        void onAgreeChanged(boolean z);
    }

    public AgreeCheckBox(View view, View view2, boolean z) {
        this.agreeChecked = view;
        this.agreeUnChecked = view2;
        this.agreeChecked.setOnClickListener(this);
        this.agreeUnChecked.setOnClickListener(this);
        setCheckState(z);
    }

    public AgreeCheckBox setOnAgreeChangeListener(OnAgreeChangeListener onAgreeChangeListener) {
        this.listener = onAgreeChangeListener;
        return this;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (this.agreeChecked.getVisibility() == 0) {
            this.agreeChecked.setVisibility(8);
            this.agreeUnChecked.setVisibility(0);
        } else {
            this.agreeChecked.setVisibility(0);
            this.agreeUnChecked.setVisibility(8);
        }
        OnAgreeChangeListener onAgreeChangeListener = this.listener;
        if (onAgreeChangeListener != null) {
            onAgreeChangeListener.onAgreeChanged(isChecked());
        }
    }

    public boolean isChecked() {
        View view = this.agreeChecked;
        return view != null && view.getVisibility() == 0;
    }

    public void setCheckState(boolean z) {
        if (z) {
            this.agreeChecked.setVisibility(0);
            this.agreeUnChecked.setVisibility(8);
        } else {
            this.agreeChecked.setVisibility(8);
            this.agreeUnChecked.setVisibility(0);
        }
    }
}