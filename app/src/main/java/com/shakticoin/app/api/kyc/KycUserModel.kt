package com.shakticoin.app.api.kyc

enum class RelationshipStatus {
    SINGLE, MARRIED, WINDOW, DIVORCED
}

class KycUserModel {
    var firstName: String? = null
    var lastName: String? = null
    var middleName: String? = null
    var fullName: String? = null
    var dob: String? = null
    var referenceEmail: String? = null
    var referenceMobile: String? = null
    var address: AddressModel? = null
    var secondaryEmail: String? = null
    var secondaryMobile: String? = null
    var relationshipStatus: String? = null
    var relation: RelationModel? = null
    var education1: String? = null
    var education2: String? = null
    var deviceUDID: String? = null
    var emailAlert: Boolean = false
    var occupation: String? = null
}
