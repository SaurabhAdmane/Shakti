package com.shakticoin.app.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.license.LicenseRepository;
import com.shakticoin.app.api.license.NodeOperatorModel;
import com.shakticoin.app.api.license.SubscribedLicenseModel;
import com.shakticoin.app.api.onboard.OnboardRepository;
import com.shakticoin.app.api.wallet.SessionException;
import com.shakticoin.app.api.wallet.WalletRepository;
import com.shakticoin.app.databinding.ActivityWalletBinding;
import com.shakticoin.app.miner.BecomeMinerActivity;
import com.shakticoin.app.registration.SignInActivity;
import com.shakticoin.app.room.AppDatabase;
import com.shakticoin.app.room.LockStatusDao;
import com.shakticoin.app.room.entity.LockStatus;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.vault.VaultChooserActivity;
import com.shakticoin.app.widget.DrawerActivity;

import java.math.BigDecimal;
import java.util.List;

public class WalletActivity extends DrawerActivity {
    private ActivityWalletBinding binding;
    private WalletModel viewModel;

    private WalletRepository walletRepository = new WalletRepository();
    private KYCRepository kycRepository = new KYCRepository();
    private OnboardRepository onboardRepository = new OnboardRepository();
    private LicenseRepository licenseRepository = new LicenseRepository();

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

        // need to call in any subclass of DrawerActivity
        super.onInitView(binding.getRoot(), getString(R.string.wallet_toolbar_title));

        viewModel = ViewModelProviders.of(this).get(WalletModel.class);
        binding.setViewModel(viewModel);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // update wallet balance
        getWalletBalance();

        // Decides if we should display call-to-action "Become a miner".
        // Basically, if the user has a license then do not display.
        Activity activity = this;
        licenseRepository.getNodeOperator(new OnCompleteListener<NodeOperatorModel>() {
            @Override
            public void onComplete(NodeOperatorModel value, Throwable error) {
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                    return;
                }
                List<SubscribedLicenseModel> licences = value.getSubscribedLicenses();
                if (licences != null && !licences.isEmpty()) {
                    binding.becomeMinerBox.setVisibility(View.GONE);
                } else {
                    binding.becomeMinerBox.setVisibility(View.VISIBLE);
                }
            }
        });

        // check wallet lock status and display action buttons if unlocked
        new CheckWalletLocked(getSupportFragmentManager(), binding.walletActionsProgressBar).execute();
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 0;
    }

    public void onAdminWallet(View v) {
        startActivity(new Intent(this, WalletAdminActivity.class));
    }

    public void onBalanceHistory(View v) {
        startActivity(new Intent(this, WalletHistoryActivity.class));
    }

    public void onShowBusinessVaultInfo(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogBusinessVault.newInstance(v1 -> {
            Intent intent = new Intent(this, VaultChooserActivity.class);
            startActivity(intent);
        }).show(fragmentManager, DialogBusinessVault.TAG);
    }

    public void onBecomeMiner(View v) {
        startActivity(new Intent(this, BecomeMinerActivity.class));
    }

    public void onShowUnlockInfo(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogGrabWallet.newInstance().show(fragmentManager, DialogGrabWallet.TAG);
    }

    private void getWalletBalance() {
        if (Session.getWalletPassphrase() == null) {
            DialogPass.getInstance(password -> {
                if (!TextUtils.isEmpty(password)) {
                    Session.setWalletPassphrase(password);
                }
                getWalletBalance();
            }).show(getSupportFragmentManager(), DialogPass.class.getName());
            return;
        }

        Activity activity = this;

        viewModel.isProgressBarActive.set(true);
        String walletBytes = walletRepository.getExistingWallet();
        if (walletBytes == null) {
            String passphrase = Session.getWalletPassphrase();
            if (!TextUtils.isEmpty(passphrase)) {
                // we need to create a new wallet
                onboardRepository.createWallet(Session.getWalletPassphrase(), new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(String walletBytes, Throwable error) {
                        viewModel.isProgressBarActive.set(false);
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
                    viewModel.isProgressBarActive.set(false);
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

    static class CheckWalletLocked extends AsyncTask<Void, Void, Boolean> {
        FragmentManager fragmentManager;
        ProgressBar walletActionsProgressBar;

        CheckWalletLocked(FragmentManager fragmentManager, ProgressBar progressBar) {
            this.fragmentManager = fragmentManager;
            walletActionsProgressBar = progressBar;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            LockStatusDao lockStatuses = AppDatabase.getDatabase(ShaktiApplication.getContext()).lockStatusDao();
            LockStatus status = lockStatuses.getWalletKYCLockStatus();
            return status != null && "UNLOCKED".equals(status.getStatus());
        }

        @Override
        protected void onPostExecute(Boolean unlocked) {
            if (unlocked) {
                walletActionsProgressBar.setVisibility(View.GONE);
                fragmentManager
                        .beginTransaction()
                        .add(R.id.wallet_actions, new WalletActionsFragment())
                        .commit();
            } else {
                new KYCRepository().isWalletUnlocked(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(Boolean unlocked, Throwable error) {
                        walletActionsProgressBar.setVisibility(View.GONE);
                        fragmentManager
                                .beginTransaction()
                                .add(R.id.wallet_actions, error == null && unlocked ?
                                        new WalletActionsFragment() :
                                        new KycVerificationRequiredFragment())
                                .commit();
                    }
                });
            }
        }
    }
}
