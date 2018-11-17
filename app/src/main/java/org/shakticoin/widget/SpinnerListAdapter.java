package org.shakticoin.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.shakticoin.R;

import java.util.List;

public class SpinnerListAdapter<T> extends ArrayAdapter<T> {

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
        textView.setText(item != null ? item.toString() : null);

        return view;
    }


}
