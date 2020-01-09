package com.shakticoin.app.registration;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.user.UserRepository;

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
                progressBar.setVisibility(View.VISIBLE);
                UserRepository repository = new UserRepository();
                repository.acvivateUser(Integer.valueOf(userId), token, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Void value, Throwable error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error != null) {
                            message.setText(R.string.activation_failed);
                            return;
                        }
                        Toast.makeText(self, R.string.activation_active, Toast.LENGTH_LONG).show();
                        startActivity(Session.unauthorizedIntent(self));
                    }
                });
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
