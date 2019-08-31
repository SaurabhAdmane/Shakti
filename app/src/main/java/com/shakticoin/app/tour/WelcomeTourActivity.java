package com.shakticoin.app.tour;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.shakticoin.app.registration.SignUpActivity;

import com.shakticoin.app.R;

import com.shakticoin.app.util.PreferenceHelper;


public class WelcomeTourActivity extends AppCompatActivity {
    public static final long VIEW_DELAY         = 500L;
    public static final long FADE_IN_DURATION   = 500L;

    private ViewPager pager;
    private ImageView[] tickmarks = new ImageView[3];
    private WelcomeTourModel viewModel;

    @Override
    public void onBackPressed() {
        int currentItem = pager.getCurrentItem();
        if (currentItem == 0) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(currentItem - 1);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        viewModel = ViewModelProviders.of(this).get(WelcomeTourModel.class);

        tickmarks[0] = findViewById(R.id.tickmark1);
        tickmarks[1] = findViewById(R.id.tickmark2);
        tickmarks[2] = findViewById(R.id.tickmark3);

        pager = findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new WelcomeTourAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {}

            @Override
            public void onPageSelected(int i) {
                setPageIndicator(i);
                viewModel.fadeInButton.set(i == 2);
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });

        setPageIndicator(0);

        /*
         * animate background starting from 1/3 alpha because natural background of the activity
         * is white and this may create negative visual effect.
         */
        View root = findViewById(android.R.id.content);
        root.setAlpha(0.3f);
        root.animate()
                .alpha(1f)
                .setDuration(FADE_IN_DURATION * 2)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // trigger animation for text and image on the first page
                        viewModel.fadeInFirstPage.set(true);
                    }
                });
    }

    private void setPageIndicator(int page) {
        for (int i = 0; i < tickmarks.length; i++) {
            if (i == page) {
                tickmarks[i].setImageAlpha(0xFF);
            } else {
                tickmarks[i].setImageAlpha(0x4D);
            }
        }
    }

    public void onEndTour(View view) {
        SharedPreferences prefs = getSharedPreferences(PreferenceHelper.GENERAL_PREFERENCES, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(PreferenceHelper.PREF_KEY_TOUR_DONE, true).apply();
        startActivity(new Intent(this, SignUpActivity.class));
        finish();
    }

    class WelcomeTourAdapter extends FragmentPagerAdapter {

        WelcomeTourAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new StepOneFragment();
                case 1:
                    return new StepTwoFragment();
                case 2:
                    return new StepThreeFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
