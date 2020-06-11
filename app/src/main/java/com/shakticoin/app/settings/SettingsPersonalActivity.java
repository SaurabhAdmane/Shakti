package com.shakticoin.app.settings;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.databinding.ActivityPersonalInfoBinding;
import com.shakticoin.app.widget.DrawerActivity;

public class SettingsPersonalActivity extends DrawerActivity {
    private ActivityPersonalInfoBinding binding;
    private SettingsPersonalViewModel viewModel;
    private UserRepository userRepo;

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
        // FIXME: commented out because the code uses obsolete usersevice API
//        int userId = Session.getUser().getId();
//        userRepo.getUserInfo(userId, new OnCompleteListener<User>() {
//            @Override
//            public void onComplete(User value, Throwable error) {
//                binding.progressBar.setVisibility(View.INVISIBLE);
//                if (error != null) {
//                    if (error instanceof UnauthorizedException) {
//                        startActivity(Session.unauthorizedIntent(self));
//                    } else {
//                        Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
//                    }
//                    return;
//                }
//
//                viewModel.user.setValue(value);
//            }
//        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }
}
