package com.shakticoin.app.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.Subdivision;
import com.shakticoin.app.util.Validator;

import java.util.Arrays;
import java.util.List;


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
        // TODO: we skip page with address information for now but keep it just in case
        if ("ContactsPage".equals(tag)) {
//            if (validateContacts()) onAddressPageSelected();
            if (validateContacts()) onPasswordPageSelected();

//        } else if ("AddressPage".equals(tag)) {
//            if (validateAddress()) onPasswordPageSelected();
//
        } else if ("PasswordPage".equals(tag)) {
            if (validatePassword()) startRegistration();
        }
    }

    public void startRegistration() {
//        final Activity activity = this;
//        viewModel.createUser(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(Void value, Throwable error) {
//                if (error != null) {
//                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
//                    Debug.logException(error);
//                    return;
//                }
//
//                UserAccount user = Session.getUserAccount();
//                // TODO: confirmation in email does not work yet
//
//                DialogConfirmEmail.getInstance(false)
//                        .show(getSupportFragmentManager(), DialogConfirmEmail.class.getSimpleName());
//            }
//        });
    }

    public void onGoBack(View view) {
        onContactsPageSelected();
    }

    public void onDoLogin(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    public void onCancel(View view) {
        startActivity(Session.unauthorizedIntent(this));
        finish();
    }

    /** Return true if all fields on the first page has valid values */
    private boolean validateContacts() {
        boolean hasErrors = false;
        /*
        if (TextUtils.isEmpty(viewModel.firstName.getValue())) {
            viewModel.firstNameErrMsg.setValue(getString(R.string.err_required));
            hasErrors = true;
        }
        if (TextUtils.isEmpty(viewModel.lastName.getValue())) {
            viewModel.lastNameErrMsg.setValue(getString(R.string.err_required));
            hasErrors = true;
        }
        */

        boolean hasEmailAndPhone = true;
        if (!Validator.isPhoneNumber(viewModel.phoneNumber.getValue())) {
            viewModel.phoneNumberErrMsg.setValue(getString(R.string.err_phone_required));
            hasEmailAndPhone = false;
            hasErrors = true;
        }
        if (!Validator.isEmail(viewModel.emailAddress.getValue())) {
            viewModel.emailAddressErrMsg.setValue(getString(R.string.err_email_required));
            hasEmailAndPhone = false;
            hasErrors = true;
        }

        // UI does not show both fields at the same time and field error mark can be hidden for either
        // email or phone, depending on the switch position. This is why we show a toast message.
        if (!hasEmailAndPhone) {
            Toast.makeText(this, "Both email address and phone number are required", Toast.LENGTH_LONG).show();
        }

        return !hasErrors;
    }

    /** Return true if all fields on the second page has valid values */
    private boolean validateAddress() {
        boolean hasErrors = false;
        if (viewModel.countryCode.get() == null) {
            viewModel.countryCodeErrMsg.setValue(getString(R.string.err_country_required));
            hasErrors = true;
        }
        if (viewModel.citizenshipCode.get() == null) {
            viewModel.citizenshipCodeErrMsg.setValue(getString(R.string.err_citizenship_required));
            hasErrors = true;
        }
        if (TextUtils.isEmpty(viewModel.address.getValue())) {
            viewModel.addressErrMsg.setValue(getString(R.string.err_required));
            hasErrors = true;
        }
        if (TextUtils.isEmpty(viewModel.city.getValue())) {
            viewModel.cityErrMsg.setValue(getString(R.string.err_required));
            hasErrors = true;
        }
        Country selectedCountry = viewModel.countryCode.get();
        if (!Validator.isPostalCodeValid(
                selectedCountry != null ? selectedCountry.getCode() : null, viewModel.postalCode.getValue())) {
            viewModel.postalCodeErrMsg.setValue(getString(R.string.err_postalCode_requird));
            hasErrors = true;
        }
        if (selectedCountry != null) {
            String code = selectedCountry.getCode();
            // ensure state/province is set for USA and Canada
            List<String> stateRequiredContries = Arrays.asList("US", "CA");
            if (stateRequiredContries.contains(code)) {
                Subdivision subdivision = viewModel.stateProvinceCode.get();
                if (subdivision == null || !subdivision.getCountry().equals(code)) {
                    viewModel.stateErrMsg.setValue(getString(R.string.err_state_reqired));
                    hasErrors = true;
                }
            }
        }
        return !hasErrors;
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

        // Passwords are not empty and equals at this point. Let's verify the password is
        // strong enough.
        if (!Validator.isPasswordStrong(newPassword)) {
            Toast.makeText(this, R.string.err_password_not_strong, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void onContactsPageSelected() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, new SignUpContactsFragment())
                .commit();
    }

    /**
     * Check if we can use entered email and phone for a new account and switch to the next page.
     */
    private void onAddressPageSelected() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, new SignUpAddressFragment())
                .addToBackStack(null)
                .commit();
    }

    private void onPasswordPageSelected() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame, new SignUpPasswordFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        onMainAction(v);
        return true;
    }
}
