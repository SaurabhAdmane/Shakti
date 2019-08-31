package com.shakticoin.app.referral;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.View;

import com.shakticoin.app.R;
import com.shakticoin.app.widget.PanelDialog;

public class DialogHowToBonus extends DialogFragment implements View.OnClickListener {
    public static final String TAG = DialogHowToBonus.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new PanelDialog.Builder(getContext())
                .setIcon(R.drawable.ic_miner)
                .setTitle(R.string.dlg_howto_title)
                .setText(R.string.dlg_howto_text)
                .setMainButton(R.string.dlg_howto_action, this)
                .create();
    }

    @Override
    public void onClick(View v) {
        getDialog().dismiss();
    }

    public static DialogHowToBonus getInstance() {
        return new DialogHowToBonus();
    }
}
