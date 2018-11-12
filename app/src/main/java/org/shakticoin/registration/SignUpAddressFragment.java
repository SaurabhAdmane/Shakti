package org.shakticoin.registration;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.shakticoin.R;
import org.shakticoin.api.country.Country;
import org.shakticoin.databinding.FragmentSignupAddressBinding;

import java.util.ArrayList;
import java.util.List;

public class SignUpAddressFragment extends Fragment {
    private SignUpActivityModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            viewModel = ViewModelProviders.of(activity).get(SignUpActivityModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSignupAddressBinding binding = FragmentSignupAddressBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        if (viewModel != null) {
            binding.setViewModel(viewModel);
        }

        TextView.OnEditorActionListener listener = (TextView.OnEditorActionListener) getActivity();
        if (listener != null) {
            binding.postalCode.setOnEditorActionListener(listener);
        }

        Activity activity = getActivity();
        if (activity != null) {
            // initially the adapter is empty and updated via data binding
            ArrayList<Object> countryList = new ArrayList<>();
            countryList.add(getString(R.string.hint_country));
            CountryListAdapter countryListAdapter = new CountryListAdapter(getActivity(), R.layout.spinner_styled_item, countryList);
            countryListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            binding.countries.setAdapter(countryListAdapter);

            // initially the adapter is empty and updated via data binding
            ArrayList<Object> countryList1 = new ArrayList<>();
            countryList1.add(getString(R.string.hint_citizenship));
            CountryListAdapter citizenshipListAdapter = new CountryListAdapter(getActivity(), R.layout.spinner_styled_item, countryList1);
            citizenshipListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            binding.citizenship.setAdapter(citizenshipListAdapter);
        }

        // display error callout for the field if error message is set
        viewModel.addressErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.addressLayout.setError(s);
                viewModel.addressErrMsg.setValue(null);
            }
        });
        viewModel.cityErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.cityLayout.setError(s);
                viewModel.cityErrMsg.setValue(null);
            }
        });
        viewModel.postalCodeErrMsg.observe(this, s -> {
            if (!TextUtils.isEmpty(s)) {
                binding.postalCodeLayout.setError(s);
                viewModel.postalCodeErrMsg.setValue(null);
            }
        });

        return binding.getRoot();
    }

    @BindingAdapter("android:entries")
    public static void setCountryList(Spinner view, List<Country> countries) {
        CountryListAdapter adapter = (CountryListAdapter) view.getAdapter();
        if (adapter != null) {
            Object firstItem = adapter.getItem(0);
            adapter.clear();
            adapter.add(firstItem);
            if (countries != null) {
                adapter.addAll(countries);
            }
        }
    }

    @BindingAdapter("newValue")
    public static void setCountry(Spinner view, Country country) {
        if (country != null) {
            SpinnerAdapter adapter = view.getAdapter();
            Object selectedCountry = view.getSelectedItem();
            if (!selectedCountry.equals(country)) {
                // skip item at 0 as it isn't an instance of Country, just a String
                for (int i = 1; i < adapter.getCount(); i++) {
                    Country c = (Country) adapter.getItem(i);
                    if (c.equals(country)) {
                        view.setSelection(i);
                    }
                }
            }
        }
    }

    /**
     * Add special processing for the first item to emulate hint message.
     */
    class CountryListAdapter extends ArrayAdapter<Object> {

        CountryListAdapter(@NonNull Context context, int resource, @NonNull List<Object> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // change color of the first item because in fact it is a hint and should not be selectable
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTextColor(position == 0 ? Color.GRAY : Color.WHITE);
            return view;
        }

        @Override
        public boolean isEnabled(int position) {
            // disable first item that play the role of a hint
            return position != 0;
        }
    }

}
