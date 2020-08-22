package com.example.mobileduel;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MySharedPreferences {

    public interface KEYS {
        String TOP10_PLAYERS= "TOP10_PLAYERS";
    }

    private static MySharedPreferences instance;
    private SharedPreferences preferences;

    public static MySharedPreferences getInstance() {
        return instance;
    }

    private MySharedPreferences(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences("GAME", Context.MODE_PRIVATE);
    }

    public static MySharedPreferences initHelper(Context context) {
        if (instance == null) {
            instance = new MySharedPreferences(context);
        }
        return instance;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String def) {
        return preferences.getString(key, def);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }

    public void putPlayers(ArrayList<Player> players) {
        Gson gson = new Gson();
        String playersJson = gson.toJson(players);
        preferences.edit().putString(KEYS.TOP10_PLAYERS, playersJson).apply();
    }

    public ArrayList<Player> getPlayers() {
        Gson gson = new Gson();
        ArrayList<Player> players;
        players = gson.fromJson(preferences.getString(KEYS.TOP10_PLAYERS, null), new TypeToken<ArrayList<Player>>(){}.getType());
        if (players == null) {
            players = new ArrayList<>();
        }
        return players;
    }





}
