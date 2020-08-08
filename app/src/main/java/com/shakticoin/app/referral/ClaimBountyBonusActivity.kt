package com.shakticoin.app.referral

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.shakticoin.app.databinding.ActivityClaimBountyBinding

class ClaimBountyBonusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClaimBountyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimBountyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}