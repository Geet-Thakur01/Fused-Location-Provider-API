package com.smdt.typedefdemo;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * Created by aplite_pc302 on 12/13/18.
 */

public class FusedLocationTracker implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static final int ALL_PERMISSION_RESULT = 1001;
    private static final Integer PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000;
    private static FusedLocationTracker trackerinstance;
    private final String TAG = "FusedLocationTracker>>>";
    ArrayList<String> permissionstoRequest;
    ArrayList<String> permissionsToReject = new ArrayList<>();
    ArrayList<String> permissionList = new ArrayList<>();
    Activity activity;
    //    private TextView location_text; // krte h iska kaam
    private Location location;
    private GoogleApiClient googleapiclient;
    private LocationRequest locationRequest;

    public static FusedLocationTracker getTrackerinstance() {
        if (trackerinstance == null) {
            trackerinstance = new FusedLocationTracker();
        }
        return trackerinstance;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

//    permission ok we get last location
        location = LocationServices.FusedLocationApi.getLastLocation(googleapiclient);
//        location_text.setText(location.getLatitude() + ">>>><<<<<" + location.getLongitude());

        if(location!=null){
            Log.e(TAG, location.getLatitude() + ">>>><<<<<" + location.getLongitude());
        }
        startLocationApdate();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
//            location_text.setText(location.getLatitude() + ">>>><<<<<" + location.getLongitude());
            Log.e(TAG,location.getLatitude() + ">>>><<<<<" + location.getLongitude());
        }
    }

    private void startLocationApdate() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG,"you need to enable location request");
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleapiclient, locationRequest, this);
    }

    public void buildTracker(Activity activity) {
        this.activity = activity;
        permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);

        permissionstoRequest = permissionstoRequest(permissionList);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (permissionstoRequest.size() > 0) {
                activity.requestPermissions(permissionstoRequest.toArray(new String[permissionstoRequest.size()]), ALL_PERMISSION_RESULT);
            }
        }
        googleapiclient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private ArrayList<String> permissionstoRequest(ArrayList<String> permissionList) {
        ArrayList<String> result = new ArrayList<>();
        for (String prm : permissionList) {
            if (!hasPermission(prm)) {
                result.add(prm);
            }
        }
        return result;
    }

    private boolean hasPermission(String prm) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.checkSelfPermission(prm) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public void connectGoogleapiClient() {
        if (googleapiclient != null) {
            googleapiclient.connect();
        }
    }

    public void checkServices() {
        if (!checkPlayServices()) {
            Log.e(TAG,"you need to start play services use app properly");
//            location_text.setText("you need to start play services use app properly");
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultcode = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (resultcode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultcode)) {
                googleApiAvailability.getErrorDialog(activity, resultcode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
                activity.finish();
            }
        } else {
            return false;
        }
        return true;
    }

    public void disconnectService() {
        if (googleapiclient != null && googleapiclient.isConnected()) {
            googleapiclient.disconnect();
        }
    }

    public ArrayList<String> getrejectedPermissions() {
        for (String prm : permissionstoRequest) {
            if (!hasPermission(prm)) {
                permissionsToReject.add(prm);
            }
        }
        return permissionsToReject;
    }
}
