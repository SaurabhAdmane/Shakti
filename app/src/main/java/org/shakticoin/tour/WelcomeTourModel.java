package org.shakticoin.tour;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

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
