package org.shakticoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.shakticoin.registration.SignInActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_splash);

        route();
        finish();
    }

    private void route() {
        // TODO: we can send new user directly to registration but not sure we need it. Think about this.
        startActivity(new Intent(this, SignInActivity.class));
    }
}
