package org.shakticoin.registration;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;

public class BonusBountyModel extends ViewModel {
    public enum BonusBounty {BB1, BB2, BB3, BB4, BB5}

    public ObservableBoolean bountyBonus1 = new ObservableBoolean();
    public ObservableBoolean bountyBonus2 = new ObservableBoolean();
    public ObservableBoolean bountyBonus3 = new ObservableBoolean();
    public ObservableBoolean bountyBonus4 = new ObservableBoolean();
    public ObservableBoolean bountyBonus5 = new ObservableBoolean();

    MutableLiveData<BonusBountyModel.BonusBounty> selected = new MutableLiveData<>();

    public BonusBountyModel() {
        bountyBonus1.set(true);
        selected.setValue(BonusBounty.BB1);
        bountyBonus1.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                bountyBonus1.set(true);
                if (bountyBonus2.get()) bountyBonus2.set(false);
                if (bountyBonus3.get()) bountyBonus3.set(false);
                if (bountyBonus4.get()) bountyBonus4.set(false);
                if (bountyBonus5.get()) bountyBonus5.set(false);
                selected.setValue(BonusBounty.BB1);
            }
        });
        bountyBonus2.set(false);
        bountyBonus2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                bountyBonus2.set(true);
                if (bountyBonus1.get()) bountyBonus1.set(false);
                if (bountyBonus3.get()) bountyBonus3.set(false);
                if (bountyBonus4.get()) bountyBonus4.set(false);
                if (bountyBonus5.get()) bountyBonus5.set(false);
                selected.setValue(BonusBounty.BB2);
            }
        });
        bountyBonus3.set(false);
        bountyBonus3.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                bountyBonus3.set(true);
                if (bountyBonus2.get()) bountyBonus2.set(false);
                if (bountyBonus1.get()) bountyBonus1.set(false);
                if (bountyBonus4.get()) bountyBonus4.set(false);
                if (bountyBonus5.get()) bountyBonus5.set(false);
                selected.setValue(BonusBounty.BB3);
            }
        });
        bountyBonus4.set(false);
        bountyBonus4.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                bountyBonus4.set(true);
                if (bountyBonus2.get()) bountyBonus2.set(false);
                if (bountyBonus3.get()) bountyBonus3.set(false);
                if (bountyBonus1.get()) bountyBonus1.set(false);
                if (bountyBonus5.get()) bountyBonus5.set(false);
                selected.setValue(BonusBounty.BB4);
            }
        });
        bountyBonus5.set(false);
        bountyBonus5.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                bountyBonus5.set(true);
                if (bountyBonus2.get()) bountyBonus2.set(false);
                if (bountyBonus3.get()) bountyBonus3.set(false);
                if (bountyBonus4.get()) bountyBonus4.set(false);
                if (bountyBonus1.get()) bountyBonus1.set(false);
                selected.setValue(BonusBounty.BB5);
            }
        });
    }
}
