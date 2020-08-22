package com.shakticoin.app.settings;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.kyc.AddressModel;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.kyc.KycUserView;
import com.shakticoin.app.databinding.ActivityPersonalInfoBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.widget.DrawerActivity;

public class SettingsPersonalActivity extends DrawerActivity {
    private ActivityPersonalInfoBinding binding;
    private SettingsPersonalViewModel viewModel;
    private KYCRepository kycRepository = new KYCRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_info);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(SettingsPersonalViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.settings_personal_title), true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Activity activity = this;
        binding.progressBar.setVisibility(View.VISIBLE);
        kycRepository.getUserDetails(new OnCompleteListener<KycUserView>() {
            @Override
            public void onComplete(KycUserView userinfo, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(activity));
                    } else {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                viewModel.firstName.setValue(userinfo.getFirstName());
                viewModel.middleName.setValue(userinfo.getMiddleName());
                viewModel.lastName.setValue(userinfo.getLastName());
                viewModel.birthDate.setValue(userinfo.getDob());
                String emailAddress = userinfo.getPrimaryEmail();
                if (TextUtils.isEmpty(emailAddress)) emailAddress = userinfo.getSecondaryEmail();
                viewModel.emailAddress.setValue(emailAddress);
                String phoneNumber = userinfo.getPrimaryMobile();
                if (TextUtils.isEmpty(phoneNumber)) phoneNumber = userinfo.getSecondaryMobile();
                viewModel.phoneNumber.setValue(phoneNumber);
                viewModel.occupation.setValue(userinfo.getOccupation());
                viewModel.education.setValue(userinfo.getEducation1());

                AddressModel address = userinfo.getAddress();
                if (address != null) {
                    viewModel.address1.setValue(address.getAddress1());
                    viewModel.address2.setValue(address.getAddress2());
                    viewModel.city.setValue(address.getCity());
                    viewModel.stateProvince.setValue(address.getStateProvinceCode());
                    viewModel.country.setValue(address.getCountry());
                    viewModel.postalCode.setValue(address.getZipCode());
                }

            }
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }
}
