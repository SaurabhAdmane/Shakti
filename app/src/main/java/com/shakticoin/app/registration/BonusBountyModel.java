package com.shakticoin.app.registration;

import androidx.annotation.NonNull;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BonusBountyModel extends ViewModel {
    public enum BountyBonusEnum {BB1, BB2, BB3, BB4, BB5}

    public ObservableBoolean bountyBonus1 = new ObservableBoolean(true);
    public ObservableBoolean bountyBonus2 = new ObservableBoolean(false);
    public ObservableBoolean bountyBonus3 = new ObservableBoolean(false);
    public ObservableBoolean bountyBonus4 = new ObservableBoolean(false);
    public ObservableBoolean bountyBonus5 = new ObservableBoolean(false);

    private List<BountyBonus> bountyList = new ArrayList<>(5);
    public MutableLiveData<BountyBonus> selectedBonus = new MutableLiveData<>();

    public BonusBountyModel() {
        // set up collection on bounty bonuses
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    bountyList.add(new BountyBonus(new BigDecimal(5555.00),
                            new BigDecimal(1111.0), 6, BountyBonusEnum.BB1));
                    break;
                case 1:
                    bountyList.add(new BountyBonus(new BigDecimal(5040.00),
                            new BigDecimal(1008.0), 12, BountyBonusEnum.BB2));
                    break;
                case 2:
                    bountyList.add(new BountyBonus(new BigDecimal(4440.00),
                            new BigDecimal(888.0), 18, BountyBonusEnum.BB3));
                    break;
                case 3:
                    bountyList.add(new BountyBonus(new BigDecimal(3930.00),
                            new BigDecimal(786.0), 6, BountyBonusEnum.BB4));
                    break;
                case 4:
                    bountyList.add(new BountyBonus(new BigDecimal(3515.00),
                            new BigDecimal(703.0), 6, BountyBonusEnum.BB5));
                    break;
            }
        }

        selectedBonus.setValue(findBountyBonusByTag(BountyBonusEnum.BB1));
        bountyBonus1.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (bountyBonus2.get()) bountyBonus2.set(false);
                    if (bountyBonus3.get()) bountyBonus3.set(false);
                    if (bountyBonus4.get()) bountyBonus4.set(false);
                    if (bountyBonus5.get()) bountyBonus5.set(false);
                    selectedBonus.setValue(findBountyBonusByTag(BountyBonusEnum.BB1));
                }
            }
        });
        bountyBonus2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (bountyBonus1.get()) bountyBonus1.set(false);
                    if (bountyBonus3.get()) bountyBonus3.set(false);
                    if (bountyBonus4.get()) bountyBonus4.set(false);
                    if (bountyBonus5.get()) bountyBonus5.set(false);
                    selectedBonus.setValue(findBountyBonusByTag(BountyBonusEnum.BB2));
                }
            }
        });
        bountyBonus3.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (bountyBonus1.get()) bountyBonus1.set(false);
                    if (bountyBonus2.get()) bountyBonus2.set(false);
                    if (bountyBonus4.get()) bountyBonus4.set(false);
                    if (bountyBonus5.get()) bountyBonus5.set(false);
                    selectedBonus.setValue(findBountyBonusByTag(BountyBonusEnum.BB3));
                }
            }
        });
        bountyBonus4.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (bountyBonus1.get()) bountyBonus1.set(false);
                    if (bountyBonus2.get()) bountyBonus2.set(false);
                    if (bountyBonus3.get()) bountyBonus3.set(false);
                    if (bountyBonus5.get()) bountyBonus5.set(false);
                    selectedBonus.setValue(findBountyBonusByTag(BountyBonusEnum.BB4));
                }
            }
        });
        bountyBonus5.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = ((ObservableBoolean) sender).get();
                if (value) {
                    if (bountyBonus1.get()) bountyBonus1.set(false);
                    if (bountyBonus2.get()) bountyBonus2.set(false);
                    if (bountyBonus3.get()) bountyBonus3.set(false);
                    if (bountyBonus4.get()) bountyBonus4.set(false);
                    selectedBonus.setValue(findBountyBonusByTag(BountyBonusEnum.BB5));
                }
            }
        });
    }

    /**
     * Conveniece method to find particualr BountyBonus object in the collection.
     */
    private BountyBonus findBountyBonusByTag(BountyBonusEnum tag) {
        for (BountyBonus bonus : bountyList) {
            if (bonus.getTag().equals(tag)) return bonus;
        }
        return null;
    }

    public class BountyBonus {
        private BigDecimal priceUSD;
        private BigDecimal priceSXE;
        private Calendar unlockDate;
        private BountyBonusEnum tag;

        BountyBonus(@NonNull BigDecimal priceUSD, @NonNull BigDecimal priceSXE,
                    @NonNull Integer unlockPeriod, @NonNull BountyBonusEnum tag) {
            this.tag = tag;
            this.priceUSD = priceUSD;
            this.priceSXE = priceSXE;
            //TODO: assigned fixed dates for demo purpose, we need some other solution
//            this.unlockDate = Calendar.getInstance();
//            unlockDate.add(Calendar.MONTH, unlockPeriod);
            unlockDate = Calendar.getInstance();
            unlockDate.set(Calendar.HOUR_OF_DAY, 0);
            unlockDate.set(Calendar.MINUTE, 0);
            switch (tag) {
                case BB1:
                    unlockDate.set(Calendar.DAY_OF_MONTH, 13);
                    unlockDate.set(Calendar.MONTH, 11);
                    unlockDate.set(Calendar.YEAR, 2018);
                    break;
                case BB2:
                    unlockDate.set(Calendar.DAY_OF_MONTH, 23);
                    unlockDate.set(Calendar.MONTH, 11);
                    unlockDate.set(Calendar.YEAR, 2018);
                    break;
                case BB3:
                    unlockDate.set(Calendar.DAY_OF_MONTH, 14);
                    unlockDate.set(Calendar.MONTH, 0);
                    unlockDate.set(Calendar.YEAR, 2019);
                    break;
                case BB4:
                    unlockDate.set(Calendar.DAY_OF_MONTH, 29);
                    unlockDate.set(Calendar.MONTH, 0);
                    unlockDate.set(Calendar.YEAR, 2019);
                    break;
                case BB5:
                    unlockDate.set(Calendar.DAY_OF_MONTH, 10);
                    unlockDate.set(Calendar.MONTH, 1);
                    unlockDate.set(Calendar.YEAR, 2019);
                    break;
            }
            // End of demo dates
        }

        BountyBonusEnum getTag() {
            return tag;
        }

        public String formatUSD() {
            NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);
            String formattedPrice = fmt.format(priceUSD.doubleValue());
            return formattedPrice.substring(0, formattedPrice.indexOf("."));
        }

        public String formatSXE() {
            return String.format(Locale.US, "%1$.2f SXE", priceSXE);
        }

        public String formatDate() {
            String ending = "th";
            int lastDigit = unlockDate.get(Calendar.DAY_OF_MONTH) % 10;
            switch (lastDigit) {
                case 1:
                    ending = "st";
                    break;
                case 2:
                    ending = "nd";
                    break;
                case 3:
                    ending = "rd";
                    break;
            }
            return String.format(Locale.US, "%1$tb %1td%2$s", unlockDate, ending);
        }

        public String formatYear() {
            return Integer.toString(unlockDate.get(Calendar.YEAR));
        }

        public String formatPromoText(String fmt) {
            return String.format(Locale.US, fmt, priceUSD);
        }
    }
}
