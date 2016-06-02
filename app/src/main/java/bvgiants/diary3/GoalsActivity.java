package bvgiants.diary3;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Zack on 2/06/2016.
 */
public class GoalsActivity extends AppCompatActivity {


    //Database init
    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;
    public User User;
    public static Context context;
    private int USERID;
    private User user;


    // User input strings
    private EditText sugarInput;
    private EditText stepsInput;
    private EditText kjInput;
    private EditText calInput;

    Button saveButton;

    // Constant nutrition Ints. Probably need some for minimum/maximum etc etc
    public static final int RECOMMENDED_SUGAR = 1;
    public static final int RECOMMENDED_STEPS = 1;
    public static final int RECOMMENDED_CALORIES = 1;
    public static final int RECOMMENDED_KJS = 1;
    public static final int MINIMUM_CALORIES = 1;

    // On Create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //Initialise vars
        saveButton = (Button) findViewById(R.id.save_goals);
        sugarInput = (EditText) findViewById(R.id.sugarGoal);
        stepsInput = (EditText) findViewById(R.id.stepsGoal);
        kjInput = (EditText) findViewById(R.id.kJGoal);
        calInput = (EditText) findViewById(R.id.calGoal);


        //Initialise database
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
        user = databaseHelper.getUserTraits(USERID);


        // Set onClick listener for Save button.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
    }

    // Inflate the top menu bar.
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
        if (id == R.id.action_home) {
            Intent startHome = new Intent(this, MainActivity.class);
            startActivity(startHome);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Validate user input. If no errors, query database (UpdateUser).
    public void checkInput() {
        if (!validate()) {
            saveFailed();
            return;
        } else {
            updateGoals();
        }
        saveButton.setEnabled(false);

    } // End checkInput()

    // Validate the USER GOALS:

    public boolean validate() {

        // Strings to check length of input
        String userSugar = sugarInput.getText().toString();
        String userSteps = stepsInput.getText().toString();
        String userKjs = kjInput.getText().toString();
        String userCals = calInput.getText().toString();

        // Integers for more complex validation
        int sugar = Integer.parseInt(sugarInput.getText().toString());
        int steps = Integer.parseInt(stepsInput.getText().toString());
        int kJs = Integer.parseInt(kjInput.getText().toString());
        int calories = Integer.parseInt(calInput.getText().toString());

        // Validate boolean
        boolean valid = true;

        /// Check users SUGAR input.

        if (userSugar.isEmpty() || userSugar.length() > 6) {
            sugarInput.setError("Please enter a valid number (grams)");
            valid = false;
        } else if (sugar > RECOMMENDED_SUGAR) {
            warningMessage("daily", "SUGAR", "male", RECOMMENDED_SUGAR);
        } else {
            sugarInput.setError(null);
        } //End If Else


        ///  Check users STEPS input.

        if (userSteps.isEmpty() || userSteps.length() > 10 ) {
            stepsInput.setError("Please enter a valid number of steps");
            valid = false;
        } else if (steps < RECOMMENDED_STEPS) {
            Toast.makeText(getBaseContext(), "WARNING: The recommended (daily/weekly) step count " +
                    "for the average (gender) is " + RECOMMENDED_STEPS, Toast.LENGTH_LONG).show();
        } else {
            stepsInput.setError(null);
        } //End If Else


        ///  Check users KILOJOULES input.

        if (userKjs.isEmpty() || userKjs.length() > 10 ) {
            kjInput.setError("Please enter a valid number");
            valid = false;
        } else if (kJs < RECOMMENDED_KJS) {
            warningMessage("daily", "KILOJOULE", "male", RECOMMENDED_KJS);
        } else {
            kjInput.setError(null);
        } //End If Else


        ///  Check users CALORIES input.

        if (userCals.isEmpty() || userCals.length() > 10 ) {
           calInput.setError("Please enter a valid number");
            valid = false;
        } else if (calories > RECOMMENDED_CALORIES || calories < MINIMUM_CALORIES) {

            warningMessage("daily", "CALORIE", "male", RECOMMENDED_CALORIES);

        } else {
            stepsInput.setError(null);
        } // End If Else

        return valid;

    } /// End validate()


    public void warningMessage(String time, String nutrition, String gender, int warning){

        Toast.makeText(getBaseContext(), "WARNING: The recommended" + time + nutrition
                + "intake for the average" + gender + " is "  + warning + nutrition,
                Toast.LENGTH_LONG).show();
    }
    //End Warning message


    public void saveFailed()  {
        Toast.makeText(getBaseContext(), "Profile update failed!", Toast.LENGTH_LONG).show();
        saveButton.setEnabled(true);
    } // End saveFailed()

    public void updateGoals() {



    }

} // End Class
