package com.shakticoin.app.api.license

import android.os.Parcel
import android.os.Parcelable

val MINING_PLANS: List<String> = listOf("M101", "T100", "T200", "T300", "T400")

/** Subscription periods - week, month, and year. */
enum class MiningLicenseCycle {
    W, M, Y
}

class NodeOperatorUpdateModel {
    var nodeID: String? = null
    var walletID: String? = null
    var bizVaultID: String? = null
    var vanityID1: String? = null
    var vanityID2: String? = null
    var brandID: String? = null
    var email: String? = null
    var mobileNumber: String? = null
}

class NodeOperatorModel {
    var nodeID: String? = null
    var walletID: String? = null
    var bizVaultID: String? = null
    var vanityID1: String? = null
    var vanityID2: String? = null
    var brandID: String? = null
    var email: String? = null
    var mobileNumber: String? = null
    var address: AddressModel? = null
    var subscribedLicenses: List<SubscribedLicenseModel>? = null
}

class CheckoutModel {
    var planCode: String? = null
    var address: AddressModel? = null
}

class CheckoutResponse {
    var status: Boolean? = null
    var message: String? = null
    var hostedpage: String? = null
}

class AddressModel {
    var country: String? = null
    var countryCode: String? = null
    var province: String? = null
    var city: String? = null
    var street: String? = null
    var zipCode: String? = null
}

class PlanCodeRequest {
    var planCode: String? = null
}

class CheckoutPlanRequest {
    var planCode: String? = null
    var subscriptionId: String? = null
    var userName: String? = null
    var guest: Boolean = false
}

class SubscribedLicenseModel() : Parcelable {
    var planCode: String? = null
    var subscriptionId: String? = null
    /** Value is one of ACTION_ constants */
    var action: String? = null
    /** Value is on of PMNT_ constants */
    var paymentStatus: String? = null
    var dateOfPurchase: Long? = null

    constructor(parcel: Parcel) : this() {
        planCode = parcel.readString()
        subscriptionId = parcel.readString()
        action = parcel.readString()
        paymentStatus = parcel.readString()
        dateOfPurchase = parcel.readValue(Long::class.java.classLoader) as? Long
    }

    val planType : String? get() = planCode?.substring(0..3);

    override fun describeContents(): Int {
        return 0;
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(planCode)
        parcel.writeString(subscriptionId)
        parcel.writeString(action)
        parcel.writeString(paymentStatus)
        parcel.writeValue(dateOfPurchase)
    }

    companion object CREATOR : Parcelable.Creator<SubscribedLicenseModel> {
        const val ACTION_INITIATED = "INITIATED"
        const val ACTION_JOINED = "JOINED"
        const val ACTION_UPGRADED = "UPGRADED"
        const val ACTION_DOWNGRADED = "DOWNGRADED"
        const val ACTION_RENEWED = "RENEWED"
        const val ACTION_CANCELLED = "CANCELLED"
        const val ACTION_EXPIRED = "EXPIRED"

        const val PMNT_INITIATED    = "INITIATED"
        const val PMNT_SUCCESS      = "SUCCESS"
        const val PMNT_FAILED       = "FAILED"

        override fun createFromParcel(parcel: Parcel): SubscribedLicenseModel {
            return SubscribedLicenseModel(parcel)
        }

        override fun newArray(size: Int): Array<SubscribedLicenseModel?> {
            return arrayOfNulls(size)
        }
    }
}