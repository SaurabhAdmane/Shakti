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
import com.shakticoin.app.databinding.ActivityFeatsChildBinding;
import com.shakticoin.app.profile.CompanyProfileActivity;
import com.shakticoin.app.profile.KycSelectorFragment;
import com.shakticoin.app.widget.DatePicker;
import com.shakticoin.app.widget.DrawerActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class OnboardChildActivity extends DrawerActivity {
    private final String[] tags = new String[] {
            OnboardChildPage1Fragment.class.getSimpleName(),
            OnboardChildPage2Fragment.class.getSimpleName(),
            OnboardChildPage3Fragment.class.getSimpleName(),
            OnboardChildPage4Fragment.class.getSimpleName(),
            KycSelectorFragment.class.getSimpleName()};
    private FragmentAdapter pageAdapter;

    private OnboardChildViewModel viewModel;
    private ActivityFeatsChildBinding binding;

    private TextView toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(OnboardChildViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feats_child);
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);

        View rootView = binding.getRoot();

        onInitView(binding.getRoot(), getString(R.string.feats_title)+"\n"+getString(R.string.feats_onboard_child));
        toolbarTitle = rootView.findViewById(R.id.toolbarTitle);

        String[] pageIndicatorItems = new String[] {
                getString(R.string.wallet_page_personal),
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

    public void onNextPersonalInfo(View v) {
        selectPage(1);
    }

    public void onUpdatePersonalInfo(View v) {
        selectPage(2);
    }

    public void onNextAdditionalInfo(View v) {
        selectPage(3);
    }

    public void onUpdateAdditionalInfo(View v) {
        selectPage(4);
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

            fragments = new ArrayList<>(5);
            fragments.add(new OnboardChildPage1Fragment());
            fragments.add(new OnboardChildPage2Fragment());
            fragments.add(new OnboardChildPage3Fragment());
            fragments.add(new OnboardChildPage4Fragment());
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

    public void onSetBirthDate(View v) {
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
            viewModel.birthDate.setValue(fmt.format(cal.getTime()));
        });
        picker.show(getSupportFragmentManager(), "date-picker");
    }
}
