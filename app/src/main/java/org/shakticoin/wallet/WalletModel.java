package org.shakticoin.wallet;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.Log;

import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.miner.MinerDataResponse;
import org.shakticoin.api.miner.MinerRepository;
import org.shakticoin.api.miner.User;

public class WalletModel extends ViewModel {
    public ObservableBoolean progressBarTrigger = new ObservableBoolean(false);
    public ObservableField<String> fullName = new ObservableField<>();
    public ObservableField<String> referralCode = new ObservableField<>();
    public ObservableField<String> referralLink = new ObservableField<>();

    public WalletModel() {
        progressBarTrigger.set(true);
        MinerRepository minerRepository = new MinerRepository();
        minerRepository.getUserInfo(new OnCompleteListener<MinerDataResponse>() {
            @Override
            public void onComplete(MinerDataResponse value, Throwable error) {
                progressBarTrigger.set(false);
                if (error == null && value != null) {
                    // build full name
                    User user = value.getUser();
                    if (user != null) {
                        StringBuilder sb = new StringBuilder();
                        if (user.getFirst_name() != null) {
                            sb.append(user.getFirst_name());
                            if (user.getLast_name() != null) {
                                sb.append(" ");
                            }
                        }
                        if (user.getLast_name() != null) sb.append(user.getLast_name());
                        fullName.set(sb.toString());
                    }
                    // update referral info
                    referralCode.set(value.getReferral_code());
                    String code = value.getReferral_code();
                    if (code != null) {
                        referralLink.set(BaseUrl.get() + "en/registration/?referral_code=" + code);
                    } else {
                        referralLink.set(null);
                    }

                }
            }
        });
    }

}
