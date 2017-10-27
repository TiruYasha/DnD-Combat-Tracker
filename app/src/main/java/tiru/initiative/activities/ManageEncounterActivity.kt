package tiru.initiative.activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_manage_encounter.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import tiru.initiative.R
import tiru.initiative.adapters.ManageEncounterPagerAdapter
import tiru.initiative.fragments.EncounterDetailFragment
import tiru.initiative.fragments.EncounterDetailFragmentListener
import tiru.initiative.presenters.EncounterDetailsPresenter
import tiru.initiative.services.EncounterService
import java.util.concurrent.Future

class ManageEncounterActivity : AppCompatActivity(), EncounterDetailFragmentListener {

    private lateinit var encounterDetailsPresenter: EncounterDetailsPresenter

    private lateinit var pagerAdapter: ManageEncounterPagerAdapter
    private lateinit var service: EncounterService
    private var encounterId: Long? = null
    private var encounterDraftId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        service = EncounterService(contentResolver)

        setContentView(R.layout.activity_manage_encounter)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        pagerAdapter = ManageEncounterPagerAdapter(supportFragmentManager, null)

        container.adapter = pagerAdapter

        tabs.setupWithViewPager(container)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        //TODO presenter with pageadapter
        val encounterDetailsFragment = pagerAdapter.getFragmentByPosition(0)

        if(encounterDetailsFragment is EncounterDetailFragment){
            encounterDetailsPresenter = EncounterDetailsPresenter(encounterDetailsFragment, loaderManager, LoaderProvider(this), encounterDraftId)
        }else{
            throw RuntimeException("$encounterDetailsFragment Should be of type EncounterDetailFragment")
        }

        setupDraft()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_manage_encounter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when (id) {
            R.id.action_add_encounter -> {
                service.saveEncounterFromDraft(encounterId, encounterDraftId)
                finish()
                return true
            }
            R.id.action_restore_draft -> {
                restoreDraft()
                return true
            }
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun setupDraft() = doAsync {
        encounterDraftId = service.setupDraft(encounterId)

        if(service.getDraftCountByEncounterId(encounterId) > 1){
            uiThread {
                showRestoreDraftMessage()

            }
        }
    }

    private fun showRestoreDraftMessage() {
        Snackbar.make(main_content, "Restore previous data", Snackbar.LENGTH_LONG)
                .setAction("Restore", {
                    restoreDraft()
                }).show()
    }

    private fun restoreDraft() = doAsync{
        val encounter = service.restoreDraft(encounterId)

    }

    override fun onEncounterNameChanged(name: String): Future<Unit> = doAsync {
        service.updateDraftName(encounterDraftId, name)
    }
}
