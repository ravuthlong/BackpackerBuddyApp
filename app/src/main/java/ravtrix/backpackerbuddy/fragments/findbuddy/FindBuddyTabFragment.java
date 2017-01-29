package ravtrix.backpackerbuddy.fragments.findbuddy;

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
import ravtrix.backpackerbuddy.fragments.findbuddy.findbuddynear.FindBuddyNearFragment;
import ravtrix.backpackerbuddy.fragments.findbuddy.findbuddyrecentlyonline.RecentlyOnlineUsersFragment;

/**
 * Created by Ravinder on 3/29/16.
 */

public class FindBuddyTabFragment extends Fragment  {
    @BindView(R.id.viewpager) protected ViewPager viewPager;
    @BindView(R.id.tabs) protected TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tabs, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        new Handler().postDelayed(
                new Runnable(){
                    @Override
                    public void run() {
                        tabLayout.getTabAt(0).select();
                    }
                }, 100);

        return v;
    }

    private void setupViewPager(final ViewPager viewPager) {

        final ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(new RecentlyOnlineUsersFragment(), "Recently Online");
        adapter.addFragment(new FindBuddyNearFragment(), "Nearby");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // scrolling
            }

            /*
             * Creates a communication between the two tabs so it keeps the state of selected drop down
             * in FindBuddyNearFragment
             */
            @Override
            public void onPageSelected(int position) {
                RecentlyOnlineUsersFragment fragmentOne = (RecentlyOnlineUsersFragment) adapter.getItem(0);
                FindBuddyNearFragment fragmentTwo = (FindBuddyNearFragment) adapter.getItem(1);
                int currentSelectedDropdown;

                // User currently viewing FindBuddyNearFragment
                if (position == 0) {
                    currentSelectedDropdown = fragmentTwo.getCurrentSelectedDropdown();
                    fragmentOne.setCurrentSelectedDropdown(currentSelectedDropdown);
                }
                // User currently viewing RecentlyOnlineUsersFragment
                if (position == 1) {
                    currentSelectedDropdown = fragmentOne.getCurrentSelectedDropdown();
                    fragmentTwo.setCurrentSelectedDropdown(currentSelectedDropdown);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });
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
