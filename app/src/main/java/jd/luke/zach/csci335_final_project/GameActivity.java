package jd.luke.zach.csci335_final_project;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class GameActivity extends AppCompatActivity {
    private Button currentCell; // cell that the user last clicked on
    public int mistakes = 0;
    Button[][] buttons = new Button[9][9];
    ArrayList<Button> allForStyles = new ArrayList<>();
    String[] puzzles = new String[] {
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
    String[] solutions = new String[] {
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

    int puzzle_in_use_index = 0;

    HashMap<Button, Integer> button_map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        LinearLayout sudokuGrid = findViewById(R.id.sudoku_grid);

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
                button.setTextSize(30);

                button_map.put(button, row * 9 + col);
                button.setOnClickListener(v -> { // onclick listener for cells
                    Button clickedCell = (Button) v;
                    if(getCurrentCell() == null)
                        setCurrentCell(clickedCell);
                    setClickedCellStyle(clickedCell);
                    setCurrentCell(clickedCell);
                }); // to set color: button.setTextColor(getResources().getColor(R.color.primary));


                grid_row.addView(button);
                buttons[row][col] = button;
            }
            sudokuGrid.addView(grid_row);
        }

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
                button.setText(Integer.toString(i + 1));
                button.setTextColor(getColor(R.color.text));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { // on click listener for bottom input buttons
                        Button clickedInput = (Button) v;
                        inputGiven(clickedInput); // takes whatever the user clicked for inputting
                    }
                });
            }
            // for backspace
            else
            {
                button.setForeground(ContextCompat.getDrawable(this, R.drawable.backspace));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button clickedInput = (Button) v;
                        removeInput();
                    }

                });
            }

            sudokuInput.addView(button);
        }
        fillBoard();
    }

    public Button getCurrentCell() {
        return currentCell;
    }
    public void setCurrentCell(Button btn) {
        currentCell = btn;
    }

    public void fillBoard() {
        String current_puzzle = puzzles[puzzle_in_use_index];
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                char curr_char = current_puzzle.charAt(i * 9 + j);
                if(curr_char != '0') {
                    buttons[i][j].setText("" + curr_char);
                    buttons[i][j].setTextColor(getColor(R.color.text));
                }
                else {
                    buttons[i][j].setText("");
                }
            }
        }
    }


    public void inputGiven(Button btn) { // takes whatever the user clicked for inputting
        Button cell = getCurrentCell();
        if (cell == null)
            return;
        if(cell.getCurrentTextColor() != getColor(R.color.text)) {  // this if statement avoids replacing fixed puzzle numbers
            if (cell.getText() == btn.getText()) { // allow for removal of numbers
                cell.setText("");
                //return;
            } else {
                char value = btn.getText().charAt(0);
                boolean correct = isCorrect(cell, value);
                if (!correct)
                    mistakes++;
                setValueStyle(correct);
                if (!correct && mistakes >= 3) {
                    startGameOver(cell); // takes a placeholder rn
                }
                cell.setText(btn.getText());
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
        Integer integer_index = button_map.get(cell);
        int index = integer_index;
        String solution = getCurrentPuzzleSolution();
        char solution_char = solution.charAt(index);
        return value == solution_char;
    }


    public void removeInput() {
        Button cell = getCurrentCell();
        if (cell == null)
            return;
        if (cell.getCurrentTextColor() != getColor(R.color.text)) {
            cell.setText("");
        }
    }


    public void setValueStyle(boolean correct) { // takes true if value is correct
        // maybe set selected here or something for styles

        // buttons[row + 1][col + 1].setText("HELP");
        if(correct) {
            getCurrentCell().setTextColor(getColor(R.color.primary));
        }
        else {
            getCurrentCell().setTextColor(getColor(R.color.error));
        }
    }

    public void setClickedCellStyle(Button clickedCell) {

        // clear the highlighting from each of them
        getCurrentCell().getBackground().clearColorFilter();

        // set highlighting for each cell
        clickedCell.getBackground().setColorFilter(ContextCompat.getColor(GameActivity.this,
                R.color.background), PorterDuff.Mode.MULTIPLY);


    }

    public ArrayList<Button> getSameRCS() { // gets all the buttons in the same row, column, and square for a clicked cell
        ArrayList<Button> result = new ArrayList<>();
        Button btn = getCurrentCell();
        int index = button_map.get(btn).intValue();
        int row = index / 9;
        int col = index % 9;
        for(int i = 0; i < 9; i++) { // adding all buttons in row and column
            result.add(buttons[row][i]);
            result.add(buttons[i][col]);
        }
        for(int i = 0; i < 3; i++) { // adding all buttons in square
            for (int j = 0; j < 3; j++) {
                result.add(buttons[(3 * (row/3) + i)][(3 * (col/3) + j)]);
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

    public String getCurrentPuzzleSolution() {
        return solutions[puzzle_in_use_index];
    }

}
