package com.pigdogbay.foodhygieneratings.cards;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by Mark on 23/03/2017.
 *
 */
public interface ICard {

    int getViewType();
    RecyclerView.ViewHolder createViewHolder(ViewGroup parent);
    void bindViewHolder(RecyclerView.ViewHolder viewHolder);
}
