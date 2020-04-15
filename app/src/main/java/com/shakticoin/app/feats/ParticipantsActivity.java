package com.shakticoin.app.feats;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityPoeParticipantsBinding;
import com.shakticoin.app.widget.DrawerActivity;

public class ParticipantsActivity extends DrawerActivity {
    private ActivityPoeParticipantsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_poe_participants);
        binding.setLifecycleOwner(this);
        View v = binding.getRoot();

        onInitView(v, getString(R.string.parties_label));
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 0;
    }
}
