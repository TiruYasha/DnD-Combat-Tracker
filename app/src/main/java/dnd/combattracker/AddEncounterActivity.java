package dnd.combattracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import dnd.combattracker.repository.EncounterDbHelper;
import dnd.combattracker.repository.EncounterProvider;

import static dnd.combattracker.repository.EncounterContract.EncounterEntry;


public class AddEncounterActivity extends AppCompatActivity {

    private EditText encounterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_encounter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.encounter_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        encounterName = (EditText) findViewById(R.id.input_encounter_name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_encounter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add_encounter:
                addEncounter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void addEncounter() {
        ContentValues values = new ContentValues();
        values.put(EncounterEntry.COLUMN_NAME, encounterName.getText().toString());

//        db.insert(EncounterEntry.TABLE_NAME, null, values);
        Uri encounterUri = getContentResolver().insert(EncounterProvider.urlForItems(30), values);
        //onBackPressed();
        finish();
    }
}
