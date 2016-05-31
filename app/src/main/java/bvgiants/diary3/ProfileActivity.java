package bvgiants.diary3;

        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.support.v7.app.AppCompatActivity;
        import android.net.Uri;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.Snackbar;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.View;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.content.Intent;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

/**
 * Created by Zack on 6/05/2016.
 */
public class ProfileActivity extends AppCompatActivity{

    Button saveButton;

    public SQLiteDatabase db;
    public DatabaseHelper databaseHelper;
    public User User;

    private EditText firstName;
    private EditText lastName;
    private EditText height;
    private EditText weight;
    private EditText age;
    private EditText gender;

    public static Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        //Get variables
        context = getApplicationContext();
        saveButton = (Button) findViewById(R.id.save_button);

        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.lastName);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        age = (EditText) findViewById(R.id.age);
        gender = (EditText) findViewById(R.id.gender);

        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

    }

    // Parsed variables
    String fName = firstName.getText().toString();
    String lName = lastName.getText().toString();
    String userGender = gender.getText().toString();

    String heightString = height.getText().toString();
    int userHeight = Integer.parseInt(heightString);

    String weightString = weight.getText().toString();
    int userWeight = Integer.parseInt(weightString);

    String ageString = age.getText().toString();
    int userAge = Integer.parseInt(ageString);


    public void saveUser() {
        if (!validate()) {
            onSaveFailed();
            return;
        }
        else {
            updateUser();
        }
       saveButton.setEnabled(false);
    }


    public void onSaveSuccess() {
        saveButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    // Validate ()
    public boolean validate(){
        boolean valid = true;

        if (fName.isEmpty() || fName.length() < 2)   {
            firstName.setError("please enter a valid first name");
            valid = false;
        } else {
            firstName.setError(null);
        }

        if (lName.isEmpty() || lName.length() < 3 )  {
            lastName.setError("please enter a valid last name");
            valid = false;
        } else {
            lastName.setError(null);
        }

        if (heightString.isEmpty() || heightString.length() < 2 || heightString.length() > 3) {
            height.setError("height must be a realistic number (10cm - 999cm)");
            valid = false;
        } else {
            height.setError(null);
        }

        if (weightString.isEmpty() || weightString.length() < 2 || weightString.length() > 3) {
            weight.setError("weight must be a realistic number (10kg - 999kg)");
            valid = false;
        } else {
           weight.setError(null);
        }

        if (ageString.isEmpty() || ageString.length() > 3) {
            age.setError("age must be a realistic number (1 - 999)");
            valid = false;
        } else {
            age.setError(null);
        }

        if (userGender.isEmpty() || userGender.equalsIgnoreCase("Male")|| userGender.equalsIgnoreCase("female")) {
            gender.setError("Please enter Male or Female");
            valid = false;
        } else {
            gender.setError(null);
        }
        return valid;
    }


    public void updateUser() {

        //Not sure how to get the current user. Either getUserId
        User newUser = new User(User.getId(),fName, lName, userHeight, userWeight, userAge, userGender);
        databaseHelper.insertUserTraits(newUser);
        onSaveSuccess();

    }

    public void onSaveFailed() {
        Toast.makeText(getBaseContext(), "Profile update failed!", Toast.LENGTH_LONG).show();
        saveButton.setEnabled(true);

    }
}
