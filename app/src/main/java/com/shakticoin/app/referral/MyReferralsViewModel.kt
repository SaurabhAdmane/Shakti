package com.shakticoin.app.referral

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyReferralsViewModel : ViewModel() {

    val bountyId : MutableLiveData<String> = MutableLiveData()
    val remainingMonths : MutableLiveData<Int> = MutableLiveData(0)
    val sxeBalance : MutableLiveData<Int> = MutableLiveData()

}