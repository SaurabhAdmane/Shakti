package org.shakticoin.profile;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

public class KycCommonViewModel extends ViewModel {
    public ObservableBoolean progressBarTrigger = new ObservableBoolean(false);

}
