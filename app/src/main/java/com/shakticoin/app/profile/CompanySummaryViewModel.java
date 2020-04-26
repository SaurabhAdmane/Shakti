package com.shakticoin.app.profile;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CompanySummaryViewModel extends ViewModel {
    public MutableLiveData<String> vault = new MutableLiveData<>();
    public MutableLiveData<String> companyName = new MutableLiveData<>();
    public MutableLiveData<String> tradeName = new MutableLiveData<>();
    public MutableLiveData<String> structure = new MutableLiveData<>();
    public MutableLiveData<String> dateEstablished = new MutableLiveData<>();
    public MutableLiveData<String> industry = new MutableLiveData<>();
    public MutableLiveData<String> country = new MutableLiveData<>();
    public MutableLiveData<String> state = new MutableLiveData<>();
    public MutableLiveData<String> city = new MutableLiveData<>();
    public MutableLiveData<String> emailAddress = new MutableLiveData<>();
    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> affiliatedCompany = new MutableLiveData<>();
    public MutableLiveData<String> relationship = new MutableLiveData<>();
    public MutableLiveData<String> type = new MutableLiveData<>();

    public CompanySummaryViewModel() {
        // TODO: demo data - remove
        vault.setValue("Lorem ipsum");
        companyName.setValue("Lorem ipsum");
        tradeName.setValue("Lorem ipsum");
        structure.setValue("Lorem ipsum");
        dateEstablished.setValue("02-02-2020");
        industry.setValue("Lorem ipsum");
        country.setValue("United States");
        state.setValue("New York");
        city.setValue("New York");
        emailAddress.setValue("company@example.com");
        phoneNumber.setValue("+1 222 6948473");
        affiliatedCompany.setValue("Lorem ipsum");
        relationship.setValue("Lorem ipsum");
        type.setValue("Lorem ipsum");
    }
}
