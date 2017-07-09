package dnd.combattracker.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnd.combattracker.R;
import dnd.combattracker.adapters.viewholders.EncounterViewHolder;

public class EncounterAdapter extends RecyclerView.Adapter<EncounterViewHolder> implements View.OnClickListener {
    private String[] dataSet;
    private View.OnClickListener listener;

    public EncounterAdapter(String[] dataSet, View.OnClickListener listener){
        this.dataSet = dataSet;
        this.listener = listener;
    }

    @Override
    public EncounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_encounter, parent, false);

        return new EncounterViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(EncounterViewHolder holder, int position) {
        holder.setData(dataSet, position);
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }
}
