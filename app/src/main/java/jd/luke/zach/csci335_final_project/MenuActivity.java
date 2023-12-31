package jd.luke.zach.csci335_final_project;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MenuActivity extends AppCompatActivity {
    SharedPreferences prefs;

    Button continue_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        setTheme(prefs.getInt("themePref", R.style.Base_Theme_CSCI335_Final_Project));
        setContentView(R.layout.activity_menu);


        continue_button = findViewById(R.id.continue_button);
        String user_puzzle = prefs.getString("userPuzzle", "none");
        Log.d("IsItNone?", user_puzzle);
        if (!user_puzzle.equals("none")) {
            continue_button.setEnabled(true);
        }
    }

    public void startGame(View view) {
        String user_puzzle = prefs.getString("userPuzzle", "none");

        if (!user_puzzle.equals("none")) {
            MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);

            dialog.setTitle(getResources().getString(R.string.confirm_new_game_dialog_title));
            dialog.setMessage(getResources().getString(R.string.confirm_new_game_dialog_message));
            dialog.setNeutralButton(R.string.dialog_decline, (dialogInterface, id) -> dialogInterface.cancel());
            dialog.setPositiveButton(R.string.dialog_accept, (dialogInterface, id) -> {
                Intent intent = new Intent(this, GameActivity.class);
                intent.putExtra(GameActivity.EXTRA_START_NEW_GAME, true);
                intent.putExtra(GameActivity.EXTRA_NEXT_PUZZLE, true);
//                intent.putExtra(GameActivity.EXTRA_RETRY_PUZZLE, false);
                startActivity(intent);
            });
            dialog.show();
        } else {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(GameActivity.EXTRA_START_NEW_GAME, true);
            intent.putExtra(GameActivity.EXTRA_NEXT_PUZZLE, true);
//            intent.putExtra(GameActivity.EXTRA_RETRY_PUZZLE, false);

            startActivity(intent);
        }
    }

    public void continueGame(View view) {
        String user_puzzle = prefs.getString("userPuzzle", "none");
        if (!user_puzzle.equals("none")) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra(GameActivity.EXTRA_START_NEW_GAME, false);
            intent.putExtra(GameActivity.EXTRA_NEXT_PUZZLE, false);
//            intent.putExtra(GameActivity.EXTRA_RETRY_PUZZLE, false);

            startActivity(intent);
        }
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
    SharedPreferences.OnSharedPreferenceChangeListener prefListener = (prefs, key) -> recreate();

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Death", prefs.getString("userPuzzle", "We Resumed Menu"));
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener);
    }

    @Override
    protected void onPause() {
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
        super.onPause();
    }

}