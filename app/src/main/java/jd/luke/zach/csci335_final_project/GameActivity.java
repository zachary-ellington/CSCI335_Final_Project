package jd.luke.zach.csci335_final_project;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.gridlayout.widget.GridLayout;

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
    ArrayList<Button> allForStyles = new ArrayList<>();
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

        GridLayout sudokuGrid = findViewById(R.id.sudoku_grid);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button button = new Button(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(row, 1f);
                params.columnSpec = GridLayout.spec(col, 1f);
                params.setMargins(0, 0, 0, 0);
                button.setPadding(0, 0, 0, 0);
                button.setLayoutParams(params);
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
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { // onclick listener for cells
                        Button clickedCell = (Button) v;
                        if(getCurrentCell() == null)
                            setCurrentCell(clickedCell);
                        setClickedCellStyle(clickedCell);
                        setCurrentCell(clickedCell);
                    }
                }); // to set color: button.setTextColor(getResources().getColor(R.color.primary));
                sudokuGrid.addView(button);
                buttons[row][col] = button;
            }
        }

        ViewGroup.LayoutParams grid_params = sudokuGrid.getLayoutParams();
        int grid_size = sudokuGrid.getMeasuredWidth() < sudokuGrid.getMeasuredHeight() ? sudokuGrid.getMeasuredWidth() : sudokuGrid.getMeasuredHeight();

        grid_params.width = grid_size;
        grid_params.height = grid_size;

        sudokuGrid.setLayoutParams(grid_params);


        LinearLayout sudokuInput = findViewById(R.id.sudoku_linput);
        for (int i = 0; i < 9; i++) {
            Button button = new Button(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(9, 1, 1f);
            params.width = 0;
            params.height = -1;

            params.setMargins(0, 0, 0, 0);

            button.setPadding(0, 0, 0, 0);
            button.setLayoutParams(params);
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.plain_background));
            button.setText(Integer.toString(i + 1));
            button.setTextColor(getColor(R.color.text));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // on click listener for bottom input buttons
                    Button clickedInput = (Button) v;
                    inputGiven(clickedInput); // takes whatever the user clicked for inputting
                }
            });

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
        if(cell.getCurrentTextColor() != getColor(R.color.text)) {  // this if statement avoids replacing fixed puzzle numbers
            if (cell.getText() == btn.getText()) { // allow for removal of numbers
                cell.setText("");
                return;
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

    public void setClickedCellStyle(Button clickedCell) { // for setting styles for selected cell and
        // cells in the same row and column when a cell is selected
//        if(!allForStyles.isEmpty()) {
//            for(Button i : allForStyles) {
//                System.out.println("We cleared " + i);
//                i.getBackground().clearColorFilter();
//            }
//        }

        // clear the highlighting from each of them
        getCurrentCell().getBackground().clearColorFilter();
//        for(Button i : allForStyles) {
//            System.out.println("We cleared " + i);
//            i.getBackground().clearColorFilter();
//        }

        // set highlighting for each cell
        clickedCell.getBackground().setColorFilter(ContextCompat.getColor(GameActivity.this,
                R.color.background), PorterDuff.Mode.MULTIPLY);
//        for(Button i : allForStyles) {
//            if(i != null) {
//                i.getBackground().setColorFilter(ContextCompat.getColor(GameActivity.this,
//                        R.color.background), PorterDuff.Mode.MULTIPLY);
//            }
//        }


    }

    public boolean checkConflicts(String val) { // loop through cells to get conflicts
        // takes in the value of input to check other cells against
        ArrayList<Button> all = getSameRCS();
        for(int i = 0; i < all.size(); i++) {
            if(all.get(i).getText().equals(val)) {
                System.out.println("wrong");
                mistakes++;
                return false;
            }
        }

        return true;
    }

    public ArrayList<Button> getSameRCS() { // gets all the buttons in the same row, column, and square for a clicked cell
        ArrayList<Button> result = new ArrayList<>();
        Button btn = getCurrentCell();
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
