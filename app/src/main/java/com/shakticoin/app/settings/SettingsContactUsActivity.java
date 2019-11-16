package com.shakticoin.app.settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityContactUsBinding;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.wallet.BaseWalletActivity;

public class SettingsContactUsActivity extends BaseWalletActivity {
    private ActivityContactUsBinding binding;
    private ContactUsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(ContactUsViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.settings_contact_us_title), true);

        binding.emailLayout.setValidator((view, value) -> Validator.isEmail(value));
        binding.emailLayout.setValidator((view, value) -> Validator.isPhoneNumber(value));
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    public void onSend(View v) {
        boolean validationSuccessful = true;
        if (TextUtils.isEmpty(viewModel.name.getValue())) {
            validationSuccessful = false;
            binding.nameLayout.setError(getString(R.string.err_required));
        }
        if (!Validator.isEmail(viewModel.emailAddress.getValue())) {
            validationSuccessful = false;
            binding.emailLayout.setError(getString(R.string.err_email_required));
        }
        if (!Validator.isPhoneNumber(viewModel.phoneNumber.getValue())) {
            validationSuccessful = false;
            binding.phoneLayout.setError(getString(R.string.err_phone_required));
        }
        if (TextUtils.isEmpty(viewModel.message.getValue())) {
            validationSuccessful = false;
            binding.messageLayout.setError(getString(R.string.err_required));
        }

        if (validationSuccessful) {
            finish();
        }
    }

    public void onCancel(View v) {
        finish();
    }
}
