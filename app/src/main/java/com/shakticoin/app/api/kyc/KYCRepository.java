package com.shakticoin.app.api.kyc;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.util.Debug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class KYCRepository extends BackendRepository {
    private KYCService service;
    private AuthRepository authRepository;

    public KYCRepository() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.KYC_USER_SERVICE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        service = retrofit.create(KYCService.class);
        authRepository = new AuthRepository();
    }

    public void getUserDetails(OnCompleteListener<KycUserView> listener) {
        getUserDetails(listener, false);
    }
    private void getUserDetails(OnCompleteListener<KycUserView> listener, boolean hasRecover401) {
        service.getUserDetails(Session.getAuthorizationHeader()).enqueue(new Callback<KycUserView>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<KycUserView> call, Response<KycUserView> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    KycUserView values = response.body();
                    listener.onComplete(values, null);
                } else {
                    switch (response.code()) {
                        case 401:
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                    @Override
                                    public void onComplete(TokenResponse value, Throwable error) {
                                        if (error != null) {
                                            listener.onComplete(null, new UnauthorizedException());
                                            return;
                                        }
                                        getUserDetails(listener, true);
                                    }
                                });
                            } else {
                                listener.onComplete(null, new UnauthorizedException());
                                return;
                            }
                            break;
                        case 404:
                            ResponseBody errorBody = response.errorBody();
                            String message = response.message();
                            if (errorBody != null) {
                                try {
                                    String errorResponse = errorBody.string();
                                    JSONObject json = new JSONObject(errorResponse);
                                    if (json.has("message")) {
                                        message = json.getString("message");
                                    }
                                } catch (IOException | JSONException e) {
                                    Debug.logDebug(e.getMessage());
                                }
                            }
                            listener.onComplete(null, new RemoteException(message, 404));
                            break;
                        default:
                            Debug.logErrorResponse(response);
                            returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<KycUserView> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void createUserDetails(@NonNull KycUserModel parameters, @NonNull OnCompleteListener<Map<String, Object>> listener) {
        createUserDetails(parameters, listener, false);
    }
    public void createUserDetails(@NonNull KycUserModel parameters, @NonNull OnCompleteListener<Map<String, Object>> listener, boolean hasRecover401) {
        service.createUserDetails(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<Map<String, Object>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    switch (response.code()) {
                        case 401:
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                    @Override
                                    public void onComplete(TokenResponse value, Throwable error) {
                                        if (error != null) {
                                            listener.onComplete(null, new UnauthorizedException());
                                            return;
                                        }
                                        createUserDetails(parameters, listener, true);
                                    }
                                });
                            } else {
                                listener.onComplete(null, new UnauthorizedException());
                                return;
                            }
                            break;
                        default:
                            JSONObject jsonResponse = errorBodyJSON(response.errorBody());
                            if (jsonResponse != null && jsonResponse.has("rootArray")) {
                                try {
                                    JSONArray msgArray = jsonResponse.getJSONArray("rootArray");
                                    listener.onComplete(null, new RemoteException(msgArray.getString(0)));
                                    return;
                                } catch (JSONException e) {
                                    Debug.logDebug(e.getMessage());
                                }
                            }
                            returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void updateUserDetails(KycUserModel parameters, OnCompleteListener<Map<String, Object>> listener) {
        updateUserDetails(parameters, listener, false);
    }
    public void updateUserDetails(KycUserModel parameters, OnCompleteListener<Map<String, Object>> listener, boolean hasRecover401) {
        service.updateUserDetails(Session.getAuthorizationHeader(), parameters).enqueue(new Callback<Map<String, Object>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    switch (response.code()) {
                        case 401:
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                    @Override
                                    public void onComplete(TokenResponse value, Throwable error) {
                                        if (error != null) {
                                            listener.onComplete(null, new UnauthorizedException());
                                            return;
                                        }
                                        updateUserDetails(parameters, listener, true);
                                    }
                                });
                            } else {
                                listener.onComplete(null, new UnauthorizedException());
                                return;
                            }
                            break;
                        case 400:
                            String errMsg = getResponseErrorMessage("message", response.errorBody());
                            listener.onComplete(null, new RemoteException(errMsg, response.code()));
                            break;
                        default:
                            JSONObject jsonResponse = errorBodyJSON(response.errorBody());
                            if (jsonResponse != null && jsonResponse.has("rootArray")) {
                                try {
                                    JSONArray msgArray = jsonResponse.getJSONArray("rootArray");
                                    listener.onComplete(null, new RemoteException(msgArray.getString(0)));
                                    return;
                                } catch (JSONException e) {
                                    Debug.logDebug(e.getMessage());
                                }
                            }
                            returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void getKycDocumentTypes(OnCompleteListener<List<Map<String, Object>>> listener) {
        getKycDocumentTypes(listener, false);
    }
    public void getKycDocumentTypes(OnCompleteListener<List<Map<String, Object>>> listener, boolean hasRecover401) {
        service.getDocumentTypes(Session.getAuthorizationHeader()).enqueue(new Callback<ResponseBody>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (body != null) {
                        try {
                            String content = body.string();
                            Debug.logDebug(content);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    switch (response.code()) {
                        case 401:
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                    @Override
                                    public void onComplete(TokenResponse value, Throwable error) {
                                        if (error != null) {
                                            listener.onComplete(null, new UnauthorizedException());
                                            return;
                                        }
                                        getKycDocumentTypes(listener, true);
                                    }
                                });
                            } else {
                                listener.onComplete(null, new UnauthorizedException());
                                return;
                            }
                            break;
                        default:
                            Debug.logErrorResponse(response);
                            returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    /**
     * Retrieve KYC information.
     */
    public void getKycCategories(OnCompleteListener<List<KycCategory>> listener) {
        Context context = ShaktiApplication.getContext();

        List<KycCategory> categories = new ArrayList<>();

        KycCategory category = new KycCategory();
        category.setId(1);
        category.setName(context.getString(R.string.profile_kyc_utility_bills));
        category.setDescription(context.getString(R.string.profile_kyc_utility_bills_desc));
        List<KycDocType> docTypes = new ArrayList<>(8);
        docTypes.add(new KycDocType("UTILITY_PHONE", context.getString(R.string.kyc_select_doctype_phone)));
        docTypes.add(new KycDocType("UTILITY_GAS", context.getString(R.string.kyc_select_doctype_gas)));
        docTypes.add(new KycDocType("UTILITY_ELECTRICITY", context.getString(R.string.kyc_select_doctype_electricity)));
        docTypes.add(new KycDocType("UTILITY_WATER", context.getString(R.string.kyc_select_doctype_water)));
        docTypes.add(new KycDocType("UTILITY_MUNICIPAL_TAX", context.getString(R.string.kyc_select_doctype_tax)));
        docTypes.add(new KycDocType("UTILITY_INSURANCE_COVERAGE", context.getString(R.string.kyc_select_doctype_insurance)));
        docTypes.add(new KycDocType("UTILITY_CABLE", context.getString(R.string.kyc_select_doctype_cable)));
        docTypes.add(new KycDocType("UTILITY_OTHER", context.getString(R.string.kyc_select_doctype_other)));
        category.setDoc_types(docTypes);
        categories.add(category);

        category = new KycCategory();
        category.setId(2);
        category.setName(context.getString(R.string.profile_kyc_property));
        category.setDescription(context.getString(R.string.profile_kyc_property_desc));
        docTypes = new ArrayList<>(3);
        docTypes.add(new KycDocType("PROPERTY_ANY", context.getString(R.string.kyc_opt_propagreement)));
        docTypes.add(new KycDocType("VEHICLE_ANY", context.getString(R.string.kyc_opt_vehicleagreement)));
        docTypes.add(new KycDocType("FINANCE_ANY", context.getString(R.string.kyc_opt_financestmt)));
        category.setDoc_types(docTypes);
        categories.add(category);

        category = new KycCategory();
        category.setId(3);
        category.setName(context.getString(R.string.profile_kyc_gov));
        category.setDescription(context.getString(R.string.profile_kyc_gov_desc));
        docTypes = new ArrayList<>(8);
        docTypes.add(new KycDocType("AUTHORITY_SCHOOL_ADMISSION_LETTER", context.getString(R.string.kyc_opt_schooladmission)));
        docTypes.add(new KycDocType("AUTHORITY_SCHOOL_REPORT_CARD", context.getString(R.string.kyc_opt_schoolreport)));
        docTypes.add(new KycDocType("AUTHORITY_INCOME_TAX_RECEIPT", context.getString(R.string.kyc_opt_incometax)));
        docTypes.add(new KycDocType("AUTHORITY_GOVT_ISSUED_ADDRESS", context.getString(R.string.kyc_opt_govid)));
        docTypes.add(new KycDocType("AUTHORITY_PASSPORT", context.getString(R.string.kyc_opt_passport)));
        docTypes.add(new KycDocType("AUTHORITY_NGO_ISSUED_ID", context.getString(R.string.kyc_opt_ngo)));
        docTypes.add(new KycDocType("AUTHORITY_NGO_LETTER", context.getString(R.string.kyc_opt_ngoletter)));
        docTypes.add(new KycDocType("AUTHORITY_JOB_LETTER", context.getString(R.string.kyc_opt_jobletter)));
        category.setDoc_types(docTypes);
        categories.add(category);

        listener.onComplete(categories, null);
    }

    public void uploadDocument(List<MultipartBody.Part> files, OnCompleteListener<Void> listener) {
        uploadDocument(files, listener, false);
    }
    public void uploadDocument(List<MultipartBody.Part> files, OnCompleteListener<Void> listener, boolean hasRecover401) {
        service.uploadDocuments(Session.getAuthorizationHeader(), files).enqueue(new Callback<Map<String, Object>>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    Map<String, Object> body = response.body();
                    Debug.logDebug(body.toString());
                    listener.onComplete(null, null);
                } else {
                    switch (response.code()) {
                        case 401:
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                    @Override
                                    public void onComplete(TokenResponse value, Throwable error) {
                                        if (error != null) {
                                            listener.onComplete(null, new UnauthorizedException());
                                            return;
                                        }
                                        uploadDocument(files, listener, true);
                                    }
                                });
                            } else {
                                listener.onComplete(null, new UnauthorizedException());
                                return;
                            }
                            break;
                        case 400:
                            String errMsg = getResponseErrorMessage("message", response.errorBody());
                            if (TextUtils.isEmpty(errMsg)) {
                                errMsg = ShaktiApplication.getContext().getString(R.string.err_unexpected);
                            }
                            listener.onComplete(null, new RemoteException(errMsg, response.code()));
                            break;
                        default:
                            Debug.logErrorResponse(response);
                            returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void subscription(@NonNull OnCompleteListener<Void> listener) {
        subscription(listener, false);
    }
    public void subscription(@NonNull OnCompleteListener<Void> listener, boolean hasRecover401) {
        Call<ResponseBody> call = service.subscription(Session.getAuthorizationHeader());
        Debug.logDebug(call.request().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(null, null);
                } else {
                    switch (response.code()) {
                        case 401:
                            if (!hasRecover401) {
                                authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                    @Override
                                    public void onComplete(TokenResponse value, Throwable error) {
                                        if (error != null) {
                                            listener.onComplete(null, new UnauthorizedException());
                                            return;
                                        }
                                        subscription(listener, true);
                                    }
                                });
                            } else {
                                listener.onComplete(null, new UnauthorizedException());
                                return;
                            }
                            break;
                        case 404:
                            listener.onComplete(null, new RemoteException(
                                    ShaktiApplication.getContext().getString(R.string.err_user_not_found), 404));
                            break;
                        default:
                            Debug.logErrorResponse(response);
                            returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                returnError(listener, t);
            }
        });
    }

    public void isWalletUnlocked(@NonNull OnCompleteListener<Boolean> listener) {
        getUserDetails(new OnCompleteListener<KycUserView>() {
            @Override
            public void onComplete(KycUserView value, Throwable error) {
                if (error != null) {
                    listener.onComplete(false, null);
                    return;
                }
                listener.onComplete(KycUserView.STATUS_UNLOCKED.equals(value.getKycStatus()), null);
            }
        }, false);
    }
}
