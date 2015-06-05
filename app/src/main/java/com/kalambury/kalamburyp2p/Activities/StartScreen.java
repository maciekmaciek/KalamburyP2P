package com.kalambury.kalamburyp2p.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import com.kalambury.kalamburyp2p.Communication.DataExchangeManager;
import com.kalambury.kalamburyp2p.Communication.GameMode;
import com.kalambury.kalamburyp2p.R;
import com.kalambury.kalamburyp2p.Utils.Database;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class StartScreen extends Activity {
    /**
     * Called when the activity is first created.
     */

    private DataExchangeManager dem = DataExchangeManager.getInstance();
    Database db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        db = new Database(getApplicationContext());
        db.open();
        db.populateDatabase();
        //ArrayList<Pair<Integer,String>> pairs = new ArrayList<Pair<Integer, String>>();
        //pairs = db.getHaslaFromCursor(db.getAllHasloCursor());
        db.close();
    }

    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_start:
                logIn();
                break;
            case R.id.button_about:
                new AlertDialog.Builder(this)
                        .setTitle("O grze")
                        .setMessage("Kalambury, 2graczy, wifi direct")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .show();
                break;
            case R.id.button_exit:
                finish();
        }
    }


    private void logIn(){   //tutaj bedzie laczenie z innym userem
        if(dem.logIn())
            register();
        GameMode gm = GameMode.DRAWING;
        //Intent i = new Intent(this, GameScreen.class);
        Intent i = new Intent(this, WiFiDirectActivity.class);
        i.putExtra("mode", gm);
        startActivity(i);
    }

    private void register(){
        dem.register("a", "a");
    }
    private void saveUser(){
        dem.saveUser("a","a", this);
    }

}