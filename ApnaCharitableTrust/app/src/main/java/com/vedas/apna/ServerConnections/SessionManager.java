package com.vedas.apna.ServerConnections;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import com.vedas.apna.Helper.ProgressDialog;
import com.vedas.apna.Home.View.HomeActivity;
import java.io.File;
import java.util.Objects;

public class SessionManager {
    // Shared Preferences
    final SharedPreferences pref;
    // Editor for Shared preferences
    final SharedPreferences.Editor editor;
    // Context
    final Context _context;
    // Shared pref mode
    final int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "Apna";
    //vmartApp
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_USERID = "mobile";
    // --Commented out by Inspection (2/25/2021 10:09 PM):public static final String IS_ONLINE = "false";

    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String userid) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        ///storing the mobile value
        editor.putString(KEY_USERID, userid);
        // commit changes
        editor.commit();
    }

    public void logoutUser() {
        editor.putBoolean(IS_LOGIN, false);
        editor.clear();
        editor.apply();
        SharedPreferences myPrefs = _context.getSharedPreferences("LoginPref",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.clear();
        editor.apply();
        @SuppressLint("SdCardPath") File sharedPreferenceFile = new File("/data/data/"+ _context.getPackageName()+ "/shared_prefs/");
        File[] listFiles = sharedPreferenceFile.listFiles();
        for (File file : Objects.requireNonNull(listFiles)) {
            file.delete();
        }

        Intent i = new Intent(_context, HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        _context.startActivity(i);
        ProgressDialog.getInstance().hideProgress();
        //PreferenceManager.getDefaultSharedPreferences(_context).edit().clear().apply();
    }

    /**
     * Quick checkky for login
     **/
    // Get Login State
    public boolean isLoggedIn() {
        String userid = pref.getString(SessionManager.KEY_USERID, "");
        Log.e("sessionMobile", " " + userid);
        return pref.getBoolean(IS_LOGIN, false);
    }
}