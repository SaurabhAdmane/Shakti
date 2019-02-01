package org.shakticoin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.shakticoin.miner.BecomeMinerActivity;
import org.shakticoin.miner.UpgradeMinerActivity;
import org.shakticoin.referral.EffortRatesActivity;
import org.shakticoin.registration.SignInActivity;
import org.shakticoin.registration.SignUpActivity;
import org.shakticoin.settings.SettingsActivity;
import org.shakticoin.tour.WelcomeTourActivity;
import org.shakticoin.util.PreferenceHelper;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_splash);

//        route();
        startActivity(new Intent(this, SettingsActivity.class));
        finish();
    }

    private void route() {
        SharedPreferences prefs = getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
        boolean tourDone = prefs.getBoolean(PreferenceHelper.PREF_KEY_TOUR_DONE, false);
        if (tourDone) {
            boolean hasAccount = prefs.getBoolean(PreferenceHelper.PREF_KEY_HAS_ACCOUNT, false);
            if (hasAccount) {
                startActivity(new Intent(this, SignInActivity.class));
            } else {
                startActivity(new Intent(this, SignUpActivity.class));
            }
        } else {
            startActivity(new Intent(this, WelcomeTourActivity.class));
        }
    }
}
