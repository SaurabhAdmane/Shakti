package com.shakticoin.app.registration;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.otp.EmailOTPRepository;
import com.shakticoin.app.api.otp.PhoneOTPRepository;
import com.shakticoin.app.databinding.ActivitySignupOtpBinding;
import com.shakticoin.app.util.CommonUtil;

public class SignUpOTPActivity extends AppCompatActivity {
    ActivitySignupOtpBinding binding;
    private String emailAddress;
    private String phoneNumber;
    private String password;

    private EmailOTPRepository emailOTPRepository = new EmailOTPRepository();
    private PhoneOTPRepository phoneOTPRepository = new PhoneOTPRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        emailAddress = intent.getStringExtra(CommonUtil.prefixed("emailAddress", this));
        phoneNumber = intent.getStringExtra(CommonUtil.prefixed("phoneNumber", this));
        password = intent.getStringExtra(CommonUtil.prefixed("password", this));

        if (!TextUtils.isEmpty(emailAddress)) {
            sendEmaiOTPRequest();
        }
//        if (!TextUtils.isEmpty(phoneNumber)) {
//            sendPhoneOTPRequest();
//        }
    }

    private void sendEmaiOTPRequest() {
        emailOTPRepository.requestRegistration(emailAddress, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                if (error != null) {
                    // TODO: process error
                    return;
                }
            }
        });
    }

    private void sendPhoneOTPRequest() {
        phoneOTPRepository.requestRegistration(phoneNumber, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Void value, Throwable error) {
                if (error != null) {
                    return;
                }
            }
        });
    }
}
