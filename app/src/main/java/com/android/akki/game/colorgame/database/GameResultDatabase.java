package com.android.akki.game.colorgame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.akki.game.colorgame.MemoryGame;
import com.android.akki.game.colorgame.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by e01106 on 5/17/2017.
 */
public class GameResultDatabase extends SQLiteOpenHelper {


    private static String DATABASE_NAME="scoreData";
    private static final int DATABASE_VERSION = 1;
    private static final String HIGH_SCORES_TABLE = "HighScoresList";

    // Table columns names.
    private static final String RANK = "_id";
    private static final String NAME = "name";
    private static final String TOTAL_SCORE = "total_score";

    private static final String CREATE_SCORE_TABLE = "CREATE TABLE " + HIGH_SCORES_TABLE + "("
            + RANK + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + NAME + " TEXT,"
            + TOTAL_SCORE + " TEXT" + ")";

    private SQLiteOpenHelper mSQL;
    private SQLiteDatabase db;


    public GameResultDatabase(Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_SCORE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + HIGH_SCORES_TABLE);

        // Create tables again
        onCreate(db);
    }

    public void addDetails( String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(TOTAL_SCORE, score);
        //db.insert(HIGH_SCORES_TABLE, null, values);
        db.insertWithOnConflict(HIGH_SCORES_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    public ArrayList<Player> getHighScores() {
        ArrayList<Player> playerDataList;
        playerDataList = new ArrayList<Player>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + HIGH_SCORES_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Player player = new Player();
                player.setName(cursor.getString(1));
                player.setScore(cursor.getString(2));
                playerDataList.add(player);
            } while (cursor.moveToNext());
        }

        //Close cursor
        if(cursor != null) {
            cursor.close();
        }

        db.close();

        return playerDataList;
    }
}
