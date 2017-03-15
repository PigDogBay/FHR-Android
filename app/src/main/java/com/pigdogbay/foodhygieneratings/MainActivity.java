package com.pigdogbay.foodhygieneratings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabFilter;
    private String previousFragmentTag = "";
    private String currentFragmentTag = "";
    private AdView _AdView;

    private HomeFragment homeFragment;
    private HomeFragment getHomeFragment(){
        if (homeFragment==null){
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabFilter = (FloatingActionButton) findViewById(R.id.fab);
        fabFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setUpAds();

        Fragment fragment =getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);

        //New instance or a rotation?
        currentFragmentTag = HomeFragment.TAG;
        if (fragment==null)
        {
            //show help when first start
            showHome();
        }
    }

    @Override
    protected void onRestart() {
        if (_AdView != null) {
            _AdView.resume();
        }
        super.onRestart();
    }

    @Override
    protected void onPause() {
        if (_AdView != null) {
            _AdView.pause();
        }
        super.onPause();
    }

    void setUpAds() {
        // Look up the AdView as a resource and load a request.
        _AdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.code_test_device_1_id))
                .addTestDevice(getString(R.string.code_test_device_2_id))
                .addTestDevice(getString(R.string.code_test_device_3_id))
                .addTestDevice(getString(R.string.code_test_device_4_id))
                .addTestDevice(getString(R.string.code_test_device_5_id))
                .addTestDevice(getString(R.string.code_test_device_6_id))
                .addTestDevice(getString(R.string.code_test_device_7_id))
                .addTestDevice(getString(R.string.code_test_device_8_id))
                .build();
        _AdView.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void replaceMainFragment(Fragment fragment, String tag)
    {
        fabFilter.setVisibility(View.GONE);
        FragmentManager manager = getSupportFragmentManager();
        Fragment f= manager.findFragmentById(R.id.main_fragment_container);
        if (f!=null){
            previousFragmentTag = f.getTag();
        }
        currentFragmentTag = tag;
        manager
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment, tag)
                .commit();
    }
    private void setNavigateHome(boolean enabled)
    {
        final ActionBar ab = getSupportActionBar();
        if (ab!=null){ab.setDisplayHomeAsUpEnabled(enabled);}
    }

    private void showHome(){
        if (getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG)==null)
        {
            this.setNavigateHome(false);
            replaceMainFragment(getHomeFragment(), HomeFragment.TAG);
        }

    }

}
