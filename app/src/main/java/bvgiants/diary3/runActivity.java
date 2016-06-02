package bvgiants.diary3;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.service.carrier.CarrierMessagingService;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class runActivity extends AppCompatActivity {

    Context mContext;
    static int totalSteps;
    static float distanceValue;
    static float percentageValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView)findViewById(R.id.stepCounterView)).setText(""+totalSteps);
                                ((TextView)findViewById(R.id.distanceCounterView)).setText(""+distanceValue);
                                ((TextView)findViewById(R.id.percentageCounterView)).setText(String.format("%.2f", percentageValue)+" %");
                            }
                        });
                    }
                } catch (InterruptedException e) {

                }
            }
        };

        t.start();

    } // End onCreate

    public void startBackgroundProcess(View view, Context c){
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

}// End all