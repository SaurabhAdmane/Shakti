package com.shakticoin.app.api

/**
 * Convenience class to obtain different URL for debug and release modes.
 */
object BaseUrl {
    /** Possible values "-stg", "-qa", and empty string  */
    private const val ENV_CODE = "-dev"

//    const val IAM_BASE_URL = "https://iam${ENV_CODE}.shakticoin.com/oxauth/restv1/"

    //const val WALLETSERVICE_BASE_URL = "https://walletservice${ENV_CODE}.shakticoin.com/walletservice/api/v1/"
//    const val LICENSESERVICE_BASE_URL =
//        "https://licenseservice${ENV_CODE}.shakticoin.com/license-service/api/v1/"
//    const val KYC_USER_SERVICE_BASE_URL =
//        "https://kycuser${ENV_CODE}.shakticoin.com/kyc-user-service/api/v1/"
    const val SELF_ID_SERVICE_BASE_URL =
        "https://selfyid${ENV_CODE}.shakticoin.com/selfyid-service/api/v1/"

//    https://kycuser-stg.shakticoin.com/kyc-user-service/api/v1/ kyc/wallet
//    https://selfyid-stg.shakticoin.com/selfyid-service/api/v1/ selfyid/walletId

    const val KYC_CORP_SERVICE_BASE_URL = "https://kyccorpservice${ENV_CODE}.shakticoin.com/???"

    //    const val PHONE_OTP_SERVICE_BASE_URL = "https://mobileotpservice${ENV_CODE}.shakticoin.com/sms-otp-service/api/v1/"
//    const val EMAIL_OTP_SERVICE_BASE_URL = "https://emailotpservice${ENV_CODE}.shakticoin.com/email-otp-service/api/v1/"
//    const val ONBOARD_SERVICE_BASE_URL =
//        "https://onboardshakti${ENV_CODE}.shakticoin.com/onboardshakti-service/api/v1/"
//    const val BOUNTY_SERVICE_BASE_URL =
//        "https://referral${ENV_CODE}.shakticoin.com/bountyservice/api/v1/"


//    const val BIZVAULT_SERVICE_BASE_URL =
//        "https://bizvault${ENV_CODE}.shakticoin.com/bizvault/api/v1/bizvaults/"

//------------------------------------------------------------------------------------------------------------------

//------------------------------------------------------------------------------------------------------------------

    /** BELOW ARE NEW BASE URL*/

    const val BASE = "gatewayservice" //gatewayservice
    const val IAM_BASE_URL = "https://iam${ENV_CODE}.shakticoin.com/oxauth/restv1/"
    const val WALLETSERVICE_BASE_URL =
        "https://${BASE+ENV_CODE}.shakticoin.com/wallet/api/v2/"
    const val EMAIL_OTP_SERVICE_BASE_URL =
        "https://${BASE + ENV_CODE}.shakticoin.com/email/api/v2/"
//        "https://emailservice${ENV_CODE}.shakticoin.com/email/api/v2/"
    const val PHONE_OTP_SERVICE_BASE_URL =
        "https://${BASE + ENV_CODE}.shakticoin.com/sms/api/v2/"
    const val BOUNTY_SERVICE_BASE_URL =
        "https://${BASE + ENV_CODE}.shakticoin.com/bounty-referral/api/v2/"
    const val BIZVAULT_SERVICE_BASE_URL =
        "https://${BASE + ENV_CODE}.shakticoin.com/bizvault/api/v2/"
    const val KYC_USER_SERVICE_BASE_URL =
        "https://${BASE + ENV_CODE}.shakticoin.com/kyc-user/api/v2/"
    const val LICENSESERVICE_BASE_URL =
        "https://${BASE + ENV_CODE}.shakticoin.com/license/api/v2/"
    const val ONBOARD_SERVICE_BASE_URL =
        "https://${BASE + ENV_CODE}.shakticoin.com/user-management/api/v2/"
}