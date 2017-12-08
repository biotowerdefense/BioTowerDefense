package cisgvsu.biotowerdefense;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Handle navigation between store and inventory tabs, mostly just passing information
 * to them that we received from the main game activity about where to put a tower.
 */
public class StoreInventoryNavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Store & Inventory");
        setSupportActionBar(toolbar);

        BioTowerDefense app = (BioTowerDefense) getApplicationContext();
        final Game game = app.getGame();

        // Get the tower position we were sent & the contents of the inventory
        Intent intent = getIntent();
        int position = intent.getIntExtra(MainActivity.EXTRA_TOWER_POSITION, 0);
        int money = game.getMoney();
        toolbar.setSubtitle("Money: " + money);
        ArrayList<String> strInventory = game.getInventoryAsStrings();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Store"));
        tabLayout.addTab(tabLayout.newTab().setText("Inventory"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        // Build bundle to set position and inventory
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.EXTRA_TOWER_POSITION, position);
        bundle.putInt(MainActivity.EXTRA_MONEY, money);
        bundle.putStringArrayList(MainActivity.EXTRA_INVENTORY, strInventory);
        adapter.setBundle(bundle);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_store_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
