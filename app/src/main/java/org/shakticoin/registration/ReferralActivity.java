package org.shakticoin.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.shakticoin.R;


public class ReferralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);
    }

    public void OnSkipReferral(View view) {
    }

    public void onReward(View view) {
    }

    public void onReadQRCode(View view) {
    }
}
