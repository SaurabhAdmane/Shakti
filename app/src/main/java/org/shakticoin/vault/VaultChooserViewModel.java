package org.shakticoin.vault;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class VaultChooserViewModel extends ViewModel {
    public enum Options {MERCHANT, POWER_MINER, FEDUCIA, EXPRESS, DEVDOT};

    public ObservableBoolean stateOption1 = new ObservableBoolean(true);
    public ObservableBoolean stateOption2 = new ObservableBoolean(false);
    public ObservableBoolean stateOption3 = new ObservableBoolean(false);
    public ObservableBoolean stateOption4 = new ObservableBoolean(false);
    public ObservableBoolean stateOption5 = new ObservableBoolean(false);

    public ObservableField<Options> selectedVaultType = new ObservableField<>(Options.MERCHANT);

    public VaultChooserViewModel() {
        stateOption1.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = stateOption1.get();
                if (value) {
                    if (stateOption2.get()) stateOption2.set(false);
                    if (stateOption3.get()) stateOption3.set(false);
                    if (stateOption4.get()) stateOption4.set(false);
                    if (stateOption5.get()) stateOption5.set(false);
                    selectedVaultType.set(Options.MERCHANT);
                }
            }
        });
        stateOption2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = stateOption2.get();
                if (value) {
                    if (stateOption1.get()) stateOption1.set(false);
                    if (stateOption3.get()) stateOption3.set(false);
                    if (stateOption4.get()) stateOption4.set(false);
                    if (stateOption5.get()) stateOption5.set(false);
                    selectedVaultType.set(Options.POWER_MINER);
                }
            }
        });
        stateOption3.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = stateOption3.get();
                if (value) {
                    if (stateOption2.get()) stateOption2.set(false);
                    if (stateOption1.get()) stateOption1.set(false);
                    if (stateOption4.get()) stateOption4.set(false);
                    if (stateOption5.get()) stateOption5.set(false);
                    selectedVaultType.set(Options.FEDUCIA);
                }
            }
        });
        stateOption4.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = stateOption4.get();
                if (value) {
                    if (stateOption2.get()) stateOption2.set(false);
                    if (stateOption3.get()) stateOption3.set(false);
                    if (stateOption1.get()) stateOption1.set(false);
                    if (stateOption5.get()) stateOption5.set(false);
                    selectedVaultType.set(Options.EXPRESS);
                }
            }
        });
        stateOption5.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = stateOption5.get();
                if (value) {
                    if (stateOption2.get()) stateOption2.set(false);
                    if (stateOption3.get()) stateOption3.set(false);
                    if (stateOption4.get()) stateOption4.set(false);
                    if (stateOption1.get()) stateOption1.set(false);
                    selectedVaultType.set(Options.DEVDOT);
                }
            }
        });
    }
}
