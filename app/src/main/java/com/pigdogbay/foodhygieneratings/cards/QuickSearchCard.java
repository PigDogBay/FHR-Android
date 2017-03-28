package com.pigdogbay.foodhygieneratings.cards;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.R;

/**
 * Created by Mark on 28/03/2017.
 *
 */

public class QuickSearchCard implements ICard{
    private final OnButtonClickListener listener;

    public QuickSearchCard(OnButtonClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getViewType() {
        return 10;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_quick_search,parent,false);
        return new QuickSearchCard.ViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder viewHolder) {
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        EditText nameEdit, placeEdit;

        ViewHolder(View view) {
            super(view);

            nameEdit = (EditText) view.findViewById(R.id.card_quick_search_name_text);
            placeEdit = (EditText) view.findViewById(R.id.card_quick_search_place_text);

            placeEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (EditorInfo.IME_ACTION_SEARCH==i){
                        search();
                        return true;
                    }
                    return false;
                }
            });

            view.findViewById(R.id.card_quick_search_name_clear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nameEdit.setText("");
                }
            });

            view.findViewById(R.id.card_quick_search_clear_place).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeEdit.setText("");
                }
            });

            view.findViewById(R.id.card_quick_search_search_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    search();
                }
            });

        }
        private void search(){
            String name = nameEdit.getText().toString();
            String place = placeEdit.getText().toString();
            listener.onButtonPressed(R.id.card_quick_search_search_button, new String[]{name,place});

        }
    }

}
