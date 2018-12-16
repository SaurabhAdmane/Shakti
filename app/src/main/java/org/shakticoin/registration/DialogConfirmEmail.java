package org.shakticoin.registration;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import org.shakticoin.R;
import org.shakticoin.widget.PanelDialog;

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

        PanelDialog dialog = new PanelDialog.Builder(getContext())
                .setTitle(R.string.dlg_confirm_email_title)
                .setText(R.string.dlg_confirm_email_text)
                .setMainButton(R.string.dlg_confirm_email_action, this)
                .create();

        return dialog;
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
            Intent intent = new Intent(getActivity(), SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        getDialog().dismiss();
    }
}
