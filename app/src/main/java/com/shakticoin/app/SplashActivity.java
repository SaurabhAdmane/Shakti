package com.shakticoin.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.miner.MinerDataResponse;
import com.shakticoin.app.api.miner.MinerRepository;
import com.shakticoin.app.registration.DialogConfirmEmail;
import com.shakticoin.app.registration.ReferralActivity;
import com.shakticoin.app.registration.SignInActivity;
import com.shakticoin.app.registration.SignUpActivity;
import com.shakticoin.app.tour.WelcomeTourActivity;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.PreferenceHelper;
import com.shakticoin.app.wallet.WalletActivity;

import com.shakticoin.app.R;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_splash);

        route();
    }

    private void route() {
        SharedPreferences prefs = getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
        boolean tourDone = prefs.getBoolean(PreferenceHelper.PREF_KEY_TOUR_DONE, false);
        if (tourDone) {
            boolean hasAccount = prefs.getBoolean(PreferenceHelper.PREF_KEY_HAS_ACCOUNT, false);
            if (hasAccount) {
                if (Session.key(this) == null) {
                    startActivity(new Intent(this, SignInActivity.class));
                } else {
                    Activity self = this;
                    MinerRepository minerRepository = new MinerRepository();
                    minerRepository.getUserInfo(new OnCompleteListener<MinerDataResponse>() {
                        @Override
                        public void onComplete(MinerDataResponse value, Throwable error) {
                            if (error != null) {
                                Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_SHORT).show();
                                Debug.logException(error);
                                startActivity(new Intent(self, SignInActivity.class));
                                return;
                            }

                            if (value != null) {
                                Session.setUser(value.getUser());

                                int registrationStatus = value.getRegistration_status();
                                if (registrationStatus < 3/*not verified*/) {
                                    // we cannot continue before email is confirmed and
                                    // just display an information in a popup window
                                    DialogConfirmEmail dialog = DialogConfirmEmail.getInstance(true);
                                    dialog.show(getSupportFragmentManager(), DialogConfirmEmail.class.getSimpleName());
                                    // TODO: when we should close the Splash screen?

                                } else if (registrationStatus == 3) {
                                    // add referral code if exists and pay the enter fee
                                    startActivity(new Intent(self, ReferralActivity.class));

                                } else {
                                    // go to the wallet
                                    startActivity(new Intent(self, WalletActivity.class));

                                }
                            }
                        self.finish();
                        }
                    });

                }
            } else {
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(this, WelcomeTourActivity.class));
            finish();
        }
    }
}
