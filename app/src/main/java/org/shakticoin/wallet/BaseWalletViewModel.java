package org.shakticoin.wallet;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BaseWalletViewModel extends ViewModel {

    /** List of unread notification messages */
    MutableLiveData<List<BaseWalletViewModel.Notification>> notifications = new MutableLiveData<>();

    public BaseWalletViewModel() {
        /*
         * TODO: Create a list of notifications for testing purpose. Replace with real stuff later.
         */
        List<BaseWalletViewModel.Notification> testList = new ArrayList<>();
        BaseWalletViewModel.Notification notification = new BaseWalletViewModel.Notification();
        notification.setTitle("Welcome to your Wallet.");
        notification.setMessage("Complete your KYC verification to fully unlock it and get ready for your Bonus Bounty!");
        notification.setDate(new Date(2019, 1, 6));
        testList.add(notification);
        notification = new BaseWalletViewModel.Notification();
        notification.setTitle("Congratulations!");
        notification.setMessage("You've choosen the 1008.00 SXE Bonus Bounty. Refer friends to unlock it sooner!");
        notification.setDate(new Date(2019, 1, 7));
        testList.add(notification);
        notification = new BaseWalletViewModel.Notification();
        notification.setTitle("A friend has singed up.");
        notification.setMessage("Alan Reynolds has registered you as a referral upon singing up.");
        notification.setDate(new Date(2019, 1, 12));
        testList.add(notification);
        Collections.sort(testList, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        notifications.setValue(testList);
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
