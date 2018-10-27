package org.shakticoin.tour;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.shakticoin.R;

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
