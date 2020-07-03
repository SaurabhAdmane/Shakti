package com.shakticoin.app.profile;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.kyc.KycCategory;
import com.shakticoin.app.databinding.ActivityKycBinding;
import com.shakticoin.app.util.CommonUtil;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.widget.DrawerActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class KycActivity extends DrawerActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 342;
    private static final int REQUEST_CAMERA_PERMISSION = 385;

    private ActivityKycBinding binding;
    private KycCommonViewModel viewModel;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kyc);
        binding.setLifecycleOwner(this);
        viewModel = ViewModelProviders.of(this).get(KycCommonViewModel.class);
        binding.setViewModel(viewModel);

        onInitView(binding.getRoot(), getString(R.string.profile_kyc_title));

        Intent intent = getIntent(); Context context = ShaktiApplication.getContext();
        viewModel.kycCategories = intent.getParcelableArrayListExtra(CommonUtil.prefixed("KYC_CATEGORY_LIST", context));
        KycCategory category = getIntent().getParcelableExtra(CommonUtil.prefixed("KYC_CATEGORY", context));
        viewModel.kycCategory.setValue(category);

        if (viewModel.kycCategories != null && viewModel.kycCategories.size() > 0) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, new KycDoctypeFragment())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFragment, new KycSelectorFragment())
                    .commit();
        }
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 4;
    }

    public void onOpenDocuments(View v) {
        if (viewModel.kycDocumentType == null) {
            Toast.makeText(this, R.string.kyc_opt_doctype, Toast.LENGTH_SHORT).show();
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new KycFilesFragment())
                .addToBackStack(KycFilesFragment.class.getSimpleName())
                .commit();
    }

    public void onReSelect(View v) {
        viewModel.kycDocumentType = null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFragment, new KycSelectorFragment())
                .addToBackStack(KycFilesFragment.class.getSimpleName())
                .commit();
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

        KycCategory category = viewModel.kycCategory.getValue();
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
            }
        });

    }

    public void onCancel(View v) {
        finish();
    }
}
