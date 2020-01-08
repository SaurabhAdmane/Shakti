package com.shakticoin.app.vault;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.api.vault.VaultExtended;

import java.util.List;

public class VaultChooserViewModel extends ViewModel {
    public enum Options {MERCHANT, POWER_MINER, FEDUCIA, EXPRESS, DEVDOT};

    private List<VaultExtended> vaults;

    public ObservableBoolean stateOption1 = new ObservableBoolean(false);
    public ObservableBoolean stateOption2 = new ObservableBoolean(false);
    public ObservableBoolean stateOption3 = new ObservableBoolean(false);
    public ObservableBoolean stateOption4 = new ObservableBoolean(false);
    public ObservableBoolean stateOption5 = new ObservableBoolean(false);

    public ObservableField<Options> selectedVaultType = new ObservableField<>(Options.MERCHANT);
    public ObservableField<VaultExtended> selectedVault = new ObservableField<>();

    public VaultChooserViewModel() {
    }

    public void init(List<VaultExtended> vaults) {
        this.vaults = vaults;
        if (vaults != null && vaults.size() > 0) {
            for (int i = 0; i < vaults.size(); i++) {
                switch (i) {
                    case 0:
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
                                    if (vaults.size() > 0) {
                                        selectedVault.set(vaults.get(0));
                                    }
                                }
                            }
                        });
                        break;
                    case 1:
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
                                    if (vaults.size() > 1) {
                                        selectedVault.set(vaults.get(1));
                                    }
                                }
                            }
                        });
                        break;
                    case 2:
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
                                    if (vaults.size() > 2) {
                                        selectedVault.set(vaults.get(2));
                                    }
                                }
                            }
                        });
                        break;
                    case 3:
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
                                    if (vaults.size() > 3) {
                                        selectedVault.set(vaults.get(3));
                                    }
                                }
                            }
                        });
                        break;
                    case 4:
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
                                    if (vaults.size() > 4) {
                                        selectedVault.set(vaults.get(4));
                                    }
                                }
                            }
                        });
                        break;
                }
            }

            stateOption1.set(true);
        }
    };

}
