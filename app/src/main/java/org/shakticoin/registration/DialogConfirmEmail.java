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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        PanelDialog dialog = new PanelDialog.Builder(getContext())
                .setTitle(R.string.dlg_confirm_email_title)
                .setText(R.string.dlg_confirm_email_text)
                .setMainButton(R.string.dlg_confirm_email_action, this)
                .create();
        return dialog;
    }

    public static DialogConfirmEmail getInstance() {
        return new DialogConfirmEmail();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), SignInActivity.class));
        getDialog().dismiss();
    }
}
