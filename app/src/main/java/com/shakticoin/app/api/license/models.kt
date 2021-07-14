package com.shakticoin.app.api.license

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

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
    var ipAddress: String? = null
    var port: String? = null
    var nodeName: String? = null
    var publicKey: String? = null
    var walletID: String? = null
    var bizVaultID: String? = null
    var email: String? = null
    var mobileNumber: String? = null
    var guest: Boolean = false
    var nodeID: String? = null
    var vanityID1: String? = null
    var vanityID2: String? = null
    var brandID: String? = null
    var address: AddressModel? = null
    var activeLicense: ActiveLicenseModel? = null
    var subscribedLicenses: List<SubscribedLicenseModel>? = null
    var occupiedLicenses: List<SubscribedLicenseModel>? = null
}

class ActiveLicenseModel{
    var subscriptionId:String? = null
    var planCode:String? = null
    var action:String? = null
    var active:Boolean = false
    var paymentStatus:String? = null
    var dateOfPurchase:Long? = 0
    var licensePurchasedEmail:String? = null
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
}

class SubscribedLicenseModel() : Parcelable {
    var planCode: String? = null
    var subscriptionId: String? = null
    var active: Boolean = false
    /** Value is one of ACTION_ constants */
    var action: String? = null
    /** Value is on of PMNT_ constants */
    var paymentStatus: String? = null
    var dateOfPurchase: Long? = null
    var consumedEmail: String? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this() {
        planCode = parcel.readString()
        subscriptionId = parcel.readString()
        active = parcel.readBoolean()
        action = parcel.readString()
        paymentStatus = parcel.readString()
        dateOfPurchase = parcel.readValue(Long::class.java.classLoader) as? Long
        consumedEmail = parcel.readString()
    }

    val planType : String? get() = planCode?.substring(0..3);

    override fun describeContents(): Int {
        return 0;
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(planCode)
        parcel.writeString(subscriptionId)
        parcel.writeBoolean(active)
        parcel.writeString(action)
        parcel.writeString(paymentStatus)
        parcel.writeValue(dateOfPurchase)
        parcel.writeString(consumedEmail)
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

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): SubscribedLicenseModel {
            return SubscribedLicenseModel(parcel)
        }

        override fun newArray(size: Int): Array<SubscribedLicenseModel?> {
            return arrayOfNulls(size)
        }
    }
}

data class GeoResponse (val list: List<Map<String, String>>)

data class Country (val countryCode: String, val country: String) {
    override fun toString(): String {
        return country;
    }
}

data class Subdivision (val provinceCode: String?, val province: String) {
    override fun toString(): String {
        return province
    }
}

data class City(val city: String) {
    override fun toString(): String {
        return city
    }
}