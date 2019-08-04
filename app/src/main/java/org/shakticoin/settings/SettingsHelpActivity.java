package org.shakticoin.settings;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;

import org.apache.commons.io.IOUtils;
import org.shakticoin.R;
import org.shakticoin.databinding.ActivityHelpBinding;
import org.shakticoin.wallet.BaseWalletActivity;

import java.io.IOException;
import java.io.InputStream;

public class SettingsHelpActivity extends BaseWalletActivity {
    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_help);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_help_title), true);

        try {
            InputStream is = getResources().getAssets().open("help.html");
            String helpPage = IOUtils.toString(is);
            binding.htmlView.setText(Html.fromHtml(helpPage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
