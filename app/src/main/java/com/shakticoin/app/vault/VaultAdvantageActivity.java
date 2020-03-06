package com.shakticoin.app.vault;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.widget.DrawerActivity;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityVaultAdvantageBinding;

public class VaultAdvantageActivity extends DrawerActivity {
    private ActivityVaultAdvantageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vault_advantage);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.vault_title));
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 1;
    }

    public void onCreateVault(View v) {
        Intent intent = new Intent(this, VaultChooserActivity.class);
        startActivity(intent);
    }

    public void onCancel(View v) {
        finish();
    }
}
