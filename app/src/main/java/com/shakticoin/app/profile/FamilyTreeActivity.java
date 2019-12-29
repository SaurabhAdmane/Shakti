package com.shakticoin.app.profile;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.shakticoin.app.R;
import com.shakticoin.app.wallet.BaseWalletActivity;

public class FamilyTreeActivity extends BaseWalletActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_tree);

        onInitView(findViewById(R.id.container), getString(R.string.family_title), true);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 6;
    }
}
