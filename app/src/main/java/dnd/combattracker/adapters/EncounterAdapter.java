package dnd.combattracker.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dnd.combattracker.R;
import dnd.combattracker.repository.EncounterContract.EncounterEntry;

public class EncounterAdapter extends CursorAdapter {
    private LayoutInflater encounterInflater;

    public EncounterAdapter(Context context, Cursor cursor, int flags){
        super(context, cursor, flags);
        encounterInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return encounterInflater.inflate(R.layout.list_item_encounter, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewName = (TextView) view.findViewById(R.id.list_item_encounter_name);
        String name = cursor.getString(cursor.getColumnIndex(EncounterEntry.COLUMN_NAME));
        textViewName.setText(name);
    }
}
