package com.shakticoin.app.widget;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.BindingAdapter;

import com.shakticoin.app.R;
import com.shakticoin.app.api.country.Country;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BindingAdapters {

    @BindingAdapter("android:entries")
    public static void setList(Spinner view, List<?> values) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        spinner.clear();
        if (values != null) {
            spinner.add(new Country(null, ""));
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

    /**
     * Re-format a date string that is specified as "1970-12-10" to
     * a medium date format that is specific for the current locale.
     */
    @BindingAdapter("localizedDate")
    public static void setLocalizedDate(TextView view, String date) {
        if (date == null) {
            view.setText(null);
            return;
        }

        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = sdf.parse(date);
            if (dateOfBirth != null) {
                DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());
                view.setText(df.format(dateOfBirth));
                return;
            }

        } catch (java.text.ParseException e) {
            view.setText(date);
            return;
        }

        view.setText(null);
    }

    /**
     * Format full name.
     */
    @BindingAdapter({"firstName", "middleName", "lastName"})
    public static void setFullName(TextView view, String firstName, String middleName, String lastName) {
        StringBuilder sb = new StringBuilder("");
        if (firstName != null) sb.append(firstName);
        if (middleName != null) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(middleName);
        }
        if (lastName != null) {
            if (sb.length() > 0) sb.append(" ");
            sb.append(lastName);
        }
        view.setText(sb.toString());
    }

    /**
     * Format postal address to display it in a single TextView.
     */
    @BindingAdapter({"addressLine1", "addressLine2", "city", "postalCode", "subdivision", "country"})
    public static void setFormattedPostalAddress(TextView view, String addressLine1,
                                                 String addressLine2, String city, String postalCode,
                                                 String subdivision, String country) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(addressLine1)) {
            sb.append(addressLine1);
        }
        if (!TextUtils.isEmpty(addressLine2)) {
            sb.append("\n").append(addressLine2);
        }
        if (!TextUtils.isEmpty(city)) {
            sb.append("\n").append(city);
            if (!TextUtils.isEmpty(subdivision)) {
                if (subdivision.length() > 3) {
                    sb.append("\n").append(subdivision);
                    if (!TextUtils.isEmpty(postalCode)) {
                        sb.append(", ").append(postalCode).append("\n");
                    }
                } else {
                    sb.append(", ").append(subdivision);
                    if (!TextUtils.isEmpty(postalCode)) {
                        sb.append(" ").append(postalCode).append("\n");
                    }
                }
            } else {
                sb.append("\n");
                if (!TextUtils.isEmpty(postalCode)) {
                    sb.append(postalCode).append(" - ");
                }
            }
            if (!TextUtils.isEmpty(country)) {
                sb.append(country);
            }
        }
        view.setText(sb.toString());
    }
}
