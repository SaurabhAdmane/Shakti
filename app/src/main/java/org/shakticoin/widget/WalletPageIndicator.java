package org.shakticoin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import org.shakticoin.R;

public class WalletPageIndicator extends ConstraintLayout {
    int selectedIndex = 0;

    public WalletPageIndicator(Context context) {
        this(context, null);
    }

    public WalletPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WalletPageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WalletPageIndicator, 0, 0);
        selectedIndex = a.getInteger(R.styleable.WalletPageIndicator_selectedIndex, 1);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_wallet_pageindicator, this, true);

        ImageView tickmark = null;
        TextView label = null;
        switch (selectedIndex) {
            case 1:
                tickmark = findViewById(R.id.tickmark1);
                label = findViewById(R.id.tickmark1label);
                break;
            case 2:
                tickmark = findViewById(R.id.tickmark2);
                label = findViewById(R.id.tickmark2label);
                break;
            case 3:
                tickmark = findViewById(R.id.tickmark3);
                label = findViewById(R.id.tickmark3label);
                break;
        }
        if (tickmark != null) tickmark.setAlpha(1f);
        if (label != null) label.setAlpha(1f);
    }

}
