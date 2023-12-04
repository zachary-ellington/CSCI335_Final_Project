package jd.luke.zach.csci335_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        Intent intent = getIntent();
        boolean did_win = intent.getBooleanExtra(GameActivity.EXTRA_WIN, false);

        TextView game_over_message_view = findViewById(R.id.game_over_display_message);
        if (did_win)
            game_over_message_view.setText(R.string.win_message);
        else
            game_over_message_view.setText(R.string.lose_message);

        String puzzle_input = intent.getStringExtra(GameActivity.EXTRA_PUZZLE_INPUT);
        String puzzle_solution = intent.getStringExtra(GameActivity.EXTRA_PUZZLE_SOLUTION);
        String puzzle_original = intent.getStringExtra(GameActivity.EXTRA_PUZZLE_ORIGINAL);
        int primaryColor = intent.getIntExtra(GameActivity.EXTRA_PUZZLE_PRIMARY_COLOR, getColor(R.color.default_primary));
        int errorColor = intent.getIntExtra(GameActivity.EXTRA_PUZZLE_ERROR_COLOR, getColor(R.color.default_error));
        int textColor = intent.getIntExtra(GameActivity.EXTRA_PUZZLE_ORIGINAL, getColor(R.color.text));


        LinearLayout sudokuGrid = findViewById(R.id.game_over_board);
        // gives the whole board a thicker outer border
        sudokuGrid.setBackground(getDrawable(R.drawable.grid_border));
        sudokuGrid.setPadding(7, 7, 7, 7);
        for (int row = 0; row < 9; row++)
        {
            LinearLayout grid_row = new LinearLayout(this);
            grid_row.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams row_params = new LinearLayout.LayoutParams(-1, -1, 1.0f);
            grid_row.setLayoutParams(row_params);
            for (int col = 0; col < 9; col++)
            {
                TextView text = new TextView(this);
                LinearLayout.LayoutParams text_params = new LinearLayout.LayoutParams(-1, -1, 1.0f);
                text.setPadding(0, 0,0,0);
                text.setGravity(Gravity.CENTER);
                text.setLayoutParams(text_params);


                // nightmare setting buttons to have thick borders at the right spots
                if ((row == 2 && col == 3) || (row == 2 && col == 6) || (row == 5 && col == 3)
                        || (row == 5 && col == 6)) { // if it's at an intersection for thick lines
                    text.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_thick_border_left_bottom));
                } else if (row == 2 || row == 5) {
                    text.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_thick_border_bottom));
                } else if (col == 3 || col == 6) {
                    text.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_thick_border_left));
                } else {
                    text.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
                }

                char input_char = puzzle_input.charAt(row * 9 + col);
                char solution_char = puzzle_solution.charAt(row * 9 + col);
                char original_char = puzzle_original.charAt(row * 9 + col);


                if (input_char != '0') {
                    text.setText("" + input_char);
                }

                if (input_char == original_char) {
                    text.setTextColor(textColor);
                } else if (input_char == solution_char) {
                    text.setTextColor(primaryColor);
                } else {
                    text.setTextColor(errorColor);
                }

                text.setTextSize(30);

                grid_row.addView(text);
            }
            sudokuGrid.addView(grid_row);
        }
    }


    public void toMainMenu(View view) {
        finish();
    }
}