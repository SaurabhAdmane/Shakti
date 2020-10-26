package com.shakticoin.app.registration

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.shakticoin.app.R
import com.shakticoin.app.api.OnCompleteListener
import com.shakticoin.app.api.RemoteException
import com.shakticoin.app.api.Session
import com.shakticoin.app.api.onboard.OnboardRepository
import com.shakticoin.app.api.otp.EmailOTPRepository
import com.shakticoin.app.api.otp.PhoneOTPRepository
import com.shakticoin.app.databinding.ActivityRegistrationBinding
import com.shakticoin.app.util.Debug
import com.shakticoin.app.util.Validator

class RegActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private var viewModel: RegViewModel? = null

    private lateinit var otpEmailRepository: EmailOTPRepository
    private lateinit var otpPhoneRepository: PhoneOTPRepository
    private lateinit var onboardRepository: OnboardRepository

    private var imm : InputMethodManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this).get(RegViewModel::class.java)
        binding.viewModel = viewModel
        setContentView(binding.root)

        viewModel?.currentStep?.observe(this, Observer<RegViewModel.Step> { t -> t?.let { updateIndicator(it) } })

        onboardRepository = OnboardRepository()
        onboardRepository.setLifecycleOwner(this)
        otpPhoneRepository = PhoneOTPRepository()
        otpPhoneRepository.setLifecycleOwner(this)
        otpEmailRepository = EmailOTPRepository()
        otpEmailRepository.setLifecycleOwner(this)

        imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?

        supportFragmentManager
                .beginTransaction()
                .add(binding.fragments.id, RegEnterEmailFragment())
                .commit()
    }

    /** Initiates OTP verification for the email address and advances the process to the next step. */
    fun onVerifyEmail(v: View?) {
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        val self: AppCompatActivity = this

        // validate email address
        val emailAddress = viewModel?.emailAddress?.value
        if (emailAddress == null || emailAddress.isEmpty()) {
            viewModel?.emailAddressError?.value = getString(R.string.reg__email_empty_err)
            return
        }
        if (!Validator.isEmail(emailAddress)) {
            viewModel?.emailAddressError?.value = getString(R.string.reg__email_validation_err)
            return
        }

        viewModel?.progressOn?.value = true
        otpEmailRepository.requestRegistration(emailAddress, object : OnCompleteListener<Void?>() {
            override fun onComplete(value: Void?, error: Throwable?) {
                viewModel?.progressOn?.value = false
                if (error != null) {
                    if (error is RemoteException) {
                        when(error.responseCode) {
                            409 -> {
                                Toast.makeText(self, error.message, Toast.LENGTH_LONG).show()
                                startActivity(Session.unauthorizedIntent(self))
                                return
                            }
                            422 -> {
                                Toast.makeText(self, error.message, Toast.LENGTH_LONG).show()
                                onEnterPhone(null)
                                return
                            }
                        }
                    }
                    Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show()
                    return
                }
                self.supportFragmentManager
                        .beginTransaction()
                        .replace(binding.fragments.id, RegVerifyEmailFragment())
                        .addToBackStack(null)
                        .commit()
            }
        })
    }

    /** Sends another request to email otp service w/o changing the current page */
    fun onReSendEmailRequest(v: View) {
        val self: AppCompatActivity = this
        val emailAddress = viewModel?.emailAddress?.value
        if (emailAddress != null) {
            viewModel?.progressOn?.value = true
            otpEmailRepository.requestRegistration(emailAddress, object : OnCompleteListener<Void?>() {
                override fun onComplete(value: Void?, error: Throwable?) {
                    viewModel?.progressOn?.value = false
                    if (error != null) {
                        Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show()
                        return
                    }
                }
            })
        }
    }

    fun onEnterPhone(v: View?) {
        supportFragmentManager
                .beginTransaction()
                .replace(binding.fragments.id, RegEnterMobileFragment())
                .addToBackStack(null)
                .commit()
    }

    fun onVerifyPhone(v: View?) {
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        val self: AppCompatActivity = this

        val countryCode = viewModel?.selectedCountryCode?.value?.countryCode
        if (TextUtils.isEmpty(countryCode)) {
            Toast.makeText(this, R.string.reg__mobile_no_code, Toast.LENGTH_SHORT).show()
            return;
        }
        val phoneNumber = viewModel?.phoneNumber?.value
        if (TextUtils.isEmpty(phoneNumber) || !Validator.isPhoneNumber(phoneNumber)) {
            Toast.makeText(this, R.string.reg__mobile_validation_err, Toast.LENGTH_SHORT).show()
            return;
        }

        viewModel?.progressOn?.value = true
        otpPhoneRepository.checkPhoneNumberStatus(countryCode!!, phoneNumber!!, object : OnCompleteListener<Boolean>() {
            override fun onComplete(isVerified: Boolean?, error: Throwable?) {
                if (error != null) {
                    viewModel?.progressOn?.value = false
                    Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show()
                    return
                }

                if (isVerified != null && isVerified) {
                    viewModel?.progressOn?.value = false
                    supportFragmentManager
                            .beginTransaction()
                            .replace(binding.fragments.id, RegPasswordFragment())
                            .addToBackStack(null)
                            .commit()
                } else {
                    otpPhoneRepository.requestRegistration(countryCode, phoneNumber, object : OnCompleteListener<Void?>() {
                        override fun onComplete(value: Void?, error: Throwable?) {
                            viewModel?.progressOn?.value = false
                            if (error != null) {
                                Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show()
                                return
                            }
                            // error with code 409 allowed to reach this point
                            supportFragmentManager
                                    .beginTransaction()
                                    .replace(binding.fragments.id, RegVerifyMobileFragment())
                                    .addToBackStack(null)
                                    .commit()
                        }
                    })
                }
            }
        })
    }

    fun onReSendSMS(v: View?) {
        val self: AppCompatActivity = this
        val phoneNumber = viewModel?.phoneNumber?.value
        val countryCode = viewModel?.selectedCountryCode?.value?.countryCode
        if (countryCode != null && phoneNumber != null) {
            viewModel?.progressOn?.value = true
            otpPhoneRepository.requestRegistration(countryCode, phoneNumber, object : OnCompleteListener<Void?>() {
                override fun onComplete(value: Void?, error: Throwable?) {
                    viewModel?.progressOn?.value = false
                    if (error != null) {
                        Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show()
                        return
                    }
                }
            })
        }
    }

    fun onSetPassword(v: View?) {
        val activity = this

        val securityCode = viewModel?.smsSecurityCode?.value
        if (TextUtils.isEmpty(securityCode)) {
            viewModel?.smsSecurityCodeError?.value = getString(R.string.reg__mobile_no_security_code)
            return
        }

        viewModel?.progressOn?.value = true
        otpPhoneRepository.confirmRegistration(
                viewModel?.selectedCountryCode?.value?.countryCode!!,
                viewModel?.phoneNumber?.value!!,
                securityCode!!,
                object: OnCompleteListener<Boolean?>() {
                    override fun onComplete(value: Boolean?, error: Throwable?) {
                        viewModel?.progressOn?.value = false
                        if (error != null) {
                            Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show()
                            return
                        }

                        supportFragmentManager
                                .beginTransaction()
                                .replace(binding.fragments.id, RegPasswordFragment())
                                .addToBackStack(null)
                                .commit()
                    }

                })
    }

    fun onCreateAccount(v: View) {
        if (viewModel?.password1?.value != viewModel?.password2?.value) {
            Toast.makeText(this, R.string.err_incorrect_password, Toast.LENGTH_LONG).show()
            return
        }

        if (!viewModel?.hasUppercase?.value!! || !viewModel?.hasLowercase?.value!! ||
                !viewModel?.hasDigit?.value!! || !viewModel?.hasSymbol?.value!! ||
                !Validator.isPasswordStrong(viewModel?.password1?.value) ||
                TextUtils.isEmpty(viewModel?.password1?.value)) {
            Toast.makeText(this, R.string.err_password_not_strong, Toast.LENGTH_LONG).show()
            return
        }

        val self: AppCompatActivity = this
        viewModel?.progressOn?.value = true
        onboardRepository.addUser(viewModel?.emailAddress?.value!!, viewModel?.selectedCountryCode?.value?.countryCode!!,
                viewModel?.phoneNumber?.value!!, viewModel?.password1?.value!!, object: OnCompleteListener<Void>() {
            override fun onComplete(value: Void?, error: Throwable?) {
                viewModel?.progressOn?.value = false
                if (error != null && !(error is RemoteException && 409 == error.responseCode)) {
                    Toast.makeText(self, Debug.getFailureMsg(self, error), Toast.LENGTH_LONG).show()
                    supportFragmentManager
                            .beginTransaction()
                            .replace(binding.fragments.id, RegEnterEmailFragment())
                            .commit()
                    return
                }
                if (error != null && error is RemoteException && 409 == error.responseCode) {
                    Toast.makeText(self, error.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(self, R.string.activation_active, Toast.LENGTH_LONG).show()
                }
                startActivity(Session.unauthorizedIntent(self))
            }
        })
    }

    /** Returns to the previous page */
    fun onGoBack(v: View) {
        viewModel?.progressOn?.value = false
        supportFragmentManager.popBackStackImmediate()
        if ("BackFromEnterMobile" == v.tag) {
            // need to go back for an extra step
            supportFragmentManager.popBackStackImmediate()
        }
    }

    /**
     * Just reset the task and go to login screen
     */
    fun onGoLogin(v: View) {
        startActivity(Session.unauthorizedIntent(this))
    }

    /**
     * Update visual appearance of the indicator according to the current step.
     */
    fun updateIndicator(step: RegViewModel.Step) {
        val currentColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getColor(R.color.colorBrand)
        } else {
            resources.getColor(R.color.colorBrand)
        }
        val whiteColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getColor(android.R.color.white)
        } else {
            resources.getColor(android.R.color.white)
        }
        val greyColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getColor(R.color.colorGrey1)
        } else {
            resources.getColor(R.color.colorGrey1)
        }

        // reset all tickmarks and labels to default color
        ImageViewCompat.setImageTintList(binding.page1, ColorStateList.valueOf(greyColor))
        binding.pageLabel1.setTextColor(greyColor)
        ImageViewCompat.setImageTintList(binding.page2, ColorStateList.valueOf(greyColor))
        binding.pageLabel2.setTextColor(greyColor)
        ImageViewCompat.setImageTintList(binding.page3, ColorStateList.valueOf(greyColor))
        binding.pageLabel3.setTextColor(greyColor)
        ImageViewCompat.setImageTintList(binding.page4, ColorStateList.valueOf(greyColor))
        binding.pageLabel4.setTextColor(greyColor)
        ImageViewCompat.setImageTintList(binding.page5, ColorStateList.valueOf(greyColor))
        binding.pageLabel5.setTextColor(greyColor)

        when (step) {
            RegViewModel.Step.EnterEmail -> {
                ImageViewCompat.setImageTintList(binding.page1, ColorStateList.valueOf(currentColor))
                binding.pageLabel1.setTextColor(whiteColor)

            }
            RegViewModel.Step.VerifyEmail -> {
                ImageViewCompat.setImageTintList(binding.page2, ColorStateList.valueOf(currentColor))
                binding.pageLabel2.setTextColor(whiteColor)
                ImageViewCompat.setImageTintList(binding.page1, ColorStateList.valueOf(whiteColor))
                binding.pageLabel1.setTextColor(whiteColor)
            }
            RegViewModel.Step.EnterMobile -> {
                ImageViewCompat.setImageTintList(binding.page3, ColorStateList.valueOf(currentColor))
                binding.pageLabel3.setTextColor(whiteColor)
                ImageViewCompat.setImageTintList(binding.page1, ColorStateList.valueOf(whiteColor))
                binding.pageLabel1.setTextColor(whiteColor)
                ImageViewCompat.setImageTintList(binding.page2, ColorStateList.valueOf(whiteColor))
                binding.pageLabel2.setTextColor(whiteColor)
            }
            RegViewModel.Step.VerifyMobile -> {
                ImageViewCompat.setImageTintList(binding.page4, ColorStateList.valueOf(currentColor))
                binding.pageLabel4.setTextColor(whiteColor)
                ImageViewCompat.setImageTintList(binding.page1, ColorStateList.valueOf(whiteColor))
                binding.pageLabel1.setTextColor(whiteColor)
                ImageViewCompat.setImageTintList(binding.page2, ColorStateList.valueOf(whiteColor))
                binding.pageLabel2.setTextColor(whiteColor)
                ImageViewCompat.setImageTintList(binding.page3, ColorStateList.valueOf(whiteColor))
                binding.pageLabel3.setTextColor(whiteColor)
            }
            RegViewModel.Step.SetPassword -> {
                ImageViewCompat.setImageTintList(binding.page5, ColorStateList.valueOf(currentColor))
                binding.pageLabel5.setTextColor(whiteColor)
                ImageViewCompat.setImageTintList(binding.page1, ColorStateList.valueOf(whiteColor))
                binding.pageLabel1.setTextColor(whiteColor)
                ImageViewCompat.setImageTintList(binding.page2, ColorStateList.valueOf(whiteColor))
                binding.pageLabel2.setTextColor(whiteColor)
                ImageViewCompat.setImageTintList(binding.page3, ColorStateList.valueOf(whiteColor))
                binding.pageLabel3.setTextColor(whiteColor)
                ImageViewCompat.setImageTintList(binding.page4, ColorStateList.valueOf(whiteColor))
                binding.pageLabel4.setTextColor(whiteColor)
            }
        }
    }
}