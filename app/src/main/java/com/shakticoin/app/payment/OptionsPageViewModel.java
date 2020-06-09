package com.shakticoin.app.payment;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.license.LicenseType;

import java.util.ArrayList;

public class OptionsPageViewModel extends ViewModel {
    public enum Period {WEEKLY, MONTHLY, ANNUAL}

    public ArrayList<LicenseType> licenseTypes;

    public MutableLiveData<Period> selectedPeriod = new MutableLiveData<>();

    public ObservableBoolean onWeekly = new ObservableBoolean();
    public ObservableBoolean onMonthly = new ObservableBoolean();
    public ObservableBoolean onAnnual = new ObservableBoolean();

    public ObservableBoolean enabledWeekly = new ObservableBoolean(true);
    public ObservableBoolean enabledMonthly = new ObservableBoolean(true);
    public ObservableBoolean enabledAnnual = new ObservableBoolean(true);

    public OptionsPageViewModel() {
        onWeekly.set(false);
        onWeekly.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onMonthly.get()) onMonthly.set(false);
                    if (onAnnual.get()) onAnnual.set(false);
                    selectedPeriod.setValue(Period.WEEKLY);
                }
            }
        });
        onMonthly.set(false);
        onMonthly.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onWeekly.get()) onWeekly.set(false);
                    if (onAnnual.get()) onAnnual.set(false);
                    selectedPeriod.setValue(Period.MONTHLY);
                }
            }
        });
        onAnnual.set(true);
        selectedPeriod.setValue(Period.ANNUAL);
        onAnnual.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onWeekly.get()) onWeekly.set(false);
                    if (onMonthly.get()) onMonthly.set(false);
                    selectedPeriod.setValue(Period.ANNUAL);
                }
            }
        });
    }
}
