package com.shakticoin.app.registration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.bounty.BountyRepository;
import com.shakticoin.app.api.bounty.RegisterReferralResponseModel;
import com.shakticoin.app.api.otp.IntlPhoneCountryCode;
import com.shakticoin.app.databinding.ActivityReferralBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.wallet.WalletActivity;
import com.shakticoin.app.widget.MessageBox;
import com.shakticoin.app.widget.qr.QRScannerActivity;

import java.util.List;


public class ReferralActivity extends AppCompatActivity {
    private ReferralActivityModel viewModel;
    private ActivityReferralBinding binding;
    private BountyRepository bountyRepository;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRScannerActivity.REQUEST_QR) {
            if (resultCode == RESULT_OK) {
                String referralCodeKey = CommonUtil.prefixed(QRScannerActivity.KEY_REFERRAL_CODE);
                if (data != null && data.hasExtra(referralCodeKey)) {
                    String referralCode = data.getStringExtra(referralCodeKey);
                    // TODO: initially we put the referral code to the corresponding field but now
                    // this field is removed, so, we probably should pass through it to API call and
                    // close activity. Or what?
//                    viewModel.referralCode.setValue(referralCode);
                    Toast.makeText(this, referralCode, Toast.LENGTH_SHORT).show();
                    postReferralInfo();
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_referral);

        viewModel = ViewModelProviders.of(this).get(ReferralActivityModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        bountyRepository = new BountyRepository();
        bountyRepository.setLifecycleOwner(this);

        // display a special icon if content of the field conform the target format
        binding.emailAddressLayout.setValidator((view, value) -> Validator.isEmail(value));
        binding.mobileNumberLayout.setValidator((view, value) -> Validator.isPhoneNumber(value));

        binding.contactMechSelector.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                binding.emailAddressLayout.setVisibility(View.GONE);
                binding.countryCodesLayout.setVisibility(View.VISIBLE);
                binding.mobileNumberLayout.setVisibility(View.VISIBLE);
            } else {
                binding.emailAddressLayout.setVisibility(View.VISIBLE);
                binding.countryCodesLayout.setVisibility(View.GONE);
                binding.mobileNumberLayout.setVisibility(View.GONE);
            }
        });

        Activity activity = this;
        if (viewModel.getCountryCodes() != null) {
            viewModel.getCountryCodes().observe(this, new Observer<List<IntlPhoneCountryCode>>() {
                @Override
                public void onChanged(List<IntlPhoneCountryCode> codes) {
                    if (codes != null) {
                        String deviceIsoCode = CommonUtil.getCountryCode2(activity);
                        if (deviceIsoCode != null) {
                            List<IntlPhoneCountryCode> codesList = viewModel.getCountryCodes().getValue();
                            if (codesList != null) {
                                for (IntlPhoneCountryCode item : codesList) {
                                    if (deviceIsoCode.equals(item.getIsoCode())) {
                                        viewModel.getSelectedCountryCode().setValue(item);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }

    }

    public void OnSkipReferral(View view) {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }

    public void onReward(View view) {
        String emailOrMobile;
        boolean emailRegistration = false;
        boolean validationSuccessful = true;
        if (binding.contactMechSelector.isChecked()) {
            // phone number selected
            if (!Validator.isPhoneNumber(viewModel.getMobileNumber().getValue())) {
                validationSuccessful = false;
                binding.mobileNumberLayout.setError(getString(R.string.err_phone_required));
            }
            IntlPhoneCountryCode phoneCode = viewModel.getSelectedCountryCode().getValue();
            StringBuilder sb = new StringBuilder();
            if (phoneCode != null) {
                sb.append(phoneCode.getCountryCode());
            }
            sb.append(binding.mobileNumber.getText().toString());
            emailOrMobile = sb.toString();
        } else {
            // email address selected
            emailRegistration = true;
            if (!Validator.isEmail(viewModel.getEmailAddress().getValue())) {
                validationSuccessful = false;
                binding.emailAddressLayout.setError(getString(R.string.err_email_required));
            }
            emailOrMobile = binding.emailAddress.getText().toString();
        }
        if (TextUtils.isEmpty(viewModel.getPromoCode().getValue())) {
            binding.promoCodeLayout.setError(getString(R.string.ref_promocode_required));
            validationSuccessful = false;
        }
        if (!validationSuccessful) return;

        Activity activity = this;
        binding.progressBar.setVisibility(View.VISIBLE);
        bountyRepository.registerReferral(emailOrMobile, emailRegistration,
                viewModel.getPromoCode().getValue(), new OnCompleteListener<RegisterReferralResponseModel>() {
            @Override
            public void onComplete(RegisterReferralResponseModel value, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    new MessageBox(Debug.getFailureMsg(activity, error)).show(getSupportFragmentManager(), null);
                    return;
                }

                Boolean success = value.isReferralRegisterSuccessful();
                if (success == null) success = false;

                if (!success) {
                    new MessageBox(value.getMessage()).show(getSupportFragmentManager(), null);
                    return;
                }

                Intent intent = new Intent(activity, ReferralConfirmationActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onReadQRCode(View view) {
        startActivityForResult(new Intent(this, QRScannerActivity.class), QRScannerActivity.REQUEST_QR);
    }

    private void postReferralInfo() {

    }

}
