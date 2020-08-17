package com.shakticoin.app.api.license

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
    var planCode: String? = null
    var address: AddressModel? = null
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

class SubscribedLicenseModel {
    var planCode: String? = null
    var subscriptionId: String? = null
    /** Value is one of ACTION_ constants */
    var action: String? = null
    /** Value is on of PMNT_ constants */
    var paymentStatus: String? = null

    companion object {
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
    }
}