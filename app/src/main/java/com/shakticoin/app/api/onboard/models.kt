package com.shakticoin.app.api.onboard

class OnboardShaktiModel {
    var countryCode: String? = null
    var mobileNo: String? = null
    var deviceId: String? = null
    var email: String? = null
    var geojson: GeoJSONModel? = null
    var ipaddress: String? = null
    var password: String? = null
    var authorizationBytes: String? = "dev" // blank until further directions
}

class UpdatePasswordModel {
    var currentPassword: String? = null
    var newPassword: String? = null
}

class WalletRequest {
    var authorizationBytes: String = ""
    var passphrase: String? = null
    var walletBytes: String? = null
    var cacheBytes: String? = null
    
    var addressNumber: Int? = null
    var sessionToken: String? = null

    var timestamp: String? = null



}

class GeoJSONModel {
    var latitude: Double? = null
    var longitude: Double? = null
}

class ResponseVault {
    var details : Response = Response()
    var message : String? = null
    var path : String? = null
    var status : Int? = null
    var timestamp : String? = null
    var sessionToken : String? = null
    var walletBalance : String? = null
}

class Response {
    var bizvaultIdStatus : Boolean = false
}

class ResponseBean {
    var details : Map<String, Object>? = null
    var message : String? = null
    var path : String? = null
    var status : Int? = null
    var timestamp : String? = null
    var sessionToken : String? = null
    var walletBalance : String? = null
    var lockedGenesisBounty : String? = null
}