package jd.luke.zach.csci335_final_project;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
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
    public Button getCurrentCell() {
        return currentCell;
    }
    public void setCurrentCell(Button btn) {
        currentCell = btn;
    }
    Button[][] buttons = new Button[9][9];
    ArrayList<String[][]> puzzles = new ArrayList<>();
    HashMap<Button, ArrayList<Integer>> btnmap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        puzzles.add(new String[][]{
                {"4", "9", "0", "6", "7", "1", "0", "3", "0"},
                {"0", "0", "0", "4", "0", "2", "8", "0", "0"},
                {"5", "0", "0", "9", "0", "0", "0", "7", "0"},
                {"1", "0", "4", "5", "0", "0", "0", "0", "0"},
                {"0", "0", "9", "0", "0", "0", "1", "0", "0"},
                {"0", "3", "0", "0", "1", "0", "0", "0", "7"},
                {"0", "0", "0", "7", "0", "9", "2", "0", "5"},
                {"0", "0", "0", "2", "0", "0", "0", "1", "6"},
                {"9", "2", "0", "0", "0", "5", "0", "4", "0"},
        });

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        LinearLayout sudokuGrid = findViewById(R.id.sudoku_grid);
        sudokuGrid.setBackground(ContextCompat.getDrawable(this, R.drawable.grid_border));
        sudokuGrid.setPadding(10, 10, 10, 10); // shows thick border around grid

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

                ArrayList<Integer> myNumbers = new ArrayList<Integer>();
                myNumbers.add(row);
                myNumbers.add(col);

                btnmap.put(button, myNumbers);
                button.setOnClickListener(v -> { // onclick listener for cells
                    Button clickedCell = (Button) v;
                    if(getCurrentCell() == null)
                        setCurrentCell(clickedCell);
                    setClickedCellStyle(clickedCell);
                    setCurrentCell(clickedCell);
                });


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

    public void fillBoard() {
        String[][] currentpuzzle = puzzles.get(0);
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if(currentpuzzle[i][j] != "0") {
                    buttons[i][j].setText((String) currentpuzzle[i][j]);
                    buttons[i][j].setTextColor(getColor(R.color.text));
                }
                else {
                    buttons[i][j].setText((String) "");
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
            } else {
                boolean correct = checkConflicts((String) btn.getText());
                setValueStyle(correct);
                if (!correct && mistakes >= 3) {
                    startGameOver(getCurrentCell()); // takes a placeholder rn
                }
                cell.setText(btn.getText());
            }
        }

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
        ArrayList<Button> style_buttons = getSameRCS(getCurrentCell()); // list of old cells
        for(int i = 0; i < style_buttons.size(); i++) {
            style_buttons.get(i).getBackground().clearColorFilter();
        }

        for(int i = 0; i < buttons.length; i++) { // remove highlighting for same values (e.g. clear highlight all the 1s)
            for(int j = 0; j < buttons[i].length; j++) {
                if(buttons[i][j].getText() == getCurrentCell().getText()) {
                    buttons[i][j].getBackground().clearColorFilter();
                }
            }
        }


        // set highlighting for each cell
        style_buttons = getSameRCS(clickedCell); // re-using list for new cells
        for(int i = 0; i < style_buttons.size(); i++) {
            darkenButton(style_buttons.get(i), 2);
        }

        for(int i = 0; i < buttons.length; i++) { // add highlighting for same values (e.g. highlight all the 1s)
            for(int j = 0; j < buttons[i].length; j++) {
                if(buttons[i][j].getText() == clickedCell.getText()) {
                    darkenButton(buttons[i][j], 10);
                }
            }
        }
        darkenButton(clickedCell, 10);
    }

    private void darkenButton(Button button, int darkness) {
        float[] hsv = new float[3]; // hue, saturation, value
        int color = ContextCompat.getColor(this, R.color.background);
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.0 - (darkness / 100.0); // adjust brightness; 100 is max darkness
        hsv[1] = 0.05f;
        hsv[0] = 0.05f;
        button.getBackground().setColorFilter(Color.HSVToColor(hsv), PorterDuff.Mode.MULTIPLY);
    }


    public boolean checkConflicts(String val) { // loop through cells to get conflicts
        // takes in the value of input to check other cells against
        ArrayList<Button> all = getSameRCS(getCurrentCell());
        for(int i = 0; i < all.size(); i++) {
            if(all.get(i).getText().equals(val)) {
                System.out.println("wrong");
                mistakes++;
                return false;
            }
        }

        return true;
    }

    public ArrayList<Button> getSameRCS(Button btn) { // gets all the buttons in the same row, column, and square for a clicked cell
        ArrayList<Button> result = new ArrayList<>();
        int row = btnmap.get(btn).get(0);
        int col = btnmap.get(btn).get(1);
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

}
