package com.shakticoin.app.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shakticoin.app.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerListAdapter<T> extends ArrayAdapter<T> {

    public SpinnerListAdapter(@NonNull Context context) {
        this(context, new ArrayList<T>());
    }

    public SpinnerListAdapter(@NonNull Context context, @NonNull List objects) {
        super(context, R.layout.widget_inlinelbl_spinner_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.widget_inlinelbl_spinner, null);
        }

        TextView tvName = view.findViewById(R.id.tv_selected_element);
        T item = getItem(position);
        tvName.setText(item != null ? item.toString() : null);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup root) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.widget_inlinelbl_spinner_item, null);
        }

        TextView textView = view.findViewById(R.id.text);
        T item = getItem(position);
        if (item != null) {
            textView.setText(item.toString());
        }

        return view;
    }


}
