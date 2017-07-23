package dnd.combattracker.adapters;


import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnd.combattracker.R;
import dnd.combattracker.adapters.viewholders.EncounterSelectableViewHolder;
import dnd.combattracker.listeners.OnItemClickListener;

public class EncounterSelectableAdapter extends CursorSelectableAdapter<EncounterSelectableViewHolder> implements OnItemClickListener{

    private OnItemClickListener listener;

    public EncounterSelectableAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public EncounterSelectableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_encounter_selector, parent, false);

        return new EncounterSelectableViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EncounterSelectableViewHolder holder, Cursor cursor) {
        cursor.moveToPosition(cursor.getPosition());

        holder.setData(cursor);
        holder.setSelected(isSelected(cursor.getPosition()));
        holder.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(View itemView, int position) {
        if(listener != null){
            listener.onItemClick(itemView, position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
