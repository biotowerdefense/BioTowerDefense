package cisgvsu.biotowerdefense;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Handles paging between tabs in store/inventory.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    StoreFragment store;
    InventoryFragment inventory;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
        store = new StoreFragment();
        inventory = new InventoryFragment();

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return store;
            case 1:
                return inventory;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public void setBundle(Bundle bundle) {
        inventory.setArguments(bundle);
        store.setArguments(bundle);
    }
}
