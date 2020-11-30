package com.shakticoin.app.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;
import com.shakticoin.app.api.Constants;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.kyc.KYCRepository;
import com.shakticoin.app.api.selfId.SelfRepository;
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
    private KYCRepository kycRepository;
    private SelfRepository selfRepository;

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

        selfRepository = new SelfRepository();
        selfRepository.setLifecycleOwner(this);

        kycRepository = new KYCRepository();
        kycRepository.setLifecycleOwner(this);

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
                break;

            case R.id.doPaySXE:
                DialogPaySXE.getInstance(this::makeSxePayment,  true)
                        .show(requireActivity().getSupportFragmentManager(), DialogPaySXE.class.getSimpleName());
                break;
        }

    }


//    public void onReceive(View v) {
//        // TODO: for now we are able only to send coins to a wallet address, no mapping ID to address.
//        // This rise another problem because we have no way to know what is wallet address is.
//        // So, I use this button to advertise his own wallet address to a user that he can share with
//        // a payer. But this is temporarily.
//        final String[] address = new String[1];
//        final AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
//                .setTitle("You wallet address")
//                .setNegativeButton("Close", null)
//                .setNeutralButton("Copy to cliboard", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ClipboardManager cm =
//                                (ClipboardManager) ShaktiApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                        if (cm != null && !TextUtils.isEmpty(address[0])) {
//                            ClipData clip = ClipData.newPlainText("wallet address", address[0]);
//                            cm.setPrimaryClip(clip);
//                        }
//                    }
//                });
//        viewModel.isProgressBarActive.set(true);
//        walletRepository.getAddress(new OnCompleteListener<String>() {
//            @Override
//            public void onComplete(String walletAddress, Throwable error) {
//                viewModel.isProgressBarActive.set(false);
//                if (error != null && TextUtils.isEmpty(walletAddress)) {
//                    Toast.makeText(getActivity(), Debug.getFailureMsg(getContext(), error), Toast.LENGTH_LONG).show();
//                    return;
//                }
//                address[0] = walletAddress;
//                builder.setMessage(walletAddress);
//                builder.create().show();
//            }
//        });
//    }


    private void createWalletByte(String payee, BigDecimal amount, String message) {
        viewModel.isProgressBarActive.set(true);
        kycRepository.getWalletRequestAPI(new OnCompleteListener<String>() {
            @Override
            public void onComplete(String walletBytes, Throwable error) {
                if (error != null) {
                    Toast.makeText(getActivity(), Debug.getFailureMsg(getActivity(), error), Toast.LENGTH_LONG).show();
                    if (error instanceof RemoteException && ((RemoteException) error).getResponseCode() == 201) {
                        Session.setWalletPassphrase(null);
                    }
                    return;
                }
                Session.setWalletBytes(walletBytes);
                getWalletByte(walletBytes, payee, amount, message);
            }
        });
    }

    private void getWalletByte(String walletBytes, String payee, BigDecimal amount, String message) {
        viewModel.isProgressBarActive.set(true);
        selfRepository.getWalletRequestAPI(walletBytes, new OnCompleteListener<String>() {
            @Override
            public void onComplete(String walletBytes, Throwable error) {
                viewModel.isProgressBarActive.set(false);
                if (error != null) {
                    Toast.makeText(getActivity(), Debug.getFailureMsg(getActivity(), error), Toast.LENGTH_LONG).show();
                    if (error instanceof RemoteException && ((RemoteException) error).getResponseCode() == 201) {
                        Session.setWalletPassphrase(null);
                    }
                    return;
                }
                Session.setWalletBytes(walletBytes);
                getSessionApi(walletBytes, payee, amount, message);
            }
        });
    }

    /**
     * Get the session token
     */
    private void getSessionApi(String bytes, String payee, BigDecimal amount, String message) {

        viewModel.isProgressBarActive.set(true);

        walletRepository.createSession(
                Session.getWalletPassphrase(),
//                "123",
                "",
                bytes
//                "fhctFR+Dj4G72BgCqR6VgXemQUP9V2W2jC65SEecJNNVnciL6F/Bz3fxs7DWAzwtnsNXGQECMLqUQbvBk0KDfDt0vbEY5SFdoRYQ39FhJEznr9H+DC0eN8WT/qcnW+NNwycLsvNJW/m0PgeSuwT3aLjwKhld0GFoLo/BTxiuNezokMU4GZIuDf3/jcfWSrdti6nKYjv0BZe9srs5vAMY3Q"

                , new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(String walletBytes, Throwable error) {
                        if (error != null) {
                            Toast.makeText(getActivity(), Debug.getFailureMsg(getActivity(), error), Toast.LENGTH_LONG).show();
                            if (error instanceof RemoteException && ((RemoteException) error).getResponseCode() == 201) {
                                Session.setWalletPassphrase(null);
                            }
                            return;
                        }
                        Session.setWalletSessionToken(Long.parseLong(walletBytes));
                        transferApi(walletBytes, payee, amount, message);

                    }
                });
    }

    private void transferApi(String bytes, String payee, BigDecimal amount, String message) {
        viewModel.isProgressBarActive.set(true);
        long toshiAmount = amount.multiply(BigDecimal.valueOf(Constants.TOSHI_FACTOR)).longValue();
        walletRepository.transfer((bytes), payee, toshiAmount, message, new OnCompleteListener<TransferModelResponse>() {
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


    private void makeSxePayment(@NonNull String payee, @NonNull BigDecimal amount, String message) {
        createWalletByte(payee, amount, message);

    }
}
