package org.shakticoin.registration;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
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
        ((ViewDataBinding) binding).setLifecycleOwner(this);

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
