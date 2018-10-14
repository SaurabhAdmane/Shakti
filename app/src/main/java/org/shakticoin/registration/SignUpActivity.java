package org.shakticoin.registration;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.api.country.Country;
import org.shakticoin.databinding.ActivitySignupBinding;
import org.shakticoin.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;


public class SignUpActivity extends AppCompatActivity {

    private SignUpModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySignupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(SignUpModel.class);
        viewModel.initCountryList(getResources().getConfiguration().locale);
        binding.setViewModel(viewModel);

        // initially the adapter is empty and updated via data binding
        Spinner ctrlCountries = findViewById(R.id.countries);
        ArrayList<Object> countryList = new ArrayList<>();
        countryList.add(getString(R.string.hint_country));
        CountryListAdapter countryListAdapter = new CountryListAdapter(this, R.layout.spinner_styled_item, countryList);
        countryListAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        ctrlCountries.setAdapter(countryListAdapter);
    }

    public void onStartRegistration(View view) {
        String emailAddress = viewModel.emailAddress.getValue();
        String phoneNumber = viewModel.phoneNumber.getValue();
        String postalCode = viewModel.postalCode.getValue();
        String countryCode = viewModel.getCurrentCountryCode();
        if (TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(phoneNumber)
                || TextUtils.isEmpty(postalCode) || TextUtils.isEmpty(countryCode)) {
            Toast.makeText(this, R.string.err_all_fields_required, Toast.LENGTH_SHORT).show();
            return;
        }

        // pass all data along to the next activity
        Intent intent = new Intent(this, PasswordActivity.class);
        intent.putExtra(CommonUtil.prefixed(PasswordActivity.KEY_EMAIL_ADDR, this), emailAddress);
        intent.putExtra(CommonUtil.prefixed(PasswordActivity.KEY_PHONE_NUMBER, this), phoneNumber);
        intent.putExtra(CommonUtil.prefixed(PasswordActivity.KEY_POSTAL_CODE, this), postalCode);
        intent.putExtra(CommonUtil.prefixed(PasswordActivity.KEY_COUNTRY_CODE, this), countryCode);
        startActivity(intent);
    }

    public void onDoLogin(View v) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    /**
     * Return the country code where the SIM card of the device is registered.</br>
     * It is not an universal solution but must work in most cases.
     */
    public String getCountryCode() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager != null ? telephonyManager.getSimCountryIso() : null;
    }

    @BindingAdapter("android:entries")
    public static void setCountryList(Spinner view, List<Country> countries) {
        CountryListAdapter adapter = (CountryListAdapter) view.getAdapter();
        Object firstItem = adapter.getItem(0);
        adapter.clear();
        adapter.add(firstItem);
        if (countries != null) {
            adapter.addAll(countries);
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
