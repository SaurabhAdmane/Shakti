package com.shakticoin.app.wallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shakticoin.app.referral.MyReferralsActivity;
import com.shakticoin.app.widget.PanelDialog;

import com.shakticoin.app.R;

public class DialogGrabWallet extends DialogFragment implements View.OnClickListener {
    public static final String TAG = DialogGrabWallet.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new PanelDialog.Builder(getContext())
                .setIcon(R.drawable.ic_wallet)
                .setTitle(R.string.dlg_grab_wallet_title)
                .setText(R.string.dlg_grab_wallet_text)
                .setMainButton(R.string.dlg_grab_wallet_action, this)
                .create();
    }

    public static DialogGrabWallet newInstance() {

        Bundle args = new Bundle();

        DialogGrabWallet fragment = new DialogGrabWallet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MyReferralsActivity.class);
        startActivity(intent);
    }
}
