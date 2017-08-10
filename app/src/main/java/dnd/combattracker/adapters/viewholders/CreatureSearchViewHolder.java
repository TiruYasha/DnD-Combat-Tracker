package dnd.combattracker.adapters.viewholders;


import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import dnd.combattracker.R;
import dnd.combattracker.repository.CombatTrackerContract;

public class CreatureSearchViewHolder extends CustomViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private Button addCreatureButton;
    private TextView creatureNameTextView;

    public CreatureSearchViewHolder(View view) {
        super(view);

        creatureNameTextView = (TextView) view.findViewById(R.id.list_item_creature_name);
        addCreatureButton = (Button) view.findViewById(R.id.button_add_creature);

        view.setOnClickListener(this);
        addCreatureButton.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    public void setData(Cursor c) {
        creatureNameTextView.setText(c.getString(c.getColumnIndex(CombatTrackerContract.CreatureEntry.NAME)));
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
