package com.shakticoin.app.tour;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.databinding.ObservableBoolean;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.shakticoin.app.R;

public class StepOneFragment extends Fragment {

    private WelcomeTourModel viewModel;

    private ImageView ctrlIcon;
    private TextView ctrlTitle;
    private TextView ctrlSubtitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity != null) {
            viewModel = ViewModelProviders.of(activity).get(WelcomeTourModel.class);
            // fade in content when animation of the activity background is finished
            viewModel.fadeInFirstPage.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if (((ObservableBoolean) sender).get()) {
                        fadeIn();
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tour_1, container, false);

        ctrlIcon = v.findViewById(R.id.icon);
        ctrlTitle = v.findViewById(R.id.title);
        ctrlSubtitle = v.findViewById(R.id.subtitle);
        if (viewModel != null && viewModel.fadeInFirstPage.get()) {
            makeVisible();
        }

        return v;
    }

    private void fadeIn() {
        ctrlIcon.setAlpha(0f);
        ctrlTitle.setAlpha(0f);
        ctrlSubtitle.setAlpha(0f);
        makeVisible();
        ctrlIcon.animate()
                .alpha(1f)
                .setDuration(WelcomeTourActivity.FADE_IN_DURATION);
        ctrlTitle.animate()
                .alpha(1f)
                .setDuration(WelcomeTourActivity.FADE_IN_DURATION);
        ctrlSubtitle.animate()
                .alpha(1f)
                .setDuration(WelcomeTourActivity.FADE_IN_DURATION);
    }

    private void makeVisible() {
        ctrlIcon.setVisibility(View.VISIBLE);
        ctrlTitle.setVisibility(View.VISIBLE);
        ctrlSubtitle.setVisibility(View.VISIBLE);
    }
}
