package tiru.initiative.fragments

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.*
import tiru.initiative.R
import tiru.initiative.adapters.EncounterAdapter
import tiru.initiative.presenters.EncounterListPresenter
import tiru.initiative.presenters.EncounterListView

class EncounterListFragment : Fragment(), EncounterListView {
    private lateinit var presenter : EncounterListPresenter
    private lateinit var encounterAdapter : EncounterAdapter

    override fun setPresenter(presenter: EncounterListPresenter) {
        this.presenter = presenter
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        encounterAdapter = EncounterAdapter(null)

        recyclerView.adapter = encounterAdapter
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun showEncounters(cursor: Cursor) {
        encounterAdapter.changeCursor(cursor)
    }
}
