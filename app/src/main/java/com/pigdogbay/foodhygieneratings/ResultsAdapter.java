package com.pigdogbay.foodhygieneratings;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pigdogbay.foodhygieneratings.model.Establishment;
import com.pigdogbay.lib.usercontrols.OnListItemClickedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 17/03/2017.
 *
 */
class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {


    private final List<Establishment> establishments;
    private final OnListItemClickedListener<Establishment> listener;

    ResultsAdapter(OnListItemClickedListener<Establishment> listener){

        establishments = new ArrayList<>();
        this.listener = listener;
    }

    void clear() {
        establishments.clear();
    }
    void addItems(List<Establishment> newItems){
        establishments.addAll(newItems);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_result,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindItem(establishments.get(position));
        if (listener!=null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onListItemClicked(holder.establishment,holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return establishments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Establishment establishment;
        private final View view;
        private final ImageView imageView;
        private final TextView text, subtitle;

        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.imageView = (ImageView) itemView.findViewById(R.id.list_result_image);
            this.text = (TextView) itemView.findViewById(R.id.list_result_text);
            this.subtitle = (TextView) itemView.findViewById(R.id.list_result_subtitle);
        }


        void bindItem(Establishment establishment){
            this.establishment = establishment;
            this.text.setText(establishment.getBusiness().getName());
            this.subtitle.setText(establishment.getAddress().flatten());
            Drawable drawable = ContextCompat.getDrawable(view.getContext(),establishment.getRating().getIconId());
            this.imageView.setImageDrawable(drawable);
        }
    }
}
