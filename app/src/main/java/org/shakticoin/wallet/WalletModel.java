package org.shakticoin.wallet;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import org.shakticoin.api.BaseUrl;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.miner.MinerDataResponse;
import org.shakticoin.api.miner.MinerRepository;
import org.shakticoin.api.miner.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WalletModel extends ViewModel {
    public ObservableBoolean progressBarTrigger = new ObservableBoolean(false);
    public ObservableField<String> fullName = new ObservableField<>();
    public ObservableField<String> referralCode = new ObservableField<>();
    public ObservableField<String> referralLink = new ObservableField<>();

    public ObservableField<BigDecimal> balance = new ObservableField<>(BigDecimal.ZERO);
    public ObservableField<BigDecimal> bonusBalance = new ObservableField<>(BigDecimal.ZERO);

    /** List of unread notification messages */
    MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();

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

        /*
         * TODO: Create a list of notifications for testing purpose. Replace with real stuff later.
         */
        List<Notification> testList = new ArrayList<>();
        Notification notification = new Notification();
        notification.setTitle("Welcome to your Wallet.");
        notification.setMessage("Complete your KYC verification to fully unlock it and get ready for your Bonus Bounty!");
        notification.setDate(new Date(2019, 1, 6));
        testList.add(notification);
        notification = new Notification();
        notification.setTitle("Congratulations!");
        notification.setMessage("You've choosen the 1008.00 SXE Bonus Bounty. Refer friends to unlock it sooner!");
        notification.setDate(new Date(2019, 1, 7));
        testList.add(notification);
        notification = new Notification();
        notification.setTitle("A friend has singed up.");
        notification.setMessage("Alan Reynolds has registered you as a referral upon singing up.");
        notification.setDate(new Date(2019, 1, 12));
        testList.add(notification);
        Collections.sort(testList, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        notifications.setValue(testList);
    }

    public String getFormattedBalance() {
        return String.format(Locale.getDefault(), "SXE %1$.2f", balance.get());
    }
    public String getFormattedBonusBalance() {
        return String.format(Locale.getDefault(), "SXE %1$.2f", bonusBalance.get());
    }

    /**
     * Class that holds a notification message and use as a model for list of notifications
     * in pull down list.
     */
    public class Notification {
        private String title;
        private String message;
        private Date date;
        private boolean read = false;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public boolean isRead() {
            return read;
        }

        public void setRead(boolean read) {
            this.read = read;
        }
    }
}
