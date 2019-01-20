package org.shakticoin.wallet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import org.shakticoin.R;
import org.shakticoin.referral.MyReferralsActivity;
import org.shakticoin.widget.PanelDialog;

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
