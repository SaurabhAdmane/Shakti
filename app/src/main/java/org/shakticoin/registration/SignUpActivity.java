package org.shakticoin.registration;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.api.OnCompleteListener;


public class SignUpActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private SignUpActivityModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        viewModel = ViewModelProviders.of(this).get(SignUpActivityModel.class);
        viewModel.initCountryList(getResources().getConfiguration().locale);

        onContactsPageSelected();
    }

    public void onMainAction(View v) {
        String tag = (String) v.getTag();
        if ("ContactsPage".equals(tag)) {
            if (validateContacts()) onAddressPageSelected();

        } else if ("AddressPage".equals(tag)) {
            if (validateAddress()) onPasswordPageSelected();

        } else if ("PasswordPage".equals(tag)) {
            if (validatePassword()) startRegistration();
        }
    }

    public void startRegistration() {
        final Activity activity = this;
        viewModel.createUser(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                if (error != null) {
                    Toast.makeText(activity, R.string.err_unexpected, Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(activity, ConfirmEmailActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onDoLogin(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    /** Return true if all fields on the first page has valid values */
    private boolean validateContacts() {
        if (TextUtils.isEmpty(viewModel.firstName.getValue())
                || TextUtils.isEmpty(viewModel.lastName.getValue())
                || TextUtils.isEmpty(viewModel.emailAddress.getValue())
                || TextUtils.isEmpty(viewModel.phoneNumber.getValue())) {
            Toast.makeText(this, R.string.err_all_fields_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /** Return true if all fields on the second page has valid values */
    private boolean validateAddress() {
        if (TextUtils.isEmpty(viewModel.address.getValue())
                || TextUtils.isEmpty(viewModel.city.getValue())
                || TextUtils.isEmpty(viewModel.postalCode.getValue())) {
            Toast.makeText(this, R.string.err_all_fields_required, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /** Return true if both fields contain the same string */
    private boolean validatePassword() {
        String newPassword = viewModel.newPassword.getValue();
        String verifyPassword = viewModel.verifyPassword.getValue();
        if (TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(verifyPassword)
                || !newPassword.equals(verifyPassword)) {
            Toast.makeText(this, R.string.err_incorrect_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void onContactsPageSelected() {
        SignUpContactsFragment fragment = new SignUpContactsFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame, fragment).commit();
    }

    private void onAddressPageSelected() {
        SignUpAddressFragment fragment = new SignUpAddressFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame, fragment).addToBackStack("AddressPage").commit();
    }

    private void onPasswordPageSelected() {
        SignUpPasswordFragment fragment = new SignUpPasswordFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frame, fragment).addToBackStack("PasswordPage").commit();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        onMainAction(v);
        return true;
    }
}
