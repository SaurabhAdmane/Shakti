package com.shakticoin.app.api.bounty

import com.shakticoin.app.R
import com.shakticoin.app.ShaktiApplication
import com.shakticoin.app.api.*
import com.shakticoin.app.api.auth.AuthRepository
import com.shakticoin.app.api.auth.TokenResponse
import com.shakticoin.app.util.Debug
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BountyRepository : BackendRepository() {
    private val http = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

    private val service : BountyService = Retrofit.Builder()
            .baseUrl(BaseUrl.BOUNTY_SERVICE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(http)
            .build()
            .create(BountyService::class.java)

    val authRepository = AuthRepository()

    private val activeCalls : MutableList<Call<*>> = mutableListOf()

    fun getOffers(listener : OnCompleteListener<List<OfferModel>>) {
        getOffers(listener, false)
    }
    private fun getOffers(listener : OnCompleteListener<List<OfferModel>>, hasRecover401: Boolean = false) {
        val call = service.getOffers(Session.getAuthorizationHeader())
        call.enqueue(object : Callback<OfferListResponseViewModel?> {
            override fun onResponse(call: Call<OfferListResponseViewModel?>, response: Response<OfferListResponseViewModel?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val result = response.body()
                    listener.onComplete(result?.data, null)
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        getOffers(listener, true)
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

            override fun onFailure(call: Call<OfferListResponseViewModel?>, t: Throwable) {
                returnError(listener, t)
            }

        })
        activeCalls.add(call)
    }

    fun claimBonusBounty(id: String, birthDay: String?, listener: OnCompleteListener<BountyReferralViewModel>) {
        claimBonusBounty(id, birthDay, listener, false)
    }
    private fun claimBonusBounty(id: String, birthDay: String?, listener: OnCompleteListener<BountyReferralViewModel>, hasRecover401: Boolean) {
        val params = GenesisBonusBountyCreateModel(id, birthDay)
        val call = service.claimBonusBounty(Session.getAuthorizationHeader(), params)
        call.enqueue(object: Callback<BountyReferralViewModel?> {
            override fun onResponse(call: Call<BountyReferralViewModel?>, response: Response<BountyReferralViewModel?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resp = response.body()
                    if (resp != null) {
                        listener.onComplete(resp, null)
                    } else {
                        listener.onComplete(null, null)
                    }
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        claimBonusBounty(id, birthDay, listener, true)
                                    }
                                })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        409 -> listener.onComplete(null, RemoteException(
                                ShaktiApplication.getContext().getString(R.string.bonus__409_claimed_already),
                                response.code()))
                        410 -> listener.onComplete(null, RemoteException(
                                ShaktiApplication.getContext().getString(R.string.bonus__410_expired),
                                response.code()))
                        400 -> listener.onComplete(null, RemoteException(
                                getResponseErrorMessage("errorMessage", response.errorBody()),
                                response.code()))
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<BountyReferralViewModel?>, t: Throwable) {
                returnError(listener, t)
            }
        })
        activeCalls.add(call)
    }

    fun registerReferral(emailOrMobile: String, emailRegistration: Boolean, promoCode: String, listener: OnCompleteListener<RegisterReferralResponseModel>) {
        registerReferral(emailOrMobile, emailRegistration, promoCode, listener, false)
    }
    private fun registerReferral(emailOrMobile: String, emailRegistration: Boolean, promoCode: String, listener: OnCompleteListener<RegisterReferralResponseModel>, hasRecover401: Boolean) {
        val params = RegisterReferralModel(emailOrMobile, emailRegistration, promoCode)
        val call = service.registerReferral(Session.getAuthorizationHeader(), params)
        call.enqueue(object : Callback<RegisterReferralResponseModel?> {
            override fun onResponse(call: Call<RegisterReferralResponseModel?>, response: Response<RegisterReferralResponseModel?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), null)
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        registerReferral(emailOrMobile, emailRegistration, promoCode, listener, true)
                                    }
                                })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        else -> listener.onComplete(null, RemoteException(
                                getResponseErrorMessage("errorMessage", response.errorBody()), response.code()))
                    }
                }
            }

            override fun onFailure(call: Call<RegisterReferralResponseModel?>, t: Throwable) {
                returnError(listener, t)
            }

        })
        activeCalls.add(call)
    }

    fun getBounties(listener : OnCompleteListener<BountyReferralData?>) {
        getBounties(listener, false)
    }
    private fun getBounties(listener : OnCompleteListener<BountyReferralData?>, hasRecover401: Boolean) {
        val call = service.getBounties(Session.getAuthorizationHeader());
        call.enqueue(object : Callback<BountyReferralViewModel?> {
            override fun onResponse(call: Call<BountyReferralViewModel?>, response: Response<BountyReferralViewModel?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(response.body()?.data, null)
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        getBounties(listener, true)
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

            override fun onFailure(call: Call<BountyReferralViewModel?>, t: Throwable) {
                returnError(listener, t)
            }
        })
        activeCalls.add(call)
    }

    fun getPromoCode(bountyId: String, media: String, listener: OnCompleteListener<PromotionalCodeResponseViewModel>) {
        getPromoCode(bountyId, media, listener, false)
    }
    private fun getPromoCode(bountyId: String, media: String, listener: OnCompleteListener<PromotionalCodeResponseViewModel>, hasRecover401: Boolean) {
        val params = PromotionalCodeModel(media)
        val call = service.promoCode(Session.getAuthorizationHeader(), bountyId, params);
        call.enqueue(object : Callback<PromotionalCodeResponseViewModel> {
            override fun onResponse(call: Call<PromotionalCodeResponseViewModel>, response: Response<PromotionalCodeResponseViewModel>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), null)
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        getPromoCode(bountyId, media, listener, true)
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

            override fun onFailure(call: Call<PromotionalCodeResponseViewModel>, t: Throwable) {
                returnError(listener, t)
            }
        })
        activeCalls.add(call)
    }

    fun addReferral(bountyId: String, emailAddress: String?, phoneNumber: String?, listener: OnCompleteListener<CommonReferralResponseViewModel?>) {
        addReferral(bountyId, emailAddress, phoneNumber, listener, false)
    }
    private fun addReferral(bountyId: String, emailAddress: String?, phoneNumber: String?, listener: OnCompleteListener<CommonReferralResponseViewModel?>, hasRecover401: Boolean) {
        val params = CommonReferralModel(emailAddress, phoneNumber)
        val call = service.addCommonReferral(Session.getAuthorizationHeader(), bountyId, params)
        call.enqueue(object: Callback<CommonReferralResponseViewModel?> {
            override fun onResponse(call: Call<CommonReferralResponseViewModel?>, response: Response<CommonReferralResponseViewModel?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), null)
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        addReferral(bountyId, emailAddress, phoneNumber, listener, true)
                                    }
                                })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        400 -> listener.onComplete(null, RemoteException(
                                getResponseErrorMessage("errorMessage", response.errorBody()), response.code()))
                        else -> returnError(listener, response)
                    }
                }
            }

            override fun onFailure(call: Call<CommonReferralResponseViewModel?>, t: Throwable) {
                returnError(listener, t)
            }

        })
        activeCalls.add(call)
    }

    fun updateGrantOption(bountyId: String, option: String, listener: OnCompleteListener<BountyGrantOptionInfoViewModel?>) {
        updateGrantOption(bountyId, option, listener, false)
    }
    private fun updateGrantOption(bountyId: String, option: String, listener: OnCompleteListener<BountyGrantOptionInfoViewModel?>, hasRecover401: Boolean) {
        val params = BountyGrantOptionInfoModel(option)
        val call = service.grantOptions(Session.getAuthorizationHeader(), bountyId, params)
        call.enqueue(object : Callback<BountyGrantOptionInfoViewModel> {
            override fun onResponse(call: Call<BountyGrantOptionInfoViewModel>, response: Response<BountyGrantOptionInfoViewModel>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), null)
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        updateGrantOption(bountyId, option, listener, true)
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

            override fun onFailure(call: Call<BountyGrantOptionInfoViewModel>, t: Throwable) {
                returnError(listener, t)
            }

        })
        activeCalls.add(call)
    }

    fun updateInfluenceCount(bountyId: String, influenceCount: Int, media: String, listener: OnCompleteListener<CommonReferralResponseViewModel?>) {
        updateInfluenceCount(bountyId, influenceCount, media, listener, false)
    }
    private fun updateInfluenceCount(bountyId: String, influenceCount: Int, media: String, listener: OnCompleteListener<CommonReferralResponseViewModel?>, hasRecover401: Boolean) {
        val params = InfluenceModel(influenceCount, media)
        val call = service.setInfluence(Session.getAuthorizationHeader(), bountyId, params)
        call.enqueue(object : Callback<CommonReferralResponseViewModel?> {
            override fun onResponse(call: Call<CommonReferralResponseViewModel?>, response: Response<CommonReferralResponseViewModel?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), null)
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        updateInfluenceCount(bountyId, influenceCount, media, listener, true)
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

            override fun onFailure(call: Call<CommonReferralResponseViewModel?>, t: Throwable) {
                returnError(listener, t)
            }

        })
        activeCalls.add(call)
    }

    fun getProgress(bountyId: String, listener: OnCompleteListener<ProgressingStatusListResponseViewModel?>) {
        getProgress(bountyId, listener, false)
    }
    private fun getProgress(bountyId: String, listener: OnCompleteListener<ProgressingStatusListResponseViewModel?>, hasRecover401: Boolean) {
        val call = service.getProgressingData(Session.getAuthorizationHeader(), bountyId)
        call.enqueue(object : Callback<ProgressingStatusListResponseViewModel?> {
            override fun onResponse(call: Call<ProgressingStatusListResponseViewModel?>, response: Response<ProgressingStatusListResponseViewModel?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), null)
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        getProgress(bountyId, listener, true)
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

            override fun onFailure(call: Call<ProgressingStatusListResponseViewModel?>, t: Throwable) {
                returnError(listener, t)
            }

        })
        activeCalls.add(call)
    }

    fun getEffortRate(bountyId: String, listener: OnCompleteListener<List<EffortRate>?>) {
        getEffortRate(bountyId, listener, false)
    }
    private fun getEffortRate(bountyId: String, listener: OnCompleteListener<List<EffortRate>?>, hasRecover401: Boolean) {
        val call = service.getEffortRate(Session.getAuthorizationHeader(), bountyId)
        call.enqueue(object : Callback<EffortStatusListResponseViewModel?> {
            override fun onResponse(call: Call<EffortStatusListResponseViewModel?>, response: Response<EffortStatusListResponseViewModel?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    val resultList = arrayListOf<EffortRate>()
                    val resp = response.body()
                    if (resp?.data != null && resp.data!!.size > 0) {
                        for (item in resp.data!! as List<Map<String, Any?>>) {
                            resultList.add(EffortRate(
                                    item["media"] as String,
                                    item["convertedPercentage"] as Int,
                                    item["progressingPercentage"] as Int,
                                    item["influencePercentage"] as Int))
                        }
                    }
                    listener.onComplete(resultList, null)
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        getEffortRate(bountyId, listener, true)
                                    }
                                })
                            } else {
                                listener.onComplete(null, UnauthorizedException())
                                return
                            }
                        }
                        else -> listener.onComplete(null, RemoteException(
                                getResponseErrorMessage("errorMessage", response.errorBody()), response.code()))
                    }
                }
            }

            override fun onFailure(call: Call<EffortStatusListResponseViewModel?>, t: Throwable) {
                returnError(listener, t)
            }

        })
        activeCalls.add(call)
    }

    fun generateQR(listener : OnCompleteListener<QRCodeResponseViewModel?>) {
        generateQR(listener, false)
    }
    private fun generateQR(listener : OnCompleteListener<QRCodeResponseViewModel?>, hasRecover401: Boolean) {
        val call = service.generateQR(Session.getAuthorizationHeader())
        call.enqueue(object : Callback<QRCodeResponseViewModel?> {
            override fun onResponse(call: Call<QRCodeResponseViewModel?>, response: Response<QRCodeResponseViewModel?>) {
                Debug.logDebug(response.toString())
                if (response.isSuccessful) {
                    listener.onComplete(response.body(), null)
                } else {
                    when(response.code()) {
                        401 -> {
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), object : OnCompleteListener<TokenResponse?>() {
                                    override fun onComplete(value: TokenResponse?, error: Throwable?) {
                                        if (error != null) {
                                            listener.onComplete(null, error)
                                            return
                                        }
                                        generateQR(listener, true)
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

            override fun onFailure(call: Call<QRCodeResponseViewModel?>, t: Throwable) {
                returnError(listener, t)
            }

        })
        activeCalls.add(call)
    }

    override fun onStop() {
        super.onStop()
        for (c in activeCalls) {
            if (!c.isCanceled) c.cancel()
        }
        activeCalls.clear()
    }
}