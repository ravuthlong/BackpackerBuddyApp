package ravtrix.backpackerbuddy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ravtrix.backpackerbuddy.TabAdapter.ViewPagerAdapter;

/**
 * Created by Ravinder on 3/29/16.
 */
public class TabFragment extends Fragment implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener  {
    private ViewPager viewPager;
    private TabHost tabHost;
    private View v;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_tabs, container, false);

        setHasOptionsMenu(true);
        initTabHost();
        initViewPager();
        setTabFont();
        return v;
    }

    public class TabContent implements TabHost.TabContentFactory {
        Context context;

        public TabContent(Context context) {
            this.context = context;
        }

        // Defining the tab
        @Override
        public View createTabContent(String tag) {
            View currentView = new View(context);
            currentView.setMinimumHeight(0);
            currentView.setMinimumWidth(0);
            return currentView;
        }
    }

    // Set view pager with the two fragment tabs and set listener to it
    private void initViewPager() {
        List<Fragment> listFrag = new ArrayList<>();
        listFrag.add(new UserCountriesFragment());
        listFrag.add(new UserInboxFragment());

        this.viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), listFrag);
        this.viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(this);
    }

    // Populate the tab host with two tabs and set listener
    private void initTabHost() {
        tabHost = (TabHost) v.findViewById(R.id.tabHost);
        tabHost.setup();

        String[] tabNames = {"Countries", "Inbox"};

        for (int i = 0; i < tabNames.length; i++) {
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabNames[i]);
            tabSpec.setIndicator(tabNames[i]);
            tabSpec.setContent(new TabContent(getActivity()));
            tabHost.addTab(tabSpec);
        }
        tabHost.setOnTabChangedListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabChanged(String tabId) {
        int selectedTab = tabHost.getCurrentTab();
        viewPager.setCurrentItem(selectedTab);
    }

    // Set font size and type for the tabs
    private void setTabFont() {
        //Typeface menufont = Typeface.createFromAsset(getActivity().getAssets(), "Menufont.ttf");

        for (int i = 0; i <= tabHost.getChildCount(); i++) {
            TextView tab = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tab.setTextSize(16);
            //tab.setTypeface(menufont);
        }
    }
}
