package bvgiants.diary3;

import android.content.Context;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ProgressBar;

import com.google.android.gms.common.ConnectionResult;

public class MainActivity extends AppCompatActivity {


    Context mContext;
    static float percentageValue;
    private int USERID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
         setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
       /*( fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);

        USERID = getIntent().getIntExtra("UserID", 0);
        // Not loading in because google fit isn't connecting yet
        //((ProgressBar)findViewById(R.id.progressBarStepsGoal)).setProgress((int)percentageValue);
        //Log.e("Progress Bar", "Progress Bar is: " + percentageValue);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.eat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettings = new Intent(this, SettingsActivity.class);
            startActivity(startSettings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    // Takes user to selected screen(Activity)
    public void menuSelect( View v ) {
        switch (v.getId()) {
            case (R.id.buttonRun):
                Intent startRun = new Intent(this, runActivity.class);
                startActivity(startRun);
                break;

            case (R.id.buttonEat):
                Intent startEat = new Intent(this, EatActivity.class);
                Bundle userCreds = new Bundle();
                userCreds.putInt("UserID", USERID);
                startEat.putExtras(userCreds);
                startActivity(startEat);
                break;

            case (R.id.buttonData):
                Intent startData = new Intent(this, DataActivity.class);
                startActivity(startData);
                break;

        }
    }

    public void startBackgroundProcess(View view, Context c){
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

}
