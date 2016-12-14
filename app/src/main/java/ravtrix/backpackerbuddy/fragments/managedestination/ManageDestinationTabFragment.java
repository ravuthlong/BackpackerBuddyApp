package ravtrix.backpackerbuddy.fragments.managedestination;

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
import ravtrix.backpackerbuddy.fragments.managedestination.ausercountryposts.AUserCountryPostsFragment;
import ravtrix.backpackerbuddy.fragments.managedestination.auserfavoriteposts.AUserFavPostsFragment;

/**
 * Created by Ravinder on 12/13/16.
 */

public class ManageDestinationTabFragment extends Fragment {
    @BindView(R.id.viewpager) protected ViewPager viewPager;
    @BindView(R.id.tabs) protected TabLayout tabLayout;
    private AUserCountryPostsFragment aUserCountryPostsFragment;
    private AUserFavPostsFragment aUserFavPostsFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_tabs, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);

        this.aUserCountryPostsFragment = new AUserCountryPostsFragment();
        this.aUserFavPostsFragment = new AUserFavPostsFragment();

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setFirstTabSelectedOnStart();
        return v;
    }

    /**
     * Set up the two tabs, one being the "Recent Posts" and the other being "Filter Tools"
     * @param viewPager         the view pager for the tabs
     */
    private void setupViewPager(final ViewPager viewPager) {
        ManageDestinationTabFragment.ViewPagerAdapter adapter =
                new ManageDestinationTabFragment.ViewPagerAdapter(getChildFragmentManager());

        adapter.addFragment(this.aUserCountryPostsFragment, "Your Posts");
        adapter.addFragment(this.aUserFavPostsFragment, "Watch List");
        viewPager.setAdapter(adapter);
    }

    private void setFirstTabSelectedOnStart() {
        new Handler().postDelayed(
                new Runnable(){
                    @Override
                    public void run() {
                        tabLayout.getTabAt(0).select();
                    }
                }, 100);
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
