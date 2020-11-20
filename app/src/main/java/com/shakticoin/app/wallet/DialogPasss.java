package com.shakticoin.app.wallet;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.wallet.WalletRepository;
import com.shakticoin.app.widget.PanelDialog;

public class DialogPasss extends DialogFragment {

    public interface OnPassListener {
        void onPassEntered(@Nullable String password);
    }
    private OnPassListener listener;

    private EditText vPassphrase;


    private DialogPasss(@NonNull OnPassListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new PanelDialog(requireContext());
        dialog.setContentView(R.layout.dialog_passs);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        vPassphrase = dialog.findViewById(R.id.passphrase);

        vPassphrase.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSubmit();
                return true;
            }
            return false;
        });

        Button doMainAction = dialog.findViewById(R.id.doOk);
        doMainAction.setOnClickListener(v -> onSubmit());

        WalletRepository repository = new WalletRepository();
        repository.setLifecycleOwner(this);

        return dialog;
    }

    private void onSubmit() {
        String passphrase = vPassphrase.getText().toString();

        if (TextUtils.isEmpty(passphrase)) {
            Toast.makeText(ShaktiApplication.getContext(), R.string.dlg_pass__validation_msg1, Toast.LENGTH_SHORT).show();
            return;
        }

        listener.onPassEntered(passphrase);
        dismiss();
    }

    public static DialogPasss getInstance(OnPassListener listener) {
        return new DialogPasss(listener);
    }
}
