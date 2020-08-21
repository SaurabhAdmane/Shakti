package com.shakticoin.app.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.onboard.OnboardRepository;
import com.shakticoin.app.api.wallet.SessionException;
import com.shakticoin.app.api.wallet.WalletRepository;
import com.shakticoin.app.databinding.ActivityWalletAdminBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.FormatUtil;
import com.shakticoin.app.widget.DrawerActivity;

import java.math.BigDecimal;

public class WalletAdminActivity extends DrawerActivity {
    private ActivityWalletAdminBinding binding;
    private WalletRepository walletRepository = new WalletRepository();
    private OnboardRepository onboardRepository = new OnboardRepository();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_admin);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.wallet_toolbar_title), true);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            String passphrase = Session.getWalletPassphrase();
            if (!TextUtils.isEmpty(passphrase)) {
                // we need to create a new wallet
                onboardRepository.createWallet(passphrase, new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(String walletBytes, Throwable error) {
                        binding.progressBar.setVisibility(View.INVISIBLE);
                        if (error != null) {
                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                            if (error instanceof RemoteException && ((RemoteException) error).getResponseCode() == 201) {
                                Session.setWalletPassphrase(null);
                            }
                            return;
                        }
                        walletRepository.storeWallet(walletBytes);
                        getWalletBalance();
                    }
                });
            }
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
                    binding.balance.setText(FormatUtil.formatCoinAmount(balance));
                }
            });
        }
    }

    public void onOpenBalanceHistory(View v) {
        startActivity(new Intent(this, WalletHistoryActivity.class));
    }
}
