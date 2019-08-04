package org.shakticoin.registration;

import android.app.Activity;
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

import org.shakticoin.R;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.auth.AuthRepository;
import org.shakticoin.api.country.Country;
import org.shakticoin.util.Debug;
import org.shakticoin.util.Validator;


public class SignUpActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private SignUpActivityModel viewModel;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        authRepository = new AuthRepository();

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
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
                    return;
                }
                DialogConfirmEmail.getInstance(false)
                        .show(getSupportFragmentManager(), DialogConfirmEmail.class.getSimpleName());
            }
        });
    }

    public void onGoBack(View view) {
        onContactsPageSelected();
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
        if (TextUtils.isEmpty(viewModel.postalCode.getValue())) {
            viewModel.postalCodeErrMsg.setValue(getString(R.string.err_required));
            hasErrors = true;
        }
        Country selectedCountry = viewModel.countryCode.get();
        if (selectedCountry != null) {
            String code = selectedCountry.getCode();
            // ensure state/province is set for USA and Canada
            if (("US".equals(code) || "CA".equals(code))
                    && TextUtils.isEmpty(viewModel.state.getValue())) {
                viewModel.stateErrMsg.setValue(getString(R.string.err_state_reqired));
                hasErrors = true;
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
        String emailAddress = viewModel.emailAddress.getValue();
        String phoneNumber = viewModel.phoneNumber.getValue();
        Activity activity = this;
        authRepository.checkEmailPhoneExists(this, emailAddress, phoneNumber, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(Boolean value, Throwable error) {
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value) {
                    // success means neither email nor phone number used in the database
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame, new SignUpAddressFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
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
