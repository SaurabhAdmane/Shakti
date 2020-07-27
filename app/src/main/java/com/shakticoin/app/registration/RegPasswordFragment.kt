package com.shakticoin.app.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.shakticoin.app.R
import com.shakticoin.app.databinding.FragmentRegPasswordBinding

class RegPasswordFragment : Fragment() {
    private var binding: FragmentRegPasswordBinding? = null
    private var viewModel: RegViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegPasswordBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this
        viewModel = activity?.let { ViewModelProviders.of(it).get(RegViewModel::class.java) }
        binding?.viewModel = viewModel

        binding?.newPassword?.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                checkComplexity(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return binding?.root
    }

    override fun onResume() {
        viewModel?.currentStep?.value = RegViewModel.Step.SetPassword
        viewModel?.password1Error?.value = null
        viewModel?.password2Error?.value = null
        super.onResume()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    fun checkComplexity(password: String) {
        var hasUppercaseDrawable: Int = R.drawable.checkbox_unchecked
        var hasLowercaseDrawable: Int = R.drawable.checkbox_unchecked
        var hasDigitDrawable: Int = R.drawable.checkbox_unchecked
        var hasSymbolDrawable: Int = R.drawable.checkbox_unchecked

        for (c in password) {
            if (c.isUpperCase()) {
                hasUppercaseDrawable = R.drawable.checkbox_checked
                viewModel?.hasUppercase?.value = true
            }
            if (c.isLowerCase()) {
                hasLowercaseDrawable = R.drawable.checkbox_checked
                viewModel?.hasLowercase?.value = true
            }
            if (c.isDigit()) {
                hasDigitDrawable = R.drawable.checkbox_checked
                viewModel?.hasDigit?.value = true
            }
            if (!c.isLetterOrDigit()) {
                hasSymbolDrawable = R.drawable.checkbox_checked
                viewModel?.hasSymbol?.value = true
            }
        }

        binding?.markUppercase?.setImageResource(hasUppercaseDrawable)
        binding?.markLowercase?.setImageResource(hasLowercaseDrawable)
        binding?.markNumbers?.setImageResource(hasDigitDrawable)
        binding?.markSymbols?.setImageResource(hasSymbolDrawable)
    }
}