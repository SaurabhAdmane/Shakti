package com.shakticoin.app.feats;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.databinding.ActivityFeatsVerifierBinding;
import com.shakticoin.app.widget.DrawerActivity;

import java.util.ArrayList;

public class OnboardVerifierActivity extends DrawerActivity {
    private OnboardVerifierViewModel viewModel;
    private ActivityFeatsVerifierBinding binding;

    private TextView toolbarTitle;

    private final String[] tags = new String[] {
            OnboardVerifierPage1Fragment.class.getSimpleName()};
    private FragmentAdapter pageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feats_verifier);
        viewModel = ViewModelProviders.of(this).get(OnboardVerifierViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        View v = binding.getRoot();

        toolbarTitle = v.findViewById(R.id.toolbarTitle);

        onInitView(v, getString(R.string.feats_title)+"\n" + getString(R.string.feats_verifier_onboard));

        String[] pageIndicatorItems = new String[] {
                getString(R.string.feats_verifier_info)};
        binding.pageIndicator.setSizeAndLabels(pageIndicatorItems);
        binding.pageIndicator.setSelectedIndex(1);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            for (int i = 0; i < tags.length; i++) {
                String tag = tags[i];
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment != null && fragment.isVisible()) {
                    binding.pageIndicator.setSelectedIndex(i+1);
                }
            }
        });
        pageAdapter = new FragmentAdapter(getSupportFragmentManager());
        selectPage(0);

    }

    private void selectPage(int index) {
        Fragment fragment = pageAdapter.getItem(index);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (index > 0) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.replace(binding.mainFragment.getId(), fragment, fragment.getClass().getSimpleName());
        transaction.commit();

        binding.pageIndicator.setSelectedIndex(index+1);
    }

    public void onUpdate(View v) {
        finish();
    }

    public void onCancel(View v) {
        finish();
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 0;
    }

    static class FragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;

        FragmentAdapter(@NonNull FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            fragments = new ArrayList<>(1);
            fragments.add(new OnboardVerifierPage1Fragment());
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position >= fragments.size()) throw new IllegalArgumentException();
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
