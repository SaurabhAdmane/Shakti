package com.shakticoin.app.registration

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shakticoin.app.R
import com.shakticoin.app.api.OnCompleteListener
import com.shakticoin.app.api.RemoteMessageException
import com.shakticoin.app.api.Session
import com.shakticoin.app.api.onboard.OnboardRepository
import com.shakticoin.app.api.otp.EmailOTPRepository
import com.shakticoin.app.api.otp.PhoneOTPRepository
import com.shakticoin.app.databinding.ActivitySignupOtpBinding
import com.shakticoin.app.util.CommonUtil
import com.shakticoin.app.util.Debug

class SignUpOTPActivity : AppCompatActivity() {
    var binding: ActivitySignupOtpBinding? = null

    private var emailAddress: String? = null
    private var phoneNumber: String? = null
    private var password: String? = null

    private val emailOTPRepository = EmailOTPRepository()
    private val phoneOTPRepository = PhoneOTPRepository()
    private val onboardRepository = OnboardRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupOtpBinding.inflate(layoutInflater)
        setContentView(binding?.getRoot())

        emailAddress = intent.getStringExtra(CommonUtil.prefixed("emailAddress", this))
        phoneNumber = intent.getStringExtra(CommonUtil.prefixed("phoneNumber", this))
        password = intent.getStringExtra(CommonUtil.prefixed("password", this))

        binding?.phoneOtp?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onMainAction(null)
            }
            false
        }

        if (!TextUtils.isEmpty(emailAddress)) {
            sendEmaiOTPRequest()
        }
        if (!TextUtils.isEmpty(phoneNumber)) {
            sendPhoneOTPRequest()
        }
    }

    private fun sendEmaiOTPRequest() {
        val activity = this
        emailOTPRepository.requestRegistration(emailAddress!!, object : OnCompleteListener<Void?>() {
            override fun onComplete(value: Void?, error: Throwable?) {
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show()
                    return
                }
            }
        })
    }

    private fun sendPhoneOTPRequest() {
        val activity = this
        phoneOTPRepository.requestRegistration(phoneNumber!!, object : OnCompleteListener<Void?>() {
            override fun onComplete(value: Void?, error: Throwable?) {
                if (error != null) {
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show()
                    return
                }
            }
        })
    }

    fun onMainAction(v: View?) {
        val otpCode = binding!!.phoneOtp.text.toString()
        if (TextUtils.isEmpty(otpCode)) {
            binding!!.phoneOtpLayout.setError(getString(R.string.otp_smscode_required))
            return
        }
        val activity: Activity = this
        binding!!.progressBar.visibility = View.VISIBLE
        // confirm mobile phone number
        phoneOTPRepository.confirmRegistration(phoneNumber!!, otpCode, object : OnCompleteListener<Boolean?>() {
            override fun onComplete(verificationResult: Boolean?, error: Throwable?) {
                if (error != null) {
                    binding!!.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(activity, Debug.getFailureMsg(activity, error), Toast.LENGTH_LONG).show()
                    return
                }

                // repository never return false value, just in case
                if (verificationResult != null && !verificationResult) {
                    binding!!.progressBar.visibility = View.INVISIBLE
                    return
                }

                // create new shakti ID
                onboardRepository.addUser(emailAddress!!, phoneNumber!!, password!!, object : OnCompleteListener<String?>() {
                    override fun onComplete(shaktiId: String?, error: Throwable?) {
                        binding!!.progressBar.visibility = View.INVISIBLE
                        if (error != null) {
                            val errMsg = if (error is RemoteMessageException) error.message else Debug.getFailureMsg(activity, error)
                            Toast.makeText(activity, errMsg, Toast.LENGTH_LONG).show()
                            return
                        }

                        // redirect the new user to log in
                        Toast.makeText(activity, R.string.otp__success, Toast.LENGTH_LONG).show()
                        startActivity(Session.unauthorizedIntent(activity));
                    }
                })
            }
        })
    }
}