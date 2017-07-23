package dnd.combattracker.adapters.viewholders;


import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import dnd.combattracker.R;
import dnd.combattracker.listeners.OnLongItemClickListener;
import dnd.combattracker.repository.CombatTrackerContract;

public class EncounterSelectableViewHolder extends CustomViewHolder implements View.OnClickListener {

    private TextView encounterNameTextView;
    private CheckBox encounterCheckBoxView;

    public EncounterSelectableViewHolder(View view) {
        super(view);

        encounterNameTextView = (TextView) view.findViewById(R.id.list_item_encounter_name);
        encounterCheckBoxView = (CheckBox) view.findViewById(R.id.list_item_encounter_checkbox);

        view.setOnClickListener(this);
        encounterCheckBoxView.setOnClickListener(this);
    }

    public void setData(Cursor c) {
        encounterNameTextView.setText(c.getString(c.getColumnIndex(CombatTrackerContract.EncounterEntry.NAME)));
    }

    public void setSelected(boolean isSelected) {
        encounterCheckBoxView.setChecked(isSelected);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, getAdapterPosition());
        }
    }
}
