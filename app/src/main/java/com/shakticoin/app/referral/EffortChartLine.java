package com.shakticoin.app.referral;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.shakticoin.app.R;

public class EffortChartLine extends LinearLayoutCompat {
    private int convertedPercent;
    private int processingPercent;
    private int influencedPercent;
    private int leadsNumber;

    private TextView convertedPercentView;
    private TextView processingPercentView;
    private TextView influencedPercentView;
    private TextView leadsNumberView;

    public EffortChartLine(Context context) {
        this(context, null);
    }

    public EffortChartLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EffortChartLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.effort_chart_line, this, true);

        convertedPercentView = findViewById(R.id.convertedLeadsBar);
        processingPercentView = findViewById(R.id.progressingLeadsBar);
        influencedPercentView = findViewById(R.id.influencedLeadsBar);
        leadsNumberView = findViewById(R.id.numberLeads);

        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.EffortChartLine, 0, 0);

        convertedPercent = a.getInt(R.styleable.EffortChartLine_convertedPercent, 0);
        processingPercent = a.getInt(R.styleable.EffortChartLine_processingPercent, 0);
        influencedPercent = a.getInt(R.styleable.EffortChartLine_influencedPercent, 0);
        leadsNumber = a.getInt(R.styleable.EffortChartLine_leadsNumber, 0);

        updateWidget();
    }

    @SuppressLint("SetTextI18n")
    private void updateWidget() {
        convertedPercentView.setLayoutParams(
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, convertedPercent));
        processingPercentView.setLayoutParams(
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, processingPercent));
        influencedPercentView.setLayoutParams(
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, influencedPercent));
        leadsNumberView.setLayoutParams(
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, leadsNumber));

        convertedPercentView.setText(convertedPercent > 0 ? convertedPercent + "%" : "");
        processingPercentView.setText(processingPercent > 0 ? processingPercent + "%" : "");
        influencedPercentView.setText(influencedPercent > 0 ? influencedPercent + "%" : "");
        if (leadsNumber > 0) {
            leadsNumberView.setText(getContext().getString(R.string.my_refs_referred, leadsNumber).toLowerCase());
        }
    }

    public int getConvertedPercent() {
        return convertedPercent;
    }

    public void setConvertedPercent(int convertedPercent) {
        int prevValue = this.convertedPercent;
        this.convertedPercent = convertedPercent;
        if (prevValue != this.convertedPercent) {
            updateWidget();
        }
    }

    public int getProcessingPercent() {
        return processingPercent;
    }

    public void setProcessingPercent(int processingPercent) {
        int prevValue = this.processingPercent;
        this.processingPercent = processingPercent;
        if (prevValue != this.processingPercent) {
            updateWidget();
        }
    }

    public int getInfluencedPercent() {
        return influencedPercent;
    }

    public void setInfluencedPercent(int influencedPercent) {
        int prevValue = this.influencedPercent;
        this.influencedPercent = influencedPercent;
        if (prevValue != this.influencedPercent) {
            updateWidget();
        }
    }

    public int getLeadsNumber() {
        return leadsNumber;
    }

    public void setLeadsNumber(int leadsNumber) {
        int prevValue = this.leadsNumber;
        this.leadsNumber = leadsNumber;
        if (prevValue != this.leadsNumber) {
            updateWidget();
        }
    }
}
