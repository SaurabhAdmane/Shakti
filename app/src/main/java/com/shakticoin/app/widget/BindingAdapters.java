package com.shakticoin.app.widget;

import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.databinding.BindingAdapter;

import java.util.List;

public class BindingAdapters {

    @BindingAdapter("android:entries")
    public static void setList(Spinner view, List<?> values) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        spinner.clear();
        if (values != null) {
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
                for (int i = 1; i < adapter.getCount(); i++) {
                    Object c = adapter.getItem(i);
                    if (c.equals(value)) {
                        view.setSelection(i);
                    }
                }
            }
        }
    }

}
