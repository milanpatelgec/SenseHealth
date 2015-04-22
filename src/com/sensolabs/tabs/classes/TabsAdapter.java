package com.sensolabs.tabs.classes;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


/**
 * This is a helper class that implements the management of tabs and all
 * details of connecting a ViewPager with associated TabHost.  It relies on a
 * trick.  Normally a tab host has a simple API for supplying a View or
 * Intent that each tab will show.  This is not sufficient for switching
 * between pages.  So instead we make the content part of the tab host
 * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
 * view to show as the tab content.  It listens to changes in tabs, and takes
 * care of switch to the correct paged in the ViewPager whenever the selected
 * tab changes.
 */



import android.content.Context;

/**
 * This is a helper class that implements the management of tabs and all
 * details of connecting a ViewPager with associated TabHost.  It relies on a
 * trick.  Normally a tab host has a simple API for supplying a View or
 * Intent that each tab will show.  This is not sufficient for switching
 * between pages.  So instead we make the content part of the tab host
 * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
 * view to show as the tab content.  It listens to changes in tabs, and takes
 * care of switch to the correct paged in the ViewPager whenever the selected
 * tab changes.
 */

public class TabsAdapter extends FragmentPagerAdapter
implements ActionBar.TabListener, ViewPager.OnPageChangeListener {

      private final Context mContext;
      private final ActionBar mActionBar;
      private final ViewPager mViewPager;
      private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

      static final class TabInfo {
          private final Class<?> clss;
          private final Bundle args;

          TabInfo(Class<?> _class, Bundle _args) {
              clss = _class;
              args = _args;
          }
      }

public TabsAdapter(FragmentActivity activity, ViewPager pager) {
 super(activity.getSupportFragmentManager());
          mContext = activity;
          mActionBar = activity.getActionBar();
          mViewPager = pager;
          mViewPager.setAdapter(this);
          mViewPager.setOnPageChangeListener(this);
      }

public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
          TabInfo info = new TabInfo(clss, args);
          tab.setTag(info);
          tab.setTabListener(this);
          mTabs.add(info);
          mActionBar.addTab(tab);
          notifyDataSetChanged();

      }

@Override
public void onPageScrollStateChanged(int state) {
 // TODO Auto-generated method stub

}


public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
 // TODO Auto-generated method stub

}


public void onPageSelected(int position) {
 // TODO Auto-generated method stub
 mActionBar.setSelectedNavigationItem(position);
}

@Override
public void onTabReselected(Tab tab, FragmentTransaction ft) {
 // TODO Auto-generated method stub

}

@Override
public void onTabSelected(Tab tab, FragmentTransaction ft) {
 Object tag = tab.getTag();
          for (int i=0; i<mTabs.size(); i++) {
              if (mTabs.get(i) == tag) {
                  mViewPager.setCurrentItem(i);
              }
          }
          
//          ft.addToBackStack(null);
//          //ft.commit();
}

@Override
public void onTabUnselected(Tab tab, FragmentTransaction ft) {
 // TODO Auto-generated method stub

}

@Override
public Fragment getItem(int position) {
 TabInfo info = mTabs.get(position);
          return Fragment.instantiate(mContext, info.clss.getName(), info.args);
}

@Override
public int getCount() {
 return mTabs.size();
}

}