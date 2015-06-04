package com.kalambury.kalamburyp2p.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Maciej Wolanski
 * maciekwski@gmail.com
 * on 2015-05-11.
 */
public class Database {
    private static final String DEBUG_TAG = "SqLiteManager";

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database.db";
    private static final String DB_HASLA_TABLE = "hasla";

    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;
    public static final String KEY_HASLO = "haslo";
    public static final String HASLO_OPTIONS = "TEXT NOT NULL";
    public static final int HASLO_COLUMN = 1;

    private static final String DB_CREATE_HASLA_TABLE =
            "CREATE TABLE " + DB_HASLA_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_HASLO + " " + HASLO_OPTIONS +
                    ");";

    private static final String DROP_HASLA_TABLE =
            "DROP TABLE IF EXISTS " + DB_HASLA_TABLE;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    /*public boolean updateHaslo(String[] res) {
        String name = res[0];
        String country = res[1];
        String image = res[2];
        double latitude = Double.parseDouble(res[3]);
        double longitude = Double.parseDouble(res[4]);
        String description = res[5];
        return updateHaslo(name, country, image, latitude, longitude, description);
    }
*/

    private static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_HASLA_TABLE);

            Log.d(DEBUG_TAG, "Database creating...");
            Log.d(DEBUG_TAG, "Table " + DB_HASLA_TABLE + " ver." + DB_VERSION + " created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_HASLA_TABLE);

            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table " + DB_HASLA_TABLE + " updated from ver." + oldVersion + " to ver." + newVersion);
            Log.d(DEBUG_TAG, "All data is lost.");

            onCreate(db);
        }
    }

    public Database(Context context) {
        this.context = context;
    }

    public Database open() {
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void populateDatabase(){
        if(!getAllHasloCursor().moveToFirst()){ //je¿eli pusty
            ArrayList<String> hasla = HasloImporter.getHasla(context.getResources().openRawResource(
                    context.getResources().getIdentifier("hasla",
                            "raw", context.getPackageName())));
            for(String h: hasla){
                insertHaslo(h);
            }
        }

    }
    private long insertHaslo(String haslo) {
        ContentValues newHasloValues = new ContentValues();
        newHasloValues.put(KEY_HASLO, haslo);
        return db.insert(DB_HASLA_TABLE, null, newHasloValues);
    }

    private boolean updateHaslo(String haslo) {
        String where = KEY_HASLO + "= '" + haslo + "'";
        ContentValues updateHasloValues = new ContentValues();
        updateHasloValues.put(KEY_HASLO, haslo);
        return db.update(DB_HASLA_TABLE, updateHasloValues, where, null) > 0;
    }

    public boolean deleteHaslo(String haslo) {
        String where = KEY_HASLO + "= '" + haslo + "'";
        return db.delete(DB_HASLA_TABLE, where, null) > 0;
    }

    public Cursor getAllHasloCursor() {
        String[] columns = {KEY_ID, KEY_HASLO};
        return db.query(DB_HASLA_TABLE, columns, null, null, null, null, null);
    }

    public ArrayList<Pair<Integer,String>> getHaslaFromCursor(Cursor cursor){
        ArrayList<Pair<Integer,String>> hasla = new ArrayList<Pair<Integer,String>>();
        int i = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(ID_COLUMN);
                String haslo = cursor.getString(HASLO_COLUMN);
                hasla.add(new Pair<Integer,String>(id, haslo));
            } while (cursor.moveToNext());
        }
        return hasla;
    }

    public String getHaslo(int id) {    //do losowania
        String[] columns = {KEY_HASLO};
        String where = KEY_ID + "= '" + id + "'";
        Cursor cursor = db.query(DB_HASLA_TABLE, columns, where, null, null, null, null);
        String haslo = "";
        if (cursor != null && cursor.moveToFirst()) {
            haslo = cursor.getString(HASLO_COLUMN);
        }
        return haslo;
    }

    /*public Cursor getSortedFilteredCitiesCursor(int sortColumn, byte sortOrder, int filterColumn, String filterByContaining) {
        String[] columns = {KEY_HASLO, KEY_COUNTRY, KEY_IMAGE, KEY_LATITUDE, KEY_LONGITUDE, KEY_DESCRIPTION};
        StringBuilder where = null;
        StringBuilder orderBy = null;
        if(filterColumn >=0 && filterByContaining != null) {
            where = new StringBuilder();
            switch(filterColumn){
                case 0:
                    where.append(KEY_HASLO);
                    break;
                case 1:
                    where.append(KEY_COUNTRY);
                    break;
                case 2:
                    where.append(KEY_IMAGE);
                    break;
                case 3:
                    where.append(KEY_LATITUDE);
                    break;
                case 4:
                    where.append(KEY_LONGITUDE);
                    break;
                case 5:
                    where.append(KEY_DESCRIPTION);
                    break;

            }
            where.append(" LIKE '%");
            where.append(filterByContaining);
            where.append("%'");
        }

        if(sortColumn >=0 && sortOrder >= 0){
            orderBy = new StringBuilder();
            orderBy.append(sortColumn+1);
            if(sortOrder == 0)
                orderBy.append(" ASC");
            else
                orderBy.append(" DESC");
        }
        String wheres = where == null? null : where.toString();
        String orderBys = orderBy == null? null : orderBy.toString();
        return db.query(DB_HASLA_TABLE, columns, wheres, null, null, null, orderBys);
    }

*/
}
