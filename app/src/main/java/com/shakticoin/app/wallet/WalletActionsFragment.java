package com.shakticoin.app.wallet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.Constants;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.wallet.TransferModelResponse;
import com.shakticoin.app.api.wallet.WalletRepository;
import com.shakticoin.app.databinding.FragmentInlineWalletActionsBinding;
import com.shakticoin.app.payment.DialogPaySXE;
import com.shakticoin.app.util.Debug;

import java.math.BigDecimal;

public class WalletActionsFragment extends Fragment {
    private FragmentInlineWalletActionsBinding binding;
    private WalletRepository walletRepository;
    private WalletModel viewModel;
    private Boolean walletBytes;

    public WalletActionsFragment(Boolean walletBytes) {
        this.walletBytes = walletBytes;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(WalletModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInlineWalletActionsBinding.inflate(getLayoutInflater(), container, false);
        binding.setLifecycleOwner(this);

        walletRepository = new WalletRepository();
        walletRepository.setLifecycleOwner(getViewLifecycleOwner());



        binding.doPaySXE.setOnClickListener(this::onPay);
        binding.doReceiveSXE.setOnClickListener(this::onPay);

        if(walletBytes){
            binding.doReceiveSXE.setText(getResources().getString(R.string.wallet_receive_sxe_vault));
            binding.doPaySXE.setText(getResources().getString(R.string.wallet_pay_sxe_receaive));
        }else{
            binding.doReceiveSXE.setText(getResources().getString(R.string.wallet_receive_sxe));
            binding.doPaySXE.setText(getResources().getString(R.string.wallet_pay_sxe));
        }

        return binding.getRoot();
    }

    public void onPay(View view) {

        switch (view.getId()) {
            case R.id.doReceiveSXE:
                DialogPaySXE.getInstance(this::makeSxePayment,  false)
                        .show(requireActivity().getSupportFragmentManager(), DialogPaySXE.class.getSimpleName());

            case R.id.doPaySXE:
                DialogPaySXE.getInstance(this::makeSxePayment,  true)
                        .show(requireActivity().getSupportFragmentManager(), DialogPaySXE.class.getSimpleName());
        }

    }


    public void onReceive(View v) {
        // TODO: for now we are able only to send coins to a wallet address, no mapping ID to address.
        // This rise another problem because we have no way to know what is wallet address is.
        // So, I use this button to advertise his own wallet address to a user that he can share with
        // a payer. But this is temporarily.
        final String[] address = new String[1];
        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle("You wallet address")
                .setNegativeButton("Close", null)
                .setNeutralButton("Copy to cliboard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cm =
                                (ClipboardManager) ShaktiApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        if (cm != null && !TextUtils.isEmpty(address[0])) {
                            ClipData clip = ClipData.newPlainText("wallet address", address[0]);
                            cm.setPrimaryClip(clip);
                        }
                    }
                });
        viewModel.isProgressBarActive.set(true);
        walletRepository.getAddress(new OnCompleteListener<String>() {
            @Override
            public void onComplete(String walletAddress, Throwable error) {
                viewModel.isProgressBarActive.set(false);
                if (error != null && TextUtils.isEmpty(walletAddress)) {
                    Toast.makeText(getActivity(), Debug.getFailureMsg(getContext(), error), Toast.LENGTH_LONG).show();
                    return;
                }
                address[0] = walletAddress;
                builder.setMessage(walletAddress);
                builder.create().show();
            }
        });
    }

    private void makeSxePayment(@NonNull String payee, @NonNull BigDecimal amount, String message) {
        viewModel.isProgressBarActive.set(true);
        long toshiAmount = amount.multiply(BigDecimal.valueOf(Constants.TOSHI_FACTOR)).longValue();
        walletRepository.transfer(payee, toshiAmount, message, new OnCompleteListener<TransferModelResponse>() {
            @Override
            public void onComplete(TransferModelResponse response, Throwable error) {
                viewModel.isProgressBarActive.set(false);
                if (error != null) {
                    Toast.makeText(getContext(), Debug.getFailureMsg(getContext(), error), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
