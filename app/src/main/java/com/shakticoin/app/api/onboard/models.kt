package com.shakticoin.app.api.onboard

class OnboardShaktiUserModel {
    var countryCode: String? = null
    var deviceId: String? = null
    var email: String? = null
    var emailVerified: Boolean? = null
    var geojson: GeoJSONModel? = null
    var ipaddress: String? = null
    var mobileNo: String? = null
    var mobileVerified: Boolean? = null
    var password: String? = null
    var shaktiID: String? = null
}

class UpdatePasswordModel {
    var currentPassword: String? = null
    var newPassword: String? = null
}

class WalletRequest {
    var authorizationBytes: String = ""
    var passphrase: String? = null
}

class GeoJSONModel {
    var latitude: Double? = null
    var longitude: Double? = null
}

class ResponseBean {
    var details : Map<String, Object>? = null
    var message : String? = null
    var path : String? = null
    var status : Int? = null
    var timestamp : String? = null
}