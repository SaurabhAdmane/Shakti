package com.shakticoin.app.miner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.databinding.ActivityBecomeMinerBinding;
import com.shakticoin.app.payment.PaymentFlowActivity;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.widget.DrawerActivity;

public class BecomeMinerActivity extends DrawerActivity {
    private ActivityBecomeMinerBinding binding;
    private KYCRepository kycRepository = new KYCRepository();
    private LicenseRepository licenceRepository = new LicenseRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_become_miner);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.miner_intro_toolbar));

        binding.textNote.setText(Html.fromHtml(getString(R.string.miner_intro_text)));
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 2;
    }

    public void onPayLicense(View v) {
        Activity activity = this;

        licenceRepository.checkoutSubscription("M101Y", new OnCompleteListener<String>() {
            @Override
            public void onComplete(String targetUrl, Throwable error) {
                if (error != null) {
                    Toast.makeText(activity, R.string.err_unexpected, Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(activity, PaymentFlowActivity.class);
                intent.putExtra(CommonUtil.prefixed("planCode", activity), "M101Y");
                intent.putExtra(CommonUtil.prefixed("targetUrl", activity), targetUrl);
                startActivity(intent);
            }
        });
    }

    public void onCancel(View v) {
        finish();
    }
}
