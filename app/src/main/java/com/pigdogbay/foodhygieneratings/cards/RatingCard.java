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
 *
 */
public class RatingCard implements ICard {

    private final Establishment establishment;

    public RatingCard(Establishment establishment) {
        this.establishment = establishment;
    }

    @Override
    public int getViewType() {
        return 2;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_rating,parent,false);
        return new ViewHolder(view);
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

            Rating rating = RatingCard.this.establishment.getRating();

            StringBuilder builder = new StringBuilder();
            builder.append(rating.getRatingDescription());
            builder.append("\n");
            if (rating.hasRating()) {
                builder.append("Date Awarded: ");
                String dateString = DateFormat.getDateInstance().format(rating.getAwardedDate());
                builder.append(dateString);
                builder.append("\n");
                if (rating.isNewRatingPending()) {
                    builder.append("New Rating Pending");
                }
            }

            text.setText(builder.toString());


        }
    }

}
