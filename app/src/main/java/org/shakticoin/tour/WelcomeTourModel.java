package org.shakticoin.tour;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableBoolean;

public class WelcomeTourModel extends ViewModel {
    /**
     * Trigger animation for the exit button on the last page.
     */
    public ObservableBoolean fadeInButton = new ObservableBoolean(false);

    /**
     * Trigger animation for the content of the first page.
     */
    public ObservableBoolean fadeInFirstPage = new ObservableBoolean(false);
}
