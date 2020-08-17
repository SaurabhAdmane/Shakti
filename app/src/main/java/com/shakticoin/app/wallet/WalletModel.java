package com.shakticoin.app.wallet;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.util.FormatUtil;

import java.math.BigDecimal;
import java.util.Locale;

public class WalletModel extends ViewModel {
    public ObservableField<String> fullName = new ObservableField<>();
    public ObservableField<String> referralCode = new ObservableField<>();
    public ObservableField<String> referralLink = new ObservableField<>();

    public ObservableField<BigDecimal> balance = new ObservableField<>();
    public ObservableField<BigDecimal> bonusBalance = new ObservableField<>(BigDecimal.ZERO);

    public ObservableBoolean isProgressBarActive = new ObservableBoolean(false);

    public String getFormattedBalance(BigDecimal amount) {
        if (amount == null) return ShaktiApplication.getContext().getString(R.string.wallet_unknown_balance);
        return FormatUtil.formatCoinAmount(amount);
    }

    public String getFormattedBonusBalance() {
        return String.format(Locale.getDefault(), "SXE %1$.2f", bonusBalance.get());
    }

}
