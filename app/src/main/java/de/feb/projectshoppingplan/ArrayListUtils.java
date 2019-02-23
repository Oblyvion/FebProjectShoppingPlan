package de.feb.projectshoppingplan;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayListUtils {

    //TAG logcat
    private final static String TAG = "MyActivity";
    private Activity act;
    //Main Liste der Categories (enthält alle Categories und die dazu gehörigen ShopItem Listen)
    private ArrayList<Category> categories = new ArrayList<>();

    public ArrayListUtils(Activity activity) {
        this.act = activity;
    }

    //aus Json string wird wieder eine Arraylist<ShopItem>
    ArrayList<ShopItem> getListFromJson(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ShopItem>>() {
        }.getType();
        Log.d(TAG, "getListFromJson: JSON HIER: " + json);

        ArrayList<ShopItem> shopis = gson.fromJson(json, type);

        //Log.d(TAG, "HALLO HIER DIE SHOPITEM LIST IN GETLISTFROMJSON: "+gson.fromJson(json, type));
        //ArrayList<ShopItem> shopis = gson.fromJson(json, type);
        for (int i = 0; i < shopis.size(); i++) {
            Log.d(TAG, "die einzelnen namen der shop items: " + shopis.get(i).name);
        }
        return shopis;
    }

    public void saveArrayList(ArrayList<Category> list, String key) {
        SharedPreferences prefs = act.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    ArrayList<Category> loadArrayList(String key) {
        //Log.d(TAG, "Categories vor .clear(): "+categories+"\n");
        //Categories löschen bevor alles aus den Shared Preferences hinzugefügt wird
        categories.clear();
        //Shared Preferences abrufen
        SharedPreferences prefs = act.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        //Neues gson Objekt erzeugen
        Gson gson = new Gson();
        //Json string aus Shared Preferences abrufen
        String json = prefs.getString(key, null);
        //Type angeben damit Gson weiß in welchen Typ Json konvertiert werden soll
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        //der categories Liste den zu einer ArrayList<Category> konvertierten Json String hinzufügen
        categories = gson.fromJson(json, type);
        //Log.d(TAG, "HALLO HIER DIE CATEGORY LIST IN LOAD: "+categories);
        return categories;
    }


}
