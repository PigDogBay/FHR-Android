package com.pigdogbay.foodhygieneratings.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.R;
import com.pigdogbay.foodhygieneratings.model.Address;
import com.pigdogbay.foodhygieneratings.model.Establishment;

/**
 * Created by Mark on 23/03/2017.
 *
 */
public class AddressCard implements ICard {

    private final Establishment establishment;
    private final OnButtonClickListener listener;

    public AddressCard(Establishment establishment, OnButtonClickListener listener) {
        this.establishment = establishment;
        this.listener = listener;
    }


    @Override
    public int getViewType() {
        return 4;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_address,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
    }
    private class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View itemView) {
            super(itemView);
            TextView addressText = (TextView) itemView.findViewById(R.id.card_address_text);

            String name = AddressCard.this.establishment.getBusiness().getName();
            Address address =  AddressCard.this.establishment.getAddress();
            String flattened = address.flatten();
            if (!flattened.isEmpty()) {
                String multiLine = flattened.replace(", ","\n");
                addressText.setText(name+"\n"+multiLine);
            }

            itemView.findViewById(R.id.card_address_map_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onButtonPressed(R.id.card_address_map_button, null);
                }
            });

        }

    }

}
