package org.shakticoin.miner;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityBecomeMinerBinding;
import org.shakticoin.registration.MiningLicenseActivity;
import org.shakticoin.wallet.BaseWalletActivity;

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

    public void onSelectLicense(View v) {
        Intent intent = new Intent(this, MiningLicenseActivity.class);
        startActivity(intent);
    }

    public void onCancel(View v) {
        finish();
    }
}
