package org.shakticoin.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.support.annotation.NonNull;

@Entity
public class PhoneCountryCode {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name="geo_code")
    private String geoCode;

    @ColumnInfo(name="country_code")
    private String countryCode;

    public PhoneCountryCode(String geoCode, String countryCode) {
        this.geoCode = geoCode;
        this.countryCode = countryCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGeoCode() {
        return geoCode;
    }

    public void setGeoCode(String geoCode) {
        this.geoCode = geoCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @NonNull
    @Override
    public String toString() {
        return getGeoCode().toUpperCase() + " (" + getCountryCode() + ")";
    }

    public static PhoneCountryCode[] populateSeedData() {
        return new PhoneCountryCode[] {
                new PhoneCountryCode("MD", "373"), // Moldova
                new PhoneCountryCode("FR", "33"), // France
                new PhoneCountryCode("TJ", "992"), // Tajikistan
                new PhoneCountryCode("PG", "675"), // Papua New Guinea
                new PhoneCountryCode("UZ", "998"), // Uzbekistan
                new PhoneCountryCode("CH", "41"), // Switzerland
                new PhoneCountryCode("GL", "299"), // Greenland
                new PhoneCountryCode("FK", "500"), // Falkland Islands  [Malvinas]
                new PhoneCountryCode("GQ", "240"), // Equatorial Guinea
                new PhoneCountryCode("TF", "262"), // French Southern Territories
                new PhoneCountryCode("MS", "1664"), // Montserrat
                new PhoneCountryCode("BR", "55"), // Brazil
                new PhoneCountryCode("CR", "506"), // Costa Rica
                new PhoneCountryCode("MX", "52"), // Mexico
                new PhoneCountryCode("AS", "1684"), // American Samoa
                new PhoneCountryCode("NC", "687"), // New Caledonia
                new PhoneCountryCode("HU", "36"), // Hungary
                new PhoneCountryCode("TM", "993"), // Turkmenistan
                new PhoneCountryCode("GY", "592"), // Guyana
                new PhoneCountryCode("CI", "225"), // Côte d'Ivoire
                new PhoneCountryCode("TH", "66"), // Thailand
                new PhoneCountryCode("IQ", "964"), // Iraq
                new PhoneCountryCode("DM", "1767"), // Dominica
                new PhoneCountryCode("LB", "961"), // Lebanon
                new PhoneCountryCode("KR", "82"), // South Korea
                new PhoneCountryCode("MK", "389"), // Macedonia
                new PhoneCountryCode("DO", "1809"), // Dominican Republic ? why 3
                new PhoneCountryCode("DO", "1829"), // Dominican Republic
                new PhoneCountryCode("DO", "1849"), // Dominican Republic
                new PhoneCountryCode("AR", "54"), // Argentina
                new PhoneCountryCode("CW", "599"), // Curaçao
                new PhoneCountryCode("PM", "508"), // Saint Pierre and Miquelon
                new PhoneCountryCode("CF", "236"), // Central African Republic
                new PhoneCountryCode("ZM", "260"), // Zambia
                new PhoneCountryCode("SX", "1721"), // Sint Maarten (Dutch part)
                new PhoneCountryCode("CL", "56"), // Chile
                new PhoneCountryCode("TZ", "255"), // Tanzania
                new PhoneCountryCode("ET", "251"), // Ethiopia
                new PhoneCountryCode("VC", "1784"), // Saint Vincent and the Grenadines
                new PhoneCountryCode("GP", "590"), // Guadeloupe
                new PhoneCountryCode("DK", "45"), // Denmark
                new PhoneCountryCode("MU", "230"), // Mauritius
                new PhoneCountryCode("ST", "239"), // Sao Tome and Principe
                new PhoneCountryCode("GI", "350"), // Gibraltar
                new PhoneCountryCode("ID", "62"), // Indonesia
                new PhoneCountryCode("KZ", "7"), // Kazakhstan ? why the same as for Russia
                new PhoneCountryCode("SN", "221"), // Senegal
                new PhoneCountryCode("CN", "86"), // China
                new PhoneCountryCode("NA", "264"), // Namibia
                new PhoneCountryCode("EC", "593"), // Ecuador
                new PhoneCountryCode("NU", "683"), // Niue
                new PhoneCountryCode("MM", "95"), // Myanmar
                new PhoneCountryCode("BE", "32"), // Belgium
                new PhoneCountryCode("SH", "290"), // Saint Helena, Ascension and Tristan da Cunha
                new PhoneCountryCode("SH", "247"), // Saint Helena, Ascension and Tristan da Cunha
                new PhoneCountryCode("WF", "681"), // Wallis and Futuna
                new PhoneCountryCode("AZ", "994"), // Azerbaijan
                new PhoneCountryCode("LU", "352"), // Luxembourg
                new PhoneCountryCode("NG", "234"), // Nigeria
                new PhoneCountryCode("MY", "60"), // Malaysia
                new PhoneCountryCode("BW", "267"), // Botswana
                new PhoneCountryCode("AI", "1264"), // Anguilla
                new PhoneCountryCode("MW", "265"), // Malawi
                new PhoneCountryCode("RO", "40"), // Romania
                new PhoneCountryCode("CO", "57"), // Colombia
                new PhoneCountryCode("SA", "966"), // Saudi Arabia
                new PhoneCountryCode("US", "1"), // United States of America
                new PhoneCountryCode("KN", "1869"), // Saint Kitts and Nevis
                new PhoneCountryCode("TG", "228"), // Togo
                new PhoneCountryCode("BF", "226"), // Burkina Faso
                new PhoneCountryCode("TV", "688"), // Tuvalu
                new PhoneCountryCode("SG", "65"), // Singapore
                new PhoneCountryCode("OM", "968"), // Oman
                new PhoneCountryCode("RW", "250"), // Rwanda
                new PhoneCountryCode("UA", "380"), // Ukraine
                new PhoneCountryCode("CK", "682"), // Cook Islands
                new PhoneCountryCode("KM", "269"), // Comoros
                new PhoneCountryCode("NP", "977"), // Nepal
                new PhoneCountryCode("AG", "1268"), // Antigua and Barbuda
                new PhoneCountryCode("PY", "595"), // Paraguay
                new PhoneCountryCode("SY", "963"), // Syria
                new PhoneCountryCode("MH", "692"), // Marshall Islands
                new PhoneCountryCode("TC", "1649"), // Turks and Caicos Islands
                new PhoneCountryCode("AO", "244"), // Angola
                new PhoneCountryCode("MO", "853"), // Macao
                new PhoneCountryCode("HT", "509"), // Haiti
                new PhoneCountryCode("LT", "370"), // Lithuania
                new PhoneCountryCode("KG", "996"), // Kyrgyzstan
                new PhoneCountryCode("BT", "975"), // Bhutan
                new PhoneCountryCode("GE", "995"), // Georgia
                new PhoneCountryCode("PR", "1787"), // Puerto Rico
                new PhoneCountryCode("PR", "1939"), // Puerto Rico
                new PhoneCountryCode("JM", "1876"), // Jamaica
                new PhoneCountryCode("FO", "298"), // Faroe Islands
                new PhoneCountryCode("AU", "61"), // Australia
                new PhoneCountryCode("CU", "53"), // Cuba
                new PhoneCountryCode("BA", "387"), // Bosnia and Herzegovina
                new PhoneCountryCode("GW", "245"), // Guinea-Bissau
                new PhoneCountryCode("MA", "212"), // Morocco
                new PhoneCountryCode("SB", "677"), // Solomon Islands
                new PhoneCountryCode("SV", "503"), // El Salvador
                new PhoneCountryCode("LR", "231"), // Liberia
                new PhoneCountryCode("GM", "220"), // Gambia
                new PhoneCountryCode("CZ", "420"), // Czechia
                new PhoneCountryCode("GN", "224"), // Guinea
                new PhoneCountryCode("CM", "237"), // Cameroon
                new PhoneCountryCode("LV", "371"), // Latvia
                new PhoneCountryCode("KW", "965"), // Kuwait
                new PhoneCountryCode("ML", "223"), // Mali
                new PhoneCountryCode("MR", "222"), // Mauritania
                new PhoneCountryCode("QA", "974"), // Qatar
                new PhoneCountryCode("BD", "880"), // Bangladesh
                new PhoneCountryCode("AL", "355"), // Albania
                new PhoneCountryCode("SK", "421"), // Slovakia
                new PhoneCountryCode("JO", "962"), // Jordan
                new PhoneCountryCode("SE", "46"), // Sweden
                new PhoneCountryCode("DZ", "213"), // Algeria
                new PhoneCountryCode("ZW", "263"), // Zimbabwe
                new PhoneCountryCode("BJ", "229"), // Benin
                new PhoneCountryCode("HK", "852"), // Hong Kong
                new PhoneCountryCode("TR", "90"), // Turkey
                new PhoneCountryCode("BO", "591"), // Bolivia
                new PhoneCountryCode("WS", "685"), // Samoa
                new PhoneCountryCode("HR", "385"), // Croatia
                new PhoneCountryCode("GA", "241"), // Gabon
                new PhoneCountryCode("MN", "976"), // Mongolia
                new PhoneCountryCode("BS", "1242"), // Bahamas
                new PhoneCountryCode("GH", "233"), // Ghana
                new PhoneCountryCode("MT", "356"), // Malta
                new PhoneCountryCode("MC", "377"), // Monaco
                new PhoneCountryCode("GD", "1473"), // Grenada
                new PhoneCountryCode("CD", "243"), // Congo, Dem. Rep
                new PhoneCountryCode("FI", "358"), // Finland
                new PhoneCountryCode("PL", "48"), // Poland
                new PhoneCountryCode("NE", "227"), // Niger
                new PhoneCountryCode("TL", "670"), // Timor-Leste
                new PhoneCountryCode("PA", "507"), // Panama
                new PhoneCountryCode("CV", "238"), // Cabo Verde
                new PhoneCountryCode("NF", "672"), // Norfolk Island
                new PhoneCountryCode("LA", "856"), // Laos
                new PhoneCountryCode("TT", "1868"), // Trinidad and Tobago
                new PhoneCountryCode("KE", "254"), // Kenya
                new PhoneCountryCode("FJ", "679"), // Fiji
                new PhoneCountryCode("VI", "1340"), // Virgin Islands (US)
                new PhoneCountryCode("AT", "43"), // Austria
                new PhoneCountryCode("SL", "232"), // Sierra Leone
                new PhoneCountryCode("BZ", "501"), // Belize
                new PhoneCountryCode("UG", "256"), // Uganda
                new PhoneCountryCode("PT", "351"), // Portugal
                new PhoneCountryCode("MP", "1670"), // Northern Mariana Islands
                new PhoneCountryCode("TD", "235"), // Chad
                new PhoneCountryCode("VG", "1284"), // Virgin Islands (British)
                new PhoneCountryCode("IS", "354"), // Iceland
                new PhoneCountryCode("TN", "216"), // Tunisia
                new PhoneCountryCode("BB", "1246"), // Barbados
                new PhoneCountryCode("ES", "34"), // Spain
                new PhoneCountryCode("BQ", "599"), // Bonaire, Sint Eustatius and Saba
                new PhoneCountryCode("LK", "94"), // Sri Lanka
                new PhoneCountryCode("SM", "378"), // San Marino
                new PhoneCountryCode("CA", "1"), // Canada
                new PhoneCountryCode("KP", "850"), // North Korea
                new PhoneCountryCode("KI", "686"), // Kiribati
                new PhoneCountryCode("VU", "678"), // Vanuatu
                new PhoneCountryCode("JP", "81"), // Japan
                new PhoneCountryCode("NI", "505"), // Nicaragua
                new PhoneCountryCode("BM", "1441"), // Bermuda
                new PhoneCountryCode("MQ", "596"), // Martinique
                new PhoneCountryCode("BN", "673"), // Brunei
                new PhoneCountryCode("UY", "598"), // Uruguay
                new PhoneCountryCode("KH", "855"), // Cambodia
                new PhoneCountryCode("SR", "597"), // Suriname
                new PhoneCountryCode("CG", "242"), // Congo
                new PhoneCountryCode("SI", "386"), // Slovenia
                new PhoneCountryCode("BH", "973"), // Bahrain
                new PhoneCountryCode("TK", "690"), // Tokelau
                new PhoneCountryCode("ME", "382"), // Montenegro
                new PhoneCountryCode("DE", "49"), // Germany
                new PhoneCountryCode("CY", "357"), // Cyprus
                new PhoneCountryCode("MG", "261"), // Madagascar
                new PhoneCountryCode("RU", "7"), // Russia
                new PhoneCountryCode("GR", "30"), // Greece
                new PhoneCountryCode("ER", "291"), // Eritrea
                new PhoneCountryCode("PE", "51"), // Peru
                new PhoneCountryCode("BI", "257"), // Burundi
                new PhoneCountryCode("AE", "971"), // United Arab Emirates
                new PhoneCountryCode("GT", "502"), // Guatemala
                new PhoneCountryCode("SC", "248"), // Seychelles
                new PhoneCountryCode("SS", "211"), // South Sudan
                new PhoneCountryCode("KY", "1345"), // Cayman Islands
                new PhoneCountryCode("EG", "20"), // Egypt
                new PhoneCountryCode("AM", "374"), // Armenia
                new PhoneCountryCode("VN", "84"), // Vietnam
                new PhoneCountryCode("BY", "375"), // Belarus
                new PhoneCountryCode("LI", "423"), // Liechtenstein
                new PhoneCountryCode("HN", "504"), // Honduras
                new PhoneCountryCode("PH", "63"), // Philippines
                new PhoneCountryCode("IE", "353"), // Ireland
                new PhoneCountryCode("AW", "297"), // Aruba
                new PhoneCountryCode("PF", "689"), // French Polynesia
                new PhoneCountryCode("GF", "594"), // French Guiana
                new PhoneCountryCode("SO", "252"), // Somalia
                new PhoneCountryCode("PW", "680"), // Palau
                new PhoneCountryCode("DJ", "253"), // Djibouti
                new PhoneCountryCode("NO", "47"), // Norway
                new PhoneCountryCode("GB", "44"), // United Kingdom
                new PhoneCountryCode("ZA", "27"), // South Africa
                new PhoneCountryCode("LS", "266"), // Lesotho
                new PhoneCountryCode("RS", "381"), // Serbia
                new PhoneCountryCode("NR", "674"), // Nauru
                new PhoneCountryCode("LY", "218"), // Libya
                new PhoneCountryCode("VE", "58"), // Venezuela
                new PhoneCountryCode("IT", "39"), // Italy
                new PhoneCountryCode("GU", "1671"), // Guam
                new PhoneCountryCode("YE", "967"), // Yemen
                new PhoneCountryCode("TW", "886"), // Taiwan
                new PhoneCountryCode("AF", "93"), // Afghanistan
                new PhoneCountryCode("IN", "91"), // India
                new PhoneCountryCode("LC", "1758"), // Saint Lucia
                new PhoneCountryCode("NL", "31"), // Netherlands
                new PhoneCountryCode("FM", "691"), // Micronesia
                new PhoneCountryCode("TO", "676"), // Tonga
                new PhoneCountryCode("MV", "960"), // Maldives
                new PhoneCountryCode("MZ", "258"), // Mozambique
                new PhoneCountryCode("AD", "376"), // Andorra
                new PhoneCountryCode("EE", "372"), // Estonia
                new PhoneCountryCode("NZ", "64"), // New Zealand
                new PhoneCountryCode("BG", "359"), // Bulgaria
                new PhoneCountryCode("PK", "92"), // Pakistan
                new PhoneCountryCode("IL", "972"), // Israel
                new PhoneCountryCode("SD", "249"), // Sudan
                new PhoneCountryCode("IR", "98") // Iran
        };
    }
}
