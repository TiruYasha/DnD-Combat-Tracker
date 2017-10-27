package tiru.initiative.presenters

import android.app.LoaderManager
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import tiru.initiative.activities.LoaderProvider
import tiru.initiative.activities.MainActivity
import tiru.initiative.providers.EncounterProvider
import tiru.initiative.repository.InitiativeTrackerContract

class EncounterListPresenter(private val view: EncounterListView,
                             private val loaderManager: LoaderManager,
                             private val loaderProvider: LoaderProvider) : BasePresenter, LoaderManager.LoaderCallbacks<Cursor> {

    init {
        view.setPresenter(this)
    }

    override fun start() {
        loaderManager.restartLoader(0, null, this)
    }

    override fun onLoaderReset(p0: Loader<Cursor>?) {
    }

    override fun onLoadFinished(loader: Loader<Cursor>?, cursor: Cursor?) {
        view.showEncounters(cursor!!)
    }

    override fun onCreateLoader(p0: Int, p1: Bundle?): Loader<Cursor> {
        return loaderProvider.createCursorLoader(EncounterProvider.CONTENT_URI, arrayOf(InitiativeTrackerContract.EncounterEntry.NAME), null, null, null)
    }
}

interface EncounterListView : BaseView<EncounterListPresenter> {
    fun showEncounters(cursor: Cursor)
}