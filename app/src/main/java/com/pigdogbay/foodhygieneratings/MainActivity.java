package com.pigdogbay.foodhygieneratings;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.pigdogbay.foodhygieneratings.model.FetchState;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.lib.utils.ObservableProperty;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, ObservableProperty.PropertyChangedObserver<FetchState> {

    private FloatingActionButton fabFilter;
    private AdView _AdView;

    private HomeFragment homeFragment;
    private ProgressDialog progressDialog;

    private HomeFragment getHomeFragment() {
        if (homeFragment == null) {
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
        fabFilter.setVisibility(View.GONE);

        setUpAds();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount==0){
            showHome();
        } else if (backStackCount>1) {
            setNavigateHome(true);
            setUpTitle();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainModel.get(this).getDataProvider().getFetchStateProperty().addObserver(this);
        update(MainModel.get(this).getDataProvider().getFetchStateProperty().getValue());
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
        MainModel.get(this).getDataProvider().getFetchStateProperty().removeObserver(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void pushFragment(Fragment fragment, String tag) {

        FragmentManager manager = getSupportFragmentManager();
        manager
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager manager = getSupportFragmentManager();
        switch (manager.getBackStackEntryCount()) {
            case 0:
                finish();
                break;
            case 1:
                //Home screen
                this.setNavigateHome(false);
                setTitle("Food Hygiene");
                break;
            default:
                this.setNavigateHome(true);
                setUpTitle();
                break;
        }
    }




    private void setNavigateHome(boolean enabled) {
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(enabled);
        }
    }

    public void showHome() {
        pushFragment(getHomeFragment(), HomeFragment.TAG);
    }

    public void showResults() {
        pushFragment(new ResultsFragment(), ResultsFragment.TAG);
    }

    public void showAdvancedSearch() {
        pushFragment(new AdvancedSearchFragment(), AdvancedSearchFragment.TAG);
    }

    public void showDetails() {
        pushFragment(new DetailsFragment(), DetailsFragment.TAG);
    }

    public void showMap() {
        pushFragment(new MapFragment(), MapFragment.TAG);
    }

    private void setUpTitle(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 0) { return;}
        String tag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();

        Log.v("mpdb", "Tag = "+tag);
        switch (tag){
            case ResultsFragment.TAG:
                setTitle("Results");
                break;
            case AdvancedSearchFragment.TAG:
                setTitle("Advanced");
                break;
            case DetailsFragment.TAG:
                setTitle("Details");
                break;
            case MapFragment.TAG:
                setTitle("Map");
                break;
            default:
                setTitle("Food Hygiene Ratings");
                break;
        }
    }

    @Override
    public void update(ObservableProperty<FetchState> sender, final FetchState update) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                update(update);
            }
        });
    }

    private void update(FetchState state){
        switch (state){
            case ready:
                break;
            case requestingLocationAuthorization:
                showBusy();
                break;
            case locating:
                break;
            case foundLocation:
                break;
            case notAuthorizedForLocating:
                break;
            case errorLocating:
                break;
            case loading:
                showBusy();
                break;
            case loaded:
                hideBusy();
                break;
            case error:
                hideBusy();
                break;
        }

    }

    private void hideBusy() {
        if (progressDialog!=null) {
            progressDialog.cancel();
        }
    }

    private void showBusy() {
        if (progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }
}
