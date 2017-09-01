package dnd.combattracker.activities.manageencounter;


import android.content.ContentValues;
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
import dnd.combattracker.controllers.EncounterDraftController;
import dnd.combattracker.repository.CombatTrackerContract;

public class ManageEncounterActivity extends AppCompatActivity implements CreatureSearchFragment.CreatureSearchFragmentListener {

    private ManageEncounterPagerAdapter manageEncounterPagerAdapter;
    private EncounterController encounterController;
    private EncounterDraftController encounterDraftController;
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
        encounterDraftController = new EncounterDraftController(getContentResolver());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        if (b != null && b.getLong("encounterId") != -1) {
            encounterId = b.getLong("encounterId");
        }

        //createDraft();
        setupDraft();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupDraft() {
        if (encounterExists()) {
            setupExistingEncounterDraft();
        } else {
            setupNewEncounterDraft();
        }
    }

    private void setupExistingEncounterDraft() {
        int draftCount = encounterDraftController.getDraftCountById(encounterId);

        switch (draftCount) {
            case 0:
                encounterDraftId = encounterDraftController.insertDraftFromEncounter(encounterId);
                break;
            case 1:
                encounterDraftId = encounterDraftController.insertDraftFromEncounter(encounterId);
                showRestoreDraftSnackbar();
                break;
            default:
                encounterDraftId = encounterDraftController.insertDraftFromEncounter(encounterId);
                encounterDraftController.deleteOldestDraftForEncounterId(encounterId);
                showRestoreDraftSnackbar();
                break;
        }

        setupPager();
    }

    private void setupNewEncounterDraft() {
        int draftCount = encounterDraftController.getDraftCount();

        switch (draftCount) {
            case 0:
                encounterDraftId = encounterDraftController.insertEmptyEncounterDraft();
                break;
            case 1:
                encounterDraftId = encounterDraftController.insertEmptyEncounterDraft();
                showRestoreDraftSnackbar();
                break;
            default:
                encounterDraftId = encounterDraftController.insertEmptyEncounterDraft();
                encounterDraftController.deleteOldestDraft();
                showRestoreDraftSnackbar();
                break;
        }

        setupPager();
    }

    private void showRestoreDraftSnackbar() {
        Snackbar.make(findViewById(R.id.manage_layout), "Restore previous data", Snackbar.LENGTH_LONG)
                .setAction("Restore", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        encounterDraftController.deleteDraftById(encounterDraftId);
                        if (encounterExists()) {
                            encounterDraftId = encounterDraftController.getOldestDraftIdByEncounterId(encounterId);
                        } else {
                            encounterDraftId = encounterDraftController.getOldestDraftId();
                        }
                        manageEncounterPagerAdapter.changeEncounterDraftId(encounterDraftId);
                    }
                }).show();
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
    public void onAddCreature(long creatureId, String creatureName) {
        encounterController.addCreatureToEncounterDraft(encounterDraftId, creatureId);

        Snackbar.make(findViewById(R.id.manage_layout), "Added " + creatureName + " successfully", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    private boolean encounterExists() {
        return encounterId != -1;
    }
}
