package org.shakticoin.settings;

import android.os.Bundle;
import android.text.Html;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import org.apache.commons.io.IOUtils;
import org.shakticoin.R;
import org.shakticoin.databinding.ActivityProcessingFeeBinding;
import org.shakticoin.wallet.BaseWalletActivity;

import java.io.IOException;
import java.io.InputStream;

public class SettingsProcFeeActivity extends BaseWalletActivity {
    private ActivityProcessingFeeBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_processing_fee);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.settings_proc_fee_title), true);

        try {
            InputStream is = getResources().getAssets().open("processingFees.html");
            String helpPage = IOUtils.toString(is);
            binding.htmlView.setText(Html.fromHtml(helpPage));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
