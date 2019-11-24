package com.shakticoin.app.payment;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PaymentOptionsViewModel extends ViewModel {
    public enum Option {WEEKLY, MONTHLY, ANNUAL}
    public enum Plan {M101, T100, T200, T300, T400}

    public final int monthlySavingValue = 19;
    public final int annualSavingValue = 55;

    public ObservableBoolean onWeekly = new ObservableBoolean();
    public ObservableBoolean onMonthly = new ObservableBoolean();
    public ObservableBoolean onAnnual = new ObservableBoolean();

    public ObservableBoolean enabledWeekly = new ObservableBoolean(true);
    public ObservableBoolean enabledMonthly = new ObservableBoolean(true);
    public ObservableBoolean enabledAnnual = new ObservableBoolean(true);

    MutableLiveData<PaymentOptionsViewModel.Option> selectedOption = new MutableLiveData<>();
    public MutableLiveData<PaymentOptionsViewModel.Plan> selectedPlan = new MutableLiveData<>();

    public PaymentOptionsViewModel() {
        onWeekly.set(false);
        onWeekly.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onMonthly.get()) onMonthly.set(false);
                    if (onAnnual.get()) onAnnual.set(false);
                    selectedOption.setValue(Option.WEEKLY);
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
                    selectedOption.setValue(Option.MONTHLY);
                }
            }
        });
        onAnnual.set(true);
        selectedOption.setValue(Option.ANNUAL);
        onAnnual.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onWeekly.get()) onWeekly.set(false);
                    if (onMonthly.get()) onMonthly.set(false);
                    selectedOption.setValue(Option.ANNUAL);
                }
            }
        });
    }
}
