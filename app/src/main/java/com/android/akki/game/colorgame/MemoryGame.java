package com.android.akki.game.colorgame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.akki.game.colorgame.database.GameResultDatabase;
import com.android.akki.game.colorgame.model.Player;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryGame extends AppCompatActivity {
    private static int ROW_COUNT = -1;
    private static int COL_COUNT = -1;
    private Context context;
    private Drawable backImage;
    private int [] [] cards;
    private List<Drawable> images;
    private Card firstCard;
    private Card secondCard;
    private ButtonListener buttonListener;

    private static Object lock = new Object();

    int score;
    TextView scoreTextView;
    private TableLayout mainTable;
    private UpdateCardsHandler handler;

    int totalCardPassed = 0;
    int totalPointGained = 0;

    private static final String TAG = "MemoryGame";
    private String playerName = "";

    GameResultDatabase gameResultDb;
    ArrayList<Player> playerDataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler = new UpdateCardsHandler();
        loadImages();
        setContentView(R.layout.activity_memory_game);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.logo);


        backImage =  getResources().getDrawable(R.drawable.card_bg);

        //High Score button click event
        ((Button)findViewById(R.id.btn_high_score)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHighScores();
            }
        });

        buttonListener = new ButtonListener();
        mainTable = (TableLayout)findViewById(R.id.TableLayout01);
        context  = mainTable.getContext();

        setGame(4, 4);

        //
        Spinner s = (Spinner) findViewById(R.id.Spinner01);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(
                    android.widget.AdapterView<?> arg0,
                    View arg1, int pos, long arg3){

                ((Spinner) findViewById(R.id.Spinner01)).setSelection(0);

                int x,y;

                switch (pos) {
                    case 1:
                        x=4;y=4;
                        break;
                    case 2:
                        x=4;y=5;
                        break;
                    case 3:
                        x=4;y=6;
                        break;
                    case 4:
                        x=5;y=6;
                        break;
                    case 5:
                        x=6;y=6;
                        break;
                    default:
                        return;
                }
                setGame(x,y);
            }


            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });
        //
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRestart() {
        super.onRestart();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    private void setGame(int c, int r) {
        ROW_COUNT = r;
        COL_COUNT = c;

        cards = new int [COL_COUNT] [ROW_COUNT];

        TableRow tr = ((TableRow)findViewById(R.id.TableRow02));
        tr.removeAllViews();

        mainTable = new TableLayout(context);
        tr.addView(mainTable);

        for (int y = 0; y < ROW_COUNT; y++) {
            mainTable.addView(createRow(y));
        }

        firstCard=null;
        loadCards();

        score=0;
        scoreTextView = (TextView)findViewById(R.id.curr_score);
        scoreTextView.setText("Score: "+score);
    }

    private void loadImages() {
        images = new ArrayList<>();

        images.add(getResources().getDrawable(R.drawable.card1));
        images.add(getResources().getDrawable(R.drawable.card2));
        images.add(getResources().getDrawable(R.drawable.card3));
        images.add(getResources().getDrawable(R.drawable.card4));
        images.add(getResources().getDrawable(R.drawable.card5));
        images.add(getResources().getDrawable(R.drawable.card6));
        images.add(getResources().getDrawable(R.drawable.card7));
        images.add(getResources().getDrawable(R.drawable.card8));

        images.add(getResources().getDrawable(R.drawable.card9));
        images.add(getResources().getDrawable(R.drawable.card10));
        images.add(getResources().getDrawable(R.drawable.card11));
        images.add(getResources().getDrawable(R.drawable.card12));
        images.add(getResources().getDrawable(R.drawable.card13));
        images.add(getResources().getDrawable(R.drawable.card14));
        images.add(getResources().getDrawable(R.drawable.card15));
        images.add(getResources().getDrawable(R.drawable.card16));
        images.add(getResources().getDrawable(R.drawable.card17));
        images.add(getResources().getDrawable(R.drawable.card18));
        images.add(getResources().getDrawable(R.drawable.card19));
        images.add(getResources().getDrawable(R.drawable.card20));
        images.add(getResources().getDrawable(R.drawable.card21));

    }

    private void loadCards(){
        try{
            int size = ROW_COUNT*COL_COUNT;

            Log.i("loadCards()","size=" + size);

            ArrayList<Integer> list = new ArrayList<Integer>();

            for(int i=0;i<size;i++){
                list.add(new Integer(i));
            }


            Random r = new Random();

            for(int i=size-1;i>=0;i--){
                int t=0;

                if(i>0){
                    t = r.nextInt(i);
                }

                t=list.remove(t).intValue();
                cards[i%COL_COUNT][i/COL_COUNT]=t%(size/2);

                Log.i("loadCards()", "card["+(i%COL_COUNT)+
                        "]["+(i/COL_COUNT)+"]=" + cards[i%COL_COUNT][i/COL_COUNT]);
            }
        }
        catch (Exception e) {
            Log.e("loadCards()", e+"");
        }

    }

    private TableRow createRow(int y){
        TableRow row = new TableRow(context);
        row.setHorizontalGravity(Gravity.CENTER);

        for (int x = 0; x < COL_COUNT; x++) {
            row.addView(createImageButton(x,y));
        }
        return row;
    }

    private View createImageButton(int x, int y){
        Button button = new Button(context);
        button.setBackgroundDrawable(backImage);
        button.setId(100*x+y);
        button.setOnClickListener(buttonListener);
        return button;
    }

    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            synchronized (lock) {
                if(firstCard!=null && secondCard != null){
                    return;
                }
                int id = v.getId();
                int x = id/100;
                int y = id%100;
                turnCard((Button)v,x,y);
            }

        }

        private void turnCard(Button button,int x, int y) {
            button.setBackgroundDrawable(images.get(cards[x][y]));

            if(firstCard==null){
                firstCard = new Card(button,x,y);
            }
            else{

                if(firstCard.x == x && firstCard.y == y){
                    return; //the user pressed the same card
                }

                secondCard = new Card(button,x,y);

                TimerTask tt = new TimerTask() {

                    @Override
                    public void run() {
                        try{
                            synchronized (lock) {
                                handler.sendEmptyMessage(0);
                            }
                        }
                        catch (Exception e) {
                            Log.e("E1", e.getMessage());
                        }
                    }
                };

                Timer t = new Timer(false);
                t.schedule(tt, 1000);
            }
        }
    }

    class UpdateCardsHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                checkCards();
            }
        }
        public void checkCards(){
            if(cards[secondCard.x][secondCard.y] == cards[firstCard.x][firstCard.y]){
                Log.d(TAG, " card match");
                firstCard.button.setVisibility(View.INVISIBLE);
                secondCard.button.setVisibility(View.INVISIBLE);

                totalPointGained = totalPointGained + 2;
                totalCardPassed = totalCardPassed + 2;

                Log.d(TAG, "TotalPointGained: " +totalPointGained + "::totalCardPassed: " +totalCardPassed);

                scoreTextView.setText("Score: "+totalPointGained);

                if(totalCardPassed == 16) {
                    processResult();
                }

            }
            else {
                Log.d(TAG, "wrong match");
                secondCard.button.setBackgroundDrawable(backImage);
                firstCard.button.setBackgroundDrawable(backImage);

                totalPointGained--;
                scoreTextView.setText("Score: "+totalPointGained);
            }

            firstCard=null;
            secondCard=null;
        }
    }


    private void processResult() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Your Name");
        builder.setCancelable(false);


        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerName = input.getText().toString();

                if(!playerName.isEmpty()) {
                    try {
                        gameResultDb = new GameResultDatabase(context);
                        gameResultDb.addDetails(playerName, totalPointGained);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally {
                        gameResultDb.close();
                    }
                } else {
                    Toast.makeText(MemoryGame.this, "Please Enter Player Name!",
                            Toast.LENGTH_SHORT).show();
                    input.requestFocus();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


    public void getHighScores() {
        playerDataList = new ArrayList<Player>();

        try {
            gameResultDb = new GameResultDatabase(context);
            playerDataList = gameResultDb.getHighScores();
            Log.d(TAG, "GetHighScores: " +playerDataList.toString());

            //TODO:
            if(playerDataList.isEmpty()) {
                Toast.makeText(MemoryGame.this, "No Record Found!...Your Are the 1st Player!",
                        Toast.LENGTH_SHORT).show();
            } else{
                //Populate table result
                Intent resultIntent = new Intent(this, HighScoresActivity.class);
                resultIntent.putExtra("result", playerDataList);
                startActivity(resultIntent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}