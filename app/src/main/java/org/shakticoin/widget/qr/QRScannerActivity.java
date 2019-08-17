package org.shakticoin.widget.qr;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.shakticoin.R;
import org.shakticoin.util.CommonUtil;
import org.shakticoin.util.Debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class QRScannerActivity extends AppCompatActivity {

    public static final int REQUEST_QR              = 99;
    public static final String KEY_REFERRAL_CODE    = "referralCode";

    private static final int PERMISSION_REQUESTS = 34;

    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scanner);

        preview = findViewById(R.id.preview);
        graphicOverlay = findViewById(R.id.overlay);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        if (allPermissionsGranted()) {
            createCameraSource();
        } else {
            getRuntimePermissions();
        }
    }

    private void createCameraSource() {
        // if there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
        }

        BarcodeScanningProcessor processor = new BarcodeScanningProcessor();
        processor.addBarcodeFoundListener(url -> {
            preview.stop();
            if (cameraSource != null) {
                cameraSource.release();
            }

            // referral url contains query parameters referral_code and we need to extract it
            Uri referralUri = Uri.parse(url);
            String referralCode = referralUri.getQueryParameter("referral_code");

            if (!TextUtils.isEmpty(referralCode)) {
                Intent intent = new Intent();
                intent.putExtra(CommonUtil.prefixed(KEY_REFERRAL_CODE, this), referralCode);
                this.setResult(RESULT_OK, intent);
            } else {
                Toast.makeText(this, R.string.err_qr_incorrect, Toast.LENGTH_LONG).show();
                this.setResult(RESULT_CANCELED);
            }
            finish();
        });
        cameraSource.setMachineLearningFrameProcessor(processor);
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Debug.logDebug("Preview is null");
                    return;
                }
                if (graphicOverlay == null) {
                    Debug.logDebug("graphOverlay is null");
                    return;
                }
                preview.start(cameraSource, graphicOverlay);

            } catch (IOException e) {
                Debug.logException(e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startCameraSource();
    }

    /** Stops the camera. */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (allPermissionsGranted()) {
            createCameraSource();
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        } else {
            Toast.makeText(this, R.string.err_qr_permissions_not_granted, Toast.LENGTH_LONG).show();
            this.setResult(RESULT_CANCELED);
            finish();
        }
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        return (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }

}
