package com.shakticoin.app.settings;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.user.User;

public class SettingsViewModel extends ViewModel {

    public MutableLiveData<User> user = new MutableLiveData<>();

    /**
     * Build full name from user's data.
     */
    public String formatFullName(String first, String middle, String last) {
        StringBuilder sb = new StringBuilder("");
        User data = user.getValue();
        if (data != null) {
            if (data.getFirst_name() != null) sb.append(data.getFirst_name());
            if (data.getMiddle_name() != null) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(data.getMiddle_name());
            }
            if (data.getLast_name() != null) {
                if (sb.length() > 0) sb.append(" ");
                sb.append(data.getLast_name());
            }
        }
        return sb.toString();
    }
}
