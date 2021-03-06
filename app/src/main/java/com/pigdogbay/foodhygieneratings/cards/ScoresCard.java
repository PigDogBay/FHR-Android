package com.pigdogbay.foodhygieneratings.cards;

import androidx.recyclerview.widget.RecyclerView;
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
    private final OnButtonClickListener listener;

    public ScoresCard(Establishment establishment, OnButtonClickListener listener) {
        this.establishment = establishment;
        this.listener = listener;
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

        ViewHolder(View itemView) {
            super(itemView);

            Scores scores = establishment.getRating().getScores();
            StringBuilder builder = new StringBuilder();
            builder.append("Hygiene: ");
            builder.append(scores.getHygieneDescription());
            builder.append("\n");

            builder.append("Structural: ");
            builder.append(scores.getStructuralDescription());
            builder.append("\n");

            builder.append("Management: ");
            builder.append(scores.getManagementDescription());
            builder.append("\n");

            TextView text = itemView.findViewById(R.id.card_text);
            text.setText(builder.toString());

            itemView.findViewById(R.id.card_scores_info_button).setOnClickListener(view -> listener.onButtonPressed(R.id.card_scores_info_button, null));

        }
    }

}
