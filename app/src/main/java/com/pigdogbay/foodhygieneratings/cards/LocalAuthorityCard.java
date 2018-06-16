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
    private final OnButtonClickListener listener;

    public LocalAuthorityCard(Establishment establishment, OnButtonClickListener listener) {
        this.establishment = establishment;
        this.listener = listener;
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

        ViewHolder(View itemView) {
            super(itemView);

            LocalAuthority la = establishment.getLocalAuthority();
            String builder = la.getName() +
                    "\n" +
                    la.getEmail() +
                    "\n" +
                    la.getWeb();

            TextView text = itemView.findViewById(R.id.card_text);
            text.setText(builder);
            //Email address can be quite long and will wrap over to the next line which looks ugly
            //This prevents line wrapping, however I don't seem be able to scroll the text tho.
            text.setHorizontallyScrolling(true);

            itemView.findViewById(R.id.card_la_email_button).setOnClickListener(view -> listener.onButtonPressed(R.id.card_la_email_button, null));
            itemView.findViewById(R.id.card_la_web_button).setOnClickListener(view -> listener.onButtonPressed(R.id.card_la_web_button, null));
        }
    }
}
