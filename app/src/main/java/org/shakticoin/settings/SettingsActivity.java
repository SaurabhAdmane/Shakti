package org.shakticoin.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.shakticoin.R;
import org.shakticoin.wallet.BaseWalletActivity;

public class SettingsActivity extends BaseWalletActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
