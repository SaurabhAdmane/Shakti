package com.shakticoin.app.wallet;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.util.FormatUtil;

import java.math.BigDecimal;
import java.util.Locale;

public class WalletModel extends ViewModel {
    public ObservableField<String> fullName = new ObservableField<>();
    public ObservableField<String> referralCode = new ObservableField<>();
    public ObservableField<String> referralLink = new ObservableField<>();

    public ObservableField<BigDecimal> balance = new ObservableField<>();
    public ObservableField<BigDecimal> bonusBalance = new ObservableField<>(BigDecimal.ZERO);


    public WalletModel() {
//        progressBarTrigger.set(true);
        UserRepository userRepository = new UserRepository();
//        userRepository.getUserInfo(new OnCompleteListener<UserResponse>() {
//            @Override
//            public void onComplete(UserResponse value, Throwable error) {
//                progressBarTrigger.set(false);
//                if (error == null && value != null) {
//                    // build full name
//                    User user = value.getUser();
//                    if (user != null) {
//                        StringBuilder sb = new StringBuilder();
//                        if (user.getFirst_name() != null) {
//                            sb.append(user.getFirst_name());
//                            if (user.getLast_name() != null) {
//                                sb.append(" ");
//                            }
//                        }
//                        if (user.getLast_name() != null) sb.append(user.getLast_name());
//                        fullName.set(sb.toString());
//                    }
//                    // update referral info
//                    referralCode.set(value.getReferral_code());
//                    String code = value.getReferral_code();
//                    if (code != null) {
//                        referralLink.set(BaseUrl.get() + "en/registration/?referral_code=" + code);
//                    } else {
//                        referralLink.set(null);
//                    }
//
//                }
//            }
//        });
    }

    public String getFormattedBalance(BigDecimal amount) {
        if (amount == null) return ShaktiApplication.getContext().getString(R.string.wallet_unknown_balance);
        return FormatUtil.formatCoinAmount(amount);
    }

    public String getFormattedBonusBalance() {
        return String.format(Locale.getDefault(), "SXE %1$.2f", bonusBalance.get());
    }

}
