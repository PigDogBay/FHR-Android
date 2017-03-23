package com.pigdogbay.foodhygieneratings.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.R;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.Rating;

import java.text.DateFormat;

/**
 * Created by Mark on 23/03/2017.
 */

public class BusinessCard implements ICard {
    private final Establishment establishment;

    public BusinessCard(Establishment establishment) {
        this.establishment = establishment;
    }

    @Override
    public int getViewType() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_business,parent,false);
        return new BusinessCard.ViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private final View view;
        private final TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            text = (TextView) view.findViewById(R.id.card_text);


            StringBuilder builder = new StringBuilder();
            builder.append(establishment.getBusiness().getName());
            builder.append("\n");
            builder.append(establishment.getBusiness().getType());

            text.setText(builder.toString());


        }
    }

}
