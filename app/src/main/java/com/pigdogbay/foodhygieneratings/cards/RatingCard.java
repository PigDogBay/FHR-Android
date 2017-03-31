package com.pigdogbay.foodhygieneratings.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.R;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.FoodHygieneAPI;
import com.pigdogbay.foodhygieneratings.model.Rating;

import java.text.DateFormat;

/**
 * Created by Mark on 23/03/2017.
 *
 */
public class RatingCard implements ICard {

    private final Establishment establishment;
    private final OnButtonClickListener listener;

    public RatingCard(Establishment establishment, OnButtonClickListener listener) {
        this.establishment = establishment;
        this.listener = listener;
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

        ViewHolder(View view) {
            super(view);
            Rating rating = RatingCard.this.establishment.getRating();

            StringBuilder builder = new StringBuilder();
            builder.append(establishment.getBusiness().getName())
                .append("\n")
                .append(establishment.getBusiness().getType())
                .append("\n");
            if (rating.hasRating()) {
                builder.append("Date Awarded: ");
                String dateString = DateFormat.getDateInstance().format(rating.getAwardedDate());
                builder.append(dateString);
                builder.append("\n");
                if (rating.isNewRatingPending()) {
                    builder.append("New Rating Pending");
                }
            }

            TextView text = (TextView) view.findViewById(R.id.card_text);
            text.setText(builder.toString());

            ImageView imageView = (ImageView) view.findViewById(R.id.card_rating_logo);
            imageView.setImageResource(rating.getLogoId());
        }
    }

}
