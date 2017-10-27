package tiru.initiative.repository

import android.content.Context
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper

class InitiativeTrackerDbHelper(context: Context) : SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "InitiativeTracker.db"
    }
}