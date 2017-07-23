package dnd.combattracker.adapters.viewholders;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import dnd.combattracker.listeners.OnItemClickListener;
import dnd.combattracker.listeners.OnLongItemClickListener;

public abstract class CustomViewHolder extends RecyclerView.ViewHolder {

    protected OnItemClickListener listener;
    protected OnLongItemClickListener longListener;

    public CustomViewHolder(View itemView) {
        super(itemView);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setOnLongItemClickListener(OnLongItemClickListener longListener){
        this.longListener = longListener;
    }
}
