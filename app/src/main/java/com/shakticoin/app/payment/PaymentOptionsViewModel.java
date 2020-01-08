package com.shakticoin.app.payment;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.vault.PackageDiscount;
import com.shakticoin.app.api.vault.PackageExtended;
import com.shakticoin.app.api.vault.PackagePlanExtended;

import java.util.List;

public class PaymentOptionsViewModel extends ViewModel {
    public enum Plan {WEEKLY, MONTHLY, ANNUAL}
    public enum PackageType {M101, T100, T200, T300, T400}

    public ObservableField<PackagePlanExtended> weeklyPlan = new ObservableField<>();
    public ObservableField<PackagePlanExtended> monthlyPlan = new ObservableField<>();
    public ObservableField<PackagePlanExtended> annualPlan = new ObservableField<>();

    public final int monthlySavingValue = 19;
    public final int annualSavingValue = 55;
    public ObservableField<String> discountPromoText = new ObservableField<>();

    public ObservableBoolean onWeekly = new ObservableBoolean();
    public ObservableBoolean onMonthly = new ObservableBoolean();
    public ObservableBoolean onAnnual = new ObservableBoolean();

    public ObservableBoolean enabledWeekly = new ObservableBoolean(true);
    public ObservableBoolean enabledMonthly = new ObservableBoolean(true);
    public ObservableBoolean enabledAnnual = new ObservableBoolean(true);

    MutableLiveData<Plan> selectedOption = new MutableLiveData<>();
    public ObservableField<PackagePlanExtended> selectedPlan = new ObservableField<>();
    public MutableLiveData<PackageExtended> selectedPackage = new MutableLiveData<>();
    public MutableLiveData<PackageType> selectedPackageType = new MutableLiveData<>();

    public PaymentOptionsViewModel() {
        onWeekly.set(false);
        onWeekly.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onMonthly.get()) onMonthly.set(false);
                    if (onAnnual.get()) onAnnual.set(false);
                    selectedOption.setValue(Plan.WEEKLY);
                    selectedPlan.set(weeklyPlan.get());
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
                    selectedOption.setValue(Plan.MONTHLY);
                    selectedPlan.set(monthlyPlan.get());
                }
            }
        });
        onAnnual.set(true);
        selectedOption.setValue(Plan.ANNUAL);
        onAnnual.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onWeekly.get()) onWeekly.set(false);
                    if (onMonthly.get()) onMonthly.set(false);
                    selectedOption.setValue(Plan.ANNUAL);
                    selectedPlan.set(annualPlan.get());
                }
            }
        });
    }
}
