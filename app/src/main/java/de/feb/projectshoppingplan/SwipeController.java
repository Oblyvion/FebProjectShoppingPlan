package de.feb.projectshoppingplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.LoginFilter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.support.v7.widget.helper.ItemTouchHelper.*;

public class SwipeController extends Callback {

    //TAG logcat
    final static String TAG = "MyActivity";

    private RecyclerView.ViewHolder viewHolder;
    private RecyclerView recyclerView;
    private ExpandableRecyclerViewAdapter adapter;

    private ArrayList<Category> categories = new ArrayList<>();

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            this.recyclerView = recyclerView;
            this.adapter = (ExpandableRecyclerViewAdapter) recyclerView.getAdapter();
            return makeMovementFlags(0, LEFT | RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//TODO richtige position bekommen, löschen, categories saven, adapter notify
            //            this.viewHolder = viewHolder;
//            loadArrayList("categories_arraylist");
//
//            Log.d(TAG, "Das ist recyclerview: " + (recyclerView.getAdapter().getItemCount()));
//            Log.d(TAG, "Das ist recyclerview item position: "+viewHolder.getAdapterPosition());
//
//        //recyclerView.getAdapter().notifyDataSetChanged();
//
//            int realPosition = 0;
//            int catPos = -1;
//            for (int i = 0; i < viewHolder.getAdapterPosition();i++) {
////                if (adapter.getItemViewType(i) == 2 && adapter.isGroupExpanded(i)) {
////                    catPos += 1;
////                    realPosition += adapter.getCategory(catPos).getItemCount();
////                }
//                realPosition += 1;
//            }
//            Log.d(TAG, "RealPosition: "+ realPosition);


            //            this.viewHolder = viewHolder;
//            Log.d(TAG, "Das ist viewholder getitemid: "+viewHolder.getItemViewType());
//            loadArrayList("categories_arraylist");
//
//// BRingt leider nix denn es kann auch eine kategorie aufgeklappt sein und gelöscht werden
//            if (viewHolder.getItemViewType() == 2 && viewHolder) {
//                categories.remove(viewHolder.getAdapterPosition()-1);
//                saveArrayList(categories, "categories_arraylist");
//            }

        }
    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float alpha = 1 - (Math.abs(dX) / recyclerView.getWidth());
            viewHolder.itemView.setAlpha(alpha);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    //aus Json string wird wieder eine Arraylist<ShopItem>
    public ArrayList<ShopItem> getListFromJson(String json){
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
        SharedPreferences prefs = viewHolder.itemView.getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public void loadArrayList(String key) {
        //Log.d(TAG, "Categories vor .clear(): "+categories+"\n");
        //Categories löschen bevor alles aus den Shared Preferences hinzugefügt wird
        categories.clear();
        //Shared Preferences abrufen
        SharedPreferences prefs = viewHolder.itemView.getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        //Neues gson Objekt erzeugen
        Gson gson = new Gson();
        //Json string aus Shared Preferences abrufen
        String json = prefs.getString(key, null);
        //Type angeben damit Gson weiß in welchen Typ Json konvertiert werden soll
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        //der categories Liste den zu einer ArrayList<Category> konvertierten Json String hinzufügen
        categories = gson.fromJson(json, type);
        //Log.d(TAG, "HALLO HIER DIE CATEGORY LIST IN LOAD: "+categories);
    }}
