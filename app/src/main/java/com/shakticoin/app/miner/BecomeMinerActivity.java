package com.shakticoin.app.miner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.registration.MiningLicenseActivity;
import com.shakticoin.app.wallet.BaseWalletActivity;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityBecomeMinerBinding;

public class BecomeMinerActivity extends BaseWalletActivity {
    private ActivityBecomeMinerBinding binding;

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

    public void onSelectLicense(View v) {
        Intent intent = new Intent(this, MiningLicenseActivity.class);
        startActivity(intent);
    }

    public void onCancel(View v) {
        finish();
    }
}
