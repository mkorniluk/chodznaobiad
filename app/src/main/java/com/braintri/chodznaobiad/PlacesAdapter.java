package com.braintri.chodznaobiad;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private List<Place> places;
    private OnItemClickedListener onItemClickedListener;

    public PlacesAdapter(List<Place> places, OnItemClickedListener onItemClickedListener) {
        this.places = places;
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_place, parent, false));
    }

    @Override
    public void onBindViewHolder(PlacesAdapter.ViewHolder holder, final int position) {
        Place place = places.get(position);
        holder.votes.setText("" + place.votes.size());
        holder.name.setText(place.name);
        holder.itemView.setOnClickListener(view -> {
            onItemClickedListener.onItemClicked(position);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView votes, name;

        public ViewHolder(View itemView) {
            super(itemView);
            votes = itemView.findViewById(R.id.votes);
            name = itemView.findViewById(R.id.name);
        }
    }
}
