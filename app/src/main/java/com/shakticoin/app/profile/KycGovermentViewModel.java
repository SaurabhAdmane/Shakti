package com.shakticoin.app.profile;

import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class KycGovermentViewModel extends ViewModel {
    public enum Options {PHOTO, UPLOAD, SKIP}

    public ObservableBoolean takePhotoState = new ObservableBoolean(true);
    public ObservableBoolean uploadFileState = new ObservableBoolean(false);
    public ObservableBoolean skipState = new ObservableBoolean(false);

    public ObservableField<KycGovermentViewModel.Options> selectedOption = new ObservableField<>(KycGovermentViewModel.Options.PHOTO);

    public KycGovermentViewModel() {
        takePhotoState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean value = takePhotoState.get();
                if (value) {
                    if (uploadFileState.get()) uploadFileState.set(false);
                    if (skipState.get()) skipState.set(false);
                    selectedOption.set(KycGovermentViewModel.Options.PHOTO);
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
                    selectedOption.set(KycGovermentViewModel.Options.UPLOAD);
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
                    selectedOption.set(KycGovermentViewModel.Options.SKIP);
                }
            }
        });

    }
}
