package org.shakticoin.settings;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityPersonalInfoBinding;
import org.shakticoin.wallet.BaseWalletActivity;

public class SettingsPersonalActivity extends BaseWalletActivity {
    private ActivityPersonalInfoBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_info);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_personal_title), true);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }
}
