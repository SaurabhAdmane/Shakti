package org.shakticoin.registration;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;

import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.tier.Tier;
import org.shakticoin.api.tier.TierRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MiningLicenseModel extends ViewModel {
    public enum Plan {M101, T100, T200, T300, T400}

    public ObservableBoolean onM101 = new ObservableBoolean();
    public ObservableBoolean onT100 = new ObservableBoolean();
    public ObservableBoolean onT200 = new ObservableBoolean();
    public ObservableBoolean onT300 = new ObservableBoolean();
    public ObservableBoolean onT400 = new ObservableBoolean();

    public MutableLiveData<Plan> selectedPlan = new MutableLiveData<>();

    private List<Tier> tiers = new ArrayList<>();

    public MiningLicenseModel() {
        onM101.set(true);
        selectedPlan.setValue(Plan.M101);
        onM101.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (onT100.get()) onT100.set(false);
                    if (onT200.get()) onT200.set(false);
                    if (onT300.get()) onT300.set(false);
                    if (onT400.get()) onT400.set(false);
                    selectedPlan.setValue(Plan.M101);
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
                    selectedPlan.setValue(Plan.T100);
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
                    selectedPlan.setValue(Plan.T200);
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
                    selectedPlan.setValue(Plan.T300);
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
                    if (onT300.get()) onT300.set(false);
                    selectedPlan.setValue(Plan.T400);
                }
            }
        });
    }

    public void init(List<Tier> tiersList) {
        if (tiersList != null) {
            tiers = tiersList;
            disableInactivePlans();
        } else {
            // grab tiers in advance
            TierRepository repository = new TierRepository();
            repository.getTiers("AF", new OnCompleteListener<List<Tier>>() {
                @Override
                public void onComplete(List<Tier> value, Throwable error) {
                    if (error == null) {
                        tiers = new ArrayList<>(value);
                        selectedPlan.setValue(selectedPlan.getValue());
                        disableInactivePlans();
                    }
                }
            });
        }
    }

    public BigDecimal getPaymentAmount() {
        Plan plan = selectedPlan.getValue();
        if (plan != null) {
            String planName = plan.name();
            for (Tier tier : tiers) {
                if (planName.equalsIgnoreCase(tier.getName())) {
                    return BigDecimal.valueOf(tier.getPrice());
                }
            }
        }
        return null;
    }

    private void disableInactivePlans() {
        if (tiers == null) return;
        String planName = selectedPlan.getValue().name();
        for (Tier tier : tiers) {
            // TODO: we must disable buttons if corresponding tier level is not active or does not exists
        }
    }

}
