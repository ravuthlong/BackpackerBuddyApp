package ravtrix.backpackerbuddy.fragments.userdestinationfrag;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.backpackerbuddy.R;
import ravtrix.backpackerbuddy.fragments.userdestinationfrag.countrybyfilter.CountryFilterFragment;
import ravtrix.backpackerbuddy.fragments.userdestinationfrag.countrybytime.CountryRecentFragment;

/**
 * Created by Ravinder on 10/9/16.
 */

public class CountryTabFragment extends Fragment {

    @BindView(R.id.viewpager) protected ViewPager viewPager;
    @BindView(R.id.tabs) protected TabLayout tabLayout;
    private CountryTabFragment.ViewPagerAdapter adapter;
    private CountryRecentFragment countryRecentFragment;
    private CountryFilterFragment countryFilterFragment;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tabs, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);
        setRetainInstance(true);

        this.countryRecentFragment = new CountryRecentFragment();
        this.countryFilterFragment = new CountryFilterFragment();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        if (this.bundle != null) {
            this.countryRecentFragment.setReceivedQueryBundle(bundle);
            adapter.changeTitle();
            adapter.notifyDataSetChanged();
        }

        new Handler().postDelayed(
                new Runnable(){
                    @Override
                    public void run() {
                        tabLayout.getTabAt(0).select();
                    }
                }, 100);
        return v;
    }

    public void setHasBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    /**
     * Set up the two tabs, one being the "Recent Posts" and the other being "Filter Tools"
     * @param viewPager         the viewpager for the tabs
     */
    private void setupViewPager(final ViewPager viewPager) {
        adapter = new CountryTabFragment.ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(this.countryRecentFragment, "Recent Posts");
        adapter.addFragment(this.countryFilterFragment, "Filter Tools");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        void changeTitle() {
            adapter.mFragmentTitleList.set(0, "Filtered Posts");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) { // refresh from edit post
                Fragment fragment = adapter.getItem(0);
                ((CountryRecentFragment) fragment).refreshPage();
            }
        }
    }

    // Refresh is called directly to fragment
    public void refreshAUserCountryTab() {
        Fragment fragment = adapter.getItem(0);
        ((CountryRecentFragment) fragment).refreshPage();
    }

}
