package com.shakticoin.app.settings;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.common.CommonRepository;
import com.shakticoin.app.api.common.ContactUs;
import com.shakticoin.app.api.common.RequestReason;
import com.shakticoin.app.databinding.ActivityContactUsBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.wallet.BaseWalletActivity;

import java.util.List;

public class SettingsContactUsActivity extends BaseWalletActivity {
    private ActivityContactUsBinding binding;
    private ContactUsViewModel viewModel;
    private CommonRepository commonRepository = new CommonRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(ContactUsViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.settings_contact_us_title), true);

        commonRepository.getRequestReasons(new OnCompleteListener<List<RequestReason>>() {
            @Override
            public void onComplete(List<RequestReason> value, Throwable error) {
                if (error != null) {
                    return;
                }
                viewModel.reasonList.setValue(value);
            }
        });

        binding.emailLayout.setValidator((view, value) -> Validator.isEmail(value));
        binding.phoneLayout.setValidator((view, value) -> Validator.isPhoneNumber(value));
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

        if (viewModel.selectedReason.getValue() == null) {
            validationSuccessful = false;
            binding.reasonLayout.setError(getString(R.string.settings_contact_us_err_reason));
        }
        if (!validationSuccessful) return;

        ContactUs messagePayload = new ContactUs();
        RequestReason reason = viewModel.selectedReason.getValue();
        if (reason != null) {
            messagePayload.setReason(reason.getId());
        }
        messagePayload.setEmail(viewModel.emailAddress.getValue());
        messagePayload.setMobile(viewModel.phoneNumber.getValue());
        messagePayload.setName(viewModel.name.getValue());
        messagePayload.setMessage(viewModel.message.getValue());

        final Activity self = this;
        commonRepository.sendSupportMessage(messagePayload, new OnCompleteListener<ContactUs>() {
            @Override
            public void onComplete(ContactUs value, Throwable error) {
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(self));
                    } else {
                        Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                Toast.makeText(self, R.string.settings_contact_us_success, Toast.LENGTH_SHORT).show();
            }
        });
        finish();
    }

    public void onCancel(View v) {
        finish();
    }
}
