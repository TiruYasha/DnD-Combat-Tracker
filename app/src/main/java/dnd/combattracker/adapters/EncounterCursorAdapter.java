package dnd.combattracker.adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dnd.combattracker.R;
import dnd.combattracker.adapters.viewholders.EncounterViewHolder;

public class EncounterCursorAdapter extends CursorRecyclerViewAdapter<EncounterViewHolder> implements View.OnClickListener {

    private View.OnClickListener listener;

    public EncounterCursorAdapter(Context context, Cursor cursor, View.OnClickListener listener) {
        super(context, cursor);
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(EncounterViewHolder viewHolder, Cursor cursor) {
        EncounterViewHolder holder = (EncounterViewHolder) viewHolder;
        cursor.moveToPosition(cursor.getPosition());
        holder.setData(cursor);
    }

    @Override
    public EncounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item_encounter, parent, false);
        return new EncounterViewHolder(v, this);
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
//        EncounterViewHolder holder = (EncounterViewHolder) viewHolder;
//        cursor.moveToPosition(cursor.getPosition());
//        holder.setData(cursor);
//    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }
}
