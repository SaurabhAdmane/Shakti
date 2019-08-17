package org.shakticoin.settings;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;

import org.apache.commons.io.IOUtils;
import org.shakticoin.R;
import org.shakticoin.databinding.ActivityAppTermsBinding;
import org.shakticoin.wallet.BaseWalletActivity;

import java.io.IOException;
import java.io.InputStream;

public class SettingsAppTermsActivity extends BaseWalletActivity {
    private ActivityAppTermsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app_terms);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_app_terms_title), true);

        try {
            InputStream is = getResources().getAssets().open("applicationTerms.html");
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
