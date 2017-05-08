package com.example.android.notepad;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ZhouShiqiao on 2017/4/29 0029.
 */

public class Preferences {
    private Context context;
    private SharedPreferences sp;

    public Preferences(Context context, String name) {
        this.context = context;
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return sp.getString(key, null);
    }

    public int getInt(String key) {
        return sp.getInt(key, 0);
    }
}
