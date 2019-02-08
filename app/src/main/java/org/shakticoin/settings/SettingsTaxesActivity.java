package org.shakticoin.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;

import org.apache.commons.io.IOUtils;
import org.shakticoin.R;
import org.shakticoin.databinding.ActivityHelpBinding;
import org.shakticoin.databinding.ActivityTaxesBinding;
import org.shakticoin.wallet.BaseWalletActivity;

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
}
