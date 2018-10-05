package org.shakticoin.tour;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.shakticoin.R;


public class WelcomeTourActivity extends AppCompatActivity {
    private ViewPager pager;
    private ImageView[] tickmarks = new ImageView[3];

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
            }

            @Override
            public void onPageScrollStateChanged(int i) {}
        });

        setPageIndicator(0);
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
