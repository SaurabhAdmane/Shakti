package com.shakticoin.app.profile;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.api.country.Subdivision;
import com.shakticoin.app.api.user.Kinship;
import com.shakticoin.app.api.user.Residence;
import com.shakticoin.app.api.user.User;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.databinding.ActivityProfileBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.wallet.BaseWalletActivity;
import com.shakticoin.app.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends BaseWalletActivity {
    private ActivityProfileBinding binding;
    private PersonalViewModel viewModel;
    private PersonalInfoViewModel personalInfoViewModel;
    private AdditionalInfoViewModel additionInfoViewModel;

    private UserRepository userRepo = new UserRepository();
    private CountryRepository countryRepo = new CountryRepository();

    private TextView toolbarTitle;

    private final String[] tags = new String[] {
            PersonalInfoFragment1.class.getSimpleName(),
            PersonalInfoFragment2.class.getSimpleName(),
            AdditionalInfoFragment1.class.getSimpleName(),
            AdditionalInfoFragment2.class.getSimpleName(),
            KycSelectorFragment.class.getSimpleName()};
    private ProfileFragmentAdapter adapter;

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

        toolbarTitle = binding.getRoot().findViewById(R.id.toolbarTitle);

        String[] pageIndicatorItems = new String[] {
                getString(R.string.wallet_page_personal),
                null,
                getString(R.string.wallet_page_additional),
                null,
                getString(R.string.wallet_page_kyc)
        };
        binding.pageIndicator.setSizeAndLabels(pageIndicatorItems);
        binding.pageIndicator.setSelectedIndex(1);

        viewModel.progressBarTrigger.set(true);
        final Activity self = this;
        userRepo.getUserInfo(Session.getUser().getId(), new OnCompleteListener<User>() {
            @Override
            public void onComplete(User user, Throwable error) {
                viewModel.progressBarTrigger.set(false);
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(self));
                    }
                    return;
                }

                List<Residence> residences = user.getResidence();
                if (residences != null && residences.size() > 0) {
                    Residence residence = residences.get(0);
                    countryRepo.getCountry(residence.getCountry_code(), new OnCompleteListener<Country>() {
                        @Override
                        public void onComplete(Country value, Throwable error) {
                            if (error != null) {
                                if (error instanceof UnauthorizedException) {
                                    startActivity(Session.unauthorizedIntent(self));
                                }
                                return;
                            }
                            personalInfoViewModel.selectedCountry.setValue(value);
                        }
                    });
                    if (residence.getSubdivision_id() != null) {
                        countryRepo.getSubdivision(residence.getCountry_code(), residence.getSubdivision_id(),
                                new OnCompleteListener<Subdivision>() {
                                    @Override
                                    public void onComplete(Subdivision value, Throwable error) {
                                        if (error != null) {
                                            if (error instanceof UnauthorizedException) {
                                                startActivity(Session.unauthorizedIntent(self));
                                            }
                                            return;
                                        }
                                        personalInfoViewModel.selectedState.setValue(value);
                                    }
                                });
                    }
                    personalInfoViewModel.address1.setValue(residence.getAddress_line_1());
                    personalInfoViewModel.address2.setValue(residence.getAddress_line_2());
                    personalInfoViewModel.city.setValue(residence.getCity());
                    personalInfoViewModel.postalCode.setValue(residence.getZip_code());
                }

                personalInfoViewModel.firstName.setValue(user.getFirst_name());
                personalInfoViewModel.middleName.setValue(user.getMiddle_name());
                personalInfoViewModel.lastName.setValue(user.getLast_name());
                personalInfoViewModel.birthDate.setValue(user.getDate_of_birth());
                personalInfoViewModel.emailAddress.setValue(user.getEmail());
                personalInfoViewModel.phoneNumber.setValue(user.getMobile());
                personalInfoViewModel.occupation.setValue(user.getOccupation());
                personalInfoViewModel.selectedEducationLevel.setValue(user.getHighest_level_of_education());

                List<Kinship> kindships = user.getKinship();
                if (kindships != null && kindships.size() > 0) {
                    Kinship kinship = kindships.get(0);
                    personalInfoViewModel.kinFullName.setValue(kinship.getFull_name());
                    personalInfoViewModel.kinContact.setValue(kinship.getEmail() != null ? kinship.getEmail() : kinship.getMobile());
                    personalInfoViewModel.kinRelationship.setValue(kinship.getRelation());
                }
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            for (int i = 0; i < tags.length; i++) {
                String tag = tags[i];
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (fragment != null && fragment.isVisible()) {
                    binding.pageIndicator.setSelectedIndex(i+1);
                    Debug.logDebug(tag);
                }
            }
        });
        adapter = new ProfileFragmentAdapter(getSupportFragmentManager());
        selectPage(0);
    }

    private void selectPage(int index) {
        Fragment fragment = adapter.getItem(index);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (index > 0) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.replace(binding.mainFragment.getId(), fragment, fragment.getClass().getSimpleName());
        transaction.commit();

        binding.pageIndicator.setSelectedIndex(index+1);

        if (index == 0 || index == 1) {
            toolbarTitle.setText(R.string.wallet_page_personal);
            binding.profileBackground.setBackgroundResource(R.drawable.personal_background);
        } else if (index == 2 || index == 3) {
            toolbarTitle.setText(R.string.wallet_page_additional);
            binding.profileBackground.setBackgroundResource(R.drawable.additional_background);
        } else {
            toolbarTitle.setText(R.string.wallet_page_kyc);
            binding.profileBackground.setBackgroundResource(R.drawable.kyc_validation_background);
        }
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    public void onDoKYC(View v) {
        Toast.makeText(this, R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
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
            selectPage(1);
        }
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
            selectPage(2);
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
            selectPage(3);
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
            selectPage(4);
        }
    }

    public void onCancel(View v) {
        finish();
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
            personalInfoViewModel.birthDate.setValue(fmt.format(cal.getTime()));
        });
        picker.show(getSupportFragmentManager(), "date-picker");
    }

    /** Collection of fragments for the activity */
    static class ProfileFragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;

        ProfileFragmentAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            fragments = new ArrayList<>(5);
            fragments.add(new PersonalInfoFragment1());
            fragments.add(new PersonalInfoFragment2());
            fragments.add(new AdditionalInfoFragment1());
            fragments.add(new AdditionalInfoFragment2());
            fragments.add(new KycSelectorFragment());
        }

        @NonNull
        @Override
        public Fragment getItem(int i) {
            if (i >= fragments.size()) throw new IllegalArgumentException();
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
