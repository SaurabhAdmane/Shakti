package com.shakticoin.app.payment;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shakticoin.app.R;
import com.shakticoin.app.util.Debug;
import com.shakticoin.app.widget.PanelDialog;

import java.math.BigDecimal;

public class DialogPaySXE extends DialogFragment {

    public interface OnPayListener {
        void onPay(@NonNull String payee, @NonNull BigDecimal amount, @Nullable String message);
    }

    private OnPayListener listener;

    private EditText vRecipient;
    private EditText vAmount;
    private EditText vMessage;

    private DialogPaySXE(OnPayListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new PanelDialog(requireContext());
        dialog.setContentView(R.layout.dialog_pay_sxe);

        vRecipient = dialog.findViewById(R.id.recipient);
        vAmount = dialog.findViewById(R.id.amount);
        vMessage = dialog.findViewById(R.id.messageToRecipient);

        TextView title = dialog.findViewById(R.id.title);
        title.setText(Html.fromHtml(getString(R.string.dlg_sxe_pay_title)));

        vMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSubmit();
                return true;
            }
            return false;
        });

        ImageButton doClose = dialog.findViewById(R.id.doClose);
        doClose.setOnClickListener(v -> dialog.dismiss());
        Button doCancel = dialog.findViewById(R.id.doCancel);
        doCancel.setOnClickListener(v -> dialog.dismiss());

        Button doMainAction = dialog.findViewById(R.id.doOk);
        doMainAction.setOnClickListener(v -> onSubmit());

        return dialog;
    }

    private void onSubmit() {
        // TODO: recipient must be a Shakti ID, but for now we use wallet address for testing
        String recipient = vRecipient.getText().toString();
        String messageToRecipient = vMessage.getText().toString();
        String amount = vAmount.getText().toString();
        if (TextUtils.isEmpty(recipient) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(messageToRecipient)) {
            Toast.makeText(getContext(), R.string.err_field_mandatory, Toast.LENGTH_LONG).show();
            return;
        }

        BigDecimal amountNum;
        try {
            amountNum = new BigDecimal(amount);
            if (amountNum.compareTo(BigDecimal.ZERO) <= 0) {
                Toast.makeText(getContext(), R.string.err_field_positive_amount, Toast.LENGTH_LONG).show();
            }
        } catch (NumberFormatException e) {
            Debug.logDebug(e.getMessage());
            Toast.makeText(getContext(), R.string.err_field_incorrect_number, Toast.LENGTH_LONG).show();
            return;
        }

        listener.onPay(recipient, amountNum, messageToRecipient);

        dismiss();
    }

    public static DialogPaySXE getInstance(@NonNull OnPayListener listener) {
        return new DialogPaySXE(listener);
    }
}
