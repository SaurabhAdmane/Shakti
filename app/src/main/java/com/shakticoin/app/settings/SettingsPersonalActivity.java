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
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.databinding.ActivityPersonalInfoBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.widget.DrawerActivity;

import java.util.Map;

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
        kycRepository.getUserDetails(new OnCompleteListener<Map<String, Object>>() {
            @Override
            public void onComplete(Map<String, Object> values, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(activity));
                    } else {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                viewModel.firstName.setValue((String) values.get("firstName"));
                viewModel.middleName.setValue((String) values.get("middleName"));
                viewModel.lastName.setValue((String) values.get("lastName"));
                viewModel.birthDate.setValue((String) values.get("dob"));
                String emailAddress = (String) values.get("primaryEmail");
                if (TextUtils.isEmpty(emailAddress)) emailAddress = (String) values.get("secondaryEmail");
                viewModel.emailAddress.setValue(emailAddress);
                String phoneNumber = (String) values.get("primaryMobile");
                if (TextUtils.isEmpty(phoneNumber)) phoneNumber = (String) values.get("secondaryMobile");
                viewModel.phoneNumber.setValue(phoneNumber);
                viewModel.occupation.setValue((String) values.get("occupation"));
                viewModel.education.setValue((String) values.get("education1"));

                Map<String, String> address = CommonUtil.checkMap(values.get("address"));
                if (address != null && address.size() > 0) {
                    viewModel.address1.setValue(address.get("address1"));
                    viewModel.address2.setValue(address.get("address2"));
                    viewModel.city.setValue(address.get("city"));
                    viewModel.stateProvince.setValue(address.get("stateProvinceCode"));
                    viewModel.country.setValue(address.get("country"));
                    viewModel.postalCode.setValue(address.get("zipCode"));
                }

            }
        }, false);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }
}
