package org.shakticoin.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.shakticoin.R;
import org.shakticoin.wallet.WalletActivity;


public class ReferralConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_confirmation);
    }

    public void onGoWallet(View view) {
        Intent intent = new Intent(this, WalletActivity.class);
        startActivity(intent);
    }
}
