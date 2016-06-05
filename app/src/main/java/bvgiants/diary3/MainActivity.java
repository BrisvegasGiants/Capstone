package bvgiants.diary3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {


    private Context context;
    static float percentageValue;
    private int USERID;
    private User loggedinUser;
    private User userGoals;
    private String alias;

    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;

    private ProgressBar stepCounterProgressBar;
    private ProgressBar calorieCounterProgressBar;
    private ProgressBar kJCounterProgressBar;
    private ProgressBar sugarCounterProgressBar;
    private ArrayList<OrderRow> allFoodOrders = new ArrayList<>();
    private ArrayList<FoodItem> allFoodConsumed = new ArrayList<>();
    private ArrayList<FoodItem> allFoods = new ArrayList<>();

    int userStepGoal = 10000;
    private int calorieCounter;
    private int kJcounter;
    private int sugarCounter;
    private float currentStepsPercent;
    private float currentCalPercent;
    private float currentEnergPercent;
    private float currentSugPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        USERID = getIntent().getIntExtra("UserID", 0);
        loggedinUser = databaseHelper.getUserTraits(USERID);
        userGoals = databaseHelper.getUserGoals(USERID);
        allFoodOrders = databaseHelper.showTodaysFood(USERID);
        alias = databaseHelper.getUserAlias(USERID);
        allFoods = databaseHelper.allFood();

        Log.v("MAIN ACTIVITY USERID= ", String.valueOf(USERID));
        calorieCounter = 0;
        kJcounter = 0;
        sugarCounter = 0;
        currentStepsPercent = 0;
        currentCalPercent = 0;
        currentEnergPercent = 0;
        currentSugPercent = 0;
        for (int i = 0; i < allFoodOrders.size(); i++) {
            for (int k = 0; k < allFoods.size(); k++) {
                if (allFoodOrders.get(i).getFoodId() == allFoods.get(k).getFoodId()) {
                    allFoodConsumed.add(allFoods.get(k));
                    calorieCounter += allFoods.get(k).getCalories();
                    Log.v("FOOD CALORIES", String.valueOf(calorieCounter));
                    kJcounter += allFoods.get(k).getEnergy();
                    Log.v("FOOD ENERGY", String.valueOf(kJcounter));
                    sugarCounter += allFoods.get(k).getSugar();
                    Log.e("FOOD ITEM =", allFoods.get(k).toString());
                }
            }
        }

        startBackgroundProcess(this.findViewById(android.R.id.content), context);


        // Not loading in because google fit isn't connecting yet
        //((ProgressBar)findViewById(R.id.progressBarStepsGoal)).setProgress((int)percentageValue);
        //Log.e("Progress Bar", "Progress Bar is: " + percentageValue);

        stepCounterProgressBar = (ProgressBar) findViewById(R.id.progressBarStepsGoal);
        calorieCounterProgressBar = (ProgressBar) findViewById(R.id.progressBarCalories);
        kJCounterProgressBar = (ProgressBar) findViewById(R.id.progressBarKilojoules);
        sugarCounterProgressBar = (ProgressBar) findViewById(R.id.progressBarSugar);

        if (loggedinUser.getId() == 0) {
            Log.e("HELLOWORLD", "STAGE ONE COMPLETE");
            Toast.makeText(getBaseContext(), "Hi " + alias + "! " +
                            "Please enter your personal details through the settings window!",
                    Toast.LENGTH_LONG).show();
        } else if (userGoals.getId() == 0) {
            Log.e("HELLOWORLD", "STAGE TWO COMPLETE");
            Toast.makeText(getBaseContext(), "Hi " + alias + "! " +
                            " Please enter your GOALS details through the settings window!",
                    Toast.LENGTH_LONG).show();

            Log.e("USERGOALS ID= ", String.valueOf(userGoals.getId()));
            if (loggedinUser.getId() == 0 || userGoals.getId() == 0) {
                Log.e("HELLOWORLD", "STAGE THREE COMPLETE");
                if (loggedinUser.getAge() == 0 || loggedinUser.getWeight() == 0) {
                    Log.e("HELLOWORLD", "STAGE FOUR COMPLETE");
                    Toast.makeText(getBaseContext(), "Hi " + alias + "!" + "\n" +
                                    "Please enter your personal details through the settings window!",
                            Toast.LENGTH_LONG).show();
                } else if (userGoals.getStepGoal() == 0 || userGoals.getKilojoulesGoal() == 0) {
                    Log.e("HELLOWORLD", "STAGE FIVE COMPLETE");
                    Toast.makeText(getBaseContext(), "Hi " + alias + "!" + "\n" +
                                    "Please enter your GOALS details through the settings window!",
                            Toast.LENGTH_LONG).show();
                }

            }
            if (userGoals.getId() > 0) {
                Log.e("HELLOWORLD", "IT SHOULD WORK?");
                stepCounterProgressBar.setMax(userGoals.getStepGoal());
                calorieCounterProgressBar.setMax(userGoals.getCalorieGoal());
                kJCounterProgressBar.setMax(userGoals.getKilojoulesGoal());
                sugarCounterProgressBar.setMax(userGoals.getSugarGoal());
                currentStepsPercent = ((float) 10000 / userGoals.getStepGoal()) * 100;
                currentCalPercent = ((float) calorieCounter / userGoals.getCalorieGoal()) * 100;
                currentEnergPercent = ((float) kJcounter / userGoals.getKilojoulesGoal()) * 100;
                currentSugPercent = ((float) sugarCounter / userGoals.getSugarGoal()) * 100;
                Log.e("CurrentSteps% ", String.valueOf(currentStepsPercent));
                Log.e("GOALS Step =", String.valueOf(userGoals.getStepGoal()));
                Log.e("GOALS Cal=", String.valueOf(userGoals.getCalorieGoal()));
                Log.e("GOALS Kil=", String.valueOf(userGoals.getKilojoulesGoal()));
                Log.e("GOALS Sugar=", String.valueOf(userGoals.getSugarGoal()));
            }


            stepCounterProgressBar.setProgress(10000);
            calorieCounterProgressBar.setProgress(calorieCounter);
            kJCounterProgressBar.setProgress(kJcounter);
            sugarCounterProgressBar.setProgress(sugarCounter);


            // Have to change the percentage in the progress bars

            ((TextView) findViewById(R.id.currentSteps)).setText(String.format("%.2f", currentStepsPercent) + "%");
            ((TextView) findViewById(R.id.currentCalorie)).setText(String.format("%.2f", currentCalPercent) + "%");
            ((TextView) findViewById(R.id.currentKJ)).setText(String.format("%.2f", currentEnergPercent) + "%");
            ((TextView) findViewById(R.id.currentSugar)).setText(String.format("%.2f", currentSugPercent) + "%");


        }
    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.eat_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            if (id == R.id.action_settings) {
                Intent startSettings = new Intent(this, SettingsActivity.class);
                Bundle userCreds = new Bundle();
                userCreds.putInt("UserID", USERID);
                startSettings.putExtras(userCreds);
                startActivity(startSettings);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        // Takes user to selected screen(Activity)

    public void menuSelect(View v) {
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

            case (R.id.buttonMaps):
                Intent startMaps = new Intent(this, MapsActivity.class);
                startActivity(startMaps);
                break;

        }
    }

    public void startBackgroundProcess(View view, Context c) {
        startService(new Intent(getBaseContext(), BackgroundService.class));
    }

}
