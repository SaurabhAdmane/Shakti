package com.shakticoin.app.widget;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DrawerActivityViewModel extends ViewModel {

    /** Selected button in the drawer. 0 based index. */
    MutableLiveData<Integer> selectedDrawer = new MutableLiveData<>();

    /** List of unread notification messages */
    MutableLiveData<List<Notification>> notifications = new MutableLiveData<>();

    public ObservableBoolean isMinerExpanded = new ObservableBoolean(false);

    public DrawerActivityViewModel() {
        selectedDrawer.setValue(0);
        List<DrawerActivityViewModel.Notification> testList = new ArrayList<>();
        DrawerActivityViewModel.Notification notification = new DrawerActivityViewModel.Notification();
        // TODO: no messaging api
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
