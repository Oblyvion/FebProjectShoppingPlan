package de.feb.projectshoppingplan;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ArrayListUtils {

    //TAG logcat
    private final static String TAG = "MyActivity";
    //Main list of Categories (contains all Categories and the corresponding ShopItem lists)
    private ArrayList<Category> categories = new ArrayList<>();

    public ArrayListUtils() {
    }

    /**
     * Convert json string to ArrayList<ShopItem>.
     * @param json String
     * @return ArrayList<ShopItem>
     */
    ArrayList<ShopItem> getListFromJson(String json){
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ShopItem>>() {}.getType();
        //Log.d(TAG, "getListFromJson: JSON HIER: " + json);
        ArrayList<ShopItem> shopis = gson.fromJson(json, type);
        return shopis;
    }

    /**
     * Saves the arrayList in shared preferences with name myPrefs.
     * @param list ArrayList<Category>
     * @param key String
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
     * @param key string which determines which SharedPreferences key will be loaded
     * @return ArrayList<Category>
     */
    ArrayList<Category> loadArrayList(String key) {
        //Log.d(TAG, "Categories vor .clear(): "+categories+"\n");
        //Delete Categories before adding everything from Shared Preferences
        categories.clear();
        //get Shared Preferences myPrefs
        SharedPreferences prefs = AppContext.getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        //create new Gson object
        Gson gson = new Gson();
        //get json string of Shared Preferences
        String json = prefs.getString(key, null);
        //specify type so that Gson knows which type Json should be converted to
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        //add the Json string converted to an ArrayList<Category> to the categories list
        categories = gson.fromJson(json, type);
        //Log.d(TAG, "DIE CATEGORY LIST IN LOAD: "+categories);
        return categories;
    }


}
