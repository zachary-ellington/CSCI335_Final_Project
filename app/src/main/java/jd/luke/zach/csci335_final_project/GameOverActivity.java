package jd.luke.zach.csci335_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;


public class GameOverActivity extends AppCompatActivity {
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        setTheme(prefs.getInt("themePref", R.style.Base_Theme_CSCI335_Final_Project));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);


        Toolbar myToolbar = findViewById(R.id.toolbar8);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Game Over");
    }


    public void toMainMenu(View view) {
        finish();
    }
}