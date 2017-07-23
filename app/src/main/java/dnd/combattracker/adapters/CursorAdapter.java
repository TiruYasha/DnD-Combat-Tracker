package dnd.combattracker.adapters;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

public abstract class CursorAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Cursor cursor;

    private int rowIdColumn;
    private boolean dataValid;

    public CursorAdapter(Cursor cursor) {
        this.cursor = cursor;
        dataValid = cursor != null;
        rowIdColumn = dataValid ? this.cursor.getColumnIndex("_id") : -1;
    }

    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public int getItemCount() {
        if (cursor != null) {
            return cursor.getCount();
        }

        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            return cursor.getLong(rowIdColumn);
        }

        return 0;
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    public abstract void onBindViewHolder(VH viewHolder, Cursor cursor);

    @Override
    public void onBindViewHolder(VH viewHolder, int position){
        if (!dataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!cursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        onBindViewHolder(viewHolder, cursor);
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == cursor) {
            return null;
        }

        final Cursor oldCursor = cursor;
        cursor = newCursor;

        if (cursor != null) {
            rowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            dataValid = true;
            notifyDataSetChanged();
        } else {
            rowIdColumn = -1;
            dataValid = false;
            notifyDataSetChanged();
        }
        return oldCursor;
    }
}
