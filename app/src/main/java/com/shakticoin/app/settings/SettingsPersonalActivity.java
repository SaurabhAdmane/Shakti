package com.shakticoin.app.settings;

import android.app.Activity;
import android.os.Bundle;
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
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.databinding.ActivityPersonalInfoBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.widget.DrawerActivity;

import java.util.Map;

public class SettingsPersonalActivity extends DrawerActivity {
    private ActivityPersonalInfoBinding binding;
    private SettingsPersonalViewModel viewModel;
    private UserRepository userRepo;
    private KYCRepository kycRepository = new KYCRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_info);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(SettingsPersonalViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.settings_personal_title), true);

        userRepo = new UserRepository();

        binding.progressBar.setVisibility(View.VISIBLE);
        Activity self = this;
        kycRepository.getUserDetails(new OnCompleteListener<Map<String, Object>>() {
            @Override
            public void onComplete(Map<String, Object> values, Throwable error) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(self));
                    } else {
                        Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                viewModel.firstName.setValue((String) values.get("firstName"));
                viewModel.middleName.setValue((String) values.get("middleName"));
                viewModel.lastName.setValue((String) values.get("lastName"));
            }
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }
}
