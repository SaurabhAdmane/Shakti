package com.shakticoin.app.util;

import androidx.annotation.Nullable;

import com.shakticoin.app.api.Constants;

import java.math.BigDecimal;
import java.util.Locale;

public class FormatUtil {

    /**
     *  Return string representation of an amount in toshi.
     */
    public static String formatCoinAmount(@Nullable BigDecimal toshiAmount) {
        BigDecimal coinAmount = BigDecimal.ZERO;
        if (toshiAmount != null) {
            coinAmount = toshiAmount.divide(BigDecimal.valueOf(Constants.TOSHI_FACTOR));
        }
        return String.format(Locale.getDefault(), "SXE %1$.2f", coinAmount);
    }
}
