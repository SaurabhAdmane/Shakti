package com.shakticoin.app.registration

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.shakticoin.app.api.RemoteException
import com.shakticoin.app.api.otp.EmailOTPRepository
import com.shakticoin.app.databinding.FragmentRegVerifyEmailBinding

class RegVerifyEmailFragment : Fragment() {
    private var binding: FragmentRegVerifyEmailBinding? = null
    private var viewModel: RegViewModel? = null
    private var taskPollStatus : PollEmailStatus? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegVerifyEmailBinding.inflate(inflater, container, false)
        binding?.lifecycleOwner = this
        viewModel = activity?.let { ViewModelProviders.of(it).get(RegViewModel::class.java) }
        return binding?.root
    }

    override fun onPause() {
        // cancel background task that poll email address verification status
        taskPollStatus?.cancel(true)
        taskPollStatus = null

        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        // notify any possible observers about the current step
        viewModel?.currentStep?.value = RegViewModel.Step.VerifyEmail

        // start polling the status of the email address verification
        taskPollStatus = PollEmailStatus()
        taskPollStatus?.execute(viewModel?.emailAddress?.value)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    /**
     * The task ask the OTP service about the status of the email address in a loop and
     * stops when the address is verified.
     */
    inner class PollEmailStatus : AsyncTask<String, Void, Boolean?>() {
        val repository : EmailOTPRepository = EmailOTPRepository()

        override fun doInBackground(vararg params: String?): Boolean? {
            while(true) {
                if (isCancelled) break
                try {
                    if (params.size > 0) {
                        val emailAddress = params[0]
                        emailAddress?.let {
                            val isVerified: Boolean? = repository.getEmailStatus(emailAddress)
                            if (isCancelled) return null
                            if (isVerified == null) return null
                            if (isVerified) {
                                return true
                            }
                        }
                    } else {
                        break;
                    }
                } catch (e: RemoteException) {
                    return null
                }
            }
            return null;
        }

        override fun onPostExecute(result: Boolean?) {
            if (result != null && result) {
                (activity as RegActivity).onEnterPhone(null)
            }
        }

    }
}

