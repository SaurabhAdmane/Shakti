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
import com.shakticoin.app.api.user.User;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.registration.SignInActivity;
import com.shakticoin.app.registration.SignUpActivity;
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

                            Session.setAccessToken(value.getAccess());
                            UserRepository userRepo = new UserRepository();
                            userRepo.getUserInfo(new OnCompleteListener<User>() {
                                @Override
                                public void onComplete(User value, Throwable error) {
                                    if (error != null) {
                                        if (error instanceof UnauthorizedException) {
                                            startActivity(Session.unauthorizedIntent(self));
                                        } else {
                                            Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
                                            Debug.logException(error);
                                        }
                                        return;
                                    }

                                    // go to the wallet
                                    startActivity(new Intent(self, WalletActivity.class));
                                }
                            });
                        }
                    });
//                    UserRepository userRepository = new UserRepository();
//                    userRepository.getUserInfo(new OnCompleteListener<UserResponse>() {
//                        @Override
//                        public void onComplete(UserResponse value, Throwable error) {
//                            if (error != null) {
//                                Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_SHORT).show();
//                                Debug.logException(error);
//                                startActivity(new Intent(self, SignInActivity.class));
//                                return;
//                            }
//
//                            if (value != null) {
//                                Session.setUser(value.getUser());
//
//                                int registrationStatus = value.getRegistration_status();
//                                if (registrationStatus < 3/*not verified*/) {
//                                    // we cannot continue before email is confirmed and
//                                    // just display an information in a popup window
//                                    DialogConfirmEmail dialog = DialogConfirmEmail.getInstance(true);
//                                    dialog.show(getSupportFragmentManager(), DialogConfirmEmail.class.getSimpleName());
//                                    // TODO: when we should close the Splash screen?
//
//                                } else if (registrationStatus == 3) {
//                                    // add referral code if exists and pay the enter fee
//                                    startActivity(new Intent(self, ReferralActivity.class));
//
//                                } else {
//                                    // go to the wallet
//                                    startActivity(new Intent(self, WalletActivity.class));
//
//                                }
//                            }
//                        self.finish();
//                        }
//                    });

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
