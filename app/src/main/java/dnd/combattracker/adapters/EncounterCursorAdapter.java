package dnd.combattracker.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnd.combattracker.R;
import dnd.combattracker.adapters.viewholders.EncounterViewHolder;

public class EncounterCursorAdapter extends CursorRecyclerViewAdapter<EncounterViewHolder> implements View.OnClickListener {
    private View.OnClickListener listener;

    public EncounterCursorAdapter(Cursor cursor, View.OnClickListener listener) {
        super(cursor);
        this.listener = listener;
    }

    @Override
    public EncounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_encounter, parent, false);

        return new EncounterViewHolder(v, this);
    }

    public void onBindViewHolder(EncounterViewHolder holder, Cursor cursor){
        cursor.moveToPosition(cursor.getPosition());

        holder.setData(cursor);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);
    }
}
