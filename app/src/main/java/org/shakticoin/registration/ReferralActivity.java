package org.shakticoin.registration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.api.OnCompleteListener;
import org.shakticoin.api.tier.Tier;
import org.shakticoin.api.tier.TierRepository;
import org.shakticoin.util.CommonUtil;
import org.shakticoin.widget.qr.QRScannerActivity;

import java.util.ArrayList;
import java.util.List;


public class ReferralActivity extends AppCompatActivity {
    private ArrayList<Tier> tiers;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRScannerActivity.REQUEST_QR) {
            if (resultCode == RESULT_OK) {
                String referralCodeKey = CommonUtil.prefixed(QRScannerActivity.KEY_REFERRAL_CODE, this);
                if (data != null && data.hasExtra(referralCodeKey)) {
                    String referralCode = data.getStringExtra(referralCodeKey);
                    Toast.makeText(this, referralCode, Toast.LENGTH_SHORT).show();
                    postReferralInfo();
                }
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral);

        // grab tiers in advance
        TierRepository repository = new TierRepository();
        repository.getTiers("AF", new OnCompleteListener<List<Tier>>() {
            @Override
            public void onComplete(List<Tier> value, Throwable error) {
                if (error == null) {
                    tiers = new ArrayList<>(value);
                }
            }
        });
    }

    public void OnSkipReferral(View view) {
        selectTier();
    }

    public void onReward(View view) {
        postReferralInfo();
    }

    public void onReadQRCode(View view) {
        startActivityForResult(new Intent(this, QRScannerActivity.class), QRScannerActivity.REQUEST_QR);
    }

    private void postReferralInfo() {
        selectTier();
    }

    private void selectTier() {
        Intent intent = new Intent(this, MiningLicenseActivity.class);
        if (tiers != null) {
            intent.putParcelableArrayListExtra(CommonUtil.prefixed("tiersList", this), tiers);
        }
        startActivity(intent);
    }
}
