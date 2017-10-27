package tiru.initiative.adapters

import android.database.Cursor

import android.view.ViewGroup
import android.view.LayoutInflater
import tiru.initiative.R

class EncounterAdapter(_cursor: Cursor?) : CursorAdapter<EncounterViewHolder>(_cursor) {
    override fun onBindViewHolder(holder: EncounterViewHolder, cursor: Cursor) {
        cursor.moveToPosition(cursor.position)

        holder.setData(cursor)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EncounterViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_encounter, parent, false)

        return EncounterViewHolder(v)
    }
}

