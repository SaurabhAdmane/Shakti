package com.shakticoin.app.referral;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.bounty.BountyReferralData;
import com.shakticoin.app.api.bounty.BountyRepository;
import com.shakticoin.app.databinding.FragmentMyRefsLockedBinding;
import com.shakticoin.app.util.FormatUtil;

import java.math.BigDecimal;

public class MyReferralsLockedFragment extends Fragment {
    private FragmentMyRefsLockedBinding binding;
    private BountyRepository bountyRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyRefsLockedBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);

        bountyRepository = new BountyRepository();
        bountyRepository.setLifecycleOwner(this);

        bountyRepository.getBounties(new OnCompleteListener<BountyReferralData>() {
            @Override
            public void onComplete(BountyReferralData value, Throwable error) {
                if (error != null) {
                    return;
                }

                Integer lockedBounty = value.getLockedGenesisBounty();
                if (lockedBounty != null) {
                    binding.sxeBalance.setText(FormatUtil.formatCoinAmount(BigDecimal.valueOf(lockedBounty)));
                }
            }
        });
        return binding.getRoot();
    }
}
