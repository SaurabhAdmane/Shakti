package org.shakticoin.registration;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.databinding.ActivityBountyBinding;
import org.shakticoin.widget.CheckableRoundButton;

public class BonusBountyActivity extends AppCompatActivity {

    private ActivityBountyBinding binding;
    private BonusBountyModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bounty);setContentView(R.layout.activity_bounty);

        viewModel = ViewModelProviders.of(this).get(BonusBountyModel.class);
    }

    @BindingAdapter("is_checked")
    public static void setButtonState(CheckableRoundButton view, Boolean value) {
        view.setChecked(value);
    }

    @InverseBindingAdapter(attribute = "is_checked", event = "app:is_checkedAttrChanged")
    public static Boolean getButtonState(CheckableRoundButton view) {
        return view.isChecked();
    }

    @BindingAdapter("app:is_checkedAttrChanged")
    public static void setListeners(CheckableRoundButton view, final InverseBindingListener attrChange) {
        view.setOnCheckedChangeListener((buttonView, isChecked) -> attrChange.onChange());
    }

    public void onClaim(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onViewPromotion(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }
}
