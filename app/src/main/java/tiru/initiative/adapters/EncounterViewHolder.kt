package tiru.initiative.adapters

import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item_encounter.view.*
import tiru.initiative.repository.InitiativeTrackerContract

class EncounterViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    fun setData(cursor: Cursor){
        itemView.list_item_encounter_name.text = cursor.getString(cursor.getColumnIndex(InitiativeTrackerContract.EncounterEntry.NAME))
    }
}