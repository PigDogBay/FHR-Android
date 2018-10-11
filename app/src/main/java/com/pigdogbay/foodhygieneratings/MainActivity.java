package com.pigdogbay.foodhygieneratings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.pigdogbay.foodhygieneratings.model.AppState;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.Injector;
import com.pigdogbay.foodhygieneratings.model.MainModel;
import com.pigdogbay.foodhygieneratings.model.Query;
import com.pigdogbay.foodhygieneratings.model.SearchType;
import com.pigdogbay.foodhygieneratings.model.Settings;
import com.pigdogbay.lib.utils.ActivityUtils;
import com.pigdogbay.lib.utils.ObservableProperty;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener, ObservableProperty.PropertyChangedObserver<AppState>, SharedPreferences.OnSharedPreferenceChangeListener {

    private FloatingActionButton fabFilter;
    private AdView _AdView;

    private HomeFragment homeFragment;
    private ProgressDialog progressDialog;
    private GoogleApiClient googleApiClient;
    private boolean flagFindPlacesNearToMe = false;
    private MainModel mainModel;
    private Settings settings;

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
        Injector.INSTANCE.build(this);
        settings = Injector.settings;
        mainModel = Injector.mainModel;
        applySettings();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabFilter = findViewById(R.id.fab);
        fabFilter.setOnClickListener(view -> mapFabClicked());
        fabFilter.hide();

        setUpAds();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackCount == 0) {
            showHome();
        } else if (backStackCount > 1) {
            setNavigateHome(true);
        }
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        //Need this to ensure fab is shown when device is rotated
        showFab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainModel.getAppStateProperty().addObserver(this);
        update(mainModel.getAppStateProperty().getValue());
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
        mainModel.getAppStateProperty().removeObserver(this);
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
        MobileAds.initialize(this, "ca-app-pub-3582986480189311~2972071182");
        // Look up the AdView as a resource and load a request.
        _AdView = findViewById(R.id.adView);
        Bundle extras = new Bundle();
        extras.putString("max_ad_content_rating", "G");
        //MA = Mature Adult, may improve eCPM?
        //extras.putString("max_ad_content_rating", "MA");
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.code_test_device_acer_tablet))
                .addTestDevice(getString(R.string.code_test_device_moto_g))
                .addTestDevice(getString(R.string.code_test_device_nokia_6))
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
            case R.id.menu_main_settings:
                showSettings();
                return true;
            case R.id.menu_main_user_guide:
                showUserGuide();
                return true;
            case R.id.menu_main_advanced_search:
                showAdvancedSearch();
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
                return;
            case 1:
                //Home screen
                this.setNavigateHome(false);
                checkAppRate();
                break;
            default:
                this.setNavigateHome(true);
                break;
        }
        showFab();
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

    private void mapFabClicked(){
        Fragment f= getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
        String tag="";
        if (f!=null){
            tag = f.getTag();
        }
        if (tag==null) return;
        switch (tag){
            case HomeFragment.TAG:
                mainModel.clearResults();
            case ResultsFragment.TAG:
                showMap();
                break;
            case DetailsFragment.TAG:
                showEstablishmentMap();
                break;
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
        Establishment establishment = mainModel.getSelectedEstablishment();
        if (establishment.getAddress().isAddressSpecified() && establishment.getCoordinate().isWithinUk()) {
            pushFragment(new EstablishmentMapFragment(), EstablishmentMapFragment.TAG);
        } else {
            Toast.makeText(this,"No address or coordinates", Toast.LENGTH_SHORT).show();
        }
    }

    public void showHtmlText(int resourceId) {
        pushFragment(HtmlTextFragment.newInstance(resourceId), HtmlTextFragment.TAG);
    }

    public void showAbout(){
        pushFragment(new AboutFragment(), AboutFragment.TAG);
    }

    public void showSettings(){
        pushFragment(new SettingsFragment(),SettingsFragment.Companion.getTAG());
    }

    public void showUserGuide(){
        ActivityUtils.ShowWebPage(this,getString(R.string.user_guide_url));
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

    private void showFab(){
        FragmentManager manager = getSupportFragmentManager();
        Fragment f= manager.findFragmentById(R.id.main_fragment_container);
        String tag="";
        if (f!=null){
            tag = f.getTag();
        }
        if (tag==null) {
            fabFilter.hide();
            return;
        }
        switch (tag){
            case HomeFragment.TAG:
            case ResultsFragment.TAG:
                if (settings.isMapSearchEnabled()){
                    fabFilter.show();
                } else {
                    fabFilter.hide();
                }
                break;
            case DetailsFragment.TAG:
                fabFilter.show();
                break;
            default:
                fabFilter.hide();
                break;
        }
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
        if (!mainModel.isBusy() && getGoogleApiClient().isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }
            LocationServices.getFusedLocationProviderClient(this)
                    .getLastLocation()
                    .addOnSuccessListener(this, this::locationSuccess)
                    .addOnFailureListener(this, this::locationFail);
        }
    }
    private void locationSuccess(Location location){
        if (location != null) {
            Query query = new Query(location.getLongitude(), location.getLatitude(), settings.getSearchRadius());
            if (mainModel.findEstablishments(query)) {
                mainModel.setSearchType(SearchType.local);
                showResults();
            }
        } else {
            ActivityUtils.showInfoDialog(this,R.string.alert_no_location_title,R.string.alert_no_location_description,R.string.ok);
        }
    }
    private void locationFail(Exception e){
        ActivityUtils.showInfoDialog(this,R.string.alert_no_location_title,R.string.alert_no_location_description,R.string.ok);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.key_pref_map_enable_search).equals(key) && settings.isMapSearchEnabled()){
            //display warning that the geo information for each establishment is inaccurate
            new AlertDialog.Builder(this)
                    .setTitle(R.string.dialog_map_search_enable_title)
                    .setMessage(R.string.dialog_map_search_enable_description)
                    .setPositiveButton(R.string.dialog_map_search_enable_button,null)
                    .show();
        } else {
            applySettings();
        }
    }
    private void applySettings(){
        mainModel.getDataProvider().setTimeout(settings.getSearchTimeout()*1000);
        mainModel.setTextFilterByNameAndAddress(settings.isTextFilterNameAndAddress());
    }
}
