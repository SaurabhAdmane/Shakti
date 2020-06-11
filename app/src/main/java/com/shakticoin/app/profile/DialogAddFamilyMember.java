package com.shakticoin.app.profile;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shakticoin.app.R;
import com.shakticoin.app.api.user.FamilyMember;
import com.shakticoin.app.api.user.UserRepository;
import com.shakticoin.app.databinding.DialogFamilyMemberBinding;
import com.shakticoin.app.util.Validator;
import com.shakticoin.app.widget.PanelDialog;

import java.util.Objects;

public class DialogAddFamilyMember extends DialogFragment {
    private DialogFamilyMemberBinding binding;
    private FamilyMember familyMember;
    private UserRepository userRepository = new UserRepository();

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("familyMember", familyMember);
        super.onSaveInstanceState(outState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new PanelDialog(Objects.requireNonNull(getContext()));
        binding = DialogFamilyMemberBinding.inflate(LayoutInflater.from(getContext()), null, false);
        dialog.setContentView(binding.getRoot());

        binding.title.setText(Html.fromHtml(getString(R.string.dlg_family_title)));
        binding.doClose.setOnClickListener(v -> dialog.dismiss());
        binding.buttonCancel.setOnClickListener(v -> dialog.dismiss());
        binding.buttonMainAction.setOnClickListener(v -> addFamilyMember());

        familyMember = savedInstanceState != null
                ? savedInstanceState.getParcelable("familyMember") : new FamilyMember();
        binding.setFamilyMember(familyMember);

        binding.layoutEmailAddress.setValidator((view, value) -> Validator.isEmail(value));
        binding.layoutPhoneNumber.setValidator((view, value) -> Validator.isPhoneNumber(value));

        binding.textPhoneNumber.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addFamilyMember();
            }
            return true;
        });
        return dialog;
    }

    private void addFamilyMember() {
        boolean isValid = true;
        if (TextUtils.isEmpty(familyMember.getFirst_name())) {
            binding.textFirstName.setError(getString(R.string.err_required));
            isValid = false;
        }
        if (TextUtils.isEmpty(familyMember.getLast_name())) {
            binding.textLastName.setError(getString(R.string.err_required));
            isValid = false;
        }
        if (TextUtils.isEmpty(familyMember.getRelationship())) {
            binding.textRelationship.setError(getString(R.string.err_required));
            isValid = false;
        }
        if (!Validator.isEmail(familyMember.getEmail())) {
            binding.textEmailAddress.setError(getString(R.string.err_email_required));
            isValid = false;
        }
        if (!Validator.isPhoneNumber(familyMember.getPhone())) {
            binding.textPhoneNumber.setError(getString(R.string.err_phone_required));
            isValid = false;
        }

        if (!isValid) return;

        final Activity activity = getActivity();
        // FIXME: add a family member here when a new API available
//        userRepository.addFamilyMember(familyMember, new OnCompleteListener<FamilyMember>() {
//            @Override
//            public void onComplete(FamilyMember value, Throwable error) {
//                if (error != null) {
//                    if (error instanceof UnauthorizedException) {
//                        startActivity(Session.unauthorizedIntent(getContext()));
//                    }
//                    return;
//                }
//                Dialog dialog = getDialog();
//                if (dialog != null) {
//                    dialog.setOnDismissListener((DialogInterface.OnDismissListener) getActivity());
//                    dialog.dismiss();
//                }
//            }
//        });
    }

    public static DialogAddFamilyMember getInstance() {
        return new DialogAddFamilyMember();
    }
}
