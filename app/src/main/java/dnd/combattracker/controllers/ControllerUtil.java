package dnd.combattracker.controllers;


import android.net.Uri;

public class ControllerUtil {
    private ControllerUtil() {
        //prevent initialization
    }

    public static long getIdFromUri(Uri result) {
        return Long.parseLong(result.getLastPathSegment());
    }

}
