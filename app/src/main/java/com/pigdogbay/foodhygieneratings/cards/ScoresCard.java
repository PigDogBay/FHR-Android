package com.pigdogbay.foodhygieneratings.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.R;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.Scores;

/**
 * Created by Mark on 23/03/2017.
 *
 */
public class ScoresCard implements ICard {
    private final Establishment establishment;

    public ScoresCard(Establishment establishment) {
        this.establishment = establishment;
    }

    @Override
    public int getViewType() {
        return 3;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_scores,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {

    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.card_text);

            Scores scores = establishment.getRating().getScores();
            StringBuilder builder = new StringBuilder();
            builder.append("Hygiene: ");
            builder.append(scores.getHygiene());
            builder.append(" - ");
            builder.append(scores.getHygieneDescription());
            builder.append("\n");

            builder.append("Structural: ");
            builder.append(scores.getStructural());
            builder.append(" - ");
            builder.append(scores.getStructuralDescription());
            builder.append("\n");

            builder.append("Management: ");
            builder.append(scores.getManagement());
            builder.append(" - ");
            builder.append(scores.getManagementDescription());
            builder.append("\n");

            text.setText(builder.toString());


        }
    }

}
