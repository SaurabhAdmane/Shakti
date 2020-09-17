package com.shakticoin.app.api.license

import android.os.Parcel
import android.os.Parcelable

/**
 * API returns:
 * {
 *   "id":"licensetype:03548c90-f310-45c8-8767-4a2adecf8a7d",
 *   "_id":"licensetype:ded82f8e-f0b4-4028-801f-bca4b628bfe6",
 *   "_type":"licensetype",
 *   "_rev":"0.1",
 *   "_fkeys":[],
 *   "_ts":1589396651549,
 *   "creator":null,
 *   "lastModifiedBy":null,
 *   "lastModification":0,
 *   "creationDate":1591090734723,
 *   "planCode":"M101Y", // note last character: Y - year, M - month, W - week
 *   "planType":"M101", "T100", "T200", "T300", "T400"
 *   "modeType":"Y" or M or W - basically last letter of the planCode
 *   "licName":"M101 License",
 *   "licFeatures":[
 *     "Individual Mining License",
 *     "Mine SXE Coins from home or wherever you work from.",
 *     "Priority upgrade option to Power Mining License."
 *   ],
 *   "price":49.95,
 *   "cycle":1, // maybe 1, 12, or 52
 *   "additionalPay":0,
 *   "bonus":"Get an extra SXE 1,000 bonus bounty - that is $5,000 USD",
 *   "licCategory":"BASIC",
 *   "orderNumber":11
 *   },
 */
class LicenseType() : Parcelable {
    var id: String? = null
    var planCode: String? = null
    var planType: String? = null
    var modeType: String? = null
    var licName: String? = null
    var licFeatures: List<String>? = null
    var price: Double? = null
    var cycle: Int? = null
    var bonus: String? = null
    var licCategory: String? = null
    var orderNumber: Int? = null
    var additionalPay: Int? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        planCode = parcel.readString()
        planType = parcel.readString()
        modeType = parcel.readString()
        licName = parcel.readString()
        licFeatures = parcel.createStringArrayList()
        price = parcel.readValue(Double::class.java.classLoader) as? Double
        cycle = parcel.readValue(Int::class.java.classLoader) as? Int
        bonus = parcel.readString()
        licCategory = parcel.readString()
        orderNumber = parcel.readValue(Int::class.java.classLoader) as? Int
        additionalPay = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(planCode)
        parcel.writeString(planType)
        parcel.writeString(modeType)
        parcel.writeString(licName)
        parcel.writeStringList(licFeatures)
        parcel.writeValue(price)
        parcel.writeValue(cycle)
        parcel.writeString(bonus)
        parcel.writeString(licCategory)
        parcel.writeValue(orderNumber)
        parcel.writeValue(additionalPay)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LicenseType> {
        override fun createFromParcel(parcel: Parcel): LicenseType {
            return LicenseType(parcel)
        }

        override fun newArray(size: Int): Array<LicenseType?> {
            return arrayOfNulls(size)
        }
    }

}

/**
 * License types are arranged as M101, T100, T200, T300 and compared according to their
 * position in this list.
 */
fun compareLicenseType(type1: String?, type2: String?) : Int {
    if (type1 == null && type2 == null) return 0
    if (type1 == null) return -1;
    if (type2 == null) return 1;
    return  type1.compareTo(type2);
}