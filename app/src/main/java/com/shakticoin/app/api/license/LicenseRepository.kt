package com.shakticoin.app.api.license

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shakticoin.app.BuildConfig
import com.shakticoin.app.api.*
import com.shakticoin.app.api.auth.AuthRepository
import com.shakticoin.app.api.auth.TokenResponse
import com.shakticoin.app.api.kyc.KYCRepository
import com.shakticoin.app.api.kyc.KycUserView
import com.shakticoin.app.util.Debug
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class LicenseRepository : BackendRepository() {

    var httpLoggingInterceptor = HttpLoggingInterceptor()
    private val http = OkHttpClient.Builder()
        .readTimeout(120, TimeUnit.SECONDS)
        .addInterceptor(
            if (BuildConfig.DEBUG) httpLoggingInterceptor.setLevel(
                HttpLoggingInterceptor.Level.BODY
            ) else httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
        )
        .connectTimeout(120, TimeUnit.SECONDS)
        .build()

    private val licenseService: LicenseService = Retrofit.Builder()
        .baseUrl(BaseUrl.LICENSESERVICE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(http)
        .build()
        .create(LicenseService::class.java)

    private val authRepository = AuthRepository()
    private val kycRepository = KYCRepository()

    private var callLics: Call<List<LicenseType>?>? = null
    fun getLicenses(listener: OnCompleteListener<List<LicenseType>?>?) {
        getLicenses(listener, false)
    }

    private fun getLicenses(
        listener: OnCompleteListener<List<LicenseType>?>?,
        hasRecover401: Boolean = false
    ) {
        callLics = licenseService.getLicenses(Session.getAuthorizationHeader())
        callLics!!.enqueue(object : Callback<List<LicenseType>?> {
            override fun onResponse(
                call: Call<List<LicenseType>?>,
                response: Response<List<LicenseType>?>
            ) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val licenseTypes = response.body()
                    if (licenseTypes != null) {
                        // good to have them ordered properly
                        listener!!.onComplete(licenseTypes.sortedBy { it.orderNumber }, null)
                    } else listener!!.onComplete(listOf(), null)
                } else {
                    if (response.code() == 401) {
                        if (!hasRecover401) {
                            authRepository.refreshToken(
                                Session.getRefreshToken(),
                                object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(
                                        value: TokenResponse?,
                                        error: Throwable?
                                    ) {
                                        if (error != null) {
                                            listener!!.onComplete(null, error)
                                            return
                                        }
                                        getLicenses(listener, true)
                                    }
                                })
                        } else {
                            listener!!.onComplete(null, UnauthorizedException())
                            return
                        }
                    } else {
                        Debug.logErrorResponse(response)
                        listener!!.onComplete(
                            null,
                            RemoteException(response.message(), response.code())
                        )
                    }
                }
            }

            override fun onFailure(call: Call<List<LicenseType>?>, t: Throwable) {
                Debug.logException(t)
                listener!!.onComplete(null, t)
            }
        })
    }

    private var callGetInv: Call<ResponseBody?>? = null
    fun getInventory(
        country: String,
        stateProvince: String?,
        city: String?,
        listener: OnCompleteListener<List<Map<String, Any>>>
    ) {
        getInventory(country, stateProvince, city, listener, false)
    }

    private fun getInventory(
        country: String,
        stateProvince: String?,
        city: String?,
        listener: OnCompleteListener<List<Map<String, Any>>>,
        hasRecover401: Boolean
    ) {
        callGetInv = licenseService.getLicenseAvailable(
            Session.getAuthorizationHeader(),
            country,
            stateProvince,
            city
        )
        callGetInv!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val body = response.body()?.string()
                    Debug.logDebug(body)

                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(
                                    Session.getRefreshToken(),
                                    object : OnCompleteListener<TokenResponse?>() {
                                        override fun onComplete(
                                            value: TokenResponse?,
                                            error: Throwable?
                                        ) {
                                            if (error != null) {
                                                listener.onComplete(null, error)
                                                return
                                            }
                                            getInventory(
                                                country,
                                                stateProvince,
                                                city,
                                                listener,
                                                true
                                            )
                                        }
                                    })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                returnError(listener, t)
            }

        })
    }

    private var callNodeOp: Call<NodeOperatorModel?>? = null
    fun getNodeOperator(listener: OnCompleteListener<NodeOperatorModel?>) {
        getNodeOperator(listener, false)
    }

    private fun getNodeOperator(
        listener: OnCompleteListener<NodeOperatorModel?>,
        hasRecover401: Boolean
    ) {
        callNodeOp = licenseService.getNodeOperator(Session.getAuthorizationHeader())
        callNodeOp!!.enqueue(object : Callback<NodeOperatorModel?> {
            override fun onResponse(
                call: Call<NodeOperatorModel?>,
                response: Response<NodeOperatorModel?>
            ) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), null)
                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(
                                    Session.getRefreshToken(),
                                    object : OnCompleteListener<TokenResponse?>() {
                                        override fun onComplete(
                                            value: TokenResponse?,
                                            error: Throwable?
                                        ) {
                                            if (error != null) {
                                                listener.onComplete(null, error)
                                                return
                                            }
                                            getNodeOperator(listener, true)
                                        }
                                    })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<NodeOperatorModel?>, t: Throwable) {
                return returnError(listener, t)
            }

        })
    }

    fun checkoutSubscription(planCode: String?, listener: OnCompleteListener<String>) {
        checkoutSubscription(planCode, listener, false)
    }

    private fun checkoutSubscription(
        planCode: String?,
        listener: OnCompleteListener<String>,
        hasRecover401: Boolean
    ) {
        kycRepository.getUserDetails(object : OnCompleteListener<KycUserView>() {
            override fun onComplete(user: KycUserView?, error: Throwable?) {
                if (error != null) {
                    listener.onComplete(null, error)
                    return
                }

                val countryCode = user?.address?.countryCode
                val country = user?.address?.country
                val stateProvinceCode = user?.address?.stateProvinceCode

                getSubdivisionsByCountry(
                    countryCode!!,
                    object : OnCompleteListener<List<Subdivision>>() {
                        override fun onComplete(
                            stateProvinceList: List<Subdivision>?,
                            error: Throwable?
                        ) {
                            if (error != null) {
                                listener.onComplete(null, error)
                                return
                            }

                            var stateProvinceName: String? = null
                            if (stateProvinceCode != null && stateProvinceList != null && stateProvinceList.isNotEmpty()) {
                                for (stateProvince in stateProvinceList) {
                                    if (stateProvinceCode == stateProvince.provinceCode) {
                                        stateProvinceName = stateProvince.province
                                        break
                                    }
                                }
                            }

                            val parameters = CheckoutModel()
                            parameters.planCode = planCode
                            val address = AddressModel()
                            address.countryCode = countryCode
                            address.country = country
                            address.province = stateProvinceName ?: stateProvinceCode
                            address.city = user.address?.city
                            address.street = user.address?.address1
                            if (user.address?.address2 != null) address.street += (" " + user.address?.address2)
                            address.zipCode = user.address?.zipCode
                            parameters.address = address
                            licenseService.checkoutSubscription(
                                Session.getAuthorizationHeader(),
                                parameters
                            ).enqueue(object : Callback<CheckoutResponse?> {
                                override fun onResponse(
                                    call: Call<CheckoutResponse?>,
                                    response: Response<CheckoutResponse?>
                                ) {
                                    Debug.logDebug(response.toString())
                                    if (response.isSuccessful) {
                                        val resp = response.body()
                                        if (resp != null) Debug.logDebug(resp.message)
                                        listener.onComplete(resp?.hostedpage, null)
                                    } else {
                                        when (response.code()) {
                                            401 -> {
                                                if (!hasRecover401) {
                                                    authRepository.refreshToken(
                                                        Session.getRefreshToken(),
                                                        object :
                                                            OnCompleteListener<TokenResponse?>() {
                                                            override fun onComplete(
                                                                value: TokenResponse?,
                                                                error: Throwable?
                                                            ) {
                                                                if (error != null) {
                                                                    listener.onComplete(null, error)
                                                                    return
                                                                }
                                                                checkoutSubscription(
                                                                    planCode,
                                                                    listener,
                                                                    true
                                                                )
                                                            }
                                                        })
                                                } else {
                                                    listener.onComplete(
                                                        null,
                                                        UnauthorizedException()
                                                    )
                                                    return
                                                }
                                            }
                                            404 -> listener.onComplete(
                                                null,
                                                RemoteException(
                                                    getResponseErrorMessage(
                                                        "message",
                                                        response.errorBody()
                                                    ), response.code()
                                                )
                                            )
                                            else -> return returnError(listener, response)
                                        }
                                    }
                                }

                                override fun onFailure(
                                    call: Call<CheckoutResponse?>,
                                    t: Throwable
                                ) {
                                    return returnError(listener, t)
                                }
                            })
                        }
                    })

            }
        })
    }

    private var callUpgdSub: Call<CheckoutResponse?>? = null
    fun upgradeSubscription(
        planCode: String,
        subscriptionId: String,
        listener: OnCompleteListener<String>
    ) {
        upgradeSubscription(planCode, subscriptionId, listener, false)
    }

    private fun upgradeSubscription(
        planCode: String,
        subscriptionId: String,
        listener: OnCompleteListener<String>,
        hasRecover401: Boolean
    ) {
        val parameters = CheckoutPlanRequest()
        parameters.planCode = planCode
        parameters.subscriptionId = subscriptionId
        callUpgdSub = licenseService.checkoutUpgrade(Session.getAuthorizationHeader(), parameters)
        callUpgdSub!!.enqueue(object : Callback<CheckoutResponse?> {
            override fun onResponse(
                call: Call<CheckoutResponse?>,
                response: Response<CheckoutResponse?>
            ) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) Debug.logDebug(resp.message)
                    listener.onComplete(resp?.hostedpage, null)

                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(
                                    Session.getRefreshToken(),
                                    object : OnCompleteListener<TokenResponse?>() {
                                        override fun onComplete(
                                            value: TokenResponse?,
                                            error: Throwable?
                                        ) {
                                            if (error != null) {
                                                listener.onComplete(null, error)
                                                return
                                            }
                                            upgradeSubscription(
                                                planCode,
                                                subscriptionId,
                                                listener,
                                                true
                                            )
                                        }
                                    })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        404 -> listener.onComplete(
                            null,
                            RemoteException(
                                getResponseErrorMessage(
                                    "message",
                                    response.errorBody()
                                ), response.code()
                            )
                        )
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<CheckoutResponse?>, t: Throwable) {
                return returnError(listener, t)
            }
        })
    }

    private var callSubDowngd: Call<CheckoutResponse?>? = null
    fun downgradeSubscription(
        planCode: String,
        subscriptionId: String,
        listener: OnCompleteListener<String>
    ) {
        downgradeSubscription(planCode, subscriptionId, listener, false)
    }

    private fun downgradeSubscription(
        planCode: String,
        subscriptionId: String,
        listener: OnCompleteListener<String>,
        hasRecover401: Boolean
    ) {
        val parameters = CheckoutPlanRequest()
        parameters.planCode = planCode
        parameters.subscriptionId = subscriptionId
        callSubDowngd =
            licenseService.checkoutDowngrade(Session.getAuthorizationHeader(), parameters)
        callSubDowngd!!.enqueue(object : Callback<CheckoutResponse?> {
            override fun onResponse(
                call: Call<CheckoutResponse?>,
                response: Response<CheckoutResponse?>
            ) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) Debug.logDebug(resp.message)
                    listener.onComplete(resp?.hostedpage, null)

                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(
                                    Session.getRefreshToken(),
                                    object : OnCompleteListener<TokenResponse?>() {
                                        override fun onComplete(
                                            value: TokenResponse?,
                                            error: Throwable?
                                        ) {
                                            if (error != null) {
                                                listener.onComplete(null, error)
                                                return
                                            }
                                            downgradeSubscription(
                                                planCode,
                                                subscriptionId,
                                                listener,
                                                true
                                            )
                                        }
                                    })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        404 -> listener.onComplete(
                            null,
                            RemoteException(
                                getResponseErrorMessage(
                                    "message",
                                    response.errorBody()
                                ), response.code()
                            )
                        )
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<CheckoutResponse?>, t: Throwable) {
                return returnError(listener, t)
            }
        })
    }

    fun searchGeo(
        countryCode: String?,
        provinceCode: String?,
        province: String?
    ): LiveData<List<Map<String, Any>>> {
        return searchGeo(countryCode, provinceCode, province, false)
    }

    private fun searchGeo(
        countryCode: String?,
        provinceCode: String?,
        province: String?,
        hasRecover401: Boolean
    ): LiveData<List<Map<String, Any>>> {
        val geoList = MutableLiveData<List<Map<String, Any>>>()

        licenseService.searchGeo(
            Session.getAuthorizationHeader(),
            countryCode,
            provinceCode,
            province
        ).enqueue(object : Callback<GeoResponse?> {
            override fun onResponse(call: Call<GeoResponse?>, response: Response<GeoResponse?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val body = response.body()
                    geoList.value = body?.list
                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(
                                    Session.getRefreshToken(),
                                    object : OnCompleteListener<TokenResponse?>() {
                                        override fun onComplete(
                                            value: TokenResponse?,
                                            error: Throwable?
                                        ) {
                                            if (error != null) {
                                                return
                                            }
                                            searchGeo(countryCode, provinceCode, province, true)
                                        }
                                    })
                            } else {
                                Debug.logException(UnauthorizedException())
                                return
                            }
                        }
                        else -> {
                            Debug.logErrorResponse(response)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GeoResponse?>, t: Throwable) {
                Debug.logException(t)
            }
        })

        return geoList
    }

    fun getCountries(): LiveData<List<Country>> {
        val countryList = MutableLiveData<List<Country>>()

        licenseService.searchGeo(Session.getAuthorizationHeader(), null, null, null)
            .enqueue(object : Callback<GeoResponse?> {
                override fun onResponse(
                    call: Call<GeoResponse?>,
                    response: Response<GeoResponse?>
                ) {
                    Debug.logDebug(response.toString())
                    if (response.isSuccessful) {
                        val countries = response.body()?.list
                        if (countries != null) {
                            val result = arrayListOf<Country>()
                            for (country in countries) {
                                result.add(
                                    Country(
                                        country.get("countryCode")!!,
                                        country.get("country")!!
                                    )
                                )
                            }
                            countryList.value = result
                        }
                    }
                }

                override fun onFailure(call: Call<GeoResponse?>, t: Throwable) {
                    Debug.logException(t)
                }
            })
        return countryList
    }

    private var callCntry: Call<GeoResponse?>? = null
    fun getCountry(countryCode: String?, listener: OnCompleteListener<Country>) {
        getCountry(countryCode, listener, false)
    }

    fun getCountry(
        countryCode: String?,
        listener: OnCompleteListener<Country>,
        hasRecover401: Boolean
    ) {
        callCntry = licenseService.searchGeo(Session.getAuthorizationHeader(), null, null, null)
        callCntry?.enqueue(object : Callback<GeoResponse?> {
            override fun onResponse(call: Call<GeoResponse?>, response: Response<GeoResponse?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val countryList = response.body()?.list
                    if (countryList != null) {
                        for (country in countryList) {
                            if (countryCode == country["countryCode"]) {
                                listener.onComplete(
                                    Country(
                                        country["countryCode"]!!,
                                        country["country"]!!
                                    ), null
                                )
                                return
                            }
                        }
                        listener.onComplete(null, null)
                    }
                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(
                                    Session.getRefreshToken(),
                                    object : OnCompleteListener<TokenResponse?>() {
                                        override fun onComplete(
                                            value: TokenResponse?,
                                            error: Throwable?
                                        ) {
                                            if (error != null) {
                                                return
                                            }
                                            getCountry(countryCode, listener, true)
                                        }
                                    })
                            } else {
                                Debug.logException(UnauthorizedException())
                                return
                            }
                        }
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<GeoResponse?>, t: Throwable) {
                returnError(listener, t)
            }

        })
    }

    private var callGetSubCntry: Call<GeoResponse?>? = null
    fun getSubdivisionsByCountry(
        countryCode: String,
        listener: OnCompleteListener<List<Subdivision>>
    ) {
        getSubdivisionsByCountry(countryCode, listener, false)
    }

    private fun getSubdivisionsByCountry(
        countryCode: String,
        listener: OnCompleteListener<List<Subdivision>>,
        hasRecover401: Boolean
    ) {
        callGetSubCntry =
            licenseService.searchGeo(Session.getAuthorizationHeader(), countryCode, null, null)
        callGetSubCntry?.enqueue(object : Callback<GeoResponse?> {
            override fun onResponse(call: Call<GeoResponse?>, response: Response<GeoResponse?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val geoResponse = response.body()?.list
                    if (geoResponse != null) {
                        val provinceList: ArrayList<Subdivision> = arrayListOf()
                        for (geo in geoResponse) {
                            provinceList.add(
                                Subdivision(
                                    geo["provinceCode"],
                                    geo["province"] as String
                                )
                            )
                        }
                        listener.onComplete(provinceList, null)
                    }
                } else {
                    when (response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(
                                    Session.getRefreshToken(),
                                    object : OnCompleteListener<TokenResponse?>() {
                                        override fun onComplete(
                                            value: TokenResponse?,
                                            error: Throwable?
                                        ) {
                                            if (error != null) {
                                                return
                                            }
                                            getSubdivisionsByCountry(countryCode, listener, true)
                                        }
                                    })
                            } else {
                                Debug.logException(UnauthorizedException())
                                return
                            }
                        }
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<GeoResponse?>, t: Throwable) {
                returnError(listener, t)
            }
        })
    }

    override fun setLifecycleOwner(lifecycleOwner: LifecycleOwner?) {
        super.setLifecycleOwner(lifecycleOwner)
        authRepository.setLifecycleOwner(lifecycleOwner)
        kycRepository.setLifecycleOwner(lifecycleOwner)
    }

    override fun onStop() {
        super.onStop()
        if (callLics != null && !callLics!!.isCanceled) callLics?.cancel()
        if (callNodeOp != null && !callNodeOp!!.isCanceled) callNodeOp?.cancel()
        if (callSubDowngd != null && !callSubDowngd!!.isCanceled) callSubDowngd?.cancel()
        if (callUpgdSub != null && !callUpgdSub!!.isCanceled) callUpgdSub?.cancel()
        if (callGetSubCntry != null && !callGetSubCntry!!.isCanceled) callGetSubCntry?.cancel()
        if (callCntry != null && !callCntry!!.isCanceled) callCntry?.cancel()
    }
}