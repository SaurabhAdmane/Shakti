package com.shakticoin.app.api.kyc

class KycDocument {
    var category: String? = null
    var contentType: String? = null
    var filename: String? = null
    var size: Int? = null
    var documentId: String? = null

    companion object {
        const val UTILITY = "UTILITY"
        const val FINANCIAL = "FINANCIAL"
        const val AUTHORITY = "AUTHORITY"
    }
}