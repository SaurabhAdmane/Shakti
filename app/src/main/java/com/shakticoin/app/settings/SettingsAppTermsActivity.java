package com.shakticoin.app.settings;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityAppTermsBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.HtmlUtil;
import com.shakticoin.app.widget.DrawerActivity;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class SettingsAppTermsActivity extends DrawerActivity {
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
            binding.htmlView.setText(HtmlUtil.fromHtml(helpPage, binding.htmlView));
            binding.htmlView.setLinksClickable(true);
            binding.htmlView.setMovementMethod(new LinkMovementMethod());

        } catch (IOException e) {
            Debug.logException(e);
        }
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }
}
