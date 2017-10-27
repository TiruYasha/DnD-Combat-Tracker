package tiru.initiative.adapters

import android.database.Cursor
import android.support.v7.widget.RecyclerView

/**
 * Adapter to abstract the implementation of cursors for recyclerview
 *
 * @param [VH] Type of viewholder
 * @property cursor data to loop through
 * @constructor Creates the adapter
 */
abstract class CursorAdapter<VH : RecyclerView.ViewHolder>(private var _cursor: Cursor?) : RecyclerView.Adapter<VH>() {
    var cursor: Cursor?
        get() = _cursor
        private set(value) {
            _cursor = value
        }

    private var rowIdColumn = 0
    private var dataValid = true

    init {
        dataValid = cursor != null
        rowIdColumn = cursor?.getColumnIndex("_id") ?: -1
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    override fun getItemId(position: Int): Long {
        cursor?.moveToPosition(position)

        return cursor?.getLong(rowIdColumn) ?: 0
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    abstract fun onBindViewHolder(holder: VH, cursor: Cursor)

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (!dataValid) {
            throw IllegalStateException("this should only be called when the cursor is valid")
        }

        if (cursor?.moveToPosition(position) == false) {
            throw IllegalStateException("couldn't move cursor to position " + position)
        }

        onBindViewHolder(holder, cursor!!)
    }

    /**
     * Change the cursor closing the old one
     */
    fun changeCursor(cursor: Cursor){
        val oldCursor = swapCursor(cursor)

        oldCursor?.close()
    }

    /**
     * Swap in a [newCursor], returning the old Cursor.
     * @return the old cursor (Not closed)
     */
    fun swapCursor(newCursor: Cursor): Cursor? {
        if (newCursor == cursor) {
            return null
        }

        val oldCursor = cursor
        cursor = newCursor

        if (cursor != null) {
            rowIdColumn = newCursor.getColumnIndex("_id")
            dataValid = true
            notifyDataSetChanged()
        } else {
            rowIdColumn = -1
            dataValid = false
            notifyDataSetChanged()
        }

        return oldCursor
    }
}