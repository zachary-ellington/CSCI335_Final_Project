package jd.luke.zach.csci335_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;

import java.util.Objects;


public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Fall_Theme);
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