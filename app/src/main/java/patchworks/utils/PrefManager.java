package patchworks.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by u1421499 on 22/04/18.
 */

public class PrefManager {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "patchworks_prefs";

    public PrefManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void storeLoginSession() {
        //editor.putString("SessionToken", token);
        editor.putBoolean("IsLoggedIn", true);
        editor.commit();
    }

    public void clearLoginSession() {
        editor.putBoolean("IsLoggedIn", false);
        editor.commit();
    }
}
