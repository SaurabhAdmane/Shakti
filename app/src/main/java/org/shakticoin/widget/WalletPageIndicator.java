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
    private static final float OFF  = 0.3f;
    private static final float ON   = 1f;

    private int selectedIndex = 0;

    private ImageView[] tickmarks = new ImageView[5];
    private TextView[] tickmarkLabels = new TextView[3];

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
        assert inflater != null;
        inflater.inflate(R.layout.widget_wallet_pageindicator, this, true);

        tickmarks[0] = findViewById(R.id.tickmark1);
        tickmarks[1] = findViewById(R.id.tickmark2);
        tickmarks[2] = findViewById(R.id.tickmark3);
        tickmarks[3] = findViewById(R.id.tickmark4);
        tickmarks[4] = findViewById(R.id.tickmark5);
        tickmarkLabels[0] = findViewById(R.id.tickmark1label);
        tickmarkLabels[1] = findViewById(R.id.tickmark3label);
        tickmarkLabels[2] = findViewById(R.id.tickmark5label);

        updateIndicators();
    }

    private void updateIndicators() {
        for (ImageView tickmark : tickmarks) tickmark.setAlpha(OFF);
        for (TextView label : tickmarkLabels) label.setAlpha(OFF);
        for (int i = 0; i < selectedIndex; i++) tickmarks[i].setAlpha(ON);
        switch (selectedIndex) {
            case 1:
            case 2:
                tickmarkLabels[0].setAlpha(ON);
                break;
            case 3:
            case 4:
                for (int i = 0; i < 2; i++) tickmarkLabels[i].setAlpha(ON);
                break;
            case 5:
                for (int i = 0; i < 3; i++) tickmarkLabels[i].setAlpha(ON);
                break;
        }
    }

    /**
     * Highlight tickmarks for pages from 1 and up to provided index.
     * @param i an integer in range from 1 to 5
     */
    public void setSelectedIndex(int i) {
        selectedIndex = i;
        updateIndicators();
    }

    /**
     * Return a selected index.
     * @return an integer in range from 1 to 5
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }
}
