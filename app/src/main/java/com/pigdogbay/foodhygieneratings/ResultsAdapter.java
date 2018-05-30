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
import com.pigdogbay.foodhygieneratings.model.SearchType;
import com.pigdogbay.lib.usercontrols.OnListItemClickedListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mark on 17/03/2017.
 *
 */
class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {


    private List<Establishment> establishments;
    private final OnListItemClickedListener<Establishment> listener;
    private SearchType searchType = SearchType.quick;

    public void setEstablishments(List<Establishment> establishments){
        this.establishments = establishments;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    ResultsAdapter(OnListItemClickedListener<Establishment> listener){

        establishments = new ArrayList<>();
        this.listener = listener;
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
            holder.view.setOnClickListener(view -> listener.onListItemClicked(holder.establishment,holder.getAdapterPosition()));
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
            this.imageView = itemView.findViewById(R.id.list_result_image);
            this.text = itemView.findViewById(R.id.list_result_text);
            this.subtitle = itemView.findViewById(R.id.list_result_subtitle);
        }


        void bindItem(Establishment establishment){
            this.establishment = establishment;
            this.text.setText(establishment.getBusiness().getName());
            this.subtitle.setText(getSubtitleText(establishment));
            Drawable drawable = ContextCompat.getDrawable(view.getContext(),establishment.getRating().getIconId());
            this.imageView.setImageDrawable(drawable);
        }

        private String getSubtitleText(Establishment establishment){
            switch (searchType){

                case local:
                    return String.format(Locale.UK,"%.1f miles, %s",establishment.getDistance(),establishment.getAddress().flatten());
                case quick:
                    break;
                case advanced:
                    break;
                case map:
                    break;
            }
            return establishment.getAddress().flatten();
        }
    }
}
