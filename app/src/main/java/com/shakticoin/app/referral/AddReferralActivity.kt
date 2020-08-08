package com.shakticoin.app.referral

import android.app.Activity
import android.os.Bundle
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.shakticoin.app.R
import com.shakticoin.app.api.OnCompleteListener
import com.shakticoin.app.api.bounty.BountyReferralData
import com.shakticoin.app.api.bounty.BountyRepository
import com.shakticoin.app.api.bounty.CommonReferralResponseViewModel
import com.shakticoin.app.databinding.ActivityAddReferralBinding
import com.shakticoin.app.util.Debug
import com.shakticoin.app.util.Validator
import com.shakticoin.app.widget.DrawerActivity
import com.shakticoin.app.widget.MessageBox

class AddReferralActivity : DrawerActivity() {
    private lateinit var binding: ActivityAddReferralBinding
    private lateinit var bountyRepository: BountyRepository
    private var viewModel: AddReferralViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_referral)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(AddReferralViewModel::class.java)
        binding.viewModel = viewModel


        binding.emailFieldLayout.setValidator { _: EditText?, value: String? -> Validator.isEmail(value) }
        binding.phoneFieldLayout.setValidator { _: EditText?, value: String? -> Validator.isPhoneNumber(value) }

        onInitView(binding.root, getString(R.string.my_refs_title), true)

        bountyRepository = BountyRepository()
        bountyRepository.setLifecycleOwner(this)

        val activity: Activity = this
        binding.phoneNumber.setOnEditorActionListener { v: TextView, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                val ims = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                ims.hideSoftInputFromWindow(v.windowToken, 0)
                onAddReferral(null)
                return@setOnEditorActionListener true
            }
            false
        }

        viewModel!!.remainingMonths.observe(this, Observer<Int?> { value ->
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                binding.textWaitToUnlock.text = Html.fromHtml(getString(R.string.my_refs_tounlock, value), Html.FROM_HTML_MODE_LEGACY)
            } else {
                binding.textWaitToUnlock.text = Html.fromHtml(getString(R.string.my_refs_tounlock, value))
            }
        })
    }

    override fun onResume() {
        super.onResume()

        val activity: Activity = this
        binding.progressBar.visibility = View.VISIBLE
        bountyRepository.getBounties(object : OnCompleteListener<BountyReferralData?>() {
            override fun onComplete(value: BountyReferralData?, error: Throwable?) {
                binding.progressBar.visibility = View.INVISIBLE
                if (error != null) {
                    MessageBox(Debug.getFailureMsg(activity, error)).show(supportFragmentManager, null)
                    return
                }
                viewModel!!.remainingMonths.value = value?.remainingMonths
                viewModel!!.balance.value = value?.lockedGenesisBounty
                viewModel!!.bountyId.value = value?.id
            }
        })
    }

    override fun getCurrentDrawerSelection(): Int {
        return 3
    }

    fun onHowToEarn(v: View?) {
        DialogHowToBonus.getInstance().show(supportFragmentManager, DialogHowToBonus.TAG)
    }

    fun onAddReferral(v: View?) {
        var validationSuccessful = true
        if (!Validator.isEmail(viewModel?.emailAddress?.value)) {
            validationSuccessful = false
            binding.emailFieldLayout.setError(getString(R.string.err_email_required))
        }
        if (!Validator.isPhoneNumber(viewModel?.phoneNumber?.value)) {
            validationSuccessful = false
            binding.phoneFieldLayout.setError(getString(R.string.err_phone_required))
        }
        if (validationSuccessful) {
            val activity: Activity = this
            binding.progressBar.visibility = View.VISIBLE
            bountyRepository.addReferral(viewModel?.bountyId?.value!!, viewModel?.emailAddress?.value,
                    viewModel?.phoneNumber?.value, object : OnCompleteListener<CommonReferralResponseViewModel?>() {
                override fun onComplete(value: CommonReferralResponseViewModel?, error: Throwable?) {
                    binding.progressBar.visibility = View.INVISIBLE
                    if (error != null) {
                        MessageBox(Debug.getFailureMsg(activity, error)).show(supportFragmentManager, null)
                        return;
                    }

                    if (value?.message != null) {
                        MessageBox(value.message).show(supportFragmentManager, null)
                    }
                }

            })
        }
    }
}

class AddReferralViewModel : ViewModel() {
    val phoneNumber: MutableLiveData<String> = MutableLiveData()
    val emailAddress: MutableLiveData<String> = MutableLiveData()
    val remainingMonths : MutableLiveData<Int> = MutableLiveData(0)
    val balance : MutableLiveData<Int> = MutableLiveData()
    val bountyId : MutableLiveData<String> = MutableLiveData()
}