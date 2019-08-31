// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.shakticoin.app.widget.qr;

import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;

import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.Validator;

import java.io.IOException;
import java.util.List;

/**
 * Barcode Detector.
 */
public class BarcodeScanningProcessor extends VisionProcessorBase<List<FirebaseVisionBarcode>> {

    private final FirebaseVisionBarcodeDetector detector;
    private BarcodeFoundListener listener;

    public BarcodeScanningProcessor() {
        FirebaseVisionBarcodeDetectorOptions options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
    }

    public void addBarcodeFoundListener(BarcodeFoundListener listener) {
        this.listener = listener;
    }

    @Override
    public void stop() {
        try {
            detector.close();

        } catch (IOException e) {
            Debug.logException(e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionBarcode>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<FirebaseVisionBarcode> barcodes,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {

        graphicOverlay.clear();

        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay, originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }

        for (int i = 0; i < barcodes.size(); ++i) {
            FirebaseVisionBarcode barcode = barcodes.get(i);
            BarcodeGraphic barcodeGraphic = new BarcodeGraphic(graphicOverlay, barcode);
            graphicOverlay.add(barcodeGraphic);

            Debug.logDebug("Display: " + barcode.getDisplayValue());
            Debug.logDebug("Raw: " + barcode.getRawValue());

            int type = barcode.getValueType();
            if (type == FirebaseVisionBarcode.TYPE_URL) {
                FirebaseVisionBarcode.UrlBookmark bookmark = barcode.getUrl();
                if (bookmark != null) {
                    String url = bookmark.getUrl();
                    // the bookmark can be empty bat display value is an URL
                    if (TextUtils.isEmpty(url)) {
                        url = barcode.getDisplayValue();
                    }

                    if (Validator.isShaktiReferralUrl(url)) {
                        // TODO: add a signal, vibration or sound
                        stop();
                        if (listener != null) listener.onBarcode(url);
                        break;
                    }
                }
            };
        }

        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Debug.logException(e);
    }

}