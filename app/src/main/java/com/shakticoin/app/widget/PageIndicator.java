package com.shakticoin.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shakticoin.app.R;

public class PageIndicator extends LinearLayout {
    private static final int MAX_ITEMS = 5;
    private static final float OFF  = 0.3f;
    private static final float ON   = 1f;

    private float pixelsPerDP;

    private int selectedIndex;

    private LinearLayout[] pages = new LinearLayout[MAX_ITEMS];
    private ImageView[] tickmarks = new ImageView[MAX_ITEMS];
    private TextView[] tickmarkLabels = new TextView[MAX_ITEMS];

    public PageIndicator(Context context) {
        this(context, null);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageIndicator, 0, 0);
        selectedIndex = a.getInteger(R.styleable.PageIndicator_selectedIndex, 1);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.widget_pageindicator, this, true);

        pixelsPerDP = getResources().getDisplayMetrics().density;

        pages[0] = findViewById(R.id.page1);
        pages[1] = findViewById(R.id.page2);
        pages[2] = findViewById(R.id.page3);
        pages[3] = findViewById(R.id.page4);
        pages[4] = findViewById(R.id.page5);
        for (int i = 0; i < pages.length; i++) {
            tickmarks[i] = pages[i].findViewWithTag("tickmark" + (i+1));
            tickmarkLabels[i] = pages[i].findViewWithTag("label" + (i+1));
        }

        updateIndicators();
    }

    private void updateIndicators() {
        for (ImageView tickmark : tickmarks) tickmark.setAlpha(OFF);
        for (TextView label : tickmarkLabels) label.setAlpha(OFF);
        for (int i = 0; i < selectedIndex; i++) {
            tickmarks[i].setAlpha(ON);
            tickmarkLabels[i].setAlpha(ON);
        }
    }

    /**
     * Every element of array is a tickmark. An element may be a string and this string is
     * used as a label or it may equal to null. The array may have up to 5 elements.
     */
    public void setSizeAndLabels(String[] initializer) {
        if (initializer == null || initializer.length == 0 || initializer.length > 5) {
            throw new IllegalArgumentException();

        }
        for (int i = 0; i < initializer.length; i++) {
            pages[i].setVisibility(View.VISIBLE);
            String label = initializer[i];
            if (!TextUtils.isEmpty(label)) {
                TextView tvLabel = pages[i].findViewWithTag("label"+(i+1));
                if (tvLabel != null) tvLabel.setText(label);
            } else {
                // adjust size and position of the tickmark w/o label
                ViewGroup.LayoutParams params = tickmarks[i].getLayoutParams();
                params.height = params.width = Float.valueOf(13*pixelsPerDP).intValue();
                tickmarks[i].setPadding(0, Float.valueOf(2.5f*pixelsPerDP).intValue(), 0, 0);
            }
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
