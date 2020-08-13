package com.shakticoin.app.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.country.Country;
import com.shakticoin.app.api.country.CountryRepository;
import com.shakticoin.app.api.country.Subdivision;
import com.shakticoin.app.api.kyc.AddressModel;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.kyc.KycCategory;
import com.shakticoin.app.api.kyc.KycUserModel;
import com.shakticoin.app.api.kyc.KycUserView;
import com.shakticoin.app.api.kyc.RelationModel;
import com.shakticoin.app.databinding.ActivityProfileBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.widget.DatePicker;
import com.shakticoin.app.widget.DrawerActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileActivity extends DrawerActivity {
    private static final int REQUEST_IMAGE_CAPTURE      = 352;
    private static final int REQUEST_CAMERA_PERMISSION  = 395;

    private ActivityProfileBinding binding;
    private KYCRepository kycRepository = new KYCRepository();
    private PersonalViewModel viewModel;
    private PersonalInfoViewModel personalInfoViewModel;

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // ?
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                Toast.makeText(this, R.string.err_no_camera_permission, Toast.LENGTH_LONG).show();
            }
        }
    }

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

        Intent intent = getIntent();
        boolean showStatus = intent.getBooleanExtra("showStatus", false);
        if (!showStatus) {
            binding.pageIndicator.setSelectedIndex(1);
        } else {
            binding.pageIndicator.setSelectedIndex(5);
        }

        viewModel.getProgressBarTrigger().set(true);
        final Activity self = this;

        kycRepository.getUserDetails(new OnCompleteListener<KycUserView>() {
            @Override
            public void onComplete(KycUserView value, Throwable error) {
                viewModel.getProgressBarTrigger().set(false);
                if (error != null) {
                    if (error instanceof RemoteException && ((RemoteException) error).getResponseCode() == 404) {
                        // not an error just no KYC data stored yet
                        // enable button to the next page
                        personalInfoViewModel.nextToSecondPersonalPage.set(true);
                        return;
                    } else if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(self));
                    } else {
                        Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show();
                    }
                    return;
                }

                if (showStatus) {
                    //TODO: analize status and decide which page must be selected
                }

                // enable button to the next page if the call for user details was successful
                personalInfoViewModel.nextToSecondPersonalPage.set(true);

                // we save mainly to be able determine if an user data are created
                // already or we need to create new set
                viewModel.shaktiId.setValue((String) value.getShaktiID());

                personalInfoViewModel.firstName.setValue((String) value.getFirstName());
                personalInfoViewModel.middleName.setValue((String) value.getMiddleName());
                personalInfoViewModel.lastName.setValue((String) value.getLastName());

                personalInfoViewModel.birthDate.setValue((String) value.getDob());

                AddressModel postalAddress = value.getAddress();
                if (postalAddress != null) {
                    personalInfoViewModel.city.setValue((String) postalAddress.getCity());
                    personalInfoViewModel.postalCode.setValue((String) postalAddress.getZipCode());
                    personalInfoViewModel.address1.setValue((String) postalAddress.getAddress1());
                    personalInfoViewModel.address2.setValue((String) postalAddress.getAddress2());
                    String countryCode = (String) postalAddress.getCountryCode();
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

                        String stateProvinceCode = (String) postalAddress.getStateProvinceCode();
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

                personalInfoViewModel.emailAddress.setValue((String) value.getSecondaryEmail());
                personalInfoViewModel.phoneNumber.setValue((String) value.getSecondaryMobile());
                personalInfoViewModel.occupation.setValue((String) value.getOccupation());
                personalInfoViewModel.educationLevel.setValue((String) value.getEducation1());
                Boolean emailAlert = (Boolean) value.getEmailAlert();
                personalInfoViewModel.subscriptionConfirmed.set(emailAlert != null ? emailAlert : false);
            }
        }, false);

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
        selectPage(showStatus ? PAGE_VERIFYING : PAGE_PERSONAL_FIRST);
    }

    private void selectPage(int index) {
        Fragment fragment = adapter.getItem(index);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (index > 0) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.replace(binding.mainFragment.getId(), fragment, fragment.getClass().getSimpleName());
        transaction.commit();

        // we have a few fragments which are used under the last page indicator active
        if (index <= 4) {
            binding.pageIndicator.setSelectedIndex(index + 1);
        }

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
            selectPage(PAGE_PERSONAL_SECOND);
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
                        selectPage(PAGE_ADDITIONAL_FIRST);
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
                        selectPage(PAGE_ADDITIONAL_FIRST);
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
            selectPage(PAGE_ADDITIONAL_SECOND);
        }
    }

    public void onUpdateAdditionalInfo(View v) {
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
                selectPage(PAGE_CATEGORIES);
            }
        });
    }

    public void onSelectDoctype(View v) {
        selectPage(PAGE_DOCUMENT_TYPES);
    }

    public void onOpenDocuments(View v) {
        if (viewModel.kycDocumentType == null) {
            Toast.makeText(this, R.string.kyc_opt_doctype, Toast.LENGTH_SHORT).show();
            return;
        }
        selectPage(PAGE_FILES);
    }

    /**
     * Return from list of files to the category selector. This is necessary when a user
     * want to add new file and must select category and document type again.
     */
    public void onReSelect(View v) {
        viewModel.kycDocumentType = null;
        selectPage(PAGE_CATEGORIES);
    }

    public void onAddDocument(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            takePhoto();
        }
    }

    private void takePhoto() {
        PackageManager pm = getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Toast.makeText(this, R.string.err_no_camera, Toast.LENGTH_LONG).show();
            return;
        }

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(pm) == null) {
            Toast.makeText(this, R.string.err_no_camera_app, Toast.LENGTH_LONG).show();
            return;
        }

        File imageFile = null;
        try {
            imageFile = getPhotoFile();
        } catch (IOException e) {
            Debug.logException(e);
            return;
        }

        if (imageFile != null) {
            Uri imageUri = FileProvider.getUriForFile(this, "com.shakticoin.app.fileprovider", imageFile);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File documentDir() {
        File files = getExternalFilesDir("kyc");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (files != null) {
                if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState(files))) {
                    return null;
                }
            } else {
                return null;
            }
        }
        return files;
    }

    private File getPhotoFile() throws IOException {
        File files = documentDir();
        if (files == null) return null;

        KycCategory category = viewModel.selectedCategory.getValue();
        if (category == null) return null;

        File categoryFiles = new File(files, category.getId().toString());
        if (!categoryFiles.exists()) {
            if (!categoryFiles.mkdir()) {
                Debug.logException(new Exception());
                return null;
            }
        }

        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date());
        String documentTypeId = viewModel.kycDocumentType != null ? viewModel.kycDocumentType.getId() : "";
        String filename = getString(R.string.kyc_file_name_template, documentTypeId, timestamp);
        File imageFile = new File(categoryFiles, filename);
        return imageFile.createNewFile() ? imageFile : null;
    }

    public void onSend(View v) {
        final Activity activity = this;
        KYCRepository repository = new KYCRepository();
        File documentDir = documentDir();
        if (documentDir == null) {
            Toast.makeText(this, R.string.kyc_files_nothing_upload, Toast.LENGTH_SHORT).show();
            return;
        }

        File[] categoryDirs = documentDir.listFiles();
        if (categoryDirs == null || categoryDirs.length == 0) {
            Toast.makeText(this, R.string.kyc_files_nothing_upload, Toast.LENGTH_SHORT).show();
            return;
        }

        final List<File> uploadList = new ArrayList<>();
        List<MultipartBody.Part> uploadFileRequests = new ArrayList<>();

        for (File categoryDir : categoryDirs) {
            if (categoryDir.isDirectory()) {
                File[] documents = categoryDir.listFiles();
                if (documents != null && documents.length > 0) {
                    for (File document : documents) {
                        MediaType mediaType = null;
                        String fileExt = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(document).toString());
                        if (!TextUtils.isEmpty(fileExt)) {
                            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                            mediaType = MediaType.parse(mimeTypeMap.getMimeTypeFromExtension(fileExt));
                        }
                        if (mediaType == null) mediaType = MediaType.get("application/octet-stream");
                        uploadList.add(document);
                        String name = "document";
                        switch (categoryDir.getName()) {
                            case "1":
                                name = "utility";
                                break;
                            case "2":
                                name = "financial";
                                break;
                            case "3":
                                name = "authority";
                                break;
                        }
                        uploadFileRequests.add(
                                MultipartBody.Part.createFormData(name, document.getName(), RequestBody.create(mediaType, document)));
                    }
                }
            }
        }

        if (uploadFileRequests.size() == 0) {
            Toast.makeText(this, R.string.kyc_files_nothing_upload, Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.getProgressBarTrigger().set(true);
        repository.uploadDocument(uploadFileRequests, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                viewModel.getProgressBarTrigger().set(false);
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                    return;
                }

                // remove documents that where uploaded
                if (uploadList.size() > 0) {
                    for (File f : uploadList) {
                        f.delete();
                    }
                    viewModel.updateList.set(true);
                }

                // TODO: looks like this is not a correct finishing of the uploading - temporarily
                Toast.makeText(activity, R.string.kyc_files__uploaded, Toast.LENGTH_LONG).show();
                finish();

//                offerFastTrack();
            }
        });

    }

    public void offerFastTrack() {
        selectPage(PAGE_FAST_TRACK);
    }

    public void payFastTrack(View v) {
        viewModel.getProgressBarTrigger().set(true);
        kycRepository.subscription(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                viewModel.getProgressBarTrigger().set(false);
                if (error != null) {
                    if (error instanceof UnauthorizedException) {
                        startActivity(Session.unauthorizedIntent(ShaktiApplication.getContext()));
                    } else {
                        Toast.makeText(ShaktiApplication.getContext(), R.string.err_unexpected, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                finish();
            }
        });
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

        RelationModel relationModel = new RelationModel();
        relationModel.setNextKin1(personalInfoViewModel.kinContact.getValue());
        model.setRelation(relationModel);

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

    private static final int PAGE_PERSONAL_FIRST    = 0;
    private static final int PAGE_PERSONAL_SECOND   = 1;
    private static final int PAGE_ADDITIONAL_FIRST  = 2;
    private static final int PAGE_ADDITIONAL_SECOND = 3;
    private static final int PAGE_CATEGORIES        = 4;
    private static final int PAGE_DOCUMENT_TYPES    = 5;
    private static final int PAGE_FILES             = 6;
    private static final int PAGE_FAST_TRACK        = 7;
    private static final int PAGE_VERIFYING         = 8;
    private static final int PAGE_SUCCESS           = 9;
    private static final int PAGE_REJECTED          = 10;

    /** Collection of fragments for the activity */
    static class ProfileFragmentAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;

        ProfileFragmentAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

            fragments = new ArrayList<>();
            fragments.add(new PersonalInfoFragment1());
            fragments.add(new PersonalInfoFragment2());
            fragments.add(new AdditionalInfoFragment1());
            fragments.add(new AdditionalInfoFragment2());
            fragments.add(new KycSelectorFragment());
            fragments.add(new KycDoctypeFragment());
            fragments.add(new KycFilesFragment());
            fragments.add(new KycFastTrackFragment());
            fragments.add(new KycVerifyingFragment());
            fragments.add(new KycVerifiedFragment());
            fragments.add(new KycRejectedFragment());
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
