package com.kalambury.kalamburyp2p.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.kalambury.kalamburyp2p.Communication.DataExchangeManager;
import com.kalambury.kalamburyp2p.R;

public class StartScreen extends Activity {
    /**
     * Called when the activity is first created.
     */

    private DataExchangeManager dem = DataExchangeManager.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
    }

    public void onClick(View view) {
        logIn();
        switch(view.getId()){
            case R.id.button_start:
                Intent i = new Intent(this, GameScreen.class);
                startActivity(i);
                break;
            case R.id.button_about:
                AlertDialog ad = new AlertDialog.Builder(this)
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

    private void logIn(){
        if(dem.logIn())
            register();

    }
    private void register(){
        dem.register("a", "a");
    }
    private void saveUser(){
        dem.saveUser("a","a", this);
    }

}