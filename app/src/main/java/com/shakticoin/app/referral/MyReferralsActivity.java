package com.shakticoin.app.referral;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.bounty.BountyReferralData;
import com.shakticoin.app.api.bounty.BountyRepository;
import com.shakticoin.app.api.bounty.PromotionalCodeResponseViewModel;
import com.shakticoin.app.databinding.ActivityMyReferralsBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.PreferenceHelper;
import com.shakticoin.app.widget.DrawerActivity;
import com.shakticoin.app.widget.MessageBox;

@SuppressLint("Registered")
public class MyReferralsActivity extends DrawerActivity {
    private ActivityMyReferralsBinding binding;
    private BountyRepository bountyRepository;
    private MyReferralsViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_referrals);
        binding.setLifecycleOwner(this);
        viewModel = new ViewModelProvider(this).get(MyReferralsViewModel.class);

        onInitView(binding.getRoot(), getString(R.string.my_refs_title));

        bountyRepository = new BountyRepository();
        bountyRepository.setLifecycleOwner(this);

        SharedPreferences prefs = getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
        boolean showInfo = prefs.getBoolean(PreferenceHelper.PREF_KEY_SHOW_UNLOCK_BOUNTY_INFO, true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment,
                        showInfo ? new MyReferralsLockedFragment() : new MyReferralsSummaryFragment())
                .commit();

        final Activity activity = this;
        bountyRepository.getBounties(new OnCompleteListener<BountyReferralData>() {

            @Override
            public void onComplete(BountyReferralData value, Throwable error) {
                if (error != null) {
                    new MessageBox(Debug.getFailureMsg(activity, error)).show(getSupportFragmentManager(), null);
                    return;
                }

                viewModel.getBountyId().setValue(value.getId());
                viewModel.getRemainingMonths().setValue(value.getRemainingMonths());
                viewModel.getSxeBalance().setValue(value.getLockedGenesisBounty());
            }
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 3;
    }

    public void onHowToEarn(View v) {
        DialogHowToBonus.getInstance().show(getSupportFragmentManager(), DialogHowToBonus.TAG);
    }

    public void onAddReferral(View v) {
        Intent intent = new Intent(this, AddReferralActivity.class);
        startActivity(intent);
    }

    public void onSeeRates(View v) {
        Intent intent = new Intent(this, EffortRatesActivity.class);
        startActivity(intent);
    }

    public void onGetPromoCode(View v) {
        String media = (String) v.getTag();
        if (media == null) return;

        final Activity activity = this;

        String bountyId = viewModel.getBountyId().getValue();
        if (bountyId == null) return;

        bountyRepository.getPromoCode(bountyId, media, new OnCompleteListener<PromotionalCodeResponseViewModel>() {
            @Override
            public void onComplete(PromotionalCodeResponseViewModel value, Throwable error) {
                if (error != null) {
                    new MessageBox(Debug.getFailureMsg(activity, error)).show(getSupportFragmentManager(), null);
                    return;
                }

                if ("OTHER".equals(media)) {
                    String siteUrl = value.getPromotionalcodeurl();
                    FirebaseDynamicLinks.getInstance().createDynamicLink()
                            .setLink(Uri.parse(siteUrl))
                            .setDomainUriPrefix("https://shakticoin.page.link/")
                            .setAndroidParameters(
                                    new DynamicLink.AndroidParameters.Builder()
                                            .setMinimumVersion(34)
                                            .build()
                            )
                            .buildShortDynamicLink()
                            .addOnSuccessListener(activity, shortDynamicLink -> {
                                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_TEXT, shortDynamicLink.getShortLink().toString());
                                shareIntent.setType("text/plain");
                                startActivity(Intent.createChooser(shareIntent, null));
                            })
                            .addOnFailureListener(Debug::logException);
                } else {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    if (clipboardManager != null) {
                        clipboardManager.setPrimaryClip(ClipData.newRawUri(getString(R.string.share_uri_label), Uri.parse(value.getPromotionalcodeurl())));
                        new MessageBox(getString(R.string.share_uri_ready_msg)).show(getSupportFragmentManager(), null);
                    }
                }
            }
        });
    }

    public void onStartReferring(View v) {
        // save flag that allow to not dispay this fragment next time
        SharedPreferences prefs = getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(PreferenceHelper.PREF_KEY_SHOW_UNLOCK_BOUNTY_INFO, false).apply();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new MyReferralsSummaryFragment())
                .addToBackStack(MyReferralsSummaryFragment.class.getSimpleName())
                .commit();
    }
}
