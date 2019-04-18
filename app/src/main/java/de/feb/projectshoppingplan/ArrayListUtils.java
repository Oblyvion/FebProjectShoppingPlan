package de.feb.projectshoppingplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static de.feb.projectshoppingplan.AppContext.getContext;

public class ArrayListUtils {

    private final static String TAG = "ArrayListUtils";

    //List of something
    private ArrayList<Category> list = new ArrayList<>();

    //    private String nameCat;

    public ArrayListUtils() {
    }

//    public void setName(String name) {
//        this.nameCat = name;
//    }

    /**
     * Convert json string to ArrayList<ShopItem>.
     *
     * @param json String
     * @return ArrayList<ShopItem>
     */
    ArrayList<ShopItem> getListFromJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ShopItem>>() {
        }.getType();
        //Log.d(TAG, "getListFromJson: JSON HIER: " + json);
        return gson.fromJson(json, type);
    }

    /**
     * Saves the arrayList in shared preferences with name myPrefs.
     *
     * @param list ArrayList<Category>
     * @param key  String
     */
    public void saveArrayList(ArrayList<Category> list, String key) {
        SharedPreferences prefs = getContext().getSharedPreferences("myPrefs" + key, Context.MODE_PRIVATE);

        Log.d(TAG, "saveArrayList: PREFS === " + prefs);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    /**
     * Loads shared preferences with name myPrefs and gets Arraylist<Category>
     *
     * @return ArrayList<Category>
     */
    ArrayList<Category> loadArrayList(String key) {
        //Log.d(TAG, "list vor .clear(): "+list+"\n");

        Log.d(TAG, "loadArrayList: KEYYYYYYYYYYYYYYYYY = " + key);

        //Delete list before adding everything from Shared Preferences
        list.clear();
        //get Shared Preferences myPrefs
        SharedPreferences prefs = getContext().getSharedPreferences("myPrefs" + key, Context.MODE_PRIVATE);
        //create new Gson object
        Gson gson = new Gson();

        Log.d(TAG, "loadArrayList: PREFS ======= " + prefs);

        //get json string of Shared Preferences
        String json = prefs.getString(key, null);
        //specify type so that Gson knows which type Json should be converted to
        Type type = new TypeToken<ArrayList<Category>>() {
        }.getType();
        //add the Json string converted to an ArrayList<Category> to the list list
        list = gson.fromJson(json, type);
        //Log.d(TAG, "DIE CATEGORY LIST IN LOAD: "+list);

        Log.d(TAG, "loadArrayList: LIIIIIIIIIIIIIIIIIIIIST = " + list);

        return list;
    }
}
