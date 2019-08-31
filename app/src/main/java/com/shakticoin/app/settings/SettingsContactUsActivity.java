package com.shakticoin.app.settings;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityContactUsBinding;
import com.shakticoin.app.wallet.BaseWalletActivity;

public class SettingsContactUsActivity extends BaseWalletActivity {
    private ActivityContactUsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_contact_us_title), true);

    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }
}
