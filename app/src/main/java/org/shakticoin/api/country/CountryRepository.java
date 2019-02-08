package org.shakticoin.api.country;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import org.shakticoin.api.BaseUrl;
import org.shakticoin.util.Debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

        countryService.getCountries(locale.getLanguage()).enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, String>> call, @NonNull Response<Map<String, String>> response) {
                if (call.isExecuted()) {
                    Debug.logDebug(response.toString());
                    if (response.isSuccessful()) {
                        Map<String, String> countries = response.body();
                        if (countries != null && countries.size() > 0) {
                            Set<Map.Entry<String, String>> entries = countries.entrySet();
                            List<Country> countryList = new ArrayList<>();
                            for (Map.Entry<String, String> entry : entries) {
                                if (entry.getValue() != null)
                                    countryList.add(new Country(entry.getKey(), entry.getValue()));
                            }
                            if (countryList.size() > 0) {
                                Collections.sort(countryList, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
                                liveData.setValue(countryList);
                            }
                        }
                    } else {
                        Debug.logErrorResponse(response);
                    }
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Debug.logException(t);
            }
        });

        return liveData;
    }
}
