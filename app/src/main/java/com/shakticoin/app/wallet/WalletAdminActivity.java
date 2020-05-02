package com.shakticoin.app.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.wallet.WalletRepository;
import com.shakticoin.app.databinding.ActivityWalletAdminBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.FormatUtil;
import com.shakticoin.app.widget.DrawerActivity;

import java.math.BigDecimal;

public class WalletAdminActivity extends DrawerActivity {
    private ActivityWalletAdminBinding binding;
    private WalletRepository repository = new WalletRepository();


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

        final Activity activity = this;

        String walletBytes = repository.getExistingWallet(null);
        if (walletBytes != null) {
            repository.getBalance(new OnCompleteListener<BigDecimal>() {
                @Override
                public void onComplete(BigDecimal value, Throwable error) {
                    if (error != null) {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        return;
                    }
                    binding.balance.setText(FormatUtil.formatCoinAmount(value));
                }
            });
        }

    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 0;
    }

    public void onOpenBalanceHistory(View v) {
        startActivity(new Intent(this, WalletHistoryActivity.class));
    }
}
