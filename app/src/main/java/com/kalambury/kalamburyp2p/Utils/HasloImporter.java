package com.kalambury.kalamburyp2p.Utils;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Maciej Wolañski
 * maciekwski@gmail.com
 * on 2015-06-04.
 */
public class HasloImporter {
    private HasloImporter(){}

    public static ArrayList<String> getHasla(InputStream inputStream){
        ArrayList<String> result = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while((line = br.readLine()) != null){
                result.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}



