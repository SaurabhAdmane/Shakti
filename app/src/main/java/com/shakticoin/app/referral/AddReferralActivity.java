package com.shakticoin.app.referral;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.shakticoin.app.R;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteMessageException;
import com.shakticoin.app.api.referral.ReferralParameters;
import com.shakticoin.app.api.referral.ReferralRepository;
import com.shakticoin.app.api.referral.model.Referral;
import com.shakticoin.app.databinding.ActivityAddReferralBinding;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.widget.DrawerActivity;

import java.util.Map;

public class AddReferralActivity extends DrawerActivity {
    private ActivityAddReferralBinding binding;
    private ReferralParameters referral;
    private ReferralRepository referralRepository = new ReferralRepository();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_referral);
        binding.setLifecycleOwner(this);

        binding.emailFieldLayout.setValidator((view, value) -> Validator.isEmail(value));
        binding.phoneFieldLayout.setValidator((view, value) -> Validator.isPhoneNumber(value));

        onInitView(binding.getRoot(), getString(R.string.my_refs_title), true);

        if (savedInstanceState != null) {
            referral = savedInstanceState.getParcelable("referral");
        } else {
            referral = new ReferralParameters();
        }
        binding.setReferral(referral);

        final Activity activity = this;
        binding.phoneNumber.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                InputMethodManager ims = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (ims != null) {
                    ims.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                onAddReferral(null);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("referral", referral);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getCurrentDrawerSelection() {
        return 3;
    }

    public void onHowToEarn(View v) {
        DialogHowToBonus.getInstance().show(getSupportFragmentManager(), DialogHowToBonus.TAG);
    }

    public void onAddReferral(View v) {
        boolean validationSuccessful = true;
        if (TextUtils.isEmpty(referral.getFirstName())) {
            validationSuccessful = false;
            binding.firstNameFieldLayout.setError(getString(R.string.err_required));
        }
        if (TextUtils.isEmpty(referral.getLastName())) {
            validationSuccessful = false;
            binding.lastNameFieldLayout.setError(getString(R.string.err_required));
        }

        if (!Validator.isEmail(referral.getEmail())) {
            validationSuccessful = false;
            binding.emailFieldLayout.setError(getString(R.string.err_email_required));
        }

        if (validationSuccessful) {
            final Activity activity = this;
            binding.progressBar.setVisibility(View.VISIBLE);
            referralRepository.addReferral(referral, new OnCompleteListener<Referral>() {
                @Override
                public void onComplete(Referral value, Throwable error) {
                    binding.progressBar.setVisibility(View.INVISIBLE);
                    if (error != null) {
                        if (error instanceof RemoteMessageException) {
                            if (((RemoteMessageException) error).hasValidationErrors()) {
                                Map<String, String> errors = ((RemoteMessageException) error).getValidationErrors();
                                for (String field : errors.keySet()) {
                                    if ("firstName".equals(field)) {
                                        binding.firstNameFieldLayout.setError(errors.get(field));
                                    } else if ("lastName".equals(field)) {
                                        binding.lastNameFieldLayout.setError(errors.get(field));
                                    } else if ("email".equals(field)) {
                                        binding.emailFieldLayout.setError(errors.get(field));
                                    } else if ("phone".equals(field)) {
                                        binding.phoneFieldLayout.setError(errors.get(field));
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show();
                        }
                        return;
                    }
                    finish();
                }
            });
        }
    }
}
