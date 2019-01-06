package org.shakticoin.registration;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityNewBountyBinding;

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
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onViewTerms1(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onViewTerms2(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }
}
