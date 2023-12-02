package jd.luke.zach.csci335_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat sound;
    SwitchCompat vibration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();


        sound = findViewById(R.id.SoundSwitch);
        vibration = findViewById(R.id.VibrationSwitch);

        // set styling on app launched based on preferences
        sound.setChecked(prefs.getBoolean("playSound", true));
        vibration.setChecked(prefs.getBoolean("doVibrate", true));

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