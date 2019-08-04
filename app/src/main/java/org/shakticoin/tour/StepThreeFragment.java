package org.shakticoin.tour;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import org.shakticoin.R;


public class StepThreeFragment extends Fragment {

    private Button ctrlEndTour;
    private WelcomeTourModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity != null) {
            viewModel = ViewModelProviders.of(activity).get(WelcomeTourModel.class);
            viewModel.fadeInButton.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if (((ObservableBoolean) sender).get()) {
                        // animate the button if it is not visible, otherwise this cause blinking
                        // when we go one step back and forth again.
                        if (ctrlEndTour.getVisibility() != View.VISIBLE) {
                            Handler handler = new Handler();
                            handler.postDelayed(() -> {
                                ctrlEndTour.setAlpha(0f);
                                ctrlEndTour.setVisibility(View.VISIBLE);
                                ctrlEndTour.animate()
                                        .alpha(1f)
                                        .setDuration(WelcomeTourActivity.FADE_IN_DURATION);
                            }, WelcomeTourActivity.VIEW_DELAY);
                        }
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tour_3, container, false);

        ctrlEndTour = v.findViewById(R.id.endTour);

        return v;
    }
}
