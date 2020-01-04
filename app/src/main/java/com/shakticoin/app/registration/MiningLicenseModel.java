package com.shakticoin.app.registration;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.tier.Tier;
import com.shakticoin.app.api.vault.PackageExtended;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MiningLicenseModel extends ViewModel {
    public enum PackageType {M101, T100, T200, T300, T400}

    public List<PackageExtended> packages;

    public ObservableBoolean onM101 = new ObservableBoolean();
    public ObservableBoolean onT100 = new ObservableBoolean();
    public ObservableBoolean onT200 = new ObservableBoolean();
    public ObservableBoolean onT300 = new ObservableBoolean();
    public ObservableBoolean onT400 = new ObservableBoolean();

    public ObservableBoolean enabledM101 = new ObservableBoolean(true);
    public ObservableBoolean enabledT100 = new ObservableBoolean(true);
    public ObservableBoolean enabledT200 = new ObservableBoolean(true);
    public ObservableBoolean enabledT300 = new ObservableBoolean(true);
    public ObservableBoolean enabledT400 = new ObservableBoolean(true);

    MutableLiveData<PackageType> selectedPlan = new MutableLiveData<>();

    private List<Tier> tiers = new ArrayList<>();

    public MiningLicenseModel() {
        onM101.set(true);
        selectedPlan.setValue(PackageType.M101);
        onM101.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onT100.get()) onT100.set(false);
                    if (onT200.get()) onT200.set(false);
                    if (onT300.get()) onT300.set(false);
                    if (onT400.get()) onT400.set(false);
                    selectedPlan.setValue(PackageType.M101);
                }
            }
        });
        onT100.set(false);
        onT100.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onM101.get()) onM101.set(false);
                    if (onT200.get()) onT200.set(false);
                    if (onT300.get()) onT300.set(false);
                    if (onT400.get()) onT400.set(false);
                    selectedPlan.setValue(PackageType.T100);
                }
            }
        });
        onT200.set(false);
        onT200.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onT100.get()) onT100.set(false);
                    if (onM101.get()) onM101.set(false);
                    if (onT300.get()) onT300.set(false);
                    if (onT400.get()) onT400.set(false);
                    selectedPlan.setValue(PackageType.T200);
                }
            }
        });
        onT300.set(false);
        onT300.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onT100.get()) onT100.set(false);
                    if (onT200.get()) onT200.set(false);
                    if (onM101.get()) onM101.set(false);
                    if (onT400.get()) onT400.set(false);
                    selectedPlan.setValue(PackageType.T300);
                }
            }
        });
        onT400.set(false);
        onT400.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onT100.get()) onT100.set(false);
                    if (onT200.get()) onT200.set(false);
                    if (onM101.get()) onM101.set(false);
                    if (onT400.get()) onT300.set(false);
                    selectedPlan.setValue(PackageType.T400);
                }
            }
        });
    }

    void init(List<PackageExtended> packages) {
        this.packages = packages;
    }

    /**
     * Returns package related data that we received from the server.
     */
    public PackageExtended getSelectedPackage() {
        PackageType packageType = selectedPlan.getValue();
        if (packageType != null && packageType.ordinal() < packages.size()) {
            return packages.get(packageType.ordinal());
        }
        return null;
    }

    public BigDecimal getPaymentAmount() {
        PackageType packageType = selectedPlan.getValue();
        if (packageType != null) {
            String planName = packageType.name();
            for (Tier tier : tiers) {
                if (planName.equalsIgnoreCase(tier.getName())) {
                    return BigDecimal.valueOf(tier.getPrice());
                }
            }
        }
        return null;
    }

    Tier getSelectedPlan() {
        PackageType packageType = selectedPlan.getValue();
        if (packageType != null) {
            String planName = packageType.name();
            for (Tier tier : tiers) {
                if (planName.equalsIgnoreCase(tier.getName())) {
                    return tier;
                }
            }
        }
        return null;
    }

    private void disableInactivePlans() {
        if (tiers == null) return;
        for (Tier tier : tiers) {
            Boolean isActive = tier.getIs_active();
            String name = tier.getName();
            if (PackageType.M101.name().equals(name)) {
                enabledM101.set(isActive);
            } else if (PackageType.T100.name().equals(name)) {
                enabledT100.set(isActive);
            } else if (PackageType.T200.name().equals(name)) {
                enabledT200.set(isActive);
            } else if (PackageType.T300.name().equals(name)) {
                enabledT300.set(isActive);
            } else if (PackageType.T400.name().equals(name)) {
                enabledT400.set(isActive);
            }
        }
    }

}
