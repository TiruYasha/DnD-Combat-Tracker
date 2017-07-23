package dnd.combattracker.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnd.combattracker.R;
import dnd.combattracker.adapters.viewholders.EncounterViewHolder;
import dnd.combattracker.listeners.OnItemClickListener;
import dnd.combattracker.listeners.OnLongItemClickListener;

public class EncounterAdapter extends CursorAdapter<EncounterViewHolder> implements OnItemClickListener, OnLongItemClickListener {

    private OnItemClickListener listener = null;
    private OnLongItemClickListener longListener = null;

    public EncounterAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public EncounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_encounter, parent, false);

        return new EncounterViewHolder(v);
    }

    public void onBindViewHolder(EncounterViewHolder holder, Cursor cursor) {
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