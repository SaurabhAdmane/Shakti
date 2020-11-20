package com.shakticoin.app.api.kyc

class KycUserView {
    var walletID: String? = null
    var walletBytes: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var middleName: String? = null
    var fullName: String? = null
    var dob: String? = null
    var referenceEmail: String? = null
    var referenceMobile: String? = null
    var address: AddressModel? = null
    var primaryEmail: String? = null
    var primaryMobile: String? = null
    var secondaryEmail: String? = null
    var secondaryMobile: String? = null
    var relationshipStatus: String? = null
    var education1: String? = null
    var education2: String? = null
    var deviceUDID: String? = null
    var emailAlert: Boolean? = null
    var occupation: String? = null
    var utilities: List<KycDocument>? = null
    var financials: List<KycDocument>? = null
    var authorities: List<KycDocument>? = null
    var fastTrack: Boolean? = null
    var subscriptionId: String? = null
    var paymentStatus: String? = null
    var verificationStatus: String? = null
    var verificationComments: String? = null
    var kycStatus: String? = null
    var digitalNation: String? = null
    var micro: String? = null
    var age: String? = null
    var usValidation: String? = null


    companion object {
        const val REL_SINGLE = "SINGLE"
        const val REL_MARRIED = "MARRIED"
        const val REL_WINDOW = "WINDOW"
        const val REL_DIVORCED = "DIVORCED"

        const val PMNT_INITIATED = "INITIATED"
        const val PMNT_SUCCESS = "SUCCESS"
        const val PMNT_FAILED = "FAILED"

        const val VERIF_INITIATED = "INITIATED"
        const val VERIF_SUCCESS = "SUCCESS"
        const val VERIF_REJECTED = "REJECTED"

        const val STATUS_LOCKED = "LOCKED"
        const val STATUS_UNLOCKED = "UNLOCKED"
    }
}