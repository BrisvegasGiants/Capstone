package bvgiants.diary3;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.os.IBinder;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.concurrent.TimeUnit;

/**
 * Created by Dylan on 10/05/2016.
 */
public class BackgroundService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnDataPointListener {

    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    public GoogleApiClient mApiClient;

    public int globalSteps;

    IBinder mBinder;
    int mStartMode;

    Handler handler;

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    GoogleApiClient client;
    static GoogleMap mMap;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        //DEBUGGING CODE - Toast if the service is properly being started.
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        handler = new Handler();

        // Init Google Fit API Client

        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Fitness.SENSORS_API)
                    .addScope(Fitness.SCOPE_ACTIVITY_READ_WRITE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mApiClient.connect();
        }

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            Log.e("Google Maps", "Google Maps Connected");
            mGoogleApiClient.connect();
        }

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Maps Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://bvgiants.diary3/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);


        return START_STICKY;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public void onRebind(Intent intent){
        Toast.makeText(this, "Service Rebound", Toast.LENGTH_LONG).show();
    }


    //
    // GOOGLE MAPS API  ----------------------------------------------------------------------------------------------------------------------------
    //


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

    }

    public static void dropPin(Location mLastLocation) {
        double latitude = mLastLocation.getLatitude();
        double longitude = mLastLocation.getLongitude();

        LatLng loc = new LatLng(latitude, longitude);
        Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).title("My Location"));
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        }
    }


    public void callLocation(){

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (mLastLocation != null) {


            String myLat = Double.toString(mLastLocation.getLatitude());
            String myLong = Double.toString(mLastLocation.getLongitude());

            Log.e("GoogleMaps", "Found Location! " + "Lat: " + myLat + " " + "Long: " + myLong);
            //Toast.makeText(getApplicationContext(), myLoc, Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(this, MapsActivity.class);
            //MapsActivity.dropPin(mLastLocation);
            //startActivity(intent);
        }

    }


















    //
    // END GOOGLE MAPS API ----------------------------------------------------------------------------------------------------------------------------
    //

    //
    // START GOOGLE FIT API ----------------------------------------------------------------------------------------------------------------------------
    //

    @Override
    public void onConnected(Bundle bundle) {
        DataSourcesRequest dataSourceRequest = new DataSourcesRequest.Builder()
                .setDataTypes(DataType.TYPE_STEP_COUNT_CUMULATIVE )
                .setDataSourceTypes( DataSource.TYPE_RAW)
                .build();

        ResultCallback<DataSourcesResult> dataSourcesResultCallback = new ResultCallback<DataSourcesResult>() {
            @Override
            public void onResult(DataSourcesResult dataSourcesResult) {
                for ( DataSource dataSource : dataSourcesResult.getDataSources()){
                    if (DataType.TYPE_STEP_COUNT_CUMULATIVE.equals( dataSource.getDataType())){
                        registerFitnessDataListener(dataSource, DataType.TYPE_STEP_COUNT_CUMULATIVE);
                    }
                }
            } //End onResult
        }; //End ResultCallback

        Fitness.SensorsApi.findDataSources(mApiClient, dataSourceRequest).setResultCallback(dataSourcesResultCallback);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void registerFitnessDataListener(DataSource dataSource, DataType dataType){
        SensorRequest request = new SensorRequest.Builder()
                .setDataSource( dataSource )
                .setDataType( dataType )
                .setSamplingRate(1, TimeUnit.SECONDS )
                .build();

        Fitness.SensorsApi.add(mApiClient, request, this)
                .setResultCallback(new ResultCallback<Status>(){
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()){
                            Log.e("GoogleFit", "SensorApi Successfully Registered");
                        }
                    }
                });
    } // End regsterFitnessDataListener

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OAUTH) {
            authInProgress = false;
            if (resultCode == RESULT_OK){
                if (!mApiClient.isConnecting() && !mApiClient.isConnected()) {
                    mApiClient.connect();
                }
            } else if (resultCode == RESULT_CANCELED) {
                Log.e("GoogleFit", "RESULT_CANCELED");
            }
        } else {
            Log.e("GoogleFit", "requestCode NOT request_oauth");
        }
    } // End onActivityResult
*/

    @Override
    public void onDataPoint(DataPoint dataPoint) {
        for( final Field field : dataPoint.getDataType().getFields() ) {
            // Access final datapoint, find it's value
            Value value = dataPoint.getValue( field );
            // Manipulate it's value to match
            final Value totalSteps = value;
            final Value globalSteps = totalSteps;
            //Calc Distance (No. Steps * Step Length).
            int amtSteps = value.asInt();
            final float distanceValue = (float) (amtSteps * 0.75);
            // Calculate Percentage to goal
            final float percentageValue = ((float)amtSteps / 10000) * 100;

            // DEBUGGING CODE - Displays log of steps
            Log.e("GoogleFit", "Found Data! - " + globalSteps + " steps");
            callLocation();


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // DEBUGGING CODE - Toasts the Number of steps
                    Toast.makeText(getApplicationContext(), "Number of Steps: " + totalSteps, Toast.LENGTH_SHORT).show();

                }
            });
            
        }

    } // End onDataPoint

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        /*
        if (!authInProgress) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult(getBaseContext().this, REQUEST_OAUTH);
            } catch (IntentSender.SendIntentException e) {
                //Err...
            }
        } else {
            Log.e("GoogleFit", "authInProgress");
        }
        */
    }
}
