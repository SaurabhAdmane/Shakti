package com.shakticoin.app.api.country;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shakticoin.app.R;
import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.RemoteException;
import com.shakticoin.app.api.Session;
import com.shakticoin.app.api.UnauthorizedException;
import com.shakticoin.app.util.Debug;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class CountryRepository {
    private CountryService countryService;

    public CountryRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        countryService = retrofit.create(CountryService.class);
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

        countryService.getCountries(Session.getAuthorizationHeader(), Session.getLanguageHeader())
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
        countryService.getCountry(Session.getAuthorizationHeader(), Session.getLanguageHeader(), countryCode)
                .enqueue(new Callback<Country>() {
                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<Country> call, Response<Country> response) {
                        Debug.logDebug(response.toString());
                        if (response.isSuccessful()) {
                            Country country = response.body();
                            listener.onComplete(country, null);
                        } else {
                            if (response.code() == 401) {
                                listener.onComplete(null, new UnauthorizedException());
                            } else {
                                listener.onComplete(null, new RemoteException(response.message(), response.code()));
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

    public void getSubdivisionsByCountry(@NonNull String countryCode,
                                         OnCompleteListener<List<Subdivision>> listener,
                                         Context context) {
        countryService.getSubdivisions(Session.getAuthorizationHeader(), Session.getLanguageHeader(), countryCode)
                .enqueue(new Callback<List<Subdivision>>() {
                    @EverythingIsNonNull
                    @Override
                    public void onResponse(Call<List<Subdivision>> call, Response<List<Subdivision>> response) {
                        Debug.logDebug(response.toString());
                        if (response.isSuccessful()) {
                            List<Subdivision> subdivisions = response.body();
                            if (listener != null) {
                                listener.onComplete(subdivisions, null);
                            }
                        } else {
                            Debug.logErrorResponse(response);
                            if (listener != null) {
                                String message = response.message();
                                if (response.code() == 404) {
                                    message = context.getString(R.string.err_state_not_found);
                                }
                                listener.onComplete(null, new RemoteException(message, response.code()));
                            }
                        }
                    }

                    @EverythingIsNonNull
                    @Override
                    public void onFailure(Call<List<Subdivision>> call, Throwable t) {
                        Debug.logException(t);
                    }
                });
    }

    public void getSubdivision(@NonNull String countryCode,
                               @NonNull Integer subdivisionId,
                               @NonNull OnCompleteListener<Subdivision> listener) {
        countryService.getSubdivision(Session.getAuthorizationHeader(), Session.getLanguageHeader(),
                countryCode, subdivisionId).enqueue(new Callback<Subdivision>() {
            @Override
            public void onResponse(Call<Subdivision> call, Response<Subdivision> response) {
                Debug.logDebug(response.toString());
                if (response.isSuccessful()) {
                    Subdivision subdivision = response.body();
                    listener.onComplete(subdivision, null);
                } else {
                    if (response.code() == 401) {
                        listener.onComplete(null, new UnauthorizedException());
                    } else {
                        listener.onComplete(null, new RemoteException(response.message(), response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call<Subdivision> call, Throwable t) {
                Debug.logException(t);
                listener.onComplete(null, t);
            }
        });
    }
}
