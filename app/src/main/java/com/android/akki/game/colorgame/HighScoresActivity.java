package com.android.akki.game.colorgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.akki.game.colorgame.model.Player;

import java.util.ArrayList;

/**
 * Created by SadyAkki on 5/18/2017.
 */

public class HighScoresActivity extends AppCompatActivity{

    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores_table);
        recyclerView = (RecyclerView) findViewById(R.id.list_item);
        PLayerAdapter pLayerAdapter = new PLayerAdapter((ArrayList<Player>)getIntent().getSerializableExtra("result"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(pLayerAdapter);

    }
}
