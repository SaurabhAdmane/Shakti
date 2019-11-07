package com.shakticoin.app.widget;

import android.graphics.Typeface;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;

import com.shakticoin.app.R;

import java.util.List;

public class BindingAdapters {

    @BindingAdapter("android:entries")
    public static void setList(Spinner view, List<?> values) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        spinner.clear();
        if (values != null) {
            spinner.add("");
            spinner.addAll(values);
        }
    }

    @BindingAdapter("newValue")
    public static void setValue(Spinner view, Object value) {
        if (value != null) {
            if (view instanceof InlineLabelSpinner) {
                ((InlineLabelSpinner) view).setChoiceMade(true);
            }
            SpinnerAdapter adapter = view.getAdapter();
            Object selectedValue = view.getSelectedItem();
            if (!value.equals(selectedValue)) {
                // skip item at 0 as it isn't an instance of the item class, just a String
                for (int i = 0; i < adapter.getCount(); i++) {
                    Object c = adapter.getItem(i);
                    if (c instanceof String) continue; // hint, not real item
                    if (c.equals(value)) {
                        view.setSelection(i);
                    }
                }
            }
        }
    }

    @BindingAdapter("isPaymentOptionBold")
    public static void setPaymentOptionBold(TextView view, boolean isBold) {
        if (isBold) {
            Typeface tf = ResourcesCompat.getFont(view.getContext(), R.font.lato);
            view.setTypeface(tf, Typeface.BOLD);
        } else {
            Typeface tf = ResourcesCompat.getFont(view.getContext(), R.font.lato_light);
            view.setTypeface(tf, Typeface.NORMAL);
        }
    }
}
