package com.shakticoin.app.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.api.wallet.SessionException;
import com.shakticoin.app.api.wallet.WalletRepository;
import com.shakticoin.app.databinding.ActivityWalletBinding;
import com.shakticoin.app.registration.SignInActivity;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.widget.DrawerActivity;

import java.math.BigDecimal;

public class WalletActivity extends DrawerActivity {
    private ActivityWalletBinding binding;
    private WalletModel viewModel;

    private UserRepository userRepository = new UserRepository();
    private WalletRepository walletRepository = new WalletRepository();

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

    @Override
    protected void onResume() {
        super.onResume();

        // update wallet balance
        getWalletBalance();
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 0;
    }

    public void getWalletBalance() {
        if (Session.getWalletPassphrase() == null) {
            DialogPass.getInstance(new DialogPass.OnPassListener() {
                @Override
                public void onPassEntered(@Nullable String password) {
                    if (!TextUtils.isEmpty(password)) {
                        Session.setWalletPassphrase(password);
                    }
                    getWalletBalance();
                }
            }).show(getSupportFragmentManager(), DialogPass.class.getName());
            return;
        }

        Activity activity = this;

        binding.progressBar.setVisibility(View.VISIBLE);
        String walletBytes = walletRepository.getExistingWallet();
        if (walletBytes == null) {
            // we need to create a new wallet
            walletRepository.createWallet(Session.getWalletPassphrase(), new OnCompleteListener<String>() {
                @Override
                public void onComplete(String walletBytes, Throwable error) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    if (error != null) {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        return;
                    }
                    walletRepository.storeWallet(walletBytes);
                    getWalletBalance();
                }
            });
        } else {
            walletRepository.getBalance(new OnCompleteListener<BigDecimal>() {
                @Override
                public void onComplete(BigDecimal balance, Throwable error) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    if (error != null) {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        if (error instanceof SessionException) {
                            Session.setWalletPassphrase(null);
                            getWalletBalance();
                        }
                        return;
                    }
                    viewModel.balance.set(balance);
                }
            });
        }
    }
}
