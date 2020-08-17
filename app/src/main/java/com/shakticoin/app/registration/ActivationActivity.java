package com.shakticoin.app.registration;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shakticoin.app.R;
import com.shakticoin.app.api.Session;

import java.util.List;

public class ActivationActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        progressBar = findViewById(R.id.progressBar);
        message = findViewById(R.id.message);

        Intent intent = getIntent();
        Uri activationLink = intent.getData();
        if (activationLink != null) {
            List<String> segments = activationLink.getPathSegments();
            String token = activationLink.getLastPathSegment();
            String userId = segments.get(segments.size()-2);
            if (!TextUtils.isEmpty(token) && TextUtils.isDigitsOnly(userId)) {
                Activity self = this;
                // FIXME: decomissined user api is removed. Should we remove the entire activity?
            } else {
                message.setText(R.string.activation_failed);
                startActivity(Session.unauthorizedIntent(this));
            }
        } else {
            message.setText(R.string.activation_failed);
            startActivity(Session.unauthorizedIntent(this));
        }
    }
}
