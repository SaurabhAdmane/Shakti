package com.shakticoin.app.api

/**
 * Convenience class to obtain different URL for debug and release modes.
 */
object BaseUrl {
    /** Possible values "-stg", "-qa", and empty string  */
    private const val ENV_CODE = "-stg"

    const val BASE_URL = "https://dev-api.shakticoin.com/"
    const val IAM_BASE_URL = "https://iam${ENV_CODE}.shakticoin.com/oxauth/restv1/"
    const val WALLETSERVICE_BASE_URL = "https://walletservice${ENV_CODE}.shakticoin.com/walletservice/api/v1/"
    const val LICENSESERVICE_BASE_URL = "https://licenseservice${ENV_CODE}.shakticoin.com/license-service/api/v1/"
    const val KYC_USER_SERVICE_BASE_URL = "https://kycuser${ENV_CODE}.shakticoin.com/kyc-user-service/api/v1/"
    const val KYC_CORP_SERVICE_BASE_URL = "https://kyccorpservice${ENV_CODE}.shakticoin.com/???"
    const val PHONE_OTP_SERVICE_BASE_URL = "https://mobileotpservice${ENV_CODE}.shakticoin.com/sms-otp-service/api/v1/"
    const val EMAIL_OTP_SERVICE_BASE_URL = "https://emailotpservice${ENV_CODE}.shakticoin.com/email-otp-service/api/v1/"
    const val ONBOARD_SERVICE_BASE_URL = "https://onboardshakti${ENV_CODE}.shakticoin.com/onboardshakti-service/api/v1/"
}