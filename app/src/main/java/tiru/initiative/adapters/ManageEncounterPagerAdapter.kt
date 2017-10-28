package tiru.initiative.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import tiru.initiative.fragments.EncounterDetailFragment


class ManageEncounterPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private val fragmentList = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment {
        return when(position){
            1 -> {
                val fragment: Fragment = EncounterDetailFragment.newInstance()
                fragmentList.add(fragment)
                return fragment
            }

            else -> {
                val fragment: Fragment = EncounterDetailFragment.newInstance()
                fragmentList.add(fragment)
                return fragment
            }
        }
    }

    override fun getCount(): Int {
        return 1
    }

    fun getFragmentByPosition(position: Int): Fragment{
        return fragmentList[position]
    }
}

