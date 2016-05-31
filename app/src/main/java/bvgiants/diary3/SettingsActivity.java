package bvgiants.diary3;

import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {

    Context mContext;
    Button profileButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        startBackgroundProcess(this.findViewById(android.R.id.content), mContext);

        profileButton = (Button) findViewById(R.id.buttonProfile);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfile();
            }
        });
    }


    public void startProfile() {
        Intent startProfile = new Intent(this, ProfileActivity.class);
        startActivity(startProfile);

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


    public void startBackgroundProcess(View view, Context c){
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

}

