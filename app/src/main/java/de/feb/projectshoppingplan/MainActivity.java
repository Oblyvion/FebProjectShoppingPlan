package de.feb.projectshoppingplan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.thoughtbot.expandablerecyclerview.ExpandableListUtils;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    //TAG logcat
    final static String TAG = "MyActivity";
    //Deklaration Recyclerview
    RecyclerView recyclerView;


    private boolean isMultiSelect = false;
    //i created List of int type to store id of data, you can create custom class type data according to your need.
    private ArrayList<Integer> selectedIds = new ArrayList<>();


    // TODO  dummy_items anlegen: ZUGRIFF = categories.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
    //Categories
    final static String[] STANDARD_CATEGORIES = {"Obst & Gemüse", "Wurst & Milchprodukte",
            "Getreideprodukte", "Fleisch & Fisch", "Hygiene", "Fertiggerichte"};

    ExpandableRecyclerViewAdapter adapter;

    //Main Liste der Categories (enthält alle Categories und die dazu gehörigen ShopItem Listen)
    public ArrayList<Category> categories = new ArrayList<>();

//    @Override
//    protected void onResume() {
//        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        Log.d(TAG, "MainActivity: On Resume");
//        super.onResume();
//        if (prefs.getString("categories_arraylist", null) != null) {
//            loadArrayList("categories_arraylist");
//            Log.d(TAG, "SharedPreferences JSON categories: " + prefs.getString("categories_arraylist", null));
//            for (int i = 0; i < categories.size(); i++) {
//                ArrayList<ShopItem> shopItems;
//                //Log.d(TAG, "LOAD shop item lists: "+categories.get(i).getItems());
//                //shopItems = (ArrayList<ShopItem>) categories.get(i).getItems();
//                Gson gson = new Gson();
//                String json = gson.toJson(categories.get(i).getItems());
//                shopItems = getListFromJson(json);
//                categories.get(i).getItems().clear();
//                categories.get(i).getItems().addAll(shopItems);
//                for (int j = 0; j < shopItems.size(); j++) {
//                    //Log.d(TAG, "SharedPreferences einzelne items nach load shop item list: "+shopItems.get(j).name);
//                    shopItems.get(j).setActivity(this);
//                    shopItems.get(j).setIcon();
//                }
//            }
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d(TAG, "MainActivity: On Create");

//        delete();
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        if (prefs.getString("categories_arraylist", null) != null) {
            loadArrayList("categories_arraylist");
            Log.d(TAG, "SharedPreferences JSON categories: " + prefs.getString("categories_arraylist", null));
            for (int i = 0; i < categories.size(); i++) {
                ArrayList<ShopItem> shopItems;
                //Log.d(TAG, "LOAD shop item lists: "+categories.get(i).getItems());
                //shopItems = (ArrayList<ShopItem>) categories.get(i).getItems();
                Gson gson = new Gson();
                String json = gson.toJson(categories.get(i).getItems());
                shopItems = getListFromJson(json);
                categories.get(i).getItems().clear();
                categories.get(i).getItems().addAll(shopItems);
                for (int j = 0; j < shopItems.size(); j++) {
                    //Log.d(TAG, "SharedPreferences einzelne items nach load shop item list: "+shopItems.get(j).name);
                    shopItems.get(j).setActivity(this);
                    shopItems.get(j).setIcon();
                }
            }
        }

        if (categories.isEmpty()) {
            addStandardCats();
        }

        //Adapter wird deklariert und initialisiert
        //Kann erst hier gemacht werden, da in categories was drin sein muss
        adapter = new ExpandableRecyclerViewAdapter(categories);

//        if (adapter != null) {
//            adapter.onRestoreInstanceState(savedInstanceState);
//        } else adapter = new ExpandableRecyclerViewAdapter(categories);
//        adapter = new ExpandableRecyclerViewAdapter(categories);

        //recycler view finden
        recyclerView = findViewById(R.id.recyclerViewMain);

        //Soll man machen wenn man weiß das sich die recyclerview elemente nicht ändern (also bezogen auf größe immer gleich bleibend)
        recyclerView.setHasFixedSize(true);

        //Linearlayout dem recycleview mitgeben soll man so machen damits besser angezeigt wird
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // drag and drop
        ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                Log.d(TAG, "getMovementFlags: GRAP ITEM...");
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder fromViewHolder, @NonNull RecyclerView.ViewHolder toViewHolder) {

                Log.d(TAG, "onMove: itemViewFROM TYPE = " + fromViewHolder.getItemViewType());
                Log.d(TAG, "onMove: itemViewTO TYPE = " + toViewHolder.getItemViewType());

                //do not swap when item types are unequal
                if (fromViewHolder.getItemViewType() != toViewHolder.getItemViewType()) {
                    return false;
                }

                //TODO SPEICHER NEUE LISTE NACH DRAG & DROP
//                adapter.onSaveInstanceState(savedInstanceState);

                Log.d(TAG, "onMove: adapter POSITION FROM = " + fromViewHolder.getAdapterPosition());
                Log.d(TAG, "onMove: adapter POSITION TO = " + toViewHolder.getAdapterPosition());
                adapter.moveItem(fromViewHolder.getAdapterPosition(), toViewHolder.getAdapterPosition());

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Log.d(TAG, "HALLO HIER SWIPEY SWUPP SWIPE");
            }
        };

//        SwipeController swipeController = new SwipeController();
//        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
//        itemTouchhelper.attachToRecyclerView(recyclerView);

        datachanged();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Log.d(TAG, "das ist das menu item: "+menuItem);

                if (!isMultiSelect){
                    selectedIds = new ArrayList<>();
                    isMultiSelect = true;
                }


                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Remove all items from each category?");
                View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);

                // button setup
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        //lässt Kategorie-Erweiterungspfeile verschwinden
                        showCategoryNotExpandable();

                        clearAllCategories();

                        //Save der Liste nachdem alle categories gecleared wurden
                        saveArrayList(categories, "categories_arraylist");


                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();


                return false;
            }
        });


        // recyclerView wird itemTouchHelper hinzugefügt
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //erster wichtiger save der liste
        saveArrayList(categories, "categories_arraylist");

        //Adapter auf den recyclerview setzen
        recyclerView.setAdapter(adapter);

        datachanged();

        Log.d(TAG, "Das ist die Größe der Category Liste: " + categories.size());

        //Floating button und Alert Dialog für Category Adding
        FloatingActionButton floatingBttn_add = findViewById(R.id.floatingBttn_add);
        floatingBttn_add.setSize(50);
        floatingBttn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Das ist die Liste bei click auf floating button: " + categories.toString());

                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add a new category");
                View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_dialog, (ViewGroup) findViewById(android.R.id.content), false);
                // input setup
                final EditText input = viewInflated.findViewById(R.id.input);
                builder.setView(viewInflated);

                // button setup
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //item liste für neue cat erstellen
                        ArrayList<ShopItem> list = new ArrayList<>();

                        //newCat erstellen mit Name und Liste
                        //Category newCat = new Category(input.getText().toString() , list);

                        String temp = input.getText().toString();
                        int help = 0;
                        for (int i = 0; i < categories.size(); i++) {
                            if (categories.get(i).getTitle().equals(temp)) {
                                while (categories.get(i).getTitle().equals(temp)) {
                                    help++;
                                    temp = input.getText().toString()+"("+help+")";
                                }
                            }
                        }

                        //newCat erstellen mit Name und Liste
                        Category newCat = new Category(temp, list);

                        //Zur Liste der categories hinzufügen
                        categories.add(newCat);

                        //lässt Kategorie-Erweiterungspfeile verschwinden
                        showCategoryNotExpandable();

                        //dafür sorgen das der adapter die neue category auch anzeigt
                        adapter.addNewGroup();

                        //Save der Liste nachdem eine neue Cat hinzugefügt wurde
                        saveArrayList(categories, "categories_arraylist");

                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    private void clearAllCategories() {
        for (int i = 0; i < categories.size(); i++) {
            categories.get(i).getItems().clear();
        }
        adapter.notifyDataSetChanged();
    }

    //TODO WO MÜSSEN DIE METHODEN AUFGERUFEN WERDEN?
    //speichere den status vom expandable recyclerview
    @Override
    protected void onRestoreInstanceState(Bundle restoreState) {
        super.onRestoreInstanceState(restoreState);
        adapter.onRestoreInstanceState(restoreState);
    }


    private void addStandardCats() {
        ArrayList<ShopItem> veggie_list = new ArrayList<>();
        Category veggie = new Category(STANDARD_CATEGORIES[0], veggie_list);

        veggie_list.add(new ShopItem("banana"));
        veggie_list.add(new ShopItem("apple"));
        veggie_list.add(new ShopItem("cucumber"));
        veggie_list.add(new ShopItem("apricots"));
        veggie_list.add(new ShopItem("salad"));

        for (int i = 0; i < veggie_list.size(); i++) {
            veggie_list.get(i).setActivity(this);
            veggie_list.get(i).setIcon();
        }

        categories.add(veggie);


        ArrayList<ShopItem> hygienics_list = new ArrayList<>();
        Category hygienics = new Category(STANDARD_CATEGORIES[4], hygienics_list);

        hygienics_list.add(new ShopItem("deonummer1"));
        hygienics_list.add(new ShopItem("toothbrush"));
        hygienics_list.add(new ShopItem("shampoo"));
        hygienics_list.add(new ShopItem("perfume"));
        hygienics_list.add(new ShopItem("soap"));

        for (int i = 0; i < hygienics_list.size(); i++) {
            hygienics_list.get(i).setActivity(this);
            hygienics_list.get(i).setIcon();
        }

        categories.add(hygienics);
    }

    //TODO TODO
//    private void multiSelect(int position) {
//        ShopItem item = categories.get(position);
//        if (item != null){
//            if (actionMode != null) {
//                if (selectedIds.contains(item.))
//                    selectedIds.remove(Integer.valueOf(item.getId()));
//                else
//                    selectedIds.add(item.getId());
//
//                if (selectedIds.size() > 0)
//                    actionMode.setTitle(String.valueOf(selectedIds.size())); //show selected item count on action mode.
//                else{
//                    actionMode.setTitle(""); //remove item count from action mode.
//                    actionMode.finish(); //hide action mode.
//                }
//                adapter.setSelectedIds(selectedIds);
//
//            }
//        }
//    }

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
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
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
        SharedPreferences prefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        //Neues gson Objekt erzeugen
        Gson gson = new Gson();
        //Json string aus Shared Preferences abrufen
        String json = prefs.getString(key, null);
        //Type angeben damit Gson weiß in welchen Typ Json konvertiert werden soll
        Type type = new TypeToken<ArrayList<Category>>() {}.getType();
        //der categories Liste den zu einer ArrayList<Category> konvertierten Json String hinzufügen
        categories = gson.fromJson(json, type);
        //Log.d(TAG, "HALLO HIER DIE CATEGORY LIST IN LOAD: "+categories);
    }

    public void delete() {
        this.getSharedPreferences("myPrefs", 0).edit().clear().apply();
    }

    public void datachanged() {
        recyclerView.getRecycledViewPool().clear();
        Log.d(TAG, "datachanged: adapter = " + adapter);
        adapter.notifyDataSetChanged();
    }

    //wenn category Grösse < 1, dann blende Kategorie-Erweiterungspfeile aus
    private void showCategoryNotExpandable() {
        for (Category cat : categories) {
            ImageView imageViewCatArrow = findViewById(R.id.imageViewCategory);
            if (cat.getItems().size() < 1) {
                imageViewCatArrow.setVisibility(View.INVISIBLE);
            }
        }
    }
}
