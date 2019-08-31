package com.shakticoin.app.registration;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.tier.Tier;
import com.shakticoin.app.api.tier.TierRepository;
import com.shakticoin.app.databinding.ActivityReferralBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.wallet.WalletActivity;
import com.shakticoin.app.widget.qr.QRScannerActivity;

import java.util.ArrayList;
import java.util.List;


public class ReferralActivity extends AppCompatActivity {
    private ArrayList<Tier> tiers;
    private ReferralActivityModel viewModel;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRScannerActivity.REQUEST_QR) {
            if (resultCode == RESULT_OK) {
                String referralCodeKey = CommonUtil.prefixed(QRScannerActivity.KEY_REFERRAL_CODE, this);
                if (data != null && data.hasExtra(referralCodeKey)) {
                    String referralCode = data.getStringExtra(referralCodeKey);
                    viewModel.referralCode.setValue(referralCode);
                    Toast.makeText(this, referralCode, Toast.LENGTH_SHORT).show();
                    postReferralInfo();
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityReferralBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_referral);

        viewModel = ViewModelProviders.of(this).get(ReferralActivityModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        // grab tiers in advance
        //FIXME: Replace hardcoded country code with one selected by user
        TierRepository repository = new TierRepository();
        repository.getTiers("AF", new OnCompleteListener<List<Tier>>() {
            @Override
            public void onComplete(List<Tier> value, Throwable error) {
                if (error == null) {
                    tiers = new ArrayList<>(value);
                }
            }
        });

        // display a special icon if content of the field conform the target format
        binding.emailAddressLayout.setValidator((view, value) -> Validator.isEmail(value));
        binding.mobileNumberLayout.setValidator((view, value) -> Validator.isPhoneNumber(value));

        // default action
        binding.referralCode.setOnEditorActionListener((v, actionId, event) -> {
            if (EditorInfo.IME_ACTION_DONE == actionId) {
                postReferralInfo();
                return true;
            }
            return false;
        });
    }

    public void OnSkipReferral(View view) {
        //TODO: remove, was added for demo purpose
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);

//        selectTier();
    }

    public void onReward(View view) {
        postReferralInfo();

        //TODO: remove, was added for demo purpose
        Intent intent = new Intent(this, ReferralConfirmationActivity.class);
        startActivity(intent);
    }

    public void onReadQRCode(View view) {
        startActivityForResult(new Intent(this, QRScannerActivity.class), QRScannerActivity.REQUEST_QR);
    }

    private void postReferralInfo() {
        // at least one field should have a value
        if (TextUtils.isEmpty(viewModel.fullName.getValue()) &&
                TextUtils.isEmpty(viewModel.emailAddress.getValue()) &&
                TextUtils.isEmpty(viewModel.mobileNumber.getValue()) &&
                TextUtils.isEmpty(viewModel.referralCode.getValue()))
        {
            Toast.makeText(this, R.string.err_atleast_one_required, Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: when api is ready we should call confirmation activity if referrer found
        selectTier();
    }

    private void selectTier() {

//        Intent intent = new Intent(this, MiningLicenseActivity.class);
//        if (tiers != null) {
//            intent.putParcelableArrayListExtra(CommonUtil.prefixed("tiersList", this), tiers);
//        }
//        startActivity(intent);
    }
}
