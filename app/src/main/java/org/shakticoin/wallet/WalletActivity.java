package org.shakticoin.wallet;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityWalletBinding;
import org.shakticoin.registration.SignInActivity;

public class WalletActivity extends BaseWalletActivity {
    private ActivityWalletBinding binding;
    private WalletModel viewModel;

    @Override
    public void onBackPressed() {
        // This activity has been started as a root of new task and back stack is cleared. Pressing
        // Back finish the application but we don't want this and launch login screen instead.
        // This behaviour may conflict with back navigation through the fragments in future versions.
        Intent intent = new Intent(this, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet);
        binding.setLifecycleOwner(this);

        // need to call in any subclass of BaseWalletActivity
        super.onInitView(binding.getRoot(), getString(R.string.wallet_toolbar_title));

        viewModel = ViewModelProviders.of(this).get(WalletModel.class);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new MainFragment(), MainFragment.class.getSimpleName())
                .commit();
    }
}
