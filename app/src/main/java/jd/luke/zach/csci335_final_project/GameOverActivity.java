package jd.luke.zach.csci335_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;


public class GameOverActivity extends AppCompatActivity {
    SharedPreferences prefs;


    public static final String EXTRA_PUZZLE_INPUT = "jd.luke.zach.csci335_final_project.GameOverActivity.puzzle_input";
    public static final String EXTRA_PUZZLE_SOLUTION = "jd.luke.zach.csci335_final_project.GameOverActivity.puzzle_solution";
    public static final String EXTRA_PUZZLE_ORIGINAL = "jd.luke.zach.csci335_final_project.GameOverActivity.puzzle_original";
    public static final String EXTRA_PUZZLE_PRIMARY_COLOR = "jd.luke.zach.csci335_final_project.GameOverActivity.primary_color";
    public static final String EXTRA_PUZZLE_ERROR_COLOR = "jd.luke.zach.csci335_final_project.GameOverActivity.error_color";
    public static final String EXTRA_PUZZLE_TEXT_COLOR = "jd.luke.zach.csci335_final_project.GameOverActivity.text_color";
    public static final String EXTRA_WIN = "jd.luke.zach.csci335_final_project.GameOverActivity.win";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        setTheme(prefs.getInt("themePref", R.style.Base_Theme_CSCI335_Final_Project));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);


        Toolbar myToolbar = findViewById(R.id.game_over_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Game Over");

        Intent intent = getIntent();
        boolean did_win = intent.getBooleanExtra(GameOverActivity.EXTRA_WIN, false);

        TextView game_over_message_view = findViewById(R.id.game_over_display_message);

        if (did_win) {
            game_over_message_view.setText(R.string.win_message);
            final MediaPlayer success_sound = MediaPlayer.create(this, R.raw.success);
            if(prefs.getBoolean("playSound", true)) {
                success_sound.start();
            }
        } else {
            game_over_message_view.setText(R.string.lose_message);
            final MediaPlayer failure_sound = MediaPlayer.create(this, R.raw.failure);
            if(prefs.getBoolean("playSound", true)) {
                failure_sound.start();
            }
        }



        String puzzle_input = intent.getStringExtra(GameOverActivity.EXTRA_PUZZLE_INPUT);
        String puzzle_solution = intent.getStringExtra(GameOverActivity.EXTRA_PUZZLE_SOLUTION);
        String puzzle_original = intent.getStringExtra(GameOverActivity.EXTRA_PUZZLE_ORIGINAL);
        int primaryColor = intent.getIntExtra(GameOverActivity.EXTRA_PUZZLE_PRIMARY_COLOR, getColor(R.color.default_primary));
        int errorColor = intent.getIntExtra(GameOverActivity.EXTRA_PUZZLE_ERROR_COLOR, getColor(R.color.default_error));
        int textColor = intent.getIntExtra(GameOverActivity.EXTRA_PUZZLE_ORIGINAL, getColor(R.color.text));


        LinearLayout sudokuGrid = findViewById(R.id.game_over_board);
        // gives the whole board a thicker outer border
        sudokuGrid.setBackground(AppCompatResources.getDrawable(this, R.drawable.grid_border));
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

                assert puzzle_input != null;
                char input_char = puzzle_input.charAt(row * 9 + col);
                assert puzzle_solution != null;
                char solution_char = puzzle_solution.charAt(row * 9 + col);
                assert puzzle_original != null;
                char original_char = puzzle_original.charAt(row * 9 + col);


                if (input_char != '0') {
                    text.setText(String.valueOf(input_char));
                }

                if (input_char == original_char) {
                    text.setTextColor(textColor);
                } else if (input_char == solution_char) {
                    text.setTextColor(primaryColor);
                } else {
                    text.setTextColor(errorColor);
                }

                text.setTextSize(20);

                grid_row.addView(text);
            }
            sudokuGrid.addView(grid_row);
        }
    }


    public void toMainMenu(View view) {
        finish();
    }

    public void retryPuzzle(View view) {
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(GameActivity.EXTRA_START_NEW_GAME, true);
//        intent.putExtra(GameActivity.EXTRA_RETRY_PUZZLE, true);
        intent.putExtra(GameActivity.EXTRA_NEXT_PUZZLE, false);

        startActivity(intent);
        finish();
    }


    public void nextPuzzle(View view) {
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra(GameActivity.EXTRA_START_NEW_GAME, true);
//        intent.putExtra(GameActivity.EXTRA_RETRY_PUZZLE, false);
        intent.putExtra(GameActivity.EXTRA_NEXT_PUZZLE, true);

        startActivity(intent);
        finish();
    }



}