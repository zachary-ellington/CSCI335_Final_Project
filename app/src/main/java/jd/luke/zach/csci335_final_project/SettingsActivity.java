package jd.luke.zach.csci335_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat sound;
    SwitchCompat vibration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Winter_Theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // get the user preferences and make the editor
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // define the spinners
        // spinner for difficulty
        String[] difficultySpinner = new String[] {
                "Easy", "Medium", "Hard"
        };
        AutoCompleteTextView diffDropdown = findViewById(R.id.difficulty_dropdown);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                difficultySpinner);
        diffDropdown.setText(prefs.getString("diffPref", "Easy")); // set the default text on entering
        diffDropdown.setAdapter(adapter1);
        diffDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = adapter1.getItem(position);
            editor.putString("diffPref", selectedItem);
            editor.commit();
        });


        // spinner for color themes
        String[] themeSpinner = new String[] {
                "Default", "Spring", "Summer", "Fall", "Winter"
        };
        AutoCompleteTextView themeDropdown = findViewById(R.id.themes_dropdown);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                themeSpinner);
        themeDropdown.setText(prefs.getString("themePref", "Default")); // set the default text on entering
        themeDropdown.setAdapter(adapter2);
        themeDropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = adapter2.getItem(position);
            editor.putString("themePref", selectedItem);
            editor.commit();
        });


        // define the switches
        sound = findViewById(R.id.SoundSwitch);
        vibration = findViewById(R.id.VibrationSwitch);

        // set styling for switches based on preferences
        sound.setChecked(prefs.getBoolean("playSound", true));
        vibration.setChecked(prefs.getBoolean("doVibrate", true));

        // switch listeners
        sound.setOnCheckedChangeListener((sw, isChecked)-> {
            if(isChecked) {
                editor.putBoolean("playSound", true);
                editor.commit();
            }
            else {
                editor.putBoolean("playSound", false);
                editor.commit();
            }
        });

        vibration.setOnCheckedChangeListener((sw, isChecked)-> {
            if(isChecked) {
                editor.putBoolean("doVibrate", true);
                editor.commit();
            }
            else {
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
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}