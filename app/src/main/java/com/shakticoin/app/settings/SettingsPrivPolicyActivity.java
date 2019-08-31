package com.shakticoin.app.settings;

import android.os.Bundle;
import android.text.Html;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import org.apache.commons.io.IOUtils;
import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityPrivPolicyBinding;
import com.shakticoin.app.wallet.BaseWalletActivity;

import java.io.IOException;
import java.io.InputStream;

public class SettingsPrivPolicyActivity extends BaseWalletActivity {
    private ActivityPrivPolicyBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_priv_policy);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_privacy_policy_title), true);

        try {
            InputStream is = getResources().getAssets().open("privacyPolicy.html");
            String helpPage = IOUtils.toString(is);
            binding.htmlView.setText(Html.fromHtml(helpPage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }
}
