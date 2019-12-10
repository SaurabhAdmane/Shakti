package com.shakticoin.app.api.country;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.shakticoin.app.api.BaseUrl;
import com.shakticoin.app.util.Debug;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryRepository {
    private CountryService countryService;

    public CountryRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.get())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        countryService = retrofit.create(CountryService.class);
    }

    public LiveData<List<Country>> getCountries(@NonNull Locale locale) {

        final MutableLiveData<List<Country>> liveData = new MutableLiveData<>();
        /*
         * Due to how the custom spinner works we cannot supply an empty list to it. Instead
         * we ensure at least one item exists even if network failed and request does not return
         * anything. The country name in this case can be any string because it will be
         * replaced by hint.
         */
        liveData.setValue(Collections.singletonList(new Country(null, "")));

        countryService.getCountries(locale.getLanguage()).enqueue(new Callback<List<Country>>() {
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
}
