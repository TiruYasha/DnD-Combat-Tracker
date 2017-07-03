package dnd.combattracker.repository;


import android.provider.BaseColumns;

public final class EncounterContract {

    private EncounterContract() {
    }

    public static class EncounterEntry implements BaseColumns {
        public static final String TABLE_NAME = "Encounter";
        public static final String COLUMN_NAME = "name";

        public static String[] Columns = new String[]{_ID, COLUMN_NAME};
    }


}
