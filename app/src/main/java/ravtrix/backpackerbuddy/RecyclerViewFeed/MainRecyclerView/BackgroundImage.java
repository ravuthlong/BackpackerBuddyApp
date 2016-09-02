package ravtrix.backpackerbuddy.recyclerviewfeed.mainrecyclerview;

import java.util.HashMap;

import ravtrix.backpackerbuddy.R;

/**
 * Created by Ravinder on 4/5/16.
 */
public class BackgroundImage {
    private HashMap<String, Integer> backGroundHashMap;

    public BackgroundImage () {
        backGroundHashMap = new HashMap<>();
        setBackgroundHashMap();
    }

    private void setBackgroundHashMap() {
        backGroundHashMap.put("Afghanistan", R.drawable.afghanistan);
        backGroundHashMap.put("Cambodia", R.drawable.cambodia);
    }

    public int getBackgroundFromHash(String countryName) {
        return backGroundHashMap.get(countryName);
    }
}