package dnd.combattracker.adapters.viewholders;


import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import dnd.combattracker.R;
import dnd.combattracker.repository.CombatTrackerContract;

public class EncounterViewHolder extends CustomViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private TextView encounterNameTextView;

    public EncounterViewHolder(View view) {
        super(view);

        encounterNameTextView = (TextView) view.findViewById(R.id.list_item_encounter_name);

        //view.setLongClickable(true);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    public void setData(Cursor c) {
        encounterNameTextView.setText(c.getString(c.getColumnIndex(CombatTrackerContract.EncounterEntry.NAME)));
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (longListener != null) {
            longListener.onLongItemClick(view, getAdapterPosition());
        }

        return true;
    }
}
