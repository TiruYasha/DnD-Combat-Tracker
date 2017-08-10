package dnd.combattracker.activities.manageencounter;


import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import dnd.combattracker.R;
import dnd.combattracker.adapters.ManageEncounterPagerAdapter;
import dnd.combattracker.controllers.EncounterController;
import dnd.combattracker.repository.CombatTrackerContract;

public class ManageEncounterActivity extends AppCompatActivity implements CreatureSearchFragment.CreatureSearchFragmentListener{

    private ManageEncounterPagerAdapter manageEncounterPagerAdapter;
    private EncounterController encounterController;
    private long encounterId = -1;
    private long encounterDraftId = -1;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_encounter);

        encounterController = new EncounterController(getContentResolver());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if (b != null && b.getLong("encounterId") != -1) {
            encounterId = b.getLong("encounterId");
        }

        createDraft();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void createDraft() {
        if (encounterId == -1) {

            long draftId = encounterController.getDraftIdWithoutEncounterId();

            if (draftId > -1) {
                ContentValues values = new ContentValues();
                values.put(CombatTrackerContract.EncounterEntry.NAME, "");

                encounterDraftId = encounterController.insertEncounterDraft(values);
                setupPager();
                //showDraftDialog(new DialogNonExistingEncounterClickListener());
                return;
            }
            return;
        }

        final long draftId = encounterController.getDraftIdByEncounterId(encounterId);

        if (draftId != -1) {
            encounterDraftId = encounterController.insertDraftFromEncounter(encounterId);
            setupPager();
            //showDraftDialog(new DialogExistingEncounterClickListener());
        } else {
            encounterDraftId = encounterController.insertDraftFromEncounter(encounterId);
            setupPager();
        }
    }

    private void setupPager() {
        manageEncounterPagerAdapter = new ManageEncounterPagerAdapter(getSupportFragmentManager(), encounterDraftId);

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(manageEncounterPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manage_encounter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add_encounter:
                saveEncounter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveEncounter() {
        if (encounterId != -1) {
            encounterController.updateEncounterFromDraft(encounterId, encounterDraftId);//updateEncounterToDraft();
        } else {
            encounterController.insertEncounterFromDraft(encounterDraftId);
        }

        finish();
    }

    @Override
    public void onAddEncounter(long creatureId, String creatureName) {
        encounterController.addCreatureToEncounterDraft(encounterDraftId, creatureId);

        Snackbar.make(findViewById(R.id.manage_layout), "Added " + creatureName + " successfully", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
