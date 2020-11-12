package com.shakticoin.app.registration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteMessageException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.referral.ReferralParameters;
import com.shakticoin.app.api.referral.ReferralRepository;
import com.shakticoin.app.api.referral.model.Referral;
import com.shakticoin.app.api.referrals.BountyRepository;
import com.shakticoin.app.databinding.ActivityReferralBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.wallet.WalletActionsFragment;
import com.shakticoin.app.wallet.WalletActivity;
import com.shakticoin.app.widget.qr.QRScannerActivity;

import java.util.Map;


public class ReferralActivity extends AppCompatActivity {
    private ReferralActivityModel viewModel;
    private ActivityReferralBinding binding;
    private BountyRepository referralRepository;


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

        referralRepository = new BountyRepository();
        referralRepository.setLifecycleOwner(this);

        // display a special icon if content of the field conform the target format
        binding.emailAddressLayout.setValidator((view, value) -> Validator.isEmail(value));
        binding.mobileNumberLayout.setValidator((view, value) -> Validator.isPhoneNumber(value));

        binding.contactMechSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.emailAddressLayout.setVisibility(View.VISIBLE);
                    binding.mobileNumberLayout.setVisibility(View.GONE);
                    binding.countryPicker.setVisibility(View.GONE);
                }else{
                    binding.emailAddressLayout.setVisibility(View.GONE);
                    binding.mobileNumberLayout.setVisibility(View.VISIBLE);
                    binding.countryPicker.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void OnSkipReferral(View view) {
        //TODO: remove, was added for demo purpose
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }

    public void onReward(View view) {

        binding.progressBar.setVisibility(View.VISIBLE);
        boolean validationSuccessful = true;
        ReferralParameters referral = new ReferralParameters();

        if( binding.emailAddressLayout.getVisibility() == View.VISIBLE){
            if (!Validator.isEmail(viewModel.emailAddress.getValue())) {
                validationSuccessful = false;
                binding.emailAddressLayout.setError(getString(R.string.err_email_required));
            }
            referral.emailRegistration = true;
            referral.emailOrMobile = viewModel.emailAddress.getValue();
        }else{
            if (!Validator.isPhoneNumber(viewModel.mobileNumber.getValue())) {
                validationSuccessful = false;
                binding.mobileNumberLayout.setError(getString(R.string.err_phone_required));
            }
            referral.emailRegistration = false;
            referral.emailOrMobile = binding.tvCC.getSelectedCountryCode()
                    + viewModel.mobileNumber.getValue();
        }

        if (!validationSuccessful) return;

        if(binding.promotionalCode.getText().toString().trim().length()>0) {
            referral.promotionalCode = binding.promotionalCode.getText().toString();
        }

        referralRepository.getReferral(referral, new OnCompleteListener<Referral>() {
            @Override
            public void onComplete(Referral value, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    Toast.makeText(ReferralActivity.this, Debug.getFailureMsg(ReferralActivity.this, error), Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(ReferralActivity.this, ReferralConfirmationActivity.class);
                startActivity(intent);
            }
        });


    }

    public void onReadQRCode(View view) {
        startActivityForResult(new Intent(this, QRScannerActivity.class), QRScannerActivity.REQUEST_QR);
    }


}
