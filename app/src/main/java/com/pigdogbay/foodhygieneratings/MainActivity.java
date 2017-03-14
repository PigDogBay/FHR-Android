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

import layout.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fabFilter;
    private String previousFragmentTag = "";
    private String currentFragmentTag = "";

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
