package com.shakticoin.app.profile;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shakticoin.app.ProgressBarModel;
import com.shakticoin.app.api.kyc.KycCategory;
import com.shakticoin.app.api.kyc.KycDocType;

import java.util.List;

public class PersonalViewModel extends ViewModel implements ProgressBarModel {
    private ObservableBoolean progressBarTrigger = new ObservableBoolean(false);
    public MutableLiveData<String> shaktiId = new MutableLiveData<>();

    /** Selected KYC category */
    MutableLiveData<KycCategory> selectedCategory = new MutableLiveData<>();

    // group of variables bind to document type selector page
    public enum Options {PHOTO, UPLOAD, SKIP}
    public ObservableBoolean takePhotoState = new ObservableBoolean(true);
    public ObservableBoolean uploadFileState = new ObservableBoolean(false);
    public ObservableBoolean skipState = new ObservableBoolean(false);
    public ObservableField<Options> selectedOption = new ObservableField<>(Options.PHOTO);

    public KycDocType kycDocumentType;

    /** Read-only list of KYC categories */
    public List<KycCategory> kycCategories;

    /** Setting this variable to true triggers refreshing the list of files */
    public ObservableBoolean updateList = new ObservableBoolean(false);

    public MutableLiveData<String> fastTrackFormattedPrice = new MutableLiveData<>();

    public PersonalViewModel() {
        // FIXME: ideally this price must be requested from the server
        fastTrackFormattedPrice.setValue("$7.95 USD");

        takePhotoState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = takePhotoState.get();
                if (value) {
                    if (uploadFileState.get()) uploadFileState.set(false);
                    if (skipState.get()) skipState.set(false);
                    selectedOption.set(Options.PHOTO);
                }
            }
        });
        uploadFileState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = uploadFileState.get();
                if (value) {
                    if (takePhotoState.get()) takePhotoState.set(false);
                    if (skipState.get()) skipState.set(false);
                    selectedOption.set(Options.UPLOAD);
                }
            }
        });
        skipState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = skipState.get();
                if (value) {
                    if (takePhotoState.get()) takePhotoState.set(false);
                    if (uploadFileState.get()) uploadFileState.set(false);
                    selectedOption.set(Options.SKIP);
                }
            }
        });
    }

    @Override
    public ObservableBoolean getProgressBarTrigger() {
        return progressBarTrigger;
    }
}
