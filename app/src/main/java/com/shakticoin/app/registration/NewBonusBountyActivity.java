package com.shakticoin.app.registration;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityNewBountyBinding;

public class NewBonusBountyActivity extends AppCompatActivity {

    private ActivityNewBountyBinding binding;
    private NewBonusBountyModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_bounty);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(NewBonusBountyModel.class);
        binding.setViewModel(viewModel);
    }

    public void onRegisterReferral(View v) {
        Intent intent = new Intent(this, ReferralActivity.class);
        startActivity(intent);
    }

    public void onViewTerms1(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onViewTerms2(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }
}
