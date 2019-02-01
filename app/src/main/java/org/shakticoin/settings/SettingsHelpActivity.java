package org.shakticoin.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;

import org.apache.commons.io.IOUtils;
import org.shakticoin.R;
import org.shakticoin.databinding.ActivityHelpBinding;
import org.shakticoin.wallet.BaseWalletActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

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
