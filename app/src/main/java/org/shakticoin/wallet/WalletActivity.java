package org.shakticoin.wallet;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityWalletBinding;
import org.shakticoin.registration.SignInActivity;

public class WalletActivity extends AppCompatActivity {
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

        viewModel = ViewModelProviders.of(this).get(WalletModel.class);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragments, new HomeFragment(), HomeFragment.class.getSimpleName())
                .commit();

    }
}
