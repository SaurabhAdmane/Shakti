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
import com.shakticoin.app.databinding.ActivityFeatsSchoolBinding;
import com.shakticoin.app.profile.KycSelectorFragment;
import com.shakticoin.app.widget.DatePicker;
import com.shakticoin.app.widget.DrawerActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class OnboardSchoolActivity extends DrawerActivity {
    private final String[] tags = new String[] {
            OnboardSchoolPage1Fragment.class.getSimpleName(),
            OnboardSchoolPage2Fragment.class.getSimpleName(),
            OnboardSchoolPage3Fragment.class.getSimpleName(),
            null,
            KycSelectorFragment.class.getSimpleName()};
    private FragmentAdapter pageAdapter;

    private OnboardSchoolViewModel viewModel;
    private ActivityFeatsSchoolBinding binding;

    private TextView toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(OnboardSchoolViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feats_school);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        View rootView = binding.getRoot();

        onInitView(binding.getRoot(), getString(R.string.feats_title)+"\n"+getString(R.string.feats_onboard_school));
        toolbarTitle = rootView.findViewById(R.id.toolbarTitle);

        String[] pageIndicatorItems = new String[] {
                getString(R.string.feats_school_info),
                null,
                getString(R.string.wallet_page_additional),
                null,
                getString(R.string.wallet_page_kyc)
        };
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

    @Override
    protected int getCurrentDrawerSelection() {
        return 0;
    }

    public void onSetEstablishedDate(View v) {
        DatePicker picker = new DatePicker((view, year, month, dayOfMonth) -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            viewModel.establishedDate.setValue(fmt.format(cal.getTime()));
        });
        picker.show(getSupportFragmentManager(), "date-picker");
    }

    public void onNextSchoolInfo(View v) {
        selectPage(1);
    }

    public void onUpdateSchoolInfo(View v) {
        selectPage(2);
    }

    public void onUpdateAdditionalInfo(View v) {
        selectPage(3);
    }

    public void onCancel(View v) {
        finish();
    }

    static class FragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;

        FragmentAdapter(@NonNull FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            fragments = new ArrayList<>(4);
            fragments.add(new OnboardSchoolPage1Fragment());
            fragments.add(new OnboardSchoolPage2Fragment());
            fragments.add(new OnboardSchoolPage3Fragment());
            fragments.add(new KycSelectorFragment());
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
