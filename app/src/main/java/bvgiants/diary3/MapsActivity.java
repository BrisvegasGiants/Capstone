package bvgiants.diary3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks,
        OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    Context mContext;


    private static GoogleMap mMap;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);

        if(mGoogleApiClient!= null){
            mGoogleApiClient.connect();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void startBackgroundProcess(View view, Context c){
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        Log.e("Google Maps", "Map is now ready for use");

        // Check if Maps needs to render any pins
        SharedPreferences mapReferences = getSharedPreferences("DropPins", MODE_PRIVATE);
        int locationCount = mapReferences.getInt("locationCount", 1);
        Log.e("Pins", "Found mapreferences RUN pins");
        if (locationCount > 1) {
            dropRunPins();
        } else {
            Log.e("Pins", "No RUN Pins to drop");
        }

        int foodLocationCount = mapReferences.getInt("foodLocationCount", 1);
        Log.e("Pins", "Found mapreferences RUN pins");
        if (foodLocationCount > 1) {
            dropFoodPins();
        } else {
            Log.e("Pins", "No FOOD Pins to drop");
        }

    }

    private void dropRunPins(){
        // Loop Through Pins
        Log.e("Google Maps", "-----------------------------< START RUN PINS >------------------------------");
        SharedPreferences mapReferences = getSharedPreferences("DropPins", MODE_PRIVATE);

        //String extractedText = mapReferences.getString("lat0", "No Lat Recorded");
        /*
        String lat = mapReferences.getString("lat0", "No Lat Recorded");
        String lng = mapReferences.getString("lng0", "No Long Recorded");
         */
        int locationCount = mapReferences.getInt("locationCount", 1);
        Log.e("Google Maps", "Found the Total RUN Count - " + locationCount + " locations");


        for (int i=0; i<locationCount; i++){
            String lat = mapReferences.getString("lat"+i, "No Lat Recorded");
            String lng = mapReferences.getString("lng"+i, "No Long Recorded");
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

            // *** Start Edit

            //double distanceDif = distance(Double.parseDouble(lat)+i,Double.parseDouble(lng), -27.460584, 152.975657);

            // On first Pin, ensure it drops
            if (i == 0) {
                Log.e("Google Maps", "Loop 0 - Found Logged Location! | Looped | " + lat +" "+ lng);
                Log.e("Google Maps", "Loop 0 - Loop Counter: " + i);
                Log.e("Google Maps", "Loop 0 - Dropped Pin at " + loc);
                Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).title("Pin Number: " + i));
                //Log.e("Google Maps", "Distance between 2 point is  " + distanceDif);
            }

            // Always place one pin, then check the rest
            if (i > 0) {
                //LatLng oldLoc = loc;
                String oldlatitude = mapReferences.getString("lat"+(i-1), "No Lat Recorded");
                String oldlongitude = mapReferences.getString("lng"+(i-1), "No Long Recorded");
                Double oldlat = Double.parseDouble(oldlatitude);
                Double oldlng = Double.parseDouble(oldlongitude);

                Double distanceDif = distance(Double.parseDouble(lat), Double.parseDouble(lng), oldlat, oldlng);

                // If the distance has not changed by more than 100m...
                if (distanceDif < .10){
                    // Do nothing
                    Log.e("Google Maps", "Loop No: " + i);
                    Log.e("Google Maps", "Cannot Place Pin - Under Distance!" );
                    Log.e("Google Maps", "Distance is " + distanceDif + " at this location: " + loc );

                } else {
                    // Place a Pin
                    Log.e("Google Maps", "Loop No: " + i);
                    Log.e("Google Maps", "Placing Pin on "+i+" loop - Distance Difference is " + distanceDif );
                    Log.e("Google Maps", "Count " + i + " - Dropped Pin at " + loc);
                    Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).title("Pin Number: " + i));

                }

            }
            // Compare Distance

            // *** End Edit
            /*
            Log.e("Google Maps", "Loop Counter: " + i);
            Log.e("Google Maps", "Found Logged Location! | Looped | " + lat +" "+ lng);
            Log.e("Google Maps", "Dropped Pin at " + loc);

            */
            // Drop Pin doesn't work for some reason -- that or it only displays the first pin because I'm at home (not moving).
            //dropPin(Double.parseDouble(lat),Double.parseDouble(lng), "Pin No." + i);

            //Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).title("Pin "+i));

        }

        Log.e("Google Maps", "-----------------------------< END RUN PINS >------------------------------");

    } // End dropRunPins

    private void dropFoodPins(){
        Log.e("Google Maps", "-----------------------------< START FOOD PINS >------------------------------");
        SharedPreferences mapReferences = getSharedPreferences("FoodPins", MODE_PRIVATE);

        //String extractedText = mapReferences.getString("lat0", "No Lat Recorded");
        /*
        String lat = mapReferences.getString("lat0", "No Lat Recorded");
        String lng = mapReferences.getString("lng0", "No Long Recorded");
         */
        int foodLocationCount = mapReferences.getInt("foodLocationCount", 1);
        Log.e("Google Maps", "Found the Total FOOD Count - " + foodLocationCount + " locations");


        for (int i=0; i<foodLocationCount; i++){

            String lat = mapReferences.getString("lat"+i, "No Lat Recorded");
            String lng = mapReferences.getString("lng"+i, "No Long Recorded");
            LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));


            if (i == 0) {
                Log.e("Google Maps", "Loop "+i+" - Found Logged Location! | Looped | " + lat +" "+ lng);
                Log.e("Google Maps", "Loop "+i+" - Loop Counter: " + i);
                Log.e("Google Maps", "Loop "+i+" - Dropped Pin at " + loc);
                Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).title("This is food pin: " + i).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            }
            // Always place one pin, then check the rest
            if (i > 0) {
                    Log.e("Google Maps", "Food Pin No: " + i);
                    Log.e("Google Maps", "Placing Food Pin on "+i+" loop" );
                    Log.e("Google Maps", "Count " + i + " - Dropped Food Pin at " + loc);
                    Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).title("This is food pin: " + i).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                }

            }

        Log.e("Google Maps", "-----------------------------< END FOOD PINS >------------------------------");

        }

    //double distanceDif = distance(mLastLocation.getLatitude(), mLastLocation.getLongitude(), -27.460584, 152.975657);

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        //Log.e("Google Maps", "Distance Calculation is: " + dist + " With the following figures:" + lat1 +" "+lat2);
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }



    public void dropPin(double lat, double lng, String label) {

        /*
        LatLng loc = new LatLng(lat, lng);
        Log.e("Google Maps", "Dropped Pin at " + loc);
        Marker mMarker = mMap.addMarker(new MarkerOptions().position(loc).title(label));
        /*
        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        }
        */

    }


/*
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
*/

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
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
        Log.e("Google Maps", "Map has started");


    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
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
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("Google Maps", "Map has connected!");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Google Maps", "Connection Failed!");
    }
}
