package ravtrix.backpackerbuddy.models;

import java.util.HashMap;

import ravtrix.backpackerbuddy.activities.usermap.model.Location;

/**
 * Created by Ravinder on 3/6/17.
 */

public class LocationHash {

    private HashMap<String, Location> locationHashMap;

    public LocationHash() {
        locationHashMap = new HashMap<>();
        initCountryHash(locationHashMap);
    }

    public HashMap<String, Location> getLocationHashMap() {
        return locationHashMap;
    }

    private void initCountryHash(HashMap<String, Location> locationHashMap) {

        locationHashMap.put("Afghanistan", new Location(34.53, 69.17));
        locationHashMap.put("Albania", new Location(41.33, 19.82));
        locationHashMap.put("Algeria", new Location(36.75, 3.04));
        locationHashMap.put("Andorra", new Location(42.51, 1.52));
        locationHashMap.put("Angola", new Location(-8.84, 13.23));
        locationHashMap.put("Argentina", new Location(-34.61, -58.38));
        locationHashMap.put("Armenia", new Location(40.18, 44.51));
        locationHashMap.put("Aruba", new Location(12.52, -70.03));
        locationHashMap.put("Australia", new Location(-35.28, 149.13));
        locationHashMap.put("Austria", new Location(48.21, 16.37));
        locationHashMap.put("Azerbaijan", new Location(40.38, 49.89));
        locationHashMap.put("Bahamas", new Location(25.06, -77.34));
        locationHashMap.put("Bahrain", new Location(26.22, 50.58));
        locationHashMap.put("Bangladesh", new Location(23.71, 90.41));
        locationHashMap.put("Barbados", new Location(13.10, -59.62));
        locationHashMap.put("Belarus", new Location(53.90, 27.57));
        locationHashMap.put("Belgium", new Location(50.85, 4.35));
        locationHashMap.put("Belize", new Location(17.25, -88.77));
        locationHashMap.put("Benin", new Location(6.50, 2.60));
        locationHashMap.put("Bhutan", new Location(27.47, 89.64));
        locationHashMap.put("Bolivia", new Location(-19.03, -65.26));
        locationHashMap.put("Bosnia and Herz", new Location(43.85, 18.36));
        locationHashMap.put("Botswana", new Location(-24.65, 25.91));

        locationHashMap.put("Brazil", new Location(-15.78, -47.93));
        locationHashMap.put("British V. Islands", new Location(12.52, -70.03));
        locationHashMap.put("Brunei", new Location(12.52, -70.03));
        locationHashMap.put("Bulgaria", new Location(12.52, -70.03));
        locationHashMap.put("Burundi", new Location(12.52, -70.03));
        locationHashMap.put("Cambodia", new Location(12.52, -70.03));
        locationHashMap.put("Cameroon", new Location(12.52, -70.03));
        locationHashMap.put("Canada", new Location(12.52, -70.03));
        locationHashMap.put("Cape Verde", new Location(12.52, -70.03));
        locationHashMap.put("Cayman Islands", new Location(12.52, -70.03));
        locationHashMap.put("Chad", new Location(12.52, -70.03));
        locationHashMap.put("Chile", new Location(12.52, -70.03));
        locationHashMap.put("China", new Location(12.52, -70.03));
        locationHashMap.put("Colombia", new Location(12.52, -70.03));
        locationHashMap.put("Comoros", new Location(12.52, -70.03));
        locationHashMap.put("Congo", new Location(12.52, -70.03));
        locationHashMap.put("Costa Rica", new Location(12.52, -70.03));
        locationHashMap.put("Croatia", new Location(12.52, -70.03));
        locationHashMap.put("Cuba", new Location(12.52, -70.03));
        locationHashMap.put("Curacao", new Location(12.52, -70.03));
        locationHashMap.put("Cyprus", new Location(12.52, -70.03));
        locationHashMap.put("Czech Rep.", new Location(12.52, -70.03));
        locationHashMap.put("Denmark", new Location(12.52, -70.03));
        locationHashMap.put("Djibouti", new Location(12.52, -70.03));
        locationHashMap.put("Dominica", new Location(12.52, -70.03));
        locationHashMap.put("Dominican Rep.", new Location(12.52, -70.03));
        locationHashMap.put("Ecuador", new Location(12.52, -70.03));
        locationHashMap.put("Egypt", new Location(12.52, -70.03));
        locationHashMap.put("El Salvador", new Location(12.52, -70.03));
        locationHashMap.put("Equatorial G.", new Location(12.52, -70.03));
        locationHashMap.put("Eritrea", new Location(12.52, -70.03));
        locationHashMap.put("Estonia", new Location(12.52, -70.03));
        locationHashMap.put("Ethiopia", new Location(12.52, -70.03));
        locationHashMap.put("Fiji", new Location(12.52, -70.03));
        locationHashMap.put("Finland", new Location(12.52, -70.03));
        locationHashMap.put("France", new Location(12.52, -70.03));
        locationHashMap.put("Gabon", new Location(12.52, -70.03));
        locationHashMap.put("Gambia", new Location(12.52, -70.03));
        locationHashMap.put("Georgia", new Location(12.52, -70.03));
        locationHashMap.put("Germany", new Location(12.52, -70.03));
        locationHashMap.put("Ghana", new Location(12.52, -70.03));
        locationHashMap.put("Greece", new Location(12.52, -70.03));
        locationHashMap.put("Greenland", new Location(12.52, -70.03));
        locationHashMap.put("Grenada", new Location(12.52, -70.03));
        locationHashMap.put("Guadeloupe", new Location(12.52, -70.03));
        locationHashMap.put("Guam", new Location(12.52, -70.03));
        locationHashMap.put("Guatemala", new Location(12.52, -70.03));
        locationHashMap.put("Guinea", new Location(12.52, -70.03));
        locationHashMap.put("Guyana", new Location(12.52, -70.03));
        locationHashMap.put("Haiti", new Location(12.52, -70.03));
        locationHashMap.put("Honduras", new Location(12.52, -70.03));
        locationHashMap.put("Hong Kong", new Location(12.52, -70.03));
        locationHashMap.put("Hungary", new Location(12.52, -70.03));
        locationHashMap.put("Iceland", new Location(12.52, -70.03));
        locationHashMap.put("India", new Location(12.52, -70.03));
        locationHashMap.put("Indonesia", new Location(12.52, -70.03));
        locationHashMap.put("Iran", new Location(12.52, -70.03));
        locationHashMap.put("Iraq", new Location(12.52, -70.03));
        locationHashMap.put("Ireland", new Location(12.52, -70.03));
        locationHashMap.put("Israel", new Location(12.52, -70.03));
        locationHashMap.put("Italy", new Location(12.52, -70.03));
        locationHashMap.put("Ivory Coast", new Location(12.52, -70.03));
        locationHashMap.put("Jamaica", new Location(12.52, -70.03));
        locationHashMap.put("Japan", new Location(12.52, -70.03));
        locationHashMap.put("Jordan", new Location(12.52, -70.03));
        locationHashMap.put("Kazakhstan", new Location(12.52, -70.03));
        locationHashMap.put("Kenya", new Location(12.52, -70.03));
        locationHashMap.put("Kiribati", new Location(12.52, -70.03));
        locationHashMap.put("Kuwait", new Location(12.52, -70.03));
        locationHashMap.put("Kosovo", new Location(12.52, -70.03));
        locationHashMap.put("Kyrgyzstan", new Location(12.52, -70.03));
        locationHashMap.put("Laos", new Location(12.52, -70.03));
        locationHashMap.put("Latvia", new Location(12.52, -70.03));
        locationHashMap.put("Lebanon", new Location(12.52, -70.03));
        locationHashMap.put("Lesotho", new Location(12.52, -70.03));
        locationHashMap.put("Liberia", new Location(12.52, -70.03));
        locationHashMap.put("Libya", new Location(12.52, -70.03));
        locationHashMap.put("Liechtenstein", new Location(12.52, -70.03));
        locationHashMap.put("Lithuania", new Location(12.52, -70.03));
        locationHashMap.put("Luxembourg", new Location(12.52, -70.03));
        locationHashMap.put("Macau", new Location(12.52, -70.03));
        locationHashMap.put("Macedonia", new Location(12.52, -70.03));
        locationHashMap.put("Madagascar", new Location(12.52, -70.03));
        locationHashMap.put("Malawi", new Location(12.52, -70.03));
        locationHashMap.put("Malaysia", new Location(12.52, -70.03));
        locationHashMap.put("Maldives", new Location(12.52, -70.03));
        locationHashMap.put("Mali", new Location(12.52, -70.03));
        locationHashMap.put("Malta", new Location(12.52, -70.03));
        locationHashMap.put("Martinique", new Location(12.52, -70.03));
        locationHashMap.put("Mauritania", new Location(12.52, -70.03));
        locationHashMap.put("Mauritius", new Location(12.52, -70.03));
        locationHashMap.put("Mexico", new Location(12.52, -70.03));
        locationHashMap.put("Moldova", new Location(12.52, -70.03));
        locationHashMap.put("Monaco", new Location(12.52, -70.03));
        locationHashMap.put("Mongolia", new Location(12.52, -70.03));
        locationHashMap.put("Montenegro", new Location(12.52, -70.03));
        locationHashMap.put("Morocco", new Location(12.52, -70.03));
        locationHashMap.put("Mozambique", new Location(12.52, -70.03));
        locationHashMap.put("Myanmar", new Location(12.52, -70.03));
        locationHashMap.put("Namibia", new Location(12.52, -70.03));
        locationHashMap.put("Nauru", new Location(12.52, -70.03));
        locationHashMap.put("Nepal", new Location(12.52, -70.03));
        locationHashMap.put("Netherlands", new Location(12.52, -70.03));
        locationHashMap.put("New Zealand", new Location(12.52, -70.03));
        locationHashMap.put("Nicaragua", new Location(12.52, -70.03));
        locationHashMap.put("Niger", new Location(12.52, -70.03));
        locationHashMap.put("Nigeria", new Location(12.52, -70.03));
        locationHashMap.put("North Korea", new Location(12.52, -70.03));
        locationHashMap.put("Norway", new Location(12.52, -70.03));
        locationHashMap.put("Oman", new Location(12.52, -70.03));
        locationHashMap.put("Pakistan", new Location(12.52, -70.03));
        locationHashMap.put("Panama", new Location(12.52, -70.03));
        locationHashMap.put("Papua N. G.", new Location(12.52, -70.03));
        locationHashMap.put("Paraguay", new Location(12.52, -70.03));
        locationHashMap.put("Peru", new Location(12.52, -70.03));
        locationHashMap.put("Philippines", new Location(12.52, -70.03));
        locationHashMap.put("Poland", new Location(12.52, -70.03));
        locationHashMap.put("Portugal", new Location(12.52, -70.03));
        locationHashMap.put("Puerto Rico", new Location(12.52, -70.03));
        locationHashMap.put("Qatar", new Location(12.52, -70.03));
        locationHashMap.put("Romania", new Location(12.52, -70.03));
        locationHashMap.put("Russia", new Location(12.52, -70.03));
        locationHashMap.put("Rwanda", new Location(12.52, -70.03));
        locationHashMap.put("Saint Lucia", new Location(12.52, -70.03));
        locationHashMap.put("Samoa", new Location(12.52, -70.03));
        locationHashMap.put("Saudi Arabia", new Location(12.52, -70.03));
        locationHashMap.put("Senegal", new Location(12.52, -70.03));
        locationHashMap.put("Serbia", new Location(12.52, -70.03));
        locationHashMap.put("Sierra Leone", new Location(12.52, -70.03));
        locationHashMap.put("Singapore", new Location(12.52, -70.03));
        locationHashMap.put("Slovakia", new Location(12.52, -70.03));
        locationHashMap.put("Slovenia", new Location(12.52, -70.03));
        locationHashMap.put("Solomon", new Location(12.52, -70.03));
        locationHashMap.put("Somalia", new Location(12.52, -70.03));
        locationHashMap.put("South Africa", new Location(12.52, -70.03));
        locationHashMap.put("South Korea", new Location(12.52, -70.03));
        locationHashMap.put("Spain", new Location(12.52, -70.03));
        locationHashMap.put("Sri Lanka", new Location(12.52, -70.03));
        locationHashMap.put("Sudan", new Location(12.52, -70.03));
        locationHashMap.put("Suriname", new Location(12.52, -70.03));
        locationHashMap.put("Swaziland", new Location(12.52, -70.03));
        locationHashMap.put("Sweden", new Location(12.52, -70.03));
        locationHashMap.put("Switzerland", new Location(12.52, -70.03));
        locationHashMap.put("Syria", new Location(12.52, -70.03));
        locationHashMap.put("Taiwan", new Location(12.52, -70.03));
        locationHashMap.put("Tajikistan", new Location(12.52, -70.03));
        locationHashMap.put("Tanzania", new Location(12.52, -70.03));
        locationHashMap.put("Thailand", new Location(12.52, -70.03));
        locationHashMap.put("Togo", new Location(12.52, -70.03));
        locationHashMap.put("Tonga", new Location(12.52, -70.03));
        locationHashMap.put("Trin And Toba", new Location(12.52, -70.03));
        locationHashMap.put("Tunisia", new Location(12.52, -70.03));
        locationHashMap.put("Turkey", new Location(12.52, -70.03));
        locationHashMap.put("Turk And Cai", new Location(12.52, -70.03));
        locationHashMap.put("Tuvalu", new Location(12.52, -70.03));
        locationHashMap.put("Uganda", new Location(12.52, -70.03));
        locationHashMap.put("Ukraine", new Location(12.52, -70.03));
        locationHashMap.put("United A. Em.", new Location(12.52, -70.03));
        locationHashMap.put("United Kingdom", new Location(12.52, -70.03));
        locationHashMap.put("United States", new Location(12.52, -70.03));
        locationHashMap.put("U.S. V. Island", new Location(12.52, -70.03));
        locationHashMap.put("Uruguay", new Location(12.52, -70.03));
        locationHashMap.put("Uzbekistan", new Location(12.52, -70.03));
        locationHashMap.put("Vanuatu", new Location(12.52, -70.03));
        locationHashMap.put("Venezuela", new Location(12.52, -70.03));
        locationHashMap.put("Vietnam", new Location(12.52, -70.03));
        locationHashMap.put("Yemen", new Location(12.52, -70.03));
        locationHashMap.put("Zambia", new Location(12.52, -70.03));
        locationHashMap.put("Zimbabwe", new Location(12.52, -70.03));

    }
}
