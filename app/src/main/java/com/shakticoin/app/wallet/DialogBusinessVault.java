package com.shakticoin.app.wallet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shakticoin.app.R;
import com.shakticoin.app.widget.PanelDialog;

public class DialogBusinessVault extends DialogFragment implements View.OnClickListener {
    public static final String TAG = DialogBusinessVault.class.getSimpleName();

    private View.OnClickListener listener;

    private DialogBusinessVault(@NonNull View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new PanelDialog.Builder(getContext())
                .setIcon(R.drawable.ic_vault)
                .setTitle(R.string.dlg_business_vault_title)
                .setText(R.string.dlg_business_vault_text)
                .setMainButton(R.string.dlg_business_vault_action, this)
                .create();
    }

    static DialogBusinessVault newInstance(@NonNull View.OnClickListener listener) {
        Bundle args = new Bundle();
        DialogBusinessVault fragment = new DialogBusinessVault(listener);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
        dismiss();
    }
}
