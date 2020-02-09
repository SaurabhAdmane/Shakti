package com.shakticoin.app.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.kyc.KycCategory;
import com.shakticoin.app.databinding.ActivityKycBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.wallet.BaseWalletActivity;

public class KycActivity extends BaseWalletActivity {
    private ActivityKycBinding binding;
    private KycCommonViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kyc);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(KycCommonViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.profile_kyc_title));

        KycCategory category = getIntent().getParcelableExtra(CommonUtil.prefixed("KYC_CATEGORY", ShaktiApplication.getContext()));
        viewModel.kycCategory.setValue(category);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new KycDoctypeFragment())
                .commit();
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    public void onOpenDocuments(View v) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new KycFilesFragment())
                .addToBackStack(KycFilesFragment.class.getSimpleName())
                .commit();
    }

    public void onAddDocument(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onSend(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onCancel(View v) {
        finish();
    }
}
