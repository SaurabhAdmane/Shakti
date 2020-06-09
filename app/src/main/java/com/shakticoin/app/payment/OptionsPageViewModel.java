package com.shakticoin.app.payment;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

public class OptionsPageViewModel extends ViewModel {

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
//                    selectedOption.setValue(PaymentOptionsViewModel.Plan.WEEKLY);
//                    selectedPlan.set(weeklyPlan.get());
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
//                    selectedOption.setValue(PaymentOptionsViewModel.Plan.MONTHLY);
//                    selectedPlan.set(monthlyPlan.get());
                }
            }
        });
        onAnnual.set(true);
//        selectedOption.setValue(PaymentOptionsViewModel.Plan.ANNUAL);
        onAnnual.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onWeekly.get()) onWeekly.set(false);
                    if (onMonthly.get()) onMonthly.set(false);
//                    selectedOption.setValue(PaymentOptionsViewModel.Plan.ANNUAL);
//                    selectedPlan.set(annualPlan.get());
                }
            }
        });
    }
}
