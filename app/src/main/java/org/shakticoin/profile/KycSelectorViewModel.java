package org.shakticoin.profile;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

public class KycSelectorViewModel extends ViewModel {
    private enum KycOption {UTILITY, PROPERTY, GOVERMENT}

    public ObservableBoolean utilityDocState = new ObservableBoolean(true);
    public ObservableBoolean financeDocState = new ObservableBoolean(false);
    public ObservableBoolean govDocState = new ObservableBoolean(false);

    private KycOption selectedOption = KycOption.UTILITY;

    public KycSelectorViewModel() {
        utilityDocState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = utilityDocState.get();
                if (value) {
                    if (financeDocState.get()) financeDocState.set(false);
                    if (govDocState.get()) govDocState.set(false);
                    selectedOption = KycOption.UTILITY;
                }
            }
        });
        financeDocState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = financeDocState.get();
                if (value) {
                    if (utilityDocState.get()) utilityDocState.set(false);
                    if (govDocState.get()) govDocState.set(false);
                    selectedOption = KycOption.PROPERTY;
                }
            }
        });
        govDocState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = govDocState.get();
                if (value) {
                    if (utilityDocState.get()) utilityDocState.set(false);
                    if (financeDocState.get()) financeDocState.set(false);
                    selectedOption = KycOption.GOVERMENT;
                }
            }
        });
    }
}
