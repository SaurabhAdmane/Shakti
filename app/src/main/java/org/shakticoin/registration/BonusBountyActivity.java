package org.shakticoin.registration;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityBountyBinding;

public class BonusBountyActivity extends AppCompatActivity {

    private ActivityBountyBinding binding;
    private BonusBountyModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bounty);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(BonusBountyModel.class);
        binding.setViewModel(viewModel);
    }

    public void onClaim(View v) {
        Intent intent = new Intent(this, NewBonusBountyActivity.class);
        startActivity(intent);
    }

    public void onViewPromotion(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }
}
