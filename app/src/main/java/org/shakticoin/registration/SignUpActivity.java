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

import org.shakticoin.BuildConfig;
import org.shakticoin.R;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.util.Validator;


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
                    Toast.makeText(activity,
                            BuildConfig.DEBUG ? error.getMessage() : getString(R.string.err_unexpected),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogConfirmEmail.getInstance(false)
                        .show(getSupportFragmentManager(), DialogConfirmEmail.class.getSimpleName());
            }
        });
    }

    public void onDoLogin(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void onCancel(View view) {
        // TODO: if user cancel registration we probable must return to login screen instead of finishing the activity
        finish();
    }

    /** Return true if all fields on the first page has valid values */
    private boolean validateContacts() {
        boolean hasErrors = false;
        if (TextUtils.isEmpty(viewModel.firstName.getValue())) {
            viewModel.firstNameErrMsg.setValue(getString(R.string.err_required));
            hasErrors = true;
        }
        if (TextUtils.isEmpty(viewModel.lastName.getValue())) {
            viewModel.lastNameErrMsg.setValue(getString(R.string.err_required));
            hasErrors = true;
        }
        if (!Validator.isEmail(viewModel.emailAddress.getValue())) {
            viewModel.emailAddressErrMsg.setValue(getString(R.string.err_email_required));
            hasErrors = true;
        }
        if (!Validator.isPhoneNumber(viewModel.phoneNumber.getValue())) {
            viewModel.phoneNumberErrMsg.setValue(getString(R.string.err_phone_required));
            hasErrors = true;
        }

        return !hasErrors;
//        if (TextUtils.isEmpty(viewModel.firstName.getValue())
//                || TextUtils.isEmpty(viewModel.lastName.getValue())
//                || TextUtils.isEmpty(viewModel.emailAddress.getValue())
//                || TextUtils.isEmpty(viewModel.phoneNumber.getValue())) {
//            Toast.makeText(this, R.string.err_all_fields_required, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
    }

    /** Return true if all fields on the second page has valid values */
    private boolean validateAddress() {
        // TODO: add validation for contry fields
        boolean hasErrors = false;
        if (TextUtils.isEmpty(viewModel.address.getValue())) {
            viewModel.addressErrMsg.setValue(getString(R.string.err_required));
            hasErrors = true;
        }
        if (TextUtils.isEmpty(viewModel.city.getValue())) {
            viewModel.cityErrMsg.setValue(getString(R.string.err_required));
            hasErrors = true;
        }
        if (TextUtils.isEmpty(viewModel.postalCode.getValue())) {
            viewModel.postalCodeErrMsg.setValue(getString(R.string.err_required));
            hasErrors = true;
        }
        return !hasErrors;
//        if (TextUtils.isEmpty(viewModel.address.getValue())
//                || TextUtils.isEmpty(viewModel.city.getValue())
//                || TextUtils.isEmpty(viewModel.postalCode.getValue())) {
//            Toast.makeText(this, R.string.err_all_fields_required, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
    }

    /** Return true if both fields contain the same string */
    private boolean validatePassword() {
        boolean hasError = false;
        String newPassword = viewModel.newPassword.getValue();
        if (TextUtils.isEmpty(newPassword)) {
            viewModel.newPasswordErrMsg.setValue(getString(R.string.err_required));
            hasError = true;
        }
        String verifyPassword = viewModel.verifyPassword.getValue();
        if (TextUtils.isEmpty(verifyPassword)) {
            viewModel.verifyPasswordErrMsg.setValue(getString(R.string.err_required));
            hasError = true;
        }
        if (hasError) return false;

        if (!newPassword.equals(verifyPassword)) {
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
