package com.shakticoin.app.payment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shakticoin.app.R;
import com.shakticoin.app.widget.PanelDialog;

import java.util.Objects;

public class DialogPaySXE extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new PanelDialog(Objects.requireNonNull(getContext()));
        dialog.setContentView(R.layout.dialog_pay_sxe);

        TextView title = dialog.findViewById(R.id.title);
        title.setText(Html.fromHtml(getString(R.string.dlg_sxe_pay_title)));

        ImageButton doClose = dialog.findViewById(R.id.doClose);
        doClose.setOnClickListener(v -> dialog.dismiss());
        Button doCancel = dialog.findViewById(R.id.doCancel);
        doCancel.setOnClickListener(v -> dialog.dismiss());

        Button doMainAction = dialog.findViewById(R.id.doOk);
        doMainAction.setOnClickListener(v -> dialog.dismiss());

        return dialog;
    }

    public static DialogPaySXE getInstance() {
        return new DialogPaySXE();
    }
}
