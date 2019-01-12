package org.shakticoin.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityProfilePersonalBinding;
import org.shakticoin.wallet.BaseWalletActivity;

public class PeronalInfoActivity extends BaseWalletActivity {
    private ActivityProfilePersonalBinding binding;
    private PersonalViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_personal);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(PersonalViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.profile_personal_title));

        binding.pageIndicator.setSelectedIndex(1);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new PersonalPage1(), PersonalPage1.TAG)
                .commit();
    }

    public void onUpdate(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onNext(View v) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new PersonalPage2(), PersonalPage2.TAG)
                .addToBackStack(null)
                .commit();
    }

    public void onCancel(View v) {
        finish();
    }
}
