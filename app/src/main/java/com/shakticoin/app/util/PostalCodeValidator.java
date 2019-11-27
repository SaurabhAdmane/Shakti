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
        patterns = new ArrayList<>();
        patterns.add(CommonUtil.toMap("US", Pattern.compile("^[0-9]{5}(?:-[0-9]{4})?$")));
        patterns.add(CommonUtil.toMap("CA", Pattern.compile("^(?!.*[DFIOQU])[A-VXY][0-9][A-Z] ?[0-9][A-Z][0-9]$")));
        patterns.add(CommonUtil.toMap("UK", Pattern.compile("^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$")));
        Pattern fourDigitsPattern = Pattern.compile("^\\d{4}$");
        patterns.add(CommonUtil.toMap("AF", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("AL", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("AM", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("AQ", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("AU", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("AT", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("BD", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("BE", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("BG", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("CV", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("CX", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("CY", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("DK", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("SV", Pattern.compile("^1101$")));
        patterns.add(CommonUtil.toMap("ET", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("GE", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("GL", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("GW", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("HT", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("HM", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("HU", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("LB", Pattern.compile("^\\d{4}\\s{0,1}\\d{4}$")));
        patterns.add(CommonUtil.toMap("LR", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("LI", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("LU", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("MK", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("MD", Pattern.compile("^[Mm][Dd][- ]{0,1}\\d{4}$")));
        patterns.add(CommonUtil.toMap("MZ", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("NZ", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("NE", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("NF", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("NO", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("PY", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("PH", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("SI", Pattern.compile("^([Ss][Ii][- ]{0,1}){0,1}\\d{4}$")));
        patterns.add(CommonUtil.toMap("ZA", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("SJ", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("CH", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("TN", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("VE", Pattern.compile(" ^\\d{4}(\\s[a-zA-Z]{1})?$")));
        Pattern fiveDigitsPattern = Pattern.compile("^\\d{5}$");
        patterns.add(CommonUtil.toMap("AX", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("DZ", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("AS", Pattern.compile("^\\d{5}(-{1}\\d{4,6})$")));
        patterns.add(CommonUtil.toMap("BT", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("BA", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("KH", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("TD", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("CR", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("HR", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("CU", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("DO", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("EG", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("EE", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("FI", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("FR", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("GF", Pattern.compile("^973\\d{2}$")));
        patterns.add(CommonUtil.toMap("PF", Pattern.compile("^987\\d{2}$")));
        patterns.add(CommonUtil.toMap("DE", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("GP", Pattern.compile("^971\\d{2}$")));
        patterns.add(CommonUtil.toMap("GU", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("GT", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("HN", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("ID", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("IQ", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("IR", Pattern.compile("^\\d{5}-\\d{5}$")));
        patterns.add(CommonUtil.toMap("IT", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("JO", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("KE", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("XK", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("KW", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("LA", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("LY", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("LT", Pattern.compile("^[Ll][Tt][- ]{0,1}\\d{5}$")));
        patterns.add(CommonUtil.toMap("MY", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("MV", Pattern.compile("^\\d{4,5}$")));
        patterns.add(CommonUtil.toMap("MH", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("MQ", Pattern.compile("^972\\d{2}$")));
        patterns.add(CommonUtil.toMap("YT", Pattern.compile("^976\\d{2}$")));
        patterns.add(CommonUtil.toMap("MX", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("FM", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("MC", Pattern.compile("^980\\d{2}$")));
        patterns.add(CommonUtil.toMap("MN", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("ME", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("MA", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("MM", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("NP", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("NC", Pattern.compile("^988\\d{2}$")));
        patterns.add(CommonUtil.toMap("NI", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("MP", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("PK", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("PW", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("PE", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("PR", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("RE", Pattern.compile("^974\\d{2}$")));
        patterns.add(CommonUtil.toMap("SA", Pattern.compile("^\\d{5}(-{1}\\d{4})?$")));
        patterns.add(CommonUtil.toMap("BL", Pattern.compile("^97133$")));
        patterns.add(CommonUtil.toMap("MF", Pattern.compile("^97150$")));
        patterns.add(CommonUtil.toMap("PM", Pattern.compile("^97500$")));
        patterns.add(CommonUtil.toMap("AS", Pattern.compile("^\\d{5}(-{1}\\d{4,6})$")));
        patterns.add(CommonUtil.toMap("SM", Pattern.compile("^4789\\d$")));
        patterns.add(CommonUtil.toMap("SN", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("RS", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("KR", Pattern.compile("^\\d{6}\\s\\(\\d{3}-\\d{3}\\)$")));
        patterns.add(CommonUtil.toMap("SD", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("ES", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("LK", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("TW", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("TH", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("TR", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("VI", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("UA", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("UY", fiveDigitsPattern));
        patterns.add(CommonUtil.toMap("WF", Pattern.compile("^986\\d{2}$")));
        patterns.add(CommonUtil.toMap("ZM", fiveDigitsPattern));
        Pattern sixDigitsPattern = Pattern.compile("^\\d{6}$");
        patterns.add(CommonUtil.toMap("BY", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("CN", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("CO", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("EC", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("IN", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("KZ", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("KG", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("NG", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("RO", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("RU", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("SG", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("TM", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("UZ", Pattern.compile("^\\d{3} \\d{3}$")));
        patterns.add(CommonUtil.toMap("VN", sixDigitsPattern));
        patterns.add(CommonUtil.toMap("CL", Pattern.compile("^\\d{7}\\s\\(\\d{3}-\\d{4}\\)$")));
        patterns.add(CommonUtil.toMap("IL", Pattern.compile("^\\b\\d{5}(\\d{2})?$")));
        patterns.add(CommonUtil.toMap("CZ", Pattern.compile("^\\d{5}\\s\\(\\d{3}\\s\\d{2}\\)$")));
        patterns.add(CommonUtil.toMap("GR", Pattern.compile("^\\d{3}\\s{0,1}\\d{2}$")));
        patterns.add(CommonUtil.toMap("SK", Pattern.compile("^\\d{5}\\s\\(\\d{3}\\s\\d{2}\\)$")));
        patterns.add(CommonUtil.toMap("SE", Pattern.compile("^\\d{3}\\s*\\d{2}$")));
        patterns.add(CommonUtil.toMap("CC", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("NL", Pattern.compile("^\\d{4}\\s{0,1}[A-Za-z]{2}$")));
        patterns.add(CommonUtil.toMap("PL", Pattern.compile("^\\d{2}[- ]{0,1}\\d{3}$")));
        patterns.add(CommonUtil.toMap("PT", fourDigitsPattern));
        patterns.add(CommonUtil.toMap("JP", Pattern.compile("^\\d{7}\\s\\(\\d{3}-\\d{4}\\)$")));
        patterns.add(CommonUtil.toMap("BR", Pattern.compile("^\\d{5}-\\d{3}$")));
        patterns.add(CommonUtil.toMap("SZ", Pattern.compile("^[A-Za-z]\\d{3}$")));
        patterns.add(CommonUtil.toMap("AR", Pattern.compile("^\\d{4}|[A-Za-z]\\d{4}[a-zA-Z]{3}$")));
        patterns.add(CommonUtil.toMap("BM", Pattern.compile("^[A-Za-z]{2}\\s([A-Za-z]{2}|\\d{2})$")));
        patterns.add(CommonUtil.toMap("AZ", Pattern.compile("^[Aa][Zz]\\d{4}$")));
        patterns.add(CommonUtil.toMap("GG", Pattern.compile("^[A-Za-z]{2}\\d\\s{0,1}\\d[A-Za-z]{2}$")));
        patterns.add(CommonUtil.toMap("JE", Pattern.compile("^[Jj][Ee]\\d\\s{0,1}\\d[A-Za-z]{2}$")));
        patterns.add(CommonUtil.toMap("GI", Pattern.compile("^[Gg][Xx][1]{2}\\s{0,1}[1][Aa]{2}$")));
        patterns.add(CommonUtil.toMap("IM", Pattern.compile("^[Ii[Mm]\\d{1,2}\\s\\d\\[A-Z]{2}$")));
        patterns.add(CommonUtil.toMap("AD", Pattern.compile("^[Aa][Dd]\\d{3}$")));
        patterns.add(CommonUtil.toMap("VG", Pattern.compile("^[Vv][Gg]\\d{4}$")));
        patterns.add(CommonUtil.toMap("BN", Pattern.compile("^[A-Za-z]{2}\\d{4}$")));
        patterns.add(CommonUtil.toMap("VC", Pattern.compile("^[Vv][Cc]\\d{4}$")));
        patterns.add(CommonUtil.toMap("AI", Pattern.compile("^[Aa][I][-][2][6][4][0]$")));
        patterns.add(CommonUtil.toMap("LV", Pattern.compile("^[Ll][Vv][- ]{0,1}\\d{4}$")));
        patterns.add(CommonUtil.toMap("BB", Pattern.compile("^[Aa][Zz]\\d{5}$")));
        patterns.add(CommonUtil.toMap("KY", Pattern.compile("^[Kk][Yy]\\d[-\\s]{0,1}\\d{4}$")));
        patterns.add(CommonUtil.toMap("MT", Pattern.compile("^[A-Za-z]{3}\\s{0,1}\\d{4}$")));
        patterns.add(CommonUtil.toMap("PN", Pattern.compile("^[Pp][Cc][Rr][Nn]\\s{0,1}[1][Zz]{2}$")));
        patterns.add(CommonUtil.toMap("MS", Pattern.compile("^[Mm][Ss][Rr]\\s{0,1}\\d{4}$")));
        patterns.add(CommonUtil.toMap("FK", Pattern.compile("^[Ff][Ii][Qq]{2}\\s{0,1}[1][Zz]{2}$")));
        patterns.add(CommonUtil.toMap("SH", Pattern.compile("^[Ss][Tt][Hh][Ll]\\s{0,1}[1][Zz]{2}$")));
        patterns.add(CommonUtil.toMap("GS", Pattern.compile("^[Ss][Ii][Qq]{2}\\s{0,1}[1][Zz]{2}$")));
        patterns.add(CommonUtil.toMap("TC", Pattern.compile("^[Tt][Kk][Cc][Aa]\\s{0,1}[1][Zz]{2}$")));
        patterns.add(CommonUtil.toMap("JM", Pattern.compile("^\\d{2}$")));
        patterns.add(CommonUtil.toMap("BH", Pattern.compile("^\\d{3,4}$")));
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
