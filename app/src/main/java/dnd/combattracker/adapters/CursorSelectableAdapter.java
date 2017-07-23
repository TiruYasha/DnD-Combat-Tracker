package dnd.combattracker.adapters;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class CursorSelectableAdapter<VH extends RecyclerView.ViewHolder> extends CursorAdapter<VH> {

    private List<Integer> selectedItems;

    public CursorSelectableAdapter(Cursor cursor) {
        super(cursor);
        selectedItems = new ArrayList<>();
    }

    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    public void toggleSelection(int position) {
        if (selectedItems.contains(position)) {
            selectedItems.remove(selectedItems.indexOf(position));
        } else {
            selectedItems.add(position);
        }
        notifyItemChanged(position);
    }

    public void clearSelection() {
        List<Integer> selection = new ArrayList<>(getSelectedItems());
        selectedItems.clear();
        for (Integer position : selection) {
            notifyItemChanged(position);
        }
    }

    public void selectAll() {
        getCursor().moveToFirst();

        selectedItems.clear();

        int position = 0;
        while (getCursor().moveToNext()) {
            selectedItems.add(position);
            position++;
        }
        selectedItems.add(position);

        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems() {
        return selectedItems;
    }
}
