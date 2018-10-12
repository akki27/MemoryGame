package com.android.akki.game.colorgame;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.akki.game.colorgame.model.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
 
public class PLayerAdapter extends RecyclerView.Adapter<PLayerAdapter.MyViewHolder> {
 
    private List<Player> playerList;
 
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, rank, scrore;
 
        public MyViewHolder(View view) {
            super(view);
            scrore = (TextView) view.findViewById(R.id.player_score);
            rank = (TextView) view.findViewById(R.id.player_rank);
            name = (TextView) view.findViewById(R.id.player_name);
        }
    }
 
 
    public PLayerAdapter(List<Player> moviesList) {
        this.playerList = moviesList;

        Collections.sort(this.playerList, new Comparator<Player>() {
            @Override
            public int compare(Player o1, Player o2) {
                return  Integer.parseInt(o2.getScore()) - Integer.parseInt(o1.getScore());
            }
        });
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.high_score_table_items, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Player player = playerList.get(position);
        holder.rank.setText(position + 1 + "");
        holder.name.setText(player.getName());
        holder.scrore.setText(player.getScore());
    }
 
    @Override
    public int getItemCount() {
        return playerList.size();
    }
}