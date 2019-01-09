package org.shakticoin.wallet;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.widget.PanelDialog;

public class DialogBusinessVault extends DialogFragment implements View.OnClickListener {
    public static final String TAG = DialogBusinessVault.class.getSimpleName();

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

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public static DialogBusinessVault newInstance() {
        Bundle args = new Bundle();
        DialogBusinessVault fragment = new DialogBusinessVault();
        fragment.setArguments(args);
        return fragment;
    }
}
