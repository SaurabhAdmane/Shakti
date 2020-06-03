package com.shakticoin.app.api.license

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
class LicenseType {
    var id: String? = null
    var planCode: String? = null
    var licName: String? = null
    var licFeatures: List<String>? = null
    var price: Double? = null
    var cycle: Int? = null
    var bonus: String? = null
    var licCategory: String? = null
    var orderNumber: Int? = null
}