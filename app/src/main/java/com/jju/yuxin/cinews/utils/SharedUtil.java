package com.jju.yuxin.cinews.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedUtil {
    private static SharedPreferences preferences;

    public static SharedPreferences getInstance(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences("tab_item",
                    context.MODE_PRIVATE);
        }
        return preferences;
    }

    public static Editor getEditor(Context context) {
        if (preferences == null) {
            getInstance(context);
        }
        return preferences.edit();
    }

}
