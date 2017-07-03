package dnd.combattracker.adapters.viewholders;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import dnd.combattracker.R;
import dnd.combattracker.repository.EncounterContract;

public class EncounterViewHolder extends RecyclerView.ViewHolder {

    public TextView encounterNameTextView;

    public EncounterViewHolder(View itemView) {
        super(itemView);
        encounterNameTextView = (TextView) itemView.findViewById(R.id.list_item_encounter_name);
    }

    public void setData(Cursor c) {
        encounterNameTextView.setText(c.getString(c.getColumnIndex(EncounterContract.EncounterEntry.COLUMN_NAME)));
    }
}
