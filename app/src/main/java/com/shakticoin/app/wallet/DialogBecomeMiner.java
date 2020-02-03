package com.shakticoin.app.wallet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shakticoin.app.R;
import com.shakticoin.app.widget.PanelDialog;

public class DialogBecomeMiner extends DialogFragment implements View.OnClickListener {
    public static final String TAG = DialogBecomeMiner.class.getSimpleName();

    private View.OnClickListener listener;

    private DialogBecomeMiner(@NonNull View.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new PanelDialog.Builder(getContext())
                .setIcon(R.drawable.ic_miner)
                .setTitle(R.string.dlg_become_miner_title)
                .setText(R.string.dlg_become_miner_text)
                .setMainButton(R.string.dlg_become_miner_action, this)
                .create();
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
        dismiss();
    }

    static DialogBecomeMiner newInstance(@NonNull View.OnClickListener listener) {
        return new DialogBecomeMiner(listener);
    }
}
