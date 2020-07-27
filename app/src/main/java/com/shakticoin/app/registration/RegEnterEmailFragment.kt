package com.shakticoin.app.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.shakticoin.app.databinding.FragmentRegEnterEmailBinding
import com.shakticoin.app.util.Validator

class RegEnterEmailFragment : Fragment() {
    private var binding: FragmentRegEnterEmailBinding? = null
    private var viewModel: RegViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegEnterEmailBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this
        viewModel = activity?.let { ViewModelProviders.of(it).get(RegViewModel::class.java) }
        binding?.viewModel = viewModel

        binding?.emailAddressLayout?.setValidator { _, value -> Validator.isEmail(value) }
        binding?.emailAddress?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                (activity as RegActivity).onVerifyEmail(null)
            }
            true
        }

        return binding?.root
    }

    override fun onResume() {
        viewModel?.currentStep?.value = RegViewModel.Step.EnterEmail
        viewModel?.emailAddressError?.value = null
        super.onResume()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}