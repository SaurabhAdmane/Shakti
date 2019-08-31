package com.shakticoin.app.settings;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;

import org.apache.commons.io.IOUtils;
import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityTaxesBinding;
import com.shakticoin.app.wallet.BaseWalletActivity;

import java.io.IOException;
import java.io.InputStream;

public class SettingsTaxesActivity extends BaseWalletActivity {
    private ActivityTaxesBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_taxes);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_tax_info_title), true);

        try {
            InputStream is = getResources().getAssets().open("taxes.html");
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
