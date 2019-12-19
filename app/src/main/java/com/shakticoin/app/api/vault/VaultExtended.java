package com.shakticoin.app.api.vault;

import java.util.List;

public class VaultExtended extends Vault {
    private List<VaultFeature> features;
    private Bonus bonus;

    public List<VaultFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<VaultFeature> features) {
        this.features = features;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }
}
