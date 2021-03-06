package com.shakticoin.app.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.shakticoin.app.api.otp.IntlPhoneCountryCode
import com.shakticoin.app.api.otp.PhoneOTPRepository
import com.shakticoin.app.databinding.FragmentRegEnterMobileBinding
import com.shakticoin.app.util.CommonUtil
import com.shakticoin.app.util.Validator

class RegEnterMobileFragment : Fragment() {
    private var binding: FragmentRegEnterMobileBinding? = null
    private var viewModel: RegViewModel? = null
    private lateinit var phoneOtpRepository: PhoneOTPRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegEnterMobileBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this
        viewModel = activity?.let { ViewModelProviders.of(it).get(RegViewModel::class.java) }
        binding?.viewModel = viewModel

        phoneOtpRepository = PhoneOTPRepository()
        phoneOtpRepository.setLifecycleOwner(this)

        binding?.phoneNumberLayout?.setValidator{_, value -> Validator.isPhoneNumber(value)}
        binding?.phoneNumber?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                (activity as RegActivity).onVerifyPhone(null)
            }
            true
        }

        if (viewModel?.countryCodes != null) {
            viewModel?.countryCodes?.observe(viewLifecycleOwner, object : Observer<List<IntlPhoneCountryCode>?> {
                override fun onChanged(codes: List<IntlPhoneCountryCode>?) {
                    if (codes != null) {
                        val deviceIsoCode = CommonUtil.getCountryCode2(requireContext());
                        if (deviceIsoCode != null) {
                            val codesList: List<IntlPhoneCountryCode>? = viewModel?.countryCodes?.value
                            if (codesList != null) {
                                for (item in codesList) {
                                    if (deviceIsoCode.equals(item.isoCode, true)) {
                                        viewModel?.selectedCountryCode?.value = item
                                        break
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }

        return binding?.root
    }

    override fun onResume() {
        viewModel?.currentStep?.value = RegViewModel.Step.EnterMobile
        viewModel?.phoneNumberError?.value = null
        super.onResume()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}