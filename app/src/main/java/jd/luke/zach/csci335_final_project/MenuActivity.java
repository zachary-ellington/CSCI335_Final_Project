package jd.luke.zach.csci335_final_project;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class MenuActivity extends AppCompatActivity {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        setTheme(prefs.getInt("themePref", R.style.Base_Theme_CSCI335_Final_Project));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void startSettings(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void startTutorial(View view){
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }


    // from here down is for recreating the activity to mesh with changes made in settings
    SharedPreferences.OnSharedPreferenceChangeListener prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            recreate(); // or update the UI elements as needed
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    protected void onPause() {
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
        super.onPause();
    }

}