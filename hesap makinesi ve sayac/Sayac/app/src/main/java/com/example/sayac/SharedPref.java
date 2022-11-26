package com.example.sayac;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private static SharedPref sharedPref = new SharedPref();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String obje = "titreşim";

    public static SharedPref getInstance(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        }
        return sharedPref;
    }

    public void kaydet(boolean kontrol) {
        editor.putBoolean(obje, kontrol);
        editor.commit();
    }

    public boolean objeyiÇek() {
        return sharedPreferences.getBoolean(obje, false);
    }
}