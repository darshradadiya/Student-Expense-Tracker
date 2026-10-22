package com.student.expensetracker.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Helper class for managing user preferences such as login state,
 * remember me credentials, and dark mode configuration.
 */
public class PreferenceHelper {

    private static final String PREF_NAME = "student_expense_tracker_prefs";
    private static final String KEY_IS_LOGGED_IN = "key_is_logged_in";
    private static final String KEY_REMEMBER_ME = "key_remember_me";
    private static final String KEY_SAVED_EMAIL = "key_saved_email";
    private static final String KEY_DARK_MODE = "key_dark_mode";
    private static final String KEY_FIRST_RUN = "key_first_run";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isLoggedIn(Context context) {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        getPrefs(context).edit().putBoolean(KEY_IS_LOGGED_IN, loggedIn).apply();
    }

    public static boolean isRememberMe(Context context) {
        return getPrefs(context).getBoolean(KEY_REMEMBER_ME, false);
    }

    public static void setRememberMe(Context context, boolean remember, String email) {
        getPrefs(context).edit()
                .putBoolean(KEY_REMEMBER_ME, remember)
                .putString(KEY_SAVED_EMAIL, remember ? email : "")
                .apply();
    }

    public static String getSavedEmail(Context context) {
        return getPrefs(context).getString(KEY_SAVED_EMAIL, "rahul.patel@abc.edu");
    }

    public static boolean isDarkMode(Context context) {
        return getPrefs(context).getBoolean(KEY_DARK_MODE, false);
    }

    public static void setDarkMode(Context context, boolean isDark) {
        getPrefs(context).edit().putBoolean(KEY_DARK_MODE, isDark).apply();
    }

    public static boolean isFirstRun(Context context) {
        boolean firstRun = getPrefs(context).getBoolean(KEY_FIRST_RUN, true);
        if (firstRun) {
            getPrefs(context).edit().putBoolean(KEY_FIRST_RUN, false).apply();
        }
        return firstRun;
    }

    public static void clearSession(Context context) {
        boolean remember = isRememberMe(context);
        String savedEmail = getSavedEmail(context);
        boolean darkMode = isDarkMode(context);

        getPrefs(context).edit()
                .clear()
                .putBoolean(KEY_REMEMBER_ME, remember)
                .putString(KEY_SAVED_EMAIL, savedEmail)
                .putBoolean(KEY_DARK_MODE, darkMode)
                .putBoolean(KEY_FIRST_RUN, false)
                .apply();
    }
}
