package com.shakticoin.app.api.bounty

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

data class CommonReferralModel(val email: String? = null, val mobileNumber: String? = null)

class CommonReferralResponseViewModel {
    var data: Map<String, Any>? = null
    var message: String? = null
}

data class RegisterReferralModel(
        val emailOrMobile: String? = null,
        val emailRegistration: Boolean? = null,
        val promotionalCode: String? = null)

data class RegisterReferralResponseModel(
    var isReferralRegisterSuccessful: Boolean? = null,
    var message: String? = null)

class ProcessingStatusListResponseViewModel {
    var data: Map<String, Any>? = null
}

class EffortStatusListResponseViewModel {
    var data: Map<String, Any>? = null
}

class BountyGrantOptionHistory {
    var startDate: Long? = null
    var bountyGrantOption: String? = null
}

class BountyGrantOptionInfoModel(val bountyGrantOption: String) {
    companion object {
        const val WaitForNMonths = "WaitForNMonths"
        const val ReferNParticipents = "ReferNParticipents"
        const val ReferAndEarnInstantBounty = "ReferAndEarnInstantBounty"
    }
}

class BountyGrantOptionInfoViewModel {
    var data: BountyReferralData? = null
    var message: String? = null
}

class BountyReferralData {
    /** Unique genesis bonus bounty id */
    var id: String? = null
    var shaktiID: String? = null
    /** Walllet type. Example: PERSONAL */
    var walletType: String? = null
    /** Wallet owner birthdate, e.g. 2020-01-10T08:20:10+05:30 */
    var walletOwnerAnniversary: String? = null
    /** Max allocated bounty for the wallet/bizVault */
    var promisedGenisisBounty: Int? = null
    /** Initially it will be same as promisedGenisisBounty and on every successfully referral,
     * it will be deducted */
    var lockedGenesisBounty: Int? = null
    var successfulReferralCount: Int? = null
    /** example: genesis_bounty_offer_type:2cd03ce1-03bc-403c-af8b-931ce0909d7f */
    var offerID: String? = null
    /** Selected offer id
     * Remaining months to unlock the bonus bounty amount */
    var remainingMonths: Int? = null
    var bountyGrantOptionHistory: List<BountyGrantOptionHistory>? = null
}

class BountyReferralViewModel {
    var data: BountyReferralData? = null
    var message: String? = null
}

data class GenesisBonusBountyCreateModel(val offerID: String, val dob: String? = null)

data class InfluenceModel(
    val currentInfluencedCount: Int,
    // see constants in PromotionalCodeModel
    val media: String
)

data class PromotionalCodeModel(val media: String, val url: String = PROMO_URL) {

    companion object {
        const val EMAIL = "EMAIL"
        const val PHONE_NUMBER = "PHONE_NUMBER"
        const val FACEBOOK = "FACEBOOK"
        const val TWITTER = "TWITTER"
        const val INSTAGRAM = "INSTAGRAM"
    }
}

data class PromotionalCodeResponseViewModel (
    /**
     * Promotion code
     * example: OxdxfoTj
     */
    var promotionalcode: String? = null,
    /**
     * Referral link for a specific sharing media for a specific user
     * example: http://shakti-share.com/code=OxdxfoTj
     */
    var promotionalcodeurl: String? = null)

class ProgressingResponseViewModel {
    var isUpdateProgressingSuccessful : Boolean? = null
    var message : String? = null
}

class ProgressingStatusListResponseViewModel {
    var data : Map<String, Any>? = null
}

class TransactionModel {
    /** It is date in EpochMilli format */
    var lastActiveTransaction : Long? = null
}

class TransactionResponseModel {
    var message : String? = null
}

class BirthdayModel {
    /**
     * Date with timezone.
     * example: 1989-01-10T08:20:10+05:30
     */
    var walletOwnerAnniversary : String? = null
}

class UpdateWalletTypeModel {
    var walletType : String? = null

    companion object {
        const val PERSONAL = "PERSONAL"
        const val BUSINESS = "BUSINESS"
    }
}

class OfferModel {
    /**
     * Unique genesis bonus bounty id.
     * example: genesis_bonus_bounty:d0b13295-3676-4c67-a1ab-648b32f8747d
     */
    lateinit var id : String

    /**
     * Offer expiry date.
     * example: 2021-04-10
     */
    var expiryDate : String? = null

    var shaktiCoins : Long? = null

    var dollarAmount : Long? = null

    /** Show whether the offer is expired or not */
    var expired : Boolean = false

    fun formatUSD(): String? {
        if (dollarAmount != null) {
            val fmt = NumberFormat.getCurrencyInstance(Locale.US)
            val formattedPrice = fmt.format(dollarAmount!!)
            return formattedPrice.substring(0, formattedPrice.indexOf("."))
        }
        return null
    }

    fun formatSXE(): String? {
        if (shaktiCoins != null) {
            return String.format(Locale.US, "%1$.2f SXE", shaktiCoins?.toDouble());
        }
        return null
    }

    fun formatDate(): String? {
        if (expiryDate != null) {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val calendar = Calendar.getInstance()
            val date = formatter.parse(expiryDate!!)
            if (date != null) {
                calendar.time = date
                var ending = "th"
                val lastDigit = calendar.get(Calendar.DAY_OF_MONTH) % 10;
                when (lastDigit) {
                    1 -> ending = "st";
                    2 -> ending = "nd";
                    3 -> ending = "rd";
                }
                return String.format(Locale.US, "%1\$tb %1td%2\$s", calendar, ending);
            }
        }
        return null
    }

    fun formatYear(): String? {
        if (expiryDate != null) {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val calendar = Calendar.getInstance()
            val date = formatter.parse(expiryDate!!)
            if (date != null) {
                calendar.time = date
                return calendar.get(Calendar.YEAR).toString()
            }
        }
        return null
    }
}

class OfferResponseViewModel {
    var data : OfferModel? = null
    var message : String? = null
}

data class OfferListResponseViewModel(var data : List<OfferModel>? = null)

class QRCodeResponseViewModel {
    /**
     * Base64 of QR code image.
     * example: data:image/jpeg;base64,/9j/4AAQSkZJRgABAgAAAQ
     */
    var data : String? = null
    var message : String? = null
}