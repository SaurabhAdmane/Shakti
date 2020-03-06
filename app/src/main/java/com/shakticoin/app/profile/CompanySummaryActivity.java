package com.shakticoin.app.profile;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityCompanySummaryBinding;
import com.shakticoin.app.widget.DrawerActivity;

public class CompanySummaryActivity extends DrawerActivity {

    private ActivityCompanySummaryBinding binding;
    private CompanySummaryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_company_summary);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(CompanySummaryViewModel.class);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        onInitView(v, getString(R.string.company_sum_title), true);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 7;
    }
}
