package ravtrix.backpackerbuddy.fragments.mainfrag;

import android.os.Bundle;
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
import ravtrix.backpackerbuddy.fragments.mainfrag.countrybyfilter.CountryFilterFragment;
import ravtrix.backpackerbuddy.fragments.mainfrag.countrybytime.CountryRecentFragment;

/**
 * Created by Ravinder on 10/9/16.
 */

public class CountryTabFragment extends Fragment {

    @BindView(R.id.viewpager) protected ViewPager viewPager;
    @BindView(R.id.tabs) protected TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tabs, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }


    private void setupViewPager(final ViewPager viewPager) {
        final CountryTabFragment.ViewPagerAdapter adapter = new CountryTabFragment.ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new CountryRecentFragment(), "Recent Posts");
        adapter.addFragment(new CountryFilterFragment(), "Filter");
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

    }


}
