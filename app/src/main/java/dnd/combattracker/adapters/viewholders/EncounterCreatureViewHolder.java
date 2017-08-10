package dnd.combattracker.adapters.viewholders;


import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import dnd.combattracker.R;
import dnd.combattracker.repository.CombatTrackerContract;

import static dnd.combattracker.repository.CombatTrackerContract.*;

public class EncounterCreatureViewHolder extends CustomViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private TextView creatureNameTextView;

    public EncounterCreatureViewHolder(View view) {
        super(view);

        creatureNameTextView = (TextView) view.findViewById(R.id.list_item_creature_name);

        //view.setLongClickable(true);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    public void setData(Cursor c) {
        creatureNameTextView.setText(c.getString(c.getColumnIndex(CreatureEntry.NAME)));
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
