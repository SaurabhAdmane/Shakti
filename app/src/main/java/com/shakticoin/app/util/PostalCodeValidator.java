package com.shakticoin.app.util;

import android.text.TextUtils;
import android.widget.EditText;

import com.shakticoin.app.widget.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PostalCodeValidator implements TextInputLayout.Validator {
    private static final List<Map<String, Pattern>> patterns;
    static {
        //TODO: the list is not completed yet
        patterns = new ArrayList<>();
        patterns.add(CommonUtil.toMap("US", Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$")));
        patterns.add(CommonUtil.toMap("CA", Pattern.compile("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$")));
        patterns.add(CommonUtil.toMap("UK", Pattern.compile("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$")));
    }

    private String countryCode;

    public PostalCodeValidator(String countryCode) {
        this.countryCode = countryCode != null ? countryCode.toUpperCase() : null;
    }

    @Override
    public boolean isValid(EditText view, String value) {
        // we cannot check anything if country is not specified because some countries
        // do not use postal codes in addresses.
        if (TextUtils.isEmpty(countryCode)) return true;

        // try to find the patterns (a few may exist) for testing postal code value but if not
        // found then the country does not use postal codes (or the pattern is missing of course).
        List<Pattern> countryPatterns = new ArrayList<>();
        for (Map<String, Pattern> item : patterns) {
            if (item.containsKey(countryCode)) {
                countryPatterns.add(item.get(countryCode));
            }
        }

        // the country does not use postal codes or the pattern is not added yet
        if (countryPatterns.size() == 0) return true;

        // testing specified string for being a postal code
        for (Pattern p : countryPatterns) {
            if (p.matcher(value).matches()) {
                return true;
            }
        }

        return false;
    }
}
