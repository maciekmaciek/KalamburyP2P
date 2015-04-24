package com.kalambury.kalamburyp2p;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        Intent i = new Intent(this, GameScreen.class);
        startActivity(i);
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