package com.shakticoin.app.referral

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shakticoin.app.databinding.FragmentClaimBountyBinding

class ClaimBountyBonusFragment : Fragment() {
    private lateinit var binding: FragmentClaimBountyBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentClaimBountyBinding.inflate(inflater, container, false)
        return binding.root
    }
}