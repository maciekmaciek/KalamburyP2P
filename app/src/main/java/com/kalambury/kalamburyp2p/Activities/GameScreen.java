package com.kalambury.kalamburyp2p.Activities;


import android.app.Activity;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import com.kalambury.kalamburyp2p.Communication.GameMode;
import com.kalambury.kalamburyp2p.Components.PaintView;
import com.kalambury.kalamburyp2p.R;
import com.kalambury.kalamburyp2p.Utils.Database;
import com.kalambury.kalamburyp2p.Utils.GuessHelper;

import java.util.Random;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-07-14.
 *
 *
 * Główna aktywność gry, 2 tryby - > rysowanie i odpowiedź
 * przy rysowaniu zablokowana odpowiedź, przy odpowiedzi zablokowane rysowanie
 * W pasku: zegar, historia haseł, przybornik kolorów
 * Niżej plansza do rysowania
 * niżej pole do odpowiedzi (kiedy rysujesz, wyświetla hasło)
 */
public class GameScreen extends Activity {
    // TODO zegar, tryby - rysowanie, odpowiedź
    private byte menuVisibility = 0; // 0 - paintview, 1 - drawingmenu, 2 - scores
    private Database db;
    private PaintView paintView;    //drawing surface
    private SeekBar sizeBar;
    private Pair<Integer, String> haslo;
    private long numOfRows;
    private GameMode mode;
    private GuessHelper guessHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = (GameMode)getIntent().getExtras().get("mode");
        setContentView(R.layout.game_screen);
        paintView = (PaintView) findViewById(R.id.paint_view);

        sizeBar = (SeekBar) findViewById(R.id.size_bar);
        sizeBar.setProgress(getResources().getInteger(R.integer.init_size));
        sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { //drawingMenu change paint size
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                paintView.setCurrentSize(progress + getResources().getInteger(R.integer.size_difference));
                int tempSize = progress == 0 ? 1 : progress;
                ((TextView) findViewById(R.id.size_text)).setText(tempSize + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        db = new Database(getApplicationContext());
        db.open();
        numOfRows = DatabaseUtils.queryNumEntries(db.getSQLiteDB(), "hasla");
        if(mode == GameMode.DRAWING){
            startDrawingTurn();
        } else {
            startGuessingTurn();
        }
    }

    private void startGuessingTurn() {
        ((Button)findViewById(R.id.decide_button)).setText(getResources().getText(R.string.accept));
        ((EditText)findViewById(R.id.edit_answer)).setEnabled(true);
        ((EditText)findViewById(R.id.edit_answer)).setText("");
        paintView.setTouchable(false);
        guessHelper = new GuessHelper(haslo.second, getApplicationContext());
    }

    private void startDrawingTurn() {
        Random r = new Random();
        haslo = db.getHaslo(r.nextInt((int)numOfRows));

        ((Button)findViewById(R.id.decide_button)).setText(getResources().getText(R.string.yield));
        ((EditText)findViewById(R.id.edit_answer)).setEnabled(false);
        ((EditText)findViewById(R.id.edit_answer)).setText(haslo.second);
        paintView.setTouchable(true);

    }


    public void onClick(View view) //drawingMenu
    {
        Drawable d = view.getBackground();
        findViewById(R.id.color_preview).setBackground(d);
        switch (view.getId()) {
            case R.id.button_black:
                paintView.setCurrentColor(Color.BLACK);
                break;
            case R.id.button_blue:
                paintView.setCurrentColor(Color.BLUE);
                break;
            case R.id.button_green:
                paintView.setCurrentColor(getResources().getColor(R.color.green));
                break;
            case R.id.button_red:
                paintView.setCurrentColor(Color.RED);
                break;
            case R.id.button_yellow:
                paintView.setCurrentColor(Color.YELLOW);
                break;
            case R.id.button_brown:
                paintView.setCurrentColor(getResources().getColor(R.color.brown));
                break;
            case R.id.button_white:
                paintView.setCurrentColor(Color.WHITE);

        }
    }

    public void onClear(View view) {
        paintView.clear();
    } //clear surface

    public void onYield(View view) {
        if(mode == GameMode.DRAWING) {
            paintView.clear();
            mode = GameMode.GUESSING;
            startGuessingTurn();
        } else {
            EditText odp = (EditText)findViewById(R.id.edit_answer);
            if(guessHelper.guessHaslo(odp.getText().toString())){
                mode = GameMode.DRAWING;
                paintView.clear();
                startDrawingTurn();
            }
        }
    } //clear surface "odpowiedz", "rezygnuj"

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_screen_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // 0 - paintview, 1 - drawingmenu, 2 - scores
        // Show and hide game menus
        switch (item.getItemId()) {
            case R.id.paint_settings:
                if(mode == GameMode.DRAWING) {  //tylko dla rysowania
                    if (menuVisibility == 0) {
                        findViewById(R.id.drawing_settings).setVisibility(View.VISIBLE);
                        menuVisibility = 1;
                    } else if (menuVisibility == 1) {
                        findViewById(R.id.drawing_settings).setVisibility(View.GONE);
                        menuVisibility = 0;
                    } else {
                        findViewById(R.id.drawing_settings).setVisibility(View.VISIBLE);
                        findViewById(R.id.history_view).setVisibility(View.GONE);
                        menuVisibility = 1;
                        //hide scores
                    }
                }
                return true;
            case R.id.score_results:
                if (menuVisibility == 0) {
                    findViewById(R.id.history_view).setVisibility(View.VISIBLE);
                    menuVisibility = 2;
                } else if (menuVisibility == 1) {
                    findViewById(R.id.history_view).setVisibility(View.VISIBLE);
                    findViewById(R.id.drawing_settings).setVisibility(View.GONE);
                    menuVisibility = 2;
                } else {
                    findViewById(R.id.history_view).setVisibility(View.GONE);
                    menuVisibility = 2;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public byte getMenuVisibility() {
        return menuVisibility;
    }

    public void setMenuVisibility(byte menuVisibility) {
        this.menuVisibility = menuVisibility;
    }

}