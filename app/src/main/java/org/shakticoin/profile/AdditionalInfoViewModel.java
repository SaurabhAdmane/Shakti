package org.shakticoin.profile;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.shakticoin.widget.InlineLabelSpinner;

import java.util.ArrayList;
import java.util.List;

public class AdditionalInfoViewModel extends ViewModel {
    public MutableLiveData<List<String>> educatinLevelList = new MutableLiveData<>();
    public MutableLiveData<String> selectedEducationLevel = new MutableLiveData<>();

    public AdditionalInfoViewModel() {
        // TODO: replace test set of education levels with final
        List<String> list = new ArrayList<>();
        list.add("Associate degree");
        list.add("Bachelor's degree");
        list.add("Master's degree");
        list.add("Doctoral degree");
        educatinLevelList.setValue(list);
    }

    /** This method is bind to onItemSelected event of Spinner */
    public void onEducationLevelSelected(View view, int position) {
        InlineLabelSpinner spinner = (InlineLabelSpinner) view;
        if (spinner.isChoiceMade()) {
            selectedEducationLevel.setValue((String) spinner.getAdapter().getItem(position));
        }
    }
}
