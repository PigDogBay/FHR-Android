package com.pigdogbay.foodhygieneratings.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.R;
import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.foodhygieneratings.model.LocalAuthority;


/**
 * Created by Mark on 23/03/2017.
 *
 */
public class LocalAuthorityCard implements ICard {
    private final Establishment establishment;

    public LocalAuthorityCard(Establishment establishment) {
        this.establishment = establishment;
    }

    @Override
    public int getViewType() {
        return 5;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_local_authority,parent,false);
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
            LocalAuthority la = establishment.getLocalAuthority();
            StringBuilder builder = new StringBuilder();
            builder.append(la.getName());
            builder.append("\n");
            builder.append(la.getEmail());
            builder.append("\n");
            builder.append(la.getWeb());
            text.setText(builder.toString());

        }
    }

}
