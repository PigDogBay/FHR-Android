package com.pigdogbay.foodhygieneratings;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.pigdogbay.foodhygieneratings.model.AppState;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.Query;
import com.pigdogbay.foodhygieneratings.model.SearchType;
import com.pigdogbay.lib.utils.ActivityUtils;
import com.pigdogbay.lib.utils.ObservableProperty;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, ObservableProperty.PropertyChangedObserver<AppState> {

    private FloatingActionButton fabFilter;
    private AdView _AdView;

    private HomeFragment homeFragment;
    private ProgressDialog progressDialog;
    private GoogleApiClient googleApiClient;
    private boolean flagFindPlacesNearToMe = false;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public GoogleApiClient getGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        return googleApiClient;
    }

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabFilter = findViewById(R.id.fab);
        fabFilter.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        fabFilter.setVisibility(View.GONE);

        setUpAds();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount == 0) {
            showHome();
        } else if (backStackCount > 1) {
            setNavigateHome(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainModel.get(this).getAppStateProperty().addObserver(this);
        update(MainModel.get(this).getAppStateProperty().getValue());
        //User gave permission to use location
        if (flagFindPlacesNearToMe){
            flagFindPlacesNearToMe = false;
            findLocalEstablishments();
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
        MainModel.get(this).getAppStateProperty().removeObserver(this);
    }

    @Override
    protected void onStart() {
        getGoogleApiClient().connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    void setUpAds() {
        // Look up the AdView as a resource and load a request.
        _AdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.code_test_device_acer_tablet))
                .addTestDevice(getString(R.string.code_test_device_moto_g))
                .build();
        _AdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_main_about:
                showAbout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void pushFragment(Fragment fragment, String tag) {
        ActivityUtils.hideKeyboard(this);
        FragmentManager manager = getSupportFragmentManager();
        manager
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        ActivityUtils.hideKeyboard(this);
        FragmentManager manager = getSupportFragmentManager();
        switch (manager.getBackStackEntryCount()) {
            case 0:
                finish();
                break;
            case 1:
                //Home screen
                this.setNavigateHome(false);
                checkAppRate();
                break;
            default:
                this.setNavigateHome(true);
                break;
        }
    }
    private void checkAppRate() {
        new com.pigdogbay.lib.apprate.AppRate(this)
                .setMinDaysUntilPrompt(7).setMinLaunchesUntilPrompt(20).init();
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

    public void showEstablishmentMap() {
        pushFragment(new EstablishmentMapFragment(), EstablishmentMapFragment.TAG);
    }

    public void showHtmlText(int resourceId) {
        pushFragment(HtmlTextFragment.newInstance(resourceId), HtmlTextFragment.TAG);
    }

    public void showAbout(){
        pushFragment(new AboutFragment(), AboutFragment.TAG);
    }

    @Override
    public void update(ObservableProperty<AppState> sender, final AppState update) {
        runOnUiThread(() -> update(update));
    }

    private void update(AppState state) {
        switch (state) {
            case ready:
                break;
            case loading:
                showBusy();
                break;
            case loaded:
                hideBusy();
                break;
            case connectionError:
                ActivityUtils.showInfoDialog(this,R.string.alert_connection_error_title,R.string.alert_connection_error_description,R.string.ok);
                hideBusy();
                break;
            case error:
                hideBusy();
                break;
        }

    }

    private void hideBusy() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
    }

    private void showBusy() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (    requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
                permissions.length>0 &&
                android.Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0]) &&
                grantResults[0]== PERMISSION_GRANTED ){
            //try again, have to use onResume (android Bug)
            //http://stackoverflow.com/questions/33264031/calling-dialogfragments-show-from-within-onrequestpermissionsresult-causes
            flagFindPlacesNearToMe = true;
        }
    }

    public void findLocalEstablishments() {
        MainModel mainModel = MainModel.get(this);
        if (!mainModel.isBusy() && getGoogleApiClient().isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
            final Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(getGoogleApiClient());
            if (lastLocation != null) {
                Query query = new Query(lastLocation.getLongitude(), lastLocation.getLatitude(), 1);
                if (mainModel.findEstablishments(query)) {
                    mainModel.setSearchType(SearchType.local);
                    showResults();
                }
            } else {
                ActivityUtils.showInfoDialog(this,R.string.alert_no_location_title,R.string.alert_no_location_description,R.string.ok);
            }
        }
    }
}
