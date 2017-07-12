package dnd.combattracker.adapters.viewholders;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import dnd.combattracker.R;
import dnd.combattracker.repository.CombatTrackerContract;

public class EncounterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView encounterNameTextView;
    public View.OnClickListener listener;

    public EncounterViewHolder(View itemView, View.OnClickListener listener) {
        super(itemView);
        encounterNameTextView = (TextView) itemView.findViewById(R.id.list_item_encounter_name);
        this.listener = listener;
        encounterNameTextView.setOnClickListener(this);
    }

    public void setData(Cursor c) {
        encounterNameTextView.setText(c.getString(c.getColumnIndex(CombatTrackerContract.EncounterEntry.NAME)));
    }

    public void setData(String[] data, int position){
        encounterNameTextView.setText(data[position]);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }
}
