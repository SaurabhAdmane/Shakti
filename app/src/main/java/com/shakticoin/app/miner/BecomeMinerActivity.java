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
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.LicenseType;
import com.shakticoin.app.databinding.ActivityBecomeMinerBinding;
import com.shakticoin.app.payment.PaymentFlowActivity;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.widget.DrawerActivity;

import java.util.List;

public class BecomeMinerActivity extends DrawerActivity {
    private ActivityBecomeMinerBinding binding;
    private LicenseRepository licenceRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_become_miner);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.miner_intro_toolbar));

        licenceRepository = new LicenseRepository();
        licenceRepository.setLifecycleOwner(this);

        binding.textNote.setText(Html.fromHtml(getString(R.string.miner_intro_text)));

        // we need display a price for licence plan with code M101Y
        Activity activity = this;
        binding.progressBar.setVisibility(View.VISIBLE);
        licenceRepository.getLicenses(new OnCompleteListener<List<LicenseType>>() {
            @Override
            public void onComplete(List<LicenseType> value, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                    return;
                }

                if (value != null && !value.isEmpty()) {
                    for (LicenseType licenseType : value) {
                        if ("M101Y".equals(licenseType.getPlanCode())) {
                            Double price = licenseType.getPrice();
                            if (price != null) {
                                binding.amount.setText(getString(R.string.price_usd, price));
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 2;
    }

    public void onPayLicense(View v) {
        Activity activity = this;

        binding.progressBar.setVisibility(View.VISIBLE);
        licenceRepository.checkoutSubscription("M101Y", new OnCompleteListener<String>() {
            @Override
            public void onComplete(String targetUrl, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent(activity, PaymentFlowActivity.class);
                intent.putExtra(CommonUtil.prefixed("planCode"), "M101Y");
                intent.putExtra(CommonUtil.prefixed("targetUrl"), targetUrl);
                startActivity(intent);
            }
        });
    }

    public void onCancel(View v) {
        finish();
    }
}
