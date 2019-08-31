package com.shakticoin.app.registration;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.View;

import com.shakticoin.app.R;
import com.shakticoin.app.widget.PanelDialog;

public class DialogConfirmEmail extends DialogFragment implements View.OnClickListener {
    public static final String DISMISS_ONLY = "dismissOnlyOnAction";

    private boolean dismissOnlyOnAction = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            dismissOnlyOnAction = args.getBoolean(DISMISS_ONLY, false);
        }

        return new PanelDialog.Builder(getContext())
                .setIcon(R.drawable.ic_check_email)
                .setTitle(R.string.dlg_confirm_email_title)
                .setText(R.string.dlg_confirm_email_text)
                .setMainButton(R.string.dlg_confirm_email_action, this)
                .setOnCloseListener(this)
                .create();
    }

    /**
     * Creates an instance of DialogConfirmEmail class.
     *
     * @param dismissOnly Dialog open login activity when action button is pressed but
     *                            this flag make possible prefent it. Dialog just closing.
     */
    public static DialogConfirmEmail getInstance(boolean dismissOnly) {
        Bundle args = new Bundle();
        args.putBoolean(DialogConfirmEmail.DISMISS_ONLY, dismissOnly);
        DialogConfirmEmail dialog = new DialogConfirmEmail();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        if (!dismissOnlyOnAction) {
            /* TODO: SHAK-105 temporarily disabled until new API takes the shape */
//            Intent intent = new Intent(getActivity(), SignInActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
            Intent intent = new Intent(getActivity(), BonusBountyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // End
        } else {
            getDialog().dismiss();
        }
    }
}
