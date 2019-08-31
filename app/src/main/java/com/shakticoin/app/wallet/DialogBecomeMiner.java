package com.shakticoin.app.wallet;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shakticoin.app.widget.PanelDialog;

import com.shakticoin.app.R;

public class DialogBecomeMiner extends DialogFragment implements View.OnClickListener {
    public static final String TAG = DialogBecomeMiner.class.getSimpleName();

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
        Toast.makeText(getActivity(), R.string.err_not_implemented, Toast.LENGTH_SHORT).show();
    }

    public static DialogBecomeMiner newInstance() {

        Bundle args = new Bundle();

        DialogBecomeMiner fragment = new DialogBecomeMiner();
        fragment.setArguments(args);
        return fragment;
    }
}
