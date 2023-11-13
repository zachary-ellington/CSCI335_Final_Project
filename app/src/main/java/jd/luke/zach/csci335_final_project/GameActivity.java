package jd.luke.zach.csci335_final_project;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

import java.util.ArrayList;
import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Game");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        GridLayout sudokuGrid = findViewById(R.id.sudoku_grid);

        Button[][] buttons = new Button[9][9];

        for (int row = 0; row < 9; row++)
        {
            for (int col = 0; col < 9; col++)
            {
                Button button = new Button(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(row, 1f);
                params.columnSpec = GridLayout.spec(col, 1f);
                params.setMargins(0, 0, 0, 0);

                button.setPadding(0, 0, 0, 0);
                button.setLayoutParams(params);
                button.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
                button.setText("4");

                sudokuGrid.addView(button);
                buttons[row][col] = button;
            }
        }


        buttons[4][6].setText("9");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the game menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        if (item.getItemId() == android.R.id.home) {
            // Handle the Up/Home button
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            // Handle the settings action
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to start GameOverActivity, can be triggered by a button click for example
    public void startGameOver(View view) {
        Intent intent = new Intent(this, GameOverActivity.class);
        startActivity(intent);
        finish();
    }
}
