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
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.kyc.KycUserView;
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
    private WalletRepository walletRepository;
    private OnboardRepository onboardRepository;
    private com.shakticoin.app.api.kyc.KYCRepository kycrepository;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_admin);
        binding.setLifecycleOwner(this);

        onInitView(binding.getRoot(), getString(R.string.wallet_toolbar_title), true);

        walletRepository = new WalletRepository();
        walletRepository.setLifecycleOwner(this);
        onboardRepository = new OnboardRepository();
        onboardRepository.setLifecycleOwner(this);

        kycrepository = new com.shakticoin.app.api.kyc.KYCRepository();
        kycrepository.setLifecycleOwner(this);

        if(getIntent().getBooleanExtra("status", false)){
            binding.tvBWallet.setText(getResources().getString(R.string.wallet_admin_open));
            binding.tvBVault.setText(getResources().getString(R.string.wallet_admin_business_open));
        }else{
            binding.tvBWallet.setText(getResources().getString(R.string.wallet_admin_locked));
            binding.tvBVault.setText(getResources().getString(R.string.wallet_admin_locked));
        }

        getPassRecoveryStatus();
        getAllStatus();

    }

    private void getAllStatus() {
        kycrepository.getAllStatus(new OnCompleteListener<KycUserView>() {
            @Override
            public void onComplete(KycUserView kycUserView, Throwable error) {
                if (error != null) {
                    Toast.makeText(WalletAdminActivity.this, Debug.getFailureMsg(WalletAdminActivity.this, error), Toast.LENGTH_LONG).show();
                    if (error instanceof RemoteException && ((RemoteException) error).getResponseCode() == 201) {
                        Session.setWalletPassphrase(null);
                    }
                    return;
                }

                if(kycUserView.getKycStatus().equals("UNLOCKED")){
                    binding.tvStatusWallet.setText(getResources().getString(R.string.wallet_admin_open));
                    binding.tvStatusVault.setText(getResources().getString(R.string.wallet_admin_business_open));
                }else{
                    binding.tvStatusWallet.setText(getResources().getString(R.string.wallet_admin_locked));
                    binding.tvStatusVault.setText(getResources().getString(R.string.wallet_admin_locked));
                }

                if(kycUserView.getKycStatus().equals("UNLOCKED")){
                    binding.tvKycWallet.setText(getResources().getString(R.string.wallet_admin_open));
                    binding.tvKycVault.setText(getResources().getString(R.string.wallet_admin_business_open));
                }else{
                    binding.tvKycWallet.setText(getResources().getString(R.string.wallet_admin_locked));
                    binding.tvKycVault.setText(getResources().getString(R.string.wallet_admin_locked));
                }

                if(kycUserView.getDigitalNation().equals("UNLOCKED")){
                    binding.tvDigitalWallet.setText(getResources().getString(R.string.wallet_admin_open));
                    binding.tvDigitalVault.setText(getResources().getString(R.string.wallet_admin_business_open));
                }else{
                    binding.tvDigitalWallet.setText(getResources().getString(R.string.wallet_admin_locked));
                    binding.tvDigitalVault.setText(getResources().getString(R.string.wallet_admin_locked));
                }

                if(kycUserView.getDob().equals("UNLOCKED")){
                    binding.tvDobWallet.setText(getResources().getString(R.string.wallet_admin_open));
                    binding.tvDobVault.setText(getResources().getString(R.string.wallet_admin_business_open));
                }else{
                    binding.tvDobWallet.setText(getResources().getString(R.string.wallet_admin_locked));
                    binding.tvDobVault.setText(getResources().getString(R.string.wallet_admin_locked));
                }

                if(kycUserView.getMicro().equals("UNLOCKED")){
                    binding.tvMicroWallet.setText(getResources().getString(R.string.wallet_admin_open));
                    binding.tvMicroVault.setText(getResources().getString(R.string.wallet_admin_business_open));
                }else{
                    binding.tvMicroWallet.setText(getResources().getString(R.string.wallet_admin_locked));
                    binding.tvMicroVault.setText(getResources().getString(R.string.wallet_admin_locked));
                }

                if(kycUserView.getAge().equals("UNLOCKED")){
                    binding.tvAgeWallet.setText(getResources().getString(R.string.wallet_admin_open));
                    binding.tvAgeVault.setText(getResources().getString(R.string.wallet_admin_business_open));
                }else{
                    binding.tvAgeWallet.setText(getResources().getString(R.string.wallet_admin_locked));
                    binding.tvAgeVault.setText(getResources().getString(R.string.wallet_admin_locked));
                }

                if(kycUserView.getUsValidation().equals("UNLOCKED")){
                    binding.tvUsWallet.setText(getResources().getString(R.string.wallet_admin_open));
                    binding.textView60.setText(getResources().getString(R.string.wallet_admin_business_open));
                }else{
                    binding.tvUsWallet.setText(getResources().getString(R.string.wallet_admin_locked));
                    binding.textView60.setText(getResources().getString(R.string.wallet_admin_locked));
                }



            }
        });
    }

    private void getPassRecoveryStatus() {
        onboardRepository.passwordRecoveryStatus(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(Boolean status, Throwable error) {
                if (error != null) {
                    Toast.makeText(WalletAdminActivity.this, Debug.getFailureMsg(WalletAdminActivity.this, error), Toast.LENGTH_LONG).show();
                    if (error instanceof RemoteException && ((RemoteException) error).getResponseCode() == 201) {
                        Session.setWalletPassphrase(null);
                    }
                    return;
                }

                if(status){
                    binding.tvPassWallet.setText(getResources().getString(R.string.wallet_admin_open));
                    binding.tvPassVault.setText(getResources().getString(R.string.wallet_admin_business_open));
                }else{
                    binding.tvPassWallet.setText(getResources().getString(R.string.wallet_admin_locked));
                    binding.tvPassVault.setText(getResources().getString(R.string.wallet_admin_locked));
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        /* TODO: temporarily disabled for QA
        getWalletBalance();
         */
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
//        startActivity(new Intent(this, WalletHistoryActivity.class));
        startActivity(new Intent(this, WalletHistoryActivity.class).putExtra("balance", getIntent().getStringExtra("balance")).putExtra("screen", getIntent().getBooleanExtra("screen", false)));

    }
}
