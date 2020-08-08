package com.shakticoin.app.util;

import androidx.annotation.Nullable;

import com.shakticoin.app.api.Constants;

import java.math.BigDecimal;
import java.util.Locale;

public class FormatUtil {

    /**
     *  Return string representation of an amount in toshi.
     */
    public static String formatCoinAmountFromToshi(@Nullable BigDecimal toshiAmount) {
        BigDecimal coinAmount = BigDecimal.ZERO;
        if (toshiAmount != null) {
            coinAmount = toshiAmount.divide(BigDecimal.valueOf(Constants.TOSHI_FACTOR));
        }
        return String.format(Locale.getDefault(), "SXE %1$.2f", coinAmount);
    }

    /**
     *  Return string representation of an amount in toshi.
     */
    public static String formatCoinAmount(@Nullable BigDecimal coins) {
        if (coins == null) return "SXE -.--";
        return String.format(Locale.getDefault(), "SXE %1$.2f", coins);
    }
}
