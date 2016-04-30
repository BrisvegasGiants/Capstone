package bvgiants.diary3;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.service.carrier.CarrierMessagingService;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
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

import java.lang.Override;
import java.util.concurrent.TimeUnit;

/**
* Created by Dylan Schumacher on 15/4/2016
 * Establishes listener on window creation
 * Updates output to the user every second
 */

//TODO: Add button to maps page to track activity for the day

public class runActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnDataPointListener {

    //User Defined Variables [Default]
    double stepLength = 0.75;
    int stepGoal = 10000;
    //TODO: Request users step goals and step lengths

    //API Variables
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private GoogleApiClient mApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            authInProgress = savedInstanceState.getBoolean(AUTH_PENDING);
        }

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.SENSORS_API)
                .addScope(Fitness.SCOPE_ACTIVITY_READ_WRITE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    } // End onCreate


    @Override
    public void onStart() {
        super.onStart();
        mApiClient.connect();
    } // End onStart


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

    } // end onConnected

    private void registerFitnessDataListener(DataSource dataSource, DataType dataType){
        SensorRequest request = new SensorRequest.Builder()
                .setDataSource( dataSource )
                .setDataType( dataType )
                .setSamplingRate(2, TimeUnit.SECONDS )
                .build();

        Fitness.SensorsApi.add(mApiClient, request, this)
                .setResultCallback(new ResultCallback<Status>(){
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()){
                            Log.e("GoogleFit", "SensorApi Successfully Added");
                        }
                    }
                });
    } // End regsterFitnessDataListener


    @Override
    public void onConnectionSuspended(int i) {
        // The connection has been interrupted. Wait until onConnected() is called.
        // Do something...
    } // End onConnectionSuspended


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (!authInProgress) {
            try {
                authInProgress = true;
                connectionResult.startResolutionForResult(runActivity.this, REQUEST_OAUTH);
            } catch (IntentSender.SendIntentException e) {
                //Err...
            }
        } else {
            Log.e("GoogleFit", "authInProgress");
        }

        // Error while connecting. Try to resolve using the pending intent returned.
        /*
        if (result.getErrorCode() == FitnessStatusCodes.NEEDS_OAUTH_PERMISSIONS) {
            try {
                result.startResolutionForResult(this, REQUEST_OAUTH);
            } catch (SendIntentException e) {
            }
        }*/
    } // End onConnectionFailed


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


    @Override
    public void onDataPoint(DataPoint dataPoint) {
        for( final Field field : dataPoint.getDataType().getFields() ) {
            // Access final datapoint, find it's value
            Value value = dataPoint.getValue( field );
            // Manipulate it's value to match
            final Value totalSteps = value;
            //Calc Distance (No. Steps * Step Length).
            int amtSteps = value.asInt();
            final float distanceValue = (float) (amtSteps * stepLength);
            // Calculate Percentage to goal
            final float percentageValue = ((float)amtSteps / stepGoal) * 100;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Use toast for debugging
                    //Toast.makeText(getApplicationContext(), "Field: " + field.getName() + " Value: " + value, Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), "Field: " + field.getName() + " Value: " + totalSteps, Toast.LENGTH_SHORT).show();

                    // Update Text Fields
                    ((TextView)findViewById(R.id.stepCounterView)).setText(""+totalSteps);
                    ((TextView)findViewById(R.id.distanceCounterView)).setText(""+distanceValue);
                    ((TextView)findViewById(R.id.percentageCounterView)).setText(""+percentageValue);
                }
            });
        }
    } // End onDataPoint


    @Override
    protected void onStop() {
        super.onStop();
        Fitness.SensorsApi.remove( mApiClient, this )
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            mApiClient.disconnect();
                        }
                    }
                });
    } // End onStop


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(AUTH_PENDING, authInProgress);
    } // End onSaveInstanceState

}// End all