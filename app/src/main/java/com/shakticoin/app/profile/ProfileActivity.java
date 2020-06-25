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
import com.shakticoin.app.api.kyc.AddressModel;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.kyc.KycUserModel;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.databinding.ActivityProfileBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.widget.DatePicker;
import com.shakticoin.app.widget.DrawerActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends DrawerActivity {
    private ActivityProfileBinding binding;
    private KYCRepository kycRepository = new KYCRepository();
    private PersonalViewModel viewModel;
    private PersonalInfoViewModel personalInfoViewModel;

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

        viewModel.getProgressBarTrigger().set(true);
        final Activity self = this;

        kycRepository.getUserDetails(new OnCompleteListener<Map<String, Object>>() {
            @Override
            public void onComplete(Map<String, Object> value, Throwable error) {
                viewModel.getProgressBarTrigger().set(false);
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(self));
                    } else {
                        Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                // enable button to the next page if the call for user details was successful
                personalInfoViewModel.nextToSecondPersonalPage.set(true);

                // we save mainly to be able determine if an user data are created
                // already or we need to create new set
                viewModel.shaktiId.setValue((String) value.get("shaktiID"));

                personalInfoViewModel.firstName.setValue((String) value.get("firstName"));
                personalInfoViewModel.middleName.setValue((String) value.get("middleName"));
                personalInfoViewModel.lastName.setValue((String) value.get("lastName"));

                personalInfoViewModel.birthDate.setValue((String) value.get("dob"));

                Map<String, Object> postalAddress = CommonUtil.checkMap(value.get("address"));
                if (postalAddress != null) {
                    personalInfoViewModel.city.setValue((String) postalAddress.get("city"));
                    personalInfoViewModel.postalCode.setValue((String) postalAddress.get("zipCode"));
                    personalInfoViewModel.address1.setValue((String) postalAddress.get("address1"));
                    personalInfoViewModel.address2.setValue((String) postalAddress.get("address2"));
                    String countryCode = (String) postalAddress.get("countryCode");
                    if (countryCode != null) {
                        countryRepo.getCountry(countryCode, new OnCompleteListener<Country>() {
                            @Override
                            public void onComplete(Country value, Throwable error) {
                                if (error != null) {
                                    return;
                                }
                                personalInfoViewModel.selectedCountry.setValue(value);
                            }
                        });

                        String stateProvinceCode = (String) postalAddress.get("stateProvinceCode");
                        if (stateProvinceCode != null) {
                            countryRepo.getSubdivisionsByCountry(countryCode, new OnCompleteListener<List<Subdivision>>() {
                                @Override
                                public void onComplete(List<Subdivision> subdivisions, Throwable error) {
                                    if (error != null) {
                                        return;
                                    }

                                    personalInfoViewModel.stateProvinceList.setValue(subdivisions);

                                    for (Subdivision subdivision : subdivisions) {
                                        if (stateProvinceCode.equals(subdivision.getSubdivision())) {
                                            personalInfoViewModel.selectedState.setValue(subdivision);
                                            break;
                                        }
                                    }
                                }
                            });
                        }
                    }
                }

                personalInfoViewModel.emailAddress.setValue((String) value.get("secondaryEmail"));
                personalInfoViewModel.phoneNumber.setValue((String) value.get("secondaryMobile"));
                personalInfoViewModel.occupation.setValue((String) value.get("occupation"));
                personalInfoViewModel.educationLevel.setValue((String) value.get("education1"));
                Boolean emailAlert = (Boolean) value.get("emailAlert");
                personalInfoViewModel.subscriptionConfirmed.set(emailAlert != null ? emailAlert : false);
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
            final Activity activity = this;

            viewModel.getProgressBarTrigger().set(true);
            KycUserModel userData = createUserModel();
            if (TextUtils.isEmpty(viewModel.shaktiId.getValue())) {
                kycRepository.createUserDetails(userData, new OnCompleteListener<Map<String, Object>>() {

                    @Override
                    public void onComplete(Map<String, Object> value, Throwable error) {
                        viewModel.getProgressBarTrigger().set(false);
                        if (error != null) {
                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                            return;
                        }
                        selectPage(2);
                    }
                });
            } else {
                kycRepository.updateUserDetails(userData, new OnCompleteListener<Map<String, Object>>() {

                    @Override
                    public void onComplete(Map<String, Object> value, Throwable error) {
                        viewModel.getProgressBarTrigger().set(false);
                        if (error != null) {
                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                            return;
                        }
                        selectPage(2);
                    }
                });
            }
        }
    }

    public void onNextAdditionalInfo(View v) {
        boolean validationSuccessful = true;
        if (!Validator.isEmail(personalInfoViewModel.emailAddress.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.emailAddressErrMsg.setValue(getString(R.string.err_email_required));
        }
        if (!Validator.isPhoneNumber(personalInfoViewModel.phoneNumber.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.phoneNumberErrMsg.setValue(getString(R.string.err_phone_required));
        }
        if (TextUtils.isEmpty(personalInfoViewModel.occupation.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.occupationErrMsg.setValue(getString(R.string.err_required));
        }

        if (validationSuccessful) {
            selectPage(3);
        }
    }

    public void onUpdateAdditionalInfo(View v) {
        boolean validationSuccessful = true;
        // TODO: disabled temporarily
        /*
        if (TextUtils.isEmpty(personalInfoViewModel.kinFullName.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.kinFullNameErrMsg.setValue(getString(R.string.err_required));
        }
        if (!Validator.isEmailOrPhoneNumber(personalInfoViewModel.kinContact.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.kinContactErrMsg.setValue(getString(R.string.err_email_phone_required));
        }
        if (TextUtils.isEmpty(personalInfoViewModel.kinRelationship.getValue())) {
            validationSuccessful = false;
            personalInfoViewModel.kinRelationshipErrMsg.setValue(getString(R.string.err_required));
        }
         */

        if (validationSuccessful) {
            final Activity activity = this;

            viewModel.getProgressBarTrigger().set(true);
            KycUserModel userData = createUserModel();
            kycRepository.updateUserDetails(userData, new OnCompleteListener<Map<String, Object>>() {

                @Override
                public void onComplete(Map<String, Object> value, Throwable error) {
                    viewModel.getProgressBarTrigger().set(false);
                    if (error != null) {
                        Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        return;
                    }
                    selectPage(4);
                }
            });
        }
    }

    /**
     * KycUserModel is used when we update personal info and additional info. In both cases
     * it should contains the same data. This is why we create an instance of this class in
     * a separate method.
     */
    private KycUserModel createUserModel() {
        KycUserModel model = new KycUserModel();

        String firstName = personalInfoViewModel.firstName.getValue();
        String middleName = personalInfoViewModel.middleName.getValue();
        String lastName = personalInfoViewModel.lastName.getValue();

        model.setFirstName(firstName);
        model.setMiddleName(middleName);
        model.setLastName(lastName);

        // build a full name
        StringBuilder sb = new StringBuilder();
        if (firstName != null) sb.append(firstName);
        if (middleName != null) sb.append(" ").append(middleName);
        if (lastName != null) sb.append(" ").append(lastName);
        model.setFullName(sb.toString());

        model.setDob(personalInfoViewModel.birthDate.getValue());

        Country country = personalInfoViewModel.selectedCountry.getValue();
        Subdivision stateProvince = personalInfoViewModel.selectedState.getValue();

        AddressModel address = new AddressModel(country.getName(), country.getCode(),
                stateProvince != null ? stateProvince.getSubdivision() : null,
                personalInfoViewModel.city.getValue(), personalInfoViewModel.address1.getValue(),
                personalInfoViewModel.address2.getValue(), personalInfoViewModel.postalCode.getValue());
        model.setAddress(address);

        model.setSecondaryEmail(personalInfoViewModel.emailAddress.getValue());
        model.setSecondaryMobile(personalInfoViewModel.phoneNumber.getValue());
        model.setOccupation(personalInfoViewModel.occupation.getValue());
        model.setEducation1(personalInfoViewModel.educationLevel.getValue());
        model.setEmailAlert(personalInfoViewModel.subscriptionConfirmed.get());

        return model;
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
