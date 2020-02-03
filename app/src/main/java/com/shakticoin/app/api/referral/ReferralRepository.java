package com.shakticoin.app.api.referral;

import androidx.annotation.NonNull;

import com.shakticoin.app.api.BackendRepository;
import com.shakticoin.app.api.OnCompleteListener;
import com.shakticoin.app.api.referral.model.EffortRate;
import com.shakticoin.app.api.referral.model.Referral;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.shakticoin.app.api.referral.model.EffortRate.*;

public class ReferralRepository extends BackendRepository {

    /**
     * Returns summary of the effort rate data.
     */
    public void getEffortRates(@NonNull OnCompleteListener<List<EffortRate>> listener) {
        // TODO: return real data when API is ready

        // mockup data
        ArrayList<EffortRate> resultList = new ArrayList<>();
        resultList.add(new EffortRate(LEAD_SOURCE_FACEBOOK, 64, 20, 16));
        resultList.add(new EffortRate(LEAD_SOURCE_INSTAGRAM, 34, 29, 37));
        resultList.add(new EffortRate(LEAD_SOURCE_PLUS, 34, 29, 37));
        resultList.add(new EffortRate(LEAD_SOURCE_LINKEDIN, 20, 34, 46));
        resultList.add(new EffortRate(LEAD_SOURCE_TWITTER, 0, 42, 58));
        resultList.add(new EffortRate(LEAD_SOURCE_PINTEREST, 20, 50, 30));
        resultList.add(new EffortRate(LEAD_SOURCE_SKYPE, 20, 20, 20)); // not 100, error for testing
        resultList.add(new EffortRate(LEAD_SOURCE_VK, 20, 50, 30));
        resultList.add(new EffortRate(LEAD_SOURCE_WECHAT, 20, 50, 30));
        resultList.add(new EffortRate(LEAD_SOURCE_TUMBLR, 20, 50, 30));
        resultList.add(new EffortRate(LEAD_SOURCE_EMAIL, 20, 50, 30));
        resultList.add(new EffortRate(LEAD_SOURCE_UNKNOWN, 3, 97, 0));

        listener.onComplete(resultList, null);
    }

    /**
     * Return personal data for user's referrals.
     * @param status One of the EffortRate LEAD_STATUS constants.
     */
    public void getReferrals(Integer status, @NonNull OnCompleteListener<List<Referral>> listener) {
        // TODO: return real data when API is ready
        // mockup data
        ArrayList<Referral> referrals = new ArrayList<>();
        referrals.add(new Referral("Veera", "Frye", "frye@example.com", "1 (333)9999999", LEAD_STATUS_CONVERTED));
        referrals.add(new Referral("Gilberte", "Van Dalen", "dalen@example.com", "1 (333)9999999", LEAD_STATUS_INFLUENCED));
        referrals.add(new Referral("Tito", "Arnolfi", "arnlfi@example.com", "1 (333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Widad", "O\'Hanigan", "ohanigan@example.com", "1 (333)9999999", LEAD_STATUS_INFLUENCED));
        referrals.add(new Referral("Arnie", "Olguín", "olguin@example.com", "1 (333)9999999", LEAD_STATUS_CONVERTED));
        referrals.add(new Referral("Achim", "Ingesson", "ingesson@example.com", "1 (333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Julinha", "MacDermott", "dermott@example.com", "1 (333)9999999", LEAD_STATUS_INFLUENCED));
        referrals.add(new Referral("Yvonne", "DeGarmo", "degarmo@example.com", "1 (333)9999999", LEAD_STATUS_INFLUENCED));
        referrals.add(new Referral("Oddvar", "Rivers", "rivers@example.com", "1 (333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Taťána", "Antonsen", "antonsen@example.com", "1(333)9999999", LEAD_STATUS_CONVERTED));
        referrals.add(new Referral("Áine", "Cummins", "cummins@example.com", "1 (333)9999999", LEAD_STATUS_CONVERTED));
        referrals.add(new Referral("Odhiambo", "Mandel", "mandel@example.com", "1(333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Marcas", "Giunta", "guinta@example.com", "1 (333)9999999", LEAD_STATUS_INFLUENCED));
        referrals.add(new Referral("Freya", "Janda", "janda@example.com", "1 (333)9999999", LEAD_STATUS_INFLUENCED));
        referrals.add(new Referral("Gracja", "Kron", "kron@example.com", "1 (333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Anwer", "Gaspari", "gaspari@example.com", "1 (333)9999999", LEAD_STATUS_CONVERTED));
        referrals.add(new Referral("Rachel", "Habicht", "habiht@example.com", "1 (333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Niklas", "Kaube", "kaube@example.com", "1 (333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Krzyś", "Tang", "tang@example.com", "1 (333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Elisabeth", "Norwood", "norwood@example.com", "1 (333)9999999", LEAD_STATUS_CONVERTED));
        referrals.add(new Referral("Alois", "Jonkheer", "jankheer@example.com", "1 (333)9999999", LEAD_STATUS_CONVERTED));
        referrals.add(new Referral("Ariella", "Pellegrino", "pellegrino@example.com", "1 (333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Willemijn", "Rademakers", "rademakers@example.com", "1 (333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Esmail", "Eccleston", "eccleston@example.com", "1 (333)9999999", LEAD_STATUS_PROGRESSING));
        referrals.add(new Referral("Hector", "Ó Baoghill", "baoghill@example.com", "1 (333)9999999", LEAD_STATUS_CONVERTED));

        // sort them by name
        Collections.sort(referrals, new Comparator<Referral>() {
            @Override
            public int compare(Referral o1, Referral o2) {
                StringBuilder sb1 = new StringBuilder();
                if (o1.getFirstName() != null) {
                    sb1.append(o1.getFirstName()).append(" ");
                }
                sb1.append(o1.getLastName());
                StringBuilder sb2 = new StringBuilder();
                if (o2.getFirstName() != null) {
                    sb2.append(o2.getFirstName()).append(" ");
                }
                sb2.append(o2.getLastName());
                return sb1.toString().compareTo(sb2.toString());
            }
        });

        listener.onComplete(referrals, null);
    }
}
