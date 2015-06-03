package com.kalambury.kalamburyp2p.Activities;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import com.kalambury.kalamburyp2p.Components.PaintView;
import com.kalambury.kalamburyp2p.R;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2014-07-14.
 */
public class GameScreen extends Activity {

    private byte menuVisibility = 0; // 0 - paintview, 1 - drawingmenu, 2 - scores
    private PaintView paintView;    //drawing surface
    private SeekBar sizeBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        paintView.clear();
    } //clear surface

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