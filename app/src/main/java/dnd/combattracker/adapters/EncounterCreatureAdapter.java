package dnd.combattracker.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnd.combattracker.R;
import dnd.combattracker.adapters.viewholders.CreatureSearchViewHolder;
import dnd.combattracker.adapters.viewholders.EncounterCreatureViewHolder;
import dnd.combattracker.listeners.OnItemClickListener;
import dnd.combattracker.listeners.OnLongItemClickListener;

public class EncounterCreatureAdapter extends CursorAdapter<EncounterCreatureViewHolder> implements OnItemClickListener, OnLongItemClickListener {

    private OnItemClickListener listener = null;
    private OnLongItemClickListener longListener = null;

    public EncounterCreatureAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public EncounterCreatureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_creature_encounter_details, parent, false);

        return new EncounterCreatureViewHolder(v);
    }

    public void onBindViewHolder(EncounterCreatureViewHolder holder, Cursor cursor) {
        cursor.moveToPosition(cursor.getPosition());

        holder.setData(cursor);
        holder.setOnItemClickListener(this);
        holder.setOnLongItemClickListener(this);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (listener != null) {
            listener.onItemClick(itemView, position);
        }
    }

    @Override
    public void onLongItemClick(View itemView, int position) {
        if (longListener != null) {
            longListener.onLongItemClick(itemView, position);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener longListener) {
        this.longListener = longListener;
    }


}