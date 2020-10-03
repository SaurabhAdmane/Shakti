package com.shakticoin.app.api.country;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shakticoin.app.R;
import com.shakticoin.app.ShaktiApplication;
import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.api.auth.AuthRepository;
import com.shakticoin.app.api.auth.TokenResponse;
import com.shakticoin.app.util.Debug;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class CountryRepository extends BackendRepository {
    private CountryService countryService;
    private AuthRepository authRepository;

    public CountryRepository() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BaseUrl.BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        countryService = retrofit.create(CountryService.class);
        authRepository = new AuthRepository();
    }

    public LiveData<List<Country>> getCountries() {

        final MutableLiveData<List<Country>> liveData = new MutableLiveData<>();
        /*
         * Due to how the custom spinner works we cannot supply an empty list to it. Instead
         * we ensure at least one item exists even if network failed and request does not return
         * anything. The country name in this case can be any string because it will be
         * replaced by hint.
         */
        liveData.setValue(Collections.singletonList(new Country(null, "")));

        countryService.getCountries(Session.getLanguageHeader())
                .enqueue(new Callback<List<Country>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Country>> call, @NonNull Response<List<Country>> response) {
                        if (call.isExecuted()) {
                            Debug.logDebug(response.toString());
                            if (response.isSuccessful()) {
                                List<Country> countries = response.body();
                                if (countries != null && countries.size() > 0) {
                                    Collections.sort(countries, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                                    liveData.setValue(countries);
                                }
                            } else {
                                Debug.logErrorResponse(response);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Country>> call, @NonNull Throwable t) {
                        Debug.logException(t);
                    }
                });

        return liveData;
    }

    public void getCountry(@NonNull String countryCode, @NonNull OnCompleteListener<Country> listener) {
        getCountry(countryCode, listener, false);
    }
    public void getCountry(@NonNull String countryCode, @NonNull OnCompleteListener<Country> listener, boolean hasRecover401) {
        countryService.getCountry(Session.getLanguageHeader(), countryCode)
                .enqueue(new Callback<Country>() {
                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<Country> call, Response<Country> response) {
                        Debug.logDebug(response.toString());
                        if (response.isSuccessful()) {
                            listener.onComplete(response.body(), null);
                        } else {
                            if (response.code() == 401) {
                                if (!hasRecover401) {
                                    authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                        @Override
                                        public void onComplete(TokenResponse value, Throwable error) {
                                            if (error != null) {
                                                listener.onComplete(null, new UnauthorizedException());
                                                return;
                                            }
                                            getCountry(countryCode, listener, true);
                                        }
                                    });
                                } else {
                                    listener.onComplete(null, new UnauthorizedException());
                                }
                            } else {
                                Debug.logErrorResponse(response);
                                returnError(listener, response);
                            }
                        }
                    }

                    @EverythingIsNonNull
                    @Override
                    public void onFailure(Call<Country> call, Throwable t) {
                        Debug.logException(t);
                        listener.onComplete(null, t);
                    }
                });
    }

    public void getSubdivisionsByCountry(@NonNull String countryCode, @NonNull OnCompleteListener<List<Subdivision>> listener) {
        getSubdivisionsByCountry(countryCode, listener, false);
    }
    public void getSubdivisionsByCountry(@NonNull String countryCode,
                                         @NonNull OnCompleteListener<List<Subdivision>> listener, boolean hasRecover401) {
        countryService.getSubdivisions(Session.getLanguageHeader(), countryCode)
                .enqueue(new Callback<List<Subdivision>>() {
                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<List<Subdivision>> call, Response<List<Subdivision>> response) {
                        Debug.logDebug(response.toString());
                        if (response.isSuccessful()) {
                            listener.onComplete(response.body(), null);
                        } else {
                            if (response.code() == 401) {
                                if (!hasRecover401) {
                                    authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                        @Override
                                        public void onComplete(TokenResponse value, Throwable error) {
                                            if (error != null) {
                                                listener.onComplete(null, new UnauthorizedException());
                                                return;
                                            }
                                            getSubdivisionsByCountry(countryCode, listener, true);
                                        }
                                    });
                                } else {
                                    listener.onComplete(null, new UnauthorizedException());
                                }
                            } else {
                                String message = response.message();
                                if (response.code() == 404) {
                                    message = ShaktiApplication.getContext().getString(R.string.err_state_not_found);
                                }
                                listener.onComplete(null, new RemoteException(message, response.code()));
                            }
                        }
                    }

                    @EverythingIsNonNull
                    @Override
                    public void onFailure(Call<List<Subdivision>> call, Throwable t) {
                        returnError(listener, t);
                    }
                });
    }

    public void getSubdivision(@NonNull String countryCode, @NonNull Integer subdivisionId, @NonNull OnCompleteListener<Subdivision> listener) {
        getSubdivision(countryCode, subdivisionId, listener, false);
    }
    public void getSubdivision(@NonNull String countryCode, @NonNull Integer subdivisionId,
                               @NonNull OnCompleteListener<Subdivision> listener, boolean hasRecover401) {
        countryService.getSubdivision(Session.getLanguageHeader(),
                countryCode, subdivisionId).enqueue(new Callback<Subdivision>() {
            @EverythingIsNonNull
            @Override
            public void onResponse(Call<Subdivision> call, Response<Subdivision> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    listener.onComplete(response.body(), null);
                } else {
                    if (response.code() == 401) {
                        if (!hasRecover401) {
                            authRepository.refreshToken(Session.getRefreshToken(), new OnCompleteListener<TokenResponse>() {
                                @Override
                                public void onComplete(TokenResponse value, Throwable error) {
                                    if (error != null) {
                                        listener.onComplete(null, new UnauthorizedException());
                                        return;
                                    }
                                    getSubdivision(countryCode, subdivisionId, listener, true);
                                }
                            });
                        } else {
                            listener.onComplete(null, new UnauthorizedException());
                        }
                    } else {
                        Debug.logErrorResponse(response);
                        returnError(listener, response);
                    }
                }
            }

            @EverythingIsNonNull
            @Override
            public void onFailure(Call<Subdivision> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }

    @Override
    public void setLifecycleOwner(LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        authRepository.setLifecycleOwner(lifecycleOwner);
    }
}
