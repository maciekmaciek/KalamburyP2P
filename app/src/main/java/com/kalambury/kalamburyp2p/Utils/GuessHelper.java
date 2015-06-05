package com.kalambury.kalamburyp2p.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Maciej Wolański
 * maciekwski@gmail.com
 * on 2015-06-04.
 */
public class GuessHelper {
    String[] haslo;
    Context context;
    public GuessHelper(String haslo, Context context){
        this.context = context;
        this.haslo = haslo.replaceAll("[^A-Za-z0-9\\s]", "").toLowerCase().split(" ");
    }

    public boolean guessHaslo(String odp){
        if(odp.length() == 0)
            return false;

        String message ="";
        String[] odpArr = odp.replaceAll("[^A-Za-z0-9\\s]", "").toLowerCase().split(" ");
        int length = Math.min(haslo.length, odpArr.length);
        boolean[] common = new boolean[length];
        if(odpArr[0].charAt(0) == haslo[0].charAt(0)){
            message += "Zgadłeś 1. literę!\n";
        }

        for(int i = 0; i< length; i++){
            if(odpArr[i].equals(haslo[i])){
                message += haslo[i] + " - OK\n";
                common[i] = true;
            }
        }
        if(haslo.length == odpArr.length){
            boolean good = true;
            for(int i = 0; i< length && good; i++) {
                good = common[i];
            }
            if(good){
                message = "Gratulacje!";
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                return true;
            }
        } else {
            if(message.equals(""))
                message = "Niestety!";
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
