package org.shakticoin.profile;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityKycBinding;
import org.shakticoin.wallet.BaseWalletActivity;

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

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new KycRejectedFragment())
                .commit();
    }

    public void onOpenDocuments(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onCancel(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }
}