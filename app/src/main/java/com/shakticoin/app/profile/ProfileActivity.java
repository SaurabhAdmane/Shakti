package com.shakticoin.app.profile;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.shakticoin.app.R;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.databinding.ActivityProfileBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.wallet.BaseWalletActivity;

public class ProfileActivity extends BaseWalletActivity {
    private ActivityProfileBinding binding;
    private PersonalViewModel viewModel;
    private PersonalInfoViewModel personalInfoViewModel;
    private AdditionalInfoViewModel additionInfoViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setLifecycleOwner(this);

        viewModel = ViewModelProviders.of(this).get(PersonalViewModel.class);
        binding.setViewModel(viewModel);
        personalInfoViewModel = ViewModelProviders.of(this).get(PersonalInfoViewModel.class);
        additionInfoViewModel = ViewModelProviders.of(this).get(AdditionalInfoViewModel.class);

        onInitView(binding.getRoot(), getString(R.string.profile_personal_title));

        binding.pageIndicator.setSelectedIndex(1);

        Activity activity = this;
        binding.mainFragment.setAdapter(new ProfileFragmentAdapter(getSupportFragmentManager()));
        binding.mainFragment.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {
                Debug.logDebug(Integer.toString(i));
                binding.pageIndicator.setSelectedIndex(i+1);
                if (i == 0 || i == 1) {
                    binding.profileBackground.setBackgroundResource(R.drawable.personal_background);
                } else if (i == 2 || i == 3) {
                    binding.profileBackground.setBackgroundResource(R.drawable.additional_background);
                } else {
                    binding.profileBackground.setBackgroundResource(R.drawable.kyc_validation_background);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    public void onDoKYC(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void onUpdatePersonalInfo(View v) {
        boolean validationSuccessful = true;
        if (personalInfoViewModel.selectedCountry.getValue() == null) {
            validationSuccessful = false;
            personalInfoViewModel.countriesErrMsg.setValue(getString(R.string.err_required));
        }
        if (TextUtils.isEmpty(personalInfoViewModel.city.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.cityErrMsg.setValue(getString(R.string.err_required));
        }
        if (TextUtils.isEmpty(personalInfoViewModel.address1.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.addressErrMsg.setValue(getString(R.string.err_required));
        }
        Country selectedCountry = personalInfoViewModel.selectedCountry.getValue();
        if (!Validator.isPostalCodeValid(
                selectedCountry != null ? selectedCountry.getCode() : null, personalInfoViewModel.postalCode.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.postalCodeErrMsg.setValue(getString(R.string.err_postalCode_requird));
        }

        if (validationSuccessful) {
            binding.mainFragment.setCurrentItem(2);
        }
    }

    public void onUpdateAdditionalInfo(View v) {
        boolean validationSuccessful = true;
        if (TextUtils.isEmpty(additionInfoViewModel.kinFullName.getValue())) {
            validationSuccessful = false;
            additionInfoViewModel.kinFullNameErrMsg.setValue(getString(R.string.err_required));
        }
        if (!Validator.isEmailOrPhoneNumber(additionInfoViewModel.kinContact.getValue())) {
            validationSuccessful = false;
            additionInfoViewModel.kinContactErrMsg.setValue(getString(R.string.err_email_phone_required));
        }
        if (TextUtils.isEmpty(additionInfoViewModel.kinRelationship.getValue())) {
            validationSuccessful = false;
            additionInfoViewModel.kinRelationshipErrMsg.setValue(getString(R.string.err_required));
        }

        if (validationSuccessful) {
            binding.mainFragment.setCurrentItem(4);
        }
    }

    public void onNextPersonalInfo(View v) {
        boolean validationSuccessful = true;
        if (TextUtils.isEmpty(personalInfoViewModel.firstName.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.firstNameErrMsg.setValue(getString(R.string.err_required));
        }
        if (TextUtils.isEmpty(personalInfoViewModel.lastName.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.lastNameErrMsg.setValue(getString(R.string.err_required));
        }

        if (validationSuccessful) {
            binding.mainFragment.setCurrentItem(1);
        }
    }

    public void onNextAdditionalInfo(View v) {
        boolean validationSuccessful = true;
        if (!Validator.isEmail(additionInfoViewModel.emailAddress.getValue())) {
            validationSuccessful = false;
            additionInfoViewModel.emailAddressErrMsg.setValue(getString(R.string.err_email_required));
        }
        if (!Validator.isPhoneNumber(additionInfoViewModel.phoneNumber.getValue())) {
            validationSuccessful = false;
            additionInfoViewModel.phoneNumberErrMsg.setValue(getString(R.string.err_phone_required));
        }
        if (TextUtils.isEmpty(additionInfoViewModel.occupation.getValue())) {
            validationSuccessful = false;
            additionInfoViewModel.occupationErrMsg.setValue(getString(R.string.err_required));
        }

        if (validationSuccessful) {
            binding.mainFragment.setCurrentItem(3);
        }
    }

    public void onCancel(View v) {
        finish();
    }

    /** Collection of fragments for the activity */
    class ProfileFragmentAdapter extends FragmentPagerAdapter {

        ProfileFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new PersonalInfoFragment1();
                case 1:
                    return new PersonalInfoFragment2();
                case 2:
                    return new AdditionalInfoFragment1();
                case 3:
                    return new AdditionalInfoFragment2();
                case 4:
                    return new KycSelectorFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
