package de.feb.projectshoppingplan;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayListUtils {

//    private final static String TAG = "ArrayListUtils";

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
        SharedPreferences prefs = AppContext.getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
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
    ArrayList<Category> loadArrayList() {
        //Log.d(TAG, "list vor .clear(): "+list+"\n");
        //Delete list before adding everything from Shared Preferences
        list.clear();
        //get Shared Preferences myPrefs
        SharedPreferences prefs = AppContext.getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        //create new Gson object
        Gson gson = new Gson();
        //get json string of Shared Preferences
        String json = prefs.getString("categories_arraylist", null);    //TODO change String name for list x
        //specify type so that Gson knows which type Json should be converted to
        Type type = new TypeToken<ArrayList<Category>>() {
        }.getType();
        //add the Json string converted to an ArrayList<Category> to the list list
        list = gson.fromJson(json, type);
        //Log.d(TAG, "DIE CATEGORY LIST IN LOAD: "+list);
        return list;
    }
}
