package jd.luke.zach.csci335_final_project;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class GameActivity extends AppCompatActivity {
    Boolean stillGoing = true;

    Button[][] buttons = new Button[9][9];
    SharedPreferences prefs; // user preferences
    SharedPreferences.Editor editor;

    public static final String EXTRA_START_NEW_GAME = "jd.luke.zach.csci335_final_project.GameActivity.start_new_game";
//    public static final String EXTRA_RETRY_PUZZLE = "jd.luke.zach.csci335_final_project.GameActivity.retry";
    public static final String EXTRA_NEXT_PUZZLE = "jd.luke.zach.csci335_final_project.GameActivity.next";


    // define puzzles
    private final String[] puzzles = new String[] {
            "490671030000402800500900070104500000009000100030010007000709205000200016920005040",
            "000500002700600800001000300076050000002000000080000700390072600020000090050018030",
            "800201040000000107709006003006340000003060000002000008020903000000080090000007080",
            "100003400000600020008005300001004070406800100000000005065080002802001600000000000",
            "809402037020090500360000800500000000080037000076008000000000093700300000900054000",
            "050800607000600520100509080040001700007006490000000000921000070000005100560000000",
            "001200000000047000789006000060008701070002400100000906036000000800090000400070000",
            "400070009008605070005000026000800704670000500000000000069000000000590010020010040",
            "080006070002407010003000000000010800201060037300000240106000000034070000000090000",
            "000000040000000906350100800510030400004910008060004000030000160200000059100000000",
            "000000007000050060057000824000005782090000600008070000400009050015068200009000408",
            "000639710000000340900000000603700008000081000000500000005370600009040800700000100",
            "000000030000004060000090400002006000106300500097500004001005900049200007070010040",
            "000000003029003500800900006600000190500009000001600057000300000080000020176200300",
            "300000000901000000004701000080020073003050092000008001490500000100300007008000109",
            "012060000900200860005400120670004000401000090000820040096100000000090000004000700",
            "400000805030000000000700000020000060000080400000010000000603070500200000104000000",
            "520006000000000701300000000000400800600000050000000000041800000000030020008700000",
            "600000803040700000000000000000504070300200000106000000020000050000080600000010000",
            "480300000000000071020000000705000060000200800000000000001076000300000400000050000",
            "000014000030000200070000000000900030601000000000000080200000104000050600000708000"
    };
    private final String[] solutions = new String[] {
            "498671532713452869562938471174596328689327154235814697341769285857243916926185743",
            "869534172735621849241789356476253981912847563583196724398472615127365498654918237",
            "835271946264839157719456823976348512583162479142795638428913765357684291691527384",
            "157293468943618527628745391581934276476852139239167845365489712892371654714526983",
            "859462137127893546364175829593216478481537962276948315645781293718329654932654781",
            "259834617483617529176529384842391756317256498695748231921463875734985162568172943",
            "341289567625347198789516342264958731973162485158734926536421879817693254492875613",
            "416278359298635471735149826352861794671924583984753162169487235843592617527316948",
            "489126375652437918713985624547312896291864537368759241126543789934678152875291463",
            "671298345482753916359146872518632497724915638963874521837529164246381759195467283",
            "861942537234857961957136824146395782793281645528674193482719356315468279679523418",
            "284639715576812349931457286613794528457281963892563471145378692329146857768925134",
            "764152839918734265523698471452986713186347592397521684631475928849263157275819346",
            "465821973729463518813957246637582194548719632291634857952378461384196725176245389",
            "376245918951863724824791635589126473613457892742938561497512386165389247238674159",
            "312568974947213865865479123678954231421637598539821647796145382183792456254386719",
            "417369825632158947958724316825437169791586432346912758289643571573291684164875293",
            "527316489896542731314987562172453896689271354453698217941825673765134928238769145",
            "617459823248736915539128467982564371374291586156873294823647159791385642465912738",
            "487312695593684271126597384735849162914265837268731549851476923379128456642953718",
            "962314857134587269578296413847962531651873942329145786285639174793451628416728395"
    };


    private char[] puzzle_input = new char[81];

    // define puzzle elements
    private Button currentCell; // cell that the user last clicked on
    public int mistakes;
    private int puzzle_in_use_index = 0;
    int mistake_limit ;

    HashMap<Button, Integer> button_map = new HashMap<>();

    // displaying timer and mistakes below
    TextView mistake_counter;


    // whole bunch of timer stuff
    TextView timerTextView;
    long total_time_ms = 0;
    long lastTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    final Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long now = System.currentTimeMillis();
            total_time_ms += now - lastTime;
            lastTime = now;
            int seconds = (int) (total_time_ms / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format(Locale.getDefault(), "%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    // initialize colors to use depending on theme
    int primaryColor;

    int textColor;
    int errorColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        // define user preferences
        prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = prefs.edit();
        setTheme(prefs.getInt("themePref", R.style.Base_Theme_CSCI335_Final_Project));

        // get current index value
        int puzzle_index = prefs.getInt("puzzleIndex", -1);
        if (puzzle_index == -1) {
            puzzle_in_use_index = 0;
            editor.putInt("puzzleIndex", puzzle_in_use_index);
            editor.commit();
        } else {
            puzzle_in_use_index = puzzle_index;
        }

        boolean is_next_puzzle = intent.getBooleanExtra(GameActivity.EXTRA_NEXT_PUZZLE, false);
        if (is_next_puzzle) {
            puzzle_in_use_index = (puzzle_in_use_index + 1) % puzzles.length;
            editor.putInt("puzzleIndex", puzzle_in_use_index);
            editor.commit();
        }

        boolean should_start_new_game = intent.getBooleanExtra(GameActivity.EXTRA_START_NEW_GAME, false);
        if(should_start_new_game) {
            editor.remove("mistakes");
            editor.remove("userPuzzle");
            editor.remove("time");
            editor.commit();
        }

        setContentView(R.layout.activity_game);
        //refresh mistake count
        mistakes = prefs.getInt("mistakes", 0);

        // set difficulty
        String difficulty = prefs.getString("diffPref", "Easy");
        if(difficulty.equals("Easy")) {
            mistake_limit = 5;
        }
        else if(difficulty.equals("Medium")) {
            mistake_limit = 3;
        }
        else {
            mistake_limit = 2;
        }
        mistake_counter = findViewById(R.id.mistakes);
        mistake_counter.setText(MessageFormat.format("Mistakes: {0}/{1}", mistakes, mistake_limit));


        // make the colors happen
        defineColors();

        // create media player for button clicks
        final MediaPlayer click_sound = MediaPlayer.create(this, R.raw.ping);
        timerTextView = findViewById(R.id.timer);

        // toolbar stuff
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Sudoku");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // start defining board
        LinearLayout sudokuGrid = findViewById(R.id.sudoku_grid);
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
                Button button = new Button(this);
                LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(-1, -1, 1.0f);
                button.setPadding(0, 0,0,0);
                button.setLayoutParams(button_params);

                // nightmare setting buttons to have thick borders at the right spots
                if ((row == 2 && col == 3) || (row == 2 && col == 6) || (row == 5 && col == 3)
                        || (row == 5 && col == 6)) { // if it's at an intersection for thick lines
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_thick_border_left_bottom));
                } else if (row == 2 || row == 5) {
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_thick_border_bottom));
                } else if (col == 3 || col == 6) {
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.btn_thick_border_left));
                } else {
                    button.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
                }

                button.setText("");
                button.setTextSize(24);

                button_map.put(button, row * 9 + col);
                button.setOnClickListener(v -> { // onclick listener for cells
                    Button clickedCell = (Button) v;
                    if(getCurrentCell() == null)
                        setCurrentCell(clickedCell);
                    setClickedCellStyle(clickedCell); // styling for
                    setCurrentCell(clickedCell);
                }); // to set color: button.setTextColor(getResources().getColor(R.color.fall_primary));


                grid_row.addView(button);
                buttons[row][col] = button;
            }
            sudokuGrid.addView(grid_row);
        }

        // creating inputs at bottom
        LinearLayout sudokuInput = findViewById(R.id.sudoku_input);
        for (int i = 0; i < 10; i++) {
            Button button = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(9, 1, 1f);
            params.width = 0;
            params.height = -1;

            params.setMargins(0, 0, 0, 0);

            button.setPadding(0, 0, 0, 0);
            button.setTextSize(30);
            button.setLayoutParams(params);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.plain_background));
            // if number
            if (i < 9)
            {
                button.setText(String.format(String.valueOf(i + 1)));
                button.setTextColor(textColor);
                button.setSoundEffectsEnabled(false);
                button.setOnClickListener(v -> { // on click listener for bottom input buttons
                    if (getCurrentCell() != null) {
                        Button clickedInput = (Button) v;
                        playSound(click_sound);
                        inputGiven(clickedInput); // takes whatever the user clicked for inputting
                        setClickedCellStyle(getCurrentCell());
                        // 0 if don't end, 1 if end because mistakes, 2 if end because victory
                        int endgame = checkEndGameStatus();
                        if(endgame != 0) {
                            startGameOver(endgame);
                        }
                    }
                });
            }
            // for backspace
            else
            {
                button.setSoundEffectsEnabled(false);
                button.setForeground(ContextCompat.getDrawable(this, R.drawable.backspace));
                button.setOnClickListener(v -> {
                    Button cell = getCurrentCell();
                    if(button_map.get(cell) == null) {
                        Log.d("Error", "Error, null reference");
                        return;
                    }
                    Integer cell_index = button_map.get(cell);
                    if(cell != null && cell_index != null) {
                        boolean is_empty_from_puzzle = getCurrentPuzzle().charAt(cell_index) == '0';
                        if (is_empty_from_puzzle) {
                            removeInput(cell);
                            playSound(click_sound);
                        }
                    }
                });
            }

            sudokuInput.addView(button);
        }

        total_time_ms = prefs.getLong("time", 0);
        lastTime = System.currentTimeMillis();

        timerHandler.postDelayed(timerRunnable, 0); // start timer

        if (should_start_new_game) {
            fillBoard("none");
        } else {
            fillBoard(prefs.getString("userPuzzle", "none"));
        }

    }

    // ************************************************************************************************************
    // *************** END OF onCreate() **************************************************************************
    // ************************************************************************************************************


    //save the board on pause
    @Override
    public void onPause() {
        if(stillGoing) {
            editor.putString("userPuzzle", new String(puzzle_input));
            editor.putLong("time", total_time_ms);
            editor.commit();
        }
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        prefs.unregisterOnSharedPreferenceChangeListener(prefListener);
    }
    // listener for being out of the app and preferences change
    SharedPreferences.OnSharedPreferenceChangeListener prefListener = (prefs, key) -> {
        if(key.equals("themePref")) {
            recreate();
        }
        else if(key.equals("playSound")) {
            for(Button[] button : buttons) {
                for(Button value : button) {
                    value.setSoundEffectsEnabled(prefs.getBoolean("playSound", true));
                }
            }
        }
    };
    public Button getCurrentCell() {
        return currentCell;
    }
    public void setCurrentCell(Button btn) {
        currentCell = btn;
    }

    @SuppressLint("SetTextI18n")
    public void fillBoard(String userpuzzle) {
        if(userpuzzle.equals("none")) {
            String current_puzzle = getCurrentPuzzle();
            puzzle_input = current_puzzle.toCharArray();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    char curr_char = current_puzzle.charAt(i * 9 + j);
                    if (curr_char != '0') {
                        buttons[i][j].setText(String.valueOf(curr_char));
                        buttons[i][j].setTextColor(textColor);
                    } else {
                        buttons[i][j].setText("");
                    }
                }
            }
        }
        else {
            puzzle_input = userpuzzle.toCharArray();
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    int index = i * 9 + j;
                    char curr_char = userpuzzle.charAt(index);
                    if (curr_char != '0' && getCurrentPuzzleSolution().charAt(index) == curr_char
                            && getCurrentPuzzle().charAt(index) == '0') {
                        buttons[i][j].setText(String.valueOf(curr_char));
                        buttons[i][j].setTextColor(primaryColor);
                    } else if (curr_char != '0' && getCurrentPuzzleSolution().charAt(index) != curr_char
                            && getCurrentPuzzle().charAt(index) == '0'){
                        buttons[i][j].setText(String.valueOf(curr_char));
                        buttons[i][j].setTextColor(errorColor);
                    } else if (curr_char != '0' && getCurrentPuzzleSolution().charAt(index) == curr_char
                            && getCurrentPuzzle().charAt(index) != '0') {
                        buttons[i][j].setText(String.valueOf(curr_char));
                        buttons[i][j].setTextColor(textColor);
                    }
                    else  {
                        buttons[i][j].setText("");
                    }
                }
            }
        }
    }


    public void inputGiven(Button btn) { // takes whatever the user clicked for inputting
        Button cell = getCurrentCell();
        if (cell == null)
            return;
        Integer cell_index = button_map.get(cell);
        if(cell_index != null && getCurrentPuzzle().charAt(cell_index) == '0') {  // this if statement avoids replacing fixed puzzle numbers
            if (cell.getText().equals(btn.getText())) { // allow for removal of numbers
                removeInput(cell);
            } else {
                char value = btn.getText().charAt(0);
                boolean correct = isCorrect(cell, value);
                if (!correct) {
                    final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    doVibrate(vibrator, 250);
                    mistakes++;
                    editor.putInt("mistakes", mistakes);
                    editor.apply();
                    if(checkEndGameStatus() != 1) {
                        final MediaPlayer error_sound = MediaPlayer.create(this, R.raw.error);
                        playSound(error_sound);
                    }
                    // display mistake text
                    mistake_counter.setText(MessageFormat.format("Mistakes: {0}/{1}", mistakes, mistake_limit));
                }
                setValueStyle(correct);

                for (Button[] button : buttons) { // remove highlighting on cells with values
                    for (int j = 0; j < buttons.length; j++) {
                        if (button[j].getText().equals(getCurrentCell().getText())) {
                            button[j].getBackground().clearColorFilter();
                        }
                    }
                    darkenButton(getCurrentCell(), 10);
                }
                cell_index = button_map.get(cell);
                if (cell_index != null) {
                    puzzle_input[cell_index] = btn.getText().charAt(0);
                    cell.setText(btn.getText());
                }
            }
        }

    }

    /**
     * Checks if the value is correct for the cell
     * @param cell The button in the sudoku grid that will be checked
     * @param value The character (1-9) that is input into the cell button
     * @return True if the input value aligns correctly with solution value
     */
    public boolean isCorrect(Button cell, char value) {
        Integer index = button_map.get(cell);
        if (index == null)
            return false;
        String solution = getCurrentPuzzleSolution();
        char solution_char = solution.charAt(index);
        return value == solution_char;
    }


    public void removeInput(Button cell) {
            for (Button[] button : buttons) {
                for (int j = 0; j < buttons.length; j++) {
                    if (button[j].getText().equals(getCurrentCell().getText()) && !button[j].getText().equals("")) {
                        button[j].getBackground().clearColorFilter();
                    }
                }
                darkenButton(getCurrentCell(), 10);
            }
            Integer cell_index = button_map.get(cell);
            if (cell_index != null) {
                puzzle_input[cell_index] = '0';
                cell.setText("");
            }
    }


    public void setValueStyle(boolean correct) {
        // takes true if value is correct
        // maybe set selected here or something for styles

        // buttons[row + 1][col + 1].setText("HELP");
        if(correct) {
            getCurrentCell().setTextColor(primaryColor);
        }
        else {
            getCurrentCell().setTextColor(errorColor);
        }
    }

    public void setClickedCellStyle(Button clickedCell) {

        // clear the highlighting from each of them
        getCurrentCell().getBackground().clearColorFilter();
        ArrayList<Button> style_buttons = getSameRCS(getCurrentCell()); // list of old cells
        for(int i = 0; i < style_buttons.size(); i++) {
            style_buttons.get(i).getBackground().clearColorFilter();
        }


        for (Button[] button : buttons) { // remove highlighting for same values (e.g. clear highlight all the 1s)
            for (Button value : button) {
                if (value.getText().equals(getCurrentCell().getText())) {
                    value.getBackground().clearColorFilter();
                }
            }
        }

        // set highlighting for each cell
        style_buttons = getSameRCS(clickedCell); // re-using list for new cells
        for(int i = 0; i < style_buttons.size(); i++) {
            darkenButton(style_buttons.get(i), 2);
        }

        for (Button[] button : buttons) { // add highlighting for same values (e.g. highlight all the 1s)
            for (Button value : button) {
                // skip empties and get same values
                if (value.getText() != "" && value.getText().equals(clickedCell.getText())) {
                    darkenButton(value, 10);
                }
            }
        }
        darkenButton(clickedCell, 10);
    }

    private void darkenButton(Button button, int darkness) {
        float[] hsv = new float[3]; // hue, saturation, value
        int color = ContextCompat.getColor(this, R.color.fall_background);
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.0 - (darkness / 100.0); // adjust brightness; 100 is max darkness
        hsv[1] = 0.05f;
        hsv[0] = 0.05f;
        button.getBackground().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.MULTIPLY);
    }


    public ArrayList<Button> getSameRCS(Button btn) { // gets all the buttons in the same row, column, and square for a clicked cell
        ArrayList<Button> result = new ArrayList<>();
        Integer index = button_map.get(btn);
        if (index == null) {
            return null;
        }
        int row = index / 9;
        int col = index % 9;
        for(int i = 0; i < 9; i++) { // adding all buttons in row and column
            result.add(buttons[row][i]);
            result.add(buttons[i][col]);
        }
        for(int i = 0; i < 3; i++) { // adding all buttons in square
            int j = 0;
            while (j < 3) { // using while loop to avoid warning
                result.add(buttons[3 * (row/3) + i][3 * (col/3) + j]);
                j++;
            }
        }

        return result;
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
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            // Handle the settings action
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        } else if (item.getItemId() == R.id.action_tutorial){
            // Handle the tutorial action
            Intent tutorialIntent = new Intent(this, TutorialActivity.class);
            startActivity(tutorialIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int checkEndGameStatus() {
        // they made too many mistakes
        if(mistakes >= mistake_limit) {
            return 1;
        }
        // there are values that are unassigned or incorrect, so it's unfinished (
        for(Button[] button: buttons) {
            for(Button value : button) {
                if(value.getText().equals("") || !isCorrect(value, value.getText().charAt(0)))
                    return 0;
            }
        }
        // otherwise they must have won
        return 2;
    }

    public void startGameOver(int result) {
        editor.remove("mistakes");
        editor.remove("userPuzzle");
        Boolean test = editor.commit();
        Log.d("editorSuccess", String.valueOf(test));
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra(GameOverActivity.EXTRA_PUZZLE_INPUT, new String(puzzle_input));
        intent.putExtra(GameOverActivity.EXTRA_PUZZLE_ORIGINAL, getCurrentPuzzle());
        intent.putExtra(GameOverActivity.EXTRA_PUZZLE_SOLUTION, getCurrentPuzzleSolution());
        intent.putExtra(GameOverActivity.EXTRA_PUZZLE_PRIMARY_COLOR, primaryColor);
        intent.putExtra(GameOverActivity.EXTRA_PUZZLE_ERROR_COLOR, errorColor);
        intent.putExtra(GameOverActivity.EXTRA_PUZZLE_TEXT_COLOR, textColor);
        intent.putExtra(GameOverActivity.EXTRA_WIN, result == 2);
        stillGoing = false;
        startActivity(intent);
        finish();

    }

    public String getCurrentPuzzleSolution() {
        return solutions[puzzle_in_use_index];
    }
    public String getCurrentPuzzle () {
        return puzzles[puzzle_in_use_index];
    }

    // used for playing any sound based on the shared preferences
    public void playSound(MediaPlayer sound) {
        if(prefs.getBoolean("playSound", true)) {
            sound.start();
        }
    }

    // used for playing any vibration based on the shared preferences
    public void doVibrate(Vibrator vib, int duration) {
        if(prefs.getBoolean("doVibrate", true)) {
            vib.vibrate(duration);
        }
    }

    public void defineColors() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
        primaryColor = typedValue.data;
        getTheme().resolveAttribute(R.attr.customSecondary, typedValue, true);
        getTheme().resolveAttribute(R.attr.customError, typedValue, true);
        errorColor = typedValue.data;
        getTheme().resolveAttribute(R.attr.customText, typedValue, true);
        textColor = typedValue.data;
    }
}
