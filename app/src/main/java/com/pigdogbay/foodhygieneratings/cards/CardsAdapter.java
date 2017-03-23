package com.pigdogbay.foodhygieneratings.cards;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Mark on 23/03/2017.
 *
 */
public class CardsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<ICard> cards;

    public CardsAdapter(List<ICard> cards) {
        this.cards = cards;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (ICard card : cards){
            if (viewType == card.getViewType()){
                return card.createViewHolder(parent);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        cards.get(position).bindViewHolder(holder);
    }

    @Override
    public int getItemViewType(int position) {
        return cards.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
