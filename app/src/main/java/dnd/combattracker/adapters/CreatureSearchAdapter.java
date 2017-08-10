package dnd.combattracker.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dnd.combattracker.R;
import dnd.combattracker.adapters.viewholders.CreatureSearchViewHolder;
import dnd.combattracker.adapters.viewholders.EncounterViewHolder;
import dnd.combattracker.listeners.OnItemClickListener;
import dnd.combattracker.listeners.OnLongItemClickListener;

public class CreatureSearchAdapter extends CursorAdapter<CreatureSearchViewHolder> implements OnItemClickListener, OnLongItemClickListener {

    private OnItemClickListener listener = null;
    private OnLongItemClickListener longListener = null;

    public CreatureSearchAdapter(Cursor cursor) {
        super(cursor);
    }

    @Override
    public CreatureSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_creature_search, parent, false);

        return new CreatureSearchViewHolder(v);
    }

    public void onBindViewHolder(CreatureSearchViewHolder holder, Cursor cursor) {
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