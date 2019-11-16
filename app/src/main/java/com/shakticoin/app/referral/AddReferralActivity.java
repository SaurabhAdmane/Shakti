package com.shakticoin.app.referral;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityAddReferralBinding;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.wallet.BaseWalletActivity;

import java.util.Objects;

public class AddReferralActivity extends BaseWalletActivity {
    private ActivityAddReferralBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_referral);
        binding.setLifecycleOwner(this);

        binding.emailFieldLayout.setValidator((view, value) -> Validator.isEmail(value));
        binding.phoneFieldLayout.setValidator((view, value) -> Validator.isPhoneNumber(value));

        onInitView(binding.getRoot(), getString(R.string.my_refs_title), true);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 3;
    }

    public void onHowToEarn(View v) {
        DialogHowToBonus.getInstance().show(getSupportFragmentManager(), DialogHowToBonus.TAG);
    }

    public void onAddReferral(View v) {
        boolean validationSuccessful = true;
        if (TextUtils.isEmpty(Objects.requireNonNull(binding.firstName.getText()).toString())) {
            validationSuccessful = false;
            binding.firstNameFieldLayout.setError(getString(R.string.err_required));
        }
        if (TextUtils.isEmpty(Objects.requireNonNull(binding.lastName.getText()).toString())) {
            validationSuccessful = false;
            binding.lastNameFieldLayout.setError(getString(R.string.err_required));
        }

        String emailAddress = Objects.requireNonNull(binding.emailAddress.getText()).toString();
        String phoneNumber = Objects.requireNonNull(binding.phoneNumber.getText()).toString();
        if (!(Validator.isPhoneNumber(phoneNumber) || Validator.isEmail(emailAddress))) {
            validationSuccessful = false;
            if (!Validator.isEmail(emailAddress)) {
                binding.emailFieldLayout.setError(getString(R.string.err_either_email_phone_required));
            }
            if (!Validator.isPhoneNumber(phoneNumber)) {
                binding.phoneFieldLayout.setError(getString(R.string.err_either_email_phone_required));
            }
        }
        if (validationSuccessful) {
            finish();
        }
    }
}
