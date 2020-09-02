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
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.registration.RegActivity;
import com.shakticoin.app.registration.SignInActivity;
import com.shakticoin.app.tour.WelcomeTourActivity;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.PreferenceHelper;
import com.shakticoin.app.wallet.WalletActivity;


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
                String refreshToken = Session.getRefreshToken();
                if (refreshToken == null) {
                    startActivity(new Intent(this, SignInActivity.class));
                    finish();
                } else {
                    Activity self = this;
                    AuthRepository authRepo = new AuthRepository();
                    authRepo.setLifecycleOwner(this);
                    authRepo.refreshToken(refreshToken, new OnCompleteListener<TokenResponse>() {
                        @Override
                        public void onComplete(TokenResponse value, Throwable error) {
                            if (error != null) {
                                Debug.logException(error);
                                if (!(error instanceof UnauthorizedException)) {
                                    Toast.makeText(self, Debug.getFailureMsg(getApplicationContext(), error), Toast.LENGTH_LONG).show();
                                }
                                startActivity(Session.unauthorizedIntent(self));
                                finish();
                                return;
                            }

                            Session.setAccessToken(value.getAccess_token());

                            startActivity(new Intent(self, WalletActivity.class));
                        }
                    });
                }
            } else {
                startActivity(new Intent(this, RegActivity.class));
                finish();
            }
        } else {
            startActivity(new Intent(this, WelcomeTourActivity.class));
            finish();
        }
    }
}
