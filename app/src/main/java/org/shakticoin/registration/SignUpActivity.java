package org.shakticoin.registration;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.shakticoin.R;


public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        // TODO: temporarily, must be an image
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(R.color.colorAppGrey));
    }
}
