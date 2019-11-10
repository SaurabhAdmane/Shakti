package com.shakticoin.app.util;

import android.content.res.Resources;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shakticoin.app.R;

public class HtmlUtil {

    /**
     * Extends Html.loadHtml by post-processing the spanned string in order to implement
     * custom look.
     */
    public static SpannableString fromHtml(@NonNull String htmlText, @NonNull TextView view) {
        Spanned spannedText = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spannedText = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT);
        } else {
            spannedText = Html.fromHtml(htmlText);
        }
        SpannableString s = new SpannableString(spannedText);
        BulletSpan[] bullets = s.getSpans(0, s.length(), BulletSpan.class);
        if (bullets != null && bullets.length > 0) {
            int color = view.getCurrentTextColor();
            Resources resources = view.getContext().getResources();
            int gap = Float.valueOf(resources.getDimension(R.dimen.html_bullet_gap)).intValue();
            int radius = Float.valueOf(resources.getDimension(R.dimen.html_bullet_radius)).intValue();
            for (BulletSpan bullet : bullets) {
                int start = s.getSpanStart(bullet);
                int end = s.getSpanEnd(bullet);
                s.removeSpan(bullet);
                BulletSpan newBullet = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    newBullet = new BulletSpan(gap, color, radius);
                } else {
                    newBullet = new BulletSpan(gap, color);
                }
                s.setSpan(newBullet, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return s;
    }
}
