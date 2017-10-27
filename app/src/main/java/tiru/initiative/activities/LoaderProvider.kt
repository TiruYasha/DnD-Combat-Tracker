package tiru.initiative.activities

import android.content.Context
import android.content.CursorLoader
import android.net.Uri

class LoaderProvider(private val context: Context) {

    fun createCursorLoader(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): CursorLoader {
        return CursorLoader(context, uri, projection, selection, selectionArgs, sortOrder)
    }
}