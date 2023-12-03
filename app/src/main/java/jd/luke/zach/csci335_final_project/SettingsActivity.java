package jd.luke.zach.csci335_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat sound;
    SwitchCompat vibration;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // get the user preferences and make the editor
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        setTheme(prefs.getInt("themePref", R.style.Base_Theme_CSCI335_Final_Project));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        // don't ask me why, but not having this handler delay here makes it not work
        // correctly when recreating the activity to update colors at runtime
        new Handler().postDelayed(() -> {
            // define the spinners
            // spinner for difficulty
            String[] difficultySpinner = new String[]{
                    "Easy", "Medium", "Hard"
            };
            AutoCompleteTextView diffDropdown = findViewById(R.id.difficulty_dropdown);
            diffDropdown.clearFocus();
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    difficultySpinner);
            diffDropdown.setText(prefs.getString("diffPref", "Easy")); // set the default text on entering
            diffDropdown.setAdapter(adapter1);
            diffDropdown.setOnItemClickListener((parent, view, position, id) -> {
                String selectedItem = adapter1.getItem(position);
                editor.putString("diffPref", selectedItem);
                editor.apply();
            });

            // spinner for color themes
            String[] themeSpinner = new String[]{
                    "Default", "Spring", "Summer", "Fall", "Winter"
            };
            AutoCompleteTextView themeDropdown = findViewById(R.id.themes_dropdown);
            themeDropdown.clearFocus();
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item,
                    themeSpinner);
            themeDropdown.setText(prefs.getString("themePrefStr", "Default")); // set the default text on entering
            themeDropdown.setAdapter(adapter2);
            themeDropdown.setOnItemClickListener((parent, view, position, id) -> {
                // themePrefStr for display purposes
                // themePref for functionality purposes
                String selectedItem = adapter2.getItem(position);
                editor.putString("themePrefStr", selectedItem);
                // since selectedItem is a string, we have to do this in order to retrieve the proper value later in code
                switch (Objects.requireNonNull(selectedItem)) {
                    case "Default":
                        editor.putInt("themePref", R.style.Base_Theme_CSCI335_Final_Project);
                        break;
                    case "Spring":
                        editor.putInt("themePref", R.style.Spring_Theme);
                        break;
                    case "Summer":
                        editor.putInt("themePref", R.style.Summer_Theme);
                        break;
                    case "Fall":
                        editor.putInt("themePref", R.style.Fall_Theme);
                        break;
                    case "Winter":
                        editor.putInt("themePref", R.style.Winter_Theme);
                        break;
                }
                editor.commit();
                recreate();
            });
        }, 500);


        // define the switches
        sound = findViewById(R.id.SoundSwitch);
        vibration = findViewById(R.id.VibrationSwitch);

        // set styling for switches based on preferences
        sound.setChecked(prefs.getBoolean("playSound", true));
        vibration.setChecked(prefs.getBoolean("doVibrate", true));

        // switch listeners
        sound.setOnCheckedChangeListener((sw, isChecked) -> {
            if (isChecked) {
                editor.putBoolean("playSound", true);
                editor.commit();
            } else {
                editor.putBoolean("playSound", false);
                editor.commit();
            }
        });

        vibration.setOnCheckedChangeListener((sw, isChecked) -> {
            if (isChecked) {
                editor.putBoolean("doVibrate", true);
                editor.commit();
            } else {
                editor.putBoolean("doVibrate", false);
                editor.commit();
            }
        });

        // toolbar stuff
        Toolbar myToolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Settings");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    }