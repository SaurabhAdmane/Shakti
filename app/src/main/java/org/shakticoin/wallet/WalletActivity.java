package org.shakticoin.wallet;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.shakticoin.R;
import org.shakticoin.api.miner.MinerDataResponse;
import org.shakticoin.databinding.ActivityWalletBinding;

public class WalletActivity extends AppCompatActivity {
    private ActivityWalletBinding binding;
    private WalletModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(WalletModel.class);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragments, new HomeFragment(), HomeFragment.class.getSimpleName())
                .commit();

    }

    public void onShareLink(View view) {
    }

    public void onShareCode(View view) {
    }

}
