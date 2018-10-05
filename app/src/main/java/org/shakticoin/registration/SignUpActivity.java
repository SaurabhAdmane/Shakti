package org.shakticoin.registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.shakticoin.R;


public class SignUpActivity extends AppCompatActivity {
    private Spinner ctrlCountries;
    private EditText ctrlPostalCode;
    private EditText ctrlEmailAddress;
    private EditText ctrlPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        ctrlCountries = findViewById(R.id.contries);
        ctrlPostalCode = findViewById(R.id.postal_code);
        ctrlEmailAddress = findViewById(R.id.email_address);
        ctrlPhoneNumber = findViewById(R.id.phone_number);
        TextView ctrlSignInLink = findViewById(R.id.sign_in_link);
        final Context self = this;
        ctrlSignInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(self, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onStartRegistration(View view) {
    }
}
